/* 
 * Copyright (C) 2016 BIP-M Framework.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package dump;

import analyzer.states.ActiveConnectionWin732;
import analyzer.states.ActiveProcessWin732;
import analyzer.states.ActiveSocketWin732;
import analyzer.states.HiddenProcessWin732;
import analyzer.states.SeekerState;
import dump.factories.CrashDMPWin732Factory;
import dump.formats.CrashDump32;
import dump.formats.HeaderCrashDump32;
import dump.ooss.Windows732OS;
import dump.parsers.Parser;
import dump.parsers.connections.ParserWin732ConnectionCrashDMP;
import dump.parsers.connections.ParserWin732SocketCrashDMP;
import dump.parsers.lib.ParserLibCrashDMPWin732;
import dump.parsers.process.ParserWin732ActiveProcessCrashDMP;
import dump.parsers.process.ParserWin732HiddenProcessCrashDMP;
import dump.parsers.process.ParserWin732ThreadCrashDMP;
import entities.Entity;
import entities.connection.EntityWindows732Socket;
import entities.connection.EntityWindows732TCPConnection;
import entities.lib.EntityLibWindows732DLL;
import entities.process.EntityWindows732Process;
import entities.process.EntityWindows732Thread;
import output.OutputManagerDump;
import output.OutputManagerException;
import system.utils.Conversor;
import translation.TranslatorWin32;

/**
 *
 * @author Gonzalo
 */
public class DumpManagerWin732CD extends DumpManager {

    private DumpManagerWin732CD() {
        super();

        this.addObserver(OutputManagerException.getInstance());
        this.addObserver(OutputManagerDump.getInstance());
        try {
            CrashDMPWin732Factory factory = new CrashDMPWin732Factory();
            this._dumpFactory = factory;
            //Warning: no path. Set path later.
            this._os = this.getDumpFactory().createOperatingSystemStructure();
            this._translator = new TranslatorWin32((Windows732OS) this._os, this._dumpFormat);
        } catch (Exception ex) {
            this.notifyObservers(ex);
        }
    }

    private DumpManagerWin732CD(String _path, boolean _littleEndian) {
        super(_path, null, _littleEndian);

        try {
            CrashDMPWin732Factory factory = new CrashDMPWin732Factory();
            this._dumpFactory = factory;
            this._dumpFormat = this._dumpFactory.createDumpFormat(_path, _littleEndian);
            this._dumpFormat.setLittleEndian(_littleEndian);
            this._os = this._dumpFactory.createOperatingSystemStructure();
            this._translator = new TranslatorWin32((Windows732OS) this._os, this._dumpFormat);
        } catch (ExceptionInInitializerError ex) {
            ex.printStackTrace();
            this.notifyObservers(ex);
        }
    }

    public static DumpManagerWin732CD getInstance() {
        if (_instance == null || !(_instance instanceof DumpManagerWin732CD)) {
            _instance = new DumpManagerWin732CD();
        }
        return (DumpManagerWin732CD) _instance;
    }

    /**
     * @param path Ubicación del archivo de volcado de memoria
     * @return Instancia DumpManagerWin732CD.
     */
    public static DumpManagerWin732CD getInstance(String path, boolean littleEndian) {
        if (_instance == null || !(_instance instanceof DumpManagerWin732CD)) {
            _instance = new DumpManagerWin732CD(path, littleEndian);
        }
        return (DumpManagerWin732CD) _instance;
    }

    @Override
    public synchronized int countObservers() {
        return super.countObservers(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized boolean hasChanged() {
        return super.hasChanged(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected synchronized void clearChanged() {
        super.clearChanged(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected synchronized void setChanged() {
        super.setChanged(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized void deleteObservers() {
        super.deleteObservers(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void notifyObservers(Object o) {
        super.notifyObservers(o); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void notifyObservers() {
        super.notifyObservers(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void obtainDumpFormatData() {
        this._dumpFormat.getDumpFormatContent();

        /**
         * Persiste información relacionada con el tipo de archivo de volcado de
         * memoria.
         */
        this._persistence.persistDumpFormatInformation(this._dumpFormat);

        /**
         * Configura traductor de direcciones virtuales
         */
        configTranslator();
    }

    @Override
    public void configTranslator() {
        try {
            if (this._dumpFormat != null) {
                CrashDump32 crashDump = (CrashDump32) this._dumpFormat;
                HeaderCrashDump32 header = crashDump.getHeader();
                if (header != null) {
                    /**
                     * Obtengo valor de DTB
                     */
                    String itemContent = (String) header.getItems().get("DIRECTORY_TABLE_BASE").getContent();
                    if (itemContent != null) {
                        TranslatorWin32 translator = (TranslatorWin32) this._translator;
                        translator.setDTB(Conversor.hexToLong(itemContent));
                        this._translator = translator;
                    }

                    itemContent = (String) header.getItems().get("PAE_ENABLED").getContent();
                    if (itemContent != null) {
                        char[] c = itemContent.toCharArray();
                        int pae = c[0];
                        TranslatorWin32 translator = (TranslatorWin32) this._translator;
                        if (pae == 1) {
                            translator.setPAEenabled(true);
                        } else {
                            translator.setPAEenabled(false);
                        }
                        this._translator = translator;
                    }
                }
            }
        } catch (Exception ex) {
            this.notifyObservers(ex);
        }
    }

    @Override
    public void initializeParsers() {
        try {
            this._os.getEntityList().get(EntityWindows732Process._TAG).setParser(new ParserWin732ActiveProcessCrashDMP((CrashDump32) this._dumpFormat, (Windows732OS) this._os, (TranslatorWin32) this._translator));
            this._os.getEntityList().get(EntityWindows732Thread._TAG).setParser(new ParserWin732ThreadCrashDMP((CrashDump32) this._dumpFormat, (Windows732OS) this._os, (TranslatorWin32) this._translator));
            this._os.getEntityList().get(EntityWindows732Socket._TAG).setParser(new ParserWin732SocketCrashDMP((CrashDump32) this._dumpFormat, (Windows732OS) this._os, (TranslatorWin32) this._translator));
            this._os.getEntityList().get(EntityWindows732TCPConnection._TAG).setParser(new ParserWin732ConnectionCrashDMP((CrashDump32) this._dumpFormat, (Windows732OS) this._os, (TranslatorWin32) this._translator));
            //this._os.getEntityList().get(EntityWindows732UDPConnection._TAG).setParser(new ParserWin732UDPConnectionCrashDMP((CrashDump32) this._dumpFormat, (Windows732OS) this._os, (TranslatorWin32) this._translator));
            this._os.getEntityList().get(EntityLibWindows732DLL._TAG).setParser(new ParserLibCrashDMPWin732((CrashDump32) this._dumpFormat, (Windows732OS) this._os, (TranslatorWin32) this._translator));

        } catch (ExceptionInInitializerError ex) {
            ex.printStackTrace();
            this.notifyObservers(ex);
        }
    }

    @Override
    public void setParserByState(SeekerState state) {
        try {
            switch (state.getStateLabel()) {
                case "Active":
                    if (state instanceof ActiveProcessWin732) {
                        //this._os.getEntityList().get(state.getEntityTag()).setParser(new ParserWin732ActiveProcessCrashDMP((CrashDump32) this._dumpFormat, (Windows732OS) this._os, (TranslatorWin32) this._translator));
                        this._os.setParserByTag(state.getEntityTag(), new ParserWin732ActiveProcessCrashDMP((CrashDump32) this._dumpFormat, (Windows732OS) this._os, (TranslatorWin32) this._translator));
                    }
                    if (state instanceof ActiveSocketWin732) {
                        //this._os.getEntityList().get(state.getEntityTag()).setParser(new ParserWin732SocketCrashDMP((CrashDump32) this._dumpFormat, (Windows732OS) this._os, (TranslatorWin32) this._translator));
                        this._os.setParserByTag(state.getEntityTag(), new ParserWin732SocketCrashDMP((CrashDump32) this._dumpFormat, (Windows732OS) this._os, (TranslatorWin32) this._translator));
                    }
                    if (state instanceof ActiveConnectionWin732) {
                        //this._os.getEntityList().get(state.getTag()).setParser(new ParserWin732TCPConnectionCrashDMP((CrashDump32) this._dumpFormat, (Windows732OS) this._os, (TranslatorWin32) this._translator));
                        this._os.setParserByTag(state.getEntityTag(), new ParserWin732ConnectionCrashDMP((CrashDump32) this._dumpFormat, (Windows732OS) this._os, (TranslatorWin32) this._translator));
                    }
                    break;
                case "Hidden":
                    if (state instanceof HiddenProcessWin732) {
                        //this._os.getEntityList().get(state.getEntityTag()).setParser(new ParserWin732HiddenProcessCrashDMP((CrashDump32) this._dumpFormat, (Windows732OS) this._os, (TranslatorWin32) this._translator));
                        this._os.setParserByTag(state.getEntityTag(), new ParserWin732HiddenProcessCrashDMP((CrashDump32) this._dumpFormat, (Windows732OS) this._os, (TranslatorWin32) this._translator));
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.notifyObservers(ex);
        }
    }

    @Override
    public void setParserByEntity(Entity entity) {
        switch (entity.getTag()) {
            case "Process":
                this._os.getEntityList().get(entity.getTag()).setParser(new ParserWin732ActiveProcessCrashDMP((CrashDump32) this._dumpFormat, (Windows732OS) this._os, (TranslatorWin32) this._translator));
                break;
            case "Thread":
                this._os.getEntityList().get(entity.getTag()).setParser(new ParserWin732ThreadCrashDMP((CrashDump32) this._dumpFormat, (Windows732OS) this._os, (TranslatorWin32) this._translator));
                break;
            case "Connection":
                this._os.getEntityList().get(entity.getTag()).setParser(new ParserWin732ConnectionCrashDMP((CrashDump32) this._dumpFormat, (Windows732OS) this._os, (TranslatorWin32) this._translator));
                break;
            case "Library":
                this._os.getEntityList().get(entity.getTag()).setParser(new ParserLibCrashDMPWin732((CrashDump32) this._dumpFormat, (Windows732OS) this._os, (TranslatorWin32) this._translator));
                break;
            case "SSDT":
                this._os.getEntityList().get(entity.getTag()).setParser(new ParserLibCrashDMPWin732((CrashDump32) this._dumpFormat, (Windows732OS) this._os, (TranslatorWin32) this._translator));
                break;
            default:
                break;
        }
    }

    @Override
    public void setParser(Entity entity, Parser parser) {
        this._os.getEntityList().get(entity.getTag()).setParser(parser);
    }

}

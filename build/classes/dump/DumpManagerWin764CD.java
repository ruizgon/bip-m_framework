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

import analyzer.states.ActiveConnectionWin764;
import analyzer.states.ActiveProcessWin764;
import analyzer.states.ActiveSocketWin764;
import analyzer.states.HiddenProcessWin764;
import analyzer.states.SeekerState;
import dump.factories.CrashDMPWin764Factory;
import dump.formats.CrashDump64;
import dump.formats.HeaderCrashDump64;
import dump.ooss.Windows764OS;
import dump.parsers.Parser;
import dump.parsers.connections.ParserWin764ConnectionCrashDMP;
import dump.parsers.connections.ParserWin764SocketCrashDMP;
import dump.parsers.lib.ParserLibCrashDMPWin764;
import dump.parsers.process.ParserWin764ActiveProcessCrashDMP;
import dump.parsers.process.ParserWin764HiddenProcessCrashDMP;
import dump.parsers.process.ParserWin764ThreadCrashDMP;
import entities.Entity;
import entities.connection.EntityWindows764Socket;
import entities.connection.EntityWindows764TCPConnection;
import entities.lib.EntityLibWindows764DLL;
import entities.process.EntityWindows764Process;
import entities.process.EntityWindows764Thread;
import system.utils.Conversor;
import translation.TranslatorWin64;

/**
 *
 * @author Gonzalo
 */
public class DumpManagerWin764CD extends DumpManager {

    public DumpManagerWin764CD() {
        super();
        try {
            CrashDMPWin764Factory factory = new CrashDMPWin764Factory();
            this._dumpFactory = factory;
            //Warning: no path. Set path later.
            this._os = this.getDumpFactory().createOperatingSystemStructure();
            this._translator = new TranslatorWin64((Windows764OS) this._os, this._dumpFormat);
        } catch (Exception ex) {
            ex.printStackTrace();
            this.notifyObservers(ex);
        }
    }

    public DumpManagerWin764CD(String _path, boolean _littleEndian) {
        super(_path, null, _littleEndian);

        try {
            CrashDMPWin764Factory factory = new CrashDMPWin764Factory();
            this._dumpFactory = factory;
            this._dumpFormat = this._dumpFactory.createDumpFormat(_path, _littleEndian);
            this._dumpFormat.setLittleEndian(_littleEndian);
            this._os = this._dumpFactory.createOperatingSystemStructure();
            this._translator = new TranslatorWin64((Windows764OS) this._os, this._dumpFormat);
        } catch (ExceptionInInitializerError ex) {
            ex.printStackTrace();
            this.notifyObservers(ex);
        }
    }

    public static DumpManagerWin764CD getInstance() {
        if (_instance == null || !(_instance instanceof DumpManagerWin764CD)) {
            _instance = new DumpManagerWin764CD();
        }
        return (DumpManagerWin764CD) _instance;
    }

    /**
     * @param path Ubicación del archivo de volcado de memoria
     * @return Instancia DumpManagerWin764CD.
     */
    public static DumpManagerWin764CD getInstance(String path, boolean littleEndian) {
        if (_instance == null || !(_instance instanceof DumpManagerWin764CD)) {
            _instance = new DumpManagerWin764CD(path, littleEndian);
        }
        return (DumpManagerWin764CD) _instance;
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
    public void configTranslator() {
        try {
            if (this._dumpFormat != null) {
                CrashDump64 crashDump = (CrashDump64) this._dumpFormat;
                HeaderCrashDump64 header = crashDump.getHeader();
                if (header != null) {
                    /**
                     * Obtengo valor de DTB
                     */
                    String itemContent = (String) header.getItems().get("DIRECTORY_TABLE_BASE").getContent();
                    if (itemContent != null) {
                        TranslatorWin64 translator = (TranslatorWin64) this._translator;
                        translator.setDTB(Conversor.hexToLong(itemContent));
                        this._translator = translator;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.notifyObservers(ex);
        }
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
    public void initializeParsers() {
        try {
            this._os.getEntityList().get(EntityWindows764Process._TAG).setParser(new ParserWin764ActiveProcessCrashDMP((CrashDump64) this._dumpFormat, (Windows764OS) this._os, (TranslatorWin64) this._translator));
            this._os.getEntityList().get(EntityWindows764Thread._TAG).setParser(new ParserWin764ThreadCrashDMP((CrashDump64) this._dumpFormat, (Windows764OS) this._os, (TranslatorWin64) this._translator));
            this._os.getEntityList().get(EntityWindows764Socket._TAG).setParser(new ParserWin764SocketCrashDMP((CrashDump64) this._dumpFormat, (Windows764OS) this._os, (TranslatorWin64) this._translator));
            this._os.getEntityList().get(EntityWindows764TCPConnection._TAG).setParser(new ParserWin764ConnectionCrashDMP((CrashDump64) this._dumpFormat, (Windows764OS) this._os, (TranslatorWin64) this._translator));
            this._os.getEntityList().get(EntityLibWindows764DLL._TAG).setParser(new ParserLibCrashDMPWin764((CrashDump64) this._dumpFormat, (Windows764OS) this._os, (TranslatorWin64) this._translator));

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
                    if (state instanceof ActiveProcessWin764) {
                        //this._os.getEntityList().get(state.getEntityTag()).setParser(new ParserWin732ActiveProcessCrashDMP((CrashDump32) this._dumpFormat, (Windows732OS) this._os, (TranslatorWin32) this._translator));
                        this._os.setParserByTag(state.getEntityTag(), new ParserWin764ActiveProcessCrashDMP((CrashDump64) this._dumpFormat, (Windows764OS) this._os, (TranslatorWin64) this._translator));
                    }
                    if (state instanceof ActiveSocketWin764) {
                        //this._os.getEntityList().get(state.getEntityTag()).setParser(new ParserWin732SocketCrashDMP((CrashDump32) this._dumpFormat, (Windows732OS) this._os, (TranslatorWin32) this._translator));
                        this._os.setParserByTag(state.getEntityTag(), new ParserWin764SocketCrashDMP((CrashDump64) this._dumpFormat, (Windows764OS) this._os, (TranslatorWin64) this._translator));
                    }
                    if (state instanceof ActiveConnectionWin764) {
                        //this._os.getEntityList().get(state.getTag()).setParser(new ParserWin732TCPConnectionCrashDMP((CrashDump32) this._dumpFormat, (Windows732OS) this._os, (TranslatorWin32) this._translator));
                        this._os.setParserByTag(state.getEntityTag(), new ParserWin764ConnectionCrashDMP((CrashDump64) this._dumpFormat, (Windows764OS) this._os, (TranslatorWin64) this._translator));
                    }
                    break;
                case "Hidden":
                    if (state instanceof HiddenProcessWin764) {
                        //this._os.getEntityList().get(state.getEntityTag()).setParser(new ParserWin732HiddenProcessCrashDMP((CrashDump32) this._dumpFormat, (Windows732OS) this._os, (TranslatorWin32) this._translator));
                        this._os.setParserByTag(state.getEntityTag(), new ParserWin764HiddenProcessCrashDMP((CrashDump64) this._dumpFormat, (Windows764OS) this._os, (TranslatorWin64) this._translator));
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
                this._os.getEntityList().get(entity.getTag()).setParser(new ParserWin764ActiveProcessCrashDMP((CrashDump64) this._dumpFormat, (Windows764OS) this._os, (TranslatorWin64) this._translator));
                break;
            case "Thread":
                this._os.getEntityList().get(entity.getTag()).setParser(new ParserWin764ThreadCrashDMP((CrashDump64) this._dumpFormat, (Windows764OS) this._os, (TranslatorWin64) this._translator));
                break;
            case "Connection":
                this._os.getEntityList().get(entity.getTag()).setParser(new ParserWin764ConnectionCrashDMP((CrashDump64) this._dumpFormat, (Windows764OS) this._os, (TranslatorWin64) this._translator));
                break;
            case "Library":
                this._os.getEntityList().get(entity.getTag()).setParser(new ParserLibCrashDMPWin764((CrashDump64) this._dumpFormat, (Windows764OS) this._os, (TranslatorWin64) this._translator));
                break;
            case "SSDT":
                this._os.getEntityList().get(entity.getTag()).setParser(new ParserLibCrashDMPWin764((CrashDump64) this._dumpFormat, (Windows764OS) this._os, (TranslatorWin64) this._translator));
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

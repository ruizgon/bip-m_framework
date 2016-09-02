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

import analyzer.states.ActiveProcessWin764;
import analyzer.states.SeekerState;
import dump.factories.RawDMPWin764Factory;
import dump.formats.RawDumpWin7;
import dump.ooss.Windows764OS;
import dump.parsers.Parser;
import dump.parsers.ParserKDBGRawDMPWin764;
import dump.parsers.process.ParserWin764ActiveProcessRawDMP;
import entities.Entity;
import entities.EntityKDBG;
import translation.TranslatorWin64;

/**
 *
 * @author Gonzalo
 */
public class DumpManagerWin764RD extends DumpManagerRD {

    public DumpManagerWin764RD() {
        super();
        try {
            RawDMPWin764Factory factory = new RawDMPWin764Factory();
            this._dumpFactory = factory;
            //Warning: no path. Set path later.
            this._os = this.getDumpFactory().createOperatingSystemStructure();
            this._translator = new TranslatorWin64((Windows764OS) this._os, this._dumpFormat);
        } catch (Exception ex) {
            ex.printStackTrace();
            this.notifyObservers(ex);
        }
    }

    public DumpManagerWin764RD(String _path, boolean _littleEndian) {
        super(_path, null, _littleEndian);

        try {
            RawDMPWin764Factory factory = new RawDMPWin764Factory();
            this._dumpFactory = factory;

            this._os = this._dumpFactory.createOperatingSystemStructure();
            this._translator = new TranslatorWin64((Windows764OS) this._os, this._dumpFormat);

            RawDumpWin7 dumpFormat = (RawDumpWin7) this._dumpFactory.createDumpFormat(_path, _littleEndian);

            /**
             * Instancia Parser necesario para obtener EntityKDBG
             */
            dumpFormat.setParserKDBG(new ParserKDBGRawDMPWin764(dumpFormat, this._os, this._translator));
            this._dumpFormat = dumpFormat;
            this._dumpFormat.setLittleEndian(_littleEndian);
        } catch (ExceptionInInitializerError ex) {
            ex.printStackTrace();
            this.notifyObservers(ex);
        }
    }

    public static DumpManagerWin764RD getInstance() {
        if (_instance == null || !(_instance instanceof DumpManagerWin764CD)) {
            _instance = new DumpManagerWin764CD();
        }
        return (DumpManagerWin764RD) _instance;
    }

    /**
     * @param path Ubicación del archivo de volcado de memoria
     * @return Instancia DumpManagerWin764CD.
     */
    public static DumpManagerWin764RD getInstance(String path, boolean littleEndian) {
        if (_instance == null || !(_instance instanceof DumpManagerWin764RD)) {
            _instance = new DumpManagerWin764RD(path, littleEndian);
        }
        return (DumpManagerWin764RD) _instance;
    }

    @Override
    public EntityKDBG getKDBG() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long getDTB() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        TranslatorWin64 translator = (TranslatorWin64) this._translator;
        RawDumpWin7 dumpFormat = (RawDumpWin7) this._dumpFormat;
        translator.setDTB(dumpFormat.getEntityKDBG().getDTB());
        this._translator = translator;
    }

    @Override
    public void initializeParsers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setParserByState(SeekerState state) {
        try {
            switch (state.getStateLabel()) {
                case "Active":
                    if (state instanceof ActiveProcessWin764) {
                        //this._os.getEntityList().get(state.getEntityTag()).setParser(new ParserWin732ActiveProcessCrashDMP((CrashDump32) this._dumpFormat, (Windows732OS) this._os, (TranslatorWin32) this._translator));
                        this._os.setParserByTag(state.getEntityTag(), new ParserWin764ActiveProcessRawDMP((RawDumpWin7) this._dumpFormat, (Windows764OS) this._os, (TranslatorWin64) this._translator));
                    }
                    /*if (state instanceof ActiveSocketWin764) {
                        //this._os.getEntityList().get(state.getEntityTag()).setParser(new ParserWin732SocketCrashDMP((CrashDump32) this._dumpFormat, (Windows732OS) this._os, (TranslatorWin32) this._translator));
                        this._os.setParserByTag(state.getEntityTag(), new ParserWin764SocketCrashDMP((RawDumpWin7) this._dumpFormat, (Windows764OS) this._os, (TranslatorWin64) this._translator));
                    }
                    if (state instanceof ActiveConnectionWin764) {
                        //this._os.getEntityList().get(state.getTag()).setParser(new ParserWin732TCPConnectionCrashDMP((CrashDump32) this._dumpFormat, (Windows732OS) this._os, (TranslatorWin32) this._translator));
                        this._os.setParserByTag(state.getEntityTag(), new ParserWin764ConnectionCrashDMP((RawDumpWin7) this._dumpFormat, (Windows764OS) this._os, (TranslatorWin64) this._translator));
                    }*/
                    break;
                /*case "Hidden":
                    if (state instanceof HiddenProcessWin764) {
                        //this._os.getEntityList().get(state.getEntityTag()).setParser(new ParserWin732HiddenProcessCrashDMP((CrashDump32) this._dumpFormat, (Windows732OS) this._os, (TranslatorWin32) this._translator));
                        this._os.setParserByTag(state.getEntityTag(), new ParserWin764HiddenProcessCrashDMP((RawDumpWin7) this._dumpFormat, (Windows764OS) this._os, (TranslatorWin64) this._translator));
                    }
                    break;*/
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setParser(Entity entity, Parser parser) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

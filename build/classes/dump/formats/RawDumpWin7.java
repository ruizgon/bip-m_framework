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
package dump.formats;

import dump.parsers.Parser;
import dump.parsers.ParserKDBGRawDMPWin764;
import entities.EntityKDBG;
import java.math.BigInteger;
import java.util.Map;
import output.OutputManagerConsole;
import system.utils.CharOperator;
import system.utils.DataManager;
import system.utils.RandomFileSeeker;

/**
 *
 * @author Gonzalo
 */
public class RawDumpWin7 extends RawDump {

    private EntityKDBG _entityKDBG;
    private Parser _parserKDBG;

    public RawDumpWin7(String _path) {
        super(_path);
    }

    public RawDumpWin7(String _path, boolean littleEndian) {
        super(_path);
        this._randomFileSeeker = new RandomFileSeeker();
        this._dataManager = DataManager.getInstance(_path, _randomFileSeeker, littleEndian);

        /**
         * Registra observadores
         */
        this.addObserver(OutputManagerConsole.getInstance());
    }

    public EntityKDBG getEntityKDBG() {
        return _entityKDBG;
    }

    public void setEntityKDBG(EntityKDBG _entityKDBG) {
        this._entityKDBG = _entityKDBG;
    }

    public Parser getParserKDBG() {
        return _parserKDBG;
    }

    public void setParserKDBG(Parser _parserKDBG) {
        this._parserKDBG = _parserKDBG;
    }

    @Override
    public void getDumpFormatContent() {
        /**
         * Inicializo _randomFileSeeker
         */
        this.getRandomFileSeeker().obtainRandomAccessFile(this.getPath());

        this._parserKDBG.parse(null);

        /**
         * Setea KDBG Entity
         */
        EntityKDBG eKDBG = (EntityKDBG) this._parserKDBG.getEntities().get("KDBG");
        this.setEntityKDBG(eKDBG);
    }

    @Override
    public void setLittleEndian(boolean _littleEndian) {
        this._littleEndian = _littleEndian;
    }

    @Override
    public long getFileOffset(BigInteger position) {
        long offset = 0;

        try {
            offset = position.longValue();
        } catch (Exception ex) {
ex.printStackTrace();
        }
        return offset;
    }

    @Override
    public String getContentByOffset(long offset, int length) {
        String content = this._randomFileSeeker.getContent(offset, length);

        if (_littleEndian) {
            content = CharOperator.getLittleEndianCharArray(content).toString();
        }

        return content;
    }

    @Override
    public BigInteger getPhysicalAddresByOffset(long offset) {
        BigInteger physicalAddress = BigInteger.ZERO;

        try {
            physicalAddress = physicalAddress.add(BigInteger.valueOf(offset));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return physicalAddress;
    }

    @Override
    public void processCommand(String command, String modifier) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

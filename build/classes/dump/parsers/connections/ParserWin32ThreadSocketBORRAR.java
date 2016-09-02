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
package dump.parsers.connections;

import dump.formats.DumpFormat;
import dump.ooss.OperatingSystemStructure;
import dump.parsers.ParserWin;
import translation.Translator;

/**
 *
 * @author Gonzalo
 */
public abstract class ParserWin32ThreadSocketBORRAR extends ParserWin {

    protected final int _LINE_LENGTH = 16;
    protected final int _LIST_ENTRY_ENTRY_SIZE = 0x004;
    protected final int _PAGE_SIZE = 4096;

    private ParserWin32Connection _parserConnection;

    public ParserWin32ThreadSocketBORRAR(DumpFormat _dumpFormat, OperatingSystemStructure _os) {
        super(_dumpFormat, _os);
    }

    public ParserWin32ThreadSocketBORRAR(DumpFormat _dumpFormat, OperatingSystemStructure _os, Translator _translator) {
        super(_dumpFormat, _os, _translator);
    }
    
     public ParserWin32ThreadSocketBORRAR(DumpFormat _dumpFormat, OperatingSystemStructure _os, Translator _translator, ParserWin32Connection _parserConnection) {
        super(_dumpFormat, _os, _translator);
        this.setParserConnection(_parserConnection);
    }

    public ParserWin32Connection getParserConnection() {
        return _parserConnection;
    }

    public void setParserConnection(ParserWin32Connection _parserConnection) {
        this._parserConnection = _parserConnection;
    }

}

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
import dump.parsers.ParserWin64;
import dump.parsers.interfaces.IParserConnection;
import dump.parsers.process.ParserWin64Process;
import translation.Translator;

/**
 *
 * @author Gonzalo
 */
public abstract class ParserWin64Connection extends ParserWin64 implements IParserConnection {

    private ParserWin64Process _parserProcess;

    public ParserWin64Connection(DumpFormat _dumpFormat, OperatingSystemStructure _os) {
        super(_dumpFormat, _os);
    }

    public ParserWin64Connection(DumpFormat _dumpFormat, OperatingSystemStructure _os, Translator _translator) {
        super(_dumpFormat, _os, _translator);
    }

    public ParserWin64Connection(DumpFormat _dumpFormat, OperatingSystemStructure _os, Translator _translator, ParserWin64Process _parserProcess) {
        super(_dumpFormat, _os, _translator);
        this.setParserProcess(_parserProcess);
    }

    public ParserWin64Process getParserProcess() {
        return _parserProcess;
    }

    public void setParserProcess(ParserWin64Process _parserProcess) {
        this._parserProcess = _parserProcess;
    }
}

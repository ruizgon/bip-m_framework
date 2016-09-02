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
package dump.parsers.process;

import dump.formats.DumpFormat;
import dump.ooss.OperatingSystemStructure;
import dump.parsers.ParserWin32;
import dump.parsers.interfaces.IParserProcess;
import translation.Translator;

/**
 *
 * @author Gonzalo
 */
public abstract class ParserWin32Process extends ParserWin32 implements IParserProcess {

    public ParserWin32Process(DumpFormat _dumpFormat, OperatingSystemStructure _os) {
        super(_dumpFormat, _os);
    }

    public ParserWin32Process(DumpFormat _dumpFormat, OperatingSystemStructure _os, Translator _translator) {
        super(_dumpFormat, _os, _translator);
    }

    /**
     * TODO implementar
     *
     * @param offset
     * @param DTB
     * @return
     */
    /*@Override
    public EntityKPROCESS obtainKPROCESSContent(long offset, long DTB) {
        EntityKPROCESSWin732 kProcess = null;

        try {
            kProcess = new EntityKPROCESSWin732();
            kProcess.setAttributes(new HashMap<String, EntityAttribute>());
            EntityAttribute a0 = new EntityAttribute("DirectoryTableBase", 1, null, "int 32", false);
            CrashDump32 dump = (CrashDump32) this.getDumpFormat();
            String directoryTableBase = dump.getHeader().getItems().get("DIRECTORY_TABLE_BASE").getContent().toString();
            a0.setContent(directoryTableBase);
            kProcess.getAttributes().put("DirectoryTableBase", a0);

            if (Conversor.hexToLong(directoryTableBase) != DTB) {
                kProcess = null;
            }

        } catch (Exception ex) {

        }

        return kProcess;
    }*/
}

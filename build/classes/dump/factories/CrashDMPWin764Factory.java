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
package dump.factories;

import dump.formats.CrashDump;
import dump.formats.CrashDump64;
import dump.ooss.Windows764OS;

/**
 *
 * @author Gonzalo
 */
public class CrashDMPWin764Factory extends DumpFactory{

    @Override
    public CrashDump createDumpFormat(String path, boolean littleEndian) {
       CrashDump64 dumpFormat = new CrashDump64(path, littleEndian);
       
       return dumpFormat;
    }

    @Override
    public Windows764OS createOperatingSystemStructure() {
        Windows764OS operatingSystem = new Windows764OS();
        
        /**
         * Setear a cada Entity en Parser correspondiente
         */
        
        return operatingSystem;
    }
    
}

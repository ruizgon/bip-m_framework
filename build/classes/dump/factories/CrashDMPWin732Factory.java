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
import dump.formats.CrashDump32;
import dump.ooss.Windows732OS;

/**
 *
 * @author Gonzalo 
 */
public class CrashDMPWin732Factory extends DumpFactory{

    @Override
    public CrashDump createDumpFormat(String path, boolean littleEndian) { //Crear una instancia de un CrashDump 
       CrashDump32 dumpFormat = new CrashDump32(path, littleEndian);
       
       return dumpFormat;
    }

    @Override
    public Windows732OS createOperatingSystemStructure() { //Crear una instancia de Win732OS 
        Windows732OS operatingSystem = new Windows732OS();
        
        /**
         * Setear a cada Entity en Parser correspondiente
         */
        
        return operatingSystem;
    }
    
}

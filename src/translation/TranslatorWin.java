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
package translation;

import dump.formats.DumpFormat;
import dump.ooss.OperatingSystemStructure;
import dump.ooss.Windows;

/**
 *
 * @author Gonzalo
 */
public abstract class TranslatorWin extends Translator {
    /**
     * Directory Table Base (Registro CR3) (PDBR)
     */
    private long DTB;

    public TranslatorWin() {
    }

    public TranslatorWin(OperatingSystemStructure structure, DumpFormat dumpFormat) {
        super(structure, dumpFormat);
        Windows w = (Windows) structure;
    }

    public long getDTB() {
        return DTB;
    }

    public void setDTB(long DTB) {
        this.DTB = DTB;
    }

}

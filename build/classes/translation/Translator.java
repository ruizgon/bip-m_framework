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
import entities.translation.EntityAddressSpace;
import java.util.ArrayList;
import system.utils.Observable;

/**
 *
 * @author Gonzalo
 */
public abstract class Translator extends Observable implements ITranslator {

    protected OperatingSystemStructure _structure;
    protected DumpFormat _dumpFormat;
    protected String _startAddressSpace;
    protected String _endAddressSpace;
    protected ArrayList<EntityAddressSpace> _aSList;
    protected boolean littleEndian;

    protected Translator() {
    }

    protected Translator(OperatingSystemStructure structure, DumpFormat dumpFormat) {
        this._structure = structure;
        this._dumpFormat = dumpFormat;
    }

    public OperatingSystemStructure getStructure() {
        return _structure;
    }

    public void setStructure(OperatingSystemStructure _structure) {
        this._structure = _structure;
    }

    public DumpFormat getDumpFormat() {
        return _dumpFormat;
    }

    public void setDumpFormat(DumpFormat _dumpFormat) {
        this._dumpFormat = _dumpFormat;
    }

    /*public ArrayList<EntityAddressSpace> executeTranslateProcess() {
     obtainAddressSpaceList();
     for (EntityAddressSpace aS : _aSList) {
     calculatePhysicalAddress(aS);
     obtainPhysicalAddressContent(aS);
     _aSList.add(aS);
     }

     return _aSList;
     }*/
    public String getStartAddressSpace() {
        return _startAddressSpace;
    }

    public void setStartAddressSpace(String _startAddressSpace) {
        this._startAddressSpace = _startAddressSpace;
    }

    public String getEndAddressSpace() {
        return _endAddressSpace;
    }

    public void setEndAddressSpace(String _endAddressSpace) {
        this._endAddressSpace = _endAddressSpace;
    }

    public ArrayList<EntityAddressSpace> getaSList() {
        return _aSList;
    }

    public void setaSList(ArrayList<EntityAddressSpace> _aSList) {
        this._aSList = _aSList;
    }

    public boolean isLittleEndian() {
        return littleEndian;
    }

    public void setLittleEndian(boolean littleEndian) {
        this.littleEndian = littleEndian;
    }

}

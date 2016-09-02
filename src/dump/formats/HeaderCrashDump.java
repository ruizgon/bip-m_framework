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

import java.util.Map;
import system.utils.RandomFileSeeker;

/**
 *
 * @author Gonzalo
 */
public abstract class HeaderCrashDump {

    private String _path;
    private RandomFileSeeker _randomFileSeeker;
    private boolean _littleEndian;
    private Map<String, HeaderCrashDumpItem> _items;

    public HeaderCrashDump(String _path, RandomFileSeeker _randomFileSeeker, boolean _littleEndian) {
        this._path = _path;
        this._randomFileSeeker = _randomFileSeeker;
        this._littleEndian = _littleEndian;
    }

    public String getPath() {
        return _path;
    }

    public void setPath(String _path) {
        this._path = _path;
    }

    public RandomFileSeeker getRandomFileSeeker() {
        return _randomFileSeeker;
    }

    public void setRandomFileSeeker(RandomFileSeeker _randomFileSeeker) {
        this._randomFileSeeker = _randomFileSeeker;
    }

    public boolean isLittleEndian() {
        return _littleEndian;
    }

    public void setLittleEndian(boolean _littleEndian) {
        this._littleEndian = _littleEndian;
    }

    public Map<String, HeaderCrashDumpItem> getItems() {
        return _items;
    }

    public void setItems(Map<String, HeaderCrashDumpItem> _items) {
        this._items = _items;
    }

    public abstract void setLittleEndianProcess(boolean _littleEndian);
    
    /*
     * @Descripción: obtiene el contenido por cada elemento del hashMap _items
     * Obtiene el contenido de los objeto de Clase PhysicalMemoryDescriptor32 y
     * ExceptionRecord32
     */
    public abstract void executeObtainingItemsContentProcess();

}

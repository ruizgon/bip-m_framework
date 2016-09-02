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
package entities.hive;

import java.util.Date;

/**
 *
 * @author alberdij
 */
public class EntityHHiveHBaseBlock {
    
    /*
    Estos parámetros serían del _HBASE_BLOCK, ver si se genera una clase más con 
    estos datos y se colocan otros en HHive
    */
    private Date _timeLastAccess;
    private String _firstKey;
    private String _fileName;
    private String _checksum;
    private EntityCell _rootCell;

    public Date getTimeLastAccess() {
        return _timeLastAccess;
    }

    public void setTimeLastAccess(Date timeLastAccess) {
        this._timeLastAccess = timeLastAccess;
    }

    public String getFirstKey() {
        return _firstKey;
    }

    public void setFirstKey(String firstKey) {
        this._firstKey = firstKey;
    }

    public String getFileName() {
        return _fileName;
    }

    public void setFileName(String fileName) {
        this._fileName = fileName;
    }

    public String getChecksum() {
        return _checksum;
    }

    public void setChecksum(String checksum) {
        this._checksum = checksum;
    }

    public EntityCell getRootCell() {
        return _rootCell;
    }

    public void setRootCell(EntityCell rootCell) {
        this._rootCell = rootCell;
    }
    
    
    
}

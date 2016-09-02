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
package persistence.database;

/**
 *
 * @author juani
 */
public abstract class MapperFactory implements IMapperMalware{

    protected String _path;

    protected String _currentHashType;

    protected String _currentHash;

    protected String _databaseName;

    public String getPath() {
        return _path;
    }

    public void setPath(String _path) {
        this._path = _path;
    }

    public String getCurrentHashType() {
        return _currentHashType;
    }

    public void setCurrentHashType(String _currentHashType) {
        this._currentHashType = _currentHashType;
    }

    public String getCurrentHash() {
        return _currentHash;
    }

    public void setCurrentHash(String _currentHash) {
        this._currentHash = _currentHash;
    }

    public String getDatabaseName() {
        return _databaseName;
    }

    public void setDatabaseName(String _databaseName) {
        this._databaseName = _databaseName;
    }

    public abstract MapperSeeker creatByTag(String tag);

    public abstract MapperGeneric create();

    public abstract MapperProcess createMapperProcess();
    
    public abstract MapperThread createMapperThread();

    public abstract MapperLib createMapperLib();

    public abstract MapperConnection createMapperConnection();
    
    public abstract MapperSocket createMapperSocket();

}

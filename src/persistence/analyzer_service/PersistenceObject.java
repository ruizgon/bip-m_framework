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
package persistence.analyzer_service;

import dump.DumpManager;

/**
 *
 * @author Gonzalo
 */
public abstract class PersistenceObject implements IProcessService, IThreadService, IFileService, ILibService, IHandleService, IHiveService, IConnectionService, IRootKitService, ILoadAnalysisResult {

    protected static PersistenceObject _instance;
    private DumpManager _dumpManager;
    protected String _version;
    protected String _propertiesFile;
    protected String _path;
    protected String _currentHashType;
    protected String _currentHash;

    public String getVersion() {
        return _version;
    }

    public void setVersion(String _version) {
        this._version = _version;
    }

    public String getPropertiesFile() {
        return _propertiesFile;
    }

    public void setPropertiesFile(String _propertiesFile) {
        this._propertiesFile = _propertiesFile;
    }

    protected PersistenceObject(DumpManager _dumpManager) {
        this._dumpManager = _dumpManager;
    }

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

    public DumpManager getDumpManager() {
        return _dumpManager;
    }

    public void setDumpManager(DumpManager _dumpManager) {
        this._dumpManager = _dumpManager;
    }

}

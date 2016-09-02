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

import entities.Entity;
import output.OutputManagerAnalyzer;
import output.OutputManagerConsole;
import output.OutputManagerException;
import system.utils.Observable;

/**
 *
 * @author Gonzalo
 */
public abstract class Mapper extends Observable  {
    protected String _databaseName;
    protected String _path;
    protected String _currentHashType;
    protected String _currentHash;
    protected ModelClass _model;

    public Mapper() {
        /**
         * Registra observadores
         */
        this.addObserver(OutputManagerException.getInstance());
        this.addObserver(OutputManagerAnalyzer.getInstance());
        this.addObserver(OutputManagerConsole.getInstance());
    }

    public String getDatabaseName() {
        return _databaseName;
    }

    public void setDatabaseName(String _databaseName) {
        this._databaseName = _databaseName;
    }
    
    public ModelClass getModel(){
        return this._model;
    }
    
    public void setModel(ModelClass _modelClass){
        this._model = _modelClass;
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
    public abstract String getScript(String path);
    public abstract int persist(Entity entity);
}

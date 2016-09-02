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
package dump.strategy;

import dump.formats.DumpFormat;
import dump.ooss.OperatingSystemStructure;
import entities.Entity;
import persistence.dump_service.mysql.DContentServiceMySQL;
import translation.Translator;

/**
 *
 * @author Gonzalo
 */
public class MySQLLoader extends DatabaseLoader {

    public MySQLLoader(OperatingSystemStructure operatingSystemStructure, DumpFormat dumpFormat, Translator translator) {
        super(operatingSystemStructure, dumpFormat, translator);
        this._persitenceDmpObj = DContentServiceMySQL.getInstance();
        
        /**
         * Registra observadores
         */
        this.addObserver(output.OutputManagerConsole.getInstance());
    }

    @Override
    public boolean isValidDump() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void createStructure() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void createStructure(OperatingSystemStructure structure) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Responsabilidad del dumpManager, se comenta antes de descartar por
     * completo
     *
     * @param entities
     * @return
     */
    @Override
    public String loadContent(java.util.Map<Object, Entity> entities) {
        String resultado = "No procesado";

        try {
            String log = "Persistiendo entidades...";
            this.notifyObservers(log);
            resultado = this._persitenceDmpObj.loadContent(entities);
            log = "Persistencia finalizada.";
            this.notifyObservers(log);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return resultado;
    }

    @Override
    public String loadContentEntity(Entity entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updatePersistenceDmpObjectParameters() {
        if (this._persitenceDmpObj instanceof DContentServiceMySQL) {
            DContentServiceMySQL pObj = (DContentServiceMySQL) this._persitenceDmpObj;
            pObj.setCurrentHashType(this._currentHashType);
            pObj.setCurrentHash(this._currentHash);
            pObj.setPath(this._path);
            pObj.setVersion(this._version);
            pObj.setDatabaseName(this._databaseName);
            this._persitenceDmpObj = pObj;
        }
    }

    @Override
    public int persistMetadata(Entity entity, String tag, String status) {
        int respuesta = 0;

        try {
            respuesta = this._persitenceDmpObj.persistMetadata(entity, tag, status);
        } catch (Exception ex) {
            respuesta = -1;
        }
        
        return respuesta;
    }

    @Override
    public void update(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

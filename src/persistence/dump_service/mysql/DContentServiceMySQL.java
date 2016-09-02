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
package persistence.dump_service.mysql;

import dump.formats.DumpFormat;
import entities.Entity;
import java.util.Iterator;
import java.util.Map.Entry;
import persistence.database.IDServiceDatabase;
import persistence.database.MapperFactory;
import persistence.database.mysql.MapperFactoryMySQLWin732;
import persistence.database.mysql.MapperFactoryMySQLWin764;
import persistence.dump_service.PersistenceDumpObject;

public class DContentServiceMySQL extends PersistenceDumpObject implements IDServiceDatabase {

    private static DContentServiceMySQL _instance;

    private String _databaseName;
    private MapperFactory _mapperFactory;

    private DContentServiceMySQL() {
        super();
    }

    public static DContentServiceMySQL getInstance() {
        if (_instance == null) {
            _instance = new DContentServiceMySQL();
        }
        return _instance;
    }

    public String getDatabaseName() {
        return _databaseName;
    }

    public void setDatabaseName(String _databaseName) {
        this._databaseName = _databaseName;
    }

    public MapperFactory getMapperFactory() {
        return _mapperFactory;
    }

    public void setMapperFactory(MapperFactory _mapperFactory) {
        this._mapperFactory = _mapperFactory;
    }

    @Override
    public String loadContent(java.util.Map<Object, Entity> entities) {
        String resultado = "No procesado";

        try {
            int respuesta = 0;
            Iterator<Entry<Object, Entity>> i = entities.entrySet().iterator();
            while (i.hasNext()) {
                Entry<Object, Entity> e = (Entry<Object, Entity>) i.next();
                Entity entity = (Entity) e.getValue();
                respuesta = this.getMapperFact().creatByTag(entity.getTag()).persist(entity);
            }
            
            resultado = "Procesado";
        } catch (Exception ex) {
            ex.printStackTrace();
            resultado = "Error";
        }

        return resultado;
    }

    @Override
    public String loadContentEntity(Entity entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateLoadingState(String tag) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void obtainLoadingStates() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MapperFactory getMapperFactoryByVersion(String version, String databaseName) {
        MapperFactory mapperFactory = null;

        try {
            switch (version) {
                case "WIN7_X86":
                    mapperFactory = new MapperFactoryMySQLWin732();
                    mapperFactory.setPath(this._path);
                    mapperFactory.setCurrentHashType(this._currentHashType);
                    mapperFactory.setCurrentHash(this._currentHash);
                    mapperFactory.setDatabaseName(this._databaseName);
                    break;
                case "WIN7_X64":
                    mapperFactory = new MapperFactoryMySQLWin764();
                    mapperFactory.setPath(this._path);
                    mapperFactory.setCurrentHashType(this._currentHashType);
                    mapperFactory.setCurrentHash(this._currentHash);
                    mapperFactory.setDatabaseName(this._databaseName);
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {

        }

        return mapperFactory;
    }

    @Override
    public MapperFactory getMapperFact() {
        if (this._mapperFactory == null) {
            this._mapperFactory = this.getMapperFactoryByVersion(this._version, this._databaseName);
        }

        return this._mapperFactory;
    }

    @Override
    public int persistDumpFormatInformation(DumpFormat dumpFormat) {
        int respuesta = 0;

        try {
            respuesta = this.getMapperFact().create().persistDumpFormatInformation(dumpFormat);
        } catch (Exception ex) {
            respuesta = -1;
            ex.printStackTrace();
        }

        return respuesta;
    }

    @Override
    public int persistMetadata(Entity entity, String tag, String status) {
        int respuesta = 0;

        try {
            respuesta = this.getMapperFact().create().persistMetadata(entity, tag, status);
        } catch (Exception ex) {
            respuesta = -1;
            ex.printStackTrace();
        }

        return respuesta;
    }

}

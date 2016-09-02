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

import dump.formats.CrashDump32;
import dump.formats.CrashDump64;
import dump.formats.DumpFormat;
import entities.Entity;
import persistence.IDataService;
import persistence.database.mysql.MapperMySQLCrashDMP32;
import persistence.database.mysql.MapperMySQLCrashDMP64;

/**
 *
 * @author Gonzalo
 */
public abstract class MapperGeneric extends MapperSeeker implements IDataService{

    /**
     * 
     * @param databaseName
     * @return true: si existen bases de datos creadas.
     */
    public abstract boolean isDataBaseCreated(String databaseName);
    /**
     * 
     * @param path
     * @return entero que indica si la creación de la base de datos fue
     * satisfactoria.
     */
    public abstract int createStructure(String path);
    
    public MapperDumpFormat getMapperDumpFormat(DumpFormat dumpFormat){
        MapperDumpFormat mapper = null;
        
        try{
            if(dumpFormat instanceof CrashDump32){
                mapper = new MapperMySQLCrashDMP32();
                mapper._databaseName = this._databaseName;              
                mapper.setCurrentHashType(this._currentHashType);
                mapper.setCurrentHash(this._currentHash);
            }else{
                if(dumpFormat instanceof CrashDump64){
                    mapper = new MapperMySQLCrashDMP64();
                mapper._databaseName = this._databaseName;              
                mapper.setCurrentHashType(this._currentHashType);
                mapper.setCurrentHash(this._currentHash);
                }
            }
        }catch(Exception ex){
            mapper = null;
            ex.printStackTrace();
        }
        
        return mapper;
    }
    
    public abstract int persistDumpFormatInformation(DumpFormat dumpFormat);
    public abstract int persistMetadata(Entity entity, String tag, String status);
}

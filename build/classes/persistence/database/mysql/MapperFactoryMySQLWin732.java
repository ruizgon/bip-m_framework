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
package persistence.database.mysql;

import persistence.database.MapperSocket;
import persistence.database.IMapperMalware;
import persistence.database.MapperSeeker;
import persistence.database.MapperConnection;
import persistence.database.MapperFactory;
import persistence.database.MapperGeneric;
import persistence.database.MapperLib;
import persistence.database.MapperProcess;
import persistence.database.MapperSSDT;
import persistence.database.MapperSSDTRootkit;
import persistence.database.MapperThread;

/**
 *
 * @author juani
 */
public class MapperFactoryMySQLWin732 extends MapperFactory {

    @Override
    public MapperSeeker creatByTag(String tag) {
        MapperSeeker mapper = null;

        switch (tag) {
            case "Process":
                mapper = this.createMapperProcess();
                break;
            case "Thread":
                mapper = this.createMapperThread();
                break;
            case "Library":
                mapper = this.createMapperLib();
                break;
            case "Connection":
                mapper = this.createMapperConnection();
                break;
            case "Socket":
                mapper = this.createMapperSocket();
                break;
            case "SSDT":
                mapper = this.createMapperSSDT();
                break;
            case "Rootkit_SSDTHook":
                mapper = this.createMapperSSDTRootkit();
                break;
            default:
                mapper = this.create();
                break;
        }

        return mapper;
    }

    @Override
    public MapperGeneric create() {
        return new MapperMySQLWin732(this._path, this._currentHashType, this._currentHash, this._databaseName);
    }

    @Override
    public MapperProcess createMapperProcess() {
        return new MapperProcessMySQLWin732();
    }

    @Override
    public MapperThread createMapperThread() {
        return new MapperThreadMySQLWin732();
    }

    @Override
    public MapperLib createMapperLib() {
        return new MapperLibMySQLWin732();
    }

    @Override
    public MapperConnection createMapperConnection() {
        return new MapperConnectionMySQLWin732();
    }

    @Override
    public MapperSocket createMapperSocket() {
        return new MapperSocketMySQLWin732();
    }

    @Override
    public MapperSSDT createMapperSSDT() {
        return new MapperSSDTMySQLWin732();
    }

    @Override
    public MapperSSDTRootkit createMapperSSDTRootkit() {
        return new MapperHooksSSDTMySQLWin732();
    }

}

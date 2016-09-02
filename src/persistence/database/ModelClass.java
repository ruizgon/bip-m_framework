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

import java.sql.Connection;
import javax.sql.RowSet;

/**
 *
 * @author juani
 */
public abstract class ModelClass {

    protected Connection _connection;

    public Connection getConnection() {
        return _connection;
    }

    public void setConnection(Connection _connection) {
        this._connection = _connection;
    }

    public abstract void modifyConnection(String _server, int _port, String _database, String _user, String _pass);
    
    /**
     * Debe devolver un ResultSet, luego ver bien qué y cómo
     */
    public abstract RowSet get(String query, Object[] parametersIn, Object[] parametersOut);

    /**
     * Debe devolver un código que será el restultado OK o FAIL de la operación
     * insert
     */
    public abstract int insert(String query, Object[] parametersIn, Object[] parametersOut);

    /**
     * Debe devolver un código que será el restultado OK o FAIL de la operación
     * remove
     */
    public abstract int remove(String query, Object[] parametersIn, Object[] parametersOut);
}

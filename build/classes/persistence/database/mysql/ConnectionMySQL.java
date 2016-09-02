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

import java.sql.Connection;
import java.sql.DriverManager;
import persistence.database.DatabaseConnection;

/**
 *
 * @author Gonzalo
 */
public class ConnectionMySQL extends DatabaseConnection {

    private ConnectionMySQL() {

    }

    public static ConnectionMySQL getInstance() {
        if (_instance == null) {
            _instance = new ConnectionMySQL();
        }

        return (ConnectionMySQL) _instance;
    }

    @Override
    public Connection getConnection(String server, int port, String database, String user, String pass) {
        Connection conn = null;

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:mysql://" + server + ":" + String.valueOf(port) + "/" + database + "?" + "user=" + user + "&password=" + pass);
            this.setConnection(conn);
        } catch (Exception ex) {
            conn = null;
        }

        return conn;
    }

    @Override
    public Connection getConnection(String server, int port, String user, String pass) {
        Connection conn = null;

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:mysql://" + server + ":" + String.valueOf(port) + "/?" + "user=" + user + "&password=" + pass);
            this.setConnection(conn);
        } catch (Exception ex) {
            conn = null;
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }

        return conn;
    }
}

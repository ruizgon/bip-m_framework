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

import com.mysql.jdbc.CallableStatement;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import persistence.database.ModelClass;
import system.utils.Conversor;

/**
 *
 * @author Gonzalo
 */
public class MySQLModel extends ModelClass {

    private static MySQLModel _instance;

    private String _server;
    private int _port;
    private String _database;
    private String _user;
    private String _pass;

    private MySQLModel() {
        this._server = "localhost";
        this._port = 3306;
        this._user = "root";
        this._pass = "bipm";
    }

    private MySQLModel(String _server, int _port, String _database, String _user, String _pass) {
        this._server = _server;
        this._port = _port;
        this._database = _database;
        this._user = _user;
        this._pass = _pass;
    }

    public MySQLModel(String _server, int _port, String _user, String _pass) {
        this._server = _server;
        this._port = _port;
        this._user = _user;
        this._pass = _pass;
        ConnectionMySQL connection = ConnectionMySQL.getInstance();
        this._connection = connection.getConnection(_server, _port, _user, _pass);
    }

    public static MySQLModel getInstane() {
        if (_instance == null) {
            _instance = new MySQLModel();
        }

        return _instance;
    }

    public static MySQLModel getInstane(String _server, int _port, String _database, String _user, String _pass) {
        if (_instance == null) {
            _instance = new MySQLModel(_server, _port, _database, _user, _pass);
        }

        return _instance;
    }

    public static MySQLModel getInstane(String _server, int _port, String _user, String _pass) {
        if (_instance == null) {
            _instance = new MySQLModel(_server, _port, _user, _pass);
        }

        return _instance;
    }

    public Connection obtainConnection() {
        try {
            if (this._connection == null) {
                ConnectionMySQL connection = ConnectionMySQL.getInstance();
                if (this._database == null) {
                    this._connection = connection.getConnection(_server, _port, _user, _pass);
                } else {
                    this._connection = connection.getConnection(_server, _port, _database, _user, _pass);
                }
            } else {
                if (this._connection.isClosed()) {
                    ConnectionMySQL connection = ConnectionMySQL.getInstance();
                    if (this._database == null) {
                        this._connection = connection.getConnection(_server, _port, _user, _pass);
                    } else {
                        this._connection = connection.getConnection(_server, _port, _database, _user, _pass);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return this._connection;
    }

    @Override
    public void modifyConnection(String _server, int _port, String _database, String _user, String _pass) {
        try {
            if (_server == null) {
                _server = this._server;
            } else {
                this._server = _server;
            }
            if (_port == -1) {
                _port = this._port;
            } else {
                this._port = _port;
            }
            if (_database == null) {
                _database = this._database;
            } else {
                this._database = _database;
            }
            if (_user == null) {
                _user = this._user;
            } else {
                this._user = _user;
            }
            if (_pass == null) {
                _pass = this._pass;
            } else {
                this._pass = _pass;
            }
            ConnectionMySQL connection = ConnectionMySQL.getInstance();
            this._connection = connection.getConnection(_server, _port, _database, _user, _pass);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public RowSet get(String query, Object[] parametersIn, Object[] parametersOut) {
        ResultSet resultSet = null;
        CachedRowSet crs = null;

        PreparedStatement stmt = null;
        Connection conn = null;

        try {
            stmt = this.obtainConnection().prepareCall(query);
            CallableStatement callStmt = null;
            if (stmt instanceof CallableStatement) {
                callStmt = (CallableStatement) stmt;
            }
            int i = 0;
            if (parametersIn != null) {
                for (Object o : parametersIn) {
                    ++i;
                    if (o != null) {
                        if (o instanceof Integer) {
                            callStmt.setInt(i, (int) o);
                        } else if (o instanceof Long) {
                            callStmt.setLong(i, (long) o);
                        } else if (o instanceof String) {
                            callStmt.setString(i, (String) o);
                        } else if (o instanceof BigInteger) {
                            BigInteger b = (BigInteger) o;
                            String bHex = Conversor.bigIntToHexString(b);
                            callStmt.setString(i, bHex);
                        } else if (o instanceof Date) {
                            callStmt.setDate(i, (Date) o);
                        }
                    } else {
                        callStmt.setNull(i, Types.NULL);
                    }
                }
            }
            int out = 0;
            if (parametersOut != null) {
                for (Object o : parametersOut) {
                    ++i;
                    ++out;
                    if (o != null) {
                        if (o instanceof Integer) {
                            callStmt.registerOutParameter(i, Types.INTEGER);
                        } else if (o instanceof Long) {
                            callStmt.registerOutParameter(i, Types.INTEGER);
                        } else if (o instanceof String) {
                            callStmt.registerOutParameter(i, Types.VARCHAR);
                        } else if (o instanceof BigInteger) {
                            callStmt.registerOutParameter(i, Types.BIGINT);
                        } else if (o instanceof Date) {
                            callStmt.registerOutParameter(i, Types.DATE);
                        }
                    } else {
                        callStmt.registerOutParameter(i, Types.NULL);
                    }
                }
            }
            resultSet = stmt.executeQuery();
            /**
             * A partir de acá la lectura de los parámetros output
             */
            if (out > 0) {
                int outIndex = i - out + 1;
                for (int j = outIndex; j <= i; j++) {
                    callStmt.getInt(j);
                }
            }
            crs = RowSetProvider.newFactory().createCachedRowSet();
            crs.populate(resultSet);
            resultSet.close();
            stmt.close();
        } catch (Exception ex) {
            resultSet = null;
            ex.printStackTrace();
        } finally {
            try {
                this.obtainConnection().close();
            } catch (SQLException ex) {
                Logger.getLogger(MySQLModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return crs;
    }

    @Override
    public int insert(String query, Object[] parametersIn, Object[] parametersOut) {
        int respuesta = 0;

        PreparedStatement stmt = null;
        Connection conn = null;

        try {
            stmt = this.obtainConnection().prepareCall(query);
            CallableStatement callStmt = null;
            if (stmt instanceof CallableStatement) {
                callStmt = (CallableStatement) stmt;
            }
            int i = 0;
            for (Object o : parametersIn) {
                ++i;
                if (o != null) {
                    if (o instanceof Integer) {
                        callStmt.setInt(i, (int) o);
                    } else if (o instanceof Long) {
                        callStmt.setLong(i, (long) o);
                    } else if (o instanceof String) {
                        callStmt.setString(i, (String) o);
                    } else if (o instanceof BigInteger) {
                        BigInteger b = (BigInteger) o;
                        String bHex = Conversor.bigIntToHexString(b);
                        callStmt.setString(i, bHex);
                    } else if (o instanceof Date) {
                        callStmt.setDate(i, (Date) o);
                    } else if (o instanceof Timestamp) {
                        callStmt.setTimestamp(i, (Timestamp) o);
                    }
                } else {
                    callStmt.setNull(i, Types.NULL);
                }
            }
            int out = 0;
            if (parametersOut != null) {
                for (Object o : parametersOut) {
                    ++i;
                    ++out;
                    if (o != null) {
                        if (o instanceof Integer) {
                            callStmt.registerOutParameter(i, Types.INTEGER);
                        } else if (o instanceof Long) {
                            callStmt.registerOutParameter(i, Types.INTEGER);
                        } else if (o instanceof String) {
                            callStmt.registerOutParameter(i, Types.VARCHAR);
                        } else if (o instanceof BigInteger) {
                            callStmt.registerOutParameter(i, Types.BIGINT);
                        } else if (o instanceof Date) {
                            callStmt.registerOutParameter(i, Types.DATE);
                        } else if (o instanceof Timestamp) {
                            callStmt.registerOutParameter(i, Types.TIMESTAMP);
                        }
                    } else {
                        callStmt.registerOutParameter(i, Types.NULL);
                    }
                }
            }
            respuesta = stmt.executeUpdate();
            /**
             * A partir de acá la lectura de los parámetros output
             */
            if (out > 0) {
                int outIndex = i - out + 1;
                for (int j = outIndex; j <= i; j++) {
                    respuesta = callStmt.getInt(j);
                    parametersOut[outIndex-i] = respuesta;
                }
            }
            stmt.close();
        } catch (Exception ex) {
            respuesta = -1;
            ex.printStackTrace();
        } finally {
            try {
                this.obtainConnection().close();
            } catch (SQLException ex) {
                Logger.getLogger(MySQLModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return respuesta;
    }

    @Override
    public int remove(String query, Object[] parametersIn, Object[] parametersOut
    ) {
        int respuesta = 0;

        PreparedStatement stmt = null;
        Connection conn = null;

        try {
            stmt = this.obtainConnection().prepareCall(query);
            CallableStatement callStmt = null;
            if (stmt instanceof CallableStatement) {
                callStmt = (CallableStatement) stmt;
            }
            int i = 0;
            for (Object o : parametersIn) {
                ++i;
                if (o != null) {
                    if (o instanceof Integer) {
                        callStmt.setInt(i, (int) o);
                    } else if (o instanceof Long) {
                        callStmt.setLong(i, (long) o);
                    } else if (o instanceof String) {
                        callStmt.setString(i, (String) o);
                    } else if (o instanceof BigInteger) {
                        BigInteger b = (BigInteger) o;
                        String bHex = Conversor.bigIntToHexString(b);
                        callStmt.setString(i, bHex);
                    } else if (o instanceof Date) {
                        callStmt.setDate(i, (Date) o);
                    } else if (o instanceof Timestamp) {
                        callStmt.setTimestamp(i, (Timestamp) o);
                    }
                } else {
                    callStmt.setNull(i, Types.NULL);
                }
            }
            int out = 0;
            if (parametersOut != null) {
                for (Object o : parametersOut) {
                    ++i;
                    ++out;
                    if (o != null) {
                        if (o instanceof Integer) {
                            callStmt.registerOutParameter(i, Types.INTEGER);
                        } else if (o instanceof Long) {
                            callStmt.registerOutParameter(i, Types.INTEGER);
                        } else if (o instanceof String) {
                            callStmt.registerOutParameter(i, Types.VARCHAR);
                        } else if (o instanceof BigInteger) {
                            callStmt.registerOutParameter(i, Types.BIGINT);
                        } else if (o instanceof Date) {
                            callStmt.registerOutParameter(i, Types.DATE);
                        } else if (o instanceof Timestamp){
                            callStmt.registerOutParameter(i, Types.TIMESTAMP);
                        }
                    } else {
                        callStmt.registerOutParameter(i, Types.NULL);
                    }
                }
            }
            respuesta = stmt.executeUpdate();
            /**
             * A partir de acá la lectura de los parámetros output
             */
            if (out > 0) {
                int outIndex = i - out + 1;
                for (int j = outIndex; j <= i; j++) {
                    respuesta = callStmt.getInt(j);
                    parametersOut[outIndex-i] = respuesta;
                }
            }
            stmt.close();
        } catch (Exception ex) {
            respuesta = -1;
            ex.printStackTrace();
        } finally {
            try {
                this.obtainConnection().close();
            } catch (SQLException ex) {
                Logger.getLogger(MySQLModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return respuesta;
    }

}

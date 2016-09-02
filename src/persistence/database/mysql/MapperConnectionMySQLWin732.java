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

import analyzer.states.ConnectionState;
import entities.Entity;
import entities.EntityList;
import entities.EntityListWin32;
import entities.connection.EntityAddressFamily;
import entities.connection.EntityAddressFamilyWin32;
import entities.connection.EntityAddressInfo;
import entities.connection.EntityAddressInfoWin32;
import entities.connection.EntityConnection;
import entities.connection.EntityLocalAddress;
import entities.connection.EntityLocalAddressWin32;
import entities.connection.EntityWindows732TCPConnection;
import entities.connection.EntityWindows732UDPConnection;
import entities.connection.EntityWindowsConnectionState;
import entities.process.EntityProcess;
import entities.process.EntityWindows732Process;
import entities.translation.EntityAddressSpace;
import entities.translation.EntityAddressSpaceWin32;
import java.util.ArrayList;
import javax.sql.rowset.CachedRowSet;
import persistence.database.MapperConnection;
import persistence.database.MapperProcess;

/**
 *
 * @author juani
 */
public class MapperConnectionMySQLWin732 extends MapperConnection {

    public MapperConnectionMySQLWin732() {
        this._model = MySQLModel.getInstane();
    }

    @Override
    public String getScript(String path) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int persist(Entity entity) {
        int respuesta = 0;
        int idConnection = -1;

        try {
            EntityWindows732TCPConnection tcpConnection = null;
            EntityWindows732UDPConnection udpConnection = null;

            EntityAddressFamilyWin32 addressFamily = null;
            EntityLocalAddressWin32 localAddress = null;
            EntityAddressInfoWin32 addressInfo = null;
            EntityWindows732Process process = null;
            EntityWindowsConnectionState connectionState = null;
            int tcp = -1;
            if (entity.getaS() != null) {
                if (null == this.getConnectionByVirtualAddress((String) entity.getaS().getVirtualAddressHex())) {
                    if (entity instanceof EntityWindows732TCPConnection) {

                        tcp = 1;
                        tcpConnection = (EntityWindows732TCPConnection) entity;
                        addressFamily = (EntityAddressFamilyWin32) tcpConnection.getAttributes().get("AddressFamily").getEntity();
                        addressInfo = (EntityAddressInfoWin32) tcpConnection.getAttributes().get("AddressInfo").getEntity();
                        process = (EntityWindows732Process) tcpConnection.getAttributes().get("Owner").getEntity();
                        connectionState = (EntityWindowsConnectionState) tcpConnection.getAttributes().get("State").getEntity();
                    } else if (entity instanceof EntityWindows732UDPConnection) {
                        tcp = 0;
                        udpConnection = (EntityWindows732UDPConnection) entity;
                        addressFamily = (EntityAddressFamilyWin32) udpConnection.getAttributes().get("AddressFamily").getEntity();
                        localAddress = (EntityLocalAddressWin32) udpConnection.getAttributes().get("LocalAddres").getEntity();
                        process = (EntityWindows732Process) udpConnection.getAttributes().get("Owner").getEntity();
                    }

                    if (tcp != -1) {

                        //mapeo el proceso si no està persistido, si está persistido no hace nada
                        MapperProcess _mp = new MapperProcessMySQLWin732();
                        _mp.persist(process);

                        Object[] parametersIn = new Object[1];
                        String query = "CALL SP_ADDRESS_FAMILY_GET (?)";
                        parametersIn[0] = (String) addressFamily.getAttributes().get("IPVersion").getContent();
                        CachedRowSet cachedRowSet = (CachedRowSet) this._model.get(query, parametersIn, null);
                        int idAddressFamily = 0;
                        while (cachedRowSet.next()) {
                            idAddressFamily = (int) cachedRowSet.getInt("idAddressFamily");
                        }

                        //instancia TCP
                        if (tcp == 1) {

                            parametersIn = new Object[1];
                            parametersIn[0] = connectionState.getCurrentState();
                            query = "CALL SP_CONNECTION_STATE_GET (?)";
                            cachedRowSet = (CachedRowSet) this._model.get(query, parametersIn, null);
                            int idState = 0;
                            while (cachedRowSet.next()) {
                                idState = cachedRowSet.getInt("idConnectionState");
                            }

                            parametersIn = new Object[11];
                            Object[] parametersOut = new Object[1];
                            query = "CALL SP_TCP_CONNECTION_INSERT (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                            parametersIn[0] = idAddressFamily; // _idAddressFamily
                            parametersIn[1] = (String) addressInfo.getAttributes().get("Local").getContent(); // _localAddress
                            parametersIn[2] = (String) addressInfo.getAttributes().get("Remota").getContent(); // _remoteAddress
                            parametersIn[3] = idState; // _idConnectionState
                            parametersIn[4] = (String) tcpConnection.getAttributes().get("LocalPort").getContent(); // _localPort
                            parametersIn[5] = (String) tcpConnection.getAttributes().get("RemotePort").getContent(); // _remotePort
                            long processID = 0;
                            if (process.getaS() != null) {
                                if (process.getProcessID() <= Integer.MAX_VALUE) {
                                    processID = process.getProcessID();
                                }
                            }
                            parametersIn[6] = processID; // _uniqueProcessId
                            parametersIn[7] = (String) tcpConnection.getAttributes().get("CreateTime").getContent(); // _createTime
                            parametersIn[8] = tcpConnection.getaS().getVirtualAddressHex(); // _virtualAddress
                            parametersIn[9] = tcpConnection.getaS().getPhysicalAddressHex();  // _physicalAddress
                            parametersIn[10] = tcpConnection.getaS().getOffsetInFileHex(); // _offset

                            //Ejecuta query
                            respuesta = this._model.insert(query, parametersIn, parametersOut);

                            //Obtiene id de la conexión insertada
                            idConnection = (int) parametersOut[0];

                            EntityList list = (EntityListWin32) tcpConnection.getAttributes().get("ConnectionLinks").getEntity();
                            parametersIn = new Object[3];
                            parametersIn[0] = idConnection;
                            parametersIn[1] = list.getfLinkHex();
                            parametersIn[2] = list.getbLinkHex();
                            query = "CALL SP_CONNECTION_LIST_INSERT (?, ?, ?)";
                            respuesta = this._model.insert(query, parametersIn, null);

                        }

                        //instancia UDP
                        if (tcp == 0) {

                            parametersIn = new Object[8];
                            Object[] parametersOut = new Object[1];
                            query = "CALL SP_UDP_CONNECTION_INSERT (?, ?, ?, ?, ?, ?, ?, ?, ?)";

                            long processID = 0;
                            if (process.getaS() != null) {
                                if (process.getProcessID() <= Integer.MAX_VALUE) {
                                    processID = process.getProcessID();
                                }
                            }
                            parametersIn[0] = processID; // _uniqueProcessId
                            parametersIn[1] = (String) udpConnection.getAttributes().get("CreateTime").getContent(); // _createTime
                            parametersIn[2] = (String) localAddress.getAttributes().get("Local").getContent();
                            parametersIn[3] = idAddressFamily; // _idAddressFamily
                            parametersIn[4] = (String) udpConnection.getAttributes().get("Port").getContent(); // _localPort
                            parametersIn[5] = udpConnection.getaS().getVirtualAddressHex(); // _virtualAddress
                            parametersIn[6] = udpConnection.getaS().getPhysicalAddressHex();  // _physicalAddress
                            parametersIn[7] = udpConnection.getaS().getOffsetInFileHex(); // _offset

                            //Ejecuta query
                            respuesta = this._model.insert(query, parametersIn, parametersOut);

                            //Obtiene id de la conexión insertada
                            idConnection = (int) parametersOut[0];

                        }

                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            respuesta = -1;
        }

        entity.setId(idConnection);
        return respuesta;
    }

    @Override
    public ArrayList<EntityConnection> getConnectionList(ConnectionState _connectionState, EntityConnection _entityConnection) {
        ArrayList<EntityConnection> entityConnectionList = null;

        try {
            entityConnectionList = new ArrayList<EntityConnection>();

            String query = "CALL SP_TCP_CONNECTION_GET () ";

            CachedRowSet cachedRowSet = (CachedRowSet) this._model.get(query, null, null);
            while (cachedRowSet.next()) {
                EntityConnection e = new EntityWindows732TCPConnection();

            //******************* VERIFICAR SI HAY QUE INSERTAR MÁS VALORES *************************
                e.setId(cachedRowSet.getLong("idTcpConnection"));

                EntityAddressSpace _as = new EntityAddressSpaceWin32();
                _as.setVirtualAddressHex(cachedRowSet.getString("virtualAddress"));
                _as.setVirtualAddressHex(cachedRowSet.getString("physicalAddress"));
                _as.setVirtualAddressHex(cachedRowSet.getString("offset"));
                e.setaS(_as);

                EntityAddressFamily _af = new EntityAddressFamilyWin32();
                _af.getAttributes().get("IPVersion").setContent(cachedRowSet.getString("addressFamily"));
                _af.setId(cachedRowSet.getLong("idAddressFamily"));
                e.getAttributes().get("AddressFamily").setContent(_af);

                EntityAddressInfo _ai = new EntityAddressInfoWin32();
                _ai.getAttributes().get("Local").setContent(cachedRowSet.getString("localAddress"));
                _ai.getAttributes().get("Remota").setContent(cachedRowSet.getString("remoteAddress"));
                e.getAttributes().get("AddressInfo").setContent(_ai);

                EntityList eList = new EntityListWin32();
                eList.setbLinkHex(cachedRowSet.getString("blink"));
                eList.setbLinkHex(cachedRowSet.getString("flink"));
                e.getAttributes().get("ConnectionLinks").setContent(eList);

                EntityWindowsConnectionState _cs = new EntityWindowsConnectionState();
                _cs.setCurrentState(cachedRowSet.getString("state"));
                e.getAttributes().get("State").setContent(_cs);

                e.getAttributes().get("CreateTime").setContent(cachedRowSet.getString("createTime"));
                e.getAttributes().get("LocalPort").setContent(cachedRowSet.getString("localPort"));
                e.getAttributes().get("RemotePort").setContent(cachedRowSet.getString("remotePort"));

                MapperProcess _mp = new MapperProcessMySQLWin732();
                EntityProcess _ep = new EntityWindows732Process();
                _ep = (EntityWindows732Process) _mp.getProcessById(cachedRowSet.getInt("uniqueProcessId"));
                e.getAttributes().get("Owner").setContent(_ep);

                entityConnectionList.add(e);
            }

            query = "CALL SP_UDP_CONNECTION_GET () ";

            cachedRowSet = (CachedRowSet) this._model.get(query, null, null);
            while (cachedRowSet.next()) {
                EntityConnection e = new EntityWindows732UDPConnection();

            //******************* VERIFICAR SI HAY QUE INSERTAR MÁS VALORES *************************
                e.setId(cachedRowSet.getLong("idUdpConnection"));

                EntityAddressSpace _as = new EntityAddressSpaceWin32();
                _as.setVirtualAddressHex(cachedRowSet.getString("virtualAddress"));
                _as.setVirtualAddressHex(cachedRowSet.getString("physicalAddress"));
                _as.setVirtualAddressHex(cachedRowSet.getString("offset"));
                e.setaS(_as);

                EntityAddressFamily _af = new EntityAddressFamilyWin32();
                _af.getAttributes().get("IPVersion").setContent(cachedRowSet.getString("addressFamily"));
                _af.setId(cachedRowSet.getLong("idAddressFamily"));
                e.getAttributes().get("AddressFamily").setContent(_af);

                EntityLocalAddress _la = new EntityLocalAddressWin32();
                _la.getAttributes().get("Local").setContent(cachedRowSet.getString("localAddress"));
                e.getAttributes().get("LocalAddres").setContent(_la);

                e.getAttributes().get("CreateTime").setContent(cachedRowSet.getString("createTime"));
                e.getAttributes().get("Port").setContent(cachedRowSet.getString("port"));

                MapperProcess _mp = new MapperProcessMySQLWin732();
                EntityProcess _ep = new EntityWindows732Process();
                _ep = (EntityWindows732Process) _mp.getProcessById(cachedRowSet.getInt("uniqueProcessId"));
                e.getAttributes().get("Owner").setContent(_ep);

                entityConnectionList.add(e);
            }
        } catch (Exception ex) {
            this.notifyObservers(ex);
        }

        return entityConnectionList;
    }

    @Override
    public EntityConnection getConnectionByVirtualAddress(String virtualAddress) {
        EntityConnection e = null;

        try {
            /**
             * Pamámetros:
             *
             * @virtualAddress: dirección virtual del proceso. Proceso a buscar
             */
            Object[] parameters = new Object[1];
            parameters[0] = virtualAddress;
            String query = "CALL SP_ENTITY_TCP_CONNECTION_GET_BY_VIRTUAL_ADDRESS (?)";
            CachedRowSet cachedRowSet = (CachedRowSet) this._model.get(query, parameters, null);
            while (cachedRowSet.next()) {
                e = new EntityWindows732TCPConnection();

            //******************* VERIFICAR SI HAY QUE INSERTAR MÁS VALORES *************************
                e.setId(cachedRowSet.getLong("idTcpConnection"));

                EntityAddressSpace _as = new EntityAddressSpaceWin32();
                _as.setVirtualAddressHex(cachedRowSet.getString("virtualAddress"));
                _as.setVirtualAddressHex(cachedRowSet.getString("physicalAddress"));
                _as.setVirtualAddressHex(cachedRowSet.getString("offset"));
                e.setaS(_as);

                EntityAddressFamily _af = new EntityAddressFamilyWin32();
                _af.getAttributes().get("IPVersion").setContent(cachedRowSet.getString("addressFamily"));
                _af.setId(cachedRowSet.getLong("idAddressFamily"));
                e.getAttributes().get("AddressFamily").setContent(_af);

                EntityAddressInfo _ai = new EntityAddressInfoWin32();
                _ai.getAttributes().get("Local").setContent(cachedRowSet.getString("localAddress"));
                _ai.getAttributes().get("Remota").setContent(cachedRowSet.getString("remoteAddress"));
                e.getAttributes().get("AddressInfo").setContent(_ai);

                EntityList eList = new EntityListWin32();
                eList.setbLinkHex(cachedRowSet.getString("blink"));
                eList.setbLinkHex(cachedRowSet.getString("flink"));
                e.getAttributes().get("ConnectionLinks").setContent(eList);

                EntityWindowsConnectionState _cs = new EntityWindowsConnectionState();
                _cs.setCurrentState(cachedRowSet.getString("state"));
                e.getAttributes().get("State").setContent(_cs);

                e.getAttributes().get("CreateTime").setContent(cachedRowSet.getString("createTime"));
                e.getAttributes().get("LocalPort").setContent(cachedRowSet.getString("localPort"));
                e.getAttributes().get("RemotePort").setContent(cachedRowSet.getString("remotPort"));

                MapperProcess _mp = new MapperProcessMySQLWin732();
                EntityProcess _ep = new EntityWindows732Process();
                _ep = (EntityWindows732Process) _mp.getProcessById(cachedRowSet.getInt("uniqueProcessId"));
                e.getAttributes().get("Owner").setContent(_ep);

            }

            if (e == null) {
                query = "CALL SP_ENTITY_UDP_CONNECTION_GET_BY_VIRTUAL_ADDRESS (?)";
                cachedRowSet = (CachedRowSet) this._model.get(query, parameters, null);
                while (cachedRowSet.next()) {
                    e = new EntityWindows732UDPConnection();

                    //******************* VERIFICAR SI HAY QUE INSERTAR MÁS VALORES *************************
                    e.setId(cachedRowSet.getLong("idUdpConnection"));

                    EntityAddressSpace _as = new EntityAddressSpaceWin32();
                    _as.setVirtualAddressHex(cachedRowSet.getString("virtualAddress"));
                    _as.setVirtualAddressHex(cachedRowSet.getString("physicalAddress"));
                    _as.setVirtualAddressHex(cachedRowSet.getString("offset"));
                    e.setaS(_as);

                    EntityAddressFamily _af = new EntityAddressFamilyWin32();
                    _af.getAttributes().get("IPVersion").setContent(cachedRowSet.getString("addressFamily"));
                    _af.setId(cachedRowSet.getLong("idAddressFamily"));
                    e.getAttributes().get("AddressFamily").setContent(_af);

                    EntityLocalAddress _la = new EntityLocalAddressWin32();
                    _la.getAttributes().get("Local").setContent(cachedRowSet.getString("localAddress"));
                    e.getAttributes().get("LocalAddres").setContent(_la);

                    e.getAttributes().get("CreateTime").setContent(cachedRowSet.getString("createTime"));
                    e.getAttributes().get("Port").setContent(cachedRowSet.getInt("port"));

                    MapperProcess _mp = new MapperProcessMySQLWin732();
                    EntityProcess _ep = new EntityWindows732Process();
                    _ep = (EntityWindows732Process) _mp.getProcessById(cachedRowSet.getInt("uniqueProcessId"));
                    e.getAttributes().get("Owner").setContent(_ep);
                }
            }

        } catch (Exception ex) {
            this.notifyObservers(ex);
        }

        return e;
    }

}

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
import analyzer.states.SocketState;
import entities.Entity;
import entities.connection.EntityAddressFamily;
import entities.connection.EntityAddressFamilyWin32;
import entities.connection.EntityConnection;
import entities.connection.EntityLocalAddress;
import entities.connection.EntityLocalAddressWin32;
import entities.connection.EntitySocket;
import entities.connection.EntityWindows732Socket;
import entities.connection.EntityWindows732TCPConnection;
import entities.connection.EntityWindowsConnectionState;
import entities.process.EntityProcess;
import entities.process.EntityWindows732Process;
import entities.translation.EntityAddressSpace;
import entities.translation.EntityAddressSpaceWin32;
import java.util.ArrayList;
import java.util.List;
import javax.sql.rowset.CachedRowSet;
import persistence.database.MapperConnection;
import persistence.database.MapperProcess;

/**
 *
 * @author Gonzalo
 */
public class MapperSocketMySQLWin732 extends MapperSocket {

    public MapperSocketMySQLWin732() {
        this._model = MySQLModel.getInstane();
    }

    @Override
    public List<EntitySocket> getSocketList(SocketState _socketState, EntitySocket _entitySocket) {
        EntitySocket e = null;
        List<EntitySocket> entitySocketList = null;

        try {
            this._model = MySQLModel.getInstane();
            
            entitySocketList = new ArrayList<EntitySocket>();
            Object[] parametersIn = new Object[0];
            String query = "CALL SP_SOCKET_GET ()";
            CachedRowSet cachedRowSet = (CachedRowSet) this._model.get(query, parametersIn, null);
            while (cachedRowSet.next()) {
                e = new EntityWindows732Socket();

                e.setId(cachedRowSet.getLong("idSocket"));

                EntityAddressSpace _as = new EntityAddressSpaceWin32();
                _as.setVirtualAddressHex(cachedRowSet.getString("virtualAddress"));
                _as.setPhysicalAddressHex(cachedRowSet.getString("physicalAddress"));
                _as.setOffsetInFileHex(cachedRowSet.getString("offset"));
                e.setaS(_as);

                EntityAddressFamily _af = new EntityAddressFamilyWin32();
                _af.getAttributes().get("IPVersion").setContent(cachedRowSet.getString("addressFamily"));
                _af.setId(cachedRowSet.getLong("idAddressFamily"));
                e.getAttributes().get("AddressFamily").setContent(_af);

                EntityWindowsConnectionState _cs = new EntityWindowsConnectionState();
                _cs.setCurrentState(cachedRowSet.getString("state"));
                e.getAttributes().get("State").setContent(_cs);

                e.getAttributes().get("CreateTime").setContent(cachedRowSet.getString("createTime"));
                e.getAttributes().get("Port").setContent(cachedRowSet.getString("port"));

                MapperProcess _mp = new MapperProcessMySQLWin732();
                EntityProcess _ep = new EntityWindows732Process();
                _ep = (EntityWindows732Process) _mp.getProcessById(cachedRowSet.getInt("uniqueProcessId"));
                e.getAttributes().get("Owner").setContent(_ep);

                EntityLocalAddress _la = new EntityLocalAddressWin32();
                _la.getAttributes().get("Local").setContent(cachedRowSet.getString("localAddress"));
                e.getAttributes().get("LocalAddress").setContent(_la);

                //obtengo la TCP entity a partir del idTcpConnection almacenado en la tabla socket
                // primero tomo la VA del TCP y luego con el mapper TCP obtengo la entity TCP
                String tcpVirtualAddress = null;
                Object[] parametersTCP = new Object[1];
                parametersTCP[0] = (int) cachedRowSet.getInt("idTcpConnection");
                String queryTCP = "CALL SP_VA_TCP_CONNECTION_GET_BY_ID (?)";
                CachedRowSet cachedRowSetTCP = (CachedRowSet) this._model.get(queryTCP, parametersTCP, null);
                while (cachedRowSetTCP.next()) {
                    tcpVirtualAddress = cachedRowSetTCP.getString("virtualAddress");
                }
                MapperConnection _mc = new MapperConnectionMySQLWin732();
                EntityConnection _ec = new EntityWindows732TCPConnection();
                _ec = (EntityWindows732TCPConnection) _mc.getConnectionByVirtualAddress(tcpVirtualAddress);
                e.getAttributes().get("EndPoint").setContent(_ec);

                entitySocketList.add(e);
            }

        } catch (Exception ex) {
            this.notifyObservers(ex);
        }

        return entitySocketList;
    }

    @Override
    public String getScript(String path) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int persist(Entity entity) {
        int respuesta = 0;
        int idSocket = -1;

        try {
            EntitySocket socket = null;

            if (entity instanceof EntityWindows732Socket) {
                //acá estoy verificando si existe en la DB. Si ya se ha parseado, va a devolver la entity
                if (null == this.getSocketByVirtualAddress((String) entity.getaS().getVirtualAddressHex())) {
                    socket = (EntityWindows732Socket) entity;
                }
            }
            if (socket != null) {
                EntityAddressFamily addressFamily = null;
                EntityLocalAddress localAddress = null;
                EntityProcess process = null;
                EntityWindowsConnectionState socketState = null;
                EntityConnection endpoint = null;

                socket = (EntityWindows732Socket) entity;
                addressFamily = (EntityAddressFamilyWin32) socket.getAttributes().get("AddressFamily").getEntity();
                localAddress = (EntityLocalAddressWin32) socket.getAttributes().get("LocalAddress").getEntity();
                process = (EntityWindows732Process) socket.getAttributes().get("Owner").getEntity();
                socketState = (EntityWindowsConnectionState) socket.getAttributes().get("State").getEntity();
                endpoint = (EntityWindows732TCPConnection) socket.getAttributes().get("EndPoint").getEntity();

                //mapeo el proceso si no está persistido, si está persistido no hace nada
                MapperProcess _mp = new MapperProcessMySQLWin732();
                _mp.persist(process);

                //mapeo la conexión "ENDPOINT" si no está persistido, si está persistido no hace nada
                MapperConnection _mc = new MapperConnectionMySQLWin732();
                _mc.persist(endpoint);

                Object[] parametersIn = new Object[1];
                String query = "CALL SP_ADDRESS_FAMILY_GET (?)";
                parametersIn[0] = (String) addressFamily.getAttributes().get("IPVersion").getContent();
                CachedRowSet cachedRowSet = (CachedRowSet) this._model.get(query, parametersIn, null);
                long idAddressFamily = 0;
                while (cachedRowSet.next()) {
                    idAddressFamily = (long) cachedRowSet.getInt("idAddressFamily");
                }

                parametersIn = new Object[1];
                parametersIn[0] = socketState.getCurrentState();
                query = "CALL SP_CONNECTION_STATE_GET (?)";
                cachedRowSet = (CachedRowSet) this._model.get(query, parametersIn, null);
                int idState = 0;
                while (cachedRowSet.next()) {
                    idState = cachedRowSet.getInt("idConnectionState");
                }

                parametersIn = new Object[10];
                Object[] parametersOut = new Object[1];
                query = "CALL SP_SOCKET_INSERT (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                parametersIn[0] = idState; // _idConnectionState
                long processID = 0;
                if (process.getaS() != null) {
                    if (process.getProcessID() <= Integer.MAX_VALUE) {
                        processID = process.getProcessID();
                    }
                }
                parametersIn[1] = processID; // _uniqueProcessId
                parametersIn[2] = (String) socket.getAttributes().get("CreateTime").getContent(); // _createTime
                parametersIn[3] = (String) localAddress.getAttributes().get("Local").getContent();
                parametersIn[4] = idAddressFamily;
                parametersIn[5] = (long) endpoint.getId();
                parametersIn[6] = (String) socket.getAttributes().get("Port").getContent(); // _Port
                parametersIn[7] = socket.getaS().getVirtualAddressHex(); // _virtualAddress
                parametersIn[8] = socket.getaS().getPhysicalAddressHex();  // _physicalAddress
                parametersIn[9] = socket.getaS().getOffsetInFileHex(); // _offset

                /**
                 * Ejecuta query
                 */
                respuesta = this._model.insert(query, parametersIn, parametersOut);

                /**
                 * Obtiene idProcess del proceso insertado
                 */
                idSocket = (int) parametersOut[0];

            }

        } catch (Exception ex) {
            this.notifyObservers(ex);
            respuesta = -1;
        }

        entity.setId(idSocket);
        return respuesta;
    }

    @Override
    public EntitySocket getSocketByVirtualAddress(String virtualAddress) {
        EntitySocket e = null;

        try {
            /**
             * Pamámetros:
             *
             * @virtualAddress: dirección virtual del proceso. Proceso a buscar
             */
            Object[] parameters = new Object[1];
            parameters[0] = virtualAddress;
            String query = "CALL SP_ENTITY_SOCKET_GET_BY_VIRTUAL_ADDRESS (?)";
            CachedRowSet cachedRowSet = (CachedRowSet) this._model.get(query, parameters, null);
            while (cachedRowSet.next()) {
                e = new EntityWindows732Socket();

                e.setId(cachedRowSet.getLong("idSocket"));

                EntityAddressSpace _as = new EntityAddressSpaceWin32();
                _as.setVirtualAddressHex(cachedRowSet.getString("virtualAddress"));
                _as.setVirtualAddressHex(cachedRowSet.getString("physicalAddress"));
                _as.setVirtualAddressHex(cachedRowSet.getString("offset"));
                e.setaS(_as);

                EntityAddressFamily _af = new EntityAddressFamilyWin32();
                _af.getAttributes().get("IPVersion").setContent(cachedRowSet.getString("addressFamily"));
                _af.setId(cachedRowSet.getLong("idAddressFamily"));
                e.getAttributes().get("AddressFamily").setContent(_af);

                EntityWindowsConnectionState _cs = new EntityWindowsConnectionState();
                _cs.setCurrentState(cachedRowSet.getString("state"));
                e.getAttributes().get("State").setContent(_cs);

                e.getAttributes().get("CreateTime").setContent(cachedRowSet.getString("createTime"));
                e.getAttributes().get("Port").setContent(cachedRowSet.getString("port"));

                MapperProcess _mp = new MapperProcessMySQLWin732();
                EntityProcess _ep = new EntityWindows732Process();
                _ep = (EntityWindows732Process) _mp.getProcessById(cachedRowSet.getInt("uniqueProcessId"));
                e.getAttributes().get("Owner").setContent(_ep);

                EntityLocalAddress _la = new EntityLocalAddressWin32();
                _la.getAttributes().get("Local").setContent(cachedRowSet.getString("localAddress"));
                e.getAttributes().get("LocalAddres").setContent(_la);

                //obtengo la TCP entity a partir del idTcpConnection almacenado en la tabla socket
                // primero tomo la VA del TCP y luego con el mapper TCP obtengo la entity TCP
                String tcpVirtualAddress = null;
                Object[] parametersTCP = new Object[1];
                parametersTCP[1] = (int) cachedRowSet.getInt("idTcpConnection");
                String queryTCP = "CALL SP_VA_TCP_CONNECTION_GET_BY_ID (?)";
                CachedRowSet cachedRowSetTCP = (CachedRowSet) this._model.get(queryTCP, parametersTCP, null);
                while (cachedRowSetTCP.next()) {
                    tcpVirtualAddress = cachedRowSetTCP.getString("virtualAddress");
                }
                MapperConnection _mc = new MapperConnectionMySQLWin732();
                EntityConnection _ec = new EntityWindows732TCPConnection();
                _ec = (EntityWindows732TCPConnection) _mc.getConnectionByVirtualAddress(tcpVirtualAddress);
                e.getAttributes().get("EndPoint").setContent(_ec);

            }

        } catch (Exception ex) {
            this.notifyObservers(ex);
        }

        return e;

    }

}

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
package persistence.analyzer_service;

import analyzer.states.ConnectionState;
import analyzer.states.SocketState;
import entities.connection.EntityConnection;
import entities.connection.EntitySocket;
import entities.process.EntityProcess;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gonzalo
 */
public interface IConnectionService {
    
    public abstract ArrayList<EntityConnection> getAllConnections(EntityConnection entity);

    public abstract List<EntityConnection> getConnectionList(ConnectionState state, EntityConnection entity);

    public abstract EntityConnection getFirstConnection(ConnectionState state, EntityConnection entity);

    public abstract EntityConnection getNextConnection(ConnectionState state, EntityConnection connection);

    public abstract EntityConnection getPrevConnection(ConnectionState state, EntityConnection connection);
    
    public abstract List<EntitySocket> getAllSockets(EntitySocket entity);

    public abstract List<EntitySocket> getSocketList(SocketState state, EntitySocket entity);

    public abstract EntitySocket getFirstSocket(SocketState state, EntitySocket entity);

    public abstract EntitySocket getNextSocket(SocketState state, EntitySocket entity);

    public abstract EntitySocket getPrevSocket(SocketState state, EntitySocket entity);
    
    public abstract ArrayList<EntityConnection> getConnectionListByProcess(ConnectionState state, EntityConnection entity, EntityProcess process);
    
    public abstract ArrayList<EntitySocket> getSocketListByProcess(SocketState state, EntitySocket entity, EntityProcess process);

}

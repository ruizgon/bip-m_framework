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
package analyzer.connections;

import analyzer.Seeker;
import analyzer.interfaces.State;
import analyzer.states.ConnectionState;
import analyzer.states.SocketState;
import entities.Entity;
import entities.connection.EntityConnection;
import entities.connection.EntitySocket;
import entities.process.EntityProcess;
import java.util.ArrayList;
import java.util.List;
import output.OutputManagerAnalyzer;
import output.OutputManagerConsole;
import output.OutputManagerException;

/**
 *
 * @author Gonzalo
 */
public abstract class SeekerConnection extends Seeker implements State {

    private String _connectionListHeadPointer;
    private EntityConnection _firstConnectionLocated;
    private EntitySocket _firstSocketLocated;
    private int _connectionCount;
    private ConnectionState _connectionState;
    private SocketState _socketState;
    private ArrayList<EntityConnection> _connectionList;
    private ArrayList<EntitySocket> _socketList;

    public SeekerConnection() {
        /**
         * Registra observadores
         */
        this.addObserver(OutputManagerException.getInstance());
        this.addObserver(OutputManagerAnalyzer.getInstance());
        this.addObserver(OutputManagerConsole.getInstance());
    }

    /*
     *Constructor para cuando decora a otro Seeker
     */
    public SeekerConnection(Seeker s) {
        this.setSeeker(s);

        /**
         * Registra observadores
         */
        this.addObserver(OutputManagerException.getInstance());
        this.addObserver(OutputManagerAnalyzer.getInstance());
        this.addObserver(OutputManagerConsole.getInstance());
    }

    public String getConnectionListHeadPointer() {
        return _connectionListHeadPointer;
    }

    public void setConnectionListHeadPointer(String _connectionListHeadPointer) {
        this._connectionListHeadPointer = _connectionListHeadPointer;
    }

    public int getConnectionCount() {
        return _connectionCount;
    }

    public EntityConnection getFirstConnectionLocated() {
        return _firstConnectionLocated;
    }

    public void setFirstConnectionLocated(EntityConnection _firstConnectionLocated) {
        this._firstConnectionLocated = _firstConnectionLocated;
    }

    public EntitySocket getFirstSocketLocated() {
        return _firstSocketLocated;
    }

    public void setFirstSocketLocated(EntitySocket _firstSocketLocated) {
        this._firstSocketLocated = _firstSocketLocated;
    }

    public void setConnectionCount(int _connectionCount) {
        this._connectionCount = _connectionCount;
    }

    public ConnectionState getConnectionState() {
        return _connectionState;
    }

    public void setConnectionState(ConnectionState _connectionState) {
        this._connectionState = _connectionState;
    }

    public SocketState getSocketState() {
        return _socketState;
    }

    public void setSocketState(SocketState _socketState) {
        this._socketState = _socketState;
    }

    public ArrayList<EntityConnection> getConnectionList() {
        /**
         * Notifica a observadores
         */
        String log = "Se solicita lista de conexiones del tipo = " + this._connectionState.getStateLabel() + ".";
        this.notifyObservers(log);

        ArrayList<EntityConnection> connectionList = null;

        if (this._connectionList != null) {
            connectionList = this._connectionList;
        } else {
            connectionList = new ArrayList<EntityConnection>();
            List<Entity> entities = this._connectionState.getList();
            log = "Tipo    Local Address    Remote Address    Local Port    Remote Port    State    CreateTime    IPVersion    PID    VA    PA    FileOffset    Blink    Flink";
            this.notifyObservers(log);
            log = "------------------------------------------------------------------------------------------------------------------------------------------------------------";
            this.notifyObservers(log);
            for (Entity e : entities) {
                EntityConnection c = (EntityConnection) e;
                connectionList.add(c);
                this.notifyObservers(c);
            }
            this._connectionList = connectionList;
        }

        /**
         * Notifica a observadores
         */
        int cantConnections = 0;
        if (connectionList != null) {
            cantConnections = connectionList.size();
        }
        log = "Finalizó la búsqueda de conexiones del tipo = " + this._connectionState.getStateLabel() + ".";
        this.notifyObservers(log);
        log = "Cantidad de conexiones encontrados = " + cantConnections + ".";
        this.notifyObservers(log);

        return this._connectionList;
    }

    public void setConnectionList(ArrayList<EntityConnection> _connectionList) {
        this._connectionList = _connectionList;
    }

    public ArrayList<EntitySocket> getSocketList() {
        /**
         * Notifica a observadores
         */
        String log = "Se solicita lista de sockets del tipo = " + this._socketState.getStateLabel() + ".";
        this.notifyObservers(log);
        
        ArrayList<EntitySocket> socketList = null;

        if (this._socketList != null) {
            socketList = this._socketList;
        } else {
            socketList = new ArrayList<EntitySocket>();
            List<Entity> list = this._socketState.getList();
            log = "State    Owner    Local    Port    Create Time    PA    FileOffset";
            this.notifyObservers(log);
            log = "-------------------------------------------------------------------";
            this.notifyObservers(log);
            for (Entity e : list) {
                EntitySocket s = (EntitySocket) e;
                socketList.add(s);
                this.notifyObservers(s);
            }
            this._socketList = socketList;
        }

        /**
         * Notifica a observadores
         */
        int cantConnections = 0;
        if (socketList != null) {
            cantConnections = socketList.size();
        }
        log = "Finalizó la búsqueda de sockets del tipo = " + this._socketState.getStateLabel() + ".";
        this.notifyObservers(log);
        log = "Cantidad de sockets encontrados = " + cantConnections + ".";
        this.notifyObservers(log);
        
        return this._socketList;
    }

    public void setSocketList(ArrayList<EntitySocket> _socketList) {
        this._socketList = _socketList;
    }

    /*
     * Template methods
     */
    public final ArrayList<EntityConnection> seekConnectionList() {
        this._connectionList = new ArrayList<EntityConnection>();
        EntityConnection connection;

        this._firstConnectionLocated = getFirstConnection(this._connectionState);

        if (this._firstConnectionLocated != null) {
            this._connectionList.add(this._firstConnectionLocated);
            connection = getNextConnection(this._firstConnectionLocated, this._connectionState);
            while (connection != null) {
                this._connectionList.add(connection);
                connection = getNextConnection(connection, _connectionState);
            }
        }

        this._connectionCount = this._connectionList.size();

        return this._connectionList;
    }

    /*
     * Template methods
     */
    public void seekSocketList() {

    }

    /*
     * Métodos hook
     */
    /**
     * Si el state is null, significa que busca en todas la conexiones, sin
     * importar el estado
     *
     * @param state
     * @return
     */
    protected abstract EntityConnection getFirstConnection(analyzer.states.ConnectionState state);

    protected abstract EntityConnection getNextConnection(EntityConnection connection, analyzer.states.ConnectionState state);

    protected abstract EntityConnection getPrevConnection(EntityConnection connection, ConnectionState state);

    protected abstract EntitySocket getFirstSocket(SocketState state);

    protected abstract EntitySocket getNextSocket(EntitySocket socket, SocketState state);

    protected abstract EntitySocket getPrevSocket(EntitySocket socket, SocketState state);

    public ArrayList<EntityConnection> getListByProcess(EntityProcess process, analyzer.states.ConnectionState state) {
        ArrayList<EntityConnection> connectionList = null;

        connectionList = this._connectionState.getListByProcess(process);

        return connectionList;
    }

}

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
package entities.process;

import dump.parsers.interfaces.IEntityParserVisitor;
import entities.Entity;
import entities.EntityOwnerProcess;
import entities.connection.EntityConnection;
import entities.connection.EntitySocket;
import entities.lib.EntityLib;
import java.util.ArrayList;
import java.util.Date;
import system.utils.IEntityVisitor;

/**
 *
 * @author Gonzalo
 */
public abstract class EntityProcess extends Entity {

    public static final String _TAG = "Process";
    private long _processID;
    private String _name;
    private String _stateLabel;
    private EntityOwnerProcess _owner;
    private Date _createTime;

    /**
     * TODO Evaluar si esta entidad directamente pasa a ser parte del Map
     */
    private EntityProcess _nextProcess;//En Windows Active Process Link
    private EntityProcess _prevProcess;//
    private ArrayList<EntityLib> _libs;
    private ArrayList<EntityConnection> _connections;
    private ArrayList<EntitySocket> _sockets;

    public long getProcessID() {
        return _processID;
    }

    public void setProcessID(long processID) {
        this._processID = processID;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getStateLabel() {
        return _stateLabel;
    }

    public void setStateLabel(String state) {
        this._stateLabel = state;
    }

    public EntityOwnerProcess getOwner() {
        return _owner;
    }

    public void setOwner(EntityOwnerProcess owner) {
        this._owner = owner;
    }

    public Date getCreateTime() {
        return _createTime;
    }

    public void setCreateTime(Date createTime) {
        this._createTime = createTime;
    }

    public EntityProcess getNextProcess() {
        return _nextProcess;
    }

    public void setNextProcess(EntityProcess nextProcess) {
        this._nextProcess = nextProcess;
    }

    public EntityProcess getPrevProcess() {
        return _prevProcess;
    }

    public void setPrevProcess(EntityProcess prevProcess) {
        this._prevProcess = prevProcess;
    }

    public ArrayList<EntityLib> getLibs() {
        return _libs;
    }

    public void setLibs(ArrayList<EntityLib> libs) {
        this._libs = libs;
    }

    public ArrayList<EntityConnection> getConnections() {
        return _connections;
    }

    public void setConnections(ArrayList<EntityConnection> connections) {
        this._connections = connections;
    }

    public ArrayList<EntitySocket> getSockets() {
        return _sockets;
    }

    public void setSockets(ArrayList<EntitySocket> sockets) {
        this._sockets = sockets;
    }

    @Override
    public void accept(IEntityVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void accept(IEntityParserVisitor visitor) {
        visitor.visit(this);
    }

}

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
package entities.connection;

import dump.parsers.interfaces.IEntityParserVisitor;
import entities.Entity;
import java.util.Date;
import system.utils.IEntityVisitor;

/**
 *
 * @author Gonzalo
 */
public abstract class EntitySocket extends Entity { //Existe una lista simplemente enlazada de sockets
    /*
     *Esta información la posee la estrucutra _ADDRESS_OBJECT
     */

    public static final String _TAG = "Socket";
    private String _stateLabel;
    private int _processID;
    private String offset;
    private int _protocol;
    private int _localAddress;
    private int _localPort;
    private Date _createTime;
    private EntityTCPConnection _TCPConnection;

    public String getStateLabel() {
        return _stateLabel;
    }

    public void setStateLabel(String _stateLabel) {
        this._stateLabel = _stateLabel;
    }

    public int getProcessID() {
        return _processID;
    }

    public void setProcessID(int _processID) {
        this._processID = _processID;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public int getProtocol() {
        return _protocol;
    }

    public void setProtocol(int _protocol) {
        this._protocol = _protocol;
    }

    public int getLocalAddress() {
        return _localAddress;
    }

    public void setLocalAddress(int _localAddress) {
        this._localAddress = _localAddress;
    }

    public int getLocalPort() {
        return _localPort;
    }

    public void setLocalPort(int _localPort) {
        this._localPort = _localPort;
    }

    public Date getCreateTime() {
        return _createTime;
    }

    public void setCreateTime(Date _createTime) {
        this._createTime = _createTime;
    }

    public EntityTCPConnection getTCPConnection() {
        return _TCPConnection;
    }

    public void setTCPConnection(EntityTCPConnection _TCPConnection) {
        this._TCPConnection = _TCPConnection;
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

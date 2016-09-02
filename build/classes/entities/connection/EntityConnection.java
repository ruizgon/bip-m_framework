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
import system.utils.IEntityVisitor;

/**
 *
 * @author Gonzalo
 */
public abstract class EntityConnection extends Entity { //En Windows tiene casi una correspondencia directa con TCB struct
    /*
     *Esta información la posee la estrucutra _TCPT_OBJECT
     */

    public static final String _TAG = "Connection";
    private String _stateLabel;
    private String _offset;
    private int _processID;
    private int _remoteAddress;
    private int _localAddress;
    private int _remotePort;
    private int _localPort;

    public String getStateLabel() {
        return _stateLabel;
    }

    public void setStateLabel(String _stateLabel) {
        this._stateLabel = _stateLabel;
    }

    public String getOffset() {
        return _offset;
    }

    public void setOffset(String _offset) {
        this._offset = _offset;
    }

    public int getProcessID() {
        return _processID;
    }

    public void setProcessID(int _processID) {
        this._processID = _processID;
    }

    public int getRemoteAddress() {
        return _remoteAddress;
    }

    public void setRemoteAddress(int _remoteAddress) {
        this._remoteAddress = _remoteAddress;
    }

    public int getLocalAddress() {
        return _localAddress;
    }

    public void setLocalAddress(int _localAddress) {
        this._localAddress = _localAddress;
    }

    public int getRemotePort() {
        return _remotePort;
    }

    public void setRemotePort(int _remotePort) {
        this._remotePort = _remotePort;
    }

    public int getLocalPort() {
        return _localPort;
    }

    public void setLocalPort(int _localPort) {
        this._localPort = _localPort;
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

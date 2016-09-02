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
import dump.parsers.interfaces.IParserConnection;
import dump.parsers.interfaces.IParserSocket;
import entities.Entity;
import entities.EntityAttribute;
import java.util.HashMap;
import java.util.Map;
import system.utils.IEntityVisitor;

/**
 *
 * @author Gonzalo
 */
public class EntityWindowsConnectionState extends Entity {

    private Map<Integer, String> _states;
    private String _currentState;

    /**
     * Valor leido desde el dump es de tipo int 64
     */
    public EntityWindowsConnectionState() {
        try {
            this._states = new HashMap<Integer, String>();
            this._states.put(0, "Closed");
            this._states.put(1, "Listening");
            this._states.put(2, "SYN Sent");
            this._states.put(3, "SYN Received");
            this._states.put(4, "Established");
            this._states.put(5, "FIN Wait 1");
            this._states.put(6, "FIN Wait 2");
            this._states.put(7, "Close Wait");
            this._states.put(8, "Closing");
            this._states.put(9, "Last ACK");
            this._states.put(12, "Time Wait");
            this._states.put(13, "Delete TCB");
        } catch (Exception ex) {

        }

    }

    public Map<Integer, String> getStates() {
        return _states;
    }

    public void setStates(Map<Integer, String> _states) {
        this._states = _states;
    }

    public String getCurrentState() {
        return _currentState;
    }

    public void setCurrentState(String _currentState) {
        this._currentState = _currentState;
    }

    @Override
    public void getAttributesContent(long position, Map.Entry<String, EntityAttribute> e) {
        if (this.getParser() != null) {
            if (this.getParser() instanceof IParserConnection) {
                IParserConnection parser = (IParserConnection) this.getParser();
                String state = parser.getConnectionStateByOffset(position, e);
                this.setCurrentState(state);
            } else {
                if (this.getParser() instanceof IParserSocket) {
                    IParserSocket parser = (IParserSocket) this.getParser();
                    String state = parser.getConnectionStateByOffset(position, e);
                    this.setCurrentState(state);
                }
            }

        }
    }

    @Override
    public void accept(IEntityVisitor visitor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void accept(IEntityParserVisitor visitor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

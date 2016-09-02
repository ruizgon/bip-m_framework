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

import analyzer.states.ActiveConnectionWin764;
import analyzer.states.ActiveSocketWin764;
import analyzer.states.ConnState;
import analyzer.states.ConnectionState;
import analyzer.states.HiddenConnectionWin764;
import analyzer.states.HiddenSocketWin764;
import analyzer.states.SeekerState;
import analyzer.states.SocketState;
import entities.Entity;
import entities.connection.EntityConnection;
import entities.connection.EntitySocket;
import output.OutputManagerAnalyzer;
import output.OutputManagerException;

/**
 *
 * @author Gonzalo
 */
public class SeekerConnectionWin764 extends SeekerConnection {

    public SeekerConnectionWin764(ConnState state) {
        if (state instanceof ConnectionState) {
            super.setConnectionState((ConnectionState) state);
        } else {
            if (state instanceof SocketState) {
                super.setSocketState((SocketState) state);
            }
        }
    }

    @Override
    protected EntityConnection getFirstConnection(analyzer.states.ConnectionState state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected EntityConnection getNextConnection(EntityConnection connection, analyzer.states.ConnectionState state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected EntityConnection getPrevConnection(EntityConnection connection, ConnectionState state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected EntitySocket getFirstSocket(SocketState state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected EntitySocket getNextSocket(EntitySocket socket, SocketState state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected EntitySocket getPrevSocket(EntitySocket socket, SocketState state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Entity getEntityByAttributeValue(Entity entity, String attribute, Object content) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SeekerState obtainPosibleSeekerState(String modifier) {
        SeekerState state = null;
        SeekerState stateS = null;

        try {

            if (modifier == null) {
                modifier = "";
            }

            switch (modifier) {
                case "active":
                    state = new ActiveConnectionWin764(this.getAService());
                    stateS = new ActiveSocketWin764(this.getAService());
                    break;
                case "hidden":
                    state = new HiddenConnectionWin764(this.getAService());
                    stateS = new HiddenSocketWin764(this.getAService());
                    break;
                default:
                    state = new ActiveConnectionWin764(this.getAService());
                    stateS = new ActiveSocketWin764(this.getAService());
                    break;
            }

            this.setConnectionState((ConnectionState) state);
            this.setSocketState((SocketState) stateS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return state;
    }

}

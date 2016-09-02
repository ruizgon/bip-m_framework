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
package analyzer.states;

import entities.Entity;
import entities.connection.EntitySocket;
import entities.connection.EntityWindows732Socket;
import entities.process.EntityProcess;
import java.util.ArrayList;
import java.util.List;
import persistence.analyzer_service.PersistenceObject;

/**
 *
 * @author Gonzalo
 */
public class ActiveSocketWin732 extends SocketStateWin732 {

    public static final String _STATE_LABEL = "Active";

    public ActiveSocketWin732() {
        this.setEntityTag(_TAG);
        String tag = _TAG + "_" + _STATE_LABEL;
        this.setTag(tag);
        this.setStateLabel(_STATE_LABEL);
    }

    public ActiveSocketWin732(PersistenceObject persistenceObject) {
        this.setEntityTag(_TAG);
        String tag = _TAG + "_" + _STATE_LABEL;
        this.setTag(tag);
        this.setStateLabel(_STATE_LABEL);
        this.setPersistenceObject(persistenceObject);
    }

    @Override
    public List<Entity> getList() {
        ArrayList<Entity> list = new ArrayList<Entity>();

        try {
            List<EntitySocket> socketList = this.getPersistenceObject().getSocketList(this, new EntityWindows732Socket());

            for (EntitySocket s : socketList) {
                list.add(s);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;
    }

    @Override
    public Entity getFirst() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Entity getNext(Entity entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Entity getPrev(Entity entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Entity> getListByProcess(EntityProcess process) {
        ArrayList<Entity> list = new ArrayList<Entity>();

        try {
            ArrayList<EntitySocket> socketList = this.getPersistenceObject().getSocketListByProcess(this, null, process);

            for (EntitySocket s : socketList) {
                list.add(s);
            }
        } catch (Exception ex) {

        }

        return list;
    }

}

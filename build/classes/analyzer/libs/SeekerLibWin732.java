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
package analyzer.libs;

import entities.Entity;
import entities.lib.EntityLib;
import entities.lib.EntityLibWindows732DLL;
import entities.malware.EntitySDE;
import entities.malware.EntitySSDT;
import entities.process.EntityProcess;
import entities.process.EntityWindows732Process;
import java.math.BigInteger;
import persistence.analyzer_service.PersistenceObject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gonzalo
 */
public class SeekerLibWin732 extends SeekerLib {

    public SeekerLibWin732(PersistenceObject persistenceObject) {
        super(persistenceObject);
    }

    @Override
    protected EntityLib getFirstHive() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityLib getNextHive(EntityLib hive) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityLib getPrevHive(EntityLib hive) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Entity getEntityByAttributeValue(Entity entity, String attribute, Object content) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<EntityLib> getList() {
        List<EntityLib> list = new ArrayList<EntityLib>();

        try {
            List<EntityLib> libList = this.getAService().getLibList(this, new EntityLibWindows732DLL());

            for (EntityLib l : libList) {
                list.add(l);
            }
        } catch (Exception ex) {

        }

        return list;
    }

    @Override
    public ArrayList<EntityLib> getListByProcess(EntityProcess process) {

        ArrayList<EntityLib> list = new ArrayList<EntityLib>();

        try {

            ArrayList<EntityLib> libList = this.getAService().getLibListByProcess(this, process, new EntityLibWindows732DLL());

            for (EntityLib l : libList) {
                list.add(l);
            }
        } catch (Exception ex) {

        }

        return list;
    }

    @Override
    public List<EntitySSDT> getSSDTList(BigInteger virtualAddress, List<EntitySSDT> ssdtList) {
        List<EntitySSDT> ssdtListRet = null;

        try {
            ssdtListRet = this.getAService().getSSDTList(this, ssdtList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ssdtListRet;
    }

    @Override
    public ArrayList<EntitySDE> getSDEListBySSDT(BigInteger offset) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntitySSDT getSSDT(BigInteger virtualAddress) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityProcess getProcessInstance(int id) {
        EntityWindows732Process p = new EntityWindows732Process();
        p.setProcessID(id);
        
        return p;
    }

}

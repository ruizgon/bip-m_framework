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
import entities.lib.EntityLibWindows764DLL;
import entities.malware.EntitySDE;
import entities.malware.EntitySSDT;
import entities.malware.EntityWin764SSDT;
import entities.process.EntityProcess;
import entities.process.EntityWindows764Process;
import entities.translation.EntityAddressSpaceWin64;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import persistence.analyzer_service.PersistenceObject;

/**
 *
 * @author Gonzalo
 */
public class SeekerLibWin764 extends SeekerLib {

    public SeekerLibWin764(PersistenceObject persistenceObject) {
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
            List<EntityLib> libList = this.getAService().getLibList(this, new EntityLibWindows764DLL());

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

            ArrayList<EntityLib> libList = this.getAService().getLibListByProcess(this, process, new EntityLibWindows764DLL());

            for (EntityLib l : libList) {
                list.add(l);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;
    }

    @Override
    public List<EntitySSDT> getSSDTList(BigInteger virtualAddress, List<EntitySSDT> ssdtList) {
        List<EntitySSDT> ssdtListRet = null;

        try {
            EntityWin764SSDT ssdt = new EntityWin764SSDT();
            EntityAddressSpaceWin64 aS = new EntityAddressSpaceWin64();
            aS.setVirtualAddress(virtualAddress);
            ssdt.setaS(aS);
            ssdtList.add(ssdt);
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
        EntityWindows764Process p = new EntityWindows764Process();
        p.setProcessID(id);
        
        return p;
    }

}

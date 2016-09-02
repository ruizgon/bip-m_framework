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
package persistence.analyzer_service.memory;

import analyzer.libs.SeekerLib;
import analyzer.malware.SeekerRootkit;
import analyzer.states.ConnectionState;
import analyzer.states.ProcessState;
import analyzer.states.SeekerState;
import analyzer.states.SocketState;
import dump.DumpManager;
import entities.Entity;
import entities.EntityFile;
import entities.connection.EntityConnection;
import entities.connection.EntitySocket;
import entities.hive.EntityHive;
import entities.lib.EntityLib;
import entities.malware.EntityRootkit;
import entities.malware.EntitySSDT;
import entities.process.EntityProcess;
import entities.process.EntityThread;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import persistence.IDataService;
import persistence.analyzer_service.PersistenceObject;
import persistence.dump_service.memory.DContentServiceMemory;
import system.utils.Conversor;

/**
 *
 * @author Gonzalo
 */
public class AServiceMemory extends PersistenceObject implements IDataService { //implementa todas las interfaces que debe implementar PersistenceObject

    private AServiceMemory(DumpManager dumpManager) {
        super(dumpManager);
    }

    public static AServiceMemory getInstance(DumpManager dumpManager) {
        if (_instance == null) {
            _instance = new AServiceMemory(dumpManager);
        }
        return (AServiceMemory) _instance;
    }

    /**
     * Evaluar pasar como parámetro la entity y usar Entity._TAG
     *
     * @param classData
     * @return
     */
    @Override
    public boolean isDataPresent(entities.Entity entity) {
        boolean isDataPresent = false;

        DContentServiceMemory d = DContentServiceMemory.getInstance();
        if (DContentServiceMemory.getInstance().getEntitites().get(entity.getTag()) != null) {
            if (DContentServiceMemory.getInstance().getEntititesParseStatus().get(entity.getTag())) {
                isDataPresent = true;
            }
        }

        return isDataPresent;
    }

    @Override
    public boolean isDataPresent(SeekerState state) {
        boolean isDataPresent = false;

        try {
            String tagSatus = state.getTag();
            DContentServiceMemory d = DContentServiceMemory.getInstance();
            if (DContentServiceMemory.getInstance().getEntitites().get(state.getTag()) != null) {
                if (DContentServiceMemory.getInstance().getEntititesParseStatus().get(state.getTag())) {
                    isDataPresent = true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isDataPresent;
    }

    /**
     * Evaluar pasar la EntityProcess específica como parámetro
     *
     * @return
     */
    @Override
    public ArrayList<EntityProcess> getAllProcesses(EntityProcess entity) {
        ArrayList<EntityProcess> processList = null;
        if (this.isDataPresent(entity)) {
            ArrayList<Entity> list = DContentServiceMemory.getInstance().getEntitites().get("Process");

            processList = new ArrayList<EntityProcess>();
            for (Entity e : list) {
                if (e instanceof EntityProcess) {
                    EntityProcess process = (EntityProcess) e;
                    processList.add(process);
                }
            }
        } else {

        }

        return processList;
    }

    @Override
    public ArrayList<EntityProcess> getProcessList(ProcessState state, EntityProcess entity) {
        ArrayList<EntityProcess> processList = null;

        try {
            processList = new ArrayList<EntityProcess>();
            if (this.isDataPresent(state)) {
                ArrayList<Entity> list = DContentServiceMemory.getInstance().getEntitites().get(entity.getTag());

                for (Entity e : list) {
                    EntityProcess p = (EntityProcess) e;
                    if (state.getStateLabel().equals(p.getStateLabel())) {
                        processList.add(p);
                    }
                }
            } else { //Se lo solicita al DumpManager
                this.getDumpManager().executeLoadProcessByState(state);

                ArrayList<Entity> list = DContentServiceMemory.getInstance().getEntitites().get(entity.getTag());

                if (list != null) {
                    this.setParseStatus(state);
                    for (Entity e : list) {
                        EntityProcess p = (EntityProcess) e;
                        if (state.getStateLabel().equals(p.getStateLabel())) {
                            processList.add(p);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return processList;
    }

    @Override
    public EntityProcess getFirstProcess(ProcessState state, EntityProcess entity) {
        EntityProcess process = (EntityProcess) DContentServiceMemory.getInstance().getEntitites().get("Process").get(0);

        return process;
    }

    @Override
    public EntityProcess getNextProcess(ProcessState state, EntityProcess entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityProcess getPrevProcess(ProcessState state, EntityProcess entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityProcess getSpecificProcessById(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityProcess getSpecificProcessByName(String s, Object par1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<EntityThread> getThreadListByProcess(SeekerState state, EntityProcess process, EntityThread entity) {
        ArrayList<EntityThread> threadList = null;

        try {
            threadList = new ArrayList<EntityThread>();

            /**
             * Seteo parámetros
             */
            Object[] params = new Object[1];
            params[0] = process;
            this.getDumpManager().executeLoadProcessByEntity(entity, params);

            DContentServiceMemory dCont = DContentServiceMemory.getInstance();
            ArrayList<Entity> list = DContentServiceMemory.getInstance().getEntitites().get(entity.getTag());

            for (Entity e : list) {
                EntityThread t = (EntityThread) e;
                threadList.add(t);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return threadList;
    }

    @Override
    public ArrayList<EntityFile> getFileList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityFile getFirstFile() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityFile getNextFile(EntityFile file) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityFile getPrevFile(EntityFile file) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<EntityLib> getLibList(SeekerLib lib, EntityLib entity) {
        ArrayList<EntityLib> libList = null;

        try {
            libList = new ArrayList<EntityLib>();

            //******************************************
            //revisar y verificar si la clase es entity o lib
            //******************************************
            if (this.isDataPresent(entity)) {
                ArrayList<Entity> list = DContentServiceMemory.getInstance().getEntitites().get(entity.getTag());

                for (Entity e : list) {
                    EntityLib l = (EntityLib) e;
                    libList.add(l);
                }
            } else { //Se lo solicita al DumpManager
                //******************************************
                //revisar la solicitud al dump, está aún para procesos, debo revisarlo para Lib
                //******************************************
                this.getDumpManager().executeLoadProcessByTag(entity.getTag());

                DContentServiceMemory dCont = DContentServiceMemory.getInstance();
                ArrayList<Entity> list = DContentServiceMemory.getInstance().getEntitites().get(entity.getTag());

                if (list != null) {
                    this.setParseStatus(entity);
                    for (Entity e : list) {
                        EntityLib l = (EntityLib) e;
                        libList.add(l);
                    }
                }
//            }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return libList;
    }

    @Override
    public EntityLib getFirstLib() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityLib getNextLib(EntityLib lib) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityLib getPrevLib(EntityLib lib) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<EntitySSDT> getSSDTList(SeekerLib lib, List<EntitySSDT> entityList) {
        List<EntitySSDT> ssdtList = null;

        try {
            ssdtList = new ArrayList<EntitySSDT>();
            if (this.isDataPresent(entityList.get(0))) {
                List<Entity> list = DContentServiceMemory.getInstance().getEntitites().get(entityList.get(0).getTag());

                for (Entity e : list) {
                    ssdtList.add((EntitySSDT) e);
                }
            } else { //Se lo solicita al DumpManager
                Object[] params = new Object[2];
                int i = 0;
                for (EntitySSDT s : entityList) {
                    params[i++] = s.getaS().getVirtualAddress();
                }
                this.getDumpManager().executeLoadProcessByEntity(entityList.get(0), params);

                List<Entity> list = DContentServiceMemory.getInstance().getEntitites().get(entityList.get(0).getTag());

                if (list != null) {
                    this.setParseStatus(entityList.get(0));
                    for (Entity e : list) {
                        ssdtList.add((EntitySSDT) e);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ssdtList;
    }

    @Override
    public ArrayList<Entity> getHandleList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Entity getFirstHandle() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Entity getNextHandle(Entity handle) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Entity getPrevHandle(Entity handle) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<EntityHive> getHiveList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityHive getFirstHive() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityHive getNextHive(EntityHive hive) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityHive getPrevHive(EntityHive hive) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<EntityConnection> getAllConnections(EntityConnection entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<EntityConnection> getConnectionList(ConnectionState state, EntityConnection entity) {
        ArrayList<EntityConnection> connectionList = null;

        try {
            connectionList = new ArrayList<EntityConnection>();
            if (this.isDataPresent(state)) {
                ArrayList<Entity> list = DContentServiceMemory.getInstance().getEntitites().get(entity.getTag());

                if (list != null) {
                    for (Entity e : list) {
                        EntityConnection c = (EntityConnection) e;
                        if (state.getStateLabel().equals(c.getStateLabel())) {
                            connectionList.add(c);
                        }
                    }
                }
            } else { //Se lo solicita al DumpManager
                this.getDumpManager().executeLoadProcessByState(state);

                DContentServiceMemory dCont = DContentServiceMemory.getInstance();
                ArrayList<Entity> list = DContentServiceMemory.getInstance().getEntitites().get(entity.getTag());

                if (list != null) {
                    this.setParseStatus(state);
                    for (Entity e : list) {
                        EntityConnection c = (EntityConnection) e;
                        if (state.getStateLabel().equals(c.getStateLabel())) {
                            connectionList.add(c);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return connectionList;
    }

    public void setParseStatus(SeekerState state) {
        DContentServiceMemory.getInstance().getEntititesParseStatus().put(state.getTag(), true);
    }

    public void setParseStatus(Entity entity) {
        DContentServiceMemory.getInstance().getEntititesParseStatus().put(entity.getTag(), true);
    }

    @Override
    public EntityConnection getFirstConnection(ConnectionState state, entities.connection.EntityConnection entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityConnection getNextConnection(ConnectionState state, EntityConnection connection) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityConnection getPrevConnection(ConnectionState state, EntityConnection connection) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<EntityRootkit> getRootkitList(SeekerRootkit seekerRootkit, EntityRootkit entityRootkit) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<EntityRootkit> getRootkitList(analyzer.malware.SeekerRootkit seekerRootkit, entities.malware.EntityRootkit entityRootkit, ArrayList<EntityRootkit> entityRootkitList) {
        return null;
    }

    @Override
    public EntityRootkit getFirstRootkit(entities.malware.EntityRootkit entityRootkit) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityRootkit getNextRootkit(EntityRootkit hive) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityRootkit getPrevRootkit(EntityRootkit hive) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Entity getHookedStructure() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<EntitySocket> getAllSockets(EntitySocket entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<EntitySocket> getSocketList(SocketState state, EntitySocket entity) {
        ArrayList<EntitySocket> socketList = null;

        try {
            socketList = new ArrayList<EntitySocket>();
            if (this.isDataPresent(state)) {
                ArrayList<Entity> list = DContentServiceMemory.getInstance().getEntitites().get(entity.getTag());

                for (Entity e : list) {
                    EntitySocket s = (EntitySocket) e;
                    if (state.getStateLabel().equals(s.getStateLabel())) {
                        socketList.add(s);
                    }
                }
            } else { //Se lo solicita al DumpManager
                this.getDumpManager().executeLoadProcessByState(state);

                DContentServiceMemory dCont = DContentServiceMemory.getInstance();
                ArrayList<Entity> list = DContentServiceMemory.getInstance().getEntitites().get(entity.getTag());

                for (Entity e : list) {
                    EntitySocket s = (EntitySocket) e;
                    if (state.getStateLabel().equals(s.getStateLabel())) {
                        socketList.add(s);
                    }
                }
            }
        } catch (Exception ex) {

        }

        return socketList;
    }

    @Override
    public EntitySocket getFirstSocket(SocketState state, EntitySocket entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntitySocket getNextSocket(SocketState state, EntitySocket entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntitySocket getPrevSocket(SocketState state, EntitySocket entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<EntityConnection> getConnectionListByProcess(ConnectionState state, EntityConnection entity, EntityProcess process) {
        ArrayList<EntityConnection> connectionList = null;

        try {
            connectionList = new ArrayList<EntityConnection>();
            if (this.isDataPresent(state)) {
                ArrayList<Entity> list = DContentServiceMemory.getInstance().getEntitites().get(entity.getTag());

                for (Entity e : list) {
                    EntityConnection c = (EntityConnection) e;
                    EntityProcess p = null;
                    long processID = 0;
                    if (c.getAttributes().get("Owner").getContent() != null) {
                        p = (EntityProcess) c.getAttributes().get("Owner").getContent();
                        if (p.getAttributes().get("UniqueProcessId").getContent() != null) {
                            processID = Conversor.hexToLong(p.getAttributes().get("UniqueProcessId").getContent().toString());
                        }
                    }
                    if (state.getStateLabel().equals(c.getStateLabel()) && processID == process.getProcessID()) {
                        connectionList.add(c);
                    }
                }
            } else { //Se lo solicita al DumpManager
                this.getDumpManager().executeLoadProcessByState(state);

                DContentServiceMemory dCont = DContentServiceMemory.getInstance();
                ArrayList<Entity> list = DContentServiceMemory.getInstance().getEntitites().get(entity.getTag());

                for (Entity e : list) {
                    EntityConnection c = (EntityConnection) e;
                    if (state.getStateLabel().equals(c.getStateLabel()) && Conversor.hexToLong(((EntityProcess) c.getAttributes().get("Owner").getContent()).getAttributes().get("UniqueProcessID").toString()) == process.getProcessID()) {
                        connectionList.add(c);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return connectionList;
    }

    @Override
    public ArrayList<EntitySocket> getSocketListByProcess(SocketState state, EntitySocket entity, EntityProcess process) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<EntityLib> getLibListByProcess(SeekerLib lib, EntityProcess process, EntityLib entity) {
        ArrayList<EntityLib> libList = null;

        try {
            libList = new ArrayList<EntityLib>();

            /**
             * Seteo parámetros
             */
            Object[] params = new Object[1];
            params[0] = process;
            this.getDumpManager().executeLoadProcessByEntity(entity, params);

            DContentServiceMemory dCont = DContentServiceMemory.getInstance();
            ArrayList<Entity> list = DContentServiceMemory.getInstance().getEntitites().get(entity.getTag());

            for (Entity e : list) {
                EntityLib l = (EntityLib) e;
                libList.add(l);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return libList;
    }

    @Override
    public boolean isDataPresent(SeekerState state, Entity entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isDataPresent(String tag) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String loadContent(Map<Object, Entity> entities) {
        String resultado = "No procesado";

        try {
            boolean first = true;

            Map<Object, Entity> map = new TreeMap<Object, Entity>(entities);
            Iterator<Map.Entry<Object, Entity>> i = map.entrySet().iterator();
            int index = 0;
            while (i.hasNext()) {
                Map.Entry<Object, Entity> e = (Map.Entry<Object, Entity>) i.next();
                if (first) {
                    /**
                     * Limpia por si existen residuos
                     */
                    if (DContentServiceMemory.getInstance().getEntitites().get(e.getValue().getTag()) != null) {
                        DContentServiceMemory.getInstance().getEntitites().get(e.getValue().getTag()).clear();
                    } else {
                        ArrayList<Entity> entitiesToAdd = new ArrayList<Entity>();
                        DContentServiceMemory.getInstance().getEntitites().put(e.getValue().getTag(), entitiesToAdd);
                    }
                    first = false;
                }
                e.getValue().setPositionInList(index++);
                DContentServiceMemory.getInstance().getEntitites().get(e.getValue().getTag()).add(e.getValue());
            }
            resultado = "Ok";
        } catch (Exception ex) {
            ex.printStackTrace();
            resultado = "Error";
        }

        return resultado;
    }

    @Override
    public String loadContentEntity(Entity entity) {
        String resultado = "No procesado";

        try {
            if (DContentServiceMemory.getInstance().getEntitites().get(entity.getTag()) == null) {
                ArrayList<Entity> entitiesToAdd = new ArrayList<Entity>();
                DContentServiceMemory.getInstance().getEntitites().put(entity.getTag(), entitiesToAdd);
            }

            DContentServiceMemory.getInstance().getEntitites().get(entity.getTag()).add(entity);
            resultado = "Ok";
        } catch (Exception ex) {
            ex.printStackTrace();
            resultado = "Error";
        }

        return resultado;
    }

    @Override
    public void updateLoadingState(String tag) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

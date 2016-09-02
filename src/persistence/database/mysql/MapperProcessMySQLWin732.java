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
package persistence.database.mysql;

import analyzer.states.ProcessState;
import entities.Entity;
import entities.EntityList;
import entities.EntityListWin32;
import entities.EntityOwner;
import entities.lib.EntityLib;
import entities.process.EntityProcess;
import entities.process.EntityThread;
import entities.process.EntityWin732KPROCESS;
import entities.process.EntityWin732KTHREAD;
import entities.process.EntityWindows732Process;
import entities.process.EntityWindows732Thread;
import entities.translation.EntityAddressSpaceWin32;
import java.util.ArrayList;
import javax.sql.rowset.CachedRowSet;
import persistence.database.MapperProcess;
import system.utils.Conversor;

/**
 *
 * @author juani
 */
public class MapperProcessMySQLWin732 extends MapperProcess {

    public MapperProcessMySQLWin732() {
        this._model = MySQLModel.getInstane();
    }

    @Override
    public EntityProcess getProcessById(int id) {
        EntityProcess e = null;

        try {
            /**
             * Pamámetros:
             *
             * @id: id de proceso.
             * @name: nombre del proceso buscado. Default: NULL
             */
            String query = "CALL SP_ENTITY_PROCESS_GET (?, ?, ?) ";
            Object[] parameters = new Object[3];
            parameters[0] = id;
            parameters[1] = null;
            parameters[2] = null;
            CachedRowSet cachedRowSet = (CachedRowSet) this._model.get(query, parameters, null);
            while (cachedRowSet.next()) {
                e = new EntityWindows732Process();
                e.setProcessID(cachedRowSet.getInt("UniqueProcessId"));
                e.setName(cachedRowSet.getString("imageFileName"));
                e.setId(cachedRowSet.getLong("idProcess"));
                e.getAttributes().get("UniqueProcessId").setContent(cachedRowSet.getLong("UniqueProcessId"));
                e.setProcessID(cachedRowSet.getInt("UniqueProcessId"));
                e.getAttributes().get("InheritedFromUniqueProcessId").setContent(cachedRowSet.getInt("InheritedFromUniqueProcessId"));
                e.setName(cachedRowSet.getString("ImageFileName"));
                e.getAttributes().get("ImageFileName").setContent(cachedRowSet.getString("ImageFileName"));
                e.getAttributes().get("CreateTime").setContent(cachedRowSet.getString("CreateTime"));
                e.getAttributes().get("ExitTime").setContent(cachedRowSet.getString("ExitTime"));
                EntityAddressSpaceWin32 aS = new EntityAddressSpaceWin32();
                aS.setVirtualAddressHex(cachedRowSet.getString("virtualAddress"));
                aS.setPhysicalAddressHex(cachedRowSet.getString("physicalAddress"));
                aS.setOffsetInFileHex(cachedRowSet.getString("offset"));
                e.setaS(aS);
                e.setStateLabel(cachedRowSet.getString("state"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return e;
    }

    @Override
    public EntityProcess getProcessByName(String name) {
        EntityProcess e = null;

        try {
            /**
             * Pamámetros:
             *
             * @id: id de proceso. Default: NULL
             * @name: nombre del proceso buscado
             */
            String query = "CALL SP_ENTITY_PROCESS_GET (?, ?, ?)";
            Object[] parameters = new Object[2];
            parameters[0] = null;
            parameters[1] = name;
            CachedRowSet cachedRowSet = (CachedRowSet) this._model.get(query, parameters, null);
            while (cachedRowSet.next()) {
                e = new EntityWindows732Process();
                e.setProcessID(cachedRowSet.getInt("UniqueProcessId"));
                e.setName(cachedRowSet.getString("imageFileName"));
                e.setId(cachedRowSet.getLong("idProcess"));
                e.getAttributes().get("UniqueProcessId").setContent(cachedRowSet.getLong("UniqueProcessId"));
                e.setProcessID(cachedRowSet.getInt("UniqueProcessId"));
                e.getAttributes().get("InheritedFromUniqueProcessId").setContent(cachedRowSet.getInt("InheritedFromUniqueProcessId"));
                e.setName(cachedRowSet.getString("ImageFileName"));
                e.getAttributes().get("ImageFileName").setContent(cachedRowSet.getString("ImageFileName"));
                e.getAttributes().get("CreateTime").setContent(cachedRowSet.getString("CreateTime"));
                e.getAttributes().get("ExitTime").setContent(cachedRowSet.getString("ExitTime"));
                EntityAddressSpaceWin32 aS = new EntityAddressSpaceWin32();
                aS.setVirtualAddressHex(cachedRowSet.getString("virtualAddress"));
                aS.setPhysicalAddressHex(cachedRowSet.getString("physicalAddress"));
                aS.setOffsetInFileHex(cachedRowSet.getString("offset"));
                e.setaS(aS);
                e.setStateLabel(cachedRowSet.getString("state"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return e;
    }

    @Override
    public EntityProcess getProcessByOwner(EntityOwner owner) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<EntityProcess> getProcessList(ProcessState _processState, EntityProcess _entityProcess) {
        ArrayList<EntityProcess> entityProcessList = null;

        try {
            entityProcessList = new ArrayList<EntityProcess>();
            /**
             * Parámetros:
             *
             * @state: estado del proceso. Default: NULL
             */
            String query = "CALL SP_ENTITY_PROCESS_LIST_GET (?) ";
            Object[] parametersIn = new Object[1];
            parametersIn[0] = _processState.getStateLabel();
            CachedRowSet cachedRowSet = (CachedRowSet) this._model.get(query, parametersIn, null);
            while (cachedRowSet.next()) {
                EntityWindows732Process e = new EntityWindows732Process();
                e.setId(cachedRowSet.getLong("idProcess"));
                e.getAttributes().get("UniqueProcessId").setContent(cachedRowSet.getLong("UniqueProcessId"));
                e.setProcessID(cachedRowSet.getInt("UniqueProcessId"));
                e.getAttributes().get("InheritedFromUniqueProcessId").setContent(cachedRowSet.getInt("InheritedFromUniqueProcessId"));
                e.setName(cachedRowSet.getString("ImageFileName"));
                e.getAttributes().get("ImageFileName").setContent(cachedRowSet.getString("ImageFileName"));
                e.getAttributes().get("CreateTime").setContent(cachedRowSet.getString("CreateTime"));
                e.getAttributes().get("ExitTime").setContent(cachedRowSet.getString("ExitTime"));
                EntityAddressSpaceWin32 aS = new EntityAddressSpaceWin32();
                aS.setVirtualAddressHex(cachedRowSet.getString("virtualAddress"));
                aS.setPhysicalAddressHex(cachedRowSet.getString("physicalAddress"));
                aS.setOffsetInFileHex(cachedRowSet.getString("offset"));
                e.setaS(aS);
                e.setStateLabel(cachedRowSet.getString("state"));
                entityProcessList.add(e);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return entityProcessList;
    }

    @Override
    public String getScript(String path) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int persist(Entity entity) {
        int respuesta = 0;

        try {
            EntityWindows732Process process = null;
            EntityWin732KPROCESS kProcess = null;
            if (entity instanceof EntityWindows732Process) {
                //acá estoy verificando si existe en la DB. Si ya se ha parseado, va a devolver la entity
                if (entity.getaS() != null) {
                    if (null == this.getProcessByVirtualAddress((String) entity.getaS().getVirtualAddressHex())) {
                        process = (EntityWindows732Process) entity;
                        kProcess = (EntityWin732KPROCESS) process.getAttributes().get("KPROCESS").getEntity();
                    }
                }
            }
            if (process != null) {
                Object[] parametersEntity = new Object[1];
                String query = "CALL SP_PROCESS_STATE_GET (?)";
                String state = process.getStateLabel();
                parametersEntity[0] = state;
                CachedRowSet cachedRowSet = (CachedRowSet) this._model.get(query, parametersEntity, null);
                int idState = 0;
                while (cachedRowSet.next()) {
                    idState = cachedRowSet.getInt(1);
                }

                Object[] parametersIn = new Object[14];
                query = "CALL SP_ENTITY_PROCESS_INSERT (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                long processID = process.getProcessID();
                if(processID >Integer.MAX_VALUE){
                    processID = 0;
                }
                parametersIn[0] = processID; //_uniqueProcessID
                parametersIn[1] = null; // _idKprocess
                parametersIn[2] = process.getAttributes().get("ProcessLock").getContent(); // _processLock
                parametersIn[3] = process.getAttributes().get("CreateTime").getContent(); // _createTime
                parametersIn[4] = process.getAttributes().get("ExitTime").getContent(); // _exitTime
                parametersIn[5] = process.getAttributes().get("RundownProtect").getContent(); // _roundownProtect
                long inheritedProcessID = Conversor.hexToLong(process.getAttributes().get("InheritedFromUniqueProcessId").getContent().toString());
                if(inheritedProcessID >Integer.MAX_VALUE){
                    inheritedProcessID = 0;
                }
                parametersIn[6] = inheritedProcessID; // _inheritedFromUniqueProcessId
                parametersIn[7] = process.getAttributes().get("ImageFileName").getContent(); // _imageFileName
                parametersIn[8] = idState; // _idProcessState
                parametersIn[9] = 0;  // _idThread
                parametersIn[10] = 0; // _idPEB
                parametersIn[11] = process.getaS().getVirtualAddressHex(); // _virtualAddress
                parametersIn[12] = process.getaS().getPhysicalAddressHex(); // _physicalAddress
                parametersIn[13] = process.getaS().getOffsetInFileHex(); // _offset

                Object[] parametersOut = new Object[1];
                int idProcess = 0;
                parametersOut[0] = idProcess; // out idProcess
                /**
                 * Ejecuta query
                 */
                respuesta = this._model.insert(query, parametersIn, parametersOut);

                /**
                 * Obtiene idProcess del proceso insertado
                 */
                idProcess = (int) parametersOut[0];
                EntityList activeProcessLink = (EntityListWin32) process.getAttributes().get("ActiveProcessLinks").getEntity();
                parametersIn = new Object[3];
                query = "CALL SP_ACTIVE_PROCESS_LINK_INSERT (?, ?, ?)";
                parametersIn[0] = idProcess;
                parametersIn[1] = activeProcessLink.getfLinkHex();
                parametersIn[2] = activeProcessLink.getbLinkHex();
                respuesta = this._model.insert(query, parametersIn, null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            respuesta = -1;
        }

        return respuesta;
    }

    @Override
    public ArrayList<EntityThread> getThreadsByProcess(EntityProcess process) {
        ArrayList<EntityThread> entityThreadList = null;

        try {
            entityThreadList = new ArrayList<EntityThread>();
            /**
             * Parámetros:
             *
             * @state: estado del proceso. Default: NULL
             */
            String query = "CALL SP_THREAD_LIST_GET (?) ";
            Object[] parametersIn = new Object[1];
            parametersIn[0] = process.getId();
            CachedRowSet cachedRowSet = (CachedRowSet) this._model.get(query, parametersIn, null);
            while (cachedRowSet.next()) {
                EntityWindows732Thread e = new EntityWindows732Thread();
                e.setId(cachedRowSet.getLong("idThread"));
                e.getAttributes().get("UniqueProcess").setContent(cachedRowSet.getLong("uniqueProcess"));
                e.setProcessID(cachedRowSet.getInt("uniqueProcess"));
                e.getAttributes().get("UniqueThread").setContent(cachedRowSet.getInt("uniqueThread"));
                e.getAttributes().get("CreateTime").setContent(cachedRowSet.getString("createTime"));
                e.getAttributes().get("ExitTime").setContent(cachedRowSet.getString("exitTime"));
                EntityWin732KTHREAD kThread = new EntityWin732KTHREAD();
                kThread.setId(cachedRowSet.getLong("idKThread"));
                kThread.getAttributes().get("SSDT").setContent(cachedRowSet.getString("SSDTVA"));
                e.getAttributes().get("KTHREAD").setContent(kThread);
                e.getAttributes().get("KTHREAD").setEntity(kThread);
                EntityAddressSpaceWin32 aS = new EntityAddressSpaceWin32();
                aS.setVirtualAddressHex(cachedRowSet.getString("virtualAddress"));
                aS.setPhysicalAddressHex(cachedRowSet.getString("physicalAddress"));
                aS.setOffsetInFileHex(cachedRowSet.getString("offset"));
                e.setaS(aS);
                entityThreadList.add(e);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return entityThreadList;
    }

    @Override
    public ArrayList<EntityLib> getLibsByProcess(EntityProcess process) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityProcess getProcessByVirtualAddress(String virtualAddress) {
        EntityProcess e = null;

        try {
            /**
             * Pamámetros:
             *
             * @id: id de proceso. Default: NULL
             * @name: nombre del proceso. Default: NULL
             * @virtualAddress: dirección virtual del proceso. Proceso a buscar
             */
            String query = "CALL SP_ENTITY_PROCESS_GET (?, ?, ?)";
            Object[] parameters = new Object[3];
            parameters[0] = null;
            parameters[1] = null;
            parameters[2] = virtualAddress;
            CachedRowSet cachedRowSet = (CachedRowSet) this._model.get(query, parameters, null);
            while (cachedRowSet.next()) {
                e = new EntityWindows732Process();
                e.setProcessID(cachedRowSet.getInt("UniqueProcessId"));
                e.setName(cachedRowSet.getString("imageFileName"));
                e.setId(cachedRowSet.getLong("idProcess"));
                e.getAttributes().get("UniqueProcessId").setContent(cachedRowSet.getLong("UniqueProcessId"));
                e.setProcessID(cachedRowSet.getInt("UniqueProcessId"));
                e.getAttributes().get("InheritedFromUniqueProcessId").setContent(cachedRowSet.getInt("InheritedFromUniqueProcessId"));
                e.setName(cachedRowSet.getString("ImageFileName"));
                e.getAttributes().get("ImageFileName").setContent(cachedRowSet.getString("ImageFileName"));
                e.getAttributes().get("CreateTime").setContent(cachedRowSet.getString("CreateTime"));
                e.getAttributes().get("ExitTime").setContent(cachedRowSet.getString("ExitTime"));
                EntityAddressSpaceWin32 aS = new EntityAddressSpaceWin32();
                aS.setVirtualAddressHex(cachedRowSet.getString("virtualAddress"));
                aS.setPhysicalAddressHex(cachedRowSet.getString("physicalAddress"));
                aS.setOffsetInFileHex(cachedRowSet.getString("offset"));
                e.setaS(aS);
                e.setStateLabel(cachedRowSet.getString("state"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return e;
    }
}

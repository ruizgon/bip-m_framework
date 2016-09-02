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

import entities.Entity;
import entities.EntityList;
import entities.EntityListWin32;
import entities.process.EntityThread;
import entities.process.EntityWin732KTHREAD;
import entities.process.EntityWindows732Thread;
import javax.sql.rowset.CachedRowSet;
import persistence.database.MapperThread;
import system.utils.Conversor;

/**
 *
 * @author Gonzalo
 */
public class MapperThreadMySQLWin732 extends MapperThread {

    @Override
    public String getScript(String path) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int persist(Entity entity) {
        int respuesta = 0;

        try {
            if (null == this.getThreadByVirtualAddress(((String) entity.getaS().getVirtualAddressHex())))  {
                this._model = MySQLModel.getInstane();
                EntityWindows732Thread thread = null;
                EntityWin732KTHREAD kThread = null;
                if (entity instanceof EntityWindows732Thread) {
                    //acá estoy verificando si existe en la DB. Si ya se ha parseado, va a devolver la entity
                    if (null == this.getThreadByVirtualAddress((String) entity.getaS().getVirtualAddressHex())) {
                        thread = (EntityWindows732Thread) entity;
                        kThread = (EntityWin732KTHREAD) thread.getAttributes().get("KTHREAD").getEntity();
                    }
                }
                if (thread != null) {
                    Object[] parametersIn = new Object[2];
                    String query = "CALL SP_ENTITY_KTHREAD_INSERT (?, ?, ?)";
                    parametersIn[0] = 0;
                    parametersIn[1] = kThread.getAttributes().get("SSDT").getContent();
                    Object[] parametersOut = new Object[1];
                    int idKThread = 0;
                    parametersOut[0] = idKThread;
                    respuesta = this._model.insert(query, parametersIn, parametersOut);

                    idKThread = (int) parametersOut[0];

                    parametersIn = new Object[3];
                    query = "CALL SP_ENTITY_PROCESS_GET (?, ?, ?)";
                    parametersIn[0] = thread.getProcessID();
                    parametersIn[1] = null;
                    parametersIn[2] = null;
                    CachedRowSet cachedRowSet = (CachedRowSet) this._model.get(query, parametersIn, null);
                    int idProcess = 0;
                    while (cachedRowSet.next()) {
                        idProcess = cachedRowSet.getInt("idProcess");
                    }

                    parametersIn = new Object[9];
                    query = "CALL SP_ENTITY_THREAD_INSERT (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    parametersIn[0] = idProcess; //_idProcess
                    parametersIn[1] = idKThread; // _idKThread
                    parametersIn[2] = Conversor.hexToInt((String) thread.getAttributes().get("UniqueThread").getContent()); // _uniqueThread
                    parametersIn[3] = Conversor.hexToInt((String) thread.getAttributes().get("UniqueProcess").getContent()); // _uniqueProcess
                    parametersIn[4] = thread.getAttributes().get("CreateTime").getContent(); // _createTime
                    parametersIn[5] = thread.getAttributes().get("ExitTime").getContent(); // _exitTime
                    parametersIn[6] = thread.getaS().getVirtualAddressHex();
                    parametersIn[7] = thread.getaS().getPhysicalAddressHex();
                    parametersIn[8] = thread.getaS().getOffsetInFileHex();

                    parametersOut = new Object[1];
                    int idThread = 0;
                    parametersOut[0] = idThread; // out idThread

                    respuesta = this._model.insert(query, parametersIn, parametersOut);

                    /**
                     * Obtiene idThread del proceso insertado
                     */
                    idThread = (int) parametersOut[0];
                    EntityList threadListEntry = (EntityListWin32) thread.getAttributes().get("ThreadListEntry").getEntity();
                    parametersIn = new Object[3];
                    query = "CALL SP_THREAD_LIST_ENTRY_INSERT (?, ?, ?)";
                    parametersIn[0] = idThread;
                    parametersIn[1] = threadListEntry.getfLinkHex();
                    parametersIn[2] = threadListEntry.getbLinkHex();
                    respuesta = this._model.insert(query, parametersIn, null);

                    /**
                     * Persiste el resto de los threads de la lista
                     * ThreadListEntry
                     */
                    if (thread.getThreadList() != null) {
                        for (EntityThread t : thread.getThreadList()) {
                            this.persist(t);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            respuesta = -1;
        }

        return respuesta;
    }

    public EntityThread getThreadByVirtualAddress(String virtualAddress) {
        return null;
    }

}

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
import entities.EntityListWin64;
import entities.EntityUnicodeStringWin64;
import entities.lib.EntityLib;
import entities.lib.EntityLibWindows764DLL;
import entities.translation.EntityAddressSpace;
import entities.translation.EntityAddressSpaceWin64;
import java.util.ArrayList;
import javax.sql.rowset.CachedRowSet;
import persistence.database.MapperLib;
import system.utils.Conversor;

/**
 *
 * @author juani
 */
public class MapperLibMySQLWin764 extends MapperLib {

    @Override
    public EntityLib getLibByName(String name) {
        EntityLib entityLib = null;

        try {
            this._model = MySQLModel.getInstane();
            /**
             * Pamámetro:
             *
             * @name: nombre de la librería buscada. Default: NULL
             */
            String query = "CALL SP_ENTITY_LIB_GET_BY_NAME (?) ";
            Object[] parameters = new Object[1];
            parameters[0] = name;
            CachedRowSet cachedRowSet = (CachedRowSet) this._model.get(query, parameters, null);
            while (cachedRowSet.next()) {
                entityLib = new EntityLibWindows764DLL();
                entityLib.setId(cachedRowSet.getLong("idEntityLib"));
                entityLib.setFullPath(cachedRowSet.getString("fullDllName"));
                EntityUnicodeStringWin64 baseDllName = new EntityUnicodeStringWin64();
                baseDllName.getAttributes().get("Name").setContent(cachedRowSet.getString("baseDllName"));
                entityLib.getAttributes().get("BaseDllName").setContent(baseDllName);
                entityLib.setName(cachedRowSet.getString("baseDllName"));

                EntityAddressSpace _as = new EntityAddressSpaceWin64();
                _as.setVirtualAddressHex(cachedRowSet.getString("virtualAddress"));
                _as.setPhysicalAddressHex(cachedRowSet.getString("physicalAddress"));
                _as.setOffsetInFileHex(cachedRowSet.getString("offset"));
                entityLib.setaS(_as);

                entityLib.getAttributes().get("sizeOfImage").setContent(cachedRowSet.getInt("sizeOfImage"));
                entityLib.getAttributes().get("fullDllName").setContent(cachedRowSet.getString("fullDllName"));
                entityLib.getAttributes().get("loadCount").setContent(cachedRowSet.getInt("loadCount"));
                entityLib.getAttributes().get("loadTime").setContent(cachedRowSet.getInt("loadTime"));

                Object[] parameters1 = new Object[1];
                parameters1[0] = (int) cachedRowSet.getLong("idEntityLib");
                String query1;
                //InLoadOrderLinks
                query1 = "CALL SP_LIB_LOAD_ORDER_GET (?)";
                CachedRowSet cachedRowSet1 = (CachedRowSet) this._model.get(query1, parameters1, null);
                EntityListWin64 eList1 = new EntityListWin64();
                eList1.setbLinkHex(cachedRowSet1.getString("blink"));
                eList1.setbLinkHex(cachedRowSet1.getString("flink"));
                entityLib.getAttributes().get("InLoadOrderLinks").setContent(eList1);

                //InMemoryOrderLinks
                query1 = "CALL SP_LIB_MEMORY_ORDER_GET (?)";
                CachedRowSet cachedRowSet2 = (CachedRowSet) this._model.get(query1, parameters1, null);
                EntityListWin64 eList2 = new EntityListWin64();
                eList2.setbLinkHex(cachedRowSet2.getString("blink"));
                eList2.setbLinkHex(cachedRowSet2.getString("flink"));
                entityLib.getAttributes().get("InMemoryOrderLinks").setContent(eList2);

                //InInitializationOrderLinks
                query1 = "CALL SP_LIB_INITIALIZATION_ORDER_GET (?)";
                CachedRowSet cachedRowSet3 = (CachedRowSet) this._model.get(query1, parameters1, null);
                EntityListWin64 eList3 = new EntityListWin64();
                eList3.setbLinkHex(cachedRowSet3.getString("blink"));
                eList3.setbLinkHex(cachedRowSet3.getString("flink"));
                entityLib.getAttributes().get("InInitializationOrderLinks").setContent(eList3);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return entityLib;
    }

    @Override
    public ArrayList<EntityLib> getLibList(EntityLib _entityLib) {
        ArrayList<EntityLib> entityLibList = null;

        try {
            this._model = MySQLModel.getInstane();
            entityLibList = new ArrayList<EntityLib>();

            String query = "CALL SP_ENTITY_LIB_LIST_GET () ";

            CachedRowSet cachedRowSet = (CachedRowSet) this._model.get(query, null, null);
            while (cachedRowSet.next()) {
                EntityLibWindows764DLL e = new EntityLibWindows764DLL();

                e.setId(cachedRowSet.getLong("idEntityLib"));
                e.setFullPath(cachedRowSet.getString("fullDllName"));
                e.setName(cachedRowSet.getString("baseDllName"));

                EntityAddressSpace _as = new EntityAddressSpaceWin64();
                _as.setVirtualAddressHex(cachedRowSet.getString("virtualAddress"));
                _as.setPhysicalAddressHex(cachedRowSet.getString("physicalAddress"));
                _as.setOffsetInFileHex(cachedRowSet.getString("offset"));
                e.setaS(_as);

                EntityUnicodeStringWin64 baseDllName = new EntityUnicodeStringWin64();
                baseDllName.getAttributes().get("Name").setContent(cachedRowSet.getString("baseDllName"));
                e.getAttributes().get("BaseDllName").setContent(baseDllName);
                
                e.getAttributes().get("DllBase").setContent(cachedRowSet.getString("dllBase"));
                e.getAttributes().get("SizeOfImage").setContent(cachedRowSet.getString("sizeOfImage"));
                
                EntityUnicodeStringWin64 fullDllName = new EntityUnicodeStringWin64();
                fullDllName.getAttributes().get("Name").setContent(cachedRowSet.getString("fullDllName"));
                e.getAttributes().get("FullDllName").setContent(fullDllName);
                
                e.getAttributes().get("LoadCount").setContent(cachedRowSet.getInt("loadCount"));
                e.getAttributes().get("LoadTime").setContent(cachedRowSet.getString("loadTime"));

                Object[] parameters1 = new Object[1];
                parameters1[0] = (int) cachedRowSet.getLong("idEntityLib");
                String query1;
                //InLoadOrderLinks
                query1 = "CALL SP_LIB_LOAD_ORDER_GET (?)";
                CachedRowSet cachedRowSet1 = (CachedRowSet) this._model.get(query1, parameters1, null);
                while (cachedRowSet1.next()) {
                    EntityListWin64 eList1 = new EntityListWin64();
                    eList1.setbLinkHex(cachedRowSet1.getString("blink"));
                    eList1.setbLinkHex(cachedRowSet1.getString("flink"));
                    e.getAttributes().get("InLoadOrderLinks").setContent(eList1);
                }

                //InMemoryOrderLinks
                query1 = "CALL SP_LIB_MEMORY_ORDER_GET (?)";
                CachedRowSet cachedRowSet2 = (CachedRowSet) this._model.get(query1, parameters1, null);
                while (cachedRowSet2.next()) {
                    EntityListWin64 eList2 = new EntityListWin64();
                    eList2.setbLinkHex(cachedRowSet2.getString("blink"));
                    eList2.setbLinkHex(cachedRowSet2.getString("flink"));
                    e.getAttributes().get("InMemoryOrderLinks").setContent(eList2);
                }

                //InInitializationOrderLinks
                query1 = "CALL SP_LIB_INITIALIZATION_ORDER_GET (?)";
                CachedRowSet cachedRowSet3 = (CachedRowSet) this._model.get(query1, parameters1, null);
                while (cachedRowSet3.next()) {
                    EntityListWin64 eList3 = new EntityListWin64();
                    eList3.setbLinkHex(cachedRowSet3.getString("blink"));
                    eList3.setbLinkHex(cachedRowSet3.getString("flink"));
                    e.getAttributes().get("InInitializationOrderLinks").setContent(eList3);
                }
                entityLibList.add(e);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return entityLibList;
    }

    @Override
    public String getScript(String path) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int persist(Entity entity) {
        int respuesta = 0;
        int idLib = -1;

        try {
            this._model = MySQLModel.getInstane();
            EntityLibWindows764DLL lib = null;
            
             if (null == this.getLibByVirtualAddress((String) entity.getaS().getVirtualAddressHex())) {
                if (entity instanceof EntityLibWindows764DLL) {
                    
                    lib = (EntityLibWindows764DLL) entity;

                    Object[] parametersIn = new Object[9];
                    String query = "CALL SP_ENTITY_LIB_INSERT (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    parametersIn[0] = lib.getAttributes().get("DllBase").getContent();
                    parametersIn[1] = lib.getAttributes().get("SizeOfImage").getContent();

                    EntityUnicodeStringWin64 us = (EntityUnicodeStringWin64) lib.getAttributes().get("FullDllName").getEntity();
                    parametersIn[2] = (String) us.getAttributes().get("Name").getContent();

                    us = (EntityUnicodeStringWin64) lib.getAttributes().get("BaseDllName").getEntity();
                    parametersIn[3] = (String) us.getAttributes().get("Name").getContent();

                    parametersIn[4] = Conversor.hexToInt((String) lib.getAttributes().get("LoadCount").getContent());
                    parametersIn[5] = (String) lib.getAttributes().get("LoadTime").getContent();
                    parametersIn[6] = (String) lib.getaS().getVirtualAddressHex(); // _virtualAddress
                    parametersIn[7] = (String) lib.getaS().getPhysicalAddressHex(); // _physicalAddress
                    parametersIn[8] = (String) lib.getaS().getOffsetInFileHex(); // _offset

                    Object[] parametersOut = new Object[1];
                    parametersOut[0] = idLib; // out idLib
                    /**
                     * Ejecuta query
                     */
                    respuesta = this._model.insert(query, parametersIn, parametersOut);

                    /**
                     * Obtiene idLib del módulo insertado
                     */
                    idLib = (int) parametersOut[0];

                    //ACÁ AGREGA LA INSERCIÓN PARA CADA UNA DE LAS 3 LISTAS DE MÓDULOS
                    query = "CALL SP_LIB_IN_LOAD_ORDER_LINKS_INSERT (?, ?, ?)";
                    parametersIn = new Object[3];
                    parametersIn[0] = idLib;
                    EntityList eList = (EntityListWin64) lib.getAttributes().get("InLoadOrderLinks").getEntity();
                    parametersIn[1] = (String) eList.getfLinkHex();
                    parametersIn[2] = (String) eList.getbLinkHex();
                    this._model.insert(query, parametersIn, null);

                    query = "CALL SP_LIB_IN_MEMORY_ORDER_LINKS_INSERT (?, ?, ?)";
                    parametersIn = new Object[3];
                    parametersIn[0] = idLib;
                    eList = (EntityListWin64) lib.getAttributes().get("InMemoryOrderLinks").getEntity();
                    parametersIn[1] = (String) eList.getfLinkHex();
                    parametersIn[2] = (String) eList.getbLinkHex();
                    this._model.insert(query, parametersIn, null);

                    query = "CALL SP_LIB_IN_INITIALIZATION_ORDER_LINKS_INSERT (?, ?, ?)";
                    parametersIn = new Object[3];
                    parametersIn[0] = idLib;
                    eList = (EntityListWin64) lib.getAttributes().get("InInitializationOrderLinks").getEntity();
                    parametersIn[1] = (String) eList.getfLinkHex();
                    parametersIn[2] = (String) eList.getbLinkHex();
                    this._model.insert(query, parametersIn, null);
                }
             }

        } catch (Exception ex) {
            ex.printStackTrace();
            respuesta = -1;
        }
        entity.setId(idLib);
        return respuesta;
    }

    @Override
    public EntityLib getLibByVirtualAddress(String _virtualAddress) {
        EntityLib entityLib = null;

        try {
            this._model = MySQLModel.getInstane();
            /**
             * Pamámetro:
             *
             * @name: nombre de la librería buscada. Default: NULL
             */
            String query = "CALL SP_ENTITY_LIB_GET_BY_VIRTUAL_ADDRESS (?) ";
            Object[] parameters = new Object[1];
            parameters[0] = _virtualAddress;
            CachedRowSet cachedRowSet = (CachedRowSet) this._model.get(query, parameters, null);
            while (cachedRowSet.next()) {
                entityLib = new EntityLibWindows764DLL();
                entityLib.setId(cachedRowSet.getLong("idEntityLib"));
                entityLib.setFullPath(cachedRowSet.getString("fullDllName"));
                entityLib.setName(cachedRowSet.getString("baseDllName"));

                EntityAddressSpace _as = new EntityAddressSpaceWin64();
                _as.setVirtualAddressHex(cachedRowSet.getString("virtualAddress"));
                _as.setPhysicalAddressHex(cachedRowSet.getString("physicalAddress"));
                _as.setOffsetInFileHex(cachedRowSet.getString("offset"));
                entityLib.setaS(_as);

                EntityUnicodeStringWin64 baseDllName = new EntityUnicodeStringWin64();
                baseDllName.getAttributes().get("Name").setContent(cachedRowSet.getString("baseDllName"));
                entityLib.getAttributes().get("BaseDllName").setContent(baseDllName);
                
                entityLib.getAttributes().get("sizeOfImage").setContent(cachedRowSet.getInt("sizeOfImage"));
                entityLib.getAttributes().get("fullDllName").setContent(cachedRowSet.getString("fullDllName"));
                entityLib.getAttributes().get("loadCount").setContent(cachedRowSet.getInt("loadCount"));
                entityLib.getAttributes().get("loadTime").setContent(cachedRowSet.getInt("loadTime"));

                Object[] parameters1 = new Object[1];
                parameters1[0] = (int) cachedRowSet.getLong("idEntityLib");
                String query1;
                //InLoadOrderLinks
                query1 = "CALL SP_LIB_LOAD_ORDER_GET (?)";
                CachedRowSet cachedRowSet1 = (CachedRowSet) this._model.get(query1, parameters1, null);
                EntityListWin64 eList1 = new EntityListWin64();
                eList1.setbLinkHex(cachedRowSet1.getString("blink"));
                eList1.setbLinkHex(cachedRowSet1.getString("flink"));
                entityLib.getAttributes().get("InLoadOrderLinks").setContent(eList1);

                //InMemoryOrderLinks
                query1 = "CALL SP_LIB_MEMORY_ORDER_GET (?)";
                CachedRowSet cachedRowSet2 = (CachedRowSet) this._model.get(query1, parameters1, null);
                EntityListWin64 eList2 = new EntityListWin64();
                eList2.setbLinkHex(cachedRowSet2.getString("blink"));
                eList2.setbLinkHex(cachedRowSet2.getString("flink"));
                entityLib.getAttributes().get("InMemoryOrderLinks").setContent(eList2);

                //InInitializationOrderLinks
                query1 = "CALL SP_LIB_INITIALIZATION_ORDER_GET (?)";
                CachedRowSet cachedRowSet3 = (CachedRowSet) this._model.get(query1, parameters1, null);
                EntityListWin64 eList3 = new EntityListWin64();
                eList3.setbLinkHex(cachedRowSet3.getString("blink"));
                eList3.setbLinkHex(cachedRowSet3.getString("flink"));
                entityLib.getAttributes().get("InInitializationOrderLinks").setContent(eList3);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return entityLib;
    }
}

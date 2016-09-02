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
import entities.malware.EntitySDE;
import entities.malware.EntitySSDT;
import entities.malware.EntityWin764SDE;
import entities.malware.EntityWin764SSDT;
import entities.malware.EntityWindowsFunction;
import entities.malware.EntityWindowsFunctionOperation;
import entities.translation.EntityAddressSpace;
import entities.translation.EntityAddressSpaceWin64;
import java.util.ArrayList;
import java.util.List;
import javax.sql.rowset.CachedRowSet;
import persistence.database.MapperSSDT;
import system.utils.Conversor;

/**
 *
 * @author Gonzalo
 */
public class MapperSSDTMySQLWin764 extends MapperSSDT {

    @Override
    public String getScript(String path) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int persist(Entity entity) {
        int respuesta = 0;

        try {
            if (null == this.getSSDTByVirtualAddress((String) entity.getaS().getVirtualAddressHex())) {
                this._model = MySQLModel.getInstane();
                EntityWin764SSDT ssdt = (EntityWin764SSDT) entity;

                Object[] parametersIn = new Object[3];
                String query = "CALL SP_ENTITY_SSDT_INSERT (?, ?, ?, ?)";
                parametersIn[0] = ssdt.getDescription();
                parametersIn[1] = ssdt.getaS().getVirtualAddressHex();
                parametersIn[2] = ssdt.getaS().getPhysicalAddressHex();

                Object[] parametersOut = new Object[1];
                int idSSDT = 0;
                parametersOut[0] = idSSDT; // out idSSDT

                /**
                 * Ejecuta query
                 */
                respuesta = this._model.insert(query, parametersIn, parametersOut);

                /**
                 * Obtiene idSSDT insertado
                 */
                idSSDT = (int) parametersOut[0];

                for (EntitySDE sde : ssdt.getEntryList()) {
                    if (sde.isValid()) {
                        parametersIn = new Object[5];
                        query = "CALL SP_ENTITY_SDE_INSERT (?, ?, ?, ?, ?, ?)";
                        parametersIn[0] = idSSDT;
                        parametersIn[1] = sde.getAttributes().get("functionTableVA").getContent();
                        parametersIn[2] = Conversor.hexToInt((String) sde.getAttributes().get("counterBaseTable").getContent());
                        parametersIn[3] = Conversor.hexToInt((String) sde.getAttributes().get("serviceLimit").getContent());
                        parametersIn[4] = sde.getAttributes().get("argumentTable").getContent();

                        parametersOut = new Object[1];
                        int idSDE = 0;
                        parametersOut[0] = idSDE; // out idSDE
                        respuesta = this._model.insert(query, parametersIn, parametersOut);

                        idSDE = (int) parametersOut[0];

                        for (EntityWindowsFunction f : sde.getFunctionArray()) {
                            parametersIn = new Object[3];
                            query = "CALL SP_FUNCTION_INSERT (?, ?, ?, ?)";
                            parametersIn[0] = f.getName();
                            parametersIn[1] = f.getFunctionVAHex();
                            parametersIn[2] = idSDE;

                            parametersOut = new Object[1];
                            int idFunction = 0;
                            parametersOut[0] = idFunction; // out idFunction
                            respuesta = this._model.insert(query, parametersIn, parametersOut);

                            idFunction = (int) parametersOut[0];

                            for (EntityWindowsFunctionOperation op : f.getOperations()) {
                                parametersIn = new Object[5];
                                query = "CALL SP_FUNCTION_OPERATION_INSERT (?, ?, ?, ?, ?, ?)";
                                parametersIn[0] = idFunction;
                                parametersIn[1] = op.getOffset();
                                parametersIn[2] = (String) op.getAttributes().get("operation").getContent();
                                parametersIn[3] = Conversor.hexToInt((String) op.getAttributes().get("desp").getContent());
                                parametersIn[4] = (String) op.getAttributes().get("value").getContent();

                                parametersOut = new Object[1];
                                int idOperation = 0;
                                parametersOut[0] = idOperation; // out idOperation
                                respuesta = this._model.insert(query, parametersIn, parametersOut);

                                idOperation = (int) parametersOut[0];
                            }
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

    @Override
    public List<EntitySSDT> getSSDTList(EntitySSDT _entitySSDT) {
        List<EntitySSDT> entitySSDTList = null;

        try {
            this._model = MySQLModel.getInstane();
            entitySSDTList = new ArrayList<EntitySSDT>();

            Object[] parametersIn = new Object[1];
            String query = "CALL SP_ENTITY_SSDT_GET (?) ";
            parametersIn[0] = null;

            CachedRowSet cachedRowSet = (CachedRowSet) this._model.get(query, parametersIn, null);
            while (cachedRowSet.next()) {
                EntityWin764SSDT e = new EntityWin764SSDT();

                e.setId(cachedRowSet.getLong("idSSDT"));
                e.setDescription(cachedRowSet.getString("descriptionSSDT"));

                EntityAddressSpace aS = new EntityAddressSpaceWin64();
                aS.setVirtualAddressHex(cachedRowSet.getString("virtualAddress"));
                aS.setPhysicalAddressHex(cachedRowSet.getString("physicalAddress"));
                e.setaS(aS);

                entitySSDTList.add(e);
            }

            for (EntitySSDT s : entitySSDTList) {
                parametersIn = new Object[2];
                query = "CALL SP_ENTITY_SDE_GET (?,?)";
                parametersIn[0] = null;
                parametersIn[1] = s.getId();

                cachedRowSet = (CachedRowSet) this._model.get(query, parametersIn, null);

                EntitySDE[] entryList = new EntitySDE[4];
                int i = 0;
                while (cachedRowSet.next()) {
                    EntityWin764SDE sde = new EntityWin764SDE();

                    sde.setId(cachedRowSet.getInt("idSDE"));
                    sde.setFunctionTableVAHex(cachedRowSet.getString("functionTableVA"));
                    sde.getAttributes().get("counterBaseTable").setContent(cachedRowSet.getInt("counterBaseTable"));
                    sde.getAttributes().get("serviceLimit").setContent(cachedRowSet.getInt("serviceLimit"));
                    sde.setServiceLimit((int) sde.getAttributes().get("serviceLimit").getContent());
                    sde.getAttributes().get("argumentTable").setContent(cachedRowSet.getString("argumentTable"));
                    entryList[i++] = sde;
                }
                s.setEntryList(entryList);

                for (EntitySDE sde : s.getEntryList()) {
                    if (sde != null) {
                        parametersIn = new Object[2];
                        query = "CALL SP_FUNCTION_GET (?,?)";
                        parametersIn[0] = null;
                        parametersIn[1] = sde.getId();

                        cachedRowSet = (CachedRowSet) this._model.get(query, parametersIn, null);

                        EntityWindowsFunction[] functionList = new EntityWindowsFunction[sde.getServiceLimit()];
                        int j = 0;
                        while (cachedRowSet.next()) {
                            EntityWindowsFunction function = new EntityWindowsFunction();
                            function.setId(cachedRowSet.getInt("idSDE"));
                            function.setName(cachedRowSet.getString("name"));
                            function.setFunctionVAHex(cachedRowSet.getString("virtualAddress"));

                            functionList[j++] = function;
                        }
                        sde.setFunctionArray(functionList);

                        for (EntityWindowsFunction f : sde.getFunctionArray()) {
                            parametersIn = new Object[2];
                            query = "CALL SP_FUNCTION_OPERATION_GET (?,?)";
                            parametersIn[0] = null;
                            parametersIn[1] = f.getId();

                            cachedRowSet = (CachedRowSet) this._model.get(query, parametersIn, null);

                            cachedRowSet.last();
                            EntityWindowsFunctionOperation[] operationList = new EntityWindowsFunctionOperation[cachedRowSet.getRow()];
                            cachedRowSet.first();
                            int k = 0;
                            while (cachedRowSet.next()) {
                                EntityWindowsFunctionOperation operation = new EntityWindowsFunctionOperation();
                                operation.setId(cachedRowSet.getInt("id"));
                                operation.setOffsetHex(cachedRowSet.getString("offset"));
                                operation.getAttributes().get("operation").setContent(cachedRowSet.getString("operation"));
                                operation.getAttributes().get("desp").setContent(cachedRowSet.getString("desplazamiento"));
                                operation.getAttributes().get("value").setContent(cachedRowSet.getString("value"));

                                operationList[k++] = operation;
                            }
                            f.setOperations(operationList);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return entitySSDTList;
    }

    @Override
    public EntitySSDT getSSDTByVirtualAddress(String virtualAddress) {
        EntitySSDT entitySSDT = null;

        try {
            this._model = MySQLModel.getInstane();
            /**
             * Pamámetro:
             *
             * @name: nombre de la librería buscada. Default: NULL
             */
            String query = "CALL SP_ENTITY_SSDT_GET_BY_VIRTUAL_ADDRESS (?) ";
            Object[] parameters = new Object[1];
            parameters[0] = virtualAddress;
            CachedRowSet cachedRowSet = (CachedRowSet) this._model.get(query, parameters, null);
            if (cachedRowSet != null) {
                while (cachedRowSet.next()) {
                    entitySSDT = new EntityWin764SSDT();
                    entitySSDT.setId(cachedRowSet.getLong("idSSDT"));
                    entitySSDT.setDescription(cachedRowSet.getString("descriptionSSDT"));

                    EntityAddressSpace _as = new EntityAddressSpaceWin64();
                    _as.setVirtualAddressHex(cachedRowSet.getString("virtualAddress"));
                    _as.setPhysicalAddressHex(cachedRowSet.getString("physicalAddress"));
                    entitySSDT.setaS(_as);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return entitySSDT;
    }
}

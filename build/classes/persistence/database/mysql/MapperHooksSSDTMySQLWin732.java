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
import entities.malware.EntityDuplicationSSDTHook;
import entities.malware.EntityHook;
import entities.malware.EntityInLineSSDTHook;
import entities.malware.EntityPointerReplacementSSDTHook;
import entities.malware.EntityRootkit;
import entities.malware.EntityWin732SSDTRootkit;
import entities.malware.EntityWindowsFunction;
import entities.malware.EntityWindowsFunctionOperation;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.sql.rowset.CachedRowSet;
import persistence.database.MapperSSDTRootkit;
import system.utils.Conversor;

/**
 *
 * @author Gonzalo
 */
public class MapperHooksSSDTMySQLWin732 extends MapperSSDTRootkit {

    @Override
    public List<EntityRootkit> getRootkitList(EntityRootkit _entityProcess) {
        List<EntityRootkit> entityRootkitList = null;

        try {
            this._model = MySQLModel.getInstane();
            entityRootkitList = new ArrayList<EntityRootkit>();
            /**
             * Parámetros:
             *
             * @state: estado del proceso. Default: NULL
             */
            String query = "CALL SP_MALWARE_LIST_GET () ";
            Object[] parametersIn = new Object[0];
            CachedRowSet cachedRowSet = (CachedRowSet) this._model.get(query, parametersIn, null);
            while (cachedRowSet.next()) {
                EntityWin732SSDTRootkit e = new EntityWin732SSDTRootkit();
                e.setId(cachedRowSet.getInt("id"));
                e.setMalwareName(cachedRowSet.getString("malware"));

                entityRootkitList.add(e);
            }

            for (EntityRootkit r : entityRootkitList) {
                query = "CALL SP_SSDT_HOOK_LIST_GET (?) ";
                parametersIn = new Object[1];
                parametersIn[0] = r.getId();
                cachedRowSet = (CachedRowSet) this._model.get(query, parametersIn, null);
                List<EntityHook> hookList = new ArrayList<EntityHook>();
                while (cachedRowSet.next()) {
                    EntityHook h = null;
                    String hookType = cachedRowSet.getString("SSDT_HOOK_TYPE");

                    switch (hookType) {
                        case "PointerReplacement":
                            h = new EntityPointerReplacementSSDTHook();
                            break;
                        case "TableDuplication":
                            h = new EntityDuplicationSSDTHook();
                            break;
                        case "InLineHook":
                            h = new EntityInLineSSDTHook();
                            break;
                        default:
                            break;
                    }
                    if (h != null) {
                        h.setId(cachedRowSet.getInt("id"));
                        h.setHookType(cachedRowSet.getString("SSDT_HOOK"));
                        h.setHookDescription(cachedRowSet.getString("SSDT_HOOK_TYPE"));
                        hookList.add(h);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            entityRootkitList = null;
        }

        return entityRootkitList;
    }

    @Override
    public String getScript(String path) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int persist(Entity entity) {
        int respuesta = 0;

        try {
            this._model = MySQLModel.getInstane();
            EntityWin732SSDTRootkit rootkit = null;
            if (entity instanceof EntityWin732SSDTRootkit) {
                rootkit = (EntityWin732SSDTRootkit) entity;

                String query = "";
                if (rootkit != null) {
                    Object[] parametersIn = new Object[1];
                    query = "CALL SP_MALWARE_TYPE_GET (?)";
                    parametersIn[0] = EntityWin732SSDTRootkit._TAG; //_uniqueProcessID

                    CachedRowSet cachedRowSet = (CachedRowSet) this._model.get(query, parametersIn, null);
                    int idMalwareType = 0;
                    while (cachedRowSet.next()) {
                        idMalwareType = cachedRowSet.getInt(1);
                    }

                    parametersIn = new Object[1];
                    query = "CALL SP_MALWARE_SUBTYPE_GET (?)";
                    parametersIn[0] = EntityWin732SSDTRootkit._MALWARE_TAG; //_uniqueProcessID

                    cachedRowSet = (CachedRowSet) this._model.get(query, parametersIn, null);
                    int idMalwareSubType = 0;
                    while (cachedRowSet.next()) {
                        idMalwareSubType = cachedRowSet.getInt(1);
                    }

                    parametersIn = new Object[5];
                    query = "CALL SP_MALWARE_INSERT (?, ?, ?, ?, ?, ?)";
                    parametersIn[0] = EntityWin732SSDTRootkit._NAME;
                    parametersIn[1] = EntityWin732SSDTRootkit._DESCRIPTION;
                    parametersIn[2] = idMalwareType;
                    parametersIn[3] = idMalwareSubType;
                    Calendar calendar = Calendar.getInstance();
                    parametersIn[4] = new Timestamp(calendar.getTimeInMillis()); //
                    int idMalware = 0;
                    Object[] parametersOut = new Object[1];
                    parametersOut[0] = idMalware;

                    /**
                     * Ejecuta query
                     */
                    respuesta = this._model.insert(query, parametersIn, parametersOut);
                    idMalware = (int) parametersOut[0];

                    for (EntityHook h : rootkit.getHookList()) {
                        /**
                         * Obtiene id de tipo de hook del proceso insertado
                         */
                        parametersIn = new Object[1];
                        query = "CALL SP_SSDT_HOOK_TYPE_GET (?)";
                        parametersIn[0] = h.getHookType();
                        cachedRowSet = (CachedRowSet) this._model.get(query, parametersIn, null);
                        int idHookType = 0;
                        while (cachedRowSet.next()) {
                            idHookType = cachedRowSet.getInt(1);
                        }

                        parametersIn = new Object[4];
                        query = "CALL SP_SSDT_HOOK_INSERT (?, ?, ?, ?, ?)";
                        parametersIn[0] = idMalware;
                        parametersIn[1] = h.getHookedSSDT().getaS().getVirtualAddressHex();
                        parametersIn[2] = idHookType;
                        parametersIn[3] = h.getHookDescription();
                        parametersOut = new Object[1];
                        int idHook = 0;
                        parametersOut[0] = idHook;
                        respuesta = this._model.insert(query, parametersIn, parametersOut);

                        idHook = (int) parametersOut[0];

                        if (h instanceof EntityPointerReplacementSSDTHook || h instanceof EntityInLineSSDTHook) {
                            if (h instanceof EntityPointerReplacementSSDTHook) {
                                EntityPointerReplacementSSDTHook hPR = (EntityPointerReplacementSSDTHook) h;
                                List<EntityWindowsFunction> functions = hPR.getHookedFunctionList();
                                for (EntityWindowsFunction f : functions) {
                                    parametersIn = new Object[1];
                                    query = "CALL SP_FUNCTION_GET (?)";
                                    parametersIn[0] = f.getFunctionVAHex();
                                    int idFunction = 0;
                                    cachedRowSet = (CachedRowSet) this._model.get(query, parametersIn, null);
                                    while (cachedRowSet.next()) {
                                        idFunction = cachedRowSet.getInt(1);
                                    }

                                    parametersIn = new Object[2];
                                    query = "CALL SP_HOOKED_FUNCTION_INSERT (?, ?)";
                                    parametersIn[0] = idHook;
                                    parametersIn[1] = idFunction;

                                    respuesta = this._model.insert(query, parametersIn, null);
                                }
                            } else if (h instanceof EntityInLineSSDTHook) {
                                EntityInLineSSDTHook hIL = (EntityInLineSSDTHook) h;
                                List<EntityWindowsFunction> functions = hIL.getHookedFunctionList();
                                for (EntityWindowsFunction f : functions) {
                                    parametersIn = new Object[1];
                                    query = "CALL SP_FUNCTION_GET (?)";
                                    parametersIn[0] = f.getFunctionVAHex();
                                    int idFunction = 0;
                                    cachedRowSet = (CachedRowSet) this._model.get(query, parametersIn, null);
                                    while (cachedRowSet.next()) {
                                        idFunction = cachedRowSet.getInt(1);
                                    }

                                    parametersIn = new Object[2];
                                    query = "CALL SP_HOOKED_FUNCTION_INSERT (?, ?)";
                                    parametersIn[0] = idHook;
                                    parametersIn[1] = idFunction;

                                    respuesta = this._model.insert(query, parametersIn, null);

                                    EntityWindowsFunctionOperation[] operations = f.getOperations();
                                    for (EntityWindowsFunctionOperation op : operations) {
                                        if (op.isHooked()) {
                                            parametersIn = new Object[1];
                                            query = "CALL SP_FUNCTION_OPERATION_GET (?)";
                                            int idOperation = 0;
                                            parametersIn[0] = Conversor.longToHexString(op.getOffset());
                                            cachedRowSet = (CachedRowSet) this._model.get(query, parametersIn, null);
                                            while (cachedRowSet.next()) {
                                                idOperation = cachedRowSet.getInt(1);
                                            }

                                            parametersIn = new Object[3];
                                            query = "CALL SP_HOOKED_FUNCTION_OPERATION_INSERT (?, ?, ?)";
                                            parametersIn[0] = idHook;
                                            parametersIn[1] = idFunction;
                                            parametersIn[2] = idOperation;

                                        }
                                    }
                                }
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

}

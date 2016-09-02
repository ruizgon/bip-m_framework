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
package persistence.dump_service.memory;

import dump.formats.DumpFormat;
import entities.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import persistence.dump_service.PersistenceDumpObject;
import system.utils.ConfigurationManager;

/**
 *
 * @author Gonzalo
 */
public class DContentServiceMemory extends PersistenceDumpObject {

    private static DContentServiceMemory _instance;
    private Map<String, ArrayList<Entity>> _entitites;
    private Map<String, Boolean> _entititesParseStatus;

    private DContentServiceMemory() {
        super();
        this._entitites = new HashMap<String, ArrayList<Entity>>();
        /*ArrayList<Entity> processes = new ArrayList<Entity>();
         this._entitites.put("Process", processes);
         ArrayList<Entity> sockets = new ArrayList<Entity>();
         this._entitites.put("Socket", sockets);
         ArrayList<Entity> connections = new ArrayList<Entity>();
         this._entitites.put("TCPConnection", connections);
         ArrayList<Entity> UDPconnections = new ArrayList<Entity>();
         this._entitites.put("UDPConnection", UDPconnections);*/
        /*ArrayList<Entity> modules = new ArrayList<Entity>();
         this._entitites.put("Library", modules);*/
        this._entititesParseStatus = new HashMap<String, Boolean>();
        /**
         * Esta colección se inicializa según el archivo de configuración
         * parse_configuration.xml
         */

        Iterator<Entry<Integer, String>> i = ConfigurationManager.getInstance().getParseConfigurations().entrySet().iterator();
        while (i.hasNext()) {
            /**
             * Por cada item, de la configuración, inicializa la colección de
             * parse status
             */

            Entry<Integer, String> e = (Entry<Integer, String>) i.next();
            this._entititesParseStatus.put(e.getValue(), Boolean.FALSE);
        }
    }

    public static DContentServiceMemory getInstance() {
        if (_instance == null) {
            _instance = new DContentServiceMemory();
        }
        return _instance;
    }

    public Map<String, ArrayList<Entity>> getEntitites() {
        return _entitites;
    }

    public void setEntitites(Map<String, ArrayList<Entity>> _entitites) {
        this._entitites = _entitites;
    }

    public Map<String, Boolean> getEntititesParseStatus() {
        return _entititesParseStatus;
    }

    public void setEntititesParseStatus(Map<String, Boolean> _entititesParseStatus) {
        this._entititesParseStatus = _entititesParseStatus;
    }

    @Override
    public String loadContent(java.util.Map<Object, Entity> entities) {
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
                    if (this._entitites.get(e.getValue().getTag()) != null) {
                        this._entitites.get(e.getValue().getTag()).clear();
                    } else {
                        ArrayList<Entity> entitiesToAdd = new ArrayList<Entity>();
                        this._entitites.put(e.getValue().getTag(), entitiesToAdd);
                    }
                    first = false;
                }
                e.getValue().setPositionInList(index++);
                this._entitites.get(e.getValue().getTag()).add(e.getValue());
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
            if (this._entitites.get(entity.getTag()) == null) {
                ArrayList<Entity> entitiesToAdd = new ArrayList<Entity>();
                this._entitites.put(entity.getTag(), entitiesToAdd);
            }

            this._entitites.get(entity.getTag()).add(entity);
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

    @Override
    public void obtainLoadingStates() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int persistDumpFormatInformation(DumpFormat dumpFormat) {
        this._entititesParseStatus.put("dump_header", Boolean.TRUE);
        
        return 1;
    }

    @Override
    public int persistMetadata(Entity entity, String tag, String status) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

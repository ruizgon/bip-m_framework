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
package dump.strategy;

import dump.formats.DumpFormat;
import dump.ooss.OperatingSystemStructure;
import entities.Entity;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import system.utils.Observable;
import system.utils.Observer;
import java.util.TreeMap;
import persistence.dump_service.PersistenceDumpObject;
import translation.Translator;

/**
 *
 * @author Gonzalo
 */
public abstract class LoadingStrategy extends Observable implements IDumpLoader, Observer {

    protected String _version;
    protected String _path;
    protected String _currentHashType;
    protected String _currentHash;

    public String getPath() {
        return _path;
    }

    public void setPath(String _path) {
        this._path = _path;
    }

    public String getCurrentHashType() {
        return _currentHashType;
    }

    public void setCurrentHashType(String _currentHashType) {
        this._currentHashType = _currentHashType;
    }

    public PersistenceDumpObject getPersitenceDmpObj() {
        return _persitenceDmpObj;
    }

    public void setPersitenceDmpObj(PersistenceDumpObject _persitenceDmpObj) {
        this._persitenceDmpObj = _persitenceDmpObj;
    }

    protected OperatingSystemStructure _operatingSystemStructure;
    protected DumpFormat _dumpFormat;
    protected Translator _translator;

    protected boolean _isStructureCreated;

    protected PersistenceDumpObject _persitenceDmpObj;

    public String getCurrentHash() {
        return _currentHash;
    }

    public void setCurrentHash(String _currentHash) {
        this._currentHash = _currentHash;
    }

    public LoadingStrategy(OperatingSystemStructure operatingSystemStructure, DumpFormat dumpFormat, Translator translator) {
        this._operatingSystemStructure = operatingSystemStructure;
        this._dumpFormat = dumpFormat;

        this._isStructureCreated = false;
    }

    public String getVersion() {
        return _version;
    }

    public void setVersion(String _version) {
        this._version = _version;
    }

    public OperatingSystemStructure getOperatingSystemStructure() {
        return _operatingSystemStructure;
    }

    public void setOperatingSystemStructure(OperatingSystemStructure operatingSystemStructure) {
        this._operatingSystemStructure = operatingSystemStructure;
    }

    public DumpFormat getDumpFormat() {
        return _dumpFormat;
    }

    public void setDumpFormat(DumpFormat dumpFormat) {
        this._dumpFormat = dumpFormat;
    }

    public Translator getTranslator() {
        return _translator;
    }

    public void setTranslator(Translator _translator) {
        this._translator = _translator;
    }

    public boolean isIsStructureCreated() {
        return _isStructureCreated;
    }

    public void setIsStructureCreated(boolean _isStructureCreated) {
        this._isStructureCreated = _isStructureCreated;
    }

    /**
     * Crea las estructuras de las entidades en el medio de persistencia que
     * corresponda para luego realizar el procesos de persistencia de los datos
     * Aplica, por ejemplo, para estrategia de persistencia de Base de Datos,
     * donde se debe crear la estructura de tablas.
     *
     * @return mensaje con el resultado del proceso
     */
    public String executeStructuresCreationFromOSProcess() {
        String mensaje = "ERROR";

        try {
            createStructure();
        } catch (Exception ex) {
            this.notifyObservers(ex);
        }

        return mensaje;
    }

    /**
     * A cada entidad de OperatingSystemStructure, se le solicita se parsee.
     * Para ello, cada entidad solicita la colaboración de su respectivo Parser.
     *
     * @return mensaje con el resultado del proceso.
     */
    public String executeLoadProcess() {
        String mensaje = "ERROR";
        try {
            /**
             * Previamente el Dump Manager tiene que haber carfado datos del
             * Dump
             */
            Map<Object, Entity> entities = new HashMap<Object, Entity>();

            if (_operatingSystemStructure.getEntityList() != null) {
                Iterator<Entry<String, Entity>> i = _operatingSystemStructure.getEntityList().entrySet().iterator();
                while (i.hasNext()) {
                    Entry<String, Entity> e = (Entry<String, Entity>) i.next();
                    e.getValue().getParser().setDumpFormat(_dumpFormat);
                    entities = e.getValue().parse(null);

                    /**
                     * loadContent es implementado por las subclases. Inversión
                     * de control La subclase debe pedir colaboración al
                     * servicio de persistencia.
                     */
                    mensaje = loadContent(entities);
                }
            }
            mensaje = "CARGA DE CONTENIDO PROCESADA SIN ERRORES";
        } catch (Exception ex) {

        }
        return mensaje;
    }

    /**
     *
     * @param tag Indica qué tipo de Entidad se desea parsear, por ejemplo,
     * "Process"
     * @return
     */
    public String executeLoadProcessByTag(String tag, String stateTag) {
        String mensaje = "ERROR";

        try {
            Map<Object, Entity> entities = new HashMap<Object, Entity>();

            if (_operatingSystemStructure.getEntityList() != null) {
                _operatingSystemStructure.getEntityList().get(tag).getParser().setDumpFormat(_dumpFormat);
                entities = _operatingSystemStructure.getEntityList().get(tag).parse(null);

                /**
                 * loadContent es implementado por las subclases. Inversión de
                 * control La subclase debe pedir colaboración al servicio de
                 * persistencia.
                 */
                TreeMap entitiesTreeMap = new TreeMap<Object, Entity>();
                entitiesTreeMap.putAll(entities);
                mensaje = loadContent(entitiesTreeMap);
                if (stateTag == null) {
                    stateTag = "Active";
                    tag.concat(" ");
                    tag.concat(stateTag);
                    stateTag = tag;
                }

                if (mensaje.equals("Procesado")) {
                    String status = "OK";
                    persistMetadata(_operatingSystemStructure.getEntityList().get(tag), stateTag, status);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            mensaje = "ERROR";
        }

        return mensaje;
    }

    /**
     *
     * @param entity Permite tomar deciciones en el parseo según la entity
     * pasada como parámetro
     * @return
     */
    public String executeLoadProcessByEntity(Entity entity, Object[] params) {
        String mensaje = "ERROR";

        try {
            Map<Object, Entity> entities = new HashMap<Object, Entity>();

            if (_operatingSystemStructure.getEntityList() != null) {
                String tag = entity.getTag();
                if(tag.contains("#")){
                    String[] tagElement = tag.split("#");
                    tag = tagElement[0];
                }
                _operatingSystemStructure.getEntityList().get(tag).getParser().setDumpFormat(_dumpFormat);
                entities = _operatingSystemStructure.getEntityList().get(tag).parse(entity, params);

                /**
                 * loadContent es implementado por las subclases. Inversión de
                 * control La subclase debe pedir colaboración al servicio de
                 * persistencia.
                 */
                mensaje = loadContent(entities);
                if (mensaje.equals("Procesado")) {
                    String status = "OK";
                    persistMetadata(entity, entity.getTag(), status);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            mensaje = "ERROR";
        }

        return mensaje;
    }

    public String executeLoadLibs() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return this.executeLoadProcess();
    }
}

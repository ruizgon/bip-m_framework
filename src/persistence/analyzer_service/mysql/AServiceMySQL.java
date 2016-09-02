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
package persistence.analyzer_service.mysql;

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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import persistence.IDataService;
import persistence.analyzer_service.PersistenceObject;
import persistence.database.IServiceDatabase;
import persistence.database.MapperConnection;
import persistence.database.MapperFactory;
import persistence.database.MapperLib;
import persistence.database.MapperProcess;
import persistence.database.MapperSSDT;
import persistence.database.MapperSocket;
import persistence.database.mysql.MapperFactoryMySQLWin732;
import persistence.database.mysql.MapperFactoryMySQLWin764;
import system.utils.ConfigurationManager;

/**
 *
 * @author Gonzalo
 */
public class AServiceMySQL extends PersistenceObject implements IServiceDatabase, IDataService { //implementa todas las interfaces que debe implementar PersistenceObject

    private String _databaseName;

    private MapperFactory _mapperFactory;

    public MapperFactory getMapperFactory() {
        return _mapperFactory;
    }

    public String getDatabaseName() {
        return _databaseName;
    }

    public void setDatabaseName(String _databaseName) {
        this._databaseName = _databaseName;
    }

    public void setMapperFactory(MapperFactory _mapperFactory) {
        this._mapperFactory = _mapperFactory;
    }

    private AServiceMySQL(DumpManager dumpManager) {
        super(dumpManager);
        String path = System.getProperty("user.dir") + "\\sources\\mysql.properties";
        this._propertiesFile = path;
    }

    public static AServiceMySQL getInstance(DumpManager dumpManager) {
        if (_instance == null) {
            _instance = new AServiceMySQL(dumpManager);
        }
        return (AServiceMySQL) _instance;
    }

    public void calculateDabaseName() {
        this._databaseName = this._version + this._currentHashType + "_" + this._currentHash;
    }

    @Override
    public MapperFactory getMapperFact() {
        if (this._mapperFactory == null) {
            this._mapperFactory = this.getMapperFactoryByVersion(this._version, this._databaseName);
        }

        return this._mapperFactory;
    }

    @Override
    public boolean isDataBaseCreated(String databaseName) {
        boolean created = false;

        try {
            created = this.getMapperFact().create().isDataBaseCreated(databaseName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return created;
    }

    @Override
    public int createStructure() {
        int respuesta = 0;

        try {
            /*
             * Obtiene desde el archivo de propiedades, la ubicación
             * del script correspondiente 
             */
            ConfigurationManager.getInstance().loadPersistenceProperties(this._propertiesFile);
            String path = ConfigurationManager.getInstance().getProperty(this._version);
            this.calculateDabaseName();
            if (!this.isDataBaseCreated(this._databaseName)) {
                respuesta = this.getMapperFact().create().createStructure(path);
            }
        } catch (Exception ex) {
            respuesta = -1;
            ex.printStackTrace();
        }
        return respuesta;
    }

    @Override
    public boolean isDataPresent(entities.Entity entity) {
        boolean present = false;

        try {
            present = this.getMapperFact().create().isDataPresent(entity);
        } catch (Exception ex) {

        }
        return present;
    }

    @Override
    public boolean isDataPresent(SeekerState state) {
        boolean present = false;

        try {
            present = this.getMapperFact().create().isDataPresent(state);
        } catch (Exception ex) {

        }
        return present;
    }

    @Override
    public boolean isDataPresent(SeekerState state, Entity entity) {
        boolean present = false;

        try {
            present = this.getMapperFact().create().isDataPresent(state, entity);
        } catch (Exception ex) {

        }
        return present;
    }

    @Override
    public boolean isDataPresent(String tag) {
        boolean present = false;

        try {
            present = this.getMapperFact().create().isDataPresent(tag);
        } catch (Exception ex) {

        }
        return present;
    }

    @Override
    public MapperFactory getMapperFactoryByVersion(String version, String databaseName) {
        MapperFactory mapperFactory = null;

        try {
            switch (version) {
                case "WIN7_X86":
                    mapperFactory = new MapperFactoryMySQLWin732();
                    mapperFactory.setPath(this._path);
                    mapperFactory.setCurrentHashType(this._currentHashType);
                    mapperFactory.setCurrentHash(this._currentHash);
                    mapperFactory.setDatabaseName(this._databaseName);
                    break;
                case "WIN7_X64":
                    mapperFactory = new MapperFactoryMySQLWin764();
                    mapperFactory.setPath(this._path);
                    mapperFactory.setCurrentHashType(this._currentHashType);
                    mapperFactory.setCurrentHash(this._currentHash);
                    mapperFactory.setDatabaseName(this._databaseName);
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {

        }

        return mapperFactory;
    }

    @Override
    public ArrayList<EntityProcess> getAllProcesses(EntityProcess entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<EntityProcess> getProcessList(ProcessState state, EntityProcess entity) {
        ArrayList<EntityProcess> processList = null;

        try {
            MapperProcess mapperProcess = this._mapperFactory.createMapperProcess();
            if (this.isDataPresent(state, entity)) {
                /**
                 * Solicta al mapper, el listado de procesos
                 */
                processList = mapperProcess.getProcessList(state, entity);
            } else {
                /**
                 * Solicita al Dump Manager la búsqueda y persistencia de
                 * procesos
                 */
                this.getDumpManager().executeLoadProcessByState(state);
                processList = mapperProcess.getProcessList(state, entity);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return processList;
    }

    /**
     *
     * @param state Aporta información sobre el tipo de proceso a buscar La
     * implementación aprovecha el atributo _stateLabel para poder lograr
     * polimirfismo en la búsqueda
     * @return
     */
    @Override
    public EntityProcess getFirstProcess(ProcessState state, EntityProcess entity
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityProcess getNextProcess(ProcessState state, EntityProcess entity
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityProcess getPrevProcess(ProcessState state, EntityProcess entity
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityProcess getSpecificProcessById(int id
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityProcess getSpecificProcessByName(String s, Object par1
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<EntityThread> getThreadListByProcess(SeekerState state, EntityProcess process, EntityThread entity) {
        ArrayList<EntityThread> threadList = null;

        try {
            MapperProcess mapperProcess = this._mapperFactory.createMapperProcess();
            entity.setTag(entity.getTag() + "#" + process.getId());
            if (this.isDataPresent(entity)) {
                /**
                 * Solicta al mapper, el listado de threads
                 */
                threadList = mapperProcess.getThreadsByProcess(process);
            } else {
                /**
                 * Solicita al Dump Manager la búsqueda y persistencia de
                 * threads de un proceso
                 */
                /**
                 * Seteo parámetros
                 */
                Object[] params = new Object[1];
                params[0] = process;
                this.getDumpManager().executeLoadProcessByEntity(entity, params);

                threadList = mapperProcess.getThreadsByProcess(process);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return threadList;
    }

    @Override
    public ArrayList<EntityConnection> getAllConnections(EntityConnection entity
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<EntityConnection> getConnectionList(ConnectionState state, EntityConnection entity) {
        ArrayList<EntityConnection> connectionList = null;

        try {
            MapperConnection mapperConnection = this._mapperFactory.createMapperConnection();
            if (this.isDataPresent(entity)) {
                /**
                 * Solicta al mapper, el listado de procesos
                 */
                connectionList = mapperConnection.getConnectionList(state, entity);
            } else {
                /**
                 * Solicita al Dump Manager la búsqueda y persistencia de
                 * procesos
                 */
                this.getDumpManager().executeLoadProcessByTag(entity.getTag());
                connectionList = mapperConnection.getConnectionList(state, entity);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return connectionList;
    }

    @Override
    public EntityConnection getFirstConnection(ConnectionState state, entities.connection.EntityConnection entity
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityConnection getNextConnection(ConnectionState state, EntityConnection connection
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityConnection getPrevConnection(analyzer.states.ConnectionState state, EntityConnection connection
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public EntityFile getNextFile(EntityFile file
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityFile getPrevFile(EntityFile file
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<EntityLib> getLibList(SeekerLib lib, EntityLib entity) {
        List<EntityLib> libList = null;

        try {
            MapperLib mapperLib = this._mapperFactory.createMapperLib();
            if (this.isDataPresent(entity)) {
                /**
                 * Solicta al mapper, el listado de procesos
                 */
                libList = mapperLib.getLibList(entity);
            } else {
                /**
                 * Solicita al Dump Manager la búsqueda y persistencia de
                 * procesos
                 */
                this.getDumpManager().executeLoadProcessByTag(entity.getTag());
                libList = mapperLib.getLibList(entity);
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
    public EntityLib getNextLib(EntityLib lib
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityLib getPrevLib(EntityLib lib
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<EntitySSDT> getSSDTList(SeekerLib lib, List<EntitySSDT> entityList
    ) {
        List<EntitySSDT> ssdtList = null;

        try {
            MapperSSDT mapperSSDT = this._mapperFactory.createMapperSSDT();
            if (this.isDataPresent(entityList.get(0))) {
                /**
                 * Solicta al mapper, el listado de SSDT
                 */
                ssdtList = mapperSSDT.getSSDTList(entityList.get(0));
            } else {
                /**
                 * Solicita al Dump Manager la búsqueda y persistencia de SSDT
                 */
                int cantParams = entityList.size();
                Object[] params = new Object[cantParams];
                int i = 0;
                for (i = 0; i < cantParams; i++) {
                    params[i] = entityList.get(i).getaS().getVirtualAddress();
                }
                this.getDumpManager().executeLoadProcessByEntity(entityList.get(0), params);
                ssdtList = mapperSSDT.getSSDTList(entityList.get(0));
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
    public Entity getNextHandle(Entity handle
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Entity getPrevHandle(Entity handle
    ) {
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
    public EntityHive getNextHive(EntityHive hive
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityHive getPrevHive(EntityHive hive
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<EntityRootkit> getRootkitList(SeekerRootkit seekerRootkit, EntityRootkit entityRootkit
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<EntityRootkit> getRootkitList(analyzer.malware.SeekerRootkit seekerRootkit, entities.malware.EntityRootkit entityRootkit, ArrayList<EntityRootkit> entityRootkitList
    ) {
        return null;
    }

    @Override
    public EntityRootkit getFirstRootkit(entities.malware.EntityRootkit entityRootkit
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityRootkit getNextRootkit(EntityRootkit hive
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityRootkit getPrevRootkit(EntityRootkit hive
    ) {
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
        List<EntitySocket> socketList = null;

        try {
            MapperSocket mapperSocket = this._mapperFactory.createMapperSocket();
            if (this.isDataPresent(state, entity)) {
                /**
                 * Solicta al mapper, el listado de sockets
                 */
                socketList = mapperSocket.getSocketList(state, entity);
            } else {
                /**
                 * Solicita al Dump Manager la búsqueda y persistencia de
                 * sockets
                 */
                this.getDumpManager().executeLoadProcessByState(state);
                socketList = mapperSocket.getSocketList(state, entity);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return socketList;
    }

    @Override
    public EntitySocket getFirstSocket(SocketState state, EntitySocket entity
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntitySocket getNextSocket(SocketState state, EntitySocket entity
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntitySocket getPrevSocket(SocketState state, EntitySocket entity
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<EntityConnection> getConnectionListByProcess(ConnectionState state, EntityConnection entity, EntityProcess process
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<EntitySocket> getSocketListByProcess(SocketState state, EntitySocket entity, EntityProcess process
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<EntityLib> getLibListByProcess(SeekerLib lib, EntityProcess process, EntityLib entity
    ) {
        ArrayList<EntityLib> libList = null;

        try {
            libList = new ArrayList<EntityLib>();
            MapperProcess mapperProcess = this._mapperFactory.createMapperProcess();

            /**
             * Seteo parámetros
             */
            Object[] params = new Object[1];
            params[0] = process;
            this.getDumpManager().executeLoadProcessByEntity(entity, params);

            libList = mapperProcess.getLibsByProcess(process);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return libList;
    }

    @Override
    public String loadContent(Map<Object, Entity> entities
    ) {
        String resultado = "No procesado";

        try {
            int respuesta = 0;
            Iterator<Map.Entry<Object, Entity>> i = entities.entrySet().iterator();
            while (i.hasNext()) {
                Map.Entry<Object, Entity> e = (Map.Entry<Object, Entity>) i.next();
                Entity entity = (Entity) e.getValue();
                respuesta = this.getMapperFact().creatByTag(entity.getTag()).persist(entity);
            }
            resultado = "Procesado";
        } catch (Exception ex) {
            ex.printStackTrace();
            resultado = "Error";
        }

        return resultado;
    }

    @Override
    public String loadContentEntity(Entity entity
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateLoadingState(String tag
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

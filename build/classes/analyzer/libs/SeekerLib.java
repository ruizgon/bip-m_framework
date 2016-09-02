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

import analyzer.Seeker;
import entities.Entity;
import entities.lib.EntityLib;
import entities.malware.EntitySDE;
import entities.malware.EntitySSDT;
import entities.process.EntityProcess;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import output.OutputManagerAnalyzer;
import output.OutputManagerConsole;
import output.OutputManagerException;
import persistence.analyzer_service.PersistenceObject;

/**
 *
 * @author Gonzalo
 */
public abstract class SeekerLib extends Seeker {//También va a colaborar con SeekerConnection

    private String _libListHeadPointer;//Puntero a la cabecera de la lista de drivers o librerías cargadas. Para Drivers en Windows:  psLoadedModuleList
    private List<EntityLib> _libList;
    private EntityLib _libLocated; //libreria encontrado. Utilizado para la búsqueda de un proceso específico
    private EntityLib _firstLibLocated;
    private int _libCount;

    private PersistenceObject _persistenceObject;

    public SeekerLib() {
        /**
         * Registra observadores
         */
        this.addObserver(OutputManagerException.getInstance());
        this.addObserver(OutputManagerAnalyzer.getInstance());
        this.addObserver(OutputManagerConsole.getInstance());
    }

    public SeekerLib(PersistenceObject persistenceObject) {
        this.setAService(persistenceObject);

        /**
         * Registra observadores
         */
        this.addObserver(OutputManagerException.getInstance());
        this.addObserver(OutputManagerAnalyzer.getInstance());
        this.addObserver(OutputManagerConsole.getInstance());
    }

    /*
     *Constructor para cuando decora a otro Seeker
     */
    public SeekerLib(Seeker s) {
        super.setSeeker(s);

        /**
         * Registra observadores
         */
        this.addObserver(OutputManagerException.getInstance());
        this.addObserver(OutputManagerAnalyzer.getInstance());
        this.addObserver(OutputManagerConsole.getInstance());
    }

    public String getLibListHeadPointer() {
        return _libListHeadPointer;
    }

    public void setLibListHeadPointer(String _libListHeadPointer) {
        this._libListHeadPointer = _libListHeadPointer;
    }

    public List<EntityLib> getLibList() {
        List<EntityLib> libList = null;

        /**
         * Notifica a observadores
         */
        String log = "Se solicita lista de módulos en memoria.";
        this.notifyObservers(log);

        if (this._libList != null) {
            libList = this._libList;
        } else {
            libList = new ArrayList<EntityLib>();
            List<EntityLib> entities = this.getList();
            log = "BaseDllName   FullDllName   LoadTime   VA   PA   FileOffset";
            this.notifyObservers(log);
            log = "------------------------------------------------------------";
            this.notifyObservers(log);
            for (Entity e : entities) {
                EntityLib l = (EntityLib) e;
                libList.add(l);
                this.notifyObservers(l);
            }
            this._libList = libList;
        }

        /**
         * Notifica a observadores
         */
        int cantLibs = 0;
        if (libList != null) {
            cantLibs = libList.size();
        }
        log = "Finalizó la búsqueda de módulos en memoria";
        this.notifyObservers(log);
        log = "Cantidad de módulos encontrados = " + cantLibs + ".";
        this.notifyObservers(log);

        return this._libList;
    }

    public void setLibList(List<EntityLib> libList) {
        this._libList = libList;
    }

    public EntityLib getLibLocated() {
        return _libLocated;
    }

    public void setLibLocated(EntityLib libLocated) {
        this._libLocated = libLocated;
    }

    public EntityLib getFirstLibLocated() {
        return _firstLibLocated;
    }

    public void setFirstLibLocated(EntityLib firstLibLocated) {
        this._firstLibLocated = firstLibLocated;
    }

    public int getLibCount() {
        return _libCount;
    }

    public void setLibCount(int libCount) {
        this._libCount = libCount;
    }

    /*
     * Método hook
     */
    protected abstract EntityLib getFirstHive();

    /*
     * Método hook
     */
    public abstract EntityLib getNextHive(EntityLib hive);

    /*
     * Método hook
     */
    public abstract EntityLib getPrevHive(EntityLib hive);

    public abstract List<EntityLib> getList();

    public abstract ArrayList<EntityLib> getListByProcess(EntityProcess process);

    public PersistenceObject getAService() {
        return _persistenceObject;
    }

    public void setAService(PersistenceObject _persistenceObject) {
        this._persistenceObject = _persistenceObject;
    }

    public abstract List<EntitySSDT> getSSDTList(BigInteger virtualAddress, List<EntitySSDT> ssdtList);

    public abstract EntitySSDT getSSDT(BigInteger virtualAddress);

    public abstract ArrayList<EntitySDE> getSDEListBySSDT(BigInteger offset);
    
    public abstract EntityProcess getProcessInstance(int id);
}

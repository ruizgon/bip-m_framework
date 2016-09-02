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
package analyzer.processes;

import analyzer.Seeker;
import analyzer.interfaces.State;
import analyzer.states.ProcessState;
import entities.Entity;
import entities.EntityOwner;
import entities.process.EntityProcess;
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
public abstract class SeekerProcess extends Seeker implements State {

    private EntityProcess _process;
    /*
     * Importante para aplicar patrón observer:
     * En el método que es observado se deben utilizar los métodos de la clase Observable 
     * setChanged();
     * notifyObservers();
     */
    private String _processListHeadPointer; //En Windows: PsActiveProcessHead. Cabecera de la lista doblemente enlazada de procesos.
    private List<EntityProcess> _processList;
    private EntityProcess _processLocated; //proceso encontrado. Utilizado para la búsqueda de un proceso específico
    private EntityProcess _firstProcessLocated;
    private int _processCount;
    private ProcessState _processState;
    private EntityProcess _currentThread; //Esto es útil para encontrar el ActiveProcessLinks Address, por ejemplo

    private static String _TAG = "process";

    public SeekerProcess() {
        this._tag = _TAG;

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
    public SeekerProcess(Seeker s) {
        this._tag = _TAG;
        this.setSeeker(s);

        /**
         * Registra observadores
         */
        this.addObserver(OutputManagerException.getInstance());
        this.addObserver(OutputManagerAnalyzer.getInstance());
        this.addObserver(OutputManagerConsole.getInstance());
    }

    public EntityProcess getProcess() {
        return _process;
    }

    public void setProcess(EntityProcess _process) {
        this._process = _process;
    }

    public String getPsHeadPointer() {
        return _processListHeadPointer;
    }

    public void setPsHeadPointer(String _psHeadPointer) {
        this._processListHeadPointer = _psHeadPointer;
    }

    public List<EntityProcess> getAllProcesses() {

        List<EntityProcess> list = null;

        try {
            String log = "Inicio de búsqueda de procesos...";
            this.notifyObservers(log);
            List<EntityProcess> processList = this.getAService().getAllProcesses(this._process);

            for (EntityProcess p : processList) {
                list.add(p);
            }
            log = "Inicio de búsqueda de procesos...";
            this.notifyObservers(log);
        } catch (Exception ex) {

        }

        return list;
    }

    public List<EntityProcess> getProcessList() {
        /**
         * Notifica a observadores
         */
        String log = "Se solicita lista de procesos del tipo = " + this._processState.getStateLabel() + ".";
        this.notifyObservers(log);

        /**
         * Comienza el pedido
         */
        List<EntityProcess> processList = null;

        try {
            if (this._processList != null) {
                processList = this._processList;
            } else {
                processList = new ArrayList<>();
                List<Entity> entities = this._processState.getList();
                log = "Nombre    InheritedFromPID    PID    Create Time    Exit Time    State    VA    PA    FileOffset";
                this.notifyObservers(log);
                log = "-------------------------------------------------------------------------------------------------";
                this.notifyObservers(log);
                for (Entity e : entities) {
                    EntityProcess p = (EntityProcess) e;
                    processList.add(p);
                    this.notifyObservers(p);
                }
                this._processList = processList;
            }
        } catch (Exception ex) {
            this.notifyObservers(ex);
        }

        /**
         * Notifica a observadores
         */
        int cantProcesses = 0;
        if (processList != null) {
            cantProcesses = processList.size();
        }
        log = "Finalizó la búsqueda de procesos del tipo = " + this._processState.getStateLabel() + ".";
        this.notifyObservers(log);
        log = "Cantidad de procesos encontrados = " + cantProcesses + ".";
        this.notifyObservers(log);

        return processList;
    }

    public void setProcessList(List<EntityProcess> processList) {
        this._processList = processList;
    }

    public EntityProcess getProcessLocated() {
        return _processLocated;
    }

    public void setProcessLocated(EntityProcess processLocated) {
        this._processLocated = processLocated;
    }

    public EntityProcess getFirstProcessLocated() {
        return _firstProcessLocated;
    }

    public void setFirstProcessLocated(EntityProcess firstProcessLocated) {
        this._firstProcessLocated = firstProcessLocated;
    }

    public int getProcessCount() {
        return _processCount;
    }

    public void setProcessCount(int processCount) {
        this._processCount = processCount;
    }

    public ProcessState getProcessState() {
        return _processState;
    }

    public void setProcessState(ProcessState processState) {
        this._processState = processState;
    }

    public void setAService(PersistenceObject persistenceObject) {
        super.setAService(persistenceObject);
        if (this._processState != null) {
            this._processState.setAService(persistenceObject);
        }
    }

    public EntityProcess getCurrentProcess() {
        return _currentThread;
    }

    public void setCurrentProcess(EntityProcess _currentProcess) {
        this._currentThread = _currentProcess;
    }

    /*
     * Template methods
     */
    public final List<EntityProcess> seekProcessList() {

        this._processList = new ArrayList<EntityProcess>();
        EntityProcess process;

        this._firstProcessLocated = getFirstProcess(this._processState);

        if (this._firstProcessLocated != null) {
            this._processList.add(this._firstProcessLocated);
            process = getNextProcess(this._firstProcessLocated, this._processState);
            while (process != null) {
                this._processList.add(process);
                process = getNextProcess(process, _processState);
            }
        }

        this._processCount = this._processList.size();

        return this._processList;
    }

    public final EntityProcess seekSpecificProcessById(int id) {

        /*
         * Algoritmo para búsqueda de proceso
         */
        if (_firstProcessLocated == null) {
            _firstProcessLocated = getFirstProcess(_processState);
        }

        EntityProcess process = null;
        if (_firstProcessLocated != null) {
            if (_firstProcessLocated.getProcessID() == id) {
                process = _firstProcessLocated;
            } else {
                boolean encontrado = false;
                while (!encontrado) {
                    process = getNextProcess(process, _processState);
                    if (process.getProcessID() == id) {
                        encontrado = true;
                    }
                }

                if (!encontrado) {
                    process = null;
                }
            }
        }

        _processLocated = process;

        return process;
    }

    public final EntityProcess seekSpecificProcessByName(String s) {
        /*
         * Algoritmo para búsqueda de proceso
         */
        if (_firstProcessLocated == null) {
            _firstProcessLocated = getFirstProcess(_processState);
        }

        EntityProcess process = null;
        if (_firstProcessLocated != null) {
            if (_firstProcessLocated.getName().equals(s)) {
                process = _firstProcessLocated;
            } else {
                boolean encontrado = false;
                while (!encontrado) {
                    process = getNextProcess(process, _processState);
                    if (process.getName().equals(s)) {
                        encontrado = true;
                    }
                }

                if (!encontrado) {
                    process = null;
                }
            }
        }

        _processLocated = process;

        return process;
    }

    /*
     *Búsqueda por nombre de usuario
     */
    public final EntityProcess seekSpecificProcessByOwner(EntityOwner o) {
        /*
         * Algoritmo para búsqueda de proceso
         */
        if (_firstProcessLocated == null) {
            _firstProcessLocated = getFirstProcess(_processState);
        }

        EntityProcess process = null;
        if (_firstProcessLocated != null) {
            if (_firstProcessLocated.getOwner().getUser().equals(o.getUser())) {
                process = _firstProcessLocated;
            } else {
                boolean encontrado = false;
                while (!encontrado) {
                    process = getNextProcess(process, _processState);
                    if (process.getOwner().getUser().equals(o.getUser())) {
                        encontrado = true;
                    }
                }

                if (!encontrado) {
                    process = null;
                }
            }
        }

        _processLocated = process;

        return process;
    }

    public final void calculateProcessCount() {
        int count = 0;

        if (_firstProcessLocated == null) {
            _firstProcessLocated = getFirstProcess(_processState);
        }

        if (_firstProcessLocated != null) {
            count++;
            EntityProcess process = getNextProcess(_firstProcessLocated, _processState);
            while (process != null) {
                count++;
            }
        }

        _processCount = count;
    }

    /*
     * Método hook
     */
    protected abstract EntityProcess getFirstProcess(ProcessState s);

    /*
     * Método hook
     */
    protected abstract EntityProcess getNextProcess(EntityProcess process, ProcessState s);

    /*
     * Método hook
     */
    protected abstract EntityProcess getPrevProcess(EntityProcess process, ProcessState s);
}

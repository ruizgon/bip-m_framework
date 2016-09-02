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
package analyzer;

import analyzer.malware.SeekerRootkit;
import analyzer.connections.SeekerConnection;
import analyzer.factories.AnalyzerFactory;
import analyzer.files.SeekerFile;
import analyzer.handles.SeekerHandle;
import analyzer.hives.SeekerHive;
import analyzer.libs.SeekerLib;
import analyzer.processes.SeekerProcess;
import java.util.HashMap;
import persistence.analyzer_service.PersistenceObject;

/**
 *
 * @author Gonzalo
 */
public abstract class SeekerManager {

    protected static SeekerManager _instance;
    protected AnalyzerFactory _factory;
    private HashMap<String, Seeker> _seekers;
    private SeekerProcess _seekerProcess;
    private SeekerLib _seekerLib;
    private SeekerConnection _seekerConnection;
    private SeekerFile _seekerFile;
    private SeekerHandle _seekerHandle;
    private SeekerEncrypt _seekerEncrypt;
    private SeekerHive _seekerHive;
    private SeekerRootkit _seekerRootKit;
    private PersistenceObject _aService;

    protected SeekerManager() {
        _seekers = new HashMap<String, Seeker>();
    }

    protected SeekerManager(AnalyzerFactory _factory) {
        _seekers = new HashMap<String, Seeker>();
        this._factory = _factory;
    }

    public AnalyzerFactory getFactory() {
        return _factory;
    }

    public void setFactory(AnalyzerFactory _factory) {
        this._factory = _factory;
    }

    public SeekerProcess getSeekerProcess() {
        return _seekerProcess;
    }

    /**
     * La factory es la encargada de crear la instancia espécifica de
     * SeekerProcess
     */
    public void setSeekerProcess() {
        this._seekerProcess = this._factory.createSeekerProcess();
        this._seekerProcess.setAService(this._aService);
        this._seekers.put(this._seekerProcess.getTag(), _seekerLib);
    }

    public SeekerLib getSeekerLib() {
        return _seekerLib;
    }

    /**
     * La factory es la encargada de crear la instancia espécifica de
     * SeekerProcess
     */
    public void setSeekerLib() {
        this._seekerLib = this._factory.createSeekerLib();
        this._seekerLib.setAService(this._aService);
    }

    public SeekerConnection getSeekerConnection() {
        return _seekerConnection;
    }

    /**
     * La factory es la encargada de crear la instancia espécifica de
     * SeekerProcess
     */
    public void setSeekerConnection() {
        this._seekerConnection = this._factory.createSeekerConnection();
        this._seekerConnection.setAService(this._aService);
    }

    public SeekerFile getSeekerFile() {
        return _seekerFile;
    }

    /**
     * La factory es la encargada de crear la instancia espécifica de
     * SeekerProcess
     */
    public void setSeekerFile() {
        this._seekerFile = this._factory.createSeekerFile();
        this._seekerFile.setAService(this._aService);
    }

    public SeekerHandle getSeekerHandle() {
        return _seekerHandle;
    }

    /**
     * La factory es la encargada de crear la instancia espécifica de
     * SeekerProcess
     */
    public void setSeekerHandle() {
        this._seekerHandle = this._factory.createSeekerHandle();
        this._seekerHandle.setAService(this._aService);
    }

    public SeekerEncrypt getSeekerEncrypt() {
        return _seekerEncrypt;
    }

    /**
     * La factory es la encargada de crear la instancia espécifica de
     * SeekerProcess
     */
    public void setSeekerEncrypt() {
        this._seekerEncrypt = this._factory.createSeekerEncrypt();
        this._seekerEncrypt.setAService(this._aService);
    }

    public SeekerHive getSeekerHive() {
        return _seekerHive;
    }

    /**
     * La factory es la encargada de crear la instancia espécifica de
     * SeekerProcess
     */
    public void setSeekerHive() {
        this._seekerHive = this._factory.createSeekerHive();
        this._seekerHive.setAService(this._aService);
    }

    public SeekerRootkit getSeekerRootKit() {
        return _seekerRootKit;
    }

    /**
     * La factory es la encargada de crear la instancia espécifica de
     * SeekerProcess
     */
    public void setSeekerRootKit() {
        this._seekerRootKit = this._factory.createSeekerRootkit();
        this._seekerRootKit.setAService(this._aService);
    }

    public PersistenceObject getAService() {
        return _aService;
    }

    public void setAService(PersistenceObject _persistenceObject) {
        this._aService = _persistenceObject;
    }

    public Seeker getSeeker(String command) {
        Seeker seeker = null;

        switch (command) {
            case "process":
                seeker = this._factory.createSeekerProcess();
                seeker.setAService(this.getAService());
                break;
            case "lib":
                seeker = this._factory.createSeekerLib();
                seeker.setAService(this.getAService());
                break;
            case "connection":
                seeker = this._factory.createSeekerConnection();
                seeker.setAService(this.getAService());
                break;
            case "socket":
                seeker = this._factory.createSeekerConnection();
                seeker.setAService(this.getAService());
                break;
            case "rootkitssdt":
                seeker = this._factory.createSeekerRootkit();
                seeker.setAService(this.getAService());
                break;
            default:
                break;
        }

        return seeker;
    }
}

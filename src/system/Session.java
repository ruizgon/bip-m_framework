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
package system;

import analyzer.Seeker;
import analyzer.SeekerManager;
import analyzer.SeekerManagerWin732;
import analyzer.SeekerManagerWin764;
import analyzer.connections.SeekerConnection;
import analyzer.libs.SeekerLib;
import analyzer.libs.SeekerLibWin732;
import analyzer.malware.SeekerRootkitSSDT;
import analyzer.malware.SeekerRootkitSSDTWin732;
import analyzer.malware.SeekerRootkitSSDTWin764;
import analyzer.malware.SeekerRootkitSSDTWin764;
import analyzer.processes.SeekerProcess;
import dump.DumpManager;
import dump.DumpManagerWin732CD;
import dump.DumpManagerWin764CD;
import dump.DumpManagerWin764RD;
import dump.formats.DumpFormat;
import dump.strategy.LoadingStrategy;
import dump.strategy.MemoryLoader;
import dump.strategy.MySQLLoader;
import entities.lib.EntityLib;
import entities.malware.EntityRootkit;
import entities.malware.EntitySSDT;
import entities.process.EntityProcess;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import output.OutputManager;
import output.OutputManagerAnalyzer;
import output.OutputManagerConsole;
import output.OutputManagerDump;
import output.OutputManagerException;
import persistence.analyzer_service.PersistenceObject;
import persistence.analyzer_service.memory.AServiceMemory;
import persistence.analyzer_service.mysql.AServiceMySQL;
import persistence.dump_service.PersistenceDumpObject;
import persistence.dump_service.memory.DContentServiceMemory;
import persistence.dump_service.mysql.DContentServiceMySQL;
import system.utils.ConfigurationManager;
import system.utils.Conversor;
import system.utils.DateManager;
import system.utils.FileManager;
import system.utils.Observable;
import translation.Translator;
import translation.TranslatorWin32;
import translation.TranslatorWin64;

/**
 *
 * @author Gonzalo
 */
public class Session extends Observable {

    private String _sessionID;
    private String _path;
    private String _version;
    private String _keyInitializator;
    private String _persistencia;
    private String _currentCommand;
    private String _currentModifier;
    private DumpManager _dumpManager;
    private Translator _translator;
    private SeekerManager _seekerManager;
    private ArrayList<Seeker> _seekers;
    private PersistenceObject _aService;
    private PersistenceDumpObject _dContentService;
    private LoadingStrategy _loadingStrategy;
    private ArrayList<OutputManager> _outputManagers;
    private String _logPath;
    private String _propertiesPath;
    private String _currentHashType;
    private String _currentHash;
    private int _structureCreated;

    /**
     *
     * @param args args[0]: path del archivo de volcado de memoria.
     * args[1]:formato del archivo de volcado de memoria, por ejemplo,
     * CRASH_DUMP args[2]: versión del sistema operativo al que pertenece el
     * archivo de volcado de memoria. args[3]: medio de persistencia. args[4]:
     * comando (funcionalidad a la que se desea invocar). args[5] modificador de
     * comando, por ejemplo hidden. args[6] path donde se desean persistir los
     * logs. Key de mapas de configuración: dump_format + os_version, por
     * ejemplo, CRASH_DUMP_WIN7_X86.
     */
    public static void main(String[] args) {
        String path = null;
        String format = null;
        String version = null;
        String persistencia = null;
        String command = null;
        String modifier = null;
        String logPath = null;
        StringBuilder keyInitializator = new StringBuilder();
        //int length = args.length;

        int length = 3;
        if (length >= 1) {
            for (int iC = 0; iC < args.length; iC++) {
                switch (iC) {
                    case 0:
                        path = args[iC];
                        break;
                    case 1:
                        format = args[iC];
                        break;
                    case 2:
                        version = args[iC];
                        break;
                    case 3:
                        persistencia = args[iC];
                        break;
                    case 4:
                        command = args[iC];
                        break;
                    case 5:
                        modifier = args[iC];
                        break;
                    case 6:
                        logPath = args[iC];
                        break;

                    default:
                        break;
                }
            }
            /*path = "M:\\MEMORY_W7_X86_V1.DMP";
             format = "CRASH_DUMP";
             version = "WIN7_X86";
             modifier = "";*/

            path = "n:\\memdump_DELL_X64.mem";
             format = "RAW_DUMP";
             version = "WIN7_X64";
             command = "process";
             modifier = "";
             persistencia = "MEMORY";
             logPath = "M:\\logsBIP\\";
            keyInitializator.append(format);
            keyInitializator.append("_");
            keyInitializator.append(version);
            if (path != null) {
                Session session = new Session(path, version, keyInitializator.toString(), persistencia, command, modifier, logPath);
            } else {
                Session session = new Session(version, keyInitializator.toString(), persistencia, command, modifier, logPath);
            }
        } else {
            /**
             * Escribir contenido de ayuda del archivo de ayuda
             */
            FileManager.getInstance().printFileContent("help.txt");

        }

    }

    private Session(String version, String keyInitializator, String persistencia, String command, String modifier, String logPath) {
        //Inicializar   
        this._path = null;
        this._version = version;
        this._keyInitializator = keyInitializator;
        this._persistencia = persistencia;
        this._currentCommand = command;
        this._currentModifier = modifier;
        this._propertiesPath = System.getProperty("user.dir") + "\\sources\\session.properties";
        this._logPath = logPath;

        /**
         * Inicializa configuracion
         */
        this.initializeConfiguration();

        /**
         * Inicializar componentes
         */
        this.initializeSessionComponents(keyInitializator, true);

        /**
         * Genera ID de Sesión
         */
        this.generateSessionID();

        /**
         * Inicializa Output managers
         */
        this.initializeOutputManagers();

        /**
         * Inicializa servicio de persistencia
         */
        this.initializePersistenceObject(version, persistencia);

        /**
         * Inicia
         */
        this.start(command, modifier);
    }

    /**
     *
     * @param path path del archivo de volcado de memoria pasado como parámetro.
     */
    private Session(String path, String version, String keyInitializator, String persistencia, String command, String modifier, String logPath) {
        //Inicializar
        this._path = path;
        this._version = version;
        this._keyInitializator = keyInitializator;
        this._persistencia = persistencia;
        this._currentCommand = command;
        this._currentModifier = modifier;
        this._propertiesPath = System.getProperty("user.dir") + "\\sources\\session.properties";
        this._logPath = logPath;

        /**
         * Inicializa configuracion
         */
        this.initializeConfiguration();

        /**
         * Inicializar componentes
         */
        this.initializeSessionComponents(keyInitializator, true);

        /**
         * Genera ID de Sesión
         */
        this.generateSessionID();

        /**
         * Inicializa Output managers
         */
        this.initializeOutputManagers();

        /**
         * Inicializa servicio de persistencia
         */
        this.initializePersistenceObject(version, persistencia);

        /**
         * Inicia
         */
        this.start(command, modifier);
    }

    public String getSessionID() {
        return _sessionID;
    }

    public String getPath() {
        return _path;
    }

    public DumpManager getDumpManager() {
        return _dumpManager;
    }

    public void setDumpManager(DumpManager _dumpManager) {
        this._dumpManager = _dumpManager;
    }

    public Translator getTranslator() {
        return _translator;
    }

    public void setTranslator(Translator _translator) {
        this._translator = _translator;
    }

    public ArrayList<Seeker> getSeekers() {
        return _seekers;
    }

    public void setSeekers(ArrayList<Seeker> _seekers) {
        this._seekers = _seekers;
    }

    public PersistenceDumpObject getdContentService() {
        return _dContentService;
    }

    public void setdContentService(PersistenceDumpObject _dContentService) {
        this._dContentService = _dContentService;
    }

    public ArrayList<OutputManager> getOutputManagers() {
        return _outputManagers;
    }

    public void setOutputManagers(ArrayList<OutputManager> _OutputManagers) {
        this._outputManagers = _OutputManagers;
    }

    public String getLogPath() {
        return _logPath;
    }

    public void setLogPath(String _logPath) {
        this._logPath = _logPath;
    }

    public void addSeeker(Seeker seeker) {
        _seekers.add(seeker);
    }

    public void removeSeeker(Seeker seeker) {
        _seekers.remove(seeker);
    }

    public Seeker getSeekerInstance(Class classSeeker) {
        Seeker seeker = null;
        for (Seeker s : _seekers) {
            if (s.getClass().equals(classSeeker)) {
                seeker = s;
            }
        }
        return seeker;
    }

    public void addOutputManager(OutputManager outputmanager) {
        _outputManagers.add(outputmanager);
    }

    public void removeOutputManager(OutputManager outputmanager) {
        _outputManagers.remove(outputmanager);
    }

    public OutputManager getOutputManagerInstance(Class classOutputManager) {
        OutputManager outputManager = null;
        for (OutputManager o : _outputManagers) {
            if (o.getClass().equals(classOutputManager)) {
                outputManager = o;
            }
        }
        return outputManager;
    }

    public void generateSessionID() {
        try {
            StringBuilder id = new StringBuilder("");
            if (this._path != null) {
                String[] s = this._path.split("\\\\");
                int l = s.length;
                id.append(s[l - 1]);
            }

            SimpleDateFormat dt = new SimpleDateFormat("yyyyMMddhhmmssSSSZ");
            GregorianCalendar cal = new GregorianCalendar();
            Date date = cal.getTime();
            id.append(dt.format(date));

            this._sessionID = id.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            this.notifyObservers(ex);
        }
    }

    public void initializeConfiguration() {
        ConfigurationManager.getInstance().obtainConfiguration();
        ConfigurationManager.getInstance().obtainParseConfiguration();
    }

    public void initializeSessionComponents(String keyInitializator, boolean littleEndian) {
        try {
            if (ConfigurationManager.getInstance().getConfigurations().get(keyInitializator) != null) {
                int key = ConfigurationManager.getInstance().getConfigurations().get(keyInitializator);

                /**
                 * Carga propiedades de la sesión
                 */
                ConfigurationManager.getInstance().loadSessionProperties(this._propertiesPath);
                this._logPath = ConfigurationManager.getInstance().getSessionProperties().getProperty("logPath");
                switch (key) {
                    case 0:
                        this._translator = new TranslatorWin32();
                        this._dumpManager = DumpManagerWin732CD.getInstance(this._path, littleEndian);
                        this._seekerManager = SeekerManagerWin732.getInstance();
                        break;
                    case 1:
                        this._translator = new TranslatorWin64();
                        this._dumpManager = DumpManagerWin764CD.getInstance(this._path, littleEndian);
                        this._seekerManager = SeekerManagerWin764.getInstance();
                        break;
                    /*case 2:
                        this._translator = new TranslatorWin64();
                        this._dumpManager = DumpManagerWin764CD.getInstance(this._path, littleEndian);
                        this._seekerManager = SeekerManagerWin764.getInstance();
                        break;*/
                    case 3:
                        this._translator = new TranslatorWin64();
                        this._dumpManager = DumpManagerWin764RD.getInstance(this._path, littleEndian);
                        this._seekerManager = SeekerManagerWin764.getInstance();
                        break;
                    default:
                        break;
                }
            } else {
                System.out.println("Verifique los parámetros ingresados.");
                System.exit(1);
            }
        } catch (ExceptionInInitializerError ex) {
            this.notifyObservers(ex);
        }
    }

    /**
     * TODO: Inicializar MapperFactory según versión, dentro del Persistence
     * Object
     *
     * @param version
     * @param persistenceParam
     */
    public void initializePersistenceObject(String version, String persistenceParam) {

        try {
            ConfigurationManager.getInstance().obtainPersistenceConfiguration();
            int id = ConfigurationManager.getInstance().getPersistenceConfigurations().get(persistenceParam).intValue();
            this._currentHashType = ConfigurationManager.getInstance().getSessionProperties().getProperty("hashMethod");
            String descProceso = "Calculando hash del archivo de volcado de memoria: " + DateManager.getActualDate() + "...";
            OutputManagerConsole.getInstance(_sessionID, _logPath).print(descProceso);
            //this._currentHash = ConfigurationManager.getInstance().getHash(this._path);
            //this._currentHash = "9fbeaf559cc6df46b13b3cb87d4c3cc7";
            this._currentHash = "a2d53effa93e5cb2dc20311da3f7e697"; //x64
            descProceso = "Cálculo del hash finalizado: " + DateManager.getActualDate() + ".";
            OutputManagerConsole.getInstance(_sessionID, _logPath).print(descProceso);
            switch (id) {
                case 0:
                    this._aService = AServiceMemory.getInstance(this._dumpManager);
                    this._aService.setVersion(this._version);
                    this._aService.setCurrentHashType(this._currentHashType);
                    this._aService.setCurrentHash(this._currentHash);
                    this._aService.setPath(_path);
                    this._loadingStrategy = new MemoryLoader(this._dumpManager.getOS(), this._dumpManager.getDumpFormat(), this._dumpManager.getTranslator());
                    this._loadingStrategy.setVersion(this._version);
                    this._loadingStrategy.setCurrentHash(this._currentHash);
                    this._dContentService = DContentServiceMemory.getInstance();
                    this._dContentService.setVersion(this._version);
                    this._dContentService.setCurrentHashType(this._currentHashType);
                    this._dContentService.setCurrentHash(this._currentHash);
                    this._structureCreated = 1;
                    break;
                case 1:
                    this._aService = AServiceMySQL.getInstance(this._dumpManager);
                    this._aService.setVersion(this._version);
                    this._aService.setCurrentHashType(this._currentHashType);
                    this._aService.setCurrentHash(this._currentHash);
                    this._aService.setPath(_path);
                    AServiceMySQL service = (AServiceMySQL) this._aService;
                    this._structureCreated = service.createStructure();
                    MySQLLoader mysqlLoader = new MySQLLoader(this._dumpManager.getOS(), this._dumpManager.getDumpFormat(), this._dumpManager.getTranslator());
                    mysqlLoader.setVersion(this._version);
                    mysqlLoader.setCurrentHashType(this._currentHashType);
                    mysqlLoader.setCurrentHash(this._currentHash);
                    mysqlLoader.setDatabaseName(service.getDatabaseName());
                    mysqlLoader.updatePersistenceDmpObjectParameters();
                    this._loadingStrategy = mysqlLoader;
                    this._dContentService = DContentServiceMySQL.getInstance();
                    this._dContentService.setVersion(this._version);
                    this._dContentService.setCurrentHashType(this._currentHashType);
                    this._dContentService.setCurrentHash(this._currentHash);
                    break;
                default:
                    this._aService = AServiceMemory.getInstance(this._dumpManager);
                    this._aService.setVersion(this._version);
                    this._aService.setCurrentHashType(this._currentHashType);
                    this._aService.setCurrentHash(this._currentHash);
                    this._aService.setPath(_path);
                    this._loadingStrategy = new MemoryLoader(this._dumpManager.getOS(), this._dumpManager.getDumpFormat(), this._dumpManager.getTranslator());
                    this._loadingStrategy.setVersion(this._version);
                    this._loadingStrategy.setCurrentHash(this._currentHash);
                    this._dContentService = DContentServiceMemory.getInstance();
                    this._dContentService.setVersion(this._version);
                    this._dContentService.setCurrentHashType(this._currentHashType);
                    this._dContentService.setCurrentHash(this._currentHash);
                    this._structureCreated = 1;
                    break;
            }

            this._dumpManager.setLoadingStrategy(this._loadingStrategy);
            this._dumpManager.setPersistence(this._dContentService);
            this._dumpManager.obtainDumpFormatData();
            this._aService.setDumpManager(this._dumpManager);
            this._seekerManager.setAService(this._aService);
        } catch (Exception ex) {
            this.notifyObservers(ex);
        }
    }

    public void initializeOutputManagers() {
        OutputManagerDump.getInstance(this._sessionID, this._logPath);
        OutputManagerAnalyzer.getInstance(this._sessionID, this._logPath);
        OutputManagerException.getInstance().generateLogName(this._sessionID, this._logPath);
        this.addObserver(OutputManagerException.getInstance());
        this.addObserver(OutputManagerConsole.getInstance());
    }

    public void processCommand(String command, String modifier) {
        switch (command) {
            case "process": // busca procesos
                SeekerProcess seekerProcess = (SeekerProcess) this._seekerManager.getSeeker(command);
                seekerProcess.obtainPosibleSeekerState(modifier);
                seekerProcess.getProcessList();
                break;
            case "connection": // busca conexiones
                SeekerConnection seekerConnection = (SeekerConnection) this._seekerManager.getSeeker(command);
                seekerConnection.obtainPosibleSeekerState(modifier);
                seekerConnection.getConnectionList();
                break;
            case "socket": // busca sockets
                SeekerConnection seekerSocket = (SeekerConnection) this._seekerManager.getSeeker(command);
                seekerSocket.obtainPosibleSeekerState(modifier);
                seekerSocket.getSocketList();
                break;
            case "lib": // busca módulos
                SeekerLib seekerLib = (SeekerLib) this._seekerManager.getSeeker(command);
                seekerLib.getLibList();
                if (modifier != null) {
                    try {
                        int id = Integer.parseInt(modifier);
                        EntityProcess p = seekerLib.getProcessInstance(id);
                        seekerLib.getListByProcess(p);
                    } catch (Exception ex) {
                        notifyObservers("El id del proceso debe ser numérico.");
                    }
                }
                break;
            case "translate": // traduce virtual address a physical
                Translator translator = (Translator) this._dumpManager.getTranslator();
                translator.processCommand(modifier);
                break;
            case "offset": // calcula offset en archivo
                DumpFormat dumpFormat = (DumpFormat) this._dumpManager.getDumpFormat();
                dumpFormat.processCommand(command, modifier);
                break;
            case "pabyoffset": //calcula physical address a partir de offset
                DumpFormat dumpFormatPAB = (DumpFormat) this._dumpManager.getDumpFormat();
                dumpFormatPAB.processCommand(null, modifier);
                break;
            case "rootkitssdt": // busca Rootkit SSDT atacks
                SeekerRootkitSSDT seekerSSDT = (SeekerRootkitSSDT) this._seekerManager.getSeeker(command);
                seekerSSDT.getSSDTHooks();
                break;
            case "E":
                String exitMsg = "Gracias por utilizar BIP-M Framework.";
                this.notifyObservers(exitMsg);
                break;
            default: // notificar que el comando no está soportado
                break;
        }
    }

    public void start(String command, String modifier) {

        if (this._structureCreated >= 0) {
            /**
             * procesa comando
             */
            processCommand(command, modifier);

            System.out.println("Ingrese un nuevo comando: ");
            String commandLine = "";
            Scanner scan = new Scanner(System.in);
            commandLine = scan.nextLine();
            while (commandLine != null) {
                String[] commands = commandLine.split(" ");
                command = commands[0];
                if (commands.length > 1) {
                    modifier = commands[1];
                }
                processCommand(command, modifier);
                if (command.equals("E")) {
                    break;
                }
                System.out.println("Ingrese un nuevo comando: ");
                commandLine = scan.nextLine();
            }
        } else {
            processCommand("E", "");
        }
    }
}

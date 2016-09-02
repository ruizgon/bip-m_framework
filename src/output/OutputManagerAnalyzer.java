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
package output;

import entities.Entity;
import entities.EntityList;
import entities.EntityUnicodeString;
import entities.connection.EntityAddressInfo;
import entities.connection.EntityConnection;
import entities.connection.EntityLocalAddress;
import entities.connection.EntitySocket;
import entities.connection.EntityTCPConnection;
import entities.connection.EntityUDPConnection;
import entities.connection.EntityWindowsConnectionState;
import entities.lib.EntityLib;
import entities.malware.EntityMalware;
import entities.malware.EntitySDE;
import entities.malware.EntitySSDT;
import entities.process.EntityProcess;
import entities.process.EntityThread;
import entities.process.EntityWindows732Process;
import entities.translation.EntityAddressSpace;
import java.io.File;
import system.utils.Conversor;
import system.utils.DateManager;
import system.utils.FileManager;
import system.utils.IEntityVisitor;

/**
 *
 * @author Gonzalo
 */
public class OutputManagerAnalyzer extends OutputManager implements IEntityVisitor {

    protected static OutputManagerAnalyzer _instance;

    private OutputManagerAnalyzer() {
        this.generateLogName();
    }

    private OutputManagerAnalyzer(String currentSessionID, String logPath) {
        this._currentSessionID = currentSessionID;
        this._logPath = logPath;
        this.generateLogName();
    }

    public static OutputManagerAnalyzer getInstance() {
        if (_instance == null) {
            _instance = new OutputManagerAnalyzer();
        }

        return _instance;
    }

    public static OutputManagerAnalyzer getInstance(String currentSessionID, String logPath) {
        if (_instance == null) {
            _instance = new OutputManagerAnalyzer(currentSessionID, logPath);
        }

        return _instance;
    }

    @Override
    public void update(Object o) {
        this.registerLog(this._fullLogPath, o);
    }

    @Override
    public void registerLog(String path, Object param) {
        try {
            FileManager fileManager = FileManager.getInstance();

            if (param instanceof String) {
                String paramStr = (String) param;
                fileManager.write(path, paramStr);
            } else if (param instanceof Entity) {
                Entity e = (Entity) param;
                e.accept(this);
            }
        } catch (Exception ex) {
            this.notifyObservers(ex);
        }
    }

    @Override
    public String getLog() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void generateLogName() {
        try {
            String logName = "log_analyzer_" + this._currentSessionID;
            this._logName = logName;
            
            File logFolder = new File(this._logPath);
            if(!logFolder.exists()){ // si no existe, lo crea
                logFolder.mkdirs();
            }
            
            this._fullLogPath = this._logPath + "\\" + this._logName + ".txt";

            /**
             * Registro encabezado del archivo de log
             */
            String content = ".::.BIP-M Framework.::.";
            this.registerLog(this._fullLogPath, content);
            content = "------------------------------------------------------------------------------------------------";
            this.registerLog(this._fullLogPath, content);
            content = "Logs de análisis realizados en la sesión ID: " + this._currentSessionID;
            this.registerLog(this._fullLogPath, content);

            content = "Fecha: " + DateManager.getActualDateWithISOFormat();
            this.registerLog(this._fullLogPath, content);

            content = "------------------------------------------------------------------------------------------------";
            this.registerLog(this._fullLogPath, content);
        } catch (Exception ex) {
            this.notifyObservers(ex);
        }
    }

    public String unMapProcess(EntityProcess p) {
        String pStr = null;

        try {
            /**
             * Construye una cadena con los datos de la entidad
             */
            String processIDStr = String.valueOf(Conversor.hexToLong(p.getAttributes().get("UniqueProcessId").getContent().toString()));
            String inheritedFromPIDStr = String.valueOf(Conversor.hexToLong(p.getAttributes().get("InheritedFromUniqueProcessId").getContent().toString())) + "          ";
            pStr = p.getAttributes().get("ImageFileName").getContent().toString()
                    + "    " + inheritedFromPIDStr
                    + "    " + processIDStr
                    + "    " + p.getAttributes().get("CreateTime").getContent().toString()
                    + "    " + p.getAttributes().get("ExitTime").getContent().toString()
                    + "    " + p.getStateLabel()
                    + "    0x" + p.getaS().getVirtualAddressHex()
                    + "    0x" + p.getaS().getPhysicalAddressHex()
                    + "    0x" + p.getaS().getOffsetInFileHex();
        } catch (Exception ex) {
            ex.printStackTrace();
            this.notifyObservers(ex);
        }

        return pStr;
    }

    public String unMapLib(EntityLib l) {
        String lStr = null;

        try {
            String baseDllName = "";
            String fullDllName = "";
            String loadTime = "";
            EntityUnicodeString unicodeString = null;
            if (l.getAttributes().get("BaseDllName").getContent() != null) {
                unicodeString = (EntityUnicodeString) l.getAttributes().get("BaseDllName").getContent();
                if (unicodeString.getAttributes().get("Name").getContent() != null) {
                    baseDllName = unicodeString.getAttributes().get("Name").getContent().toString();
                }
            } else {
                baseDllName = "    ";
            }
            if (l.getAttributes().get("FullDllName").getContent() != null) {
                unicodeString = (EntityUnicodeString) l.getAttributes().get("FullDllName").getContent();
                if (unicodeString.getAttributes().get("Name").getContent() != null) {
                    fullDllName = unicodeString.getAttributes().get("Name").getContent().toString();
                }
            } else {
                fullDllName = "    ";
            }
            if (l.getAttributes().get("LoadTime").getContent() != null) {
                loadTime = l.getAttributes().get("LoadTime").getContent().toString();
            } else {
                loadTime = "    ";
            }

            lStr = baseDllName
                    + "    "
                    + fullDllName
                    + "    "
                    + loadTime
                    + "    0x"
                    + l.getaS().getVirtualAddressHex()
                    + "    0x"
                    + l.getaS().getPhysicalAddressHex()
                    + "    0x"
                    + l.getaS().getOffsetInFileHex();
        } catch (Exception ex) {
            this.notifyObservers(ex);
        }

        return lStr;
    }

    public String unMapConnection(EntityConnection c) {
        String cStr = null;

        try {
            EntityAddressInfo addressInfo = null;
            if (c instanceof EntityTCPConnection) {
                addressInfo = (EntityAddressInfo) c.getAttributes().get("AddressInfo").getContent();
            }
            EntityLocalAddress localAddress = null;
            if (c instanceof EntityUDPConnection) {
                localAddress = (EntityLocalAddress) c.getAttributes().get("LocalAddres").getContent();
            }
            String tipo = "N/D";
            if (c instanceof EntityTCPConnection) {
                tipo = "TCP";
            } else {
                tipo = "UDP";
            }
            String local = "N/D";
            String remota = "N/D";
            if (c instanceof EntityTCPConnection && addressInfo != null) {
                if (addressInfo.getAttributes().get("Local").getContent() != null) {
                    local = addressInfo.getAttributes().get("Local").getContent().toString();
                }
                if (addressInfo.getAttributes().get("Remota").getContent() != null) {
                    remota = addressInfo.getAttributes().get("Remota").getContent().toString();
                }
            } else {
                if (localAddress.getAttributes().get("Local").getContent() != null) {
                    local = localAddress.getAttributes().get("Local").getContent().toString();
                }
            }

            String LocalPort = "N/D";
            String RemotePort = "N/D";
            if (c instanceof EntityTCPConnection) {
                LocalPort = String.valueOf(Conversor.hexToInt(c.getAttributes().get("LocalPort").getContent().toString()));
                RemotePort = String.valueOf(Conversor.hexToInt(c.getAttributes().get("RemotePort").getContent().toString()));
            } else {
                if (c instanceof EntityUDPConnection) {
                    LocalPort = String.valueOf(Conversor.hexToInt(c.getAttributes().get("Port").getContent().toString()));
                }
            }
            String state = "N/D";
            if (c instanceof EntityTCPConnection) {
                if (c.getAttributes().get("State").getContent() != null) {
                    EntityWindowsConnectionState s = (EntityWindowsConnectionState) c.getAttributes().get("State").getContent();
                    state = s.getCurrentState();
                }
            }
            String ipVersion = "N/D";
            if (c.getAttributes().get("AddressFamily").getEntity() != null) {
                if (c.getAttributes().get("AddressFamily").getEntity().getAttributes().get("IPVersion").getContent() != null) {
                    ipVersion = c.getAttributes().get("AddressFamily").getEntity().getAttributes().get("IPVersion").getContent().toString();
                }
            }
            String createTime = c.getAttributes().get("CreateTime").getContent().toString();
            if (createTime.equals("")) {
                createTime = "N/D";
            }
            String virtualAddressHex = c.getaS().getPhysicalAddressHex();
            String physicalAddressHex = c.getaS().getPhysicalAddressHex();
            String offsetInFileHex = c.getaS().getOffsetInFileHex();
            long processID = 0;
            String processIDStr = "";
            if (c.getAttributes().get("Owner").getContent() != null) {
                EntityWindows732Process process = (EntityWindows732Process) c.getAttributes().get("Owner").getContent();
                if (process.getAttributes().get("UniqueProcessId").getContent() != null) {
                    processID = Conversor.hexToLong(process.getAttributes().get("UniqueProcessId").getContent().toString());
                }
                processIDStr = String.valueOf(processID);
            }
            String flink = "";
            String blink = "";
            if (c.getAttributes().get("ListEntry") != null) {
                EntityList list = (EntityList) c.getAttributes().get("ListEntry").getContent();
                flink = Conversor.longToHexString(list.getfLink());
                blink = Conversor.longToHexString(list.getbLink());
            }
            cStr = tipo
                    + "    "
                    + local
                    + "    "
                    + remota
                    + "    "
                    + LocalPort
                    + "    "
                    + RemotePort
                    + "    "
                    + state
                    + "    "
                    + createTime
                    + "    "
                    + ipVersion
                    + "    "
                    + processIDStr
                    + "    0x"
                    + physicalAddressHex
                    + "    0x"
                    + offsetInFileHex
                    + "    0x"
                    + flink
                    + "    0x"
                    + blink;
        } catch (Exception ex) {
            ex.printStackTrace();
            this.notifyObservers(ex);
        }

        return cStr;
    }

    public String unMapSocket(EntitySocket s) {
        String sStr = "";

        String state = "    ";
        if (s.getAttributes().get("State") != null) { // En Win7x64 no se dispone del dato
            EntityWindowsConnectionState stateEntity = (EntityWindowsConnectionState) s.getAttributes().get("State").getContent();
            state = stateEntity.getCurrentState();
        } else {
            state = "N/D";
        }
        long owner = 0;
        if (s.getAttributes().get("Owner").getContent() != null) {
            EntityProcess process = (EntityProcess) s.getAttributes().get("Owner").getContent();
            owner = process.getProcessID();
        }
        EntityLocalAddress localAddress = (EntityLocalAddress) s.getAttributes().get("LocalAddress").getContent();
        String local = "N/D    ";
        if (localAddress.getAttributes().get("Local").getContent() != null) {
            local = localAddress.getAttributes().get("Local").getContent().toString();
        }
        String port = "    ";
        if (s.getAttributes().get("Port").getContent() != null) {
            port = Conversor.hexToLong(s.getAttributes().get("Port").getContent().toString()) + "";
        }
        String createTime = "     ";
        if (s.getAttributes().get("CreateTime").getContent() != null) {
            createTime = s.getAttributes().get("CreateTime").getContent().toString();
        }
        sStr = state
                + "    "
                + String.valueOf(owner)
                + "    "
                + local
                + "    "
                + port
                + "    "
                + createTime
                + "    0x"
                + s.getaS().getPhysicalAddressHex()
                + "    0x"
                + s.getaS().getOffsetInFileHex();

        return sStr;
    }

    public String unMap(EntitySSDT s) {
        String sStr = "";

        sStr = s.getTag()
                + " - "
                + s.getDescription()
                + "    "
                + "VA:"
                + "    "
                + "    0x"
                + s.getaS().getVirtualAddressHex()
                + "    "
                + "PA:"
                + "    0x"
                + s.getaS().getPhysicalAddressHex()
                + "    "
                + "Offset:"
                + "    0x"
                + s.getaS().getOffsetInFileHex();
        
        sStr += "/n";
        for(EntitySDE sde : s.getEntryList()){
            sStr += sde.getEntryDescription() + "VA: 0x" + sde.getaS().getVirtualAddressHex() + "\n";
        }

        return sStr;
    }

    public String unMap(EntityMalware m) {
        String mStr = "";

        mStr = m.getTag();

        return mStr;
    }

    @Override
    public void visit(EntityProcess process) {
        this.registerLog(this._fullLogPath, this.unMapProcess(process));
    }

    @Override
    public void visit(EntityLib lib) {
        this.registerLog(this._fullLogPath, this.unMapLib(lib));
    }

    @Override
    public void visit(EntityConnection connection) {
        this.registerLog(this._fullLogPath, this.unMapConnection(connection));
    }

    @Override
    public void visit(EntitySocket socket) {
        this.registerLog(this._fullLogPath, this.unMapSocket(socket));
    }

    @Override
    public void visit(EntityMalware malware) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(EntityThread thread) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(EntityAddressSpace aS) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void generateLogName(String currentSessionID, String logPath) {
        try {
            String logName = "log_analyzer_" + currentSessionID;
            this._logName = logName;
            this._fullLogPath = logPath + "\\" + this._logName + ".txt";

            /**
             * Registro encabezado del archivo de log
             */
            String content = ".::.BIP-M Framework.::.";
            this.registerLog(this._fullLogPath, content);
            content = "------------------------------------------------------------------------------------------------";
            this.registerLog(this._fullLogPath, content);
            content = "Logs de análisis realizados en la sesión ID: " + currentSessionID;
            this.registerLog(this._fullLogPath, content);

            content = "Fecha: " + DateManager.getActualDateWithISOFormat();
            this.registerLog(this._fullLogPath, content);

            content = "------------------------------------------------------------------------------------------------";
            this.registerLog(this._fullLogPath, content);
        } catch (Exception ex) {
            this.notifyObservers(ex);
        }
    }
}

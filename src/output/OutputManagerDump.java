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
import entities.process.EntityProcess;
import entities.process.EntityThread;
import entities.process.EntityWindowsProcess;
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
public class OutputManagerDump extends OutputManager implements IEntityVisitor {

    protected static OutputManagerDump _instance;

    private OutputManagerDump() {
        this.generateLogName();
    }

    private OutputManagerDump(String currentSessionID, String logPath) {
        this._currentSessionID = currentSessionID;
        this._logPath = logPath;
        this.generateLogName();
    }

    public static OutputManagerDump getInstance() {
        if (_instance == null) {
            _instance = new OutputManagerDump();
        }

        return _instance;
    }

    public static OutputManagerDump getInstance(String currentSessionID, String logPath) {
        if (_instance == null) {
            _instance = new OutputManagerDump(currentSessionID, logPath);
        }

        return _instance;
    }

    @Override
    public void update(Object o) {
        this.registerLog(this._fullLogPath, o);
    }

    /**
     * Registra el log correspondiente
     *
     * @param path
     * @param param
     */
    @Override
    public void registerLog(String path, Object param) {
        try {
            FileManager fileManager = FileManager.getInstance();

            if (param instanceof String) {
                String paramStr = (String) param;
                fileManager.write(path, paramStr);
            } else {
                if (param instanceof Entity) {
                    Entity e = (Entity) param;
                    e.accept(this);
                } /*else if (param instanceof EntityProcess) {
                 EntityProcess p = (EntityProcess) param;
                 String pStr = this.unMapProcess(p);
                 fileManager.write(path, pStr);
                 } else {
                 if (param instanceof EntityLib) {
                 EntityLib l = (EntityLib) param;
                 String lStr = this.unMapLib(l);
                 fileManager.write(path, lStr);
                 } else {
                 if (param instanceof EntityConnection) {
                 EntityConnection c = (EntityConnection) param;
                 String cStr = this.unMapConnection(c);
                 fileManager.write(path, cStr);
                 }
                 }
                 }*/

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
            String logName = "log_dump_" + this._currentSessionID;
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
            content = "Logs de parseo de dump realizados en la sesión ID: " + this._currentSessionID;
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
                    + "    "
                    + inheritedFromPIDStr
                    + "    "
                    + processIDStr
                    + "    "
                    + p.getAttributes().get("CreateTime").getContent().toString()
                    + "    "
                    + p.getAttributes().get("ExitTime").getContent().toString()
                    + "    "
                    + p.getStateLabel()
                    + "    "
                    + "0x"
                    + p.getaS().getPhysicalAddressHex()
                    + "    "
                    + "0x"
                    + p.getaS().getOffsetInFileHex();
        } catch (Exception ex) {
            ex.printStackTrace();
            this.notifyObservers(ex);
        }

        return pStr;
    }

    public String unMapLib(EntityLib l) {
        String lStr = null;

        try {
            /**
             * Construye una cadena con los datos de la entidad
             */
            String baseDllName = "N/D";
            String fullDllName = "N/D";
            EntityUnicodeString unicodeString = null;
            if (l.getAttributes().get("BaseDllName").getContent() != null) {
                unicodeString = (EntityUnicodeString) l.getAttributes().get("BaseDllName").getContent();
                if (unicodeString.getAttributes().get("Name").getContent() != null) {
                    baseDllName = unicodeString.getAttributes().get("Name").getContent().toString();
                }
            } else {
                baseDllName = "N/D";
            }
            if (l.getAttributes().get("FullDllName").getContent() != null) {
                unicodeString = (EntityUnicodeString) l.getAttributes().get("FullDllName").getContent();
                if (unicodeString.getAttributes().get("Name").getContent() != null) {
                    fullDllName = unicodeString.getAttributes().get("Name").getContent().toString();
                }
            } else {
                fullDllName = "N/D";
            }
            lStr = baseDllName
                    + "    "
                    + fullDllName
                    + "    "
                    + "0x"
                    + l.getaS().getPhysicalAddressHex()
                    + "    "
                    + "0x"
                    + l.getaS().getOffsetInFileHex();
        } catch (Exception ex) {
            ex.printStackTrace();
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
                if (localAddress != null) {
                    if (localAddress.getAttributes().get("Local").getContent() != null) {
                        local = localAddress.getAttributes().get("Local").getContent().toString();
                    }
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
            String physicalAddressHex = c.getaS().getPhysicalAddressHex();
            String offsetInFileHex = c.getaS().getOffsetInFileHex();
            long processID = 0;
            String processIDStr = "N/D";
            if (c.getAttributes().get("Owner").getContent() != null) {
                EntityWindowsProcess process = (EntityWindowsProcess) c.getAttributes().get("Owner").getContent();
                if (process.getAttributes().get("UniqueProcessId").getContent() != null) {
                    processID = Conversor.hexToLong(process.getAttributes().get("UniqueProcessId").getContent().toString());
                }
                processIDStr = String.valueOf(processID);
            }
            String flink = "    ";
            String blink = "    ";
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
                    + "    "
                    + "0x"
                    + physicalAddressHex
                    + "    "
                    + "0x"
                    + offsetInFileHex
                    + "    "
                    + "0x"
                    + flink
                    + "    "
                    + "0x"
                    + blink;
        } catch (Exception ex) {
            ex.printStackTrace();
            this.notifyObservers(ex);
        }

        return cStr;
    }

    public String unMapSocket(EntitySocket s) {
        String sStr = "";
        try {
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
        } catch (Exception ex) {
            ex.printStackTrace();
            this.notifyObservers(ex);
        }

        return sStr;
    }

    @Override
    public void visit(EntityProcess process) {
        try {
            FileManager fileManager = FileManager.getInstance();
            fileManager.write(this._fullLogPath, this.unMapProcess(process));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void visit(EntityLib lib) {
        try {
            FileManager fileManager = FileManager.getInstance();
            fileManager.write(this._fullLogPath, this.unMapLib(lib));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void visit(EntityConnection connection) {
        try {
            FileManager fileManager = FileManager.getInstance();
            fileManager.write(this._fullLogPath, this.unMapConnection(connection));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void visit(EntitySocket socket) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
            String logName = "log_dump_" + currentSessionID;
            this._logName = logName;
            this._fullLogPath = logPath + "\\" + this._logName + ".txt";

            /**
             * Registro encabezado del archivo de log
             */
            String content = ".::.BIP-M Framework.::.";
            this.registerLog(this._fullLogPath, content);
            content = "------------------------------------------------------------------------------------------------";
            this.registerLog(this._fullLogPath, content);
            content = "Logs de parseo de dump realizados en la sesión ID: " + currentSessionID;
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

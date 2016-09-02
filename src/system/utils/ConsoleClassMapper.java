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
package system.utils;

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
import entities.process.EntityProcess;
import entities.process.EntityThread;
import entities.process.EntityWindows732Process;
import entities.process.EntityWindowsProcess;
import entities.translation.EntityAddressSpace;
import java.util.ArrayList;

/**
 *
 * @author Gonzalo
 */
public class ConsoleClassMapper {

    public void print(String str) {
        System.out.println(str);
    }

    public void printEntity(EntityProcess p) {
        String processStr = p.getAttributes().get("ImageFileName").getContent().toString() + "    ";
        String processIDStr = String.valueOf(Conversor.hexToLong(p.getAttributes().get("UniqueProcessId").getContent().toString())) + "    ";
        String inheritedFromPIDStr = String.valueOf(Conversor.hexToLong(p.getAttributes().get("InheritedFromUniqueProcessId").getContent().toString())) + "    ";
        System.out.println(processStr
                + "    "
                + inheritedFromPIDStr
                + "    "
                + processIDStr
                + "    "
                //+ DateManager.hexToDateTimeStringFormat(p.getAttributes().get("CreateTime").getAttributesContent().toString(), formatStr)
                + p.getAttributes().get("CreateTime").getContent().toString()
                + "    "
                //+ DateManager.hexToDateTimeStringFormat(p.getAttributes().get("EgetAttributesContentgetContent().toString(), formatStr)
                + p.getAttributes().get("ExitTime").getContent().toString()
                + "    "
                + p.getStateLabel()
                + "    0x"
                + p.getaS().getVirtualAddressHex()
                + "    0x"
                + p.getaS().getPhysicalAddressHex()
                + "    0x"
                + p.getaS().getOffsetInFileHex());
    }

    public void printEntity(EntityConnection c) {
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
                LocalPort = Integer.valueOf(Conversor.hexToInt(c.getAttributes().get("LocalPort").getContent().toString())).toString();
                RemotePort = Integer.valueOf(Conversor.hexToInt(c.getAttributes().get("RemotePort").getContent().toString())).toString();
            } else {
                if (c instanceof EntityUDPConnection) {
                    LocalPort = Integer.valueOf(Conversor.hexToInt(c.getAttributes().get("Port").getContent().toString())).toString();
                }
            }
            String state = "N/D";
            if (c instanceof EntityTCPConnection) {
                if (c.getAttributes().get("State").getContent() != null) {
                    EntityWindowsConnectionState s = (EntityWindowsConnectionState) c.getAttributes().get("State").getContent();
                    state = s.getCurrentState() + "    ";
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
            String virtualAddressHex = c.getaS().getVirtualAddressHex();
            if (virtualAddressHex == null) {
                virtualAddressHex = "N/D";
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
                EntityList list = (EntityList) c.getAttributes().get("ListEntry").getEntity();
                flink = list.getfLinkHex();
                blink = list.getbLinkHex();
            }
            System.out.println(tipo
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
                    + virtualAddressHex
                    + "    0x"
                    + physicalAddressHex
                    + "    0x"
                    + offsetInFileHex
                    + "    0x"
                    + flink
                    + "    0x"
                    + blink);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printEntity(EntitySocket s) {
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
        String local = "N/D";
        if (localAddress != null) {
            if (localAddress.getAttributes().get("Local").getContent() != null) {
                local = localAddress.getAttributes().get("Local").getContent().toString();
            }
        }
        String port = "N/D";
        if (s.getAttributes().get("Port").getContent() != null) {
            port = String.valueOf(Conversor.hexToLong(s.getAttributes().get("Port").getContent().toString()));
        }
        String createTime = "N/D";
        if (s.getAttributes().get("CreateTime").getContent() != null) {
            createTime = s.getAttributes().get("CreateTime").getContent().toString();
        }
        System.out.println(state
                + "    "
                + String.valueOf(owner)
                + "    "
                + local
                + "    "
                + port
                + "    "
                + createTime
                + "    "
                + "0x"
                + s.getaS().getPhysicalAddressHex()
                + "    "
                + "0x"
                + s.getaS().getOffsetInFileHex());
    }

    public void printEntity(EntityThread t) {
        System.out.println("Thread encontrado");
    }

    public void printEntity(EntityLib l) {
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
        System.out.println(baseDllName
                + "    "
                + fullDllName
                + "    "
                + loadTime
                + "    0x"
                + l.getaS().getVirtualAddressHex()
                + "    0x"
                + l.getaS().getPhysicalAddressHex()
                + "    0x"
                + l.getaS().getOffsetInFileHex());
    }

    public void printEntity(EntityAddressSpace aS) {
        System.out.println("Virtual Address:    "
                + "    0x"
                + aS.getVirtualAddressHex()
                + "    "
                + "Physycal Address:"
                + "    0x"
                + aS.getPhysicalAddressHex()
                + "    "
                + "Offset:"
                + "    0x"
                + aS.getOffsetInFileHex());
    }

    public void printProcessesDetail(ArrayList<EntityProcess> processList) {
        System.out.println("Procesos encontrados = " + processList.size() + ". Procesos ocultos = " + ".");
        String formatStr = "yyyy-MM-dd HH:mm:ssZ";
        System.out.println("Nombre    InheritedFromPID    PID    Create Time    Exit Time    State    PhysicalAddress    FileOffset");
        System.out.println("--------------------------------------------------------------------------------------------------------");
        for (EntityProcess p : processList) {
            String processStr = p.getAttributes().get("ImageFileName").getContent().toString();
            String processIDStr = String.valueOf(Conversor.hexToLong(p.getAttributes().get("UniqueProcessId").getContent().toString()));
            String inheritedFromPIDStr = String.valueOf(Conversor.hexToLong(p.getAttributes().get("InheritedFromUniqueProcessId").getContent().toString()));
            System.out.println(processStr
                    + "    "
                    + inheritedFromPIDStr
                    + "    "
                    + processIDStr
                    + "    "
                    //+ DateManager.hexToDateTimeStringFormat(p.getAttributes().get("CreateTime").getAttributesContent().toString(), formatStr)
                    + p.getAttributes().get("CreateTime").getContent().toString()
                    + "    "
                    //+ DateManager.hexToDateTimeStringFormat(p.getAttributes().get("EgetAttributesContentgetContent().toString(), formatStr)
                    + p.getAttributes().get("ExitTime").getContent().toString()
                    + "    "
                    + p.getStateLabel()
                    + "    0x"
                    + p.getaS().getPhysicalAddressHex()
                    + "    0x"
                    + p.getaS().getOffsetInFileHex());
        }
    }

    public void printConnectionsDetail(ArrayList<EntityConnection> connectionList) {
        System.out.println("Conexiones encontradas = " + connectionList.size() + ".");
        System.out.println("Tipo    Local    Remota    LocalPort    RemotePort    State    Create Time    IP Version    ProcessID    PA    FileOffset");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------");
        for (EntityConnection c : connectionList) {
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
                String local = "    ";
                String remota = "    ";
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

                String LocalPort = "    ";
                String RemotePort = "    ";
                if (c instanceof EntityTCPConnection) {
                    LocalPort = Integer.valueOf(Conversor.hexToInt(c.getAttributes().get("LocalPort").getContent().toString())) + "";
                    RemotePort = Integer.valueOf(Conversor.hexToInt(c.getAttributes().get("RemotePort").getContent().toString())) + "";
                } else {
                    if (c instanceof EntityUDPConnection) {
                        LocalPort = Integer.valueOf(Conversor.hexToInt(c.getAttributes().get("Port").getContent().toString())) + "";
                    }
                }
                String state = "    ";
                if (c instanceof EntityTCPConnection) {
                    if (c.getAttributes().get("State").getContent() != null) {
                        EntityWindowsConnectionState s = (EntityWindowsConnectionState) c.getAttributes().get("State").getContent();
                        state = s.getCurrentState();
                    }
                }
                String ipVersion = "    ";
                if (c.getAttributes().get("AddressFamily").getEntity() != null) {
                    if (c.getAttributes().get("AddressFamily").getEntity().getAttributes().get("IPVersion").getContent() != null) {
                        ipVersion = c.getAttributes().get("AddressFamily").getEntity().getAttributes().get("IPVersion").getContent().toString();
                    }
                }
                String createTime = c.getAttributes().get("CreateTime").getContent().toString();
                if (createTime.equals("")) {
                    createTime = "    ";
                }
                String physicalAddressHex = c.getaS().getPhysicalAddressHex();
                String offsetInFileHex = c.getaS().getOffsetInFileHex();
                long processID = 0;
                String processIDStr = "    ";
                if (c.getAttributes().get("Owner").getContent() != null) {
                    EntityWindows732Process process = (EntityWindows732Process) c.getAttributes().get("Owner").getContent();
                    if (process.getAttributes().get("UniqueProcessId").getContent() != null) {
                        processID = Conversor.hexToLong(process.getAttributes().get("UniqueProcessId").getContent().toString());
                    }
                    processIDStr = String.valueOf(processID);
                }

                System.out.println(tipo
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
                        + offsetInFileHex);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void printSocketsDetail(ArrayList<EntitySocket> socketList) {
        System.out.println("Sockets encontrados = " + socketList.size() + ".");
        String formatStr = "yyyy-MM-dd HH:mm:ssZ";
        System.out.println("Local    Port    Create Time    PA    FileOffset");
        System.out.println("---------------------------------------------------------------------------------");
        for (EntitySocket s : socketList) {
            EntityAddressInfo addressInfo = (EntityAddressInfo) s.getAttributes().get("LocalAddress").getContent();
            String local = addressInfo.getAttributes().get("Local").getContent().toString();
            String port = s.getAttributes().get("Port").getContent().toString();
            System.out.println(local
                    + "    "
                    + port
                    + "    "
                    + s.getAttributes().get("CreateTime").getContent().toString()
                    + "    0x"
                    + s.getaS().getPhysicalAddressHex()
                    + "    0x"
                    + s.getaS().getOffsetInFileHex());
        }
    }

    public void printModuleDetail(ArrayList<EntityLib> moduleList) {
        //Esto está escrito a medias, lo tengo que terminar. Simplemente hice un copy paste y
        //cambié algunas variables.
        System.out.println("Librerías encontradas = " + moduleList.size() + ".");
        String formatStr = "yyyy-MM-dd HH:mm:ssZ";
        String baseDllName = "";
        String fullDllName = "";
        String loadTime = "";
        System.out.println("BaseDllName    FullDllName    LoadTime    PA    FileOffset");
        System.out.println("-----------------------------------------------------------");
        for (EntityLib l : moduleList) {
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
            System.out.println(baseDllName
                    + "    "
                    + fullDllName
                    + "    "
                    + loadTime
                    + "    0x"
                    + l.getaS().getPhysicalAddressHex()
                    + "    0x"
                    + l.getaS().getOffsetInFileHex());
        }
    }

}

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
package dump.ooss;

import dump.parsers.Parser;
import dump.parsers.utils.pools.PoolManagerWin64;
import entities.Entity;
import entities.connection.EntityWindows764Socket;
import entities.connection.EntityWindows764TCPConnection;
import entities.lib.EntityLibWindows764DLL;
import entities.malware.EntityWin764SSDT;
import entities.process.EntityWindows764Process;
import entities.process.EntityWindows764Thread;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Gonzalo
 */
public class Windows764OS extends Windows {

    public Windows764OS() {
        super();

        try {
            this._entityList = new HashMap<String, Entity>();
            EntityWindows764Process process = new EntityWindows764Process();
            this._entityList.put(EntityWindows764Process._TAG, process);
            EntityWindows764Thread thread = new EntityWindows764Thread();
            this._entityList.put(EntityWindows764Thread._TAG, thread);
            EntityWindows764Socket socket = new EntityWindows764Socket();
            this._entityList.put(EntityWindows764Socket._TAG, socket);
            EntityWindows764TCPConnection TCPConnection = new EntityWindows764TCPConnection();
            this._entityList.put(EntityWindows764TCPConnection._TAG, TCPConnection);
            /*EntityWindows764UDPConnection UDPConnection = new EntityWindows764UDPConnection();
             this._entityList.put(EntityWindows764UDPConnection._TAG, UDPConnection);*/
            EntityLibWindows764DLL modules = new EntityLibWindows764DLL();
            this._entityList.put(EntityLibWindows764DLL._TAG, modules);
            EntityWin764SSDT ssdt = new EntityWin764SSDT();
            this._entityList.put(EntityWin764SSDT._TAG, ssdt);
            this.setPoolManager(new PoolManagerWin64());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Windows764OS(Map<String, Entity> _entityList) {
        super(_entityList);
    }

    @Override
    public void setParserByTag(String tag, Parser parser) {
        switch (tag) {
            case "TCPConnection":
                this.getEntityList().get("TCPConnection").setParser(parser);
                this.getEntityList().get("UDPConnection").setParser(parser);
                break;
            default:
                this.getEntityList().get(tag).setParser(parser);
                break;
        }
    }

}

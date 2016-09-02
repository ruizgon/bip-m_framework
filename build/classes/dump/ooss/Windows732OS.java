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
import dump.parsers.utils.pools.PoolManagerWin32;
import entities.Entity;
import entities.connection.EntityWindows732Socket;
import entities.connection.EntityWindows732TCPConnection;
import entities.lib.EntityLibWindows732DLL;
import entities.malware.EntityWin732SSDT;
import entities.process.EntityWindows732Process;
import entities.process.EntityWindows732Thread;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Gonzalo
 */
public class Windows732OS extends Windows {

    /*
     * Setea cada Entity correspondiente
     */
    public Windows732OS() {
        super();
        try{
        this._entityList = new HashMap<String, Entity>();
        EntityWindows732Process process = new EntityWindows732Process();
        this._entityList.put(EntityWindows732Process._TAG, process);
        EntityWindows732Thread thread = new EntityWindows732Thread();
        this._entityList.put(EntityWindows732Thread._TAG, thread);
        EntityWindows732Socket socket = new EntityWindows732Socket();
        this._entityList.put(EntityWindows732Socket._TAG, socket);
        EntityWindows732TCPConnection TCPConnection = new EntityWindows732TCPConnection();
        this._entityList.put(EntityWindows732TCPConnection._TAG, TCPConnection);
        /*EntityWindows732UDPConnection UDPConnection = new EntityWindows732UDPConnection();
        this._entityList.put(EntityWindows732UDPConnection._TAG, UDPConnection);*/
        EntityLibWindows732DLL modules = new EntityLibWindows732DLL();
        this._entityList.put(EntityLibWindows732DLL._TAG, modules);
        EntityWin732SSDT ssdt = new EntityWin732SSDT();
        this._entityList.put(EntityWin732SSDT._TAG, ssdt);
        this.setPoolManager(new PoolManagerWin32());
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public Windows732OS(Map<String, Entity> _entityList) {
        super(_entityList);
    }

    @Override
    public void setParserByTag(String tag, Parser parser) {
        switch (tag) {
            case "TCPConnection":
                this.getEntityList().get("TCPConnection").setParser(parser);
                this.getEntityList().get("UDPConnection").setParser(parser);
                break;
            case "Process":
                this.getEntityList().get("Thread").setParser(parser);
                this.getEntityList().get(tag).setParser(parser);
                break;
            default:
                this.getEntityList().get(tag).setParser(parser);
                break;
        }
    }


}

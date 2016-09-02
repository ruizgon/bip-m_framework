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
import entities.connection.EntityConnection;
import entities.connection.EntitySocket;
import entities.lib.EntityLib;
import entities.malware.EntityMalware;
import entities.process.EntityProcess;
import entities.process.EntityThread;
import entities.translation.EntityAddressSpace;
import system.utils.ConsoleClassMapper;
import system.utils.IEntityVisitor;

/**
 *
 * @author Gonzalo
 */
public class OutputManagerConsole extends OutputManager implements IEntityVisitor {

    protected static OutputManagerConsole _instance;

    private OutputManagerConsole() {

    }

    private OutputManagerConsole(String currentSessionID, String logPath) {
        this._currentSessionID = currentSessionID;
        this._logPath = logPath;
    }

    public static OutputManagerConsole getInstance() {
        if (_instance == null) {
            _instance = new OutputManagerConsole();
        }

        return _instance;
    }

    public static OutputManagerConsole getInstance(String currentSessionID, String logPath) {
        if (_instance == null) {
            _instance = new OutputManagerConsole(currentSessionID, null);
        }

        return _instance;
    }

    @Override
    public void registerLog(String path, Object param) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getLog() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void print(Object o) {

        ConsoleClassMapper console = new ConsoleClassMapper();
        /*if (o instanceof EntityProcess) {
         this.print((EntityProcess) o);
         }
         if (o instanceof EntityLib) {
         this.print((EntityLib) o);
         }
         if (o instanceof EntityConnection) {
         this.print((EntityConnection) o);
         }
         if (o instanceof EntitySocket) {
         this.print((EntitySocket) o);
         }*/
        if (o instanceof String) {
            this.print((String) o);
        } else if (o instanceof Entity) {
            Entity e = (Entity) o;
            e.accept(this);
        }
    }

    public void print(String cadena) {
        ConsoleClassMapper console = new ConsoleClassMapper();
        console.print(cadena);
    }

    public void print(EntityProcess process) {
        ConsoleClassMapper console = new ConsoleClassMapper();
        console.printEntity(process);
    }

    public void print(EntityLib lib) {
        ConsoleClassMapper console = new ConsoleClassMapper();
        console.printEntity(lib);
    }

    public void print(EntityConnection connection) {
        ConsoleClassMapper console = new ConsoleClassMapper();
        console.printEntity(connection);

    }

    public void print(EntitySocket socket) {
        ConsoleClassMapper console = new ConsoleClassMapper();
        console.printEntity(socket);

    }

    public void print(EntityThread thread) {
        ConsoleClassMapper console = new ConsoleClassMapper();
        console.printEntity(thread);

    }

    @Override
    public void visit(EntityAddressSpace aS) {
        ConsoleClassMapper console = new ConsoleClassMapper();
        console.printEntity(aS);
    }

    @Override
    public void generateLogName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Object o) {
        this.print(o);
    }

    @Override
    public void visit(EntityProcess process) {
        this.print(process);
    }

    @Override
    public void visit(EntityLib lib) {
        this.print(lib);
    }

    @Override
    public void visit(EntityConnection connection) {
        this.print(connection);
    }

    @Override
    public void visit(EntitySocket socket) {
        this.print(socket);
    }

    @Override
    public void visit(EntityMalware malware) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(EntityThread thread) {
        this.print(thread);
    }

    @Override
    public void generateLogName(String currentSessionID, String logPath) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

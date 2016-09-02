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
package persistence.database;

import analyzer.states.ProcessState;
import entities.EntityOwner;
import entities.lib.EntityLib;
import entities.process.EntityProcess;
import entities.process.EntityThread;
import java.util.ArrayList;

/**
 *
 * @author juani
 */
public abstract class MapperProcess extends MapperSeeker{
    public abstract EntityProcess getProcessById (int id);
    public abstract EntityProcess getProcessByName (String name);
    public abstract EntityProcess getProcessByVirtualAddress (String virtualAddress);
    public abstract EntityProcess getProcessByOwner (EntityOwner owner);
    public abstract ArrayList<EntityProcess> getProcessList (ProcessState _processState, EntityProcess _entityProcess);
    public abstract ArrayList<EntityThread> getThreadsByProcess (EntityProcess process);
    public abstract ArrayList<EntityLib> getLibsByProcess (EntityProcess process);
}

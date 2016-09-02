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
package persistence.analyzer_service;

import analyzer.states.ProcessState;
import entities.process.EntityProcess;
import java.util.ArrayList;

/**
 *
 * @author Gonzalo
 */
public interface IProcessService {

    public abstract ArrayList<EntityProcess> getAllProcesses(EntityProcess entity);
    
    public abstract ArrayList<EntityProcess> getProcessList(ProcessState state, EntityProcess entity);
    
    public abstract EntityProcess getFirstProcess(ProcessState state, EntityProcess entity);
    
    public abstract EntityProcess getNextProcess(ProcessState state, EntityProcess entity);
    
    public abstract EntityProcess getPrevProcess(ProcessState state, EntityProcess entity);

    public abstract EntityProcess getSpecificProcessById(int id);

    public abstract EntityProcess getSpecificProcessByName(String s, Object par1);
}

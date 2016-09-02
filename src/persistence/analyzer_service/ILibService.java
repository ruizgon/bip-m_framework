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

import analyzer.libs.SeekerLib;
import entities.lib.EntityLib;
import entities.malware.EntitySSDT;
import entities.process.EntityProcess;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gonzalo
 */
public interface ILibService {
    public abstract List<EntityLib> getLibList(SeekerLib lib, EntityLib entity);

    public abstract EntityLib getFirstLib();

    public abstract EntityLib getNextLib(EntityLib lib);

    public abstract EntityLib getPrevLib(EntityLib lib);
    
    public abstract ArrayList<EntityLib> getLibListByProcess(SeekerLib lib, EntityProcess process, EntityLib entity);
    
    public abstract List<EntitySSDT> getSSDTList(SeekerLib lib, List<EntitySSDT> entityList);
}

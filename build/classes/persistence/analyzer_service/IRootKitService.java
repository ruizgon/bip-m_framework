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

import analyzer.malware.SeekerRootkit;
import entities.Entity;
import entities.malware.EntityRootkit;
import java.util.ArrayList;

/**
 *
 * @author Gonzalo
 */
public interface IRootKitService {

    public abstract ArrayList<EntityRootkit> getRootkitList(SeekerRootkit seekerRootkit, EntityRootkit entityRootkit);
    
    public abstract ArrayList<EntityRootkit> getRootkitList(SeekerRootkit seekerRootkit, EntityRootkit entityRootkit, ArrayList<EntityRootkit> entityRootkitList);

    public abstract EntityRootkit getFirstRootkit(EntityRootkit entityRootkit);

    public abstract EntityRootkit getNextRootkit(EntityRootkit entityRootkit);

    public abstract EntityRootkit getPrevRootkit(EntityRootkit entityRootkit);
    
    public abstract Entity getHookedStructure();
}

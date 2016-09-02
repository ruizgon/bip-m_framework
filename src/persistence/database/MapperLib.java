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

import entities.lib.EntityLib;
import entities.malware.EntitySSDT;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author juani
 */
public abstract class MapperLib extends MapperSeeker {
    public abstract EntityLib getLibByName (String name);
    public abstract List<EntityLib> getLibList (EntityLib _entityLib);
    public abstract EntityLib getLibByVirtualAddress (String _virtualAddress);
}

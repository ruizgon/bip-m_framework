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
package dump.strategy;

import dump.ooss.OperatingSystemStructure;
import entities.Entity;

/**
 *
 * @author Gonzalo
 */
public interface IDumpLoader {

    public abstract boolean isValidDump();

    public abstract void createStructure();

    public abstract void createStructure(OperatingSystemStructure structure);

    /*     
     public abstract void loadDumpFormatContent();
    
     public abstract void loadDumpFormatContent(DumpFormat dumpFormat);
     */
    
    public abstract String loadContent(java.util.Map<Object,Entity> entities);
    
    public abstract String loadContentEntity(Entity entity);
    
    public abstract int persistMetadata(Entity entity, String tag, String status);
}

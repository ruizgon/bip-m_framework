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
package entities;

import dump.parsers.interfaces.IEntityParserVisitor;
import java.util.HashMap;
import java.util.Map;
import system.utils.IEntityVisitor;

/**
 *
 * @author Gonzalo
 */
public class EntityKDBGWin64 extends EntityKDBG{

    public EntityKDBGWin64() {try {
            this.setTag(_TAG);

            this.setAttributes(new HashMap<String, EntityAttribute>());

            EntityAttribute a0 = new EntityAttribute("PsActiveProcessHead", 0x038, "", "int 64", false);
            this.getAttributes().put("PsActiveProcessHead", a0);
            EntityAttribute a1 = new EntityAttribute("PsLoadedModuleList", 0x030, "", "int 64", false);
            this.getAttributes().put("PsLoadedModuleList", a1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }

    @Override
    public void getAttributesContent(long position, Map.Entry<String, EntityAttribute> e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void accept(IEntityVisitor visitor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void accept(IEntityParserVisitor visitor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

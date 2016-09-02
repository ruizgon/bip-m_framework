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
package entities.lib;

import dump.parsers.interfaces.IEntityParserVisitor;
import entities.Entity;
import entities.EntityAttribute;
import java.util.HashMap;
import java.util.Map;
import system.utils.IEntityVisitor;

/**
 *
 * @author Gonzalo
 */
public class EntityImageExportDirectory extends Entity{

    public static String _SIGNATURE = "PE";
    
    public EntityImageExportDirectory() {
        try {

            this.setAttributes(new HashMap<String, EntityAttribute>());

            
            EntityAttribute a0 = new EntityAttribute("Signature", 0x000, "", "char", 2, false);
            a0.setContent(_SIGNATURE);
            this.getAttributes().put("Signature", a0);
            EntityAttribute a1 = new EntityAttribute("AddressOfFunctions", 0x01C, "", "int 32", false);
            this.getAttributes().put("AddressOfFunctions", a1);
            EntityAttribute a2 = new EntityAttribute("AddressOfNames", 0x020, "", "int 32", false);
            this.getAttributes().put("AddressOfNames", a2);
            EntityAttribute a3 = new EntityAttribute("AddressOfNamesOrdinals", 0x024, "", "int 32", false);
            this.getAttributes().put("AddressOfNamesOrdinals", a3);
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

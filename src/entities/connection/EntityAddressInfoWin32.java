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
package entities.connection;

import dump.parsers.connections.ParserWin32Connection;
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
public class EntityAddressInfoWin32 extends EntityAddressInfo {
    
    public static final boolean _RESOLEV_MULTIPLICIY = true;

    public EntityAddressInfoWin32() {
        try {


            
            this.setAttributes(new HashMap<String, EntityAttribute>());
            EntityAttribute a0 = new EntityAttribute("Local", 0x000, "ip", "int 32", 1, false, 3, true);
            this.getAttributes().put("Local", a0);
            EntityAttribute a1 = new EntityAttribute("Remota", 0x008, "ip", "int 32", 1, false, 1, true);
            this.getAttributes().put("Remota", a1);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Probar
     *
     * @param position
     */
    @Override
    public void getAttributesContent(long position, Map.Entry<String, EntityAttribute> e) {
        if (this.getParser() != null) {
            ParserWin32Connection parser = (ParserWin32Connection) this.getParser();
            EntityAddressInfo addressInfo = parser.getAddressInfoByOffset(position);
            this.setAttributes(addressInfo.getAttributes());
        }
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

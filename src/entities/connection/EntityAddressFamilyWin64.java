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

import dump.parsers.connections.ParserWin64Connection;
import dump.parsers.connections.ParserWin64Socket;
import dump.parsers.interfaces.IEntityParserVisitor;
import entities.EntityAttribute;
import java.util.HashMap;
import java.util.Map;
import system.utils.IEntityVisitor;

/**
 *
 * @author Gonzalo
 */
public class EntityAddressFamilyWin64 extends EntityAddressFamily {

    public static final Map<Integer, String> _IP_VERSION = new HashMap<Integer, String>();

    static {
        _IP_VERSION.put(2, "IPV4");
        _IP_VERSION.put(0x17, "IPV6");
    }

    public EntityAddressFamilyWin64() {
        try {

            this.setAttributes(new HashMap<String, EntityAttribute>());
            EntityAttribute a0 = new EntityAttribute("IPVersion", 0x014, "", "int 8", false);
            this.getAttributes().put("IPVersion", a0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void getAttributesContent(long position, Map.Entry<String, EntityAttribute> e) {
        if (this.getParser() != null) {
            if (this.getParser() instanceof ParserWin64Connection) {
                ParserWin64Connection parser = (ParserWin64Connection) this.getParser();
                EntityAddressFamily addressFamily = parser.getAddressFamilyByOffset(position, e);
                this.setAttributes(addressFamily.getAttributes());
            } else if (this.getParser() instanceof ParserWin64Socket) {
                ParserWin64Socket parser = (ParserWin64Socket) this.getParser();
                EntityAddressFamily addressFamily = parser.getAddressFamilyByOffset(position, e);
                this.setAttributes(addressFamily.getAttributes());
            }
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

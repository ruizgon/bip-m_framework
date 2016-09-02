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
import dump.parsers.connections.ParserWin32Socket;
import dump.parsers.interfaces.IEntityParserVisitor;
import entities.EntityAttribute;
import java.util.HashMap;
import java.util.Map;
import system.utils.IEntityVisitor;

/**
 *
 * @author Gonzalo
 */
public class EntityLocalAddressWin32 extends EntityLocalAddress {

    public EntityLocalAddressWin32() {
        try {

            this.setAttributes(new HashMap<String, EntityAttribute>());
            EntityAttribute a0 = new EntityAttribute("Local", 0x00C, "ip", "int 32", 1, false, 2, true);
            this.getAttributes().put("Local", a0);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void getAttributesContent(long position, Map.Entry<String, EntityAttribute> e) {
        if (this.getParser() != null) {
            if (this.getParser() instanceof ParserWin32Connection) {
                ParserWin32Connection parser = (ParserWin32Connection) this.getParser();
                EntityLocalAddress localAddress = parser.getLocalAddressByOffset(position);
                this.setAttributes(localAddress.getAttributes());
            } else {
                if (this.getParser() instanceof ParserWin32Socket) {
                    ParserWin32Socket parser = (ParserWin32Socket) this.getParser();
                    EntityLocalAddress localAddress = parser.getLocalAddressByOffset(position);
                    this.setAttributes(localAddress.getAttributes());
                }
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

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

import dump.parsers.ParserWin64;
import dump.parsers.interfaces.IEntityParserVisitor;
import static entities.EntityUnicodeString._TAG;
import java.util.HashMap;
import java.util.Map;
import system.utils.IEntityVisitor;

/**
 *
 * @author alberdij
 */
public class EntityUnicodeStringWin64 extends EntityUnicodeString {

    public static final long _SIZE_STRUCTURE = 0x10; // 3 elementos, 0x10 bytes

    public EntityUnicodeStringWin64() {

        try {
            this.setTag(_TAG);

            this.setAttributes(new HashMap<String, EntityAttribute>());

            EntityAttribute a0 = new EntityAttribute("Length", 0x000, "", "int 16", false);
            this.getAttributes().put("Length", a0);
            EntityAttribute a1 = new EntityAttribute("MaximumLength", 0x002, "", "int 16", false);
            this.getAttributes().put("MaximumLength", a1);
            EntityAttribute a2 = new EntityAttribute("Buffer", 0x008, "", "pointer64",false);
            this.getAttributes().put("Buffer", a2);
            EntityAttribute a3 = new EntityAttribute("Name", -1, "", "char", false);
            this.getAttributes().put("Name", a3);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void getAttributesContent(long position, Map.Entry<String, EntityAttribute> e) {
        if (this.getParser() != null) {
            ParserWin64 parser = (ParserWin64) this.getParser();
            EntityUnicodeStringWin64 unicodeString = (EntityUnicodeStringWin64) parser.getUnicodeStringContent(position,e);
            this.setAttributes(unicodeString.getAttributes());
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

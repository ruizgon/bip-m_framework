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
package entities.process;

import dump.parsers.interfaces.IEntityParserVisitor;
import dump.parsers.lib.ParserLibCrashDMPWin732;
import dump.parsers.process.ParserWin32Process;
import entities.EntityAttribute;
import java.util.HashMap;
import java.util.Map;
import system.utils.IEntityVisitor;

/**
 *
 * @author alberdij
 */
public class EntityWindows732PEB extends EntityPEB {

    public static final long _SIZE_STRUCTURE = 0x248; //91 elements, 0x248 bytes 

    public EntityWindows732PEB() {
        try {
            this.setTag(_TAG);

            this.setAttributes(new HashMap<String, EntityAttribute>());
            /**
             * String _nombre, int _position, String _complexContentType, String
             * _contentType, int _length, boolean _union, int
             * _pointerMultiplicity, boolean _bigEndian, Entity _entity
             */
            EntityAttribute a0 = new EntityAttribute("PebLdrData", 0x00C, "", "pointer32 _PebLdrData", 1, false, 1, false, new EntityPebLdrDataWindows732());
            this.getAttributes().put("PebLdrData", a0);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void getAttributesContent(long position, Map.Entry<String, EntityAttribute> e) {
        if (this.getParser() != null) {
            ParserWin32Process parser = (ParserWin32Process) this.getParser();
            EntityPEB peb = parser.getPEBByOffset(position);
            this.setAttributes(peb.getAttributes());
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

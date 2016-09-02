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
import dump.parsers.process.ParserWin32Thread;
import entities.EntityAttribute;
import java.util.HashMap;
import java.util.Map;
import system.utils.IEntityVisitor;

/**
 *
 * @author Gonzalo
 */
public class EntityWin732KTHREAD extends EntityKTHREAD {

    public static final String _TAG = "KTHREAD";
    public static final long _SIZE_STRUCTURE = 0x200; // 114 elementos, 0x200 bytes 

    public EntityWin732KTHREAD() {

        try {
            this.setTag(_TAG);

            this.setAttributes(new HashMap<String, EntityAttribute>());

            /**
             * String _nombre, int _position, String _complexContentType, String
             * _contentType, boolean _union, Entity _entity
             */
            EntityAttribute a0 = new EntityAttribute("_DISPATCHER_HEADER", 0x000, "", "EntityDispatcherHeader", false, new EntityWin732DispatcherHeader());
            a0.setEnabledParse(false);
            this.getAttributes().put("_DISPATCHER_HEADER", a0);
            /**
             * String _nombre, int _position, String _complexContentType, String
             * _contentType, boolean _union, Entity _entity
             */
            EntityAttribute a1 = new EntityAttribute("SSDT", 0x0BC, "", "pointer32 _EntitySSDT", 1, false, 0, false);
            this.getAttributes().put("SSDT", a1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void getAttributesContent(long position, Map.Entry<String, EntityAttribute> e) {
        if (this.getParser() != null) {
            ParserWin32Thread parser = (ParserWin32Thread) this.getParser();
            EntityKTHREAD kThread = parser.obtainKTHREADContent(position, e);
            this.setAttributes(kThread.getAttributes());
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

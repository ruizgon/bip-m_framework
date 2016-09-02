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
import dump.parsers.process.ParserWin64Process;
import entities.EntityAttribute;
import entities.EntityListWin64;
import java.util.HashMap;
import java.util.Map;
import system.utils.IEntityVisitor;

/**
 *
 * @author alberdij
 */
public class EntityPebLdrDataWindows764  extends EntityPebLdrData{

    //Esta entidad representa la estructura _PEB_LDR_DATA
    
    public static final long _SIZE_STRUCTURE = 0x58; // // 9 elements, 0x58 bytes (sizeof)
    
    public EntityPebLdrDataWindows764() {
        try {
            this.setTag(_TAG);

            this.setAttributes(new HashMap<String, EntityAttribute>());

            EntityAttribute a0 = new EntityAttribute("InLoadOrderModuleList", 0x010, null, "EntityList", false, new EntityListWin64());
            this.getAttributes().put("InLoadOrderModuleList", a0);
            EntityAttribute a1 = new EntityAttribute("InMemoryOrderModuleList", 0x020, null, "EntityList", false, new EntityListWin64());
            this.getAttributes().put("InMemoryOrderModuleList", a1);
            EntityAttribute a2 = new EntityAttribute("InInitializationOrderModuleList", 0x030, null, "EntityList", false, new EntityListWin64());
            this.getAttributes().put("InInitializationOrderModuleList", a2);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void getAttributesContent(long position, Map.Entry<String, EntityAttribute> e) {
        if (this.getParser() != null) {
            ParserWin64Process parser = (ParserWin64Process) this.getParser();
            EntityPebLdrData pebLdrData = parser.getPEBLdrDataByOffset(position);
            this.setAttributes(pebLdrData.getAttributes());
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

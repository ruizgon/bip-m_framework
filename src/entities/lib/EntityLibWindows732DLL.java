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

import entities.Entity;
import entities.EntityAttribute;
import entities.EntityListWin32;
import entities.EntityUnicodeStringWin32;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Gonzalo
 */
public class EntityLibWindows732DLL extends EntityLib {

    //Esta entidad representa la estructura _LDR_DATA_TABLE_ENTRY la cual contiene
    //toda la información de los módulos cargados.
    public static final long _SIZE_STRUCTURE = 0x78; // 24 elements, 0x78 bytes (sizeof)
    public static final long _ENTITY_LIST_OFFSET = 0x000;

    public EntityLibWindows732DLL() {
        try {
            this.setTag(_TAG);

            this.setAttributes(new HashMap<String, EntityAttribute>());

            EntityAttribute a0 = new EntityAttribute("InLoadOrderLinks", 0x000, "", "EntityList", false, new EntityListWin32());
            this.getAttributes().put("InLoadOrderLinks", a0);
            EntityAttribute a1 = new EntityAttribute("InMemoryOrderLinks", 0x008, "", "EntityList", false, new EntityListWin32());
            this.getAttributes().put("InMemoryOrderLinks", a1);
            EntityAttribute a2 = new EntityAttribute("InInitializationOrderLinks", 0x010, "", "EntityList", false, new EntityListWin32());
            this.getAttributes().put("InInitializationOrderLinks", a2);
            /**
             * String _nombre, int _position, String _complexContentType, String
             * _contentType, int _length, boolean _union
             */
            EntityAttribute a3 = new EntityAttribute("DllBase", 0x018, "", "pointer32", false);
            this.getAttributes().put("DllBase", a3);
            EntityAttribute a4 = new EntityAttribute("SizeOfImage", 0x020, "", "int 32", false);
            this.getAttributes().put("SizeOfImage", a4);
            /**
             * String _nombre, int _position, String _complexContentType, String
             * _contentType, int _length, boolean _union, int
             * _pointerMultiplicity, boolean _bigEndian
             */
            EntityAttribute a5 = new EntityAttribute("FullDllName", 0x024, "", "EntityUnicodeStringWindows732", false, new EntityUnicodeStringWin32());
            this.getAttributes().put("FullDllName", a5);
            /**
             * String _nombre, int _position, String _complexContentType, String
             * _contentType, int _length, boolean _union, int
             * _pointerMultiplicity, boolean _bigEndian
             */
            EntityAttribute a6 = new EntityAttribute("BaseDllName", 0x02c, "", "EntityUnicodeStringWindows732",false, new EntityUnicodeStringWin32());
            this.getAttributes().put("BaseDllName", a6);
            EntityAttribute a7 = new EntityAttribute("LoadCount", 0x038, "", "int 16", false);
            this.getAttributes().put("LoadCount", a7);
            EntityAttribute a8 = new EntityAttribute("LoadTime", 0x070, "windows file time", "int 64", true);
            this.getAttributes().put("LoadTime", a8);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void getAttributesContent(long position, Map.Entry<String, EntityAttribute> e) {
        if (this.getParser() != null) {
            Entity entity = this.getParser().parseEntityByOffset(position, null, null);

            this.setAttributes(entity.getAttributes());
        }
    }

}

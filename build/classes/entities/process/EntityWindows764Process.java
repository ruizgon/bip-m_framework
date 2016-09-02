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

import entities.Entity;
import entities.EntityAttribute;
import entities.EntityListWin64;
import java.util.HashMap;
import java.util.Map;
import system.utils.Conversor;

/**
 *
 * @author Gonzalo
 */
public class EntityWindows764Process extends EntityWindowsProcess {

    public static final long _SIZE_STRUCTURE = 0x4D0; //135 elementos, 0x4D0 bytes 
    public static final long _ENTITY_LIST_OFFSET = 0x188;

    public EntityWindows764Process() {

        try {
            this.setTag(_TAG);

            this.setAttributes(new HashMap<String, EntityAttribute>());

            EntityAttribute a0 = new EntityAttribute("KPROCESS", 0x000, "", "KPROCESS", false, new EntityWin764KPROCESS());//PCB Process Control Block
            a0.setEnabledParse(false);
            this.getAttributes().put("KPROCESS", a0);
            EntityAttribute a1 = new EntityAttribute("ProcessLock", 0x160, "", "int 64", false);
            this.getAttributes().put("ProcessLock", a1);
            EntityAttribute a2 = new EntityAttribute("CreateTime", 0x168, "windows file time", "int 64", true);
            this.getAttributes().put("CreateTime", a2);
            EntityAttribute a3 = new EntityAttribute("ExitTime", 0x170, "windows file time", "int 64", true);
            this.getAttributes().put("ExitTime", a3);
            EntityAttribute a4 = new EntityAttribute("RundownProtect", 0x178, "", "int 64", false);
            this.getAttributes().put("RundownProtect", a4);
            EntityAttribute a5 = new EntityAttribute("UniqueProcessId", 0x180, "", "int 32", false);
            this.getAttributes().put("UniqueProcessId", a5);
            EntityAttribute a6 = new EntityAttribute("ActiveProcessLinks", 0x188, "", "EntityList", false, new EntityListWin64());
            this.getAttributes().put("ActiveProcessLinks", a6);
            EntityAttribute a7 = new EntityAttribute("InheritedFromUniqueProcessId", 0x290, "", "int 32", false);
            this.getAttributes().put("InheritedFromUniqueProcessId", a7);
            EntityAttribute a8 = new EntityAttribute("ImageFileName", 0x2E0, "", "char", 15, false);
            this.getAttributes().put("ImageFileName", a8);
            /**
             * String _nombre, int _position, String _complexContentType, String
             * _contentType, int _length, boolean _union, int
             * _pointerMultiplicity, boolean _bigEndian, Entity _entity
             */
            EntityAttribute a9 = new EntityAttribute("ThreadListHead", 0x308, "", "pointer64 _EntityThread", 1, false, 1, false, new EntityWindows764Thread());
            a9.setEnabledParse(false);
            this.getAttributes().put("ThreadListHead", a9);
            /**
             * String _nombre, int _position, String _complexContentType, String
             * _contentType, int _length, boolean _union, int
             * _pointerMultiplicity, boolean _bigEndian, Entity _entity
             */
            EntityAttribute a10 = new EntityAttribute("PEB", 0x338, "", "pointer64 _PEB", 1, false, 1, false, new EntityWindows764PEB());
            this.getAttributes().put("PEB", a10);
            a10.setEnabledParse(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void getAttributesContent(long position, Map.Entry<String, EntityAttribute> e) {
        if (this.getParser() != null) {
            Entity entity = this.getParser().parseEntityByOffset(position, null, null);

            this.setAttributes(entity.getAttributes());
            this.setaS(entity.getaS());
            if (this.getAttributes().get("UniqueProcessId").getContent() != null) {
                this.setProcessID(Conversor.hexToLong(this.getAttributes().get("UniqueProcessId").getContent().toString()));
            }
        }
    }
}

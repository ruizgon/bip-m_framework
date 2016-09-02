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

import entities.EntityAttribute;
import entities.EntityListWin32;
import java.util.HashMap;
import java.util.Map;
import system.utils.Conversor;
import system.utils.IEntityVisitor;

/**
 *
 * @author Gonzalo
 */
public class EntityWindows732Process extends EntityWindowsProcess {

    public static final long _SIZE_STRUCTURE = 0x2C0; //134 elementos, 0x2C0 bytes 
    public static final long _ENTITY_LIST_OFFSET = 0x0B8;

    public EntityWindows732Process() {

        try {
            this.setTag(_TAG);

            this.setAttributes(new HashMap<String, EntityAttribute>());

            EntityAttribute a0 = new EntityAttribute("KPROCESS", 0x000, "", "KPROCESS", false, new EntityWin732KPROCESS());//PCB Process Control Block
            a0.setEnabledParse(false);
            this.getAttributes().put("KPROCESS", a0);
            EntityAttribute a1 = new EntityAttribute("ProcessLock", 0x098, "", "int 32", false);
            this.getAttributes().put("ProcessLock", a1);
            EntityAttribute a2 = new EntityAttribute("PADDING0_0x4", 0x09C, "", "int 8", false);
            this.getAttributes().put("PADDING0_0x4", a2);
            EntityAttribute a3 = new EntityAttribute("CreateTime", 0x0A0, "windows file time", "int 64", true);
            this.getAttributes().put("CreateTime", a3);
            EntityAttribute a4 = new EntityAttribute("ExitTime", 0x0A8, "windows file time", "int 64", true);
            this.getAttributes().put("ExitTime", a4);
            EntityAttribute a5 = new EntityAttribute("RundownProtect", 0x0B0, "", "int 32", false);
            this.getAttributes().put("RundownProtect", a5);
            EntityAttribute a6 = new EntityAttribute("UniqueProcessId", 0x0B4, "", "int 32", false);
            this.getAttributes().put("UniqueProcessId", a6);
            EntityAttribute a7 = new EntityAttribute("ActiveProcessLinks", 0x0B8, "", "EntityList", false, new EntityListWin32());
            this.getAttributes().put("ActiveProcessLinks", a7);
            EntityAttribute a8 = new EntityAttribute("InheritedFromUniqueProcessId", 0x140, "", "int 32", false);
            this.getAttributes().put("InheritedFromUniqueProcessId", a8);
            EntityAttribute a9 = new EntityAttribute("ImageFileName", 0x16C, "", "char", 15, false);
            this.getAttributes().put("ImageFileName", a9);
            /**
             * String _nombre, int _position, String _complexContentType, String
             * _contentType, int _length, boolean _union, int
             * _pointerMultiplicity, boolean _bigEndian, Entity _entity
             */
            EntityAttribute a10 = new EntityAttribute("ThreadListHead", 0x188, "", "pointer32 _EntityThread", 1, false, 1, false, new EntityWindows732Thread());
            a10.setEnabledParse(false);
            this.getAttributes().put("ThreadListHead", a10);
            /**
             * String _nombre, int _position, String _complexContentType, String
             * _contentType, int _length, boolean _union, int
             * _pointerMultiplicity, boolean _bigEndian, Entity _entity
             */
            EntityAttribute a11 = new EntityAttribute("PEB", 0x1A8, "", "pointer32 _PEB", 1, false, 1, false, new EntityWindows732PEB());
            this.getAttributes().put("PEB", a11);
            a11.setEnabledParse(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * @param position: normalmente es la dirección del comienzo de la
     * estructura
     * @param e
     */
    @Override
    public void getAttributesContent(long position, Map.Entry<String, EntityAttribute> e) {
        if (this.getParser() != null) {
            EntityWindows732Process entity = (EntityWindows732Process) this.getParser().parseEntityByOffset(position, this, e);

            this.setAttributes(entity.getAttributes());
            this.setaS(entity.getaS());
            if (this.getAttributes().get("UniqueProcessId").getContent() != null) {
                this.setProcessID(Conversor.hexToLong(this.getAttributes().get("UniqueProcessId").getContent().toString()));
            }
        }
    }

}

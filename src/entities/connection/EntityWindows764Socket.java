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

import entities.Entity;
import entities.EntityAttribute;
import entities.process.EntityWindows764Process;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Gonzalo
 */
public class EntityWindows764Socket extends EntitySocket {

    public EntityWindows764Socket() {

        try {
            this.setTag(_TAG);

            this.setAttributes(new HashMap<String, EntityAttribute>());

            EntityAttribute a0 = new EntityAttribute("State", 0x010, "", "int 8", false, new EntityWindowsConnectionState());
            this.getAttributes().put("State", a0);
            EntityAttribute a1 = new EntityAttribute("CreateTime", 0x020, "windows file time", "int 64", true);
            this.getAttributes().put("CreateTime", a1);
            EntityAttribute a2 = new EntityAttribute("Owner", 0x028, "", "pointer64 _EPROCESS", 1, false, 1, false, new EntityWindows764Process());
            this.getAttributes().put("Owner", a2);
            EntityAttribute a3 = new EntityAttribute("LocalAddress", 0x058, "", "pointer64 _LocalAddress", 1, false, 1, false, new EntityLocalAddressWin64());
            this.getAttributes().put("LocalAddress", a3);
            EntityAttribute a4 = new EntityAttribute("AddressFamily", 0x060, "", "pointer64 _AddressFamiliy", 1, false, 1, false, new EntityAddressFamilyWin64());
            this.getAttributes().put("AddressFamily", a4);
            EntityAttribute a5 = new EntityAttribute("Port", 0x06a, "", "int 16", 1, false, 0, true);
            this.getAttributes().put("Port", a5);
            EntityAttribute a6 = new EntityAttribute("OwnerVA", 0x028, "", "int 64", 1, false);
            this.getAttributes().put("OwnerVA", a6);
        } catch (Exception ex) {

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

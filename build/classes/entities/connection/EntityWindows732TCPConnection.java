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

import entities.EntityAttribute;
import entities.EntityListWin32;
import entities.process.EntityWindows732Process;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Gonzalo
 */
public class EntityWindows732TCPConnection extends EntityTCPConnection {

    public EntityWindows732TCPConnection() {
        try {
            this.setTag(_TAG);

            this.setAttributes(new HashMap<String, EntityAttribute>());

            /**
             * String _nombre, int _position, String _complexContentType, String
             * _contentType, int _length, boolean _union, int
             * _pointerMultiplicity, boolean _bigEndian
             */
            EntityAttribute a0 = new EntityAttribute("CreateTime", 0x000, "windows file time", "int 64", true);
            this.getAttributes().put("CreateTime", a0);
            EntityAttribute a1 = new EntityAttribute("AddressFamily", 0x00C, "", "pointer32 _AddressFamiliy", 1, false, 1, false, new EntityAddressFamilyWin32());
            this.getAttributes().put("AddressFamily", a1);
            EntityAttribute a2 = new EntityAttribute("AddressInfo", 0x010, "", "pointer32 _AddressInfo", 1, false, 1, false, new EntityAddressInfoWin32());
            a2.setResolveMultiplicity(EntityAddressInfoWin32._RESOLEV_MULTIPLICIY);
            a2.setShiftPosition(true);
            this.getAttributes().put("AddressInfo", a2);
            EntityAttribute a3 = new EntityAttribute("ConnectionLinks", 0x014, "", "EntityList", false, new EntityListWin32());
            this.getAttributes().put("ConnectionLinks", a3);
            EntityAttribute a4 = new EntityAttribute("State", 0x034, "", "int 8", false, new EntityWindowsConnectionState());
            this.getAttributes().put("State", a4);
            EntityAttribute a5 = new EntityAttribute("LocalPort", 0x038, "", "int 16", 1, false, 0, true);
            this.getAttributes().put("LocalPort", a5);
            EntityAttribute a6 = new EntityAttribute("RemotePort", 0x03A, "", "int 16", 1, false, 0, true);
            this.getAttributes().put("RemotePort", a6);
            /**
             * String _nombre, int _position, String _complexContentType, String
             * _contentType, int _length, boolean _union, Entity _entity
             */
            EntityAttribute a7 = new EntityAttribute("Owner", 0x178, "", "pointer32 _EPROCESS", 1, false, 1, false, new EntityWindows732Process());
            this.getAttributes().put("Owner", a7);
            EntityAttribute a8 = new EntityAttribute("OwnerVA", 0x178, "", "int 32", 1, false);
            this.getAttributes().put("OwnerVA", a8);
        } catch (Exception ex) {

        }
    }

    @Override
    public void getAttributesContent(long position, Map.Entry<String, EntityAttribute> e) {
        if (this.getParser() != null) {
            EntityWindows732TCPConnection entity = (EntityWindows732TCPConnection) this.getParser().parseEntityByOffset(position, null, null);

            this.setAttributes(entity.getAttributes());
        }
    }
}

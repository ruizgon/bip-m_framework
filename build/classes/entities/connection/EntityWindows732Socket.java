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
import entities.process.EntityWindows732Process;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Gonzalo
 */
public class EntityWindows732Socket extends EntitySocket {

    public EntityWindows732Socket() {
        try {
            this.setTag(_TAG);

            /**
             * Inicializa atributos
             */
            this.setAttributes(new HashMap<String, EntityAttribute>());

            EntityAttribute a0 = new EntityAttribute("State", 0x008, "", "int 8", false, new EntityWindowsConnectionState());
            this.getAttributes().put("State", a0);
            /**
             * String _nombre, int _position, String _complexContentType, String
             * _contentType, int _length, boolean _union
             */
            EntityAttribute a1 = new EntityAttribute("CreateTime", 0x00C, "windows file time", "int 64", true);
            this.getAttributes().put("CreateTime", a1);
            /**
             * String _nombre, int _position, String _complexContentType, String
             * _contentType, int _length, boolean _union, Entity _entity
             */
            EntityAttribute a2 = new EntityAttribute("Owner", 0x018, "", "pointer32 _EPROCESS", 1, false, 1, false, new EntityWindows732Process());
            this.getAttributes().put("Owner", a2);
            /**
             * String _nombre, int _position, String _contentType, int _length,
             * boolean _union, int _pointerMultiplicity, boolean _bigEndian
             */
            EntityAttribute a3 = new EntityAttribute("LocalAddress", 0x034, "", "pointer32 _LocalAddress", 1, false, 1, false, new EntityLocalAddressWin32());
            this.getAttributes().put("LocalAddress", a3);
            EntityAttribute a4 = new EntityAttribute("AddressFamily", 0x038, "", "pointer32 _AddressFamily", 1, false, 1, false, new EntityAddressFamilyWin32());
            this.getAttributes().put("AddressFamily", a4);
            EntityAttribute a5 = new EntityAttribute("Port", 0x03E, "", "int 16", 1, false, 0, true);
            this.getAttributes().put("Port", a5);
            EntityAttribute a6 = new EntityAttribute("EndPoint", 0x050, "", "pointer32 _TCPConnection", 1, false, 1, false, new EntityWindows732TCPConnection());
            this.getAttributes().put("EndPoint", a6);
            EntityAttribute a7 = new EntityAttribute("EndPointVA", 0x050, "", "int 32", 1, false);
            this.getAttributes().put("EndPointVA", a7);
            EntityAttribute a8 = new EntityAttribute("OwnerVA", 0x018, "", "int 32", 1, false);
            this.getAttributes().put("OwnerVA", a8);
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

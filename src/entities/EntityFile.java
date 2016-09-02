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

/**
 *
 * @author Gonzalo
 */
public abstract class EntityFile  extends Entity{

    private String _name;
    private String _permission;
    private EntityOwnerFile _owner;

    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public String getPermission() {
        return _permission;
    }

    public void setPermission(String _permission) {
        this._permission = _permission;
    }

    public EntityOwnerFile getOwner() {
        return _owner;
    }

    public void setOwner(EntityOwnerFile _owner) {
        this._owner = _owner;
    }

}

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
package dump.parsers.utils.pools;

import dump.ooss.OperatingSystemStructure;
import entities.EntityAttribute;
import java.util.Map;

/**
 *
 * @author Gonzalo
 */
public abstract class PoolManager {

    protected OperatingSystemStructure _oSStructure;
    protected Map<String, EntityAttribute> _attributes;

    public OperatingSystemStructure getoSStructure() {
        return _oSStructure;
    }

    public void setoSStructure(OperatingSystemStructure _oSStructure) {
        this._oSStructure = _oSStructure;
    }

    public Map<String, EntityAttribute> getAttributes() {
        return _attributes;
    }

    public void setAttributes(Map<String, EntityAttribute> _attributes) {
        this._attributes = _attributes;
    }

}

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
package dump.ooss;

import dump.parsers.utils.pools.PoolManager;
import dump.parsers.utils.pools.PoolManagerWin32;
import entities.Entity;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author Gonzalo
 */
public abstract class Windows extends OperatingSystemStructure {

    private boolean PAEEnabled;
    private PoolManager _poolManager;

    public Windows() {
        super();
    }

    public Windows(Map<String, Entity> _entityList) {
        super(_entityList);
    }

    public boolean isPAEEnabled() {
        return PAEEnabled;
    }

    public void setPAEEnabled(boolean PAEEnabled) {
        this.PAEEnabled = PAEEnabled;
    }

    public PoolManager getPoolManager() {
        return _poolManager;
    }

    public void setPoolManager(PoolManager _poolManager) {
        this._poolManager = _poolManager;
    }

}

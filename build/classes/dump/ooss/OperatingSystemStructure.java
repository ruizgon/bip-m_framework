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

import dump.parsers.Parser;
import entities.Entity;
import java.util.Map;

/**
 *
 * @author Gonzalo
 */
public abstract class OperatingSystemStructure {

    protected String _keyInitializator;
    //private ArrayList<Entity> _entityList;
    protected Map<String, Entity> _entityList;

    public OperatingSystemStructure() {

    }

    public OperatingSystemStructure(Map<String, Entity> _entityList) {
        this._entityList = _entityList;
    }

    public String getKeyInitializator() {
        return _keyInitializator;
    }

    public void setKeyInitializator(String _keyInitializator) {
        this._keyInitializator = _keyInitializator;
    }

    public Map<String, Entity> getEntityList() {
        return _entityList;
    }

    public void setEntityList(Map<String, Entity> _entityList) {
        this._entityList = _entityList;
    }

    /**
     * A todas las entidades con este tag le asigna el Parser
     *
     * @param tag
     */
    public abstract void setParserByTag(String tag, Parser parser);
    
}

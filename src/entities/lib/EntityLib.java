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

import dump.parsers.interfaces.IEntityParserVisitor;
import entities.Entity;
import system.utils.IEntityVisitor;

/**
 *
 * @author Gonzalo
 */
public abstract class EntityLib extends Entity {

    public static final String _TAG = "Library";
    private String _name;
    private String _path;
    private String _fullPath;
    private long _dllBaseOffset;

    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public String getPath() {
        return _path;
    }

    public void setPath(String _path) {
        this._path = _path;
    }

    public String getFullPath() {
        return _fullPath;
    }

    public void setFullPath(String _fullPath) {
        this._fullPath = _fullPath;
    }

    public long getDllBaseOffset() {
        return _dllBaseOffset;
    }

    public void setDllBaseOffset(long _dllBaseOffset) {
        this._dllBaseOffset = _dllBaseOffset;
    }

    @Override
    public void accept(IEntityVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void accept(IEntityParserVisitor visitor) {
        visitor.visit(this);
    }

}

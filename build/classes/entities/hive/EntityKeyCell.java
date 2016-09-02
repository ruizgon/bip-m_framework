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
package entities.hive;

import java.util.Date;

/**
 *
 * @author Gonzalo
 */
public abstract class EntityKeyCell extends EntityCell { //Cell que contiene cell de tipo key (Registry Key o key node)

    private Date _timeStamp; //útlima actualización

    private EntityCellIndex _parentKeyIndex;
    private EntityCellIndex _subKeyIndex;
    private EntityCellIndex _securityDescriptorIndex;

    private EntityKeyCell _subKeyList; //para cuando se trata de una sibkey-list cell

    public Date getTimeStamp() {
        return _timeStamp;
    }

    public void setTimeStamp(Date _timeStamp) {
        this._timeStamp = _timeStamp;
    }

    public EntityCellIndex getParentKeyIndex() {
        return _parentKeyIndex;
    }

    public void setParentKeyIndex(EntityCellIndex _parentKeyIndex) {
        this._parentKeyIndex = _parentKeyIndex;
    }

    public EntityCellIndex getSubKeyIndex() {
        return _subKeyIndex;
    }

    public void setSubKeyIndex(EntityCellIndex _subKeyIndex) {
        this._subKeyIndex = _subKeyIndex;
    }

    public EntityCellIndex getSecurityDescriptorIndex() {
        return _securityDescriptorIndex;
    }

    public void setSecurityDescriptorIndex(EntityCellIndex _securityDescriptorIndex) {
        this._securityDescriptorIndex = _securityDescriptorIndex;
    }

    public EntityKeyCell getSubKeyList() {
        return _subKeyList;
    }

    public void setSubKeyList(EntityKeyCell _subKeyList) {
        this._subKeyList = _subKeyList;
    }

}

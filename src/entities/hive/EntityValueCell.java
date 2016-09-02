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

/**
 *
 * @author Gonzalo
 */
public abstract class EntityValueCell extends EntityCell {

    private String _valueType;
    private String _valueName;
    private EntityCellIndex _valueData;

    private EntityValueCell _valueCellList; //para cuano se trata de una Value-list cell

    public String getValueType() {
        return _valueType;
    }

    public void setValueType(String _valueType) {
        this._valueType = _valueType;
    }

    public String getValueName() {
        return _valueName;
    }

    public void setValueName(String _valueName) {
        this._valueName = _valueName;
    }

    public EntityCellIndex getValueData() {
        return _valueData;
    }

    public void setValueData(EntityCellIndex _valueData) {
        this._valueData = _valueData;
    }

    public EntityValueCell getValueCellList() {
        return _valueCellList;
    }

    public void setValueCellList(EntityValueCell _valueCellList) {
        this._valueCellList = _valueCellList;
    }

}

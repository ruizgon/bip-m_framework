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

import dump.parsers.Parser;
import dump.parsers.interfaces.IEntityParserVisitable;
import entities.translation.EntityAddressSpace;
import java.util.Map;
import system.utils.IEntityVisitable;

/**
 *
 * @author Gonzalo
 */
public abstract class Entity implements IEntityVisitable, IEntityParserVisitable {

    private long id;
    private String _tag;
    private int positionInList;
    private Parser _parser;
    private EntityAddressSpace _aS;
    private Map<String, EntityAttribute> _attributes;
    private String _possibleStates;
    private boolean falsePositive;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTag() {
        return _tag;
    }

    public void setTag(String _tag) {
        this._tag = _tag;
    }

    public int getPositionInList() {
        return positionInList;
    }

    public void setPositionInList(int positionInList) {
        this.positionInList = positionInList;
    }

    public Parser getParser() {
        return _parser;
    }

    public void setParser(Parser _parser) {
        this._parser = _parser;
    }

    public EntityAddressSpace getaS() {
        return _aS;
    }

    public void setaS(EntityAddressSpace _aS) {
        this._aS = _aS;
    }

    /*public long getPhysicalAddress() {
     return _physicalAddress;
     }

     public void setPhysicalAddress(long _physicalAddress) {
     this._physicalAddress = _physicalAddress;
     }

     public long getVirtualAddress() {
     return _virtualAddress;
     }

     public void setVirtualAddress(long _virtualAddress) {
     this._virtualAddress = _virtualAddress;
     }

     public String getPhysicalAddressHex() {
     return _physicalAddressHex;
     }

     public void setPhysicalAddressHex(String _physicalAddress) {
     this._physicalAddressHex = _physicalAddress;
     }

     public String getVirtualAddressHex() {
     return _virtualAddressHex;
     }

     public void setVirtualAddressHex(String _visrtualAddress) {
     this._virtualAddressHex = _visrtualAddress;
     }*/
    public Map<String, EntityAttribute> getAttributes() {
        return _attributes;
    }

    public void setAttributes(Map<String, EntityAttribute> _attibutes) {
        this._attributes = _attibutes;
    }

    public String getPossibleStates() {
        return _possibleStates;
    }

    public void setPossibleStates(String _possibleStates) {
        this._possibleStates = _possibleStates;
    }

    public boolean isFalsePositive() {
        return falsePositive;
    }

    public void setFalsePositive(boolean falsePositive) {
        this.falsePositive = falsePositive;
    }

    /**
     * Command Parser
     *
     * @return
     */
    public Map<Object, Entity> parse(Object[] params) {
        _parser.run();
        Map<Object, Entity> entities = _parser.getEntities();

        return entities;
    }

    /**
     *
     * @param entity
     * @return
     */
    public Map<Object, Entity> parse(Entity entity, Object[] params) {
        _parser.parse(entity, params);
        Map<Object, Entity> entities = _parser.getEntities();

        return entities;
    }

    public abstract void getAttributesContent(long position, Map.Entry<String, EntityAttribute> e);

}

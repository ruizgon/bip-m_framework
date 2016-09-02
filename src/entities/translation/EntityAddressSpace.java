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
package entities.translation;

import entities.Entity;
import java.math.BigInteger;
import system.utils.IEntityVisitable;
import system.utils.IEntityVisitor;

/**
 *
 * @author Gonzalo
 */
public abstract class EntityAddressSpace extends Entity implements IEntityVisitable{

    private BigInteger _physicalAddress;
    private BigInteger _virtualAddress;
    private String _physicalAddressHex;//Hexa
    private String _virtualAddressHex;//Hexa
    private long _offsetInFile;
    private String _offsetInFileHex;//Hexa
    private String _virtualAddressContent;
    private String _physicalAddressContent;

    public BigInteger getPhysicalAddress() {
        return _physicalAddress;
    }

    public void setPhysicalAddress(BigInteger _physicalAddress) {
        this._physicalAddress = _physicalAddress;
    }

    public BigInteger getVirtualAddress() {
        return _virtualAddress;
    }

    public void setVirtualAddress(BigInteger _virtualAddress) {
        this._virtualAddress = _virtualAddress;
    }

    public String getPhysicalAddressHex() {
        return _physicalAddressHex;
    }

    public void setPhysicalAddressHex(String _physicalAddressHex) {
        this._physicalAddressHex = _physicalAddressHex;
    }

    public String getVirtualAddressHex() {
        return _virtualAddressHex;
    }

    public void setVirtualAddressHex(String _virtualAddressHex) {
        this._virtualAddressHex = _virtualAddressHex;
    }

    public long getOffsetInFile() {
        return _offsetInFile;
    }

    public void setOffsetInFile(long _offsetInFile) {
        this._offsetInFile = _offsetInFile;
    }

    public String getOffsetInFileHex() {
        return _offsetInFileHex;
    }

    public void setOffsetInFileHex(String _offsetInFileHex) {
        this._offsetInFileHex = _offsetInFileHex;
    }

    public String getVirtualAddressContent() {
        return _virtualAddressContent;
    }

    public void setVirtualAddressContent(String virtualAddressContent) {
        this._virtualAddressContent = virtualAddressContent;
    }

    public String getPhysicalAddressContent() {
        return _physicalAddressContent;
    }

    public void setPhysicalAddressContent(String physicalAddressContent) {
        this._physicalAddressContent = physicalAddressContent;
    }

}

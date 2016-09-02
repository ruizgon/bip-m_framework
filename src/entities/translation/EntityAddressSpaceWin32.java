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

import dump.parsers.interfaces.IEntityParserVisitor;
import entities.EntityAttribute;
import java.math.BigInteger;
import java.util.Map;
import system.utils.IEntityVisitor;

/**
 *
 * @author Gonzalo
 */
public class EntityAddressSpaceWin32 extends EntityAddressSpace {

    public static int _PDP_SELECTOR_LENGTH = 2;
    public static int _PDP_SELECTOR_START_POS = 0;
    public static int _PD_SELECTOR_LENGTH = 9;
    public static int _PD_SELECTOR_START_POS = 2;
    public static int _PT_SELECTOR_LENGTH = 9;
    public static int _PT_SELECTOR_START_POS = 11;
    public static int _OFFSET_LENGTH = 12;
    public static int _OFFSET_START_POS = 20;

    private String pageDirectoryPointerIndexHex; // bits 31-30 (PDPI) --> Con PAE http://en.wikipedia.org/wiki/Physical_Address_Extension#Design [hex]
    private String pageDirectoryIndexHex; // bits 29-21 (PDI) [hex]
    private String pageTableIndexHex; //  bits 20-12 (PTI) [hex]
    private String pageByteOffsetHex; // bits 11-0  [hex]

    private BigInteger pageDirectoryPointerIndex;
    private BigInteger pageDirectoryIndex;
    private BigInteger pageTableIndex;
    private BigInteger pageByteOffset;
    private boolean _prototypePTE; //bit 10 de los últimos 12 bits de la dirección virtual        
    private boolean _largePage; //bit 7 de los últimos 12 bits de la dirección virtual

    /*
     * TODO: Ver sin PAE
     */
    public String getPageDirectoryPointerIndexHex() {
        return pageDirectoryPointerIndexHex;
    }

    public void setPageDirectoryPointerIndexHex(String pageDirectoryPointerIndexHex) {
        this.pageDirectoryPointerIndexHex = pageDirectoryPointerIndexHex;
    }

    public String getPageDirectoryIndexHex() {
        return pageDirectoryIndexHex;
    }

    public void setPageDirectoryIndexHex(String pageDirectoryIndexHex) {
        this.pageDirectoryIndexHex = pageDirectoryIndexHex;
    }

    public String getPageTableIndexHex() {
        return pageTableIndexHex;
    }

    public void setPageTableIndexHex(String pageTableIndexHex) {
        this.pageTableIndexHex = pageTableIndexHex;
    }

    public String getPageByteOffsetHex() {
        return pageByteOffsetHex;
    }

    public void setPageByteOffsetHex(String pageByteOffsetHex) {
        this.pageByteOffsetHex = pageByteOffsetHex;
    }

    public BigInteger getPageDirectoryPointerIndex() {
        return pageDirectoryPointerIndex;
    }

    public void setPageDirectoryPointerIndex(BigInteger pageDirectoryPointerIndex) {
        this.pageDirectoryPointerIndex = pageDirectoryPointerIndex;
    }

    public BigInteger getPageDirectoryIndex() {
        return pageDirectoryIndex;
    }

    public void setPageDirectoryIndex(BigInteger pageDirectoryIndex) {
        this.pageDirectoryIndex = pageDirectoryIndex;
    }

    public BigInteger getPageTableIndex() {
        return pageTableIndex;
    }

    public void setPageTableIndex(BigInteger pageTableIndex) {
        this.pageTableIndex = pageTableIndex;
    }

    public BigInteger getPageByteOffset() {
        return pageByteOffset;
    }

    public void setPageByteOffset(BigInteger pageByteOffset) {
        this.pageByteOffset = pageByteOffset;
    }

    public boolean isPrototypePTE() {
        return _prototypePTE;
    }

    public void setPrototypePTE(boolean _prototypePTE) {
        this._prototypePTE = _prototypePTE;
    }

    public boolean isLargePage() {
        return _largePage;
    }

    public void setLargePage(boolean _largePage) {
        this._largePage = _largePage;
    }

    @Override
    public void getAttributesContent(long position, Map.Entry<String, EntityAttribute> e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void accept(IEntityVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void accept(IEntityParserVisitor visitor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

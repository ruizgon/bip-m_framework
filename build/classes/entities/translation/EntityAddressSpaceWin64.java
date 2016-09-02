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
public class EntityAddressSpaceWin64 extends EntityAddressSpace {

    /*
     * Verificar
     */
    public static int _PML4_SELECTOR_LENGTH = 9;
    public static int _PML4_SELECTOR_START_POS = 16;//Se descartan los 16 bits más significativos. En los procesadores actuales el direccionamiento es con 48 de 64 bits
    public static int _PDP_SELECTOR_LENGTH = 9;
    public static int _PDP_SELECTOR_START_POS = 25;
    public static int _PD_SELECTOR_LENGTH = 9;
    public static int _PD_SELECTOR_START_POS = 34;
    public static int _PT_SELECTOR_LENGTH = 9;
    public static int _PT_SELECTOR_START_POS = 43;
    public static int _OFFSET_LENGTH = 12;
    public static int _OFFSET_START_POS = 52;

    private String pageDirectoryPointerIndexHex; // bits 47-39 (PDPI)
    private String pageDirectoryIndexHex; // bits 38-30 (PDI)
    private String pageTableIndexHex; //  bits 29-21 (PTI)
    private String pageTableEntryHex; // bits 20-12 (PTE)
    private String pageByteOffsetHex; // bits 11-0

    private BigInteger pageDirectoryPointerIndex; // bits 47-39 (PDPI)
    private BigInteger pageDirectoryIndex; // bits 38-30 (PDI)
    private BigInteger pageTableIndex; //  bits 29-21 (PTI)
    private BigInteger pageTableEntry; // bits 20-12 (PTE)
    private BigInteger pageByteOffset; // bits 11-0

    private boolean _largePage; //bit 7 de los últimos 12 bits de la dirección virtual

    public String getPageDirectoryPointerIndexHex() {
        return pageDirectoryPointerIndexHex;
    }

    public void setPageDirectoryPointerIndexHex(String pageDirectoryPointerIndex) {
        this.pageDirectoryPointerIndexHex = pageDirectoryPointerIndex;
    }

    public String getPageDirectoryIndexHex() {
        return pageDirectoryIndexHex;
    }

    public void setPageDirectoryIndexHex(String pageDirectoryIndex) {
        this.pageDirectoryIndexHex = pageDirectoryIndex;
    }

    public String getPageTableIndexHex() {
        return pageTableIndexHex;
    }

    public void setPageTableIndexHex(String pageTableIndex) {
        this.pageTableIndexHex = pageTableIndex;
    }

    public String getPageTableEntryHex() {
        return pageTableEntryHex;
    }

    public void setPageTableEntryHex(String pageTableEntry) {
        this.pageTableEntryHex = pageTableEntry;
    }

    public String getPageByteOffsetHex() {
        return pageByteOffsetHex;
    }

    public void setPageByteOffsetHex(String pageByteOffset) {
        this.pageByteOffsetHex = pageByteOffset;
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

    public BigInteger getPageTableEntry() {
        return pageTableEntry;
    }

    public void setPageTableEntry(BigInteger pageTableEntry) {
        this.pageTableEntry = pageTableEntry;
    }

    public BigInteger getPageByteOffset() {
        return pageByteOffset;
    }

    public void setPageByteOffset(BigInteger pageByteOffset) {
        this.pageByteOffset = pageByteOffset;
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

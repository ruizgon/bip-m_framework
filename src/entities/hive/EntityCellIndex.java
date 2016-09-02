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
public class EntityCellIndex {

    private String sOrv; //sttable hive on disk or Volatile
    private String directoryIndex;
    private String tableIndex;
    private String cellOffset;
    private String cellContent;

    public String getsOrv() {
        return sOrv;
    }

    public void setsOrv(String sOrv) {
        this.sOrv = sOrv;
    }

    public String getDirectoryIndex() {
        return directoryIndex;
    }

    public void setDirectoryIndex(String directoryIndex) {
        this.directoryIndex = directoryIndex;
    }

    public String getTableIndex() {
        return tableIndex;
    }

    public void setTableIndex(String tableIndex) {
        this.tableIndex = tableIndex;
    }

    public String getCellOffset() {
        return cellOffset;
    }

    public void setCellOffset(String cellOffset) {
        this.cellOffset = cellOffset;
    }

    public String getCellContent() {
        return cellContent;
    }

    public void setCellContent(String cellContent) {
        this.cellContent = cellContent;
    }

}

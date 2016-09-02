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
package dump.parsers.utils.tags;

import java.util.Map;

/**
 *
 * @author Gonzalo
 */
public abstract class TagManager {

    protected Map<String, TagItem> _tagItems;

    /*Process Proc Pro\xe3 1304 Nonpaged psscan
     Threads Thrd Thr\xe4 1248 Nonpaged thrdscan
     Desktops Desk Des\xeb 296 Nonpaged deskscan
     Window
     Stations
     Wind Win\xe4 224 Nonpaged wndscan
     Mutants Mute Mut\xe5 128 Nonpaged mutantscan
     File Objects File Fil\xe5 288 Nonpaged filescan
     Drivers Driv Dri\xf6 408 Nonpaged driverscan
     Symbolic Links Link Lin\xeb 104 Nonpaged symlinkscan*/
    
    public Map<String, TagItem> getTagItems() {
        return _tagItems;
    }

    public void setTagItems(Map<String, TagItem> _tagItems) {
        this._tagItems = _tagItems;
    }

}

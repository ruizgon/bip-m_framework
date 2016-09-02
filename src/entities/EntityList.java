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

import java.math.BigInteger;

/**
 *
 * @author Gonzalo
 */
public abstract class EntityList extends Entity {

    private long _fLink;
    private long _bLink;
    private String _fLinkHex;
    private String _bLinkHex;

    public long getfLink() {
        return _fLink;
    }

    public void setfLink(long _fLink) {
        this._fLink = _fLink;
    }

    public long getbLink() {
        return _bLink;
    }

    public void setbLink(long _bLink) {
        this._bLink = _bLink;
    }

    public String getfLinkHex() {
        return _fLinkHex;
    }

    public void setfLinkHex(String _fLinkHex) {
        this._fLinkHex = _fLinkHex;
    }

    public String getbLinkHex() {
        return _bLinkHex;
    }

    public void setbLinkHex(String _bLinkHex) {
        this._bLinkHex = _bLinkHex;
    }

}

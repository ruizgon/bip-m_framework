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

import dump.parsers.ParserWin64;
import dump.parsers.interfaces.IEntityParserVisitor;
import java.math.BigInteger;
import java.util.Map;
import system.utils.IEntityVisitor;

/**
 *
 * @author Gonzalo
 */
public class EntityListWin64 extends EntityList{
    private static int _FLINK_OFFSET = 0x0;
    private static int _BLINK_OFFSET = 0x8;
    
    private BigInteger _fLinkBig;
    private BigInteger _bLinkBig;

    public BigInteger getfLinkBig() {
        return _fLinkBig;
    }

    public void setfLinkBig(BigInteger _fLink) {
        this._fLinkBig = _fLink;
    }

    public BigInteger getbLinkBig() {
        return _bLinkBig;
    }

    public void setbLinkBig(BigInteger _bLink) {
        this._bLinkBig = _bLink;
    }
    
    @Override
    public void getAttributesContent(long position, Map.Entry<String, EntityAttribute> e) {
        if (this.getParser() != null) {
            ParserWin64 parser = (ParserWin64) this.getParser();
            EntityListWin64 list = (EntityListWin64) parser.getListContent(position);
            this.setfLinkBig(list.getfLinkBig());
            this.setfLinkHex(list.getfLinkHex());
            this.setbLinkBig(list.getbLinkBig());
            this.setbLinkHex(list.getbLinkHex());
        }
    }

    @Override
    public void accept(IEntityVisitor visitor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void accept(IEntityParserVisitor visitor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

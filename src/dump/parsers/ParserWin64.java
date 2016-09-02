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
package dump.parsers;

import dump.formats.DumpFormat;
import dump.ooss.OperatingSystemStructure;
import dump.parsers.interfaces.IParser;
import entities.EntityAttribute;
import entities.EntityList;
import entities.EntityListWin64;
import entities.EntitySingleList;
import entities.EntityUnicodeString;
import entities.EntityUnicodeStringWin64;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.Map;
import system.utils.CharOperator;
import system.utils.Conversor;
import translation.Translator;

/**
 *
 * @author Gonzalo
 */
public abstract class ParserWin64 extends ParserWin implements IParser {

    protected final int _LINE_LENGTH = 16;
    protected final int _LIST_ENTRY_ENTRY_SIZE = 0x008;
    protected int _pageSize = 4096;
    
    public ParserWin64(DumpFormat _dumpFormat, OperatingSystemStructure _os) {
        super(_dumpFormat, _os);
    }

    public ParserWin64(DumpFormat _dumpFormat, OperatingSystemStructure _os, Translator _translator) {
        super(_dumpFormat, _os, _translator);
    }

    @Override
    public EntitySingleList getSingleListContent(long position) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityList getListContent(long position) {
         EntityListWin64 list = null;

        try {
            list = new EntityListWin64();

            String content = this.getDumpFormat().getRandomFileSeeker().getContent(position, _LIST_ENTRY_ENTRY_SIZE);

            if (this.getDumpFormat().isLittleEndian()) {
                content = CharOperator.getLittleEndianCharArray(content).toString();
            }

            String hex = Conversor.toHexString(content);

            list.setfLinkHex(hex);
            
            BigInteger virtualAddress = Conversor.hexToBigInteger(hex);

            list.setfLinkBig(virtualAddress);

            position = position + _LIST_ENTRY_ENTRY_SIZE;

            content = this.getDumpFormat().getRandomFileSeeker().getContent(position, _LIST_ENTRY_ENTRY_SIZE);

            if (this.getDumpFormat().isLittleEndian()) {
                content = CharOperator.getLittleEndianCharArray(content).toString();
            }

            hex = Conversor.toHexString(content);

            list.setbLinkHex(hex);

            virtualAddress = Conversor.hexToBigInteger(hex);

            list.setbLinkBig(virtualAddress);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public BigInteger getNextEntityAddress(EntityList list, long entityListOffset) {
        BigInteger position = BigInteger.ZERO;

        EntityListWin64 list64 = (EntityListWin64) list;
        try {
            position = this.getTranslator().calculatePhysicalAddress(Conversor.bigIntToHexString(list64.getfLinkBig())).subtract(BigInteger.valueOf(entityListOffset));
        } catch (Exception ex) {
            ex.printStackTrace();
            position = BigInteger.valueOf(-1);
        }

        return position;
    }

    @Override
    public EntityUnicodeString getUnicodeStringContent(long position, Map.Entry<String, EntityAttribute> e) {
        EntityUnicodeStringWin64 unicodeString = (EntityUnicodeStringWin64) e.getValue().getEntity();

        long offset = position;

        Iterator<Map.Entry<String, EntityAttribute>> i = unicodeString.getAttributes().entrySet().iterator();
        while (i.hasNext()) {
            /**
             * Por cada item, le solicta a randomFileSeeker que obtenga el
             * contenido
             */

            Map.Entry<String, EntityAttribute> entry = (Map.Entry<String, EntityAttribute>) i.next();
            if (!entry.getKey().equals("Name")) {
                this.getEntryAttributeContent(offset, entry);
            }
        }

        /**
         * Ya obtenido el valor de Buffer (puntero al DLLName, obtener el
         * contenido de esa dirección y setearlo en
         * unicodeString.getAttributes.get("Name")
         */
        if (unicodeString.getAttributes().get("Buffer").getContent() != null) {
            String bufferPointer = unicodeString.getAttributes().get("Buffer").getContent().toString();

            BigInteger address = this.getTranslator().calculatePhysicalAddress(bufferPointer);
            long absoluteAttributeOffset = this.getDumpFormat().getFileOffset(address);

            int lengthAttribute = 0;

            if (unicodeString.getAttributes().get("Length").getContent() != null) {
                lengthAttribute = Conversor.hexToInt(unicodeString.getAttributes().get("Length").getContent().toString());
            }

            String content = this.getDumpFormat().getDataManager().getItemContent("char", absoluteAttributeOffset, lengthAttribute, unicodeString.getAttributes().get("Buffer").isUnion());

            unicodeString.getAttributes().get("Name").setContent(content);
        }

        return unicodeString;
    }

}

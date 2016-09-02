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

import dump.formats.CrashDump32;
import dump.ooss.Windows732OS;
import entities.Entity;
import entities.EntityAttribute;
import entities.EntityList;
import entities.EntitySingleList;
import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import system.utils.Conversor;
import translation.TranslatorWin32;

/**
 *
 * @author Gonzalo
 */
public class ParserKDBGCrashDMPWin732 extends ParserWin32 {

    public ParserKDBGCrashDMPWin732(CrashDump32 _dumpFormat, Windows732OS _os) {
        super(_dumpFormat, _os);
    }

    public ParserKDBGCrashDMPWin732(CrashDump32 _dumpFormat, Windows732OS _os, TranslatorWin32 _translator) {
        super(_dumpFormat, _os, _translator);
    }

    @Override
    public void parse(Object[] params) {
        ConcurrentHashMap<Object, Entity> entities = null;

        try {
            CrashDump32 crashDump = (CrashDump32) this.getDumpFormat();

            /**
             *
             * Obtiene dirección virtual de KDBG desde el encabezado del dump
             */
            String KDBGVirtualAddress = (String) crashDump.getHeader().getItems().get("KD_DEBUGGER_DATA_BLOCK").getContent();

            /**
             * El traductor calcula dirección física
             */
            BigInteger KDBGPhysicalAddress = this.getTranslator().calculatePhysicalAddress(KDBGVirtualAddress);

            long offset = this.getDumpFormat().getFileOffset(KDBGPhysicalAddress);

            String offsetHex = Conversor.longToHexString(offset);

        } catch (Exception ex) {

        }

        this.setEntities(entities);
    }

    @Override
    public EntitySingleList getSingleListContent(long position) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityList getListContent(long position) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BigInteger getNextEntityAddress(EntityList list, long entityListOffset) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void parse(long initialOffset, long maxOffset, Object[] params) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void parse(long offset, Object[] params) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object call() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isValidStructure(java.lang.Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Entity getEntityByAttributeValue(Entity entity, String attribute, Object content) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Entity parseEntityByOffset(long offset, Entity entity, Map.Entry<String, EntityAttribute> attribute) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map.Entry<String, EntityAttribute> setParserAttribute(Map.Entry<String,EntityAttribute> entry) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void parse(Entity entity, Object[] params) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean validateAttributeContent(Map.Entry<String, EntityAttribute> attribute) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

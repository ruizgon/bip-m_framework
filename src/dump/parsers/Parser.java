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
import entities.Entity;
import entities.EntityAttribute;
import entities.EntityList;
import entities.EntitySingleList;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import system.utils.Observable;
import translation.Translator;

/**
 *
 * @author Gonzalo
 */
public abstract class Parser extends Observable implements Runnable, Callable {

    private DumpFormat _dumpFormat;
    private OperatingSystemStructure _os;
    private Translator _translator;
    private ConcurrentHashMap<Object, Entity> _entities;
    private String status;
    private String statusDetail;
    private Calendar dateStatus;

    private long _initialOffset;
    private long _maxOffset;

    private HashMap<String, Parser> _helperParsers;

    public HashMap<String, Parser> getHelperParsers() {
        return _helperParsers;
    }

    public void setHelperParsers(HashMap<String, Parser> _helperParser) {
        this._helperParsers = _helperParser;
    }

    public Parser(DumpFormat _dumpFormat, OperatingSystemStructure _os) {
        this._dumpFormat = _dumpFormat;
        this._os = _os;
    }

    public Parser(DumpFormat _dumpFormat, OperatingSystemStructure _os, Translator _translator) {
        this._dumpFormat = _dumpFormat;
        this._os = _os;
        this._translator = _translator;
    }

    public DumpFormat getDumpFormat() {
        return _dumpFormat;
    }

    public void setDumpFormat(DumpFormat _dumpFormat) {
        this._dumpFormat = _dumpFormat;
    }

    public Translator getTranslator() {
        return _translator;
    }

    public void setTranslator(Translator _translator) {
        this._translator = _translator;
    }

    public ConcurrentHashMap<Object, Entity> getEntities() {
        return _entities;
    }

    public void setEntities(ConcurrentHashMap<Object, Entity> _entities) {
        this._entities = _entities;
    }

    public OperatingSystemStructure getOs() {
        return _os;
    }

    public void setOs(OperatingSystemStructure _os) {
        this._os = _os;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDetail() {
        return statusDetail;
    }

    public void setStatusDetail(String statusDetail) {
        this.statusDetail = statusDetail;
    }

    public Calendar getDateStatus() {
        return dateStatus;
    }

    public void setDateStatus(Calendar dateStatus) {
        this.dateStatus = dateStatus;
    }

    public long getInitialOffset() {
        return _initialOffset;
    }

    public void setInitialOffset(long _initialOffset) {
        this._initialOffset = _initialOffset;
    }

    public long getMaxOffset() {
        return _maxOffset;
    }

    public void setMaxOffset(long _endOffset) {
        this._maxOffset = _endOffset;
    }

    /**
     * Debe setear _entities
     */
    public abstract void parse(Object[] params);

    /**
     *
     * @param entity
     * @param params: parámetros para que el parser pueda tomar decisiones.
     */
    public abstract void parse(Entity entity, Object[] params);

    /**
     * Toma importancia cuando es costoso el parseo de las estructuras y se
     * requiere de la obtención de la entidad, una a una
     *
     * @param offset
     */
    public abstract void parse(long offset, Object[] params);

    public abstract void parse(long initialOffset, long maxOffset, Object[] params);

    public abstract Entity parseEntityByOffset(long offset, Entity entity, Map.Entry<String, EntityAttribute> attribute);

    public abstract boolean isValidStructure(java.lang.Object o);

    public abstract EntitySingleList getSingleListContent(long position);

    public abstract EntityList getListContent(long position);

    public abstract BigInteger getNextEntityAddress(EntityList list, long entityListOffset);

    public abstract boolean validateAttributeContent(Map.Entry<String, EntityAttribute> attribute);

    /**
     *
     * @param entity
     * @param attribute
     * @param content
     * @return
     */
    public abstract Entity getEntityByAttributeValue(Entity entity, String attribute, Object content);

    public abstract Map.Entry<String, EntityAttribute> setParserAttribute(Map.Entry<String, EntityAttribute> entry);

    public long getAddressContentAccordingToPointerMultiplicity(long absoluteOffset, Map.Entry<String, EntityAttribute> entry) {
        long absoluteAttributeOffset = 0;

        try {
            String content = null;
            BigInteger address = BigInteger.ZERO;
            long offsetAttr = 0;
            int pointerMultiplicity = entry.getValue().getPointerMultiplicity();

            absoluteAttributeOffset = absoluteOffset + entry.getValue().getPosition();
            while (pointerMultiplicity > 0) {

                content = this.getDumpFormat().getDataManager().getItemContent(entry.getValue().getContentType(), absoluteAttributeOffset, entry.getValue().getLength(), entry.getValue().isUnion());

                address = this.getTranslator().calculatePhysicalAddress(content);

                offsetAttr = this.getDumpFormat().getFileOffset(address);

                absoluteAttributeOffset = offsetAttr;

                pointerMultiplicity--;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return absoluteAttributeOffset;
    }

    public Map.Entry<String, EntityAttribute> getEntryAttributeContent(long absoluteOffset, Map.Entry<String, EntityAttribute> e) {

        if (e.getValue().isEnabledParse()) {

            if (e.getValue().getPointerMultiplicity() > 0 && !e.getValue().isResolveMultiplicity()) {
                absoluteOffset = this.getAddressContentAccordingToPointerMultiplicity(absoluteOffset, e);
            }

            if (absoluteOffset >= 0) {
                e.getValue().setOffset(absoluteOffset);
                if (e.getValue().getEntity() != null) {
                    Map.Entry<String, EntityAttribute> entry = setParserAttribute(e);
                    e = entry;
                }

                e.getValue().getAttributeContent(e);
                if (e.getValue().getEntity() != null) {
                    e.getValue().setContent(e.getValue().getEntity());
                }
            }
        }

        return e;
    }

}

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

import java.util.Map;
import system.utils.DataManager;
import system.utils.DataPrimitiveType;

/**
 *
 * @author Gonzalo
 */
public class EntityAttribute {

    protected String _nombre;
    protected int _position;
    protected String _complexContentType;
    protected String _contentType;
    protected Object _content;
    protected int _length;
    protected boolean _union;
    protected int _pointerMultiplicity;
    protected boolean _bigEndian;
    protected long _offset;
    protected boolean _resolveMultiplicity;
    /**
     * Puede tener una entidad asociada cuando el atributo es del tipo de
     * estructura compleja
     */
    protected Entity _entity;
    protected boolean _enabledParse;
    protected long _baseDir; //Dirección base, aplica cuando se trabaja con RVA
    protected boolean _valid;
    protected boolean _shiftPosition; //Si el valor es true, fuerza el shift desde el offset, aunque pointer multiplicity > 0

    public EntityAttribute(String _nombre, int _position, String _complexContentType, String _contentType, int _length, boolean _union) {
        this._nombre = _nombre;
        this._position = _position;
        this._complexContentType = _complexContentType;
        this._contentType = _contentType;
        this._length = _length;
        this._union = _union;
        this._pointerMultiplicity = 0;
        this._bigEndian = false;
        this._resolveMultiplicity = false;
        this._enabledParse = true;
        this._valid = true;
        this._shiftPosition = false;
    }

    public EntityAttribute(String _nombre, int _position, String _complexContentType, String _contentType, int _length, boolean _union, Entity _entity) {
        this._nombre = _nombre;
        this._position = _position;
        this._complexContentType = _complexContentType;
        this._contentType = _contentType;
        this._length = _length;
        this._union = _union;
        this._pointerMultiplicity = 0;
        this._bigEndian = false;
        this._entity = _entity;
        this._resolveMultiplicity = false;
        this._enabledParse = true;
        this._valid = true;
        this._shiftPosition = false;
    }

    public EntityAttribute(String _nombre, int _position, String _complexContentType, String _contentType, boolean _union) {
        this._nombre = _nombre;
        this._position = _position;
        this._complexContentType = _complexContentType;
        this._contentType = _contentType;
        this._length = 1;
        this._union = _union;
        this._pointerMultiplicity = 0;
        this._bigEndian = false;
        this._resolveMultiplicity = false;
        this._enabledParse = true;
        this._valid = true;
        this._shiftPosition = false;
    }

    public EntityAttribute(String _nombre, int _position, String _complexContentType, String _contentType, boolean _union, Entity _entity) {
        this._nombre = _nombre;
        this._position = _position;
        this._complexContentType = _complexContentType;
        this._contentType = _contentType;
        this._length = 1;
        this._union = _union;
        this._pointerMultiplicity = 0;
        this._bigEndian = false;
        this._entity = _entity;
        this._resolveMultiplicity = false;
        this._enabledParse = true;
        this._valid = true;
        this._shiftPosition = false;
    }

    public EntityAttribute(String _nombre, int _position, String _complexContentType, String _contentType, int _length, boolean _union, int _pointerMultiplicity, boolean _bigEndian) {
        this._nombre = _nombre;
        this._position = _position;
        this._complexContentType = _complexContentType;
        this._contentType = _contentType;
        this._length = _length;
        this._union = _union;
        this._pointerMultiplicity = _pointerMultiplicity;
        this._bigEndian = _bigEndian;
        this._resolveMultiplicity = false;
        this._enabledParse = true;
        this._valid = true;
        this._shiftPosition = false;
    }

    public EntityAttribute(String _nombre, int _position, String _complexContentType, String _contentType, int _length, boolean _union, int _pointerMultiplicity, boolean _bigEndian, Entity _entity) {
        this._nombre = _nombre;
        this._position = _position;
        this._complexContentType = _complexContentType;
        this._contentType = _contentType;
        this._length = _length;
        this._union = _union;
        this._pointerMultiplicity = _pointerMultiplicity;
        this._bigEndian = _bigEndian;
        this._entity = _entity;
        this._resolveMultiplicity = false;
        this._enabledParse = true;
        this._valid = true;
        this._shiftPosition = false;
    }

    public String getNombre() {
        return _nombre;
    }

    public void setNombre(String _nombre) {
        this._nombre = _nombre;
    }

    public int getPosition() {
        return _position;
    }

    public void setPosition(int _position) {
        this._position = _position;
    }

    public String getComplexContentType() {
        return _complexContentType;
    }

    public void setComplexContentType(String _complexContentType) {
        this._complexContentType = _complexContentType;
    }

    public String getContentType() {
        return _contentType;
    }

    public void setContentType(String _contentType) {
        this._contentType = _contentType;
    }

    public Object getContent() {
        return _content;
    }

    public void setContent(Object _content) {
        this._content = _content;
    }

    public int getLength() {
        return _length;
    }

    public void setLength(int _length) {
        this._length = _length;
    }

    public boolean isUnion() {
        return _union;
    }

    public void setUnion(boolean _union) {
        this._union = _union;
    }

    public int getPointerMultiplicity() {
        return _pointerMultiplicity;
    }

    public void setPointerMultiplicity(int _pointerMultiplicity) {
        this._pointerMultiplicity = _pointerMultiplicity;
    }

    public boolean isBigEndian() {
        return _bigEndian;
    }

    public void setBigEndian(boolean _bigEndian) {
        this._bigEndian = _bigEndian;
    }

    public long getOffset() {
        return _offset;
    }

    public void setOffset(long _offset) {
        this._offset = _offset;
    }

    public Entity getEntity() {
        return _entity;
    }

    public void setEntity(Entity _entity) {
        this._entity = _entity;
    }

    public boolean isResolveMultiplicity() {
        return _resolveMultiplicity;
    }

    public void setResolveMultiplicity(boolean _resolveMultiplicity) {
        this._resolveMultiplicity = _resolveMultiplicity;
    }

    public boolean isEnabledParse() {
        return _enabledParse;
    }

    public void setEnabledParse(boolean _enabledParse) {
        this._enabledParse = _enabledParse;
    }

    public long getBaseDir() {
        return _baseDir;
    }

    public void setBaseDir(long baseDir) {
        this._baseDir = baseDir;
    }

    public boolean isValid() {
        return _valid;
    }

    public void setValid(boolean valid) {
        this._valid = valid;
    }

    public boolean isShiftPosition() {
        return _shiftPosition;
    }

    public void setShiftPosition(boolean _shiftPosition) {
        this._shiftPosition = _shiftPosition;
    }

    public void getAttributeContent(Map.Entry<String, EntityAttribute> entry) {
        try {
            long offset = 0;
            //offset = this._offset + this._position;
            if (entry.getValue().getPointerMultiplicity() == 0 || entry.getValue().isShiftPosition()) {
                offset = this._offset + this._position;
            } else {
                offset = this._offset;
            }
            if (DataPrimitiveType.getInstance().isPrimitiveType(this._contentType) && this._entity == null) {
                String respuesta = DataManager.getInstance().getComplexItemContent(this._complexContentType, this._contentType, offset, this._length, this._union, this._bigEndian);
                this._content = respuesta;
            } else {//Solicita al propio objeto que obtenga el contenido de sus atributos
                if (this._entity != null) {
                    this._entity.getAttributesContent(offset, entry);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

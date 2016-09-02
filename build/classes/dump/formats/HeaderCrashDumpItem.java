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
package dump.formats;

/**
 *
 * @author Gonzalo
 */
public class HeaderCrashDumpItem {

    private String _nombre;
    private int _position;
    private String _contentType;
    private Object _content;
    private int _length;
    private boolean _littleEndian;

    /**
     *
     * @param _nombre Nombre del item del encabezado.
     * @param _position Posición absoluta del item en el encabezado.
     * @param _contentType Tipo de contenido del item.
     */
    public HeaderCrashDumpItem(String _nombre, int _position, String _contentType) {
        this._nombre = _nombre;
        this._position = _position;
        this._contentType = _contentType;
        this._length = 1;
        this._littleEndian = _littleEndian;
    }

    /**
     *
     * @param _nombre Nombre del item del encabezado.
     * @param _position Posición absoluta del item en el encabezado.
     * @param _contentType Tipo de contenido del item.
     * @param _littleEndian Indica si la persistencia en el volcado es
     * littleEndian o no.
     */
    public HeaderCrashDumpItem(String _nombre, int _position, String _contentType, boolean _littleEndian) {
        this._nombre = _nombre;
        this._position = _position;
        this._contentType = _contentType;
        this._length = 1;
        this._littleEndian = _littleEndian;
    }

    /**
     *
     * @param _nombre Nombre del item del encabezado.
     * @param _position Posición absoluta del item en el encabezado.
     * @param _contentType Tipo de contenido del item.
     * @param _length Longitud (opcional por sobrecarga).
     */
    public HeaderCrashDumpItem(String _nombre, int _position, String _contentType, int _length) {
        this._nombre = _nombre;
        this._position = _position;
        this._contentType = _contentType;
        this._length = _length;
        this._littleEndian = false;
    }

    /**
     *
     * @param _nombre Nombre del item del encabezado.
     * @param _position Posición absoluta del item en el encabezado.
     * @param _contentType Tipo de contenido del item.
     * @param _length Longitud (opcional por sobrecarga).
     * @param _littleEndian Indica si la persistencia en el volcado es
     * littleEndian o no.
     */
    public HeaderCrashDumpItem(String _nombre, int _position, String _contentType, int _length, boolean _littleEndian) {
        this._nombre = _nombre;
        this._position = _position;
        this._contentType = _contentType;
        this._length = _length;
        this._littleEndian = false;
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

    public void setLength(int length) {
        this._length = length;
    }

    public boolean isLittleEndian() {
        return _littleEndian;
    }

    public void setLittleEndian(boolean _littleEndian) {
        this._littleEndian = _littleEndian;
    }

}

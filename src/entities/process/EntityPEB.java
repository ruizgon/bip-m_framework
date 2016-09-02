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
package entities.process;

import entities.Entity;


/**
 *
 * @author Gonzalo
 */
public abstract class EntityPEB  extends Entity{

    public static final String _TAG = "PEB";
    private String _imageBaseAddress;
    private String _processFlags;
    private String _OSVersionInfo;
    
    //puntero que guarda la dirección de EntityPebLdrDataWindows732
    private String _ldr_ptr;
    private EntityPebLdrData _PEB_LDR_DATA;

    public String getLdr_ptr() {
        return _ldr_ptr;
    }

    public void setLdr_ptr(String _ldr_ptr) {
        this._ldr_ptr = _ldr_ptr;
    }

    public String getImageBaseAddress() {
        return _imageBaseAddress;
    }

    public void setImageBaseAddress(String imageBaseAddress) {
        this._imageBaseAddress = imageBaseAddress;
    }

    public String getProcessFlags() {
        return _processFlags;
    }

    public void setProcessFlags(String processFlags) {
        this._processFlags = processFlags;
    }

    public String getOSVersionInfo() {
        return _OSVersionInfo;
    }

    public void setOSVersionInfo(String OSVersionInfo) {
        this._OSVersionInfo = OSVersionInfo;
    }


}

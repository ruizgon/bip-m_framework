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

import system.utils.RandomFileSeeker;

/**
 *
 * @author Gonzalo
 */
public class ExceptionRecord32 extends ExceptionRecord {

    protected int _exceptionCode;
    protected int _exceptionFlags;
    protected int _exceptionRecord;
    protected int _exceptionAddress;
    protected int _numberParameters;
    protected int[] _exceptionInformation = new int[15];

    public ExceptionRecord32(boolean _littleEndian, RandomFileSeeker _randomFileSeeker) {
        super(_littleEndian, _randomFileSeeker);
    }

    public int getExceptionCode() {
        return _exceptionCode;
    }

    public void setExceptionCode(int _exceptionCode) {
        this._exceptionCode = _exceptionCode;
    }

    public int getExceptionFlags() {
        return _exceptionFlags;
    }

    public void setExceptionFlags(int _exceptionFlags) {
        this._exceptionFlags = _exceptionFlags;
    }

    public int getExceptionRecord() {
        return _exceptionRecord;
    }

    public void setExceptionRecord(int _exceptionRecord) {
        this._exceptionRecord = _exceptionRecord;
    }

    public int getExceptionAddress() {
        return _exceptionAddress;
    }

    public void setExceptionAddress(int _exceptionAddress) {
        this._exceptionAddress = _exceptionAddress;
    }

    public int getNumberParameters() {
        return _numberParameters;
    }

    public void setNumberParameters(int _numberParameters) {
        this._numberParameters = _numberParameters;
    }

    public int[] getExceptionInformation() {
        return _exceptionInformation;
    }

    public void setExceptionInformation(int[] _exceptionInformation) {
        this._exceptionInformation = _exceptionInformation;
    }

    @Override
    public void obtainExceptionRecordData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

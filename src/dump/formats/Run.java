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

import java.math.BigInteger;
import system.utils.RandomFileSeeker;

/**
 *
 * @author Gonzalo
 */
public abstract class Run {

    private long _startOffset;
    private boolean _littleEndian;
    private RandomFileSeeker _randomFileSeeker;
    private long _fileOffset;
    private BigInteger _startAddress;
    private String _startAddressHex;//[hex]
    private BigInteger _length;
    private String _lengthHex;//[hex]

    public Run(long _startOffset, boolean _littleEndian) {
        this._startOffset = _startOffset;
        this._littleEndian = _littleEndian;
    }

    public long getStartOffset() {
        return _startOffset;
    }

    public void setStartOffset(long _startOffset) {
        this._startOffset = _startOffset;
    }

    public boolean isLittleEndian() {
        return _littleEndian;
    }

    public void setLittleEndian(boolean _littleEndian) {
        this._littleEndian = _littleEndian;
    }

    public RandomFileSeeker getRandomFileSeeker() {
        return _randomFileSeeker;
    }

    public void setRandomFileSeeker(RandomFileSeeker _randomFileSeeker) {
        this._randomFileSeeker = _randomFileSeeker;
    }

    public long getFileOffset() {
        return _fileOffset;
    }

    public void setFileOffset(long _fileOffset) {
        this._fileOffset = _fileOffset;
    }

    public BigInteger getStartAddress() {
        return _startAddress;
    }

    public void setStartAddress(BigInteger _startAddress) {
        this._startAddress = _startAddress;
    }

    public String getStartAddressHex() {
        return _startAddressHex;
    }

    public void setStartAddressHex(String _startAddressHex) {
        this._startAddressHex = _startAddressHex;
    }

    public BigInteger getLength() {
        return _length;
    }

    public void setLength(BigInteger _length) {
        this._length = _length;
    }

    public String getLengthHex() {
        return _lengthHex;
    }

    public void setLengthHex(String _lengthHex) {
        this._lengthHex = _lengthHex;
    }

    public abstract void obtainRunData();
}

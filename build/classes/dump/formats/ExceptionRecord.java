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
public abstract class ExceptionRecord {

    protected boolean _littleEndian;
    protected RandomFileSeeker _randomFileSeeker;

    public ExceptionRecord(boolean _littleEndian, RandomFileSeeker _randomFileSeeker) {
        this._littleEndian = _littleEndian;
        this._randomFileSeeker = _randomFileSeeker;
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
    
    public abstract void obtainExceptionRecordData();
}

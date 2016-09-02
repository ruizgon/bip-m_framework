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

import java.util.Observable;
import system.utils.RandomFileSeeker;

/**
 *
 * @author Gonzalo
 */
public abstract class PhysicalMemoryDescriptor extends Observable {

    private boolean _littleEndian;
    private RandomFileSeeker _randomFileSeeker;
    private int _numberOfRuns; //[hex]
    private long _numberOfPages;
    private String _numberOfPagesHexString; //[hex]

    public PhysicalMemoryDescriptor(boolean _littleEndian) {
        this._littleEndian = _littleEndian;
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

    public int getNumberOfRuns() {
        return _numberOfRuns;
    }

    public void setNumberOfRuns(int _numberOfRuns) {
        this._numberOfRuns = _numberOfRuns;
    }

    public long getNumberOfPages() {
        return _numberOfPages;
    }

    public void setNumberOfPages(long _numberOfPages) {
        this._numberOfPages = _numberOfPages;
    }

    public String getNumberOfPagesHexString() {
        return _numberOfPagesHexString;
    }

    public void setNumberOfPagesHexString(String _numberOfPagesHexString) {
        this._numberOfPagesHexString = _numberOfPagesHexString;
    }

    public abstract void obtainNumberOfRuns();

    public abstract void obtainNumberOfPages();

    public abstract void obtainRuns();
}

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
import system.utils.DataManager;
import system.utils.Observable;
import system.utils.RandomFileSeeker;

/**
 *
 * @author Gonzalo
 */
public abstract class DumpFormat extends Observable{

    public static final String _TAG = "Dump";
    
    private String _path;
    private String extension;
    private float size;
    private String sizeUnit;
    private String content;
    protected boolean _littleEndian;
    protected RandomFileSeeker _randomFileSeeker;
    protected DataManager _dataManager;

    public DumpFormat(String _path) {
        this._path = _path;
    }

    public abstract void getDumpFormatContent();

    public String getPath() {
        return _path;
    }

    public void setPath(String _path) {
        this._path = _path;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public String getSizeUnit() {
        return sizeUnit;
    }

    public void setSizeUnit(String sizeUnit) {
        this.sizeUnit = sizeUnit;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isLittleEndian() {
        return _littleEndian;
    }

    public RandomFileSeeker getRandomFileSeeker() {
        return _randomFileSeeker;
    }

    public void setRandomFileSeeker(RandomFileSeeker _randomFileSeeker) {
        this._randomFileSeeker = _randomFileSeeker;
    }

    public DataManager getDataManager() {
        return _dataManager;
    }

    public void setDataManager(DataManager _dataManager) {
        this._dataManager = _dataManager;
    }

    public abstract void setLittleEndian(boolean _littleEndian);

    public abstract long getFileOffset(BigInteger position);

    public abstract String getContentByOffset(long offset, int length);
    
    public abstract BigInteger getPhysicalAddresByOffset(long offset);
    
    public abstract void processCommand(String command, String modifier);
    
}

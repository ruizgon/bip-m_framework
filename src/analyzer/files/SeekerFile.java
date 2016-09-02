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
package analyzer.files;

import analyzer.Seeker;
import analyzer.states.FileState;
import java.util.ArrayList;
import entities.EntityFile;

/**
 *
 * @author Gonzalo
 */
public abstract class SeekerFile extends Seeker {

    private ArrayList<EntityFile> _fileList;
    private EntityFile _fileLocated;
    private EntityFile _firstFileLocated;
    private int _fileCount;
    private FileState _fileState;

    public SeekerFile() {
    }

    /*
     *Constructor para cuando decora a otro Seeker
     */
    public SeekerFile(Seeker s) {
        super.setSeeker(s);
    }

    public ArrayList<EntityFile> getFileList() {
        return _fileList;
    }

    public void setFileList(ArrayList<EntityFile> fileList) {
        this._fileList = fileList;
    }

    public EntityFile getFileLocated() {
        return _fileLocated;
    }

    public void setFileLocated(EntityFile fileLocated) {
        this._fileLocated = fileLocated;
    }

    public EntityFile getFirstFileLocated() {
        return _firstFileLocated;
    }

    public void setFirstFileLocated(EntityFile firstFileLocated) {
        this._firstFileLocated = firstFileLocated;
    }

    public int getFileCount() {
        return _fileCount;
    }

    public void setFileCount(int fileCount) {
        this._fileCount = fileCount;
    }

    public FileState getFileState() {
        return _fileState;
    }

    public void setFileState(FileState fileState) {
        this._fileState = fileState;
    }

    public void seekFileList() {
        _fileList = new ArrayList<>();
        EntityFile file;

        _firstFileLocated = getFirstFile(_fileState);

        if (_firstFileLocated != null) {
            _fileList.add(_firstFileLocated);
            file = getNextFile(_firstFileLocated, _fileState);
            while (file != null) {
                _fileList.add(file);
                file = getNextFile(file, _fileState);
            }
        }

        _fileCount = _fileList.size();
    }

    public void seekFilesProcessById(int id) {
        /*
         * TODO:
         * Algoritmo para búsqueda de archivo
         */
    }

    public void seekSpecificFileByName(String s) {
        /*
         * TODO:
         * Algoritmo para búsqueda de archivo
         */
    }

    public void seekSpecificFileByOwner(String s) {
        /*
         * TODO:
         * Algoritmo para búsqueda de archivo
         */
    }

    public void calculateFileCount() {
        /*
         * TODO:
         * Algoritmo para búsqueda de archivo
         */
    }

    /*
    * Métodos hook
    */
    protected abstract EntityFile getFirstFile(FileState s);

    protected abstract EntityFile getNextFile(EntityFile file, FileState s);

    protected abstract EntityFile getPrevProcess(EntityFile file, FileState s);
}

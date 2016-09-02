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
package system.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import output.OutputManagerException;

/**
 *
 * @author Gonzalo
 */
public class RandomFileSeeker extends Observable {

    private RandomAccessFile _randomAccesFile;
    /**
     * @String indica la etiqueta del tipo de datos
     * @Object indica la longitud en bits de lo que debe recolectar
     */
    private Map<String, Object> _dataTypes;
    private Object PhysicalMemoryDescriptor32;
    private String _path;

    /**
     * Inicializa tipos de datos (sólo primitivos) Registra observadores
     */
    public RandomFileSeeker() {
        this._dataTypes = new HashMap<>();
        //this._dataTypes.put("char", 8);
        Map<Integer, String> nombreMap = new HashMap<Integer, String>();
        nombreMap.size();
        this._dataTypes.put("int 8", 8);
        this._dataTypes.put("int 16", 16);
        this._dataTypes.put("int 32", 32);
        this._dataTypes.put("int 64", 64);

        this.addObserver(OutputManagerException.getInstance());
    }

    @Override
    public synchronized int countObservers() {
        return super.countObservers(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized boolean hasChanged() {
        return super.hasChanged(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected synchronized void clearChanged() {
        super.clearChanged(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected synchronized void setChanged() {
        super.setChanged(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized void deleteObservers() {
        super.deleteObservers(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void notifyObservers(Object o) {
        super.notifyObservers(o); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void notifyObservers() {
        super.notifyObservers(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized void deleteObserver(Observer obsrvr) {
        super.deleteObserver(obsrvr); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized void addObserver(Observer obsrvr) {
        super.addObserver(obsrvr); //To change body of generated methods, choose Tools | Templates.
    }

    public synchronized void obtainRandomAccessFile(String path) {
        RandomAccessFile file = null;

        try {
            file = new RandomAccessFile(path, "r");
        } catch (FileNotFoundException ex) {
            this.notifyObservers(ex);
            System.out.println("Error: archivo no encontrado.");
        }

        this._randomAccesFile = file;
        this._path = path;
    }

    /**
     *
     * @param pos posición desde la cual leer en el archivo
     * @param contentType tipo de contenido que va a leer
     * @param length longitud de lo que debe leer
     * @return String de 0s y 1s
     */
    public synchronized String getBinaryContent(long pos, String contentType, int length) {
        String content = "";

        try {
            int i = 0;
            StringBuilder outputBinary = new StringBuilder();
            char[] cArray = new char[length - 1];
            while (i < length) {
                this._randomAccesFile.seek(pos);
                char c = (char) this._randomAccesFile.read();//VER SI FUNCIONA
                cArray[i++] = c;
            }

            /**
             * Recorrro el arreglo de caracteres y lo convierto en una cadena de
             * 0s y 1s
             */
            for (char c : cArray) {
                outputBinary.append(Conversor.charToBinaryString(c));
            }

            content = outputBinary.toString();
        } catch (IOException ex) {
            this.notifyObservers(ex);
            ex.printStackTrace();
        }

        return content;
    }

    public synchronized String getContent(long pos, int length) {
        String content = "";

        try {
            if (pos >= 0) {
                long position = pos;
                int i = 0;
                StringBuilder output = new StringBuilder();

                while (i < length) {
                    this._randomAccesFile.seek(position++);
                    output.append((char) this._randomAccesFile.read());
                    i++;
                }
                content = output.toString();
            }
        } catch (IOException ex) {
            this.notifyObservers(ex);
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }

        return content;
    }

    public long obtainFileLength() throws IOException {
        return this._randomAccesFile != null ? this._randomAccesFile.length() : -1;
    }

}

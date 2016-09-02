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
import java.util.HashMap;
import java.util.Observer;
import system.utils.BinaryOperator;
import system.utils.CharOperator;
import system.utils.Conversor;

/**
 *
 * @author Gonzalo
 */
public class PhysicalMemoryDescriptor64 extends PhysicalMemoryDescriptor {

    private static int _POSITION = 0x88;
    private static int _NUMBER_OF_RUNS_SIZE = 52; //[bits]
    private static int _NUMBER_OF_PAGES_SIZE = 64; //[bits]
    private static int _START_OFFSET_RUNS = 116; //[bits]

    private HashMap<Integer, Run64> _runs; //[hex]

    public PhysicalMemoryDescriptor64(boolean _littleEndian) {
        super(_littleEndian);
    }

    public HashMap<Integer, Run64> getRuns() {
        return _runs;
    }

    public void setRuns(HashMap<Integer, Run64> _runs) {
        this._runs = _runs;
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

    @Override
    public void obtainNumberOfRuns() {
        try {
            int length = BinaryOperator.numberOfChar(_NUMBER_OF_RUNS_SIZE);
            String content = this.getRandomFileSeeker().getContent(_POSITION, length);
            if (this.isLittleEndian()) {
                content = CharOperator.getLittleEndianCharArray(content).toString();
            }
            String contentBin = Conversor.stringToBinaryString(content);

            int contentLength = contentBin.length();
            int left = contentLength - _NUMBER_OF_RUNS_SIZE;

            content = BinaryOperator.cutBinaryStringLeft(contentBin, left);

            this.setNumberOfRuns(Integer.parseInt(content, 2));

            String mensaje = "Se obtuvo número de Runs: ";
            mensaje.concat(String.valueOf(this.getNumberOfRuns()));
            notifyObservers(mensaje);
        } catch (Exception ex) {
            notifyObservers(ex);
        }
    }

    @Override
    public void obtainNumberOfPages() {
        try {
            int length = BinaryOperator.numberOfChar(_NUMBER_OF_PAGES_SIZE) + 1;
            String content = this.getRandomFileSeeker().getContent(_POSITION + new Double(_NUMBER_OF_RUNS_SIZE / 8).intValue(), length);
            if (this.isLittleEndian()) {
                content = CharOperator.getLittleEndianCharArray(content).toString();
            }
            String contentBin = Conversor.stringToBinaryString(content);

            int contentLength = contentBin.length();
            int left = (contentLength - _NUMBER_OF_PAGES_SIZE) / 2;

            contentBin = BinaryOperator.cutBinaryStringLeft(contentBin, left);
            content = BinaryOperator.cutBinaryStringRight(contentBin, left);

            String hex = String.format("%" + String.valueOf(_NUMBER_OF_PAGES_SIZE / 8) + "X", Long.parseLong(content, 2));

            this.setNumberOfPages(Long.parseLong(hex, 16));
            this.setNumberOfPagesHexString(hex);

            String mensaje = "Se obtuvo número de Pages: ";
            mensaje.concat(String.valueOf(this.getNumberOfPages()));
            notifyObservers(mensaje);
        } catch (Exception ex) {
            notifyObservers(ex);
        }
    }

    @Override
    public void obtainRuns() {
        try {
            long offset = 8192;

            this._runs = new HashMap<Integer, Run64>();

            int i = 0;
            while (i < this.getNumberOfRuns()) {
                int runOffset = i * new Double(Run64._RUN_SIZE / 8).intValue();
                int position = _POSITION + new Double(_START_OFFSET_RUNS / 8).intValue() + runOffset;
                Run64 run = new Run64(position, this.isLittleEndian());
                run.setRandomFileSeeker(getRandomFileSeeker());
                run.obtainRunData();

                run.setFileOffset(offset);

                this._runs.put(i++, run);

                offset = run.getLength().add(BigInteger.valueOf(offset)).longValue();
            }
        } catch (Exception ex) {

        }
    }

}

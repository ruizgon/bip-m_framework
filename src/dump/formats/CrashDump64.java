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
import java.util.Map;
import output.OutputManagerConsole;
import system.utils.CharOperator;
import system.utils.Conversor;
import system.utils.DataManager;
import system.utils.RandomFileSeeker;

/**
 *
 * @author Gonzalo
 */
public class CrashDump64 extends CrashDump {

    private HeaderCrashDump64 _header;

    public CrashDump64(String _path, boolean _littleEndian) {
        super(_path);
        this._randomFileSeeker = new RandomFileSeeker();
        this._header = new HeaderCrashDump64(_path, this._randomFileSeeker, this._littleEndian);
        this._dataManager = DataManager.getInstance(_path, _randomFileSeeker, _littleEndian);
        
        /**
         * Registra observadores
         */
        this.addObserver(OutputManagerConsole.getInstance());
    }

    public HeaderCrashDump64 getHeader() {
        return _header;
    }

    public void setHeader(HeaderCrashDump64 _header) {
        this._header = _header;
    }

    @Override
    public void setLittleEndian(boolean _littleEndian) {
        this._littleEndian = _littleEndian;
        this._header.setLittleEndianProcess(_littleEndian);
    }

    @Override
    public long getFileOffset(BigInteger position) {
        long offset = 0;

        PhysicalMemoryDescriptor64 physicalMemoryDescriptor64 = (PhysicalMemoryDescriptor64) this._header.getItems().get("PHYSICAL_MEMORY_BLOCK_BUFFER").getContent();

        Map<Integer, Run64> runs = physicalMemoryDescriptor64.getRuns();

        int numberOfRuns = physicalMemoryDescriptor64.getNumberOfRuns();

        int i = 0;
        Run64 run = null;

        while (i < numberOfRuns) {
            run = runs.get(i);

            BigInteger start = run.getStartAddress();
            BigInteger end = start.add(run.getLength());

            if (position.compareTo(start) >= 0 && position.compareTo(end) <= 0) {
                break;
            }

            i++;
        }

        offset = position.subtract(run.getStartAddress()).longValue() + run.getFileOffset();

        return offset;
    }

    @Override
    public void getDumpFormatContent() {
        this.obtainHeader();
    }

    @Override
    public String getContentByOffset(long offset, int length) {
        String content = this._randomFileSeeker.getContent(offset, length);

        if (_littleEndian) {
            content = CharOperator.getLittleEndianCharArray(content).toString();
        }

        return content;
    }

    @Override
    public void obtainHeader() {
        this._header.executeObtainingItemsContentProcess();
    }

    @Override
    public BigInteger getPhysicalAddresByOffset(long offset) {
        BigInteger physicalAddress = BigInteger.ZERO;

        try {
            if (this._header != null) {
                this._header.getItems().get("");
                PhysicalMemoryDescriptor64 physicalMemoryDescriptor64 = (PhysicalMemoryDescriptor64) this._header.getItems().get("PHYSICAL_MEMORY_BLOCK_BUFFER").getContent();
                Map<Integer, Run64> runs = physicalMemoryDescriptor64.getRuns();
                int numberOfRuns = physicalMemoryDescriptor64.getNumberOfRuns();

                int i = 0;
                Run64 run = null;
                long fileOffset = 0;
                long endFileOffset = 0;
                BigInteger diff = BigInteger.ZERO;
                BigInteger start = BigInteger.ZERO;
                BigInteger end = BigInteger.ZERO;

                while (i < numberOfRuns) {
                    run = runs.get(i);

                    fileOffset = run.getFileOffset();
                    endFileOffset = fileOffset + run.getLength().longValue();
                    start = run.getStartAddress();
                    end = start.add(run.getLength());

                    if (BigInteger.valueOf(offset).compareTo(BigInteger.valueOf(fileOffset)) >= 0 && BigInteger.valueOf(offset).compareTo(BigInteger.valueOf(endFileOffset)) <= 0) {
                        diff = BigInteger.valueOf(offset).subtract(BigInteger.valueOf(fileOffset));
                        break;
                    }

                    i++;
                }

                physicalAddress = start.add(diff);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return physicalAddress;
    }

    @Override
    public void processCommand(String command, String modifier) {
        try {
            String log = "";
            switch (command) {
                case "offset":
                    long offset = this.getFileOffset(Conversor.hexToBigInteger(modifier));
                    /**
                     * Notifica a observadores
                     */
                    String offsetHex = Conversor.longToHexString(offset);
                    log = "Physical Address:"
                            + "    0x"
                            + modifier
                            + "    "
                            + "Offset:"
                            + "    0x"
                            + offsetHex;
                    this.notifyObservers(log);
                    break;
                case "pabyoffset":
                    BigInteger pa = this.getPhysicalAddresByOffset(Conversor.hexToLong(modifier));
                    /**
                     * Notifica a observadores
                     */
                    String paHex = Conversor.bigIntToHexString(pa);
                    log = "Offset:"
                            + "    0x"
                            + modifier
                            + "    "
                            + "Physical Address:"
                            + "    0x"
                            + paHex;
                    this.notifyObservers(log);
                    break;
                default: //notificar que comando no está soportado
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

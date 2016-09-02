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
package translation;

import dump.formats.DumpFormat;
import dump.ooss.OperatingSystemStructure;
import entities.translation.EntityAddressSpace;
import entities.translation.EntityAddressSpaceWin64;
import java.math.BigInteger;
import output.OutputManagerConsole;
import system.utils.BinaryOperator;
import system.utils.Conversor;

/**
 *
 * @author Gonzalo
 */
public class TranslatorWin64 extends TranslatorWin {

    private static final int _PMLSELECTOR_ENTRIES = 4; //PAGE MAP LEVEL SELECTOR
    private static final int _PMLSELECTOR_ENTRY_LENGTH = 8; //Para traducción
    private static final int _PMLSELECTOR_ENTRY_BYTES = 8;
    private static final int _PDPT_ENTRIES = 4;
    private static final int _PDPT_ENTRY_BYTES = 8;
    private static final int _PDPT_ENTRY_LENGTH = 8; //Para traducción
    private static final int _PD_TABLES = 4; //Por Proceso
    private static final int _PDT_ENTRIES = 512; //Por Tabla
    private static final int _PDT_ENTRY_BYTES = 8;
    private static final int _PDT_ENTRY_LENGTH = 8;//Para traducción
    private static final int _PT_ENTRIES = 512; //Por Tabla
    private static final int _PT_ENTRY_BYTES = 8;//Para obtención desde archivo
    private static final int _PT_ENTRY_LENGTH = 8;//Para obtención desde archivo
    private static final int _PAGE_BYTE_ENTRY_LENGTH = 4096;//Tamaño de página, para traducción
    private static final int _OFFSET_ENTRY_BYTES = 8;

    private String _PML4;
    private String _PDPE;
    private String _PDE;
    private String _PTE;

    private EntityAddressSpaceWin64 _currentAS;

    public TranslatorWin64() {
        this._currentAS = new EntityAddressSpaceWin64();

        /**
         * Registra observadores
         */
        this.addObserver(OutputManagerConsole.getInstance());
    }

    /**
     *
     * @param structure
     * @param dumpFormat
     */
    public TranslatorWin64(OperatingSystemStructure structure, DumpFormat dumpFormat) {
        super(structure, dumpFormat);
        this._currentAS = new EntityAddressSpaceWin64();

        /**
         * Registra observadores
         */
        this.addObserver(OutputManagerConsole.getInstance());
    }

    public EntityAddressSpaceWin64 getCurrentAS() {
        return _currentAS;
    }

    public void setCurrentAS(EntityAddressSpaceWin64 _currentAS) {
        this._currentAS = _currentAS;
    }

    @Override
    public void getTranslationData(String virtualAddress) {
        this._currentAS = null;
        try {
            String contentBin = Conversor.hexStringToBinaryString(virtualAddress);
            int contentBinLength = contentBin.length();
            if (contentBinLength < 64) {
                contentBin = "0000000000000000000000000000000000000000000000000000000000000000" + contentBin;
                contentBinLength = contentBin.length();
                contentBin = contentBin.substring(contentBinLength - 64, contentBinLength);
            }

            this._currentAS = new EntityAddressSpaceWin64();
            this._currentAS.setPageDirectoryPointerIndexHex(Conversor.binaryStringToHex(contentBin.substring(EntityAddressSpaceWin64._PML4_SELECTOR_START_POS, EntityAddressSpaceWin64._PML4_SELECTOR_START_POS + EntityAddressSpaceWin64._PML4_SELECTOR_LENGTH), 4));
            this._currentAS.setPageDirectoryIndexHex(Conversor.binaryStringToHex(contentBin.substring(EntityAddressSpaceWin64._PDP_SELECTOR_START_POS, EntityAddressSpaceWin64._PDP_SELECTOR_START_POS + EntityAddressSpaceWin64._PDP_SELECTOR_LENGTH), 4));
            this._currentAS.setPageTableIndexHex(Conversor.binaryStringToHex(contentBin.substring(EntityAddressSpaceWin64._PD_SELECTOR_START_POS, EntityAddressSpaceWin64._PD_SELECTOR_START_POS + EntityAddressSpaceWin64._PD_SELECTOR_LENGTH), 4));
            this._currentAS.setPageTableEntryHex(Conversor.binaryStringToHex(contentBin.substring(EntityAddressSpaceWin64._PT_SELECTOR_START_POS, EntityAddressSpaceWin64._PT_SELECTOR_START_POS + EntityAddressSpaceWin64._PT_SELECTOR_LENGTH), 4));
            this._currentAS.setPageByteOffsetHex(Conversor.binaryStringToHex(contentBin.substring(EntityAddressSpaceWin64._OFFSET_START_POS, EntityAddressSpaceWin64._OFFSET_START_POS + EntityAddressSpaceWin64._OFFSET_LENGTH), 4));

            this._currentAS.setPageDirectoryPointerIndex(Conversor.hexToBigInteger(this._currentAS.getPageDirectoryPointerIndexHex()));
            this._currentAS.setPageDirectoryIndex(Conversor.hexToBigInteger(this._currentAS.getPageDirectoryIndexHex()));
            this._currentAS.setPageTableIndex(Conversor.hexToBigInteger(this._currentAS.getPageTableIndexHex()));
            this._currentAS.setPageTableEntry(Conversor.hexToBigInteger(this._currentAS.getPageTableEntryHex()));
            this._currentAS.setPageByteOffset(Conversor.hexToBigInteger(this._currentAS.getPageByteOffsetHex()));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * @return
     */
    public BigInteger getPML4PointerValue() {
        BigInteger PML4Pointer = BigInteger.ZERO;

        try {
            PML4Pointer = BigInteger.valueOf(this.getDTB()).add(this._currentAS.getPageDirectoryPointerIndex().multiply(BigInteger.valueOf(_PMLSELECTOR_ENTRY_LENGTH)));
            this._PML4 = Conversor.bigIntToHexString(PML4Pointer);
            long offset = this._dumpFormat.getFileOffset(PML4Pointer);

            String content = this._dumpFormat.getContentByOffset(offset, _PMLSELECTOR_ENTRY_BYTES);
            String hex = Conversor.toHexString(content);

            PML4Pointer = Conversor.hexToBigInteger(hex);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return PML4Pointer;
    }

    /**
     *
     * @param virtualAddress
     * @return Devuelve el puntero a la base de la Page Directory Table
     */
    public BigInteger getPDPPointerValue(String virtualAddress) {
        BigInteger PDPPointer = BigInteger.ZERO;

        try {
            /**
             * Descarto los últimos 12 bits (1000h)
             */
            BigInteger PML4Pointer = BinaryOperator.discardLast12Bits(Conversor.hexToBigInteger(virtualAddress));

            PDPPointer = PML4Pointer.add(this._currentAS.getPageDirectoryIndex().multiply(BigInteger.valueOf(_PDPT_ENTRY_LENGTH)));
            this._PDPE = Conversor.bigIntToHexString(PDPPointer);
            long offset = this._dumpFormat.getFileOffset(PDPPointer);

            String content = this._dumpFormat.getContentByOffset(offset, _PDPT_ENTRY_BYTES);
            String hex = Conversor.toHexString(content);

            PDPPointer = Conversor.hexToBigInteger(hex);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return PDPPointer;
    }

    /**
     *
     * @param virtualAddress
     * @return Devuelve el puntero a la base de la Page Table
     */
    public BigInteger getPDPointerValue(String virtualAddress) {
        BigInteger PDPointer = BigInteger.ZERO;

        try {
            /**
             * Descarto los últimos 12 bits (1000h)
             */
            BigInteger PDPPointer = BinaryOperator.discardLast12Bits(Conversor.hexToBigInteger(virtualAddress));

            PDPointer = PDPPointer.add(this._currentAS.getPageTableIndex().multiply(BigInteger.valueOf(_PDT_ENTRY_LENGTH)));
            this._PDE = Conversor.bigIntToHexString(PDPointer);

            long offset = this._dumpFormat.getFileOffset(PDPointer);

            String content = this._dumpFormat.getContentByOffset(offset, _PDT_ENTRY_BYTES);
            String hex = Conversor.toHexString(content);

            PDPointer = Conversor.hexToBigInteger(hex);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return PDPointer;
    }

    /**
     *
     * @param virtualAddress
     * @return Devuelve el puntero a la base del Frame en memoria física En el
     * caso de PAE ENABLED, no se utiliza como puntero
     */
    public BigInteger getPTPointerValue(String virtualAddress) {
        BigInteger PTPointer = BigInteger.ZERO;

        try {
            BigInteger PDPointer = BinaryOperator.discardLast12Bits(Conversor.hexToBigInteger(virtualAddress));
            PTPointer = PDPointer.add(this._currentAS.getPageTableEntry().multiply(BigInteger.valueOf(_PT_ENTRY_LENGTH)));
            this._PTE = Conversor.bigIntToHexString(PTPointer);

            long offset = this._dumpFormat.getFileOffset(PTPointer);

            String content = this._dumpFormat.getContentByOffset(offset, _PT_ENTRY_BYTES);
            String hex = Conversor.toHexString(content);

            PTPointer = Conversor.hexToBigInteger(hex);
            /**
             * Descarta los 16 bits más significativos
             */
            PTPointer = BinaryOperator.discardMostSignificant16Bits(PTPointer);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return PTPointer;
    }

    public BigInteger getPhysicalAddress(BigInteger virtualAddress) {
        BigInteger physicalAddress = BinaryOperator.discardLast12Bits(virtualAddress).add(this._currentAS.getPageByteOffset());
        BigInteger operand = BigInteger.ZERO;

        //physicalAddress = physicalAddress.add(this._currentAS.getPageByteOffset());
        //String p = Conversor.bigIntToHexString(this._currentAS.getPageByteOffset());
        return physicalAddress;
    }

    @Override
    public BigInteger calculatePhysicalAddress(String virtualAddress) {
        BigInteger address = BigInteger.ZERO;

        try {
            /**
             * Obtengo los datos necesarios para la traducción de la dirección
             * virtual
             */
            this.getTranslationData(virtualAddress);

            address = getPML4PointerValue();
            address = getPDPPointerValue(Conversor.bigIntToHexString(address));
            address = getPDPointerValue(Conversor.bigIntToHexString(address));

            /**
             * Obtiene algunos flags de los últimos 12 bits
             */
            this.getVirtualAddressData(address);

            if (this._currentAS.isLargePage()) {
                BigInteger addressLess12 = BinaryOperator.discardLast12Bits(address);
                address = addressLess12.add(this._currentAS.getPageTableEntry().multiply(BigInteger.valueOf(_PAGE_BYTE_ENTRY_LENGTH)));
                address = address.add(this._currentAS.getPageByteOffset());
            } else {
                address = getPTPointerValue(Conversor.bigIntToHexString(address));
                address = getPhysicalAddress(address);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return address;
    }

    @Override
    public long calculatePhysicalAddress(entities.translation.EntityAddressSpace aS) { // Debe calcular y setear en aS
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void obtainPhysicalAddressContent(entities.translation.EntityAddressSpace aS) { // Debe calcular y setear en aS
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void obtainAddressSpaceList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityAddressSpace obtainAddressSpace(String virtualAddress) {
        EntityAddressSpaceWin64 aS = null;

        try {
            aS = new EntityAddressSpaceWin64();
            aS.setVirtualAddress(Conversor.hexToBigInteger(virtualAddress));
            aS.setVirtualAddressHex(virtualAddress);
            BigInteger physicalAddress = this.calculatePhysicalAddress(virtualAddress);
            aS.setPhysicalAddress(physicalAddress);
            aS.setPhysicalAddressHex(Conversor.bigIntToHexString(physicalAddress));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return aS;
    }

    @Override
    public EntityAddressSpace obtainAddressSpace(BigInteger virtualAddress) {
        EntityAddressSpaceWin64 aS = null;

        try {
            aS = new EntityAddressSpaceWin64();
            aS.setVirtualAddress(virtualAddress);
            aS.setVirtualAddressHex(Conversor.bigIntToHexString(virtualAddress));
            BigInteger physicalAddress = this.calculatePhysicalAddress(virtualAddress);
            aS.setPhysicalAddress(physicalAddress);
            aS.setPhysicalAddressHex(Conversor.bigIntToHexString(physicalAddress));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return aS;
    }

    @Override
    public BigInteger calculatePhysicalAddress(BigInteger virtualAddress) {
        BigInteger address = this.calculatePhysicalAddress(Conversor.bigIntToHexString(virtualAddress));
        return address;
    }

    @Override
    public void getVirtualAddressData(BigInteger virtualAddress) {
        try {
            String virtualAddressHex = Conversor.bigIntToHexString(virtualAddress);

            String contentBin = Conversor.hexStringToBinaryString(virtualAddressHex);
            int contentBinLength = contentBin.length();
            if (contentBinLength < 32) {
                contentBin = "00000000000000000000000000000000" + contentBin;
                contentBinLength = contentBin.length();
                contentBin = contentBin.substring(contentBinLength - 32, contentBinLength);
            }

            int virtualAddressLength = contentBin.length();

            String virtualAddressLast12Bits = contentBin.substring(virtualAddressLength - 12, virtualAddressLength);
            virtualAddressLength = virtualAddressLast12Bits.length();

            String largePageStr = virtualAddressLast12Bits.substring(virtualAddressLength - 8, virtualAddressLength - 7);

            if (largePageStr.equals("1")) {
                this._currentAS.setLargePage(true);
            } else {
                this._currentAS.setLargePage(false);
            }

        } catch (Exception ex) {

        }
    }

    @Override
    public EntityAddressSpace processCommand(String modifier) {
        EntityAddressSpace as = null;

        try {
            try {
                BigInteger paTest = Conversor.hexToBigInteger(modifier);
                
                as = new EntityAddressSpaceWin64();
                
                as.setVirtualAddressHex(modifier);
                as.setPhysicalAddress(this.calculatePhysicalAddress(modifier));
                as.setPhysicalAddressHex(Conversor.bigIntToHexString(as.getPhysicalAddress()));
                long offset = this.getDumpFormat().getFileOffset(as.getPhysicalAddress());
                as.setOffsetInFile(offset);
                as.setOffsetInFileHex(Conversor.longToHexString(offset));

                /**
                 * Notifica a observadores
                 */
                this.notifyObservers(as);
            } catch (Exception ex) {
                this.notifyObservers("Ingrese una dirección válida");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return as;
    }

}

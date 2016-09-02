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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import system.utils.CharOperator;
import system.utils.Conversor;
import system.utils.DataPrimitiveType;
import system.utils.RandomFileSeeker;

/**
 *
 * @author Gonzalo
 */
public class HeaderCrashDump32 extends HeaderCrashDump {

    public static int _HEADER_SIZE = 4096; //[BYTES]

    /**
     *
     * @param _path
     * @param _randomFileSeeker
     * @param _littleEndian
     */
    public HeaderCrashDump32(String _path, RandomFileSeeker _randomFileSeeker, boolean _littleEndian) {
        super(_path, _randomFileSeeker, _littleEndian);

        try {
            this.setItems(new HashMap<String, HeaderCrashDumpItem>());

            HeaderCrashDumpItem h0 = new HeaderCrashDumpItem("SIGNATURE", 0x0, "char", 4);
            this.getItems().put("SIGNATURE", h0);
            HeaderCrashDumpItem h1 = new HeaderCrashDumpItem("VALID_DUMP", 0x4, "char", 4);
            this.getItems().put("VALID_DUMP", h1);
            HeaderCrashDumpItem h2 = new HeaderCrashDumpItem("MAJOR_VERSION", 0x8, "int 32");
            this.getItems().put("MAJOR_VERSION", h2);
            HeaderCrashDumpItem h3 = new HeaderCrashDumpItem("MINOR_VERSION", 0xC, "int 32");
            this.getItems().put("MINOR_VERSION", h3);
            HeaderCrashDumpItem h4 = new HeaderCrashDumpItem("DIRECTORY_TABLE_BASE", 0x10, "int 32");
            this.getItems().put("DIRECTORY_TABLE_BASE", h4);
            HeaderCrashDumpItem h5 = new HeaderCrashDumpItem("PFN_DATA_BASE", 0x14, "int 32");
            this.getItems().put("PFN_DATA_BASE", h5);
            HeaderCrashDumpItem h6 = new HeaderCrashDumpItem("PS_LOADED_MODULE_LIST", 0x18, "int 32");
            this.getItems().put("PS_LOADED_MODULE_LIST", h6);
            HeaderCrashDumpItem h7 = new HeaderCrashDumpItem("PS_ACTIVE_PROCESS_HEAD", 0x1C, "int 32");
            this.getItems().put("PS_ACTIVE_PROCESS_HEAD", h7);
            HeaderCrashDumpItem h8 = new HeaderCrashDumpItem("MACHINE_IMAGE_TYPE", 0x20, "int 32");
            this.getItems().put("MACHINE_IMAGE_TYPE", h8);
            HeaderCrashDumpItem h9 = new HeaderCrashDumpItem("NUMBER_PROCESSORS", 0x24, "int 32");
            this.getItems().put("NUMBER_PROCESSORS", h9);
            HeaderCrashDumpItem h10 = new HeaderCrashDumpItem("BUG_CHECK_CODE", 0x28, "int 32");
            this.getItems().put("BUG_CHECK_CODE", h10);
            HeaderCrashDumpItem h11 = new HeaderCrashDumpItem("BUG_CHECK_CODE_PARAMETER", 0x2C, "int 32", 4);
            this.getItems().put("BUG_CHECK_CODE_PARAMETER", h11);
            HeaderCrashDumpItem h12 = new HeaderCrashDumpItem("VERSION_USER", 0x3C, "char", 32);
            this.getItems().put("VERSION_USER", h12);
            HeaderCrashDumpItem h13 = new HeaderCrashDumpItem("PAE_ENABLED", 0x5C, "char");
            this.getItems().put("PAE_ENABLED", h13);
            HeaderCrashDumpItem h14 = new HeaderCrashDumpItem("KD_SECONDARY_VERSION", 0x5D, "char");
            this.getItems().put("KD_SECONDARY_VERSION", h14);
            HeaderCrashDumpItem h15 = new HeaderCrashDumpItem("VERSION_USER2", 0x5E, "char", 2);
            this.getItems().put("VERSION_USER2", h15);
            HeaderCrashDumpItem h16 = new HeaderCrashDumpItem("KD_DEBUGGER_DATA_BLOCK", 0x60, "int 32");
            this.getItems().put("KD_DEBUGGER_DATA_BLOCK", h16);
            HeaderCrashDumpItem h17 = new HeaderCrashDumpItem("PHYSICAL_MEMORY_BLOCK_BUFFER", 0x64, "PhysicalMemoryDescriptor32");
            this.getItems().put("PHYSICAL_MEMORY_BLOCK_BUFFER", h17);
            HeaderCrashDumpItem h18 = new HeaderCrashDumpItem("CONTEXT_RECORD", 0x320, "char", 1200);
            this.getItems().put("CONTEXT_RECORD", h18);
            HeaderCrashDumpItem h19 = new HeaderCrashDumpItem("EXCEPTION_RECORD32", 0x7D0, "ExceptionRecord32");
            this.getItems().put("EXCEPTION_RECORD32", h19);
            HeaderCrashDumpItem h20 = new HeaderCrashDumpItem("COMMENT", 0x820, "char", 128);
            this.getItems().put("COMMENT", h20);
            HeaderCrashDumpItem h21 = new HeaderCrashDumpItem("DUMP_TYPE", 0xf88, "int 32");
            this.getItems().put("DUMP_TYPE", h21);
            HeaderCrashDumpItem h22 = new HeaderCrashDumpItem("MINI_DUMP_FIELDS", 0xF8C, "int 32");
            this.getItems().put("MINI_DUMP_FIELDS", h22);
            HeaderCrashDumpItem h23 = new HeaderCrashDumpItem("SECONDARY_DATA_STATE", 0xF90, "int 32");
            this.getItems().put("SECONDARY_DATA_STATE", h23);
            HeaderCrashDumpItem h24 = new HeaderCrashDumpItem("PRODUCT_TYPE", 0xF94, "int 32");
            this.getItems().put("PRODUCT_TYPE", h24);
            HeaderCrashDumpItem h25 = new HeaderCrashDumpItem("SUITE_MASK", 0xF98, "int 32");
            this.getItems().put("SUITE_MASK", h25);
            HeaderCrashDumpItem h26 = new HeaderCrashDumpItem("WRITER_STATUS", 0xF9C, "int 32");
            this.getItems().put("WRITER_STATUS", h26);
            HeaderCrashDumpItem h27 = new HeaderCrashDumpItem("REQUIRED_DUMPSPACE", 0xFA0, "int 64");
            this.getItems().put("REQUIRED_DUMPSPACE", h27);
            HeaderCrashDumpItem h28 = new HeaderCrashDumpItem("SYSTEM_UP_TIME", 0xFB8, "int 64");
            this.getItems().put("SYSTEM_UP_TIME", h28);
            HeaderCrashDumpItem h29 = new HeaderCrashDumpItem("SYSTEM_TIME", 0xFC0, "int 64");
            this.getItems().put("SYSTEM_TIME", h29);
            HeaderCrashDumpItem h30 = new HeaderCrashDumpItem("RESERVED3", 0xFC8, "char", 56);
            this.getItems().put("RESERVED3", h30);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void setLittleEndianProcess(boolean _littleEndian) {
        this.setLittleEndian(_littleEndian);

        /**
         * setea todos sus items
         */
        Iterator<Entry<String, HeaderCrashDumpItem>> i = this.getItems().entrySet().iterator();
        while (i.hasNext()) {
            Entry<String, HeaderCrashDumpItem> e = (Entry<String, HeaderCrashDumpItem>) i.next();
            e.getValue().setLittleEndian(_littleEndian);
        }
    }

    /*
     * @Descripción: obtiene el contenido por cada elemento del hashMap _items
     * Obtiene el contenido de los objeto de Clase PhysicalMemoryDescriptor32 y
     * ExceptionRecord32
     */
    @Override
    public void executeObtainingItemsContentProcess() {
        try {
            /**
             * Inicializo _randomFileSeeker
             */
            this.getRandomFileSeeker().obtainRandomAccessFile(this.getPath());

            /**
             * Por cada item, le solicta a randomFileSeeker que obtenga el
             * contenido
             */
            Iterator<Entry<String, HeaderCrashDumpItem>> i = this.getItems().entrySet().iterator();
            while (i.hasNext()) {
                Entry<String, HeaderCrashDumpItem> e = (Entry<String, HeaderCrashDumpItem>) i.next();
                if (!e.getValue().getContentType().equals("PhysicalMemoryDescriptor32") && !e.getValue().getContentType().equals("ExceptionRecord32")) {
                    int length = DataPrimitiveType.getInstance().getTypes().get(e.getValue().getContentType()).intValue() * e.getValue().getLength();
                    //String content = this._dumpFormat.getItemContent(e.getValue().getContentType(), absoluteOffset, e.getValue().getLength());

                    
                    String content = this.getRandomFileSeeker().getContent(e.getValue().getPosition(), length);
                    if (this.isLittleEndian() && !e.getValue().getContentType().equals("char")) {
                        content = CharOperator.getLittleEndianCharArray(content).toString();
                    }

                    /**
                     * Si corresponde, expreso en hexadecimal
                     */
                    if (e.getValue().getContentType().equals("int 32") || e.getValue().getContentType().equals("int 64")) {
                        int lengthDataType = DataPrimitiveType.getInstance().getTypes().get(e.getValue().getContentType());
                        int cant = e.getValue().getLength() * lengthDataType; 
                        StringBuilder sb = new StringBuilder("");
                        
                        switch (lengthDataType) {
                            case 4:
                                sb.append("00000000");
                                break;
                            case 8:
                                sb.append("0000000000000000");
                                break;
                            default:
                                sb.append("00000000");
                                break;
                        }
                        
                        content = Conversor.stringToBinaryString(content);
                        
                        String subStr = new String();
                        int lengthData = 8;
                        for (int j = 0; j < cant; j++) {
                            if (e.getValue().getContentType().equals("int 32")) {
                                subStr = content.substring(j * lengthData, (j + 1) * lengthData);
                                //subStr = String.format("%4X", Long.parseLong(subStr, 2));
                                subStr = Conversor.binaryStringToHex(subStr, 4);
                            }
                            if (e.getValue().getContentType().equals("int 64")) {
                                subStr = content.substring(j * lengthData, (j + 1) * lengthData);
                                //subStr = String.format("%8X", Long.parseLong(subStr, 2));
                                subStr = Conversor.binaryStringToHex(subStr, 8);
                            }
                            subStr = subStr.trim();
                            sb.append(subStr);
                        }
                        
                        content = sb.toString().substring(sb.toString().length() - 8, sb.toString().length());
                    }

                    e.getValue().setContent(content);
                }/* else {
                    System.out.println("El item es de un tipo complejo, se obtendrá más tarde...");
                }*/
            }
            PhysicalMemoryDescriptor32 physicalMemoryDescriptor32 = new PhysicalMemoryDescriptor32(this.isLittleEndian());
            physicalMemoryDescriptor32 = obtainPhysicalMemoryDescriptor32Content();
            this.getItems().get("PHYSICAL_MEMORY_BLOCK_BUFFER").setContent(physicalMemoryDescriptor32);
            ExceptionRecord32 exceptionRecord32 = obtainExceptionRecord32Content();
            this.getItems().get("EXCEPTION_RECORD32").setContent(exceptionRecord32);
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    public PhysicalMemoryDescriptor32 obtainPhysicalMemoryDescriptor32Content() {

        PhysicalMemoryDescriptor32 physicalMemoryDescriptor32 = new PhysicalMemoryDescriptor32(this.isLittleEndian());

        try {
            physicalMemoryDescriptor32.setRandomFileSeeker(this.getRandomFileSeeker());
            physicalMemoryDescriptor32.obtainNumberOfRuns();
            physicalMemoryDescriptor32.obtainNumberOfPages();
            physicalMemoryDescriptor32.obtainRuns();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println(ex.getStackTrace().toString());
        }
        return physicalMemoryDescriptor32;
    }

    public ExceptionRecord32 obtainExceptionRecord32Content() {
        ExceptionRecord32 exceptionRecord32 = null;

        try {
            exceptionRecord32 = new ExceptionRecord32(this.isLittleEndian(), this.getRandomFileSeeker());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return exceptionRecord32;
    }

}

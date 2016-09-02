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

/**
 *
 * @author Gonzalo
 */
public class DataManager {

    private String _path;
    private RandomFileSeeker _randomFileSeeker;
    private boolean _littleEndian;
    private static DataManager _instance;

    private DataManager(String _path, RandomFileSeeker _randomFileSeeker, boolean _littleEndian) {
        this._path = _path;
        this._randomFileSeeker = _randomFileSeeker;
        this._littleEndian = _littleEndian;
    }

    public static DataManager getInstance() {
        return _instance;
    }

    public static DataManager getInstance(String _path, RandomFileSeeker _randomFileSeeker, boolean _littleEndian) {
        if (_instance == null) {
            _instance = new DataManager(_path, _randomFileSeeker, _littleEndian);
        }

        return _instance;
    }

    public String getItemContent(String contentType, long position, int lengthInput, boolean union) {
        String content = null;

        try {
            if (DataPrimitiveType.getInstance().isPrimitiveType(contentType) && position >= 0) {
                contentType = DataPrimitiveType.getInstance().getPrimitiveType(contentType);
                int length = DataPrimitiveType.getInstance().getTypes().get(contentType).intValue() * lengthInput;
                content = this._randomFileSeeker.getContent(position, length);
                if (this._littleEndian && !contentType.equals("char")) {
                    content = CharOperator.getLittleEndianCharArray(content).toString();
                }

                /**
                 * Si corresponde, expreso en hexadecimal
                 */
                if (DataPrimitiveType.getInstance().isNumericPrimitiveType(contentType)) {
                    //if (contentType.equals("int 32") || contentType.equals("int 64")) {
                    int lengthDataType = DataPrimitiveType.getInstance().getTypes().get(contentType);
                    int cant = lengthInput * lengthDataType;
                    StringBuilder sb = new StringBuilder("");
                    int minus = 8;
                    switch (lengthDataType) {
                        case 1:
                            sb.append("0000");
                            minus = 2;
                            break;
                        case 2:
                            sb.append("00000000");
                            minus = 4;
                            break;
                        case 4:
                            sb.append("0000000000000000");
                            minus = 8;
                            break;
                        case 8:
                            sb.append("00000000000000000000000000000000");
                            minus = 16;
                            break;
                        default:
                            sb.append("0000000000000000");
                            minus = 8;
                            break;
                    }

                    content = Conversor.stringToBinaryString(content);

                    String subStr = new String();
                    int lengthData = 8;
                    int contentLength = content.length();
                    if (contentLength < 8) {
                        content = "00000000" + content;
                        contentLength = content.length();
                        content = content.substring(contentLength - 8, contentLength);
                    }
                    for (int j = 0; j < cant; j++) {
                        if (contentType.equals("int 8")) {
                            subStr = content.substring(j * lengthData, (j + 1) * lengthData);
                            //subStr = String.format("%4X", Long.parseLong(subStr, 2));
                            subStr = Conversor.binaryStringToHex(subStr, 4);
                        }
                        if (contentType.equals("int 16")) {
                            subStr = content.substring(j * lengthData, (j + 1) * lengthData);
                            //subStr = String.format("%4X", Long.parseLong(subStr, 2));
                            subStr = Conversor.binaryStringToHex(subStr, 4);
                        }
                        if (contentType.equals("int 32") || contentType.equals("pointer32")) {
                            subStr = content.substring(j * lengthData, (j + 1) * lengthData);
                            //subStr = String.format("%4X", Long.parseLong(subStr, 2));
                            subStr = Conversor.binaryStringToHex(subStr, 4);
                        }
                        if (contentType.equals("int 64") || contentType.equals("pointer64")) {
                            subStr = content.substring(j * lengthData, (j + 1) * lengthData);
                            //subStr = String.format("%8X", Long.parseLong(subStr, 2));
                            subStr = Conversor.binaryStringToHex(subStr, 8);
                        }
                        subStr = subStr.trim();
                        sb.append(subStr);
                    }

                    content = sb.toString().substring(sb.toString().length() - minus * lengthInput, sb.toString().length());
                    if (union) {
                        int contL = content.length();
                        String highPart = content.substring(0, minus / 2);
                        String lowPart = content.substring(minus / 2, contL);
                        content = highPart + lowPart;
                    }
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return content;
    }

    public String getItemContent(String contentType, long position, int lengthInput, boolean union, boolean bigEndian) {
        String content = null;

        try {
            if (DataPrimitiveType.getInstance().isPrimitiveType(contentType) && position >= 0) {
                contentType = DataPrimitiveType.getInstance().getPrimitiveType(contentType);
                int length = DataPrimitiveType.getInstance().getTypes().get(contentType).intValue() * lengthInput;
                content = this._randomFileSeeker.getContent(position, length);
                if (this._littleEndian && !bigEndian && !contentType.equals("char")) {
                    content = CharOperator.getLittleEndianCharArray(content).toString();
                }

                /**
                 * Si corresponde, expreso en hexadecimal
                 */
                if (DataPrimitiveType.getInstance().isNumericPrimitiveType(contentType)) {
                    int lengthDataType = DataPrimitiveType.getInstance().getTypes().get(contentType);
                    int cant = lengthInput * lengthDataType;
                    StringBuilder sb = new StringBuilder("");
                    int minus = 8;
                    switch (lengthDataType) {
                        case 1:
                            sb.append("0000");
                            minus = 2;
                            break;
                        case 2:
                            sb.append("00000000");
                            minus = 4;
                            break;
                        case 4:
                            sb.append("0000000000000000");
                            minus = 8;
                            break;
                        case 8:
                            sb.append("00000000000000000000000000000000");
                            minus = 16;
                            break;
                        default:
                            sb.append("0000000000000000");
                            minus = 8;
                            break;
                    }

                    content = Conversor.stringToBinaryString(content);

                    String subStr = new String();
                    int lengthData = 8;
                    for (int j = 0; j < cant; j++) {
                        if (contentType.equals("int 8")) {
                            subStr = content.substring(j * lengthData, (j + 1) * lengthData);
                            subStr = Conversor.binaryStringToHex(subStr, 4);
                        }
                        if (contentType.equals("int 16")) {
                            subStr = content.substring(j * lengthData, (j + 1) * lengthData);
                            subStr = Conversor.binaryStringToHex(subStr, 4);
                        }
                        if (contentType.equals("int 32") || contentType.equals("pointer32")) {
                            subStr = content.substring(j * lengthData, (j + 1) * lengthData);
                            subStr = Conversor.binaryStringToHex(subStr, 4);
                        }
                        if (contentType.equals("int 64") || contentType.equals("pointer64")) {
                            subStr = content.substring(j * lengthData, (j + 1) * lengthData);
                            subStr = Conversor.binaryStringToHex(subStr, 8);
                        }
                        subStr = subStr.trim();
                        sb.append(subStr);
                    }

                    content = sb.toString().substring(sb.toString().length() - minus * lengthInput, sb.toString().length());
                    if (union && bigEndian) {
                        int contL = content.length();
                        String highPart = content.substring(0, minus / 2);
                        String lowPart = content.substring(minus / 2, contL);
                        content = lowPart + highPart;
                    }
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return content;
    }

    public String renderDataContent(String contentType, String content) {
        String rendered = null;

        try {
            if (DataPrimitiveType.getInstance().isNumericPrimitiveType(contentType)) {
                contentType = "int";
                rendered = String.valueOf(Conversor.hexToInt(content));
            } else {
                rendered = content;
            }

        } catch (Exception ex) {

        }
        return rendered;
    }

    /**
     * TODO: implementar getUnicodeString, debe tener en cuenta toda la
     * estructura
     *
     * @param complexContentType
     * @param contentType
     * @param position
     * @param lengthInput
     * @param union
     * @param bigEndian
     * @return
     */
    public String getComplexItemContent(String complexContentType, String contentType, long position, int lengthInput, boolean union, boolean bigEndian) {
        String respuesta = null;
        try {
            switch (complexContentType) {
                case "windows file time":
                    respuesta = getItemContent(contentType, position, lengthInput, union, bigEndian);
                    respuesta = getWindowsFileTime(respuesta);
                    break;
                case "ip":
                    respuesta = getItemContent(contentType, position, lengthInput, union, bigEndian);
                    respuesta = getIPAddress(respuesta);
                    break;
                default:
                    respuesta = getItemContent(contentType, position, lengthInput, union, bigEndian);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return respuesta;
    }

    /**
     * Obtiene fecha y hora con formato
     *
     * @param content
     * @return
     */
    public String getWindowsFileTime(String content) {
        String windowsFileTime = null;

        String formatStr = "yyyy-MM-dd HH:mm:ssZ";
        windowsFileTime = DateManager.hexToDateTimeStringFormat(content, formatStr);

        return windowsFileTime;
    }

    /**
     * Obtiene IPAddress con formato
     *
     * @param content
     * @return
     */
    public String getIPAddress(String content) {
        StringBuilder ipAddress = null;

        try {
            if (content != null) {
                ipAddress = new StringBuilder();
                int length = content.length();

                String item = null;

                for (int i = 0; i < length; i = i + 2) {
                    if (i > 0) {
                        ipAddress.append(".");
                    }

                    item = content.substring(i, i + 2);
                    item = String.valueOf(Conversor.hexToInt(item));
                    ipAddress.append(item);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String ip = null;
        if (ipAddress != null) {
            ip = ipAddress.toString();
        }

        return ip;
    }

    public boolean isNumeric(String s) {
        try {
            double d = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}

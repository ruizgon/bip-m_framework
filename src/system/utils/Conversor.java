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

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Gonzalo
 */
public class Conversor {

    public static int hexToInt(String hex) {
        int hexInt = 0;

        try {
            if (!hex.equals("")) {
                long limit = Long.parseLong("B70F0000", 16);
                long hexValue = Long.parseLong(hex, 16);
                if (hexValue < limit) {
                    hexInt = Integer.parseInt(hex, 16);
                }
            }
        } catch (Exception ex) {
            
        }

        return hexInt;
    }

    public static long hexToLong(String hex) {
        if (hex != null) {
            if (hex.toUpperCase().equals("FFFFFFFFFFFFFFFF") || hex.equals("")) {
                hex = null;
            }
        }

        return hex != null ? Long.parseLong(hex, 16) : 0;
    }

    public static BigInteger hexToBigInteger(String hex) {
        BigInteger b = null;

        try {
            b = new BigInteger(hex, 16);
        } catch (Exception ex) {
            // ex.printStackTrace();
            b = BigInteger.ZERO;
        }

        return b;
    }

    public static StringBuilder convertHexToString(String hex) {

        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        //49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for (int i = 0; i < hex.length() - 1; i += 2) {

            //grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            //convert hex to decimal
            int decimal = Integer.parseInt(output, 8);
            //convert the decimal to character
            sb.append((char) decimal);

            temp.append(decimal);
        }
        //System.out.println("Decimal : " + temp.toString());

        return sb;
    }

    public static StringBuilder hexToASCII(String hex) {
        StringBuilder output = new StringBuilder();
        int longitudCadena = hex.length();
        int posSumar = 0;
        if (longitudCadena < 2) {
            posSumar = 1;
        } else {
            posSumar = 2;
        }
        for (int i = 0; i < hex.length(); i += 2) {
            if (i + posSumar > hex.length()) {
                posSumar--;
            }
            String str = hex.substring(i, i + posSumar);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output;
    }

    public static String stringToBinaryString(String s) {
        StringBuilder content = new StringBuilder("");

        for (char c : s.toCharArray()) {
            content.append(charToBinaryString(c));
        }

        return content.toString();
    }

    public static String hexStringToBinaryString(String s) {
        StringBuilder content = new StringBuilder("");

        if (s != null) {
            for (char c : s.toCharArray()) {
                content.append(hexCharToBinaryString(c));
            }
        }

        return content.toString();
    }

    public static String charToBinaryString(char c) {
        StringBuilder sb = new StringBuilder("00000000");

        sb.append(Integer.toBinaryString(c));

        String binaryString = sb.toString().substring(sb.length() - 8, sb.length());

        return binaryString;
    }

    public static String hexCharToBinaryString(char c) {
        StringBuilder sb = new StringBuilder("0000");

        int entero = Integer.parseInt(String.valueOf(c), 16);

        String binary = Integer.toBinaryString(entero);
        sb.append(binary);

        String binaryString = sb.toString().substring(sb.length() - 4, sb.length());

        return binaryString;
    }

    public static String toHexString(String content) {
        StringBuilder hexContent = new StringBuilder("");

        if (content != null) {
            for (char c : content.toCharArray()) {
                StringBuilder hexString = new StringBuilder("");
                if (c <= 15) {
                    hexString.append("0");
                    hexString.append(Integer.toHexString(c));
                } else {
                    hexString.append(Integer.toHexString(c));
                }
                hexContent.append(hexString);
            }
        }

        return hexContent.toString();
    }

    public static String binaryStringToHex(String s, int cantChar) {
        String hex = String.format("%" + String.valueOf(cantChar) + "X", Long.parseLong(s, 2)).trim();
        if (Conversor.hexToLong(hex) <= 15) {
            StringBuilder sb = new StringBuilder("0");
            sb.append(hex);
            hex = sb.toString();
        }

        return hex;
    }

    public static int stringBinaryToInt(String binary) {
        return Integer.parseInt(binary, 2);
    }

    public static String intToHexString(int i) {
        return Integer.toHexString(i);
    }

    public static String longToHexString(long l) {
        return Long.toHexString(l);
    }

    public static String bigIntToHexString(BigInteger b) {
        return b.toString(16);
    }

    public static String byteArrayToHexString(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            stringBuffer.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }

    static final Matcher m = Pattern.compile(
            "-?([89A-Fa-f])(\\p{XDigit}+)").matcher("");

    static int parseHexInteger(String str, int bitCount) {
        boolean isNeg = str.startsWith("-");
        int len = (isNeg ? bitCount + 4 : bitCount) / 4;
        if (m.reset(str).matches() && str.length() == len) {
            int n = (Integer.parseInt(m.group(1), 16) << (bitCount - 4))
                    | Integer.parseInt(m.group(2), 16);
            return isNeg ? -n : n;
        } else {
            return Integer.parseInt(str, 16);
        }
    }

    public static long parseLong(String str) {
        boolean isNeg = str.startsWith("-");
        int len = isNeg ? 17 : 16;
        int mid = len - 8;
        long n = ((long) parseHexInteger(str.substring(0, mid), 32) << 32)
                | (long) parseHexInteger(str.substring(mid), 32);
        return n;
    }
}

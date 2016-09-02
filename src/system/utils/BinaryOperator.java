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

/**
 *
 * @author Gonzalo
 */
public class BinaryOperator {

    public static int numberOfChar(int bits) {
        int number = 0;

        int resto = bits % 8;
        int cociente = new Double(bits / 8).intValue();

        if (resto > 0) {
            cociente++;
        }

        number = cociente;

        return number;
    }

    public static String cutBinaryStringLeft(String content, int bits) {
        int length = content.length();

        String contentCut = content.substring(bits, length);

        return contentCut;
    }

    public static String cutBinaryStringRight(String content, int bits) {
        int length = content.length() - bits;

        String contentCut = content.substring(0, length);

        return contentCut;
    }

    public static long discardLast12Bits(long value) {
        String hex = Long.toHexString(value);

        long valueOffset = 0;

        if (hex.length() >= 3) {
            hex = hex.substring(0, hex.length() - 3) + "000";

            valueOffset = Conversor.hexToLong(hex);
        }

        return valueOffset;
    }

    public static BigInteger discardLast12Bits(BigInteger value) {
        String hex = value.toString(16);

        BigInteger valueOffset = BigInteger.ZERO;

        if (hex.length() >= 3) {
            hex = hex.substring(0, hex.length() - 3) + "000";

            valueOffset = Conversor.hexToBigInteger(hex);
        }

        return valueOffset;
    }
    
    public static BigInteger discardMostSignificant16Bits(BigInteger value){
        String hex = value.toString(16);

        BigInteger valueOffset = BigInteger.ZERO;

        if (hex.length() >= 12) {
            hex = "0000" + hex.substring(4,hex.length());

            valueOffset = Conversor.hexToBigInteger(hex);
        }else{
            valueOffset = value;
        }

        return valueOffset;
    }

    public static BigInteger discardLast20Bits(BigInteger value) {
        String hex = value.toString(16);

        BigInteger valueOffset = BigInteger.ZERO;

        if (hex.length() >= 5) {
            hex = hex.substring(0, hex.length() - 5) + "00000";

            valueOffset = Conversor.hexToBigInteger(hex);
        }

        return valueOffset;
    }

    /**
     * The bitwise XOR may be used to invert selected bits in a register (also
     * called toggle or flip). Any bit may be toggled by XORing it with 1. For
     * example, given the bit pattern 0010 (decimal 2) the second and fourth
     * bits may be toggled by a bitwise XOR with a bit pattern containing 1 in
     * the second and fourth positions:
     *
     * 0010 (decimal 2) XOR 1010 (decimal 10) = 1000 (decimal 8)
     */
}

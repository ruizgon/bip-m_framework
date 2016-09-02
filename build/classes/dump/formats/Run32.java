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
import system.utils.BinaryOperator;
import system.utils.CharOperator;
import system.utils.Conversor;

/**
 *
 * @author Gonzalo
 */
public class Run32 extends Run {

    public static int _RUN_SIZE = 64;
    public static int _START_ADDRESS_SIZE = 32;
    public static int _LENGTH_SIZE = 32;

    public Run32(long _startOffset, boolean _littleEndian) {
        super(_startOffset, _littleEndian);
    }

    @Override
    public void obtainRunData() {
        try {
            /**
             * Obtengo startAddress
             */
            int length = BinaryOperator.numberOfChar(_START_ADDRESS_SIZE) + 1;
            String content = this.getRandomFileSeeker().getContent(this.getStartOffset(), length);
            if (this.isLittleEndian()) {
                content = CharOperator.getLittleEndianCharArray(content).toString();
            }
            String contentBin = Conversor.stringToBinaryString(content);

            int contentLength = contentBin.length();
            int left = (contentLength - _START_ADDRESS_SIZE) / 2;

            contentBin = BinaryOperator.cutBinaryStringLeft(contentBin, left);
            content = BinaryOperator.cutBinaryStringRight(contentBin, left);

            String hex = "00000000" + String.format("%" + String.valueOf(_START_ADDRESS_SIZE / 8) + "X", Long.parseLong(content, 2));

            this.setStartAddress(new BigInteger(hex, 16));
            this.setStartAddressHex(hex.substring(hex.length() - 8, hex.length()));

            /**
             * Obtengo length
             */
            length = BinaryOperator.numberOfChar(_LENGTH_SIZE) + 1;
            long position = this.getStartOffset() + new Double(_START_ADDRESS_SIZE / 8).intValue();
            content = this.getRandomFileSeeker().getContent(position, length);
            if (this.isLittleEndian()) {
                content = CharOperator.getLittleEndianCharArray(content).toString();
            }
            contentBin = Conversor.stringToBinaryString(content);

            contentLength = contentBin.length();
            left = (contentLength - _START_ADDRESS_SIZE) / 2;

            contentBin = BinaryOperator.cutBinaryStringLeft(contentBin, left);
            content = BinaryOperator.cutBinaryStringRight(contentBin, left);

            hex = "00000000" + String.format("%" + String.valueOf(_START_ADDRESS_SIZE / 8) + "X", Long.parseLong(content, 2));

            this.setLength(new BigInteger(hex, 16));
            this.setLengthHex(hex.substring(hex.length() - 8, hex.length()));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

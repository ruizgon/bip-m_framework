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
public class CharOperator {

    /**
     * 
     * @param content Cadena con el contenido a invertir
     * @return devuelve un StringBuilder con el contenido invertido
     */
    public static StringBuilder getLittleEndianCharArray(String content) {
        StringBuilder sb = new StringBuilder();

        char[] contentArray = content.toCharArray();
        int cALength = contentArray.length;

        for (int i = 0, j = cALength - 1; i < j; i++, j--) {
            char charWise = contentArray[i];
            contentArray[i] = contentArray[j];
            contentArray[j] = charWise;
        }

        for (char c : contentArray) {
            sb.append(c);
        }

        return sb;
    }
}

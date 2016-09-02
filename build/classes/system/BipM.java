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
package system;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Gonzalo
 */
public class BipM {
    

    /**
     * @param args the command line arguments
     */
    public static void main2(String[] args) throws FileNotFoundException, IOException {
        
        
        
        
        //Session sessionManager = new Session();
        
        
        
        //Esto es parte de la etapa de pruebas
        File archivo = new File("c:\\DUMPS\\MEMORY.DMP");
        File salidaArchivo = new File("c:\\DUMPS\\salida.txt");

        FileReader fr = new FileReader(archivo);
        BufferedReader br = new BufferedReader(fr);
        
        //Probar DataInputStream
        DataInputStream ds = new DataInputStream((new BufferedInputStream(new FileInputStream(archivo))));
        //LittleEndianDataInputStream le = new LittleEndianDataInputStream(ds);
        
        /*while (ds.available() != 0)  
        {  
            System.out.print((char) ds.readByte());  
        }*/

        // Se abre el fichero donde se hará la copia
        FileWriter fileOutput = new FileWriter(salidaArchivo);
        BufferedWriter bufferedOutput = new BufferedWriter(fileOutput);

        boolean retorno = false;
        StringBuilder caracterString = new StringBuilder("");

        String linea = null;
        //linea = br.readLine();

        StringBuilder salida = new StringBuilder("");
        StringBuilder direccionSalida = new StringBuilder("");
        StringBuilder direccionEscribe = new StringBuilder("0x");

        StringBuilder rellenoHexa = new StringBuilder("000000");
        StringBuilder hexaEscribe = new StringBuilder("");
        StringBuilder caracterEscribe = new StringBuilder("");
        StringBuilder caracterEscribeBruto = new StringBuilder("");

        int posCaracter = 0;
        int posCaracterBruto = 0;

        int direccionNro = 0;

        /*
         *TODO
         *Crear un haschMap con los valores en Unicode para poder manejar casos de char mayor a 164
         */
        /*int iaux = 165;
         char aux;
         if (iaux == 165) {
         aux = '\u006E';
         } else {
         aux = (char) iaux;
         }
         System.out.println(Character.toString(aux));*/
        String hexaDef = new String("");

        /*
         * CADA 16 CARACTERES ESCRIBIR UN SALTO DE LINEA
         */
        //hexaEscribe = new StringBuilder(" ");
        //caracterEscribe = new StringBuilder("");
        caracterEscribeBruto = new StringBuilder("");
        //char ch = (char)ds.read();
        //char ch = (char) le.readByte();
        char ch = (char)-1;
        while (ch != -1) {
            //ch = (char) ds.readByte();
            posCaracter++;
            rellenoHexa.append("000000");
            rellenoHexa.append(Integer.toHexString(ch).toString().toUpperCase());
            hexaDef = rellenoHexa.substring(rellenoHexa.length() - 2, rellenoHexa.length());
            if (hexaDef.toString().compareToIgnoreCase("FF") > 0) {//es mayor
                hexaEscribe.append(rellenoHexa.substring(rellenoHexa.length() - 4, rellenoHexa.length() - 2));
                hexaEscribe.append(" ");
                if (posCaracter % 16 == 0) {
                    direccionSalida.append("000000000");
                    direccionSalida.append(Integer.toHexString(direccionNro));

                    direccionEscribe.append(direccionSalida.substring(direccionSalida.length() - 8, direccionSalida.length()));
                    direccionEscribe.append("  ");

                    bufferedOutput.write(direccionEscribe.toString());
                    bufferedOutput.write(hexaEscribe.toString());
                    bufferedOutput.write(caracterEscribe.toString());
                    bufferedOutput.write("\r\n");//salto de linea

                    direccionNro = direccionNro + 16;

                    hexaEscribe = new StringBuilder("");
                    caracterEscribe = new StringBuilder("");

                    direccionSalida = new StringBuilder("  ");
                    direccionEscribe = new StringBuilder("0x");
                }
                hexaEscribe.append(rellenoHexa.substring(rellenoHexa.length() - 2, rellenoHexa.length()));
                hexaEscribe.append(" ");
                //validar si ya llegué al final de la línea
                posCaracter++;

            } else {
                hexaEscribe.append(rellenoHexa.substring(rellenoHexa.length() - 2, rellenoHexa.length()));
                hexaEscribe.append(" ");
            }

            String valor = Integer.toHexString(ch);
            if (valor.equals("f") || valor.equals("0")) {
                caracterEscribe.append(".");
            } else {
                caracterEscribe.append(hexToASCII(Integer.toHexString(ch).toString().toUpperCase()).toString());
            }

            if (posCaracter % 16 == 0) {//VUELVE A VALIDAR SI LLEGUE AL FINAL DE LINEA CUANDO EL CARACTER ES DOBLE
                direccionSalida.append("000000000");
                direccionSalida.append(Integer.toHexString(direccionNro));

                direccionEscribe.append(direccionSalida.substring(direccionSalida.length() - 8, direccionSalida.length()));
                direccionEscribe.append("  ");

                bufferedOutput.write(direccionEscribe.toString());
                bufferedOutput.write(hexaEscribe.toString());
                bufferedOutput.write(caracterEscribe.toString());
                bufferedOutput.write("\r\n");//salto de linea

                direccionNro = direccionNro + 16;

                hexaEscribe = new StringBuilder("");
                caracterEscribe = new StringBuilder("");

                direccionSalida = new StringBuilder("  ");
                direccionEscribe = new StringBuilder("0x");
            }

            caracterEscribeBruto.append(hexToASCII(Integer.toHexString(ch).toString().toUpperCase()).toString());
            
            if(posCaracter % 255 == 0){
                caracterEscribeBruto = new StringBuilder("");
            }

            rellenoHexa = new StringBuilder("");

            //ch = (char)br.read();
            ch = (char)ds.readByte();
        }

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
        System.out.println("Decimal : " + temp.toString());

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

}

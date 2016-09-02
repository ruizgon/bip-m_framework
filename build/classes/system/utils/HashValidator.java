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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 *
 * @author Gonzalo
 */
public class HashValidator {

    private static String getHashFile(String path, String algorithm){
        String hash = null;
    try{
        File file = new File(path);
        FileInputStream inputStream = new FileInputStream(file);
        MessageDigest digest = MessageDigest.getInstance(algorithm);
 
        byte[] bytesBuffer = new byte[1024];
        int bytesRead = -1;
 
        int cont = 0;
        while ((bytesRead = inputStream.read(bytesBuffer)) != -1) {
            digest.update(bytesBuffer, 0, bytesRead);
            cont++;
        }
 
        byte[] hashedBytes = digest.digest();
 
        hash = Conversor.byteArrayToHexString(hashedBytes);
    } catch (Exception ex) {
        System.out.println("Error.");
    }
    
    return hash;
}
    public static String getMD5Hash(String path) {
        String md5 = null;

        try { 
            md5 = getHashFile(path,"MD5");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return md5;
    }

    public static String getSHA1Hash(String path) {
        String sha1 = null;

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA"); //Obtiene instancia SHA-1
            InputStream archivo = new FileInputStream(path);
            byte[] buffer = new byte[1];
            int fin_archivo = -1;
            int caracter;

            caracter = archivo.read(buffer);

            while (caracter != fin_archivo) {

                messageDigest.update(buffer); //Por cada caracter, actualiza el hash
                caracter = archivo.read(buffer);
            }

            archivo.close();
            byte[] hash = messageDigest.digest(); //Genera el hash SHA-1

            /**
             * Genera una representación hexadecimal
             */
            String str = "";
            for (int i = 0; i < hash.length; i++) {
                str += Integer.toHexString((hash[i] >> 4) & 0xf);
            }
            sha1 = str;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return sha1;
    }

    public static boolean validateMD5Hash(String path, String originalHash) {
        boolean valid = false;

        try {
            String hash = getMD5Hash(path);//Obtiene nuevamente el hash
            if (originalHash.equals(hash)) {
                valid = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return valid;
    }

    public static boolean validateSHA1Hash(String path, String originalHash) {
        boolean valid = false;

        try {
            String hash = getSHA1Hash(path);
            if (originalHash.equals(hash)) {
                valid = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return valid;
    }
}

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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 *
 * @author Gonzalo
 */
public class FileManager {

    private static FileManager _instance;

    private FileManager() {

    }

    public static FileManager getInstance() {
        if (_instance == null) {
            _instance = new FileManager();
        }

        return _instance;
    }

    public synchronized void printFileContent(String name) {
        try {
            URL urlToDictionary = this.getClass().getResource("/sources/" + name);
            InputStream stream = urlToDictionary.openStream();
            InputStreamReader ir = new InputStreamReader(stream);
            BufferedReader br = new BufferedReader(ir);
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    public synchronized void write(String path, String content) {

        try {
            FileWriter output = new FileWriter(path,true);
            output.write(content);
            String retorno = System.getProperty("line.separator");
            output.write(retorno);
            output.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized String read(String path) {
        String content = null;

        return content;
    }
}

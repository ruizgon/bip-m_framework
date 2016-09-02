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

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Gonzalo
 */
public class DataPrimitiveType {

    private static DataPrimitiveType _instance;
    private static Map<String, Integer> _types;

    private DataPrimitiveType() {
        _types = new HashMap<String, Integer>();
        _types.put("char", 1);
        _types.put("int 8", 1);
        _types.put("int 16", 2);
        _types.put("int 32", 4);
        _types.put("int 64", 8);
        _types.put("pointer32", 4);
        _types.put("pointer64", 8);
        _types.put("RVApointer32", 4);
        _types.put("RVApointer64", 8);
    }

    public static DataPrimitiveType getInstance() {
        if (_instance == null) {
            _instance = new DataPrimitiveType();
        }
        return _instance;
    }

    public Map<String, Integer> getTypes() {
        return _types;
    }

    public void setTypes(Map<String, Integer> _types) {
        this._types = _types;
    }

    public boolean isPrimitiveType(String type) {
        boolean primitive = false;

        type = getPrimitiveType(type);

        /*if(type.equals("int 8") || type.equals("int 16") || type.equals("int 32") || type.equals("int 64") || type.equals("char")){
         primitive = true;
         }*/
        if (_types.containsKey(type)) {
            primitive = true;
        }

        return primitive;
    }

    public String getPrimitiveType(String type) {
        if (type.contains("pointer")) {
            String[] typeArray = type.split(" ");
            if (typeArray.length > 0) {
                type = typeArray[0];
            }
        }

        return type;
    }

    public boolean isNumericPrimitiveType(String type) {
        boolean numeric = false;

        type = getPrimitiveType(type);

        switch (type) {
            case ("int 8"):
                numeric = true;
                break;
            case ("int 16"):
                numeric = true;
                break;
            case ("int 32"):
                numeric = true;
                break;
            case ("int 64"):
                numeric = true;
                break;
            case ("pointer32"):
                numeric = true;
                break;
            case ("pointer64"):
                numeric = true;
                break;
            case ("RVApointer32"):
                numeric = true;
                break;
            case ("RVApointer64"):
                numeric = true;
                break;
            default:
                break;
        }

        return numeric;
    }

    public boolean isPointer(String type) {
        boolean pointer = false;

        type = getPrimitiveType(type);

        switch (type) {
            case ("pointer32"):
                pointer = true;
                break;
            case ("pointer64"):
                pointer = true;
                break;
            case ("RVApointer32"):
                pointer = true;
                break;
            case ("RVApointer64"):
                pointer = true;
                break;
            default:
                break;
        }

        return pointer;
    }
}

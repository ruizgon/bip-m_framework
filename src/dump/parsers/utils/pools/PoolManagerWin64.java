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
package dump.parsers.utils.pools;

import entities.EntityAttribute;
import java.util.HashMap;

/**
 *
 * @author Gonzalo
 */
public class PoolManagerWin64 extends PoolManager{
    public static final int _POOL_HEADER_SIZE = 0x10; //10 elementos, 0x10 bytes
    public static final int _OBJECT_HEADER_PROCESS_INFO = 0x10; //2 elementos, 0x10 bytes
    public static final int _OBJECT_HEADER_QUOTA_INFO = 0x20; //5 elementos, 0x20 bytes
    public static final int _OBJECT_HEADER_HANDLE_INFO = 0x10; //2 elementos, 0x10 bytes
    public static final int _OBJECT_HEADER_NAME_INFO = 0x20; //3 elementos, 0x20 bytes
    public static final int _OBJECT_HEADER_CREATOR_INFO = 0x20; //4 elementos, 0x20 bytes
    public static final int _OBJECT_HEADER = 0x38; //12 elementos, 0x38 bytes
    public static final long _MIN_SIZE_STRUCTURE = 0X40;//[bytes]
    public static final long _MAX_SIZE_STRUCTURE = 0XC8;//[bytes]

    public PoolManagerWin64() {
        try{
            this.setAttributes(new HashMap<String, EntityAttribute>());

            EntityAttribute a0 = new EntityAttribute("PoolTag", 0x004, null, "PoolTag", false);
            this.getAttributes().put("PoolTag", a0);
        }catch(Exception ex){
            
        }
    } 
}

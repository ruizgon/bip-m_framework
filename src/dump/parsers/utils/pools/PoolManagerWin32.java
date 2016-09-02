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
public class PoolManagerWin32 extends PoolManager {

    public static final int _POOL_HEADER_SIZE = 0x08; //8 elements, 0x08 bytes
    public static final int _OBJECT_HEADER_PROCESS_INFO = 0x08; //2 elements, 0x8 bytes
    public static final int _OBJECT_HEADER_QUOTA_INFO = 0x10; //4 elements, 0x10 bytes
    public static final int _OBJECT_HEADER_HANDLE_INFO = 0x08; //2 elements, 0x8 bytes
    public static final int _OBJECT_HEADER_NAME_INFO = 0x10; //3 elements, 0x10 bytes
    public static final int _OBJECT_HEADER_CREATOR_INFO = 0x10; //4 elements, 0x10 bytes
    public static final int _OBJECT_HEADER = 0x18; //12 elements, 0x20 bytes. Se modifica a 0x18, dado que por ingeniería inversa se comprueban estos tamaños.
    public static final long _MIN_SIZE_STRUCTURE = 0X20;//[bytes] Se modifica de 0x28 a 0x20, dado que por ingeniería inversa se comprueban estos tamaños.
    public static final long _MAX_SIZE_STRUCTURE = 0X68;//[bytes]

    public PoolManagerWin32() {
        try{
            this.setAttributes(new HashMap<String, EntityAttribute>());

            EntityAttribute a0 = new EntityAttribute("PoolTag", 0x004, null, "PoolTag", false);
            this.getAttributes().put("PoolTag", a0);
        }catch(Exception ex){
            
        }
    } 
    
    
}

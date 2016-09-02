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
package dump.parsers.utils.tags;

import java.util.HashMap;

/**
 *
 * @author Gonzalo
 */
public class TagManagerWin32 extends TagManager{
    
    /**
     * Inicializa el map con las etiquetas de los distintos objetos
     * que se pueden encontrar en el archivo de volcado de memoria
     */
    public TagManagerWin32() {
        try{
            _tagItems = new HashMap<String,TagItem>();
            TagItemWin32 i0 = new TagItemWin32("Proã", "Proc", 1304);
            _tagItems.put("Process", i0);
            TagItemWin32 i1 = new TagItemWin32("Thrä", "Thrd", 1248);
            _tagItems.put("Thread", i1);
            TagItemWin32 i2 = new TagItemWin32("Filå", "File", 288);
            _tagItems.put("File", i2);
            /**
             * It is a special type of file that contains a reference to 
             * another file or directory in the form of an absolute or 
             * relative path and that affects pathname resolution
             */
            TagItemWin32 i3 = new TagItemWin32("Linö", "Link", 104);
            _tagItems.put("SymbolicLink", i3);
            TagItemWin32 i4 = new TagItemWin32("Driö", "Driv", 408);
            _tagItems.put("Driver", i4);
            TagItemWin32 i5 = new TagItemWin32("Desö", "Desk", 296);
            _tagItems.put("Desktop", i5);
            TagItemWin32 i6 = new TagItemWin32("Winä", "Wind", 224);
            _tagItems.put("WindowStation", i6);
             TagItemWin32 i7 = new TagItemWin32("TcpE", "TcpE", 36);
            _tagItems.put("TCPConnection", i7);
             TagItemWin32 i8 = new TagItemWin32("TcpL", "TcpL", 598);
            _tagItems.put("TCPSocket", i8);
             TagItemWin32 i9 = new TagItemWin32("UdpA", "UdpA", 36); //Validar!
            _tagItems.put("UDPConnection", i9);
            TagItemWin32 i10 = new TagItemWin32("CM10", "CM10", 36); //Validar!
            _tagItems.put("RegistryEntry", i10);
            TagItemWin32 i11 = new TagItemWin32("KDBG@", "KDBG@", 0xA4);
            _tagItems.put("KernelDebugger", i11);
        }catch(Exception ex){
            
        }
    }
    
}

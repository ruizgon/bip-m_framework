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
public class TagManagerWin64 extends TagManager {

    /**
     * Inicializa el map con las etiquetas de los distintos objetos que se
     * pueden encontrar en el archivo de volcado de memoria
     */
    public TagManagerWin64() {
        try {
            _tagItems = new HashMap<String, TagItem>();
            TagItemWin64 i0 = new TagItemWin64("Proã", "Proc", 1304);
            _tagItems.put("Process", i0);
            TagItemWin64 i1 = new TagItemWin64("Thrä", "Thrd", 1248);
            _tagItems.put("Thread", i1);
            TagItemWin64 i2 = new TagItemWin64("Filå", "File", 288);
            _tagItems.put("File", i2);
            /**
             * It is a special type of file that contains a reference to another
             * file or directory in the form of an absolute or relative path and
             * that affects pathname resolution
             */
            TagItemWin64 i3 = new TagItemWin64("Linö", "Link", 104);
            _tagItems.put("SymbolicLink", i3);
            TagItemWin64 i4 = new TagItemWin64("Driö", "Driv", 408);
            _tagItems.put("Driver", i4);
            TagItemWin64 i5 = new TagItemWin64("Desö", "Desk", 296);
            _tagItems.put("Desktop", i5);
            TagItemWin64 i6 = new TagItemWin64("Winä", "Wind", 224);
            _tagItems.put("WindowStation", i6);
            TagItemWin64 i7 = new TagItemWin64("TcpE", "TcpE", 36);
            _tagItems.put("TCPConnection", i7);
            TagItemWin64 i8 = new TagItemWin64("TcpL", "TcpL", 598);
            _tagItems.put("TCPSocket", i8);
            TagItemWin64 i9 = new TagItemWin64("UdpA", "UdpA", 36); //Validar!
            _tagItems.put("UDPConnection", i9);
            TagItemWin64 i10 = new TagItemWin64("CM10", "CM10", 36); //Validar!
            _tagItems.put("RegistryEntry", i10);
            TagItemWin64 i11 = new TagItemWin64("KDBG@", "KDBG@", 0xA4);
            _tagItems.put("KernelDebugger", i11);
        } catch (Exception ex) {

        }
    }
}

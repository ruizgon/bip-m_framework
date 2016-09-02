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
package translation;

import entities.translation.EntityAddressSpace;
import java.math.BigInteger;

/**
 *
 * @author Gonzalo
 */
public interface ITranslator {

    /*
     * Obtiene los valores de _startAddressSpace y _endAddressSpace
     * LLena _aSList con entidades AddressSpace para hacer traducción
     * y obtener contenido
     */
    public abstract void obtainAddressSpaceList();

    public abstract void getTranslationData(String virtualAddress);

    public abstract long calculatePhysicalAddress(EntityAddressSpace aS);

    public abstract BigInteger calculatePhysicalAddress(String virtualAddress);

    public abstract BigInteger calculatePhysicalAddress(BigInteger virtualAddress);

    public abstract void obtainPhysicalAddressContent(EntityAddressSpace aS);

    public abstract EntityAddressSpace obtainAddressSpace(String virtualAddress);

    public abstract EntityAddressSpace obtainAddressSpace(BigInteger virtualAddress);

    public abstract void getVirtualAddressData(BigInteger virtualAddress);

    public abstract EntityAddressSpace processCommand(String modifier);
}

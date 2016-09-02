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
package persistence;

import analyzer.states.SeekerState;

/**
 *
 * @author Gonzalo
 */
public interface IDataService {
       /*
     * El servicio de persistencia debe implementar un mecanismo para
     * saber si los datos se encuentran presentes o hay que solicitar al
     * DumpManager
     * Para ello, isDataPresent debe conocer la clase de objeto que se está buscando,
     * por ejemplo, EntityProcess.getClass()
     */
    public abstract boolean isDataPresent(entities.Entity entity);
    public abstract boolean isDataPresent(SeekerState state); 
    public abstract boolean isDataPresent(SeekerState state, entities.Entity entity); 
    public abstract boolean isDataPresent(String tag); 
}

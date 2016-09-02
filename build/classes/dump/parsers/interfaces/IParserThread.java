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
package dump.parsers.interfaces;

import entities.Entity;
import entities.EntityAttribute;
import entities.process.EntityDispatcherHeader;
import entities.process.EntityKTHREAD;
import entities.process.EntityProcess;
import entities.process.EntityThread;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Gonzalo
 */
public interface IParserThread {
    /**
     * Devuelve el proceso con su lista de Threads instanciada
     * @param process
     * @return 
     */
    public abstract ConcurrentHashMap<Object, Entity> getThreadsByProcess(EntityProcess process);
    public abstract EntityKTHREAD obtainKTHREADContent(long offset, Map.Entry<String, EntityAttribute> e);
    public abstract EntityDispatcherHeader obtainDispatcherHeader(long offset, Map.Entry<String, EntityAttribute> e);
}

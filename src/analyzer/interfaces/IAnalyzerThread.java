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
package analyzer.interfaces;

import entities.process.EntityProcess;
import entities.process.EntityThread;
import java.util.ArrayList;

/**
 *
 * @author Gonzalo
 * Se defiune una interfaz para comportamiento relacionado con la búsqueda de
 * threads, desacoplada del SeekerProcess, dado que la implementación puede
 * diferir en otros sistemas operativos, por ejemplo, linux.
 */
public interface IAnalyzerThread {
    public abstract ArrayList<EntityThread> getThreadsByProcess(EntityProcess process);
}

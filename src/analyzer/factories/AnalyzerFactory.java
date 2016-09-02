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
package analyzer.factories;

import analyzer.SeekerEncrypt;
import analyzer.malware.SeekerRootkit;
import analyzer.connections.SeekerConnection;
import analyzer.files.SeekerFile;
import analyzer.handles.SeekerHandle;
import analyzer.hives.SeekerHive;
import analyzer.libs.SeekerLib;
import analyzer.processes.SeekerProcess;
import analyzer.states.SeekerState;

/**
 *
 * @author Gonzalo
 */
public abstract class AnalyzerFactory {
    public abstract SeekerProcess createSeekerProcess();
    public abstract SeekerLib createSeekerLib();
    public abstract SeekerConnection createSeekerConnection();
    public abstract SeekerRootkit createSeekerRootkit();
    public abstract SeekerFile createSeekerFile();
    public abstract SeekerHandle createSeekerHandle();
    public abstract SeekerHive createSeekerHive();
    public abstract SeekerEncrypt createSeekerEncrypt();
    
    public abstract SeekerProcess createSeekerProcess(SeekerState state);
    public abstract SeekerLib createSeekerLib(SeekerState state);
    public abstract SeekerConnection createSeekerConnection(SeekerState state);
    public abstract SeekerRootkit createSeekerRootkit(SeekerState state);
    public abstract SeekerFile createSeekerFile(SeekerState state);
    public abstract SeekerHandle createSeekerHandle(SeekerState state);
    public abstract SeekerHive createSeekerHive(SeekerState state);
    public abstract SeekerEncrypt createSeekerEncrypt(SeekerState state);
}

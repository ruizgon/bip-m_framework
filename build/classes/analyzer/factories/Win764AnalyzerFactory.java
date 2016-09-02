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
import analyzer.connections.SeekerConnectionWin764;
import analyzer.files.SeekerFile;
import analyzer.handles.SeekerHandle;
import analyzer.hives.SeekerHive;
import analyzer.libs.SeekerLib;
import analyzer.libs.SeekerLibWin764;
import analyzer.malware.SeekerRootkitSSDTWin732;
import analyzer.malware.SeekerRootkitSSDTWin764;
import analyzer.processes.SeekerProcess;
import analyzer.processes.SeekerProcessWin764;
import analyzer.states.SeekerState;

/**
 *
 * @author Gonzalo
 */
public class Win764AnalyzerFactory extends AnalyzerFactory{

    @Override
    public SeekerProcess createSeekerProcess() {
        return new SeekerProcessWin764(null);
    }

    @Override
    public SeekerLib createSeekerLib() {
        return new SeekerLibWin764(null);
    }

    @Override
    public SeekerConnection createSeekerConnection() {
        return new SeekerConnectionWin764(null);
    }

    @Override
    public SeekerRootkit createSeekerRootkit() {
        return new SeekerRootkitSSDTWin764(null);
    }

    @Override
    public SeekerFile createSeekerFile() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SeekerHandle createSeekerHandle() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SeekerHive createSeekerHive() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SeekerEncrypt createSeekerEncrypt() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SeekerProcess createSeekerProcess(SeekerState state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SeekerLib createSeekerLib(SeekerState state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SeekerConnection createSeekerConnection(SeekerState state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SeekerRootkit createSeekerRootkit(SeekerState state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SeekerFile createSeekerFile(SeekerState state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SeekerHandle createSeekerHandle(SeekerState state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SeekerHive createSeekerHive(SeekerState state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SeekerEncrypt createSeekerEncrypt(SeekerState state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

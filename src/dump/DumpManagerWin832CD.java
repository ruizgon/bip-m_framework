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
package dump;

import analyzer.Seeker;
import analyzer.states.SeekerState;
import dump.parsers.Parser;
import entities.Entity;
import java.util.Observer;

/**
 *
 * @author Gonzalo
 */
public class DumpManagerWin832CD extends DumpManager{

    public DumpManagerWin832CD(String _path, boolean _littleEndian) {
        super(_path, null, _littleEndian);
    }

    public static DumpManagerWin832CD getInstance(String path, boolean littleEndian) {
        if (_instance == null) {
            _instance = new DumpManagerWin832CD(path, littleEndian);
        }
        return (DumpManagerWin832CD) _instance;
    }

    @Override
    public synchronized int countObservers() {
        return super.countObservers(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized boolean hasChanged() {
        return super.hasChanged(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected synchronized void clearChanged() {
        super.clearChanged(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected synchronized void setChanged() {
        super.setChanged(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized void deleteObservers() {
        super.deleteObservers(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void notifyObservers(Object o) {
        super.notifyObservers(o); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void notifyObservers() {
        super.notifyObservers(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void configTranslator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void obtainDumpFormatData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void initializeParsers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setParserByState(SeekerState state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setParserByEntity(Entity entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setParser(Entity entity, Parser parser) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

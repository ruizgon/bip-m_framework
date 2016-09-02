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
package system.utils;

import system.utils.Observer;
import java.util.ArrayList;


/**
 *
 * @author Gonzalo
 */
public abstract class Observable {
    private boolean _changed;
    private ArrayList<Observer> _observers;

    public Observable(){
        _observers = new ArrayList<>();
    }

    public synchronized void addObserver(Observer observer) {
        this._observers.add(observer);
    }

    public synchronized void deleteObserver(Observer observer) {
        this._observers.add(observer);
    }

    public void notifyObservers() {
        for(Observer o : this._observers){
            o.update(null);
        }
    }

    public void notifyObservers(Object obj) {
        for(Observer o : this._observers){
            o.update(obj);
        }
    }

    public synchronized void deleteObservers() {
        this._observers.clear();
    }

    protected synchronized void setChanged() {
       throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    protected synchronized void clearChanged() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public synchronized boolean hasChanged() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public synchronized int countObservers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

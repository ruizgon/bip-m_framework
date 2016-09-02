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
package analyzer;

import entities.Entity;
import system.utils.Observable;
import persistence.analyzer_service.PersistenceObject;

/**
 *
 * @author Gonzalo
 */
public abstract class Seeker extends Observable {

    protected Seeker _seeker;
    private boolean _busy;
    private PersistenceObject _aService;
    protected String _tag;

    public Seeker getSeeker() {
        return _seeker;
    }

    public void setSeeker(Seeker _seeker) {
        this._seeker = _seeker;
    }

    public boolean isBusy() {
        return _busy;
    }

    public void setBusy(boolean _busy) {
        this._busy = _busy;
    }

    public PersistenceObject getAService() {
        return _aService;
    }

    public void setAService(PersistenceObject _persistenceObject) {
        this._aService = _persistenceObject;
    }

    public String getTag() {
        return _tag;
    }

    public void setTag(String _tag) {
        this._tag = _tag;
    }

    /**
     *
     * @param entity
     * @param attribute
     * @param content
     * @return
     */
    public abstract Entity getEntityByAttributeValue(Entity entity, String attribute, Object content);
}

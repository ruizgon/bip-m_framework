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
package analyzer.states;

import dump.parsers.Parser;
import entities.Entity;
import java.util.ArrayList;
import java.util.List;
import output.OutputManagerAnalyzer;
import output.OutputManagerException;
import persistence.analyzer_service.PersistenceObject;
import system.utils.Observable;

/**
 *
 * @author Gonzalo
 */
public abstract class SeekerState extends Observable{

    private Entity _entity;
    private String _stateLabel;
    private PersistenceObject _persistenceObject;
    private String _entityTag; //Seteado por la entidad a la que compone
    private String _tag; //Seteado por la entidad y el estado
    private Parser _parser;

    public SeekerState() {
        /**
         * Registra observadores
         */
        this.addObserver(OutputManagerException.getInstance());
        this.addObserver(OutputManagerAnalyzer.getInstance());
    }

    public Entity getEntity() {
        return _entity;
    }
    
    public void setAService(PersistenceObject persistenceObject){
        this._persistenceObject = persistenceObject;
    }

    public void setEntity(Entity _entity) {
        this._entity = _entity;
    }

    public String getStateLabel() {
        return _stateLabel;
    }

    public void setStateLabel(String _stateLabel) {
        this._stateLabel = _stateLabel;
    }

    public PersistenceObject getPersistenceObject() {
        return _persistenceObject;
    }

    public void setPersistenceObject(PersistenceObject _persistenceObject) {
        this._persistenceObject = _persistenceObject;
    }

    public String getEntityTag() {
        return _entityTag;
    }

    public void setEntityTag(String _entityTag) {
        this._entityTag = _entityTag;
    }

    public String getTag() {
        return _tag;
    }

    public void setTag(String _tag) {
        this._tag = _tag;
    }

    public Parser getParser() {
        return _parser;
    }

    public void setParser(Parser _parser) {
        this._parser = _parser;
    }

    public abstract List<Entity> getList();

    public abstract Entity getFirst();

    public abstract Entity getNext(Entity entity);

    public abstract Entity getPrev(Entity entity);
}

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
package analyzer.hives;

import analyzer.Seeker;
import entities.hive.EntityCell;
import entities.hive.EntityHHiveHBaseBlock;
import entities.hive.EntityHive;
import java.util.ArrayList;
import persistence.analyzer_service.PersistenceObject;

/**
 *
 * @author Gonzalo
 */
public abstract class SeekerHive extends Seeker {

    private ArrayList<EntityHive> _hiveList; //Lista de CMHIVEs
    private ArrayList<EntityCell> _cellList; //listados de entrada/valor de registro
    private EntityHive _hiveLocated; //hive encontrada.
    private EntityHive _firstHiveLocated;
    private int _hiveCount;
    private PersistenceObject _persistenceObject;

    public ArrayList<EntityHive> getHiveList() {
        return _hiveList;
    }

    public void setHiveList(ArrayList<EntityHive> _hiveList) {
        this._hiveList = _hiveList;
    }

    public ArrayList<EntityCell> getCellList() {
        return _cellList;
    }

    public void setCellList(ArrayList<EntityCell> _cellList) {
        this._cellList = _cellList;
    }

    public EntityHive getHiveLocated() {
        return _hiveLocated;
    }

    public void setHiveLocated(EntityHive _hiveLocated) {
        this._hiveLocated = _hiveLocated;
    }

    public EntityHive getFirstHiveLocated() {
        return _firstHiveLocated;
    }

    public void setFirstHiveLocated(EntityHive _firstHiveLocated) {
        this._firstHiveLocated = _firstHiveLocated;
    }

    public int getHiveCount() {
        return _hiveCount;
    }

    public void setHiveCount(int _hiveCount) {
        this._hiveCount = _hiveCount;
    }

    public SeekerHive() {
    }

    /*
     *Constructor para cuando decora a otro Seeker
     */
    public SeekerHive(Seeker s) {
        super.setSeeker(s);
    }

    /*
     * Método hook
     */
    public abstract EntityHive getFirstHive();

    /*
     * Método hook
     */
    public abstract EntityHive getNextHive(EntityHive hive);

    /*
     * Método hook
     */
    public abstract EntityHive getPrevHive(EntityHive hive);

    /*
     * Método hook
     */
    public abstract EntityCell getFirstCell(EntityHHiveHBaseBlock hive);

    /*
     * Método hook
     */
    public abstract EntityCell getNextCell(EntityCell hive);

    /*
     * Método hook
     */
    public abstract EntityCell getPrevCell(EntityCell hive);

    public PersistenceObject getAService() {
        return _persistenceObject;
    }

    public void setAService(PersistenceObject _persistenceObject) {
        this._persistenceObject = _persistenceObject;
    }

}

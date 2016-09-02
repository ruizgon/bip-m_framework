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
package analyzer.handles;

import analyzer.Seeker;
import entities.Entity;
import entities.process.EntityProcess;
import java.util.ArrayList;

/**
 *
 * @author Gonzalo
 */
public abstract class SeekerHandle extends Seeker {

    private ArrayList<Entity> _handleList;

    public SeekerHandle() {
    }

    /*
     *Constructor para cuando decora a otro Seeker
     */
    public SeekerHandle(Seeker s) {
        super.setSeeker(s);
    }

    public ArrayList<Entity> getHandleList() {
        return _handleList;
    }

    public void setHandleList(ArrayList<Entity> _handleList) {
        this._handleList = _handleList;
    }

    /*
     * Template methods
     */
    public ArrayList<Entity> seekHandlesByProcess(EntityProcess process) {
        ArrayList<Entity> handleList = new ArrayList<Entity>();

        return handleList;
    }

    public ArrayList<Entity> seekAllHandles() {
        ArrayList<Entity> handleList = new ArrayList<Entity>();

        return handleList;
    }

    /*
     * Hook methods
     */
    public abstract Entity seekFirstHandleInTable();

    public abstract Entity seekeNextHandleInTable(Entity e);

    public abstract Entity seekePrevHandleInTable(Entity e);
}

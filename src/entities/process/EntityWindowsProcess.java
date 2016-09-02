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
package entities.process;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Gonzalo
 */
public abstract class EntityWindowsProcess extends EntityProcess {

    private EntityPEB PEB;
    private String handleTable; //puntero a Handle Table
    private Date kernelTime;
    private Date userTime;
    private ArrayList<EntityThread> _threads; //En Windows es el KTHREAD

    public EntityPEB getPEB() {
        return PEB;
    }

    public void setPEB(EntityPEB PEB) {
        this.PEB = PEB;
    }

    public String getHandleTable() {
        return handleTable;
    }

    public void setHandleTable(String handleTable) {
        this.handleTable = handleTable;
    }

    public Date getKernelTime() {
        return kernelTime;
    }

    public void setKernelTime(Date kernelTime) {
        this.kernelTime = kernelTime;
    }

    public Date getUserTime() {
        return userTime;
    }

    public void setUserTime(Date userTime) {
        this.userTime = userTime;
    }

    public ArrayList<EntityThread> getThreads() {
        return _threads;
    }

    public void setThreads(ArrayList<EntityThread> threads) {
        this._threads = threads;
    }
}

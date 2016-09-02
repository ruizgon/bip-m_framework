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
package output;

import java.util.Date;
import system.utils.Observable;
import system.utils.Observer;

/**
 *
 * @author Gonzalo
 */
public abstract class OutputManager extends Observable implements Observer {

    protected String _currentSessionID;
    protected String _user;
    protected String _lastLogDescription;
    protected Date _lastLogDate;
    protected String _logPath;
    protected String _logName;
    protected String _fullLogPath;

    public String getUser() {
        return _user;
    }

    public void setUser(String user) {
        this._user = user;
    }

    public String getLastLogDescription() {
        return _lastLogDescription;
    }

    public void setLastLogDescription(String lastLogDescription) {
        this._lastLogDescription = lastLogDescription;
    }

    public Date getLastLog() {
        return _lastLogDate;
    }

    public void setLastLog(Date lastLog) {
        this._lastLogDate = lastLog;
    }

    public String getCurrentSessionID() {
        return _currentSessionID;
    }

    public void setCurrentSessionID(String _currentSessionID) {
        this._currentSessionID = _currentSessionID;
    }

    public Date getLastLogDate() {
        return _lastLogDate;
    }

    public void setLastLogDate(Date _lastLogDate) {
        this._lastLogDate = _lastLogDate;
    }

    public String getLogPath() {
        return _logPath;
    }

    public void setLogPath(String _logPath) {
        this._logPath = _logPath;
    }

    public String getLogName() {
        return _logName;
    }

    public void setLogName(String _logName) {
        this._logName = _logName;
    }

    public String getFullLogPath() {
        return _fullLogPath;
    }

    public void setFullLogPath(String _fullLogPath) {
        this._fullLogPath = _fullLogPath;
    }

    public abstract void registerLog(String path, Object param);

    public abstract String getLog();

    /**
     * Genera el nombre del archivo para registrar luego el log correspondiente
     */
    public abstract void generateLogName();

    public abstract void generateLogName(String currentSessionID, String logPath);
}

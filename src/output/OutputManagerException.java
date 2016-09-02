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

import java.io.File;
import system.utils.DateManager;
import system.utils.FileManager;

/**
 *
 * @author Gonzalo
 */
public class OutputManagerException extends OutputManager {

    protected static OutputManager _instance;

    private OutputManagerException() {
        this.generateLogName();
    }

    private OutputManagerException(String currentSessionID, String logPath) {
        this._currentSessionID = currentSessionID;
        this._logPath = logPath;
        this.generateLogName();
    }

    public static OutputManagerException getInstance() {
        if (_instance == null) {
            _instance = new OutputManagerException();
        }

        return (OutputManagerException) _instance;
    }

    public static OutputManagerException getInstance(String currentSessionID, String logPath) {
        if (_instance == null) {
            _instance = new OutputManagerException(currentSessionID, logPath);
        }

        return (OutputManagerException) _instance;
    }

    @Override
    public void registerLog(String path, Object param) {
        try {
            FileManager fileManager = FileManager.getInstance();

            if (param instanceof String) {
                String paramStr = (String) param;
                fileManager.write(path, paramStr);
            }

        } catch (Exception ex) {

        }
    }

    @Override
    public String getLog() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Object o) {
        String log = "";
        if (o instanceof String) {
            log = (String) o;
        } else {
            if (o instanceof Exception) {
                Exception ex = (Exception) o;
                StringBuilder sbError = new StringBuilder();
                StackTraceElement[] stacks = ex.getStackTrace();
                for(StackTraceElement e : stacks){
                    sbError.append("ClassName: " + e.getClassName() + " ");
                    sbError.append("FileName: " + e.getFileName()+ " ");
                    sbError.append("MethodName: " + e.getMethodName()+ " ");
                    sbError.append("Line: " + e.getLineNumber()+ " ");
                    sbError.append("\n");
                }
                log = "Mensaje: " + ex.getMessage() + " " + sbError.toString();
            }
        }
        this.registerLog(this._fullLogPath, log);
    }

    @Override
    public void generateLogName() {
        try {
            if (this._currentSessionID != null) {
                String logName = "log_exception_" + this._currentSessionID;
                this._logName = logName;

                File logFolder = new File(this._logPath);
                if (!logFolder.exists()) { // si no existe, lo crea
                    logFolder.mkdirs();
                }

                this._fullLogPath = this._logPath + "\\" + this._logName + ".txt";

                /**
                 * Registro encabezado del archivo de log
                 */
                String content = ".::.BIP-M Framework.::.";
                this.registerLog(this._fullLogPath, content);
                content = "------------------------------------------------------------------------------------------------";
                this.registerLog(this._fullLogPath, content);
                content = "Logs de excepciones de procesos realizados en la sesión ID: " + this._currentSessionID;
                this.registerLog(this._fullLogPath, content);

                content = "Fecha: " + DateManager.getActualDateWithISOFormat();
                this.registerLog(this._fullLogPath, content);

                content = "------------------------------------------------------------------------------------------------";
                this.registerLog(this._fullLogPath, content);
            }
        } catch (Exception ex) {
            this.notifyObservers(ex);
        }
    }

    @Override
    public void generateLogName(String currentSessionID, String logPath) {
        try {
            if (currentSessionID != null) {
                this._currentSessionID = currentSessionID;
                this._logPath = logPath;

                String logName = "log_exception_" + currentSessionID;
                this._logName = logName;
                this._fullLogPath = logPath + "\\" + this._logName + ".txt";

                /**
                 * Registro encabezado del archivo de log
                 */
                String content = ".::.BIP-M Framework.::.";
                this.registerLog(this._fullLogPath, content);
                content = "------------------------------------------------------------------------------------------------";
                this.registerLog(this._fullLogPath, content);
                content = "Logs de excepciones de procesos realizados en la sesión ID: " + currentSessionID;
                this.registerLog(this._fullLogPath, content);

                content = "Fecha: " + DateManager.getActualDateWithISOFormat();
                this.registerLog(this._fullLogPath, content);

                content = "------------------------------------------------------------------------------------------------";
                this.registerLog(this._fullLogPath, content);
            }
        } catch (Exception ex) {
            this.notifyObservers(ex);
        }
    }
}

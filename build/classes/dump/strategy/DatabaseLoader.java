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
package dump.strategy;

import dump.formats.DumpFormat;
import dump.ooss.OperatingSystemStructure;
import translation.Translator;

/**
 *
 * @author Gonzalo
 */
public abstract class DatabaseLoader extends LoadingStrategy {

    protected String _databaseName;
    
    public DatabaseLoader(OperatingSystemStructure _operatingSystemStructure, DumpFormat _dumpFormat, Translator _translator) {
        super(_operatingSystemStructure, _dumpFormat, _translator);
    }

    public String getDatabaseName() {
        return _databaseName;
    }

    public void setDatabaseName(String _databaseName) {
        this._databaseName = _databaseName;
    }

    public abstract void updatePersistenceDmpObjectParameters();
}

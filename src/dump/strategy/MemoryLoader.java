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
import entities.Entity;
import java.util.Map;
import persistence.dump_service.memory.DContentServiceMemory;
import translation.Translator;

/**
 *
 * @author Gonzalo
 */
public class MemoryLoader extends LoadingStrategy {

    public MemoryLoader(OperatingSystemStructure operatingSystemStructure, DumpFormat dumpFormat, Translator translator) {
        super(operatingSystemStructure, dumpFormat, translator);
        this._persitenceDmpObj = DContentServiceMemory.getInstance();
        
        /**
         * Registra observadores
         */
        this.addObserver(output.OutputManagerConsole.getInstance());
    }

    @Override
    public boolean isValidDump() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void createStructure() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void createStructure(OperatingSystemStructure structure) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String loadContent(Map<Object, Entity> entities) {
        String message = "";

        try {
            this._persitenceDmpObj.loadContent(entities);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return message;
    }

    @Override
    public String loadContentEntity(Entity entity) {
        String message = "";

        try {
            DContentServiceMemory.getInstance().loadContentEntity(entity);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return message;
    }

    @Override
    public int persistMetadata(Entity entity, String tag, String status) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

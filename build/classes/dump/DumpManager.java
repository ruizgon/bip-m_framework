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
package dump;

import analyzer.states.SeekerState;
import dump.factories.DumpFactory;
import dump.formats.DumpFormat;
import dump.ooss.OperatingSystemStructure;
import dump.parsers.Parser;
import dump.strategy.LoadingStrategy;
import entities.Entity;
import java.util.ArrayList;
import java.util.Map;
import persistence.dump_service.PersistenceDumpObject;
import system.utils.Observable;
import translation.Translator;

/**
 *
 * @author Gonzalo
 */
public abstract class DumpManager extends Observable {

    protected static DumpManager _instance;
    protected DumpFactory _dumpFactory;
    protected DumpFormat _dumpFormat;
    protected OperatingSystemStructure _os;
    protected LoadingStrategy _loadingStrategy;
    protected Translator _translator;
    protected String _path;
    protected boolean _littleEndian;
    /**
     * Cada Map corresponde a una colección de Entities específicas, por ejemplo
     * Map<Integer,EntityProcess>
     */
    protected ArrayList<Map<String, Entity>> _entityCollections;

    protected PersistenceDumpObject _persistence;

    protected DumpManager() {

    }

    protected DumpManager(String path, Translator translator, boolean littleEndian) {
        this._path = path;
        this._translator = translator;
        this._littleEndian = littleEndian;
    }

    public DumpFactory getDumpFactory() {
        return _dumpFactory;
    }

    public void setDumpFactory(DumpFactory dumpFactory) {
        this._dumpFactory = dumpFactory;
    }

    public DumpFormat getDumpFormat() {
        return _dumpFormat;
    }

    public void setDumpFormat(DumpFormat dumpFormat) {
        this._dumpFormat = dumpFormat;
    }

    public OperatingSystemStructure getOS() {
        return _os;
    }

    public void setOS(OperatingSystemStructure os) {
        this._os = os;
    }

    public LoadingStrategy getLoadingStrategy() {
        return _loadingStrategy;
    }

    public void setLoadingStrategy(LoadingStrategy loadingStrategy) {
        this._loadingStrategy = loadingStrategy;
    }

    public Translator getTranslator() {
        return _translator;
    }

    public void setTranslator(Translator _translator) {
        this._translator = _translator;
    }

    public String getPath() {
        return _path;
    }

    public void setPath(String _path) {
        this._path = _path;
    }

    public boolean checkValidDump() {
        return _loadingStrategy.isValidDump();
    }

    public PersistenceDumpObject getPersistence() {
        return _persistence;
    }

    public void setPersistence(PersistenceDumpObject _persistence) {
        this._persistence = _persistence;
    }

    public String executeLoadProcess() {
        configTranslator();
        initializeParsers();

        String mensaje = _loadingStrategy.executeLoadProcess();

        return mensaje;
    }

    public String executeLoadProcessByState(SeekerState state) {
        String mensaje = "";
        try {
            configTranslator();

            /**
             * Setea Parser según state
             */
            setParserByState(state);
            mensaje = _loadingStrategy.executeLoadProcessByTag(state.getEntityTag(), state.getTag());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return mensaje;
    }

    public String executeLoadProcessByTag(String tag) {
        configTranslator();
        initializeParsers();
        /**
         * Setea Parser según entity
         */
        String mensaje = _loadingStrategy.executeLoadProcessByTag(tag, null);

        return mensaje;
    }

    public String executeLoadProcessByEntity(Entity entity, Object[] params) {
        configTranslator();

        /**
         * Setea Parser según entity
         */
        String tagOrigin = entity.getTag();
        if (tagOrigin.contains("#")) {
            String[] tagElement = tagOrigin.split("#");
            String tag = tagElement[0];
            entity.setTag(tag);
        }
        setParserByEntity(entity);
        entity.setTag(tagOrigin);
        String mensaje = _loadingStrategy.executeLoadProcessByEntity(entity, params);

        return mensaje;
    }

    /**
     * Obtiene datos del volcado que pueden ser de utilidad para parseo y/o
     * traducción
     */
    public abstract void obtainDumpFormatData();

    /**
     * Configura al traductor con información adicional que se obtiene del dump
     */
    public abstract void configTranslator();

    /**
     * Inicializa parsers
     */
    public abstract void initializeParsers();

    /**
     * Modifica Parser según state
     */
    public abstract void setParserByState(SeekerState state);

    /**
     * Reemplaza parser que se quiera utilizar
     *
     * @param seeker
     */
    public abstract void setParserByEntity(Entity entity);

    /**
     *
     * @param entity
     * @param parser
     */
    public abstract void setParser(Entity entity, Parser parser);

}

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
package persistence.database.mysql;

import analyzer.states.SeekerState;
import dump.formats.DumpFormat;
import entities.Entity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Calendar;
import java.sql.Timestamp;
import java.util.Scanner;
import javax.sql.rowset.CachedRowSet;
import persistence.database.MapperDumpFormat;
import persistence.database.MapperGeneric;
import system.utils.DataManager;
import system.utils.DateManager;

/**
 *
 * @author juani
 */
/**
 * Con esta consulta obtengo todas las bases de datos: Esto debo usarlo para ver
 * si existe una base relacionada al dump que quiero analizar SELECT SCHEMA_NAME
 * FROM INFORMATION_SCHEMA.SCHEMATA where schema_name like 'bipm_%';
 *
 * La nomenclarura para el nombre de las bases será: bipm_el hash del
 * dump_nombre representativo ememplo:
 * so_arquitecturaycifrado_hash_fecha(formatoyyyyMMddThhmmssSSS)
 */
public class MapperMySQLWin732 extends MapperGeneric {

    private MapperDumpFormat _mapperDumpFormat;

    public MapperMySQLWin732() {
        this._model = MySQLModel.getInstane();
    }

    public MapperMySQLWin732(String path, String currentHashType, String currentHash, String databaseName) {
        this._path = path;
        this._currentHashType = currentHashType;
        this._currentHash = currentHash;
        this._databaseName = databaseName;

        this._model = MySQLModel.getInstane();
    }

    public MapperDumpFormat getMapperDumpFormat() {
        return _mapperDumpFormat;
    }

    public void setMapperDumpFormat(MapperDumpFormat _mapperDumpFormat) {
        this._mapperDumpFormat = _mapperDumpFormat;
    }

    @Override
    public boolean isDataPresent(Entity entity) {
        boolean present = false;

        try {
            Object[] parameters = new Object[2];
            String query = "CALL SP_METADATO_ENTITY_GET (?,?)";
            parameters[0] = entity.getTag();
            parameters[1] = null;
            CachedRowSet cachedRowSet = (CachedRowSet) this._model.get(query, parameters, null);

            while (cachedRowSet.next()) {
                present = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return present;
    }

    @Override
    public boolean isDataPresent(SeekerState state) {
        boolean present = false;

        try {
            Object[] parameters = new Object[2];
            String query = "CALL SP_METADATO_ENTITY_GET (?,?)";
            parameters[0] = state.getEntityTag();
            parameters[1] = state.getTag();
            CachedRowSet cachedRowSet = (CachedRowSet) this._model.get(query, parameters, null);

            while (cachedRowSet.next()) {
                present = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return present;
    }

    @Override
    public boolean isDataPresent(SeekerState state, Entity entity) {
        boolean present = false;

        try {
            Object[] parameters = new Object[2];
            String query = "CALL SP_METADATO_ENTITY_GET (?,?)";
            parameters[0] = entity.getTag();
            parameters[1] = state.getTag();
            CachedRowSet cachedRowSet = (CachedRowSet) this._model.get(query, parameters, null);

            while (cachedRowSet.next()) {
                present = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return present;
    }

    @Override
    public boolean isDataPresent(String tag) {
        boolean present = false;

        try {
            Object[] parameters = new Object[0];
            String query = "CALL SP_METADATOS_GET ()";
            CachedRowSet cachedRowSet = (CachedRowSet) this._model.get(query, parameters, null);

            while (cachedRowSet.next()) {
                if (tag.equals("Dump")) {
                    /**
                     * Evaluar valor de la columna en cuestión
                     */
                    present = true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return present;
    }

    @Override
    public boolean isDataBaseCreated(String databaseName) {
        boolean created = false;

        try {
            String output = "";
            MySQLModel model = MySQLModel.getInstane();
            Object[] parametersIn = new Object[0];
            output = "Buscando schemas existentes para el dump a analizar...";
            this.notifyObservers(output);

            String query = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA where schema_name like '" + databaseName + "%'";
            CachedRowSet cachedRowSet = (CachedRowSet) model.get(query, parametersIn, null);
            cachedRowSet.last();
            int row = cachedRowSet.getRow();
            cachedRowSet.beforeFirst();

            String commandLine = "";
            String[] schemas = new String[row];
            String currentSchema = "";
            int i = 0;
            while (cachedRowSet.next()) {
                if (i == 0) {
                    output = "Schemas encontrados: ";
                    this.notifyObservers(output);
                }
                created = true;
                currentSchema = cachedRowSet.getString(1); //SCHEMA_NAME
                schemas[i++] = currentSchema;
                output = i + ") " + currentSchema + ".";
                this.notifyObservers(output);
            }
            output = "Selecciona una opción de las encontradas o \"C\" para crear una nueva Base de datos: ";
            this.notifyObservers(output);
            Scanner scan = new Scanner(System.in);
            commandLine = scan.nextLine();
            while (!commandLine.equals("C")) {
                if (DataManager.getInstance().isNumeric(commandLine)) {
                    int option = Integer.valueOf(commandLine);
                    if (option <= i) {
                        String selectedSchema = schemas[option - 1];
                        /**
                         * Modifica la conexión, para que apunte a la base de
                         * datos creada
                         */
                        model.modifyConnection(null, -1, selectedSchema, null, null);
                        break;
                    } else {
                        output = "Selecciona una opción válida: ";
                        this.notifyObservers(output);
                        commandLine = scan.nextLine();
                    }
                } else {
                    output = "Selecciona una opción válida: ";
                    this.notifyObservers(output);
                    commandLine = scan.nextLine();
                }
            }

            if (commandLine.equals("C")) {
                created = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return created;
    }

    @Override
    public int createStructure(String path) {
        int respuesta = 0;

        try {
            String log = "Creando Base de Datos...";
            this.notifyObservers(log);

            MySQLModel model = MySQLModel.getInstane();
            String script = this.getScript(path);

            if (script != null) {
                Object[] parametersIn = new Object[0];
                String[] sArray = script.split("\\$\\$");
                String databaseName = this._databaseName + "_" + DateManager.getActualDate("yyyyMMdd", "hhmmssSSS");
                /**
                 * Crea la base de datos.
                 */
                String createQuery = "CREATE DATABASE if not exists " + databaseName + ";";
                respuesta = model.insert(createQuery, parametersIn, null);
                /**
                 * Modifica la conexión, para que apunte a la base de datos
                 * creada
                 */
                model.modifyConnection(null, -1, databaseName, null, null);

                /**
                 * Ejecuta el script de creación de tablas y procedimientos
                 */
                for (String s : sArray) {
                    if (!s.equals("") && !s.equals(" \n")) {
                        model.insert(s, parametersIn, null);
                    }
                }

                /**
                 * Guarda HASH, tipo HASH, FECHA y PATH del archivo de volcado
                 * de memoria
                 */
                parametersIn = new Object[8];
                String query = "CALL SP_METADATOS_INSERT (?, ?, ?, ?, ?, ?, ?, ?)";
                parametersIn[0] = this._path; // _fullPathDumpFile
                File file = new File(path);
                String nameDumpFile = file.getName();
                parametersIn[1] = nameDumpFile; // _nameDumpFile,
                Calendar calendar = Calendar.getInstance();
                parametersIn[2] = new Timestamp(calendar.getTimeInMillis()); // _dateDumpFile,
                parametersIn[3] = "Windows 7 x86"; //_operatingSystemsDumpFile,
                parametersIn[4] = this._currentHashType; //_hashTypeDump,
                parametersIn[5] = this._currentHash; //_hashDump,
                parametersIn[6] = ""; //_hostName,
                parametersIn[7] = ""; //_originPlace
                respuesta = model.insert(query, parametersIn, null);

                log = "Creación de Base de Datos finalizada.";
                this.notifyObservers(log);
            } else {
                respuesta = -1;
            }
        } catch (Exception ex) {
            respuesta = -1;
            ex.printStackTrace();
        }

        return respuesta;
    }

    @Override
    public String getScript(String path) {
        StringBuilder sb = null;
        String linea = null;

        try {
            linea = "";

            File f = new File(path);

            if (f.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(path));

                sb = new StringBuilder("");
                linea = reader.readLine();

                while (linea != null) {
                    sb.append(linea + " \n");
                    linea = reader.readLine();
                }
            } else {
                System.out.println("No existe el archivo: " + path);
            }
        } catch (Exception ex) {
            this.notifyObservers(ex);
        }

        return sb!= null ? sb.toString() : null;
    }

    @Override
    public int persist(Entity entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int persistDumpFormatInformation(DumpFormat dumpFormat) {
        int respuesta = 0;

        try {
            if (!this.isDataPresent(DumpFormat._TAG)) {
                this._mapperDumpFormat = this.getMapperDumpFormat(dumpFormat);
                this._mapperDumpFormat.persistDumpFormatInformation(dumpFormat);
            }
        } catch (Exception ex) {
            respuesta = -1;
        }
        return respuesta;
    }

    @Override
    public int persistMetadata(Entity entity, String tag, String status) {
        int respuesta = 0;

        try {
            String log = "Persistiendo metadatos...";
            this.notifyObservers(log);

            String query = "CALL SP_METADATOS_ENTITIES_INSERT (?, ?, ?, ?)";

            Object[] parametersIn = new Object[4];
            parametersIn[0] = entity.getTag();
            parametersIn[1] = tag;
            Calendar calendar = Calendar.getInstance();
            parametersIn[2] = new Timestamp(calendar.getTimeInMillis());
            String parseSatus = "1";
            parametersIn[3] = parseSatus;
            respuesta = this._model.insert(query, parametersIn, null);
            log = "Metadatos persistidos.";
            this.notifyObservers(log);
        } catch (Exception ex) {
            ex.printStackTrace();
            respuesta = -1;
        }
        return respuesta;
    }

}

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
package persistence.database;

/**
 *
 * @author Gonzalo
 * @description defines methods to create database structure and get mapper 
 * with methods to get and/or persist information
 */
public interface IServiceDatabase {

    public abstract MapperFactory getMapperFact();
    public abstract boolean isDataBaseCreated(String databaseName);
    public abstract int createStructure();
    public abstract MapperFactory getMapperFactoryByVersion(String version, String databaseName);

}

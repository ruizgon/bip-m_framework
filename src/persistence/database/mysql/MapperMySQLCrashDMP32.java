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

import dump.formats.CrashDump32;
import dump.formats.DumpFormat;
import dump.formats.HeaderCrashDump32;
import dump.formats.PhysicalMemoryDescriptor32;
import dump.formats.Run32;
import entities.Entity;
import java.util.Iterator;
import java.util.Map.Entry;
import persistence.database.MapperDumpFormat;
import persistence.database.mysql.MySQLModel;
import system.utils.Conversor;

/**
 *
 * @author Gonzalo
 */
public class MapperMySQLCrashDMP32 extends MapperDumpFormat {

    @Override
    public int persistDumpFormatInformation(DumpFormat dumpFormat) {
        int respuesta = 0;

        try {
            MySQLModel model = MySQLModel.getInstane();
            if (dumpFormat instanceof CrashDump32) {
                CrashDump32 crashDMP = (CrashDump32) dumpFormat;
                HeaderCrashDump32 header = crashDMP.getHeader();
                /**
                 * Mapea el contenido del header para persistirlo en la bd
                 */
                Object[] parametersIn = new Object[29];
                String query = "CALL SP_HEADER_CRASH_DUMP_INSERT (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                parametersIn[0] = header.getItems().get("SIGNATURE").getContent(); //signature
                parametersIn[1] = header.getItems().get("VALID_DUMP").getContent(); // validDump
                parametersIn[2] = header.getItems().get("MAJOR_VERSION").getContent(); // majorVersion
                parametersIn[3] = header.getItems().get("MINOR_VERSION").getContent(); // minorVersion
                parametersIn[4] = header.getItems().get("DIRECTORY_TABLE_BASE").getContent().toString(); // directoryTableBase
                parametersIn[5] = header.getItems().get("PFN_DATA_BASE").getContent(); // pfnDataBase
                parametersIn[6] = header.getItems().get("PS_LOADED_MODULE_LIST").getContent().toString(); // psLoadedModuleList
                parametersIn[7] = header.getItems().get("PS_ACTIVE_PROCESS_HEAD").getContent().toString(); // psActiveProcessHead
                parametersIn[8] = header.getItems().get("MACHINE_IMAGE_TYPE").getContent(); // machineImageType
                parametersIn[9] = header.getItems().get("NUMBER_PROCESSORS").getContent(); // numberProcessors
                parametersIn[10] = header.getItems().get("BUG_CHECK_CODE").getContent();  // bugCheckCode
                parametersIn[11] = header.getItems().get("BUG_CHECK_CODE_PARAMETER").getContent(); // bugCheckCodeParameter
                parametersIn[12] = header.getItems().get("VERSION_USER").getContent(); // versionUser
                parametersIn[13] = header.getItems().get("PAE_ENABLED").getContent(); // paeEnabled
                parametersIn[14] = header.getItems().get("KD_SECONDARY_VERSION").getContent(); // kdSecondaryVersion
                parametersIn[15] = header.getItems().get("VERSION_USER2").getContent(); // versionUser2
                parametersIn[16] = header.getItems().get("KD_DEBUGGER_DATA_BLOCK").getContent(); // kdDebuggerDataBlock
                parametersIn[17] = header.getItems().get("CONTEXT_RECORD").getContent(); // contextRecord
                parametersIn[18] = header.getItems().get("COMMENT").getContent(); // comment
                parametersIn[19] = header.getItems().get("DUMP_TYPE").getContent(); // dumpType
                parametersIn[20] = header.getItems().get("MINI_DUMP_FIELDS").getContent(); // miniDumpFields
                parametersIn[21] = header.getItems().get("SECONDARY_DATA_STATE").getContent(); // secondaryDataState
                parametersIn[22] = header.getItems().get("PRODUCT_TYPE").getContent(); // productType
                parametersIn[23] = header.getItems().get("SUITE_MASK").getContent(); // suiteMask
                parametersIn[24] = header.getItems().get("WRITER_STATUS").getContent(); // writerStatus
                parametersIn[25] = header.getItems().get("REQUIRED_DUMPSPACE").getContent(); // requiredDumpSpace
                parametersIn[26] = header.getItems().get("SYSTEM_UP_TIME").getContent(); // systemUpTime
                parametersIn[27] = header.getItems().get("SYSTEM_TIME").getContent(); // systemTime
                parametersIn[28] = header.getItems().get("RESERVED3").getContent(); // reserved3

                respuesta = model.insert(query, parametersIn, null);

                /**
                 * Se deben mapear el contenido de PhysycalMemoryDescriptor
                 */
                PhysicalMemoryDescriptor32 physycalMemoryDescrip = (PhysicalMemoryDescriptor32) header.getItems().get("PHYSICAL_MEMORY_BLOCK_BUFFER").getContent();
                parametersIn = new Object[3];
                query = "CALL SP_PHYSICAL_MEMORY_DESCRIPTOR_INSERT (?, ?, ?)";
                parametersIn[0] = PhysicalMemoryDescriptor32._POSITION + PhysicalMemoryDescriptor32._START_OFFSET_RUNS;
                parametersIn[1] = physycalMemoryDescrip.getNumberOfPages();
                parametersIn[2] = physycalMemoryDescrip.getNumberOfRuns();
                respuesta = model.insert(query, parametersIn, null);
                
                /**
                 * Se deben mapear los runs de PhysycalMemoryDescriptor
                 */
                Iterator<Entry<Integer, Run32>> iterat = physycalMemoryDescrip.getRuns().entrySet().iterator();
                while (iterat.hasNext()) {
                    Entry<Integer, Run32> e = (Entry<Integer, Run32>) iterat.next();
                    Run32 run = (Run32) e.getValue();
                    parametersIn = new Object[3];
                    query = "CALL SP_RUNS_INSERT (?, ?, ?)";
                    parametersIn[0] = run.getFileOffset();
                    parametersIn[1] = run.getStartAddressHex();
                    parametersIn[2] = run.getLength();
                    respuesta = model.insert(query, parametersIn, null);
                }
            }
        } catch (Exception ex) {
            respuesta = -1;
            ex.printStackTrace();
        }

        return respuesta;
    }

    @Override
    public String getScript(String path) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int persist(Entity entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

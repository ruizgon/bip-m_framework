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
package dump.parsers.process;

import dump.formats.CrashDump64;
import dump.ooss.Windows764OS;
import entities.Entity;
import entities.EntityAttribute;
import entities.EntityListWin64;
import entities.process.EntityKPROCESS;
import entities.process.EntityWin764KPROCESS;
import entities.process.EntityPEB;
import entities.process.EntityPebLdrData;
import entities.process.EntityProcess;
import entities.process.EntityWindows764Process;
import entities.translation.EntityAddressSpaceWin64;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import system.utils.Conversor;
import system.utils.DateManager;
import translation.TranslatorWin64;

/**
 *
 * @author Gonzalo
 */
public class ParserWin764ActiveProcessCrashDMP extends ParserWin64Process {

    public ParserWin764ActiveProcessCrashDMP(CrashDump64 _dumpFormat, Windows764OS _os) {
        super(_dumpFormat, _os);

        /**
         * Registra observadores
         */
        this.addObserver(output.OutputManagerDump.getInstance());
        this.addObserver(output.OutputManagerConsole.getInstance());
    }

    public ParserWin764ActiveProcessCrashDMP(CrashDump64 _dumpFormat, Windows764OS _os, TranslatorWin64 _translator) {
        super(_dumpFormat, _os, _translator);

        /**
         * Registra observadores
         */
        this.addObserver(output.OutputManagerDump.getInstance());
        this.addObserver(output.OutputManagerConsole.getInstance());
    }

    @Override
    public void parse(Object[] params) {
        this.setStatus("Iniciado");
        this.setStatusDetail("Proceso de obtención de procesos activos Windows 7 x64.");
        String dateString = DateManager.getActualDateWithISOFormat();
        StringBuilder log = new StringBuilder();
        log.append(this.getStatus() + " " + this.getStatusDetail() + " " + dateString);

        this.notifyObservers(this.getStatus());
        this.notifyObservers(this.getStatusDetail());
        this.notifyObservers("Nombre    InheritedFromPID    PID    Create Time    Exit Time    State    PA    FileOffset");
        this.notifyObservers("-------------------------------------------------------------------------------------------");

        ConcurrentHashMap<Object, Entity> entities = null;

        try {
            entities = new ConcurrentHashMap<Object, Entity>();

            CrashDump64 crashDump = (CrashDump64) this.getDumpFormat();

            /**
             *
             * Obtiene dirección virtual de PsActiveProcessHead desde el
             * encabezado del dump Es el puntero al primer elemento de la lista
             * doblemente enlazada de procesos activos en memoria
             */
            String psActiveProcessHeadString = (String) crashDump.getHeader().getItems().get("PS_ACTIVE_PROCESS_HEAD").getContent();

            /**
             * El traductor calcula dirección física
             */
            BigInteger psActiveProcessHeadPhysicalAddress = this.getTranslator().calculatePhysicalAddress(psActiveProcessHeadString);

            long psActiveProcessHeadAbsoluteOffset = this.getDumpFormat().getFileOffset(psActiveProcessHeadPhysicalAddress);

            /**
             * Obtiene EntityList
             */
            EntityListWin64 psActiveProcessHead = (EntityListWin64) this.getListContent(psActiveProcessHeadAbsoluteOffset);
            //String h = Conversor.bigIntToHexString(psActiveProcessHead.getfLinkBig());
            EntityAddressSpaceWin64 aS = (EntityAddressSpaceWin64) this.getTranslator().obtainAddressSpace(psActiveProcessHead.getfLinkBig());
            String p = Conversor.bigIntToHexString(aS.getPhysicalAddress());
            long physicalAddressFileOffsetAS = this.getDumpFormat().getFileOffset(aS.getPhysicalAddress());
            aS.setOffsetInFile(physicalAddressFileOffsetAS);
            aS.setOffsetInFileHex(Conversor.longToHexString(physicalAddressFileOffsetAS));

            long initialAbsoluteOffset = 0;
            long absoluteOffset = 0;
            String content = null;
            int indexMap = 0;
            if (psActiveProcessHead != null) {
                boolean firstReference = true;

                BigInteger nextPosition = getNextEntityAddress(psActiveProcessHead, EntityWindows764Process._ENTITY_LIST_OFFSET);
                long nextOffset = this.getDumpFormat().getFileOffset(nextPosition);
                long nextOffsetComparator = psActiveProcessHeadAbsoluteOffset - 0x188; //0x188: OFFSET AL INICIO DE LA ESTRUCTURA
                EntityListWin64 activeProcessLinks = psActiveProcessHead;

                //Inicializa índice para key del HashMap entities
                int ind = 0;
                while (activeProcessLinks != null) {
                    EntityWindows764Process entityProcess = new EntityWindows764Process();

                    /**
                     * Busca la estructura _LIST_ENTRY
                     */
                    if (!firstReference && nextOffset == nextOffsetComparator) {
                        break;
                    }
                    long absoluteAttributeOffset = nextOffset + entityProcess.getAttributes().get("ActiveProcessLinks").getPosition();
                    activeProcessLinks = (EntityListWin64) getListContent(absoluteAttributeOffset);
                    String activeProcessLinksHex = Conversor.bigIntToHexString(activeProcessLinks.getfLinkBig());
                    entityProcess.getAttributes().get("ActiveProcessLinks").setContent(activeProcessLinks);

                    initialAbsoluteOffset = nextOffset;

                    Iterator<Entry<String, EntityAttribute>> i = entityProcess.getAttributes().entrySet().iterator();
                    while (i.hasNext()) {
                        absoluteOffset = initialAbsoluteOffset;
                        /**
                         * Por cada item, le solicta a randomFileSeeker que
                         * obtenga el contenido
                         */

                        Entry<String, EntityAttribute> e = (Entry<String, EntityAttribute>) i.next();

                        this.getEntryAttributeContent(absoluteOffset, e);
                    }
                    entityProcess.setStateLabel("Active");

                    /**
                     * A partir de acá, los atributos de tipo estructura
                     * compleja y otros atributos
                     */
                    entityProcess.setaS(aS);
                    /**
                     * Calcula nuevo aS para próximo paso
                     */
                    aS = (EntityAddressSpaceWin64) this.getTranslator().obtainAddressSpace(activeProcessLinks.getfLinkBig());
                    String physicalAddresshex = Conversor.bigIntToHexString(aS.getPhysicalAddress());
                    physicalAddressFileOffsetAS = this.getDumpFormat().getFileOffset(aS.getPhysicalAddress());
                    aS.setOffsetInFile(physicalAddressFileOffsetAS);
                    aS.setOffsetInFileHex(Conversor.longToHexString(physicalAddressFileOffsetAS));

                    /**
                     * Agrega entidad al HashMap
                     */
                    entityProcess.setProcessID(Conversor.hexToInt(entityProcess.getAttributes().get("UniqueProcessId").getContent().toString()));
                    
                    /**
                     * Agrega entidad al HashMap
                     */
                    entities.put(indexMap++, entityProcess);

                    /**
                     * Notifica observadores
                     */
                    this.notifyObservers(entityProcess);

                    nextOffset = this.getDumpFormat().getFileOffset(getNextEntityAddress(activeProcessLinks, EntityWindows764Process._ENTITY_LIST_OFFSET));

                    /**
                     * Ya no es a referencia al primer nodo
                     */
                    if (firstReference) {
                        firstReference = false;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (entities != null) {
            this.notifyObservers("Procesos activos encontrados = " + entities.size() + ". Procesos ocultos = " + ".");
        } else {
            this.notifyObservers("ERROR. Procesos activos encontrados = 0. Procesos ocultos = 0" + ".");
        }

        this.setEntities(entities);
    }

    @Override
    public void parse(long offset, Object[] params) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void parse(long initialOffset, long maxOffset, Object[] params) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Entity parseEntityByOffset(long offset, Entity entity, Map.Entry<String, EntityAttribute> attribute) {
        EntityWindows764Process entityProcess = null;
        try {
            long initialAbsoluteOffset = offset;
            long absoluteOffset = 0;

            entityProcess = new EntityWindows764Process();

            Iterator<Entry<String, EntityAttribute>> i = entityProcess.getAttributes().entrySet().iterator();
            while (i.hasNext()) {
                absoluteOffset = initialAbsoluteOffset;
                /**
                 * Por cada item, le solicta a randomFileSeeker que obtenga el
                 * contenido
                 */

                Entry<String, EntityAttribute> e = (Entry<String, EntityAttribute>) i.next();

                this.getEntryAttributeContent(absoluteOffset, e);
            }
            entityProcess.setStateLabel("Active");

            /**
             * A partir de acá, los atributos de tipo estructura compleja y
             * otros atributos
             */
            /*EntityListWin64 psActiveProcessHead = (EntityListWin64) this.getListContent(offset);
             EntityAddressSpaceWin64 aS = (EntityAddressSpaceWin64) this.getTranslator().obtainAddressSpace(BigInteger.valueOf(psActiveProcessHead.getfLink()));
             entityProcess.setaS(aS);*/
            EntityAddressSpaceWin64 aS = new EntityAddressSpaceWin64();
            aS.setOffsetInFile(offset);
            aS.setOffsetInFileHex(Conversor.longToHexString(offset));
            BigInteger pA = this.getDumpFormat().getPhysicalAddresByOffset(offset);
            aS.setPhysicalAddress(pA);
            aS.setPhysicalAddressHex(Conversor.bigIntToHexString(pA));

            if (entityProcess.getAttributes().get("UniqueProcessId").getContent() != null) {
                entityProcess.setProcessID(Conversor.hexToLong(entityProcess.getAttributes().get("UniqueProcessId").getContent().toString()));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return entityProcess;
    }

    @Override
    public void run() {
        this.parse(null);
    }

    @Override
    public Object call() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isValidStructure(java.lang.Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Entity getEntityByAttributeValue(Entity entity, String attribute, Object content) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map.Entry<String, EntityAttribute> setParserAttribute(Map.Entry<String, EntityAttribute> entry) {
        if (entry.getValue().getEntity() instanceof EntityWin764KPROCESS) {
            if (entry.getValue().getEntity().getParser() == null) {
                entry.getValue().getEntity().setParser(new ParserWin764ActiveProcessCrashDMP((CrashDump64) this.getDumpFormat(), (Windows764OS) this.getOs(), (TranslatorWin64) this.getTranslator()));
            }
        } else {
            entry.getValue().getEntity().setParser(this);
        }

        return entry;
    }

    @Override
    public void parse(Entity entity, Object[] params) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityKPROCESS obtainKPROCESSContent(long offset, Entry<String, EntityAttribute> e) {
        EntityWin764KPROCESS kProcess = null;

        try {
            kProcess = new EntityWin764KPROCESS();
            long initialAbsoluteOffsetProcess = offset;
            long absoluteOffsetProcess = 0;
            Iterator<Map.Entry<String, EntityAttribute>> i = kProcess.getAttributes().entrySet().iterator();
            while (i.hasNext()) {
                absoluteOffsetProcess = initialAbsoluteOffsetProcess;
                /**
                 * Por cada item, le solicta a randomFileSeeker que obtenga el
                 * contenido
                 */

                Map.Entry<String, EntityAttribute> entry = (Map.Entry<String, EntityAttribute>) i.next();

                this.getEntryAttributeContent(absoluteOffsetProcess, entry);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return kProcess;
    }

    @Override
    public EntityPEB getPEBByProcess(EntityProcess process) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityPEB getPEBByOffset(long offset) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityPebLdrData getPEBLdrDataByOffset(long offset) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean validateAttributeContent(Entry<String, EntityAttribute> attribute) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

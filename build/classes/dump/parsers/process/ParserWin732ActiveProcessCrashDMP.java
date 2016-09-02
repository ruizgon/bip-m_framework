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

import dump.formats.CrashDump32;
import dump.ooss.Windows732OS;
import entities.Entity;
import entities.EntityAttribute;
import entities.EntityListWin32;
import entities.process.EntityKPROCESS;
import entities.process.EntityWin732KPROCESS;
import entities.process.EntityPEB;
import entities.process.EntityWindows732PEB;
import entities.process.EntityPebLdrData;
import entities.process.EntityPebLdrDataWindows732;
import entities.process.EntityProcess;
import entities.process.EntityWindows732Process;
import entities.process.EntityWindows732Thread;
import entities.translation.EntityAddressSpaceWin32;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import system.utils.Conversor;
import system.utils.DateManager;
import translation.TranslatorWin32;

/**
 *
 * @author Gonzalo
 */
public class ParserWin732ActiveProcessCrashDMP extends ParserWin32Process {

    private Windows732OS _os;

    public ParserWin732ActiveProcessCrashDMP(CrashDump32 _dumpFormat, Windows732OS _os) {
        super(_dumpFormat, _os);

        /**
         * Registra observadores
         */
        this.addObserver(output.OutputManagerException.getInstance());
        this.addObserver(output.OutputManagerDump.getInstance());
        this.addObserver(output.OutputManagerConsole.getInstance());
    }

    public ParserWin732ActiveProcessCrashDMP(CrashDump32 _dumpFormat, Windows732OS _os, TranslatorWin32 _translator) {
        super(_dumpFormat, _os, _translator);

        /**
         * Registra observadores
         */
        this.addObserver(output.OutputManagerDump.getInstance());
        this.addObserver(output.OutputManagerConsole.getInstance());
    }

    public Windows732OS getOs() {
        return _os;
    }

    public void setOs(Windows732OS _os) {
        this._os = _os;
    }

    @Override
    public void parse(Object[] params) {

        Calendar cal = new GregorianCalendar();
        this.setStatus("Iniciado");
        this.setStatusDetail("Proceso de parseo de dump y obtención de procesos activos Windows 7 x86: " + cal.getTime().toString() + ".");
        String dateString = DateManager.getActualDateWithISOFormat();
        StringBuilder log = new StringBuilder();
        log.append(this.getStatus() + " " + this.getStatusDetail() + " " + dateString);

        this.notifyObservers(this.getStatus());
        this.notifyObservers(this.getStatusDetail());
        this.notifyObservers("Nombre    InheritedFromPID    PID    Create Time    Exit Time    State    VA    PA    FileOffset");
        this.notifyObservers("-------------------------------------------------------------------------------------------------");

        ConcurrentHashMap<Object, Entity> entities = null;

        try {
            entities = new ConcurrentHashMap<Object, Entity>();

            CrashDump32 crashDump = (CrashDump32) this.getDumpFormat();

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
            EntityListWin32 psActiveProcessHead = (EntityListWin32) this.getListContent(psActiveProcessHeadAbsoluteOffset);
            EntityAddressSpaceWin32 aS = (EntityAddressSpaceWin32) this.getTranslator().obtainAddressSpace(BigInteger.valueOf(psActiveProcessHead.getfLink()));
            long physicalAddressFileOffsetAS = this.getDumpFormat().getFileOffset(aS.getPhysicalAddress());
            aS.setOffsetInFile(physicalAddressFileOffsetAS);
            aS.setOffsetInFileHex(Conversor.longToHexString(physicalAddressFileOffsetAS));

            long initialAbsoluteOffset = 0;
            long absoluteOffset = 0;
            String content = null;
            int indexMap = 0;
            if (psActiveProcessHead != null) {
                boolean firstReference = true;

                BigInteger nextPosition = getNextEntityAddress(psActiveProcessHead, EntityWindows732Process._ENTITY_LIST_OFFSET);
                long nextOffset = this.getDumpFormat().getFileOffset(nextPosition);
                long nextOffsetComparator = psActiveProcessHeadAbsoluteOffset - 0x0B8; //0x0B8: OFFSET AL INICIO DE LA ESTRUCTURA
                EntityListWin32 activeProcessLinks = psActiveProcessHead;

                //Inicializa índice para key del HashMap entities
                int ind = 0;
                while (activeProcessLinks != null) {
                    EntityWindows732Process entityProcess = new EntityWindows732Process();

                    /**
                     * Busca la estructura _LIST_ENTRY
                     */
                    if (!firstReference && nextOffset == nextOffsetComparator) {
                        break;
                    }
                    long absoluteAttributeOffset = nextOffset + entityProcess.getAttributes().get("ActiveProcessLinks").getPosition();
                    activeProcessLinks = (EntityListWin32) getListContent(absoluteAttributeOffset);
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
                    aS = (EntityAddressSpaceWin32) this.getTranslator().obtainAddressSpace(BigInteger.valueOf(activeProcessLinks.getfLink()));
                    physicalAddressFileOffsetAS = this.getDumpFormat().getFileOffset(aS.getPhysicalAddress());
                    aS.setOffsetInFile(physicalAddressFileOffsetAS);
                    aS.setOffsetInFileHex(Conversor.longToHexString(physicalAddressFileOffsetAS));

                    entityProcess.setProcessID(Conversor.hexToInt(entityProcess.getAttributes().get("UniqueProcessId").getContent().toString()));

                    /**
                     * Agrega entidad al HashMap
                     */
                    entities.put(indexMap++, entityProcess);

                    /**
                     * Notifica observadores
                     */
                    this.notifyObservers(entityProcess);

                    nextOffset = this.getDumpFormat().getFileOffset(getNextEntityAddress(activeProcessLinks, EntityWindows732Process._ENTITY_LIST_OFFSET));

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
            this.notifyObservers("Procesos activos encontrados = " + entities.size() + ". Procesos ocultos = . " + cal.getTime().toString() + ".");
        } else {
            this.notifyObservers("ERROR. Procesos activos encontrados = 0. Procesos ocultos = 0" + ".");
        }

        this.setEntities(entities);
    }

    @Override
    public synchronized int countObservers() {
        return super.countObservers();
    }

    @Override
    public synchronized boolean hasChanged() {
        return super.hasChanged();
    }

    @Override
    protected synchronized void clearChanged() {
        super.clearChanged();
    }

    @Override
    protected synchronized void setChanged() {
        super.setChanged();
    }

    @Override
    public synchronized void deleteObservers() {
        super.deleteObservers();
    }

    @Override
    public void notifyObservers(Object o) {
        super.notifyObservers(o);
    }

    @Override
    public void notifyObservers() {
        super.notifyObservers();
    }

    @Override
    public void parse(long initialOffset, long maxOffset, Object[] params) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Entity parseEntityByOffset(long offset, Entity entity, Map.Entry<String, EntityAttribute> attribute) {
        EntityWindows732Process entityProcess = null;
        try {
            long initialAbsoluteOffset = offset;
            long absoluteOffset = 0;

            entityProcess = new EntityWindows732Process();

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
            /*EntityListWin32 psActiveProcessHead = (EntityListWin32) this.getListContent(offset);
            EntityAddressSpaceWin32 aS = (EntityAddressSpaceWin32) this.getTranslator().obtainAddressSpace(BigInteger.valueOf(psActiveProcessHead.getfLink()));
            entityProcess.setaS(aS);*/
            EntityAddressSpaceWin32 aS = new EntityAddressSpaceWin32();
            aS.setOffsetInFile(offset);
            aS.setOffsetInFileHex(Conversor.longToHexString(offset));
            BigInteger pA = this.getDumpFormat().getPhysicalAddresByOffset(offset);
            aS.setPhysicalAddress(pA);
            aS.setPhysicalAddressHex(Conversor.bigIntToHexString(pA));
            entityProcess.setaS(aS);

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
    public void parse(long offset, Object[] params) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public Map.Entry<String, EntityAttribute> setParserAttribute(Entry<String, EntityAttribute> entry) {
        if (entry.getValue().getEntity() instanceof EntityWin732KPROCESS) {
            if (entry.getValue().getEntity().getParser() == null) {
                entry.getValue().getEntity().setParser(new ParserWin732ActiveProcessCrashDMP((CrashDump32) this.getDumpFormat(), (Windows732OS) this.getOs(), (TranslatorWin32) this.getTranslator()));
            }
        } else {
            entry.getValue().getEntity().setParser(this);
        }

        return entry;
    }

    @Override
    public void parse(Entity entity, Object[] params) {
        EntityWindows732Thread thread = null;
        EntityWindows732Process process = null;

        try {
            if (params[0] != null) {
                if (params[0] instanceof EntityWindows732Process) {
                    process = (EntityWindows732Process) params[0];
                }
            }

            if (entity instanceof EntityWindows732Thread && process != null) {

                this.setStatus("Iniciado");
                this.setStatusDetail("Proceso de obtención de threads del proceso PID:" + process.getProcessID());
                String dateString = DateManager.getActualDateWithISOFormat();
                StringBuilder log = new StringBuilder();
                log.append(this.getStatus() + " " + this.getStatusDetail() + " " + dateString);

                this.notifyObservers(this.getStatus());
                this.notifyObservers(this.getStatusDetail());
                this.notifyObservers("Nombre                 InheritedFromPID     PID           Create Time                      Exit Time              State           PhysicalAddress       FileOffset");
                this.notifyObservers("------------------------------------------------------------------------------------------------------------------------------------------------------------------");

                ConcurrentHashMap<Object, Entity> entities = null;

                thread = (EntityWindows732Thread) entity;
                long processOffset = process.getaS().getOffsetInFile();
                /**
                 * Offset donde se encuentra el puntero al primer thread del
                 * proceso en la lista doblemente enlazada
                 */
                long threadListHeadOffset = process.getAttributes().get("ThreadListHead").getOffset();

                long threadListHeadAbsoluteOffset = processOffset + threadListHeadOffset;
                EntityListWin32 threadListHead = (EntityListWin32) this.getListContent(threadListHeadAbsoluteOffset);
                EntityAddressSpaceWin32 aS = (EntityAddressSpaceWin32) this.getTranslator().obtainAddressSpace(BigInteger.valueOf(threadListHead.getfLink()));
                long physicalAddressFileOffsetAS = this.getDumpFormat().getFileOffset(aS.getPhysicalAddress());
                aS.setOffsetInFile(physicalAddressFileOffsetAS);
                aS.setOffsetInFileHex(Conversor.longToHexString(physicalAddressFileOffsetAS));

                long initialAbsoluteOffsetThread = 0;
                long absoluteOffsetThread = 0;
                if (threadListHead != null) {
                    boolean firstReference = true;

                    BigInteger nextPosition = getNextEntityAddress(threadListHead, EntityWindows732Thread._ENTITY_LIST_OFFSET);
                    long nextOffset = this.getDumpFormat().getFileOffset(nextPosition);
                    long nextOffsetComparator = threadListHeadAbsoluteOffset - 0x268; //0x268: OFFSET AL INICIO DE LA ESTRUCTURA
                    EntityListWin32 activeThreadLinks = threadListHead;

                    //Inicializa índice para key del HashMap entities
                    int ind = 0;
                    int indexMap = 0;
                    while (activeThreadLinks != null) {
                        EntityWindows732Thread entityThread = new EntityWindows732Thread();

                        /**
                         * Busca la estructura _LIST_ENTRY
                         */
                        if (!firstReference && nextOffset == nextOffsetComparator) {
                            break;
                        }
                        long absoluteAttributeOffset = nextOffset + entityThread.getAttributes().get("ThreadListEntry").getPosition();
                        activeThreadLinks = (EntityListWin32) getListContent(absoluteAttributeOffset);
                        entityThread.getAttributes().get("ThreadListEntry").setContent(activeThreadLinks);

                        initialAbsoluteOffsetThread = nextOffset;

                        Iterator<Map.Entry<String, EntityAttribute>> i = thread.getAttributes().entrySet().iterator();
                        while (i.hasNext()) {
                            absoluteOffsetThread = initialAbsoluteOffsetThread;
                            /**
                             * Por cada item, le solicta a randomFileSeeker que
                             * obtenga el contenido
                             */

                            Map.Entry<String, EntityAttribute> entry = (Map.Entry<String, EntityAttribute>) i.next();

                            this.getEntryAttributeContent(absoluteOffsetThread, entry);
                        }

                        /**
                         * A partir de acá, los atributos de tipo estructura
                         * compleja y otros atributos
                         */
                        entityThread.setaS(aS);
                        /**
                         * Calcula nuevo aS para próximo paso
                         */
                        aS = (EntityAddressSpaceWin32) this.getTranslator().obtainAddressSpace(BigInteger.valueOf(activeThreadLinks.getfLink()));
                        physicalAddressFileOffsetAS = this.getDumpFormat().getFileOffset(aS.getPhysicalAddress());
                        aS.setOffsetInFile(physicalAddressFileOffsetAS);
                        aS.setOffsetInFileHex(Conversor.longToHexString(physicalAddressFileOffsetAS));

                        /**
                         * Agrega entidad al HashMap
                         */
                        entityThread.setProcessID(Conversor.hexToInt(entityThread.getAttributes().get("UniqueProcessId").getContent().toString()));
                        entities.put(indexMap++, entityThread);

                        /**
                         * Notifica observadores
                         */
                        this.notifyObservers(entityThread);

                        nextOffset = this.getDumpFormat().getFileOffset(getNextEntityAddress(activeThreadLinks, EntityWindows732Thread._ENTITY_LIST_OFFSET));

                        /**
                         * Ya no es a referencia al primer nodo
                         */
                        if (firstReference) {
                            firstReference = false;
                        }
                    }
                }
            }

        } catch (Exception ex) {

        }
    }

    @Override
    public EntityPEB getPEBByProcess(EntityProcess process) {

        Iterator<Map.Entry<String, EntityAttribute>> iProcess = process.getAttributes().entrySet().iterator();
        long initialAbsoluteOffsetProcess = process.getaS().getOffsetInFile() - 0x0B8;
        long absoluteOffsetProcess = 0;
        while (iProcess.hasNext()) {
            absoluteOffsetProcess = initialAbsoluteOffsetProcess;
            /**
             * Por cada item, le solicta a randomFileSeeker que obtenga el
             * contenido
             */

            Map.Entry<String, EntityAttribute> entryProcess = (Map.Entry<String, EntityAttribute>) iProcess.next();
            if (entryProcess.getKey().equals("PEB")) {
                entryProcess.getValue().setEnabledParse(true);
                this.getEntryAttributeContent(absoluteOffsetProcess, entryProcess);
            }
        }

        return (EntityWindows732PEB) process.getAttributes().get("PEB").getContent();
    }

    @Override
    public EntityPEB getPEBByOffset(long offset) {
        EntityWindows732PEB peb = null;

        try {
            peb = new EntityWindows732PEB();

            long initialAbsoluteOffsetProcess = offset;
            long absoluteOffsetProcess = 0;
            Iterator<Map.Entry<String, EntityAttribute>> i = peb.getAttributes().entrySet().iterator();
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

        return peb;
    }

    @Override
    public EntityPebLdrData getPEBLdrDataByOffset(long offset) {
        EntityPebLdrDataWindows732 pebLdrData = null;

        try {
            pebLdrData = new EntityPebLdrDataWindows732();
            long initialAbsoluteOffsetProcess = offset;
            long absoluteOffsetProcess = 0;
            Iterator<Map.Entry<String, EntityAttribute>> i = pebLdrData.getAttributes().entrySet().iterator();
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

        return pebLdrData;
    }

    @Override
    public EntityKPROCESS obtainKPROCESSContent(long offset, Map.Entry<String, EntityAttribute> e) {
        EntityWin732KPROCESS kProcess = null;

        try {
            kProcess = new EntityWin732KPROCESS();
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
    public boolean validateAttributeContent(Entry<String, EntityAttribute> attribute) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

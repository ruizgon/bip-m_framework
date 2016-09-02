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
import dump.formats.DumpFormat;
import dump.formats.PhysicalMemoryDescriptor32;
import dump.formats.Run32;
import dump.ooss.OperatingSystemStructure;
import dump.ooss.Windows732OS;
import dump.parsers.utils.pools.PoolManagerWin32;
import dump.parsers.utils.tags.TagManagerWin32;
import entities.Entity;
import entities.EntityAttribute;
import entities.EntityListWin32;
import entities.process.EntityKPROCESS;
import entities.process.EntityWin732KPROCESS;
import entities.process.EntityPEB;
import entities.process.EntityPebLdrData;
import entities.process.EntityProcess;
import entities.process.EntityWindows732Process;
import entities.translation.EntityAddressSpaceWin32;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import system.utils.BinaryOperator;
import system.utils.Conversor;
import system.utils.DataManager;
import system.utils.DateManager;
import translation.Translator;
import translation.TranslatorWin32;

/**
 *
 * @author Gonzalo
 */
public class ParserWin732HiddenProcessCrashDMP extends ParserWin32Process {

    private int _CANT_PAGES = 8;
    private TagManagerWin32 _tagManager;
    private Map<Long, EntityWindows732Process> _activeProcesses;

    public ParserWin732HiddenProcessCrashDMP(DumpFormat _dumpFormat, OperatingSystemStructure _os, Map<Long, EntityWindows732Process> _activeProcesses, ConcurrentHashMap<Object, Entity> _entities) {
        super(_dumpFormat, _os);
        this._tagManager = new TagManagerWin32();
        this._activeProcesses = _activeProcesses;
        this.setEntities(_entities);

        /**
         * Registra observadores
         */
        this.addObserver(output.OutputManagerDump.getInstance());
        this.addObserver(output.OutputManagerConsole.getInstance());
    }

    public ParserWin732HiddenProcessCrashDMP(DumpFormat _dumpFormat, OperatingSystemStructure _os) {
        super(_dumpFormat, _os);
        this._tagManager = new TagManagerWin32();

        /**
         * Registra observadores
         */
        this.addObserver(output.OutputManagerDump.getInstance());
        this.addObserver(output.OutputManagerConsole.getInstance());
    }

    public ParserWin732HiddenProcessCrashDMP(DumpFormat _dumpFormat, OperatingSystemStructure _os, Translator _translator) {
        super(_dumpFormat, _os, _translator);
        this._tagManager = new TagManagerWin32();

        /**
         * Registra observadores
         */
        this.addObserver(output.OutputManagerDump.getInstance());
        this.addObserver(output.OutputManagerConsole.getInstance());
    }

    public ParserWin732HiddenProcessCrashDMP(DumpFormat _dumpFormat, OperatingSystemStructure _os, TranslatorWin32 _translator, Map<Long, EntityWindows732Process> _activeProcesses, ConcurrentHashMap<Object, Entity> _entities, long initialOffset, long maxOffset) {
        super(_dumpFormat, _os, _translator);
        this.setInitialOffset(initialOffset);
        this.setMaxOffset(maxOffset);

        /**
         * Registra observadores
         */
        this.addObserver(output.OutputManagerDump.getInstance());
        this.addObserver(output.OutputManagerConsole.getInstance());

        this._tagManager = new TagManagerWin32();
        this._activeProcesses = _activeProcesses;
        this.setEntities(_entities);
    }

    public TagManagerWin32 getTagManager() {
        return _tagManager;
    }

    public void setTagManager(TagManagerWin32 _tagManager) {
        this._tagManager = _tagManager;
    }

    public Map<Long, EntityWindows732Process> getActiveProcesses() {
        return _activeProcesses;
    }

    public void setActiveProcesses(Map<Long, EntityWindows732Process> _activeProcess) {
        this._activeProcesses = _activeProcess;
    }

    @Override
    public void parse(Object[] params) {
        try {
            Calendar cal = new GregorianCalendar();
            System.out.println("Comenzó búsqueda de procesos ocultos Windows 7 x86: " + cal.getTime().toString());
            obtainActiveProcesses();

            this.setStatusDetail("Proceso de parseo de dump y obtención de procesos ocultos. Buscando...");
            this.notifyObservers(this.getStatus());
            this.notifyObservers(this.getStatusDetail());
            this.notifyObservers("Nombre    InheritedFromPID    PID    Create Time    Exit Time    State    VA    PA    FileOffset");
            this.notifyObservers("-------------------------------------------------------------------------------------------------");

            this.setEntities(new ConcurrentHashMap<Object, Entity>());

            CrashDump32 dumpFormat = (CrashDump32) this.getDumpFormat();
            PhysicalMemoryDescriptor32 physicalMemoryDescriptor32 = (PhysicalMemoryDescriptor32) dumpFormat.getHeader().getItems().get("PHYSICAL_MEMORY_BLOCK_BUFFER").getContent();
            Map<Integer, Run32> runs = physicalMemoryDescriptor32.getRuns();

            long initialOffset = -1;

            int cantRuns = runs.size();
            long maxOffset = -1;

            Map<Integer, Callable<Map<Integer, Entity>>> parsersThread = new HashMap<Integer, Callable<Map<Integer, Entity>>>();

            /*for (int i = 0; i < cantRuns; i++) {

             initialOffset = runs.get(i).getFileOffset();
             maxOffset = runs.get(i).getFileOffset() + runs.get(i).getLength();

             ParserWin732ThreadHiddenProcess parserThread = new ParserWin732HiddenProcessCrashDMP(this.getDumpFormat(), this.getOs(), this.getTranslator(), this._activeProcesses, this.getEntities(), initialOffset, maxOffset);
             parsersThread.put(i, parserThread);

             initialOffset = -1;
             maxOffset = -1;
             }

             Thread[] threads = new Thread[cantRuns];

             Iterator<Entry<Integer, ParserWin732HiddenProcessCrashDMP>> i = parsersThread.entrySet().iterator();
             int j = 0;
             while (i.hasNext()) {
             Entry<Integer, ParserWin732HiddenProcessCrashDMP> e = (Entry<Integer, ParserWin732HiddenProcessCrashDMPv>) i.next();
             Thread t = new Thread(e.getValue());

             threads[j++] = t;

             t.start();
             }

             for (int k = 0; k < threads.length; k++) {
             threads[k].join();
             }

             Iterator<Entry<Integer, ParserWin732ThreadHiddenProcess>> it = parsersThread.entrySet().iterator();

             while (it.hasNext()) {
             Entry<Integer, ParserWin732ThreadHiddenProcess> e = (Entry<Integer, ParserWin732ThreadHiddenProcess>) it.next();
             */
            /**
             * Inserta en la colección
             */
            /*Map<Integer, Entity> entities = e.getValue().getEntities();

             Iterator<Entry<Integer, Entity>> ite = entities.entrySet().iterator();
             while (ite.hasNext()) {
             Entry<Integer, Entity> ee = (Entry<Integer, Entity>) ite.next();
             this.getEntities().put(ee.getKey(), ee.getValue());
             }
             }*/
            //ExecutorService pool = Executors.newFixedThreadPool(cantRuns);
            int cantThreads = cantRuns * 4;
            ScheduledExecutorService pool = Executors.newScheduledThreadPool(cantThreads);

            //Set<Future<Map<Integer, Entity>>> set = new HashSet<Future<Map<Integer, Entity>>>();
            Set<ScheduledFuture<Map<Integer, Entity>>> set = new HashSet<ScheduledFuture<Map<Integer, Entity>>>();
            int delaySeconds = 3;
            for (int i = 0; i < cantRuns; i++) {

                initialOffset = runs.get(i).getFileOffset();
                maxOffset = BigInteger.valueOf(runs.get(i).getFileOffset()).add(runs.get(i).getLength()).longValue();

                long difOffset = maxOffset - initialOffset;

                if (difOffset < 100000000) {
                    Callable<Map<Integer, Entity>> callable = new ParserWin732HiddenProcessCrashDMP(this.getDumpFormat(), this.getOs(), (TranslatorWin32) this.getTranslator(), this._activeProcesses, this.getEntities(), initialOffset, maxOffset);
                    //Future<Map<Integer, Entity>> future = pool.submit(callable);
                    ScheduledFuture<Map<Integer, Entity>> future = pool.schedule(callable, delaySeconds, TimeUnit.SECONDS);
                    set.add(future);
                } else { //Divide el trabajo en 4 hilos
                    Callable<Map<Integer, Entity>> callable2 = new ParserWin732HiddenProcessCrashDMP(this.getDumpFormat(), this.getOs(), (TranslatorWin32) this.getTranslator(), this._activeProcesses, this.getEntities(), initialOffset, maxOffset / 4);
                    ScheduledFuture<Map<Integer, Entity>> future2 = pool.schedule(callable2, delaySeconds, TimeUnit.SECONDS);
                    set.add(future2);

                    Callable<Map<Integer, Entity>> callable3 = new ParserWin732HiddenProcessCrashDMP(this.getDumpFormat(), this.getOs(), (TranslatorWin32) this.getTranslator(), this._activeProcesses, this.getEntities(), maxOffset / 4 + 1, maxOffset / 2);
                    ScheduledFuture<Map<Integer, Entity>> future3 = pool.schedule(callable3, delaySeconds, TimeUnit.SECONDS);
                    set.add(future3);

                    Callable<Map<Integer, Entity>> callable4 = new ParserWin732HiddenProcessCrashDMP(this.getDumpFormat(), this.getOs(), (TranslatorWin32) this.getTranslator(), this._activeProcesses, this.getEntities(), maxOffset / 2 + 1, 3 * maxOffset / 4);
                    ScheduledFuture<Map<Integer, Entity>> future4 = pool.schedule(callable4, delaySeconds, TimeUnit.SECONDS);
                    set.add(future4);

                    Callable<Map<Integer, Entity>> callable5 = new ParserWin732HiddenProcessCrashDMP(this.getDumpFormat(), this.getOs(), (TranslatorWin32) this.getTranslator(), this._activeProcesses, this.getEntities(), 3 * maxOffset / 4 + 1, maxOffset);
                    ScheduledFuture<Map<Integer, Entity>> future5 = pool.schedule(callable5, delaySeconds, TimeUnit.SECONDS);
                    set.add(future5);
                }

                initialOffset = -1;
                maxOffset = -1;
            }

            /*initialOffset = Conversor.hexToLong("DA590000");
             maxOffset = Conversor.hexToLong("DDADBBBB");
             Callable<Map<Integer, Entity>> callable = new ParserWin732HiddenProcessCrashDMP(this.getDumpFormat(), this.getOs(), (TranslatorWin32) this.getTranslator(), this._activeProcesses, this.getEntities(), initialOffset, maxOffset);

             ScheduledFuture<Map<Integer, Entity>> future = pool.schedule(callable, delaySeconds, TimeUnit.SECONDS);
             set.add(future);*/
            int eIndex = 0;
            for (ScheduledFuture<Map<Integer, Entity>> f : set) {
                eIndex = this.getEntities().size();

                Map<Integer, Entity> entities = f.get();

                Iterator<Entry<Integer, Entity>> ite = entities.entrySet().iterator();
                while (ite.hasNext()) {
                    Entry<Integer, Entity> ee = (Entry<Integer, Entity>) ite.next();
                    this.getEntities().putIfAbsent(ee.getKey(), ee.getValue());
                }
            }

            /**
             * Aguarda que finalicen todos los thread del pool
             */
            pool.shutdown();
            try {
                pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException ex) {
                ex.getMessage();
            }
            cal = new GregorianCalendar();
            System.out.println("Finalizó la búsqueda de procesos ocultos " + cal.getTime().toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void obtainActiveProcesses() {

        this.setStatus("Iniciado");
        this.setStatusDetail("Proceso de obtención de procesos activos.");
        String dateString = DateManager.getActualDateWithISOFormat();
        StringBuilder log = new StringBuilder();
        log.append(this.getStatus() + " " + this.getStatusDetail() + " " + dateString);

        this.notifyObservers(this.getStatus());
        this.notifyObservers(this.getStatusDetail());
        this.notifyObservers("Nombre                 InheritedFromPID     PID           Create Time                      Exit Time              State           PhysicalAddress       FileOffset");
        this.notifyObservers("------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        Map<Long, EntityWindows732Process> entities = new HashMap<Long, EntityWindows732Process>();

        try {

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

                    Iterator<Map.Entry<String, EntityAttribute>> i = entityProcess.getAttributes().entrySet().iterator();
                    while (i.hasNext()) {
                        /**
                         * Por cada item, le solicta a randomFileSeeker que
                         * obtenga el contenido
                         */

                        Map.Entry<String, EntityAttribute> e = (Map.Entry<String, EntityAttribute>) i.next();
                        long absoluteOffset = nextOffset;

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
                    entities.put(entityProcess.getProcessID(), entityProcess);

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

        this._activeProcesses = entities;
    }

    @Override
    public void parse(long initialOffset, long maxOffset, Object[] params) {
        try {
            if (initialOffset < 0) {
                initialOffset = this.getInitialOffset();
            }
            if (maxOffset < 0) {
                maxOffset = this.getMaxOffset();
            }

            Windows732OS os = (Windows732OS) this.getOs();
            PoolManagerWin32 poolManager = (PoolManagerWin32) os.getPoolManager();
            TranslatorWin32 translator = (TranslatorWin32) this.getTranslator();

            long pageOffset = initialOffset;

            String tag = this._tagManager.getTagItems().get("Process").getTag();
            long poolTagOffset = poolManager.getAttributes().get("PoolTag").getPosition();
            String pageContent = "";
            EntityAddressSpaceWin32 aS = null;
            String content = null;
            while (pageOffset <= maxOffset) {
                //System.out.println(pageOffset + " ");
                pageContent = this.getDumpFormat().getRandomFileSeeker().getContent(pageOffset, this._pageSize * this._CANT_PAGES);

                int index = pageContent.indexOf(tag);
                int lastIndex = index;
                long absoluteOffset = 0;
                long relativeOffset = 0;
                while (index > -1) {

                    /**
                     * Si la diferencia es menor al tamaño de la estructura,
                     * descarta, porque no hay suficiente espacio para almacenar
                     * una estructura de proceso válida. Sigue buscando. Si no,
                     * busca el contenido de la estructura
                     */
                    //if (index - lastIndex >= EntityWindows732Process._SIZE_STRUCTURE) {
                    relativeOffset = index - poolTagOffset;
                    /**
                     * Offset del comienzo del Pool
                     */
                    absoluteOffset = pageOffset + relativeOffset;
                    long initialAbsoluteOffset = absoluteOffset;
                    EntityWindows732Process entityProcess = new EntityWindows732Process();

                    int kProcessPos = entityProcess.getAttributes().get("KPROCESS").getPosition();
                    long kProcessOffset = 0;
                    CrashDump32 dumpFormat = (CrashDump32) this.getDumpFormat();
                    BigInteger DTB = Conversor.hexToBigInteger(dumpFormat.getHeader().getItems().get("DIRECTORY_TABLE_BASE").getContent().toString());

                    /**
                     * Realiza distintos corrimientos contemplando el tamaño
                     * mínimo y máximo del Pool Header
                     */
                    long suma = 0;
                    BigInteger directoryTableBase = BigInteger.ZERO;
                    EntityWin732KPROCESS kProcess = null;
                    long gapStructure = poolManager._MAX_SIZE_STRUCTURE - poolManager._MIN_SIZE_STRUCTURE;
                    boolean found = false;
                    while (suma < gapStructure) {
                        kProcessOffset = initialAbsoluteOffset + poolManager._MIN_SIZE_STRUCTURE + Long.valueOf(kProcessPos) + suma;
                        kProcess = (EntityWin732KPROCESS) this.obtainKPROCESSContent(kProcessOffset, null);

                        if (kProcess != null) {
                            directoryTableBase = Conversor.hexToBigInteger(kProcess.getAttributes().get("DirectoryTableBase").getContent().toString());
                        }

                        if (BinaryOperator.discardLast12Bits(directoryTableBase).compareTo(BinaryOperator.discardLast12Bits(DTB)) == 0) { // DESCARTAR ÜLTIMOS 12 BITS PARA COMPARAR
                            found = true;
                            break;
                        }
                        suma++;
                    }

                    if (!found) {
                        suma = 0;
                        while (suma < gapStructure) {
                            kProcessOffset = initialAbsoluteOffset + poolManager._MIN_SIZE_STRUCTURE + kProcessPos + suma;
                            kProcess = (EntityWin732KPROCESS) this.obtainKPROCESSContent(kProcessOffset, null);

                            if (kProcess != null) {
                                directoryTableBase = Conversor.hexToBigInteger(kProcess.getAttributes().get("DirectoryTableBase").getContent().toString());
                            }

                            if (BinaryOperator.discardLast20Bits(directoryTableBase).compareTo(BinaryOperator.discardLast20Bits(DTB)) == 0) { // DESCARTAR ÜLTIMOS 20 BITS PARA COMPARAR. Puede ser un candidato.
                                found = true;
                                break;
                            }
                            suma++;
                        }
                    }

                    if (!found) {
                        suma = 0;
                        // while (suma < gapStructure) {
                        kProcessOffset = initialAbsoluteOffset + poolManager._MIN_SIZE_STRUCTURE + kProcessPos + suma;
                        kProcess = (EntityWin732KPROCESS) this.obtainKPROCESSContent(kProcessOffset, null);

                        int namePos = entityProcess.getAttributes().get("ImageFileName").getPosition() + entityProcess.getAttributes().get("ImageFileName").getLength();

                        String contentName = DataManager.getInstance().getItemContent("char", kProcessOffset, namePos, false);

                        int indexName = contentName.indexOf("System");
                        if (indexName > 0) {
                            kProcessOffset = kProcessOffset + indexName - entityProcess.getAttributes().get("ImageFileName").getPosition();
                            found = true;
                        }

                        /*if (kProcess != null) {
                         directoryTableBase = Conversor.hexToBigInteger(kProcess.getAttributes().get("DirectoryTableBase").getContent().toString());
                         }

                         if (directoryTableBase.compareTo(BigInteger.ZERO) == 0) { // DTB = 0. Puede ser un candidato.
                         found = true;
                         break;
                         }*/
                        //suma++;
                        //}
                    }

                    if (found) {//Si coincide, continua con el mapeo de la estructura al objeto

                        Iterator<Map.Entry<String, EntityAttribute>> i = entityProcess.getAttributes().entrySet().iterator();
                        while (i.hasNext()) {
                            /**
                             * Por cada item, le solicta a randomFileSeeker que
                             * obtenga el contenido
                             */

                            Map.Entry<String, EntityAttribute> e = (Map.Entry<String, EntityAttribute>) i.next();
                            //absoluteOffset = kProcess.getRelativeOffsetInPool() - kProcessPos + e.getValue().getPosition();
                            absoluteOffset = kProcessOffset + e.getValue().getPosition();

                            switch (e.getKey()) {
                                case "CreateTime":
                                    content = this.getDumpFormat().getDataManager().getComplexItemContent("windows file time", e.getValue().getContentType(), absoluteOffset, e.getValue().getLength(), e.getValue().isUnion(), e.getValue().isBigEndian());
                                    break;
                                case "ExitTime":
                                    content = this.getDumpFormat().getDataManager().getComplexItemContent("windows file time", e.getValue().getContentType(), absoluteOffset, e.getValue().getLength(), e.getValue().isUnion(), e.getValue().isBigEndian());
                                    break;
                                default:
                                    content = this.getDumpFormat().getDataManager().getItemContent(e.getValue().getContentType(), absoluteOffset, e.getValue().getLength(), e.getValue().isUnion());
                                    break;
                            }

                            if (content != null) {
                                e.getValue().setContent(content);
                            }

                            content = null;
                        }

                        entityProcess.setProcessID(Conversor.hexToInt(entityProcess.getAttributes().get("UniqueProcessId").getContent().toString()));

                        if (this._activeProcesses.get(entityProcess.getProcessID()) == null) {
                            entityProcess.setStateLabel("Hidden");
                        } else {
                            entityProcess.setStateLabel("Active");
                        }

                        /**
                         * A partir de acá, los atributos de tipo estrucutra
                         * compleja y otros atributos
                         */
                        long absoluteAttributeOffset = kProcessOffset + entityProcess.getAttributes().get("ActiveProcessLinks").getPosition();
                        aS = new EntityAddressSpaceWin32();
                        BigInteger pA = this.getDumpFormat().getPhysicalAddresByOffset(absoluteAttributeOffset);
                        aS.setPhysicalAddress(pA);
                        aS.setPhysicalAddressHex(Conversor.bigIntToHexString(pA));
                        //long physicalAddressFileOffsetAS = this.getDumpFormat().getFileOffset(aS.getPhysicalAddress());
                        long physicalAddressFileOffsetAS = absoluteAttributeOffset;
                        aS.setOffsetInFile(physicalAddressFileOffsetAS);
                        aS.setOffsetInFileHex(Conversor.longToHexString(physicalAddressFileOffsetAS));
                        entityProcess.setaS(aS);

                        /**
                         * Agrega entidad al HashMap
                         */
                        this.getEntities().put(entityProcess.getProcessID(), entityProcess);

                        /**
                         * Notifica observadores
                         */
                        this.notifyObservers(entityProcess);

                        System.out.println("Proceso Encontrado. absoluteOffset: " + String.valueOf(absoluteOffset));
                    } else {//Lo marca como falso positivo y realiza un mapeo mínimo
                        entityProcess.setFalsePositive(true);
                        System.out.println("Falso Positivo --> Proceso No Encontrado. absoluteOffset: " + String.valueOf(absoluteOffset));
                        /**
                         * Mapea nombre del proceso
                         */

                        /**
                         * Mapea ID del proceso del que hereda
                         */
                        /**
                         * Mapea ID del proceso
                         */
                        /**
                         * Mapea Createtime
                         */
                        /**
                         * Mapea ExitTime
                         */
                    }
                    lastIndex = index; //Se queda con el índice anterior
                    index = pageContent.indexOf(tag, index + tag.length());//Busca otra ocurrencia más adelante en la página
                }
                /**
                 * Se mueve por offset, por lo que no es necesario realizar
                 * cálculo
                 */
                pageOffset = pageOffset + this._pageSize * this._CANT_PAGES;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Entity parseEntityByOffset(long offset, Entity entity, Map.Entry<String, EntityAttribute> attribute) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        this.parse(null);
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
    public synchronized void clearChanged() {
        super.clearChanged();
    }

    @Override
    public synchronized void setChanged() {
        super.setChanged();
    }

    @Override
    public synchronized void deleteObservers() {
        super.deleteObservers();
    }

    @Override
    public synchronized void notifyObservers(Object o) {
        super.notifyObservers(o);
    }

    @Override
    public synchronized void notifyObservers() {
        super.notifyObservers();
    }

    @Override
    public void parse(long offset, Object[] params) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object call() throws Exception {
        this.parse(this.getInitialOffset(), this.getMaxOffset(), null);

        return this.getEntities();
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
                entry.getValue().getEntity().setParser(new ParserWin732HiddenProcessCrashDMP((CrashDump32) this.getDumpFormat(), (Windows732OS) this.getOs(), (TranslatorWin32) this.getTranslator()));
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

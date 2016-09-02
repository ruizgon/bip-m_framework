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
package dump.parsers.connections;

import dump.formats.CrashDump64;
import dump.formats.DumpFormat;
import dump.formats.HeaderCrashDump64;
import dump.formats.PhysicalMemoryDescriptor64;
import dump.formats.Run64;
import dump.ooss.OperatingSystemStructure;
import dump.ooss.Windows764OS;
import dump.parsers.process.ParserWin764ActiveProcessCrashDMP;
import dump.parsers.utils.pools.PoolManagerWin64;
import dump.parsers.utils.tags.TagManagerWin64;
import entities.Entity;
import entities.EntityAttribute;
import entities.connection.EntityAddressFamily;
import entities.connection.EntityAddressFamilyWin64;
import entities.connection.EntityAddressInfo;
import entities.connection.EntityAddressInfoWin64;
import entities.connection.EntityConnection;
import entities.connection.EntityLocalAddress;
import entities.connection.EntityLocalAddressWin64;
import entities.connection.EntityWindows764TCPConnection;
import entities.connection.EntityWindows764UDPConnection;
import entities.connection.EntityWindowsConnectionState;
import entities.process.EntityProcess;
import entities.process.EntityWindows764Process;
import entities.translation.EntityAddressSpaceWin64;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
import system.utils.Conversor;
import system.utils.DateManager;
import translation.Translator;
import translation.TranslatorWin64;

/**
 *
 * @author Gonzalo
 */
public class ParserWin764ConnectionCrashDMP extends ParserWin64Connection {

    private int _CANT_PAGES = 8;
    private TagManagerWin64 _tagManager;
    private Map<Integer, EntityConnection> _connections;

    public ParserWin764ConnectionCrashDMP(DumpFormat _dumpFormat, OperatingSystemStructure _os) {
        super(_dumpFormat, _os);

        /**
         * Registra observadores
         */
        this.addObserver(output.OutputManagerDump.getInstance());
        this.addObserver(output.OutputManagerConsole.getInstance());

        this._tagManager = new TagManagerWin64();
    }

    public ParserWin764ConnectionCrashDMP(DumpFormat _dumpFormat, OperatingSystemStructure _os, Translator _translator) {
        super(_dumpFormat, _os, _translator);

        /**
         * Registra observadores
         */
        this.addObserver(output.OutputManagerDump.getInstance());
        this.addObserver(output.OutputManagerConsole.getInstance());

        this._tagManager = new TagManagerWin64();
    }

    public ParserWin764ConnectionCrashDMP(DumpFormat _dumpFormat, OperatingSystemStructure _os, Translator _translator, ConcurrentHashMap<Object, Entity> _entities, long _initialOffset, long _maxOffset) {
        super(_dumpFormat, _os, _translator);
        this.setInitialOffset(_initialOffset);
        this.setMaxOffset(_maxOffset);

        /**
         * Registra observadores
         */
        this.addObserver(output.OutputManagerDump.getInstance());
        this.addObserver(output.OutputManagerConsole.getInstance());

        this._tagManager = new TagManagerWin64();
        this.setEntities(_entities);
    }

    public TagManagerWin64 getTagManager() {
        return _tagManager;
    }

    public void setTagManager(TagManagerWin64 _tagManager) {
        this._tagManager = _tagManager;
    }

    @Override
    public void parse(Object[] params) {
        this.setStatus("Iniciado");
        this.setStatusDetail("Proceso de obtención de conexiones Windows 7 x64.");
        String dateString = DateManager.getActualDateWithISOFormat();
        StringBuilder log = new StringBuilder();
        log.append(this.getStatus() + " " + this.getStatusDetail() + " " + dateString);

        this.notifyObservers(this.getStatus());
        this.notifyObservers(this.getStatusDetail());
        this.notifyObservers("Tipo    Local Address    Remote Address    Local Port    Remote Port    State    CreateTime    IPVersion    PID    VA    PA    FileOffset    Blink    Flink");
        this.notifyObservers("-----------------------------------------------------------------------------------------------------------------------------------------------------------");

        this.notifyObservers(this);
        try {
            Calendar cal = new GregorianCalendar();

            this.setEntities(new ConcurrentHashMap<Object, Entity>());

            CrashDump64 dumpFormat = (CrashDump64) this.getDumpFormat();
            PhysicalMemoryDescriptor64 physicalMemoryDescriptor32 = (PhysicalMemoryDescriptor64) dumpFormat.getHeader().getItems().get("PHYSICAL_MEMORY_BLOCK_BUFFER").getContent();
            Map<Integer, Run64> runs = physicalMemoryDescriptor32.getRuns();

            long initialOffset = -1;

            int cantRuns = runs.size();
            long maxOffset = -1;

            HeaderCrashDump64 header = dumpFormat.getHeader();
            String itemContentDTB = null;
            if (header != null) {
                /**
                 * Obtengo valor de DTB
                 */
                itemContentDTB = (String) header.getItems().get("DIRECTORY_TABLE_BASE").getContent();
            }

            int cantThreads = cantRuns * 4;
            ScheduledExecutorService pool = Executors.newScheduledThreadPool(cantThreads);

            Set<ScheduledFuture<Map<Integer, Entity>>> set = new HashSet<ScheduledFuture<Map<Integer, Entity>>>();
            int delaySeconds = 3;
            for (int i = 0; i < cantRuns; i++) {

                initialOffset = runs.get(i).getFileOffset();
                maxOffset = runs.get(i).getFileOffset() + runs.get(i).getLength().longValue();

                long difOffset = maxOffset - initialOffset;

                if (difOffset < 1000000000) {

                    TranslatorWin64 translator = new TranslatorWin64(this.getOs(), this.getDumpFormat());

                    if (itemContentDTB != null) {
                        translator.setDTB(Conversor.hexToLong(itemContentDTB));
                    }
                    Callable<Map<Integer, Entity>> callable = new ParserWin764ConnectionCrashDMP(this.getDumpFormat(), this.getOs(), translator, this.getEntities(), initialOffset, maxOffset);
                    ScheduledFuture<Map<Integer, Entity>> future = pool.schedule(callable, delaySeconds, TimeUnit.SECONDS);
                    set.add(future);
                } else {//Divide el trabajo en 4 hilos
                    TranslatorWin64 translator2 = new TranslatorWin64(this.getOs(), this.getDumpFormat());
                    TranslatorWin64 translator3 = new TranslatorWin64(this.getOs(), this.getDumpFormat());
                    TranslatorWin64 translator4 = new TranslatorWin64(this.getOs(), this.getDumpFormat());
                    TranslatorWin64 translator5 = new TranslatorWin64(this.getOs(), this.getDumpFormat());
                    if (itemContentDTB != null) {
                        translator2.setDTB(Conversor.hexToLong(itemContentDTB));
                        translator3.setDTB(Conversor.hexToLong(itemContentDTB));
                        translator4.setDTB(Conversor.hexToLong(itemContentDTB));
                        translator5.setDTB(Conversor.hexToLong(itemContentDTB));
                    }

                    Callable<Map<Integer, Entity>> callable2 = new ParserWin764ConnectionCrashDMP(this.getDumpFormat(), this.getOs(), translator2, this.getEntities(), initialOffset, maxOffset / 4);
                    ScheduledFuture<Map<Integer, Entity>> future2 = pool.schedule(callable2, delaySeconds, TimeUnit.SECONDS);
                    set.add(future2);

                    Callable<Map<Integer, Entity>> callable3 = new ParserWin764ConnectionCrashDMP(this.getDumpFormat(), this.getOs(), translator3, this.getEntities(), maxOffset / 4 + 1, maxOffset / 2);
                    ScheduledFuture<Map<Integer, Entity>> future3 = pool.schedule(callable3, delaySeconds, TimeUnit.SECONDS);
                    set.add(future3);

                    Callable<Map<Integer, Entity>> callable4 = new ParserWin764ConnectionCrashDMP(this.getDumpFormat(), this.getOs(), translator4, this.getEntities(), maxOffset / 2 + 1, 3 * maxOffset / 4);
                    ScheduledFuture<Map<Integer, Entity>> future4 = pool.schedule(callable4, delaySeconds, TimeUnit.SECONDS);
                    set.add(future4);

                    Callable<Map<Integer, Entity>> callable5 = new ParserWin764ConnectionCrashDMP(this.getDumpFormat(), this.getOs(), translator5, this.getEntities(), 3 * maxOffset / 4 + 1, maxOffset);
                    ScheduledFuture<Map<Integer, Entity>> future5 = pool.schedule(callable5, delaySeconds, TimeUnit.SECONDS);
                    set.add(future5);
                }

                initialOffset = -1;
                maxOffset = -1;
            }

            /*initialOffset = Conversor.hexToLong("EE780000");
             maxOffset = Conversor.hexToLong("EE7fD180");
             TranslatorWin64 translator = new TranslatorWin64(this.getOs(), this.getDumpFormat());
             if (itemContentDTB != null) {
             translator.setDTB(Conversor.hexToLong(itemContentDTB));
             }
             Callable<Map<Integer, Entity>> callable = new ParserWin764ConnectionCrashDMP(this.getDumpFormat(), this.getOs(), translator, this.getEntities(), initialOffset, maxOffset);

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
                ex.printStackTrace();
            }
            cal = new GregorianCalendar();
            this.notifyObservers("Finalizó la búsqueda de conexiones " + cal.getTime().toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (this.getEntities() != null) {
            this.notifyObservers("Conexiones TCP/UDP encontradas = " + this.getEntities().size() + ".");
        }
    }

    @Override
    public void parse(Entity entity, Object[] params) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void parse(long offset, Object[] params) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
            Windows764OS os = (Windows764OS) this.getOs();
            PoolManagerWin64 poolManager = (PoolManagerWin64) os.getPoolManager();
            TranslatorWin64 translator = (TranslatorWin64) this.getTranslator();

            long pageOffset = initialOffset;

            String tag = this._tagManager.getTagItems().get("TCPConnection").getTag();
            String tagUDP = this._tagManager.getTagItems().get("UDPConnection").getTag();
            int tagSize = 0;
            if (tag.length() > tagUDP.length()) {
                tagSize = tag.length();
            } else {
                tagSize = tagUDP.length();
            }
            String pageContent = "";
            EntityAddressSpaceWin64 aS = null;

            int encontrado = 0;
            while (pageOffset <= maxOffset) {
                //System.out.println(pageOffset + " ");

                pageContent = this.getDumpFormat().getRandomFileSeeker().getContent(pageOffset, this._pageSize * this._CANT_PAGES);

                int index = pageContent.indexOf(tag);
                int indexUDP = pageContent.indexOf(tagUDP);
                int lastIndex = index;
                long initialAbsoluteOffset = 0;
                long absoluteOffset = 0;
                long relativeOffset = 0;

                String content = null;
                long eprocessAddress = 0;
                long eprocessOffset = 0;
                while (index > -1 || indexUDP > -1) {

                    /**
                     * Para TCP
                     */
                    if (index > -1) {
                        relativeOffset = index;
                        /**
                         * Offset del comienzo del Pool
                         */
                        //initialAbsoluteOffset = pageOffset + relativeOffset + tag.length();
                        initialAbsoluteOffset = pageOffset + relativeOffset - 0x004 + poolManager._POOL_HEADER_SIZE;
                        EntityWindows764TCPConnection entityConnection = new EntityWindows764TCPConnection();

                        boolean positivo = this.isValidStructure(absoluteOffset);

                        if (positivo) {//Si coincide, continua con el mapeo de la estructura al objeto

                            Iterator<Map.Entry<String, EntityAttribute>> i = entityConnection.getAttributes().entrySet().iterator();
                            while (i.hasNext()) {
                                absoluteOffset = initialAbsoluteOffset;
                                /**
                                 * Por cada item, le solicta a randomFileSeeker
                                 * que obtenga el contenido
                                 */

                                Map.Entry<String, EntityAttribute> e = (Map.Entry<String, EntityAttribute>) i.next();

                                this.getEntryAttributeContent(absoluteOffset, e);

                            }
                            /**
                             * Seteo estado
                             */
                            String ownerVA = (String) entityConnection.getAttributes().get("OwnerVA").getContent();
                            EntityProcess p = (EntityProcess) entityConnection.getAttributes().get("Owner").getEntity();
                            if (p.getaS() != null) {
                                p.getaS().setVirtualAddressHex(ownerVA);
                                p.getaS().setVirtualAddress(Conversor.hexToBigInteger(ownerVA));
                                entityConnection.getAttributes().get("Owner").setEntity(p);
                                entityConnection.getAttributes().get("Owner").setContent(p);
                            }
                            entityConnection.setStateLabel("Active");

                            /**
                             * A partir de acá, los atributos de tipo estrucutra
                             * compleja y otros atributos.
                             */
                            aS = new EntityAddressSpaceWin64();
                            BigInteger physicalAddress = this.getDumpFormat().getPhysicalAddresByOffset(absoluteOffset);
                            aS.setPhysicalAddress(physicalAddress);
                            aS.setPhysicalAddressHex(Conversor.bigIntToHexString(physicalAddress));
                            long physicalAddressFileOffsetAS = absoluteOffset;
                            aS.setOffsetInFile(physicalAddressFileOffsetAS);
                            aS.setOffsetInFileHex(Conversor.longToHexString(physicalAddressFileOffsetAS));
                            entityConnection.setaS(aS);

                            /**
                             * Agrega entidad al HashMap
                             */
                            //this.getEntities().put(entityUDPConnection.getLocalAddress(), entityUDPConnection);
                            this.getEntities().put(encontrado++, entityConnection);

                            /**
                             * Notifica observadores
                             */
                            this.notifyObservers(entityConnection);
                        } else {//Lo marca como falso positivo y realiza un mapeo mínimo
                            entityConnection.setFalsePositive(true);
                            System.out.println("Conexión No Encontrada. absoluteOffset: " + String.valueOf(absoluteOffset));
                        }
                        lastIndex = index; //Se queda con el índice anterior
                        index = pageContent.indexOf(tag, index + tag.length());//Busca otra ocurrencia más adelante en la página
                    }

                    /**
                     * Para UDP
                     */
                    if (indexUDP > -1) {
                        relativeOffset = indexUDP;
                        /**
                         * Offset del comienzo del Pool
                         */
                        initialAbsoluteOffset = pageOffset + relativeOffset - 0x004 + poolManager._POOL_HEADER_SIZE;
                        EntityWindows764UDPConnection entityUDPConnection = new EntityWindows764UDPConnection();

                        boolean positivo = this.isValidStructure(absoluteOffset);

                        if (positivo) {//Si coincide, continua con el mapeo de la estructura al objeto

                            Iterator<Map.Entry<String, EntityAttribute>> i = entityUDPConnection.getAttributes().entrySet().iterator();
                            while (i.hasNext()) {
                                absoluteOffset = initialAbsoluteOffset;

                                Map.Entry<String, EntityAttribute> e = (Map.Entry<String, EntityAttribute>) i.next();

                                this.getEntryAttributeContent(absoluteOffset, e);
                            }
                            /**
                             * Seteo estado
                             */
                            String ownerVA = (String) entityUDPConnection.getAttributes().get("OwnerVA").getContent();
                            EntityProcess p = (EntityProcess) entityUDPConnection.getAttributes().get("Owner").getEntity();
                            if (p.getaS() != null) {
                                p.getaS().setVirtualAddressHex(ownerVA);
                                p.getaS().setVirtualAddress(Conversor.hexToBigInteger(ownerVA));
                                entityUDPConnection.getAttributes().get("Owner").setEntity(p);
                                entityUDPConnection.getAttributes().get("Owner").setContent(p);
                            }
                            entityUDPConnection.setStateLabel("Active");

                            /**
                             * A partir de acá, los atributos de tipo estrucutra
                             * compleja y otros atributos TODO: CREAR METODO
                             * PARA CALCULAR PA SEGUN OFFSET
                             */
                            aS = new EntityAddressSpaceWin64();
                            BigInteger physicalAddress = this.getDumpFormat().getPhysicalAddresByOffset(absoluteOffset);
                            aS.setPhysicalAddress(physicalAddress);
                            aS.setPhysicalAddressHex(Conversor.bigIntToHexString(physicalAddress));
                            long physicalAddressFileOffsetAS = absoluteOffset;
                            aS.setOffsetInFile(physicalAddressFileOffsetAS);
                            aS.setOffsetInFileHex(Conversor.longToHexString(physicalAddressFileOffsetAS));
                            entityUDPConnection.setaS(aS);

                            /**
                             * Agrega entidad al HashMap
                             */
                            //this.getEntities().put(entityUDPConnection.getLocalAddress(), entityUDPConnection);
                            this.getEntities().put(encontrado++, entityUDPConnection);

                            /**
                             * Notifica observadores
                             */
                            this.notifyObservers(entityUDPConnection);

                            //System.out.println("Conexión Encontrada. absoluteOffset: " + String.valueOf(absoluteOffset));
                        } else {//Lo marca como falso positivo y realiza un mapeo mínimo
                            entityUDPConnection.setFalsePositive(true);
                            System.out.println("Conexión No Encontrada. absoluteOffset: " + String.valueOf(absoluteOffset));
                        }
                        lastIndex = indexUDP; //Se queda con el índice anterior
                        indexUDP = pageContent.indexOf(tagUDP, indexUDP + tagUDP.length());//Busca otra ocurrencia más adelante en la página
                    }
                }
                /**
                 * Se mueve por offset, por lo que no es necesario realizar
                 * cálculo
                 */
                pageOffset = pageOffset + this._pageSize * this._CANT_PAGES - tagSize;
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
    public boolean isValidStructure(Object o) {
        boolean positivo = true;

        long absoluteAttributeOffset = (long) o;
        String content = this.getDumpFormat().getContentByOffset(absoluteAttributeOffset, 4);

        if (content.equals("    ")) {
            positivo = false;
        }

        return positivo;
    }

    @Override
    public Entity getEntityByAttributeValue(Entity entity, String attribute, Object content) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map.Entry<String, EntityAttribute> setParserAttribute(Map.Entry<String, EntityAttribute> entry) {
        if (entry.getValue().getEntity() instanceof EntityWindows764Process) {
            if (this.getParserProcess() == null) {
                this.setParserProcess(new ParserWin764ActiveProcessCrashDMP((CrashDump64) this.getDumpFormat(), (Windows764OS) this.getOs(), (TranslatorWin64) this.getTranslator()));
            }
            entry.getValue().getEntity().setParser(this.getParserProcess());
        } else {
            entry.getValue().getEntity().setParser(this);
        }

        return entry;
    }

    @Override
    public void run() {
        this.parse(null);
    }

    @Override
    public Object call() throws Exception {
        this.parse(this.getInitialOffset(), this.getMaxOffset(), null);

        return this.getEntities();
    }

    @Override
    public EntityProcess getProcessByOffset(long offset) {
        EntityProcess process = null;
        try {
            /**
             * Si no lo tiene instanciado, instamncia un parser para obtener la
             * entidad proceso
             */
            if (this.getParserProcess() == null) {
                this.setParserProcess(new ParserWin764ActiveProcessCrashDMP((CrashDump64) this.getDumpFormat(), (Windows764OS) this.getOs(), (TranslatorWin64) this.getTranslator()));
            }

            process = (EntityWindows764Process) this.getParserProcess().parseEntityByOffset(offset, null, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return process;
    }

    @Override
    public EntityAddressFamily getAddressFamilyByOffset(long offset, Map.Entry<String, EntityAttribute> e) {
        EntityAddressFamily aF = null;
        try {
            String content = null;
            aF = new EntityAddressFamilyWin64();
            offset += aF.getAttributes().get("IPVersion").getPosition();
            content = this.getDumpFormat().getDataManager().getItemContent(e.getValue().getContentType(), offset, e.getValue().getLength(), e.getValue().isUnion(), e.getValue().isBigEndian());

            /*BigInteger offsetAF = this.getTranslator().calculatePhysicalAddress(content);

             offsetAF = BigInteger.valueOf(this.getDumpFormat().getFileOffset(offsetAF));

             offsetAF = offsetAF.add(BigInteger.valueOf(e.getValue().getEntity().getAttributes().get("IPVersion").getPosition()));

             content = this.getDumpFormat().getDataManager().getItemContent(e.getValue().getEntity().getAttributes().get("IPVersion").getContentType(), offsetAF.longValue(), e.getValue().getEntity().getAttributes().get("IPVersion").getLength(), e.getValue().getEntity().getAttributes().get("IPVersion").isUnion(), e.getValue().getEntity().getAttributes().get("IPVersion").isBigEndian());*/
            String ipVersion = "Undetermined";
            if (content != null) {
                ipVersion = EntityAddressFamilyWin64._IP_VERSION.get(Conversor.hexToInt(content));
            }

            aF.getAttributes().get("IPVersion").setContent(ipVersion);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return aF;
    }

    @Override
    public EntityAddressInfo getAddressInfoByOffset(long offset) {
        EntityAddressInfoWin64 addressInfo = new EntityAddressInfoWin64();

        long absoluteAttributeOffset = 0;
        int pointerMultiplicity = 0;
        boolean bigEndian = false;

        String content = null;
        BigInteger address = BigInteger.ZERO;
        long offsetAttr = 0;
        Iterator<Map.Entry<String, EntityAttribute>> i = addressInfo.getAttributes().entrySet().iterator();
        while (i.hasNext()) {
            /**
             * Por cada item, le solicta a randomFileSeeker que obtenga el
             * contenido
             */

            Map.Entry<String, EntityAttribute> e = (Map.Entry<String, EntityAttribute>) i.next();
            absoluteAttributeOffset = offset + e.getValue().getPosition();
            pointerMultiplicity = e.getValue().getPointerMultiplicity();
            bigEndian = e.getValue().isBigEndian();
            switch (e.getKey()) {
                case "Local":
                    while (pointerMultiplicity >= 0) {
                        if (pointerMultiplicity == 1) {
                            absoluteAttributeOffset += 0xC; //Offset del puntero en _LOCAL_ADDRESS
                        }
                        content = this.getDumpFormat().getDataManager().getItemContent(e.getValue().getContentType(), absoluteAttributeOffset, e.getValue().getLength(), e.getValue().isUnion());

                        address = this.getTranslator().calculatePhysicalAddress(content);

                        offsetAttr = this.getDumpFormat().getFileOffset(address);

                        absoluteAttributeOffset = offsetAttr;

                        pointerMultiplicity--;
                    }

                    /**
                     * Obtengo contenido para dirección IP
                     */
                    content = this.getDumpFormat().getDataManager().getComplexItemContent(e.getValue().getComplexContentType(), e.getValue().getContentType(), absoluteAttributeOffset, e.getValue().getLength(), e.getValue().isUnion(), e.getValue().isBigEndian());

                    if (content != null) {
                        e.getValue().setContent(content);
                    }
                    break;
                case "Remota":
                    while (pointerMultiplicity >= 0) {
                        content = this.getDumpFormat().getDataManager().getItemContent(e.getValue().getContentType(), absoluteAttributeOffset, e.getValue().getLength(), e.getValue().isUnion());

                        address = this.getTranslator().calculatePhysicalAddress(content);

                        offsetAttr = this.getDumpFormat().getFileOffset(address);

                        absoluteAttributeOffset = offsetAttr;

                        pointerMultiplicity--;
                    }

                    /**
                     * Obtengo contenido para dirección IP
                     */
                    content = this.getDumpFormat().getDataManager().getComplexItemContent(e.getValue().getComplexContentType(), e.getValue().getContentType(), absoluteAttributeOffset, e.getValue().getLength(), e.getValue().isUnion(), e.getValue().isBigEndian());

                    if (content != null) {
                        e.getValue().setContent(content);
                    }
                    break;
                default:
                    break;
            }
        }

        return addressInfo;
    }

    @Override
    public String getConnectionStateByOffset(long offset, Map.Entry<String, EntityAttribute> e) {
        String state = null;

        try {
            String content = this.getDumpFormat().getDataManager().getItemContent(e.getValue().getContentType(), offset, e.getValue().getLength(), e.getValue().isUnion());

            EntityWindowsConnectionState connectionState = new EntityWindowsConnectionState();

            int s = Conversor.hexToInt(content);

            state = connectionState.getStates().get(s);

            if (state == null) {
                state = "Undetermined";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return state;
    }

    @Override
    public EntityLocalAddress getLocalAddressByOffset(long offset) {
        EntityLocalAddressWin64 localAddress = new EntityLocalAddressWin64();

        long absoluteAttributeOffset = 0;
        int pointerMultiplicity = 0;
        boolean bigEndian = false;

        String content = null;
        BigInteger address = BigInteger.ZERO;
        long offsetAttr = 0;
        Iterator<Map.Entry<String, EntityAttribute>> i = localAddress.getAttributes().entrySet().iterator();
        while (i.hasNext()) {
            /**
             * Por cada item, le solicta a randomFileSeeker que obtenga el
             * contenido
             */

            Map.Entry<String, EntityAttribute> e = (Map.Entry<String, EntityAttribute>) i.next();
            absoluteAttributeOffset = offset + e.getValue().getPosition();
            pointerMultiplicity = e.getValue().getPointerMultiplicity();
            bigEndian = e.getValue().isBigEndian();
            while (pointerMultiplicity >= 0) {
                if (pointerMultiplicity == 0) {
                    absoluteAttributeOffset += 0xC; //Offset del puntero en _LOCAL_ADDRESS
                }
                content = this.getDumpFormat().getDataManager().getItemContent(e.getValue().getContentType(), absoluteAttributeOffset, e.getValue().getLength(), e.getValue().isUnion());

                address = this.getTranslator().calculatePhysicalAddress(content);

                offsetAttr = this.getDumpFormat().getFileOffset(address);

                absoluteAttributeOffset = offsetAttr;

                pointerMultiplicity--;
            }

            /**
             * Obtengo contenido para dirección IP
             */
            content = this.getDumpFormat().getDataManager().getComplexItemContent("ip", e.getValue().getContentType(), absoluteAttributeOffset, e.getValue().getLength(), e.getValue().isUnion(), e.getValue().isBigEndian());

            if (content != null) {
                e.getValue().setContent(content);
            }

        }

        return localAddress;
    }

    @Override
    public boolean validateAttributeContent(Entry<String, EntityAttribute> attribute) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

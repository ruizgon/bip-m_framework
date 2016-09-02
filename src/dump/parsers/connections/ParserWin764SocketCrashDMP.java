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
import dump.formats.PhysicalMemoryDescriptor64;
import dump.formats.Run64;
import dump.ooss.OperatingSystemStructure;
import dump.ooss.Windows764OS;
import dump.parsers.Parser;
import dump.parsers.interfaces.IParserSocket;
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
import entities.connection.EntityWindows764Socket;
import entities.connection.EntityWindows764TCPConnection;
import entities.connection.EntityWindowsConnectionState;
import entities.process.EntityProcess;
import entities.process.EntityWindows764Process;
import entities.translation.EntityAddressSpaceWin64;
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
import system.utils.Conversor;
import system.utils.DateManager;
import translation.Translator;
import translation.TranslatorWin64;

/**
 *
 * @author Gonzalo
 */
public class ParserWin764SocketCrashDMP extends ParserWin64Socket implements IParserSocket {

    private int _CANT_PAGES = 8;
    private TagManagerWin64 _tagManager;
    private Map<Integer, EntityWindows764Socket> _sockets;

    public ParserWin764SocketCrashDMP(DumpFormat _dumpFormat, OperatingSystemStructure _os) {
        super(_dumpFormat, _os);

        /**
         * Registra observadores
         */
        this.addObserver(output.OutputManagerDump.getInstance());
        this.addObserver(output.OutputManagerConsole.getInstance());

        this._tagManager = new TagManagerWin64();

        /**
         * Inicializa parsers colaboradores
         */
        this.setHelperParsers(new HashMap<String, Parser>());
        this.getHelperParsers().put(EntityWindows764Process._TAG, new ParserWin764ActiveProcessCrashDMP((CrashDump64) this.getDumpFormat(), (Windows764OS) this.getOs(), (TranslatorWin64) this.getTranslator()));
        this.getHelperParsers().put(EntityWindows764TCPConnection._TAG, new ParserWin764ConnectionCrashDMP((CrashDump64) this.getDumpFormat(), (Windows764OS) this.getOs(), (TranslatorWin64) this.getTranslator()));
    }

    public ParserWin764SocketCrashDMP(DumpFormat _dumpFormat, OperatingSystemStructure _os, Translator _translator) {
        super(_dumpFormat, _os, _translator);

        /**
         * Registra observadores
         */
        this.addObserver(output.OutputManagerDump.getInstance());
        this.addObserver(output.OutputManagerConsole.getInstance());

        this._tagManager = new TagManagerWin64();
        //this.setParserConnection(new ParserWin764ConnectionCrashDMP(_dumpFormat, _os, _translator));

        /**
         * Inicializa parsers colaboradores
         */
        this.setHelperParsers(new HashMap<String, Parser>());
        this.getHelperParsers().put(EntityWindows764Process._TAG, new ParserWin764ActiveProcessCrashDMP((CrashDump64) this.getDumpFormat(), (Windows764OS) this.getOs(), (TranslatorWin64) this.getTranslator()));
        this.getHelperParsers().put(EntityWindows764TCPConnection._TAG, new ParserWin764ConnectionCrashDMP((CrashDump64) this.getDumpFormat(), (Windows764OS) this.getOs(), (TranslatorWin64) this.getTranslator()));
    }

    public ParserWin764SocketCrashDMP(DumpFormat _dumpFormat, OperatingSystemStructure _os, Translator _translator, ParserWin64Connection _parserConnection) {
        super(_dumpFormat, _os, _translator, _parserConnection);

        /**
         * Registra observadores
         */
        this.addObserver(output.OutputManagerDump.getInstance());
        this.addObserver(output.OutputManagerConsole.getInstance());

        this._tagManager = new TagManagerWin64();
        //this.setParserConnection(_parserConnection);

        /**
         * Inicializa parsers colaboradores
         */
        this.setHelperParsers(new HashMap<String, Parser>());
        this.getHelperParsers().put(EntityWindows764Process._TAG, new ParserWin764ActiveProcessCrashDMP((CrashDump64) this.getDumpFormat(), (Windows764OS) this.getOs(), (TranslatorWin64) this.getTranslator()));
        this.getHelperParsers().put(EntityWindows764TCPConnection._TAG, new ParserWin764ConnectionCrashDMP((CrashDump64) this.getDumpFormat(), (Windows764OS) this.getOs(), (TranslatorWin64) this.getTranslator()));
    }

    public ParserWin764SocketCrashDMP(DumpFormat _dumpFormat, OperatingSystemStructure _os, Translator _translator, ParserWin64Connection _parserConnection, ConcurrentHashMap<Object, Entity> _entities, long _initialOffset, long _maxOffset) {
        super(_dumpFormat, _os, _translator, _parserConnection);
        this.setInitialOffset(_initialOffset);
        this.setMaxOffset(_maxOffset);

        /**
         * Registra observadores
         */
        this.addObserver(output.OutputManagerDump.getInstance());
        this.addObserver(output.OutputManagerConsole.getInstance());

        this._tagManager = new TagManagerWin64();
        //this.setParserConnection(_parserConnection);

        /**
         * Inicializa parsers colaboradores
         */
        this.setHelperParsers(new HashMap<String, Parser>());
        this.getHelperParsers().put(EntityWindows764Process._TAG, new ParserWin764ActiveProcessCrashDMP((CrashDump64) this.getDumpFormat(), (Windows764OS) this.getOs(), (TranslatorWin64) this.getTranslator()));
        this.getHelperParsers().put(EntityWindows764TCPConnection._TAG, new ParserWin764ConnectionCrashDMP((CrashDump64) this.getDumpFormat(), (Windows764OS) this.getOs(), (TranslatorWin64) this.getTranslator()));

        this.setEntities(_entities);
    }

    @Override
    public void parse(Object[] params) {
        try {
            Calendar cal = new GregorianCalendar();

            this.setStatus("Iniciado");
            this.setStatusDetail("Proceso de parseo de dump y obtención de sockets Windows 7 x64: " + cal.getTime().toString());
            String dateString = DateManager.getActualDateWithISOFormat();
            StringBuilder log = new StringBuilder();
            log.append(this.getStatus() + " " + this.getStatusDetail() + " " + dateString);

            this.notifyObservers(this.getStatus());
            this.notifyObservers(this.getStatusDetail());
            this.notifyObservers("State    Owner    Local    Port    Create Time    PA    FileOffset");
            this.notifyObservers("-------------------------------------------------------------------");

            this.setEntities(new ConcurrentHashMap<Object, Entity>());

            CrashDump64 dumpFormat = (CrashDump64) this.getDumpFormat();
            PhysicalMemoryDescriptor64 physicalMemoryDescriptor64 = (PhysicalMemoryDescriptor64) dumpFormat.getHeader().getItems().get("PHYSICAL_MEMORY_BLOCK_BUFFER").getContent();
            Map<Integer, Run64> runs = physicalMemoryDescriptor64.getRuns();

            long initialOffset = -1;

            int cantRuns = runs.size();
            long maxOffset = -1;

            Map<Integer, Callable<Map<Integer, Entity>>> parsersThread = new HashMap<Integer, Callable<Map<Integer, Entity>>>();

            int cantThreads = cantRuns * 4;
            ScheduledExecutorService pool = Executors.newScheduledThreadPool(cantThreads);

            Set<ScheduledFuture<Map<Integer, Entity>>> set = new HashSet<ScheduledFuture<Map<Integer, Entity>>>();
            int delaySeconds = 3;

            for (int i = 0; i < cantRuns; i++) {

                initialOffset = runs.get(i).getFileOffset();
                maxOffset = BigInteger.valueOf(runs.get(i).getFileOffset()).add(runs.get(i).getLength()).longValue();

                long difOffset = maxOffset - initialOffset;

                if (difOffset < 1000000000) {//Divide el trabajo en 4 hilos
                    Callable<Map<Integer, Entity>> callable = new ParserWin764SocketCrashDMP(this.getDumpFormat(), this.getOs(), new TranslatorWin64(this.getOs(), this.getDumpFormat()), (ParserWin64Connection) this.getHelperParsers().get(EntityWindows764TCPConnection._TAG), this.getEntities(), initialOffset, maxOffset);
                    ScheduledFuture<Map<Integer, Entity>> future = pool.schedule(callable, delaySeconds, TimeUnit.SECONDS);
                    set.add(future);
                } else {
                    Callable<Map<Integer, Entity>> callable2 = new ParserWin764SocketCrashDMP(this.getDumpFormat(), this.getOs(), new TranslatorWin64(this.getOs(), this.getDumpFormat()), (ParserWin64Connection) this.getHelperParsers().get(EntityWindows764TCPConnection._TAG), this.getEntities(), initialOffset, maxOffset / 4);
                    ScheduledFuture<Map<Integer, Entity>> future2 = pool.schedule(callable2, delaySeconds, TimeUnit.SECONDS);
                    set.add(future2);

                    Callable<Map<Integer, Entity>> callable3 = new ParserWin764SocketCrashDMP(this.getDumpFormat(), this.getOs(), new TranslatorWin64(this.getOs(), this.getDumpFormat()), (ParserWin64Connection) this.getHelperParsers().get(EntityWindows764TCPConnection._TAG), this.getEntities(), maxOffset / 4 + 1, maxOffset / 2);
                    ScheduledFuture<Map<Integer, Entity>> future3 = pool.schedule(callable3, delaySeconds, TimeUnit.SECONDS);
                    set.add(future3);

                    Callable<Map<Integer, Entity>> callable4 = new ParserWin764SocketCrashDMP(this.getDumpFormat(), this.getOs(), new TranslatorWin64(this.getOs(), this.getDumpFormat()), (ParserWin64Connection) this.getHelperParsers().get(EntityWindows764TCPConnection._TAG), this.getEntities(), maxOffset / 2 + 1, 3 * maxOffset / 4);
                    ScheduledFuture<Map<Integer, Entity>> future4 = pool.schedule(callable4, delaySeconds, TimeUnit.SECONDS);
                    set.add(future4);

                    Callable<Map<Integer, Entity>> callable5 = new ParserWin764SocketCrashDMP(this.getDumpFormat(), this.getOs(), new TranslatorWin64(this.getOs(), this.getDumpFormat()), (ParserWin64Connection) this.getHelperParsers().get(EntityWindows764TCPConnection._TAG), this.getEntities(), 3 * maxOffset / 4 + 1, maxOffset);
                    ScheduledFuture<Map<Integer, Entity>> future5 = pool.schedule(callable5, delaySeconds, TimeUnit.SECONDS);
                    set.add(future5);
                }

                initialOffset = -1;
                maxOffset = -1;
            }

            /*initialOffset = Conversor.hexToLong("ed90d9a0");
             maxOffset = Conversor.hexToLong("ed90f9a0");
             String itemContentDTB = null;
             if (dumpFormat.getHeader() != null) {

             itemContentDTB = (String) dumpFormat.getHeader().getItems().get("DIRECTORY_TABLE_BASE").getContent();
             }
             TranslatorWin64 translator = new TranslatorWin64(this.getOs(), this.getDumpFormat());
             if (itemContentDTB != null) {
             translator.setDTB(Conversor.hexToLong(itemContentDTB));
             }

             Callable<Map<Integer, Entity>> callable = new ParserWin764SocketCrashDMP(this.getDumpFormat(), this.getOs(), translator, (ParserWin64Connection) this.getHelperParsers().get(EntityWindows764TCPConnection._TAG), this.getEntities(), initialOffset, maxOffset);
            
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
            this.notifyObservers("Finalizó la búsqueda de sockets " + cal.getTime().toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (this.getEntities() != null) {
            this.notifyObservers("Sockets encontrados = " + this.getEntities().size() + ".");
        } else {
            this.notifyObservers("ERROR. Sockets encontrados = 0.");
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

            String tag = this._tagManager.getTagItems().get("TCPSocket").getTag();
            String pageContent = "";
            EntityAddressSpaceWin64 aS = null;

            int encontrado = 0;
            while (pageOffset <= maxOffset) {
                //System.out.println(pageOffset + " ");

                pageContent = this.getDumpFormat().getRandomFileSeeker().getContent(pageOffset, this._pageSize * this._CANT_PAGES);

                int index = pageContent.indexOf(tag);
                int lastIndex = index;
                long initialAbsoluteOffset = 0;
                long absoluteOffset = 0;
                long absoluteAttributeOffset = 0;
                long relativeOffset = 0;

                while (index > -1) {
                    relativeOffset = index;
                    /**
                     * Offset del comienzo del Pool
                     */
                    long offsetPrueba = pageOffset + relativeOffset + poolManager._POOL_HEADER_SIZE;;

                    initialAbsoluteOffset = pageOffset + relativeOffset - 0x004 + poolManager._POOL_HEADER_SIZE;
                    EntityWindows764Socket entitySocket = new EntityWindows764Socket();

                    boolean positivo = this.isValidStructure(offsetPrueba);

                    if (positivo) {//Si coincide, continua con el mapeo de la estructura al objeto

                        Iterator<Map.Entry<String, EntityAttribute>> i = entitySocket.getAttributes().entrySet().iterator();
                        while (i.hasNext()) {
                            absoluteOffset = initialAbsoluteOffset;
                            /**
                             * Por cada item, le solicta a randomFileSeeker que
                             * obtenga el contenido
                             */

                            Map.Entry<String, EntityAttribute> e = (Map.Entry<String, EntityAttribute>) i.next();

                            this.getEntryAttributeContent(absoluteOffset, e);
                        }

                        /**
                         * A partir de acá, los atributos de tipo estrucutra
                         * compleja y otros atributos
                         */
                        aS = new EntityAddressSpaceWin64();
                        BigInteger pA = this.getDumpFormat().getPhysicalAddresByOffset(absoluteOffset);
                        aS.setPhysicalAddress(pA);
                        aS.setPhysicalAddressHex(Conversor.bigIntToHexString(pA));
                        long physicalAddressFileOffsetAS = this.getDumpFormat().getFileOffset(aS.getPhysicalAddress());
                        aS.setOffsetInFile(physicalAddressFileOffsetAS);
                        aS.setOffsetInFileHex(Conversor.longToHexString(physicalAddressFileOffsetAS));
                        entitySocket.setaS(aS);

                        String ownerVA = (String) entitySocket.getAttributes().get("OwnerVA").getContent();
                        EntityProcess p = (EntityProcess) entitySocket.getAttributes().get("Owner").getEntity();
                        if (p.getaS() != null) {
                            p.getaS().setVirtualAddressHex(ownerVA);
                            p.getaS().setVirtualAddress(Conversor.hexToBigInteger(ownerVA));
                            entitySocket.getAttributes().get("Owner").setEntity(p);
                            entitySocket.getAttributes().get("Owner").setContent(p);
                        }
                        /**
                         * Agrega entidad al HashMap
                         */
                        //this.getEntities().put(entitySocket.getLocalAddress(), entitySocket);
                        this.getEntities().put(encontrado++, entitySocket);

                        /**
                         * Notifica observadores
                         */
                        this.notifyObservers(entitySocket);

                        //System.out.println("Socket Encontrado. absoluteOffset: " + String.valueOf(absoluteOffset));
                    } else {//Lo marca como falso positivo y realiza un mapeo mínimo
                        entitySocket.setFalsePositive(true);
                    }
                    lastIndex = index; //Se queda con el índice anterior
                    index = pageContent.indexOf(tag, index + tag.length());//Busca otra ocurrencia más adelante en la página
                    //}
                }
                /**
                 * Se mueve por offset, por lo que no es necesario realizar
                 * cálculo
                 */
                pageOffset = pageOffset + this._pageSize * this._CANT_PAGES;
            }
        } catch (Exception ex) {
            ex.getMessage();
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

        if (content == null) {
            positivo = false;
        } else {
            if (content.equals("    ") || content.equals("")) {
                positivo = false;
            }
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
            entry.getValue().getEntity().setParser(this.getHelperParsers().get(EntityWindows764Process._TAG));
        } else {
            if (entry.getValue().getEntity() instanceof EntityWindows764TCPConnection) {
                entry.getValue().getEntity().setParser(this.getHelperParsers().get(EntityWindows764TCPConnection._TAG));
            } else {
                entry.getValue().getEntity().setParser(this);
            }
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
    public EntityConnection getConnection(long offset, Map.Entry<String, EntityAttribute> e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityProcess getProcessByOffset(long offset) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
            pointerMultiplicity = e.getValue().getPointerMultiplicity() - 1;//resta 1 porque en el caso de Socket, apunta directamente a Local Address
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
            content = this.getDumpFormat().getDataManager().getComplexItemContent(e.getValue().getComplexContentType(), e.getValue().getContentType(), absoluteAttributeOffset, e.getValue().getLength(), e.getValue().isUnion(), e.getValue().isBigEndian());

            if (content != null) {
                e.getValue().setContent(content);
            }

        }

        return localAddress;
    }

    @Override
    public boolean validateAttributeContent(Map.Entry<String, EntityAttribute> attribute) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

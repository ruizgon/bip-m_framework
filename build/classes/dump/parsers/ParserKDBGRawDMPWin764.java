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
package dump.parsers;

import dump.formats.CrashDump64;
import dump.formats.DumpFormat;
import dump.formats.RawDumpWin7;
import dump.ooss.OperatingSystemStructure;
import dump.ooss.Windows764OS;
import dump.parsers.process.ParserWin764ActiveProcessCrashDMP;
import dump.parsers.process.ParserWin764ActiveProcessRawDMP;
import dump.parsers.utils.pools.PoolManagerWin64;
import dump.parsers.utils.tags.TagManagerWin64;
import entities.Entity;
import entities.EntityAttribute;
import entities.EntityKDBG;
import entities.EntityKDBGWin64;
import entities.EntityListWin64;
import entities.process.EntityKPROCESS;
import entities.process.EntityProcess;
import entities.process.EntityWin764KPROCESS;
import entities.process.EntityWindows764Process;
import entities.translation.EntityAddressSpaceWin64;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import system.utils.Conversor;
import system.utils.DataManager;
import system.utils.DateManager;
import translation.Translator;
import translation.TranslatorWin64;

/**
 *
 * @author Gonzalo
 */
public class ParserKDBGRawDMPWin764 extends ParserWin64 {

    private int _CANT_PAGES = 8;
    private TagManagerWin64 _tagManager;
    private long _pageOffset = 0;

    public ParserKDBGRawDMPWin764(DumpFormat _dumpFormat, OperatingSystemStructure _os) {
        super(_dumpFormat, _os);
        this._tagManager = new TagManagerWin64();

        /**
         * Registra observadores
         */
        this.addObserver(output.OutputManagerConsole.getInstance());
    }

    public ParserKDBGRawDMPWin764(DumpFormat _dumpFormat, OperatingSystemStructure _os, Translator _translator) {
        super(_dumpFormat, _os, _translator);
        this._tagManager = new TagManagerWin64();

        /**
         * Registra observadores
         */
        this.addObserver(output.OutputManagerConsole.getInstance());
    }

    @Override
    public void parse(Object[] params) {
        ConcurrentHashMap<Object, Entity> entities = null;
        
        try {
            entities = new ConcurrentHashMap<Object, Entity>();
            
            Windows764OS os = (Windows764OS) this.getOs();
            PoolManagerWin64 poolManager = (PoolManagerWin64) os.getPoolManager();
            TranslatorWin64 translator = (TranslatorWin64) this.getTranslator();

            String tag = this._tagManager.getTagItems().get("KernelDebugger").getTag();
            long poolTagOffset = poolManager.getAttributes().get("PoolTag").getPosition();
            String pageContent = "";
            EntityAddressSpaceWin64 aS = null;
            String content = null;

            long pageOffset = 0;
            long maxOffset = this.getDumpFormat().getRandomFileSeeker().obtainFileLength();
            long iniPageOffset = maxOffset;//Comenzará la búsqueda desde el final
            long toPageOffset = maxOffset;//Comenzará la búsqueda desde el final
            int lengthToRead = this._pageSize * this._CANT_PAGES;
            iniPageOffset -= lengthToRead;
            long KDBGOffset = 0;

            Calendar cal = new GregorianCalendar();
            this.setStatus("Iniciado.");
            this.setStatusDetail("Buscando estructura KDBG: " + cal.getTime().toString() + ".");
            String dateString = DateManager.getActualDateWithISOFormat();
            StringBuilder log = new StringBuilder();
            log.append(this.getStatus() + " " + this.getStatusDetail() + " " + dateString);

            this.notifyObservers(log.toString());
            boolean valid = false;
            boolean pBool = false;
            EntityKDBG entity = null;
            while (pageOffset <= maxOffset && !valid) {

                if (KDBGOffset <= 0) {
                    pageContent = this.getDumpFormat().getRandomFileSeeker().getContent(pageOffset, lengthToRead);

                    int index = pageContent.indexOf(tag);

                    if (index >= 0) {
                        KDBGOffset = pageOffset + index;
                        entity = this.buildEntityKDBGByOffset(KDBGOffset);
                    }
                }

                if (KDBGOffset > 0) {
                    String tagP = this._tagManager.getTagItems().get("Process").getTag();

                    EntityProcess p = null;
                    int cantProc = 0;
                    //Mientras no encuentre una estructura EPROCESS p
                    while (p == null || cantProc == 0) {
                        cantProc++;
                        p = this.findProcessByTag(tagP, 3177443328L, 3177476096L);
                        if (p != null) {
                            pBool = true;
                        }
                        if (this._pageOffset < toPageOffset) {
                            iniPageOffset = this._pageOffset;

                        } else {
                            toPageOffset = iniPageOffset;
                            iniPageOffset -= lengthToRead;
                        }
                    }

                    long DTB = 0;

                    try {
                        EntityWin764KPROCESS kProcess = (EntityWin764KPROCESS) p.getAttributes().get("KPROCESS").getContent();
                        String DTBHex = (String) kProcess.getAttributes().get("DirectoryTableBase").getContent();
                        DTB = Conversor.hexToLong(DTBHex);
                    } catch (Exception ex) {
                        this.notifyObservers(ex);
                    }

                    this.getTranslator().setDumpFormat(this.getDumpFormat());
                    translator = (TranslatorWin64) this.getTranslator();
                    translator.setDTB(DTB);
                    this.setTranslator(translator);
                    if (this.isValidDTB(DTB, entity)) {
                        valid = true;
                        entity.setDTB(DTB);
                        entities.put("KDBG",entity);
                        this.setEntities(entities);//put de kdbg
                    } else {
                        translator.setDTB(0);
                        this.setTranslator(translator);
                        valid = false;
                    }
                }

                if (pBool) {
                    if (this._pageOffset >= toPageOffset) {
                        pageOffset += lengthToRead;
                        pBool = false;
                    }
                } else {
                    pageOffset += lengthToRead;
                }
            }
        } catch (Exception ex) {
            this.notifyObservers(ex);
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Entity parseEntityByOffset(long offset, Entity entity, Map.Entry<String, EntityAttribute> attribute) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isValidStructure(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean validateAttributeContent(Map.Entry<String, EntityAttribute> attribute) {
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
                entry.getValue().getEntity().setParser(new ParserWin764ActiveProcessRawDMP((RawDumpWin7) this.getDumpFormat(), (Windows764OS) this.getOs(), (TranslatorWin64) this.getTranslator()));
            }
        } else {
            entry.getValue().getEntity().setParser(this);
        }

        return entry;
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object call() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public EntityKDBG buildEntityKDBGByOffset(long offset) {
        EntityKDBG kdbg = null;
        try {
            kdbg = new EntityKDBGWin64();
            String tagLast = DataManager.getInstance().getItemContent("int 8", offset + 5, 3, false, true);

            long initialOffset = offset + 8;
            if (tagLast.equals("030000")) {
                Iterator<Entry<String, EntityAttribute>> i = kdbg.getAttributes().entrySet().iterator();
                while (i.hasNext()) {
                    Entry<String, EntityAttribute> e = (Entry<String, EntityAttribute>) i.next();

                    this.getEntryAttributeContent(initialOffset, e);
                }
            }
        } catch (Exception ex) {
            this.notifyObservers(ex);
        }
        return kdbg;
    }

    public EntityProcess getEntityProcessByOffset(long offset) {
        EntityProcess p = null;

        try {
            p = new EntityWindows764Process();
        } catch (Exception ex) {
            this.notifyObservers(ex);
        }
        return p;
    }

    public boolean isValidDTB(long DTB, EntityKDBG kdbg) {
        boolean valid = true;

        ConcurrentHashMap<Object, Entity> entities = null;

        try {
            entities = new ConcurrentHashMap<Object, Entity>();

            RawDumpWin7 rawDump = (RawDumpWin7) this.getDumpFormat();

            /**
             *
             * Obtiene dirección virtual de PsActiveProcessHead desde el
             * encabezado del dump Es el puntero al primer elemento de la lista
             * doblemente enlazada de procesos activos en memoria
             */
            String psActiveProcessHeadString = (String) kdbg.getAttributes().get("PsActiveProcessHead").getContent();

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
                while (activeProcessLinks != null && !valid) {
                    EntityWindows764Process entityProcess = new EntityWindows764Process();
                    entityProcess.getAttributes().get("KPROCESS").setEnabledParse(true);

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

                    Iterator<Map.Entry<String, EntityAttribute>> i = entityProcess.getAttributes().entrySet().iterator();
                    while (i.hasNext()) {
                        absoluteOffset = initialAbsoluteOffset;
                        /**
                         * Por cada item, le solicta a randomFileSeeker que
                         * obtenga el contenido
                         */

                        Map.Entry<String, EntityAttribute> e = (Map.Entry<String, EntityAttribute>) i.next();

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
                    EntityWin764KPROCESS kProcess = (EntityWin764KPROCESS) entityProcess.getAttributes().get("KPROCESS").getContent();
                    String processDTBHex = (String) kProcess.getAttributes().get("DirectoryTableBase").getContent();
                    long processDTB = Conversor.hexToLong(processDTBHex);
                    /**
                     * TODO mecanisno para validar DTB. Puede ser que verofoque
                     * si blonk vielva a offset.oroginal de psactive prcesshead
                     */
                    valid = true;

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

        return valid;
    }

    public EntityProcess findProcessByTag(String tag, long fromOffset, long toOffset) {
        EntityProcess entityProcess = null;
        try {
            String pageContent = null;
            long initialAbsoluteOffset = 0;
            long pageOffset = fromOffset;
            long maxOffset = this.getDumpFormat().getRandomFileSeeker().obtainFileLength();
            int lengthToRead = this._pageSize * this._CANT_PAGES;
            long KDBGOffset = 0;

            Windows764OS os = (Windows764OS) this.getOs();
            PoolManagerWin64 poolManager = (PoolManagerWin64) os.getPoolManager();
            long poolTagOffset = poolManager.getAttributes().get("PoolTag").getPosition();
            EntityWindows764Process eProcess = new EntityWindows764Process();
            int kProcessPos = eProcess.getAttributes().get("KPROCESS").getPosition();
            int index = -1;
            while (pageOffset <= toOffset && entityProcess == null) {

                pageContent = this.getDumpFormat().getRandomFileSeeker().getContent(pageOffset, lengthToRead);

                index = pageContent.indexOf(tag);

                if (index >= 0) {
                    pageOffset = pageOffset + index - poolTagOffset + poolManager._MIN_SIZE_STRUCTURE + kProcessPos;
                    initialAbsoluteOffset = pageOffset;
                    entityProcess = getProcess(initialAbsoluteOffset);
                    if (entityProcess == null) {
                        initialAbsoluteOffset += poolManager._OBJECT_HEADER_PROCESS_INFO;
                        entityProcess = getProcess(initialAbsoluteOffset);
                        if (entityProcess == null) {
                            initialAbsoluteOffset += poolManager._OBJECT_HEADER_HANDLE_INFO;
                            entityProcess = getProcess(initialAbsoluteOffset);
                        }
                    }
                } else {
                    pageOffset += lengthToRead;
                }
                this._pageOffset = pageOffset;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            entityProcess = null;
        }
        return entityProcess;
    }

    public EntityProcess getProcess(long offset) {
        EntityProcess entityProcess = null;

        try {
            entityProcess = new EntityWindows764Process();
            long absoluteOffset = 0;

            entityProcess.getAttributes().get("KPROCESS").setEnabledParse(true);

            Iterator<Entry<String, EntityAttribute>> i = entityProcess.getAttributes().entrySet().iterator();
            absoluteOffset = offset;
            while (i.hasNext()) {
                /**
                 * Por cada item, le solicta a randomFileSeeker que obtenga el
                 * contenido
                 */

                Entry<String, EntityAttribute> e = (Entry<String, EntityAttribute>) i.next();

                this.getEntryAttributeContent(absoluteOffset, e);
            }
            entityProcess.setStateLabel("Active");
            String imageFileName = (String) entityProcess.getAttributes().get("ImageFileName").getContent();
            if (!imageFileName.equals("System         ")) {
                entityProcess = null;
            }

            if (entityProcess != null) {
                /**
                 * A partir de acá, los atributos de tipo estructura compleja y
                 * otros atributos
                 */
                /*EntityListWin32 psActiveProcessHead = (EntityListWin32) this.getListContent(offset);
                 EntityAddressSpaceWin32 aS = (EntityAddressSpaceWin32) this.getTranslator().obtainAddressSpace(BigInteger.valueOf(psActiveProcessHead.getfLink()));
                 entityProcess.setaS(aS);*/
                EntityAddressSpaceWin64 aS = new EntityAddressSpaceWin64();
                aS.setOffsetInFile(absoluteOffset);
                aS.setOffsetInFileHex(Conversor.longToHexString(absoluteOffset));
                BigInteger pA = this.getDumpFormat().getPhysicalAddresByOffset(absoluteOffset);
                aS.setPhysicalAddress(pA);
                aS.setPhysicalAddressHex(Conversor.bigIntToHexString(pA));
                entityProcess.setaS(aS);

                if (entityProcess.getAttributes().get("UniqueProcessId").getContent() != null) {
                    entityProcess.setProcessID(Conversor.hexToLong(entityProcess.getAttributes().get("UniqueProcessId").getContent().toString()));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            entityProcess = null;
        }

        return entityProcess;
    }
}

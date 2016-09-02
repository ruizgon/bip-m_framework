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
import dump.formats.HeaderCrashDump32;
import dump.ooss.Windows732OS;
import entities.Entity;
import entities.EntityAttribute;
import entities.EntityListWin32;
import entities.process.EntityDispatcherHeader;
import entities.process.EntityKTHREAD;
import entities.process.EntityProcess;
import entities.process.EntityWin732DispatcherHeader;
import entities.process.EntityWin732KTHREAD;
import entities.process.EntityWindows732Thread;
import entities.process.EntityWindowsThread;
import entities.translation.EntityAddressSpaceWin32;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import system.utils.Conversor;
import system.utils.DataManager;
import system.utils.DataPrimitiveType;
import translation.TranslatorWin32;

/**
 *
 * @author Gonzalo
 */
public class ParserWin732ThreadCrashDMP extends ParserWin32Thread {

    private Windows732OS _os;

    public ParserWin732ThreadCrashDMP(CrashDump32 _dumpFormat, Windows732OS _os) {
        super(_dumpFormat, _os);

        /**
         * Registra observadores
         */
        this.addObserver(output.OutputManagerDump.getInstance());
        this.addObserver(output.OutputManagerConsole.getInstance());
    }

    public ParserWin732ThreadCrashDMP(CrashDump32 _dumpFormat, Windows732OS _os, TranslatorWin32 _translator) {
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void parse(Entity entity, Object[] params) {

        if (params.length > 0) {
            if (params[0] instanceof EntityProcess) {
                this.getThreadsByProcess((EntityProcess) params[0]);
            }
        }
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
        EntityWindows732Thread entityThread = null;
        try {
            entityThread = new EntityWindows732Thread();
            long initialAbsoluteOffset = offset - entityThread.getAttributes().get("ThreadListEntry").getPosition();
            long absoluteOffset = 0;

            Iterator<Map.Entry<String, EntityAttribute>> i = entityThread.getAttributes().entrySet().iterator();
            while (i.hasNext()) {
                absoluteOffset = initialAbsoluteOffset;
                /**
                 * Por cada item, le solicita a randomFileSeeker que obtenga el
                 * contenido
                 */

                Map.Entry<String, EntityAttribute> e = (Map.Entry<String, EntityAttribute>) i.next();

                this.getEntryAttributeContent(absoluteOffset, e);
            }

            EntityListWin32 list = (EntityListWin32) entityThread.getAttributes().get("ThreadListEntry").getContent();

            long threadListOffset = 0;
            List<EntityWindowsThread> threadList = new ArrayList<EntityWindowsThread>();

            BigInteger pA = this.getTranslator().calculatePhysicalAddress(list.getfLinkHex());
            threadListOffset = this.getDumpFormat().getFileOffset(pA);

            EntityAddressSpaceWin32 aS = new EntityAddressSpaceWin32();
            aS.setVirtualAddressHex(list.getfLinkHex());
            aS.setVirtualAddress(Conversor.hexToBigInteger(list.getfLinkHex()));
            aS.setPhysicalAddress(pA);
            aS.setPhysicalAddressHex(Conversor.bigIntToHexString(pA));
            aS.setOffsetInFile(offset);
            aS.setOffsetInFileHex(Conversor.longToHexString(offset));

            while (threadListOffset != offset) {
                EntityWindows732Thread threadChild = new EntityWindows732Thread();
                long newInitialAbsoluteOffset = threadListOffset - threadChild.getAttributes().get("ThreadListEntry").getPosition();
                long newAbsoluteOffset = 0;

                Iterator<Map.Entry<String, EntityAttribute>> j = threadChild.getAttributes().entrySet().iterator();
                while (j.hasNext()) {
                    newAbsoluteOffset = newInitialAbsoluteOffset;
                    /**
                     * Por cada item, le solicita a randomFileSeeker que obtenga
                     * el contenido
                     */

                    Map.Entry<String, EntityAttribute> e = (Map.Entry<String, EntityAttribute>) j.next();

                    this.getEntryAttributeContent(newAbsoluteOffset, e);
                }
                threadChild.setProcessID(entityThread.getProcessID());
                list = (EntityListWin32) threadChild.getAttributes().get("ThreadListEntry").getContent();
                pA = this.getTranslator().calculatePhysicalAddress(list.getfLinkHex());
                threadListOffset = this.getDumpFormat().getFileOffset(pA);

                threadChild.setaS(aS);
                threadList.add(threadChild);

                aS = new EntityAddressSpaceWin32();
                aS.setVirtualAddressHex(list.getfLinkHex());
                aS.setVirtualAddress(Conversor.hexToBigInteger(list.getfLinkHex()));
                aS.setPhysicalAddress(pA);
                aS.setPhysicalAddressHex(Conversor.bigIntToHexString(pA));
                aS.setOffsetInFile(threadListOffset);
                aS.setOffsetInFileHex(Conversor.longToHexString(threadListOffset));
            }
            entityThread.setThreadList(threadList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return entityThread;
    }

    @Override
    public boolean isValidStructure(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Entity getEntityByAttributeValue(Entity entity, String attribute, Object content) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map.Entry<String, EntityAttribute> setParserAttribute(Map.Entry<String, EntityAttribute> entry) {
        if (entry.getValue().getEntity() instanceof EntityWin732KTHREAD) {
            if (entry.getValue().getEntity().getParser() == null) {
                entry.getValue().getEntity().setParser(new ParserWin732ThreadCrashDMP((CrashDump32) this.getDumpFormat(), (Windows732OS) this.getOs(), (TranslatorWin32) this.getTranslator()));
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

    @Override
    public ConcurrentHashMap<Object, Entity> getThreadsByProcess(EntityProcess process) {

        ConcurrentHashMap<Object, Entity> entities = null;

        try {
            entities = new ConcurrentHashMap<Object, Entity>();
            EntityWindows732Thread entityThread = null;

            Iterator<Map.Entry<String, EntityAttribute>> iProcess = process.getAttributes().entrySet().iterator();
            long initialAbsoluteOffsetProcess = Conversor.hexToLong(process.getaS().getOffsetInFileHex()) - 0x0B8;
            long absoluteOffsetProcess = 0;
            while (iProcess.hasNext()) {
                absoluteOffsetProcess = initialAbsoluteOffsetProcess;
                /**
                 * Por cada item, le solicta a randomFileSeeker que obtenga el
                 * contenido
                 */

                Map.Entry<String, EntityAttribute> entryProcess = (Map.Entry<String, EntityAttribute>) iProcess.next();
                if (entryProcess.getKey().equals("ThreadListHead")) {
                    entryProcess.getValue().setEnabledParse(true);
                    this.getEntryAttributeContent(absoluteOffsetProcess, entryProcess);
                    entityThread = (EntityWindows732Thread) entryProcess.getValue().getContent();
                    String vA = DataManager.getInstance().getItemContent("pointer32", absoluteOffsetProcess + entryProcess.getValue().getPosition(), 1, false);
                    EntityAddressSpaceWin32 aS = (EntityAddressSpaceWin32) this.getTranslator().obtainAddressSpace(Conversor.hexToBigInteger(vA));
                    long oIF = this.getDumpFormat().getFileOffset(aS.getPhysicalAddress());
                    aS.setOffsetInFile(oIF);
                    aS.setOffsetInFileHex(Conversor.longToHexString(oIF));
                    entityThread.setProcessID(process.getProcessID());
                    entityThread.setaS(aS);
                }
            }

            /**
             * Hasta aquí, se obtiene el primer EntityThread de la lista.
             * Realizar iteración para recorre
             */
            EntityListWin32 threadListEntryHead = (EntityListWin32) this.getListContent(absoluteOffsetProcess + process.getAttributes().get("ThreadListHead").getPosition());
            EntityAddressSpaceWin32 aS = (EntityAddressSpaceWin32) this.getTranslator().obtainAddressSpace(BigInteger.valueOf(threadListEntryHead.getfLink()));
            long physicalAddressFileOffsetAS = this.getDumpFormat().getFileOffset(entityThread.getaS().getPhysicalAddress());

            int indexMap = 0;
            entities.put(indexMap, entityThread);

            /**
             * Notifica observadores
             */
            this.notifyObservers(entityThread);

            /**
             * Comienza el reocrrido de la lista de threads
             */
            long initialAbsoluteOffset = 0;
            long absoluteOffset = 0;
            boolean firstReference = true;
            long nextOffset = this.getDumpFormat().getFileOffset(getNextEntityAddress(threadListEntryHead, EntityWindows732Thread._ENTITY_LIST_OFFSET));
            long nextOffsetComparator = entityThread.getaS().getOffsetInFile() - EntityWindows732Thread._ENTITY_LIST_OFFSET;
            EntityListWin32 threadEntry = threadListEntryHead;

            //Inicializa índice para key del HashMap entities
            int ind = 0;
            while (threadEntry != null) {
                if (nextOffset == nextOffsetComparator) {
                    break;
                }
                entityThread = new EntityWindows732Thread();

                /**
                 * Busca la estructura _LIST_ENTRY (ThreadListEntry)
                 */
                if (!firstReference && nextOffset == nextOffsetComparator) {
                    break;
                }
                long absoluteAttributeOffset = nextOffset + entityThread.getAttributes().get("ThreadListEntry").getPosition();
                threadEntry = (EntityListWin32) getListContent(absoluteAttributeOffset);
                entityThread.getAttributes().get("ThreadListEntry").setContent(threadEntry);

                initialAbsoluteOffset = nextOffset;

                Iterator<Map.Entry<String, EntityAttribute>> i = entityThread.getAttributes().entrySet().iterator();
                while (i.hasNext()) {
                    absoluteOffset = initialAbsoluteOffset;
                    /**
                     * Por cada item, le solicta a randomFileSeeker que obtenga
                     * el contenido
                     */

                    Map.Entry<String, EntityAttribute> e = (Map.Entry<String, EntityAttribute>) i.next();

                    this.getEntryAttributeContent(absoluteOffset, e);
                }

                /**
                 * A partir de acá, los atributos de tipo estructura compleja y
                 * otros atributos
                 */
                aS = (EntityAddressSpaceWin32) this.getTranslator().obtainAddressSpace(Conversor.hexToBigInteger(threadEntry.getfLinkHex()));
                long oIF = this.getDumpFormat().getFileOffset(aS.getPhysicalAddress());
                aS.setOffsetInFile(oIF);
                aS.setOffsetInFileHex(Conversor.longToHexString(oIF));
                entityThread.setaS(aS);
                entityThread.setaS(aS);

                /**
                 * Agrega entidad al HashMap
                 */
                entities.put(indexMap++, entityThread);

                /**
                 * Notifica observadores
                 */
                this.notifyObservers(entityThread);

                nextOffset = this.getDumpFormat().getFileOffset(getNextEntityAddress(threadEntry, EntityWindows732Thread._ENTITY_LIST_OFFSET));

                /**
                 * Ya no es a referencia al primer nodo
                 */
                if (firstReference) {
                    firstReference = false;
                }
            }
            this.setEntities(entities);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return entities;
    }

    @Override
    public EntityKTHREAD obtainKTHREADContent(long offset, Map.Entry<String, EntityAttribute> e) {
        EntityWin732KTHREAD kThread = null;

        try {
            kThread = new EntityWin732KTHREAD();
            long initialAbsoluteOffsetThread = offset;
            long absoluteOffsetThread = 0;
            Iterator<Map.Entry<String, EntityAttribute>> i = kThread.getAttributes().entrySet().iterator();
            while (i.hasNext()) {
                absoluteOffsetThread = initialAbsoluteOffsetThread;
                /**
                 * Por cada item, le solicta a randomFileSeeker que obtenga el
                 * contenido
                 */

                Map.Entry<String, EntityAttribute> entry = (Map.Entry<String, EntityAttribute>) i.next();

                this.getEntryAttributeContent(absoluteOffsetThread, entry);
                this.validateAttributeContent(entry);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return kThread;
    }

    @Override
    public EntityDispatcherHeader obtainDispatcherHeader(long offset, Map.Entry<String, EntityAttribute> e) {
        return new EntityWin732DispatcherHeader();
    }

    @Override
    public boolean validateAttributeContent(Map.Entry<String, EntityAttribute> attribute) {
        boolean valid = false;

        try {
            Object content = attribute.getValue().getContent();
            String contentStr = null;
            if (content instanceof String) {
                contentStr = (String) content;
            }
            String contentType = attribute.getValue().getContentType();
            contentType = DataPrimitiveType.getInstance().getPrimitiveType(contentType);

            BigInteger address = BigInteger.ZERO;
            long offset = 0;
            switch (contentType) {
                case "int 32":
                    address = this.getTranslator().calculatePhysicalAddress(contentStr);
                    offset = this.getDumpFormat().getFileOffset(address);
                    break;
                case "int 64":
                    address = this.getTranslator().calculatePhysicalAddress(contentStr);
                    offset = this.getDumpFormat().getFileOffset(address);
                    break;
                case "pointer32":
                    address = this.getTranslator().calculatePhysicalAddress(contentStr);
                    offset = this.getDumpFormat().getFileOffset(address);
                    break;
                case "pointer64":
                    address = this.getTranslator().calculatePhysicalAddress(contentStr);
                    offset = this.getDumpFormat().getFileOffset(address);
                    break;
                case "RVApointer32":
                    address = this.getTranslator().calculatePhysicalAddress(contentStr);
                    offset = this.getDumpFormat().getFileOffset(address);
                    break;
                case "RVApointer64":
                    address = this.getTranslator().calculatePhysicalAddress(contentStr);
                    offset = this.getDumpFormat().getFileOffset(address);
                    break;
                default:
                    offset = 1;
                    valid = true;
            }

            if (offset < HeaderCrashDump32._HEADER_SIZE) {
                valid = false;
            } else {
                valid = true;
            }

            attribute.getValue().setValid(valid);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return valid;
    }
}

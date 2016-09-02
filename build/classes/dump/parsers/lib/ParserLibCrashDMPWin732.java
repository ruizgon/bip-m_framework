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
package dump.parsers.lib;

import dump.formats.CrashDump32;
import dump.formats.DumpFormat;
import dump.formats.HeaderCrashDump32;
import dump.ooss.OperatingSystemStructure;
import dump.ooss.Windows732OS;
import dump.parsers.ParserWin32;
import dump.parsers.interfaces.IEntityParserVisitor;
import dump.parsers.interfaces.IParserLib;
import dump.parsers.process.ParserWin732ActiveProcessCrashDMP;
import entities.Entity;
import entities.EntityAttribute;
import entities.EntityListWin32;
import entities.connection.EntityConnection;
import entities.connection.EntitySocket;
import entities.lib.EntityLib;
import entities.lib.EntityLibWindows732DLL;
import entities.malware.EntityMalware;
import entities.malware.EntitySDE;
import entities.malware.EntitySSDT;
import entities.malware.EntityWin732SDE;
import entities.malware.EntityWin732SSDT;
import entities.malware.EntityWindowsFunction;
import entities.malware.EntityWindowsFunctionOperation;
import entities.process.EntityPebLdrDataWindows732;
import entities.process.EntityProcess;
import entities.process.EntityThread;
import entities.process.EntityWin732KPROCESS;
import entities.process.EntityWindows732PEB;
import entities.process.EntityWindows732Process;
import entities.translation.EntityAddressSpace;
import entities.translation.EntityAddressSpaceWin32;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import system.utils.Conversor;
import system.utils.DataManager;
import system.utils.DateManager;
import translation.Translator;
import translation.TranslatorWin32;

/**
 *
 * @author Gonzalo
 */
public class ParserLibCrashDMPWin732 extends ParserWin32 implements IParserLib, IEntityParserVisitor {

    private Windows732OS _os;

    public ParserLibCrashDMPWin732(DumpFormat _dumpFormat, OperatingSystemStructure _os) {
        super(_dumpFormat, _os);

        /**
         * Registra observadores
         */
        this.addObserver(output.OutputManagerException.getInstance());
        this.addObserver(output.OutputManagerDump.getInstance());
        this.addObserver(output.OutputManagerConsole.getInstance());
    }

    public ParserLibCrashDMPWin732(DumpFormat _dumpFormat, OperatingSystemStructure _os, Translator _translator) {
        super(_dumpFormat, _os, _translator);

        /**
         * Registra observadores
         */
        this.addObserver(output.OutputManagerException.getInstance());
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
        this.setStatus("Iniciado");
        this.setStatusDetail("Proceso de parseo de dump y obtención de módulos/librerías Windows 7 x86.");
        String dateString = DateManager.getActualDateWithISOFormat();
        StringBuilder log = new StringBuilder();
        log.append(this.getStatus() + " " + this.getStatusDetail() + " " + dateString);

        this.notifyObservers(this.getStatus());
        this.notifyObservers(this.getStatusDetail());
        this.notifyObservers("BaseDllName   FullDllName   LoadTime   VA   PA   FileOffset");
        this.notifyObservers("------------------------------------------------------------");

        ConcurrentHashMap<Object, Entity> entities = null;

        //PS_LOADED_MODULE_LIST
        try {
            entities = new ConcurrentHashMap<Object, Entity>();

            CrashDump32 crashDump = (CrashDump32) this.getDumpFormat();

            /**
             *
             * Obtiene dirección virtual de PS_LOADED_MODULE_LIST desde el
             * encabezado del dump Es el puntero al primer elemento de la lista
             * doblemente enlazada de encabezado de módulos cargados en memoria
             */
            String psLoadedModuleList = (String) crashDump.getHeader().getItems().get("PS_LOADED_MODULE_LIST").getContent();

            /**
             * El traductor calcula dirección física
             */
            BigInteger psLoadedModuleHeadPhysicalAddress = this.getTranslator().calculatePhysicalAddress(psLoadedModuleList);

            long psLoadedModuleHeadAbsoluteOffset = this.getDumpFormat().getFileOffset(psLoadedModuleHeadPhysicalAddress);

            /**
             * Obtiene EntityList
             */
            EntityListWin32 psLoadedModuleHead = (EntityListWin32) this.getListContent(psLoadedModuleHeadAbsoluteOffset);
            EntityAddressSpaceWin32 aS = (EntityAddressSpaceWin32) this.getTranslator().obtainAddressSpace(BigInteger.valueOf(psLoadedModuleHead.getfLink()));
            long physicalAddressFileOffsetAS = this.getDumpFormat().getFileOffset(aS.getPhysicalAddress());
            aS.setOffsetInFile(physicalAddressFileOffsetAS);
            aS.setOffsetInFileHex(Conversor.longToHexString(physicalAddressFileOffsetAS));

            long initialAbsoluteOffset = 0;
            long absoluteOffset = 0;
            int iLibrary = 0;
            if (psLoadedModuleHead != null) {
                boolean firstReference = true;

                BigInteger nextPosition = getNextEntityAddress(psLoadedModuleHead, EntityLibWindows732DLL._ENTITY_LIST_OFFSET);
                long nextOffset = this.getDumpFormat().getFileOffset(nextPosition);
                long nextOffsetComparator = psLoadedModuleHeadAbsoluteOffset - 0x000; //0x000: OFFSET AL INICIO DE LA ESTRUCTURA (está al inicio)
                EntityListWin32 loadedModuleLinks = psLoadedModuleHead;

                //Inicializa índice para key del HashMap entities
                int ind = 0;
                while (loadedModuleLinks != null) {
                    EntityLibWindows732DLL entityLib = new EntityLibWindows732DLL();

                    /**
                     * Busca la estructura _LIST_ENTRY
                     */
                    if (!firstReference && nextOffset == nextOffsetComparator) {
                        break;
                    }
                    long absoluteAttributeOffset = nextOffset + entityLib.getAttributes().get("InLoadOrderLinks").getPosition();
                    loadedModuleLinks = (EntityListWin32) getListContent(absoluteAttributeOffset);

                    initialAbsoluteOffset = nextOffset;

                    Iterator<Map.Entry<String, EntityAttribute>> i = entityLib.getAttributes().entrySet().iterator();
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
                     * A partir de acá, los atributos de tipo estructura
                     * compleja y otros atributos
                     */
                    entityLib.setaS(aS);

                    BigInteger dllBasePA = this.getTranslator().calculatePhysicalAddress((String) entityLib.getAttributes().get("DllBase").getContent());
                    long dllBaseoffset = this.getDumpFormat().getFileOffset(dllBasePA);
                    entityLib.setDllBaseOffset(dllBaseoffset);

                    /**
                     * Calcula nuevo aS para próximo paso
                     */
                    aS = (EntityAddressSpaceWin32) this.getTranslator().obtainAddressSpace(BigInteger.valueOf(loadedModuleLinks.getfLink()));
                    physicalAddressFileOffsetAS = this.getDumpFormat().getFileOffset(aS.getPhysicalAddress());
                    aS.setOffsetInFile(physicalAddressFileOffsetAS);
                    aS.setOffsetInFileHex(Conversor.longToHexString(physicalAddressFileOffsetAS));

                    if (entityLib.getAttributes().get("BaseDllName").getContent() != null) {
                        entityLib.setName(entityLib.getAttributes().get("BaseDllName").getContent().toString());
                    }
                    entities.put(iLibrary++, entityLib);

                    /**
                     * Notifica observadores
                     */
                    this.notifyObservers(entityLib);

                    nextOffset = this.getDumpFormat().getFileOffset(getNextEntityAddress(loadedModuleLinks, EntityLibWindows732DLL._ENTITY_LIST_OFFSET));

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

        this.notifyObservers("Librerías encontradas = " + entities.size() + ".");

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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public void run() {
        this.parse(null);
    }

    @Override
    public Object call() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map.Entry<String, EntityAttribute> setParserAttribute(Map.Entry<String, EntityAttribute> entry) {
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

        try {
            ConcurrentHashMap<Object, Entity> entities = null;
            EntityWindows732Process process = null;
            if (entity instanceof EntitySSDT) {
                StringBuilder log = new StringBuilder();
                log.append("Realizando búsqueda de SSDTs...");
                this.notifyObservers(log);

                entities = new ConcurrentHashMap<Object, Entity>();
                int i = 1;
                for (Object o : params) {

                    BigInteger vA = (BigInteger) o;
                    BigInteger pA = this.getTranslator().calculatePhysicalAddress(vA);
                    long offset = this.getDumpFormat().getFileOffset(pA);

                    if (offset > HeaderCrashDump32._HEADER_SIZE) {
                        EntitySSDT ssdt = new EntityWin732SSDT();
                        EntityAddressSpace aS = new EntityAddressSpaceWin32();
                        aS.setVirtualAddress(vA);
                        ssdt.setaS(aS);
                        ssdt.accept(this);

                        EntitySDE[] sdeList = ssdt.getEntryList();
                        switch (i) {
                            case 1: //KeServiceDescriptorTable
                                ssdt.setDescription("KeServiceDescriptorTable");
                                sdeList[0].setEntryDescription("nt!KeServiceTable");
                                sdeList[1].setEntryDescription("Other");
                                sdeList[2].setEntryDescription("Other");
                                sdeList[3].setEntryDescription("Other");
                                ssdt.setEntryList(sdeList);
                                break;
                            case 2: //KeServiceDescriptorTableShadow
                                ssdt.setDescription("KeServiceDescriptorTableShadow");
                                sdeList[0].setEntryDescription("nt!KeServiceTable (Shadow)");
                                sdeList[1].setEntryDescription("Other");
                                sdeList[2].setEntryDescription("win32k!W32pServiceTable");
                                sdeList[3].setEntryDescription("Other");
                                ssdt.setEntryList(sdeList);
                                break;
                            default:
                                break;
                        }
                        entities.put("ssdt[" + i++ + "]", ssdt);
                    }
                }
                log.append("Búsqueda de SSDTs finalizada.");
                this.notifyObservers(log);
            } else if (params.length > 0) {
                if (params[0] != null) {
                    if (params[0] instanceof EntityWindows732Process) {
                        process = (EntityWindows732Process) params[0];
                        StringBuilder log = new StringBuilder();
                        log.append("Realizando búsqueda de Módulos por proceso ID: " + process.getProcessID() + "...");
                        this.notifyObservers(log);

                        process.accept(this);

                        log.append("Búsqueda de Módulos por proceso ID: " + process.getProcessID() + " finalizada.");
                        this.notifyObservers(log);
                    }
                }
            }

            this.setEntities(entities);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean validateAttributeContent(Map.Entry<String, EntityAttribute> attribute) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(EntityProcess process) {
        /**
         * TODO Implementar para el param[1] espera en qué orden se desea
         * recorrer la lista String tagOrder = (String) params[1];
         */

        ParserWin732ActiveProcessCrashDMP parserProcess = new ParserWin732ActiveProcessCrashDMP((CrashDump32) this.getDumpFormat(), this.getOs(), (TranslatorWin32) this.getTranslator());

        EntityWindows732PEB peb = (EntityWindows732PEB) parserProcess.getPEBByProcess(process);

        EntityPebLdrDataWindows732 ldrData = (EntityPebLdrDataWindows732) peb.getAttributes().get("PebLdrData").getContent();
        EntityListWin32 inLoadOrderModuleList = (EntityListWin32) ldrData.getAttributes().get("InLoadOrderModuleList").getContent();

        this.setStatus("Iniciado");
        this.setStatusDetail("Proceso de obtención de módulos/librerías.");
        String dateString = DateManager.getActualDateWithISOFormat();
        StringBuilder log = new StringBuilder();
        log.append(this.getStatus() + " " + this.getStatusDetail() + " " + dateString);

        this.notifyObservers(this);

        ConcurrentHashMap<Object, Entity> entities = null;

        //PS_LOADED_MODULE_LIST
        try {
            entities = new ConcurrentHashMap<Object, Entity>();

            CrashDump32 crashDump = (CrashDump32) this.getDumpFormat();

            /**
             *
             * Obtiene dirección virtual de PS_LOADED_MODULE_LIST desde el
             * encabezado del dump Es el puntero al primer elemento de la lista
             * doblemente enlazada de encabezado de módulos cargados en memoria
             */
            //String psLoadedModuleList = (String) crashDump.getHeader().getItems().get("PS_LOADED_MODULE_LIST").getContent();
            /**
             * El traductor calcula dirección física
             */
            //long psLoadedModuleHeadPhysicalAddress = this.getTranslator().calculatePhysicalAddress(psLoadedModuleList);
            //long psLoadedModuleHeadAbsoluteOffset = this.getDumpFormat().getFileOffset(psLoadedModuleHeadPhysicalAddress);
            BigInteger psLoadedModuleHeadPhysicalAddress = this.getTranslator().calculatePhysicalAddress(BigInteger.valueOf(inLoadOrderModuleList.getfLink()));

            long psLoadedModuleHeadAbsoluteOffset = this.getDumpFormat().getFileOffset(psLoadedModuleHeadPhysicalAddress);

            /**
             * Obtiene EntityList
             */
            //EntityListWin32 psLoadedModuleHead = (EntityListWin32) this.getListContent(psLoadedModuleHeadAbsoluteOffset);
            EntityListWin32 psLoadedModuleHead = inLoadOrderModuleList;

            EntityAddressSpaceWin32 aS = (EntityAddressSpaceWin32) this.getTranslator().obtainAddressSpace(BigInteger.valueOf(psLoadedModuleHead.getfLink()));
            long physicalAddressFileOffsetAS = this.getDumpFormat().getFileOffset(aS.getPhysicalAddress());
            aS.setOffsetInFile(physicalAddressFileOffsetAS);
            aS.setOffsetInFileHex(Conversor.longToHexString(physicalAddressFileOffsetAS));

            long initialAbsoluteOffset = 0;
            long absoluteOffset = 0;
            int iLibrary = 0;
            if (psLoadedModuleHead != null) {
                boolean firstReference = true;

                BigInteger nextPosition = BigInteger.valueOf(psLoadedModuleHead.getfLink());
                BigInteger nextPhysicalAddress = this.getTranslator().calculatePhysicalAddress(nextPosition);
                long nextOffset = this.getDumpFormat().getFileOffset(nextPhysicalAddress);
                long nextOffsetComparator = psLoadedModuleHeadAbsoluteOffset;
                EntityListWin32 loadedModuleLinks = psLoadedModuleHead;

                //Inicializa índice para key del HashMap entities
                int ind = 0;
                while (loadedModuleLinks != null) {
                    EntityLibWindows732DLL entityLib = new EntityLibWindows732DLL();

                    /**
                     * Busca la estructura _LIST_ENTRY
                     */
                    if (!firstReference && nextOffset == nextOffsetComparator) {
                        break;
                    }
                    long absoluteAttributeOffset = nextOffset + entityLib.getAttributes().get("InLoadOrderLinks").getPosition();
                    loadedModuleLinks = (EntityListWin32) getListContent(absoluteAttributeOffset);

                    initialAbsoluteOffset = nextOffset;

                    Iterator<Map.Entry<String, EntityAttribute>> iDLL = entityLib.getAttributes().entrySet().iterator();
                    while (iDLL.hasNext()) {
                        absoluteOffset = initialAbsoluteOffset;
                        /**
                         * Por cada item, le solicta a randomFileSeeker que
                         * obtenga el contenido
                         */

                        Map.Entry<String, EntityAttribute> entryDLL = (Map.Entry<String, EntityAttribute>) iDLL.next();

                        this.getEntryAttributeContent(absoluteOffset, entryDLL);
                    }

                    /**
                     * A partir de acá, los atributos de tipo estructura
                     * compleja y otros atributos
                     */
                    entityLib.setaS(aS);
                    /**
                     * Calcula nuevo aS para próximo paso
                     */
                    aS = (EntityAddressSpaceWin32) this.getTranslator().obtainAddressSpace(BigInteger.valueOf(loadedModuleLinks.getfLink()));
                    physicalAddressFileOffsetAS = this.getDumpFormat().getFileOffset(aS.getPhysicalAddress());
                    aS.setOffsetInFile(physicalAddressFileOffsetAS);
                    aS.setOffsetInFileHex(Conversor.longToHexString(physicalAddressFileOffsetAS));

                    /**
                     * *****ESTA PARTE LA TENGO QUE TRABAJAR -
                     * REPROGRAMAR************* Agrega entidad al HashMap Aquí
                     * debo colocar un atributo que identifique al módulo. Un
                     * valor factible sería el nómbre del módulo.
                     */
                    if (entityLib.getAttributes().get("BaseDllName").getContent() != null) {
                        entityLib.setName(entityLib.getAttributes().get("BaseDllName").getContent().toString());
                    }
                    entities.put(iLibrary++, entityLib);

                    nextOffset = this.getDumpFormat().getFileOffset(getNextEntityAddress(loadedModuleLinks, EntityLibWindows732DLL._ENTITY_LIST_OFFSET));

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

        this.setEntities(entities);
    }

    @Override
    public void visit(EntitySSDT ssdt) {

        try {
            int cant = 0;
            BigInteger vA = ssdt.getaS().getVirtualAddress();
            BigInteger pA = this.getTranslator().calculatePhysicalAddress(vA);
            long offset = this.getDumpFormat().getFileOffset(pA);
            long initialOffset = 0;
            long shift = 0;
            int cantSDE = 4;
            EntityWin732SDE[] sdeList = new EntityWin732SDE[cantSDE];
            while (cant < 4) {
                initialOffset = offset + shift;
                long currentOffset = initialOffset;

                String sdeVA = DataManager.getInstance().getItemContent("int 32", currentOffset, 1, false);
                currentOffset += 0x004;
                String counterBaseTable = DataManager.getInstance().getItemContent("int 32", currentOffset, 1, false);
                currentOffset += 0x004;
                String serviceLimit = DataManager.getInstance().getItemContent("int 32", currentOffset, 1, false);
                currentOffset += 0x004;
                String argumentTable = DataManager.getInstance().getItemContent("int 32", currentOffset, 1, false);

                EntityWin732SDE sde = new EntityWin732SDE();
                sde.setFunctionTableVA(Conversor.hexToBigInteger(sdeVA));
                sde.setFunctionTableVAHex(sdeVA);
                BigInteger sdePA = this.getTranslator().calculatePhysicalAddress(sdeVA);
                long sdeOffset = this.getDumpFormat().getFileOffset(sdePA);
                if (sdeOffset > HeaderCrashDump32._HEADER_SIZE) { //Tamaño del encabezad
                    sde.setValid(true);
                } else {
                    sde.setValid(false);
                }
                sde.setServiceLimit(Conversor.hexToInt(serviceLimit));

                sde.getAttributes().get("functionTableVA").setContent(sdeVA);
                sde.getAttributes().get("counterBaseTable").setContent(counterBaseTable);
                sde.getAttributes().get("serviceLimit").setContent(serviceLimit);
                sde.getAttributes().get("argumentTable").setContent(argumentTable);

                sdeList[cant++] = sde;

                shift += 0x010;
            }

            /**
             * Busca las funciones asociadas a cada una de las SDE
             */
            for (int i = 0; i < sdeList.length; i++) {
                BigInteger pAFunctions = this.getTranslator().calculatePhysicalAddress(sdeList[i].getFunctionTableVA());
                long offsetFunctions = this.getDumpFormat().getFileOffset(pAFunctions);
                /**
                 * A partir del offset, recupera todos los VA decada una de las
                 * funciones
                 */
                EntityWindowsFunction[] fArray = new EntityWindowsFunction[sdeList[i].getServiceLimit()];
                for (int j = 0; j < sdeList[i].getServiceLimit(); j++) {
                    EntityWindowsFunction function = new EntityWindowsFunction();
                    String vAF = DataManager.getInstance().getItemContent("int 32", offsetFunctions, 1, false);
                    if (vAF != null) {
                        /**
                         * Calcula VA absoluto de la función
                         */
                        long value = new BigInteger(vAF, 16).longValue();
                        long result = value >> 4 & 0x00000000000FFFFFL;
                        String resultHex = Conversor.longToHexString(result);
                        BigInteger rVAF = Conversor.hexToBigInteger(resultHex);
                        BigInteger sdeVA = sdeList[i].getFunctionTableVA();
                        BigInteger absoluteVAF = sdeVA.add(rVAF);

                        function.setFunctionVAHex(Conversor.bigIntToHexString(absoluteVAF));
                        function.setFunctionVA(absoluteVAF);
                        BigInteger fPA = this.getTranslator().calculatePhysicalAddress(absoluteVAF);
                        long fOffset = this.getDumpFormat().getFileOffset(fPA);
                        function.setOffset(fOffset);
                        fArray[j] = function;
                        offsetFunctions = offsetFunctions + 4; //Desplazamiento hasta la próxima función
                    }
                }

                for (EntityWindowsFunction f : fArray) {
                    int q = 0;
                    int cantOperations = EntityWindowsFunction._SIZE_STRUCTURE / EntityWindowsFunctionOperation._SIZE_STRUCTURE;
                    EntityWindowsFunctionOperation[] operations = new EntityWindowsFunctionOperation[cantOperations];
                    if (f != null) {
                        if (f.getFunctionVA() != null) {
                            BigInteger fPA = this.getTranslator().calculatePhysicalAddress(f.getFunctionVA());
                            long fInitialOffset = this.getDumpFormat().getFileOffset(fPA);
                            long fOffset = 0;
                            int o = 0;
                            while (q < f.getFunctionMaxSize()) {
                                fOffset = fInitialOffset + q;
                                String operation = DataManager.getInstance().getItemContent("int 8", fOffset, 4, false, true);
                                String desp = DataManager.getInstance().getItemContent("int 8", fOffset + 4, 4, false);
                                String value = DataManager.getInstance().getItemContent("int 8", fOffset + 4, 1, false);
                                EntityWindowsFunctionOperation op = new EntityWindowsFunctionOperation();
                                op.getAttributes().get("operation").setContent(operation);
                                op.getAttributes().get("desp").setContent(desp);
                                op.getAttributes().get("value").setContent(value);
                                op.setOffset(fOffset);
                                operations[o++] = op;
                                q += 9;
                            }
                            f.setOperations(operations);
                        }
                    }
                }
                sdeList[i].setFunctionArray(fArray);
            }

            ssdt.setEntryList(sdeList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void visit(EntityLib lib) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(EntityConnection connection) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(EntitySocket socket) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(EntityMalware malware) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(EntityThread thread) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

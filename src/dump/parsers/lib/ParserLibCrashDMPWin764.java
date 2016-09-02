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

import dump.formats.CrashDump64;
import dump.formats.DumpFormat;
import dump.formats.HeaderCrashDump64;
import dump.ooss.OperatingSystemStructure;
import dump.ooss.Windows764OS;
import dump.parsers.ParserWin64;
import dump.parsers.interfaces.IEntityParserVisitor;
import dump.parsers.interfaces.IParserLib;
import dump.parsers.process.ParserWin764ActiveProcessCrashDMP;
import entities.Entity;
import entities.EntityAttribute;
import entities.EntityListWin64;
import entities.connection.EntityConnection;
import entities.connection.EntitySocket;
import entities.lib.EntityImageDOSHeader;
import entities.lib.EntityImageExportDirectory;
import entities.lib.EntityImageNTHeader64;
import entities.lib.EntityLib;
import entities.lib.EntityLibWindows764DLL;
import entities.malware.EntityMalware;
import entities.malware.EntitySDE;
import entities.malware.EntitySSDT;
import entities.malware.EntityWin764SDE;
import entities.malware.EntityWin764SSDT;
import entities.process.EntityProcess;
import entities.process.EntityThread;
import entities.malware.EntityMalware;
import entities.malware.EntitySSDT;
import entities.malware.EntityWin764SDE;
import entities.malware.EntityWin764SSDT;
import entities.malware.EntityWindowsFunction;
import entities.malware.EntityWindowsFunctionOperation;
import entities.process.EntityProcess;
import entities.process.EntityThread;
import entities.process.EntityWin764KPROCESS;
import entities.process.EntityWindows764Process;
import entities.translation.EntityAddressSpace;
import entities.translation.EntityAddressSpaceWin64;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.Map;
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
public class ParserLibCrashDMPWin764 extends ParserWin64 implements IParserLib, IEntityParserVisitor {

    private int _CANT_PAGES = 8;

    private Windows764OS _os;

    public ParserLibCrashDMPWin764(DumpFormat _dumpFormat, OperatingSystemStructure _os) {
        super(_dumpFormat, _os);

        /**
         * Registra observadores
         */
        this.addObserver(output.OutputManagerException.getInstance());
        this.addObserver(output.OutputManagerDump.getInstance());
        this.addObserver(output.OutputManagerConsole.getInstance());
    }

    public ParserLibCrashDMPWin764(DumpFormat _dumpFormat, OperatingSystemStructure _os, Translator _translator) {
        super(_dumpFormat, _os, _translator);

        /**
         * Registra observadores
         */
        this.addObserver(output.OutputManagerException.getInstance());
        this.addObserver(output.OutputManagerDump.getInstance());
        this.addObserver(output.OutputManagerConsole.getInstance());
    }

    public Windows764OS getOs() {
        return _os;
    }

    public void setOs(Windows764OS _os) {
        this._os = _os;
    }

    @Override
    public void parse(Object[] params) {
        this.setStatus("Iniciado");
        this.setStatusDetail("Proceso de parseo de dump y obtención de módulos/librerías Windows 7 x64.");
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

            CrashDump64 crashDump = (CrashDump64) this.getDumpFormat();

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
            EntityListWin64 psLoadedModuleHead = (EntityListWin64) this.getListContent(psLoadedModuleHeadAbsoluteOffset);
            EntityAddressSpaceWin64 aS = (EntityAddressSpaceWin64) this.getTranslator().obtainAddressSpace(psLoadedModuleHead.getfLinkHex());
            long physicalAddressFileOffsetAS = this.getDumpFormat().getFileOffset(aS.getPhysicalAddress());
            aS.setOffsetInFile(physicalAddressFileOffsetAS);
            aS.setOffsetInFileHex(Conversor.longToHexString(physicalAddressFileOffsetAS));

            long initialAbsoluteOffset = 0;
            long absoluteOffset = 0;
            int iLibrary = 0;
            if (psLoadedModuleHead != null) {
                boolean firstReference = true;

                BigInteger nextPosition = getNextEntityAddress(psLoadedModuleHead, EntityLibWindows764DLL._ENTITY_LIST_OFFSET);
                long nextOffset = this.getDumpFormat().getFileOffset(nextPosition);
                long nextOffsetComparator = psLoadedModuleHeadAbsoluteOffset - 0x000; //0x000: OFFSET AL INICIO DE LA ESTRUCTURA (está al inicio)
                EntityListWin64 loadedModuleLinks = psLoadedModuleHead;

                //Inicializa índice para key del HashMap entities
                int ind = 0;
                while (loadedModuleLinks != null) {
                    EntityLibWindows764DLL entityLib = new EntityLibWindows764DLL();

                    /**
                     * Busca la estructura _LIST_ENTRY
                     */
                    if (!firstReference && nextOffset == nextOffsetComparator) {
                        break;
                    }
                    long absoluteAttributeOffset = nextOffset + entityLib.getAttributes().get("InLoadOrderLinks").getPosition();
                    loadedModuleLinks = (EntityListWin64) getListContent(absoluteAttributeOffset);

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
                    aS = (EntityAddressSpaceWin64) this.getTranslator().obtainAddressSpace(loadedModuleLinks.getfLinkBig());
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

                    /**
                     * Notifica observadores
                     */
                    this.notifyObservers(entityLib);

                    nextOffset = this.getDumpFormat().getFileOffset(getNextEntityAddress(loadedModuleLinks, EntityLibWindows764DLL._ENTITY_LIST_OFFSET));

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
    public void parse(Entity entity, Object[] params) {
        try {
            ConcurrentHashMap<Object, Entity> entities = null;
            EntityWindows764Process process = null;
            if (entity instanceof EntitySSDT) {
                StringBuilder log = new StringBuilder();
                log.append("Realizando búsqueda de SSDTs...");
                this.notifyObservers(log);

                entities = new ConcurrentHashMap<Object, Entity>();
                int i = 1;
                Object[] paramsParse = new Object[2];
                if (params != null) {
                    if (params.length > 0) {
                        BigInteger vA = (BigInteger) params[0];
                        BigInteger ssdtVA = this.getSSDTVA(vA);
                        paramsParse[0] = ssdtVA;
                        BigInteger shift = new BigInteger("64"); //0x040 Shift to Shadow
                        paramsParse[1] = ssdtVA.add(shift);
                    }
                }
                for (Object o : paramsParse) {

                    BigInteger ssdtVA = (BigInteger) o;

                    EntitySSDT ssdt = new EntityWin764SSDT();
                    EntityAddressSpace aS = new EntityAddressSpaceWin64();
                    aS.setVirtualAddress(ssdtVA);
                    ssdt.setaS(aS);
                    ssdt.accept(this);

                    EntitySDE[] sdeList = ssdt.getEntryList();
                    switch (i) {
                        case 1: //KeServiceDescriptorTable
                            ssdt.setDescription("KeServiceDescriptorTable");
                            sdeList[0].setEntryDescription("nt!KeServiceTable");
                            sdeList[1].setEntryDescription("Other");
                            ssdt.setEntryList(sdeList);
                            break;
                        case 2: //KeServiceDescriptorTableShadow
                            ssdt.setDescription("KeServiceDescriptorTableShadow");
                            sdeList[0].setEntryDescription("nt!KeServiceTable (Shadow)");
                            sdeList[1].setEntryDescription("win32k!W32pServiceTable");
                            ssdt.setEntryList(sdeList);
                            break;
                        default:
                            break;
                    }
                    entities.put("ssdt[" + i++ + "]", ssdt);
                }
                log.append("Búsqueda de SSDTs finalizada.");
                this.notifyObservers(log);
            } else if (params.length > 0) {
                if (params[0] != null) {
                    if (params[0] instanceof EntityWindows764Process) {
                        process = (EntityWindows764Process) params[0];
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
    public void run() {
        this.parse(null);
    }

    @Override
    public Object call() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean validateAttributeContent(Map.Entry<String, EntityAttribute> attribute) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(EntityProcess process) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    public BigInteger getSSDTVA(BigInteger vA) {
        BigInteger vARet = BigInteger.ZERO;

        try {
            int cant = 0;

            BigInteger pA = this.getTranslator().calculatePhysicalAddress(vA);

            /**
             * offset de DllBase de ntoskrnl.exe
             */
            long offset = this.getDumpFormat().getFileOffset(pA);

            EntityImageDOSHeader imgDOSHeader = new EntityImageDOSHeader();
            long e_flnewR = imgDOSHeader.getAttributes().get("e_lfnew").getPosition();
            BigInteger e_flnewVA = vA.add(BigInteger.valueOf(e_flnewR));

            /**
             * e_flnew
             */
            BigInteger pAFlNew = this.getTranslator().calculatePhysicalAddress(e_flnewVA);
            long e_flNewOff = this.getDumpFormat().getFileOffset(pAFlNew);
            String e_flnew = DataManager.getInstance().getItemContent("int 32", e_flNewOff, 1, false);

            /**
             * IMAGE_NT_HEADER64
             */
            BigInteger vANTHeader = vA.add(Conversor.hexToBigInteger(e_flnew));
            BigInteger pANTHeader = this.getTranslator().calculatePhysicalAddress(vANTHeader);
            long NTHeaderOff = this.getDumpFormat().getFileOffset(pANTHeader);

            EntityImageNTHeader64 imgNT64 = new EntityImageNTHeader64();
            int optHeaderROff = imgNT64.getAttributes().get("OptionalHeader").getPosition();
            int dataDirectoryROff = imgNT64.getAttributes().get("OptionalHeader").getEntity().getAttributes().get("DataDirectory").getPosition();

            /**
             * IMAGE_EXPORT_DIRECTORY [0] --> Export Directory int 32 RVA int 32
             * size
             */
            long exportDirectoryRVAOff = NTHeaderOff + optHeaderROff + dataDirectoryROff;
            String exportDirectoryRVA = DataManager.getInstance().getItemContent("int 32", exportDirectoryRVAOff, 1, false);
            BigInteger exportDirectoryVA = vA.add(Conversor.hexToBigInteger(exportDirectoryRVA));
            BigInteger exportDirectoryPA = this.getTranslator().calculatePhysicalAddress(exportDirectoryVA);
            long exportDirectoryOff = this.getDumpFormat().getFileOffset(exportDirectoryPA);
            EntityImageExportDirectory exportDirectory = new EntityImageExportDirectory();

            /**
             * AddressOfNames
             */
            long addressOfNamesRVAOff = exportDirectoryOff + exportDirectory.getAttributes().get("AddressOfNames").getPosition();
            String addressOfNamesRVA = DataManager.getInstance().getItemContent("int 32", addressOfNamesRVAOff, 1, false);
            BigInteger addressOfNamesVA = vA.add(Conversor.hexToBigInteger(addressOfNamesRVA));
            BigInteger addressOfNamesPA = this.getTranslator().calculatePhysicalAddress(addressOfNamesVA);
            long addressOfNamesOff = this.getDumpFormat().getFileOffset(addressOfNamesPA);

            String functionToFind = "KeAddSystemServiceTable";
            long pageOffset = addressOfNamesOff;
            String aOfNameContentAux = "";
            int indexFin = -1;
            int indFunction = 0;
            boolean encontrado = false;
            while (indexFin < 0) {
                aOfNameContentAux = DataManager.getInstance().getItemContent("int 32", pageOffset, 1, false);
                BigInteger aOfNameContentAuxVA = vA.add(Conversor.hexToBigInteger(aOfNameContentAux));
                BigInteger aOfNameContentAuxPA = this.getTranslator().calculatePhysicalAddress(aOfNameContentAuxVA);
                long aOfNameContentAuxOff = this.getDumpFormat().getFileOffset(aOfNameContentAuxPA);
                String funcStr = DataManager.getInstance().getItemContent("char", aOfNameContentAuxOff, 100, false);
                if (funcStr.indexOf(functionToFind) == 0) {
                    indexFin = indFunction;
                    encontrado = true;
                    break;
                }

                if (indFunction > 10000) {
                    indexFin = 0;
                    encontrado = false;
                    break;
                }
                pageOffset += 4;
                indFunction++;
            }

            if (encontrado) {
                /**
                 * AddressOfNamesOrdinales: según el indFunction, buscar el
                 * índice dentro de AddressOfFunction
                 */
                long addressOfNamesOrdRVAOff = exportDirectoryOff + exportDirectory.getAttributes().get("AddressOfNamesOrdinals").getPosition();
                String addressOfNamesOrdRVA = DataManager.getInstance().getItemContent("int 32", addressOfNamesOrdRVAOff, 1, false);
                BigInteger addressOfNamesOrdVA = vA.add(Conversor.hexToBigInteger(addressOfNamesOrdRVA));
                BigInteger addressOfNamesOrdPA = this.getTranslator().calculatePhysicalAddress(addressOfNamesOrdVA);
                long addressOfNamesOrdOff = this.getDumpFormat().getFileOffset(addressOfNamesOrdPA);
                /**
                 * Cada ítem en AddressOfNamesOrdinals es de 2 bytes.
                 */
                int itemAofNOrdSize = 2;
                long indexOrd = itemAofNOrdSize * indFunction;
                String aOfNamesIndexValue = DataManager.getInstance().getItemContent("int 16", addressOfNamesOrdOff + indexOrd, 1, false);

                /**
                 * AddressOfFunctions
                 */
                long addressOfFunctionsRVAOff = exportDirectoryOff + exportDirectory.getAttributes().get("AddressOfFunctions").getPosition();
                String addressOfFunctionsRVA = DataManager.getInstance().getItemContent("int 32", addressOfFunctionsRVAOff, 1, false);
                BigInteger addressOfFunctionsVA = vA.add(Conversor.hexToBigInteger(addressOfFunctionsRVA));
                BigInteger addressOfFunctionsPA = this.getTranslator().calculatePhysicalAddress(addressOfFunctionsVA);
                long addressOfFunctionsOff = this.getDumpFormat().getFileOffset(addressOfFunctionsPA);
                /**
                 * Cada ítem en AddressOfNamesOrdinals es de 4 bytes.
                 */
                int itemAofFuncSize = 4;
                int indexAofFunction = Conversor.hexToInt(aOfNamesIndexValue);
                long functionArrayOff = itemAofFuncSize * indexAofFunction;
                String keAddSystemRVA = DataManager.getInstance().getItemContent("int 32", addressOfFunctionsOff + functionArrayOff, 1, false);

                BigInteger keAddSystemVA = vA.add(Conversor.hexToBigInteger(keAddSystemRVA));
                BigInteger keAddSystemPA = this.getTranslator().calculatePhysicalAddress(keAddSystemVA);
                long keAddSystemOff = this.getDumpFormat().getFileOffset(keAddSystemPA);

                long indexKeOp = -1;
                long pageOp = keAddSystemOff;
                boolean opEncontrada = false;
                int indexOp = 0;
                String opStrAux = "";
                while (indexKeOp < 0) {
                    opStrAux = DataManager.getInstance().getItemContent("int 32", pageOp, 1, false, true);
                    for (String s : EntityWindowsFunctionOperation._KeServiceDescriptorInstructions) {
                        indexOp = opStrAux.indexOf(s);
                        if (opStrAux.indexOf(s) >= 0) {
                            opEncontrada = true;
                            break;
                        }
                    }

                    if (opEncontrada || indexOp > 120) {
                        break;
                    }
                    indexOp++;
                    pageOp += 1;
                }

                if (opEncontrada) {
                    pageOp = pageOp + indexOp - 1;
                    String desp = DataManager.getInstance().getItemContent("int 32", pageOp + 4, 1, false);
                    BigInteger ssdtVA = vA.add(Conversor.hexToBigInteger(desp));
                    vARet = ssdtVA;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return vARet;
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
            int cantSDE = 2;
            EntityWin764SDE[] sdeList = new EntityWin764SDE[cantSDE];
            while (cant < 2) {
                initialOffset = offset + shift;
                long currentOffset = initialOffset;

                String sdeVA = DataManager.getInstance().getItemContent("int 64", currentOffset, 1, false);
                currentOffset += 0x008;
                String counterBaseTable = DataManager.getInstance().getItemContent("int 64", currentOffset, 1, false);
                currentOffset += 0x008;
                String serviceLimit = DataManager.getInstance().getItemContent("int 64", currentOffset, 1, false);
                currentOffset += 0x008;
                String argumentTable = DataManager.getInstance().getItemContent("int 64", currentOffset, 1, false);

                EntityWin764SDE sde = new EntityWin764SDE();
                sde.setFunctionTableVA(Conversor.hexToBigInteger(sdeVA));
                sde.setFunctionTableVAHex(sdeVA);
                BigInteger sdePA = this.getTranslator().calculatePhysicalAddress(sdeVA);
                long sdeOffset = this.getDumpFormat().getFileOffset(sdePA);
                if (sdeOffset < HeaderCrashDump64._HEADER_SIZE) { //Tamaño del encabezad
                    sde.setValid(false);
                }
                sde.setServiceLimit(Conversor.hexToInt(serviceLimit));

                sde.getAttributes().get("functionTableVA").setContent(sdeVA);
                sde.getAttributes().get("counterBaseTable").setContent(counterBaseTable);
                sde.getAttributes().get("serviceLimit").setContent(serviceLimit);
                sde.getAttributes().get("argumentTable").setContent(argumentTable);

                sdeList[cant++] = sde;

                shift += 0x020;
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
                    /**
                     * Calcula VA absoluto de la función
                     */
                    long value = new BigInteger(vAF, 16).longValue();
                    long result = value >> 4 & 0x0000000000FFFFFFL;
                    String resultHex = Conversor.longToHexString(result);
                    BigInteger rVAF = Conversor.hexToBigInteger(resultHex);
                    BigInteger sdeVA = sdeList[i].getFunctionTableVA();
                    BigInteger absoluteVAF = absoluteVAF = sdeVA.add(rVAF);

                    function.setFunctionVAHex(Conversor.bigIntToHexString(absoluteVAF));
                    function.setFunctionVA(absoluteVAF);
                    BigInteger fPA = this.getTranslator().calculatePhysicalAddress(absoluteVAF);
                    long fOffset = this.getDumpFormat().getFileOffset(fPA);
                    function.setOffset(fOffset);
                    fArray[j] = function;
                    offsetFunctions = offsetFunctions + 4; //Desplazamiento hasta la próxima función
                }

                for (EntityWindowsFunction f : fArray) {
                    int q = 0;
                    int cantOperations = EntityWindowsFunction._SIZE_STRUCTURE / EntityWindowsFunctionOperation._SIZE_STRUCTURE;
                    EntityWindowsFunctionOperation[] operations = new EntityWindowsFunctionOperation[cantOperations];
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
                sdeList[i].setFunctionArray(fArray);
            }

            ssdt.setEntryList(sdeList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

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
package system.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Gonzalo
 */
public class ConfigurationManager {

    private static ConfigurationManager _instance;
    private Map<String, Integer> _configurations;
    private Map<String, Integer> _persistenceConfigurations;
    private Map<Integer, String> _parseConfigurations;

    private Properties _properties = null;
    private Properties _sessionProperties = null;

    private ConfigurationManager() {

    }

    public static ConfigurationManager getInstance() {
        if (_instance == null) {
            _instance = new ConfigurationManager();
        }

        return _instance;
    }

    public Map<String, Integer> getConfigurations() {
        return _configurations;
    }

    public void setConfigurations(Map<String, Integer> _configurations) {
        this._configurations = _configurations;
    }

    public Map<String, Integer> getPersistenceConfigurations() {
        return _persistenceConfigurations;
    }

    public void setPersistenceConfigurations(Map<String, Integer> _persistenceConfigurations) {
        this._persistenceConfigurations = _persistenceConfigurations;
    }

    public Map<Integer, String> getParseConfigurations() {
        return _parseConfigurations;
    }

    public void setParseConfigurations(Map<Integer, String> _parseConfigurations) {
        this._parseConfigurations = _parseConfigurations;
    }

    public Properties getSessionProperties() {
        return _sessionProperties;
    }

    public void setSessionProperties(Properties _sessionProperties) {
        this._sessionProperties = _sessionProperties;
    }

    public void obtainConfiguration() {
        try {
            this._configurations = new HashMap<String, Integer>();

            //URL urlToDictionary = this.getClass().getResource(System.getProperty("user.dir") + "\\sources\\configuration.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            String path = System.getProperty("user.dir") + "\\sources\\configuration.xml";
            InputStream stream = new FileInputStream(new File(path));

            Document doc = db.parse(stream);
            doc.getDocumentElement().normalize();
            //System.out.println("Elemento raíz " + doc.getDocumentElement().getNodeName());
            NodeList nodeLst = doc.getElementsByTagName("conf");
            //System.out.println("Info sobre configuración:");

            for (int s = 0; s < nodeLst.getLength(); s++) {

                Node fstNode = nodeLst.item(s);

                if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element elmnt = (Element) fstNode;
                    NodeList label = elmnt.getElementsByTagName("label");
                    Element tagElmnt = (Element) label.item(0);
                    NodeList tag = tagElmnt.getChildNodes();
                    //System.out.println("Tag : " + ((Node) tag.item(0)).getNodeValue());
                    NodeList idlbl = elmnt.getElementsByTagName("id");
                    Element idElmnt = (Element) idlbl.item(0);
                    NodeList id = idElmnt.getChildNodes();
                    //System.out.println("id : " + ((Node) id.item(0)).getNodeValue());
                    this._configurations.put(((Node) tag.item(0)).getNodeValue().toString(), Integer.parseInt(((Node) id.item(0)).getNodeValue().toString()));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void obtainPersistenceConfiguration() {
        try {
            this._persistenceConfigurations = new HashMap<String, Integer>();

            //URL urlToDictionary = this.getClass().getResource(System.getProperty("user.dir") + "\\sources\\persistence_configuration.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            String path = System.getProperty("user.dir") + "\\sources\\persistence_configuration.xml";
            InputStream stream = new FileInputStream(new File(path));
            //InputStream stream = urlToDictionary.openStream();

            Document doc = db.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeLst = doc.getElementsByTagName("persistence_conf");

            for (int s = 0; s < nodeLst.getLength(); s++) {

                Node fstNode = nodeLst.item(s);

                if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element elmnt = (Element) fstNode;
                    NodeList label = elmnt.getElementsByTagName("label");
                    Element tagElmnt = (Element) label.item(0);
                    NodeList tag = tagElmnt.getChildNodes();
                    NodeList idlbl = elmnt.getElementsByTagName("source");
                    Element idElmnt = (Element) idlbl.item(0);
                    NodeList id = idElmnt.getChildNodes();
                    this._persistenceConfigurations.put(((Node) tag.item(0)).getNodeValue().toString(), Integer.parseInt(((Node) id.item(0)).getNodeValue().toString()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void obtainParseConfiguration() {
        try {
            this._parseConfigurations = new HashMap<Integer, String>();

            //URL urlToDictionary = this.getClass().getResource(System.getProperty("user.dir") + "\\sources\\parse_configuration.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            String path = System.getProperty("user.dir") + "\\sources\\parse_configuration.xml";
            InputStream stream = new FileInputStream(new File(path));
            //InputStream stream = urlToDictionary.openStream();

            Document doc = db.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeLst = doc.getElementsByTagName("parse_item");

            for (int s = 0; s < nodeLst.getLength(); s++) {

                Node fstNode = nodeLst.item(s);

                if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element elmnt = (Element) fstNode;
                    NodeList label = elmnt.getElementsByTagName("label");
                    Element tagElmnt = (Element) label.item(0);
                    NodeList tag = tagElmnt.getChildNodes();;
                    this._parseConfigurations.put(s, ((Node) tag.item(0)).getNodeValue().toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadSessionProperties(String path) {
        this._sessionProperties = new Properties();
        try {
            File file = new File(path);
            InputStream is = new FileInputStream(file);
            this._sessionProperties.load(is);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void loadPersistenceProperties(String path) {
        this._properties = new Properties();
        try {
            File file = new File(path);
            InputStream is = new FileInputStream(file);
            this._properties.load(is);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Retorna la propiedad deseada
     *
     * @param prop
     * @return
     */
    public String getProperty(String prop) {
        return this._properties.getProperty(prop);
    }

    public String getHash(String path) {
        String hash = null;

        try {
            String hashMethod = this._sessionProperties.getProperty("hashMethod");
            switch (hashMethod) {
                case "MD5":
                    hash = HashValidator.getMD5Hash(path);
                    break;
                case "SHA-1":
                    hash = HashValidator.getSHA1Hash(path);
                    break;
                default:
                    hash = HashValidator.getMD5Hash(path);
                    break;
            }
        } catch (Exception ex) {
            System.out.println("No es posible calcular hash.");
        }
        return hash;
    }
}

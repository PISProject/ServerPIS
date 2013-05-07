/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author mat.aules
 */
public class XMLParser {
    DocumentBuilderFactory dbFactory;
    DocumentBuilder dBuilder;
    Document doc;
    File fXmlFile;
    
    public XMLParser() throws ParserConfigurationException{
        dbFactory = DocumentBuilderFactory.newInstance();
        dBuilder = dbFactory.newDocumentBuilder();
    }
    
    /**
     * ejemplo:
     * File :: ~/file_path
     * 
     * <element>
     *  <map.getKey(firstElement) map.getValue()[0]="..." map.getValue()[1]="..."/>
     * </element>
     * 
     * @param file_path
     * @param element
     * @param map
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException 
     */
    public ArrayList<String> parseXMLData(String file_path, String element, HashMap<String, String[]> map) throws ParserConfigurationException, SAXException, IOException{
        ArrayList<String> retlist = new ArrayList<>();
        NodeList elements;
        fXmlFile = new File(new File("").getAbsolutePath()+file_path);
        doc = dBuilder.parse(fXmlFile);

        
        doc.getDocumentElement().normalize();

        /*
         * Hacemos una busqueda de todos los elementos bajo el tag 'element', 
         * que es el que contiene los elementos fundamentales del archivo
         * como seria 'monster' en el caso de monsters.xml
         */
        
        NodeList nList = doc.getElementsByTagName(element);

        //Iteramos por la lista resultante
        for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    /*
                     * Ahora, para cada uno de los nodos bajo el tag 'element' 
                     * hemos de obtener los atributos que vienen determinados en
                     * el HashMap. Lo leeremos todo como Strings.
                     */
                    Element eElement = (Element) nNode;
                    for (Map.Entry ent : map.entrySet()) {
                        
                        elements = eElement.getElementsByTagName((String)ent.getKey());
                        String [] lista = (String [])ent.getValue();
                        for (int i = 0; i < elements.getLength(); i++) {
                            for (int j = 0; j < lista.length; j++) {
                                retlist.add((String)((Element)elements.item(i)).getAttribute(lista[j]));
                            }
                        }
                    }
                }
            }
        return retlist;
}
    
}
    

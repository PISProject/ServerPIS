/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import game.monsters.MonsterModel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    
    public ArrayList<String> readFromXML(String file_path, String element, HashMap<String, String[]> map) throws ParserConfigurationException, SAXException, IOException{
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
                    //Añadimos el 'path' de cada uno de los monstruos encontrados
                    Iterator it = map.entrySet().iterator();
                    /*
                     * Para todas las entradas del hashmap...
                     */
                    while (it.hasNext()){
                        Map.Entry ent = (Map.Entry)it.next();
                        
                        elements = eElement.getElementsByTagName((String)ent.getKey());
                        String [] lista = (String [])ent.getValue();
                        for (int i = 0; i < elements.getLength(); i++) {
                            for (int j = 0; j < lista.length; j++) {
                                System.out.println(elements.getLength());
                                retlist.add((String)((Element)elements.item(i)).getAttribute(lista[j]));
                                
                            }
                        }
                    }
                }
            }
        return retlist;
}
    
}
    

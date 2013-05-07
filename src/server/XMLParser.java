/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import game.monsters.MonsterModel;
import game.monsters.Monsters;
import java.io.File;
import java.io.IOException;
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
    
    public XMLParser() throws ParserConfigurationException{
        dbFactory = DocumentBuilderFactory.newInstance();
        dBuilder = dbFactory.newDocumentBuilder();
    }
    
    public Monsters parseMonsterList(String file_path) throws ParserConfigurationException, SAXException, IOException{
        Monsters monsters = new Monsters();

        File fXmlFile = new File(new File("").getAbsolutePath()+file_path);
        Document doc = dBuilder.parse(fXmlFile);

        
        doc.getDocumentElement().normalize();

        /*
         * Hacemos una busqueda de todos los elementos bajo el tag 'element', 
         * que es el que contiene los elementos fundamentales del archivo
         * como seria 'monster' en el caso de monsters.xml
         */
        NodeList elements;
        NodeList nList = doc.getElementsByTagName("monster");

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
                    Monsters.MONSTER_LIST.put(eElement.getAttribute("name"), parseMonster(eElement.getAttribute("path")));
                    
                }
            }
        return monsters;
}
    
     public MonsterModel parseMonster(String file_path) throws ParserConfigurationException, SAXException, IOException{
        MonsterModel m = new MonsterModel();
        File fXmlFile = new File(new File("").getAbsolutePath()+"/src/resources/monsters/"+file_path);
        Document doc = dBuilder.parse(fXmlFile);

        doc.getDocumentElement().normalize();

        //Hacemos una busqueda de todos los elementos bajo el tag 'monster'
        NodeList nList = doc.getElementsByTagName("monster");

        //Iteramos por la lista resultante

        Node nNode = nList.item(0);
        if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) nNode;
                //Añadimos el 'path' de cada uno de los monstruos encontrados

                m.name = eElement.getAttribute("name");
                m.speed = Double.parseDouble(((Element)eElement.getElementsByTagName("attributes").item(0)).getAttribute("speed"));
                m.hp = Integer.parseInt(((Element)eElement.getElementsByTagName("attributes").item(0)).getAttribute("health"));
                m.model = Integer.parseInt(((Element)eElement.getElementsByTagName("attributes").item(0)).getAttribute("looktype"));
                m.changedir_prob = Float.parseFloat(((Element)eElement.getElementsByTagName("behavior").item(0)).getAttribute("changedir"));
                m.stchange_rate = Float.parseFloat(((Element)eElement.getElementsByTagName("behavior").item(0)).getAttribute("stchangerate"));
                m.attack_damage = Integer.parseInt(((Element)eElement.getElementsByTagName("skills").item(0)).getAttribute("attack"));
                
                
                /*
                 * TODO: Optimizar el codigo, esto es un truño..
                 */


        }
        return m;
     }
}
    

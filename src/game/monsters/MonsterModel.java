/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.monsters;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
 * @author kirtash
 */
public class MonsterModel {
    public String name;
    public int hp;
    public int attack_damage;
    public double speed;
    public int model;
    public double changedir_prob;
    
    
    // PARAMETROS DE COMPORTAMIENTO

    public int target; //uid del target
    public double rand_movedir;
    public double stchange_rate; //Cada segundo
    
    
    public MonsterModel(String xmlPath) {
        try {
            readFromXML(xmlPath);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }
    
    
    private void readFromXML(String xmlString) throws IOException, ParserConfigurationException, SAXException{
        ArrayList<String> monsters = new ArrayList<>();
        File fXmlFile = new File(new File("").getAbsolutePath()+"/src/resources/monsters/"+xmlString);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        doc.getDocumentElement().normalize();

        //Hacemos una busqueda de todos los elementos bajo el tag 'monster'
        NodeList nList = doc.getElementsByTagName("monster");

        //Iteramos por la lista resultante

        Node nNode = nList.item(0);
        if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) nNode;
                //Añadimos el 'path' de cada uno de los monstruos encontrados

                name = eElement.getAttribute("name");
                speed = Double.parseDouble(((Element)eElement.getElementsByTagName("attributes").item(0)).getAttribute("speed"));
                hp = Integer.parseInt(((Element)eElement.getElementsByTagName("attributes").item(0)).getAttribute("health"));
                model = Integer.parseInt(((Element)eElement.getElementsByTagName("attributes").item(0)).getAttribute("looktype"));
                changedir_prob = Float.parseFloat(((Element)eElement.getElementsByTagName("behavior").item(0)).getAttribute("changedir"));
                stchange_rate = Float.parseFloat(((Element)eElement.getElementsByTagName("behavior").item(0)).getAttribute("stchangerate"));
                attack_damage = Integer.parseInt(((Element)eElement.getElementsByTagName("skills").item(0)).getAttribute("attack"));
                
                
                /*
                 * TODO: Optimizar el codigo, esto es un truño..
                 */


        }
    }
}

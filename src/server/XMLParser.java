/*******************************************************************************
 * Machango Fight, the Massive Multiplayer Online.
 * Server Application
 * 
 * Curso 2012-2013
 * 
 * Este software ha sido desarrollado integramente para la asignatura 'Projecte
 * Integrat de Software' en la Universidad de Barcelona por los estudiantes
 * Pablo Martínez Martínez, Albert Folch, Xavi Moreno y Aaron Negrín.
 * 
 ******************************************************************************/

package server;

import game.heroes.Heros;
import game.models.Game;
import game.models.Games;
import game.models.Horde;
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
 * @author PabloMartinez
 */
public class XMLParser {
    DocumentBuilderFactory dbFactory;
    DocumentBuilder dBuilder;
    
    public XMLParser() throws ParserConfigurationException{
        dbFactory = DocumentBuilderFactory.newInstance();
        dBuilder = dbFactory.newDocumentBuilder();
    }
    
    public void parseMonsterList(String file_path) throws ParserConfigurationException, SAXException, IOException{
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
     
     public void parseHeroList(String file_path) throws ParserConfigurationException, SAXException, IOException{
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
        NodeList nList = doc.getElementsByTagName("hero");

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
                    Heros.HEROS_LIST.put(eElement.getAttribute("name"), parseMonster(eElement.getAttribute("file")));
                }
            }
}
    
     public MonsterModel parseHero(String file_path) throws ParserConfigurationException, SAXException, IOException{
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
     
     
     
    public void parseGameList(String file_path) throws ParserConfigurationException, SAXException, IOException{
        //Monsters monsters = new Monsters();

        File fXmlFile = new File(new File("").getAbsolutePath()+file_path);
        Document doc = dBuilder.parse(fXmlFile);

        
        doc.getDocumentElement().normalize();

        /*
         * Hacemos una busqueda de todos los elementos bajo el tag 'element', 
         * que es el que contiene los elementos fundamentales del archivo
         * como seria 'monster' en el caso de monsters.xml
         */
        NodeList elements;
        NodeList nList = doc.getElementsByTagName("game");

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
                    Games.GAME_LIST.put(Integer.parseInt(eElement.getAttribute("id")), parseGame(eElement.getAttribute("path")));
                    
                }
            }
        //return monsters;
}

    private Game parseGame(String file_path) throws ParserConfigurationException, SAXException, IOException{
        Game g = new Game();
        File fXmlFile = new File(new File("").getAbsolutePath()+"/src/resources/games/"+file_path);
        Document doc = dBuilder.parse(fXmlFile);

        doc.getDocumentElement().normalize();

        //Hacemos una busqueda de todos los elementos bajo el tag 'monster'
        NodeList nList = doc.getElementsByTagName("game");

        //Iteramos por la lista resultante

        Node nNode = nList.item(0);
        if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) nNode;
                Element e2;
                //Añadimos el 'path' de cada uno de los monstruos encontrados

                
                // Cojemos la informacion bajo el tag 'basics'
                e2 = (Element) eElement.getElementsByTagName("basics").item(0);
                
                g.name = e2.getAttribute("name");
                g.game_type = Integer.parseInt(e2.getAttribute("id"));
                g.numplayers = Integer.parseInt(e2.getAttribute("numplayers"));
                g.scenario = Integer.parseInt(e2.getAttribute("scenario"));
                
                
                //Cojemos la informacion bajo el tag 'hordes'
                e2 = (Element) eElement.getElementsByTagName("hordes").item(0);
                g.n_hordes = Integer.parseInt(e2.getAttribute("number"));
                
                
                //Sacamos una lista de los elementos contenidos en el tag 'horde'
                nList = doc.getElementsByTagName("horde");
                
                //Nuevo elemento Horde
                Horde h;
                for (int i = 0; i < nList.getLength(); i++) {
                    //Creamos una nueva horda por cada elemento.
                    h = new Horde();
                    e2 = (Element)nList.item(i);
                    h.time = Integer.parseInt(e2.getAttribute("time"));
                    
                    //Creamos una segunda lista de nodos para recorrer 'monster'
                    NodeList n2 = ((Element)nList.item(i)).getElementsByTagName("monster");
                    for (int j = 0; j < n2.getLength(); j++) {
                        e2 = (Element) n2.item(j);
                        h.list.add(e2.getAttribute("name"));
                    }
                    g.hordes.add(h);
                }
                    
            }
                
                /*
                 * TODO: Optimizar el codigo, esto es un truño..
                 */

        return g;
    }
}
    

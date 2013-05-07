/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.monsters;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import server.XMLParser;

/**
 *
 * @author kirtash
 */
public class Monsters{
    private static final String MONSTERS_PATH = "/src/resources/monsters/monsters.xml";
    HashMap<String,String[]> monster_loader = new HashMap<>();
    HashMap<String,MonsterModel> monster_list;
    public Monsters(){
        monster_list = new HashMap<>();
        /*
         * Inicializamos el Hashmap para cargar los monstruos
         */
        
        
        monster_loader.put("monster",new String[]{"name","path"});
        
    }
    
    public int readFromXML() throws ParserConfigurationException, SAXException, IOException{
        XMLParser parser = new XMLParser();
        ArrayList<String> alist = parser.parseXMLData(MONSTERS_PATH, "monsters", monster_loader);
        int i = alist.size();
        while (i>0){
            monster_list.put(alist.remove(0), new MonsterModel(alist.remove(0)));
            i-=2;
        }
        
        System.out.println(monster_list);
        return 1;
        
        /*File fXmlFile = new File(new File("").getAbsolutePath()+MONSTERS_PATH);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        doc.getDocumentElement().normalize();

        //Hacemos una busqueda de todos los elementos bajo el tag 'monster'
        NodeList nList = doc.getElementsByTagName("monster");

        //Iteramos por la lista resultante
        for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                        Element eElement = (Element) nNode;
                        //Añadimos el 'path' de cada uno de los monstruos encontrados
                        monster_list.put(eElement.getAttribute("name"), new MonsterModel(eElement.getAttribute("path")));


                }
            }
        System.out.println(monster_list);
        return 1;
    */}
}

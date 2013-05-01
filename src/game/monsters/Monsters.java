/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.monsters;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author kirtash
 */
public class Monsters {
    private static final String MONSTERS_PATH = "/src/resources/monsters/monsters.xml";
    
    HashMap<String,MonsterModel> monster_list;
    public Monsters(){
        monster_list = new HashMap<>();
    }
    
    public int readFromXML(){
      try {
          ArrayList<String> monsters = new ArrayList<>();
        File fXmlFile = new File(new File("").getAbsolutePath()+MONSTERS_PATH);
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
                        MonsterModel m = new MonsterModel(eElement.getElementsByTagName("path").item(0).getTextContent());
                        monster_list.put(eElement.getElementsByTagName("name").item(0).getTextContent(), m);


                }
            }
          System.out.println(monsters);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
      return 1;
    }
}

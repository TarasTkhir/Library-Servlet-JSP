package parser;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class XMLParser {

    private static Map<String, String> allMessages;

    public static Map<String, String> getMessages() {
        if (null == allMessages) {
            getAllMessages();
        }
        return allMessages;
    }

    private static void getAllMessages() {
//        File xmlFile = new File(System.getProperty("user.dir") + "\\src\\messages\\messages.xml");
      File xmlFile = new File("C:\\Users\\taras.tkhir\\Desktop\\LibraryJSP\\src\\messages\\messages.xml");
//        System.out.println(System.getProperty("user.dir"));
//        File file = new File(".");


//        for (String fileNames : file.list()) System.out.println(fileNames);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document doc = null;
        try {
            builder = factory.newDocumentBuilder();

            doc = builder.parse(xmlFile);
        } catch (SAXException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }


        allMessages = new HashMap<>();
        NodeList studentNodes = doc.getElementsByTagName("message");
        for (int i = 0; i < studentNodes.getLength(); i++) {
            Node studentNode = studentNodes.item(i);
            if (studentNode.getNodeType() == Node.ELEMENT_NODE) {
                Element messageElement = (Element) studentNode;
                allMessages.put(messageElement.getElementsByTagName("message-key").item(0).getTextContent(),
                        messageElement.getElementsByTagName("message-value").item(0).getTextContent());
            }
        }
    }

}

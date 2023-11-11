package edu.cs.student.studentassignment.student;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.*;

@Service
public class StudentService {


    public void storeData(Map<String, String> data) {
        try {
            Document document = loadOrCreateXMLFile();
            addStudentData(document, data);
            saveXMLDocument(document);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Map<String, String>> search(String keyword, String searchBy) {
        Document document = null;
        try {
            document = loadOrCreateXMLFile();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        if (searchBy.equals("GPA"))
            return searchByGPA(document, keyword);
        else
            return searchByFirstName(document, keyword);
    }

    public String delete(String studentId) {
        Document document = null;
        try {
            document = loadOrCreateXMLFile();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return deleteById(document, studentId);
    }

    private List<Map<String, String>> searchByGPA(Document document, String gpa) {
        List<Map<String, String>> res = new ArrayList<>();
        NodeList nodeList = document.getElementsByTagName("Student");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element studentElement = (Element) node;
                String studentGPA = studentElement.getElementsByTagName("GPA").item(0).getTextContent();
                if (studentGPA.equals(gpa)) {
                    System.out.println("Search result by GPA:");
                    res.add(fillStudentInfo(studentElement));
                }
            }
        }
        return res;
    }

    private String deleteById(Document document, String id) {
        NodeList nodeList = document.getElementsByTagName("Student");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element studentElement = (Element) node;
                String studentId = studentElement.getAttribute("ID");
                if (studentId.equals(id)) {
                    node.getParentNode().removeChild(node);
                    try {
                        saveXMLDocument(document);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return "node deleted successfully";

                }
            }
        }
        return "Student not found";
    }

    private List<Map<String, String>> searchByFirstName(Document document, String firstName) {
        List<Map<String, String>> res = new ArrayList<>();
        NodeList nodeList = document.getElementsByTagName("Student");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element studentElement = (Element) node;
                String studentFirstName = studentElement.getElementsByTagName("FirstName").item(0).getTextContent();
                if (studentFirstName.equalsIgnoreCase(firstName)) {
                    System.out.println("Search result by FirstName:");
                    res.add(fillStudentInfo(studentElement));
                }
            }
        }
        return res;
    }
//
//    private  void deleteRecord(Document document, int recordToDelete) {
//        NodeList nodeList = document.getElementsByTagName("Student");
//        if (recordToDelete >= 1 && recordToDelete <= nodeList.getLength()) {
//            Node nodeToRemove = nodeList.item(recordToDelete - 1);
//            nodeToRemove.getParentNode().removeChild(nodeToRemove);
//            System.out.println("Record deleted successfully.");
//        } else {
//            System.out.println("Invalid record number.");
//        }
//    }

    private void saveXMLDocument(Document document) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File("university.xml"));
        transformer.transform(source, result);
    }

    private Map<String, String> fillStudentInfo(Element studentElement) {
        Map<String, String> student = new HashMap<>();
        student.put("ID", studentElement.getAttribute("ID"));
        student.put("FirstName", studentElement.getElementsByTagName("FirstName").item(0).getTextContent());
        student.put("LastName", studentElement.getElementsByTagName("LastName").item(0).getTextContent());
        student.put("Gender", studentElement.getElementsByTagName("Gender").item(0).getTextContent());
        student.put("GPA", studentElement.getElementsByTagName("GPA").item(0).getTextContent());
        student.put("Level", studentElement.getElementsByTagName("Level").item(0).getTextContent());
        student.put("Address", studentElement.getElementsByTagName("Address").item(0).getTextContent());
        return student;
    }

    private Document loadOrCreateXMLFile() throws ParserConfigurationException {
        Document document;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(new File("university.xml"));
        } catch (Exception e) {
            // If the file doesn't exist, create a new document
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();
            Element university = document.createElement("University");
            document.appendChild(university);
        }
        return document;
    }

    private static void addStudentData(Document document, Map<String, String> data) {

        Iterator<Map.Entry<String, String>> iterator = data.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println("Key: " + key + ", Value: " + value);
            Element studentElement = document.createElement("Student");

            studentElement.setAttribute("ID", entry.getValue());
            entry = iterator.next();

            createElementAndAppend(document, studentElement, "FirstName", entry.getValue());
            entry = iterator.next();
            createElementAndAppend(document, studentElement, "LastName", entry.getValue());
            entry = iterator.next();
            createElementAndAppend(document, studentElement, "Gender", entry.getValue());
            entry = iterator.next();
            createElementAndAppend(document, studentElement, "GPA", entry.getValue());
            entry = iterator.next();
            createElementAndAppend(document, studentElement, "Level", entry.getValue());
            entry = iterator.next();
            createElementAndAppend(document, studentElement, "Address", entry.getValue());
            document.getDocumentElement().appendChild(studentElement);
        }

    }

    private static void createElementAndAppend(Document document, Element parentElement, String tagName, String textContent) {
        Element element = document.createElement(tagName);
        element.appendChild(document.createTextNode(textContent));
        parentElement.appendChild(element);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi2023.mavenproject1.readers.chainOfResponsibility;

import mephi2023.mavenproject1.readers.chainOfResponsibility.JsonHandler;
import mephi2023.mavenproject1.readers.chainOfResponsibility.YamlHandler;
import mephi2023.mavenproject1.readers.chainOfResponsibility.XmlHandler;
import java.util.ArrayList;
import mephi2023.mavenproject1.workWithCollection.Reactor;

/**
 *
 * @author Kseny
 */
public class BuildChain {
    private final Handler h1;
    private final Handler h2;
    private final Handler h3;
    public BuildChain(){
        h1 = new JsonHandler();
        h2 = new YamlHandler();
        h3 = new XmlHandler();
        h1.setNext(h2);
        h2.setNext(h3);        
    }
    public String readByChain(String fileName, ArrayList<Reactor> collection){
        return h1.handle(fileName, collection);
    }
}

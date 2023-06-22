/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi2023.mavenproject1.workWithCollection;

import java.util.ArrayList;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Kseny
 */
public class CollectionManipulation {

    private final ArrayList<Reactor> reactors;
    
    public CollectionManipulation(){
        reactors = new ArrayList<>();
        TypesStorage ts = new TypesStorage();
    }
        
    public void clearCollection(){
        this.reactors.clear();
    }
    
    public ArrayList<Reactor> getCollection(){
        return reactors;
    }
    
    public ArrayList<String> getReactorTypes(){
        return TypesStorage.getTypes();
    }
    
    private Reactor getReactorByType(String type){        
        for (Reactor r : reactors) {
            if (r.getType().equals(type)) {
                return r;
            }
        }
        return reactors.get(reactors.size()-1);
    }
    
    public double getBurnupByReactor(String type){
        type = type.trim();
        return getReactorByType(type).getBurnup();
    }
    public double getFirstLoadByReactor(String type){
        type = type.trim();
        return getReactorByType(type).getFirst_load();
    }
    
    public DefaultMutableTreeNode addInfoToTree(){        
        DefaultMutableTreeNode main = new DefaultMutableTreeNode("Реакторы");
        for (Reactor r: reactors){
            main.add(r.getNode());
        }
        return main;
    }
}

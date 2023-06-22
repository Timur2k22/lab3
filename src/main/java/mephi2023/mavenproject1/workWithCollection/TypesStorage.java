/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi2023.mavenproject1.workWithCollection;

import java.util.ArrayList;

/**
 *
 * @author Kseny
 */
public class TypesStorage {
    private static ArrayList<String> types;
    
    public TypesStorage (){
        types = new ArrayList();
        types.add("MKER");
        types.add("RBMK");
        types.add("VVER-1200");
        types.add("AP1000");
        types.add("BWR");
        types.add("ABWR");
        types.add("EPR");
        types.add("MAGNOX");
        types.add("BN");
        types.add("PWR");
        types.add("CPR-1000");
        types.add("CANDU");
        types.add("PHWR");
        types.add("KLT-40");
    }
    
    public static ArrayList<String> getTypes() {
        return types;
    }
    
}

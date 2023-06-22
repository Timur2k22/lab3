/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mephi2023.mavenproject1.readers.chainOfResponsibility;

import java.util.ArrayList;
import mephi2023.mavenproject1.workWithCollection.Reactor;

/**
 *
 * @author Kseny
 */
public interface Handler {
    public void setNext(Handler h);
    public String handle(String fileName, ArrayList<Reactor> collection);
}

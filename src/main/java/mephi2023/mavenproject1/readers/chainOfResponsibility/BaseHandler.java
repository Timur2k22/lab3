/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi2023.mavenproject1.readers.chainOfResponsibility;

import mephi2023.mavenproject1.readers.chainOfResponsibility.Handler;
import java.util.ArrayList;
import mephi2023.mavenproject1.workWithCollection.Reactor;

/**
 *
 * @author Kseny
 */
public class BaseHandler implements Handler {
    private Handler next = null;
    @Override
    public void setNext(Handler h) {
        next = h;
    }

    @Override
    public String handle(String fileName, ArrayList<Reactor> collection) {
        if (next != null){
            return next.handle(fileName, collection);
        }
        return "файл не прочитан";
    }
    
}

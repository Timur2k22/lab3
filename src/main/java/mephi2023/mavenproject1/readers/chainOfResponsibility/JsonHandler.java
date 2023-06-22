/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi2023.mavenproject1.readers.chainOfResponsibility;

import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mephi2023.mavenproject1.workWithCollection.Reactor;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Kseny
 */
public class JsonHandler extends BaseHandler{
    @Override
    public String handle(String fileName, ArrayList<Reactor> collection) {
        try {
            String source = "json";
            JSONArray jsonArray = (JSONArray) new JSONParser().parse(new FileReader(fileName));
            for (int i = 0; i < jsonArray.size(); i++) {
                Reactor newReactor = new Gson().fromJson(jsonArray.get(i).toString(), Reactor.class);
                newReactor.setSource(source);
                collection.add(newReactor);
            }
            return "json";
        } catch (Exception ex) {
            return super.handle(fileName, collection);
            //Logger.getLogger(JsonHandler.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi2023.mavenproject1.readers.chainOfResponsibility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import mephi2023.mavenproject1.workWithCollection.Reactor;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author Kseny
 */
public class YamlHandler extends BaseHandler{
    @Override
    public String handle (String fileName, ArrayList<Reactor> collection) {
        InputStream inputStream = null;
        try {
            String source = "yaml";
            inputStream = new FileInputStream(new File(fileName));
            Yaml yaml = new Yaml();
            ArrayList<LinkedHashMap<String,Object>> data = yaml.load(inputStream);
            for (LinkedHashMap<String,Object> d: data){
                collection.add(new Reactor(d, source));
            }
            return "yaml";
        } catch (Exception ex) {
            return super.handle(fileName, collection);
            //Logger.getLogger(YamlHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
                //Logger.getLogger(YamlHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

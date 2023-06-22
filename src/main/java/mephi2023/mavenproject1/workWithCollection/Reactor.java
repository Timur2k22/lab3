/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi2023.mavenproject1.workWithCollection;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

/**
 *
 * @author Kseny
 */
public class Reactor {

    private String type;
    private double burnup;
    private double kpd;
    private double enrichment;
    private double termal_capacity;
    private double electrical_capacity;
    private double life_time;
    private double first_load;
    private String source;

    public Reactor() {
    }

    public Reactor(String type, double burnup, double kpd, double enrichment, 
                   double termal_capacity, double electrical_capacity, double life_time, 
                   double first_load, String source) {
        this.type = type;
        this.burnup = burnup;
        this.kpd = kpd;
        this.enrichment = enrichment;
        this.termal_capacity = termal_capacity;
        this.electrical_capacity = electrical_capacity;
        this.life_time = life_time;
        this.first_load = first_load;
        this.source = source;
    }
    public Reactor(ArrayList<String> info, String source) {
        int tempID = 0;
        this.type = info.get(tempID++);        
        this.burnup = Double.parseDouble(info.get(tempID++));
        this.kpd = Double.parseDouble(info.get(tempID++));
        this.enrichment = Double.parseDouble(info.get(tempID++));
        this.termal_capacity = Double.parseDouble(info.get(tempID++));
        this.electrical_capacity = Double.parseDouble(info.get(tempID++));
        this.life_time = Double.parseDouble(info.get(tempID++));
        this.first_load = Double.parseDouble(info.get(tempID++));
        this.source = source;
    }
    public Reactor(LinkedHashMap<String,Object> lhm, String source) {
        this.type = (String) lhm.get("type");
        this.burnup = (double) lhm.get("burnup");
        this.kpd = (double) lhm.get("kpd");
        this.enrichment = (double) lhm.get("enrichment");
        this.termal_capacity = (double) lhm.get("termal_capacity");
        this.electrical_capacity = (double) lhm.get("electrical_capacity");
        this.life_time = (double) lhm.get("life_time");
        this.first_load = (double) lhm.get("first_load");
        this.source = source;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setBurnup(double burnup) {
        this.burnup = burnup;
    }

    public void setKpd(double kpd) {
        this.kpd = kpd;
    }

    public void setEnrichment(double enrichment) {
        this.enrichment = enrichment;
    }

    public void setTermal_capacity(double termal_capacity) {
        this.termal_capacity = termal_capacity;
    }

    public void setElectrical_capacity(double electrical_capacity) {
        this.electrical_capacity = electrical_capacity;
    }

    public void setLife_time(double life_time) {
        this.life_time = life_time;
    }

    public void setFirst_load(double first_load) {
        this.first_load = first_load;
    }
    
    public void setSource (String source){
        this.source = source;
    }
    
    public String getType() {
        return type;
    }

    public double getBurnup() {
        return burnup;
    }

    public double getKpd() {
        return kpd;
    }

    public double getEnrichment() {
        return enrichment;
    }

    public double getTermal_capacity() {
        return termal_capacity;
    }

    public double getElectrical_capacity() {
        return electrical_capacity;
    }

    public double getLife_time() {
        return life_time;
    }

    public double getFirst_load() {
        return first_load;
    }
    
    public String getSource(){
        return source;
    }
   
    /*@Override
    public String toString() {
        return  "\nType: " + this.type + "\nBurnup: " + this.burnup + 
                "\nKPD: " + this.kpd + "\nEnrichment: " + this.enrichment + 
                "\nThermal capacity: " + this.termal_capacity + 
                "\nElectrical capacity: " + this.electrical_capacity + 
                "\nLife time: " + this.life_time + "\nFirst load: " + this.first_load +
                "\nSource: " + this.source;
    }*/
    

    public MutableTreeNode getNode() {
        DefaultMutableTreeNode rnode = new DefaultMutableTreeNode(this.type);
        rnode.add(new DefaultMutableTreeNode("Выгорание [использование топлива]: " + this.burnup));
        rnode.add(new DefaultMutableTreeNode("КПД: " + this.kpd));
        rnode.add(new DefaultMutableTreeNode("Обогащение: " + this.enrichment));
        rnode.add(new DefaultMutableTreeNode("Тепловая мощность: " + this.termal_capacity));
        rnode.add(new DefaultMutableTreeNode("Электрическая мощность: " + this.electrical_capacity));
        rnode.add(new DefaultMutableTreeNode("Время эксплуатации: " + this.life_time));
        rnode.add(new DefaultMutableTreeNode("Первая загрузка U продукта: " + this.first_load));
        rnode.add(new DefaultMutableTreeNode("Тип файла загрузки: " + this.source));
        
        return rnode;
    }
    
}

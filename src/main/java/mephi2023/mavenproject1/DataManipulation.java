/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi2023.mavenproject1;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.tree.DefaultMutableTreeNode;
import mephi2023.mavenproject1.readers.ReaderTXT;
import mephi2023.mavenproject1.readers.chainOfResponsibility.BuildChain;
import mephi2023.mavenproject1.workWithBD.BDManipulation;
import mephi2023.mavenproject1.workWithBD.ConnectBD;
import mephi2023.mavenproject1.workWithBD.QuariesStorage;
import mephi2023.mavenproject1.workWithCollection.CollectionManipulation;
import mephi2023.mavenproject1.workWithTable.Exemplar;
import mephi2023.mavenproject1.workWithTable.TableManipulation;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 *
 * @author Kseny
 */
public class DataManipulation {
    private final CollectionManipulation cm;
    private final ConnectBD cbd;
    public DataManipulation(JLabel instruction1Label,JLabel exceptionLabel){
        cm = new CollectionManipulation();
        cbd = new ConnectBD();
        QuariesStorage qs = new QuariesStorage();
        try {
            Connection conn = cbd.getConnection();
            if (!BDManipulation.checkTables(conn, QuariesStorage.getQueryFindUnitTable())){
                instruction1Label.setText("1. Необходимо создать базу данных.");
            } else {
                instruction1Label.setText("1. База данных создана и заполнена. Можно перейти к шагу 5.");
            }
            exceptionLabel.setText("");
        } catch (Exception ex) {
            instruction1Label.setText("1. Необходимо создать базу данных.");
        }
    }
    
    public void readByBuildChain(String fileName, JLabel fileLabel,JLabel exceptionLabel){
        cm.clearCollection();
        BuildChain bc = new BuildChain();
        String source = bc.readByChain(fileName, cm.getCollection());
        fileLabel.setText(source);
        exceptionLabel.setText(""); 
    }
    
    public DefaultMutableTreeNode getMainNode(){
        return cm.addInfoToTree();
    }
    
    public void createBD(JLabel instruction1Label,JLabel exceptionLabel){
        try {
            Connection conn = cbd.getConnection();
            String queryDelete = ReaderTXT.read(".\\resources\\deletion.txt");
            String queryCreate = ReaderTXT.read(".\\resources\\creation.txt");
            BDManipulation.doQuery(conn, queryDelete, "удалена");
            BDManipulation.doQuery(conn, queryCreate, "создана");
            instruction1Label.setText("1. База данных создана. Перейдите к шагу 3.");
            exceptionLabel.setText("");
            
        } catch (SQLException | IOException ex) {
            exceptionLabel.setText("Ошибка:" + ex.getMessage());
        }
    }
    
    public void deleteBD(JLabel instruction1Label,JLabel exceptionLabel){
        try {
            Connection conn = cbd.getConnection();
            String queryDelete = ReaderTXT.read(".\\resources\\deletion.txt");
            BDManipulation.doQuery(conn, queryDelete, "удалена");
            instruction1Label.setText("1. Необходимо создать базу данных.");
            exceptionLabel.setText("");
        } catch (SQLException | IOException ex) {
            exceptionLabel.setText("Ошибка:" + ex.getMessage());
        }
    }
    
    public void fillBD(JLabel instruction1Label,JLabel exceptionLabel){
        try {
            Connection conn = cbd.getConnection();
            String fileName = ".\\resources\\ReactorData.xlsx";
            if (!cm.getCollection().isEmpty()){
                exceptionLabel.setText("База данных заполняется...");
                BDManipulation.fillBd(conn, fileName, cm);
                instruction1Label.setText("1. База данных создана и заполнена. Можно перейти к шагу 5.");
                exceptionLabel.setText("");
            } else {                
                exceptionLabel.setText("Не хватает данных для заполнения базы, выберите файл.");
            }           
        } catch (SQLException | IOException | InvalidFormatException ex) {
            exceptionLabel.setText("Ошибка:" + ex.getMessage());
        } 
    }
    
    public void countFuelForEachReactor(JTable fuelTable, JLabel totalFuelLabel, 
            JLabel exceptionLabel){
        try {
            Connection conn = cbd.getConnection();
            ArrayList<Exemplar> parameters = BDManipulation.getAnnuelFuelReactor(conn, QuariesStorage.getQueryEachReactor());
            TableManipulation.drawModel(parameters, "Реактор", fuelTable);
            totalFuelLabel.setText(String.valueOf(BDManipulation.getSumAnnuelFuelReactor(conn, QuariesStorage.getQuerySumReactor())));
            exceptionLabel.setText("");
        } catch (SQLException ex) {
            exceptionLabel.setText("Ошибка:" + ex.getMessage());
        }
    }
    
    public void countFuelForCountryReactor(JTable fuelTable, JLabel totalFuelLabel, 
            JLabel exceptionLabel){
        try {
            Connection conn = cbd.getConnection();
            ArrayList<Exemplar> parameters = BDManipulation.getAnnuelFuelReactor(conn, QuariesStorage.getQueryCountryReactor());
            TableManipulation.drawModel(parameters, "Страна", fuelTable);
            totalFuelLabel.setText(String.valueOf(BDManipulation.getSumAnnuelFuelReactor(conn, QuariesStorage.getQuerySumReactor())));
            exceptionLabel.setText("");
        } catch (SQLException ex) {
            exceptionLabel.setText("Ошибка:" + ex.getMessage());
        }
    }
    
    public void countFuelForRegionReactor(JTable fuelTable, JLabel totalFuelLabel, 
            JLabel exceptionLabel){
        try {
            Connection conn = cbd.getConnection();
            ArrayList<Exemplar> parameters = BDManipulation.getAnnuelFuelReactor(conn, QuariesStorage.getQueryRegionReactor());
            TableManipulation.drawModel(parameters, "Регион", fuelTable);
            totalFuelLabel.setText(String.valueOf(BDManipulation.getSumAnnuelFuelReactor(conn, QuariesStorage.getQuerySumReactor())));
            exceptionLabel.setText("");
        } catch (SQLException ex) {
            exceptionLabel.setText("Ошибка:" + ex.getMessage());
        }
    }
    
    public void countFuelForCompanyReactor(JTable fuelTable, JLabel totalFuelLabel, 
            JLabel exceptionLabel){
        try {
            Connection conn = cbd.getConnection();
            ArrayList<Exemplar> parameters = BDManipulation.getAnnuelFuelReactor(conn, QuariesStorage.getQueryCompanyReactor());
            TableManipulation.drawModel(parameters, "Компания", fuelTable);
            totalFuelLabel.setText(BDManipulation.getSumAnnuelFuelReactor(conn, QuariesStorage.getQuerySumReactor()));
            exceptionLabel.setText("");
        } catch (SQLException ex) {
            exceptionLabel.setText("Ошибка:" + ex.getMessage());
        }
    }
    
    
}

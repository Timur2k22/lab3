/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi2023.mavenproject1.workWithBD;

import mephi2023.mavenproject1.workWithTable.Exemplar;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import mephi2023.mavenproject1.workWithCollection.CollectionManipulation;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Kseny
 */
public class BDManipulation {
    public static void doQuery(Connection conn, String query, String name) {
        try (Statement stmt = conn.createStatement()) {
            try {
                stmt.executeUpdate(query);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Таблица успешно : " + name);
        } catch (SQLException e) {
            System.out.println("Ошибка при подключении к базе данных: " + e.getMessage());
        }
    }
    
    
    private static void updateDefaultValuesBd(Connection conn) throws SQLException{
        ArrayList<String> updateQueries = new ArrayList<>();
        updateQueries.add("INSERT INTO public.sites (id, npp_name, place, owner_id) VALUES (247, 'NO DATA', 1, 1);");
        updateQueries.add("UPDATE public.units SET site=247 WHERE site IS NULL;");
        updateQueries.add("UPDATE public.units SET load_factor=90 WHERE load_factor IS NULL;");
        updateQueries.add("UPDATE public.companies SET country_id=1 WHERE country_id IS NULL;");
        
        for (String sql : updateQueries){
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
            pstmt.close();
        }
    }
    
    private static void updateClassBd(Connection conn) throws SQLException{
        ArrayList<String> updateQueries = new ArrayList<>();
        updateQueries.add("UPDATE public.units SET class='MAGNOX' WHERE class LIKE '%AGR%';");
        updateQueries.add("UPDATE public.units SET class='PWR' WHERE class LIKE '%PWR%';");
        updateQueries.add("UPDATE public.units SET class='CPR-1000' WHERE class LIKE '%CNP%' "
                         + "OR class LIKE '%Hualong%' OR class LIKE '%APR%' OR class LIKE '%ACP%' OR class LIKE '%AСPR-1000%' ;");
        updateQueries.add("UPDATE public.units SET class='VVER-1200' WHERE class LIKE '%VVER%';");
        updateQueries.add("UPDATE public.units SET class='MAGNOX' WHERE class LIKE '%HTGR%';");
        
        for (String sql : updateQueries){
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
            pstmt.close();
        }
    }
    
    private static void updateBurnupBd(Connection conn, CollectionManipulation cm) throws SQLException{
        ArrayList<String> typesReactor = cm.getReactorTypes();        
        String sql = "UPDATE public.units SET burnup=(SELECT CAST(? AS NUMERIC(6,3))) WHERE TRIM(class)=?;";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for (String tr : typesReactor){
            pstmt.setString(1, String.valueOf(cm.getBurnupByReactor(tr)));
            pstmt.setString(2, tr);
            pstmt.executeUpdate();
        }
        pstmt.close();
    }

    private static void updateFirstLoadBd(Connection conn, CollectionManipulation cm) throws SQLException{
        ArrayList<String> typesReactor = cm.getReactorTypes();        
        String sql = "UPDATE public.units SET first_load=(SELECT CAST(? AS NUMERIC(6,3))) WHERE TRIM(class)=?;";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for (String tr : typesReactor){
            pstmt.setString(1, String.valueOf(cm.getFirstLoadByReactor(tr)));
            pstmt.setString(2, tr);
            pstmt.executeUpdate();
        }
        pstmt.close();
    }
    
    public static void fillBd(Connection conn, String fileName, CollectionManipulation cm) throws FileNotFoundException, IOException, SQLException, InvalidFormatException{
        File file = new File(fileName);
        Workbook workbook = new XSSFWorkbook(file);
        int sequenceNumbers[] = {3,2,4,1,0};
        for (int i : sequenceNumbers) {
            Sheet sheet = workbook.getSheetAt(i);
            String tableName = sheet.getSheetName();
            int numColumns = sheet.getRow(0).getLastCellNum();
            String sql = "INSERT INTO " + tableName + " VALUES (";
            for (int j = 0; j < numColumns; j++) {
                sql += "?";
                if (j < numColumns - 1) {
                    sql += ",";
                }
            }
            sql += ")";
            System.out.println(sql);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cell.getCellType()) {
                        case NUMERIC -> {
                            if (DateUtil.isCellDateFormatted(cell)) {
                                java.util.Date date = cell.getDateCellValue();
                                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                                pstmt.setDate(cell.getColumnIndex() + 1, sqlDate);
                            } else {
                                String cellValue = cell.toString();
                                if (NumberUtils.isCreatable(cellValue)) {
                                    double doubleValue = Double.parseDouble(cellValue);
                                    if (doubleValue == Math.floor(doubleValue)) {
                                        int intValue = (int) doubleValue;
                                        pstmt.setInt(cell.getColumnIndex() + 1, intValue);
                                    } else {
                                        pstmt.setDouble(cell.getColumnIndex() + 1, doubleValue);
                                    }
                                }
                            }
                        }
                        case STRING -> {
                            if ("".equals(cell.getStringCellValue())) {
                                pstmt.setNull(cell.getColumnIndex() + 1, Types.NULL);
                            } else {
                                pstmt.setString(cell.getColumnIndex() + 1, cell.getStringCellValue());
                            }
                        }
                        case BOOLEAN -> pstmt.setString(cell.getColumnIndex() + 1, String.valueOf(cell.getBooleanCellValue()));
                        case BLANK -> pstmt.setNull(cell.getColumnIndex() + 1, Types.NULL);
                    }
                }
                System.out.println(tableName);
                pstmt.executeUpdate();
            }
            pstmt.close();
        }
        System.out.println("Заполнили базу, теперь обновляем данные");
        updateDefaultValuesBd(conn);
        updateClassBd(conn);
        updateBurnupBd(conn, cm);
        updateFirstLoadBd(conn, cm);
        System.out.println("Данные успешно загружены в базу данных");
        
    }

    /*public static void doQueries(Connection conn, CollectionManipulation cm) throws SQLException{
        updateClassBd(conn);
        updateBurnupBd(conn, cm);
        getQBEachReactor(conn);
        getCountEachReactor(conn);
        getQBCountryReactor(conn);
        getQBRegionReactor(conn);
        getQBCompanyReactor(conn);
        
        
    }*/
    
    /*private static int getCountColumn(Connection conn, String query) throws SQLException{
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        int n = (rs.getMetaData().getColumnCount());
        rs.close();
        stmt.close();
        
        return n;
    }*/
    
    public static String getSumAnnuelFuelReactor(Connection conn, String query) throws SQLException{
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        String res = rs.getString(1);
        rs.close();
        stmt.close();
        return res;
    }
    
    public static ArrayList<Exemplar> getAnnuelFuelReactor(Connection conn, String query) throws SQLException{
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        ArrayList<Exemplar> res = new ArrayList<>();
        while (rs.next()) {
            Exemplar ex = new Exemplar(rs.getString(2), 
                    Double.valueOf(rs.getString(1))); 

            System.out.print(rs.getString(2) + " " + rs.getString(1));        
            System.out.println("*");
            res.add(ex);
        }
        System.out.println("");
        rs.close();
        stmt.close();
        return res;
    }
    //
    public static boolean checkTables(Connection conn, String query) throws Exception{
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        System.out.println(rs.getString(1));        
        int res = rs.getInt(1);
        rs.close();
        stmt.close();
        return res > 0;
    }
    
    
    
    /*public static ArrayList<Object> getQBEachReactor(Connection conn) throws SQLException{
        String query = """
                       SELECT (thermal_capacity*load_factor*365/burnup/1000/100), burnup, (thermal_capacity*load_factor), unit_name
                       FROM public.units
                       WHERE commercial_operation < '2023-01-01' AND date_shutdown > '2023-12-31';""";
        String query = """
                       SELECT SUM(thermal_capacity*load_factor*365/burnup/1000/100)
                       FROM public.units
                       WHERE commercial_operation < '2023-01-01' AND date_shutdown > '2023-12-31';""";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        ArrayList<Object> res = new ArrayList<>();
        while (rs.next()) {
            String str = rs.getString(1) ; 
            System.out.print(str + " ");
            int n = 1;
            for (int t = 2; t <= n; t++){
                System.out.print(rs.getString(t) + " ");
            }         
            System.out.println(" *");
            res.add(str);
        }
        System.out.println("");
        rs.close();
        stmt.close();
        return res;
    }
    public static ArrayList<Object> getQBCountryReactor(Connection conn) throws SQLException{
        String query = """
                       SELECT SUM(thermal_capacity*load_factor*365/burnup/1000/100), country_name
                       FROM public.units 
                       JOIN public.sites ON public.units.site = public.sites.id
                       JOIN public.countries ON public.sites.place = public.countries.id
                       WHERE public.units.commercial_operation < '2023-01-01' AND 
                             public.units.date_shutdown > '2023-12-31'
                       GROUP BY public.countries.country_name;""";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        ArrayList<Object> res = new ArrayList<>();
        while (rs.next()) {
            String str = rs.getString(1) ; 
            System.out.print(str + " ");
            int n = 2;
            for (int t = 2; t <= n; t++){
                System.out.print(rs.getString(t) + " ");
            }         
            System.out.println(" *");
            res.add(str);
        }
        System.out.println("");
        rs.close();
        stmt.close();
        return res;
    }
    public static ArrayList<Object> getQBRegionReactor(Connection conn) throws SQLException{
        String query = """
                       SELECT SUM(thermal_capacity*load_factor*365/burnup/1000/100), region_name
                       FROM public.units 
                       JOIN public.sites ON public.units.site = public.sites.id
                       JOIN public.countries ON public.sites.place = public.countries.id
                       JOIN public.regions ON public.countries.region_id = public.regions.id
                       WHERE public.units.commercial_operation < '2023-01-01' AND 
                             public.units.date_shutdown > '2023-12-31'
                       GROUP BY public.regions.region_name;""";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        ArrayList<Object> res = new ArrayList<>();
        while (rs.next()) {
            String str = rs.getString(1) ; 
            System.out.print(str + " ");
            int n = 2;
            for (int t = 2; t <= n; t++){
                System.out.print(rs.getString(t) + " ");
            }         
            System.out.println(" *");
            res.add(str);
        }
        System.out.println("");
        rs.close();
        stmt.close();
        return res;
    }
    public static ArrayList<Object> getQBCompanyReactor(Connection conn) throws SQLException{
        String query = """
                       SELECT SUM(thermal_capacity*load_factor*365/burnup/1000/100), companies_name
                       FROM public.units 
                       JOIN public.sites ON public.units.site = public.sites.id
                       JOIN public.companies ON public.sites.owner_id = public.companies.id
                       WHERE public.units.commercial_operation < '2023-01-01' AND 
                             public.units.date_shutdown > '2023-12-31'
                       GROUP BY public.companies.companies_name;""";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        ArrayList<Object> res = new ArrayList<>();
        while (rs.next()) {
            String str = rs.getString(1) ; 
            System.out.print(str + " ");
            int n = 2;
            for (int t = 2; t <= n; t++){
                System.out.print(rs.getString(t) + " ");
            }         
            System.out.println(" *");
            res.add(str);
        }
        System.out.println("");
        rs.close();
        stmt.close();
        return res;
    }
    
    public static void getCountEachReactor(Connection conn) throws SQLException{
        String query = """
                       SELECT COUNT(*)
                       FROM public.units
                       JOIN public.sites ON public.units.site = public.sites.id
                       JOIN public.companies ON public.sites.owner_id = public.companies.id;""";
                       //JOIN public.countries ON public.sites.place = public.countries.id
                       //JOIN public.regions ON public.countries.region_id = public.regions.id;""";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            System.out.println(rs.getString(1));
        }
        System.out.println("");
        rs.close();
        stmt.close();
    }
    
    public static ArrayList<ArrayList<Object>> getInfoEachColumn(Connection conn, String query) throws SQLException{
        int n = getCountColumn(conn, query);
        ArrayList<ArrayList<Object>> res = new ArrayList<>(); 
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            ArrayList<Object> resStr = new ArrayList<>();
            System.out.println("*");
            for (int i = 1; i <= n; i++){
                String str = rs.getString(i) ;
                System.out.print(str + " ");
                resStr.add(rs.getString(i));
            }
            res.add(resStr);
        }
        System.out.println("");
        rs.close();
        stmt.close();
       
        System.out.println("*******************");
        return res;
    }*/
}


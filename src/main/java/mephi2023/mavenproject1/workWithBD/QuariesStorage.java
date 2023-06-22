/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi2023.mavenproject1.workWithBD;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Kseny
 */
public class QuariesStorage {
    private static String queryEachReactor;
    private static String queryCountryReactor;
    private static String queryRegionReactor;
    private static String queryCompanyReactor;
    private static String querySumReactor;
    private static String queryFindUnitTable;
    private static String queryInOperationReactor;
    
    
    public QuariesStorage(){      
        queryInOperationReactor = """
                       SELECT thermal_capacity*load_factor*365/burnup/1000/100 AS annuel_fuel, unit_name
                       FROM public.units
                       WHERE commercial_operation < '2023-01-01' AND date_shutdown > '2023-12-31'
                       ORDER BY annuel_fuel DESC;""";
        String subQuery = """
                        WITH sampleOFUnits AS
                            (SELECT thermal_capacity*load_factor*365/burnup/1000/100 AS annuel_fuel, unit_name, site
                            FROM public.units
                            WHERE commercial_operation < '2023-01-01' AND date_shutdown > '2023-12-31'
                            UNION
                            SELECT first_load AS annuel_fuel, unit_name, site
                            FROM public.units
                            WHERE commercial_operation > '2022-12-31' AND commercial_operation < '2024-01-01'
                            ORDER BY annuel_fuel DESC)""";
        
        queryEachReactor = subQuery + """
                       SELECT annuel_fuel, unit_name
                       FROM sampleOFUnits;""";
        
        querySumReactor = subQuery + """
                       SELECT SUM(annuel_fuel)
                       FROM sampleOFUnits;""";
        queryCountryReactor = subQuery + """
                       SELECT SUM(annuel_fuel) AS annuel_fuel, country_name
                       FROM sampleOFUnits 
                       JOIN public.sites ON sampleOFUnits.site = public.sites.id
                       JOIN public.countries ON public.sites.place = public.countries.id
                       GROUP BY public.countries.country_name
                       ORDER BY annuel_fuel DESC;""";
        queryRegionReactor = subQuery + """
                       SELECT SUM(annuel_fuel) AS annuel_fuel, region_name
                       FROM sampleOFUnits 
                       JOIN public.sites ON sampleOFUnits.site = public.sites.id
                       JOIN public.countries ON public.sites.place = public.countries.id
                       JOIN public.regions ON public.countries.region_id = public.regions.id
                       GROUP BY public.regions.region_name
                       ORDER BY annuel_fuel DESC;""";
        queryCompanyReactor = subQuery + """
                       SELECT SUM(annuel_fuel) AS annuel_fuel, companies_name
                       FROM sampleOFUnits 
                       JOIN public.sites ON sampleOFUnits.site = public.sites.id
                       JOIN public.companies ON public.sites.owner_id = public.companies.id
                       GROUP BY public.companies.companies_name
                       ORDER BY annuel_fuel DESC;""";
        
        queryFindUnitTable = """
                             SELECT COUNT(*) FROM public.units;""";
    }
    
    public  static String getQueryInOperationReactor() {
        return queryInOperationReactor;
    }
    
    public  static String getQueryFindUnitTable() {
        return queryFindUnitTable;
    }
    
    public  static String getQueryEachReactor() {
        return queryEachReactor;
    }

    public static String getQueryCountryReactor() {
        return queryCountryReactor;
    }

    public static String getQueryRegionReactor() {
        return queryRegionReactor;
    }

    public static String getQueryCompanyReactor() {
        return queryCompanyReactor;
    }
    
    public static String getQuerySumReactor() {
        return querySumReactor;
    }
}

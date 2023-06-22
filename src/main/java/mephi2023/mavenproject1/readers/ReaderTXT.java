/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi2023.mavenproject1.readers;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * @author Kseny
 */
public class ReaderTXT {
    public static String read(String fileName) throws IOException{
        List<String> linesCreate = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
        String contentCreate = String.join(System.lineSeparator(), linesCreate);
        return contentCreate;
    }            
}

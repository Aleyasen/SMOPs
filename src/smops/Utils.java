/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smops;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Aale
 */
public class Utils {

    public static String truncate(String str, int len) {
        if (len > str.length()) {
            return str;
        }
        return str.substring(0, len);
    }

    public static void writeDataIntoFile(String content, String filepath) {
        try {
//            System.out.println("writeDataIntoFile filepath=" + filepath);
            FileWriter fw = new FileWriter(filepath, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
//            System.out.println("File saved in " + filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> readFileLineByLine(String filepath) {
        List<String> lines = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filepath));
            String line = br.readLine();
            while (line != null) {
                lines.add(line);
                line = br.readLine();
            }
            br.close();
            return lines;
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    static final String pattern = "([a-zA-Z,0-9,\\-,_,\\.]+)\\.js\\b";
    static Pattern r = Pattern.compile(pattern);

    public static String matchJsLib(String str) {

        Matcher m = r.matcher(str);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(matchJsLib("js/jquery.shuffle.js?DE"));

        System.out.println(matchJsLib("js/jquery.shuffle.jsp"));

    }
}

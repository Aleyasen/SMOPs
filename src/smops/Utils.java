/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smops;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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
        return readFileLineByLine(filepath, null);
    }

    public static List<String> readFileLineByLine(String filepath, Integer limit) {
        int count = 0;
        int max;
        if (limit == null) {
            max = Integer.MAX_VALUE;
        } else {
            max = limit;
        }
        List<String> lines = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filepath));
            String line = br.readLine();
            count++;
            while (line != null) {
                lines.add(line);
                line = br.readLine();
                count++;
                if (count > max) {
                    break;
                }
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
        splitFile("1000-websites\\websites_1000.txt", "1000-websites\\split", 50);
//        System.out.println(matchJsLib("js/jquery.shuffle.js?DE"));

//        System.out.println(matchJsLib("js/jquery.shuffle.jsp"));
    }

    public static String getDomain(String url) {
        System.out.println("url=" + url);
        int slashslash = 0;
        if (url.contains("http://") || url.contains("https://")) {
            slashslash = url.indexOf("//") + 2;
        }
        System.out.println("slashslash=" + slashslash);
        System.out.println("2==" + url.indexOf("/", slashslash));
        String domain_str = null;
        if (url.indexOf("/", slashslash) == -1) {
            domain_str = url.substring(slashslash);
        } else {
            domain_str = url.substring(slashslash, url.indexOf("/", slashslash));
        }
        System.out.println(domain_str);
        return domain_str;
    }

    public static void splitFile(String inputfile, String outputfilePrefix, int inEachFile) {
        PrintWriter writer = null;
        try {
            final List<String> lines = Utils.readFileLineByLine(inputfile);
            int index = 0;

            int fileIndex = 1;
            String outputfile = outputfilePrefix + "-" + fileIndex + ".txt";
            writer = new PrintWriter(outputfile);
            while (true) {
                if (index >= lines.size()) {
                    writer.close();
                    break;
                }
                writer.println(lines.get(index));
                index++;
                if (index % inEachFile == 0) {
                    fileIndex++;
                    writer.close();
                    outputfile = outputfilePrefix + "-" + fileIndex + ".txt";
                    writer = new PrintWriter(outputfile);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            writer.close();
        }
    }
}

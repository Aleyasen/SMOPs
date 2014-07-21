/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smops.dao;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Aale
 */
public class Seal {

    String name;
    String url;
    String anchorText;

    public static List<Seal> seals = new ArrayList<Seal>();

    static {
        initializeSealsList();
    }

    public Seal() {
    }

    public Seal(String name, String url, String anchorText) {
        this.name = name;
        this.url = url;
        this.anchorText = anchorText;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAnchorText() {
        return anchorText;
    }

    public void setAnchorText(String anchorText) {
        this.anchorText = anchorText;
    }

    public static String getSealByAnchorText(String text) {
        for (Seal seal : seals) {
            if (text.toLowerCase().contains(seal.getAnchorText().toLowerCase())) {
                return seal.getName();
            }
        }
        return null;
    }

    public static String getSealByURL(String url) {
        for (Seal seal : seals) {
            if (url.toLowerCase().contains(seal.getUrl().toLowerCase())) {
                return seal.getName();
            }
        }
        return null;
    }

    private static void initializeSealsList() {
        String filepath = "conf/seals.txt";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filepath));
            String line = br.readLine();

            while (line != null) {
                String[] split = line.split("\\t");
                if (split.length == 3) {
                    seals.add(new Seal(split[0], split[1], split[2]));
                } else {
                    System.out.println("error in parsing seals.txt . split size = " + split.length);
                }
                line = br.readLine();
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println(seals);
    }

    @Override
    public String toString() {
        return "Seal{" + "name=" + name + ", url=" + url + ", anchorText=" + anchorText + '}';
    }

}

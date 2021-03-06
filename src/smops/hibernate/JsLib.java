package smops.hibernate;
// Generated Jul 15, 2014 3:02:40 PM by Hibernate Tools 3.6.0


import java.util.HashSet;
import java.util.Set;

/**
 * JsLib generated by hbm2java
 */
public class JsLib  implements java.io.Serializable {


     private Integer id;
     private String name;
     private String libVersion;
     private String fullname;
     private String type;
     private Set jsBusinesses = new HashSet(0);

    public JsLib() {
    }

    public JsLib(String name, String libVersion, String fullname, String type, Set jsBusinesses) {
       this.name = name;
       this.libVersion = libVersion;
       this.fullname = fullname;
       this.type = type;
       this.jsBusinesses = jsBusinesses;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getLibVersion() {
        return this.libVersion;
    }
    
    public void setLibVersion(String libVersion) {
        this.libVersion = libVersion;
    }
    public String getFullname() {
        return this.fullname;
    }
    
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    public Set getJsBusinesses() {
        return this.jsBusinesses;
    }
    
    public void setJsBusinesses(Set jsBusinesses) {
        this.jsBusinesses = jsBusinesses;
    }




}



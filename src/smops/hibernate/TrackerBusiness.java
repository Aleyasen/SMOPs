package smops.hibernate;
// Generated Jun 19, 2014 11:11:54 AM by Hibernate Tools 3.6.0



/**
 * TrackerBusiness generated by hbm2java
 */
public class TrackerBusiness  implements java.io.Serializable {


     private Integer id;
     private Business business;
     private Tracker tracker;
     private Integer type;
     private String url;

    public TrackerBusiness() {
    }

    public TrackerBusiness(Business business, Tracker tracker, Integer type, String url) {
       this.business = business;
       this.tracker = tracker;
       this.type = type;
       this.url = url;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Business getBusiness() {
        return this.business;
    }
    
    public void setBusiness(Business business) {
        this.business = business;
    }
    public Tracker getTracker() {
        return this.tracker;
    }
    
    public void setTracker(Tracker tracker) {
        this.tracker = tracker;
    }
    public Integer getType() {
        return this.type;
    }
    
    public void setType(Integer type) {
        this.type = type;
    }
    public String getUrl() {
        return this.url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }




}



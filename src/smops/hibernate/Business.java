package smops.hibernate;
// Generated Jun 17, 2014 10:27:47 AM by Hibernate Tools 3.6.0


import java.util.HashSet;
import java.util.Set;

/**
 * Business generated by hbm2java
 */
public class Business  implements java.io.Serializable {


     private Integer id;
     private String yelpId;
     private Boolean isClaimed;
     private Boolean isClose;
     private String name;
     private String url;
     private String phone;
     private Long reviewCount;
     private String categories;
     private Double rating;
     private String snippetText;
     private String address;
     private String displayAddress;
     private String city;
     private String stateCode;
     private String postalCode;
     private String countryCode;
     private String neighborhoods;
     private String website;
     private Set jsBusinesses = new HashSet(0);
     private Set trackerBusinesses = new HashSet(0);
     private Set forms = new HashSet(0);

    public Business() {
    }

    public Business(String yelpId, Boolean isClaimed, Boolean isClose, String name, String url, String phone, Long reviewCount, String categories, Double rating, String snippetText, String address, String displayAddress, String city, String stateCode, String postalCode, String countryCode, String neighborhoods, String website, Set jsBusinesses, Set trackerBusinesses, Set forms) {
       this.yelpId = yelpId;
       this.isClaimed = isClaimed;
       this.isClose = isClose;
       this.name = name;
       this.url = url;
       this.phone = phone;
       this.reviewCount = reviewCount;
       this.categories = categories;
       this.rating = rating;
       this.snippetText = snippetText;
       this.address = address;
       this.displayAddress = displayAddress;
       this.city = city;
       this.stateCode = stateCode;
       this.postalCode = postalCode;
       this.countryCode = countryCode;
       this.neighborhoods = neighborhoods;
       this.website = website;
       this.jsBusinesses = jsBusinesses;
       this.trackerBusinesses = trackerBusinesses;
       this.forms = forms;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public String getYelpId() {
        return this.yelpId;
    }
    
    public void setYelpId(String yelpId) {
        this.yelpId = yelpId;
    }
    public Boolean getIsClaimed() {
        return this.isClaimed;
    }
    
    public void setIsClaimed(Boolean isClaimed) {
        this.isClaimed = isClaimed;
    }
    public Boolean getIsClose() {
        return this.isClose;
    }
    
    public void setIsClose(Boolean isClose) {
        this.isClose = isClose;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getUrl() {
        return this.url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    public String getPhone() {
        return this.phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public Long getReviewCount() {
        return this.reviewCount;
    }
    
    public void setReviewCount(Long reviewCount) {
        this.reviewCount = reviewCount;
    }
    public String getCategories() {
        return this.categories;
    }
    
    public void setCategories(String categories) {
        this.categories = categories;
    }
    public Double getRating() {
        return this.rating;
    }
    
    public void setRating(Double rating) {
        this.rating = rating;
    }
    public String getSnippetText() {
        return this.snippetText;
    }
    
    public void setSnippetText(String snippetText) {
        this.snippetText = snippetText;
    }
    public String getAddress() {
        return this.address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    public String getDisplayAddress() {
        return this.displayAddress;
    }
    
    public void setDisplayAddress(String displayAddress) {
        this.displayAddress = displayAddress;
    }
    public String getCity() {
        return this.city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    public String getStateCode() {
        return this.stateCode;
    }
    
    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }
    public String getPostalCode() {
        return this.postalCode;
    }
    
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    public String getCountryCode() {
        return this.countryCode;
    }
    
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    public String getNeighborhoods() {
        return this.neighborhoods;
    }
    
    public void setNeighborhoods(String neighborhoods) {
        this.neighborhoods = neighborhoods;
    }
    public String getWebsite() {
        return this.website;
    }
    
    public void setWebsite(String website) {
        this.website = website;
    }
    public Set getJsBusinesses() {
        return this.jsBusinesses;
    }
    
    public void setJsBusinesses(Set jsBusinesses) {
        this.jsBusinesses = jsBusinesses;
    }
    public Set getTrackerBusinesses() {
        return this.trackerBusinesses;
    }
    
    public void setTrackerBusinesses(Set trackerBusinesses) {
        this.trackerBusinesses = trackerBusinesses;
    }
    public Set getForms() {
        return this.forms;
    }
    
    public void setForms(Set forms) {
        this.forms = forms;
    }




}



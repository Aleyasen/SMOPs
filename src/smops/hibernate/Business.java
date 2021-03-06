package smops.hibernate;
// Generated Jul 15, 2014 3:02:40 PM by Hibernate Tools 3.6.0


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
     private String domain;
     private String source;
     private Boolean smops;
     private String serverType;
     private Boolean supportLogin;
     private Boolean isThirdPartyLogin;
     private String thirdPartyLoginList;
     private Boolean isTrackerAds;
     private Boolean isCrawled;
     private Integer numPages;
     private Boolean hasCookie;
     private Integer cookieLifetime;
     private Boolean hasContactUs;
     private Boolean hasPrivacyPolicy;
     private Boolean hasEmailAddress;
     private Boolean hasPhoneNumber;
     private Boolean hasPhysicalAddress;
     private String contactUsUrl;
     private String contactUsAnchorText;
     private String privacyPolicyUrl;
     private String privacyPolicyAnchorText;
     private String wbEmailAddress;
     private String wbPhoneNumber;
     private String wbPhysicalAddress;
     private String loginUrl;
     private String loginAnchorText;
     private String websiteType;
     private Integer rank;
     private Set trackerBusinesses = new HashSet(0);
     private Set sealBusinesses = new HashSet(0);
     private Set forms = new HashSet(0);
     private Set jsBusinesses = new HashSet(0);

    public Business() {
    }

    public Business(String yelpId, Boolean isClaimed, Boolean isClose, String name, String url, String phone, Long reviewCount, String categories, Double rating, String snippetText, String address, String displayAddress, String city, String stateCode, String postalCode, String countryCode, String neighborhoods, String website, String domain, String source, Boolean smops, String serverType, Boolean supportLogin, Boolean isThirdPartyLogin, String thirdPartyLoginList, Boolean isTrackerAds, Boolean isCrawled, Integer numPages, Boolean hasCookie, Integer cookieLifetime, Boolean hasContactUs, Boolean hasPrivacyPolicy, Boolean hasEmailAddress, Boolean hasPhoneNumber, Boolean hasPhysicalAddress, String contactUsUrl, String contactUsAnchorText, String privacyPolicyUrl, String privacyPolicyAnchorText, String wbEmailAddress, String wbPhoneNumber, String wbPhysicalAddress, String loginUrl, String loginAnchorText, String websiteType, Integer rank, Set trackerBusinesses, Set sealBusinesses, Set forms, Set jsBusinesses) {
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
       this.domain = domain;
       this.source = source;
       this.smops = smops;
       this.serverType = serverType;
       this.supportLogin = supportLogin;
       this.isThirdPartyLogin = isThirdPartyLogin;
       this.thirdPartyLoginList = thirdPartyLoginList;
       this.isTrackerAds = isTrackerAds;
       this.isCrawled = isCrawled;
       this.numPages = numPages;
       this.hasCookie = hasCookie;
       this.cookieLifetime = cookieLifetime;
       this.hasContactUs = hasContactUs;
       this.hasPrivacyPolicy = hasPrivacyPolicy;
       this.hasEmailAddress = hasEmailAddress;
       this.hasPhoneNumber = hasPhoneNumber;
       this.hasPhysicalAddress = hasPhysicalAddress;
       this.contactUsUrl = contactUsUrl;
       this.contactUsAnchorText = contactUsAnchorText;
       this.privacyPolicyUrl = privacyPolicyUrl;
       this.privacyPolicyAnchorText = privacyPolicyAnchorText;
       this.wbEmailAddress = wbEmailAddress;
       this.wbPhoneNumber = wbPhoneNumber;
       this.wbPhysicalAddress = wbPhysicalAddress;
       this.loginUrl = loginUrl;
       this.loginAnchorText = loginAnchorText;
       this.websiteType = websiteType;
       this.rank = rank;
       this.trackerBusinesses = trackerBusinesses;
       this.sealBusinesses = sealBusinesses;
       this.forms = forms;
       this.jsBusinesses = jsBusinesses;
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
    public String getDomain() {
        return this.domain;
    }
    
    public void setDomain(String domain) {
        this.domain = domain;
    }
    public String getSource() {
        return this.source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    public Boolean getSmops() {
        return this.smops;
    }
    
    public void setSmops(Boolean smops) {
        this.smops = smops;
    }
    public String getServerType() {
        return this.serverType;
    }
    
    public void setServerType(String serverType) {
        this.serverType = serverType;
    }
    public Boolean getSupportLogin() {
        return this.supportLogin;
    }
    
    public void setSupportLogin(Boolean supportLogin) {
        this.supportLogin = supportLogin;
    }
    public Boolean getIsThirdPartyLogin() {
        return this.isThirdPartyLogin;
    }
    
    public void setIsThirdPartyLogin(Boolean isThirdPartyLogin) {
        this.isThirdPartyLogin = isThirdPartyLogin;
    }
    public String getThirdPartyLoginList() {
        return this.thirdPartyLoginList;
    }
    
    public void setThirdPartyLoginList(String thirdPartyLoginList) {
        this.thirdPartyLoginList = thirdPartyLoginList;
    }
    public Boolean getIsTrackerAds() {
        return this.isTrackerAds;
    }
    
    public void setIsTrackerAds(Boolean isTrackerAds) {
        this.isTrackerAds = isTrackerAds;
    }
    public Boolean getIsCrawled() {
        return this.isCrawled;
    }
    
    public void setIsCrawled(Boolean isCrawled) {
        this.isCrawled = isCrawled;
    }
    public Integer getNumPages() {
        return this.numPages;
    }
    
    public void setNumPages(Integer numPages) {
        this.numPages = numPages;
    }
    public Boolean getHasCookie() {
        return this.hasCookie;
    }
    
    public void setHasCookie(Boolean hasCookie) {
        this.hasCookie = hasCookie;
    }
    public Integer getCookieLifetime() {
        return this.cookieLifetime;
    }
    
    public void setCookieLifetime(Integer cookieLifetime) {
        this.cookieLifetime = cookieLifetime;
    }
    public Boolean getHasContactUs() {
        return this.hasContactUs;
    }
    
    public void setHasContactUs(Boolean hasContactUs) {
        this.hasContactUs = hasContactUs;
    }
    public Boolean getHasPrivacyPolicy() {
        return this.hasPrivacyPolicy;
    }
    
    public void setHasPrivacyPolicy(Boolean hasPrivacyPolicy) {
        this.hasPrivacyPolicy = hasPrivacyPolicy;
    }
    public Boolean getHasEmailAddress() {
        return this.hasEmailAddress;
    }
    
    public void setHasEmailAddress(Boolean hasEmailAddress) {
        this.hasEmailAddress = hasEmailAddress;
    }
    public Boolean getHasPhoneNumber() {
        return this.hasPhoneNumber;
    }
    
    public void setHasPhoneNumber(Boolean hasPhoneNumber) {
        this.hasPhoneNumber = hasPhoneNumber;
    }
    public Boolean getHasPhysicalAddress() {
        return this.hasPhysicalAddress;
    }
    
    public void setHasPhysicalAddress(Boolean hasPhysicalAddress) {
        this.hasPhysicalAddress = hasPhysicalAddress;
    }
    public String getContactUsUrl() {
        return this.contactUsUrl;
    }
    
    public void setContactUsUrl(String contactUsUrl) {
        this.contactUsUrl = contactUsUrl;
    }
    public String getContactUsAnchorText() {
        return this.contactUsAnchorText;
    }
    
    public void setContactUsAnchorText(String contactUsAnchorText) {
        this.contactUsAnchorText = contactUsAnchorText;
    }
    public String getPrivacyPolicyUrl() {
        return this.privacyPolicyUrl;
    }
    
    public void setPrivacyPolicyUrl(String privacyPolicyUrl) {
        this.privacyPolicyUrl = privacyPolicyUrl;
    }
    public String getPrivacyPolicyAnchorText() {
        return this.privacyPolicyAnchorText;
    }
    
    public void setPrivacyPolicyAnchorText(String privacyPolicyAnchorText) {
        this.privacyPolicyAnchorText = privacyPolicyAnchorText;
    }
    public String getWbEmailAddress() {
        return this.wbEmailAddress;
    }
    
    public void setWbEmailAddress(String wbEmailAddress) {
        this.wbEmailAddress = wbEmailAddress;
    }
    public String getWbPhoneNumber() {
        return this.wbPhoneNumber;
    }
    
    public void setWbPhoneNumber(String wbPhoneNumber) {
        this.wbPhoneNumber = wbPhoneNumber;
    }
    public String getWbPhysicalAddress() {
        return this.wbPhysicalAddress;
    }
    
    public void setWbPhysicalAddress(String wbPhysicalAddress) {
        this.wbPhysicalAddress = wbPhysicalAddress;
    }
    public String getLoginUrl() {
        return this.loginUrl;
    }
    
    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }
    public String getLoginAnchorText() {
        return this.loginAnchorText;
    }
    
    public void setLoginAnchorText(String loginAnchorText) {
        this.loginAnchorText = loginAnchorText;
    }
    public String getWebsiteType() {
        return this.websiteType;
    }
    
    public void setWebsiteType(String websiteType) {
        this.websiteType = websiteType;
    }
    public Integer getRank() {
        return this.rank;
    }
    
    public void setRank(Integer rank) {
        this.rank = rank;
    }
    public Set getTrackerBusinesses() {
        return this.trackerBusinesses;
    }
    
    public void setTrackerBusinesses(Set trackerBusinesses) {
        this.trackerBusinesses = trackerBusinesses;
    }
    public Set getSealBusinesses() {
        return this.sealBusinesses;
    }
    
    public void setSealBusinesses(Set sealBusinesses) {
        this.sealBusinesses = sealBusinesses;
    }
    public Set getForms() {
        return this.forms;
    }
    
    public void setForms(Set forms) {
        this.forms = forms;
    }
    public Set getJsBusinesses() {
        return this.jsBusinesses;
    }
    
    public void setJsBusinesses(Set jsBusinesses) {
        this.jsBusinesses = jsBusinesses;
    }




}



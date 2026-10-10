package model;

public class Certification {

    private String name;
    private String issuingOrganization;

    public Certification(String name, String issuingOrganization) {
        this.name = name;
        this.issuingOrganization = issuingOrganization;
    }

    public String getName() {
        return name;
    }

    public String getIssuingOrganization() {
        return issuingOrganization;
    }
}
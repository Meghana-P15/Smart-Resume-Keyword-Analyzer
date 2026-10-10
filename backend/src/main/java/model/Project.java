package model;

public class Project {

    private String title;
    private String description;
    private String technologies;

    public Project(String title, String description, String technologies) {
        this.title = title;
        this.description = description;
        this.technologies = technologies;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTechnologies() {
        return technologies;
    }
}
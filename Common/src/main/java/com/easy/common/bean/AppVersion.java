package com.easy.common.bean;

public class AppVersion {
    private String type;
    private String version;
    private String download_url;
    private String title;
    private String notes;
    private String force_version;
    private String package_url;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getForce_version() {
        return force_version;
    }

    public void setForce_version(String force_version) {
        this.force_version = force_version;
    }

    public String getPackage_url() {
        return package_url;
    }

    public void setPackage_url(String package_url) {
        this.package_url = package_url;
    }

    @Override
    public String toString() {
        return "AppVersion{" +
                "type='" + type + '\'' +
                ", version='" + version + '\'' +
                ", download_url='" + download_url + '\'' +
                ", title='" + title + '\'' +
                ", notes='" + notes + '\'' +
                ", force_version='" + force_version + '\'' +
                ", package_url='" + package_url + '\'' +
                '}';
    }
}

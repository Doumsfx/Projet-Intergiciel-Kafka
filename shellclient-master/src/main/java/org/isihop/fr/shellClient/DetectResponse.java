package org.isihop.fr.shellClient;

public class DetectResponse {
    private String language;

    public DetectResponse() {}

    public DetectResponse(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}

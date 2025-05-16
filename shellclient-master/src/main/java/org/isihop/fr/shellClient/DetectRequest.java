package org.isihop.fr.shellClient;

public class DetectRequest {
    private String q;

    public DetectRequest() {}
    
    public DetectRequest(String q) {
        this.q = q;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }
}

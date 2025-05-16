package org.isihop.fr.shellClient;

public class TranslateRequest {
    private String q;
    private String source;
    private String target;
    private String format = "text"; // Valeur par défaut
    private int alternatives = 1;

    public TranslateRequest() {}

    public TranslateRequest(String q, String source, String target, int alternatives) {
        this.q = q;
        this.source = source;
        this.target = target;
        this.format = "text";
        this.alternatives = alternatives;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setAlternatives(int alternatives) {
        this.alternatives = alternatives;
    }
    
    public int getAlternatives() {
        return alternatives;
    }
}
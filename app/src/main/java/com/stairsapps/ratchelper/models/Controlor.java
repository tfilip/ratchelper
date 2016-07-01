package com.stairsapps.ratchelper.models;

import java.util.Date;

public class Controlor {

    private String line;
    private String numar;
    private String data;
    private boolean liber;

    public Controlor(String line, String numar, String data, boolean liber) {
        this.line = line;
        this.numar = numar;
        this.data = data;
        this.liber = liber;
    }

    public Controlor(String line, String data, boolean liber) {
        this.line = line;
        this.data = data;
        this.liber = liber;
    }

    public Controlor() {
    }

    public String getLine() {
        return line;
    }

    public String getNumar() {
        return numar;
    }

    public String getData() {
        return data;
    }

    public boolean isLiber() {
        return liber;
    }
}

package com.example.rmorawski.letsmakesomefun.settings;

/**
 * Created by r.morawski on 3/23/2018.
 */

public class Settings {

    private static Settings instance;

    private String ip;
    private int port;
    private boolean debug;
    private boolean logged;

    public static Settings getInstance() {
        if(instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public boolean isDebug() {
        return debug;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }
}

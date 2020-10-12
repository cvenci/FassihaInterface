package com.example.fassiha;

public class FassihaResponse {
    public int level;
    public int app_id;
    public String core;
    public String args;

    public FassihaResponse(int level, int app_id, String core, String args){
        this.level = level;
        this.app_id = app_id;
        this.core = core;
        this.args = args;
    }
}

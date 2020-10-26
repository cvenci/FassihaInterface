package com.example.fassiha.dataclass;

public class FassihaResponse {
    public int level;
    public int app_id;
    public String core;
    public String args;
    public String command;

    public FassihaResponse(int level, int app_id, String core, String args, String command){
        this.level = level;
        this.app_id = app_id;
        this.core = core;
        this.args = args;
        this.command = command;
    }
}

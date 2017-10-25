package com.example.robert.smartremote;

/**
 * Created by yunl on 10/24/2017.
 */

public class CommandManager {

    private String ip;
    public CommandManager(String ip){
        this.ip  = ip;
    }

    public CommandManager(){

    }

    public void setDestIpAddr(String ip){
        this.ip = ip;
    }

    public void sendScrollUp(){
        String[] command = new String[]{CmdType.SCROLL_UP, ip};
        new SocketController().execute(command);
    }

    public void sendScrollDown(){
        String[] command = new String[]{CmdType.SCROLL_DOWN, ip};
        new SocketController().execute(command);
    }
}

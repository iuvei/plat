package com.na.manager.common.websocket;

/**
 * Created by sunny on 2017/6/27 0027.
 */
public class MyMessage {
    private int cmd;
    private String text;

    public MyMessage() {
    }

    public MyMessage(CmdEnum cmd, String text) {
        this.cmd = cmd.get();
        this.text = text;
    }

    public int getCmd() {
        return cmd;
    }

    public CmdEnum getCmdEnum(){
        return CmdEnum.get(cmd);
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

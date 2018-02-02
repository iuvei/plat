package com.na.betRobot.cache;

import com.na.betRobot.ServerClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * Author: Alan
 * Date: 2018/1/27 14:48
 */
public class RobotCache {

    private static List<ServerClient> clients;

    public static List<ServerClient> getClients() {
        if(clients == null) {
            clients = new Vector<>();
        }
        return clients;
    }

    public static void setClients(List<ServerClient> clients) {
        RobotCache.clients = clients;
    }
}

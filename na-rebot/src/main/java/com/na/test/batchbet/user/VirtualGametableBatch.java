package com.na.test.batchbet.user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by Administrator on 2017/5/23 0023.
 */
public class VirtualGametableBatch {
    private final static String DB_USER = "nalive";
    private final static String DB_PWD = "Newasia888";
    private final static String DB_IP ="nalive.cuyhebproway.ap-southeast-1.rds.amazonaws.com";
    private final static String DB_NAME ="nalive";

    public static void main(String[] args) {
        int gid = 302;
        int num = 2000;
        insertVirTable(gid, num);
    }

    private static void insertVirTable(int gid, int num) {
        String url = String.format("jdbc:mysql://%s/%s?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&allowMultiQueries=true",DB_IP,DB_NAME);
        String querysql = "select * from gametable where tid = %d";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url,DB_USER,DB_PWD);
            PreparedStatement st = conn.prepareStatement(String.format(querysql,gid));
            ResultSet resultSet = st.executeQuery();
            Virtualgametable virtualgametable = null;
            if(resultSet.next()) {
                virtualgametable = new Virtualgametable();
                virtualgametable.tid = resultSet.getString("tid");
                virtualgametable.gid = resultSet.getString("gid");
                virtualgametable.cn = resultSet.getString("cn");
                virtualgametable.en = resultSet.getString("en");
                virtualgametable.status = resultSet.getString("status");
                virtualgametable.CountDownSeconds = resultSet.getString("CountDownSeconds");
                virtualgametable.type = resultSet.getString("type");
                virtualgametable.tablecode = resultSet.getString("tablecode");
                virtualgametable.tablenum = resultSet.getString("tablenum");
            }

            if(virtualgametable!=null) {
                for(int i=0;i<num;i++) {
                    String sql = "INSERT INTO `virtualgametable` (`Tid`,`Gid`,`Cn`,`En`,`Status`,`CountDownSeconds`,`Type`,`tablecode`,`tablenum`,`orderby`) values(?,?,?,?,?,?,?,?,?,?)";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    int index = 1;
                    ps.setObject(index++, virtualgametable.tid);
                    ps.setObject(index++, virtualgametable.gid);
                    ps.setObject(index++, virtualgametable.cn);
                    ps.setObject(index++, virtualgametable.en);
                    ps.setObject(index++, virtualgametable.status);
                    ps.setObject(index++, virtualgametable.CountDownSeconds);
                    ps.setObject(index++, virtualgametable.type);
                    ps.setObject(index++, virtualgametable.tablecode);
                    ps.setObject(index++, virtualgametable.tablenum);
                    ps.setObject(index++, i);
                    ps.execute();
                }
            }else {
                System.out.println("游戏桌没找到……");
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    static class Virtualgametable{
        public String tid;
        public String gid;
        public String cn;
        public String en;
        public String status;
        public String CountDownSeconds;
        public String type;
        public String tablecode;
        public String tablenum;
        public String orderby;
    }
}

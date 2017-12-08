package com.gameportal.web.util.ip;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;

public class IPUtils {
    
    public final static String FILE_IPSECTION = "PHIPS.txt";
    private static Logger logger = Logger.getLogger(IPUtils.class);
    
    /*
     * 验证IP是否属于某个IP段 ipSection IP段（以'-'分隔） ip 所验证的IP号码
     */
    public static boolean ipExistsInRange(String ip, String ipSection) {

        ipSection = ipSection.trim();

        ip = ip.trim();

        int idx = ipSection.indexOf('-');

        String beginIP = ipSection.substring(0, idx);

        String endIP = ipSection.substring(idx + 1);

        return getIp2long(beginIP) <= getIp2long(ip) && getIp2long(ip) <= getIp2long(endIP);

    }

    public static long getIp2long(String ip) {
        
        ip = ip.trim();

        String[] ips = ip.split("\\.");

        long ip2long = 0L;

        for (int i = 0; i < 4; ++i) {

            ip2long = ip2long << 8 | Integer.parseInt(ips[i]);

        }

        return ip2long;

    }

    public static long getIp2long2(String ip) {

        ip = ip.trim();

        String[] ips = ip.split("\\.");

        long ip1 = Integer.parseInt(ips[0]);

        long ip2 = Integer.parseInt(ips[1]);

        long ip3 = Integer.parseInt(ips[2]);

        long ip4 = Integer.parseInt(ips[3]);

        long ip2long = 1L * ip1 * 256 * 256 * 256 + ip2 * 256 * 256 + ip3 * 256 + ip4;

        return ip2long;

    }
    
    /**
     * 校验IP是否属于菲律宾IP
     * @param ip
     * @return
     */
    public static boolean checkIpIsPH(String ip){
        if(ip.contains(":") || ip.equals("127.0.0.1") || ip.equals("locahost")){
            return false;
        }
        BufferedReader reader = null;
        try {
            String path = IPUtils.class.getResource(FILE_IPSECTION).getPath();
            File file = new File(path);
            reader = new BufferedReader(new FileReader(file));
            String lineStr = null;
            while ((lineStr = reader.readLine())!=null) {
                String[] ipInfo = lineStr.split("\t") ;
                String ipStart = ipInfo[0];
                String ipEnd = ipInfo[1];
                String ipSection = ipStart + "-" + ipEnd;
//                logger.info("ipSection:"+ipSection);
                boolean exists = ipExistsInRange(ip, ipSection);
                if(exists){
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
        
    }
    
    public static void main(String[] args) {
        String ip = "115.146.128.0";
        System.out.println(checkIpIsPH(ip));
            
        //10.10.10.116 是否属于固定格式的IP段10.10.1.00-10.10.255.255
        /*String ip = "10.10.10.116";
        String ipSection = "10.10.1.00-10.10.255.255";
        boolean exists = ipExistsInRange(ip, ipSection);
        System.out.println(exists);
        System.out.println(getIp2long(ip));
        System.out.println(getIp2long2(ip));*/
    }

}

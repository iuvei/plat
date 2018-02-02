package com.na.user.socketserver.util;

/**
 * IP转换工具类
 * Created by sunny on 2017/7/18 0018.
 */
public class IpUtils {
    public static long ip2Num(String ip){
        String[] ips = ip.split("\\.");
        long num = 0;
        num |= (Long.valueOf(ips[0])<<24);
        num |= (Long.valueOf(ips[1])<<16);
        num |= (Long.valueOf(ips[2])<<8);
        num |= (Long.valueOf(ips[3]));
        return num;
    }

    public static String num2Ip(Long num){
        StringBuilder ip = new StringBuilder()
                .append((0x00_00_00_00_ff_ff_ff_ffL & num)>>24).append(".")
                .append((0x00_00_00_00_00_ff_ff_ffL & num)>>16).append(".")
                .append((0x00_00_00_00_00_00_ff_ffL & num)>>8).append(".")
                .append((0x00_00_00_00_00_00_00_ffL & num));
        return ip.toString();
    }


    public static void main(String[] args) {
        String ip = "192.168.0.2";
        long num=ip2Num(ip);
        String ip2 = num2Ip(num);
        System.out.println(num+"    "+ip2);
    }
}

package com.gameportal.comms.http.utils;

import java.util.NoSuchElementException;

/**
 * 这个类表示一个IP地址由32位整数值表示。点分十进制表示法将
 * 32位的地址分成4个8位（一个字节）字段，并指定每个字段的值，作为一个独立的
 * 十进制数由圆点分隔的字段
 * IP地址分为三类
 * @author cs
 * @version 1.0
 * @see IPAddress
 */
public class IPAddress implements Cloneable{
	
	 protected int ipAddress = 0;
	 
	 /**
	  * 返回的IP地址的整数表示。
	  * @return
	  */
	 public final int getIPAddress() {
		 return ipAddress;
	 }
	 
	 /**
	  * 构造函数
	  * @param ipAddressStr IP的IP地址的字符串表示形式。的IP地址的字符串表示形式的格式必须按照十进制点缀符号xxx.xxx.xxx.xxx。
	  */
	 public IPAddress(String ipAddressStr){
        ipAddress = parseIPAddress(ipAddressStr);
     }
	 
	 /**
      * 构造函数.
      * @param IP二进制表示的IP地址。
      */
     public IPAddress(int address){
        ipAddress = address;
     }
	 
	 /**
	  * 返回常见的十进制点符号为xxx.xxx.xxx.xxx的IP地址的字符串表示形式。
	  * @return 返回的IP地址的字符串表示形式。
	  */
	 public String toString() {
        StringBuffer result = new StringBuffer();
        int temp;
        
        temp = ipAddress & 0x000000FF;
        result.append(temp);
        result.append(".");

        temp = (ipAddress >> 8) & 0x000000FF;
        result.append(temp);
        result.append(".");

        temp = (ipAddress >> 16) & 0x000000FF;
        result.append(temp);
        result.append(".");

        temp = (ipAddress >> 24) & 0x000000FF;
        result.append(temp);

        return result.toString();
    }
	
	/**
	 * 检查如果IP地址是属于一类A的IP地址。
	 * @return <code>true</code> 如果封装的IP地址属于一类A的IP地址，否则
	 * @return <code>false</code>.
	 */
	public final boolean isClassA() {
        return (ipAddress & 0x00000001) == 0;
    }
	
	/**
	 * 检查如果IP地址是属于一个B类IP地址
	 * @return <code>true</code> 如果封装的IP地址属于一类B的IP地址，否则
	 * @return <code>false</code>
	 */
	public final boolean isClassB() {
        return (ipAddress & 0x00000003) == 1;
    }
	
	/**
	 * 检查如果IP地址是属于一类C的IP地址。
	 * @return <code>true</code> 如果封装的IP地址属于一类C的IP地址，否则
	 * @return <code>false</code>.
	 */
	public final boolean isClassC() {
        return (ipAddress & 0x00000007) == 3;
    }
	
	/**
	 * 一个十进制点符号表示一个IP地址转换成一个32位基于整数的值。
	 * @param ipAddressStr 十进制虚线标记（xxx.xxx.xxx.xxx）的IP地址.
	 * @return 返回的32位整数表示的IP地址。
	 * @exception InvalidIPAddressException抛出这个异常，如果指定的IP地址是不符合
	 * 十进制点符号xxx.xxx.xxx.xxx。
	 */
	final int parseIPAddress(String ipAddressStr) {
        int result = 0;

        if (ipAddressStr == null) {
            throw new IllegalArgumentException();
        }

        try {
            String tmp = ipAddressStr;

            // 获得3个数
            int offset = 0;
            for (int i = 0; i < 3; i++) {

                // 得到的第一个点的位置
                int index = tmp.indexOf('.');

                // 如果没有一个点，那么这个ip的字符串表示
                // 不兼容到小数点点缀表示。
                if (index != -1) {

                    // 前点的数量，并把它转换成
                    // 一个整数。
                    String numberStr = tmp.substring(0, index);
                    int number = Integer.parseInt(numberStr);
                    if ((number < 0) || (number > 255)) {
                        throw new IllegalArgumentException("Invalid IP Address [" + ipAddressStr + "]");
                    }

                    result += number << offset;
                    offset += 8;
                    tmp = tmp.substring(index + 1);
                } else {
                    throw new IllegalArgumentException("Invalid IP Address [" + ipAddressStr + "]");
                }
            }

            // 剩余部分的字符串应该是最后的数字。
            if (tmp.length() > 0) {
                int number = Integer.parseInt(tmp);
                if ((number < 0) || (number > 255)) {
                    throw new IllegalArgumentException("Invalid IP Address [" + ipAddressStr + "]");
                }

                result += number << offset;
                ipAddress = result;
            } else {
                throw new IllegalArgumentException("Invalid IP Address [" + ipAddressStr + "]");
            }
        } catch (NoSuchElementException ex) {
            throw new IllegalArgumentException("Invalid IP Address [" + ipAddressStr + "]", ex);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid IP Address [" + ipAddressStr + "]", ex);
        }

        return result;
    }
	
	public int hashCode() {
        return this.ipAddress;
    }

    public boolean equals(Object another) {
        if (another instanceof IPAddress) {
            return ipAddress == ((IPAddress) another).ipAddress;
        }
        return false;
    }

}

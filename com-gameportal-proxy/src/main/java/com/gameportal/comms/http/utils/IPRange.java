package com.gameportal.comms.http.utils;


/**
 *这个类表示一个IP范围，这是由一个IP地址和子网掩码表示。该标准
 *描述现代化的路由协议通常是指扩展网络前缀长度，而不是子网掩码。
 *的前缀长度等于相邻的一个在传统的子网掩码的位的数目。这意味着
 *指定的网络地址与子网掩码为255.255.255.0 130.5.5.25也可以表示为130.5.5.25/24 。
 *为符号更紧凑，更容易理解比写在其传统的面具
 *点分十进制格式。
 * 130.5.5.25
 *  10 000010 。 00000101 。 00000101 。 00011001 <BR/>
 * 255.255.255.0
 * 11111111。 11111111。 11111111。 00000000 <BR/>
 * < - 扩展网络前缀 - >
 *或
 * 130.5.5.25/24
 * 10  000010 。 00000101 。 00000101 。 00011001 <BR/>
 * 的
 *这个类支持两种标准：扩展网络前缀和子网掩码。
 * @version 1.0
 * @version 1.0
 * @see IPAddress
 */
public class IPRange {
	 /** IP 地址 */
    private IPAddress ipAddress             = null;

    /** IP子网掩码 */
    private IPAddress ipSubnetMask          = null;

    /** 扩展网络前缀 */
    private int       extendedNetworkPrefix = 0;
    
    /**
     * 构造函数
     * @param range IP的IP地址的字符串表示形式。支持以下两种格式：<br/>
     * <li/>xxx.xxx.xxx.xxx/xxx.xxx.xxx.xxx <li/>xxx.xxx.xxx.xxx/xx < - 扩展的网络前缀
     * @exception InvalidIPRangeException 指定的字符串时，抛出这个异常并不代表一个有效的IP地址。
     */
    public IPRange(String range){
        parseRange(range);
    }
    
    /**
     * 构造函数
     * @param ipAddress 引用到的IP地址数
     * @param subnetMask 引用子网掩码
     */
    public IPRange(IPAddress ipAddress, IPAddress subnetMask){
        if ((ipAddress == null) || (subnetMask == null)) {
            throw new IllegalArgumentException();
        }

        this.ipAddress = ipAddress;
        this.ipSubnetMask = subnetMask;

        extendedNetworkPrefix = computeNetworkPrefixFromMask(subnetMask);
        if (extendedNetworkPrefix == -1) {
            throw new IllegalArgumentException();
        }
    }
    
    /**
     * * 构造函数
     * @param ipAddress 该引用的IP地址
     * @param extendedNetworkPrefix 扩展网络前缀（0-32）。
     */
    public IPRange(IPAddress ipAddress, int extendedNetworkPrefix){
        if (ipAddress == null) {
            throw new IllegalArgumentException();
        }

        if ((extendedNetworkPrefix < 0) || (extendedNetworkPrefix > 32)) {
            throw new IllegalArgumentException();
        }

        this.ipAddress = ipAddress;
        this.extendedNetworkPrefix = extendedNetworkPrefix;
        this.ipSubnetMask = computeMaskFromNetworkPrefix(extendedNetworkPrefix);
    }
    
    public final IPAddress getIPAddress() {
        return ipAddress;
    }
    
    public final IPAddress getIPSubnetMask() {
        return ipSubnetMask;
    }
    
    public final int getExtendedNetworkPrefix() {
        return extendedNetworkPrefix;
    }
    
    /**
     * 转换成一个字符串表示的IP范围。
     * @return 返回的共同格式xxx.xxx.xxx.xxx/ XX（IP地址/扩展网络前缀），IP地址的字符串表示形式。
     */
    public String toString() {
        StringBuffer result = new StringBuffer(ipAddress.toString());
        result.append("/");
        result.append(extendedNetworkPrefix);

        return result.toString();
    }
    
    /**
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + extendedNetworkPrefix;
        result = prime * result + ((ipAddress == null) ? 0 : ipAddress.hashCode());
        result = prime * result + ((ipSubnetMask == null) ? 0 : ipSubnetMask.hashCode());
        return result;
    }
    
    /**
     * 比较指定IP范围封装。
     * @param 其他的IP范围进行比较。
     * @return <code>true</code>如果封装的IP范围是相同的指定的一个
     * @return 否则<code>false</code>
     */
    public boolean equals(Object another) {
        if (another instanceof IPRange) {
            IPRange range = (IPRange) another;

            return (ipAddress.equals(range.getIPAddress()) && (extendedNetworkPrefix == range.extendedNetworkPrefix));
        }
        return false;
    }
    
    /**
     * 解析字符串表示的IP范围。
     * @param range 范围内字符串表示形式的IP范围。
     * @exception InvalidIPRangeException 如果指定的范围是不是一个有效的IP网络范围，则抛出此异常。
     */
    final void parseRange(String range) {
        if (range == null) {
            throw new IllegalArgumentException("Invalid IP range");
        }

        int index = range.indexOf('/');
        String subnetStr = null;
        if (index == -1) {
            ipAddress = new IPAddress(range);
        } else {
            ipAddress = new IPAddress(range.substring(0, index));
            subnetStr = range.substring(index + 1);
        }

        // 尝试转换成十进制转换成其它的部分的范围值
        try {
            if (subnetStr != null) {
                extendedNetworkPrefix = Integer.parseInt(subnetStr);
                if ((extendedNetworkPrefix < 0) || (extendedNetworkPrefix > 32)) {
                    throw new IllegalArgumentException("Invalid IP range [" + range + "]");
                }
                ipSubnetMask = computeMaskFromNetworkPrefix(extendedNetworkPrefix);
            }
        } catch (NumberFormatException ex) {

            // 剩余的部分是不是一个有效的十进制值。
            // 检查，如果它是一个十进制点缀符号。
            ipSubnetMask = new IPAddress(subnetStr);

            // 建立相应的子网十进制
            extendedNetworkPrefix = computeNetworkPrefixFromMask(ipSubnetMask);
            if (extendedNetworkPrefix == -1) {
                throw new IllegalArgumentException("Invalid IP range [" + range + "]", ex);
            }
        }
    }
    
    /**
     * 计算扩展的网络前缀的IP子网掩码。
     * @param mask 参考子网掩码IP号码。
     * @return 返回扩展的网络前缀。返回-1，如果指定的面膜能不能转换成一个扩展的前缀网络。
     */
    private int computeNetworkPrefixFromMask(IPAddress mask) {

        int result = 0;
        int tmp = mask.getIPAddress();

        while ((tmp & 0x00000001) == 0x00000001) {
            result++;
            tmp = tmp >>> 1;
        }

        if (tmp != 0) {
            return -1;
        }
        return result;
    }
    
    public static String toDecimalString(String inBinaryIpAddress) {
        StringBuilder decimalip = new StringBuilder();
        String[] binary = new String[4];

        for (int i = 0, c = 0; i < 32; i = i + 8, c++) {
            binary[c] = inBinaryIpAddress.substring(i, i + 8);
            int octet = Integer.parseInt(binary[c], 2);
            decimalip.append(octet);
            if (c < 3) {
                
                decimalip.append('.');
            }
        }
        return decimalip.toString();
    }
    
    /**
     * 扩展的网络前缀整数转换成IP数字。
     * @param prefix 该网络前缀号码。
     * @return 返回对应的扩展网络前缀的IP号码。
     */
    private IPAddress computeMaskFromNetworkPrefix(int prefix) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            if (i < prefix) {
                str.append("1");
            } else {
                str.append("0");
            }
        }

        String decimalString = toDecimalString(str.toString());
        return new IPAddress(decimalString);
    }
    
    /**
     * 检查指定的IP地址是在封装的范围内。
     * @param address 该IP地址来进行测试。
     * @return <code>true</code>如果指定的IP地址是封装的IP范围内，否则返回
     * <code>false</code>
     */
    public boolean isIPAddressInRange(IPAddress address) {
        if (ipSubnetMask == null) {
            return this.ipAddress.equals(address);
        }
        
        int result1 = address.getIPAddress() & ipSubnetMask.getIPAddress();
        int result2 = ipAddress.getIPAddress() & ipSubnetMask.getIPAddress();

        return result1 == result2;
    }
    

}

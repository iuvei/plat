package com.gameportal.pay.util;
import java.io.ByteArrayOutputStream;  
  
public class Base64  
{  
    private static char[] base64EncodeChars = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',  
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',  
            'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };  
  
    private static byte[] base64DecodeChars = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55,  
            56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,  
            21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46,  
            47, 48, 49, 50, 51, -1, -1, -1, -1, -1 };  
  
    /**
     * Byte used to pad output.
     */
    static final byte PAD = (byte) '=';
    
    /**
     * The base length.
     */
    static final int BASELENGTH = 255;

    /**
     * Lookup length.
     */
    static final int LOOKUPLENGTH = 64;
    
    /**
     * Used to get the number of Quadruples.
     */
    static final int FOURBYTE = 4;

    
    private static byte[] base64Alphabet = new byte[BASELENGTH];
    private static byte[] lookUpBase64Alphabet = new byte[LOOKUPLENGTH];
    
    private Base64(){}  
  
    public static synchronized String encode(byte[] data)  
    {  
        int len = data.length;  
        int i = 0;  
        int b1, b2, b3;  
        StringBuilder sb = new StringBuilder(len);  
  
        while (i < len)  
        {  
            b1 = data[i++] & 0xff;  
            if (i == len)  
            {  
                sb.append(base64EncodeChars[b1 >>> 2]);  
                sb.append(base64EncodeChars[(b1 & 0x3) << 4]);  
                sb.append("==");  
                break;  
            }  
            b2 = data[i++] & 0xff;  
            if (i == len)  
            {  
                sb.append(base64EncodeChars[b1 >>> 2]);  
                sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);  
                sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);  
                sb.append("=");  
                break;  
            }  
            b3 = data[i++] & 0xff;  
            sb.append(base64EncodeChars[b1 >>> 2]);  
            sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);  
            sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);  
            sb.append(base64EncodeChars[b3 & 0x3f]);  
        }  
        return sb.toString();  
    }  
  
    public static synchronized byte[] decode(String str) {  
        byte[] data = str.getBytes();  
        int len = data.length;  
        ByteArrayOutputStream buf = new ByteArrayOutputStream(len);  
        int i = 0;  
        int b1, b2, b3, b4;  
  
        while (i < len)  
        {  
  
            /* b1 */  
            do  
            {  
                b1 = base64DecodeChars[data[i++]];  
            }  
            while (i < len && b1 == -1);  
            if (b1 == -1)  
            {  
                break;  
            }  
  
            /* b2 */  
            do  
            {  
                b2 = base64DecodeChars[data[i++]];  
            }  
            while (i < len && b2 == -1);  
            if (b2 == -1)  
            {  
                break;  
            }  
            buf.write((int) ((b1 << 2) | ((b2 & 0x30) >>> 4)));  
  
            /* b3 */  
            do  
            {  
                b3 = data[i++];  
                if (b3 == 61)  
                {  
                    return buf.toByteArray();  
                }  
                b3 = base64DecodeChars[b3];  
            }  
            while (i < len && b3 == -1);  
            if (b3 == -1)  
            {  
                break;  
            }  
            buf.write((int) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));  
  
            /* b4 */  
            do  
            {  
                b4 = data[i++];  
                if (b4 == 61)  
                {  
                    return buf.toByteArray();  
                }  
                b4 = base64DecodeChars[b4];  
            }  
            while (i < len && b4 == -1);  
            if (b4 == -1)  
            {  
                break;  
            }  
            buf.write((int) (((b3 & 0x03) << 6) | b4));  
        }  
        return buf.toByteArray();  
    }  
    
    private static boolean isBase64(byte octect) {
        if (octect == PAD) {
            return true;
        } else if (base64Alphabet[octect] == -1) {
            return false;
        } else {
            return true;
        }
    }
    
    static byte[] discardNonBase64(byte[] data) {
        byte groomedData[] = new byte[data.length];
        int bytesCopied = 0;

        for (int i = 0; i < data.length; i++) {
            if (isBase64(data[i])) {
                groomedData[bytesCopied++] = data[i];
            }
        }

        byte packedData[] = new byte[bytesCopied];

        System.arraycopy(groomedData, 0, packedData, 0, bytesCopied);

        return packedData;
    }
    
    public static byte[] decodeBase64(byte[] base64Data) {
        // RFC 2045 requires that we discard ALL non-Base64 characters
        base64Data = discardNonBase64(base64Data);

        // handle the edge case, so we don't have to worry about it later
        if (base64Data.length == 0) {
            return new byte[0];
        }
        int numberQuadruple = base64Data.length / FOURBYTE;
        byte decodedData[] = null;
        byte b1 = 0, b2 = 0, b3 = 0, b4 = 0, marker0 = 0, marker1 = 0;
        // Throw away anything not in base64Data
        int encodedIndex = 0;
        int dataIndex = 0;
        {
            // this sizes the output array properly - rlw
            int lastData = base64Data.length;
            // ignore the '=' padding
            while (base64Data[lastData - 1] == PAD) {
                if (--lastData == 0) {
                    return new byte[0];
                }
            }
            decodedData = new byte[lastData - numberQuadruple];
        }

        for (int i = 0; i < numberQuadruple; i++) {
            dataIndex = i * 4;
            marker0 = base64Data[dataIndex + 2];
            marker1 = base64Data[dataIndex + 3];

            b1 = base64Alphabet[base64Data[dataIndex]];
            b2 = base64Alphabet[base64Data[dataIndex + 1]];

            if (marker0 != PAD && marker1 != PAD) {
                //No PAD e.g 3cQl
                b3 = base64Alphabet[marker0];
                b4 = base64Alphabet[marker1];

                decodedData[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
                decodedData[encodedIndex + 1] =
                        (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
                decodedData[encodedIndex + 2] = (byte) (b3 << 6 | b4);
            } else if (marker0 == PAD) {
                //Two PAD e.g. 3c[Pad][Pad]
                decodedData[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
            } else if (marker1 == PAD) {
                //One PAD e.g. 3cQ[Pad]
                b3 = base64Alphabet[marker0];

                decodedData[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
                decodedData[encodedIndex + 1] =
                        (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
            }
            encodedIndex += 3;
        }
        return decodedData;
    }

    

}
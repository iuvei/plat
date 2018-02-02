package com.na.gate.proto.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.na.gate.proto.base.MyField;
import com.na.gate.proto.base.Request;
import com.na.gate.proto.base.STypeEnum;
import com.na.gate.proto.bean.CommandEnum;

import io.netty.buffer.ByteBuf;

/**
 * Created by sunny on 2017/7/25 0025.
 */
public class DecodeUtils {
	private static Logger logger = LoggerFactory.getLogger(DecodeUtils.class);
	public static void encodeRequest(List<Request> msg, ByteBuf destBuf) throws IllegalAccessException, UnsupportedEncodingException {
		destBuf.writeIntLE(msg.size());
		for(Request item : msg){
			encodeRequest(item, destBuf);
		}
	}
	
    public static void encodeRequest(Request msg, ByteBuf destBuf) throws IllegalAccessException, UnsupportedEncodingException {
        Field[] fields = msg.getClass().getDeclaredFields();
        Arrays.sort(fields, new Comparator<Field>() {
            @Override
            public int compare(Field o1, Field o2) {
                MyField order1 = o1.getAnnotation(MyField.class);
                MyField order2 = o2.getAnnotation(MyField.class);
                return Integer.compare(order1.order(), order2.order());
            }
        });
        for (Field field : fields) {
            field.setAccessible(true);
            Object val = field.get(msg);
            logger.debug("{}--->{}",field.getName(),val);
            MyField myField = field.getAnnotation(MyField.class);
            if(field.getType()==int.class || field.getType()==Integer.class){
                destBuf.writeIntLE((int) val);
            }else if(field.getType()==long.class || field.getType()==Long.class){
                switch (myField.sourceType()){
                	case INT8:
                		destBuf.writeByte((int)val);
                		break;
                    case INT32:
                        destBuf.writeIntLE((int)val);
                        break;
                    case UINT32:
                        destBuf.writeIntLE(((Long)val).intValue());
                        break;
                    case UINT64:
                        destBuf.writeLongLE((long)val);
                        break;
                }
            } else if (field.getType()==String.class){
                String tmp = (String) val;
                byte[] b = tmp.getBytes("utf-8");
                destBuf.writeIntLE(b.length);
                destBuf.writeBytes(b);
            }else if(field.getType()==double.class || field.getType()==Double.class){
            	 destBuf.writeLongLE(Double.doubleToRawLongBits(Double.valueOf(String.valueOf(val))));
            }
        }
    }

    public static Object decode(ByteBuf msg, CommandEnum commandEnum) throws InstantiationException, IllegalAccessException, UnsupportedEncodingException {
        Class cls = commandEnum.getCls();
        Object res = cls.newInstance();

        Field[] fields = cls.getDeclaredFields();
        Arrays.sort(fields,(f1, f2)->{
            MyField o1 = f1.getAnnotation(MyField.class);
            MyField o2 = f2.getAnnotation(MyField.class);
            return Integer.compare(o1.order(), o2.order());
        });
        for (Field field : fields) {
            field.setAccessible(true);
            MyField myField = field.getAnnotation(MyField.class);
            if(field.getType()==String.class){
                int len = msg.readIntLE();
                byte[] bstr = new byte[len];
                msg.readBytes(bstr);
                field.set(res,new String(bstr,"utf-8"));
            }else if(field.getType()==Integer.class || field.getType()==int.class){
                field.setInt(res,msg.readIntLE());
            }else if(field.getType()==Long.class || field.getType()==long.class){
                if(myField.sourceType()==STypeEnum.INT32){
                    field.setLong(res,(long)msg.readIntLE());
                }else if(myField.sourceType()==STypeEnum.UINT32){
                    field.setLong(res,msg.readUnsignedIntLE());
                }else if(myField.sourceType()==STypeEnum.UINT64){
                    field.setLong(res,msg.readLongLE());
                }
            }else if(field.getType()==double.class || field.getType()==Double.class){
                field.setDouble(res,msg.readDouble());
            }else if(field.getType()==float.class || field.getType()==Float.class){
                field.setFloat(res,msg.readFloat());
            }
        }
        return res;
    }
    
    public static void main(String[] args) {
    	System.out.println(Double.doubleToRawLongBits(20.0));
	}
}

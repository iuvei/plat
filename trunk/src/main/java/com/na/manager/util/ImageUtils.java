package com.na.manager.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AccessDeniedException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;

import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.TreeMultimap;

/**
 * 图片工具类。要杜绝文件威胁必须做如下操作方可。<br>
 *     <ul>
 *         <li>进行文件安全验证</li>
 *         <li>对图片文件重新压缩输出</li>
 *     </ul>
 *
 * Created by sunny on 2017/6/10 0010.
 */
public class ImageUtils {
    private static Multimap<String, Integer> SUFFIX_MAP = ArrayListMultimap.create();
    private static TreeMultimap<Integer, String> SUFFIX_INVERSE = Multimaps.invertFrom(SUFFIX_MAP, TreeMultimap.create());
    static {
    	SUFFIX_MAP.put("jpg",0xff_d8_ff_e0);
    	SUFFIX_MAP.put("jpg",0xff_d8_ff_e1);
    	SUFFIX_MAP.put("jpeg",0xff_d8_ff_e0);
    	SUFFIX_MAP.put("jpeg",0xff_d8_ff_e1);
    	SUFFIX_MAP.put("png",0x89_50_4e_47);
    	
    	SUFFIX_INVERSE = Multimaps.invertFrom(SUFFIX_MAP, TreeMultimap.create());
    }

    /**
     * 对图片文件进行安全验证。
     * <ul>
     *     <li>后缀名</li>
     *     <li>魔术判断</li>
     *     <li>内容验证</li>
     * </ul>
     * @param file
     * @return
     */
    public static boolean securityCheck(File file){
    	
        if(file==null) return false;

        String[] names = file.getName().split("\\.");
        if(names.length<2){
            return false;
        }
        String suffix = names[names.length-1].toLowerCase();
        if(!SUFFIX_MAP.containsKey(suffix)){
            return false;
        }

        if(!file.exists()){
            return false;
        }

        try (InputStream in = new FileInputStream(file);){
            byte[] buf = new byte[4];
            in.read(buf);
            int magic = byteArrayToInt(buf);
            if(!SUFFIX_MAP.get(suffix).contains(magic)){
                return false;
            }
        }catch (Exception e){
            return false;
        }

        Image img = null;
        try {
            img = ImageIO.read(file);
            if(img==null || img.getWidth(null)<=0 || img.getHeight(null)<=0){
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 对图片文件进行安全验证。
     * <ul>
     *     <li>魔术判断</li>
     *     <li>内容验证</li>
     * </ul>
     * @param in
     * @return
     */
    public static String securityCheck(MultipartFile in)throws IOException{
        String suffix = getFileType(in);
        Image img = ImageIO.read(in.getInputStream());
        if(img==null || img.getWidth(null)<=0 || img.getHeight(null)<=0){
            throw new AccessDeniedException("文件类型无法识别");
        }
        return suffix;
    }

    public static String getFileType(MultipartFile in) throws IOException{
        if(in.getBytes().length<=4){
            throw new AccessDeniedException("文件类型无法识别");
        }

        byte[] buf = new byte[4];
        System.arraycopy(in.getBytes(),0,buf,0,4);

        int magic = byteArrayToInt(buf);
        if(!SUFFIX_INVERSE.get(magic).isEmpty()){
        	return SUFFIX_INVERSE.get(magic).last();
        }
        throw new AccessDeniedException("只接受jpg、png格式文件");
    }

    /**
     * 以JPEG编码保存图片
     *
     * @param file
     *            要处理的图像图片
     * @param JPEGcompression
     *            压缩比
     * @param fos
     *            文件输出流
     * @throws IOException
     */
    public static void saveAsJPEG(Object file,float JPEGcompression, FileOutputStream fos, String suffix) throws IOException {
        BufferedImage image_to_save ;
        if(file instanceof FileInputStream){
            image_to_save = ImageIO.read((FileInputStream)file);
        }else {
            image_to_save = ImageIO.read((File)file);
        }

        ImageWriter imageWriter =  ImageIO.getImageWritersBySuffix(suffix).next();

        try(ImageOutputStream ios = ImageIO.createImageOutputStream(fos);){
            imageWriter.setOutput(ios);
            IIOMetadata imageMetaData = imageWriter.getDefaultImageMetadata(new ImageTypeSpecifier(image_to_save), null);

//            if (JPEGcompression >= 0 && JPEGcompression <= 1f) {
//                ImageWriteParam jpegParams = imageWriter.getDefaultWriteParam();
//                jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
//                jpegParams.setCompressionQuality(JPEGcompression);
//            }
            imageWriter.write(imageMetaData,new IIOImage(image_to_save, null, null), null);
            imageWriter.dispose();
        }
    }

    private static int byteArrayToInt(byte[] b) {
        return   b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    public static void main(String[] args) throws Exception{
        File f = new File("d:\\test3.png");
        if(!securityCheck(f)){
            System.out.println("文件有威胁。。。");
            return;
        }
        FileOutputStream fos = new FileOutputStream("d:\\bak\\"+f.getName());
        saveAsJPEG(f,0.1f,fos,"jpg");
        System.out.println(ImageUtils.securityCheck(f));
        
    	Multimap<String, Integer> SUFFIX_SET = ArrayListMultimap.create();
    	SUFFIX_SET.put("jpg",0xff_d8_ff_e0);
    	SUFFIX_SET.put("jpg",0xff_d8_ff_e1);
    	SUFFIX_SET.put("jpeg",0xff_d8_ff_e0);
    	SUFFIX_SET.put("jpeg",0xff_d8_ff_e1);
    	SUFFIX_SET.put("png",0x89_50_4e_47);
    	
    	TreeMultimap<Integer, String> inverse = Multimaps.invertFrom(SUFFIX_SET, TreeMultimap.create());
    	System.out.println(inverse.get(0x89_50_4e_47).first());
    	
    	System.out.println(SUFFIX_SET.get("jpg").contains(0xff_d8_ff_e1));
    	SUFFIX_SET.forEach((k,v)->{
    		System.out.println(k+"=>"+v);
    	});
    	System.out.println("----------");
    	System.out.println(SUFFIX_SET.containsKey("jpg"));
    	System.out.println(SUFFIX_SET.containsValue(0xff_d8_ff_e1));
    	Iterator<Entry<String, Collection<Integer>>> iter=SUFFIX_SET.asMap().entrySet().iterator();
    	while(iter.hasNext()){
    		Entry<String, Collection<Integer>> i=iter.next();
    		System.out.println(i.getKey()+"=>"+i.getValue());
    	}
    }
}

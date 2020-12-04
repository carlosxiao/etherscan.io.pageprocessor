package com.cc.etherscan.io;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import org.junit.Test;
import sun.awt.image.PNGImageDecoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class JsonTest {

    static ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void should_1() throws IOException {
        List<Hua> huaList = objectMapper.readValue(this.getClass().getResource("/all_final.json"), new TypeReference<List<Hua>>() {
        });
        System.out.println(huaList.size());
        AtomicReference<Integer> a = new AtomicReference<>(0);
        AtomicReference<Integer> b = new AtomicReference<>(0);
        AtomicReference<Integer> c = new AtomicReference<>(0);
        AtomicReference<Integer> d = new AtomicReference<>(0);
        huaList.forEach(item -> {
            List<Product> productList = item.getProductList();
            productList.forEach(p -> {
                if (p.getProductCode().equals("SPH00002891")) {
                    a.updateAndGet(v -> v + p.getQuantity());
                } else if (p.getProductCode().equals("SPH00001665")) {
                    b.updateAndGet(v -> v + p.getQuantity());
                } else if (p.getProductCode().equals("SPH00002642")) {
                    c.updateAndGet(v -> v + p.getQuantity());
                } else if (p.getProductCode().equals("SPH100000208")) {
                    d.updateAndGet(v -> v + p.getQuantity());
                }
            });
        });
        System.out.println("SPH00002891: " + a.get());
        System.out.println("SPH00001665: " + b.get());
        System.out.println("SPH00002642: " + c.get());
        System.out.println("SPH100000208: " + d.get());
    }

    @Test
    public void should_2() throws Exception {
        String PATH_DEST = "/Users/carlosxiao/Desktop/pic_merge/";
        // 源文件
        String pathSrc = PATH_DEST + "sources/1.jpg";
        // 盖章图片
        String sealFile = PATH_DEST + "seal.png";
        // 输出路径
        String outputPATH = PATH_DEST + "new/new.jpg";
        // 得到文件流
        FileInputStream fileInputStream = new FileInputStream(new File(pathSrc));
        // 得到输入的编码器，将文件流进行jpg格式编码
        JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(fileInputStream);
        // 得到编码后的图片对象
        BufferedImage image = decoder.decodeAsBufferedImage();
        Graphics g = image.getGraphics();
        InputStream imageSeal = new FileInputStream(new File(sealFile));
        // 得到输入的编码器，将文件流进行jpg格式编码
        // 得到编码后的图片对象
        BufferedImage image2 = ImageIO.read(new File(sealFile));
        // 加盖图片章
        ImageObserver imageObserver = null;
        int x = image.getWidth() - (image2.getWidth() + 400);
        int y = image.getHeight() - (image2.getHeight() + 600);
        g.drawImage(image2, x, y, imageObserver);
        g.dispose();
        ImageIO.write(image, "jpeg", new File(outputPATH));
        System.out.println("ok");
    }

    public static void saveFixedBoundIcon(File imageFile, int height, int width) throws Exception {
        double Ratio = 0.0;
        if (imageFile == null || !imageFile.isFile())
            throw new Exception(imageFile + "找不到指定的文件!");
        String filePath = imageFile.getPath();
        BufferedImage Bi = ImageIO.read(imageFile);
        if ((Bi.getHeight() > height) || (Bi.getWidth() > width)) {
            if (Bi.getHeight() > Bi.getWidth()) {
                Ratio = (new Integer(height)).doubleValue() / Bi.getHeight();
            } else {
                Ratio = (new Integer(width)).doubleValue() / Bi.getWidth();
            }
            File saveFile = new File(filePath + "_" + height + "_" + width + ".jpg");
            Image Itemp = Bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(Ratio, Ratio), null);
            Itemp = op.filter(Bi, null);
            try {
                ImageIO.write((BufferedImage) Itemp, "jpeg", saveFile);
            } catch (Exception ex) {

            }
        }
    }

    @Test
    public void should_3() {
        try {
            saveFixedBoundIcon(new File("C:\\test.jpg"), 200, 200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

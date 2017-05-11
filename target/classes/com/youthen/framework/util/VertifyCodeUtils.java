// ============================================================
// Copyright(c) youthen Incorporated All Right Reserved.
// File: $Id$
// ============================================================

package com.youthen.framework.util;

/**
 * 。
 * 
 * @author Administrator
 * @author Modifier By $Author: $
 * @version $Revision: $<br>
 *          $Date: $
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Random;
import javax.imageio.ImageIO;

public class VertifyCodeUtils {

    // 使用到Algerian字体，系统里没有的话需要安装字体，字体只显示大写，去掉了1,0,i,o几个容易混淆的字符
    public static final String VERIFY_CODES = "0123456789";
    private static Random random = new Random();

    /**
     * 使用系统默认字符源生成验证码
     * 
     * @param verifySize 验证码长度
     * @return
     */
    public static String generateVerifyCode(final int verifySize) {
        return generateVerifyCode(verifySize, VERIFY_CODES);
    }

    /**
     * 使用指定源生成验证码
     * 
     * @param verifySize 验证码长度
     * @param sources 验证码字符源
     * @return
     */
    public static String generateVerifyCode(final int verifySize, String sources) {
        if (sources == null || sources.length() == 0) {
            sources = VERIFY_CODES;
        }
        final int codesLen = sources.length();
        final Random rand = new Random(System.currentTimeMillis());
        final StringBuilder verifyCode = new StringBuilder(verifySize);
        for (int i = 0; i < verifySize; i++) {
            verifyCode.append(sources.charAt(rand.nextInt(codesLen - 1)));
        }
        return verifyCode.toString();
    }

    /**
     * 生成随机验证码文件,并返回验证码值
     * 
     * @param w
     * @param h
     * @param outputFile
     * @param verifySize
     * @return
     * @throws IOException
     */
    public static String outputVerifyImage(final int w, final int h, final File outputFile, final int verifySize)
            throws IOException {
        final String verifyCode = generateVerifyCode(verifySize);
        outputImage(w, h, outputFile, verifyCode);
        return verifyCode;
    }

    /**
     * 输出随机验证码图片流,并返回验证码值
     * 
     * @param w
     * @param h
     * @param os
     * @param verifySize
     * @return
     * @throws IOException
     */
    public static String outputVerifyImage(final int w, final int h, final OutputStream os, final int verifySize)
            throws IOException {
        final String verifyCode = generateVerifyCode(verifySize);
        outputImage(w, h, os, verifyCode);
        return verifyCode;
    }

    /**
     * 生成指定验证码图像文件
     * 
     * @param w
     * @param h
     * @param outputFile
     * @param code
     * @throws IOException
     */
    public static void outputImage(final int w, final int h, final File outputFile, final String code)
            throws IOException {
        if (outputFile == null) {
            return;
        }
        final File dir = outputFile.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            outputFile.createNewFile();
            final FileOutputStream fos = new FileOutputStream(outputFile);
            outputImage(w, h, fos, code);
            fos.close();
        } catch (final IOException e) {
            throw e;
        }
    }

    /**
     * 输出指定验证码图片流
     * 
     * @param w
     * @param h
     * @param os
     * @param code
     * @throws IOException
     */
    public static void outputImage(final int w, final int h, final OutputStream os, final String code)
            throws IOException {
        final int verifySize = code.length();
        final BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        final Random rand = new Random();
        final Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        final Color[] colors = new Color[5];
        final Color[] colorSpaces = new Color[] {Color.WHITE, Color.CYAN,
                Color.GRAY, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE,
                Color.PINK, Color.YELLOW};
        final float[] fractions = new float[colors.length];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = colorSpaces[rand.nextInt(colorSpaces.length)];
            fractions[i] = rand.nextFloat();
        }
        Arrays.sort(fractions);

        g2.setColor(Color.GRAY);// 设置边框色
        g2.fillRect(0, 0, w, h);

        final Color c = getRandColor(200, 250);
        g2.setColor(c);// 设置背景色
        g2.fillRect(0, 2, w, h - 4);

        // 绘制干扰线
        final Random random = new Random();
        g2.setColor(getRandColor(160, 200));// 设置线条的颜色
        for (int i = 0; i < 20; i++) {
            final int x = random.nextInt(w - 1);
            final int y = random.nextInt(h - 1);
            final int xl = random.nextInt(6) + 1;
            final int yl = random.nextInt(12) + 1;
            g2.drawLine(x, y, x + xl + 40, y + yl + 20);
        }

        // 添加噪点
        final float yawpRate = 0.05f;// 噪声率
        final int area = (int) (yawpRate * w * h);
        for (int i = 0; i < area; i++) {
            final int x = random.nextInt(w);
            final int y = random.nextInt(h);
            final int rgb = getRandomIntColor();
            image.setRGB(x, y, rgb);
        }

        shear(g2, w, h, c);// 使图片扭曲

        g2.setColor(getRandColor(100, 160));
        final int fontSize = h - 4;
        final Font font = new Font("Algerian", Font.ITALIC, fontSize);
        g2.setFont(font);
        final char[] chars = code.toCharArray();
        for (int i = 0; i < verifySize; i++) {
            final AffineTransform affine = new AffineTransform();
            affine.setToRotation(Math.PI / 4 * rand.nextDouble() * (rand.nextBoolean() ? 1 : -1), (w / verifySize) * i
                    + fontSize / 2, h / 2);
            g2.setTransform(affine);
            g2.drawChars(chars, i, 1, ((w - 10) / verifySize) * i + 5, h / 2 + fontSize / 2 - 10);
        }

        g2.dispose();
        ImageIO.write(image, "jpg", os);
    }

    private static Color getRandColor(int fc, int bc) {
        if (fc > 255) fc = 255;
        if (bc > 255) bc = 255;
        final int r = fc + random.nextInt(bc - fc);
        final int g = fc + random.nextInt(bc - fc);
        final int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    private static int getRandomIntColor() {
        final int[] rgb = getRandomRgb();
        int color = 0;
        for (final int c : rgb) {
            color = color << 8;
            color = color | c;
        }
        return color;
    }

    private static int[] getRandomRgb() {
        final int[] rgb = new int[3];
        for (int i = 0; i < 3; i++) {
            rgb[i] = random.nextInt(255);
        }
        return rgb;
    }

    private static void shear(final Graphics g, final int w1, final int h1, final Color color) {
        shearX(g, w1, h1, color);
        shearY(g, w1, h1, color);
    }

    private static void shearX(final Graphics g, final int w1, final int h1, final Color color) {

        final int period = random.nextInt(2);

        final boolean borderGap = true;
        final int frames = 1;
        final int phase = random.nextInt(2);

        for (int i = 0; i < h1; i++) {
            final double d = (period >> 1)
                    * Math.sin((double) i / (double) period
                            + (6.2831853071795862D * phase)
                            / frames);
            g.copyArea(0, i, w1, 1, (int) d, 0);
            if (borderGap) {
                g.setColor(color);
                g.drawLine((int) d, i, 0, i);
                g.drawLine((int) d + w1, i, w1, i);
            }
        }

    }

    private static void shearY(final Graphics g, final int w1, final int h1, final Color color) {

        final int period = random.nextInt(40) + 10; // 50;

        final boolean borderGap = true;
        final int frames = 20;
        final int phase = 7;
        for (int i = 0; i < w1; i++) {
            final double d = (period >> 1)
                    * Math.sin((double) i / (double) period
                            + (6.2831853071795862D * phase)
                            / frames);
            g.copyArea(i, 0, 1, h1, 0, (int) d);
            if (borderGap) {
                g.setColor(color);
                g.drawLine(i, (int) d, i, 0);
                g.drawLine(i, (int) d + h1, i, h1);
            }

        }

    }

    public static void main(final String[] args) throws IOException {
        final File dir = new File("E:/verifies");
        final int w = 200, h = 80;
        for (int i = 0; i < 50; i++) {
            final String verifyCode = generateVerifyCode(4);
            final File file = new File(dir, verifyCode + ".jpg");
            outputImage(w, h, file, verifyCode);
        }
    }
}

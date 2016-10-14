package com.cqutprint.shundai.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Administrator on 2016/10/8.
 */
public class SetXmlValues {
    static float hdpiScale = 1.5f;//240 dpi
    static float xhdpiScale = 2f;//320 dpi
    static float xxhdpiScale = 3f;//480dpi

    static float xx515 = 515f/160;

    static float xx_xhdpiScale = 3.5f;//560dpi
    static float xxxhdpiScale = 4f;//640dpi
    static int start = 0;
    static int end = 375;
    static final int defaultWidth=375;
    public static void main(String[] args) {

        writeFileByWidth(480,hdpiScale,"res/values-hdpi/dimens.xml");
        writeFileByWidth(720D,xhdpiScale,"res/values-xhdpi/dimens.xml");
        writeFileByWidth(1080D,xxhdpiScale,"res/values-xxhdpi/dimens.xml");
        writeFileByWidth(1440D,xx_xhdpiScale,"res/values-xxxhdpi-2560x1440/dimens.xml");

        writeFileByWidth(1440D,xx515,"res/values-xxhdpi-2560x1440/dimens.xml");

        writeFileByWidth(1440D,xxxhdpiScale,"res/values-xxxhdpi/dimens.xml");
        //平板
        writeFileByWidth(1600D,xhdpiScale,"res/values-sw600dp/dimens.xml");
    }

    public static void writeFileByWidth(double width,double xDPI,String fileName){
        double k=width/xDPI/defaultWidth;
        String content = getPxToDpDimens(start,end,k);
        System.out.println(fileName);
        System.out.println(content);
        writeFile(fileName,content);
    }

    public static String getPxToDpDimens(int start,int end,double k){
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        sb.append("\r\n").append("<resources>").append("\r\n");
        for (int i = start; i <= end; i++) {
            for (int j = 0; j < 2; j++) {
                float temp = i;
                if(j==1){
                    temp = (float) (i+0.5);
                }
                String st = "<dimen name=\"px_key_dp\">value_dp</dimen>";
                st = st.replaceAll("key", temp+"");
                st = st.replaceAll("value_",String.format("%.2f", k*temp));
                sb.append("\t").append(st).append("\r\n");
            }
        }
        sb.append("</resources>").append("\r\n");
        return sb.toString();
    }

    public static void writeFile(String filePaht, String text) {
        filePaht="app/src/main/"+filePaht;
        File file = new File(filePaht);
        File parent = file.getParentFile();
        if(!parent.exists()){
            parent.mkdirs();
        }
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            out.println(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.close();
    }
}

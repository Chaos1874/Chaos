package com.example.tianchao.school_sample.Tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * 流转换成字符串的工具类
 */

public class StreamString {
    public static String Stream2String(InputStream inputStream) throws Exception {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = inputStream.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    public static Bitmap imgStringtobitmap(String str) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(str,Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    public static byte[] InputStreamToByte(InputStream is) throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int ch;
        while ((ch = is.read()) != -1) {
            bytestream.write(ch);
        }
        byte imgdata[] = bytestream.toByteArray();
        bytestream.close();
        return imgdata;
    }
}

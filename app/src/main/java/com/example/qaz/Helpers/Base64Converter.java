package com.example.qaz.Helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class Base64Converter {
    public static String BitmapToBase64(Bitmap bit){
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 40, bos);//参数100表示不压缩
        byte[] bytes=bos.toByteArray();
        String content = "data:image/jpg;base64," + Base64.encodeToString(bytes, Base64.NO_WRAP);
        Log.d("Base64Util", "content:" + content);
        return content;
    }

    public static Bitmap Base64ToBitMap(String content) {
        String base64 = content.substring(content.indexOf(",") + 1);
        byte[] decode = Base64.decode(base64, Base64.NO_WRAP);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        return bitmap;
    }
}

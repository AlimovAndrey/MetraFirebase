package alimov.com.metrafirebase.util;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import alimov.com.metrafirebase.db.entity.Photo;

/**
 * Created by Andrey on 17.12.2016.
 */

public class Util {

    public static byte[] toByteArray(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public static List<Photo> sortByDate(List<Photo> list) {
        Collections.sort(list, new Comparator<Photo>() {
            @Override
            public int compare(Photo photo1, Photo photo2) {
                return Long.valueOf(photo2.getmCreatedAt()).compareTo(photo1.getmCreatedAt());
            }
        });
        return list;
    }
}

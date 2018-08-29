package com.example.welington.locedu.Controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by xinel on 01/12/2017.
 */

public class StorageHelper {
    private String path;
    private Context context;
    File file;

    public StorageHelper(Context context, String path) {
        this.path = path;
        this.context = context;
    }

    public FileInputStream open() {
        file = new File(context.getFilesDir(), path);
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(file);
            return stream;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean save(byte[] bytes) {
        try {
            FileOutputStream stream = context.openFileOutput(path, Context.MODE_PRIVATE);
            stream.write(bytes);
            stream.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(){
        file = new File(context.getFilesDir(), path);
        return file.delete();
    }

    public Bitmap openImage() {
        FileInputStream stream = this.open();
        Bitmap bitmap = BitmapFactory.decodeStream(stream);
        return bitmap;
    }
}

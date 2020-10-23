package fr.chardonnet_rakotoanosy.soundroulette.storage;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

public abstract class FileStorage<T> implements Storage<T> {

    private static final String PREFIX = "storage_";
    private Context context;
    private String fileName;

    public FileStorage(Context context, String name, String extension) {
        this.context = context;
        this.fileName = PREFIX + name + "." + extension;
    }

    protected abstract void initialize(String s);
    protected abstract void create();
    protected abstract String getValue();

    protected void read() {
        try {
            FileInputStream in = context.openFileInput((fileName));
            if (in != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                String temp;
                StringBuilder sb = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null) {
                    sb.append(temp);
                }
                in.close();
                initialize((sb.toString()));
            }
        } catch (FileNotFoundException e) {
            create();
            write();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void write() {
        try {
            FileOutputStream out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(out);
            writer.write(getValue());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

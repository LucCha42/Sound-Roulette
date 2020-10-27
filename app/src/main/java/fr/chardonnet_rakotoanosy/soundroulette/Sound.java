package fr.chardonnet_rakotoanosy.soundroulette;

import android.net.Uri;

import java.io.Serializable;

public class Sound implements Serializable {

    private int id;
    private String name;
    private Uri uri;

    public Sound(int id, String name, Uri uri) {
        this.id = id;
        this.name = name;
        this.uri = uri;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getUri() {
        return uri;
    }
}

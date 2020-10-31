package fr.chardonnet.soundroulette;

import android.net.Uri;

import java.io.Serializable;

public class Sound implements Serializable {

    private final int id;
    private final Uri uri;
    private String name;

    public Sound(int id, Uri uri, String name) {
        this.id = id;
        this.uri = uri;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public Uri getUri() {
        return uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

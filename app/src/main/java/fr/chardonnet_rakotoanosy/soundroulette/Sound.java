package fr.chardonnet_rakotoanosy.soundroulette;

import android.net.Uri;

import java.io.Serializable;

public class Sound implements Serializable {

    private String name;
    private Uri uri;

    public Sound(String name, Uri uri) {
        this.name = name;
        this.uri = uri;
    }

    public Sound(Uri uri) {
        this.uri = uri;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Uri getUri() {
        return uri;
    }
}

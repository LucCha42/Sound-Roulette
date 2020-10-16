package fr.chardonnet_rakotoanosy.soundroulette;

import android.net.Uri;

public class Sound {

    private String name;
    private Uri uri;

    public Sound(String name, Uri uri) {
        this.name = name;
        this.uri = uri;
    }

    public Sound(String name) {
        this.name = name;
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

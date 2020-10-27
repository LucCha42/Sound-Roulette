package fr.chardonnet_rakotoanosy.soundroulette;

import android.net.Uri;

import java.io.Serializable;

public class Sound implements Serializable {

    private final int ID;
    private final Uri URI;
    private String name;

    public Sound(int ID, Uri URI, String name) {
        this.ID = ID;
        this.URI = URI;
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public Uri getURI() {
        return URI;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

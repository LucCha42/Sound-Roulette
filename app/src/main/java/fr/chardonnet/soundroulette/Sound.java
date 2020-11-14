package fr.chardonnet.soundroulette;

import android.net.Uri;

import java.io.Serializable;

public class Sound implements Serializable {

    private final int id;
    private final Uri uri;
    private String name;
    private boolean playing;

    public Sound(int id, Uri uri, String name, boolean playing) {
        this.id = id;
        this.uri = uri;
        this.name = name;
        this.playing = playing;
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

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }
}

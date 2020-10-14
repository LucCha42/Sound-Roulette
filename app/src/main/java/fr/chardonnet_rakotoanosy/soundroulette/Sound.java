package fr.chardonnet_rakotoanosy.soundroulette;

import java.io.File;

public class Sound {

    private String name;
    private File file;

    public Sound(String name, File file) {
        this.name = name;
        this.file = file;
    }

    public Sound(File file) {
        this.file = file;
    }

    public String getName() {
        return this.name;
    }
    public File getFile() {
        return this.file;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setFile(File file) {
        this.file = file;
    }
}

package fr.chardonnet_rakotoanosy.soundroulette;

import java.io.File;

public class Sound {
    private int id;
    private String name;
    private File file;

    //Getter
    public int getId(){
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public File getFile() {
        return this.file;
    }

    //Setter
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setFile(File file) {
        this.file = file;
    }
}

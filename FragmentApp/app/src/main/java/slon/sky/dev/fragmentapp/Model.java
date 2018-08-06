package slon.sky.dev.fragmentapp;

/**
 * Created by Slon on 25.04.2017.
 */

public class Model {
    private int id;
    private String content;

    public Model(int id, String content) {
        this.id = id;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}

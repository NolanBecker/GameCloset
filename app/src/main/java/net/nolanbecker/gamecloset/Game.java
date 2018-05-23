package net.nolanbecker.gamecloset;

public class Game {

    private int id;
    private String name, imgUrl;

    public Game(int id, String name, String imgUrl) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getThumb() {
        return imgUrl;
    }

    public void setThumb(String imgUrl) {
        this.imgUrl = imgUrl;
    }

}

package net.nolanbecker.gamecloset;

import java.io.Serializable;

public class Game implements Serializable{

    private int id, year, rank, age;
    private String name, imgUrl, desc, image, players, playtime, difficulty;

    public Game(int id, String name, String imgUrl, String desc, int year, String image, int rank, String players, String playtime, int age, String difficulty) {
        this.id = id;
        this.name = name;
        if (imgUrl == null)
            this.imgUrl = "";
        else
            this.imgUrl = imgUrl;
        this.desc = desc;
        this.year = year;
        this.image = image;
        this.rank = rank;
        this.players = players;
        this.playtime = playtime;
        this.age = age;
        this.difficulty = difficulty;
    }

    public int getId() {
        return id;
    }

    public int getRank() {
        return rank;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public String getThumb() {
        return imgUrl;
    }

    public String getDesc() {
        return desc;
    }

    public int getYear() {
        return year;
    }

    public String getImage() {
        return image;
    }

    public String getPlayers() {
        return players;
    }

    public String getPlaytime() {
        return playtime;
    }

    public String getDifficulty() {
        return difficulty;
    }


    public void setThumb(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPlayers(String players) {
        this.players = players;
    }

    public void setPlaytime(String playtime) {
        this.playtime = playtime;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}

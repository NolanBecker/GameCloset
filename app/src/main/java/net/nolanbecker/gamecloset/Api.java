package net.nolanbecker.gamecloset;

public class Api {

    private static final String ROOT_URL = "https://nolanbecker.net/GameApi/v1/Api.php?apicall=";

    public static final String URL_ADD_GAME = ROOT_URL + "addgame";
    public static final String URL_READ_GAMES = ROOT_URL + "getgames";
    public static final String URL_UPDATE_GAME = ROOT_URL + "updatagame";
    public static final String URL_DELETE_GAME = ROOT_URL + "deletegame";

}
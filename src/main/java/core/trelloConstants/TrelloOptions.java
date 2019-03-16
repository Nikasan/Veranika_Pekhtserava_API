package core.trelloConstants;

public class TrelloOptions {

    public static final String KEY = "3445103a21ddca2619eaceb0e833d0db";
    public static final String TOKEN = "a9b951262e529821308e7ecbc3e4b7cfb14a24fef5ea500a68c69d374009fcc0";
    public static final String TRELLO_API_BASE_URL = "https://trello.com/1";
    public static final String TRELLO_API_BOARD = "members/me/boards";


    public static final String BOARD_ID = "5a27e3b62fef5d3a74dca48a";
    public static final String CARD_UNIQUE_ID = "5a27e722e2f04f3ab6924931";

    public class Endpoints {
        public static final String GET_BOARD = "boards/%1$s";
        public static final String CREATE_BOARD = "boards";
        public static final String DELETE_BOARD = "boards/%1$s";
        public static final String UPDATE_BOARD = "boards/%1$s";
        public static final String GET_LABELS = "boards/%1$s/labels";
        public static final String POST_LABEL = "labels";
        public static final String UPDATE_LABEL = "labels/%1$s/color";
        public static final String UPDATE_LABEL_NAME = "labels/%1$s/name";
        public static final String UPDATE_LABEL_BY_ID = "labels/%1$s";
    }
}

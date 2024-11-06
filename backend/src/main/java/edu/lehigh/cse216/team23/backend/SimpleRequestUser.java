package edu.lehigh.cse216.team23.backend;

public class SimpleRequestUser {
    String userId;
    String email;
    String name;
    String pictureUrl;
    String locale;
    String familyName;
    String givenName;

    public SimpleRequestUser(String userId, String email, String name, String pictureUrl, String locale, String familyName, String givenName) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.locale = locale;
        this.familyName = familyName;
        this.givenName = givenName;
    }
}

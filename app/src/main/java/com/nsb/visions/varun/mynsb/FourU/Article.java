package com.nsb.visions.varun.mynsb.FourU;


// Article model class, this will of course be expanded later
class Article {
    String name;
    String LongDesc;
    String ImageURL;
    String issuuLink;

    public Article(String name, String longDesc, String imageURL, String issuuLink) {
        this.name = name;
        this.LongDesc = longDesc;
        this.ImageURL = imageURL;
        this.issuuLink = issuuLink;
    }
}

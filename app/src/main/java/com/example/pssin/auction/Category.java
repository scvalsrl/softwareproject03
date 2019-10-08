package com.example.pssin.auction;

public class Category {

    String boardNumber;
    String id;
    String title;
    String category;
    String tradeLocation;
    String auctionStartTime;
    String auctionTimeLimit;

    public String getboardNumber() {
        return boardNumber;
    }

    public void setboardNumber(String boardNumber) {
        this.boardNumber = boardNumber;
    }

    public String getid() {
        return id;
    }

    public void setidd(String id) {
        this.id = id;
    }

    public String gettitle() {
        return title;
    }

    public void settitle(String title) {
        this.title = title;
    }

    public String getcategory() {
        return category;
    }

    public void setcategory(String category) {
        this.category = category;
    }

    public String gettradeLocation() {
        return tradeLocation;
    }

    public void settradeLocation(String tradeLocation) {
        this.tradeLocation = tradeLocation;
    }

    public String getauctionStartTime() {
        return auctionStartTime;
    }

    public void setauctionStartTime(String auctionStartTime) {
        this.auctionStartTime = auctionStartTime;
    }

    public String getauctionTimeLimit() {
        return auctionTimeLimit;
    }

    public void setauctionTimeLimit(String auctionTimeLimit) {
        this.auctionTimeLimit = auctionTimeLimit;
    }

    public Category(String boardNumber, String id, String title, String category, String tradeLocation, String auctionStartTime, String auctionTimeLimit) {
        this.boardNumber = boardNumber;
        this.id = id;
        this.title = title;
        this.category = category;
        this.tradeLocation = tradeLocation;
        this.auctionStartTime = auctionStartTime;
        this.auctionTimeLimit = auctionTimeLimit;
    }

}

package com.example.sayedsalah.book_listing_app;

/**
 * Created by Sayed Salah on 10/26/2017.
 */
public class Book {
    private String BookTitle;
    private String[] BookAuthors;
    private String BookDescription;

    public Book(String bookTitle, String[] bookAuthors, String bookDescription) {
        BookTitle = bookTitle;
        BookAuthors = bookAuthors;
        BookDescription = bookDescription;
    }

    public String getBookTitle() {
        return BookTitle;
    }


    public String getauthors() {
        String s = "";
        for (int i = 0; i < BookAuthors.length; i++) {
            if (i == BookAuthors.length - 1)
                s += BookAuthors[i];
            else
                s += BookAuthors[i] + ", ";
        }
        return s;
    }


    public String getBookDescription() {
        return BookDescription;
    }


}

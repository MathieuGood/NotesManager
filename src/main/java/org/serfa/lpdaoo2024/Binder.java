package org.serfa.lpdaoo2024;

import java.sql.*;

public class Binder {

    private final int binderID;
    private final int userID;
    private String binderName;
    private int binderColorID;


    public Binder(
            Notebook notebook,
            int binderID,
            String binderName,
            int binderColorID
    ) {
        this.binderID = binderID;
        this.userID = notebook.getUserID();
        this.binderName = binderName;
        this.binderColorID = binderColorID;
    }


    public int getBinderID() {
        return this.binderID;
    }


    public int getUserID() {
        return this.userID;
    }


    public String getBinderName() {
        return this.binderName;
    }


    public int getBinderColorID() {
        return this.binderColorID;
    }


    public int editName(String newName) {
        this.binderName = newName;
        return DatabaseManager.update(
                "binders",
                "binder_name",
                newName,
                "binder_id",
                String.valueOf(this.binderID)
        );
    }


    public int editColor(int newColorID) {
        this.binderColorID = newColorID;
        return DatabaseManager.update(
                "binders",
                "binder_color_id",
                String.valueOf(newColorID),
                "binder_id",
                String.valueOf(this.binderID)
        );
    }

}

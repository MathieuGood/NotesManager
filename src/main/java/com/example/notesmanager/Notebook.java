package com.example.notesmanager;

import java.sql.*;
import java.util.ArrayList;



public class Notebook {


    // An ArrayList that holds all the Binder objects associated with this Notebook.
    private ArrayList<Binder> binders = new ArrayList<>();

    // The unique identifier for the User that owns this Notebook.
    private final int userID;


    
    public Notebook(User user) {
        this.userID = user.getUserID();
        setNotebookContent();
    }


    
    public int getUserID() {
        return userID;
    }


    
    public ArrayList<Binder> getBinders() {
        return binders;
    }


    public Note getNoteByID(int noteID) {
        for (Binder binder : binders) {
            for (Tab tab : binder.getTabs()) {
                for (Note note : tab.getNotes()) {
                    if (note.getNoteID() == noteID) {
                        return note;
                    }
                }
            }
        }
        return null;
    }


    
    public void addBinderToList(Binder binder) {
        binders.add(binder);
    }


    
    public ResultSet fetchAllNotebookContent() {

        System.out.println("\n***");
        System.out.println("getContentTree() for userID " + userID);

        String[] fields = {
                "binders.binder_id",
                "binders.binder_name",
                "binders.binder_color_id",
                "tabs.tab_id",
                "tabs.tab_name",
                "tabs.tab_color_id",
                "notes.note_id",
                "notes.note_name",
                "label1.label_name",
                "label2.label_name"
        };
        String[] conditionFields = {"users.user_id"};
        String[] conditionValues = {String.valueOf(userID)};

        return DatabaseManager.select(
                "binders "
                        + "LEFT JOIN users ON binders.user_id = users.user_id "
                        + "LEFT JOIN tabs ON tabs.binder_id = binders.binder_id "
                        + "LEFT JOIN notes ON notes.tab_id = tabs.tab_id "
                        + "LEFT JOIN labels AS label1 ON notes.note_label1_id = label1.label_id "
                        + "LEFT JOIN labels AS label2 ON notes.note_label2_id = label2.label_id ",
                fields,
                conditionFields,
                conditionValues
        );
    }


    
    public void setNotebookContent() {
        setNotebookContent(null);
    }


    
    public void setNotebookContent(String labelNameFilter) {
        // Clear the binders list
        binders = new ArrayList<>();

        // Fetch all content related to the Notebook from the database
        ResultSet notebookContent = fetchAllNotebookContent();

        // Initialize current binder and tab IDs
        int currentBinderID = 0;
        int currentTabID = 0;

        try {
            // Iterate over each row in the ResultSet
            while (notebookContent.next()) {
                // Retrieve data from the current row of the ResultSet
                int binderID = notebookContent.getInt(1);
                String binderName = notebookContent.getString(2);
                int binderColorID = notebookContent.getInt(3);
                int tabID = notebookContent.getInt(4);
                String tabName = notebookContent.getString(5);
                int tabColorID = notebookContent.getInt(6);
                int noteID = notebookContent.getInt(7);
                String noteName = notebookContent.getString(8);
                String noteLabel1 = notebookContent.getString(9);
                String noteLabel2 = notebookContent.getString(10);

                // Print the retrieved data
                //  System.out.println("\t> " + binderID + " / " + binderName + " / " + binderColorID + " / " + tabID + " / " + tabName + " / " + tabColorID + " / " + noteID + " / " + noteName + " / " + noteColorID);

                // If a labelNameFilter is provided, do not skip the current row if the note's labels do not match the filter
                if (labelNameFilter != null) {
                    // If either label1 or label2 does not match the filter, skip the current row
                    if (!labelNameFilter.equals(noteLabel1) && !labelNameFilter.equals(noteLabel2)) {
                        continue;
                    }
                }

                System.out.println("Adding to notebook : " + noteName + " / " + noteLabel1 + " / " + noteLabel2);

                // If the binder ID corresponds to a new binder, create a new Binder object and add it to the binders list
                if (binderID != currentBinderID) {
                    System.out.println("Binder : " + binderName + " / ID #" + binderID);
                    addBinderToList(new Binder(this, binderID, binderName, binderColorID));
                    currentBinderID = binderID;
                }

                // If the tab ID corresponds to a new tab and is not null, create a new Tab object and add it to the tabs list of the last binder
                if (tabID != currentTabID && tabID != 0) {
                    System.out.println("  > Tab : " + tabName);
                    binders.getLast().addTabToList(new Tab(binders.getLast(), tabID, tabName, tabColorID));
                    currentTabID = tabID;
                }

                // If the note ID is not null, create a new Note object and add it to the notes list of the last tab of the last binder
                if (noteID != 0) {
                    System.out.println("Checking if " + noteLabel1 + " or " + noteLabel2 + " is null");
                    // Create a list of LabelNote objects to store the note's labels
                    ArrayList<NoteLabel> labels = new ArrayList<>();
                    if (noteLabel1 != null) {
                        labels.add(new NoteLabel(noteLabel1));
                        System.out.println("Adding label " + noteLabel1 + " to note " + noteName);
                    }
                    if (noteLabel2 != null) {
                        labels.add(new NoteLabel(noteLabel2));
                        System.out.println("Adding label " + noteLabel2 + " to note " + noteName);
                    }

                    System.out.println("\t>> Note : " + noteName);
                    binders.getLast().getTabs().getLast().addNoteToList(new Note(
                            binders.getLast().getTabs().getLast(),
                            noteID,
                            noteName,
                            labels
                    ));
                }
            }
        } catch (Exception e) {
            // Print any exceptions that occur
            System.out.println("Error : " + e);
        }
    }


    
    private ArrayList<Binder> fetchAllBinders() {
        System.out.println("\n***");
        System.out.println("getAllBinders() for userID " + this.userID + " :");

        ResultSet resultSet = DatabaseManager.select(
                "binders",
                new String[]{
                        "binders.binder_id",
                        "binders.binder_name",
                        "binders.binder_color_id"
                },
                new String[]{"user_id"},
                new String[]{String.valueOf(userID)}
        );

        // Create ArrayList to store all Binder objects
        ArrayList<Binder> binders = new ArrayList<>();

        // Parse query results to new Binder object and store it into ArrayList
        try {
            while (resultSet.next()) {
                // Retrieve data from resultSet
                int binderID = resultSet.getInt(1);
                String binderName = resultSet.getString(2);
                int binderColorID = resultSet.getInt(3);

                // Print out data from resultSet
                System.out.println("\t> " + binderID + " / " + binderName + " / " + binderColorID);

                // Create new Binder object with parsed data and add it to ArrayList
                binders.add(new Binder(this, binderID, binderName, binderColorID));
            }
        } catch (Exception e) {
            System.out.println("Error : " + e);
            return null;
        }
        return binders;
    }


    
    public Binder createBinder(String binderName, int binderColorID) {
        System.out.println("\n***");
        System.out.println("createBinder() : " + binderName + " / userID " + userID + " / colorID " + binderColorID);

        String[] fields = {"binder_name", "user_id", "binder_color_id"};
        String[] values = {binderName, String.valueOf(userID), String.valueOf(binderColorID)};

        int binderID = DatabaseManager.insert("binders", fields, values);

        Binder binder = new Binder(this, binderID, binderName, binderColorID);
        binders.add(binder);

        return binder;
    }


    
    public int deleteBinder(int binderID) {
        System.out.println("\n***");
        System.out.println("deleteBinder() : " + " binderID " + binderID);

        int result = DatabaseManager.delete("binders", "binder_id", String.valueOf(binderID));

        // If query is successful, remove the Binder object from the ArrayList
        if (result > 0) {

            for (Binder binder : binders) {
                if (binder.getBinderID() == binderID) {
                    binders.remove(binder);
                    break;
                }
            }
        }
        return result;
    }

    
    public Binder getBinderByName(String binderName) {
        for (Binder binder : this.binders) {
            if (binder.getBinderName().equalsIgnoreCase(binderName)) {
                return binder;
            }
        }
        return null;
    }

    
    public Tab getTabByName(String tabName) {
        for (Binder binder : binders) {
            for (Tab tab : binder.getTabs()) {
                if (tab.getTabName().equalsIgnoreCase(tabName)) {
                    return tab;
                }
            }
        }
        return null;
    }


    public Note getNoteFromBinderTabNoteName(String noteName, String tabName, String binderName) {

        ArrayList<Binder> binders = this.getBinders();
        for (Binder binder : binders) {

            if (binder.getBinderName().equals(binderName)) {
                System.out.println("Binder " + binderName + " found.");

                ArrayList<Tab> tabs = binder.getTabs();
                for (Tab tab : tabs) {
                    if (tab.getTabName().equals(tabName)) {
                        System.out.println("Tab " + tabName + " found.");

                        ArrayList<Note> notes = tab.getNotes();
                        for (Note note : notes) {
                            if (note.getNoteName().equals(noteName)) {
                                return note;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }


}

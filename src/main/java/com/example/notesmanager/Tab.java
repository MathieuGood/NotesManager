package com.example.notesmanager;


import java.sql.ResultSet;
import java.util.ArrayList;



public class Tab {


    // An ArrayList that holds all the Note objects associated with this Tab.
    private ArrayList<Note> notes = new ArrayList<>();

    // The unique identifier for this Tab.
    private final int tabID;

    // The unique identifier for the Binder that this Tab belongs to.
    private final int binderID;

    // The name of this Tab.
    private String tabName;

    // The unique identifier for the color of this Tab.
    private int tabColorID;


    public Tab(
            Binder binder,
            int tabID,
            String tabName,
            int tabColorID
    ) {
        this.tabID = tabID;
        this.binderID = binder.getBinderID();
        this.tabName = tabName;
        this.tabColorID = tabColorID;
    }


    
    public ArrayList<Note> getNotes() {
        return notes;
    }


    
    public int getTabID() {
        return tabID;
    }


    
    public int getBinderID() {
        return binderID;
    }


    
    public String getTabName() {
        return tabName;
    }


    
    public int getTabColorID() {
        return tabColorID;
    }


    public void addNoteToList(Note note) {
        notes.add(note);
    }

    
    public int editName(String newName) {
        int result = DatabaseManager.update(
                "tabs",
                "tab_name",
                newName,
                "tab_id",
                String.valueOf(this.tabID)
        );

        if (result > 0) {
            this.tabName = newName;
        }

        return result;
    }


    
    public int editColor(int newColorID) {
        int result = DatabaseManager.update(
                "tabs",
                "tab_color_id",
                String.valueOf(newColorID),
                "tab_id",
                String.valueOf(this.tabID)
        );

        if (result > 0) {
            this.tabColorID = newColorID;
        }

        return result;
    }


    public Note createNote(String noteName) {
        System.out.println("\n***");
        System.out.println("createNote() : " + noteName + " / tabID " + tabID);

        String[] fields = {"note_name", "tab_id"};
        String[] values = {noteName, String.valueOf(tabID)};

        int noteID = DatabaseManager.insert("notes", fields, values);

        Note note = new Note(this, noteID, noteName, new ArrayList<NoteLabel>());
        notes.add(note);

        return note;
    }


    
    public int deleteNote(int noteID) {
        System.out.println("\n***");
        System.out.println("deleteNote() : " + " noteID " + noteID);

        int result = DatabaseManager.delete("notes", "note_id", String.valueOf(noteID));

        // If the delete operation was successful, remove the note from the ArrayList
        if (result > 0) {
            for (Note note : notes) {
                if (note.getNoteID() == noteID) {
                    notes.remove(note);
                    break;
                }
            }
        }
        return result;
    }

    public String getColorHex() {
        return NotebookColor.getHexColorByID(this.tabColorID);
    }

}

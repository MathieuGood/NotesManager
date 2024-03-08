package com.example.notesmanager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Represents a note in a tab in a note-taking application.
 * It contains methods to get and set the note's properties, and to interact with the database.
 */
public class Note {


    private ArrayList<NoteLabel> labels = new ArrayList<>();

    /**
     * The unique identifier for this Note.
     */
    private final int noteID;

    /**
     * The unique identifier for the Tab that this Note belongs to.
     */
    private final int tabID;

    /**
     * The name of this Note.
     */
    private String noteName;

    /**
     * The content of this Note.
     */
    private String noteContent;


    private LabelMenuBuilder notebookLabel;


    public Note(
            Tab tab,
            int noteID,
            String noteName,
            ArrayList<NoteLabel> labels
    ) {
        this.noteID = noteID;
        this.tabID = tab.getTabID();
        this.noteName = noteName;
        this.labels = labels;
    }


    /**
     * Returns the unique identifier for this Note.
     *
     * @return The unique identifier for this Note.
     */
    public int getNoteID() {
        return noteID;
    }


    /**
     * Returns the unique identifier for the Tab that this Note belongs to.
     *
     * @return The unique identifier for the Tab that this Note belongs to.
     */
    public int getTabID() {
        return tabID;
    }


    /**
     * Returns the name of this Note.
     *
     * @return The name of this Note.
     */
    public String getNoteName() {
        return noteName;
    }


//    public String getNoteLabel1() {
//        return noteLabel1;
//    }
//
//    public String getNoteLabel2() {
//        return noteLabel2;
//    }

    public ArrayList<NoteLabel> getLabels() {
        return labels;
    }


    /**
     * Returns the content of this Note.
     *
     * @return The content of this Note.
     */
    public String getNoteContent() {
        return noteContent;
    }

    /**
     * Sets the name of this Note.
     *
     * @param noteName The new name for this Note.
     */
    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    /**
     * Sets the content of this Note.
     *
     * @param noteContent The new content for this Note.
     */
    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }


    /**
     * Fetches the content of this Note from the database.
     * If the noteContent is null, it queries the database for the note content using the note's ID.
     * The result is then assigned to the noteContent.
     */
    public void fetchNoteContent() {
        if (noteContent == null) {
            ResultSet resultSet = DatabaseManager.select(
                    "notes",
                    new String[]{"note_content"},
                    new String[]{"note_id"},
                    new String[]{String.valueOf(this.noteID)}
            );

            try {
                resultSet.first();
                noteContent = resultSet.getString(1);
            } catch (SQLException e) {
                System.out.println("Error : " + e);
            }
        }
    }

    public static ResultSet fetchNoteContentByNoteName(String noteName, String tabName, String binderName) {

        int binderIdFromName = 0;
        int tabIdFromName = 0;

        ResultSet resultSetNote = null;

        // GetId binder from the name
        String[] fieldsBinder = {"binders.binder_id"};
        String[] conditionFieldsBinder = {"binders.binder_name"};
        String[] conditionValuesBinder = {binderName};

        ResultSet resultSetBinder = DatabaseManager.select("binders", fieldsBinder, conditionFieldsBinder, conditionValuesBinder);

        try {
            while (resultSetBinder.next()) binderIdFromName = resultSetBinder.getInt(1);
        } catch (Exception e) {
            System.out.println("Error : " + e);
        }

        // Si binder trouvé
        if (binderIdFromName > 0) {
            String[] fieldsTab = {"tabs.tab_id"};
            String[] conditionFieldsTab = {"tabs.tab_name", "tabs.binder_id"};
            String[] conditionValuesTab = {tabName, String.valueOf(binderIdFromName)};

            ResultSet resultSetTab = DatabaseManager.select("tabs", fieldsTab, conditionFieldsTab, conditionValuesTab);

            try {
                while (resultSetTab.next()) tabIdFromName = resultSetTab.getInt(1);
            } catch (Exception e) {
                System.out.println("Error : " + e);
            }
        }

        if (tabIdFromName > 0) {
            String[] fieldsNote = {
                    "notes.note_id",
                    "notes.tab_id",
                    "notes.note_name",
                    "notes.note_content",
                    "notes.note_label1_id",
                    "notes.note_label2_id",
            };
            String[] conditionFieldsNote = {"notes.note_name", "notes.tab_id"};
            String[] conditionValuesNote = {noteName, String.valueOf(tabIdFromName)};

            resultSetNote = DatabaseManager.select("notes", fieldsNote, conditionFieldsNote, conditionValuesNote);
        }

        System.out.println("binder Id " + binderIdFromName);
        System.out.println("tab Id " + tabIdFromName);

        return resultSetNote;

        // noteID
        // noteName
        // noteContent
        // tabID


    }


    /**
     * Edits the name of this Note.
     * It updates the note name in the database and returns the result of the update operation.
     *
     * @param newName The new name for this Note.
     * @return The result of the update operation. Typically, the number of rows affected.
     */
    public int editName(String newName) {
        int result = DatabaseManager.update(
                "notes",
                "note_name",
                newName,
                "note_id",
                String.valueOf(this.noteID)
        );

        if (result > 0) {
            this.noteName = newName;
        }

        return result;
    }


    /**
     * Edits the content of this Note.
     * It updates the note content in the database and returns the result of the update operation.
     *
     * @param newContent The new content for this Note.
     * @return The result of the update operation. Typically, the number of rows affected.
     */
    public int editContent(String newContent) {
        int result = DatabaseManager.update(
                "notes",
                "note_content",
                newContent,
                "note_id",
                String.valueOf(this.noteID)
        );

        if (result > 0) {
            this.noteContent = newContent;
        }

        return result;
    }


    public void addNoteLabel(String labelName) {
        String field;
        // Get the label ID from the label name
        notebookLabel = new LabelMenuBuilder();
        int labelID = notebookLabel.getLabelID(labelName);

        // Check if the note already has two labels or if the chosen label is already applied to the note (take in account that labels can be empty)
        if (labels.size() == 2) {
            System.out.println("Can't apply label " + labelName + " Note already has two labels");
            return;
        } else if (labels.stream().anyMatch(label -> label.getLabelName().equals(labelName))) {
            System.out.println("Can't apply label " + labelName + " Note already has this label");
            return;
        } else {
            // Determine which field to update in the database
            if (labels.isEmpty()) {
                field = "note_label1_id";
            } else {
                field = "note_label2_id";
            }

            // Update the note in the database
            int result = DatabaseManager.update(
                    "notes",
                    field,
                    String.valueOf(labelID),
                    "note_id",
                    String.valueOf(this.noteID)
            );

            // If the update operation was successful, add the label to the note's labels list
            if (result > 0) {
                labels.add(new NoteLabel(labelName));
            }

        }

    }

    public void removeNoteLabel(String labelName) {
        String field;

        // Update the note in the database
        String[] args = {String.valueOf(this.noteID), labelName};
        int result = DatabaseManager.call(
                "remove_note_label",
                args
        );

        // If the update operation was successful, remove the label from the note's labels list
        if (result > 0) {
            labels.removeIf(label -> label.getLabelName().equals(labelName));
        }
    }

}
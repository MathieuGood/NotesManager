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


    private LabelManager notebookLabel;


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


    public int attachLabelToNote(String labelName) {
        String field;
        // Get the label ID from the label name
        notebookLabel = new LabelManager();
        int labelID = notebookLabel.getLabelID(labelName);

        // Check if the note already has two labels or if the chosen label is already applied to the note (take in account that labels can be empty)
        if (labels.size() == 2) {

            System.out.println("Can't apply label " + labelName + " Note already has two labels");
            // Print all labels in the note
            System.out.println("Labels in note " + this.noteID + " : ");
            for (NoteLabel label : labels) {
                System.out.println(label.getLabelName());
            }

            // TODO : Add alert

            return 0;

        } else if (labels.stream().anyMatch(label -> label.getLabelName().equals(labelName))) {
            System.out.println("Can't apply label " + labelName + " Note already has this label");
            System.out.println("Labels in note " + this.noteID + " : ");

            for (NoteLabel label : labels) {
                System.out.println(label.getLabelName());
            }

            // TODO : Add alert

            return 0;

        } else {
            // Update the note in the database with the new label
            String[] inArgs = {
                    // ID of label to add
                    String.valueOf(labelID),
                    // ID of the note to which the label is added
                    String.valueOf(this.noteID)
            };

            int result = DatabaseManager.call(
                    "UpdateNoteLabelToNewValue",
                    inArgs,
                    ""
            );

            // If the update operation was successful, add the label to the note's labels list
            if (result > 0) {
                labels.add(new NoteLabel(labelName));
            }

            return result;
        }

    }


    public int detachLabelFromNote(String labelName) {
        // Get the label ID from the label name
        notebookLabel = new LabelManager();
        int labelID = notebookLabel.getLabelID(labelName);

        // Get the label ID from the label name
        String[] inArgs = {
                // ID of label to remove
                String.valueOf(labelID),
                // ID of the note from which the label is removed
                String.valueOf(this.noteID)
        };

        int result = DatabaseManager.call(
                "UpdateNoteLabelToNull",
                inArgs,
                ""
        );

        // If the update operation was successful, remove the label from the note's labels list
        if (result > 0) {

            // Print content of labels ArrayList
            System.out.println("Before removing label " + labelName + " from note " + this.noteID);
            for (NoteLabel label : labels) {
                System.out.println(label.getLabelName());
            }

            labels.removeIf(label -> label.getLabelName().equals(labelName));

            // Print content of labels ArrayList
            System.out.println("After removing label " + labelName + " from note " + this.noteID);
            for (NoteLabel label : labels) {
                System.out.println(label.getLabelName());
            }
        }

        return result;
    }

}
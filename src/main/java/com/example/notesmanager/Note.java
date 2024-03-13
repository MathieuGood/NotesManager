package com.example.notesmanager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Note {


    private ArrayList<NoteLabel> labels = new ArrayList<>();

    
    private final int noteID;

    
    private final int tabID;

    
    private String noteName;

    
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


    
    public int getNoteID() {
        return noteID;
    }


    
    public int getTabID() {
        return tabID;
    }


    
    public String getNoteName() {
        return noteName;
    }


    public ArrayList<NoteLabel> getLabels() {
        return labels;
    }


    
    public String getNoteContent() {
        return noteContent;
    }

    
    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    
    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }


    
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
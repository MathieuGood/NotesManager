package com.example.notesmanager;

public class NoteLabel {

    private String labelName;


    NoteLabel(String labelName) {
        this.labelName = labelName;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }
}

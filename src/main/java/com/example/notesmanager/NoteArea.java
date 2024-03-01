package com.example.notesmanager;

import javafx.scene.web.HTMLEditor;

public class NoteArea {
    Note note;
    HTMLEditor noteArea;
    public NoteArea(Note note, HTMLEditor noteArea){
        this.note = note;
        this.noteArea= noteArea;
        if(!getContent().isEmpty()){
            noteArea.setHtmlText(getContent());
        }
    }
    public String getContent(){
        return this.note.getNoteContent();
    }
    public void setContent(String newContent){
        try {
            note.editContent(newContent);
            System.out.println("Saved");
        } catch (Exception ex) {
            System.out.println("Error : " + ex);
        }
        this.noteArea.setHtmlText(newContent);
    }
}
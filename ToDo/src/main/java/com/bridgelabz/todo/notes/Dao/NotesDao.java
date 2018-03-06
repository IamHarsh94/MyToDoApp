package com.bridgelabz.todo.notes.Dao;

import java.util.List;

import com.bridgelabz.todo.notes.model.Note;

public interface NotesDao {

	void saveNote(Note note);

	void deleteNote(int userId);

	void updateNote(Note note);

	Note getNoteByNoteId(int userId);

	List<Note> getNotesByUserId(int userId);
	
	
}

package com.bridgelabz.todo.notes.service;

import java.util.List;

import com.bridgelabz.todo.notes.model.CreateNoteDto;
import com.bridgelabz.todo.notes.model.NoteResponseDto;
import com.bridgelabz.todo.notes.model.UpdateNoteDto;

public interface NoteService {


	NoteResponseDto saveNote(CreateNoteDto newNoteDto, int userId);

	void deleteNote(int userId, int noteId);

	NoteResponseDto updateNote(UpdateNoteDto updateDTO, int userId);

	List<NoteResponseDto> getNotes(int tokenId);
	
	
	
}

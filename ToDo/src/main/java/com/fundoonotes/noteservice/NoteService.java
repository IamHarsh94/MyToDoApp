package com.fundoonotes.noteservice;

import java.util.List;

public interface NoteService {


	NoteResponseDto saveNote(CreateNoteDto newNoteDto, int userId);

	NoteResponseDto updateNote(UpdateNoteDto updateDTO, int userId);

	List<NoteResponseDto> getNotes(int tokenId);

	void delete(int tokenId, int noteId);

	void saveLabel(LabelDTO labelObj, int userId);

	List<LabelDTO> getLabels(int userId);
	
	
	
}

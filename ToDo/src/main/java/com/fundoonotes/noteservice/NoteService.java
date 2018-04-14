package com.fundoonotes.noteservice;

import java.util.List;

import com.fundoonotes.userservice.UserModel;

public interface NoteService {


	NoteResponseDto saveNote(CreateNoteDto newNoteDto, int userId);

	NoteResponseDto updateNote(UpdateNoteDto updateDTO, int userId);

	List<NoteResponseDto> getNotes(int tokenId);

	void delete(int tokenId, int noteId);

	void saveLabel(LabelDTO labelObj, int userId);

	List<LabelDTO> getLabels(int userId);

   void addLabel(AddRemoveLabelDTO reqDTO, int userId);

   void removeLabel(AddRemoveLabelDTO reqDTO, int userId);

   UserModel addCollaborator(CollaboratorReqDTO personReqDTO);
	
	
	
}

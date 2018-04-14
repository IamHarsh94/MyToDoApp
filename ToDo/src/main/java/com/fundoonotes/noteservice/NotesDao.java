package com.fundoonotes.noteservice;

import java.util.List;

import com.fundoonotes.userservice.UserDTO;

public interface NotesDao {

	void saveNote(NoteModel note);

	void deleteNote(int noteId);

	void updateNote(NoteModel note);

	NoteModel getNoteByNoteId(int userId);

	List<NoteModel> getNotesByUserId(int userId);

	void saveLabel(LabelDTO labelObj);

	List<LabelDTO> getLabelsByUserId(int userId);

   List<LabelResDTO> getLabelByNoteId(int noteId);

   void addLabelToNote(AddRemoveLabelDTO reqDTO);

   void removeLabelFromNote(AddRemoveLabelDTO reqDTO);

   void addcollaborator(int id, CollaboratorReqDTO personReqDTO);

   List<UserDTO> getCollaborators(int noteId);
	
}

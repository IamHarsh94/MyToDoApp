package com.fundoonotes.noteservice;

import java.util.List;

import com.fundoonotes.userservice.UserDTO;
import com.fundoonotes.utilservice.UrlData;

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

   boolean addcollaborator(int id, CollaboratorReqDTO personReqDTO, int userId);

   List<UserDTO> getCollaborators(int noteId);

   List<ResCollaboratorDTO> getSharedNoteIDAndUserId(int userId);

   CollaboratorResponseDTO getSharedNotes(int noteId,int userId);

   void deleteCollaborator(int noteId) throws Exception;

   boolean getRowFromCollaborator(int noteId, int userId);

   boolean isCollaborate(int noteId);

   void deleteLabelByUserId(LabelDTO labelObj);

   void updateLabelByLabelId(LabelDTO labelObj);

}

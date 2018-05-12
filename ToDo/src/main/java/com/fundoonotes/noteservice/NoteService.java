package com.fundoonotes.noteservice;

import java.util.List;
import org.springframework.stereotype.Service;
import com.fundoonotes.userservice.UserModel;
import com.fundoonotes.utilservice.UrlData;
@Service
public interface NoteService {


	NoteResponseDto saveNote(CreateNoteDto newNoteDto, int userId);

	NoteResponseDto updateNote(UpdateNoteDto updateDTO, int userId);

	List<NoteResponseDto> getNotes(int tokenId);

	void delete(int tokenId, int noteId);

	void saveLabel(LabelDTO labelObj, int userId);

	List<LabelDTO> getLabels(int userId);

   void addLabel(AddRemoveLabelDTO reqDTO, int userId);

   void removeLabel(AddRemoveLabelDTO reqDTO, int userId);

   UserModel addRemoveCollaborator(CollaboratorReqDTO personReqDTO, int userId);

   void uploadImage(UpdateNoteDto imageReqDTO, int userId);

   void deleteLabel(LabelDTO labelObj);

   void updateLabel(LabelDTO labelObj);
	
	
	
}

package com.fundoonotes.noteservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fundoonotes.exception.UnAuthorizedAcess;
import com.fundoonotes.userservice.UserModel;
import com.fundoonotes.userservice.IuserDao;

@Service
public class NoteServiceImpl implements NoteService {

	@Autowired
	private NotesDao noteDao;

	@Autowired
	private IuserDao userDao;

	@Override
	public NoteResponseDto saveNote(CreateNoteDto newNoteDto, int userId) {
		
		NoteModel note = new NoteModel(newNoteDto);
		
		note.setCreateDate(new Date());
		note.setLastUpdateDate(new Date());
		
		UserModel user = new UserModel();
		user.setId(userId);
		note.setUser(user);
	
		noteDao.saveNote(note);
		note.setReminder(null);
		return new NoteResponseDto(note);
	}
	
	@Override
	public void delete(int tokenId, int noteId) {
		UserModel user = userDao.getUserById(tokenId);

		NoteModel note = noteDao.getNoteByNoteId(noteId);
		if (user.getId() != note.getUser().getId()) {
			throw new UnAuthorizedAcess();
		}

		noteDao.deleteNote(noteId);
			
	}
	@Override
	public NoteResponseDto updateNote(UpdateNoteDto updateDTO, int tokenId) {
		UserModel user = userDao.getUserById(tokenId);
		
		NoteModel note =new NoteModel(updateDTO);
		note.setTitle(updateDTO.getTitle());
		note.setDescription(updateDTO.getDescription());
		
		Date updatedAt = new Date();
		note.setLastUpdateDate(updatedAt);
		note.setUser(user);
		
		note.setReminder(updateDTO.getReminder());
		noteDao.updateNote(note);
	
		return new NoteResponseDto(note);
		
	}

	@Override
	public List<NoteResponseDto> getNotes(int userId) {
		UserModel user = new UserModel();
		user.setId(userId);
		
		List<NoteModel> list = noteDao.getNotesByUserId(userId);
		
		List<NoteResponseDto> notes = new ArrayList<>();
		for (NoteModel note : list) {
			NoteResponseDto dto = new NoteResponseDto(note);
			notes.add(dto);
		}
		return notes;
	}

	@Override
	public void saveLabel(LabelDTO labelObj, int userId) {
		
		UserModel user = new UserModel();
		user.setId(userId);
		labelObj.setUser(user);
		noteDao.saveLabel(labelObj);
		
	}

	@Override
	public List<LabelDTO> getLabels(int userId) {
		List<LabelDTO> list = noteDao.getLabelsByUserId(userId);
		return list;
	}

}

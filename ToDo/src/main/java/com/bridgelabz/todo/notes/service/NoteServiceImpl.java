package com.bridgelabz.todo.notes.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.todo.notes.Dao.NotesDao;
import com.bridgelabz.todo.notes.Exception.UnAuthorizedAcess;
import com.bridgelabz.todo.notes.model.CreateNoteDto;
import com.bridgelabz.todo.notes.model.Note;
import com.bridgelabz.todo.notes.model.NoteResponseDto;
import com.bridgelabz.todo.notes.model.UpdateNoteDto;
import com.bridgelabz.todo.user.Dao.userDao;
import com.bridgelabz.todo.user.model.User;

@Service
public class NoteServiceImpl implements NoteService {

	@Autowired
	private NotesDao noteDao;

	@Autowired
	private userDao userDao;

	@Override
	public NoteResponseDto saveNote(CreateNoteDto newNoteDto, int userId) {

		Note note = new Note(newNoteDto);

		Date date = new Date();
		note.setCreateDate(date);
		note.setLastUpdateDate(date);

		User user = new User();
		user.setId(userId);
		note.setUser(user);

		noteDao.saveNote(note);

		return new NoteResponseDto(note);
	}

	@Override
	public void deleteNote(int tokenId, int noteId) {
		User user = userDao.getUserById(tokenId);

		Note note = noteDao.getNoteByNoteId(noteId);

		if (user.getId() != note.getUser().getId()) {
			throw new UnAuthorizedAcess();
		}

		noteDao.deleteNote(noteId);
	}

	@Override
	public NoteResponseDto updateNote(UpdateNoteDto updateDTO, int tokenId) {
		User user = userDao.getUserById(tokenId);

		Note note = noteDao.getNoteByNoteId(updateDTO.getNoteId());
		if (user.getId() != note.getUser().getId()) {
			throw new UnAuthorizedAcess();
		}
		
		note.setTitle(updateDTO.getTitle());
		note.setDescription(updateDTO.getDescription());

		Date updatedAt = new Date();
		note.setLastUpdateDate(updatedAt);
		
		noteDao.updateNote(note);
		
		return new NoteResponseDto(note);
	}

	@Override
	public List<NoteResponseDto> getNotes(int tokenId) {
		User user = new User();
		user.setId(tokenId);
		
		List<Note> list = noteDao.getNotesByUserId(tokenId);

		List<NoteResponseDto> notes = new ArrayList<>();
		for (Note note : list) {
			NoteResponseDto dto = new NoteResponseDto(note);
			notes.add(dto);
		}
		return notes;
	}

}

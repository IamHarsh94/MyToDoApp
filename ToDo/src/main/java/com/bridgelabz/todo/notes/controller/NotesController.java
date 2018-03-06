package com.bridgelabz.todo.notes.controller;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.todo.notes.model.CreateNoteDto;
import com.bridgelabz.todo.notes.model.NoteResponseDto;
import com.bridgelabz.todo.notes.model.UpdateNoteDto;
import com.bridgelabz.todo.notes.service.NoteService;
import com.bridgelabz.todo.user.util.Token;

@RestController
@RequestMapping("/note/")
public class NotesController {
	@Autowired 
	private NoteService noteService;

	@RequestMapping(value="createNote",method =RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createNote(@RequestBody CreateNoteDto newNoteDto ,HttpServletRequest req) {

		int tokenId = Token.verifyToken(req.getHeader("authtoken")); 
		NoteResponseDto noteResObj= noteService.saveNote(newNoteDto, tokenId);
		return new ResponseEntity<NoteResponseDto>(noteResObj,HttpStatus.OK);

	}

	@RequestMapping(value="deleteNote",method =RequestMethod.DELETE ,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteNode(HttpServletRequest req) {
		int tokenId = Token.verifyToken(req.getHeader("authtoken"));
		int noteId = req.getIntHeader("noteId");
		noteService.deleteNote(tokenId,noteId);
		return new ResponseEntity<String>("Note deleted",HttpStatus.OK);
	}

	@RequestMapping(value="updateNote",method =RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateNote(@RequestBody UpdateNoteDto updateDTO,HttpServletRequest req) {

		int tokenId = Token.verifyToken(req.getHeader("authtoken"));
		NoteResponseDto	resNote=noteService.updateNote(updateDTO,tokenId);
		return new ResponseEntity<NoteResponseDto>(resNote,HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value="getNotes",method =RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getNotes(HttpServletRequest req) {
		
		int userId = (int)req.getAttribute("userId");
		List<NoteResponseDto>notes=noteService.getNotes(userId);
		return new ResponseEntity<List>(notes,HttpStatus.OK);



	}

}

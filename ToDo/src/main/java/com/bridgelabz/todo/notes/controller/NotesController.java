package com.bridgelabz.todo.notes.controller;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
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

	@RequestMapping(value="createNote",method =RequestMethod.PUT ,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createNote(@RequestBody CreateNoteDto newNoteDto,@RequestAttribute(name="userId") int userId){
	
		NoteResponseDto noteResObj= noteService.saveNote(newNoteDto, userId);
		return new ResponseEntity<NoteResponseDto>(noteResObj,HttpStatus.OK);
		
	}
	
	@RequestMapping(value="delete/{noteId}",method =RequestMethod.DELETE ,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> delete(HttpServletRequest req,@PathVariable int noteId,@RequestAttribute(name="userId") int userId) {
		noteService.delete(userId,noteId);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value="updateNote",method =RequestMethod.PUT ,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateNote(@RequestBody UpdateNoteDto updateDTO,HttpServletRequest req,@RequestAttribute(name="userId") int userId) {
		
		NoteResponseDto	resNote=noteService.updateNote(updateDTO,userId);
		return new ResponseEntity<NoteResponseDto>(resNote,HttpStatus.OK);
	}
	
	@RequestMapping(value="getNotes",method =RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getNotes(HttpServletRequest req,@RequestAttribute(name="userId") int userId) {
		
		List<NoteResponseDto>notes=noteService.getNotes(userId);
		return new ResponseEntity<List>(notes,HttpStatus.OK);
	}

}

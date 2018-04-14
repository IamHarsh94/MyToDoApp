package com.fundoonotes.noteservice;
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

import com.fundoonotes.userservice.CustomResponseDTO;
import com.fundoonotes.userservice.IUserService;
import com.fundoonotes.userservice.UserModel;

/**
 * <p>
 * This is a Rest Controller for CRUD operation of note {@RestController}, we have added all
 * general purpose methods here those method will accept a rest request in JSON
 * form and will return a JSON response.
 * </p>
 * 
 * <p>
 *    We are returning the note in response DTO object  
 * </p>
 * 
 * @author BridgeLabz
 * 
 */
@RestController
@RequestMapping("/note/")
public class NotesController 
{
   private static CustomResponseDTO response = new CustomResponseDTO();
   
	@Autowired 
	private NoteService noteService;
	
	@Autowired
	private IUserService userService;
	
	 /**
    * This rest API for new note creation with respective current loged user by
    * Getting the user id from request attribute 
    * 
    * @param request DTO Object to be save the note
    * @return Response Note DTo Object with HTTP status.
    */
	@RequestMapping(value="createNote",method =RequestMethod.PUT ,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createNote(@RequestBody CreateNoteDto newNoteDto,@RequestAttribute(name="userId") int userId)
	{
	  
		NoteResponseDto noteResObj= noteService.saveNote(newNoteDto, userId);
		return new ResponseEntity<NoteResponseDto>(noteResObj,HttpStatus.OK);
		
	}
	
	@RequestMapping(value="delete/{noteId}",method =RequestMethod.DELETE ,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> delete(HttpServletRequest req,@PathVariable int noteId,@RequestAttribute(name="userId") int userId)
	{
		
	   noteService.delete(userId,noteId);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value="updateNote",method =RequestMethod.PUT ,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateNote(@RequestBody UpdateNoteDto updateDTO,HttpServletRequest req,@RequestAttribute(name="userId") int userId) 
	{
		
		NoteResponseDto resNote=noteService.updateNote(updateDTO,userId);
		return new ResponseEntity<NoteResponseDto>(resNote,HttpStatus.OK);
	}
	
	@RequestMapping(value="getNotes",method =RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getNotes(HttpServletRequest req,@RequestAttribute(name="userId") int userId)
	{
		
		List<NoteResponseDto>notes=noteService.getNotes(userId);
		return new ResponseEntity<List<NoteResponseDto>>(notes,HttpStatus.OK);
	}
	
	@RequestMapping(value="createLabel",method =RequestMethod.PUT ,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createLabel(@RequestBody  LabelDTO labelObj,@RequestAttribute(name="userId") int userId)
	{
		noteService.saveLabel(labelObj, userId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	

	@RequestMapping(value="getLabels",method =RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getlabels(HttpServletRequest req,@RequestAttribute(name="userId") int userId)
	{
		
		List<LabelDTO>labels=noteService.getLabels(userId);
		return new ResponseEntity<List>(labels,HttpStatus.OK);
	}
	

   @RequestMapping(value="addRemoveLabel",method =RequestMethod.PUT ,produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<?> addRemoveLabel(@RequestBody  AddRemoveLabelDTO reqDTO,@RequestAttribute(name="userId") int userId)
   {
      if(reqDTO.isChecked())
      {
         noteService.addLabel(reqDTO,userId);   
         return new ResponseEntity<>(HttpStatus.OK);
         
      }
      else if(!reqDTO.isChecked())
      {
         noteService.removeLabel(reqDTO,userId);
         return new ResponseEntity<>(HttpStatus.OK);  
      }
      else
      {
         return new ResponseEntity<>(HttpStatus.CONFLICT);
      }       
   }
   
   @RequestMapping(value="addcollaborator",method =RequestMethod.PUT ,produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<?> addcollaborator(@RequestBody CollaboratorReqDTO personReqDTO, @RequestAttribute(name="userId") int userId)
   {
      UserModel user=noteService.addCollaborator(personReqDTO);
      
      if(user!=null)
      {
         response.setMessage("Successfully add");
         response.setStatusCode(200);
         return new ResponseEntity<>(response,HttpStatus.OK);
      } 
      response.setMessage("Collaborator adding fail ");
      response.setStatusCode(400);
      return new ResponseEntity<>(response,HttpStatus.OK);
   }
   
   
}

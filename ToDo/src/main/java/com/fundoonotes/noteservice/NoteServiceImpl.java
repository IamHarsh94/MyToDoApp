package com.fundoonotes.noteservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fundoonotes.exception.UnAuthorizedAcess;
import com.fundoonotes.userservice.UserModel;
import com.fundoonotes.userservice.CustomResponseDTO;
import com.fundoonotes.userservice.IuserDao;
import com.fundoonotes.userservice.UserController;
import com.fundoonotes.userservice.UserDTO;

@Service
public class NoteServiceImpl implements NoteService {

   private final org.apache.log4j.Logger LOGGER = LogManager.getLogger(UserController.class);

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
      if(note!=null)
      {
         if (user.getId() != note.getUser().getId())
         {
            throw new UnAuthorizedAcess();
         }

         noteDao.deleteNote(noteId);
      }
      LOGGER.info("Notes not present in db of "+noteId+" note id");

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

      List<ResCollaboratorDTO> collabList=noteDao.getSharedNoteIDAndUserId(userId);

      if(collabList!=null) 
      {
         for( ResCollaboratorDTO object:collabList){

            CollaboratorResponseDTO collab=noteDao.getSharedNotes(object.getNoteId(),object.getUserId());
            collab.setOwnerId(object.getUserId());
            NoteModel obj = new NoteModel(collab);
            list.add(obj);
         }
      }

      List<NoteResponseDto> notes = new ArrayList<>();

      for (NoteModel note : list)
      {
         NoteResponseDto dto = new NoteResponseDto(note);

         List<LabelResDTO> labels=noteDao.getLabelByNoteId(dto.getNoteId());

         List<UserDTO> collabolators=noteDao.getCollaborators(dto.getNoteId());
         dto.setCollaborators(collabolators);
         dto.setLabels(labels);
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

   @Override
   public void addLabel(AddRemoveLabelDTO reqDTO, int userId)
   {  
      NoteModel note=noteDao.getNoteByNoteId(reqDTO.getNoteId());

      if(note!=null)
      {
         if(userId!=note.getUser().getId())
         {
            throw new UnAuthorizedAcess();
         }
         noteDao.addLabelToNote(reqDTO);
      }

   }

   @Override
   public void removeLabel(AddRemoveLabelDTO reqDTO, int userId)
   {
      NoteModel note=noteDao.getNoteByNoteId(reqDTO.getNoteId());

      if(note!=null)
      {
         if(userId!=note.getUser().getId())
         {
            throw new UnAuthorizedAcess();
         }
         noteDao.removeLabelFromNote(reqDTO);
      }
   }
   @Override
   public UserModel addRemoveCollaborator(CollaboratorReqDTO personReqDTO,int userId)
   {
      if(personReqDTO.getRemoveUserMail()!=null)
      {
         UserModel user=userDao.getUserByEmailId(personReqDTO.getRemoveUserMail());
         
         userDao.removeCollaborator(personReqDTO.getNoteId(),user.getId());
         user.setPassWord(null);
         return user;
      }else {
         UserModel user=userDao.getUserByEmailId(personReqDTO.getPersonEmail());
         if(user!=null && user.getId()==userId)
         {
            user.setFullName(null);
            return user;
         }
         else 
         {
            boolean isAdd=noteDao.addcollaborator(user.getId(),personReqDTO,userId);

            if(!isAdd)
            {     
               user.setFullName(null);
              
            }
     
         }
         return user;
      }
     
   }

   @Override
   public void removeCollaborator(CollaboratorReqDTO personReqDTO)
   {


   }


}

package com.fundoonotes.noteservice;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.fundoonotes.exception.DatabaseException;
import com.fundoonotes.userservice.UserController;
import com.fundoonotes.userservice.UserDTO;
import com.fundoonotes.userservice.UserModel;
import com.fundoonotes.utilservice.DataBaseQueries;


public class notesDaoImpl implements NotesDao{
  
   private final org.apache.log4j.Logger LOGGER = LogManager.getLogger(UserController.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void saveNote(NoteModel note) {
		
		String INSERT_SQL = DataBaseQueries.getSavenotequery();
		
		KeyHolder holder = new GeneratedKeyHolder();
		
		int num = jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1,note.getId());
				ps.setString(2,note.getTitle());
				ps.setString(3,note.getDescription());
				ps.setDate(4, new Date(note.getCreateDate().getTime()));
				ps.setDate(5,new Date(note.getLastUpdateDate().getTime()));
				ps.setInt(6, note.getUser().getId());
				ps.setString(7,note.getColor());
				ps.setInt(8, note.getStatus());
				ps.setDate(9, null);
				ps.setString(10, note.getImage());
				return ps;
			}
		}, holder);
		
		if(num==0) 
		{
		   throw new DatabaseException();
		}
		
		int noteId = holder.getKey().intValue();
		note.setId(noteId);
	}

	public void deleteCollaborator(int noteId) throws Exception  {
       
	   int num=jdbcTemplate.update(DataBaseQueries.getDeletecollaboratorquery(), new Object[] { noteId });
      
      if(num==0)
      {
        throw new DatabaseException();
      }
  }
	@Override
	public boolean isCollaborate(int noteId) {
	   
	   String sql ="select * from Collaborators WHERE noteId=?";
	   
	   List<CollaboratorResponseDTO> list = jdbcTemplate.query(sql, new Object[] {noteId}, new getRowFromCollaborator());
      
      return list.size() > 0 ? true: false;
	}
	
	@Override
	public void deleteNote(int noteId)
	{
	   try { 
	         if(isCollaborate(noteId)) {
	            deleteCollaborator(noteId);
	         }
	      int num=jdbcTemplate.update(DataBaseQueries.getDeletenotequery(), new Object[] { noteId });
         
	      if(num==0)
	      {
	         throw new DatabaseException();
	      }
	   }catch(Exception e)
	   {
	      LOGGER.error("Error while deleting collaborator", e);
	   }
	    
	}
	
	
	
	@Override
	public void updateNote(NoteModel note) {
		
	   String sql = DataBaseQueries.getUpdatenotequery();
	      
		int num = jdbcTemplate.update(sql, new Object[] {note.getTitle(),note.getDescription(),
				note.getLastUpdateDate(),note.getColor(),note.getStatus(),note.getReminder(),note.getImage(),note.getId()});
		
		if( num == 0)
		{
			throw new DatabaseException();
		}
		
	}
	@Override
	public NoteModel getNoteByNoteId(int noteId) {
	   
	   String sql = DataBaseQueries.getSelectnotebynoteidquery();
		
	   List<NoteModel> list = jdbcTemplate.query(sql, new Object[] {noteId}, new MyMapperClass());
		
		if(list.size()==0)
		{
			throw new DatabaseException();
		}
		return list.size() > 0 ? list.get(0) : null;
	}
	
	@Override
	public List<NoteModel> getNotesByUserId(int userId) {
		
	   String sql =DataBaseQueries.getSelectnotebyuseridquery();
	   
		List<NoteModel> list = jdbcTemplate.query(sql, new Object[] {userId}, new MyMapperClass());
		
		return list;
	}

	@Override
	public void saveLabel(LabelDTO labelObj) {
	
		String sql = DataBaseQueries.getSavelabelquery();
		
		int num = jdbcTemplate.update(sql, new Object[] {labelObj.getLabelId(),labelObj.getLabelTitle(),labelObj.getUser().getId()});
		 
		if(num==0)
		{
			throw new DatabaseException();
		}
		
	}

	@Override
	public List<LabelDTO>getLabelsByUserId(int userId) {
		
	   String sql = DataBaseQueries.getGetlabelbyuseridquery();
		
	   List<LabelDTO> list = jdbcTemplate.query(sql, new Object[] {userId}, new MyLabelMapperClass());
		
	   return list;

	}

   @Override
   public List<LabelResDTO> getLabelByNoteId(int noteId)
   {
      
      String sql = DataBaseQueries.getLabelandnoteLabeljoinquery();
      
      List<LabelResDTO> list = jdbcTemplate.query(sql, new Object[] {noteId}, new GetLabelMapperClass());
      
      return list.size() > 0 ? list : null;

      
   }

   @Override
   public void addLabelToNote(AddRemoveLabelDTO reqDTO)
   {
     
      String sql = DataBaseQueries.getSavenoteLabel();
     
      int num = jdbcTemplate.update(sql, new Object[] {reqDTO.getNoteId(),reqDTO.getLabelId()});
      
      if(num==0)
      {
        throw new DatabaseException();
      }
   }

   @Override
   public void removeLabelFromNote(AddRemoveLabelDTO reqDTO)
   {
      int num=jdbcTemplate.update(DataBaseQueries.getDeletefromnoteLabelquery(), new Object[] { reqDTO.getNoteId(),reqDTO.getLabelId() });
      
      if(num==0)
      {
         throw new DatabaseException();
      }
      
   }
   
   @Override
   public boolean getRowFromCollaborator(int noteId,int userId)
   {
    
      String sql=DataBaseQueries.getSelectfromcollaboratorbyuseridandnoteid();
      
      List<CollaboratorResponseDTO> list = jdbcTemplate.query(sql, new Object[] {userId,noteId}, new getRowFromCollaborator());
      
      return list.size() > 0 ? true: false;
   }
   
   class getRowFromCollaborator implements org.springframework.jdbc.core.RowMapper<CollaboratorResponseDTO> 
   {
      public CollaboratorResponseDTO mapRow(ResultSet rs, int rowNum) throws SQLException
      {
         CollaboratorResponseDTO obj = new CollaboratorResponseDTO();   
         obj.setNoteId(rs.getInt("noteId"));
         obj.setOwnerId(rs.getInt("sharedUserId"));
         
        return obj;
      }

   }
   @Override
   public boolean addcollaborator(int id,CollaboratorReqDTO personReqDTO,int userId)
   {
     boolean isPresent=getRowFromCollaborator(personReqDTO.getNoteId(),id); 
     
     if(!isPresent) {
        String sql = DataBaseQueries.getInsertcollaboratorquery();
        
        int num = jdbcTemplate.update(sql, new Object[] {personReqDTO.getId(),personReqDTO.getNoteId(),id,userId});
        
        if(num==0)
        {
           throw new DatabaseException();
         }
        return true;
     }
     return false;
     
   }
   
   @Override
   public List<UserDTO> getCollaborators(int noteId)
   {
    
      String sql=DataBaseQueries.getGetcollaboratorbyuserandcollaboratorjoin(); 
      
      List<UserDTO> list = jdbcTemplate.query(sql, new Object[] {noteId}, new GetCollaborators());
      
      return list.size() > 0 ? list : null;
   }

   @Override
   public List<ResCollaboratorDTO> getSharedNoteIDAndUserId(int userId)
   {
      String sql=DataBaseQueries.getSelectfromcollaboratorbyuserid();
     
      List<ResCollaboratorDTO> list = jdbcTemplate.query(sql, new Object[] {userId}, new GetCollaboratorsObject());
      
      return list.size() > 0 ? list : null;
   }

   @Override
   public CollaboratorResponseDTO getSharedNotes(int noteId,int userId) 
   {
     
      String sql=DataBaseQueries.getSelectfromnoteanduser();
      
      List<CollaboratorResponseDTO> list = jdbcTemplate.query(sql, new Object[] {noteId,userId}, new GetSharedNotes());
     
      return list.size() > 0 ? list.get(0) : null;
   }
   
   public boolean getLabel_Note(int labelId) {
      String sql="select * from Note_Label where labelId=?";
      List<AddRemoveLabelDTO> list = jdbcTemplate.query(sql, new Object[] {labelId}, new GetLabelNote());
      
      return list.size() > 0 ? true : false;
   }
   
   public void deleteMappingLabelNote(int labelId) {
       String sql="delete from Note_Label where labelId=?";
       
       int num=jdbcTemplate.update(sql, new Object[] { labelId});
       
       if(num==0)
       {
         throw new DatabaseException();
       }
   }
  
   @Override
   public void deleteLabelByUserId(LabelDTO labelObj)
   {
     if(getLabel_Note(labelObj.getLabelId())) {
        deleteMappingLabelNote(labelObj.getLabelId());
     }
     
     String sql="delete from LABEL where labelId=? and userId=?";
     int num=jdbcTemplate.update(sql, new Object[] { labelObj.getLabelId(),labelObj.getUser().getId()});
     
     if(num==0)
     {
       throw new DatabaseException();
     }
      
   }

   @Override
   public void updateLabelByLabelId(LabelDTO labelObj)
   {
     String sql="update LABEL set labelTitle=? where labelId=?";
     int num=jdbcTemplate.update(sql, new Object[] { labelObj.getLabelTitle(),labelObj.getLabelId()});
     
     if(num==0)
     {
       throw new DatabaseException();
     }
      
   }
   
}

class GetLabelNote implements org.springframework.jdbc.core.RowMapper<AddRemoveLabelDTO>
{
   public AddRemoveLabelDTO mapRow(ResultSet rs, int rowNum) throws SQLException
   {
      AddRemoveLabelDTO NoteLabel = new AddRemoveLabelDTO();
      NoteLabel.setLabelId(rs.getInt("labelId"));
      NoteLabel.setNoteId(rs.getInt("noteId"));
      return NoteLabel;
   }
}

class GetCollaborators implements org.springframework.jdbc.core.RowMapper<UserDTO>
{
   public UserDTO mapRow(ResultSet rs, int rowNum) throws SQLException
   {
      UserDTO user = new UserDTO();
      user.setFullName(rs.getString("fullName"));
      user.setUserEmail(rs.getString("userEmail"));
      return user;
   }
}

class GetSharedNotes implements org.springframework.jdbc.core.RowMapper<CollaboratorResponseDTO>
{
   public CollaboratorResponseDTO mapRow(ResultSet rs, int rowNum) throws SQLException
   {
      CollaboratorResponseDTO note = new CollaboratorResponseDTO();
      
      note.setNoteId(rs.getInt("id"));
      note.setTitle(rs.getString("title"));
      note.setDescription(rs.getString("description"));
      note.setFullName(rs.getString("fullName")); 
      note.setColor(rs.getString("color"));
      
      return note;
   }
}


class GetCollaboratorsObject implements org.springframework.jdbc.core.RowMapper<ResCollaboratorDTO>
{
   public ResCollaboratorDTO mapRow(ResultSet rs, int rowNum) throws SQLException
   {
      ResCollaboratorDTO collab = new ResCollaboratorDTO();
      collab.setNoteId(rs.getInt("noteId"));
      collab.setSharedUserId(rs.getInt("sharedUserId"));
      collab.setUserId(rs.getInt("userId"));
      return collab;
   }
}
class MyMapperClass implements org.springframework.jdbc.core.RowMapper<NoteModel>
{
	public NoteModel mapRow(ResultSet rs, int rowNum) throws SQLException
	{
		NoteModel note = new NoteModel();			
		
		note.setId(rs.getInt("id"));
		note.setTitle(rs.getString("title"));
		note.setDescription(rs.getString("description"));
		note.setCreateDate(rs.getDate("createDate"));
		note.setLastUpdateDate(rs.getDate("lastUpdateDate"));
		note.setStatus(rs.getInt("status"));
		note.setColor(rs.getString("color"));
		note.setImage(rs.getString("image"));
		try {
		   java.util.Date date=null;
		   Timestamp timestamp = rs.getTimestamp("reminder");
		
		   if (timestamp != null) {
		      date = new java.util.Date(timestamp.getTime());
			   note.setReminder(date);
		   }
		}catch(Exception e)
		{
			e.printStackTrace();
		}	
		int userId = rs.getInt("userId");
		UserModel user = new UserModel();
		user.setId(userId);
		note.setUser(user);
		return note;
	}

}
class MyLabelMapperClass implements org.springframework.jdbc.core.RowMapper<LabelDTO>
{
   public LabelDTO mapRow(ResultSet rs, int rowNum) throws SQLException
   {
      LabelDTO label = new LabelDTO();       
      label.setLabelId(rs.getInt("labelId"));
      label.setLabelTitle(rs.getString("labelTitle"));
      
      int userId = rs.getInt("userId");
      UserModel user = new UserModel();
      user.setId(userId);
      label.setUser(user);
      return label;
   }

}

class GetLabelMapperClass implements org.springframework.jdbc.core.RowMapper<LabelResDTO> 
{
   public LabelResDTO mapRow(ResultSet rs, int rowNum) throws SQLException
   {
	  
      LabelResDTO label = new LabelResDTO();			
		label.setLabelId(rs.getInt("labelId"));
		label.setLabelTitle(rs.getString("labelTitle"));
		return label;
	}

}


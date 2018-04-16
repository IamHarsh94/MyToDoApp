package com.fundoonotes.noteservice;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.fundoonotes.exception.DatabaseException;
import com.fundoonotes.userservice.UserDTO;
import com.fundoonotes.userservice.UserModel;


public class notesDaoImpl implements NotesDao{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void saveNote(NoteModel note) {
		String INSERT_SQL = "insert into NOTES values(?,?,?,?,?,?,?,?,?)";	
		
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

	@Override
	public void deleteNote(int noteId) {
		int num=jdbcTemplate.update("DELETE FROM NOTES WHERE id=?", new Object[] { noteId });
		if(num==0) {
			throw new DatabaseException();
		}
	}
	
	
	
	@Override
	public void updateNote(NoteModel note) {
		
		
		String sql = "update NOTES set title=?,description=?,lastUpdateDate=?,color=?,status=?,reminder=? where id=?";
		int num = jdbcTemplate.update(sql, new Object[] {note.getTitle(),note.getDescription(),
				note.getLastUpdateDate(),note.getColor(),note.getStatus(),note.getReminder(),note.getId()});
		if( num == 0) {
			throw new DatabaseException();
		}
		
	}

	@Override
	public NoteModel getNoteByNoteId(int noteId) {
		String sql = "select * from NOTES where id=?";
		List<NoteModel> list = jdbcTemplate.query(sql, new Object[] {noteId}, new MyMapperClass());
		
		if(list.size()==0) {
			
			throw new DatabaseException();
		}
	
		return list.size() > 0 ? list.get(0) : null;
	}
	
	@Override
	public List<NoteModel> getNotesByUserId(int userId) {
		String sql = "select * from NOTES where userId=?";
		List<NoteModel> list = jdbcTemplate.query(sql, new Object[] {userId}, new MyMapperClass());
		return list;
	}

	@Override
	public void saveLabel(LabelDTO labelObj) {
		String sql = "insert into LABEL values(?,?,?)";
		int num = jdbcTemplate.update(sql, new Object[] {labelObj.getLabelId(),labelObj.getLabelTitle(),labelObj.getUser().getId()});
		 if(num==0) {
			throw new DatabaseException();
		 }
		
	}

	@Override
	public List<LabelDTO>getLabelsByUserId(int userId) {
		String sql = "select * from LABEL where userId=?";
		List<LabelDTO> list = jdbcTemplate.query(sql, new Object[] {userId}, new MyLabelMapperClass());
		return list;

	}

   @Override
   public List<LabelResDTO> getLabelByNoteId(int noteId)
   {
      String sql = "SELECT LABEL.labelTitle,Note_Label.labelId\n" + 
            "FROM LABEL \n" + 
            "INNER JOIN Note_Label \n" + 
            "ON Note_Label.labelId=LABEL.labelId \n" + 
            "where Note_Label.noteId=?;";
      
      List<LabelResDTO> list = jdbcTemplate.query(sql, new Object[] {noteId}, new GetLabelMapperClass());
      return list.size() > 0 ? list : null;

      
   }

   @Override
   public void addLabelToNote(AddRemoveLabelDTO reqDTO)
   {
      String sql = "insert into Note_Label values(?,?)";
      int num = jdbcTemplate.update(sql, new Object[] {reqDTO.getNoteId(),reqDTO.getLabelId()});
      if(num==0) {
        throw new DatabaseException();
      }
   }

   @Override
   public void removeLabelFromNote(AddRemoveLabelDTO reqDTO)
   {
      int num=jdbcTemplate.update("DELETE FROM Note_Label WHERE noteId=? and labelId=?", new Object[] { reqDTO.getNoteId(),reqDTO.getLabelId() });
      if(num==0) {
         throw new DatabaseException();
      }
      
   }

   @Override
   public void addcollaborator(int id,CollaboratorReqDTO personReqDTO,int userId)
   {
      String sql = "insert into Collaborators values(?,?,?,?)";
      int num = jdbcTemplate.update(sql, new Object[] {personReqDTO.getId(),personReqDTO.getNoteId(),id,userId});
       if(num==0) {
         throw new DatabaseException();
       }
      
   }
   // do here
   @Override
   public List<UserDTO> getCollaborators(int noteId)
   {
      String sql="SELECT USER.fullName,USER.userEmail\n"+
      "FROM USER\n"+ 
      "INNER JOIN Collaborators\n"+ 
      "ON USER.id=Collaborators.sharedUserId\n"+ 
      "where Collaborators.noteId=?;\n";
      
      List<UserDTO> list = jdbcTemplate.query(sql, new Object[] {noteId}, new GetCollaborators());
      return list.size() > 0 ? list : null;
   }

   @Override
   public List<ResCollaboratorDTO> getSharedNoteIDAndUserId(int userId)
   {
      String sql="select * from Collaborators where sharedUserId=?;";
      
      List<ResCollaboratorDTO> list = jdbcTemplate.query(sql, new Object[] {userId}, new GetCollaboratorsObject());
      return list.size() > 0 ? list : null;
   }

   @Override
   public CollaboratorResponseDTO getSharedNotes(int noteId,int userId)
   {
      String sql="SELECT NOTES.title,NOTES.description,USER.fullName\n" + 
            "FROM NOTES,USER  \n" + 
            "where NOTES.id=? and USER.id=? ;";
      
      List<CollaboratorResponseDTO> list = jdbcTemplate.query(sql, new Object[] {noteId,userId}, new GetSharedNotes());
      return list.size() > 0 ? list.get(0) : null;
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
      note.setTitle(rs.getString("title"));
      note.setDescription(rs.getString("description"));
      note.setFullName(rs.getString("fullName"));  
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
class MyMapperClass implements org.springframework.jdbc.core.RowMapper<NoteModel> {
	public NoteModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		NoteModel note = new NoteModel();			
		note.setId(rs.getInt("id"));
		note.setTitle(rs.getString("title"));
		note.setDescription(rs.getString("description"));
		note.setCreateDate(rs.getDate("createDate"));
		note.setLastUpdateDate(rs.getDate("lastUpdateDate"));
		note.setStatus(rs.getInt("status"));
		note.setColor(rs.getString("color"));
		try {
		java.util.Date date=null;
		Timestamp timestamp = rs.getTimestamp("reminder");
		if (timestamp != null)
		    date = new java.util.Date(timestamp.getTime());
			note.setReminder(date);
		}catch(Exception e) {
			e.printStackTrace();
		}	
		int userId = rs.getInt("userId");
		UserModel user = new UserModel();
		user.setId(userId);
		note.setUser(user);
		return note;
	}

}
class MyLabelMapperClass implements org.springframework.jdbc.core.RowMapper<LabelDTO> {
   public LabelDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
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

class GetLabelMapperClass implements org.springframework.jdbc.core.RowMapper<LabelResDTO> {
	public LabelResDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
	   LabelResDTO label = new LabelResDTO();			
		 label.setLabelId(rs.getInt("labelId"));
		 label.setLabelTitle(rs.getString("labelTitle"));
		return label;
	}

}

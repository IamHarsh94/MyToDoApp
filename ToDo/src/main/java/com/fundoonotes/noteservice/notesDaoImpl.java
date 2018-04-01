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
import com.fundoonotes.userservice.UserModel;


public class notesDaoImpl implements NotesDao{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void saveNote(NoteModel note) {
		String INSERT_SQL = "insert into NOTES values(?,?,?,?,?,?,?,?,?)";	
		KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {

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
		
		if(list.size()<=0) {
			System.out.println("in database row not update");
			throw new DatabaseException();
		}
		System.out.println("in db row update");
		return list.get(0);
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

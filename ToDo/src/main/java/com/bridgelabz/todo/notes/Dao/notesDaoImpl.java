package com.bridgelabz.todo.notes.Dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/*import javax.sql.DataSource;*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.bridgelabz.todo.notes.Exception.DatabaseException;
import com.bridgelabz.todo.notes.model.Note;
import com.bridgelabz.todo.user.model.User;


public class notesDaoImpl implements NotesDao{
	/*@Autowired
	private DataSource dataSource;*/
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void saveNote(Note note) {
		String INSERT_SQL = "insert into NOTES values(?,?,?,?,?,?)";	
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
				return ps;
			}
		}, holder);

		int noteId = holder.getKey().intValue();
		note.setId(noteId);
	}

	@Override
	public void deleteNote(int noteId) {
		int num=jdbcTemplate.update("DELETE FROM NOTES WHERE id=?", new Object[] { noteId });
		if(num!=1) {
			throw new DatabaseException();
		}

	}

	@Override
	public void updateNote(Note note) {
		String sql = "update NOTES set title=?,description=?,lastUpdateDate=? where id=?";
		int num = jdbcTemplate.update(sql, new Object[] {note.getTitle(),note.getDescription(),note.getLastUpdateDate(),note.getId()});
		if( num != 1) {
			throw new DatabaseException();
		}
		
	}

	@Override
	public Note getNoteByNoteId(int noteId) {
		String sql = "select * from NOTES where id=?";
		List<Note> list = jdbcTemplate.query(sql, new Object[] {noteId}, new MyMapperClass());
		if(list.size()<=0) {
			throw new DatabaseException();
		}
		return list.get(0);
	}

	@Override
	public List<Note> getNotesByUserId(int userId) {
		String sql = "select * from NOTES where userId=?";
		List<Note> list = jdbcTemplate.query(sql, new Object[] {userId}, new MyMapperClass());
		return list;
	}
}
class MyMapperClass implements org.springframework.jdbc.core.RowMapper<Note> {
	public Note mapRow(ResultSet rs, int rowNum) throws SQLException {
		Note note = new Note();			
		note.setId(rs.getInt("id"));
		note.setTitle(rs.getString("title"));
		note.setDescription(rs.getString("description"));
		note.setCreateDate(rs.getDate("createDate"));
		note.setLastUpdateDate(rs.getDate("lastUpdateDate"));
		
		int userId = rs.getInt("userId");
		User user = new User();
		user.setId(userId);
		note.setUser(user);
		return note;
	}

}

package com.bridgelabz.todo.user.Dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.bridgelabz.todo.user.model.User;

public class UserDaoImpl implements userDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	public boolean save(User user) {
		String sql = "insert into USER values(?,?,?,?,?,?,?,?)";
		int num = jdbcTemplate.update(sql, new Object[] { user.getId(), user.getFullName(), user.getUserEmail(), user.getPassWord(),
				user.getMobileNum(), user.getAddress() ,user.isActive(),user.getUUID()});
		return num != 0;
	}
	public User getUserByEmailId(String UserEmail) {
		String sql = "select * from USER where userEmail=?";
		List<User> list = jdbcTemplate.query(sql, new Object[] { UserEmail}, new MyMapper());
		return list.size()>0? list.get(0):null;
	}
	class MyMapper implements org.springframework.jdbc.core.RowMapper<User> {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getInt("id"));
			user.setFullName(rs.getString("fullName"));
			user.setUserEmail(rs.getString("userEmail"));
			user.setPassWord(rs.getString("passWord"));
			user.setMobileNum(rs.getString("mobileNum"));
			user.setAddress(rs.getString("address"));
			user.setActive(rs.getBoolean("isActive"));
			user.setUUID(rs.getString("UUID"));	
			return user;
		}
	}
	public boolean updateUUID(String email,String randomUUID) {
		String sql = "update USER set UUID=? where userEmail=?";
		int num = jdbcTemplate.update(sql, new Object[] {randomUUID,email});
		return  num != 0;
	}
	@Override
	public String fetchEmailByUUID(String userUUID) {
		String query = "select userEmail from USER where UUID=?";
		Object[] obj = new Object[] {userUUID};
		String userEmail = jdbcTemplate.queryForObject(query, obj, String.class);
		return userEmail;
	}
	@Override
	public boolean updatePassword(String passWord, String email) {
		String sql = "update USER set passWord=? where userEmail=?";
		int num = jdbcTemplate.update(sql, new Object[] {passWord,email});
		return num != 0;
	}
	@Override
	public User getUserByUUID(String userUUID) {
		String sql = "select * from USER where UUID=?";
		List<User> list = jdbcTemplate.query(sql, new Object[] {userUUID}, new MyMapper());
		return list.size()>0? list.get(0):null;
	}
	@Override
	public boolean activateUser(User user) {
		String sql = "update USER set isActive=? where userEmail=?";
		int num = jdbcTemplate.update(sql, new Object[] {user.isActive(),user.getUserEmail()});
		return  num != 0;
	}
	@Override
	public User getUserById(int userId) {
		String sql = "select * from USER where id=?";
		List<User> list = jdbcTemplate.query(sql, new Object[] {userId}, new MyMapper());
		return list.size()>0? list.get(0):null;
	}
}

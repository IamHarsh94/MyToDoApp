package com.fundoonotes.userservice;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.fundoonotes.exception.DatabaseException;
import com.fundoonotes.exception.UserNotExistException;
import com.fundoonotes.noteservice.CollaboratorReqDTO;
import com.fundoonotes.utilservice.DataBaseQueries;

public class UserDaoImpl implements IuserDao
{

   @Autowired
   private JdbcTemplate jdbcTemplate;

   /**
    * This method used to save the user 
    * 
    * @return return true if user save successfully otherwise return false
    * */   
   public boolean save(UserModel user)
   {
      String sql = "insert into USER values(?,?,?,?,?,?,?,?)";
      
      int num = jdbcTemplate.update(sql,
            
            new Object[] { 
           
              user.getId(), user.getFullName(), user.getUserEmail(),user.getPassWord(),
              user.getMobileNum(), user.getAddress(), user.isActive(), user.getUUID() 
            
            });
      
      if(num==0)
      {
         throw new DatabaseException();
      }
      return true;
   }

   /**
    * Fetching the user by user email id
    * 
    * @return return the user if present otherwise return null
    * 
    */
   public UserModel getUserByEmailId(String UserEmail)
   {
      String sql = "select * from USER where userEmail=?";

      List<UserModel> list = jdbcTemplate.query(sql, new Object[] { UserEmail }, new MyMapper());
     
     if(list.size()==0)
      {
        throw new UserNotExistException();
     }
      return list.get(0);
   }

   /**
    * 1.Fetching the rows from db
    * 2.Rowmapper An interface used by JdbcTemplate for mapping rows 
    * of a java.sql.ResultSet on a per-row basis.
    */
   class MyMapper implements org.springframework.jdbc.core.RowMapper<UserModel>
   {
      public UserModel mapRow(ResultSet rs, int rowNum) throws SQLException
      {
         UserModel user = new UserModel();
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

   public boolean updateUUID(String email, String randomUUID)
   {
      String sql = "update USER set UUID=? where userEmail=?";
      
      int num = jdbcTemplate.update(sql, new Object[] { randomUUID, email });
      
      if (num == 0)
      {
         throw new DatabaseException();
      }
      return true;
   }

   @Override
   public String fetchEmailByUUID(String userUUID)
   {
      String query = "select userEmail from USER where UUID=?";
      Object[] obj = new Object[] { userUUID };
      String userEmail = jdbcTemplate.queryForObject(query, obj, String.class);
      return userEmail;
   }

   @Override
   public boolean updatePassword(String passWord, String email)
   {
      String sql = "update USER set passWord=? where userEmail=?";
      int num = jdbcTemplate.update(sql, new Object[] { passWord, email });
      return num != 0;
   }
   
   /**
    * Fetching user by token
    * @param user token
    * 
    * @retun return list if user present otherwise null
    * */
   @Override
   public UserModel getUserByUUID(String userUUID)
   {
      String sql = "select * from USER where UUID=?";
      
      List<UserModel> list = jdbcTemplate.query(sql, new Object[] { userUUID }, new MyMapper());
      
      return list.size() > 0 ? list.get(0) : null;
   }

   /**
    *<p>
    *  Simply update the status by user email id
    *  If status not update then throw my custom exception 
    * </p>   
    * */
   @Override
   public void activateUser(UserModel user)
   {
      String sql = "update USER set isActive=? where userEmail=?";
      
      int num = jdbcTemplate.update(sql, new Object[] { user.isActive(), user.getUserEmail() });
      
      if (num == 0)
      {
         throw new DatabaseException();
      }
   }

   @Override
   public UserModel getUserById(int userId)
   {
      String sql = "select * from USER where id=?";
      List<UserModel> list = jdbcTemplate.query(sql, new Object[] { userId }, new MyMapper());
      return list.size() > 0 ? list.get(0) : null;
   }

   @Override
   public void removeCollaborator(int noteId,int userId)
   {
      int num=jdbcTemplate.update("delete from Collaborators where noteId=? and sharedUserId=?;", new Object[] {noteId,userId });
      
      if(num==0)
      {
         throw new DatabaseException();
      }
      
   }
}

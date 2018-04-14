package com.fundoonotes.userservice;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fundoonotes.noteservice.CollaboratorReqDTO;
import com.fundoonotes.utilservice.MailService;
import com.fundoonotes.utilservice.TokenUtil;
import com.fundoonotes.utilservice.UserValidation;

@Service
public class UserServiceImpl implements IUserService
{
   @Autowired
   private IuserDao userDao;

   @Autowired
   PasswordEncoder passwordEncoder;

   @Autowired
   private MailService userMail;

   @Autowired
   private TokenUtil userToken;

   /**
    * <p>
    * This method used to save the user and send the confirmation link to his
    * mail If user not register successfully then throw the exception
    * </p>
    * 
    */

   @Transactional(rollbackFor = Exception.class)
   public boolean userRegistration(UserDTO DTOuser, String url) throws Exception
   {

      UserModel user = new UserModel(DTOuser.getFullName(), DTOuser.getUserEmail(), DTOuser.getPassWord(),
            DTOuser.getConfirmpassword(), DTOuser.getMobileNum(), DTOuser.getAddress());

      UserModel user1 = userDao.getUserByEmailId(user.getUserEmail());

      if (user1 == null) {

         String hashCode = passwordEncoder.encode(user.getPassWord());

         user.setPassWord(hashCode);

         String randomUUID = UUID.randomUUID().toString();

         user.setUUID(randomUUID);

         boolean isSave = userDao.save(user);

         if (isSave) {
            String URL = url + randomUUID;

            String to = user.getUserEmail();
            String subject = "Registration confirmation link";
            String message = URL;

            boolean isSent = userMail.sendMail(to, subject, message);

            if (isSent) {
               return true;
            }
            return false;
         }
      }
      return false;
   }

   /**
    * This method used to validate user from db by entered email id and password
    * 
    * @param request user DTO
    * @return return jwt if user successfully validate otherwise return null
    */

   @SuppressWarnings("static-access")
   @Override
   public String userLogin(UserDTO DTOuser)
   {
      UserModel user = new UserModel(DTOuser.getUserEmail(), DTOuser.getPassWord());

      UserModel userObj = userDao.getUserByEmailId(user.getUserEmail());
      if (userObj != null && userObj.isActive() && BCrypt.checkpw(user.getPassWord(), userObj.getPassWord())) {

         return userToken.generateToken(userObj.getId());
      }
      return null;
   }

   @Override
   public boolean sendEmail(String userEmail, String requestUrl)
   {

      UserValidation valid = new UserValidation();
      String msg = valid.emailValidate(userEmail);

      if (msg != null) {
         return false;
      } else {
         UserModel user = userDao.getUserByEmailId(userEmail);

         if (user != null) {
            String randomUUID = UUID.randomUUID().toString();
            boolean idUpdate = userDao.updateUUID(userEmail, randomUUID);

            if (idUpdate) {
               String URL = requestUrl + randomUUID;
               String to = userEmail;
               String subject = "Link to reset password";
               String message = URL;
               boolean isSent = userMail.sendMail(to, subject, message);

               if (isSent) {
                  return true;
               }
            }
            return false;
         }
         return false;
      }
   }

   @Override
   public String getEmailByUUID(String userUUID)
   {
      String userEmail = userDao.fetchEmailByUUID(userUUID);

      return userEmail != null ? userEmail : null;
   }

   @Override
   public boolean resetPassword(UserDTO DTOuser)
   {
      UserModel user = new UserModel(DTOuser.getUserEmail(), DTOuser.getPassWord());

      String hashCode = passwordEncoder.encode(user.getPassWord());

      user.setPassWord(hashCode);

      return (userDao.updatePassword(user.getPassWord(), user.getUserEmail()) == true) ? true : false;
   }

   /**
    * <p>
    * *This method use to make status 1 in db (activate user), which is by
    * default 0 when user register
    * 
    * *Getting the user by token from db if have that user then activate him by
    * changing status 0 to 1
    * </p>
    * 
    * @param accept the token( passing from controller)
    * @retun return true if activate successfully otherwise return false
    */
   @Override
   public boolean userActivation(String userUUID)
   {
      UserModel user = userDao.getUserByUUID(userUUID);

      if (user != null) {
         user.setActive(true);
         userDao.activateUser(user);
         return true;
      }
      return false;
   }

   public UserModel fetchUserByUserId(int userId)
   {

      return userDao.getUserById(userId);
   }


}
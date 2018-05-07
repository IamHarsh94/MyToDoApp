package com.fundoonotes.userservice;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.fundoonotes.exception.RegisterErrors;
import com.fundoonotes.utilservice.EmailProperties;
import com.fundoonotes.utilservice.UserValidation;

/**
 * <p>
 * This is a Rest Controller for User With {@RestController}, we have added all
 * general purpose methods here those method will accept a rest request in JSON
 * form and will return a JSON response.
 * </p>
 * 
 * <p>
 * Response Object is used to return JSON as response to incoming request.
 * </p>
 * 
 * @author BridgeLabz
 * 
 */
@RestController
public class UserController
{

   private final org.apache.log4j.Logger LOGGER = LogManager.getLogger(UserController.class);

   private static CustomResponseDTO response = new CustomResponseDTO();

   private static RegisterErrors errorRes = new RegisterErrors();

   @Autowired
   private IUserService userService;

   @Autowired
   private UserValidation validator;

   @Autowired
   private EmailProperties emailService;
   /**
    * <p>
    * This rest API for new user registration with {@RequestMapping} to mapped
    * rest address.
    * </p>
    * 
    * @param Object to be save, should not be null.
    * @return Response Object with HTTP status and message.
    */
   @RequestMapping(value = "register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<?> registrationUser(HttpServletRequest req, @RequestBody UserDTO DTOuser,
         BindingResult BindResult)
   {
      validator.validate(DTOuser, BindResult);

      if (BindResult.hasErrors()) {
         LOGGER.error("User Registration validation error");
         response.setMessage("Enter field properly");
         response.setStatusCode(400);

         return new ResponseEntity<CustomResponseDTO>(response, HttpStatus.CONFLICT);
      }
      try {

         String url = req.getRequestURL().toString();

         String requestUrl = url.substring(0, url.lastIndexOf("/")) + "/registerConfirmation/";

         boolean isRegister = userService.userRegistration(DTOuser, requestUrl);

         if (isRegister) {
            LOGGER.info("User " + DTOuser.getUserEmail() + " is registered successfully");
            response.setMessage("user register success");
            response.setStatusCode(200);

            return new ResponseEntity<CustomResponseDTO>(response, HttpStatus.OK);
         } else {
            LOGGER.info("User registration failed..");
            response.setMessage("registration failed..");
            response.setStatusCode(400);

            return new ResponseEntity<CustomResponseDTO>(errorRes, HttpStatus.CONFLICT);
         }
      } catch (Exception e) {
         LOGGER.error("Error while registering user", e);
         return new ResponseEntity<String>("Server error", HttpStatus.INTERNAL_SERVER_ERROR);
      }
   }

   /**
    * <p>
    * This rest API for user activation through email, get token(UUID) from
    * link. link send to user valid email at the time of registration.
    * </p>
    * 
    * @param token as a UUID(get token from path which sent in mail)
    * @param response HTTP
    * @return redirect to login URL
    */
   @RequestMapping(value = "registerConfirmation/{randomUUID}", method = RequestMethod.POST)
   public ResponseEntity<?> registerConfirmation(@PathVariable("randomUUID") String userUUID)
   {
      boolean isActivate = userService.userActivation(userUUID);

      if (isActivate) {
         response.setMessage("user activation success");
         response.setStatusCode(200);

         return new ResponseEntity<CustomResponseDTO>(response, HttpStatus.OK);
      }

      response.setMessage("user activation fail");
      response.setStatusCode(409);

      return new ResponseEntity<CustomResponseDTO>(response, HttpStatus.CONFLICT);
   }

   /**
    * <p>
    * This is simple login rest API where validate with valid existing user from
    * DB. If user found then give successful response with JWT token. If not
    * found then return appropriate response.
    * </p>
    * 
    * @param user login credential
    * @param resp HttpServletResponse
    * @return ResponseEntity.
    */
   @RequestMapping(value = "login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<?> loginUser(@RequestBody UserDTO DTOuser, HttpServletResponse res)
   {
      String token = userService.userLogin(DTOuser);

      if (token != null) {
         res.setHeader("Authorization", token);

         response.setMessage("User successfully login");
         response.setStatusCode(200);
         
         return new ResponseEntity<CustomResponseDTO>(response, HttpStatus.OK);
      }

      response.setMessage("User login failed");
      response.setStatusCode(400);

      return new ResponseEntity<CustomResponseDTO>(response, HttpStatus.OK);
   }

   /**
    * <p>
    * This rest API used when user "forgot" his password we are sending the
    * resetpassword link to user email id after checking valid email or not.
    * </p>
    * 
    * @param userEmail entered by user
    * @param HttpServletRequest
    * @return ResponseEntity
    */
   @RequestMapping(value = "forgotPassword", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<?> validateEmail(HttpServletRequest req, HttpServletResponse res, @RequestBody UserDTO DTOuser)
   {

      System.out.print("forgot password me host name hai ye :"+req.getHeader("origin"));
      String url = req.getRequestURL().toString();
      String requestUrl = url.substring(0, url.lastIndexOf("/")) + "/resetPassword/";

      boolean isSent = userService.sendEmail(DTOuser.getUserEmail(), requestUrl);

      if (isSent) {
         
         response.setMessage("Mail send successfully to your email id");
         response.setStatusCode(200);

         return new ResponseEntity<CustomResponseDTO>(response, HttpStatus.CREATED);
      }

      response.setMessage("Mail not send");
      response.setStatusCode(409);

      return new ResponseEntity<CustomResponseDTO>(response, HttpStatus.CONFLICT);
   }

   /**
    * <p>
    * This rest API for user resetPassword by email, get token(UUID) from link.
    * link send to user valid email at the time of forgot password.
    * </p>
    * 
    * @param UserDTO object which have new password
    * 
    * @param token as a UUID(get token from path which sent in mail)
    * 
    * @return redirect to login URL
    */
   @RequestMapping(value = "resetPassword/{randomUUID}", method = RequestMethod.GET)
   public void resetPassword( HttpServletResponse res,@PathVariable("randomUUID") String userUUID)
   {
      String email = userService.getEmailByUUID(userUUID);
      
      if (email != null) {
            String hostUrl=emailService.getHost()+"/"+"resetpassword?userUUID="+userUUID;
            System.out.println("hostUrl look like this"+hostUrl);
            try {
               res.sendRedirect(hostUrl);
               
            } catch (IOException e) {
               LOGGER.error("Error while redirecting to reset poge", e);
              
            }   
      }
   }
       
   @RequestMapping(value = "changepassword/{randomUUID}", method = RequestMethod.POST)
   public ResponseEntity<?> changepassword(@PathVariable("randomUUID") String userUUID, @RequestBody UserDTO DTOuser)
   {
      String email = userService.getEmailByUUID(userUUID);
      
           if (email != null) {
      
               DTOuser.setUserEmail(email);
      
             boolean isReset = userService.resetPassword(DTOuser);
      
               if (isReset) {
                  response.setMessage("password reset successfully");
                  response.setStatusCode(200);
    
                  return new ResponseEntity<CustomResponseDTO>(response, HttpStatus.OK);
               }
          }
      
           response.setMessage("password not reset try again");
           response.setStatusCode(409);
     
            return new ResponseEntity<CustomResponseDTO>(response, HttpStatus.OK);
      
   }
   /**
    * <p>
    * This rest API for get the loged user from db according to user id and
    * return loged user
    * </p>
    * 
    * @param user id which already set from user interceptor after validating
    * @return return the current loged user or appropriate response to user
    */
   @RequestMapping(value = "user/getUser/{ownerId}", method = RequestMethod.GET)
   public ResponseEntity<?> getLogedUser(@RequestAttribute(name = "userId") int userId,@PathVariable int ownerId)
   {
      UserModel user = null;
      if(ownerId!=0)
      {
          user = userService.fetchUserByUserId(ownerId);
      }else {
          user = userService.fetchUserByUserId(userId); 
      }
      if (user != null)
      {
         return new ResponseEntity<UserModel>(user, HttpStatus.OK);
      }
      response.setMessage("user not return");
      response.setStatusCode(409);

      return new ResponseEntity<CustomResponseDTO>(response, HttpStatus.CONFLICT);
   }

}
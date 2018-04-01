package com.fundoonotes.userservice;

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
      /**
       * @param BindResult (General interface that represents binding results.
       *           Extends the interface for error registration capabilities)
       * @param DTOuser object having fields
       * @return void
       */
      validator.validate(DTOuser, BindResult);

      /**
       * If any result is binded then return Custom response to the user
       */
      if (BindResult.hasErrors()) {
         LOGGER.error("User Registration validation error");
         response.setMessage("Enter field properly");
         response.setStatusCode(400);
         return new ResponseEntity<CustomResponseDTO>(response, HttpStatus.CONFLICT);
      }
      try {
         /**
          * Get current API URL form request Object
          */
         String url = req.getRequestURL().toString();

         /**
          * Creating the registerConfirmation link with Concatenating current
          * API URL
          */
         String requestUrl = url.substring(0, url.lastIndexOf("/")) + "/registerConfirmation/";

         /**
          * @param DTOuser having user fields
          * @param requestUrl (registerConfirmation link) which we will send to
          *           the user
          * @return return true if successfully register otherwise return false
          */
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
      /**
       * @param userUUID (Getting from path as a path variable)
       * @return return true if successfully activate otherwise return false
       */
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
      /**
       * @param DTOuser having user fields
       * @return return JWT token if successfully login otherwise return null
       */
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
    * This rest API used when user forgot his password we are sending the
    * resetpassword link to user email id after checking valid email or not.
    * </p>
    * 
    * @param userEmail entered by user
    * @param HttpServletRequest
    * @return ResponseEntity
    */
   @RequestMapping(value = "sendEmail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<?> validateEmail(HttpServletRequest req, HttpServletResponse res, @RequestBody UserDTO DTOuser)
   {
      /**
       * Get current API URL form request Object
       */
      String url = req.getRequestURL().toString();

      /**
       * Creating the resetPassword link with Concatenating current API URL
       */
      String requestUrl = url.substring(0, url.lastIndexOf("/")) + "/resetPassword/";

      /**
       * @param DTOuser object having user email id
       * @param requestUrl (resetPassword link) which we will send to the user
       * @return return true if successfully sent otherwise return false
       */
      boolean isSent = userService.sendEmail(DTOuser.getUserEmail(), requestUrl);

      if (isSent) {
         response.setMessage("Mail send successfully");
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
   @RequestMapping(value = "resetPassword/{randomUUID}", method = RequestMethod.POST)
   public ResponseEntity<?> resetPassword(@PathVariable("randomUUID") String userUUID, @RequestBody UserDTO DTOuser)
   {
      /**
       * Fetching the user email by token id(UUID)
       * 
       * @return return user email id if present otherwise return null
       */
      String email = userService.getEmailByUUID(userUUID);

      if (email != null) {
         /**
          * Setting the user email in DTOuser object which we fetch from db
          */
         DTOuser.setUserEmail(email);

         /**
          * @param DTOuser object having user email id and new password
          * 
          * @return return true if successfully reset the password otherwise
          *         return false
          */
         boolean isReset = userService.resetPassword(DTOuser);

         if (isReset) {
            response.setMessage("password reset successfully");
            response.setStatusCode(200);
            return new ResponseEntity<CustomResponseDTO>(response, HttpStatus.OK);
         }
      }

      response.setMessage("password not reset try again");
      response.setStatusCode(409);
      return new ResponseEntity<CustomResponseDTO>(response, HttpStatus.CONFLICT);
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
   @RequestMapping(value = "user/getUser", method = RequestMethod.GET)
   public ResponseEntity<?> getLogedUser(@RequestAttribute(name = "userId") int userId)
   {
      /**
       * Fetching the user from db with help of user id(request attribute)
       * 
       * @param user id (request attribute)
       */
      UserModel user = userService.fetchUserByUserId(userId);

      /**
       * If user not null then we simply return the user
       */
      if (user != null) {
         return new ResponseEntity<UserModel>(user, HttpStatus.OK);
      }

      response.setMessage("user not return");
      response.setStatusCode(409);
      return new ResponseEntity<CustomResponseDTO>(response, HttpStatus.CONFLICT);
   }

}
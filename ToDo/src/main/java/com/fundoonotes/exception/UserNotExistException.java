package com.fundoonotes.exception;

import com.fundoonotes.userservice.CustomResponseDTO;

public class UserNotExistException extends RuntimeException
{
   private static final long serialVersionUID = 1L;

   public UserNotExistException () {
      super("User not exist in Database");
   }

   public CustomResponseDTO getResponse() {
      CustomResponseDTO response = new CustomResponseDTO();
      response.setMessage(this.getMessage());
      response.setStatusCode(-1);
      return response;
   }
}

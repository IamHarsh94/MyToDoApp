package com.fundoonotes.utilservice;

public class DataBaseQueries
{

   private final static String saveNoteQuery="insert into NOTES values(?,?,?,?,?,?,?,?,?)";
   
   private final static String selectNoteByNoteIdQuery="select * from NOTES where id=?";
   
   private final static String selectNoteByUserIdQuery="select * from NOTES where userId=?";
   
   private final static String deleteCollaboratorQuery="DELETE FROM Collaborators WHERE noteId=?";
   
   private final static String deleteNoteQuery="DELETE FROM NOTES WHERE id=?";
   
   private final static String updateNoteQuery="update NOTES set title=?,description=?,lastUpdateDate=?,color=?,status=?,reminder=? where id=?";
   
   private final static String saveLabelQuery="insert into LABEL values(?,?,?)";
  
   private final static String labelAndNote_labelJoinQuery="SELECT LABEL.labelTitle,Note_Label.labelId FROM LABEL INNER JOIN Note_Label ON Note_Label.labelId=LABEL.labelId where Note_Label.noteId=?;";
   
   private final static String saveNote_label="insert into Note_Label values(?,?)";
   
   private final static String deleteFromNote_labelQuery="DELETE FROM Note_Label WHERE noteId=? and labelId=?";
   
   private final static String insertCollaboratorQuery="insert into Collaborators values(?,?,?,?)";
   
   private final static String getCollaboratorByUserAndCollaboratorjoin="SELECT USER.fullName,USER.userEmail FROM USER INNER JOIN Collaborators ON USER.id=Collaborators.sharedUserId where Collaborators.noteId=?;";
   
   private final static String selectFromCollaboratorByUserId="select * from Collaborators where sharedUserId=?;";
   
   private final static String selectFromNoteAndUser="SELECT NOTES.id,NOTES.title,NOTES.description,NOTES.color,USER.fullName FROM NOTES,USER where NOTES.id=? and USER.id=?;";

   private final static String getLabelByuserIdQuery="select * from LABEL where userId=?";
   
   private final static String selectFromCollaboratorByUserIdAndNoteId="select * from Collaborators where sharedUserId=? and noteId=?;";
   
   public static String getSelectfromcollaboratorbyuseridandnoteid()
   {
      return selectFromCollaboratorByUserIdAndNoteId;
   }

   public static String getGetlabelbyuseridquery()
   {
      return getLabelByuserIdQuery;
   }

   public static String getSelectfromnoteanduser()
   {
      return selectFromNoteAndUser;
   }

   public static String getSelectfromcollaboratorbyuserid()
   {
      return selectFromCollaboratorByUserId;
   }

   public static String getGetcollaboratorbyuserandcollaboratorjoin()
   {
      return getCollaboratorByUserAndCollaboratorjoin;
   }

   public static String getInsertcollaboratorquery()
   {
      return insertCollaboratorQuery;
   }

   public static String getDeletefromnoteLabelquery()
   {
      return deleteFromNote_labelQuery;
   }

   public static String getSavenoteLabel()
   {
      return saveNote_label;
   }

   public static String getLabelandnoteLabeljoinquery()
   {
      return labelAndNote_labelJoinQuery;
   }

   public static String getSavelabelquery()
   {
      return saveLabelQuery;
   }

   public static String getSelectnotebyuseridquery()
   {
      return selectNoteByUserIdQuery;
   }

   public static String getSelectnotebynoteidquery()
   {
      return selectNoteByNoteIdQuery;
   }

   public static String getUpdatenotequery()
   {
      return updateNoteQuery;
   }

   public static String getDeletenotequery()
   {
      return deleteNoteQuery;
   }

   public static String getSavenotequery()
   {
      return saveNoteQuery;
   }

   public static String getDeletecollaboratorquery()
   {
      return deleteCollaboratorQuery;
   }
      
}

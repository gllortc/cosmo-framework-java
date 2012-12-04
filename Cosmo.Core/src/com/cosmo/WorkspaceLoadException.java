package com.cosmo;

/**
 * Indica que se ha producido un error durante el cargado o la recuperación del Workspace.
 * 
 * @author Gerard Llort
 */
public class WorkspaceLoadException extends Exception 
{
   /** Serial Version UID */
   private static final long serialVersionUID = 4236007126103262540L;

   private Exception innerException;
   
   public WorkspaceLoadException()
   {
      super();
      this.innerException = null;
   }
   
   public WorkspaceLoadException(String message, Exception innerException)
   {
      super(message);
      this.innerException = innerException;
   }
   
   public Exception getInnerException() 
   {
      return innerException;
   }

   public void setInnerException(Exception innerException) 
   {
      this.innerException = innerException;
   }
}

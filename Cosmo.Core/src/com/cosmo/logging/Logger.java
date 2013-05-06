package com.cosmo.logging;

import com.cosmo.Workspace;

public class Logger 
{
   private static Logger instance = null;
   private static Workspace workspace = null;

   protected Logger(Workspace currentWorkspace) 
   {
      workspace = currentWorkspace;
   }
   
   public static Logger getInstance(Workspace workspace) 
   {
      if (instance == null) 
      {
         instance = new Logger(workspace);
      }

      return instance;
   }
}

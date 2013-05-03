package com.cosmo.security;

public class Permission extends Activity
{
   private boolean isGranted;
   
   public Permission()
   {
      super();
      
      this.isGranted = false;
   }
   
   public Permission(Activity activity, boolean isGranted)
   {
      super();
      
      this.setId(activity.getId());
      this.setDescription(activity.getDescription());
      this.setGrantedByDefault(activity.isGrantedByDefault());
      this.setEnabled(activity.isEnabled());
      this.isGranted = isGranted;
   }

   public boolean isGranted() 
   {
      return isGranted;
   }

   public void setGranted(boolean isGranted) 
   {
      this.isGranted = isGranted;
   }
   
}

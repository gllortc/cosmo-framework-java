package com.cosmo.security;

public class ActivityPermission extends Activity
{
   private boolean isGranted;
   
   public ActivityPermission()
   {
      super();
      
      this.isGranted = false;
   }
   
   public ActivityPermission(Activity activity, boolean isGranted)
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

package com.cosmo.web.sample;

import java.util.Date;

import com.cosmo.orm.annotations.CormFieldSetter;
import com.cosmo.orm.annotations.CormForeignKey;
import com.cosmo.orm.annotations.CormObject;
import com.cosmo.orm.annotations.CormObjectField;
import com.cosmo.ui.controls.FormFieldDate;
import com.cosmo.ui.controls.FormFieldInteger;
import com.cosmo.ui.controls.FormFieldList;
import com.cosmo.ui.controls.FormFieldText;
import com.cosmo.ui.controls.FormFieldTextArea;

@CormObject(formName = "frmTravel", dbTable = "travel", title = "Viajes", description = "Gesti�n de excursiones.")
public class Travel
{
   private Integer id;
   private String  name;
   private String  description;
   private String  program;
   private Date    date_ini;
   private Date    date_end;
   private String  organizer;
   private Integer status;

   // ==============================================
   // Constructors
   // ==============================================

   /**
    * Constructor de la clase.
    */
   public Travel()
   {
      this.id = 0;
      this.name = "";
      this.description = "";
      this.program = "";
      this.date_ini = null;
      this.date_end = null;
      this.organizer = "";
      this.status = 0;
   }

   // ==============================================
   // Properties
   // ==============================================

   @CormObjectField(fieldClass = FormFieldInteger.class, dbTableColumn = "id", label = "C�digo", showInObjectListGrid = true, isPrimaryKey = true, isAutogenerated = true)
   public Integer getId()
   {
      return id;
   }

   @CormFieldSetter(dbTableColumn = "id")
   public void setId(Integer id)
   {
      this.id = id;
   }

   @CormObjectField(fieldClass = FormFieldText.class, dbTableColumn = "name", label = "T�tulo", showInObjectListGrid = true)
   public String getName()
   {
      return name;
   }

   @CormFieldSetter(dbTableColumn = "name")
   public void setName(String name)
   {
      this.name = name;
   }

   @CormObjectField(fieldClass = FormFieldTextArea.class, dbTableColumn = "description", label = "Descripci�n")
   public String getDescription()
   {
      return description;
   }

   @CormFieldSetter(dbTableColumn = "description")
   public void setDescription(String description)
   {
      this.description = description;
   }

   @CormObjectField(fieldClass = FormFieldTextArea.class, dbTableColumn = "program", label = "Programa de actos")
   public String getProgram()
   {
      return program;
   }

   @CormFieldSetter(dbTableColumn = "program")
   public void setProgram(String program)
   {
      this.program = program;
   }

   @CormObjectField(fieldClass = FormFieldDate.class, dbTableColumn = "date_ini", label = "Fecha inicio", showInObjectListGrid = true)
   public Date getDate_ini()
   {
      return date_ini;
   }

   @CormFieldSetter(dbTableColumn = "date_ini")
   public void setDate_ini(Date date_ini)
   {
      this.date_ini = date_ini;
   }

   @CormObjectField(fieldClass = FormFieldDate.class, dbTableColumn = "date_end", label = "Fecha final", showInObjectListGrid = true)
   public Date getDate_end()
   {
      return date_end;
   }

   @CormFieldSetter(dbTableColumn = "date_end")
   public void setDate_end(Date date_end)
   {
      this.date_end = date_end;
   }

   @CormObjectField(fieldClass = FormFieldList.class, list = "users", dbTableColumn = "organizer", label = "Organizador", showInObjectListGrid = true)
   @CormForeignKey(dbTableName = "cosmo_users", dbTableField = "usrlogin")
   public String getOrganizer()
   {
      return organizer;
   }

   @CormFieldSetter(dbTableColumn = "organizer")
   public void setOrganizer(String organizer)
   {
      this.organizer = organizer;
   }

   @CormObjectField(fieldClass = FormFieldList.class, list = "travelStatus", dbTableColumn = "status", label = "Estado", showInObjectListGrid = true)
   public Integer getStatus()
   {
      return status;
   }

   @CormFieldSetter(dbTableColumn = "status")
   public void setStatus(Integer status)
   {
      this.status = status;
   }
}

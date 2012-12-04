package com.cosmo.ui.controls;

import com.cosmo.Cosmo;
import com.cosmo.ui.templates.Template;
import com.cosmo.ui.templates.TemplateControl;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;

/**
 * Implementa un formulario representable en una página de Cosmo.
 * 
 * @author Gerard Llort
 */
public class FormControl extends Control 
{
   private static final String CONTROL_ID = "CosmoUiCtrlForm";
   
   private String title;
   private String description;
   private String name;
   private String actionUrl;
   private boolean hasCaptcha;
   private ArrayList<FormFieldGroup> groups;
   private ArrayList<FormFieldHidden> hidden;
   private ArrayList<FormButton> buttons;
   
   //==============================================
   // Contructors
   //==============================================
   
   /**
    * Contructor de la clase.
    */
   public FormControl()
   {
      initialize();
   }

   //==============================================
   // Properties
   //==============================================
   
   /**
    * Devuelve un identificador único del tipo de control.
    */
   @Override
   public String getControlTypeId() 
   {
      return FormControl.CONTROL_ID;
   }

   public String getTitle() 
   {
      return title;
   }

   public void setTitle(String title) 
   {
      this.title = title;
   }

   public String getDescription() 
   {
      return description;
   }

   public void setDescription(String description) 
   {
      this.description = description;
   }

   public String getName() 
   {
      return name;
   }

   public void setName(String name) 
   {
      this.name = name;
   }

   public String getActionUrl() 
   {
      return actionUrl;
   }

   public void setActionUrl(String actionUrl) 
   {
      this.actionUrl = actionUrl;
   }
   
   /**
    * Indica si el formulario contiene un campo CAPTCHA.
    */
   public boolean haveCaptcha()
   {
      return this.hasCaptcha;
   }
   
   //==============================================
   // Methods
   //==============================================
   
   /**
    * Agrega un nuevo grupo de campos al formulario.
    * TODO: Es necesario poner aquí esta EXCEPCIÓN o es mejor trasladarla al RENDER???
    * 
    * @param field Una instancia de {@link FormFieldGroup} que representa el grupo y que contiene los campos.
    */
   public void addGroup(FormFieldGroup group)
   {
      /*
      // Evita que haya más de un CAPTCHA en el formulario
      if (group.haveCaptcha() && this.haveCaptcha())
      {
         throw new MalformedFormException("Only ONE captcha field is allowen in a form.");
      }
      */
      
      // Agrega el grupo al formulario
      groups.add(group);
   }
   
   /**
    * Agrega un campo oculto al formulario.
    * 
    * @param field Una instancia de {@link FormFieldHidden} que contiene los datos del campo.
    */
   public void addHiddenValue(FormFieldHidden field)
   {
      hidden.add(field);
   }
   
   /**
    * Agrega un botón de control al formulario.
    * 
    * @param button Una instancia de {@link FormButton} que contiene los datos del botón.
    */
   public void addButton(FormButton button)
   {
      buttons.add(button);
   }
   
   /**
    * Actualiza el valor de un campo del formulario.
    * 
    * @param name Nombre (único) del campo.
    * @param value Valor a establecer.
    */
   public void setFieldValue(String name, String value)
   {
      FormField field;
      
      for (FormFieldGroup group : this.groups)
      {
         Iterator<FormField> it = group.getFields();
         while (it.hasNext())
         {
            field = it.next();
            if (field.getName().equals(name))
            {
               field.setValue(value);
               return;
            }
         }
      }   
   }
   
   /**
    * Actualiza el valor de un campo del formulario.
    * 
    * @param name Nombre (único) del campo.
    * @param value Valor a establecer.
    */
   public void setFormValues(HttpServletRequest request)
   {
      FormField field;
      
      for (FormFieldGroup group : this.groups)
      {
         Iterator<FormField> it = group.getFields();
         while (it.hasNext())
         {
            field = it.next();
            field.setValue(request.getParameter(field.getName()));
         }
      }   
   }
   
   private static final String CPART_HEADER = "form-head";
   private static final String CPART_GROUP_HEADER = "form-fieldset-head";
   private static final String CPART_FIELD_CONTROL = "form-control";
   private static final String CPART_GROUP_FOOTER = "form-fieldset-footer";
   private static final String CPART_FOOTER = "form-footer";
   private static final String CPART_BUTTONS = "form-buttons";
         
   private static final String TAG_TITLE = "FTITLE";
   private static final String TAG_DESCRIPTION = "FDESC";
   private static final String TAG_CONTROL = "FCONTROL";
   private static final String TAG_LABEL = "FLABEL";
   private static final String TAG_BUTTONS = "FBUTTONS";
   private static final String TAG_FORM_OPEN = "FTAG";
   private static final String TAG_FORM_CLOSE = "FENDTAG";
   
   /**
    * Renderiza el control y genera el código XHTML de representación.
    *
    * @param template Una instancia de {@link Template} que representa la plantilla a aplicar.
    * @return Devuelve una cadena en formato XHTML que representa el control.
    */
   @Override
   public String render(Template template)
   {
      String xhtml;
      String xitem;
      FormField field;
      TemplateControl ctrl;
      Iterator<FormField> it;
      
      // Si no tiene elementos, no representa el control
      if (groups.isEmpty())
      {
         return "<-- FormControl placeholder (void) -->\n";
      }
      
      // Obtiene la plantilla y la parte del control
      ctrl = template.getControl(FormControl.CONTROL_ID);

      // Genera la cabecera del formulario
      xhtml = "";
      
      xitem = ctrl.getElement(CPART_HEADER);
      xitem = Control.replaceTag(xitem, TAG_TITLE, this.getTitle());
      xitem = Control.replaceTag(xitem, TAG_DESCRIPTION, this.getDescription());
      xitem = Control.replaceTag(xitem, TAG_FORM_OPEN, "<form id=\"" + this.name + "\" name=\"" + this.name + "\" action=\"" + this.actionUrl + "\" method=\"post\">");
      xhtml += xitem;

      for (FormFieldHidden hfield : this.hidden)
      {
         xhtml += hfield.render() + "\n";
      }
      
      for (FormFieldGroup group : this.groups)
      {
         xitem = ctrl.getElement(CPART_GROUP_HEADER);
         xitem = Control.replaceTag(xitem, TAG_TITLE, group.getTitle());
         xitem = Control.replaceTag(xitem, TAG_DESCRIPTION, group.getDescription());
         xhtml += xitem;
         
         it = group.getFields();
         while (it.hasNext())
         {
            field = it.next();
            
            if (field instanceof FormFieldText)
            {
               xitem = ctrl.getElement(CPART_FIELD_CONTROL);
               xitem = Control.replaceTag(xitem, TAG_CONTROL, field.render());
               xitem = Control.replaceTag(xitem, TAG_LABEL, ((FormFieldText) field).getLabel());
               xitem = Control.replaceTag(xitem, TAG_DESCRIPTION, ((FormFieldText) field).getDescription());
               xhtml += xitem;
            }
         }
         
         xitem = ctrl.getElement(CPART_GROUP_FOOTER);
         xhtml += xitem;
      }
      
      xitem = ctrl.getElement(CPART_BUTTONS);
      xitem = Control.replaceTag(xitem, TAG_BUTTONS, getButtonsXhtml());
      xhtml += xitem;
      
      xitem = ctrl.getElement(CPART_FOOTER);
      xitem = Control.replaceTag(xitem, TAG_FORM_CLOSE, "</form>");
      xhtml += xitem;
      
      return xhtml;
   }
   
   /**
    * Convierte la instancia en una cadena de texto.
    */
   @Override
   public String toString()
   {
      StringBuilder str = new StringBuilder();

      str.append("<div id=\"").append(this.getControlId()).append("\">").append("\n");
      str.append("  <h2>").append(this.title).append("</h2>").append("\n");
      if (!this.title.equals(""))
      {
         str.append("  <p>").append(this.description).append("</p>").append("\n");
      }
      str.append("  <form id=\"").append(this.name).append("\" name=\"").append(this.name).append("\" action=\"").append(this.actionUrl).append("\" method=\"post\">").append("\n");
      
      for (FormFieldHidden field : this.hidden)
      {
         str.append(field.render()).append("\n");
      }
      for (FormFieldGroup group : this.groups)
      {
         str.append(group.render()).append("\n");
      }
      if (!this.buttons.isEmpty()) 
      {
         str.append("    <div id=\"").append(this.getControlId()).append("btn\">").append("\n");
         for (FormButton button : this.buttons)
         {
            str.append(button.render()).append("\n");
         }
         str.append("    </div>").append("\n");
      }
      str.append("  </form>").append("\n");
      str.append("</div>").append("\n");
      
      return str.toString();
   }
   
   //==============================================
   // Private members
   //==============================================
   
   /**
    * Inicializa la instancia.
    */
   private void initialize()
   {
      this.title = "";
      this.description = "";
      this.name = "";
      this.actionUrl = "";
      this.hasCaptcha = false;
      
      this.groups = new ArrayList<FormFieldGroup>();
      this.hidden = new ArrayList<FormFieldHidden>();
      this.buttons = new ArrayList<FormButton>();
      
      // Agrega el control de envios
      this.hidden.add(new FormFieldHidden(Cosmo.KEY_UI_FORM_ACTION, Cosmo.TOKEN_UI_FORM_POSTBACK));
   }
   
   /**
    * Obtiene el código XHTML correspondiente a los botones del formulario.
    */
   private String getButtonsXhtml()
   {
      String btns = "";
      for (FormButton button : this.buttons)
      {
         btns += button.render() + "&nbsp;&nbsp;"; 
      }
      return btns;
   }
}

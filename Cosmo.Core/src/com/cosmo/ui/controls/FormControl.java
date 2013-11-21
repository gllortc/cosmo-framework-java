package com.cosmo.ui.controls;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.cosmo.Cosmo;
import com.cosmo.Workspace;
import com.cosmo.data.orm.InvalidMappingException;
import com.cosmo.data.orm.OrmFactory;
import com.cosmo.data.orm.annotations.CormObjectField;
import com.cosmo.data.orm.annotations.CormObject;
import com.cosmo.structures.FormData;
import com.cosmo.ui.controls.FormButton.ButtonType;
import com.cosmo.ui.templates.TemplateControl;

/**
 * Implementa un formulario representable en una página de Cosmo.
 * 
 * @author Gerard Llort
 */
public class FormControl extends Control 
{
   private static final String CONTROL_ID = "CosmoUiCtrlForm";

   private static final String CPART_HEADER = "form-head";
   private static final String CPART_GROUP_HEADER = "form-fieldset-head";
   private static final String CPART_FIELD_CONTROL = "form-control";
   private static final String CPART_FIELD_OPTION = "form-option";
   private static final String CPART_FIELD_TEXTAREA = "form-textarea";
   private static final String CPART_GROUP_FOOTER = "form-fieldset-footer";
   private static final String CPART_FOOTER = "form-footer";
   private static final String CPART_BUTTONS = "form-buttons";

   private static final String TAG_TITLE = "FTITLE";
   private static final String TAG_DESCRIPTION = "FDESC";
   private static final String TAG_CONTROL = "FCONTROL";
   private static final String TAG_CONTROL_NAME = "CTRL-NAME";
   private static final String TAG_LABEL = "FLABEL";
   private static final String TAG_BUTTONS = "FBUTTONS";
   private static final String TAG_FORM_NAME = "FNAME";
   private static final String TAG_FORM_METHOD = "FMETHOD";

   private String title;
   private String description;
   private String name;
   private String actionUrl;
   private boolean hasCaptcha;
   private ArrayList<FormFieldset> groups;
   private ArrayList<FormFieldHidden> hidden;
   private ArrayList<FormButton> buttons;


   //==============================================
   // Contructors
   //==============================================

   /**
    * Contructor de la clase.
    * 
    * @param id Identificador único del formulario en toda la aplicación.
    */
   public FormControl(Workspace workspace, String id)
   {
      super(workspace, id);
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
    * 
    * @param group Una instancia de {@link FormFieldset} que representa el grupo y que contiene los campos.
    */
   public void addGroup(FormFieldset group)
   {
      // Agrega el grupo al formulario
      groups.add(group);
   }

   /**
    * Agrega un nuevo grupo de campos al formulario a partir de un clase Cosmo ORM.
    * 
    * @param ormClass Una instancia de un objeto Cosmo ORM (CORM).
    * 
    * @throws InvalidMappingException 
    */
   public void addGroup(Class<?> ormClass) throws InvalidMappingException
   {
      CormObject ct;
      CormObjectField cfg;
      FormFieldset group;

      // Comprueba que sea un objeto CORM
      ct = ormClass.getAnnotation(CormObject.class);
      if (ct == null)
      {
         throw new InvalidMappingException("No CormObject annotation detected on POJO class.");
      }

      // Obtiene las propiedades de la clase y las mapea al formulario
      this.name = ct.formName();

      // Obtiene la lista de campos y los mapea a un grupo
      group = new FormFieldset("");
      group.setTitle(ct.title());
      group.setDescription(ct.description());
      for (Method method : ormClass.getMethods())
      {
         cfg = method.getAnnotation(CormObjectField.class);

         if (cfg != null && !cfg.isAutogenerated())
         {
            if (cfg.fieldClass() == FormFieldText.class)
            {
               group.addField(new FormFieldText(cfg.dbTableColumn(), cfg.label()));
            }
            else if (cfg.fieldClass() == FormFieldTextArea.class)
            {
               group.addField(new FormFieldTextArea(cfg.dbTableColumn(), cfg.label()));
            }
            else if (cfg.fieldClass() == FormFieldInteger.class)
            {
               group.addField(new FormFieldInteger(cfg.dbTableColumn(), cfg.label()));
            }
            else if (cfg.fieldClass() == FormFieldNumber.class)
            {
               group.addField(new FormFieldNumber(cfg.dbTableColumn(), cfg.label()));
            }
            else if (cfg.fieldClass() == FormFieldDate.class)
            {
               group.addField(new FormFieldDate(cfg.dbTableColumn(), cfg.label()));
            }
            else if (cfg.fieldClass() == FormFieldBoolean.class)
            {
               group.addField(new FormFieldBoolean(cfg.dbTableColumn(), cfg.label()));
            }
            else if (cfg.fieldClass() == FormFieldList.class)
            {
               group.addField(new FormFieldList(cfg.dbTableColumn(), cfg.label(), getWorkspace().getProperties().getDataProperties().getDataList(cfg.list())));
            }
            else if (cfg.fieldClass() == FormFieldCaptcha.class)
            {
               group.addField(new FormFieldCaptcha(cfg.dbTableColumn(), cfg.label()));
            }
         }
      }
      this.addGroup(group);

      // Agrega un botón de envio de datos
      this.addButton(new FormButton("cmdAcceopt", "Enviar", ButtonType.Submit));
   }

   /**
    * Agrega un nuevo grupo de campos al formulario a partir de un clase Cosmo ORM.
    * 
    * @param ormClass Una instancia de un objeto Cosmo ORM (CORM).
    * 
    * @throws InvalidMappingException 
    * @throws InvocationTargetException 
    * @throws IllegalAccessException 
    * @throws IllegalArgumentException 
    */
   public void addGroup(Object data) throws InvalidMappingException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
   {
      CormObject ct;
      CormObjectField cfg;
      FormFieldset group;

      // Comprueba si el objeto proporcionado es un objeto CORM válido
      if (!OrmFactory.isValidCormObject(data.getClass()))
      {
         throw new InvalidMappingException(data.getClass() + " is not a CORM object.");
      }

      // Obtiene las propiedades de la clase y las mapea al formulario
      ct = data.getClass().getAnnotation(CormObject.class);
      this.name = ct.formName();

      // Obtiene la lista de campos y los mapea a un grupo
      group = new FormFieldset("");
      group.setTitle(ct.title());
      group.setDescription(ct.description());
      for (Method method : data.getClass().getMethods())
      {
         cfg = method.getAnnotation(CormObjectField.class);

         if (cfg != null && !cfg.isAutogenerated())
         {
            if (cfg.fieldClass() == FormFieldText.class)
            {
               FormFieldText fld = new FormFieldText(cfg.dbTableColumn(), cfg.label());
               fld.setValue(method.invoke(data));
               group.addField(fld);
            }
            else if (cfg.fieldClass() == FormFieldTextArea.class)
            {
               FormFieldTextArea fld = new FormFieldTextArea(cfg.dbTableColumn(), cfg.label());
               fld.setValue(method.invoke(data));
               group.addField(fld);
            }
            else if (cfg.fieldClass() == FormFieldInteger.class)
            {
               FormFieldInteger fld = new FormFieldInteger(cfg.dbTableColumn(), cfg.label());
               fld.setValue(method.invoke(data));
               group.addField(fld);
            }
            else if (cfg.fieldClass() == FormFieldNumber.class)
            {
               FormFieldNumber fld = new FormFieldNumber(cfg.dbTableColumn(), cfg.label());
               fld.setValue(method.invoke(data));
               group.addField(fld);
            }
            else if (cfg.fieldClass() == FormFieldList.class)
            {
               FormFieldList fld = new FormFieldList(cfg.dbTableColumn(), cfg.label());
               fld.setList(getWorkspace().getProperties().getDataProperties().getDataList(cfg.list()));
               fld.setValue(method.invoke(data));
               group.addField(fld);
            }
            else if (cfg.fieldClass() == FormFieldBoolean.class)
            {
               FormFieldBoolean fld = new FormFieldBoolean(cfg.dbTableColumn(), cfg.label());
               fld.setValue(method.invoke(data));
               group.addField(fld);
            }
            else if (cfg.fieldClass() == FormFieldDate.class)
            {
               FormFieldDate fld = new FormFieldDate(cfg.dbTableColumn(), cfg.label());
               fld.setValue(method.invoke(data));
               group.addField(fld);
            }
            else if (cfg.fieldClass() == FormFieldCaptcha.class)
            {
               FormFieldCaptcha fld = new FormFieldCaptcha(cfg.dbTableColumn(), cfg.label());
               fld.setValue(method.invoke(data));
               group.addField(fld);
            }
         }
      }
      this.addGroup(group);

      // Agrega un botón de envio de datos
      this.addButton(new FormButton("cmdAcceopt", "Enviar", ButtonType.Submit));
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
    * 
    * @throws FieldNotFoundException 
    */
   public void setFieldValue(HttpServletRequest request, String name, String value) throws FieldNotFoundException
   {
      FormField field;
      FormData data = null;

      // Obtiene el contenedor de valores
      data = (FormData) request.getSession().getAttribute(this.getSessionControlDataKey());

      // Si no estaba definido, lo crea
      if (data == null)
      {
         data = new FormData(this.getId());
      }

      // Almacena el valor y se asegura que el campo exista
      for (FormFieldset group : this.groups)
      {
         Iterator<FormField> it = group.getFields();
         while (it.hasNext())
         {
            field = it.next();
            if (field.getName().equals(name))
            {
               data.addParameterValue(name, value);
               return;
            }
         }
      }

      // Si no ha encontrado el campo, lanza excepción
      throw new FieldNotFoundException();
   }

   /**
    * Almacena los valores del formulario en la sesión del usuario.
    * 
    * @param request Una instancia de {@link HttpServletRequest} que contiene el contexto de la llamada.
    * 
    * @return Una instancia de {@link FormData} que contiene los datos del formulario.
    */
   public FormData setFormValues(HttpServletRequest request)
   {
      FormField field;
      FormData data = new FormData(this.getId());

      for (FormFieldset group : this.groups)
      {
         Iterator<FormField> it = group.getFields();
         while (it.hasNext())
         {
            field = it.next();
            data.addParameterValue(field.getName(), request.getParameter(field.getName()));
         }
      }

      // Almacena los valores en la sessión
      request.getSession().setAttribute(this.getSessionControlDataKey(), data);

      return data;
   }

   /**
    * Renderiza el control y genera el código XHTML de representación.
    *
    * @return Devuelve una cadena en formato XHTML que representa el control. 
    */
   @Override
   public String render()
   {
      String xhtml;
      String xitem;
      FormField field;
      TemplateControl ctrl;
      Iterator<FormField> it;

      // Si no tiene grupos, no representa el control
      if (groups.isEmpty())
      {
         return "<-- FormControl placeholder (void) -->\n";
      }

      // Obtiene la plantilla y la parte del control
      ctrl = getWorkspace().getTemplate().getControl(FormControl.CONTROL_ID);

      // Genera la cabecera del formulario
      xhtml = "";

      xitem = ctrl.getElement(CPART_HEADER);
      xitem = Control.replaceTag(xitem, TAG_TITLE, this.getTitle());
      xitem = Control.replaceTag(xitem, TAG_DESCRIPTION, this.getDescription());
      xitem = Control.replaceTag(xitem, TAG_FORM_NAME, this.getName());
      xitem = Control.replaceTag(xitem, TAG_FORM_METHOD, "POST");
      xhtml += xitem;

      for (FormFieldHidden hfield : this.hidden)
      {
         xhtml += hfield.render(getWorkspace()) + "\n";
      }

      for (FormFieldset group : this.groups)
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
               xitem = Control.replaceTag(xitem, TAG_CONTROL, field.render(getWorkspace()));
               xitem = Control.replaceTag(xitem, TAG_LABEL, ((FormFieldText) field).getLabel());
               xitem = Control.replaceTag(xitem, TAG_DESCRIPTION, ((FormFieldText) field).getDescription());
               xitem = Control.replaceTag(xitem, TAG_CONTROL_NAME, ((FormFieldText) field).getName());
               xhtml += xitem;
            }
            else if (field instanceof FormFieldTextArea)
            {
               xitem = ctrl.getElement(CPART_FIELD_TEXTAREA);
               xitem = Control.replaceTag(xitem, TAG_CONTROL, field.render(getWorkspace()));
               xitem = Control.replaceTag(xitem, TAG_LABEL, ((FormFieldTextArea) field).getLabel());
               xitem = Control.replaceTag(xitem, TAG_DESCRIPTION, ((FormFieldTextArea) field).getDescription());
               xitem = Control.replaceTag(xitem, TAG_CONTROL_NAME, ((FormFieldTextArea) field).getName());
               xhtml += xitem;
            }
            else if (field instanceof FormFieldInteger)
            {
               xitem = ctrl.getElement(CPART_FIELD_CONTROL);
               xitem = Control.replaceTag(xitem, TAG_CONTROL, field.render(getWorkspace()));
               xitem = Control.replaceTag(xitem, TAG_LABEL, ((FormFieldInteger) field).getLabel());
               xitem = Control.replaceTag(xitem, TAG_DESCRIPTION, ((FormFieldInteger) field).getDescription());
               xitem = Control.replaceTag(xitem, TAG_CONTROL_NAME, ((FormFieldInteger) field).getName());
               xhtml += xitem;
            }
            else if (field instanceof FormFieldBoolean)
            {
               xitem = ctrl.getElement(CPART_FIELD_OPTION);
               xitem = Control.replaceTag(xitem, TAG_CONTROL, field.render(getWorkspace()));
               xitem = Control.replaceTag(xitem, TAG_LABEL, ((FormFieldBoolean) field).getLabel());
               xitem = Control.replaceTag(xitem, TAG_DESCRIPTION, ((FormFieldBoolean) field).getDescription());
               xitem = Control.replaceTag(xitem, TAG_CONTROL_NAME, ((FormFieldBoolean) field).getName());
               xhtml += xitem;
            }
            else if (field instanceof FormFieldDate)
            {
               xitem = ctrl.getElement(CPART_FIELD_CONTROL);
               xitem = Control.replaceTag(xitem, TAG_CONTROL, field.render(getWorkspace()));
               xitem = Control.replaceTag(xitem, TAG_LABEL, ((FormFieldDate) field).getLabel());
               xitem = Control.replaceTag(xitem, TAG_DESCRIPTION, ((FormFieldDate) field).getDescription());
               xitem = Control.replaceTag(xitem, TAG_CONTROL_NAME, ((FormFieldDate) field).getName());
               xhtml += xitem;
            }
            else if (field instanceof FormFieldList)
            {
               xitem = ctrl.getElement(CPART_FIELD_CONTROL);
               xitem = Control.replaceTag(xitem, TAG_CONTROL, field.render(getWorkspace()));
               xitem = Control.replaceTag(xitem, TAG_LABEL, ((FormFieldList) field).getLabel());
               xitem = Control.replaceTag(xitem, TAG_DESCRIPTION, ((FormFieldList) field).getDescription());
               xitem = Control.replaceTag(xitem, TAG_CONTROL_NAME, ((FormFieldList) field).getName());
               xhtml += xitem;
            }
         }

         xitem = ctrl.getElement(CPART_GROUP_FOOTER);
         xhtml += xitem;
      }

      xitem = ctrl.getElement(CPART_BUTTONS);
      xitem = Control.replaceTag(xitem, TAG_BUTTONS, getButtonsXhtml(getWorkspace().getServerSession()));
      xhtml += xitem;

      xitem = ctrl.getElement(CPART_FOOTER);
      xitem = Control.replaceTag(xitem, TAG_FORM_NAME, this.name);
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

      str.append("<div id=\"").append(this.getId()).append("\">").append("\n");
      str.append("  <h2>").append(this.title).append("</h2>").append("\n");
      if (!this.title.equals(""))
      {
         str.append("  <p>").append(this.description).append("</p>").append("\n");
      }
      str.append("  <form id=\"").append(this.name).append("\" name=\"").append(this.name).append("\" action=\"").append(this.actionUrl).append("\" method=\"post\">").append("\n");

      for (FormFieldHidden field : this.hidden)
      {
         str.append(field.render(getWorkspace())).append("\n");
      }
      for (FormFieldset group : this.groups)
      {
         str.append(group.render(getWorkspace())).append("\n");
      }
      if (!this.buttons.isEmpty()) 
      {
         str.append("    <div id=\"").append(this.getId()).append("btn\">").append("\n");
         for (FormButton button : this.buttons)
         {
            str.append(button.render(getWorkspace())).append("\n");
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

      this.groups = new ArrayList<FormFieldset>();
      this.hidden = new ArrayList<FormFieldHidden>();
      this.buttons = new ArrayList<FormButton>();

      // Agrega un campo oculto con el indicador de envío de formulario (para detectar POSTs)
      this.hidden.add(new FormFieldHidden(Cosmo.KEY_UI_FORM_ACTION, Cosmo.TOKEN_UI_FORM_POSTBACK));
   }

   /**
    * Obtiene el código XHTML correspondiente a los botones del formulario.
    */
   private String getButtonsXhtml(HttpSession session)
   {
      String btns = "";

      for (FormButton button : this.buttons)
      {
         btns += button.render(getWorkspace()) + "&nbsp;&nbsp;"; 
      }
      return btns;
   }
}

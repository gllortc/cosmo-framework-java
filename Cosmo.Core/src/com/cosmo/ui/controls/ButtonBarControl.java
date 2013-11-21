package com.cosmo.ui.controls;

import java.util.ArrayList;

import com.cosmo.Workspace;
import com.cosmo.ui.templates.TemplateControl;

/**
 * Implementa una barra de botones.
 * Estos botones son enlaces, no son usables en formularios.
 * 
 * @author Gerard Llort
 */
public class ButtonBarControl extends Control
{
   private static final String CONTROL_ID = "CosmoUiCtrlButtonBar";

   private static final String CPART_HEADER = "buttonbar-header";
   private static final String CPART_FOOTER = "buttonbar-footer";

   private ArrayList<ButtonBarItem> buttons;


   //==============================================
   // Contructors
   //==============================================

   /**
    * Contructor de la clase.
    */
   public ButtonBarControl(Workspace workspace)
   {
      super(workspace);
      initialize();
   }

   /**
    * Contructor de la clase.
    */
   public ButtonBarControl(Workspace workspace, String id)
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
      return ButtonBarControl.CONTROL_ID;
   }


   //==============================================
   // Methods
   //==============================================

   /**
    * Añade un botón a la barra.
    * 
    * @param button Una instancia de {@link ButtonBarItem} que representa el botón a añadir.
    */
   public void addButton(ButtonBarItem button)
   {
      this.buttons.add(button);
   }

   /**
    * Elimina todos los botones de la barra.
    */
   public void clear()
   {
      this.buttons.clear();
   }

   /**
    * Renderiza el control y genera el código XHTML de representación.
    *
    * @return Devuelve una cadena en formato XHTML que representa el control. 
    */
   @Override
   public String render() 
   {
      TemplateControl ctrl;
      StringBuilder str = new StringBuilder();

      // Si no tiene elementos, no representa el control (deja una traza)
      if (this.buttons.isEmpty())
      {
         return "<-- ButtonBarControl placeholder (void) -->\n";
      }

      // Obtiene la plantilla y la parte del control
      ctrl = getWorkspace().getTemplate().getControl(ButtonBarControl.CONTROL_ID);

      // Genera la cabecera de la barra de navegación
      str.append(ctrl.getElement(CPART_HEADER));

      for (ButtonBarItem item : this.buttons)
      {
         str.append(item.render(ctrl));
      }

      // Genera el pie de la barra de navegación
      str.append(ctrl.getElement(CPART_FOOTER));

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
      this.buttons = new ArrayList<ButtonBarItem>();
   }
}

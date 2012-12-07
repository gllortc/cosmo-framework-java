package com.cosmo.annotations;

/**
 * Anotación que sirve para indicar a las páginas que se precisa ser usuario autenticado.
 */
public @interface CosmoTable 
{
   /**
    * Nombre del campo en la BBDD.
    */
   String tableName();
   
   /**
    * Nombre de la tabla (se usará como nombre de formulario).
    */
   String name();
}

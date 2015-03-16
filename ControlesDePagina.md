# Controles de página #

Los controles implementados actualmente en **Cosmo** son los siguientes:

|**Control**|**Descripción**|
|:----------|:---------------|
|`BreadcrumbsControl`|Implementa un control para presentar la ruta a la página mostrada actualmente (_breadcrumb_ en inglés).|
|`ButtonBarControl`|Implementa una barra de botones o herramientas.|
|`DynamicMessageControl`|Implementa un mensaje de advertencia, información o error.|
|`ErrorMessageControl`|Implementa un mensaje (similar al `DynamicMessageControl`) especializado en mostrar excepciones de Java.|
|`FormControl`|Implementa un formulario.|
|`GridControl`|Implementa un grid (o tabla) para presentar datos.|
|`HeaderControl`|Implementa una cabecera de página y sirve para presentar títulos, descripción, etc.|
|`ListViewControl`|Implementa una lista de objetos (con soporte para distintos tipos de lista) que permite realizar listas de cualquier tipo de objeto.|
|`SliderControl`|Implementa una slider (imagen o contenido HTML deslizante).|
|`XhtmlControl`|Implementa una control para generar contenidos estáticos.|

## Estructura de un control ##

Todo control implementa la clase abstracta `com.cosmo.ui.controls.Control` y dispone de unas propiedades que se deben establecer:

|Propiedad|Descripción|
|:--------|:-----------|
|`uuid`|Es un identificador único interno que se genera automáticamente cada vez que se crea una nueva instancia del control.|
|`id`|Es un identificador único que establece el desarrollador por código sólo si desea acceder al control vía código para modificar sus propiedades. Si no se establece este identificador tomará como valor `uuid`.|

## Desarrollo de nuevos controles ##
**Cosmo** es suficientemente flexible para permitir la creación de nuevos controles cuando los existentes no cubran las necesidades de una funcionalidad concreta.

Para desarrollar un nuevos controles se debe implementar la clase abstracta `com.cosmo.ui.controls.Control` y agregar en la plantilla (o plantillas usadas) la parte XHTML / Javascript / CSS que va a usar el control.
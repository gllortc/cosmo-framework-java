# User Interface Services #

Cosmo permite generar y gestionar el ciclo de vida de las páginas de la aplicación sin codificar ni una sola línea de código XHTML/CSS/JavaScript, ya que de esto se encarga este servicio usando plantillas.

Las páginas de Cosmo se crean agregando [controles](ControlesDePagina.md) en las zonas habilitadas. Estas zonas son tres:

  * Lateral izquierda
  * Central
  * Lateral derecha

La siguiente ilustración muestra esquemáticamente la forma de crear una página:

![https://cosmo-framework.googlecode.com/svn/wiki/images/page_basics.png](https://cosmo-framework.googlecode.com/svn/wiki/images/page_basics.png)

Cada página dispone de un [estructura de página](Layouts.md) que mostrará una, dos o las tres zonas, dependiendo de las necesidades concretas.

Una vez colocados los controles, éstos son parametrizados (por código) con la información concreta que deben mostrar según el contexto de la aplicación.

## Renderizado ##
Una vez modelada y parametrizada la página y sus [controles](ControlesDePagina.md), **Cosmo** procede a renderizar la página, convirtiendo el modelo y su contenido en código XHTML (HTML 5). **Cosmo** dispone de un _motor de renderizado_ nativo (`com.cosmo.ui.render.impl.CosmoPageRenderImpl`) que usa plantillas para transformar los datos en código XHTML.

### Plantillas de presentación ###

Las plantillas (_templates_) permiten modificar el aspecto de la aplicación completamente y pueden ser aplicadas según determinadas condiciones.

Por ejemplo, si el _Browser Agent_ del navegador contiene "`Android`" o "`iPhone`" se puede aplicar una plantilla _responsive_ que se adapte a dispositivos móviles. Si no cumple ninguna de estas condiciones iniciales se aplicará otra plantilla específica para dispositivos de pantalla grande.

### Motores de renderizado ###

La arquitectura de **Cosmo** permite usar otros motores de renderizado que implementen el interface `com.cosmo.ui.render.PageRenderEngine`.

Por ejemplo, se podría desarrollar un motor de renderizado que en vez de usar las plantillas genere un interface basado en [ExtJS](http://www.sencha.com/products/extjs).
# Ciclo de vida de una página #

El siguiente diagrama de actividad muestra el ciclo de vida de las páginas generadas mediante **Cosmo**:

![https://cosmo-framework.googlecode.com/svn/wiki/images/Activity_CosmoPageLiferay.png](https://cosmo-framework.googlecode.com/svn/wiki/images/Activity_CosmoPageLiferay.png)

## Eventos de página ##

Como se aprecia en el diagrama, toda página puede disparar hasta tres eventos.

|Evento|Descripción|
|:-----|:-----------|
|`InitPageEvent`|Evento de inicialización de la página. Se usa para colocar los controles en la página.|
|`FormSendedEvent`|Evento de recepción de datos de formulario. Se dispara cuando se reciben datos de un formulario de la página (siempre en la misma página). Permite tratar los datos o realizar acciones a partir de los datos recibidos.|
|`LoadPageEvent`|Evento de carga de la página. Se usa para especificar el contenido dinámico de los controles de página, como rellenar un _grid_, mostrar una lista, etc.|

El siguiente código muestra como se implementa la clase abstracta `com.cosmo.ui.Page` y como se invocan los eventos:

```
package org.inforgest.samples.cosmo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cosmo.ui.Page;
import com.cosmo.ui.PageContext;

@WebServlet( description = "Sample page", urlPatterns = { "/SamplePage" } )
public class SamplePage extends Page
{

   @Override
   public PageContext initPageEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response) 
   {
      // Código del evento aquí
      return pc;
   }

   @Override
   public PageContext formSendedEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response) 
   {
      // Código del evento aquí
      return pc;
   }

   @Override
   public PageContext loadPageEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response) 
   {
      // Código del evento aquí
      return pc;
   }

   @Override
   public PageContext pageException(PageContext pc, Exception exception) 
   {
      // Código del evento aquí
      return pc;
   }

}
```
package com.cosmo.ui.render;

import com.cosmo.ui.Page;
import com.cosmo.ui.templates.TemplateUnavailableException;

/**
 * Interface que deben implementar todas las implementaciones de <em>renderer</em> para páginas de Cosmo.
 * 
 * @author Gerard Llort
 */
public interface PageRender
{
   /**
    * Renderiza la página convirtiendo la lógica de clases en código XHTML.
    * 
    * @param page Una instancia de {@link Page} que representa la página a renderizar.
    * @return Una cadena que contiene el código XHTML.
    * 
    * @throws TemplateUnavailableException
    * @throws PageRenderException 
    */
   public abstract String render(Page page) throws TemplateUnavailableException, PageRenderException;
}

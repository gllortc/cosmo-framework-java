package com.cosmo.ui;

/**
 * Interface que describe como debe ser una página de Cosmo.
 * 
 * @author Gerard Llort
 */
public interface PageInterface
{
   /**
    * Renderiza el contenido de una página, transformándola en una cadena XHTML.
    * 
    * @throws PageRenderException 
    */
   public void render(PageContext pc);
}

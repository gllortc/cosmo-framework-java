//==================================================================
// Acciones para Cosmo ORM Data Services
//------------------------------------------------------------------
// Autor:   Gerard Llort
// Versión: 1.0.0
//==================================================================

//------------------------------------------------------------------
// Pide confirmación antes de eliminar un registro.
//------------------------------------------------------------------
// url:	URL de eliminación del registro
//------------------------------------------------------------------
function deleteConfirm(url)
{
	bootbox.confirm("Está Ud. seguro que desea eliminar el registro?", function(result) 
	{
		if (result)
		{
			window.location = url;
		}
		else
		{
			bootbox.alert("La acción ha sido cancelada.", function() { });
		}
	}); 
}

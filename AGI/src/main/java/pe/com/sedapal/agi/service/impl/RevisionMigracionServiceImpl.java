package pe.com.sedapal.agi.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pe.com.sedapal.agi.dao.IRevisionMigracionDAO;
import pe.com.sedapal.agi.model.Revision;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.request_objects.RevisionRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.service.IRevisionMigracionService;


@Service
public class RevisionMigracionServiceImpl implements IRevisionMigracionService{
	
	static String iUsuario;	
	static Long idUsuario;
		
	@Autowired
	private IRevisionMigracionDAO dao;
	
	public Paginacion getPaginacion() {
		return this.dao.getPaginacion();
	}	
	public Error getError() {
		return this.dao.getError();
	}

	@Override
	public List<Revision> obtenerRevision(RevisionRequest revisionRequest, PageRequest pageRequest){
		List<Revision> lista = this.dao.obtenerRevision(revisionRequest, pageRequest);
		return lista;
	}
	
	@Override
	public List<Revision> obtenerRevisionFase(RevisionRequest revisionRequest, PageRequest pageRequest){
		List<Revision> lista = this.dao.obtenerRevisionFase(revisionRequest, pageRequest, idUsuario);
		return lista;
	}
	
	@Override
	public Boolean eliminarRevision(Long id) {
		return this.dao.eliminarRevision(id);
	}
	
	@Override
	public Revision crearRevision(Revision revisionRequest){
		Revision lista = this.dao.crearRevision(revisionRequest, iUsuario,idUsuario,null );
		return lista;
	}

	@Override
	public List<Revision> obtenerListaTareaAprobar(RevisionRequest revisionRequest, PageRequest pageRequest) {
		List<Revision> lista = this.dao.obtenerListaTareaAprobar(revisionRequest, pageRequest);
		return lista;
	}
		@Override
	public Revision crearDocumentoGoogleDrive (String idDocGoogle, Long idRevi){
		Revision lista = this.dao.crearDocumentoGoogleDrive(idDocGoogle, idRevi);
		return lista;
	}
}
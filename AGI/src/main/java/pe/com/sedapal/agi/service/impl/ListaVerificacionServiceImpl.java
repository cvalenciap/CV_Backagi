package pe.com.sedapal.agi.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import pe.com.sedapal.agi.dao.IListaVerificacionDAO;
import pe.com.sedapal.agi.model.DatosAuditoria;
import pe.com.sedapal.agi.model.ListaVerificacion;
import pe.com.sedapal.agi.model.ListaVerificacionAuditado;
import pe.com.sedapal.agi.model.ListaVerificacionRequisito;
import pe.com.sedapal.agi.model.NodoRequisitoLV;
import pe.com.sedapal.agi.model.Norma;
import pe.com.sedapal.agi.model.request_objects.ListaVerificacionRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.security.model.SessionInfo;
import pe.com.sedapal.agi.service.IListaVerificacionService;
import pe.com.sedapal.agi.security.config.UserAuth;
@Service
public class ListaVerificacionServiceImpl implements IListaVerificacionService{
	
	@Autowired
	SessionInfo session;
	
	@Autowired IListaVerificacionDAO dao;
	
	@Override
	public Error getError() {
		return this.dao.getError();
	}

	@Override
	public Paginacion getPaginacion() {
		// TODO Auto-generated method stub
		return this.dao.getPaginacion();
	}

	@Override
	public List<ListaVerificacion> consultarListaVerificacion(ListaVerificacionRequest listaVerificacionRequest,
			PageRequest paginaRequest) {
		// TODO Auto-generated method stub
		return this.dao.consultarListaVerificacion(listaVerificacionRequest, paginaRequest);
	}

	@Override
	public ListaVerificacion consultarListaVerifcacionPorId(Long idListaVerificacion) {
		// TODO Auto-generated method stub
		
		ListaVerificacion listaVerificacion =  this.dao.consultarListaVerifcacionPorId(idListaVerificacion);
		listaVerificacion.setListaRequisitosLV(this.obtenerRequisitosListaVerificacion(idListaVerificacion));
		List<ListaVerificacionRequisito> listaRequisitos = listaVerificacion.getListaRequisitosLV();
		
		List<Long> listaNormas = listaRequisitos.stream().map(obj -> obj.getIdNorma()).distinct().collect(Collectors.toList());
		listaNormas.stream().forEach(obj -> {
			System.out.println(obj);
		});
		
		
		List<NodoRequisitoLV> listaNodosRequisitoLV = new ArrayList<>();
		
		for(Long idNorma:listaNormas) {
			NodoRequisitoLV nodoPadre = new NodoRequisitoLV();
			nodoPadre.setId(idNorma);
			for(ListaVerificacionRequisito requisito:listaRequisitos) {
				if(requisito.getIdNorma().equals(idNorma)) {
					nodoPadre.setNombre(requisito.getDescripcionNorma());
					break;
				}
			}
			
			nodoPadre.setChildren(new ArrayList<>());
			
			for(ListaVerificacionRequisito requisito:listaRequisitos) {
				if(requisito.getIdNorma().equals(nodoPadre.getId())) {
					NodoRequisitoLV nodoHijo = new NodoRequisitoLV();
					nodoHijo.setId(requisito.getIdLVRequisito());
					nodoHijo.setNombre(requisito.getDescripcionRequisito());
					nodoPadre.getChildren().add(nodoHijo);
				}
			}
			listaNodosRequisitoLV.add(nodoPadre);
		}
		
		listaVerificacion.setListaNodosRequisitoLV(listaNodosRequisitoLV);
		listaVerificacion.setListaAuditadosLV(this.dao.obtenerAuditadosListaVerificacion(idListaVerificacion));
		
		
		

		return listaVerificacion;
	}

	@Override
	public List<ListaVerificacionRequisito> obtenerRequisitosListaVerificacion(Long idListaVerificacion) {
		// TODO Auto-generated method stub
		List<ListaVerificacionRequisito> listaRequisitoLV = this.dao.obtenerRequisitosListaVerificacion(idListaVerificacion);
		return listaRequisitoLV;
	}

	@Override
	public void actualizarListaVerificacion(ListaVerificacion listaVerificacion) {
		// TODO Auto-generated method stub
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		DatosAuditoria datosAuditoria = new DatosAuditoria();
		datosAuditoria.setUsuarioCreacion((((UserAuth)principal).getUsername()));
		datosAuditoria.setUsuarioModificacion((((UserAuth)principal).getUsername()));
		listaVerificacion.setDatosAuditoria(datosAuditoria);
		boolean registroLV = this.dao.actualizarListaVerificacion(listaVerificacion,1);
		
		if(registroLV) {
			for(ListaVerificacionRequisito requisito:listaVerificacion.getListaRequisitosLV()) {
				requisito.setDatosAuditoria(listaVerificacion.getDatosAuditoria());
				boolean registro = this.actualizarRequisitosListaVerificacion(requisito);
				if(!registro) {
					break;
				}
			}
			
			for(ListaVerificacionAuditado auditado:listaVerificacion.getListaAuditadosLV()) {
				boolean registro = true;
				if(auditado.getEstadoRegistro().equals("1")) {
					if(auditado.getIdListVeriAuditado()==null) {
						auditado.setDatosAuditoria(listaVerificacion.getDatosAuditoria());
						auditado.setIdListVeri(listaVerificacion.getIdListaVerificacion());
						registro = this.dao.registrarListaVerificacionAuditado(auditado);
					}
					
					
				}else if(auditado.getEstadoRegistro().equals("0")) {
					if(auditado.getIdListVeriAuditado()!=null) {
						auditado.setDatosAuditoria(listaVerificacion.getDatosAuditoria());
						registro = this.dao.eliminarListaVerificacionAuditado(auditado);
					}
					
				}
				
				if(!registro) {
					break;
				}
				
				
			}
		}
		
	}

	@Override
	public boolean actualizarRequisitosListaVerificacion(ListaVerificacionRequisito listaVerificacionRequisito) {
		// TODO Auto-generated method stub
		return this.dao.actualizarRequisitosListaVerificacion(listaVerificacionRequisito,1);
	}

	@Override
	public boolean aprobarRechazarListaVerificacion(ListaVerificacion listaVerificacion, int indicador) {
		// TODO Auto-generated method stub
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		DatosAuditoria datosAuditoria = new DatosAuditoria();
		datosAuditoria.setUsuarioModificacion((((UserAuth)principal).getUsername()));
		listaVerificacion.setDatosAuditoria(datosAuditoria);
		return this.dao.aprobarRechazarListaVerificacion(listaVerificacion, indicador);
	}

}

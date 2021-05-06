package pe.com.sedapal.agi.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import pe.com.sedapal.agi.security.config.UserAuth;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import pe.com.sedapal.agi.dao.IDeteccionHallazgosDAO;
import pe.com.sedapal.agi.model.ConstantesLista;
import pe.com.sedapal.agi.model.DatosAuditoria;
import pe.com.sedapal.agi.model.DeteccionHallazgos;
import pe.com.sedapal.agi.model.request_objects.DeteccionHallazgosRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.security.model.SessionInfo;
import pe.com.sedapal.agi.service.IDeteccionHallazgosService;

@Service
public class DeteccionHallazgosServiceImpl implements IDeteccionHallazgosService{

	@Autowired
	SessionInfo session;
	

	
	
	@Autowired
	private IDeteccionHallazgosDAO dao;

	@Override
	public List<DeteccionHallazgos> obtenerListaDeteccionHallazgos(DeteccionHallazgosRequest deteccionHallazgosRequest,PageRequest paginaRequest){

		
		return this.dao.obtenerListaDeteccionHallazgos(deteccionHallazgosRequest,paginaRequest);
		/*
		Map<String, Object> queryResp = this.dao.ListaDeteccionHallazgo(tipoNCO, origedeteccion, nombreDetector, apPaternoDetector,apMaternoDetector, estado, pageRequest);
		
		List<Map<String, Object>> lista = (List<Map<String, Object>>)queryResp.get("o_cursor");
		bdCast = (BigDecimal)queryResp.get("o_retorno");
						
		iRespuesta = bdCast.intValueExact();
		
		
		if (iRespuesta <= 0) {
			sRespuesta = (String)queryResp.get("o_mensaje");
			respuesta.put("estado", 0); //Estado=nRESP_SP
			respuesta.put("error", sRespuesta);
			respuesta.put("resultado", "");
			respuesta.put("paginacion","");
		}else {
			
			Number id=0;
			
			for(Map<String, Object> map : lista) {
				item = new Hallazgo();
				
				totalRegistros=(BigDecimal)map.get("RESULT_COUNT");
				
				bdCast=(BigDecimal)map.get("CODIGO_DETECCION");
				id=bdCast.longValueExact();
				item.setIdDeteccionHallazgo(id);
				
				bdCast=(BigDecimal)map.get("ID_AMBITO");
				id=bdCast.longValueExact();
				item.setIdAmbito(id);
				
				Date varDate;
				
				
				variable=(String)map.get("AMBITO");
				item.setAmbito(variable);
				
				
				bdCast=(BigDecimal)map.get("ID_ORIGEN_DETECCION");
				id=bdCast.longValueExact();
				item.setIdorigenDeteccion(id);

				 
				variable=(String)map.get("ORIGEN_DETECCION");
				item.setOrigenDeteccion(variable);
				

				bdCast=(BigDecimal)map.get("ID_TIPO_NO_CONFORMIDAD");
				id=bdCast.longValueExact();
				item.setIdTipoNoConformidad(id);																	
			
				variable=(String)map.get("TIPO_NO_CONFORMIDAD");					
				item.setTipoNoConformidad(variable); 				
				
				variable=(String)map.get("DESCRIPCION_HALLAZGO");
				item.setDescripHallazgo(variable);  
				
				bdCast=(BigDecimal)map.get("ID_DETECTOR");
				id=bdCast.longValueExact();
				item.setIdDetector(id);	
				
				variable=(String)map.get("DETECTOR");
				item.setDetector(variable);   
				
				variable=(String)map.get("ESTADO");
				item.setEstado(variable);  
				
				SimpleDateFormat parseador = new SimpleDateFormat("YY-MM-DD");
				varDate=(Date)map.get("FECHA_INGRESO");
				item.setFechaIngreso(parseador.format(varDate)); 
				
				
				variable=(String)map.get("FECHA_INICIO_PLAN");
				item.setFechaInicioPlan(variable);  
				
				variable=(String)map.get("FECHA_FIN_PLAN");
				item.setFechaFinPlan(variable);    
				
				bdCast=(BigDecimal)map.get("RNUM");
				id=bdCast.longValueExact();
				item.setItem(id);
				
				listaBandejaHallazgo.add(item);
				
			}
			
			
			oPaginacion= new Paginacion();
			oPaginacion.setPagina(pagina);
			oPaginacion.setRegistros(registros);
			oPaginacion.setTotalRegistros(Integer.parseInt(totalRegistros.toString()));
			
			respuesta.put("estado", 1);
			respuesta.put("error", "Ejecución correcta."); //error=cRESP_SP
			respuesta.put("resultado", listaBandejaHallazgo); //resultado=bRESP
			respuesta.put("paginacion", oPaginacion);
		}
		return respuesta;
		*/
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> obtenerListaConstantes(Map<String, String> RequestParm) {
		String aux;
		
		if (((String)RequestParm.get("ListaConstante")).isEmpty() || ((String)RequestParm.get("ListaConstante"))==null) { aux=""; }
		else {aux=((String)RequestParm.get("ListaConstante")).toUpperCase();}
		//System.out.println(aux);
		String listaConstante =  aux;			

		
		int iRespuesta = 0;
		//BigDecimal totalRegistros=new BigDecimal('0');
		Map<String,Object> respuesta = new TreeMap<String,Object>();
		//Paginacion oPaginacion=null;
		
		ConstantesLista item = null;
		List<ConstantesLista> listaCargaConstante= new ArrayList<>();

		String sRespuesta = null;
		BigDecimal bdCast;
		String variable;
		
		Map<String, Object> queryResp = this.dao.ListaConstantes(listaConstante);
		List<Map<String, Object>> lista = (List<Map<String, Object>>)queryResp.get("o_cursor");
		bdCast = (BigDecimal)queryResp.get("o_retorno");
		iRespuesta = bdCast.intValueExact();
		
		if (iRespuesta <= 0) {
			sRespuesta = (String)queryResp.get("o_mensaje");
			respuesta.put("estado", 0); //Estado=nRESP_SP
			respuesta.put("error", sRespuesta);
			respuesta.put("resultado", "");			
		}else {
			Number id=0;
			for(Map<String, Object> map : lista) {
				item = new ConstantesLista();										
				

				bdCast=(BigDecimal)map.get("N_IDCONS");
				id=bdCast.longValueExact();
				item.setIdConstante(id);
				
				bdCast=(BigDecimal)map.get("N_IDCONSSUPE");
				id=bdCast.longValueExact();
				item.setIdPadre(id);			
				
				variable=(String)map.get("V_VALCONS");
				item.setValorNombreConstante(variable);
				
				
				variable=(String)map.get("V_NOMCONS");				
				item.setValorNombrePadre(variable);
				 
				listaCargaConstante.add(item);
				
			}
			
			respuesta.put("estado", 1);
			respuesta.put("error", "Ejecución correcta."); //error=cRESP_SP
			respuesta.put("resultado", listaCargaConstante); //resultado=bRESP
		}
		return respuesta;
	}
	
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
	public boolean registrarDatosDeteccionHallazgos(DeteccionHallazgos deteccion) {
		// TODO Auto-generated method stub
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		DatosAuditoria datosAuditoria = new DatosAuditoria();
		datosAuditoria.setUsuarioCreacion((((UserAuth)principal).getUsername()));
		deteccion.setDatosAuditoria(datosAuditoria);
		
		if(deteccion.getValorTipoEntidad().equals("1")) {
			deteccion.setValorEntidadEquipo(null);
		}else if(deteccion.getValorTipoEntidad().equals("2")) {
			deteccion.setValorEntidadGerencia(null);
		}else {
			deteccion.setValorEntidadGerencia(null);
			deteccion.setValorEntidadEquipo(null);
		}
		
		
		return this.dao.registrarDatosDeteccionHallazgos(deteccion);
	}

}

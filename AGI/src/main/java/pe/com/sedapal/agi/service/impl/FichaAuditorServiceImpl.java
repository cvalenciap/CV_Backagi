package pe.com.sedapal.agi.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.Cursos;
import pe.com.sedapal.agi.model.FichaAuditor;
import pe.com.sedapal.agi.model.request_objects.AuditorRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.service.IFichaAuditorService;
import pe.com.sedapal.agi.dao.IFichaAuditorDAO;


@Component
public class FichaAuditorServiceImpl implements IFichaAuditorService{

	@Autowired
	private IFichaAuditorDAO dao;
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> ListaAuditorBandeja(Map<String,String> RequestParm) {	
	String aux;
		
	String iNumFicha = ((String)RequestParm.get("numFicha")).toUpperCase();
	String iNombreAuditor = ((String)RequestParm.get("nombreAuditor")).toUpperCase();
	String iApePaternoAuditor = ((String)RequestParm.get("apePaternoAuditor")).toUpperCase();
	String iApeMaternoAuditor = ((String)RequestParm.get("apeMaternoAuditor")).toUpperCase();
	String iavanzada = ((String)RequestParm.get("avanzada")).toUpperCase();
	
	if (((String)RequestParm.get("numFicha")).isEmpty() || ((String)RequestParm.get("numFicha"))==null) { aux=""; }
	else {aux=((String)RequestParm.get("numFicha")).toUpperCase();}
	System.out.println(aux);
	iNumFicha =  aux;
	
	if (((String)RequestParm.get("nombreAuditor")).isEmpty() || ((String)RequestParm.get("nombreAuditor"))==null) { aux=""; }
	else {aux=((String)RequestParm.get("nombreAuditor")).toUpperCase();}
	System.out.println(aux);
	iNombreAuditor =  aux;
	
	if (((String)RequestParm.get("apePaternoAuditor")).isEmpty() || ((String)RequestParm.get("apePaternoAuditor"))==null) { aux=""; }
	else {aux=((String)RequestParm.get("apePaternoAuditor")).toUpperCase();}
	System.out.println(aux);
	iApePaternoAuditor =  aux;
	
	if (((String)RequestParm.get("apeMaternoAuditor")).isEmpty() || ((String)RequestParm.get("apeMaternoAuditor"))==null) { aux=""; }
	else {aux=((String)RequestParm.get("apeMaternoAuditor")).toUpperCase();}
	System.out.println(aux);
	iApeMaternoAuditor =  aux;
	
	int pagina=Integer.parseInt((String)RequestParm.get("nPagina"));
	int registros=Integer.parseInt((String)RequestParm.get("nRegistro"));
	System.out.println(pagina);
	
	System.out.println(registros);
	
	int iRespuesta = 0;
	BigDecimal totalRegistros=new BigDecimal('0');
	Map<String,Object> respuesta = new TreeMap<String,Object>();
	Paginacion oPaginacion=null;
	FichaAuditor item = null;
	List<FichaAuditor> listaFichaAuditor = new ArrayList<>();

	String sRespuesta = null;
	BigDecimal bdCast;
		
	PageRequest pageRequest= new PageRequest(pagina,registros);
	
	
	Map<String, Object> queryResp = this.dao.obtenerListaFichaAuditor(iavanzada,iNumFicha,iNombreAuditor, iApePaternoAuditor, iApeMaternoAuditor, pageRequest);
	
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
		
		
		
		for(Map<String, Object> map : lista) {
			item = new FichaAuditor();
			totalRegistros=(BigDecimal)map.get("RESULT_COUNT");
		
			
			bdCast = (BigDecimal)map.get("COD_AUDITOR");
			item.setCodigo(bdCast.intValueExact());
			
			item.setNumFicha((String)map.get("NUM_FICHA"));
			
			bdCast = (BigDecimal)map.get("COD_TIPO");
			item.setTipo(bdCast.intValueExact());
			
			item.setNomTipo((String)map.get("NOM_TIPO"));
			
			item.setNombreAuditor((String)map.get("NOMBRE_AUDITOR"));
			item.setApePaternoAuditor((String)map.get("APE_PATERNO_AUDITOR"));
			item.setApeMaternoAuditor((String)map.get("APE_MATERNO_AUDITOR"));
			
			bdCast = (BigDecimal)map.get("COD_ROL");
			item.setCodigoRolAuditor(bdCast.intValueExact());
			
			item.setNomRol((String)map.get("NOM_ROL"));
			
			listaFichaAuditor.add(item);
		}
		
		
		oPaginacion= new Paginacion();
		oPaginacion.setPagina(pagina);
		oPaginacion.setRegistros(registros);
		oPaginacion.setTotalRegistros(Integer.parseInt(totalRegistros.toString()));
		
		respuesta.put("estado", 1);
		respuesta.put("error", "Ejecución correcta."); //error=cRESP_SP
		respuesta.put("resultado", listaFichaAuditor); //resultado=bRESP
		respuesta.put("paginacion",oPaginacion);
	}
	return respuesta;
  }

	@Override
	public Map<String, Object> EliminarFicha(Long codigo) {
		Map<String,Object> respuesta = new TreeMap<String,Object>();			
		Map<String, Object> queryResp = this.dao.eliminarFichaAuditor(codigo);			
		BigDecimal bdCast = (BigDecimal)queryResp.get("o_retorno");
        int iRespuesta = bdCast.intValueExact();
		String sRespuesta;
		
		
		//int iRespuesta = bdCast.intValueExact();
		
		if (iRespuesta <= 0) {
			sRespuesta = (String)queryResp.get("o_mensaje");
			respuesta.put("estado", 0); 
			respuesta.put("error", sRespuesta);

		}else {
			sRespuesta = (String)queryResp.get("o_mensaje");
			respuesta.put("estado", 1); 
			respuesta.put("error", sRespuesta);
			}

		
		return respuesta;
	}

	@Override
	public Map<String, Object> ListaDatosAuditor(Map<String, String> requestParm) {
		//int aux=0;
		
		//String iCodigoAuditor = ((String)requestParm.get("codNombreAuditor")).toUpperCase();
	
		
		//int iCodAuditor =  aux;
		//int iRespuesta = 0;
		
		Map<String,Object> respuesta = new TreeMap<String,Object>();
		
		//FichaAuditor item = null;
		//List<FichaAuditor> listaFichaAuditor = new ArrayList<>();

		//String sRespuesta = null;
		//BigDecimal bdCast;

		
	//	Map<String, Object> queryResp = this.dao.obtenerListaFichaAuditor(iCodAuditor,iNombreAuditor,pageRequest);
		
	/*	List<Map<String, Object>> lista = (List<Map<String, Object>>)queryResp.get("o_cursor");
		bdCast = (BigDecimal)queryResp.get("o_retorno");



		iRespuesta = bdCast.intValueExact();
		if (iRespuesta <= 0) {
			sRespuesta = (String)queryResp.get("o_mensaje");
			respuesta.put("estado", 0); //Estado=nRESP_SP
			respuesta.put("error", sRespuesta);
			respuesta.put("resultado", "");
			respuesta.put("paginacion","");
		}else {



			for(Map<String, Object> map : lista) {
				item = new FichaAuditor();
				totalRegistros=(BigDecimal)map.get("RESULT_COUNT");


				bdCast = (BigDecimal)map.get("COD_AUDITOR");
				item.setCodigo(bdCast.intValueExact());

				bdCast = (BigDecimal)map.get("COD_TIPO");
				item.setTipo(bdCast.intValueExact());

				item.setNomTipo((String)map.get("NOM_TIPO"));

				item.setNombreAuditor((String)map.get("NOMBRE_AUDITOR"));

				bdCast = (BigDecimal)map.get("COD_ROL");
				item.setCodigoRolAuditor(bdCast.intValueExact());

				item.setNomRol((String)map.get("NOM_ROL"));

				listaFichaAuditor.add(item);
			}


			oPaginacion= new Paginacion();
			oPaginacion.setPagina(pagina);
			oPaginacion.setRegistros(registros);
			oPaginacion.setTotalRegistros(Integer.parseInt(totalRegistros.toString()));

			respuesta.put("estado", 1);
			respuesta.put("error", "Ejecución correcta."); //error=cRESP_SP
			respuesta.put("resultado", listaFichaAuditor); //resultado=bRESP
			respuesta.put("paginacion",oPaginacion);
		}*/
		return respuesta;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> ListaConsultaConstante(Map<String, String> RequestParm) {
	
		String aux;
		int iRespuesta = 0;
		
	
		
		aux= ((String)RequestParm.get("codigo")).toUpperCase();
		Long iCodConstante = Long.parseLong(aux);
		
		System.out.println(iCodConstante);
		
		Map<String,Object> respuesta = new TreeMap<String,Object>();
		
		Constante item = null;
		List<Constante> listaConstantes = new ArrayList<>();

		String sRespuesta = null;
		BigDecimal bdCast;
		
		Map<String, Object> queryResp = this.dao.listaConsultaConstate(iCodConstante);
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
			
			///V_VALCONS, V_NOMCONS,N_IDCONSSUPE, N_IDCONS
			
			for(Map<String, Object> map : lista) {
				item = new Constante();																	
				item.setV_valcons("V_VALCONS");
				item.setV_nomcons("V_NOMCONS");
				
				bdCast = (BigDecimal)map.get("N_IDCONSSUPE");
				item.setIdconstantesuper(bdCast.longValueExact());
				
				bdCast = (BigDecimal)map.get("N_IDCONS");
				item.setIdconstante(bdCast.longValueExact());				
				
				listaConstantes.add(item);
			}
			
			
			
			respuesta.put("estado", 1);
			respuesta.put("error", "Ejecución correcta."); //error=cRESP_SP
			respuesta.put("resultado", listaConstantes); //resultado=bRESP			
		}
		return respuesta;		
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> ListaCursosObligatorios(Map<String, String> RequestParm) {
		String aux;
		int iRespuesta = 0;
		
	
		
		aux= ((String)RequestParm.get("codigo")).toUpperCase();
		Long iCodConstante = Long.parseLong(aux);
		
		aux= ((String)RequestParm.get("obligatorio")).toUpperCase();
		Long iObligatorio = Long.parseLong(aux);
		
		System.out.println(iCodConstante);
		
		Map<String,Object> respuesta = new TreeMap<String,Object>();
		
		Cursos item = null;
		List<Cursos> listaCursos = new ArrayList<>();

		String sRespuesta = null;
		BigDecimal bdCast;

		
		
		Map<String, Object> queryResp = this.dao.cargaCursosObligatorios(iCodConstante, iObligatorio);
		
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
			
			///V_VALCONS, V_NOMCONS,N_IDCONSSUPE, N_IDCONS
			
			for(Map<String, Object> map : lista) {
				item = new Cursos();
								
				bdCast= (BigDecimal)map.get("N_ROLAUDI");
				item.setIdRol(bdCast.longValueExact()); 		
								
				item.setNombre((String)map.get("V_DESCCURS"));
				
				bdCast = (BigDecimal)map.get("N_IDCURSO_AUDITORIA");
				item.setId(bdCast.longValueExact());				
				item.setObligatorio(1);
				listaCursos.add(item);
			}
			
			
			
			respuesta.put("estado", 1);
			respuesta.put("error", "Ejecución correcta."); //error=cRESP_SP
			respuesta.put("resultado", listaCursos); //resultado=bRESP			
		}
		return respuesta;		
	}

	@Override
	public List<FichaAuditor> obtenerListaAuditores(AuditorRequest auditoriaRequest, PageRequest paginaRequest) {
		// TODO Auto-generated method stub
		return this.dao.obtenerListaAuditores(auditoriaRequest, paginaRequest);
	}

	@Override
	public Paginacion getPaginacion() {
		// TODO Auto-generated method stub
		return this.dao.getPaginacion();
	}

	@Override
	public Error getError() {
		// TODO Auto-generated method stub
		return this.dao.getError();
	}	
}

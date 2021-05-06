package pe.com.sedapal.agi.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.util.UConstante;
	
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.com.sedapal.agi.dao.IBancoPreguntaDAO;
import pe.com.sedapal.agi.model.Constante;
import pe.com.sedapal.agi.model.Pregunta;
import pe.com.sedapal.agi.model.Revision;
import pe.com.sedapal.agi.service.IBancoPreguntaService;


	@Service
	public class BancoPreguntaService implements IBancoPreguntaService{

		@Autowired
		private IBancoPreguntaDAO dao;
		
		private static final Logger LOGGER = Logger.getLogger(BancoPreguntaService.class);	
		
		@Override
		public List<Pregunta> ListaBancoPreguntas(Pregunta pregunta, PageRequest pageRequest) {	
			List<Pregunta> lista = this.dao.ListaBancoPreguntas(pregunta,  pageRequest);
			return lista;
			
		}
		
		public Paginacion getPaginacion() {
			return this.dao.getPaginacion();
		}
				

		@Override
		public Map<String, Object> GuardarBancoPreguntas(Pregunta objPregunta) {
			int iRespuesta = 0;
			//List<Pregunta> listaBandejaPregunta = new ArrayList<>();
			Map<String,Object> respuesta = new TreeMap<String,Object>();
			
			String sRespuesta = null;
			BigDecimal bdCast;
			//String variable;					
			Map<String, Object> queryResp = this.dao.GuardarBancoPregunta(objPregunta);		
			BigDecimal codRespuesta = (BigDecimal)queryResp.get("L_ID");
			bdCast = (BigDecimal)queryResp.get("o_retorno");
			// Pregunta item;
			
			
			iRespuesta = bdCast.intValueExact();
			if (iRespuesta <= 0) {
				sRespuesta = (String)queryResp.get("o_mensaje");
				respuesta.put("estado", 0); //Estado=nRESP_SP
				respuesta.put("error", sRespuesta);
				respuesta.put("resultado", "");
				respuesta.put("paginacion","");
			}else {
				
				respuesta.put("estado", 1);
				respuesta.put("error", "Ejecución correcta."); //error=cRESP_SP
				respuesta.put("resultado", codRespuesta); //resultado=bRESP
				respuesta.put("paginacion", "");
					
				}									

			return respuesta;
		
	}

		@Override
		public Boolean EliminarPregunta(Long codigo) {
	        return this.dao.eliminarPregunta(codigo);
	        
		}
		
		
		
/*
		@Override
		public Map<String, Object> EliminarPregunta(Long codigo) {
			Map<String,Object> respuesta = new TreeMap<String,Object>();			
			Map<String, Object> queryResp = this.dao.eliminarPregunta(codigo);			
			BigDecimal bdCast = (BigDecimal)queryResp.get("o_retorno");
	        int iRespuesta = bdCast.intValueExact();
	        return this.dao.eliminarPregunta(codigo);
	        
	        
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

*/		
		
		

		@SuppressWarnings("unchecked")
		@Override
		public Map<String, Object> ObtenerPreguntaDatos(Map<String, String> RequestParm) {
			String aux;		
			String cod = ((String)RequestParm.get("cod")).toUpperCase();
			if (((String)RequestParm.get("cod")).isEmpty() || ((String)RequestParm.get("cod"))==null) { aux=""; }
			else {aux=((String)RequestParm.get("cod")).toUpperCase();}
			System.out.println(aux);
				
			int iRespuesta = 0;
			//BigDecimal totalRegistros=new BigDecimal('0');
			Map<String,Object> respuesta = new TreeMap<String,Object>();			
			Pregunta item = null;
			String sRespuesta = null;
			BigDecimal bdCast;
			String variable;		
			List<Pregunta> listaBandejaPregunta = new ArrayList<>();			
			Map<String, Object> queryResp = this.dao.ObtenerPregunta(cod);	
			List<Map<String, Object>> lista = (List<Map<String, Object>>)queryResp.get("o_cursor");
			bdCast = (BigDecimal)queryResp.get("o_retorno");
			
			
			
			iRespuesta = bdCast.intValueExact();
			if (iRespuesta <= 0) {
				sRespuesta = (String)queryResp.get("o_mensaje");
				respuesta.put("estado", 0);
				respuesta.put("error", sRespuesta);
				respuesta.put("resultado", "");
				respuesta.put("paginacion","");
			}else {
				
				for(Map<String, Object> map : lista) {
					item = new Pregunta();
					
					//totalRegistros=(BigDecimal)map.get("RESULT_COUNT");
					
					item.setiD(((BigDecimal)map.get("ID")).longValue());
					
					variable = (String)map.get("PREGUNTA"); 
					item.setPregunta(variable);
					
					variable=(String)map.get("AUDITOR_LIDER");
					item.setAuditorLider(variable);
					
					variable=(String)map.get("AUDITOR_LIDER_INTERNO");
					item.setAuditorLiderInterno(variable);
					
					variable=(String)map.get("AUDITOR_INTERNO");
					item.setAuditorInterno(variable);
					
					variable=(String)map.get("AUDITOR_OBSERVADOR");
					item.setAuditorObservador(variable);
					
					variable=(String)map.get("ESTADO");
					item.setEstado(variable);
									
					bdCast=(BigDecimal)map.get("RNUM");					
					item.setrNum(bdCast.intValueExact());
					
					bdCast=(BigDecimal)map.get("RESULT_COUNT");					
					item.setTotal(bdCast.intValueExact());
					
					listaBandejaPregunta.add(item);
					
				}						
				
				respuesta.put("estado", 1);
				respuesta.put("error", "Ejecución correcta."); //error=cRESP_SP
				respuesta.put("resultado", listaBandejaPregunta.get(0)); //resultado=bRESP
			}
			return respuesta;
		}


		
		
		
		
		@Override
		public Map<String, Object> ActualizarBancoPreguntas(Pregunta pregunta) {	int iRespuesta = 0;
			//List<Pregunta> listaBandejaPregunta = new ArrayList<>();
			Map<String,Object> respuesta = new TreeMap<String,Object>();
			
			String sRespuesta = null;
			BigDecimal bdCast;
			//String variable;					
			Map<String, Object> queryResp = this.dao.ActualizarBancoPregunta(pregunta);		
			BigDecimal codRespuesta = (BigDecimal)queryResp.get("o_retorno");
			bdCast = (BigDecimal)queryResp.get("o_retorno");
			// Pregunta item;
			
			
			iRespuesta = bdCast.intValueExact();
			if (iRespuesta <= 0) {
				sRespuesta = (String)queryResp.get("o_mensaje");
				respuesta.put("estado", 0); //Estado=nRESP_SP
				respuesta.put("error", sRespuesta);
				respuesta.put("resultado", "");
				respuesta.put("paginacion","");
			}else {
				
				respuesta.put("estado", 1);
				respuesta.put("error", "Ejecución correcta."); //error=cRESP_SP
				respuesta.put("resultado", codRespuesta); //resultado=bRESP
				respuesta.put("paginacion", "");
					
				}									

			return respuesta;
		
		}
		
	//ruth
		@Override
		public List<Constante> buscarRoles(String descripcion){
			// TODO Auto-generated method stub
			return dao.buscarRoles(descripcion);
			
		}
		
	
		
		@Override
		public Error getError() {
			return dao.getError();
		}
		
		
		
}		
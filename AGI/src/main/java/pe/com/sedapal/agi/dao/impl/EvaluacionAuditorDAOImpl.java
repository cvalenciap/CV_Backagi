package pe.com.sedapal.agi.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import oracle.jdbc.OracleTypes;
import pe.com.gmd.util.exception.MensajeExceptionUtil;
import pe.com.sedapal.agi.dao.IEvaluacionAuditorDAO;
import pe.com.sedapal.agi.model.Auditoria;
import pe.com.sedapal.agi.model.Colaborador;
import pe.com.sedapal.agi.model.EvaluacionAuditor;
import pe.com.sedapal.agi.model.Pregunta;
import pe.com.sedapal.agi.model.request_objects.EvaluacionAuditorRequest;
import pe.com.sedapal.agi.model.request_objects.PageRequest;
import pe.com.sedapal.agi.model.response_objects.Error;
import pe.com.sedapal.agi.model.response_objects.Paginacion;
import pe.com.sedapal.agi.util.UConstante;

@Repository
public class EvaluacionAuditorDAOImpl implements IEvaluacionAuditorDAO{
  
	@Autowired
	private JdbcTemplate	jdbc;	
	private SimpleJdbcCall	jdbcCall;
	private Paginacion		paginacion;
	private Error			error;
	
	private static final Logger LOGGER = Logger.getLogger(EvaluacionAuditorDAOImpl.class);	
	

	//la lista de la Grilla
	   
		@Override
		public List<EvaluacionAuditor> obtenerListaEvaAuditorGrilla(EvaluacionAuditorRequest evaluacionAuditorRequest, PageRequest pageRequest) {
			// TODO Auto-generated method stub
			Map<String, Object> out	= null;
			List<EvaluacionAuditor> listaEvaluacionAuditor		= new ArrayList<>();		
			this.paginacion = new Paginacion();
			this.error		= null;
			paginacion.setPagina(pageRequest.getPagina());
			paginacion.setRegistros(pageRequest.getRegistros());
			
			  
			this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
						.withSchemaName("AGI")
						.withCatalogName("PCK_AGI_AUDITORIA")
						.withProcedureName("PRC_EVA_AUDITOR_OBTENER")
						.declareParameters(
							new SqlParameter("v_numero_ficha", 			OracleTypes.VARCHAR),										
							new SqlParameter("v_apellido_pat", 	OracleTypes.VARCHAR),
							new SqlParameter("v_apellido_mat", 	OracleTypes.VARCHAR),
							new SqlParameter("v_nombre", 	OracleTypes.VARCHAR),
						
							
							new SqlParameter("i_npagina", 	OracleTypes.NUMBER),
							new SqlParameter("i_nregistros", 	OracleTypes.NUMBER),
							new SqlOutParameter("o_cursor",	OracleTypes.CURSOR),						
							new SqlOutParameter("o_retorno",	OracleTypes.NUMBER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));	
		  
			       
			SqlParameterSource in =
		    		new MapSqlParameterSource()
		    			.addValue("v_numero_ficha",			evaluacionAuditorRequest.getV_numero_ficha())
		    			.addValue("v_apellido_pat",		evaluacionAuditorRequest.getV_apellido_pat())  
		    			.addValue("v_apellido_mat",		evaluacionAuditorRequest.getV_apellido_mat())  
		    			.addValue("v_nombre",		evaluacionAuditorRequest.getV_nombre()) 
		    			.addValue("i_npagina",		pageRequest.getPagina())
		    			.addValue("i_nregistros",	pageRequest.getRegistros());

			 
			try {
				out = this.jdbcCall.execute(in);
				Integer resultado = ((BigDecimal)out.get("o_retorno")).intValue();			
				if(resultado == 0) {
					listaEvaluacionAuditor = this.mapearEvaluacionAuditorGrilla(out);
				} else {
					String mensaje			= (String)out.get("o_mensaje");  
					String mensajeInterno	= (String)out.get("o_sqlerrm");
					this.error				= new Error(resultado,mensaje,mensajeInterno);
				}
				
			}catch(Exception e) {
				Integer resultado		= 1;
				String mensaje			= "Error en EvaluacionAuditorDAOImpl.obtenerListaEvaluacionAuditorGrilla";
				String mensajeInterno	= e.getMessage();
				this.error = new Error(resultado,mensaje,mensajeInterno);
			}
			
			return listaEvaluacionAuditor;
		}
		  
		
		
		

		private List<EvaluacionAuditor> mapearEvaluacionAuditorGrilla(Map<String,Object> resultados){
			List<EvaluacionAuditor> listaEvaluacionAuditor = new ArrayList<>();
			List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
			EvaluacionAuditor item = null;
			int size = lista.size();  
			
			for(Map<String, Object> map : lista) {		
				item = new EvaluacionAuditor();
		
				if(map.get("N_IDEVALAUDI")!=null) {
					item.setIdEvaluacionAuditor(((BigDecimal) map.get("N_IDEVALAUDI")).longValue());
				}
				
				
				Auditoria auditoria =new Auditoria();
				
				if(map.get("NROAUDITORIA")!=null) {  
					auditoria.setIdAuditoria(((BigDecimal) map.get("NROAUDITORIA")).longValue());
				}
				else {					
					auditoria.setIdAuditoria(new BigDecimal(0).longValue());
				}	
				
				if(map.get("DFECINIC")!=null) {
					auditoria.setFechaInicio(((Date) map.get("DFECINIC")));		
				}

				item.setAuditoria(auditoria);
				if(map.get("APELLIDOPAT")!=null) {
					Colaborador colaborador =new Colaborador(); 
					//colaborador.setNumeroFicha(((Long) map.get("NROFICHA"))); 
					colaborador.setNombre((String) map.get("NOMBRES"));	
					colaborador.setApellidoPaterno((String) map.get("APELLIDOPAT"));
					colaborador.setApellidoMaterno((String) map.get("APELLIDOMAT"));					
					colaborador.setIdRolAuditor (((BigDecimal) map.get("ROL")).longValue());

					
					
					item.setColaborador(colaborador);
				}    
				listaEvaluacionAuditor.add(item);
				if (this.paginacion != null ) {
					this.paginacion.setTotalRegistros(((BigDecimal)map.get("RESULT_COUNT")).intValue());
				}				
			}		

			return listaEvaluacionAuditor;		
		}
		
		  
		//******************************************************************//
		
		//obtener uno 
				@Override
				public EvaluacionAuditor obtenerEvaluacionAuditor(EvaluacionAuditorRequest evaluacionAuditorRequest) {
					// TODO Auto-generated method stub 
					Map<String, Object> out	= null;
					List<EvaluacionAuditor> listaEvaluacionAuditor		= new ArrayList<>();	
					
					
					EvaluacionAuditor evaluacionAuditor=new EvaluacionAuditor();
					
					this.paginacion = new Paginacion();

					this.error		= null;
				
					
					  
					this.jdbcCall =
							new SimpleJdbcCall(this.jdbc)
								.withSchemaName("AGI")
								.withCatalogName("PCK_AGI_AUDITORIA")
								.withProcedureName("PRC_EVAAUD_OBTENER")
								.declareParameters(
									new SqlParameter("v_numero_auditoria",OracleTypes.VARCHAR),										
									
									
									new SqlOutParameter("o_cursor",	OracleTypes.CURSOR),						
									new SqlOutParameter("o_retorno",	OracleTypes.NUMBER),
									new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
									new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));	
				  
					       
					SqlParameterSource in =
				    		new MapSqlParameterSource()
				    			.addValue("v_numero_auditoria",	evaluacionAuditorRequest.getV_numero_auditoria());
				    	

					   
					try {
						out = this.jdbcCall.execute(in);
						Integer resultado = ((BigDecimal)out.get("o_retorno")).intValue();			
						if(resultado == 0) {
							listaEvaluacionAuditor = this.mapearEvaluacionAuditor(out);
							
							if(listaEvaluacionAuditor.size()>0) {
								evaluacionAuditor = listaEvaluacionAuditor.get(0);
								//logger.msgInfoInicio("ID AUDITORIA OBJETO EVALUACIONAUDITOR-->"+listaEvaluacionAuditor.get(0).getAuditoria().getIdAuditoria());
								
								
							}
							
						} else {
							String mensaje			= (String)out.get("o_mensaje");  
							String mensajeInterno	= (String)out.get("o_sqlerrm");
							this.error				= new Error(resultado,mensaje,mensajeInterno);
							
						}
						
					}catch(Exception e) {
						Integer resultado		= 1;  
						String mensaje			= "Error en EvaluacionAuditorDAOImpl.obtenerEvaluacionAuditor";
						String mensajeInterno	= e.getMessage();
						String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
						LOGGER.error(error[1], e);
						this.error = new Error(resultado,mensaje,mensajeInterno);
					}
					 
					return evaluacionAuditor;
				}
				  
				
				private List<EvaluacionAuditor> mapearEvaluacionAuditor(Map<String,Object> resultados){
					List<EvaluacionAuditor> listaEvaluacionAuditor = new ArrayList<>();
					List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
					EvaluacionAuditor item = null;
					int size = lista.size();  
					
					for(Map<String, Object> map : lista) {		
						item = new EvaluacionAuditor();
				
						if(map.get("N_IDEVALAUDI")!=null) {

							item.setIdEvaluadi(((BigDecimal) map.get("N_IDEVALAUDI")).longValue());
						}
						else {
							  
							item.setIdEvaluadi(new BigDecimal(0).longValue());
						}
						
						
						Auditoria auditoria =new Auditoria();
		
						if(map.get("NROAUDITORIA")!=null) {

							auditoria.setIdAuditoria(((BigDecimal) map.get("NROAUDITORIA")).longValue());
						}
						else {
							
							auditoria.setIdAuditoria(new BigDecimal(0).longValue());
						}
						
						if(map.get("DESCRIPCIONAUD")!=null) {

							auditoria.setDescripcionAuditoria((String) map.get("DESCRIPCIONAUD"));
						}
						else {
							
							auditoria.setDescripcionAuditoria("");
						}

						if(map.get("DFECINIC")!=null) {
							auditoria.setFechaInicio(((Date) map.get("DFECINIC"))); 													
						}
						item.setAuditoria(auditoria);
	
					
						
						
						if(map.get("APELLIDOPAT")!=null) {
							Colaborador colaborador =new Colaborador();
							colaborador.setNombre((String) map.get("NOMBRES"));	
							colaborador.setApellidoPaterno((String) map.get("APELLIDOPAT"));
							colaborador.setApellidoMaterno((String) map.get("APELLIDOMAT"));					
							colaborador.setIdRolAuditor (((BigDecimal) map.get("ROL")).longValue());

							item.setColaborador(colaborador);
						}
						
						if(map.get("HORAS")!=null) {
							item.setTotalHoras(((BigDecimal) map.get("HORAS")).longValue()); 	

						}
		    
						listaEvaluacionAuditor.add(item);
						
						
						
					}		

					return listaEvaluacionAuditor;		
				}
				   
		//**********************************GRABAR EVALUACION AUDITOR*************************************************************/		
			   //este de aqui ya no uso
				@Override
				public Boolean registrarEvaluacionAuditor(EvaluacionAuditor evaluacionAuditor) {
					       
			     
					Map<String, Object> out = null;
					this.error = null;
					this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
						.withSchemaName("AGI")
						.withCatalogName("PCK_AGI_AUDITORIA")
						.withProcedureName("PRC_EVA_AUDITOR_GUARDAR")
						.declareParameters(
							new SqlParameter("i_v_usuario",			OracleTypes.VARCHAR),
							
							new SqlParameter("i_idevalaudi",		OracleTypes.NUMBER),							
							new SqlParameter("i_idpregaudi",		OracleTypes.NUMBER),
							//new SqlParameter("i_idrptaeval",		OracleTypes.NUMBER),
							new SqlParameter("i_rptapreg",		OracleTypes.VARCHAR),
							new SqlParameter("totalHoras",		OracleTypes.NUMBER), 
					 
							new SqlParameter("i_estrptpreg",		OracleTypes.VARCHAR),
							
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
					
					SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_v_usuario",	evaluacionAuditor.getUsuarioCreador())				
						.addValue("i_idevalaudi",	evaluacionAuditor.getIdEvaluadi())
						.addValue("i_idpregaudi",	evaluacionAuditor.getIdPregAudi())
						
						//.addValue("i_idrptaeval",	evaluacionAuditor.getIdEvaluacionAuditor())	
						.addValue("i_rptapreg",	evaluacionAuditor.getRespuestaNivelPreg())	
						.addValue("totalHoras",	evaluacionAuditor.getTotalHoras())
						.addValue("i_estrptpreg",	evaluacionAuditor.getEstado());
							
					
 
					try {
						out = this.jdbcCall.execute(in);
						Integer resultado = (Integer)out.get("o_retorno");
						
						if(resultado == 0) {
							return true;
						} else {
							String mensaje			= (String)out.get("o_mensaje");
							String mensajeInterno	= (String)out.get("o_sqlerrm");
							this.error				= new Error(resultado,mensaje,mensajeInterno);
							return false;
						}
					}catch(Exception e){
						Integer resultado		= 1;
						String mensaje			= "Error en EvaluacionAuditorDAOImpl.registrarCodigoAnterior";
						String mensajeInterno	= e.getMessage();
						this.error				= new Error(resultado,mensaje,mensajeInterno);
						return false;
					}
					
					
				}
			
				
	//************************************************************************************************************************//
				   
   //**********************************GRABAR EVALUACION AUDITOR*************************************************************//		
						     
				       public Boolean registrarEvaluacion(EvaluacionAuditor evaluacionAuditor,Pregunta pregunta) {
				    	   
				    		Map<String, Object> out = null;
							this.error = null;
							this.jdbcCall =
							new SimpleJdbcCall(this.jdbc)
								.withSchemaName("AGI")
								.withCatalogName("PCK_AGI_AUDITORIA")
								.withProcedureName("PRC_EVA_AUDITOR_GUARDAR")
								.declareParameters(
									new SqlParameter("i_v_usuario",			OracleTypes.VARCHAR),							
									new SqlParameter("i_idevalaudi",		OracleTypes.NUMBER),																			
									new SqlParameter("i_idpregaudi",		OracleTypes.NUMBER),						
									new SqlParameter("i_rptapreg",		OracleTypes.VARCHAR),																		
									new SqlParameter("i_estrptpreg",		OracleTypes.VARCHAR),								
									new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
									new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
									new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
							
							SqlParameterSource in =
							new MapSqlParameterSource()
								.addValue("i_v_usuario",	evaluacionAuditor.getUsuarioCreador())				
								.addValue("i_idevalaudi",	evaluacionAuditor.getIdEvaluadi())
								
								//.addValue("i_idpregaudi",	evaluacionAuditor.getIdPregAudi())
								.addValue("i_idpregaudi",	pregunta.getiD())
								.addValue("i_rptapreg",	pregunta.getrNum())	
								.addValue("i_estrptpreg",	"1")
								.addValue("totalHoras",	evaluacionAuditor.getTotalHoras());
									
							
						
		
							try {
								out = this.jdbcCall.execute(in);
								Integer resultado = (Integer)out.get("o_retorno");
								
								if(resultado == 0) {
									return true;
								} else {
									String mensaje			= (String)out.get("o_mensaje");
									String mensajeInterno	= (String)out.get("o_sqlerrm");
									this.error				= new Error(resultado,mensaje,mensajeInterno);
									
									System.out.println("AGI - Error EvaluacionAuditorDAOImpl.registrarEvaluacionAuditor() entro a l else");
									return false;
								}
							}catch(Exception e){
								Integer resultado		= 1;
								String mensaje			= "Error en EvaluacionAuditorDAOImpl.registrarCodigoAnterior";
								String mensajeInterno	= e.getMessage();
								this.error				= new Error(resultado,mensaje,mensajeInterno);
								String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
								LOGGER.error(error[1], e);
								return false;
							}
							
				    	   
				    	   
				       }
				
				
						@Override
						public Boolean registrarEvaluacionAuditor(EvaluacionAuditor evaluacionAuditor, List<Pregunta> listaPreguntas) {
				
							  	for(int i=0;listaPreguntas.size()>i;i++) {
								
							  	registrarEvaluacion( evaluacionAuditor, listaPreguntas.get(i));
						
			
							}//fin del for
							return true;
							//return true;
						}
		
						
		//*******************************************************************************************************************//
		//*****************************ACTUALIZAR RESPUESTAS proc************************************************************/
						
										
										 public Boolean actualizarRespuestas(EvaluacionAuditor evaluacionAuditor,Pregunta pregunta) {
									    	   
									    		Map<String, Object> out = null;
												this.error = null;
												this.jdbcCall =
												new SimpleJdbcCall(this.jdbc)
													.withSchemaName("AGI")
													.withCatalogName("PCK_AGI_AUDITORIA")
													.withProcedureName("PRC_EVA_AUDITOR_ACTUALIZAR")
													.declareParameters(
														new SqlParameter("i_v_usuario",			OracleTypes.VARCHAR),							
														new SqlParameter("i_idevalaudi",		OracleTypes.NUMBER),																			
														new SqlParameter("i_idpregaudi",		OracleTypes.NUMBER),						
														new SqlParameter("i_rptapreg",		OracleTypes.VARCHAR),																		
														new SqlParameter("i_estrptpreg",		OracleTypes.VARCHAR),
														new SqlParameter("totalHoras",		OracleTypes.NUMBER),
														new SqlParameter("v_id",		OracleTypes.NUMBER),
														
														new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
														new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
														new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
												
												SqlParameterSource in =
												new MapSqlParameterSource()
													.addValue("i_v_usuario",	evaluacionAuditor.getUsuarioCreador())				
													.addValue("i_idevalaudi",	evaluacionAuditor.getIdEvaluadi())																									
													.addValue("i_idpregaudi",	pregunta.getiD())
													.addValue("i_rptapreg",	pregunta.getRadioNum())	
													.addValue("i_estrptpreg",	"1")
													.addValue("totalHoras",	evaluacionAuditor.getTotalHoras())
													
													
													
													.addValue("v_id",	evaluacionAuditor.getIdEvaluacionAuditor());												    
							
												System.out.println("evaluacionAuditor.getIdEvaluacionAuditor()--->"+evaluacionAuditor.getIdEvaluacionAuditor());
												System.out.println("pregunta.getiD()-->"+pregunta.getiD());
												System.out.println("pregunta.getRadioNum()-->"+pregunta.getRadioNum());
												
												try {
													out = this.jdbcCall.execute(in);
													Integer resultado = (Integer)out.get("o_retorno");
													
													if(resultado == 0) {
														return true;
													} else {
														String mensaje			= (String)out.get("o_mensaje");
														String mensajeInterno	= (String)out.get("o_sqlerrm");
														this.error				= new Error(resultado,mensaje,mensajeInterno);														
														System.out.println("AGI - Error EvaluacionAuditorDAOImpl.actualizarRespuestas() ");
														return false;
													}
												}catch(Exception e){
													Integer resultado		= 1;
													String mensaje			= "Error en EvaluacionAuditorDAOImpl.actualizarRespuestas";
													String mensajeInterno	= e.getMessage();
													this.error				= new Error(resultado,mensaje,mensajeInterno);
													String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
													LOGGER.error(error[1], e);
													return false;
												}
																				
									       }
						
											@Override
											public Boolean actualizarEvaluacionAuditor(EvaluacionAuditor evaluacionAuditor, List<Pregunta> listaPreguntas) {
											
												  	for(int i=0;listaPreguntas.size()>i;i++) {												
												  		actualizarRespuestas( evaluacionAuditor, listaPreguntas.get(i));
												}//fin del for
												return true;
												//return true;
											}			 
								
						
		//****************************OBTENER RESPUESTAS SEGUN ID EVALUACION**********************************************************/
						
						@Override
						public List<Pregunta> obtenerListaAspectosSegunIdEva(EvaluacionAuditorRequest evaluacionAuditorRequest, PageRequest pageRequest) {
							// TODO Auto-generated method stub
							Map<String, Object> out	= null;
							List<Pregunta> listaAspectos		= new ArrayList<>();		
							this.paginacion = new Paginacion();
							this.error		= null;
							paginacion.setPagina(pageRequest.getPagina());
							paginacion.setRegistros(pageRequest.getRegistros());
							
							System.out.println(evaluacionAuditorRequest.getN_rol_auditor());
							System.out.println(evaluacionAuditorRequest.getIdEvaluacionAuditor());
							
							System.out.println(pageRequest.getPagina());
							System.out.println(pageRequest.getRegistros());
							
							  
							this.jdbcCall =
									new SimpleJdbcCall(this.jdbc)
										.withSchemaName("AGI")
										.withCatalogName("PCK_AGI_AUDITORIA")
										.withProcedureName("PRC_ASPECTOS_OBTENER_RES")
										.declareParameters(
											new SqlParameter("n_rol_auditor", 			OracleTypes.VARCHAR),										
											new SqlParameter("n_idEvaluacion", 			OracleTypes.VARCHAR),	
											
											new SqlParameter("i_npagina", 	OracleTypes.NUMBER),
											new SqlParameter("i_nregistros", 	OracleTypes.NUMBER),
											new SqlOutParameter("o_cursor",	OracleTypes.CURSOR),						
											new SqlOutParameter("o_retorno",	OracleTypes.NUMBER),
											new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
											new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));	
						  
							         
							SqlParameterSource in =
						    		new MapSqlParameterSource()
						    			.addValue("n_rol_auditor",			evaluacionAuditorRequest.getN_rol_auditor())
						    			.addValue("n_idEvaluacion",			evaluacionAuditorRequest.getIdEvaluacionAuditor())	
						    			
						    			.addValue("i_npagina",		pageRequest.getPagina())
						    			.addValue("i_nregistros",	pageRequest.getRegistros());

							 
							try {
								out = this.jdbcCall.execute(in);
								Integer resultado = ((BigDecimal)out.get("o_retorno")).intValue();			
								if(resultado == 0) {
									listaAspectos = this.mapearAspectosRes(out); 
								} else {
									String mensaje			= (String)out.get("o_mensaje");  
									String mensajeInterno	= (String)out.get("o_sqlerrm");
									this.error				= new Error(resultado,mensaje,mensajeInterno);
								}
								
							}catch(Exception e) {
								Integer resultado		= 1; 
								String mensaje			= "Error en EvaluacionAuditorDAOImpl.obtenerListaAspectos";
								String mensajeInterno	= e.getMessage();
								this.error = new Error(resultado,mensaje,mensajeInterno);
							}
							
							return listaAspectos;
						}
						  
			//			
		
      //******************************buscar respuestas por codigo de evaluacionAuditor12022019***************************/
						
						
							@Override
							public List<Pregunta> buscarResPorCodEvaluacionAuditor(EvaluacionAuditorRequest evaluacionAuditorRequest) {
								// TODO Auto-generated method stub
								Map<String, Object> out	= null;
								List<Pregunta> listaAspectos		= new ArrayList<>();		
								
								this.jdbcCall =
										new SimpleJdbcCall(this.jdbc)
											.withSchemaName("AGI")
											.withCatalogName("PCK_AGI_AUDITORIA")
											.withProcedureName("PRC_ASPECTOS_OBTENER_COUNT")
											.declareParameters(
												new SqlParameter("n_idEvaluacion", 			OracleTypes.VARCHAR),										
											
												new SqlOutParameter("o_cursor",	OracleTypes.CURSOR),						
												new SqlOutParameter("o_retorno",	OracleTypes.NUMBER),
												new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
												new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));	
							  
								       
								SqlParameterSource in = new MapSqlParameterSource()
							    			.addValue("n_idEvaluacion",			evaluacionAuditorRequest.getIdEvaluacionAuditor());
							    		
								 
								try {
									out = this.jdbcCall.execute(in);
									Integer resultado = ((BigDecimal)out.get("o_retorno")).intValue();			
									if(resultado == 0) {
										listaAspectos = this.mapearAspectosRes(out);
									} else {
										String mensaje			= (String)out.get("o_mensaje");  
										String mensajeInterno	= (String)out.get("o_sqlerrm");
										this.error				= new Error(resultado,mensaje,mensajeInterno);
									}
									
								}catch(Exception e) {
									Integer resultado		= 1; 
									String mensaje			= "Error en EvaluacionAuditorDAOImpl.buscarResPorCodEvaluacionAuditor";
									String mensajeInterno	= e.getMessage();
									this.error = new Error(resultado,mensaje,mensajeInterno);
								}
								
								return listaAspectos;
							}
							  
						
		
				
		//*******************************ELIMINAR EVALUACION AUDITOR*************************************/
				
				
				@Override
				public Boolean eliminarEvaluacionAuditor(Long codigo, String usuario) {					
					Map<String, Object> out = null;
					this.error = null; 
					this.jdbcCall =
					new SimpleJdbcCall(this.jdbc)
						.withSchemaName("AGI") 
						.withCatalogName("PCK_AGI_AUDITORIA")
						.withProcedureName("PRC_EVA_AUDITOR_ELIMINAR")
						.declareParameters(    
							new SqlParameter("i_nid",			OracleTypes.NUMBER),
							new SqlParameter("i_vusuario",		OracleTypes.VARCHAR),
							new SqlOutParameter("o_retorno",	OracleTypes.INTEGER),
							new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
							new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));
					  
					SqlParameterSource in =
					new MapSqlParameterSource()
						.addValue("i_nid",codigo)
						.addValue("i_vusuario",	usuario);

					try {
						out = this.jdbcCall.execute(in);
						Integer resultado = (Integer)out.get("o_retorno");
						
						if(resultado == 0) {							
							return true;
						} else {
							String mensaje			= (String)out.get("o_mensaje");
							String mensajeInterno	= (String)out.get("o_sqlerrm");
							this.error				= new Error(resultado,mensaje,mensajeInterno);							
							return false;
						}
					}catch(Exception e){
						Integer resultado		= 1;
						String mensaje			= "Error en EvaluacionAuditorDAOImpl.eliminarEvaluacionAuditor";
						String mensajeInterno	= e.getMessage();
						this.error				= new Error(resultado,mensaje,mensajeInterno);
						String[] error = MensajeExceptionUtil.obtenerMensajeError(e);
						LOGGER.error(error[1], e);
						return false;
					}
				}
				
				
				
		//*********************************para la lista de aspectos a evaluar***************************/
				
				  
				@Override
				public List<Pregunta> obtenerListaAspectos(EvaluacionAuditorRequest evaluacionAuditorRequest, PageRequest pageRequest) {
					// TODO Auto-generated method stub
					Map<String, Object> out	= null;
					List<Pregunta> listaAspectos		= new ArrayList<>();		
					this.paginacion = new Paginacion();
					this.error		= null;
					paginacion.setPagina(pageRequest.getPagina());
					paginacion.setRegistros(pageRequest.getRegistros());
					
					  
					this.jdbcCall =
							new SimpleJdbcCall(this.jdbc)
								.withSchemaName("AGI")
								.withCatalogName("PCK_AGI_AUDITORIA")
								.withProcedureName("PRC_ASPECTOS_OBTENER")
								.declareParameters(
									new SqlParameter("n_rol_auditor", 			OracleTypes.VARCHAR),										
									new SqlParameter("i_npagina", 	OracleTypes.NUMBER),
									new SqlParameter("i_nregistros", 	OracleTypes.NUMBER),
									new SqlOutParameter("o_cursor",	OracleTypes.CURSOR),						
									new SqlOutParameter("o_retorno",	OracleTypes.NUMBER),
									new SqlOutParameter("o_mensaje",	OracleTypes.VARCHAR),
									new SqlOutParameter("o_sqlerrm",	OracleTypes.VARCHAR));	
				  
					       
					SqlParameterSource in =
				    		new MapSqlParameterSource()
				    			.addValue("n_rol_auditor",			evaluacionAuditorRequest.getN_rol_auditor())				   			
				    			.addValue("i_npagina",		pageRequest.getPagina())
				    			.addValue("i_nregistros",	pageRequest.getRegistros());

					               System.out.println("evaluacionAuditorRequest.getN_rol_auditor()-->"+evaluacionAuditorRequest.getN_rol_auditor());
					               System.out.println("pageRequest.getPagina()-->"+pageRequest.getPagina());							
					               System.out.println("pageRequest.getRegistros()-->"+pageRequest.getRegistros());
									
					 
					try {
						out = this.jdbcCall.execute(in);
						Integer resultado = ((BigDecimal)out.get("o_retorno")).intValue();			
						if(resultado == 0) {
							listaAspectos = this.mapearAspectos(out);
						} else {
							String mensaje			= (String)out.get("o_mensaje");  
							String mensajeInterno	= (String)out.get("o_sqlerrm");
							this.error				= new Error(resultado,mensaje,mensajeInterno);
						}
						
					}catch(Exception e) {
						Integer resultado		= 1; 
						String mensaje			= "Error en EvaluacionAuditorDAOImpl.obtenerListaAspectos";
						String mensajeInterno	= e.getMessage();
						this.error = new Error(resultado,mensaje,mensajeInterno);
					}
					
					return listaAspectos;
				}
				  
				
				
				

				private List<Pregunta> mapearAspectos(Map<String,Object> resultados){
					List<Pregunta> listaAspectos = new ArrayList<>();
					List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
					Pregunta item = null;
					int size = lista.size();  
					
					for(Map<String, Object> map : lista) {		
						item = new Pregunta();

						

						if(map.get("V_DESCPREG")!=null) {

						
							item.setPregunta(((String) map.get("V_DESCPREG")));
						}
						else {							
							item.setPregunta("");
						}
						
						
						if(map.get("N_IDPREGAUDI")!=null) {

						
							item.setiD(((BigDecimal) map.get("N_IDPREGAUDI")).longValue());
						}
						else {							
							item.setiD(((BigDecimal) map.get(0)).longValue());
						}
						
						listaAspectos.add(item);

					}  
					
					//logger.msgInfoFin("listaAspectos.size()-->"+listaAspectos.size());
					return listaAspectos;		
				}
				
		//´para con respuesta
				private List<Pregunta> mapearAspectosRes(Map<String,Object> resultados){
					List<Pregunta> listaAspectos = new ArrayList<>();
					List<Map<String, Object>> lista = (List<Map<String, Object>>)resultados.get("o_cursor");
					Pregunta item = null;
					int size = lista.size();  
					
					for(Map<String, Object> map : lista) {		
						item = new Pregunta();

						

						if(map.get("V_DESCPREG")!=null) {

						
							item.setPregunta(((String) map.get("V_DESCPREG")));
						}
						else {							
							item.setPregunta("");
						}
						
						 
						if(map.get("N_IDPREGAUDI")!=null) {

						
							item.setiD(((BigDecimal) map.get("N_IDPREGAUDI")).longValue());
						}
						else {							
							item.setiD(((BigDecimal) map.get(0)).longValue());
						}
						
						//nueva columna //va cambiar objeto Evaluacion Auditor
						
						
						if(map.get("RESPUESTA")!=null) {
						   
							item.setRadioNum((String) map.get("RESPUESTA"));
						}
						else {							
							item.setRadioNum("0");
						}
						
						  
						//logger.msgInfoFin("(String) map.get(N_IDPREGAUDI)-->"+(String) map.get("N_IDPREGAUDI"));
						
						listaAspectos.add(item);

					}  
					
					//logger.msgInfoFin("listaAspectos.size()-->"+listaAspectos.size());
					return listaAspectos;		
				}
				
		
		
		
		
		
		
		//******************************************************************//
		public Paginacion getPaginacion() {
			return this.paginacion;
		}	
		
		public Error getError() {
			return this.error;
		}
	
}

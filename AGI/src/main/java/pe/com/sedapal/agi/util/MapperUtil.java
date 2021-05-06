package pe.com.sedapal.agi.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pe.com.sedapal.agi.model.AreaAlcanceAuditoria;
import pe.com.sedapal.agi.model.AreaCargoAuditoria;

public class MapperUtil {

	public List<AreaAlcanceAuditoria> mapearAreaAlcanceAuditoria(String lstAlcances)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();

		List<Object> listaAlcances = objectMapper.readValue(lstAlcances,
				objectMapper.getTypeFactory().constructParametricType(List.class, Object.class));
		List<AreaAlcanceAuditoria> listaAlcancesAuditoria = new ArrayList<AreaAlcanceAuditoria>();

		for (Object obj : listaAlcances) {
			AreaAlcanceAuditoria areaAlcance = new AreaAlcanceAuditoria();
			@SuppressWarnings("unchecked")
			Map<String, Object> item = (Map<String, Object>) obj;
			if(item.get("n_id_alc_area") == null) {
				areaAlcance.setN_id_alc_area(null);
			}else {
				areaAlcance.setN_id_alc_area(Integer.valueOf((item.get("n_id_alc_area").toString())));
			}			
			areaAlcance.setN_id_area(Integer.valueOf((item.get("n_id_area").toString())));
			areaAlcance.setV_cod_alca((String) item.get("v_cod_alca"));
			areaAlcance.setV_abre_alca((String) item.get("v_abre_alca"));
			listaAlcancesAuditoria.add(areaAlcance);
		}
		return listaAlcancesAuditoria;
	}

	public List<AreaCargoAuditoria> mapearAreaCargoAuditoria(String lstCargos)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();

		List<Object> listaCargos = objectMapper.readValue(lstCargos,
				objectMapper.getTypeFactory().constructParametricType(List.class, Object.class));

		List<AreaCargoAuditoria> listaAreaCargoAuditoria = new ArrayList<AreaCargoAuditoria>();

		for (Object obj : listaCargos) {
			AreaCargoAuditoria areaCargoAuditoria = new AreaCargoAuditoria();
			@SuppressWarnings("unchecked")
			Map<String, Object> item = (Map<String, Object>) obj;
			if(item.get("n_id_cargo_area") == null) {
				areaCargoAuditoria.setN_id_cargo_area(null);
			}else {
				areaCargoAuditoria.setN_id_cargo_area(Integer.valueOf((item.get("n_id_cargo_area").toString())));
			}			
			areaCargoAuditoria.setN_id_cargo_sig(Integer.valueOf((item.get("n_id_cargo_sig").toString())));
			areaCargoAuditoria.setN_id_area(Integer.valueOf((item.get("n_id_area").toString())));
			areaCargoAuditoria.setV_nom_cargo_sig((String) item.get("v_nom_cargo_sig"));
			areaCargoAuditoria.setV_sigla((String) item.get("v_sigla"));
			listaAreaCargoAuditoria.add(areaCargoAuditoria);
		}

		return listaAreaCargoAuditoria;

	}

}

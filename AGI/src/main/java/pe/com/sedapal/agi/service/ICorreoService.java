package pe.com.sedapal.agi.service;

import pe.com.sedapal.agi.model.Correo;
import pe.com.sedapal.agi.model.CorreoResponse;

import pe.com.sedapal.agi.service.impl.CorreoServiceImpl;

public interface ICorreoService {
	CorreoServiceImpl devuelveInstancia();
	CorreoResponse enviarCorreoHTML(final Correo correoParametro) throws Exception;
}
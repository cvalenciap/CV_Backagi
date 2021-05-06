package pe.com.sedapal.seguridad.core.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import pe.com.sedapal.seguridad.core.bean.util.TimestampTypeAdapter;

@XmlRootElement
@XmlSeeAlso({ SistemaModuloOpcionBean.class})
public class Retorno implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2137375872639235655L;
	private String codigo;
	private String mensaje;
	private String tipo;
	private String usuario;
	private String clave;
	private String flagCambiarClave;
	private String ultimoAcceso;
	// private List<String> permisos;
	// private List<SistemaModuloOpcionBean> menu;
	
//	inicio adecuacion seguridad2
	private Integer numPerfilesAct;
	private List<PerfilSistemaBean> perfilesAct;
	private String autenticacion;
	private String token;
	private Integer codigoTrabajador;
//	fin adecuacion seguridad2
	
	public Retorno() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Integer getCodigoTrabajador() {
		return codigoTrabajador;
	}


	public void setCodigoTrabajador(Integer codigoTrabajador) {
		this.codigoTrabajador = codigoTrabajador;
	}


	public String getUltimoAcceso() {
		return ultimoAcceso;
	}


	public void setUltimoAcceso(String ultimoAcceso) {
		this.ultimoAcceso = ultimoAcceso;
	}


	public Retorno(String codigo, String mensaje) {
		super();
		this.codigo = codigo;
		this.mensaje = mensaje;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	
//	inicio adecuacion seguridad2
//	@Override
//	public String toString() {
//		return "Retorno [codigo=" + codigo + ", mensaje=" + mensaje + ", tipo=" + tipo + ", usuario=" + usuario
//				+ ", clave=" + clave + ", flagCambiarClave=" + flagCambiarClave + ", ultimoAcceso=" + ultimoAcceso
//				+ "]";
//	}
	
//	@Override
//	public String toString() {
//		return "Retorno [codigo=" + codigo + ", mensaje=" + mensaje + ", tipo=" + tipo + ", usuario=" + usuario
//				+ ", clave=" + clave + ", flagCambiarClave=" + flagCambiarClave + ", ultimoAcceso=" + ultimoAcceso
//				+ ", numPerfilesAct=" + numPerfilesAct + "]";
//	}
	
	@Override
	public String toString() {
		return "Retorno [codigo=" + codigo + ", mensaje=" + mensaje + ", tipo=" + tipo + ", usuario=" + usuario
				+ ", clave=" + clave + ", flagCambiarClave=" + flagCambiarClave + ", ultimoAcceso=" + ultimoAcceso
				+ ", numPerfilesAct=" + numPerfilesAct + ", perfilesAct=" + perfilesAct + "]";
	}
//	fin adecuacion seguridad2

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getFlagCambiarClave() {
		return flagCambiarClave;
	}

	public void setFlagCambiarClave(String flagCambiarClave) {
		this.flagCambiarClave = flagCambiarClave;
	}

//	inicio adecuacion seguridad2
	public Integer getNumPerfilesAct() {
		return numPerfilesAct;
	}


	public void setNumPerfilesAct(Integer numPerfilesAct) {
		this.numPerfilesAct = numPerfilesAct;
	}

	public List<PerfilSistemaBean> getPerfilesAct() {
		return perfilesAct;
	}


	public void setPerfilesAct(List<PerfilSistemaBean> perfilesAct) {
		this.perfilesAct = perfilesAct;
	}
	
	public String getAutenticacion() {
		return autenticacion;
	}


	public void setAutenticacion(String autenticacion) {
		this.autenticacion = autenticacion;
	}
	
	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}
//	fin adecuacion seguridad2
}

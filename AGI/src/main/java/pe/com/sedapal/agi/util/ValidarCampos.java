package pe.com.sedapal.agi.util;

import java.util.Date;
import java.util.Objects;
import java.math.BigDecimal;

public class ValidarCampos {
	public static String validaCamposString(String valor) {
		return valor = Objects.isNull(valor) ? "" : valor;
	}

	public static Integer validaCamposInteger(Integer valor) {
		return valor = Objects.isNull(valor) ? null : valor;
	}

	public static Integer castValorBigDecimalToInteger(BigDecimal valor) {
		return Objects.isNull(valor) ? null : valor.intValue();
	}
	
	public static Long castValorBigDecimalToLong(BigDecimal valor) {
		return Objects.isNull(valor) ? null : valor.longValue();
	}

	public static Date validaCamposDate(String valor) {
		if (!Objects.isNull(valor)) {
			try {
				return UConvierteFecha.convertirStringADate(valor);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}
}

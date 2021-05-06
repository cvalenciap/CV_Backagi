package pe.com.sedapal.seguridad.core.bean.util;

import java.sql.Timestamp;
import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter; 

public class TimestampTypeAdapter extends XmlAdapter<Date, Timestamp> {
	
    public Date marshal(Timestamp v) {  
        return new Date(v.getTime());  
    }  
  
    public Timestamp unmarshal(Date v) {  
        return new Timestamp(  
            v.getTime());  
    } 
}


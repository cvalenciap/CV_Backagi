package pe.com.sedapal.agi.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UJson
{
  public static Map<String, Object> convertirJsonAMapa(String cadenajson)
    throws Exception
  {
    Map<String, Object> mapa = null;
    try
    {
      InputStream flujoEntrada = new ByteArrayInputStream(cadenajson.getBytes());
      ObjectMapper objectMapper = new ObjectMapper();
      mapa = (Map)objectMapper.readValue(flujoEntrada, HashMap.class);
    }
    catch (IOException excepcion)
    {
      throw excepcion;
    }
    return mapa;
  }
  
  public static <T> T convertirJsonATipo(String cadenajson, Class<T> tipo)
    throws Exception
  {
    T retorno = null;
    try
    {
      InputStream flujoEntrada = new ByteArrayInputStream(cadenajson.getBytes(StandardCharsets.UTF_8));
      ObjectMapper objectMapper = new ObjectMapper();
      retorno = objectMapper.readValue(flujoEntrada, tipo);
    }
    catch (Exception excepcion)
    {
      throw excepcion;
    }
    return retorno;
  }
  
  public static <T> T convertirJsonATipo(InputStream flujoEntrada, Class<T> tipo)
    throws Exception
  {
    T retorno = null;
    try
    {
      ObjectMapper objectMapper = new ObjectMapper();
      retorno = objectMapper.readValue(flujoEntrada, tipo);
    }
    catch (Exception excepcion)
    {
      throw excepcion;
    }
    return retorno;
  }
  
  public static <T> T convertirFileJsonATipo(File archivoJson, Class<T> tipo)
    throws Exception
  {
    T retorno = null;
    try
    {
      byte[] archivoBytes = Files.readAllBytes(archivoJson.toPath());
      InputStream flujoEntrada = new ByteArrayInputStream(archivoBytes);
      ObjectMapper objectMapper = new ObjectMapper();
      retorno = objectMapper.readValue(flujoEntrada, tipo);
    }
    catch (Exception excepcion)
    {
      throw excepcion;
    }
    return retorno;
  }
  
  public static <T> List<T> convertirListJsonAListaTipo(String cadenajson, TypeReference<List<T>> listaTipo)
    throws Exception
  {
    List<T> retorno = null;
    try
    {
      InputStream flujoEntrada = new ByteArrayInputStream(cadenajson.getBytes(StandardCharsets.UTF_8));
      ObjectMapper objectMapper = new ObjectMapper();
      retorno = (List)objectMapper.readValue(flujoEntrada, listaTipo);
    }
    catch (Exception excepcion)
    {
      throw excepcion;
    }
    return retorno;
  }
}


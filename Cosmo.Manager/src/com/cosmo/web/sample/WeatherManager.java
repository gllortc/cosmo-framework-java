package com.cosmo.web.sample;

import java.sql.ResultSet;

import com.cosmo.data.DataAgent;
import com.cosmo.data.DataConnection;

public class WeatherManager 
{
   private DataAgent conn;
   
   public WeatherManager(DataAgent connection)
   {
      this.conn = connection;
   }
   
   public void add(Weather weather) throws Exception
   {
      try
      {
         conn.connect();
         
         String sql = "INSERT INTO weather (city, temp_lo, temp_hi, prcp) " +
                      "VALUES ('" + DataConnection.sqlFormatTextValue(weather.getCityName()) + "', " +
                      "         " + weather.getTempMin() + ", " +
                      "         " + weather.getTempMax() + ", " +
                      "         " + weather.getPrecipitation() + ")";
         conn.execute(sql);
      }
      catch (Exception ex)
      {
         throw ex;
      }
      finally
      {
         conn.disconnect();
      }
   }
   
   public ResultSet getAll() throws Exception
   {
      try
      {
         conn.connect();
         
         String sql = "SELECT   city    As Ciutat, " +
                      "         temp_lo As Temp_Min, " +
                      "         temp_hi As Temp_Max, " +
                      "         prcp    As Precipitacio " +
                      "FROM     weather " +
                      "ORDER BY city";
         return conn.executeSql(sql);
      }
      catch (Exception ex)
      {
         throw ex;
      }
      finally
      {
         conn.disconnect();
      }
   }
}

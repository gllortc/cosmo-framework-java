package com.cosmo.web.sample;

import com.cosmo.annotations.CosmoField;
import com.cosmo.annotations.CosmoField.FieldType;
import com.cosmo.annotations.CosmoTable;

@CosmoTable( name = "frmWeather", 
             tableName = "weather", 
             title = "Registre de temperatures",
             description = "Introdueixi les dades per registrar les dades climatològiques d'una ciutat." )
public class Weather 
{
   private String cityName;
   private Integer tempMin;
   private Integer tempMax;
   private Integer precipitation;
   
   //==============================================
   // Constructors
   //==============================================
   
   /**
    * Constructor de la clase.
    */
   public Weather()
   {
      this.cityName = "";
      this.tempMin = 0;
      this.tempMax = 0;
      this.precipitation = 0;
   }
   
   /**
    * Constructor de la clase.
    */
   public Weather(String cityName, int tempMin, int tempMax, int precipitation)
   {
      this.cityName = cityName;
      this.tempMin = tempMin;
      this.tempMax = tempMax;
      this.precipitation = precipitation;
   }

   //==============================================
   // Propiedades
   //==============================================
   
   @CosmoField( fieldType = FieldType.Text, tableColumnName = "city", name = "txtCity", label = "Ciutat" )
   public String getCityName() 
   {
      return cityName;
   }

   public void setCityName(String cityName) 
   {
      this.cityName = cityName;
   }

   @CosmoField( fieldType = FieldType.Integer, tableColumnName = "temp_lo", name = "txtTempLo", label = "Temperatura mínima" )
   public Integer getTempMin() 
   {
      return tempMin;
   }

   public void setTempMin(Integer tempMin) 
   {
      this.tempMin = tempMin;
   }

   @CosmoField( fieldType = FieldType.Integer, tableColumnName = "temp_hi", name = "txtTempHi", label = "Temperatura màxima" )
   public Integer getTempMax() 
   {
      return tempMax;
   }

   public void setTempMax(Integer tempMax) 
   {
      this.tempMax = tempMax;
   }

   @CosmoField( fieldType = FieldType.Integer, tableColumnName = "prcp", name = "txtPrcp", label = "Precipitació mitjana" )
   public Integer getPrecipitation()
   {
      return precipitation;
   }

   public void setPrecipitation(Integer precipitation) 
   {
      this.precipitation = precipitation;
   }   
}

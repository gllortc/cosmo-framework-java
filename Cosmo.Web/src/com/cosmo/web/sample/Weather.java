package com.cosmo.web.sample;

import com.cosmo.annotations.CosmoFieldGetter;
import com.cosmo.annotations.CosmoFieldGetter.FieldType;
import com.cosmo.annotations.CosmoFieldSetter;
import com.cosmo.annotations.CosmoTable;

@CosmoTable( name = "frmWeather", 
             tableName = "weather", 
             title = "Registre de temperatures",
             description = "Introdueixi les dades per registrar les dades climatol�giques d'una ciutat." )
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
   
   @CosmoFieldGetter( fieldType = FieldType.Text, tableColumnName = "city", name = "txtCity", label = "Ciutat" )
   public String getCityName() 
   {
      return cityName;
   }

   @CosmoFieldSetter( name = "txtCity" )
   public void setCityName(String cityName) 
   {
      this.cityName = cityName;
   }

   @CosmoFieldGetter( fieldType = FieldType.Integer, tableColumnName = "temp_lo", name = "txtTempLo", label = "Temperatura m�nima" )
   public Integer getTempMin() 
   {
      return tempMin;
   }

   @CosmoFieldSetter( name = "txtTempLo" )
   public void setTempMin(Integer tempMin) 
   {
      this.tempMin = tempMin;
   }

   @CosmoFieldGetter( fieldType = FieldType.Integer, tableColumnName = "temp_hi", name = "txtTempHi", label = "Temperatura m�xima" )
   public Integer getTempMax() 
   {
      return tempMax;
   }

   @CosmoFieldSetter( name = "txtTempHi" )
   public void setTempMax(Integer tempMax) 
   {
      this.tempMax = tempMax;
   }

   @CosmoFieldGetter( fieldType = FieldType.Integer, tableColumnName = "prcp", name = "txtPrcp", label = "Precipitaci� mitjana" )
   public Integer getPrecipitation()
   {
      return precipitation;
   }

   @CosmoFieldSetter( name = "txtPrcp" )
   public void setPrecipitation(Integer precipitation) 
   {
      this.precipitation = precipitation;
   }   
}

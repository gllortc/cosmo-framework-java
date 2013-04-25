package com.cosmo.web.sample;

import com.cosmo.data.orm.annotations.CosmoFieldGetter;
import com.cosmo.data.orm.annotations.CosmoFieldSetter;
import com.cosmo.data.orm.annotations.CosmoTable;
import com.cosmo.ui.controls.FormFieldInteger;
import com.cosmo.ui.controls.FormFieldText;

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
   
   @CosmoFieldGetter( fieldClass = FormFieldText.class, tableColumnName = "city", name = "txtCity", label = "Ciutat" )
   public String getCityName() 
   {
      return cityName;
   }

   @CosmoFieldSetter( name = "txtCity" )
   public void setCityName(String cityName) 
   {
      this.cityName = cityName;
   }

   @CosmoFieldGetter( fieldClass = FormFieldInteger.class, tableColumnName = "temp_lo", name = "txtTempLo", label = "Temperatura mínima" )
   public Integer getTempMin() 
   {
      return tempMin;
   }

   @CosmoFieldSetter( name = "txtTempLo" )
   public void setTempMin(Integer tempMin) 
   {
      this.tempMin = tempMin;
   }

   @CosmoFieldGetter( fieldClass = FormFieldInteger.class, tableColumnName = "temp_hi", name = "txtTempHi", label = "Temperatura màxima" )
   public Integer getTempMax() 
   {
      return tempMax;
   }

   @CosmoFieldSetter( name = "txtTempHi" )
   public void setTempMax(Integer tempMax) 
   {
      this.tempMax = tempMax;
   }

   @CosmoFieldGetter( fieldClass = FormFieldInteger.class, tableColumnName = "prcp", name = "txtPrcp", label = "Precipitació mitjana" )
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

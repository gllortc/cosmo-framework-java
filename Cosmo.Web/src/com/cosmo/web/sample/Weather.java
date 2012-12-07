package com.cosmo.web.sample;

import com.cosmo.annotations.CosmoField;
import com.cosmo.annotations.CosmoField.FieldType;
import com.cosmo.annotations.CosmoTable;

@CosmoTable(name="frmWeather", tableName="weather")
public class Weather 
{
   private String cityName;
   private Integer tempMin;
   private Integer tempMax;
   private Integer precipitation;
   
   public Weather()
   {
      this.cityName = "";
      this.tempMin = 0;
      this.tempMax = 0;
      this.precipitation = 0;
   }
   
   public Weather(String cityName, int tempMin, int tempMax, int precipitation)
   {
      this.cityName = cityName;
      this.tempMin = tempMin;
      this.tempMax = tempMax;
      this.precipitation = precipitation;
   }

   @CosmoField(fieldType=FieldType.Text, tableColumnName="city", name="txtCity")
   public String getCityName() 
   {
      return cityName;
   }

   public void setCityName(String cityName) 
   {
      this.cityName = cityName;
   }

   @CosmoField(fieldType=FieldType.Integer, tableColumnName="temp_lo", name="txtTempLo")
   public Integer getTempMin() 
   {
      return tempMin;
   }

   public void setTempMin(Integer tempMin) 
   {
      this.tempMin = tempMin;
   }

   @CosmoField(fieldType=FieldType.Integer, tableColumnName="temp_hi", name="txtTempHi")
   public Integer getTempMax() 
   {
      return tempMax;
   }

   public void setTempMax(Integer tempMax) 
   {
      this.tempMax = tempMax;
   }

   @CosmoField(fieldType=FieldType.Integer, tableColumnName="prcp", name="txtPrcp")
   public Integer getPrecipitation()
   {
      return precipitation;
   }

   public void setPrecipitation(Integer precipitation) 
   {
      this.precipitation = precipitation;
   }   
}

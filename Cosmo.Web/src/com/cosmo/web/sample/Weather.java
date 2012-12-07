package com.cosmo.web.sample;

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

   public String getCityName() 
   {
      return cityName;
   }

   public void setCityName(String cityName) 
   {
      this.cityName = cityName;
   }

   public Integer getTempMin() 
   {
      return tempMin;
   }

   public void setTempMin(Integer tempMin) 
   {
      this.tempMin = tempMin;
   }

   public Integer getTempMax() 
   {
      return tempMax;
   }

   public void setTempMax(Integer tempMax) 
   {
      this.tempMax = tempMax;
   }

   public Integer getPrecipitation()
   {
      return precipitation;
   }

   public void setPrecipitation(Integer precipitation) 
   {
      this.precipitation = precipitation;
   }   
}

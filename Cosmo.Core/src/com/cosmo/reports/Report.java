package com.cosmo.reports;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.cosmo.data.DataQuery;

public class Report
{
   private String templateId;
   private HashMap<String, String> staticValues;
   private HashMap<String, DataQuery> dataQueries;

   public String getTemplateId()
   {
      return templateId;
   }

   public void setTemplateId(String templateId)
   {
      this.templateId = templateId;
   }

   public void addDataQuery(DataQuery query)
   {
      this.dataQueries.put(query.getName(), query);
   }

   public Collection<DataQuery> getDataQueries()
   {
      return this.dataQueries.values();
   }
   
   public DataQuery getDataQuery(String name)
   {
      return this.dataQueries.get(name);
   }

   public void addStaticValue(String key, String value)
   {
      this.staticValues.put(key, value);
   }

   public String getStaticValue(String key)
   {
      return this.staticValues.get(key);
   }
   
   
}

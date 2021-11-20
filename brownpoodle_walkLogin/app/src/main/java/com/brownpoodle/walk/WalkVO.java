package com.brownpoodle.walk;

import org.json.JSONArray;

public class WalkVO {

    private String m_id;
    private String walk_json;
    private JSONArray longitude_jarr;
    private JSONArray latitude_jarr;
    private String longitude_str;
    private String latitude_str;

    public void setM_id(String m_id){
        this.m_id = m_id;
    }
    public void setWalk_json(String walk_json){
        this.walk_json = walk_json;
    }
    public void setLatitude_jarr(JSONArray longitude_jarr){ this.longitude_jarr = longitude_jarr; }
    public void setLongitude_jarr(JSONArray latitude_jarr){ this.latitude_jarr = latitude_jarr;   }
    public void setLatitude_str(String longitude_str){ this.latitude_str = latitude_str; }
    public void setLongitude_str(String latitude_str){ this.longitude_str = longitude_str;   }

    public String getM_id(){ return m_id;  }
    public String getWalk_json(){
        return walk_json;
    }
    public JSONArray getLatitude_jarr(){ return longitude_jarr; }
    public JSONArray getLongitude_jarr(){ return latitude_jarr;  }
    public String getLongitude_str(){ return longitude_str; }
    public String getLatitude_str(){ return latitude_str; }
}

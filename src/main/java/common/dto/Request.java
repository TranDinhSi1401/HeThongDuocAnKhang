package common.dto;

import java.io.Serializable;
import java.util.Map;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String action;
    private Map<String, Object> data;

    public Request(String action, Map<String, Object> data) {
        this.action = action;
        this.data = data;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}

package org.apache.sse;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class Stats implements Serializable {
    private static final long serialVersionUID = -6705829915457870975L;

    private long timestamp;
    private int load;
    
    public Stats() {
    }

    public Stats(long timestamp, int load) {
        this.timestamp = timestamp;
        this.load = load;
    }

    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }
}

/*
 * New Project by:
 * -= Manuel Fadljevic =-
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;

/**
 *
 * @author Home
 */
public class Detail implements Serializable{
    private String spec;
    private String value;

    public Detail() {
    }

    public Detail(String spec, String value) {
        this.spec = spec;
        this.value = value;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

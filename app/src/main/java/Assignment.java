import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public class Assignment implements Serializable {

    private int id;
    private String [] type;
    private String description;
    private Date date;
    private String [] status;


    public Assignment(int id, String[] type, String description, Date date, String[] status) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.date = date;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String[] getType() {
        return type;
    }

    public void setType(String[] type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String[] getStatus() {
        return status;
    }

    public void setStatus(String[] status) {
        this.status = status;
    }

}

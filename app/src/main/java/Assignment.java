import java.util.Date;

public class Assignment {

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
}

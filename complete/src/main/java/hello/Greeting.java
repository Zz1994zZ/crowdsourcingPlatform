package hello;

import javax.persistence.Entity;
import java.util.ArrayList;

@Entity
public class Greeting {

    private final long id;
    private final String content;


    private final ArrayList<String> list;
    public Greeting(long id, String content, ArrayList<String> list) {
        this.id = id;
        this.content = content;
        this.list = list;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;

    }

    public ArrayList<String> getList() {
        return list;
    }

}

import java.util.ArrayList;
import java.util.List;

public class Announcement {
    private int id;
    private String title;
    private String content;
    private User owner;
    private List<String> comments;

    public Announcement(int id, String title, String content, User owner){
        this.id = id;
        this.title = title;
        this.content = content;
        this.owner = owner;
        this.comments = new ArrayList<>();
    };

    public Announcement(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<String> getComments() {
        return comments;
    }
}


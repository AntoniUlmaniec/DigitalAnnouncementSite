public class Announcement {
    private int id;
    private String title;
    private String content;
    private User owner;

    public Announcement(int id, String title, String content, User owner){
        this.id = id;
        this.title = title;
        this.content = content;
        this.owner = owner;
    };

    public Announcement(){
    }

    public void addComment(User user, String comment) {
        // do dokoczenia
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
}


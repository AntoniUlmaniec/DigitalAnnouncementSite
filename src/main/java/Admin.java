public class Admin extends User {

    public Admin(String username, int id) {
        super(username,id);
    }

    public void addCategory(Category category) {
        // do dokonczenia
    }

    public void removeCategory(int categoryId) {
        // do dokonczenia
    }

    public void banUser(User user) {
        // do dokonczenia
    }

    public Statistics viewGeneralStatistics() {
        // do dokonczenia

        return new Statistics();
    }
}

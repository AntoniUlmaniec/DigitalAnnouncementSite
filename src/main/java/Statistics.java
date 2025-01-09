public class Statistics {
    private int totalUsers;
    private int totalAnnouncements;
    private int activeUsers;

    public Statistics(int totalUsers, int totalAnnouncements, int activeUsers){
        this.totalUsers = totalUsers;
        this.activeUsers = activeUsers;
        this.totalAnnouncements = totalAnnouncements;
    }

    public int getTotalUsers() {
        return totalUsers;
    }

    public int getTotalAnnouncements() {
        return totalAnnouncements;
    }

    public int getActiveUsers() {
        return activeUsers;
    }

    public String toString() {
        return "Wszyscy użytkownicy: " + totalUsers + ", Wszystkie ogłoszenia: " + totalAnnouncements + ", Aktywni użytkownicy: " + activeUsers;
    }
}

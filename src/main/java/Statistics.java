public class Statistics {
    private int totalUsers;
    private int totalAnnouncements;
    private int bannedUsers;

    public Statistics(int totalUsers, int bannedUsers, int totalAnnouncements) {
        this.totalUsers = totalUsers;
        this.bannedUsers = bannedUsers;
        this.totalAnnouncements = totalAnnouncements;
    }

    public int getTotalUsers() {
        return totalUsers;
    }

    public int getTotalAnnouncements() {
        return totalAnnouncements;
    }

    public int getBannedUsers() {
        return bannedUsers;
    }

    public String toString() {
        return "Wszyscy użytkownicy: " + totalUsers + ", Zbanowani użytkownicy: " + bannedUsers + ", Wszystkie ogłoszenia: " + totalAnnouncements;
    }
}

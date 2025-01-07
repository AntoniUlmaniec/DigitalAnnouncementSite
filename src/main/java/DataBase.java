import java.util.*;

public class DataBase {
    private HashMap<Category, List<Announcement>> announcementsInCategories;
    private List<User> users;
    private HashMap<User, List<Announcement>> wishList;
    private List<Category> categories;
    private Statistics statistics;

    static int userId = 1;
    static int announcementId = 1;
    static int categoryId = 1;

    private String[] categoryNames = {
            "Antyki i Kolekcje", "Firma i Przemysł", "Motoryzacja", "Nieruchomości", "Praca",
            "Dom i Ogród", "Elektronika", "Moda", "Rolnictwo", "Zwierzęta",
            "Dla Dzieci", "Sport i Hobby", "Muzyka i Edukacja", "Zdrowie i Uroda", "Usługi",
            "Noclegi", "Wypożyczalnia", "Oddam za darmo", "Meble", "Gry i konsole",
            "Psy rasowe", "Praca dodatkowa"
    };

    private String[][] announcementsTitles = {
            {"Sprzedam starą monetę", "Kolekcja znaczków pocztowych na sprzedaż"},
            {"Maszyny budowlane na wynajem", "Wyprzedaż sprzętu biurowego"},
            {"Sprzedam samochód osobowy", "Części zamienne do motocykla"},
            {"Mieszkanie na wynajem", "Sprzedam działkę budowlaną"},
            {"Oferty pracy w IT", "Praca dorywcza w weekendy"},
            {"Sprzedam kanapę", "Zestaw narzędzi ogrodowych"},
            {"Laptop na sprzedaż", "Nowy telefon w atrakcyjnej cenie"},
            {"Modne sukienki w wyprzedaży", "Buty sportowe w dobrej cenie"},
            {"Traktor na sprzedaż", "Maszyny rolnicze w atrakcyjnych cenach"},
            {"Sprzedam rasowego psa", "Klatka dla chomika na sprzedaż"},
            {"Wózek dziecięcy używany", "Zabawki edukacyjne dla dzieci"},
            {"Piłka nożna na sprzedaż", "Rower górski w świetnym stanie"},
            {"Gitara klasyczna na sprzedaż", "Książki edukacyjne dla młodzieży"},
            {"Kosmetyki naturalne w promocji", "Sprzęt do ćwiczeń domowych"},
            {"Usługi remontowe", "Naprawa sprzętu AGD"},
            {"Pokój w hotelu na wynajem", "Domek letniskowy na sprzedaż"},
            {"Wypożycz rower miejski", "Wynajem projektora na prezentacje"},
            {"Oddam używane ubrania", "Oddam za darmo książki do nauki"},
            {"Sprzedam szafę", "Stół z krzesłami w dobrej cenie"},
            {"Konsola do gier używana", "Gry komputerowe w promocji"},
            {"Rasowy owczarek niemiecki na sprzedaż", "Pies husky szuka domu"},
            {"Praca wieczorami", "Dodatkowa praca w weekendy"}
    };

    private String[][] announcementContents = {
            {"Stara moneta, 100 lat historii", "Znaczki z całego świata, unikalne okazy"},
            {"Koparka, wynajem na budowę", "Biurka, szafki, komputery - wyprzedaż"},
            {"Auto z rocznika 2010, w dobrym stanie", "Części zamienne do motocykla, różne modele"},
            {"3-pokojowe mieszkanie w centrum", "Duża działka na obrzeżach miasta"},
            {"Praca dla programisty Java", "Praca w sklepie, weekendowa"},
            {"Kanapa skórzana, świetny stan", "Komplet narzędzi ogrodowych, nowe"},
            {"Laptop gamingowy, świetne parametry", "Smartfon z dużą pamięcią, nowy"},
            {"Sukienka letnia, nowa kolekcja", "Buty sportowe, lekkie i wygodne"},
            {"Traktor Zetor, rocznik 2015", "Maszyna do sianokosów, stan idealny"},
            {"Pies rasy labrador, 2 lata", "Chomik z klatką, oswojony"},
            {"Wózek dziecięcy, regulowany", "Zabawki edukacyjne, różne modele"},
            {"Piłka nożna, używana w świetnym stanie", "Rower górski, 21 biegów"},
            {"Gitara klasyczna, drewno klonowe", "Podręczniki szkolne do liceum"},
            {"Kosmetyki ekologiczne, zestaw", "Mata do ćwiczeń + hantle"},
            {"Malowanie ścian, szybkie terminy", "Naprawa pralek i zmywarek"},
            {"Pokój w centrum na weekend", "Domek letniskowy nad jeziorem"},
            {"Wynajem rowerów miejskich, tanio", "Projektor, wynajem na godziny"},
            {"Oddam ubrania damskie, używane", "Oddam książki edukacyjne za darmo"},
            {"Szafa z lustrem, drewniana", "Stół + 4 krzesła, klasyczne"},
            {"Konsola PS4, używana, tanio", "Gry komputerowe, wyprzedaż"},
            {"Owczarek niemiecki, rodowodowy", "Husky, szczeniak, szuka domu"},
            {"Praca w weekendy, godziny wieczorne", "Dodatkowe zlecenia zdalne"}
    };


    public DataBase() {
        announcementsInCategories = new HashMap<>();
        users = new ArrayList<>();
        wishList = new HashMap<>();
        categories = new ArrayList<>();
        statistics = new Statistics();

        users.add(new User("Olaf", userId++));
        users.add(new User("Piotr", userId++));
        users.add(new User("Tomasz", userId++));
        users.add(new Admin("Admin Artur", userId++));


        for (int i = 0; i < categoryNames.length; i++) {
            Category cat = new Category();
            cat.setName(categoryNames[i]);
            cat.setId(categoryId++);
            categories.add(cat);


            List<Announcement> announcements = new ArrayList<>();
            Announcement announcement1 = new Announcement(announcementId++, announcementsTitles[i][0], announcementContents[i][0], users.get(0), cat);
            Announcement announcement2 = new Announcement(announcementId++, announcementsTitles[i][1], announcementContents[i][1], users.get(1), cat);
            announcements.add(announcement1);
            announcements.add(announcement2);


            announcementsInCategories.put(cat, announcements);
        }
}

    public List<Announcement> getAnnouncements() {
        List<Announcement> allAnnouncements = new ArrayList<>();
        for (Map.Entry<Category, List<Announcement>> entry : announcementsInCategories.entrySet()) {
            allAnnouncements.addAll(entry.getValue());
        }

        return allAnnouncements;
    }

    public HashMap<Category, List<Announcement>> getAnnouncementsInCategories() {
        return announcementsInCategories;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public HashMap<User, List<Announcement>> getWishList() { return wishList; }
}

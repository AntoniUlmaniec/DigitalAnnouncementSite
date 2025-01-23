let user = "";
let userId = "";
let isAdmin = false;
let currentView = "category";
let lastClickedCategory = null;

const loadUsers = async () => {
    let data = await fetchAsync({}, "/loadUsers");
    const usersDiv = document.getElementById("select-user");
    usersDiv.innerHTML = `<div class="select-user-header">! Wybierz Użytkownika !</div>`;

    data.forEach(e=>{
        let element = document.createElement("div");
        element.classList.add("user-element");
        element.innerHTML = e.username;
        element.userId = e.id;
        element.status = e.banStatus;
        element.onclick = login;

        element.onmouseover = (e) => {
            if (!e.target.classList.contains("banned")) {
                e.target.style.color = "red";
                e.target.style.fontWeight = "bold";
                e.target.style.cursor = "pointer";
            }
        }

        element.onmouseout = (e) => {
            if (!e.target.classList.contains("banned")) {
                e.target.style.color = "#00274d";
                e.target.style.fontWeight = "normal";
                e.target.style.cursor = "default";
            }
        }

        usersDiv.appendChild(element);
    });
}

const login = async (e) => {
    let banned = e.target.status;
    if(banned){
        console.log("użytkownik zbanowany");
        return; //nie można się zalogować na zbanowanego usera
    }


    let id = e.target.userId;

    let reqData = {
        "id":id
    }
    let data = await fetchAsync(reqData,"/login");

    isAdmin = false;
    if (data.state == "admin" || data.state == "logged") {
        if (data.state == "admin") isAdmin = true;
        userId = id;
        user = e.target.innerHTML;
        console.log("zalogowano:", user, "admin:", isAdmin, "id:", userId);

        document.getElementById("select-user").style.display = "none";
        document.getElementById("digital-board-header").style.display = "none";
        document.getElementById("navigation-container").style.display = "block";
        document.getElementById("select-category").style.display = "flex";

        document.getElementById("edit-announcement").innerHTML = '';
        document.getElementById("create-announcement").innerHTML = '';

        if (isAdmin) {
            document.getElementById("navigation-container").style.width = "60%";
            document.getElementById("stats-button").style.display = "inline-block";
            document.getElementById("banned-button").style.display = "inline-block";
        } else {
            document.getElementById("navigation-container").style.width = "50%";
            document.getElementById("stats-button").style.display = "none";
            document.getElementById("banned-button").style.display = "none";
        }

        await displayCategories();
    }
}

const showBannedUsers = async () => {
    document.getElementById("my-announcements").style.display = "none";
    document.getElementById("select-category").style.display = "none";
    document.getElementById("wishlist").style.display = "none";
    document.getElementById("create-announcement").style.display = "none";
    document.getElementById("no-announcements").style.display = 'none';
    document.getElementById("select-announcement").innerHTML = '';

    const container = document.getElementById("display-stats");
    container.innerHTML = '';
    container.style.visibility = 'visible';
    container.style.display = 'flex';
    container.style.flexDirection = 'column';

    const bannedUsers = await fetchAsync({}, "/fetchBannedUsers");

    if (bannedUsers.length === 0) {
        const message = document.createElement("div");
        message.innerText = "Brak zbanowanych użytkowników.";
        message.style.textAlign = "center";
        message.style.color = "#555";
        container.appendChild(message);
        showConfirmation("Brak zbanowanych użytkowników!", false);
        return;
    }

    bannedUsers.forEach(user => {
        const userDiv = document.createElement("div");
        userDiv.classList.add("user-div");

        const userInfo = document.createElement("div");
        userInfo.classList.add("user-info");
        userInfo.innerText = `Użytkownik: ${user.username}`;

        const unbanButton = document.createElement("button");
        unbanButton.classList.add("unban-button");
        unbanButton.innerText = "Odbanuj";

        unbanButton.onclick = async () => {
            const response = await fetchAsync({ id: user.id }, "/unbanUser");
            if (response.state === "success") {
                showConfirmation(`Użytkownik ${user.username} został odbanowany!`, true);
                await loadUsers();
                await showBannedUsers();
            }
        }

        userDiv.appendChild(userInfo);
        userDiv.appendChild(unbanButton);
        container.appendChild(userDiv);
    });
}


const showStats = async () => {
    document.getElementById("my-announcements").style.display = "none";
    document.getElementById("select-category").style.display = "none";
    document.getElementById("wishlist").style.display = "none";
    document.getElementById("create-announcement").style.display = "none";
    document.getElementById("no-announcements").style.display = 'none';
    document.getElementById("select-announcement").innerHTML = '';

    const container = document.getElementById("display-stats");
    container.innerHTML = '';
    container.style.visibility = 'visible';
    container.style.display = "flex";
    container.style.flexDirection = "column";

    const statsData = await fetchAsync({}, "/viewGeneralStatistics");

    const header = document.createElement("h2");
    header.innerText = "Statystyki Systemu";
    header.style.textAlign = "center";
    header.style.color = "white";
    header.style.backgroundColor = "#00274d";
    header.style.padding = "15px";
    header.style.borderRadius = "10px";
    header.style.margin = "0 auto 20px auto";
    header.style.display = "inline-block";
    container.appendChild(header);

    const table = document.createElement("table");
    table.style.width = "100%";
    table.style.maxWidth = "600px";
    table.style.margin = "0 auto";
    table.style.borderCollapse = "collapse";
    table.style.backgroundColor = "#00274d";
    table.style.color = "white";
    table.style.borderRadius = "10px";
    table.style.boxShadow = "0 4px 8px rgba(0, 0, 0, 0.2)";

    const thead = document.createElement("thead");
    thead.innerHTML = `
            <tr style="font-size: 18px;">
                <th style="padding: 10px; border: 1px solid white;">Kategoria</th>
                <th style="padding: 10px; border: 1px solid white;">Wartość</th>
            </tr>`;
    table.appendChild(thead);

    const tbody = document.createElement("tbody");
    tbody.innerHTML = `
            <tr>
                <td style="padding: 10px; border: 1px solid white;">Liczba użytkowników</td>
                <td style="padding: 10px; border: 1px solid white; text-align: center;">${statsData.totalUsers}</td>
            </tr>
            <tr>
                <td style="padding: 10px; border: 1px solid white;">Liczba zbanowanych użytkowników</td>
                <td style="padding: 10px; border: 1px solid white; text-align: center;">${statsData.bannedUsers}</td>
            </tr>
            <tr>
                <td style="padding: 10px; border: 1px solid white;">Liczba ogłoszeń</td>
                <td style="padding: 10px; border: 1px solid white; text-align: center;">${statsData.totalAnnouncements}</td>
            </tr>`;
    table.appendChild(tbody);

    container.appendChild(table);
}

const showMyAnnouncements = async () => {
    currentView = "myAnnouncements";
    document.getElementById("my-announcements").style.display = "flex";
    document.getElementById("select-category").style.display = "none";
    document.getElementById("wishlist").style.display = "none";
    document.getElementById("create-announcement").style.display = "none";
    document.getElementById("display-stats").style.display = "none";
    document.getElementById("no-announcements").style.display = 'none';
    document.getElementById("select-announcement").innerHTML = '';
    await displayMyAnnouncements();
}

const showCategories = async () => {
    currentView = "category";
    document.getElementById("select-category").style.display = "flex";
    document.getElementById("create-announcement").style.display = "none";
    document.getElementById("wishlist").style.display = "none";
    document.getElementById("my-announcements").style.display = "none";
    document.getElementById("display-stats").style.display = "none";
    document.getElementById("no-announcements").style.display = 'none';
    document.getElementById("select-announcement").innerHTML = '';

    await displayCategories();
}

const showMyWishList = async () => {
    currentView = "wishlist";
    document.getElementById("wishlist").style.display = "flex";
    document.getElementById("select-category").style.display = "none";
    document.getElementById("create-announcement").style.display = "none";
    document.getElementById("my-announcements").style.display = "none";
    document.getElementById("display-stats").style.display = "none";
    document.getElementById("no-announcements").style.display = 'none';
    document.getElementById("select-announcement").innerHTML = '';

    let data = await fetchAsync({}, `/fetchWishlist/${userId}`);

    if (data.length === 0) {
        showConfirmation("Brak ulubionych ogłoszeń!", false);
        showNoAnnouncementsMessage('no-announcements');
        return;
    } else {
        createAnnouncementsElements(data, "wishlist");
        document.getElementById("no-announcements").style.display = 'none';
    }
}

const goToHome = () => {
    document.getElementById("select-user").style.display = "block";
    document.getElementById("digital-board-header").style.display = "block";
    document.getElementById("navigation-container").style.display = "none";
    document.getElementById("my-announcements").style.display = "none";
    document.getElementById("select-category").style.display = "none";
    document.getElementById("wishlist").style.display = "none";
    document.getElementById("create-announcement").style.display = "none";
    document.getElementById("display-stats").style.display = "none";
    document.getElementById("no-announcements").style.display = 'none';

    user = "";
    userId = "";
    isAdmin = false;
    document.getElementById("select-announcement").innerHTML = '';
}


const createAnnouncementsElements = (data, containerId) => {
    const announcementsDiv = document.getElementById(containerId);
    announcementsDiv.style.visibility = "visible";
    announcementsDiv.innerHTML = "";
    announcementsDiv.classList.add('announcement-container');

    data.forEach(e => {
        const div = document.createElement("div");
        div.classList.add("announcement");

        const title = document.createElement("div");
        title.innerText = e.title;
        title.classList.add("title");

        const content = document.createElement("div");
        content.innerText = e.content;
        content.classList.add("content");

        const owner = document.createElement("div");
        owner.innerText = `Autor: ${e.owner.username}`;
        owner.classList.add("owner");

        const category = document.createElement("div");
        category.innerText = e.category.name;
        category.classList.add("category");

        const comments = document.createElement("div");
        comments.innerHTML = "Comments:<br>" + e.comments.map(c => `- ${c}`).join("<br>");
        comments.classList.add("comments");

        announcementsDiv.appendChild(div);

        const commentDiv = document.createElement("div");
        commentDiv.classList.add("comments-container");

        const commentInput = document.createElement("input");
        commentInput.placeholder = "Napisz Komentarz...";
        commentInput.classList.add("comment-input");

        const sendComment = document.createElement("div");
        sendComment.classList.add("button");
        sendComment.innerText = "Wyślij komentarz";
        sendComment.onclick = async (event) => {
            if (commentInput.value.trim() === "") {
                showConfirmation("Proszę uzupełnić treść komentarza!", false);
                return;
            } else {
                console.log(commentInput.value);
                let data = await fetchAsync({ state: user + ":" + commentInput.value }, "/commentOnAnnouncement/" + e.id);
                if (data != null) {
                    if (currentView === "category") {
                        await displayAnnouncementsByCategory(e.category.name);
                        showConfirmation("Pomyślnie dodano komentarz!", true);
                    } else if (currentView === "myAnnouncements") {
                        await displayMyAnnouncements();
                        showConfirmation("Pomyślnie dodano komentarz!", true);
                    } else if (currentView === "wishlist") {
                        await showMyWishList();
                        showConfirmation("Pomyślnie dodano komentarz!", true);
                    }
                }
            }
        }

        commentDiv.append(commentInput, sendComment);
        div.appendChild(commentDiv);

        const heart = document.createElement("div");
        heart.style.width = "50px";
        heart.style.height = "50px";
        heart.style.backgroundSize = "cover";

        heart.clicked = e.isFavorite;
        heart.style.backgroundImage = heart.clicked ? 'url("heart.png")' : 'url("heart_off.png")';

        heart.onclick = async (ev) => {
            heart.clicked = !heart.clicked;
            heart.style.backgroundImage = heart.clicked ? 'url("heart.png")' : 'url("heart_off.png")';

            await fetchAsync({ state: e.id }, `/addToWishlist/${userId}`);

            if (heart.clicked) {
                showConfirmation("Dodano ogłoszenie do ulubionych!", true);
            } else {
                showConfirmation("Usunięto ogłoszenie z ulubionych!", false);
            }

            if (currentView === "wishlist") {
                let wishlistData = await fetchAsync({}, `/fetchWishlist/${userId}`);
                createAnnouncementsElements(wishlistData, "wishlist");
            }
        }

        div.append(title, content, owner, category, comments, commentDiv, heart);

        if (isAdmin && e.owner.username !== user) {
            // Przycisk usuń
            const deleteButton = document.createElement("div");
            deleteButton.classList.add("button", "delete");
            deleteButton.innerText = "Usuń";
            deleteButton.onclick = async () => {
                event.stopPropagation();

                const modal = document.getElementById("delete-confirm-modal");
                const overlay = document.getElementById("delete-modal-overlay");
                modal.classList.add("show");
                overlay.classList.add("show");

                const modalText = modal.querySelector("p");
                modalText.innerText = "Czy na pewno chcesz usunąć to ogłoszenie?";

                const confirmButton = document.getElementById("confirm-delete-wishlist");
                const cancelButton = document.getElementById("cancel-delete-wishlist");
                confirmButton.innerText = "Usuń";

                confirmButton.onclick = async () => {
                    modal.classList.remove("show");
                    overlay.classList.remove("show");

                    let data = await fetchAsync({}, "/deleteAnnouncement/" + e.id);
                    if (data.state === "success") {
                        showConfirmation("Pomyślnie usunięto ogłoszenie!", true);
                        if (currentView === "category") {
                            await displayAnnouncementsByCategory(e.category.name);
                        } else if (currentView === "myAnnouncements") {
                            await displayMyAnnouncements();
                        } else if (currentView === "wishlist") {
                            let wishlistData = await fetchAsync({}, `/fetchWishlist/${userId}`);
                            createAnnouncementsElements(wishlistData, "wishlist");
                        }
                    }
                }

                cancelButton.onclick = () => {
                    modal.classList.remove("show");
                    overlay.classList.remove("show");
                    showConfirmation("Anulowano usuwanie ogłoszenia!", false);
                }
            }

            // Przycisk edytuj
            const editButton = document.createElement("div");
            editButton.classList.add("button");
            editButton.innerText = "Edytuj";
            editButton.onclick = async () => {
                let announcementData = await fetchAsync({}, `/getAnnouncement/${e.id}`);

                document.getElementById('edit-title').value = announcementData.title;
                document.getElementById('edit-content').value = announcementData.content;

                if (!document.getElementById('modal-overlay').classList.contains('show')) {
                    document.getElementById('modal-overlay').classList.add('show');
                    document.getElementById('edit-modal').classList.add('show');
                }

                document.getElementById('save-changes').onclick = async () => {
                    const updatedData = {
                        id: announcementData.id,
                        title: document.getElementById('edit-title').value,
                        content: document.getElementById('edit-content').value
                    }

                    const response = await fetchAsync(updatedData, "/editAnnouncement");
                    if (response.state === "success") {
                        document.getElementById('modal-overlay').classList.remove('show');
                        document.getElementById('edit-modal').classList.remove('show');
                        showConfirmation("Pomyślnie zedytowano ogłoszenie!", true);
                        if (currentView === "category") {
                            await displayAnnouncementsByCategory(e.category.name);
                        } else if (currentView === "myAnnouncements") {
                            await displayMyAnnouncements();
                        } else if (currentView === "wishlist") {
                            await showMyWishList();
                        }
                    }
                }

                document.getElementById('cancel-edit').onclick = () => {
                    document.getElementById('modal-overlay').classList.remove('show');
                    document.getElementById('edit-modal').classList.remove('show');
                    showConfirmation("Anulowano edycję ogłoszenia!", false);
                }
            }

            // Przycisk zbanuj
            const banButton = document.createElement("div");
            banButton.classList.add("button", "ban");
            banButton.innerText = "Zbanuj";
            banButton.onclick = async () => {
                event.stopPropagation();
                const username = e.owner.username;

                const modal = document.getElementById("delete-confirm-modal");
                const overlay = document.getElementById("delete-modal-overlay");

                modal.classList.add("show");
                overlay.classList.add("show");

                const modalText = modal.querySelector("p");
                modalText.innerText = `Czy na pewno chcesz zbanować użytkownika: ${username}?`;

                const confirmButton = document.getElementById("confirm-delete-wishlist");
                const cancelButton = document.getElementById("cancel-delete-wishlist");
                confirmButton.innerText = "Zbanuj";

                confirmButton.onclick = async () => {
                    modal.classList.remove("show");
                    overlay.classList.remove("show");

                    const response = await fetchAsync({ id: e.owner.id }, "/banUser");

                    if (response.state === "success") {
                        showConfirmation(`Użytkownik ${e.owner.username} został zbanowany!`, true);

                        if (currentView === "category" && lastClickedCategory) {
                            await displayAnnouncementsByCategory(lastClickedCategory.name);
                        } else if (currentView === "myAnnouncements") {
                            await displayMyAnnouncements();
                        } else if (currentView === "wishlist") {
                            await showMyWishList();
                        }

                        await loadUsers();
                    }
                }

                cancelButton.onclick = () => {
                    modal.classList.remove("show");
                    overlay.classList.remove("show");
                    showConfirmation("Anulowano banowanie użytkownika!", false);
                }
            }

            div.append(deleteButton, editButton, banButton);
        }

        announcementsDiv.appendChild(div);

        if (e.owner.username === user) {
            const buttonsDiv = document.createElement("div");
            buttonsDiv.classList.add("buttons-container");

            const deleteAnnouncement = document.createElement("div");
            deleteAnnouncement.onclick = () => {
                event.stopPropagation();

                const modal = document.getElementById("delete-confirm-modal");
                const overlay = document.getElementById("delete-modal-overlay");
                modal.classList.add("show");
                overlay.classList.add("show");

                const modalText = modal.querySelector("p");
                modalText.innerText = "Czy na pewno chcesz usunąć to ogłoszenie?";

                const confirmButton = document.getElementById("confirm-delete-wishlist");
                const cancelButton = document.getElementById("cancel-delete-wishlist");
                confirmButton.innerText = "Usuń";

                confirmButton.onclick = async () => {
                    modal.classList.remove("show");
                    overlay.classList.remove("show");

                    let data = await fetchAsync({}, `/deleteAnnouncement/${e.id}`);
                    if (data.state === "success") {
                        showConfirmation("Pomyślnie usunięto ogłoszenie!", true);
                        if (currentView === "category") {
                            await displayAnnouncementsByCategory(e.category.name);
                        } else if (currentView === "myAnnouncements") {
                            await displayMyAnnouncements();
                        } else if (currentView === "wishlist") {
                            let wishlistData = await fetchAsync({}, `/fetchWishlist/${userId}`);
                            createAnnouncementsElements(wishlistData, "wishlist");
                        }
                    }
                }

                cancelButton.onclick = () => {
                    modal.classList.remove("show");
                    overlay.classList.remove("show");
                    showConfirmation("Anulowano usuwanie ogłoszenia!", false);
                }
            }

            deleteAnnouncement.classList.add("button", "delete");
            deleteAnnouncement.innerText = "usuń";
            buttonsDiv.appendChild(deleteAnnouncement);

            const editAnnouncement = document.createElement("div");
            editAnnouncement.onclick = async () => {
                let announcementData = await fetchAsync({}, `/getAnnouncement/${e.id}`);

                document.getElementById('edit-title').value = announcementData.title;
                document.getElementById('edit-content').value = announcementData.content;

                if (!document.getElementById('modal-overlay').classList.contains('show')) {
                    document.getElementById('modal-overlay').classList.add('show');
                    document.getElementById('edit-modal').classList.add('show');
                }

                document.getElementById('save-changes').onclick = async () => {
                    const updatedData = {
                        id: announcementData.id,
                        title: document.getElementById('edit-title').value,
                        content: document.getElementById('edit-content').value
                    }

                    const response = await fetchAsync(updatedData, "/editAnnouncement");
                    if (response.state === "success") {
                        document.getElementById('modal-overlay').classList.remove('show');
                        document.getElementById('edit-modal').classList.remove('show');
                        showConfirmation("Pomyślnie zedytowano ogłoszenie!", true);
                        if (currentView === "category") {
                            await displayAnnouncementsByCategory(e.category.name);
                        } else if (currentView === "myAnnouncements") {
                            await displayMyAnnouncements();
                        } else if (currentView === "wishlist") {
                            await showMyWishList();
                        }
                    }
                }

                document.getElementById('cancel-edit').onclick = () => {
                    document.getElementById('modal-overlay').classList.remove('show');
                    document.getElementById('edit-modal').classList.remove('show');
                    showConfirmation("Anulowano edycje ogłoszenia!", false);
                }
            }
            editAnnouncement.classList.add("button");
            editAnnouncement.innerText = "edytuj";
            buttonsDiv.appendChild(editAnnouncement);


            div.appendChild(buttonsDiv);
        }
    });
}

const displayMyAnnouncements = async () => {
    let reqData = {};
    let data = await fetchAsync(reqData, "/fetchMyAnouncements/" + userId);
    const announcementsContainer = document.getElementById("my-announcements");

    if (data.length === 0) {
        announcementsContainer.innerHTML = "";
        showConfirmation("Brak dodanych ogłoszeń!", false);
        showNoAnnouncementsMessage('no-announcements');
        return;
    } else {
        createAnnouncementsElements(data, "my-announcements");
        document.getElementById("no-announcements").style.display = 'none';
    }
}

const createNewAnnouncement = async ()=> {
    let announcementsDiv = document.getElementById("create-announcement");
    announcementsDiv.innerHTML = "";

    announcementsDiv.style.visibility = "visible";
    announcementsDiv.style.display = "block";
    document.getElementById("my-announcements").style.display = "none";
    document.getElementById("select-category").style.display = "none";
    document.getElementById("wishlist").style.display = "none";
    document.getElementById("display-stats").style.display = "none";
    document.getElementById("no-announcements").style.display = 'none';
    document.getElementById("select-announcement").innerHTML = '';

    let titleDiv = document.createElement("div");
    titleDiv.classList.add("create-announcement-title");
    titleDiv.innerText = "Dodaj Ogłoszenie";

    let title = document.createElement("input");
    title.placeholder = "Tytuł";
    let content = document.createElement("textarea");

    let categoriesData = await fetchAsync({}, "/browseCategories");
    let category = document.createElement("select");
    category.name = "categories";
    category.id = "categories-select";
    category.classList.add("category-select");

    for(let i = 0; i < categoriesData.length; i++) {
        let elem = document.createElement("option");
        elem.innerHTML = categoriesData[i].name;
        elem.value = categoriesData[i].name;
        category.append(elem);
    }

    content.placeholder = "Treść ogłoszenia";
    const button = document.createElement("div");
    button.classList.add("button");
    button.innerText = "Dodaj";
    button.onclick = sendCreated;

    const div = document.createElement("div");
    div.append(title, content, category, button);
    div.classList.add("create-announcement");
    announcementsDiv.appendChild(titleDiv);
    announcementsDiv.appendChild(div);
}


const sendCreated = async (e) => {
    const elements = e.target.parentElement.childNodes;

    let title = elements[0].value.trim();
    let content = elements[1].value.trim();
    let category = document.getElementById("categories-select").value;

    if (!title || !content || !category) {
        showConfirmation("Proszę wypełnić wszystkie pola!", false);
        return;
    }

    let data = {
        id:1,
        title:elements[0].value,
        content:elements[1].value,
        owner: {
            username:user,
            id:userId,
            banStatus:false,
            wishlist:[]
        },
        category:{
            name:document.getElementById("categories-select").value,
            id:1,
            description:"kategoria"
        },
        comments:[]
    }

    let announcement = await fetchAsync(data,"/publishAnnouncement");
    elements[0].value = "";
    elements[1].value = "";
    showConfirmation("Ogłoszenie zostało pomyślnie dodane!", announcement != null);
}

const showConfirmation = (message, isSuccess) => {
    const confirmationDiv = document.createElement("div");
    confirmationDiv.classList.add("confirmation-message");
    confirmationDiv.innerText = message;

    confirmationDiv.style.position = "fixed";
    confirmationDiv.style.top = "10px";
    confirmationDiv.style.right = "10px";
    confirmationDiv.style.padding = "20px 30px";
    confirmationDiv.style.color = "white";
    confirmationDiv.style.borderRadius = "10px";
    confirmationDiv.style.boxShadow = "0 6px 12px rgba(0, 0, 0, 0.2)";
    confirmationDiv.style.zIndex = "1000";
    confirmationDiv.style.fontSize = "18px";

    if (isSuccess) {
        confirmationDiv.style.backgroundColor = "#4CAF50";
    } else {
        confirmationDiv.style.backgroundColor = "#f44336";
    }

    document.body.appendChild(confirmationDiv);

    setTimeout(() => {
        confirmationDiv.remove();
    }, 3000);
}

const displayCategories = async () => {
    let reqData = {};
    let data = await fetchAsync(reqData, "/browseCategories");
    const categoriesDiv = document.getElementById("select-category");
    categoriesDiv.style.visibility = "visible";
    categoriesDiv.innerHTML = "";

    const title = document.createElement("h2");
    title.innerText = "Kategorie Ogłoszeń:";
    title.style.color = "#00274d";
    title.style.marginBottom = "20px";
    categoriesDiv.appendChild(title);

    data.forEach(e => {
        let elem = document.createElement("div");
        elem.innerHTML = e.name;
        elem.name = e.name;
        elem.classList.add("category");

        elem.onclick = (e) => {
            let allCategories = document.querySelectorAll('.category');
            allCategories.forEach(category => {
                category.style.background = "#f5f5f5";
                category.style.fontWeight = "normal";
            });
            lastClickedCategory = e.target;

            e.target.style.background = "#9370DB";
            e.target.style.fontWeight = "bold";

            displayAnnouncements(e);
        }
        elem.onmouseover = (e) => {
            if (e.target === elem) {
                if (e.target !== lastClickedCategory || lastClickedCategory === null) {
                    e.target.style.background = "#9370DB";
                    e.target.style.fontWeight = "bold";
                }
            }
        }

        elem.onmouseout = (e) => {
            if (e.target === elem) { // Sprawdza, czy zdarzenie pochodzi bezpośrednio z elementu kategorii
                if (e.target !== lastClickedCategory || lastClickedCategory === null) {
                    e.target.style.background = "#f5f5f5";
                    e.target.style.fontWeight = "normal";
                }
            }
        }
        categoriesDiv.appendChild(elem);

        if(isAdmin){
            const removeCategory = document.createElement("div");
            removeCategory.classList.add("button");
            removeCategory.innerText = "usuń";
            removeCategory.style.backgroundColor = "red";
            removeCategory.style.color = "white";

            removeCategory.onmouseover = () => {
                removeCategory.style.backgroundColor = "#b22222";
            }

            removeCategory.onmouseout = () => {
                removeCategory.style.backgroundColor = "red";
            }

            removeCategory.onclick = (event) => {
                event.stopPropagation();
                const categoryName = e.name;

                const modal = document.getElementById("delete-confirm-modal");
                const overlay = document.getElementById("delete-modal-overlay");
                modal.classList.add("show");
                overlay.classList.add("show");

                const modalText = modal.querySelector("p");
                modalText.innerText = `Czy na pewno chcesz usunąć kategorie "${e.name}"?`;

                const confirmButton = document.getElementById("confirm-delete-wishlist");
                const cancelButton = document.getElementById("cancel-delete-wishlist");

                confirmButton.onclick = async () => {
                    modal.classList.remove("show");
                    overlay.classList.remove("show");

                    let respData = await fetchAsync({ id: e.id }, "/removeCategory");
                    if (respData != null) {
                        await displayCategories();
                        showConfirmation(`Usunięto kategorie "${categoryName}"!`, true);
                    }
                }

                cancelButton.onclick = () => {
                    modal.classList.remove("show");
                    overlay.classList.remove("show");
                    showConfirmation("Anulowano usuwanie kategorii!", false);
                }
            }
            elem.appendChild(removeCategory);
        }
    });

    if (isAdmin) {
        const addCategory = document.createElement("div");
        addCategory.classList.add("button");
        addCategory.innerText = "Dodaj Kategorie";
        addCategory.style.backgroundColor = "green";
        addCategory.style.color = "white";

        addCategory.onmouseover = () => {
            addCategory.style.backgroundColor = "#2e8b57";
        }

        addCategory.onmouseout = () => {
            addCategory.style.backgroundColor = "green";
        }

        const setupAddCategory = () => {
            addCategory.innerHTML = "Dodaj Kategorie";
            addCategory.onclick = (e) => {
                addCategory.innerHTML = '';
                addCategory.onclick = () => { return; }

                const categoryName = document.createElement("textarea");
                categoryName.placeholder = "Nazwa...";

                const sendNewCategory = document.createElement("div");
                sendNewCategory.innerText = "potwierdź";
                sendNewCategory.style.backgroundColor = "green";
                sendNewCategory.style.color = "white";

                sendNewCategory.onmouseover = () => {
                    sendNewCategory.style.backgroundColor = "#2e8b57";
                }

                sendNewCategory.onmouseout = () => {
                    sendNewCategory.style.backgroundColor = "green";
                }

                sendNewCategory.onclick = async (ev) => {
                    const category = categoryName.value.trim();
                    if (!categoryName.value.trim()) {
                        showConfirmation("Proszę wpisać nazwę kategorii!", false);
                        return;
                    }
                    let data = await fetchAsync({ name: categoryName.value.trim() }, "/addCategory");
                    if (data != null) {
                        await displayCategories();
                        showConfirmation(`Dodano kategorię: "${category}"!`, true);
                    }
                }
                e.target.append(categoryName, sendNewCategory);

                const handleOutsideClick = (event) => {
                    if (!e.target.contains(event.target)) {
                        document.removeEventListener("click", handleOutsideClick);
                        setupAddCategory();
                    }
                }

                document.addEventListener("click", handleOutsideClick);
            }
        }

        setupAddCategory();
        categoriesDiv.appendChild(addCategory);
    }
}

const displayAnnouncementsByCategory = async (categoryName) =>{
    let reqData = {"name":categoryName};
    let data = await fetchAsync(reqData, `/browseAnnouncements/${reqData.name}?userId=${userId}`);
    console.log(data);
    if (data.length === 0) {
        showNoAnnouncementsMessage("select-announcement");
        return;
    } else {
        createAnnouncementsElements(data, "select-announcement");
        document.getElementById("no-announcements").style.display = 'none';
    }
}


const displayAnnouncements = async (e) =>{
    const categoryName = e.target.name;
    let reqData = {"name":categoryName};
    let data = await fetchAsync({}, `/browseAnnouncements/${categoryName}?userId=${userId}`);
    if (data.length === 0) {
        showNoAnnouncementsMessage("select-announcement");
        return;
    } else {
        createAnnouncementsElements(data, "select-announcement");
        document.getElementById("no-announcements").style.display = 'none';
    }
}

const showNoAnnouncementsMessage = (containerId) => {
    const noAnnouncementsMessage = document.createElement('div');
    noAnnouncementsMessage.id = 'no-announcements-message';
    noAnnouncementsMessage.innerHTML = "Brak ogłoszeń!";

    const container = document.getElementById(containerId);
    if (container) {
        container.innerHTML = '';

        container.appendChild(noAnnouncementsMessage);
        container.style.display = 'flex';
    }
}

const fetchAsync = async (bodyObject, url) => {
    const options = {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(bodyObject)
    }
    let response = await fetch(url, options);
    if (!response.ok) return response.status
    else return await response.json() // response.json
}

loadUsers();
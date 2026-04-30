function renderNavbar() {
    const role = localStorage.getItem("role");

    let navLinks = `
        <a href="projects.html">Projects</a>
        <a href="tasks.html">Tasks</a>
    `;

    if (role === "ADMIN") {
        navLinks = `<a href="dashboard.html">Dashboard</a>` + navLinks;
    }

    document.body.insertAdjacentHTML("afterbegin", `
        <div class="navbar">
            <div class="navbar-left">
                ${navLinks}
            </div>
            <div class="navbar-right">
                <button id="logoutBtn">Logout</button>
            </div>
        </div>
    `);

    document.getElementById("logoutBtn").addEventListener("click", logout);
}

function logout() {
    localStorage.clear();
    window.location.href = "login.html";
}

function protectPage(allowAdminOnly = false) {
    const token = localStorage.getItem("token");
    const role = localStorage.getItem("role");

    if (!token) {
        window.location.href = "login.html";
        return;
    }

    if (allowAdminOnly && role !== "ADMIN") {
        alert("Access denied");
        window.location.href = "tasks.html";
    }
}
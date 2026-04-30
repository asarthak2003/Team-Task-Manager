async function login() {
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    const res = await fetch("/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password })
    });

    if (!res.ok) {
        alert("Login failed");
        return;
    }

    const data = await res.json();

    localStorage.setItem("token", data.token);
    localStorage.setItem("role", data.role);

    if (data.role === "ADMIN") {
        window.location.href = "dashboard.html";
    } else {
        window.location.href = "projects.html";
    }
}
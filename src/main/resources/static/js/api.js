const BASE_URL = "/api";

function getToken() {
    return localStorage.getItem("token");
}

async function apiRequest(endpoint, method = "GET", body = null) {

    const headers = {
        "Content-Type": "application/json"
    };

    const token = getToken();
    if (token) {
        headers["Authorization"] = "Bearer " + token;
    }

    const response = await fetch(BASE_URL + endpoint, {
        method,
        headers,
        body: body ? JSON.stringify(body) : null
    });

    if (response.status === 401) {
        alert("Unauthorized! Please login again.");
        window.location.href = "login.html";
        return;
    }

    if (response.status === 204 || method === "DELETE") {
        return;
    }

    return response.json();
}
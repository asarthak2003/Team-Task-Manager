async function loadDashboard() {
    const data = await apiRequest("/dashboard");

    const container = document.getElementById("dashboard");

    container.innerHTML = `
        <div class="dashboard-card">Total Tasks<br><b>${data.totalTasks}</b></div>
        <div class="dashboard-card">To Do<br><b>${data.todo}</b></div>
        <div class="dashboard-card">In Progress<br><b>${data.inProgress}</b></div>
        <div class="dashboard-card">Done<br><b>${data.done}</b></div>
        <div class="dashboard-card">Overdue<br><b>${data.overdue}</b></div>
    `;

    const perUserContainer = document.getElementById("tasksPerUser");
    if (data.tasksPerUser && Object.keys(data.tasksPerUser).length > 0) {
        let html = "<h3>Tasks Per User</h3><div class='dashboard'>";
        for (const [email, count] of Object.entries(data.tasksPerUser)) {
            html += `<div class="dashboard-card">${email}<br><b>${count} task(s)</b></div>`;
        }
        html += "</div>";
        perUserContainer.innerHTML = html;
    }
}

window.onload = loadDashboard;
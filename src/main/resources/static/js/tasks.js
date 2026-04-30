async function loadTasks() {
    const tasks = await apiRequest("/tasks");
    const role = localStorage.getItem("role");
    const container = document.getElementById("taskList");
    container.innerHTML = "";

    if (!tasks || tasks.length === 0) {
        container.innerHTML = "<p>No tasks found.</p>";
        return;
    }

    tasks.forEach(t => {
        const div = document.createElement("div");
        div.className = "card";

        div.innerHTML = `
            <b>${t.title}</b><br>
            ${t.description ? t.description + "<br>" : ""}
            Due: ${t.dueDate || "N/A"}<br>
            Priority: ${t.priority || "N/A"}<br>
            Assigned To: ${t.assignedTo ? t.assignedTo.name : "N/A"}<br>
            Project: ${t.project ? t.project.name : "N/A"}<br>
            <select onchange="updateStatus(${t.id}, this.value)">
                <option value="TODO" ${t.status === "TODO" ? "selected" : ""}>TODO</option>
                <option value="IN_PROGRESS" ${t.status === "IN_PROGRESS" ? "selected" : ""}>IN PROGRESS</option>
                <option value="DONE" ${t.status === "DONE" ? "selected" : ""}>DONE</option>
            </select>
            ${role === "ADMIN" ? `<button onclick="deleteTask(${t.id})">Delete</button>` : ""}
        `;

        container.appendChild(div);
    });
}

async function loadAdminForm() {
    const role = localStorage.getItem("role");
    if (role !== "ADMIN") return;

    document.getElementById("adminTaskForm").style.display = "block";

    const projects = await apiRequest("/projects");
    const projectSelect = document.getElementById("projectId");
    projectSelect.innerHTML = '<option value="">Select Project</option>';
    (projects || []).forEach(p => {
        projectSelect.innerHTML += `<option value="${p.id}">${p.name}</option>`;
    });

    const users = await apiRequest("/users");
    const userSelect = document.getElementById("assignedUserId");
    userSelect.innerHTML = '<option value="">Assign To User</option>';
    (users || []).forEach(u => {
        userSelect.innerHTML += `<option value="${u.id}">${u.name} (${u.role})</option>`;
    });
}

async function createTask() {
    const title = document.getElementById("title").value;
    const description = document.getElementById("desc").value;
    const dueDate = document.getElementById("dueDate").value;
    const priority = document.getElementById("priority").value;
    const projectId = document.getElementById("projectId").value;
    const userId = document.getElementById("assignedUserId").value;

    if (!title || !projectId || !userId) {
        alert("Please fill in Title, Project, and Assigned User.");
        return;
    }

    await apiRequest(`/tasks?projectId=${projectId}&userId=${userId}`, "POST", {
        title,
        description,
        status: "TODO",
        priority,
        dueDate: dueDate || null
    });

    loadTasks();
}

async function updateStatus(id, status) {
    await apiRequest(`/tasks/${id}`, "PUT", { status });
    loadTasks();
}

async function deleteTask(id) {
    if (!confirm("Delete this task?")) return;
    await apiRequest(`/tasks/${id}`, "DELETE");
    loadTasks();
}

loadAdminForm();
loadTasks();
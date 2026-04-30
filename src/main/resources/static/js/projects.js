async function loadProjects() {
    const projects = await apiRequest("/projects");
    const role = localStorage.getItem("role");
    const container = document.getElementById("projectList");
    container.innerHTML = "";

    if (role === "ADMIN") {
        document.getElementById("adminProjectForm").style.display = "block";
    }

    if (!projects || projects.length === 0) {
        container.innerHTML = "<p>No projects found.</p>";
        return;
    }

    let allUsers = [];
    if (role === "ADMIN") {
        allUsers = await apiRequest("/users") || [];
    }

    projects.forEach(p => {
        const div = document.createElement("div");
        div.className = "card";

        let membersHtml = "";
        if (p.members && p.members.length > 0) {
            membersHtml = "<br><b>Members:</b><ul style='margin:4px 0;padding-left:16px;'>" +
                p.members.map(m =>
                    `<li>${m.name} (${m.role})
                        ${role === "ADMIN"
                            ? `<button onclick="removeMember(${p.id}, ${m.id})"
                               style="width:auto;padding:2px 8px;margin-left:8px;">Remove</button>`
                            : ""}
                    </li>`
                ).join("") +
                "</ul>";
        }

        let addMemberHtml = "";
        if (role === "ADMIN") {
            const userOptions = allUsers
                .map(u => `<option value="${u.id}">${u.name} (${u.role})</option>`)
                .join("");
            addMemberHtml = `
                <select id="memberSelect_${p.id}">
                    <option value="">Select User to Add</option>
                    ${userOptions}
                </select>
                <button onclick="addMember(${p.id})" style="width:auto;padding:6px 12px;">Add Member</button>
            `;
        }

        div.innerHTML = `
            <b>${p.name}</b><br>
            ${p.description || ""}
            ${membersHtml}
            ${addMemberHtml}
            ${role === "ADMIN"
                ? `<button onclick="deleteProject(${p.id})" style="margin-top:8px;">Delete Project</button>`
                : ""}
        `;

        container.appendChild(div);
    });
}

async function createProject() {
    const name = document.getElementById("name").value;
    const description = document.getElementById("desc").value;
    if (!name) { alert("Project name is required."); return; }
    await apiRequest("/projects", "POST", { name, description });
    loadProjects();
}

async function deleteProject(id) {
    if (!confirm("Delete this project?")) return;
    await apiRequest(`/projects/${id}`, "DELETE");
    loadProjects();
}

async function addMember(projectId) {
    const select = document.getElementById(`memberSelect_${projectId}`);
    const userId = select.value;
    if (!userId) { alert("Please select a user to add."); return; }
    await apiRequest(`/projects/${projectId}/add-member/${userId}`, "POST");
    loadProjects();
}

async function removeMember(projectId, userId) {
    if (!confirm("Remove this member from the project?")) return;
    await apiRequest(`/projects/${projectId}/remove-member/${userId}`, "DELETE");
    loadProjects();
}

loadProjects();
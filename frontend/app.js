function login() {
    window.location.href = "/oauth2/authorization/github"; // Startar GitHub inloggning
}

async function getUserFiles() {
    const response = await fetch('/api/files/user-files', { credentials: 'include' });
    const files = await response.json();
    
    const fileList = document.getElementById('file-list');
    fileList.innerHTML = "";
    files.forEach(file => {
        const li = document.createElement('li');
        li.textContent = file.name;
        fileList.appendChild(li);
    });
}

document.addEventListener("DOMContentLoaded", getUserFiles);
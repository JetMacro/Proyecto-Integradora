document.addEventListener("DOMContentLoaded", () => {
    const toggleButton = document.getElementById("menu-toggle");
    const body = document.body;

    if (toggleButton) {
        toggleButton.addEventListener("click", (e) => {
            e.preventDefault();
            body.classList.toggle("sb-hidden");
        });
    } 

    const currentPath = window.location.pathname.split("/").pop() || "index.html";
    const links = document.querySelectorAll(".nav-link");
    links.forEach((link) => {
        if (link.getAttribute("href") === currentPath) {
            link.classList.add("active");
        }
    });

    // -------- Lógica de Login --------
    const form = document.querySelector(".form");
    if (!form) return; 

    const usernameInput = document.getElementById("username");
    const passwordInput = document.getElementById("password");

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const nombreUsuario = usernameInput.value.trim();
        const contrasenia = passwordInput.value.trim();

        if (!nombreUsuario || !contrasenia) {
            alert("Por favor, completa todos los campos.");
            return;
        }

        try {
            // RUTA RELATIVA: Funciona en localhost y en Railway por igual
            const response = await fetch("/api/usuario/login", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({
                    matricula: nombreUsuario,
                    contrasenia: contrasenia
                })
            });

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({ error: "Credenciales incorrectas o error en servidor" }));
                alert(errorData.error || "Error al iniciar sesión.");
                return;
            }

            const data = await response.json();

            // Guardar datos de sesión
            localStorage.setItem("id_usuario", data.id_usuario);
            localStorage.setItem("id_rol", data.id_rol);
            localStorage.setItem("nombre_usuario", `${data.nombre} ${data.apellido_paterno}`);
            localStorage.setItem("matricula", data.matricula);

            // Generar Token
            const prefijo = data.matricula.trim().toUpperCase().substring(0, 3).replace("A", "@");
            const tokenInicial = (prefijo + "-" + Date.now().toString()).padEnd(25, "X");
            localStorage.setItem("sessionToken", tokenInicial);

            // REDIRECCIÓN CRÍTICA: Sin el nombre del proyecto
            window.location.href = "administrador/dashboard.html";

        } catch (error) {
            console.error("Error:", error);
            alert("Error de conexión con el servidor.");
        }
    });
});

// CONFIGURACIÓN DE RUTAS PARA RAILWAY
const PATH_INICIO = window.location.origin + "/index.html";

function verificarSesion() {
    const path = window.location.pathname;
    
    // Si estamos en el login, no verificamos sesión
    if (path.endsWith("index.html") || path === "/" || path === "") return;

    const token = localStorage.getItem("sessionToken");
    if (!token) {
        window.location.href = PATH_INICIO;
        return;
    }

    // Validación de tiempo del token (10 min)
    const partes = token.split("-");
    if (partes.length > 1) {
        const timestampToken = parseInt(partes[1]);
        const ahora = Date.now();
        const diezMinutos = 10 * 60 * 1000;

        if (ahora - timestampToken > diezMinutos) {
            localStorage.clear();
            window.location.href = PATH_INICIO;
        }
    }
}

window.addEventListener("load", verificarSesion);

// Refrescar token al hacer clic
document.addEventListener("click", () => {
    const tokenActual = localStorage.getItem("sessionToken");
    if (tokenActual) {
        const mat = localStorage.getItem("matricula") || "USR";
        const prefijo = mat.trim().toUpperCase().substring(0, 3).replace("A", "@");
        const nuevoToken = (prefijo + "-" + Date.now().toString()).padEnd(25, "X");
        localStorage.setItem("sessionToken", nuevoToken);
    }
});

setInterval(verificarSesion, 5000);
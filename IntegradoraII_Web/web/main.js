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
            const response = await fetch("/api/usuario/login", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({
                    matricula: nombreUsuario,
                    contrasenia: contrasenia
                })
            });

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({ error: "Credenciales incorrectas" }));
                alert(errorData.error || "Error al iniciar sesión.");
                return;
            }

            const data = await response.json();

            localStorage.setItem("id_usuario", data.id_usuario);
            localStorage.setItem("id_rol", data.id_rol);
            localStorage.setItem("nombre_usuario", `${data.nombre} ${data.apellido_paterno}`);
            localStorage.setItem("matricula", data.matricula);

            // Generar Token con Timestamp inicial
            const prefijo = data.matricula.trim().toUpperCase().substring(0, 3).replace("A", "@");
            const tokenInicial = (prefijo + "-" + Date.now().toString()).padEnd(25, "X");
            localStorage.setItem("sessionToken", tokenInicial);

            window.location.href = "administrador/dashboard.html";

        } catch (error) {
            console.error("Error:", error);
            alert("Error de conexión con el servidor.");
        }
    });
});

// CONFIGURACIÓN DE RUTAS
const PATH_INICIO = window.location.origin + "/index.html";

function verificarSesion() {
    const path = window.location.pathname;
    if (path.endsWith("index.html") || path === "/" || path === "") return;

    const token = localStorage.getItem("sessionToken");
    if (!token) {
        window.location.href = PATH_INICIO;
        return;
    }

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

// Función centralizada para refrescar el tiempo del token
function refrescarSesion() {
    const tokenActual = localStorage.getItem("sessionToken");
    if (tokenActual) {
        const mat = localStorage.getItem("matricula") || "USR";
        const prefijo = mat.trim().toUpperCase().substring(0, 3).replace("A", "@");
        const nuevoToken = (prefijo + "-" + Date.now().toString()).padEnd(25, "X");
        localStorage.setItem("sessionToken", nuevoToken);
    }
}

// Escuchar carga de página
window.addEventListener("load", verificarSesion);

// REQUISITO: Refrescar token con CUALQUIER actividad (No solo clic)
// Esto evita que la sesión se cierre si el usuario está escribiendo o moviendo el mouse
["click", "mousemove", "keypress"].forEach(evt => {
    document.addEventListener(evt, refrescarSesion);
});

// Verificación automática cada 10 segundos
setInterval(verificarSesion, 10000);
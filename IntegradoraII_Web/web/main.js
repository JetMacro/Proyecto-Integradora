/**
 * Archivo: main.js
 * Controlador de autenticación de sesión y UI unificado
 */

// Redirección centralizada (Mantenemos tu versión segura)
const LOGIN_PAGE = window.location.origin + "/index.html";

document.addEventListener("DOMContentLoaded", () => {

    // ==========================================
    // 1. LÓGICA DE INTERFAZ (Menú y Navegación de tu compañero)
    // ==========================================
    const toggleButton = document.getElementById("menu-toggle");
    const body = document.body;

    // Funcionalidad del menú de hamburguesa
    if (toggleButton) {
        toggleButton.addEventListener("click", (e) => {
            e.preventDefault();
            body.classList.toggle("sb-hidden");
        });
    }

    // Marcar el enlace activo en la barra de navegación
    let currentPath = window.location.pathname.split("/").pop() || "index.html";
    currentPath = currentPath.split("?")[0]; 

    const links = document.querySelectorAll(".nav-link");
    
    links.forEach(link => link.classList.remove("active"));
    links.forEach((link) => {
        const href = link.getAttribute("href");
        if (href && href.split("?")[0] === currentPath) {
            link.classList.add("active");
        }
    });

    // ==========================================
    // 2. LÓGICA DE AUTENTICACIÓN (Tu versión de Railway)
    // ==========================================
    const API_LOGIN = "/api/usuario/login"; // Mantenemos ruta relativa para Railway
    const DASHBOARD_URL = "administrador/dashboard.html";

    const form = document.getElementById("form-login"); 
    
    if (!form) return; // Si no hay formulario, detenemos aquí

    const usernameInput = document.getElementById("username");
    const passwordInput = document.getElementById("password");

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const nombreUsuario = usernameInput.value.trim();
        const contrasenia = passwordInput.value.trim();

        if (!nombreUsuario || !contrasenia) {
            Swal.fire('Atención', 'Por favor, completa todos los campos.', 'warning');
            return;
        }

        Swal.fire({
            title: 'Autenticando...', 
            allowOutsideClick: false, 
            didOpen: () => Swal.showLoading()
        });

        try {
            const response = await fetch(API_LOGIN, {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({
                    matricula: nombreUsuario,
                    contrasenia: contrasenia
                })
            });

            if (!response.ok) {
                const errorTexto = await response.text();
                console.error("Respuesta del servidor:", errorTexto);
                Swal.fire('Error', 'El servidor encontró un problema interno.', 'error');
                return;
            }

            const data = await response.json();

            if (response.ok) {
                console.log("Usuario autenticado:", data);

                // Guardado de variables de sesión
                localStorage.setItem("id_usuario", data.id_usuario);
                localStorage.setItem("id_rol", data.id_rol);
                localStorage.setItem("nombre_usuario", `${data.nombre} ${data.apellido_paterno}`);
                localStorage.setItem("matricula", data.matricula);

                // Agregado token de seguridad
                const prefijo = data.matricula.trim().toUpperCase().substring(0, 3).replace("A", "@");
                const tokenInicial = (prefijo + "-" + Date.now().toString()).padEnd(25, "X");
                localStorage.setItem("sessionToken", tokenInicial);
                
                Swal.close();
                window.location.href = DASHBOARD_URL;
            } else {
                Swal.fire('Error', data.error || "Credenciales incorrectas.", 'error');
            }

        } catch (error) {
            console.error("Error al conectarse al servidor:", error);
            Swal.fire('Error de Conexión', 'No se pudo conectar al servidor.', 'error');
        }
    });
});

// ==========================================
// 3. LÓGICA DE SEGURIDAD (Manejo de Sesión)
// ==========================================

function verificarSesion() {
    const path = window.location.pathname;
    // Evitar verificar en el index/login
    if (path.endsWith("index.html") || path.endsWith("/") || path === "") return;

    const token = localStorage.getItem("sessionToken");
    if (!token) {
        window.location.href = LOGIN_PAGE;
        return;
    }

    const partes = token.split("-");
    if (partes.length > 1) {
        const timestampToken = parseInt(partes[1]);
        const ahora = Date.now();
        const diezMinutos = 10 * 60 * 1000;

        if (ahora - timestampToken > diezMinutos) {
            localStorage.clear();
            window.location.href = LOGIN_PAGE;
        }
    }
}

function refrescarSesion() {
    const tokenActual = localStorage.getItem("sessionToken");
    if (tokenActual) {
        const mat = localStorage.getItem("matricula") || "USR";
        const prefijo = mat.trim().toUpperCase().substring(0, 3).replace("A", "@");
        const nuevoToken = (prefijo + "-" + Date.now().toString()).padEnd(25, "X");
        localStorage.setItem("sessionToken", nuevoToken);
    }
}

// Iniciar monitoreo de sesión
window.addEventListener("load", verificarSesion);
["click", "mousemove", "keypress"].forEach(evt => {
    document.addEventListener(evt, refrescarSesion);
});
setInterval(verificarSesion, 10000);
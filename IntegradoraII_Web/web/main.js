/**
 * Archivo: main.js
 * Controlador de autenticacion de sesion
 */
document.addEventListener("DOMContentLoaded", () => {

    // Rutas Locales
    const API_LOGIN = "/api/usuario/login";
    const DASHBOARD_URL = "administrador/dashboard.html";

    const form = document.getElementById("form-login");
    if (!form)
        return;

    const usernameInput = document.getElementById("username");
    const passwordInput = document.getElementById("password");

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const nombreUsuario = usernameInput.value.trim();
        const contrasenia = passwordInput.value.trim();

        if (!nombreUsuario || !contrasenia) {
            Swal.fire('Atencion', 'Por favor, completa todos los campos.', 'warning');
            return;
        }

        Swal.fire({title: 'Autenticando...', allowOutsideClick: false, didOpen: () => Swal.showLoading()});

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
                Swal.fire('Error', 'Credenciales incorrectas o problema en el servidor.', 'error');
                return;
            }

            const data = await response.json();

            if (response.ok) {
                console.log("Usuario autenticado:", data);

                // Guardado de variables de sesion
                localStorage.setItem("id_usuario", data.id_usuario);
                localStorage.setItem("id_rol", data.id_rol);
                localStorage.setItem("nombre_usuario", `${data.nombre} ${data.apellido_paterno}`);
                localStorage.setItem("matricula", data.matricula);

                // agregado token
                const prefijo = data.matricula.trim().toUpperCase().substring(0, 3).replace("A", "@");
                const tokenInicial = (prefijo + "-" + Date.now().toString()).padEnd(25, "X");
                localStorage.setItem("sessionToken", tokenInicial);
                // fin del agregado
                Swal.close();
                window.location.href = DASHBOARD_URL;
            } else {
                Swal.fire('Error', data.error || "Credenciales incorrectas.", 'error');
            }

        } catch (error) {
            console.error("Error al conectarse al servidor:", error);
            Swal.fire('Error de Conexion', 'No se pudo conectar al servidor.', 'error');
        }
    });
});

// redireccion centralizada 
const LOGIN_PAGE = window.location.origin + "/index.html";


// agregados token seguridad

function verificarSesion() {
    const path = window.location.pathname;
    if (path.endsWith("index.html") || path.endsWith("/") || path === "")
        return;

    const token = localStorage.getItem("sessionToken");
    if (!token) {
        window.location.href = LOGIN_PAGE;// cambio de la locacion
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

const toggleButton = document.getElementById("menu-toggle");

const body = document.body;



if (toggleButton) {

    toggleButton.addEventListener("click", (e) => {

        e.preventDefault(); // Evita el comportamiento por defecto del enlace

        body.classList.toggle("sb-hidden");

    });

}

window.addEventListener("load", verificarSesion);
["click", "mousemove", "keypress"].forEach(evt => {
    document.addEventListener(evt, refrescarSesion);
});
setInterval(verificarSesion, 10000);
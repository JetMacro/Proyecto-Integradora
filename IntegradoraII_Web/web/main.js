document.addEventListener("DOMContentLoaded", () => {
    const toggleButton = document.getElementById("menu-toggle");
    const body = document.body;

    if (toggleButton) {
        const icon = toggleButton.querySelector("i");
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

    // -------- Validación de login --------
    const form = document.querySelector(".form");
    if (!form)
        return; // Evita errores si no encuentra el formulario.

    const usernameInput = document.getElementById("username");
    const passwordInput = document.getElementById("password");

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        // Obtenemos los valores de los inputs.
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
                // AQUÍ ESTÁ LA MAGIA: Cambiamos para que envíe "matricula" en lugar de "nombreUsuario"
                // para que coincida exactamente con tu modelo de Java.
                body: JSON.stringify({
                    matricula: nombreUsuario,
                    contrasenia: contrasenia
                })
            });
            
            if (!response.ok) {
                const errorData = await response.json().catch(() => ({ error: "Error interno del servidor" }));
                alert(errorData.error || "Credenciales incorrectas.");
                return;
            }

            // aqui inicia la modificacion Mau
            const data = await response.json();

            if (response.ok) {
                console.log("Usuario autenticado:", data);

                // esto me permite guardar los datos en el local storage
                localStorage.setItem("id_usuario", data.id_usuario);
                localStorage.setItem("id_rol", data.id_rol);
                localStorage.setItem("nombre_usuario", `${data.nombre} ${data.apellido_paterno}`);
                localStorage.setItem("matricula", data.matricula);

                //  TOKEN
                const prefijo = data.matricula.trim().toUpperCase().substring(0, 3).replace("A", "@");
                const tokenInicial = (prefijo + "-" + Date.now().toString()).padEnd(25, "X");
                localStorage.setItem("sessionToken", tokenInicial);

                window.location.href = "administrador/dashboard.html";
            } else {
                alert(data.error || "Credenciales incorrectas.");
            }

        } catch (error) {
            console.error("Error al conectarse al servidor:", error);
            alert("No se pudo conectar al servidor. Intenta más tarde.");
        }
    });
});

// Agregado token
const PATH_INICIO = window.location.origin + "/index.html";

// token agregado
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

window.addEventListener("load", verificarSesion);

// Refrescar token
document.addEventListener("click", () => {
    const tokenActual = localStorage.getItem("sessionToken");
    if (tokenActual) {
        const mat = localStorage.getItem("matricula") || "USR";
        const prefijo = mat.trim().toUpperCase().substring(0, 3).replace("A", "@");
        const nuevoToken = (prefijo + "-" + Date.now().toString()).padEnd(25, "X");
        localStorage.setItem("sessionToken", nuevoToken);
    }
});

// Verificación en segundo plano
setInterval(verificarSesion, 5000);
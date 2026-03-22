/**
 * ARCHIVO: prstamo.js - VERSIÓN INTEGRAL
 * Solo copia, pega y guarda.
 */
let listaGlobalPrestamos = [];
console.log("¡El archivo JS se cargó correctamente!");
document.addEventListener("DOMContentLoaded", () => {
    // --- INICIALIZACIÓN DE UI ---
    const sidebar = document.querySelector(".sidebar");
    const menuToggle = document.getElementById("menu-toggle");

    if (menuToggle) {
        menuToggle.addEventListener("click", () => {
            sidebar.classList.toggle("active");
        });
        
    const nombre = localStorage.getItem("nombre_usuario") || "Usuario";
    const rolRaw = localStorage.getItem("id_rol") || "Invitado";
    
    if (document.getElementById('userNameDisplay')) document.getElementById('userNameDisplay').textContent = nombre;
    if (document.getElementById('userRoleDisplay')) document.getElementById('userRoleDisplay').textContent = rolRaw;
    }

    const tableBody = document.querySelector("#tablaPrestamo tbody");
    const apiUrl = "http://localhost:8080/IntegradoraII_Web/api/prestamos/getAll";

    // --- 1. FUNCIÓN DE CARGA DE TABLA ---
    window.fetchPrestamo = async function () {
        try {
            const response = await fetch(apiUrl);
            if (!response.ok)
                throw new Error(`HTTP ${response.status}`);

            const data = await response.json();

            if (!data || data.length === 0) {
                tableBody.innerHTML = `<tr><td colspan="7" class="text-center text-muted">No hay préstamos registrados</td></tr>`;
                actualizarResumen([]);
                return;
            }
            listaGlobalPrestamos = data;
            console.log("Datos cargados en listaGlobalPrestamos:", listaGlobalPrestamos);

            actualizarResumen(data);
            // 1. Normalizamos a minúsculas para evitar fallos de comparación
            const rol = (localStorage.getItem("id_rol") || "").trim().toLowerCase();
            console.log("Rol detectado:", rol); // Para que verifiques en consola

            tableBody.innerHTML = "";
            data.forEach(item => {
                let badgeClass = "bg-success";
                if (item.estatus_prestamo === 'Activo')
                    badgeClass = "bg-warning";
                if (item.estatus_prestamo === 'Atrasado' || item.estatus_prestamo === 'Extraviado')
                    badgeClass = "bg-danger";

                let botonesAccion = "";

                // 2. Comparamos contra minúsculas
                if (rol === "administrador") {
                    botonesAccion = `
                    <button class="btn btn-sm btn-outline-primary me-2" onclick="prepararNuevaVersion(${item.id_prestamo})">
                        <i class="bi bi-pencil-square"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-danger me-2" onclick="eliminarItem(${item.id_prestamo})">
                        <i class="bi bi-trash3"></i>
                    </button>
                `;
                }

                // Botón de ver (siempre visible)
                botonesAccion += `
                <button class="btn btn-sm btn-outline-success" onclick="mostrarDetalle(${item.id_prestamo})">
                    <i class="bi bi-eye"></i>
                </button>
            `;

                const row = document.createElement("tr");
                // 3. Limpiamos el HTML (quitamos el td duplicado al final)
                row.innerHTML = `
                <td class="fw-medium">${item.matricula || 'N/A'}</td>
                <td>${item.id_mobiliario || 'Mueble'}</td>
                <td>${item.fecha_salida || '---'}</td>
                <td>${item.fecha_devolucion_real || '---'}</td>
                <td>
                    <span class="badge ${badgeClass}">${item.estatus_prestamo}</span>
                </td>
                <td>${item.observaciones_salida || 'Sin notas'}</td>
                <td class="text-end">
                    ${botonesAccion}
                </td>
            `;
                tableBody.appendChild(row);
            });

        } catch (error) {
            console.error("Error préstamo:", error);
            tableBody.innerHTML = `<tr><td colspan="7" class="text-center text-danger">Error al cargar datos</td></tr>`;
        }
    };


    // --- 2. CARDS INFORMATIVAS ---

    function actualizarResumen(datos) {
        const cardTotal = document.getElementById('cardTotal');
        const cardEnUso = document.getElementById('cardEnUso');
        const cardDisponibles = document.getElementById('cardDisponibles');
        const cardAtrasados = document.getElementById('cardAtrasados');

        // IMPORTANTE: Los nombres de los filtros deben coincidir con tu JSON del API
        if (cardTotal)
            cardTotal.textContent = datos.length;

        if (cardEnUso) {
            // Contamos los que están 'Activos'
            cardEnUso.textContent = datos.filter(r => r.estatus_prestamo === 'Activo').length;
        }

        if (cardDisponibles) {
            // En un módulo de préstamos, disponibles suelen ser los 'Devueltos'
            cardDisponibles.textContent = datos.filter(r => r.estatus_prestamo === 'Devuelto').length;
        }

        if (cardAtrasados) {
            cardAtrasados.textContent = datos.filter(r => r.estatus_prestamo === 'Atrasado').length + datos.filter(r => r.estatus_prestamo === 'Extraviado').length;
        }
    }

    // --- IMPORTANTE: LLAMAR A LA FUNCIÓN ---
    fetchPrestamo();

});

window.mostrarDetalle = function (id) {
    // 1. Buscamos el objeto en nuestra lista global por su ID de préstamo
    const p = listaGlobalPrestamos.find(i => Number(i.id_prestamo) === Number(id));

    if (p) {
        // --- DATOS DEL USUARIO ---
        document.getElementById('det-nombre').innerText = p.nombre || 'N/A';
        document.getElementById('det-matricula').innerText = p.matricula || 'N/A';
        document.getElementById('det-rol').innerText = p.id_rol || 'Usuario';
        document.getElementById('det-turno').innerText = p.id_turno || 'N/A';
        document.getElementById('det-correo').innerText = p.correo || 'N/A';
        document.getElementById('det-telefono').innerText = p.telefono || 'N/A';

        // --- DATOS DEL PRÉSTAMO ---
        document.getElementById('det-id-prestamo').innerText = p.id_prestamo;
        document.getElementById('det-mobiliario').innerText = p.id_mobiliario;
        document.getElementById('det-fecha-salida').innerText = p.fecha_salida;
        document.getElementById('det-fecha-prevista').innerText = p.fecha_devolucion_prevista;

        // --- ESTATUS CON BADGE ---
        const badgeEstatus = document.getElementById('det-estatus');
        badgeEstatus.innerText = p.estatus_prestamo;

        // Limpiamos clases de color previas y aplicamos la nueva
        badgeEstatus.className = "badge";
        if (p.estatus_prestamo === 'Activo')
            badgeEstatus.classList.add('bg-warning', 'text-dark');
        else if (p.estatus_prestamo === 'Atrasado')
            badgeEstatus.classList.add('bg-danger');
        else if (p.estatus_prestamo === 'Devuelto')
            badgeEstatus.classList.add('bg-success');
        else
            badgeEstatus.classList.add('bg-secondary');

        // --- OBSERVACIONES ---
        document.getElementById('det-obs-salida').innerText = p.observaciones_salida || 'Sin observaciones de salida';
        document.getElementById('det-obs-entrada').innerText = p.observaciones_entrada || 'Sin registro de devolución aún';

        // 2. MOSTRAR EL MODAL
        const modalElement = document.getElementById('modalDetallePrestamo');
        const modalInstance = new bootstrap.Modal(modalElement);
        modalInstance.show();
    } else {
        console.error("No se encontró el préstamo con ID:", id);
        Swal.fire('Error', 'No se pudieron cargar los detalles del registro', 'error');
    }
}


window.eliminarItem = async (id) => {
    console.log("Iniciando eliminación para el ID:", id);

    // 1. Preguntar de inmediato
    const result = await Swal.fire({
        title: '¿Estás seguro?',
        text: "Se eliminará el registro con ID: " + id,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Sí, eliminar'
    });

    // 2. Si el usuario confirma
    if (result.isConfirmed) {
        try {
            console.log("Enviando petición al servidor...");
            const response = await fetch(`http://localhost:8080/IntegradoraII_Web/api/prestamos/delete`, {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({id_prestamo: parseInt(id)}) // Enviamos el ID que llegó por parámetro
            });

            if (response.ok) {
                // 1. Cerramos el modal de Bootstrap primero
                const modalEl = document.getElementById('modalDetalleInventario');
                const modalInstance = bootstrap.Modal.getInstance(modalEl);

                if (modalInstance) {
                    modalInstance.hide();
                }

                // 2. Limpieza forzada de residuos de Bootstrap (el fondo oscuro)
                document.querySelectorAll('.modal-backdrop').forEach(el => el.remove());
                document.body.classList.remove('modal-open');
                document.body.style.paddingRight = '';

                // 3. Mostrar alerta de éxito
                await Swal.fire({
                    title: '¡Eliminado!',
                    text: 'El registro se borró correctamente.',
                    icon: 'success',
                    timer: 1500 // Se cierra solo en 1.5 segundos
                });

                // 4. ACTUALIZAR LA TABLA
                if (typeof window.fetchInventario === "function") {
                    window.fetchInventario();
                } else {
                    // Si por algo no encuentra la función, recarga la página completa
                    location.reload();
                }
            } else {
                const errorData = await response.json();
                Swal.fire('Error en servidor', errorData.error || 'No se pudo eliminar', 'error');
            }
        } catch (error) {
            console.error("Error de conexión:", error);
            Swal.fire('Error', 'No se pudo conectar con el servidor', 'error');
        }
    }
};

// --- FUNCION PARA ABRIR EL MODAL Y CARGAR DATOS ---
window.prepararNuevaVersion = function (id) {
    const p = listaGlobalPrestamos.find(i => Number(i.id_prestamo) === Number(id));

    if (p) {
        document.getElementById('editPrestamoId').value = p.id_prestamo;
        document.getElementById('editEstatus').value = p.estatus_prestamo;
        document.getElementById('editObservacionesEntrada').value = p.observaciones_entrada || "";

        const modal = new bootstrap.Modal(document.getElementById('modalEditarPrestamo'));
        modal.show();
    }
};

// --- FUNCION PARA ENVIAR LOS CAMBIOS AL API ---
window.guardarCambiosPrestamo = async function () {
    const id = document.getElementById('editPrestamoId').value;
    const estatus = document.getElementById('editEstatus').value;
    const obs = document.getElementById('editObservacionesEntrada').value;

    const datos = {
        id_prestamo: parseInt(id),
        estatus_prestamo: estatus,
        observaciones_entrada: obs
    };

    try {
        Swal.showLoading();
        const response = await fetch(`http://localhost:8080/IntegradoraII_Web/api/prestamos/update`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(datos)
        });

        if (response.ok) {
            Swal.fire('¡Actualizado!', 'El préstamo se actualizó correctamente.', 'success');
            bootstrap.Modal.getInstance(document.getElementById('modalEditarPrestamo')).hide();
            window.fetchPrestamo(); // Recargar tabla
        } else {
            throw new Error("Error en la respuesta del servidor");
        }
    } catch (error) {
        console.error(error);
        Swal.fire('Error', 'No se pudo actualizar el registro', 'error');
    }
};

window.guardarNuevoPrestamo = async function () {
    // 1. Obtener valores
    const matricula = document.getElementById('newMatricula').value;
    const idInventario = document.getElementById('newIdInventario').value;
    const fechaPrevista = document.getElementById('newFechaPrevista').value;
    const obsSalida = document.getElementById('newObsSalida').value;

    // 2. Validar campos obligatorios
    if (!matricula || !idInventario || !fechaPrevista) {
        Swal.fire('Atención', 'Por favor llena todos los campos obligatorios (*)', 'warning');
        return;
    }

    const datos = {
        matricula: document.getElementById('newMatricula').value,
    id_mobiliario: document.getElementById('newIdInventario').value, // Verifica que en Java sea id_mobiliario
    fecha_devolucion_prevista: document.getElementById('newFechaPrevista').value,
    observaciones_salida: document.getElementById('newObsSalida').value
    };

    try {
        Swal.showLoading();
        const response = await fetch(`http://localhost:8080/IntegradoraII_Web/api/prestamos/insert`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(datos)
        });

        if (response.ok) {
            Swal.fire('¡Éxito!', 'Préstamo registrado correctamente', 'success');
            bootstrap.Modal.getInstance(document.getElementById('modalNuevoPrestamo')).hide();
            document.getElementById('formNuevoPrestamo').reset();
            window.fetchPrestamo(); // Refrescar tabla
        } else {
            const error = await response.json();
            throw new Error(error.mensaje || "Error al insertar");
        }
    } catch (error) {
        console.error(error);
        // 'error.message' contendrá el texto: "La matrícula '...' no está registrada."
        Swal.fire({
            title: 'Validación de Datos',
            text: error.message,
            icon: 'error',
            confirmButtonColor: '#2a2155'
        });
    }
};

window.cerrarSesion = () => {
    localStorage.clear();
    window.location.href = "../index.html";
};
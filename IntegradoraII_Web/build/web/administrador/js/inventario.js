/**
 * ARCHIVO: inventario.js - VERSIÓN INTEGRAL
 * Solo copia, pega y guarda.
 */
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

    const tableBody = document.querySelector("#tablaInventario tbody");
    const apiUrl = "http://localhost:8080/IntegradoraII_Web/api/inventario/getAll";

    // --- 1. FUNCIÓN DE CARGA DE TABLA ---
    window.fetchInventario = async function () {
        try {
            const response = await fetch(apiUrl);
            if (!response.ok)
                throw new Error(`HTTP ${response.status}`);

            const data = await response.json();
            actualizarCards(data);

            if (!data.length) {
                tableBody.innerHTML = `<tr><td colspan="7" class="text-center text-muted">No hay inventario registrado</td></tr>`;
                return;
            }

            tableBody.innerHTML = "";
            data.forEach(item => {
                const disponibles = item.disponibles ?? 0;
                const total = item.total ?? 0;
                const enUso = total - disponibles;
                const porcentaje = total ? Math.round((disponibles / total) * 100) : 0;

                let badgeClass = "bg-success";
                if (porcentaje < 15)
                    badgeClass = "bg-danger";
                else if (porcentaje < 50)
                    badgeClass = "bg-warning";

                const row = document.createElement("tr");
                row.innerHTML = `
                    <td class="fw-medium">${item.material}</td>
                    <td>${item.categoria}</td>
                    <td>${total}</td>
                    <td>${disponibles}</td>
                    <td>${enUso}</td>
                    <td class="fw-medium">
                        <span class="badge ${badgeClass}">${porcentaje}% disponible</span>
                    </td>
                    <td>
                        <button class="btn btn-sm btn-outline-success" title="Detalles" onclick="verDetalleMaterial('${item.material}')">
                            <i class="bi bi-eye"></i>
                        </button>
                    </td>
                `;
                tableBody.appendChild(row);
            });

            actualizarFiltrosDesdeTabla();

        } catch (error) {
            console.error("Error inventario:", error);
            tableBody.innerHTML = `<tr><td colspan="7" class="text-center text-danger">Error al cargar inventario</td></tr>`;
        }
    };

    // --- 2. CARDS INFORMATIVAS ---
    function actualizarCards(data) {
        let totalRecursos = 0, totalDisponibles = 0, totalEnUso = 0, stockBajo = 0;

        data.forEach(item => {
            const disponibles = item.disponibles ?? 0;
            const total = item.total ?? 0;
            totalRecursos += total;
            totalDisponibles += disponibles;
            totalEnUso += (total - disponibles);
            if (total > 0 && (disponibles / total) * 100 < 30)
                stockBajo++;
        });

        if (document.getElementById("cardTotal"))
            document.getElementById("cardTotal").textContent = totalRecursos;
        if (document.getElementById("cardDisponibles"))
            document.getElementById("cardDisponibles").textContent = totalDisponibles;
        if (document.getElementById("cardEnUso"))
            document.getElementById("cardEnUso").textContent = totalEnUso;
        if (document.getElementById("cardStockBajo"))
            document.getElementById("cardStockBajo").textContent = stockBajo;
    }

    // --- 3. CARGAS INICIALES AL CARGAR DOM ---
    window.cargarEdificios();
    window.fetchInventario();
});

// --- 4. FUNCIÓN DE GUARDADO (GLOBAL) ---
window.guardarInventario = async () => {
    try {
        // Obtenemos los valores directamente
        const idEdificio = document.getElementById("txtIdEdificio").value;
        const idSalon = document.getElementById("txtIdSalon").value;

        // Si no hay valores, no seguimos
        if (!idEdificio || !idSalon) {
            Swal.fire('Atención', 'Selecciona edificio y salón', 'warning');
            return;
        }

        const datos = {
            id_salon: parseInt(idSalon),
            id_mobiliario: document.getElementById("txtIdMobi").value,
            id_edificio: parseInt(idEdificio),
            ubicacion: document.getElementById("selUbicacion").value,
            material: document.getElementById("selTipo").value,
            categoria: document.getElementById("selCategoria").value,
            estatus: "Activo",
            id_estatus_actual: "Disponible",
            observaciones: document.getElementById("txtObs").value
        };

        console.log("Enviando estos datos:", datos);

        const response = await fetch("http://localhost:8080/IntegradoraII_Web/api/inventario/save", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(datos)
        });

        // IMPORTANTE: Primero verificamos si la respuesta es exitosa
        if (response.ok) {
            Swal.fire('¡Éxito!', 'Guardado correctamente', 'success');
            // 1. Resetear los campos de texto y selects estáticos
            const formulario = document.getElementById("formInventario");
            if (formulario)
                formulario.reset();

            // 2. Limpiar los selects dinámicos para que no se queden cargados
            document.getElementById("txtIdEdificio").value = "";
            const selectSalon = document.getElementById("txtIdSalon");
            selectSalon.innerHTML = '<option value="">Seleccione un salón...</option>';

            // 3. Cerrar el modal
            const modalEl = document.getElementById('modalInventario');
            const modalInstance = bootstrap.Modal.getInstance(modalEl);
            if (modalInstance)
                modalInstance.hide();

            // 4. Refrescar la tabla de fondo
            window.fetchInventario();
        } else {
            const errorData = await response.json();
            Swal.fire('Error', errorData.error || 'Error en el servidor', 'error');
        }
    } catch (error) {
        console.error("Error crítico:", error);
        Swal.fire('Error de Conexión', 'Revisa la consola', 'error');
    }
};

// --- 5. CARGA DINÁMICA DE SELECTS ---
window.cargarEdificios = async () => {
    const selectEdi = document.getElementById("txtIdEdificio");
    try {
        const resp = await fetch("http://localhost:8080/IntegradoraII_Web/api/inventario/getEdificios");
        const edificios = await resp.json();
        selectEdi.innerHTML = '<option value="">Seleccione un edificio...</option>';
        edificios.forEach(edi => {
            const option = document.createElement("option");
            option.value = edi.id_edificio;
            option.textContent = edi.nombre;
            selectEdi.appendChild(option);
        });
    } catch (error) {
        console.error("Error edificios:", error);
    }
};

window.cargarSalones = async () => {
    const valEdificio = document.getElementById("txtIdEdificio").value;
    const selectSalon = document.getElementById("txtIdSalon");

    if (!valEdificio) {
        selectSalon.innerHTML = '<option value="">Seleccione un edificio</option>';
        return;
    }

    try {
        const resp = await fetch(`http://localhost:8080/IntegradoraII_Web/api/inventario/getSalones?idEdificio=${valEdificio}`);
        const salones = await resp.json();

        selectSalon.innerHTML = '<option value="">Seleccione un salón...</option>';

        salones.forEach(s => {
            const option = document.createElement("option");
            // CAMBIO AQUÍ: Usamos idSalon (como viene en tu JSON de Postman)
            option.value = s.idSalon;
            option.textContent = s.nombre;
            selectSalon.appendChild(option);
        });

        console.log("Salones cargados correctamente con idSalon");
    } catch (error) {
        console.error("Error al cargar salones:", error);
    }
};

// --- 6. FILTRADO ---
function actualizarFiltrosDesdeTabla() {
    const filas = document.querySelectorAll("#tablaInventario tbody tr");
    const materiales = new Set(), categorias = new Set(), rangos = new Set();

    filas.forEach(fila => {
        const celdas = fila.querySelectorAll("td");
        if (celdas.length < 6)
            return;
        materiales.add(celdas[0].textContent.trim());
        categorias.add(celdas[1].textContent.trim());
        const estado = parseFloat(celdas[5].textContent.replace("%", "").trim());
        if (!isNaN(estado)) {
            if (estado < 30)
                rangos.add("menor30");
            else if (estado < 60)
                rangos.add("entre3060");
            else
                rangos.add("entre60100");
        }
    });

    llenarSelect("filtroMaterial", materiales, "Todos los materiales");
    llenarSelect("filtroCategoria", categorias, "Todos");
    llenarSelectEstatus(rangos);
}

function llenarSelect(id, valores, textoDefault) {
    const select = document.getElementById(id);
    if (!select)
        return;
    select.innerHTML = `<option value="">${textoDefault}</option>`;
    valores.forEach(v => select.innerHTML += `<option value="${v}">${v}</option>`);
}

function llenarSelectEstatus(rangos) {
    const select = document.getElementById("filtroEstatus");
    if (!select)
        return;
    select.innerHTML = `<option value="">Todos</option>`;
    if (rangos.has("menor30"))
        select.innerHTML += `<option value="menor30">0 - 30%</option>`;
    if (rangos.has("entre3060"))
        select.innerHTML += `<option value="entre3060">30 - 60%</option>`;
    if (rangos.has("entre60100"))
        select.innerHTML += `<option value="entre60100">60 - 100%</option>`;
}

document.getElementById("btnFiltrar")?.addEventListener("click", () => {
    const mat = document.getElementById("filtroMaterial").value;
    const cat = document.getElementById("filtroCategoria").value;
    const est = document.getElementById("filtroEstatus").value;

    document.querySelectorAll("#tablaInventario tbody tr").forEach(fila => {
        const celdas = fila.querySelectorAll("td");
        const material = celdas[0].textContent.trim();
        const categoria = celdas[1].textContent.trim();
        const estado = parseFloat(celdas[5].textContent.replace("%", ""));

        let visible = true;
        if (mat && material !== mat)
            visible = false;
        if (cat && categoria !== cat)
            visible = false;
        if (est) {
            if (est === "menor30" && estado >= 30)
                visible = false;
            if (est === "entre3060" && (estado < 30 || estado >= 60))
                visible = false;
            if (est === "entre60100" && estado < 60)
                visible = false;
        }
        fila.style.display = visible ? "" : "none";
    });
});

// --- 7. DETALLES, EDICIÓN Y ELIMINACIÓN ---
window.verDetalleMaterial = async (nombreMaterial) => {
    try {
        const modalElement = document.getElementById('modalDetalleInventario');
        const modal = new bootstrap.Modal(modalElement);
        modal.show();

        const container = document.getElementById('modalDetalleBody');
        container.innerHTML = '<div class="text-center my-5"><div class="spinner-border text-primary"></div><p>Buscando...</p></div>';

        const response = await fetch(`http://localhost:8080/IntegradoraII_Web/api/inventario/getByName?nombre=${encodeURIComponent(nombreMaterial)}`);
        const data = await response.json();

        if (data && data.length > 0) {
            const info = data[0];
            let htmlUbicaciones = '';
            data.forEach(item => {
                let badgeColor = item.estatus?.trim().toLowerCase() === "disponible" ? "bg-success" : "bg-warning text-dark";
                htmlUbicaciones += `
                    <tr>
                        <td>${item.nombre}</td>
                        <td>${item.nombreEdificio}</td>
                        <td>${item.nombreSalon}</td>
                        <td>${item.ubicacion}</td>
                        <td><span class="badge ${badgeColor}">${item.estatus}</span></td>
                        <td class="text-center">
                            <button class="btn btn-sm btn-outline-primary" onclick="editarItem(${item.idInventario || item.id_inventario}, '${item.nombreEdificio}', '${item.nombreSalon}', '${item.ubicacion}', '${item.estatus}')">
                                <i class="bi bi-pencil"></i>
                            </button>
                            <button class="btn btn-sm btn-outline-danger" onclick="eliminarItem(${item.id_inventario})">
    <i class="bi bi-trash"></i>
</button>
                        </td>
                    </tr>`;
            });
            container.innerHTML = `<div class="card mb-3"><div class="card-body"><h5>${nombreMaterial}</h5></div></div><table class="table table-sm"><thead><tr><th>Nombre</th><th>Edificio</th><th>Salón</th><th>Ubicación</th><th>Estado</th><th>Acciones</th></tr></thead><tbody>${htmlUbicaciones}</tbody></table>`;
        }
    } catch (e) {
        console.error(e);
    }
};

// Esta es la función que deberías tener para abrir el modal
window.prepararEliminacion = (id) => {
    console.log("Cargando ID en el input oculto:", id);
    document.getElementById("editItemId").value = id; // AQUÍ se asigna
    // Después abres el modal
    const modal = new bootstrap.Modal(document.getElementById('modalDetalleInventario'));
    modal.show();
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
            const response = await fetch(`http://localhost:8080/IntegradoraII_Web/api/inventario/delete`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ id_inventario: id }) // Enviamos el ID que llegó por parámetro
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

window.editarItem = (id, edificio, salon, ubicacion, estatus) => {
    const modalDetalle = bootstrap.Modal.getInstance(document.getElementById('modalDetalleInventario'));
    if (modalDetalle)
        modalDetalle.hide();

    document.getElementById("editItemId").value = id;
    document.getElementById("txtSalon").value = salon;
    document.getElementById("txtEditUbicacion").value = ubicacion;
    document.getElementById("selEditEstatus").value = estatus;

    new bootstrap.Modal(document.getElementById('modalEditarItem')).show();
};

window.actualizarItem = async () => {
    const idVal = document.getElementById("editItemId").value;
    const datos = {id_inventario: parseInt(idVal), id_estatus_actual: document.getElementById("selEditEstatus").value};
    await fetch(`http://localhost:8080/IntegradoraII_Web/api/inventario/update`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(datos)
    });
    Swal.fire('¡Éxito!', '', 'success');
    bootstrap.Modal.getInstance(document.getElementById('modalEditarItem')).hide();
    window.fetchInventario();
};

window.cerrarSesion = () => {
    localStorage.clear();
    window.location.href = "../index.html";
};
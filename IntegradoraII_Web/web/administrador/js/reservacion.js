const API_URL = '/api';
const URL_RESERVACION = `${API_URL}/reservacion`;

let todasLasReservaciones = [];
let salonesBD = [];
let listaUsuariosBD = [];

document.addEventListener('DOMContentLoaded', () => {
    // HEADER - Información de sesión
    const nombre = localStorage.getItem("nombre_usuario") || "Usuario";
    const rolRaw = localStorage.getItem("id_rol") || "Invitado";
    if (document.getElementById('userNameDisplay')) document.getElementById('userNameDisplay').textContent = nombre;
    if (document.getElementById('userRoleDisplay')) document.getElementById('userRoleDisplay').textContent = rolRaw;

    inicializarModulo();
});

async function inicializarModulo() {
    await cargarListaUsuarios(); 
    await cargarCatalogos();
    await cargarReservaciones();
}

/**
 * Carga la lista de usuarios para el autocompletado de matrículas
 * Basado en tbl_personas y tbl_usuarios
 */
async function cargarListaUsuarios() {
    try {
        const response = await fetch(`${API_URL}/usuario/getAll`);
        if (!response.ok) throw new Error("Error en servidor");
        listaUsuariosBD = await response.json();
        
        const datalist = document.getElementById('listaMatriculas');
        if (datalist && Array.isArray(listaUsuariosBD)) {
            // Se usa apellido_paterno para coincidir con el Model de Java y la BD
            datalist.innerHTML = listaUsuariosBD.map(u => 
                `<option value="${u.matricula}">${u.nombre} ${u.apellido_paterno}</option>`
            ).join('');
        }
    } catch (e) {
        console.error("No se pudo cargar la lista de alumnos/usuarios.");
        listaUsuariosBD = []; 
    }
}

/**
 * Carga el catálogo de salones usando la vista v_salones_edificios
 */
async function cargarCatalogos() {
    try {
        const response = await fetch(`${URL_RESERVACION}/getSalones`);
        salonesBD = await response.json();
        
        const edificios = [...new Set(salonesBD.map(s => s.edificio))];
        const selectEdificio = document.getElementById('edificioSelect');
        const filtroEdificio = document.getElementById('filtroEdificio');

        const optionsHtml = edificios.map(e => `<option value="${e}">${e}</option>`).join('');
        
        if (selectEdificio) selectEdificio.innerHTML = '<option value="" disabled selected>Seleccione edificio</option>' + optionsHtml;
        if (filtroEdificio) filtroEdificio.innerHTML = '<option value="">Todos los edificios</option>' + optionsHtml;
    } catch (e) { console.error("Error al cargar catálogos"); }
}

window.cargarSalones = () => {
    const edificio = document.getElementById('edificioSelect').value;
    const selectSalon = document.getElementById('salonSelect');
    if (!selectSalon) return;

    selectSalon.innerHTML = '<option value="" disabled selected>Seleccione salón</option>';
    salonesBD.filter(s => s.edificio === edificio).forEach(s => {
        selectSalon.insertAdjacentHTML('beforeend', `<option value="${s.idSalon}">${s.nombre}</option>`);
    });
};

/**
 * Obtiene todas las reservaciones desde el REST
 */
async function cargarReservaciones() {
    const tbody = document.getElementById('tablaReservas');
    try {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center">Cargando reservaciones...</td></tr>';
        const response = await fetch(`${URL_RESERVACION}/getAll`);
        todasLasReservaciones = await response.json();
        
        if (todasLasReservaciones.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" class="text-center">No hay reservaciones disponibles</td></tr>';
            return;
        }
        renderTabla(todasLasReservaciones);
    } catch (e) {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center text-danger">Error al conectar con el servidor</td></tr>';
    }
}

/**
 * Pinta la tabla y actualiza las tarjetas de conteo
 */
function renderTabla(datos) {
    const tbody = document.getElementById('tablaReservas');
    if (!tbody) return;
    tbody.innerHTML = '';
    
    let activos = 0, pendientes = 0, rechazados = 0;

    datos.forEach(res => {
        if (res.estatus === 'Activo') activos++;
        else if (res.estatus === 'Pendiente') pendientes++;
        else if (res.estatus === 'Rechazado') rechazados++;

        let badgeClass = 'bg-success';
        if (res.estatus === 'Pendiente') {
            badgeClass = 'bg-warning text-dark';
        } else if (res.estatus === 'Rechazado') {
            badgeClass = 'bg-danger';
        }

        const fila = document.createElement('tr');
        fila.innerHTML = `
            <td class="fw-medium">${res.matricula}</td>
            <td>${res.nombreEdificio}</td>
            <td>${res.nombreSalon}</td>
            <td>${res.fechaHora ? res.fechaHora.replace('T', ' ') : ''}</td>
            <td><span class="badge ${badgeClass}">${res.estatus}</span></td>
            <td>
                <button class="btn btn-sm btn-outline-primary me-2" title="Editar" onclick="abrirModalEditar(${res.idReserva})">
                    <i class="bi bi-pencil-square"></i>
                </button>
                <button class="btn btn-sm btn-outline-danger" title="Eliminar" onclick="eliminarRes(${res.idReserva})">
                    <i class="bi bi-trash3"></i>
                </button>
            </td>
        `;
        tbody.appendChild(fila);
    });

    if (document.getElementById('cardActivas')) document.getElementById('cardActivas').textContent = activos;
    if (document.getElementById('cardPendientes')) document.getElementById('cardPendientes').textContent = pendientes;
    if (document.getElementById('cardRechazadas')) document.getElementById('cardRechazadas').textContent = rechazados;
    if (document.getElementById('cardTotal')) document.getElementById('cardTotal').textContent = datos.length;
}

/**
 * Guarda o Actualiza una reservación enviando datos como URLSearchParams
 */
window.guardarReservacion = async () => {
    const matriculaInput = document.getElementById('matricula').value.trim();
    const alumno = listaUsuariosBD.find(u => u.matricula === matriculaInput);
    
    if (!alumno) {
        Swal.fire({
            icon: 'warning',
            title: 'Matrícula inválida',
            text: 'Por favor, ingrese una matrícula registrada en el sistema.'
        });
        return;
    }

    const id = document.getElementById('reservaId').value;
    const endpoint = id ? `${URL_RESERVACION}/actualizar` : `${URL_RESERVACION}/insertar`;

    // Objeto alineado al Model Reservacion.java
    const reservacionObj = {
        idReserva: id ? parseInt(id) : 0,
        idUsuario: alumno.id_usuario, 
        idSalon: parseInt(document.getElementById('salonSelect').value),
        fechaHora: document.getElementById('fecha').value.replace('T', ' ') + ':00',
        estatus: document.getElementById('estadoSelect').value
    };

    const formData = new URLSearchParams();
    formData.append("reservacion", JSON.stringify(reservacionObj));

    try {
        const response = await fetch(endpoint, { 
            method: 'POST', 
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: formData 
        });
        
        const data = await response.json().catch(() => ({ error: "Error en la respuesta del servidor" }));
        
        if (data.result === 'OK' || !data.error) {
            Swal.fire({ icon: 'success', title: 'Éxito', text: 'Operación realizada correctamente', timer: 1500, showConfirmButton: false });
            bootstrap.Modal.getInstance(document.getElementById('modalReservacion')).hide();
            await cargarReservaciones();
        } else {
            Swal.fire('Error', data.error || 'No se pudo procesar la solicitud', 'error');
        }
    } catch (e) { 
        Swal.fire('Error', 'No se pudo conectar con el servidor de Railway', 'error');
    }
};

window.abrirModalAgregar = () => {
    document.getElementById('formReserva').reset();
    document.getElementById('reservaId').value = '';
    document.getElementById('modalReservaTitulo').innerHTML = '<i class="bi bi-plus-circle"></i> Agregar Reservación';
    new bootstrap.Modal(document.getElementById('modalReservacion')).show();
};

window.abrirModalEditar = (id) => {
    const res = todasLasReservaciones.find(r => r.idReserva === id);
    if (res) {
        document.getElementById('reservaId').value = res.idReserva;
        document.getElementById('matricula').value = res.matricula || '';
        document.getElementById('edificioSelect').value = res.nombreEdificio;
        
        // Carga síncrona de salones del edificio
        window.cargarSalones();
        
        // Timeout breve para asegurar que el select de salones se llenó antes de asignar el valor
        setTimeout(() => { 
            document.getElementById('salonSelect').value = res.idSalon; 
        }, 150);

        if (res.fechaHora) {
            document.getElementById('fecha').value = res.fechaHora.replace(' ', 'T').substring(0, 16);
        }
        document.getElementById('estadoSelect').value = res.estatus;
        
        document.getElementById('modalReservaTitulo').innerHTML = '<div class="text-center"><i class="bi bi-pencil-square"></i> Editar Reservación</div>';
        new bootstrap.Modal(document.getElementById('modalReservacion')).show();
    }
};

/**
 * Eliminación lógica
 */
window.eliminarRes = async (id) => {
    const result = await Swal.fire({
        title: '¿Está seguro?',
        text: 'La reservación se marcará como eliminada.',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#2a2155',
        cancelButtonColor: '#6c757d',
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar'
    });
    
    if (result.isConfirmed) {
        try {
            const f = new URLSearchParams(); 
            f.append("idReserva", id);
            
            const response = await fetch(`${URL_RESERVACION}/eliminar`, { 
                method: 'POST', 
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: f 
            });
            
            if (response.ok) {
                Swal.fire({ icon: 'success', title: 'Eliminado', text: 'Reservación eliminada correctamente', timer: 1500, showConfirmButton: false });
                await cargarReservaciones();
            }
        } catch(e) {
            Swal.fire('Error', 'Error al procesar la eliminación', 'error');
        }
    }
};

window.aplicarFiltros = () => {
    const fEdificio = document.getElementById('filtroEdificio').value;
    const fEstatus = document.getElementById('filtroEstatus').value;
    
    const filtrados = todasLasReservaciones.filter(r => 
        (fEdificio === "" || r.nombreEdificio === fEdificio) && 
        (fEstatus === "" || r.estatus === fEstatus)
    );
    renderTabla(filtrados);
};

window.cerrarSesion = () => { 
    localStorage.clear(); 
    window.location.href = window.location.origin + "/index.html";
};
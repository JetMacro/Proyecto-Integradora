const API_URL = 'https://proyecto-integradora-production.up.railway.app/api';

let todosLosUsuarios = [];

async function cargarUsuarios() {
    try {
        const tbody = document.querySelector('tbody');
        tbody.innerHTML = '<tr><td colspan="6" class="text-center">Cargando usuarios...</td></tr>';
        
        const response = await fetch(`${API_URL}/usuario/getAll`);
        
        if (!response.ok) {
            throw new Error('Error al cargar los datos');
        }
        
        todosLosUsuarios = await response.json();
        tbody.innerHTML = '';
        
        if (todosLosUsuarios.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" class="text-center">No hay usuarios registrados</td></tr>';
            actualizarResumen(0, 0, 0, 0);
            return;
        }
        
        let activos = 0, inactivos = 0, eliminados = 0;
        
        todosLosUsuarios.forEach(u => {
            if (u.id_estatus === 'Activo') activos++;
            else if (u.id_estatus === 'Inactivo') inactivos++;
            else if (u.id_estatus === 'Eliminado') eliminados++;
            
            let badgeClass = 'bg-success';
            if (u.id_estatus === 'Inactivo') badgeClass = 'bg-secondary';
            else if (u.id_estatus === 'Eliminado') badgeClass = 'bg-danger';
            
            const nombreCompleto = `${u.nombre} ${u.apellido_paterno} ${u.apellido_materno}`;
            
            const fila = document.createElement('tr');
            fila.innerHTML = `
                <td class="fw-bold text-secondary">${u.matricula}</td>
                <td class="fw-medium">${nombreCompleto}</td>
                <td>${u.id_rol}</td>
                <td>${u.id_turno}</td>
                <td><span class="badge ${badgeClass}">${u.id_estatus}</span></td>
                <td>
                    <div class="d-flex justify-content-center flex-nowrap gap-1">
                        <button class="btn btn-sm btn-outline-primary" title="Editar" onclick="abrirModalEditar(${u.id_usuario})">
                            <i class="bi bi-pencil-square"></i>
                        </button>
                        <button class="btn btn-sm btn-outline-danger" title="Eliminar" onclick="eliminarUsuario(${u.id_usuario})">
                            <i class="bi bi-trash3"></i>
                        </button>
                        <button class="btn btn-sm btn-outline-success" title="Detalles" onclick="verDetalleUsuario(${u.id_usuario})">
                            <i class="bi bi-eye"></i>
                        </button>
                    </div>
                </td>
            `;
            tbody.appendChild(fila);
        });
        
        actualizarResumen(activos, inactivos, eliminados, todosLosUsuarios.length);
        
    } catch (error) {
        console.error('Error:', error);
        document.querySelector('tbody').innerHTML = '<tr><td colspan="6" class="text-center text-danger">Error al cargar los datos</td></tr>';
        Swal.fire({ icon: 'error', title: 'Error', text: 'No se pudieron cargar los usuarios' });
    }
}

function actualizarResumen(activos, inactivos, eliminados, total) {
    const tarjetas = document.querySelectorAll('.col-sm-6.col-lg-3 .fw-bold span');
    if (tarjetas.length >= 4) {
        tarjetas[0].textContent = activos;
        tarjetas[1].textContent = inactivos;
        tarjetas[2].textContent = eliminados;
        tarjetas[3].textContent = total;
    }
}

function aplicarFiltros() {
    const filtroRol = document.getElementById('filtroRol').value;
    const filtroTurno = document.getElementById('filtroTurno').value;
    const filtroEstatus = document.getElementById('filtroEstatus').value;
    
    const tbody = document.querySelector('tbody');
    tbody.innerHTML = '';
    
    let usuariosFiltrados = todosLosUsuarios.filter(u => {
        let cumple = true;
        if (filtroRol && u.id_rol !== filtroRol) cumple = false;
        if (filtroTurno && u.id_turno !== filtroTurno) cumple = false;
        if (filtroEstatus && u.id_estatus !== filtroEstatus) cumple = false;
        return cumple;
    });
    
    if (usuariosFiltrados.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center">No hay usuarios que coincidan con los filtros</td></tr>';
        actualizarResumen(0, 0, 0, 0);
        return;
    }
    
    let activos = 0, inactivos = 0, eliminados = 0;
    
    usuariosFiltrados.forEach(u => {
        if (u.id_estatus === 'Activo') activos++;
        else if (u.id_estatus === 'Inactivo') inactivos++;
        else if (u.id_estatus === 'Eliminado') eliminados++;
        
        let badgeClass = 'bg-success';
        if (u.id_estatus === 'Inactivo') badgeClass = 'bg-secondary';
        else if (u.id_estatus === 'Eliminado') badgeClass = 'bg-danger';
        
        const fila = document.createElement('tr');
        fila.innerHTML = `
            <td class="fw-bold text-secondary">${u.matricula}</td>
            <td class="fw-medium">${u.nombre} ${u.apellido_paterno} ${u.apellido_materno}</td>
            <td>${u.id_rol}</td>
            <td>${u.id_turno}</td>
            <td><span class="badge ${badgeClass}">${u.id_estatus}</span></td>
            <td>
                <div class="d-flex justify-content-center flex-nowrap gap-1">
                    <button class="btn btn-sm btn-outline-primary" title="Editar" onclick="abrirModalEditar(${u.id_usuario})">
                        <i class="bi bi-pencil-square"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-danger" title="Eliminar" onclick="eliminarUsuario(${u.id_usuario})">
                        <i class="bi bi-trash3"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-success" title="Detalles" onclick="verDetalleUsuario(${u.id_usuario})">
                        <i class="bi bi-eye"></i>
                    </button>
                </div>
            </td>
        `;
        tbody.appendChild(fila);
    });
    
    actualizarResumen(activos, inactivos, eliminados, usuariosFiltrados.length);
}

// ------------------- MODALES -------------------

window.abrirModalAgregar = () => {
    document.getElementById('formUsuario').reset();
    document.getElementById('usuarioId').value = '';
    
    // Mostramos el campo de contraseña porque es nuevo
    document.getElementById('divContrasenia').style.display = 'block';
    document.getElementById('uEstatus').value = 'Activo';
    
    document.getElementById('modalUsuarioTitulo').innerHTML = '<i class="bi bi-person-plus-fill"></i> Agregar Usuario';
    const modal = new bootstrap.Modal(document.getElementById('modalUsuario'));
    modal.show();
};

window.abrirModalEditar = (id) => {
    const u = todosLosUsuarios.find(user => user.id_usuario === id);
    if (u) {
        document.getElementById('usuarioId').value = u.id_usuario;
        document.getElementById('uNombre').value = u.nombre;
        document.getElementById('uApPaterno').value = u.apellido_paterno;
        document.getElementById('uApMaterno').value = u.apellido_materno;
        document.getElementById('uCorreo').value = u.correo;
        document.getElementById('uTelefono').value = u.telefono;
        document.getElementById('uFechaNac').value = u.fecha_nacimiento;
        document.getElementById('uDireccion').value = u.direccion;
        document.getElementById('uMatricula').value = u.matricula;
        document.getElementById('uRol').value = u.id_rol;
        document.getElementById('uTurno').value = u.id_turno;
        document.getElementById('uEstatus').value = u.id_estatus;
        
        // Ocultamos la contraseña porque no se actualiza por aquí
        document.getElementById('divContrasenia').style.display = 'none';
        document.getElementById('uContrasenia').value = ''; 
        
        document.getElementById('modalUsuarioTitulo').innerHTML = '<i class="bi bi-pencil-square"></i> Editar Usuario';
        const modal = new bootstrap.Modal(document.getElementById('modalUsuario'));
        modal.show();
    }
};

window.verDetalleUsuario = (id) => {
    const u = todosLosUsuarios.find(user => user.id_usuario === id);
    if (u) {
        let badgeClass = 'bg-success';
        if (u.id_estatus === 'Inactivo') badgeClass = 'bg-secondary';
        else if (u.id_estatus === 'Eliminado') badgeClass = 'bg-danger';

        const html = `
            <div class="card mb-3 border-0 shadow-sm">
                <div class="card-body">
                    <h6 class="text-primary fw-bold border-bottom pb-2"><i class="bi bi-person-vcard"></i> Datos de la Cuenta</h6>
                    <p class="mb-1"><strong>Matrícula:</strong> ${u.matricula}</p>
                    <p class="mb-1"><strong>Rol:</strong> ${u.id_rol}</p>
                    <p class="mb-1"><strong>Turno:</strong> ${u.id_turno}</p>
                    <p class="mb-1"><strong>Estatus:</strong> <span class="badge ${badgeClass}">${u.id_estatus}</span></p>
                </div>
            </div>
            <div class="card border-0 shadow-sm">
                <div class="card-body">
                    <h6 class="text-primary fw-bold border-bottom pb-2"><i class="bi bi-card-text"></i> Información Personal</h6>
                    <p class="mb-1"><strong>Nombre Completo:</strong> ${u.nombre} ${u.apellido_paterno} ${u.apellido_materno}</p>
                    <p class="mb-1"><strong>Correo:</strong> ${u.correo}</p>
                    <p class="mb-1"><strong>Teléfono:</strong> ${u.telefono || 'N/A'}</p>
                    <p class="mb-1"><strong>Nacimiento:</strong> ${u.fecha_nacimiento}</p>
                    <p class="mb-1"><strong>Dirección:</strong> ${u.direccion || 'N/A'}</p>
                </div>
            </div>
        `;
        document.getElementById('modalDetalleBody').innerHTML = html;
        const modal = new bootstrap.Modal(document.getElementById('modalDetalleUsuario'));
        modal.show();
    }
};

// ------------------- ACCIONES A BD -------------------

window.guardarUsuario = async () => {
    try {
        const id = document.getElementById('usuarioId').value;
        const endpoint = id ? `${API_URL}/usuario/modificar` : `${API_URL}/usuario/insertar`;
        
        // Validación básica
        if (!document.getElementById('uNombre').value || !document.getElementById('uApPaterno').value ||
            !document.getElementById('uCorreo').value || !document.getElementById('uMatricula').value || 
            (!id && !document.getElementById('uContrasenia').value)) { // Pide contraseña solo si es nuevo
            
            Swal.fire({ icon: 'warning', title: 'Campos incompletos', text: 'Llene todos los campos con asterisco (*)' });
            return;
        }
        
        const userData = {
            id_usuario: id ? parseInt(id) : 0,
            nombre: document.getElementById('uNombre').value,
            apellido_paterno: document.getElementById('uApPaterno').value,
            apellido_materno: document.getElementById('uApMaterno').value,
            correo: document.getElementById('uCorreo').value,
            telefono: document.getElementById('uTelefono').value,
            fecha_nacimiento: document.getElementById('uFechaNac').value,
            direccion: document.getElementById('uDireccion').value,
            matricula: document.getElementById('uMatricula').value,
            contrasenia: document.getElementById('uContrasenia').value, // El backend debe hashearla
            id_rol: document.getElementById('uRol').value,
            id_turno: document.getElementById('uTurno').value,
            id_estatus: document.getElementById('uEstatus').value
        };
        
        const response = await fetch(endpoint, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(userData)
        });
        
        const data = await response.json();
        
        if (response.ok) {
            Swal.fire({ icon: 'success', title: 'Éxito', text: data.mensaje || 'Operación exitosa', timer: 1500, showConfirmButton: false });
            bootstrap.Modal.getInstance(document.getElementById('modalUsuario')).hide();
            await cargarUsuarios();
        } else {
            Swal.fire({ icon: 'error', title: 'Error', text: data.error || 'No se pudo guardar' });
        }
        
    } catch (error) {
        Swal.fire({ icon: 'error', title: 'Error', text: 'Error de red al guardar' });
    }
};

window.eliminarUsuario = async (id) => {
    const result = await Swal.fire({
        title: '¿Dar de baja usuario?',
        text: 'El usuario pasará a estatus "Eliminado"',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#6c757d',
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar'
    });
    
    if (result.isConfirmed) {
        try {
            // Como tu Java recibe @FormParam, enviamos URLSearchParams
            const params = new URLSearchParams();
            params.append('id_usuario', id);

            const response = await fetch(`${API_URL}/usuario/eliminar`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: params
            });
            
            const data = await response.json();
            
            if (response.ok) {
                Swal.fire({ icon: 'success', title: 'Eliminado', text: 'El usuario fue dado de baja', timer: 1500, showConfirmButton: false });
                await cargarUsuarios();
            } else {
                Swal.fire({ icon: 'error', title: 'Error', text: data.error || 'No se pudo eliminar' });
            }
            
        } catch (error) {
            Swal.fire({ icon: 'error', title: 'Error', text: 'Error de red al eliminar' });
        }
    }
};

// Listeners al cargar
document.addEventListener('DOMContentLoaded', () => {
    cargarUsuarios();
    document.getElementById('btnFiltrar').addEventListener('click', aplicarFiltros);
    const nombre = localStorage.getItem("nombre_usuario") || "Usuario";
    const rolRaw = localStorage.getItem("id_rol") || "Invitado";
    
    if (document.getElementById('userNameDisplay')) document.getElementById('userNameDisplay').textContent = nombre;
    if (document.getElementById('userRoleDisplay')) document.getElementById('userRoleDisplay').textContent = rolRaw;
});

window.cerrarSesion = () => {
    localStorage.clear();
    window.location.href = "../index.html";
};
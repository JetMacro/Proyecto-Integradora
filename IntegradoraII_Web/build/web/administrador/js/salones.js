// salones.js

const API_URL = 'http://localhost:8080/IntegradoraII_Web/api';

let todosLosSalones = [];

async function cargarSalones() {
    try {
        const tbody = document.querySelector('tbody');
        tbody.innerHTML = '<tr><td colspan="6" class="text-center">Cargando salones...</td></tr>';
        
        const response = await fetch(`${API_URL}/salones/getAll`);
        
        if (!response.ok) {
            throw new Error('Error al cargar los datos');
        }
        
        todosLosSalones = await response.json();
        
        tbody.innerHTML = '';
        
        if (todosLosSalones.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" class="text-center">No hay salones disponibles</td></tr>';
            return;
        }
        
        let activos = 0;
        let inactivos = 0;
        let eliminados = 0;
        
        todosLosSalones.forEach(salon => {
            if (salon.estatus === 'Activo') activos++;
            else if (salon.estatus === 'Inactivo') inactivos++;
            else if (salon.estatus === 'Eliminado') eliminados++;
            
            let badgeClass = 'bg-success';
            if (salon.estatus === 'Inactivo') {
                badgeClass = 'bg-secondary';
            } else if (salon.estatus === 'Eliminado') {
                badgeClass = 'bg-danger';
            }
            
            const fila = document.createElement('tr');
            
            fila.innerHTML = `
                <td class="fw-medium">${salon.nombre}</td>
                <td>${salon.tipo === 'Salon' ? 'Salón' : salon.tipo}</td>
                <td>${salon.capacidad}</td>
                <td>${salon.edificio}</td>
                <td><span class="badge ${badgeClass}">${salon.estatus}</span></td>
                <td>
                    <button class="btn btn-sm btn-outline-primary me-2" title="Editar" onclick="abrirModalEditar(${salon.idSalon})">
                        <i class="bi bi-pencil-square"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-danger me-2" title="Eliminar" onclick="eliminarSalon(${salon.idSalon})">
                        <i class="bi bi-trash3"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-success" title="Detalles" onclick="verDetalleSalon(${salon.idSalon})">
                        <i class="bi bi-eye"></i>
                    </button>
                </td>
            `;
            
            tbody.appendChild(fila);
        });
        
        actualizarResumen(activos, inactivos, eliminados, todosLosSalones.length);
        
        document.getElementById('filtroEdificio').value = '';
        document.getElementById('filtroTipo').value = '';
        document.getElementById('filtroEstatus').value = '';
        
    } catch (error) {
        console.error('Error:', error);
        document.querySelector('tbody').innerHTML = 
            '<tr><td colspan="6" class="text-center text-danger">Error al cargar los datos</td></tr>';
        
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'No se pudieron cargar los salones'
        });
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
    const filtroEdificio = document.getElementById('filtroEdificio').value;
    const filtroTipo = document.getElementById('filtroTipo').value;
    const filtroEstatus = document.getElementById('filtroEstatus').value;
    
    const tbody = document.querySelector('tbody');
    tbody.innerHTML = '';
    
    let salonesFiltrados = todosLosSalones.filter(salon => {
        let cumple = true;
        
        if (filtroEdificio && salon.edificio !== filtroEdificio) cumple = false;
        if (filtroTipo) {
            const tipoComparar = salon.tipo === 'Salon' ? 'Salón' : salon.tipo;
            if (tipoComparar !== filtroTipo) cumple = false;
        }
        if (filtroEstatus && salon.estatus !== filtroEstatus) cumple = false;
        
        return cumple;
    });
    
    if (salonesFiltrados.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center">No hay salones que coincidan con los filtros</td></tr>';
        actualizarResumen(0, 0, 0, 0);
        return;
    }
    
    let activos = 0;
    let inactivos = 0;
    let eliminados = 0;
    
    salonesFiltrados.forEach(salon => {
        if (salon.estatus === 'Activo') activos++;
        else if (salon.estatus === 'Inactivo') inactivos++;
        else if (salon.estatus === 'Eliminado') eliminados++;
        
        let badgeClass = 'bg-success';
        if (salon.estatus === 'Inactivo') {
            badgeClass = 'bg-secondary';
        } else if (salon.estatus === 'Eliminado') {
            badgeClass = 'bg-danger';
        }
        
        const fila = document.createElement('tr');
        
        fila.innerHTML = `
            <td class="fw-medium">${salon.nombre}</td>
            <td>${salon.tipo === 'Salon' ? 'Salón' : salon.tipo}</td>
            <td>${salon.capacidad}</td>
            <td>${salon.edificio}</td>
            <td><span class="badge ${badgeClass}">${salon.estatus}</span></td>
            <td>
                <button class="btn btn-sm btn-outline-primary me-2" title="Editar" onclick="abrirModalEditar(${salon.idSalon})">
                    <i class="bi bi-pencil-square"></i>
                </button>
                <button class="btn btn-sm btn-outline-danger me-2" title="Eliminar" onclick="eliminarSalon(${salon.idSalon})">
                    <i class="bi bi-trash3"></i>
                </button>
                <button class="btn btn-sm btn-outline-success" title="Detalles" onclick="verDetalleSalon(${salon.idSalon})">
                    <i class="bi bi-eye"></i>
                </button>
            </td>
        `;
        
        tbody.appendChild(fila);
    });
    
    actualizarResumen(activos, inactivos, eliminados, salonesFiltrados.length);
}

function resetearVista() {
    document.getElementById('filtroEdificio').value = '';
    document.getElementById('filtroTipo').value = '';
    document.getElementById('filtroEstatus').value = '';
    cargarSalones();
}

window.abrirModalAgregar = () => {
    document.getElementById('salonId').value = '';
    document.getElementById('salonIdEdificio').value = '';
    document.getElementById('salonTipo').value = '';
    document.getElementById('salonNombre').value = '';
    document.getElementById('salonCapacidad').value = '';
    document.getElementById('salonEstatus').value = 'Activo';
    
    document.getElementById('modalSalonTitulo').innerHTML = '<i class="bi bi-plus-circle"></i> Agregar Salón';
    
    const modal = new bootstrap.Modal(document.getElementById('modalSalon'));
    modal.show();
};

window.abrirModalEditar = (id) => {
    const salon = todosLosSalones.find(s => s.idSalon === id);
    
    if (salon) {
        document.getElementById('salonId').value = salon.idSalon;
        document.getElementById('salonIdEdificio').value = salon.idEdificio;
        document.getElementById('salonTipo').value = salon.tipo;
        document.getElementById('salonNombre').value = salon.nombre;
        document.getElementById('salonCapacidad').value = salon.capacidad;
        document.getElementById('salonEstatus').value = salon.estatus;
        
        document.getElementById('modalSalonTitulo').innerHTML = '<div class="text-center"><i class="bi bi-pencil-square"></i> Editar Salón</div>';
        
        const modal = new bootstrap.Modal(document.getElementById('modalSalon'));
        modal.show();
    } else {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'No se encontró el salón'
        });
    }
};

window.guardarSalon = async () => {
    try {
        const id = document.getElementById('salonId').value;
        const endpoint = id ? `${API_URL}/salones/actualizar` : `${API_URL}/salones/insertar`;
        
        if (!document.getElementById('salonIdEdificio').value || 
            !document.getElementById('salonTipo').value || 
            !document.getElementById('salonNombre').value || 
            !document.getElementById('salonCapacidad').value) {
            
            Swal.fire({
                icon: 'warning',
                title: 'Campos incompletos',
                text: 'Por favor, complete todos los campos requeridos'
            });
            return;
        }
        
        const salonData = {
            idSalon: id ? parseInt(id) : 0,
            idEdificio: parseInt(document.getElementById('salonIdEdificio').value),
            tipo: document.getElementById('salonTipo').value,
            nombre: document.getElementById('salonNombre').value,
            capacidad: parseInt(document.getElementById('salonCapacidad').value),
            estatus: document.getElementById('salonEstatus').value
        };
        
        const response = await fetch(endpoint, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(salonData)
        });
        
        const data = await response.json();
        
        if (data.mensaje === 'Salón agregado correctamente' || 
            data.mensaje === 'Salón actualizado correctamente') {
            
            Swal.fire({
                icon: 'success',
                title: 'Éxito',
                text: data.mensaje,
                timer: 1500,
                showConfirmButton: false
            });
            
            const modal = bootstrap.Modal.getInstance(document.getElementById('modalSalon'));
            modal.hide();
            
            await cargarSalones();
        } else {
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: data.mensaje
            });
        }
        
    } catch (error) {
        console.error('Error:', error);
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Error al guardar: ' + error.message
        });
    }
};

window.eliminarSalon = async (id) => {
    const result = await Swal.fire({
        title: '¿Está seguro?',
        text: 'Esta acción no se puede deshacer',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#2a2155',
        cancelButtonColor: '#6c757d',
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar'
    });
    
    if (result.isConfirmed) {
        try {
            const response = await fetch(`${API_URL}/salones/eliminar`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ idSalon: id })
            });
            
            const data = await response.json();
            
            if (data.mensaje === 'Salón eliminado correctamente') {
                Swal.fire({
                    icon: 'success',
                    title: 'Eliminado',
                    text: data.mensaje,
                    timer: 1500,
                    showConfirmButton: false
                });
                await cargarSalones();
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: data.mensaje
                });
            }
            
        } catch (error) {
            console.error('Error:', error);
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Error al eliminar: ' + error.message
            });
        }
    }
};

window.verDetalleSalon = async (idSalon) => {
    try {
        const modal = new bootstrap.Modal(document.getElementById('modalDetalleSalon'));
        modal.show();
        
        const response = await fetch(`${API_URL}/salones/detalle/${idSalon}`);
        const data = await response.json();
        
        let html = '';
        
        if (data.length > 0) {
            const salon = data[0];
            
            html = `
                <div class="row">
                    <div class="col-md-6">
                        <div class="card mb-3">
                            <div class="card-header fw-bold" style="background-color: #2a2155; color: white;">
                                <i class="bi bi-building"></i> Información General
                            </div>
                            <div class="card-body">
                                <p><strong>Nombre:</strong> ${salon.nombre}</p>
                                <p><strong>Tipo:</strong> ${salon.tipo}</p>
                                <p><strong>Capacidad:</strong> ${salon.capacidad} personas</p>
                                <p><strong>Estatus:</strong> <span class="badge ${salon.estatus === 'Activo' ? 'bg-success' : salon.estatus === 'Inactivo' ? 'bg-secondary' : 'bg-danger'}">${salon.estatus}</span></p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="card mb-3">
                            <div class="card-header fw-bold" style="background-color: #2a2155; color: white;">
                                <i class="bi bi-geo-alt"></i> Ubicación
                            </div>
                            <div class="card-body">
                                <p><strong>Edificio:</strong> ${salon.edificio}</p>
                                <p><strong>Ubicación:</strong> ${salon.ubicacionEdificio || 'N/A'}</p>
                                <p><strong>Pisos:</strong> ${salon.pisosEdificio || 'N/A'}</p>
                                <p><strong>División:</strong> ${salon.divisionAcademica || 'N/A'}</p>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="card">
                    <div class="card-header fw-bold" style="background-color: #2a2155; color: white;">
                        <i class="bi bi-box"></i> Mobiliario del Salón
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-sm table-hover">
                                <thead class="table-light">
                                    <tr>
                                        <th>Tipo</th>
                                        <th class="text-center">Total</th>
                                        <th class="text-center">Disponibles</th>
                                        <th class="text-center">En Uso</th>
                                    </tr>
                                </thead>
                                <tbody>
            `;
            
            data.forEach(item => {
                if (item.tipoMobiliario) {
                    html += `
                        <tr>
<td><i class="bi bi-box"></i> ${item.tipoMobiliario}</td>
                            <td class="text-center">${item.totalMobiliario}</td>
                            <td class="text-center"><span class="badge bg-success">${item.disponibles}</span></td>
                            <td class="text-center"><span class="badge bg-warning">${item.enUso}</span></td>
                        </tr>
                    `;
                }
            });
            
            const totalGeneral = data.reduce((sum, item) => sum + (item.totalMobiliario || 0), 0);
            const totalDisponible = data.reduce((sum, item) => sum + (item.disponibles || 0), 0);
            const totalEnUso = data.reduce((sum, item) => sum + (item.enUso || 0), 0);
            
            html += `
                                </tbody>
                                <tfoot class="table-light">
                                    <tr>
                                        <th>TOTALES</th>
                                        <th class="text-center">${totalGeneral}</th>
                                        <th class="text-center">${totalDisponible}</th>
                                        <th class="text-center">${totalEnUso}</th>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>
                </div>
            `;
        } else {
            html = '<div class="alert alert-warning">No se encontraron detalles para este salón</div>';
        }
        
        document.getElementById('modalDetalleBody').innerHTML = html;
        
    } catch (error) {
        console.error('Error:', error);
        document.getElementById('modalDetalleBody').innerHTML = `
            <div class="alert alert-danger">
                Error al cargar los detalles: ${error.message}
            </div>
        `;
    }
};

document.addEventListener('DOMContentLoaded', () => {
    cargarSalones();
    
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
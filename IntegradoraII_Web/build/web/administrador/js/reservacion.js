document.addEventListener('DOMContentLoaded', () => {
    const URL_BASE = "https://proyecto-integradora-production.up.railway.app/api/reservacion";

    let reservaciones = [];
    let salonesBD = [];

    const tablaBody = document.getElementById('tablaReservas');
    const form = document.getElementById('formReserva');
    const modalElement = document.getElementById('reservaModal');
    const modal = new bootstrap.Modal(modalElement);
    const selectEdificio = document.getElementById('edificioSelect');
    const selectSalon = document.getElementById('salonSelect');

    function mostrarAlerta(mensaje, esError = false) {
        const modalId = 'customAlertModal';
        let existingModal = document.getElementById(modalId);
        if (existingModal) existingModal.remove();

        const headerColor = esError ? 'background-color: #dc3545; color: white;' : 'background-color: #2a2155; color: white;';
        const titulo = esError ? 'Error' : 'Aviso';

        const html = `
            <div class="modal fade" id="${modalId}" tabindex="-1" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered modal-sm">
                    <div class="modal-content border-0 shadow" style="border-radius: 12px; overflow: hidden;">
                        <div class="modal-header border-0" style="${headerColor}">
                            <h6 class="modal-title m-0 fw-bold">${titulo}</h6>
                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body text-center p-4">
                            <p class="mb-0 fs-6 text-dark" style="font-weight: 500;">${mensaje}</p>
                        </div>
                        <div class="modal-footer border-0 justify-content-center pb-4">
                            <button type="button" class="btn text-white px-4" style="background-color: #2a2155; border-radius: 8px;" data-bs-dismiss="modal">Aceptar</button>
                        </div>
                    </div>
                </div>
            </div>
        `;
        document.body.insertAdjacentHTML('beforeend', html);
        new bootstrap.Modal(document.getElementById(modalId)).show();
        
        const nombre = localStorage.getItem("nombre_usuario") || "Usuario";
    const rolRaw = localStorage.getItem("id_rol") || "Invitado";
    
    if (document.getElementById('userNameDisplay')) document.getElementById('userNameDisplay').textContent = nombre;
    if (document.getElementById('userRoleDisplay')) document.getElementById('userRoleDisplay').textContent = rolRaw;
    }

    function mostrarConfirmacion(mensaje, callback) {
        const modalId = 'customConfirmModal';
        let existingModal = document.getElementById(modalId);
        if (existingModal) existingModal.remove();

        const html = `
            <div class="modal fade" id="${modalId}" tabindex="-1" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered modal-sm">
                    <div class="modal-content border-0 shadow" style="border-radius: 12px; overflow: hidden;">
                        <div class="modal-header border-0 bg-danger text-white">
                            <h6 class="modal-title m-0 fw-bold">Confirmación</h6>
                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body text-center p-4">
                            <p class="mb-0 fs-6 text-dark" style="font-weight: 500;">${mensaje}</p>
                        </div>
                        <div class="modal-footer border-0 justify-content-center gap-2 pb-4">
                            <button type="button" class="btn btn-light px-4" style="border-radius: 8px;" data-bs-dismiss="modal">Cancelar</button>
                            <button type="button" class="btn btn-danger px-4" style="border-radius: 8px;" id="btnConfirmarAccion">Eliminar</button>
                        </div>
                    </div>
                </div>
            </div>
        `;
        document.body.insertAdjacentHTML('beforeend', html);
        const confirmModalEl = document.getElementById(modalId);
        const confirmModal = new bootstrap.Modal(confirmModalEl);
        
        document.getElementById('btnConfirmarAccion').addEventListener('click', () => {
            confirmModal.hide();
            callback();
        });
        
        confirmModal.show();
    }

    function cargarEdificiosYSalones() {
        fetch(`${URL_BASE}/getSalones`)
            .then(r => r.json())
            .then(data => {
                salonesBD = data;
                
                const edificiosUnicos = [...new Set(salonesBD.map(s => s.edificio))];
                
                selectEdificio.innerHTML = '';
                edificiosUnicos.forEach(edificio => {
                    const option = document.createElement('option');
                    option.value = edificio;
                    option.textContent = edificio;
                    selectEdificio.appendChild(option);
                });

                if (edificiosUnicos.length > 0) {
                    cargarSalones();
                }
            })
            .catch(e => mostrarAlerta("Error de conexión al cargar salones.", true));
    }

    window.cargarSalones = () => {
        const edificioSeleccionado = selectEdificio.value;
        selectSalon.innerHTML = '<option value="" disabled selected>Seleccione Salón</option>';
        
        const salonesFiltrados = salonesBD.filter(s => s.edificio === edificioSeleccionado);
        
        salonesFiltrados.forEach(salon => {
            const option = document.createElement('option');
            option.value = salon.idSalon; 
            option.textContent = salon.nombre;
            selectSalon.appendChild(option);
        });
    };

    selectEdificio.addEventListener('change', cargarSalones);

    function actualizarTarjetas() {
        const total = reservaciones.length;
        const pendientes = reservaciones.filter(r => r.estatus === 'Pendiente').length;
        const activas = reservaciones.filter(r => r.estatus === 'Activo').length;
        const contadores = document.querySelectorAll('.row.g-4 h3 span');
        if (contadores && contadores.length >= 3) {
            contadores[0].innerText = total;
            contadores[1].innerText = pendientes;
            contadores[2].innerText = activas;
        }
    }

    function render() {
        tablaBody.innerHTML = '';
        reservaciones.forEach(res => {
            const tr = document.createElement('tr');
            const fechaFormateada = res.fechaHora ? res.fechaHora.replace('T', ' ') : '';
            let badgeClass = res.estatus === "Pendiente" ? "bg-warning text-dark" : 
                             res.estatus === "Activo" ? "bg-success text-white" : 
                             res.estatus === "Rechazado" ? "bg-danger text-white" : "bg-info text-white";

            tr.innerHTML = `
                <td>
                    <div class="d-flex align-items-center">
                        <div class="rounded-circle text-white d-flex align-items-center justify-content-center me-2" style="width:30px;height:30px;font-size:10px; background-color: #2a2155;">
                            ${res.matricula ? res.matricula.slice(-2) : 'NA'}
                        </div>
                        <span class="fw-medium">${res.matricula}</span>
                    </div>
                </td>
                <td><span class="text-muted small">${res.nombreEdificio}</span></td>
                <td><span class="fw-bold" style="color: #2a2155;">${res.nombreSalon}</span></td>
                <td><i class="bi bi-clock me-1 text-muted"></i> ${fechaFormateada}</td>
                <td><span class="badge ${badgeClass}">${res.estatus.toUpperCase()}</span></td>
                <td class="text-end">
                    <button class="btn btn-sm btn-outline-primary me-1" onclick="editRes(${res.idReserva})"><i class="bi bi-pencil-square"></i></button>
                    <button class="btn btn-sm btn-outline-danger" onclick="deleteRes(${res.idReserva})"><i class="bi bi-trash3-fill"></i></button>
                </td>`;
            tablaBody.appendChild(tr);
        });
        actualizarTarjetas();
    }

    function cargarReservaciones() {
        fetch(`${URL_BASE}/getAll`)
            .then(r => r.json())
            .then(data => {
                reservaciones = data;
                render();
            })
            .catch(e => mostrarAlerta("Error de conexión al cargar reservaciones.", true));
    }

    form.addEventListener('submit', (e) => {
        e.preventDefault();
        const id = document.getElementById('reservaId').value;
        const endpoint = id ? `${URL_BASE}/actualizar` : `${URL_BASE}/insertar`;

        const salonElegido = parseInt(selectSalon.value);
        if (!salonElegido) {
            mostrarAlerta("Por favor seleccione un salón.");
            return;
        }

        const reservacionObj = {
            idReserva: id ? parseInt(id) : 0,
            idUsuario: 1, 
            idSalon: salonElegido, 
            fechaHora: document.getElementById('fecha').value.replace('T', ' ') + ':00',
            estatus: document.getElementById('estadoSelect').value
        };

        const formData = new URLSearchParams();
        formData.append("reservacion", JSON.stringify(reservacionObj));

        fetch(endpoint, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: formData.toString()
        })
        .then(r => r.json())
        .then(data => {
            if (data.error) {
                mostrarAlerta(data.error, true);
            } else { 
                modal.hide(); 
                form.reset(); 
                cargarReservaciones();
                mostrarAlerta("La reservación se guardó exitosamente.");
            }
        });
    });
    
    window.editRes = (id) => {
        const res = reservaciones.find(r => r.idReserva === id);
        if (res) {
            document.getElementById('reservaId').value = res.idReserva;
            if(document.getElementById('matricula')) {
                document.getElementById('matricula').value = res.matricula;
            }
            
            selectEdificio.value = res.nombreEdificio;
            
            cargarSalones();
            
            for (let i = 0; i < selectSalon.options.length; i++) {
                if (selectSalon.options[i].text === res.nombreSalon) { 
                    selectSalon.selectedIndex = i; 
                    break; 
                }
            }
            
            if (res.fechaHora) {
                document.getElementById('fecha').value = res.fechaHora.replace(' ', 'T').substring(0, 16);
            }
            document.getElementById('estadoSelect').value = res.estatus;
            modal.show();
        }
    };

    window.deleteRes = (id) => {
        mostrarConfirmacion('¿Estás seguro de que deseas eliminar esta reservación?', () => {
            const f = new URLSearchParams(); 
            f.append("idReserva", id);
            
            fetch(`${URL_BASE}/eliminar`, { 
                method: 'POST', 
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: f 
            })
            .then(r => r.json())
            .then(data => {
                if(data.error) {
                    mostrarAlerta(data.error, true);
                } else {
                    cargarReservaciones();
                    mostrarAlerta("Reservación eliminada correctamente.");
                }
            });
        });
    };

    window.limpiarForm = () => {
        form.reset();
        document.getElementById('reservaId').value = '';
        if (selectEdificio.options.length > 0) {
            selectEdificio.selectedIndex = 0;
            cargarSalones();
        }
    };

    cargarEdificiosYSalones();
    cargarReservaciones();
});

window.cerrarSesion = () => {
    localStorage.clear();
    window.location.href = "../index.html";
};
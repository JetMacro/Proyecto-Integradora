let listaGlobalReportes = [];
let fotoBase64 = "";

document.addEventListener('DOMContentLoaded', () => {
    const nombre = localStorage.getItem("nombre_usuario") || "Usuario";
    const rolRaw = localStorage.getItem("id_rol") || "Invitado";
    
    if (document.getElementById('userNameDisplay')) document.getElementById('userNameDisplay').textContent = nombre;
    if (document.getElementById('userRoleDisplay')) document.getElementById('userRoleDisplay').textContent = rolRaw;
    
    cargarReportes();
    cargarCatalogoInventario();
});

window.previsualizarImagen = function() {
    const fileInput = document.getElementById('txtFoto');
    const preview = document.getElementById('imgPreview');
    const container = document.getElementById('containerPreview');
    const file = fileInput.files[0];

    if (file) {
        const reader = new FileReader();
        reader.onload = function(e) {
            fotoBase64 = e.target.result; 
            
            preview.src = e.target.result;
            container.classList.remove('d-none');
        };
        reader.readAsDataURL(file);
    }
};

async function cargarReportes() {
    const tbody = document.getElementById("tbodyReportes");
    if (!tbody) return;

    try {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center p-4">Cargando registros...</td></tr>';
        const response = await fetch("../api/reporte/getAll");
        listaGlobalReportes = await response.json();
        renderizarTabla(listaGlobalReportes);
        actualizarResumen(listaGlobalReportes);
    } catch (error) {
        console.error("Error:", error);
        tbody.innerHTML = '<tr><td colspan="6" class="text-center text-danger">Error de conexión</td></tr>';
    }
}

function renderizarTabla(datos) {
    const rol = (localStorage.getItem("id_rol") || "").trim().toLowerCase();
    const tbody = document.getElementById("tbodyReportes");
    if (!tbody) return;
    tbody.innerHTML = "";

    datos.forEach(r => {
        let prioClass = r.prioridad === 'Critica' ? 'bg-danger' : r.prioridad === 'Alta' ? 'bg-warning text-dark' : 'bg-info text-dark';
        let estBadge = r.estatus === 'Pendiente' ? 'bg-warning text-dark' : r.estatus === 'Baja Definitiva' ? 'bg-danger' : 'bg-success';

        let botonesAccion = "";
        if (rol === "administrador") {
            botonesAccion = `
                <button class="btn btn-sm btn-outline-primary me-2" onclick="prepararNuevaVersion(${r.id_reporte})"><i class="bi bi-pencil-square"></i></button>
                <button class="btn btn-sm btn-outline-danger me-2" onclick="eliminarReporte(${r.id_reporte})"><i class="bi bi-trash3"></i></button>
            `;
        }
        botonesAccion += `<button class="btn btn-sm btn-outline-success" onclick="mostrarDetalle(${r.id_reporte})"><i class="bi bi-eye"></i></button>`;

        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td class="fw-medium">${r.codigo_mueble}</td>
            <td>${r.tipo_material}</td>
            <td>${r.nombre_salon}</td>
            <td class="text-center"><span class="badge ${prioClass}">${r.prioridad}</span></td>
            <td class="text-center"><span class="badge ${estBadge}">${r.estatus}</span></td>
            <td class="text-end">${botonesAccion}</td>`;
        tbody.appendChild(tr);
    });
}

function cargarCatalogoInventario() {
    const select = document.getElementById("idInventario");
    if (!select) return;
    fetch("../api/reporte/getMateriales")
        .then(res => res.json())
        .then(data => {
            select.innerHTML = '<option value="" selected disabled>Seleccione material...</option>';
            data.forEach(item => {
                const opt = document.createElement("option");
                opt.value = item.id_reporte;
                opt.text = `${item.codigo_mueble} - ${item.tipo_material}`;
                select.appendChild(opt);
            });
        });
}

const formReporte = document.getElementById('formReporte');
if (formReporte) {
    formReporte.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const params = new URLSearchParams();
        const reporte = {
            tipo_dano: document.getElementById('tipoDano').value,
            descripcion: document.getElementById('descripcion').value,
            prioridad: document.getElementById('prioridad').value,
            estatus: document.getElementById('estatus').value
        };

        params.append("datos", JSON.stringify(reporte));
        params.append("idUsuario", localStorage.getItem("id_usuario") || 1);
        params.append("idInventario", document.getElementById('idInventario').value);
        params.append("fotoB64", fotoBase64);

        try {
            const resp = await fetch("../api/reporte/insertar", { 
                method: 'POST', 
                body: params,
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
            });
            const data = await resp.json();

            if (data.res === "Éxito") {
                Swal.fire('Éxito', 'Reporte guardado correctamente', 'success');
                const modalElement = document.getElementById('reporteModal');
                const modal = bootstrap.Modal.getInstance(modalElement);
                if (modal) modal.hide();
                
                limpiarForm();
                cargarReportes();
            } else {
                Swal.fire('Error', 'No se pudo guardar: ' + data.error, 'error');
            }
        } catch (error) {
            console.error(error);
            Swal.fire('Error', 'Fallo en la comunicación con el servidor', 'error');
        }
    });
}

window.eliminarReporte = function(id) {
    Swal.fire({
        title: '¿Eliminar?',
        text: 'Se marcará como Baja Definitiva',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#2a2155',
        confirmButtonText: 'Sí, eliminar'
    }).then(async (result) => {
        if (result.isConfirmed) {
            const params = new URLSearchParams();
            params.append("idReporte", id);
            await fetch("../api/reporte/eliminar", { 
                method: 'POST', 
                body: params, 
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' } 
            });
            Swal.fire('Eliminado', 'Estatus actualizado', 'success');
            cargarReportes();
        }
    });
};

function actualizarResumen(datos) {
    const p = document.getElementById('txtPendientes');
    const s = document.getElementById('txtSolucionados');
    const b = document.getElementById('txtBajas');
    const t = document.getElementById('txtTotal');
    if (p) p.textContent = datos.filter(r => r.estatus === 'Pendiente').length;
    if (s) s.textContent = datos.filter(r => r.estatus === 'Solucionado').length;
    if (b) b.textContent = datos.filter(r => r.estatus === 'Baja Definitiva').length;
    if (t) t.textContent = datos.length;
}

window.mostrarDetalle = function(id) {
    // 1. Quita el focus del botón para evitar el error de "aria-hidden" en consola
    if(document.activeElement) document.activeElement.blur(); 

    const r = listaGlobalReportes.find(i => i.id_reporte === id);
    if(r) {
        document.getElementById('detDescripcion').innerText = r.descripcion || "Sin descripción";
        document.getElementById('detFecha').innerText = r.fecha_reporte || "Sin fecha";
        document.getElementById('detUsuario').innerText = r.matricula || "N/A";

        const imgDetalle = document.getElementById('detFoto');
        
        // 2. Verificamos si existe la foto y si es una cadena Base64 válida
        if (r.fotoEvidencia && r.fotoEvidencia.length > 50) { 
            // Si la foto ya trae el prefijo "data:image", la ponemos directo. 
            // Si no, se lo agregamos.
            imgDetalle.src = r.fotoEvidencia.startsWith("data:image") 
                             ? r.fotoEvidencia 
                             : "data:image/png;base64," + r.fotoEvidencia;
            
            imgDetalle.style.display = "inline-block";
        } else {
            imgDetalle.style.display = "none";
            imgDetalle.src = "";
        }

        new bootstrap.Modal(document.getElementById('modalDetalles')).show();
    }
}

window.prepararNuevaVersion = function(id) {
    const r = listaGlobalReportes.find(i => i.id_reporte === id);
    if(r) {
        document.getElementById('idInventario').value = r.id_reporte; 
        document.getElementById('tipoDano').value = r.tipo_dano;
        document.getElementById('descripcion').value = r.descripcion;
        document.getElementById('prioridad').value = r.prioridad;
        document.getElementById('estatus').value = r.estatus;
        
        fotoBase64 = "";
        document.getElementById('containerPreview').classList.add('d-none');
        
        new bootstrap.Modal(document.getElementById('reporteModal')).show();
    }
}

function aplicarFiltros() {
    const p = document.getElementById('filtroPrioridad').value;
    const t = document.getElementById('filtroTipoDano').value;
    const e = document.getElementById('filtroEstatus').value;
    const res = listaGlobalReportes.filter(r => 
        (p === "" || r.prioridad === p) && 
        (t === "" || r.tipo_dano === t) && 
        (e === "" || r.estatus === e)
    );
    renderizarTabla(res);
}

function limpiarFiltros() {
    document.getElementById('filtroPrioridad').value = "";
    document.getElementById('filtroTipoDano').value = "";
    document.getElementById('filtroEstatus').value = "";
    renderizarTabla(listaGlobalReportes);
}

window.limpiarForm = () => { 
    if(formReporte) formReporte.reset(); 
    fotoBase64 = "";
    const container = document.getElementById('containerPreview');
    if (container) container.classList.add('d-none');
};

window.cerrarSesion = () => {
    localStorage.clear();
    window.location.href = "../index.html";
};
document.addEventListener("DOMContentLoaded", function() {
    const nombre = localStorage.getItem("nombre_usuario") || "Usuario";
    const rolRaw = localStorage.getItem("id_rol") || "Invitado";
    
    if (document.getElementById('userNameDisplay')) document.getElementById('userNameDisplay').textContent = nombre;
    if (document.getElementById('userRoleDisplay')) document.getElementById('userRoleDisplay').textContent = rolRaw;
    cargarDashboard();
});

function cargarDashboard() {
    const url = 'http://localhost:8080/IntegradoraII_Web/api/dashboard/resumen';

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Error en la red o servidor');
            }
            return response.json();
        })
        .then(data => {
            document.getElementById('total-inventario').innerText = data.totalMobiliario;
            document.getElementById('total-usuarios').innerText = data.totalUsuarios;
            document.getElementById('total-reportes').innerText = data.reporteSemanal;
            document.getElementById('reservaciones-activas').innerText = data.activas;

            const contenedorActividades = document.getElementById('contenedor-actividades');
            
            contenedorActividades.innerHTML = ''; 

            data.actividades.forEach(act => {
                const tiempoTranscurrido = calcularTiempo(act.fechaActividad);

const filaHTML = `
    <div class="d-flex justify-content-between align-items-center mb-3" 
         style="cursor: pointer;" 
         onclick="abrirModal('${act.titulo}', '${act.descripcion}', '${act.fechaActividad}', '${act.autor}')">
        <div>
            <h6 class="mb-0 fw-bold text-dark">${act.titulo}</h6>
            <small class="text-muted">${act.descripcion}</small>
        </div>
        <span class="text-muted small">${tiempoTranscurrido}</span>
    </div>
`;
                
                contenedorActividades.innerHTML += filaHTML;
            });

        })
        .catch(error => {
            console.error('Hubo un problema con la petición:', error);
            document.getElementById('total-inventario').innerText = "-";
            document.getElementById('total-usuarios').innerText = "-";
        });
}

// Función auxiliar para convertir la fecha SQL a "Hace X min"
function calcularTiempo(fechaSQL) {

    const fechaEvento = new Date(fechaSQL.replace(" ", "T")); 
    const ahora = new Date();
    
    // Calculamos la diferencia en segundos
    const diferenciaSegundos = Math.floor((ahora - fechaEvento) / 1000);
    const minutos = Math.floor(diferenciaSegundos / 60);
    const horas = Math.floor(minutos / 60);
    const dias = Math.floor(horas / 24);

    if (minutos < 1) return "Hace un momento";
    if (minutos < 60) return `Hace ${minutos} min`;
    if (horas < 24) return `Hace ${horas} hora${horas > 1 ? 's' : ''}`;
    return `Hace ${dias} día${dias > 1 ? 's' : ''}`;
}
function abrirModal(titulo, descripcion, fecha, autor) {
    document.getElementById('modal-titulo').value = titulo;
    document.getElementById('modal-descripcion').value = descripcion;
    document.getElementById('modal-fecha').value = fecha;
    
    document.getElementById('modal-autor').value = (autor && autor !== 'null') ? autor : 'Sistema';

    const miModal = new bootstrap.Modal(document.getElementById('modalDetalleActividad'));
    miModal.show();
}

window.cerrarSesion = () => {
    localStorage.clear();
    window.location.href = "../index.html";
};
/**
 * Archivo: restablecer.js
 * Controlador del flujo de recuperacion de contrasena mediante enlace por correo electronico
 */
document.addEventListener('DOMContentLoaded', () => {
    
    // Endpoint para entorno local
    const API_BASE = "/api/usuario/";

    // Referencias del DOM
    const modal = document.getElementById('modalRecuperacion');
    const btnCerrar = document.getElementById('cerrarModalRecuperacion');
    const linkOlvide = document.getElementById('link-olvide-pass');
    
    const formSolicitar = document.getElementById('form-solicitar');
    const formActualizar = document.getElementById('form-actualizar');
    const titulo = document.getElementById('tituloModal');
    const subtitulo = document.getElementById('subtituloModal');
    
    // Verificacion de parametros de URL (Retorno desde enlace de Gmail)
    const urlParams = new URLSearchParams(window.location.search);
    const isReset = urlParams.get('reset');
    const correoDestino = urlParams.get('correo');

    // Despliegue de formulario de NUEVA CONTRASENA si los parametros son validos
    if (isReset === 'true' && correoDestino) {
        if(modal) modal.style.display = 'flex';
        if(formSolicitar) formSolicitar.style.display = 'none';
        if(formActualizar) formActualizar.style.display = 'block';
        if(titulo) titulo.innerText = 'Crear nueva contrasena';
        if(subtitulo) subtitulo.innerText = 'Ingresa una nueva clave para ' + correoDestino;
        if(document.getElementById('correo-destino')) document.getElementById('correo-destino').value = correoDestino;
    }

    // Apertura del modal en modo SOLICITAR ENLACE
    if(linkOlvide) {
        linkOlvide.addEventListener('click', (e) => {
            e.preventDefault();
            modal.style.display = 'flex';
            formSolicitar.style.display = 'block';
            formActualizar.style.display = 'none';
            titulo.innerText = 'Recuperar Contrasena';
            subtitulo.innerText = 'Ingresa tu correo para recibir un enlace';
        });
    }

    // Cierre del modal y limpieza de la URL
    if(btnCerrar) {
        btnCerrar.addEventListener('click', () => {
            modal.style.display = 'none';
            if(isReset) {
                // Elimina los parametros para evitar que se reabra si se refresca la pagina
                window.history.replaceState({}, document.title, window.location.pathname);
            }
        });
    }

    // PASO 1: Peticion para enviar el correo a Gmail
    if(formSolicitar) {
        formSolicitar.addEventListener('submit', async (e) => {
            e.preventDefault();
            const correo = document.getElementById('correo-recuperacion').value;
            
            Swal.fire({ title: 'Enviando enlace...', allowOutsideClick: false, didOpen: () => Swal.showLoading() });

            try {
                const response = await fetch(API_BASE + 'solicitarRecuperacion', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ correo: correo })
                });

                const data = await response.json();
                
                if(response.ok) {
                    Swal.fire('Exito', 'Revisa tu bandeja de entrada o SPAM.', 'success');
                    modal.style.display = 'none';
                    formSolicitar.reset();
                } else {
                    Swal.fire('Error', data.error || 'No se pudo enviar el correo.', 'error');
                }
            } catch (error) {
                Swal.fire('Error', 'No se pudo conectar con el servidor.', 'error');
            }
        });
    }

    // PASO 2: Peticion para guardar la nueva contrasena
    if(formActualizar) {
        formActualizar.addEventListener('submit', async (e) => {
            e.preventDefault();
            const pass1 = document.getElementById('new-password').value;
            const pass2 = document.getElementById('confirm-password').value;
            const correo = document.getElementById('correo-destino').value;

            // Validacion local
            if(pass1 !== pass2) {
                Swal.fire('Atencion', 'Las contrasenas no coinciden.', 'warning');
                return;
            }

            Swal.fire({ title: 'Guardando...', allowOutsideClick: false, didOpen: () => Swal.showLoading() });

            try {
                const response = await fetch(API_BASE + 'actualizarPassword', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ correo: correo, contrasenia: pass1 })
                });
                
                const data = await response.json();

                if(response.ok) {
                    Swal.fire('Exito', 'Contrasena actualizada. Ya puedes iniciar sesion.', 'success').then(() => {
                        modal.style.display = 'none';
                        window.history.replaceState({}, document.title, window.location.pathname);
                        formActualizar.reset();
                    });
                } else {
                    Swal.fire('Error', data.error || 'No se pudo actualizar.', 'error');
                }
            } catch(error) {
                Swal.fire('Error', 'No se pudo conectar con el servidor.', 'error');
            }
        });
    }
});
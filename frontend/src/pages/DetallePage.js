import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { obtenerMascota } from '../services/mascotaService';
import { listarCoincidencias } from '../services/coincidenciaService';

function DetallePage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [mascota, setMascota] = useState(null);
  const [coincidencias, setCoincidencias] = useState([]);
  const [mascotasCoincidentes, setMascotasCoincidentes] = useState({});
  const [busquedaId, setBusquedaId] = useState('');
  const [error, setError] = useState('');

  const buscarReporte = (idBuscar) => {
    setError('');
    obtenerMascota(idBuscar)
      .then(response => {
        setMascota(response.data);
        return listarCoincidencias();
      })
      .then(async response => {
        const filtradas = response.data.filter(c =>
          c.mascotaPerdidaId === idBuscar || c.mascotaEncontradaId === idBuscar
        );
        setCoincidencias(filtradas);
        const mascotasMap = {};
        await Promise.all(
        filtradas.map(c => {
            const idOpuesto = c.mascotaPerdidaId === idBuscar ? c.mascotaEncontradaId : c.mascotaPerdidaId;
            return obtenerMascota(idOpuesto)
            .then(res => { mascotasMap[idOpuesto] = res.data; })
            .catch(() => { mascotasMap[idOpuesto] = { nombre: 'Desconocida', contacto: 'No disponible' }; });
        })
        );
        setMascotasCoincidentes(mascotasMap);
      })
      .catch(() => {
        setMascota(null);
        setError('No se encontró ningún reporte con ese ID.');
      });
  };

  useEffect(() => {
    if (id) {
      setBusquedaId(id);
      buscarReporte(id);
    }
  }, [id]);

  const inputStyle = {
    padding: '8px 12px',
    border: '0.5px solid var(--orange-light)',
    borderRadius: '8px',
    fontSize: '13px',
    background: 'var(--white)',
    color: 'var(--text-primary)',
    outline: 'none',
    width: '320px',
  };

  return (
    <div style={{ padding: '28px 32px' }}>
      <h2 style={{ fontSize: '20px', fontWeight: '500', color: 'var(--text-primary)', marginBottom: '20px' }}>
        Buscar reporte por ID
      </h2>

      <div style={{ display: 'flex', gap: '10px', marginBottom: '28px' }}>
        <input
          style={inputStyle}
          placeholder="Ingresa el ID del reporte..."
          value={busquedaId}
          onChange={e => setBusquedaId(e.target.value)}
        />
        <button
          onClick={() => buscarReporte(busquedaId)}
          style={{ padding: '8px 20px', background: 'var(--orange-primary)', color: '#fff', border: 'none', borderRadius: '8px', fontSize: '13px', fontWeight: '500', cursor: 'pointer' }}
        >
          Buscar
        </button>
      </div>

      {error && (
        <div style={{ background: 'var(--badge-lost-bg)', color: 'var(--badge-lost-text)', padding: '12px 16px', borderRadius: '8px', fontSize: '13px', marginBottom: '20px' }}>
          {error}
        </div>
      )}

      {mascota && (
        <div>
          <div style={{ background: 'var(--white)', border: '0.5px solid var(--orange-light)', borderRadius: '12px', padding: '20px', marginBottom: '24px' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '12px' }}>
              <h3 style={{ fontSize: '18px', fontWeight: '500', color: 'var(--text-primary)' }}>{mascota.nombre}</h3>
              <span style={{ background: mascota.estado === 'perdida' ? 'var(--badge-lost-bg)' : 'var(--badge-found-bg)', color: mascota.estado === 'perdida' ? 'var(--badge-lost-text)' : 'var(--badge-found-text)', fontSize: '12px', padding: '3px 10px', borderRadius: '999px', fontWeight: '500' }}>
                {mascota.estado?.charAt(0).toUpperCase() + mascota.estado?.slice(1)}
              </span>
            </div>
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: '12px' }}>
              <div><span style={{ fontSize: '12px', color: 'var(--text-secondary)' }}>Raza</span><p style={{ fontSize: '14px', color: 'var(--text-primary)' }}>{mascota.raza}</p></div>
              <div><span style={{ fontSize: '12px', color: 'var(--text-secondary)' }}>Color</span><p style={{ fontSize: '14px', color: 'var(--text-primary)' }}>{mascota.color}</p></div>
              <div><span style={{ fontSize: '12px', color: 'var(--text-secondary)' }}>Tamaño</span><p style={{ fontSize: '14px', color: 'var(--text-primary)' }}>{mascota.tamano}</p></div>
              <div><span style={{ fontSize: '12px', color: 'var(--text-secondary)' }}>Contacto</span><p style={{ fontSize: '14px', color: 'var(--text-primary)' }}>{mascota.contacto}</p></div>
              <div><span style={{ fontSize: '12px', color: 'var(--text-secondary)' }}>Fecha reporte</span><p style={{ fontSize: '14px', color: 'var(--text-primary)' }}>{new Date(mascota.fechaReporte).toLocaleDateString('es-CL')}</p></div>
              {mascota.fechaDesaparicion && <div><span style={{ fontSize: '12px', color: 'var(--text-secondary)' }}>Fecha desaparición</span><p style={{ fontSize: '14px', color: 'var(--text-primary)' }}>{new Date(mascota.fechaDesaparicion).toLocaleDateString('es-CL')}</p></div>}
            </div>
            {mascota.descripcion && <p style={{ fontSize: '13px', color: 'var(--text-muted)', marginTop: '12px' }}>{mascota.descripcion}</p>}
          </div>

          <h3 style={{ fontSize: '16px', fontWeight: '500', color: 'var(--text-primary)', marginBottom: '12px' }}>
            Coincidencias encontradas ({coincidencias.length})
          </h3>

          {coincidencias.length === 0 ? (
            <div style={{ background: 'var(--white)', border: '0.5px solid var(--orange-light)', borderRadius: '12px', padding: '32px', textAlign: 'center', color: 'var(--text-muted)', fontSize: '14px' }}>
              No se encontraron coincidencias para este reporte aún
            </div>
          ) : (
            coincidencias.map(c => (
              <div key={c.id} style={{ background: 'var(--white)', border: '0.5px solid var(--orange-light)', borderRadius: '12px', padding: '16px', marginBottom: '12px', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <div>
                  <div style={{ marginBottom: '6px' }}>
                    <span style={{ fontSize: '13px', fontWeight: '500', color: 'var(--text-primary)' }}>
                        {(() => {
                        const idOpuesto = c.mascotaPerdidaId === id ? c.mascotaEncontradaId : c.mascotaPerdidaId;
                        return mascotasCoincidentes[idOpuesto]?.nombre || 'Desconocida';
                        })()}
                    </span>
                    <p style={{ fontSize: '12px', color: 'var(--text-secondary)', marginTop: '2px' }}>
                        Contacto: {(() => {
                        const idOpuesto = c.mascotaPerdidaId === id ? c.mascotaEncontradaId : c.mascotaPerdidaId;
                        return mascotasCoincidentes[idOpuesto]?.contacto || 'No disponible';
                        })()}
                    </p>
                    </div>
                    <p style={{ fontSize: '13px', color: 'var(--text-secondary)', marginBottom: '4px' }}>{c.observaciones}</p>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                    <div style={{ height: '6px', borderRadius: '3px', background: 'var(--orange-border)', width: '120px', overflow: 'hidden' }}>
                      <div style={{ height: '100%', borderRadius: '3px', background: 'var(--orange-primary)', width: `${c.porcentajeSimilitud}%` }} />
                    </div>
                    <span style={{ fontSize: '13px', fontWeight: '500', color: 'var(--orange-dark)' }}>{Math.round(c.porcentajeSimilitud * 10) / 10}%</span>
                  </div>
                </div>
                <span style={{ background: c.estado === 'confirmada' ? 'var(--badge-found-bg)' : 'var(--badge-lost-bg)', color: c.estado === 'confirmada' ? 'var(--badge-found-text)' : 'var(--badge-lost-text)', fontSize: '11px', padding: '3px 9px', borderRadius: '999px', fontWeight: '500' }}>
                  {c.estado?.charAt(0).toUpperCase() + c.estado?.slice(1)}
                </span>
              </div>
            ))
          )}
        </div>
      )}
    </div>
  );
}

export default DetallePage;
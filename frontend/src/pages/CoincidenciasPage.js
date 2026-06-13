import React, { useState, useEffect } from 'react';
import { listarCoincidencias } from '../services/coincidenciaService';
import { obtenerMascota } from '../services/mascotaService';

function CoincidenciasPage() {
  const [coincidencias, setCoincidencias] = useState([]);
  const [mascotas, setMascotas] = useState({});

  useEffect(() => {
    listarCoincidencias()
      .then(async response => {
        const data = response.data;
        setCoincidencias(data);

        const ids = new Set();
        data.forEach(c => {
          ids.add(c.mascotaPerdidaId);
          ids.add(c.mascotaEncontradaId);
        });

        const mascotasMap = {};
        await Promise.all(
          [...ids].map(id =>
            obtenerMascota(id)
              .then(res => { mascotasMap[id] = res.data; })
              .catch(() => { mascotasMap[id] = { nombre: 'Desconocida', raza: '-' }; })
          )
        );
        setMascotas(mascotasMap);
      })
      .catch(error => console.error('Error al cargar coincidencias:', error));
  }, []);

  const getNombre = (id) => mascotas[id]?.nombre || id;
  const getRaza = (id) => mascotas[id]?.raza || '-';
  const getColor = (id) => mascotas[id]?.color || '-';

  return (
    <div style={{ padding: '28px 32px' }}>
      <div style={{ marginBottom: '20px' }}>
        <h2 style={{ fontSize: '20px', fontWeight: '500', color: 'var(--text-primary)', marginBottom: '4px' }}>
          Posibles coincidencias
        </h2>
        <p style={{ fontSize: '13px', color: 'var(--text-secondary)' }}>
          El sistema compara mascotas perdidas con encontradas automáticamente
        </p>
      </div>

      <div style={{ background: 'var(--white)', border: '0.5px solid var(--orange-light)', borderRadius: '12px', overflow: 'hidden' }}>
        <table style={{ width: '100%', borderCollapse: 'collapse' }}>
          <thead>
            <tr>
              {['Mascota perdida', 'Mascota encontrada', 'Similitud', 'Observaciones', 'Estado', ''].map(h => (
                <th key={h} style={{ fontSize: '12px', color: 'var(--text-secondary)', fontWeight: '500', textAlign: 'left', padding: '10px 16px', borderBottom: '0.5px solid var(--orange-border)', background: 'var(--white)' }}>
                  {h}
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {coincidencias.map(c => (
              <tr key={c.id}>
                <td style={{ padding: '14px 16px', borderBottom: '0.5px solid var(--orange-border)' }}>
                  <div style={{ fontSize: '13px', fontWeight: '500', color: 'var(--text-primary)' }}>{getNombre(c.mascotaPerdidaId)}</div>
                  <div style={{ fontSize: '12px', color: 'var(--text-secondary)' }}>{getRaza(c.mascotaPerdidaId)} · {getColor(c.mascotaPerdidaId)}</div>
                </td>
                <td style={{ padding: '14px 16px', borderBottom: '0.5px solid var(--orange-border)' }}>
                  <div style={{ fontSize: '13px', fontWeight: '500', color: 'var(--text-primary)' }}>{getNombre(c.mascotaEncontradaId)}</div>
                  <div style={{ fontSize: '12px', color: 'var(--text-secondary)' }}>{getRaza(c.mascotaEncontradaId)} · {getColor(c.mascotaEncontradaId)}</div>
                </td>
                <td style={{ padding: '14px 16px', borderBottom: '0.5px solid var(--orange-border)' }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                    <div style={{ height: '6px', borderRadius: '3px', background: 'var(--orange-border)', width: '100px', overflow: 'hidden' }}>
                      <div style={{ height: '100%', borderRadius: '3px', background: 'var(--orange-primary)', width: `${c.porcentajeSimilitud}%` }} />
                    </div>
                    <span style={{ fontSize: '13px', fontWeight: '500', color: 'var(--orange-dark)' }}>{c.porcentajeSimilitud}%</span>
                  </div>
                </td>
                <td style={{ padding: '14px 16px', borderBottom: '0.5px solid var(--orange-border)', fontSize: '12px', color: 'var(--text-muted)' }}>
                  {c.observaciones}
                </td>
                <td style={{ padding: '14px 16px', borderBottom: '0.5px solid var(--orange-border)' }}>
                  <span style={{ background: c.estado === 'confirmada' ? 'var(--badge-found-bg)' : 'var(--badge-lost-bg)', color: c.estado === 'confirmada' ? 'var(--badge-found-text)' : 'var(--badge-lost-text)', fontSize: '11px', padding: '3px 9px', borderRadius: '999px', fontWeight: '500' }}>
                    {c.estado?.charAt(0).toUpperCase() + c.estado?.slice(1)}
                  </span>
                </td>
                <td style={{ padding: '14px 16px', borderBottom: '0.5px solid var(--orange-border)' }}>
                  <button style={{ padding: '4px 10px', border: '0.5px solid var(--orange-light)', borderRadius: '8px', fontSize: '12px', background: 'var(--white)', color: 'var(--orange-dark)', cursor: 'pointer' }}>
                    Ver detalle
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        {coincidencias.length === 0 && (
          <div style={{ padding: '40px', textAlign: 'center', color: 'var(--text-muted)', fontSize: '14px' }}>
            No hay coincidencias registradas aún
          </div>
        )}
      </div>
    </div>
  );
}

export default CoincidenciasPage;
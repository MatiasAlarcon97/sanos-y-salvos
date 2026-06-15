import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { listarMascotas } from '../services/mascotaService';

function MascotasPage() {
  const [mascotas, setMascotas] = useState([]);
  const [filtro, setFiltro] = useState('todas');
  const [busqueda, setBusqueda] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    listarMascotas()
      .then(response => setMascotas(response.data))
      .catch(error => console.error('Error al cargar mascotas:', error));
  }, []);

  const mascotasFiltradas = mascotas.filter(m => {
    const coincideFiltro = filtro === 'todas' || m.estado === filtro;
    const coincideBusqueda = m.nombre.toLowerCase().includes(busqueda.toLowerCase()) ||
      m.raza.toLowerCase().includes(busqueda.toLowerCase());
    return coincideFiltro && coincideBusqueda;
  });

  const perdidas = mascotas.filter(m => m.estado === 'perdida').length;
  const encontradas = mascotas.filter(m => m.estado === 'encontrada').length;

  return (
    <div>
      <div style={{ background: 'var(--white)', borderBottom: '0.5px solid var(--orange-border)', padding: '20px 32px', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <div>
          <h1 style={{ fontSize: '19px', fontWeight: '500', color: 'var(--orange-dark)', marginBottom: '4px' }}>
            Ayuda a reunir mascotas con sus familias
          </h1>
          <p style={{ fontSize: '13px', color: 'var(--text-secondary)' }}>
            Reporta una mascota perdida o encontrada y el sistema buscará coincidencias
          </p>
        </div>
        <button onClick={() => navigate('/reportar')} style={{ background: 'var(--orange-primary)', color: '#fff', border: 'none', padding: '9px 20px', borderRadius: '8px', fontSize: '13px', fontWeight: '500', cursor: 'pointer' }}>
          + Reportar mascota
        </button>
      </div>

      <div style={{ padding: '24px 32px' }}>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3,1fr)', gap: '12px', marginBottom: '20px' }}>
          <div style={{ background: 'var(--white)', border: '0.5px solid var(--orange-light)', borderRadius: '8px', padding: '14px 18px' }}>
            <div style={{ fontSize: '12px', color: 'var(--text-secondary)', marginBottom: '4px' }}>Total reportes</div>
            <div style={{ fontSize: '24px', fontWeight: '500', color: 'var(--orange-dark)' }}>{mascotas.length}</div>
          </div>
          <div style={{ background: 'var(--white)', border: '0.5px solid var(--orange-light)', borderRadius: '8px', padding: '14px 18px' }}>
            <div style={{ fontSize: '12px', color: 'var(--text-secondary)', marginBottom: '4px' }}>Perdidas</div>
            <div style={{ fontSize: '24px', fontWeight: '500', color: 'var(--badge-lost-text)' }}>{perdidas}</div>
          </div>
          <div style={{ background: 'var(--white)', border: '0.5px solid var(--orange-light)', borderRadius: '8px', padding: '14px 18px' }}>
            <div style={{ fontSize: '12px', color: 'var(--text-secondary)', marginBottom: '4px' }}>Encontradas</div>
            <div style={{ fontSize: '24px', fontWeight: '500', color: 'var(--badge-found-text)' }}>{encontradas}</div>
          </div>
        </div>

        <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '16px' }}>
          <input
            placeholder="Buscar por nombre, raza..."
            value={busqueda}
            onChange={e => setBusqueda(e.target.value)}
            style={{ padding: '7px 12px', border: '0.5px solid var(--orange-light)', borderRadius: '8px', fontSize: '13px', background: 'var(--white)', color: 'var(--text-primary)', width: '250px', outline: 'none' }}
          />
          {['todas', 'perdida', 'encontrada'].map(f => (
            <button key={f} onClick={() => setFiltro(f)} style={{ padding: '5px 14px', borderRadius: '999px', fontSize: '12px', cursor: 'pointer', border: filtro === f ? 'none' : '0.5px solid var(--orange-light)', background: filtro === f ? 'var(--orange-primary)' : 'var(--white)', color: filtro === f ? '#fff' : 'var(--orange-dark)' }}>
              {f.charAt(0).toUpperCase() + f.slice(1)}
            </button>
          ))}
        </div>

        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3,1fr)', gap: '14px' }}>
          {mascotasFiltradas.map(mascota => (
            <div key={mascota.id} style={{ background: 'var(--white)', border: '0.5px solid var(--orange-light)', borderRadius: '12px', padding: '14px' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '6px' }}>
                <span style={{ fontSize: '14px', fontWeight: '500', color: 'var(--text-primary)' }}>{mascota.nombre}</span>
                <span style={{ background: mascota.estado === 'perdida' ? 'var(--badge-lost-bg)' : 'var(--badge-found-bg)', color: mascota.estado === 'perdida' ? 'var(--badge-lost-text)' : 'var(--badge-found-text)', fontSize: '11px', padding: '2px 8px', borderRadius: '999px', fontWeight: '500' }}>
                  {mascota.estado.charAt(0).toUpperCase() + mascota.estado.slice(1)}
                </span>
              </div>
              <div style={{ fontSize: '12px', color: 'var(--orange-dark)', marginBottom: '2px' }}>{mascota.raza} · {mascota.color} · {mascota.tamano}</div>
              <div style={{ fontSize: '12px', color: 'var(--text-muted)', marginBottom: '8px' }}>{mascota.descripcion}</div>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <span style={{ fontSize: '11px', color: 'var(--text-muted)' }}>{new Date(mascota.fechaReporte).toLocaleDateString('es-CL')}</span>
                <button style={{ padding: '4px 10px', border: '0.5px solid var(--orange-light)', borderRadius: '8px', fontSize: '12px', background: 'var(--white)', color: 'var(--orange-dark)', cursor: 'pointer' }}>
                  Ver detalle
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default MascotasPage;
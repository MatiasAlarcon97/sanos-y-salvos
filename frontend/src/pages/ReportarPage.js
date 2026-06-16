import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { crearMascota } from '../services/mascotaService';
import { registrarGeolocalizacion } from '../services/geolocalizacionService';
import MapaPicker from '../components/MapaPicker';

function ReportarPage() {
  const navigate = useNavigate();
  const [reporteCreado, setReporteCreado] = useState(null);
  const [form, setForm] = useState({
    nombre: '',
    raza: '',
    color: '',
    tamano: 'Grande',
    descripcion: '',
    estado: 'perdida',
    contacto: '',
    fechaDesaparicion: '',
    latitud: null,
    longitud: null,
  });

  const handleChange = (e) => {
    setForm(prev => ({ ...prev, [e.target.name]: e.target.value }));
  };
  
  const handleLocationSelect = (lat, lng) => {
    setForm(prev => ({ ...prev, latitud: lat, longitud: lng }));
  };

  const handleSubmit = () => {
    const datos = {
      ...form,
      fechaDesaparicion: form.fechaDesaparicion ? `${form.fechaDesaparicion}T00:00:00` : null,
    };
    crearMascota(datos)
      .then(response => {
        const mascotaCreada = response.data;
        setReporteCreado(mascotaCreada);

        if (form.latitud && form.longitud) {
          registrarGeolocalizacion({
            reporteId: mascotaCreada.id,
            latitud: form.latitud,
            longitud: form.longitud,
            descripcion: form.descripcion || 'Ubicación del reporte'
          }).catch(error => console.error('Error al registrar geolocalización:', error));
        }
      })
      .catch(error => console.error('Error al crear mascota:', error));
  };

  const inputStyle = {
    width: '100%',
    padding: '8px 12px',
    border: '0.5px solid var(--orange-light)',
    borderRadius: '8px',
    fontSize: '13px',
    background: 'var(--white)',
    color: 'var(--text-primary)',
    outline: 'none',
  };

  const labelStyle = {
    fontSize: '12px',
    color: 'var(--text-secondary)',
    marginBottom: '5px',
    display: 'block',
  };
  if (reporteCreado) {
    return (
      <div style={{ padding: '60px 32px', display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '16px' }}>
        <div style={{ fontSize: '48px' }}>🐾</div>
        <h2 style={{ fontSize: '20px', fontWeight: '500', color: 'var(--text-primary)' }}>¡Reporte creado exitosamente!</h2>
        <p style={{ fontSize: '14px', color: 'var(--text-secondary)' }}>Guarda este ID para buscar tu reporte y ver coincidencias:</p>
        <div style={{ background: 'var(--white)', border: '0.5px solid var(--orange-light)', borderRadius: '8px', padding: '14px 24px', display: 'flex', alignItems: 'center', gap: '12px' }}>
          <span style={{ fontSize: '15px', fontWeight: '500', color: 'var(--orange-dark)', fontFamily: 'monospace' }}>{reporteCreado.id}</span>
          <button onClick={() => navigator.clipboard.writeText(reporteCreado.id)} style={{ padding: '4px 10px', border: '0.5px solid var(--orange-light)', borderRadius: '6px', fontSize: '12px', background: 'var(--white)', color: 'var(--orange-dark)', cursor: 'pointer' }}>
            Copiar
          </button>
        </div>
        <div style={{ display: 'flex', gap: '12px', marginTop: '8px' }}>
          <button onClick={() => navigate('/')} style={{ padding: '8px 20px', background: 'var(--orange-primary)', color: '#fff', border: 'none', borderRadius: '8px', fontSize: '13px', fontWeight: '500', cursor: 'pointer' }}>
            Ver todos los reportes
          </button>
          <button onClick={() => navigate(`/detalle/${reporteCreado.id}`)} style={{ padding: '8px 20px', background: 'var(--white)', color: 'var(--orange-dark)', border: '0.5px solid var(--orange-light)', borderRadius: '8px', fontSize: '13px', cursor: 'pointer' }}>
            Ver coincidencias
          </button>
        </div>
      </div>
    );
  }

  return (
    <div style={{ padding: '28px 32px' }}>
      <h2 style={{ fontSize: '20px', fontWeight: '500', color: 'var(--text-primary)', marginBottom: '24px' }}>
        Reportar mascota
      </h2>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '32px' }}>

        <div style={{ background: 'var(--white)', border: '0.5px solid var(--orange-light)', borderRadius: '12px', padding: '20px' }}>
          <h3 style={{ fontSize: '15px', fontWeight: '500', color: 'var(--orange-dark)', marginBottom: '16px', paddingBottom: '10px', borderBottom: '0.5px solid var(--orange-border)' }}>
            Información de la mascota
          </h3>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '12px', marginBottom: '12px' }}>
            <div>
              <label style={labelStyle}>Nombre</label>
              <input style={inputStyle} name="nombre" value={form.nombre} onChange={handleChange} placeholder="Ej: Firulais" />
            </div>
            <div>
              <label style={labelStyle}>Raza</label>
              <input style={inputStyle} name="raza" value={form.raza} onChange={handleChange} placeholder="Ej: Labrador" />
            </div>
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '12px', marginBottom: '12px' }}>
            <div>
              <label style={labelStyle}>Color</label>
              <input style={inputStyle} name="color" value={form.color} onChange={handleChange} placeholder="Ej: Amarillo" />
            </div>
            <div>
              <label style={labelStyle}>Tamaño</label>
              <select style={inputStyle} name="tamano" value={form.tamano} onChange={handleChange}>
                <option>Grande</option>
                <option>Mediano</option>
                <option>Pequeño</option>
              </select>
            </div>
          </div>

          <div style={{ marginBottom: '12px' }}>
            <label style={labelStyle}>Descripción</label>
            <textarea style={{ ...inputStyle, resize: 'none' }} name="descripcion" value={form.descripcion} onChange={handleChange} rows={3} placeholder="Detalles adicionales..." />
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '12px' }}>
            <div>
              <label style={labelStyle}>Estado</label>
              <select style={inputStyle} name="estado" value={form.estado} onChange={handleChange}>
                <option value="perdida">Perdida</option>
                <option value="encontrada">Encontrada</option>
              </select>
            </div>
            <div>
              <label style={labelStyle}>Teléfono de contacto</label>
              <input style={inputStyle} name="contacto" value={form.contacto} onChange={handleChange} placeholder="Ej: 912345678" />
            </div>
            <div style={{ marginTop: '12px' }}>
              <label style={labelStyle}>
                {form.estado === 'perdida' ? 'Fecha de desaparición' : 'Fecha de avistamiento'}
              </label>
              <input style={inputStyle} type="date" name="fechaDesaparicion" value={form.fechaDesaparicion} onChange={handleChange} />
            </div>
          </div>
        </div>

        <div style={{ background: 'var(--white)', border: '0.5px solid var(--orange-light)', borderRadius: '12px', padding: '20px' }}>
          <h3 style={{ fontSize: '15px', fontWeight: '500', color: 'var(--orange-dark)', marginBottom: '16px', paddingBottom: '10px', borderBottom: '0.5px solid var(--orange-border)' }}>
            Ubicación del avistamiento
          </h3>

          <MapaPicker onLocationSelect={handleLocationSelect} />

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '12px', marginBottom: '16px' }}>
            <div>
              <label style={labelStyle}>Latitud</label>
              <input style={inputStyle} value={form.latitud ? form.latitud.toFixed(6) : ''} placeholder="-33.4489" readOnly />
            </div>
            <div>
              <label style={labelStyle}>Longitud</label>
              <input style={inputStyle} value={form.longitud ? form.longitud.toFixed(6) : ''} placeholder="-70.6693" readOnly />
            </div>
          </div>

          <button onClick={handleSubmit} style={{ width: '100%', background: 'var(--orange-primary)', color: '#fff', border: 'none', padding: '10px', borderRadius: '8px', fontSize: '13px', fontWeight: '500', cursor: 'pointer' }}>
            ✓ Guardar reporte
          </button>
        </div>

      </div>
    </div>
  );
}

export default ReportarPage;
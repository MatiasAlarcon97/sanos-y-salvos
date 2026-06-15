import React from 'react';
import { Link, useLocation } from 'react-router-dom';

function Navbar({ darkMode, toggleTheme }) {
  const location = useLocation();

  const navStyle = {
    background: 'var(--orange-primary)',
    padding: '0 32px',
    height: '56px',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
  };

  const brandStyle = {
    color: '#fff',
    fontSize: '18px',
    fontWeight: '500',
    display: 'flex',
    alignItems: 'center',
    gap: '8px',
    textDecoration: 'none',
  };

  const linkStyle = (path) => ({
    color: location.pathname === path ? '#fff' : 'rgba(255,255,255,0.8)',
    fontSize: '13px',
    padding: '6px 16px',
    borderRadius: '8px',
    textDecoration: 'none',
    background: location.pathname === path ? 'rgba(255,255,255,0.2)' : 'transparent',
  });

  const themeBtn = {
    background: 'rgba(255,255,255,0.2)',
    border: 'none',
    color: '#fff',
    padding: '6px 12px',
    borderRadius: '8px',
    cursor: 'pointer',
    fontSize: '14px',
  };

  return (
    <nav style={navStyle}>
      <Link to="/" style={brandStyle}>
        🐾 Sanos y Salvos
      </Link>
      <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
        <Link to="/" style={linkStyle('/')}>Mascotas</Link>
        <Link to="/coincidencias" style={linkStyle('/coincidencias')}>Coincidencias</Link>
        <button onClick={toggleTheme} style={themeBtn}>
          {darkMode ? '☀' : '☾'}
        </button>
      </div>
    </nav>
  );
}

export default Navbar;
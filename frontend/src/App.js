import React, { useState } from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import MascotasPage from './pages/MascotasPage';
import ReportarPage from './pages/ReportarPage';
import CoincidenciasPage from './pages/CoincidenciasPage';
import './App.css';

function App() {
  const [darkMode, setDarkMode] = useState(false);

  const toggleTheme = () => {
    setDarkMode(!darkMode);
    document.documentElement.setAttribute('data-theme', darkMode ? 'light' : 'dark');
  };

  return (
    <BrowserRouter>
      <div style={{ minHeight: '100vh', background: 'var(--orange-bg)' }}>
        <Navbar darkMode={darkMode} toggleTheme={toggleTheme} />
        <Routes>
          <Route path="/" element={<MascotasPage />} />
          <Route path="/reportar" element={<ReportarPage />} />
          <Route path="/coincidencias" element={<CoincidenciasPage />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;

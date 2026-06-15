import React, { useEffect, useRef } from 'react';

function MapaPicker({ onLocationSelect }) {
  const mapRef = useRef(null);
  const mapInstanceRef = useRef(null);
  const markerRef = useRef(null);

  useEffect(() => {
    if (mapInstanceRef.current) return;

    const initMap = () => {
      const L = window.L;
      if (!L || !mapRef.current) return;

      const defaultPos = [-33.4489, -70.6693];
      const map = L.map(mapRef.current).setView(defaultPos, 13);
      mapInstanceRef.current = map;

      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap'
      }).addTo(map);

      map.on('click', function(e) {
        const { lat, lng } = e.latlng;
        if (markerRef.current) {
          markerRef.current.setLatLng([lat, lng]);
        } else {
          markerRef.current = L.marker([lat, lng]).addTo(map);
        }
        onLocationSelect(lat, lng);
      });

      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
          (pos) => {
            map.setView([pos.coords.latitude, pos.coords.longitude], 13);
          },
          () => {
            map.setView(defaultPos, 13);
          }
        );
      }
    };

    if (window.L) {
      initMap();
    } else {
      window.addEventListener('load', initMap);
    }

    return () => {
      if (mapInstanceRef.current) {
        mapInstanceRef.current.remove();
        mapInstanceRef.current = null;
      }
      window.removeEventListener('load', initMap);
    };
  }, []);

  return (
    <div
      ref={mapRef}
      style={{ borderRadius: '8px', overflow: 'hidden', border: '0.5px solid var(--orange-light)', height: '220px' }}
    />
  );
}

export default MapaPicker;
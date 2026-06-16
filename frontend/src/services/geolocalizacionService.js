import axios from 'axios';

const API_URL = 'http://localhost:8080/geoloc';

export const registrarGeolocalizacion = (geolocalizacion) => {
    return axios.post(API_URL, geolocalizacion);
};

export const obtenerGeolocalizacion = (reporteId) => {
    return axios.get(`${API_URL}/${reporteId}`);
};

export const listarGeolocalizaciones = () => {
    return axios.get(API_URL);
};
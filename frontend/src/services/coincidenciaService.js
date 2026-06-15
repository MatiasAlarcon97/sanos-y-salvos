import axios from 'axios';

const API_URL = 'http://localhost:8080/coincidencias';

export const listarCoincidencias = () => {
    return axios.get(API_URL);
};

export const obtenerCoincidencia = (id) => {
    return axios.get(`${API_URL}/${id}`);
};

export const crearCoincidencia = (coincidencia) => {
    return axios.post(API_URL, coincidencia);
};

export const actualizarCoincidencia = (id, coincidencia) => {
    return axios.put(`${API_URL}/${id}`, coincidencia);
};
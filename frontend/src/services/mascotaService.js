import axios from 'axios';

const API_URL = 'http://localhost:8080/mascotas';

export const listarMascotas = () => {
    return axios.get(API_URL);
};

export const obtenerMascota = (id) => {
    return axios.get(`${API_URL}/${id}`);
};

export const crearMascota = (mascota) => {
    return axios.post(API_URL, mascota);
};

export const actualizarMascota = (id, mascota) => {
    return axios.put(`${API_URL}/${id}`, mascota);
};
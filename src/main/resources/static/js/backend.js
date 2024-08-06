const BASE_URL = 'http://localhost:8080/resort';

async function fetchData(url, options = {}) {
    try {
        const response = await fetch(url, options);
        if (!response.ok) {
            const errorData = await response.json();
            //throw new Error(errorData.message || 'Erro ao processar requisição' );
            throw Array.isArray(errorData) ? errorData : new Error(errorData.message || 'Erro ao processar requisição');

        }
        return await response.json();
    } catch (error) {
        console.error('Erro:', error);
        throw error;
    }
}

async function handleAction(url, method) {
    try {
        const response = await fetch(url, { method });
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || `Erro ao realizar a ação:`);
        }
        return response;
    } catch (error) {
        console.error('Erro:', error);
        throw error;
    }
}

async function getReservasByFiltroId(filtroId) {
    return await fetchData(`${BASE_URL}/reserva/filtro/${filtroId}`);
}

async function getReservasByHospede(hospede) {
    return await fetchData(`${BASE_URL}/reserva/hospede/${hospede}`);
}

async function getReservasByCheckin(checkin) {
    return await fetchData(`${BASE_URL}/reserva/checkin/${checkin}`);
}

async function checkinReserva(reservaId) {
    return await handleAction(`${BASE_URL}/reserva/${reservaId}/checkin`, 'PUT');
}

async function checkoutReserva(reservaId) {
    return await handleAction(`${BASE_URL}/reserva/${reservaId}/checkout`, 'PUT');
}

async function cancelarReserva(reservaId) {
    return await handleAction(`${BASE_URL}/reserva/${reservaId}`, 'DELETE');
}

async function getQuartosDisponiveis(dataEntrada, dataSaida) {
    return await fetchData(`${BASE_URL}/quarto/ocupacao/inicio/${dataEntrada}/fim/${dataSaida}`);
}

async function getHospedeById(hospedeId) {
    return await fetchData(`${BASE_URL}/hospede/${hospedeId}`);
}


async function getHospedeById(hospedeId) {
    return await fetchData(`${BASE_URL}/hospede/${hospedeId}`);
}

async function getListQuartosDisponiveis(dataEntrada, dataSaida){
    return await fetchData(`${BASE_URL}/quarto/ocupacao/inicio/${dataEntrada}/fim/${dataSaida}`);
}

async function createReserva(reserva) {
    console.log('a salvar :'+reserva)
    const url = `${BASE_URL}/reserva`;
    const options = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(reserva)
    };
    console.log('conteudo:'+options)
    return await fetchData(url, options);
  }

async function createHandler(uri, payload){
    const url = `${BASE_URL}/${uri}`;
    const options = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(payload)
    };
    return await fetchData(url, options);
}

async function updateHandler(uri, payload,id){
    const url = `${BASE_URL}/${uri}/${id}`;
    const options = {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(payload)
    };
    return await fetchData(url, options);
}

async function deleteHandler(uri, id){
    const url = `${BASE_URL}/${uri}/${id}`;
    return await handleAction(`${BASE_URL}/${uri}/${id}`, 'DELETE');
}

async function getResourceHandler(uri, id){
    return await fetchData(`${BASE_URL}/${uri}/${id}`);
}


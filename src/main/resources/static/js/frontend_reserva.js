document.addEventListener('DOMContentLoaded', function() {
    const BASE_URL = 'http://localhost:8080/resort';
    let selectedReservaId = null;
    const dataEntradaInput = document.getElementById('dataEntrada');
    const dataSaidaInput = document.getElementById('dataSaida');
    const idReservaFiltroInput = document.getElementById('idReservaFiltro');
    const hospedeReservaFiltroInput = document.getElementById('hospedeFiltro');
    const checkinReservaFiltroInput = document.getElementById('dataCheckinFiltro');
    const idQuartoInput = document.getElementById('idQuarto');
    const valorTotalInput = document.getElementById('valorTotal');
    const listaQuartoSelect = document.getElementById('listaQuarto');
    const cpfColumns = document.querySelectorAll('.cpf-column');
    

    cpfColumns.forEach(function (column) {
        const cpf = column.textContent;
        column.textContent = formatarCPF(cpf);
        console.log(column);
    });

    document.getElementById('tabelaReservas').addEventListener('click', function(event) {
        const target = event.target;
        if (target.classList.contains('checkin')) {
            const row = target.closest('tr');
            const status = row.querySelector('td:nth-child(7)').textContent.trim();
            if (status === 'PENDENTE') {
                selectedReservaId = row.querySelector('td:nth-child(1)').textContent.trim();
                document.getElementById('checkinReservaId').textContent = selectedReservaId;
                $('#checkinModal').modal('show');
            } else {
                mostrarMensagemErro({message:'Check-in só pode ser feito se a reserva estiver pendente.'});
            }
        } else if (target.classList.contains('checkout')) {
            const row = target.closest('tr');
            const status = row.querySelector('td:nth-child(7)').textContent.trim();
            if (status === 'ABERTO') {
                selectedReservaId = row.querySelector('td:nth-child(1)').textContent.trim();
                document.getElementById('checkoutReservaId').textContent = selectedReservaId;
                $('#checkoutModal').modal('show');
            } else {
                mostrarMensagemErro({message:'Check-out só pode ser feito se a reserva estiver aberta.'});
            }
        } else if (target.classList.contains('cancelar')) {
            const row = target.closest('tr');
            selectedReservaId = row.querySelector('td:nth-child(1)').textContent.trim();
            document.getElementById('cancelarReservaId').textContent = selectedReservaId;
            $('#cancelarModal').modal('show');
        }
    });

   
    document.getElementById('filtroButton').addEventListener('click', async function() {
        const filtroId = idReservaFiltroInput.value.trim();
        if (filtroId && filtroId.length>0) {
            try {

                const reservas = await getReservasByFiltroId(filtroId);
                console.log(reservas);
                atualizarTabelaReservas(reservas);
            } catch (error) {
                mostrarMensagemErro(error)
            }
        }
        else {
            let url = `${BASE_URL}/reserva`;
            window.location.href = url;
        }
    });

    document.getElementById('filtroHospedeButton').addEventListener('click', async function() {
        const hospede = hospedeReservaFiltroInput.value.trim();
        if (hospede && hospede.length>0) {
            try {
                const reservas = await getReservasByHospede(hospede);
                atualizarTabelaReservas(reservas);
            } catch (error) {
                mostrarMensagemErro(error)
            }
        }
        else {
            let url = `${BASE_URL}/reserva`;
            window.location.href = url;
        }
    });

    document.getElementById('filtroCheckinButton').addEventListener('click', async function() {
        const checkin = checkinReservaFiltroInput.value.trim();
        if (checkin && checkin.length >0) {
            try {
                const reservas = await getReservasByCheckin(checkin);
                atualizarTabelaReservas(reservas);
            } catch (error) {
                mostrarMensagemErro(error)
            }
        }
        else {
            let url = `${BASE_URL}/reserva`;
            window.location.href = url;
        }
    });

    /*Obter informações de 1 hospede por ID e popupar campo nome Hospede*/
    const nomeHospedeInput = document.getElementById('nomeHospede');
    document.getElementById('idHospede').addEventListener('blur', async function() {
        const hospedeId = this.value;
        if (hospedeId) {
           const hospede = await getHospedeById(hospedeId);
           nomeHospede.value = hospede.nome;
        } else {
            nomeHospedeInput.value = "";
        }
    });

    /*Obtendo lista de quartos disponiveis e populando em select do formulario de registro de reserva*/
    document.getElementById('dataSaida').addEventListener('blur', async function(){
        const dataEntrada = dataEntradaInput.value;
        const dataSaida = this.value;

        if (dataEntrada && dataSaida) {
             const quartos = await getListQuartosDisponiveis(dataEntrada,dataSaida);
             document.getElementById('listaQuarto').innerHTML = '<option value="">Selecione um quarto</option>'; // Clear the select options
             quartos.forEach(quarto => {
                       const option = document.createElement('option');
                       option.value = JSON.stringify(quarto); // Store the whole quarto object as a JSON string
                       option.textContent = quarto.nome;
                       document.getElementById('listaQuarto').appendChild(option);
             });

        }
    })

    document.getElementById('listaQuarto').addEventListener('change', function() {
                const selectedQuarto = JSON.parse(this.value); // Parse the JSON string back to an object
                const valorDiaria = selectedQuarto.valorDia; // Get the valorDiaria

                const dataEntrada = new Date(dataEntradaInput.value);
                const dataSaida = new Date(dataSaidaInput.value);

                if (dataEntrada && dataSaida) {
                    const timeDiff = Math.abs(dataSaida.getTime() - dataEntrada.getTime());
                    const diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));

                    const valorTotal = valorDiaria * diffDays;

                    valorTotalInput.value = valorTotal.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
                }

                idQuartoInput.value = selectedQuarto.id; // Armazena o ID do quarto selecionado
    });
    
    document.querySelectorAll('.registrar').forEach(button => {    
        button.addEventListener('click', function() {
            salvarFormulario();
            limparFormulario();
            $('#novaReservaModal').modal('hide');
        });
    });
    
    // document.querySelectorAll('.checkin').forEach(button => {
    //     button.addEventListener('click', function() {
    //         const row = this.closest('tr');
    //         const status = row.querySelector('td:nth-child(7)').textContent.trim();
    //         if (status === 'PENDENTE') {
    //             selectedReservaId = row.querySelector('td:nth-child(1)').textContent.trim();
    //             document.getElementById('checkinReservaId').textContent = selectedReservaId;
    //             $('#checkinModal').modal('show');
    //         } else {
    //             alert('Check-in só pode ser feito se a reserva estiver pendente.');
    //         }
    //     });
    // });

    document.getElementById('confirmCheckin').addEventListener('click', async function() {
        if (selectedReservaId) {
            try {
                await checkinReserva(selectedReservaId);
                //alert('Check-in realizado com sucesso!');
                atualizarStatusReserva(selectedReservaId, 'ABERTO');
                $('#checkinModal').modal('hide');
            } catch (error) {
                mostrarMensagemErro(error)
            }
        }
    });

    // document.querySelectorAll('.checkout').forEach(button => {
    //     button.addEventListener('click', function() {
    //         const row = this.closest('tr');
    //         const status = row.querySelector('td:nth-child(7)').textContent.trim();
    //         if (status === 'ABERTO') {
    //             selectedReservaId = row.querySelector('td:nth-child(1)').textContent.trim();
    //             document.getElementById('checkoutReservaId').textContent = selectedReservaId;
    //             $('#checkoutModal').modal('show');
    //         } else {
    //             alert('Check-out só pode ser feito se a reserva estiver aberta.');
    //         }
    //     });
    // });

    document.getElementById('confirmCheckout').addEventListener('click', async function() {
        if (selectedReservaId) {
            try {
                await checkoutReserva(selectedReservaId);
                //alert('Check-out realizado com sucesso!');
                atualizarStatusReserva(selectedReservaId, 'FECHADO');
                $('#checkoutModal').modal('hide');
            } catch (error) {
                mostrarMensagemErro(error)
            }
        }
    });

    document.querySelectorAll('.cancelar').forEach(button => {
        button.addEventListener('click', function() {
            const row = this.closest('tr');
            selectedReservaId = row.querySelector('td:nth-child(1)').textContent.trim();
            document.getElementById('cancelarReservaId').textContent = selectedReservaId;
            $('#cancelarModal').modal('show');
        });
    });

    document.getElementById('confirmCancelar').addEventListener('click', async function() {
        if (selectedReservaId) {
            try {
                await cancelarReserva(selectedReservaId);
                //alert('Reserva cancelada com sucesso!');
                removerReservaTabela(selectedReservaId);
                $('#cancelarModal').modal('hide');
            } catch (error) {
                mostrarMensagemErro(error)
            }
        }
    });

    function atualizarTabelaReservas(reservas) {
        const tabela = document.getElementById('tabelaReservas').getElementsByTagName('tbody')[0];
        tabela.innerHTML = '';  // Limpa a tabela

        reservas.forEach(reserva => {
            const novaLinha = tabela.insertRow();
            novaLinha.innerHTML = `
                <td>${reserva.id}</td>
                <td data-hospede-id="${reserva.idHospede}"> ${reserva.nomeHospede}</td>
                <td >${formatarCPF(reserva.cpfHospede)}</td>
                <td  data-quarto-id="${reserva.idQuarto}">${reserva.nomeQuarto}</td>
                <td >${formatarDataToPtBr(reserva.dataEntrada)}</td>
                <td >${formatarDataToPtBr(reserva.dataSaida)}</td>
                <td >${reserva.status}</td>
                <td >${reserva.valorTotal.toLocaleString('pt-br',{style: 'currency', currency: 'BRL'})}</td>
                <td class="d-flex justify-content-between">
                    <button class="btn btn-primary checkin">CHECK-IN</button>
                    <button class="btn btn-success checkout">CHECK-OUT</button>
                    <button class="btn btn-danger cancelar">CANCELAR</button>
                </td>
            `;
        });
    }

    function adicionarEmTabelaReservas(reserva) {
        const tabela = document.getElementById('tabelaReservas').getElementsByTagName('tbody')[0];
        const novaLinha = tabela.insertRow();
        novaLinha.innerHTML = `
                <td>${reserva.id}</td>
                <td data-hospede-id="${reserva.idHospede}"> ${reserva.nomeHospede}</td>
                <td >${formatarCPF(reserva.cpfHospede)}</td>
                <td  data-quarto-id="${reserva.idQuarto}">${reserva.nomeQuarto}</td>
                <td >${formatarDataToPtBr(reserva.dataEntrada)}</td>
                <td >${formatarDataToPtBr(reserva.dataSaida)}</td>
                <td >${reserva.status}</td>
                <td >${reserva.valorTotal.toLocaleString('pt-br',{style: 'currency', currency: 'BRL'})}</td>
                <td class="d-flex justify-content-between">
                    <button class="btn btn-primary checkin">CHECK-IN</button>
                    <button class="btn btn-success checkout">CHECK-OUT</button>
                    <button class="btn btn-danger cancelar">CANCELAR</button>
                </td>
        `;
        
    }

    function atualizarStatusReserva(reservaId, novoStatus) {
        const tabela = document.getElementById('tabelaReservas').getElementsByTagName('tbody')[0];
        const linhas = tabela.getElementsByTagName('tr');

        for (let i = 0; i < linhas.length; i++) {
            const celulaId = linhas[i].getElementsByTagName('td')[0];
            if (celulaId && celulaId.textContent.trim() === reservaId) {
                const celulaStatus = linhas[i].getElementsByTagName('td')[6];
                if (celulaStatus) {
                    celulaStatus.textContent = novoStatus;
                }
                break;
            }
        }
    }

    function removerReservaTabela(reservaId) {
        const tabela = document.getElementById('tabelaReservas').getElementsByTagName('tbody')[0];
        const linhas = tabela.getElementsByTagName('tr');

        for (let i = 0; i < linhas.length; i++) {
            const celulaId = linhas[i].getElementsByTagName('td')[0];
            if (celulaId && celulaId.textContent.trim() === reservaId) {
                tabela.deleteRow(i);
                break;
            }
        }
    }

    function limparFormulario() {
        document.getElementById('id').value = '';
        document.getElementById('idHospede').value = '';
        document.getElementById('nomeHospede').value = '';
        document.getElementById('dataEntrada').value = '';
        document.getElementById('dataSaida').value = '';
        document.getElementById('listaQuarto').innerHTML = '';
        document.getElementById('idQuarto').value = '';
        document.getElementById('valorTotal').value='';
    }

    async function salvarFormulario(){
        
        const id = document.getElementById('id').value
        const idHospede = document.getElementById('idHospede').value ;
        const dataEntrada = document.getElementById('dataEntrada').value ;
        const dataSaida = document.getElementById('dataSaida').value ;
        const idQuarto = document.getElementById('idQuarto').value; 
        let reservaResponse = {}
        
        try{
             reservaResponse = await createReserva({id,idHospede,dataEntrada,dataSaida,idQuarto});
             adicionarEmTabelaReservas(reservaResponse)
             console.log(reservaResponse)
        }
        catch(error){
            //console.log(error.message);
            //alert('um erro ocorreu :'+error.message);
            mostrarMensagemErro(error)
        }
    }

    // Função para mostrar mensagens de erro
    function mostrarMensagemErro(error) {

        // Verifica se o erro é um array de mensagens
        if (Array.isArray(error)) {
            messages = error.map(err => err.message);
            error.map(err => console.log(`<p>${err.message}</p>`));
            areaMensagem = document.getElementById('erroModalMensagem')
            areaMensagem.innerHTML = ''; // Limpa o conteúdo atual do modal

            messages.forEach(message => {
                const p = document.createElement('p');
                p.textContent = message;
                areaMensagem.appendChild(p);
            });
        }
        else {
            let mensagemErro = error.message || 'Ocorreu um erro desconhecido';
            document.getElementById('erroModalMensagem').textContent = mensagemErro;
        }
        $('#erroModal').modal('show');
    }

    $('#novaReservaModal').on('hidden.bs.modal', limparFormulario);
    
});
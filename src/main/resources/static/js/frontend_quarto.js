document.addEventListener('DOMContentLoaded', function () {
    const BASE_URL = 'http://localhost:8080/resort';
    const formulario = document.getElementById('formularioQuarto');
    const botaoLimparCampos = document.querySelector('.clean-field');
    const statusFiltroInput = document.getElementById('statusFiltro');
    const quantidadeQuartosInput = document.getElementById('quantidadeQuartos');
    const vistaFiltroInput = document.getElementById('vistaFiltro');
    const filtroButton = document.getElementById('filtroButton');
    const tabelaQuartos = document.getElementById('tabelaQuartos');
    const deleteButton = document.querySelector('.btn-danger');
    const salvarButton = document.querySelector('.salvarButton');
    const valorDiaInput = document.getElementById('valorDia')

    // Função para limpar todos os campos do formulário
    function limparCamposFormulario() {
        formulario.reset(); // Limpa todos os campos do formulário
        // Limpa manualmente os campos ocultos, se necessário
        formulario.querySelectorAll('input[type="hidden"]').forEach(input => {
            input.value = '';
        });
        // Remove a classe 'selected' de qualquer linha selecionada
        document.querySelectorAll('tr.selected').forEach(row => {
            row.classList.remove('selected');
        });
    }

    // Função para filtrar quartos
    function filtrarQuartos() {
        const status = statusFiltroInput.value.trim();
        const quantidade = quantidadeQuartosInput.value.trim();
        let vista = null;
        if (vistaFiltroInput.value === 'vistaMar') vista = true;
        else if (vistaFiltroInput.value === 'semVistaMar') vista = false;

        const filtroData = {
            status: status !== 'todos' ? status : null,
            quantidadeQuartos: quantidade ? parseInt(quantidade) : null,
            vistaMar: vista
        };

        fetch(`${BASE_URL}/quarto/filtro`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(filtroData)
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error('Erro na filtragem dos dados');
                }
            })
            .then(data => {
                // Limpar a tabela antes de adicionar os novos dados
                const tabela = tabelaQuartos.getElementsByTagName('tbody')[0];
                tabela.innerHTML = '';

                // Adicionar os novos dados à tabela
                data.forEach(quarto => {
                    const newRow = tabela.insertRow();

                    newRow.innerHTML = `
                    <td>${quarto.id}</td>
                    <td>${quarto.nome}</td>
                    <td>${quarto.descricao}</td>
                    <td>${quarto.vistaMar ? 'Sim' : 'Não'}</td>
                    <td>${quarto.valorDia}</td>
                    <td>${quarto.capacidade}</td>
                    <td>${quarto.status == 'DISPONIVEL' ? 'DISPONÍVEL' : quarto.status}</td>
                `;
                });
            })
            .catch(error => {
                console.error('Erro:', error);
            });
    }

    // Função para preencher o formulário com dados da tabela
    function preencherFormulario(event) {
        const linha = event.target.closest('tr');
        
        if (!linha || linha.parentElement.tagName !== 'TBODY') return;
        const celulas = linha.querySelectorAll('td');
        
        if (celulas.length < 7) return;
        const [id, nome, descricao, vistaMar, valorDia, capacidade, status] = celulas;
        let valorDia_ = valorDia.textContent.replace('R$','').replace('.','').replace(',','.').trim();
        valorDia_ = Number(valorDia_) ;
        
        if (vistaMar.textContent.trim().toLowerCase() === 'sim'){
            valorDia_ = Number(valorDia_) / 2;
        }
        valorDia_ = formataMoedaEmTabela(valorDia_).replace('R$','');
        
        document.getElementById('id').value = id.textContent.trim();
        document.getElementById('nome').value = nome.textContent.trim();
        document.getElementById('descricao').value = descricao.textContent.trim();
        document.getElementById('vistaMar').checked = vistaMar.textContent.trim().toLowerCase() === 'sim';
        document.getElementById('valorDia').value = valorDia_;
        document.getElementById('capacidade').value = capacidade.textContent.trim();
        document.querySelectorAll('tr').forEach(row => row.classList.remove('selected'));
        
        linha.classList.add('selected');
    }

    // Função para excluir um quarto
    async function excluirQuarto() {
        const id = document.getElementById('id').value;
        if (id) {
            try {
                await deleteHandler('quarto', id);
                const selectedRow = document.querySelector('.selected');
                if (selectedRow) {
                    selectedRow.parentNode.removeChild(selectedRow);
                }
                limparCamposFormulario();
            } catch (error) {
                console.error('Erro ao tentar remover quarto:', error);
                mostrarMensagemErro({ message: 'Erro ao tentar remover quarto.' });
            }
        } else {
            mostrarMensagemErro({ message: 'Selecione um quarto para excluir!' });
        }
    }

    // Função para salvar um quarto
    async function salvarQuarto() {
        const id = document.getElementById('id').value.trim();
        const nome = document.getElementById('nome').value.trim();
        const descricao = document.getElementById('descricao').value.trim();
        const valorDia = convertCurrencyToBigdecimal(document.getElementById('valorDia').value.trim());
        const capacidade = document.getElementById('capacidade').value.trim();
        const vistaMar = document.getElementById('vistaMar').checked;
        
        const payloadUpdate = { id, nome, descricao, valorDia, capacidade, vistaMar };
        const payloadCreate = { nome, descricao, valorDia, capacidade, vistaMar };

        if (id && id.length > 0) {
            try {
                const quartoUpdate = await updateHandler('quarto', payloadUpdate, id);
                atualizarTabela(quartoUpdate);
                limparCamposFormulario();
            } catch (error) {
                console.error('Erro ao atualizar quarto:', error);
                mostrarMensagemErro(error);
            }
        } else {
            try {
                console.log(payloadCreate)
                const quartoCreate = await createHandler('quarto', payloadCreate);
                atualizarTabela(quartoCreate);
                limparCamposFormulario();
            } catch (error) {
                console.error('Erro ao criar quarto:', error);
                mostrarMensagemErro(error);
            }
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

    // Função para atualizar a tabela após operações CRUD
    function atualizarTabela(quarto) {
        const tabela = document.getElementById('tabelaQuartos').getElementsByTagName('tbody')[0];
        let linhaExistente = null;

        // Verifica se já existe uma linha com o mesmo ID
        Array.from(tabela.rows).forEach(row => {
            if (row.cells[0].textContent.trim() == quarto.id) {
                linhaExistente = row;
            }
        });

        if (linhaExistente) {
            // Atualiza a linha existente
            linhaExistente.cells[1].textContent = quarto.nome;
            linhaExistente.cells[2].textContent = quarto.descricao;
            linhaExistente.cells[3].textContent = quarto.vistaMar ? 'Sim' : 'Não';
            linhaExistente.cells[4].textContent = formataMoedaEmTabela(quarto.valorDia);
            linhaExistente.cells[5].textContent = quarto.capacidade;
            linhaExistente.cells[6].textContent = quarto.status == 'DISPONIVEL' ? 'DISPONÍVEL' : quarto.status;
        } else {
            // Adiciona uma nova linha
            const novaLinha = tabela.insertRow();
            novaLinha.innerHTML = `
                <td>${quarto.id}</td>
                <td>${quarto.nome}</td>
                <td>${quarto.descricao}</td>
                <td>${quarto.vistaMar ? 'Sim' : 'Não'}</td>
                <td>${formataMoedaEmTabela(quarto.valorDia)}</td>
                <td>${quarto.capacidade}</td>
                <td>${quarto.status == 'DISPONIVEL' ? 'DISPONÍVEL' : quarto.status}</td>
            `;
        }
    }

    function formataMoedaEmTabela(valor_){
        if (valor_){
            let valor = (valor_ ).toFixed(2) + '';    
            valor = valor.replace('.', ',');
            valor = valor.replace(/\B(?=(\d{3})+(?!\d))/g, '.');
            return 'R$'+valor;
        }
        return valor_
    }

    function formatarMoeda() {
        

        let valor = document.getElementById('valorDia').value.replace(/\D/g, '').replace('R$','');

        // Adiciona as casas decimais
        valor = (valor / 100).toFixed(2) + '';

        // Adiciona os pontos e vírgulas
        valor = valor.replace('.', ',');
        valor = valor.replace(/\B(?=(\d{3})+(?!\d))/g, '.');

        // Define o valor formatado no input
        document.getElementById('valorDia').value = valor;
        
    }

    function convertCurrencyToBigdecimal(value){
        return value.replace('.','').replace(',','.');
    }


    // Adiciona eventos aos elementos da página
    botaoLimparCampos.addEventListener('click', limparCamposFormulario);
    filtroButton.addEventListener('click', filtrarQuartos);
    tabelaQuartos.addEventListener('click', preencherFormulario);
    deleteButton.addEventListener('click', excluirQuarto);
    salvarButton.addEventListener('click', salvarQuarto);
    valorDiaInput.addEventListener('keyup',formatarMoeda);
    valorDiaInput.addEventListener('change',formatarMoeda);
});

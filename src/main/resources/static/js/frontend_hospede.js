document.addEventListener('DOMContentLoaded', function() {
    const BASE_URL = 'http://localhost:8080/resort';
    const formulario = document.getElementById('formulariohospede');
    const botaoLimparCampos = document.querySelector('.clean-field');
    const idHospedeFiltroInput = document.getElementById('idHospedeFiltro');
    const filtroButton = document.getElementById('filtroButton');
    const tabelaHospedes = document.getElementById('tabelahospedes');
    const deleteButton = document.querySelector('.btn-danger');
    const salvarButton = document.querySelector('.salvarButton');
    const cpfInput = document.getElementById('cpf');
    const rgInput = document.getElementById('rg');
    const telefoneInput = document.getElementById('telefone');
    const cpfColumns = document.querySelectorAll('.cpf-column');
    const rgColumns = document.querySelectorAll('.rg-column');
    const telefoneColumns = document.querySelectorAll('.telefone-column');
    

    cpfColumns.forEach(function (column) {
        const cpf_ = column.textContent.trim().replace(/\D/g, '');
        console.log(cpf_);
        column.textContent = formatarCPF(cpf_);
    });

    rgColumns.forEach(function (column) {
        const rg_ = column.textContent.trim().replace(/\D/g, '');
        console.log(rg_);
        column.textContent = formatarRG(rg_);
    });

    telefoneColumns.forEach(function (column) {
        const telefone_ = column.textContent.trim().replace(/\D/g, '');
        console.log(telefone_);
        column.textContent = formatarTelefone(telefone_);
    });

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

    // Função para converter data para o formato de input
    function convertDateToInputFormat(dateString) {
        const [dia, mes, ano] = dateString.split('/');
        return `${ano}-${mes}-${dia}`;
    }

    // Função para filtrar hospedes por ID
    function filtrarHospedes() {
        const filtroId = idHospedeFiltroInput.value.trim();
        let url = filtroId ? `${BASE_URL}/hospede/${filtroId}/filtro` : `${BASE_URL}/hospede`;
        window.location.href = url;
    }

    // Função para preencher o formulário com dados da tabela
    function preencherFormulario(event) {
        const linha = event.target.closest('tr');
        if (!linha || linha.parentElement.tagName !== 'TBODY') return;
        const celulas = linha.querySelectorAll('td');
        if (celulas.length < 6) return;
        const [id, nome, telefone, cpf, rg, dataNascimento] = celulas;
        document.getElementById('id').value = id.textContent.trim();
        document.getElementById('nome').value = nome.textContent.trim();
        document.getElementById('telefone').value = telefone.textContent;
        document.getElementById('cpf').value = cpf.textContent;
        document.getElementById('rg').value = rg.textContent;
        document.getElementById('dataNascimento').value = convertDateToInputFormat(dataNascimento.textContent.trim());
        document.querySelectorAll('tr').forEach(row => row.classList.remove('selected'));
        linha.classList.add('selected');
    }

    // Função para excluir um hospede
    async function excluirHospede() {
        const id = document.getElementById('id').value;
        if (id) {
            try{
                await deleteHandler('hospede',id);
                const selectedRow = document.querySelector('.selected');
                if (selectedRow) {
                    selectedRow.parentNode.removeChild(selectedRow);
                }
                limparCamposFormulario();
            }
            catch(error){
                if (error) mostrarMensagemErro(error);
                else mostrarMensagemErro({message:'Erro ao tentar remover hóspede.'})
            }
           
        } else {
            mostrarMensagemErro({message:'Selecione um hospede para excluir!'});
        }
    }

    async function salvarHospede(){
        const id = document.getElementById('id').value.trim();
        const nome = document.getElementById('nome').value.trim();
        const telefone = document.getElementById('telefone').value.trim().replace(/\D/g, '');
        const cpf = document.getElementById('cpf').value.trim().replace(/\D/g, '');
        const rg = document.getElementById('rg').value.trim().replace(/\D/g, '');
        const dataNascimento = document.getElementById('dataNascimento').value.trim();
        
        const payloadUpdate = {id,nome,telefone,cpf, rg, dataNascimento};
        const paylodCreate = {nome,telefone,cpf, rg, dataNascimento};
        
        if (id && id.length>0){
            try {

                const hospedeUpdate = await updateHandler('hospede',payloadUpdate,id);
                atualizarTabela(hospedeUpdate);
                limparCamposFormulario();

            } catch(error){
                mostrarMensagemErro(error);
            }
        }
        else {
            try {
                const hospedeCreate = await createHandler('hospede',paylodCreate);
                atualizarTabela(hospedeCreate);
                limparCamposFormulario();
            }
            catch (error){
                mostrarMensagemErro(error);
            }
        }
    }
  

    function formatarInputCPF(input) {
        let valor = input.value.replace(/\D/g, '');
        valor = formatarCPF(valor)
        input.value = valor;
    }
  

    function formatarInputRG(input) {
        let valor = input.value.replace(/\D/g, '');
        input.value = formatarRG(valor);
    }

    function formatarInputTelefone(input) {
        // Remove tudo que não é dígito
        let valor = input.value.replace(/\D/g, '');
        input.value = formatarTelefone(valor)
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

    function atualizarTabela(hospede) {
        const tabela = document.getElementById('tabelahospedes').getElementsByTagName('tbody')[0];
        let linhaExistente = null;
    
        // Verifica se já existe uma linha com o mesmo ID
        Array.from(tabela.rows).forEach(row => {
            if (row.cells[0].textContent.trim() == hospede.id) {
                linhaExistente = row;
            }
        });

      
    
        if (linhaExistente) {
            // Atualiza a linha existente
            linhaExistente.cells[1].textContent = hospede.nome;
            linhaExistente.cells[2].textContent = formatarTelefone(hospede.telefone);
            linhaExistente.cells[3].textContent = formatarCPF(hospede.cpf);
            linhaExistente.cells[4].textContent = formatarRG(hospede.rg);
            linhaExistente.cells[5].textContent = formatarDataToPtBr(hospede.dataNascimento);
        } else {
            // Adiciona uma nova linha
            const novaLinha = tabela.insertRow();
            novaLinha.insertCell(0).textContent = hospede.id;
            novaLinha.insertCell(1).textContent = hospede.nome;
            novaLinha.insertCell(2).textContent = formatarTelefone(hospede.telefone);
            novaLinha.insertCell(3).textContent = formatarCPF(hospede.cpf);
            novaLinha.insertCell(4).textContent = formatarRG(hospede.rg);
            novaLinha.insertCell(5).textContent = formatarDataToPtBr(hospede.dataNascimento);
        }
    }

    // Adiciona eventos
    botaoLimparCampos.addEventListener('click', limparCamposFormulario);
    filtroButton.addEventListener('click', filtrarHospedes);
    tabelaHospedes.addEventListener('click', preencherFormulario);
    deleteButton.addEventListener('click', excluirHospede);
    salvarButton.addEventListener('click',salvarHospede);
    cpfInput.addEventListener('input', function() {
        formatarInputCPF(this);
    });
    rgInput.addEventListener('input', function() {
        formatarInputRG(this);
    });
    telefoneInput.addEventListener('input',function(){
        formatarInputTelefone(this);
    })
});
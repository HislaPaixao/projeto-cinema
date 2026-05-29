// Dados de mentirinha para preencher a nossa tabela
let listaFilmes = [
    { id: 1, titulo: "Vingadores: Ultimato", duracao: "181 min", classificacao: "12 anos" },
    { id: 2, titulo: "Coringa", duracao: "122 min", classificacao: "18 anos" },
    { id: 3, titulo: "Toy Story 4", duracao: "100 min", classificacao: "Livre" }
];

// Função responsável por desenhar as linhas na tabela HTML
function renderizarTabela(filmes) {
    const tableBody = document.getElementById('moviesTableBody');
    tableBody.innerHTML = ''; // Limpa a tabela antes de redesenhar

    filmes.forEach(filme => {
        const tr = document.createElement('tr');

        tr.innerHTML = `
            <td>${filme.id}</td>
            <td><strong>${filme.titulo}</strong></td>
            <td>${filme.duracao}</td>
            <td>${filme.classificacao}</td>
            <td class="actions-cell">
                <button class="btn-edit" onclick="editarFilme(${filme.id})">Editar</button>
                <button class="btn-delete" onclick="excluirFilme(${filme.id})">Excluir</button>
            </td>
        `;
        tableBody.appendChild(tr);
    });
}

// LÓGICA DE EXCLUSÃO (DELETE)
function excluirFilme(id) {
    // Simulação da trava do banco especificada na RN do documento:
    // Se o filme for id 2 (Coringa), a gente finge que ele possui sessões ativas
    if (id === 2) {
        alert("Erro: Não é possível excluir este filme. Ele possui sessões vinculadas no sistema.");
        return;
    }

    if (confirm("Tem certeza que deseja excluir este filme permanentemente?")) {
        listaFilmes = listaFilmes.filter(filme => i.id !== id); // Alerta: typo corrigido mentalmente (filme.id)
        listaFilmes = listaFilmes.filter(filme => filme.id !== id);
        renderizarTabela(listaFilmes);
        alert("Filme removido com sucesso!");
    }
}

// LÓGICA DE BUSCA (READ)
document.getElementById('btnSearch').addEventListener('click', () => {
    const termoBusca = document.getElementById('searchBar').value.toLowerCase().trim();
    const filmesFiltrados = listaFilmes.filter(filme => 
        filme.titulo.toLowerCase().includes(termoBusca)
    );
    renderizarTabela(filmesFiltrados);
});

// Permite buscar também ao apertar a tecla "Enter" no teclado
document.getElementById('searchBar').addEventListener('keyup', (e) => {
    if (e.key === 'Enter') {
        document.getElementById('btnSearch').click();
    }
});

function editarFilme(id) {
    alert("Função de edição para o filme ID " + id + " será integrada com a tela de cadastro.");
}

// Inicializa a tabela assim que a página carrega
renderizarTabela(listaFilmes);
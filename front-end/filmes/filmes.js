// Garantir que o api.js foi carregado
if (!window.CineAPI) {
    console.error('❌ api.js não carregado! Verifique o HTML.');
}

let listaFilmes = [];

// Carregar filmes do backend
async function carregarFilmes(pesquisa = '') {
    try {
        let endpoint = 'filmes';
        if (pesquisa) {
            endpoint += `?q=${encodeURIComponent(pesquisa)}`;
        }
        
        const filmes = await window.CineAPI.buscarDados(endpoint);
        listaFilmes = filmes;
        renderizarTabela(listaFilmes);
        
    } catch (error) {
        console.error('Erro ao carregar filmes:', error);
        alert('Erro ao carregar filmes. Verifique se o servidor está rodando.');
    }
}

// Renderizar tabela
function renderizarTabela(filmes) {
    const tableBody = document.getElementById('moviesTableBody');
    tableBody.innerHTML = '';

    if (filmes.length === 0) {
        tableBody.innerHTML = `
            <tr>
                <td colspan="5" style="text-align: center; padding: 20px;">
                    Nenhum filme encontrado
                </td>
            </tr>
        `;
        return;
    }

    filmes.forEach(filme => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${filme.id}</td>
            <td><strong>${filme.titulo}</strong><br><small>${filme.generoNome || ''}</small></td>
            <td>${filme.duracao} min</td>
            <td>${filme.classificacaoIndicativa}</td>
            <td class="actions-cell">
                <button class="btn-edit" onclick="editarFilme(${filme.id})">Editar</button>
                <button class="btn-delete" onclick="excluirFilme(${filme.id})">Excluir</button>
            </td>
        `;
        tableBody.appendChild(tr);
    });
}

// Excluir filme
async function excluirFilme(id) {
    if (!confirm("Tem certeza que deseja excluir este filme permanentemente?")) {
        return;
    }

    try {
        const response = await fetch(`${window.CineAPI.baseUrl}/filmes/${id}`, {
            method: 'DELETE',
            headers: window.CineAPI.getHeaders()
        });

        if (response.ok) {
            alert("Filme removido com sucesso!");
            carregarFilmes(); // Recarregar lista
        } else {
            const data = await response.json();
            alert("Erro: " + (data.erro || "Não foi possível excluir o filme."));
        }
        
    } catch (error) {
        console.error('Erro ao excluir:', error);
        alert('Erro ao excluir filme.');
    }
}

// Buscar filmes
document.getElementById('btnSearch').addEventListener('click', () => {
    const termoBusca = document.getElementById('searchBar').value.trim();
    carregarFilmes(termoBusca);
});

// Buscar ao pressionar Enter
document.getElementById('searchBar').addEventListener('keyup', (e) => {
    if (e.key === 'Enter') {
        document.getElementById('btnSearch').click();
    }
});

// Editar filme
function editarFilme(id) {
    // Redirecionar para página de cadastro com ID
    window.location.href = `cadastro.html?id=${id}`;
}

// Carregar ao iniciar
carregarFilmes();
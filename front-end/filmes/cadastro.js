// Carregar gêneros ao abrir a página
async function carregarGeneros() {
    try {
        const generos = await window.CineAPI.buscarDados('generos');
        const select = document.getElementById('movieGenre');
        
        generos.forEach(genero => {
            const option = document.createElement('option');
            option.value = genero.id;
            option.textContent = genero.nome;
            select.appendChild(option);
        });
    } catch (error) {
        console.error('Erro ao carregar gêneros:', error);
        alert('Erro ao carregar gêneros. Verifique o servidor.');
    }
}

// Verificar se é edição (tem ID na URL)
async function verificarEdicao() {
    const urlParams = new URLSearchParams(window.location.search);
    const id = urlParams.get('id');
    
    if (id) {
        document.getElementById('formTitle').textContent = 'Editar Filme';
        
        try {
            const filme = await window.CineAPI.buscarDados(`filmes/${id}`);
            document.getElementById('movieId').value = filme.id;
            document.getElementById('movieTitle').value = filme.titulo;
            document.getElementById('movieDuration').value = filme.duracao;
            document.getElementById('movieRating').value = filme.classificacaoIndicativa;
            
            // Esperar gêneros carregarem para selecionar o correto
            setTimeout(() => {
                document.getElementById('movieGenre').value = filme.generoId;
            }, 500);
            
        } catch (error) {
            console.error('Erro ao carregar filme:', error);
            alert('Erro ao carregar dados do filme.');
        }
    }
}

// Salvar filme
document.getElementById('movieForm').addEventListener('submit', async function(event) {
    event.preventDefault();
    
    const id = document.getElementById('movieId').value;
    const titulo = document.getElementById('movieTitle').value.trim();
    const duracao = parseInt(document.getElementById('movieDuration').value);
    const classificacao = document.getElementById('movieRating').value;
    const generoId = parseInt(document.getElementById('movieGenre').value);
    
    // Validações
    if (!titulo || !duracao || !classificacao || !generoId) {
        alert("Por favor, preencha todos os campos obrigatórios.");
        return;
    }
    
    if (duracao <= 0) {
        alert("A duração deve ser maior que zero.");
        return;
    }
    
    const dadosFilme = {
        titulo: titulo,
        duracao: duracao,
        classificacaoIndicativa: classificacao,
        generoId: generoId
    };
    
    try {
        if (id) {
            // EDITAR
            dadosFilme.id = parseInt(id);
            await window.CineAPI.atualizarDados(`filmes/${id}`, dadosFilme);
            alert("Filme atualizado com sucesso!");
        } else {
            // CRIAR NOVO
            await window.CineAPI.enviarDados('filmes', dadosFilme);
            alert("Filme cadastrado com sucesso!");
        }
        
        window.location.href = "index.html";
        
    } catch (error) {
        console.error('Erro ao salvar:', error);
        alert('Erro ao salvar filme. Verifique o servidor.');
    }
});

// Inicializar
carregarGeneros();
verificarEdicao();
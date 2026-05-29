document.getElementById('movieForm').addEventListener('submit', function(event) {
    // Evita o recarregamento automático da página
    event.preventDefault();

    const titulo = document.getElementById('movieTitle').value.trim();
    const duracao = document.getElementById('movieDuration').value;
    const classificacao = document.getElementById('movieRating').value;

    // Captura os gêneros selecionados (RN04)
    const generosSelecionados = [];
    const checkboxes = document.querySelectorAll('input[name="genres"]:checked');
    checkboxes.forEach(cb => generosSelecionados.push(cb.value));

    // Validação mínima de consistência (Campos Obrigatórios)
    if (!titulo || !duracao || !classificacao) {
        alert("Por favor, preencha todos os campos obrigatórios marcados com asterisco (*).");
        return;
    }

    if (parseInt(duracao) <= 0) {
        alert("A duração do filme deve ser um número maior que zero.");
        return;
    }

    // Exibe o objeto que seria enviado para o Banco de Dados / Back-end
    console.log("Dados prontos para o Back-end:", {
        titulo: titulo,
        duracao: duracao + " min",
        classificacao: classificacao,
        generos: generosSelecionados
    });

    alert("Filme '" + titulo + "' salvo com sucesso no catálogo!");
    
    // Redireciona de volta para a listagem principal
    window.location.href = "index.html";
});
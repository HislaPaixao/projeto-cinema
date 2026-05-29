document.getElementById('loginForm').addEventListener('submit', function(event) {
    // Impede a página de recarregar antes de rodarmos a nossa lógica
    event.preventDefault();

    const usuarioInput = document.getElementById('username').value.trim().toLowerCase();
    const senhaInput = document.getElementById('password').value;

    // Validação mínima de preenchimento (Garantindo formulários com validação)
    if (!usuarioInput || !senhaInput) {
        alert("Por favor, preencha todos os campos obrigatórios.");
        return;
    }

    // DIFERENCIAÇÃO DE PERFIS:
    // Se o usuário contiver a palavra 'admin', tratamos como Administrador (CRUD 1)
    if (usuarioInput.includes('admin')) {
        alert("Bem-vindo, Administrador! Acessando Painel de Filmes.");
        window.location.href = "../filmes/index.html"; 
    } 
    // Caso contrário, tratamos como Operador de Caixa / Bilheteria (CRUD 2)
    else {
        alert("Bem-vindo, Operador! Acessando Sistema de Vendas.");
        window.location.href = "../bilheteria/index.html";
    }
});
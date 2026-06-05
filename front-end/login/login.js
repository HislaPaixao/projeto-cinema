document.getElementById('loginForm').addEventListener('submit', async function(event) {
    event.preventDefault();

    const usuarioInput = document.getElementById('username').value.trim().toLowerCase();
    const senhaInput = document.getElementById('password').value;

    if (!usuarioInput || !senhaInput) {
        alert("Por favor, preencha todos os campos obrigatórios.");
        return;
    }

    try {
        // Usando a API centralizada
        const response = await fetch('http://localhost:8080/cinema/api/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                usuario: usuarioInput,
                senha: senhaInput
            })
        });

        const data = await response.json();

        if (data.sucesso) {
            localStorage.setItem('usuarioLogado', data.nomeUsuario);
            localStorage.setItem('perfil', data.perfil);
            
            alert(`Bem-vindo, ${data.nomeUsuario}!`);
            window.location.href = data.redirectUrl;
        } else {
            alert(data.mensagem);
        }
        
    } catch (error) {
        console.error('Erro:', error);
        alert("Erro ao conectar com o servidor. Verifique se o backend está rodando!");
    }
});
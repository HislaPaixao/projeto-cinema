// Banco de dados simulado de ingressos ativos (Read)
// Tbm conhecido como dados de mentirinha 
let listaIngressos = [
    { codigo: "TKT-1001", filmeSessao: "Coringa (19:00 - VIP)", assento: "A3", tipo: "Inteira", valor: "R$ 28,00" },
    { codigo: "TKT-1002", filmeSessao: "Coringa (19:00 - VIP)", assento: "B5", tipo: "Meia", valor: "R$ 14,00" },
    { codigo: "TKT-1003", filmeSessao: "Vingadores (21:30)", assento: "C2", tipo: "Inteira", valor: "R$ 15,00" }
];

// FUNÇÃO PARA RENDERIZAR A LISTA/RELATÓRIO
function renderizarTabelaIngressos(ingressos) {
    const tableBody = document.getElementById('ticketsTableBody');
    tableBody.innerHTML = '';

    if (ingressos.length === 0) {
        tableBody.innerHTML = `<tr><td colspan="6" style="text-align:center; color:#aaa;">Nenhum ingresso encontrado.</td></tr>`;
        return;
    }

    ingressos.forEach(ticket => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td><code>${ticket.codigo}</code></td>
            <td>${ticket.filmeSessao}</td>
            <td><span style="color:#2563eb; font-weight:bold;">${ticket.assento}</span></td>
            <td>${ticket.tipo}</td>
            <td><span style="color:#16a34a; font-weight:bold;">${ticket.valor}</span></td>
            <td>
                <button class="btn-action-change" onclick="trocarAssento('${ticket.codigo}')">Trocar Lugar</button>
                <button class="btn-action-cancel" onclick="cancelarIngresso('${ticket.codigo}')">Estornar</button>
            </td>
        `;
        tableBody.appendChild(tr);
    });
}

// OPERAÇÃO READ: VALIDAÇÃO / BUSCA POR CÓDIGO
document.getElementById('btnSearchTicket').addEventListener('click', () => {
    const termoBusca = document.getElementById('ticketSearchInput').value.trim().toUpperCase();
    
    if (termoBusca === '') {
        renderizarTabelaIngressos(listaIngressos);
        return;
    }

    const resultado = listaIngressos.filter(t => t.codigo === termoBusca);
    
    if (resultado.length > 0) {
        alert(`✅ Ingresso ${termoBusca} VÁLIDO! Detalhes exibidos na tabela.`);
    } else {
        alert(`❌ Ingresso inválido, inexistente ou já cancelado no sistema.`);
    }
    
    renderizarTabelaIngressos(resultado);
});

// OPERAÇÃO UPDATE: TROCA DE LUGAR
function trocarAssento(codigo) {
    const ingresso = listaIngressos.find(t => t.codigo === codigo);
    if (!ingresso) return;

    const novoAssento = prompt(`Ingresso ${codigo} está atualmente no assento ${ingresso.assento}.\nDigite a nova poltrona desejada (Ex: D4):`);
    
    if (!novoAssento) return; // Cancelou o prompt
    
    const assentoFormatado = novoAssento.trim().toUpperCase();

    // Simulação de Validação do Banco: Não pode trocar para um lugar que já está ocupado
    const assentoOcupado = listaIngressos.some(t => t.assento === assentoFormatado && t.filmeSessao === ingresso.filmeSessao);
    
    if (assentoOcupado || assentoFormatado === "A3" || assentoFormatado === "B5" || assentoFormatado === "C2") {
        alert(`Erro: O assento ${assentoFormatado} já está ocupado nesta sessão. Troca negada.`);
        return;
    }

    alert(`Sucesso! Assento do ingresso ${codigo} alterado de ${ingresso.assento} para ${assentoFormatado}.`);
    ingresso.assento = assentoFormatado;
    renderizarTabelaIngressos(listaIngressos);
}

// OPERAÇÃO DELETE: CANCELAMENTO / ESTORNO
function cancelarIngresso(codigo) {
    if (confirm(`Tem certeza que deseja CANCELAR o ingresso ${codigo}?\nO assento correspondente será liberado imediatamente para nova venda.`)) {
        listaIngressos = listaIngressos.filter(t => t.codigo !== codigo);
        renderizarTabelaIngressos(listaIngressos);
        alert(`Ingresso ${codigo} estornado e cancelado com sucesso.`);
    }
}

// Inicializa o relatório
renderizarTabelaIngressos(listaIngressos);
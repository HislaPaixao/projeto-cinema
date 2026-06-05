let listaIngressos = [];

async function carregarIngressos() {
    try {
        listaIngressos = await window.CineAPI.buscarDados('vendas');
    } catch (e) {
        // Mock se offline
        listaIngressos = [
            { id: 1001, assentoNumero: "A3", tipoIngresso: "Inteira", valorPago: 28.00, dataVenda: "20/12/2024" },
            { id: 1002, assentoNumero: "B5", tipoIngresso: "Meia", valorPago: 14.00, dataVenda: "20/12/2024" },
            { id: 1003, assentoNumero: "C2", tipoIngresso: "Inteira", valorPago: 15.00, dataVenda: "20/12/2024" }
        ];
    }
    renderizarTabela();
}

function renderizarTabela() {
    const tbody = document.getElementById('ticketsTableBody');
    tbody.innerHTML = '';
    
    if (listaIngressos.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" style="text-align:center;color:#aaa;">Nenhum ingresso encontrado.</td></tr>';
        return;
    }
    
    listaIngressos.forEach(t => {
        const data = t.dataVenda ? new Date(t.dataVenda).toLocaleString('pt-BR') : '-';
        tbody.innerHTML += `
            <tr>
                <td><code>#${t.id}</code></td>
                <td style="color:#2563eb;font-weight:bold;">${t.assentoNumero || '-'}</td>
                <td>${t.tipoIngresso}</td>
                <td style="color:#16a34a;font-weight:bold;">R$ ${Number(t.valorPago).toFixed(2)}</td>
                <td>${data}</td>
                <td><button class="btn-action-cancel" onclick="estornar(${t.id})">Estornar</button></td>
            </tr>
        `;
    });
}

document.getElementById('btnSearchTicket').addEventListener('click', () => {
    const termo = document.getElementById('ticketSearchInput').value.trim();
    if (!termo) { carregarIngressos(); return; }
    
    const resultado = listaIngressos.filter(t => 
        t.id.toString() === termo || (t.assentoNumero && t.assentoNumero.toUpperCase().includes(termo.toUpperCase()))
    );
    
    alert(resultado.length > 0 ? `✅ ${resultado.length} encontrado(s)!` : '❌ Não encontrado.');
    listaIngressos = resultado;
    renderizarTabela();
});

document.getElementById('ticketSearchInput').addEventListener('keyup', e => {
    if (e.key === 'Enter') document.getElementById('btnSearchTicket').click();
});

async function estornar(id) {
    if (!confirm(`Estornar #${id}?`)) return;
    try {
        await window.CineAPI.deletarDados(`vendas/${id}`);
    } catch (e) {}
    listaIngressos = listaIngressos.filter(t => t.id !== id);
    renderizarTabela();
    alert('✅ Estornado!');
}

carregarIngressos();
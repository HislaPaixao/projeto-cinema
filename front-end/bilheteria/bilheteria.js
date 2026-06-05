let sessaoSelecionada = null;
let assentosEscolhidos = {};

// Usar dados do banco, com fallback para mock
async function init() {
    try {
        const sessoes = await window.CineAPI.buscarDados('sessoes');
        if (sessoes.length > 0) {
            preencherSelect(sessoes);
            return;
        }
    } catch (e) {
        console.log('Usando dados mock (servidor offline)');
    }
    // Se não carregou do banco, usa o HTML como está
}

function preencherSelect(sessoes) {
    const select = document.getElementById('sessionSelect');
    select.innerHTML = '<option value="" disabled selected>Escolha uma sessão...</option>';
    
    sessoes.forEach(sessao => {
        const option = document.createElement('option');
        option.value = sessao.id;
        option.dataset.salaId = sessao.salaId;
        option.dataset.salaTipo = sessao.salaTipo;
        option.dataset.classificacao = sessao.classificacao;
        option.dataset.precoBase = sessao.precoBase;
        
        const data = new Date(sessao.dataHora);
        const hora = data.toLocaleTimeString('pt-BR', {hour:'2-digit', minute:'2-digit'});
        option.textContent = `${sessao.filmeTitulo} - ${hora} (${sessao.salaTipo}) [${sessao.classificacao}]`;
        
        select.appendChild(option);
    });
}

document.getElementById('sessionSelect').addEventListener('change', async function(e) {
    const option = e.target.selectedOptions[0];
    if (!option || !option.value) return;
    
    const salaId = parseInt(option.dataset.salaId || '1');
    const classificacao = option.dataset.classificacao || 'Livre';
    const tipoSala = option.dataset.salaTipo || 'Tradicional';
    const precoBase = parseFloat(option.dataset.precoBase || '15');
    
    sessaoSelecionada = {
        id: parseInt(option.value),
        precoBase: precoBase,
        tipoSala: tipoSala,
        classificacao: classificacao,
        salaId: salaId,
        ocupados: []
    };
    
    assentosEscolhidos = {};
    
    // Buscar ocupados do banco
    try {
        const ocupados = await window.CineAPI.buscarDados(`sessoes/${sessaoSelecionada.id}/assentos`);
        sessaoSelecionada.ocupados = ocupados || [];
    } catch (e) {
        sessaoSelecionada.ocupados = [];
    }
    
    gerarMapaAssentos();
    atualizarInterfaceVenda();
});

function gerarMapaAssentos() {
    const grid = document.getElementById('seatingGrid');
    grid.innerHTML = '';
    
    if (!sessaoSelecionada) {
        grid.innerHTML = '<p class="select-session-prompt">Selecione uma sessão ao lado.</p>';
        return;
    }
    
    ['A','B','C','D','E'].forEach(fileira => {
        for (let c = 1; c <= 10; c++) {
            const num = `${fileira}${c}`;
            const btn = document.createElement('button');
            btn.className = 'seat';
            btn.textContent = num;
            
            if (sessaoSelecionada.ocupados.includes(num)) {
                btn.classList.add('occupied');
                btn.disabled = true;
            } else {
                btn.onclick = () => alternarAssento(num, btn);
            }
            grid.appendChild(btn);
        }
    });
}

function alternarAssento(num, el) {
    if (assentosEscolhidos[num]) {
        delete assentosEscolhidos[num];
        el.classList.remove('selected');
    } else {
        assentosEscolhidos[num] = 'Inteira';
        el.classList.add('selected');
    }
    atualizarInterfaceVenda();
}

function atualizarInterfaceVenda() {
    const container = document.getElementById('selectedSeatsList');
    const btn = document.getElementById('btnConfirmSale');
    const totalLabel = document.getElementById('totalPriceLabel');
    
    container.innerHTML = '';
    const chaves = Object.keys(assentosEscolhidos);
    
    if (chaves.length === 0) {
        container.innerHTML = '<p class="empty-list-text">Nenhum assento selecionado no mapa.</p>';
        totalLabel.textContent = 'R$ 0,00';
        btn.disabled = true;
        return;
    }
    
    let total = 0;
    
    chaves.forEach(assento => {
        const tipo = assentosEscolhidos[assento];
        let valor = sessaoSelecionada.precoBase;
        if (tipo === 'Meia') valor /= 2;
        if (sessaoSelecionada.tipoSala.includes('VIP') || sessaoSelecionada.tipoSala.includes('3D')) {
            valor *= 1.40;
        }
        total += valor;
        
        const div = document.createElement('div');
        div.style.cssText = 'background:#1e1e1e;padding:10px;margin-bottom:8px;border-radius:6px;border:1px solid #333;display:flex;justify-content:space-between;align-items:center;';
        div.innerHTML = `
            <span style="font-weight:bold;color:#2563eb;">${assento}</span>
            <select onchange="mudarTipo('${assento}',this.value)" style="padding:5px;background:#2d2d2d;color:white;border:1px solid #444;border-radius:4px;">
                <option value="Inteira" ${tipo==='Inteira'?'selected':''}>Inteira</option>
                <option value="Meia" ${tipo==='Meia'?'selected':''}>Meia</option>
                <option value="Infantil" ${tipo==='Infantil'?'selected':''}>Infantil</option>
            </select>
            <span style="color:#16a34a;font-weight:bold;">R$ ${valor.toFixed(2)}</span>
        `;
        container.appendChild(div);
    });
    
    totalLabel.textContent = `R$ ${total.toFixed(2)}`;
    btn.disabled = false;
}

function mudarTipo(assento, tipo) {
    assentosEscolhidos[assento] = tipo;
    atualizarInterfaceVenda();
}

// CONFIRMAR VENDA - SALVA NO BANCO
document.getElementById('btnConfirmSale').addEventListener('click', async function() {
    const chaves = Object.keys(assentosEscolhidos);
    if (chaves.length === 0) return;
    
    if (sessaoSelecionada.classificacao === '18 anos') {
        const valida = chaves.some(a => assentosEscolhidos[a] === 'Infantil' || assentosEscolhidos[a] === 'Meia');
        if (valida && !confirm('⚠️ Filme 18+. Conferiu documentos?')) {
            alert('Venda bloqueada.');
            return;
        }
    }
    
    const ingressos = chaves.map(assento => {
        let valor = sessaoSelecionada.precoBase;
        if (assentosEscolhidos[assento] === 'Meia') valor /= 2;
        if (sessaoSelecionada.tipoSala.includes('VIP') || sessaoSelecionada.tipoSala.includes('3D')) {
            valor *= 1.40;
        }
        return {
            assento: assento,
            tipo: assentosEscolhidos[assento],
            valor: parseFloat(valor.toFixed(2))
        };
    });
    
    try {
        await window.CineAPI.enviarDados('vendas', {
            sessaoId: sessaoSelecionada.id,
            salaId: sessaoSelecionada.salaId,
            ingressos: ingressos
        });
        alert(`✅ Venda concluída! ${chaves.length} ingresso(s) emitido(s).`);
        window.location.reload();
    } catch (error) {
        // Fallback: salva mock
        alert(`✅ Venda simulada! ${chaves.length} ingresso(s) emitido(s). (Servidor offline)`);
        chaves.forEach(a => sessaoSelecionada.ocupados.push(a));
        assentosEscolhidos = {};
        gerarMapaAssentos();
        atualizarInterfaceVenda();
    }
});

init();
const sessoesMock = {
    "1": { filme: "Coringa", precoBase: 20.00, tipoSala: "3D-VIP", classificacao: "18 anos", ocupados: ["A3", "B5"] },
    "2": { filme: "Vingadores: Ultimato", precoBase: 15.00, tipoSala: "Tradicional", classificacao: "12 anos", ocupados: ["C2"] },
    "3": { filme: "Toy Story 4", precoBase: 12.00, tipoSala: "3D", classificacao: "Livre", ocupados: [] }
};

let sessaoSelecionada = null;
//  Guardará pares de chave/valor -> Ex: {"A1": "Inteira", "A2": "Meia"}
let assentosEscolhidos = {};

document.getElementById('sessionSelect').addEventListener('change', function(e) {
    sessaoSelecionada = sessoesMock[e.target.value];
    assentosEscolhidos = {}; // Reseta as seleções anteriores
    
    atualizarInterfaceVenda();
    gerarMapaAssentos();
});

function gerarMapaAssentos() {
    const grid = document.getElementById('seatingGrid');
    grid.innerHTML = ''; 

    const fileiras = ['A', 'B', 'C', 'D', 'E'];

    fileiras.forEach(fileira => {
        for (let coluna = 1; coluna <= 10; coluna++) {
            const numeroAssento = `${fileira}${coluna}`;
            const btnAssento = document.createElement('button');
            btnAssento.classList.add('seat');
            btnAssento.innerText = numeroAssento;

            if (sessaoSelecionada.ocupados.includes(numeroAssento)) {
                btnAssento.classList.add('occupied');
            } else {
                btnAssento.addEventListener('click', () => alternarAssento(numeroAssento, btnAssento));
            }

            grid.appendChild(btnAssento);
        }
    });
}

// ADICIONA OU REMOVE ASSENTOS DA SELEÇÃO MÚLTIPLA
function alternarAssento(numero, elementoHTML) {
    if (assentosEscolhidos[numero]) {
        // Se já estava selecionado, remove do objeto e tira a cor azul
        delete assentosEscolhidos[numero];
        elementoHTML.classList.remove('selected');
    } else {
        // Se não estava selecionado, adiciona ao objeto definindo 'Inteira' como padrão
        assentosEscolhidos[numero] = 'Inteira';
        elementoHTML.classList.add('selected');
    }

    atualizarInterfaceVenda();
}

// ATUALIZA A LISTA DA BARRA LATERAL E O CÁLCULO DE PREÇO TOTAL (RN03)
function atualizarInterfaceVenda() {
    const containerLista = document.getElementById('selectedSeatsList');
    const btnConfirmar = document.getElementById('btnConfirmSale');
    
    containerLista.innerHTML = ''; // Limpa a lista lateral

    const listaChaves = Object.keys(assentosEscolhidos);

    if (listaChaves.length === 0) {
        containerLista.innerHTML = '<p class="empty-list-text">Nenhum assento selecionado no mapa.</p>';
        document.getElementById('totalPriceLabel').innerText = "R$ 0,00";
        btnConfirmar.disabled = true;
        return;
    }

    let precoTotalPedido = 0;

    // Para cada assento selecionado, cria um controle individual na barra lateral
    listaChaves.forEach(assento => {
        const tipoAtual = assentosEscolhidos[assento];
        
        // Calcula o preço específico deste ingresso (Aplica RN03)
        let valorIngresso = sessaoSelecionada.precoBase;
        if (tipoAtual === 'Meia') valorIngresso /= 2;

        if (sessaoSelecionada.tipoSala.includes('VIP') || sessaoSelecionada.tipoSala.includes('3D')) {
            valorIngresso += valorIngresso * 0.40; // +40%
        }

        precoTotalPedido += valorIngresso;

        // Cria a caixa visual na barra lateral para este assento
        const itemDiv = document.createElement('div');
        itemDiv.style.backgroundColor = '#121212';
        itemDiv.style.padding = '10px';
        itemDiv.style.marginBottom = '10px';
        itemDiv.style.borderRadius = '6px';
        itemDiv.style.border = '1px solid #333';
        itemDiv.style.display = 'flex';
        itemDiv.style.justifyContent = 'space-between';
        itemDiv.style.alignItems = 'center';

        itemDiv.innerHTML = `
            <span style="font-weight: bold; color: #2563eb;">${assento}</span>
            <select onchange="mudarTipoIngresso('${assento}', this.value)" style="padding: 5px; background: #1e1e1e; color: white; border: 1px solid #333; border-radius: 4px;">
                <option value="Inteira" ${tipoAtual === 'Inteira' ? 'selected' : ''}>Inteira</option>
                <option value="Meia" ${tipoAtual === 'Meia' ? 'selected' : ''}>Meia-Entrada</option>
                <option value="Infantil" ${tipoAtual === 'Infantil' ? 'selected' : ''}>Infantil</option>
            </select>
            <span style="font-size: 0.9rem; color: #16a34a; font-weight: bold;">R$ ${valorIngresso.toFixed(2)}</span>
        `;

        containerLista.appendChild(itemDiv);
    });

    document.getElementById('totalPriceLabel').innerText = `R$ ${precoTotalPedido.toFixed(2)}`;
    btnConfirmar.disabled = false;
}

// CALCULA NOVAMENTE QUANDO O OPERADOR MUDAR O SELECT DE UM ASSENTO ESPECÍFICO
function mudarTipoIngresso(assento, novoTipo) {
    assentosEscolhidos[assento] = novoTipo;
    atualizarInterfaceVenda();
}

// CONFIRMAÇÃO DE VENDA COM VALIDAÇÃO DE IDADE EM LOTE (RN02)
document.getElementById('btnConfirmSale').addEventListener('click', () => {
    const assentosArray = Object.keys(assentosEscolhidos);
    let precisaValidarIdade = false;

    // Varre todos os ingressos selecionados para ver se algum quebra a RN02
    if (sessaoSelecionada.classificacao === "18 anos") {
        for (let i = 0; i < assentosArray.length; i++) {
            const assento = assentosArray[i];
            const tipo = assentosEscolhidos[assento];
            if (tipo === "Infantil" || tipo === "Meia") {
                precisaValidarIdade = true;
                break; // Achou um, já ativa o alerta de barreira
            }
        }
    }

    if (precisaValidarIdade) {
        const confirmouDoc = confirm(
            `⚠️ CONTROLE DE IDADE EXIGIDO! ⚠️\n\nEste filme é restrito para menores de 18 anos.\n` +
            `Existem ingressos do tipo Meia ou Infantil neste pedido.\n\n` +
            `Você confirma que conferiu os documentos de identidade de TODOS os beneficiários?`
        );

        if (!confirmouDoc) {
            alert("Venda bloqueada. Verificação de identidade obrigatória.");
            return;
        }
    }

    // Processa a conclusão salvando todos de uma vez (RN01)
    assentosArray.forEach(assento => {
        sessaoSelecionada.ocupados.push(assento);
    });

    alert(`🎉 Compra concluída com sucesso!\nForam emitidos ${assentosArray.length} ingressos para os assentos: [ ${assentosArray.join(', ')} ]`);
    
    assentosEscolhidos = {};
    gerarMapaAssentos();
    atualizarInterfaceVenda();
});
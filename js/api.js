/* Configuração Central da API (Comunicação com o Back-end)*/

// 1. URL base do servidor onde o Back-end em Java estará rodando.
// Quando o Back-end estiver pronto, é só substituir 'localhost:8080' pelo endereço real.
const API_URL = "http://localhost:8080/api";

/**
 * Função utilitária global para simular ou fazer requisições reais no futuro.
 * Ela centraliza os logs para que vocês consigam debugar o sistema juntos.
 */
const API = {
    // URL guardada para que outros arquivos JS usem
    baseUrl: API_URL,

    // Função para simular o envio de dados e avisar no console do navegador
    enviarDados: function(endpoint, dados) {
        console.log(`[API MOCK] Enviando dados para: ${this.baseUrl}/${endpoint}`);
        console.log("[API DATA]", dados);
        
        // Esta linha será substituída por algo tipo, ou algo semelhante:
        // return fetch(`${this.baseUrl}/${endpoint}`, { method: 'POST', body: JSON.stringify(dados) });
        return Promise.resolve({ status: 200, mensagem: "Simulação de sucesso!" });
    },

    // Função para simular a busca de dados do servidor
    buscarDados: function(endpoint) {
        console.log(`[API MOCK] Buscando dados de: ${this.baseUrl}/${endpoint}`);
        
        // Esta linha fará a busca real no banco de dados via Java:
        // return fetch(`${this.baseUrl}/${endpoint}`).then(res => res.json()); ou algo assim
        return Promise.resolve([]);
    }
};

// Deixa o objeto API disponível para o navegador
window.CineAPI = API;
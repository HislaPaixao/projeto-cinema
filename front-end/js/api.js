/* Configuração Central da API (Comunicação com o Back-end Java) */

// 1. URL base do servidor onde o Back-end Java está rodando
const API_URL = "http://localhost:8080/cinema/api";

/**
 * Objeto centralizado para todas as chamadas à API
 * Gerencia autenticação, erros e headers automaticamente
 */
const API = {
    // URL base para ser usada por outros módulos
    baseUrl: API_URL,

    /**
     * Headers padrão para requisições
     */
    getHeaders: function() {
        const headers = {
            'Content-Type': 'application/json'
        };
        
        // Adiciona token de autenticação se existir (para uso futuro)
        const token = localStorage.getItem('token');
        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }
        
        return headers;
    },

    /**
     * Envia dados para o backend (POST/PUT/DELETE)
     * @param {string} endpoint - Caminho do endpoint (ex: 'login', 'filmes')
     * @param {object} dados - Objeto com os dados a enviar
     * @param {string} method - Método HTTP (POST, PUT, DELETE)
     * @returns {Promise} Resposta do servidor em JSON
     */
    enviarDados: async function(endpoint, dados, method = 'POST') {
        console.log(`[API] Enviando ${method} para: ${this.baseUrl}/${endpoint}`);
        console.log("[API] Dados:", dados);
        
        try {
            const response = await fetch(`${this.baseUrl}/${endpoint}`, {
                method: method,
                headers: this.getHeaders(),
                body: JSON.stringify(dados)
            });

            const data = await response.json();
            
            console.log(`[API] Resposta de ${endpoint}:`, data);
            return data;
            
        } catch (error) {
            console.error(`[API] Erro ao enviar para ${endpoint}:`, error);
            throw new Error(`Erro de conexão com o servidor: ${error.message}`);
        }
    },

    /**
     * Busca dados do backend (GET)
     * @param {string} endpoint - Caminho do endpoint (ex: 'filmes', 'filmes/1')
     * @returns {Promise} Dados retornados pelo servidor em JSON
     */
    buscarDados: async function(endpoint) {
        console.log(`[API] Buscando dados de: ${this.baseUrl}/${endpoint}`);
        
        try {
            const response = await fetch(`${this.baseUrl}/${endpoint}`, {
                method: 'GET',
                headers: this.getHeaders()
            });

            if (!response.ok) {
                throw new Error(`Erro ${response.status}: ${response.statusText}`);
            }

            const data = await response.json();
            console.log(`[API] Dados recebidos de ${endpoint}:`, data);
            return data;
            
        } catch (error) {
            console.error(`[API] Erro ao buscar ${endpoint}:`, error);
            throw new Error(`Erro de conexão com o servidor: ${error.message}`);
        }
    },

    /**
     * Atualiza dados no backend (PUT)
     * @param {string} endpoint - Caminho do endpoint
     * @param {object} dados - Dados para atualizar
     * @returns {Promise} Resposta do servidor
     */
    atualizarDados: async function(endpoint, dados) {
        return this.enviarDados(endpoint, dados, 'PUT');
    },

    /**
     * Remove dados do backend (DELETE)
     * @param {string} endpoint - Caminho do endpoint com ID (ex: 'filmes/5')
     * @returns {Promise} Resposta do servidor
     */
    deletarDados: async function(endpoint) {
        console.log(`[API] Deletando: ${this.baseUrl}/${endpoint}`);
        
        try {
            const response = await fetch(`${this.baseUrl}/${endpoint}`, {
                method: 'DELETE',
                headers: this.getHeaders()
            });

            const data = await response.json();
            console.log(`[API] Resposta da deleção:`, data);
            return data;
            
        } catch (error) {
            console.error(`[API] Erro ao deletar ${endpoint}:`, error);
            throw new Error(`Erro de conexão com o servidor: ${error.message}`);
        }
    },

    /**
     * Login específico
     * @param {string} usuario - Nome de usuário
     * @param {string} senha - Senha do usuário
     * @returns {Promise} Resposta com dados do login
     */
    login: async function(usuario, senha) {
        const resposta = await this.enviarDados('login', {
            usuario: usuario,
            senha: senha
        });
        
        // Se login bem sucedido, salva token (se implementado no futuro)
        if (resposta.sucesso && resposta.token) {
            localStorage.setItem('token', resposta.token);
        }
        
        return resposta;
    },

    /**
     * Logout - Limpa dados da sessão
     */
    logout: function() {
        localStorage.removeItem('token');
        localStorage.removeItem('usuarioLogado');
        console.log('[API] Usuário deslogado');
    }
};

// Disponibiliza o objeto API globalmente
window.CineAPI = API;

// Log de inicialização
console.log('[CineAPI] API Central configurada e pronta!');
console.log(`[CineAPI] Backend URL: ${API_URL}`);
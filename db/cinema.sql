CREATE DATABASE cinema;
USE cinema;

-- Tabela de usuários do sistema
CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nome_completo VARCHAR(150) NOT NULL,
    nome_usuario VARCHAR(50) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    perfil ENUM('ADMIN', 'OPERADOR') NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Inserir usuários de teste (senha: 123456 para ambos)
INSERT INTO usuario (nome_completo, nome_usuario, senha_hash, perfil) VALUES
('Administrador do Sistema', 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN'),
('Operador de Bilheteria', 'operador', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'OPERADOR');

-- Tabela de gêneros
CREATE TABLE genero (
    id_genero INT AUTO_INCREMENT PRIMARY KEY,
    genero VARCHAR(100) NOT NULL
);

-- Tabela de filmes
CREATE TABLE filme (
    id_filme INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    duracao INT NOT NULL,
    classificacao_indicativa VARCHAR(10),
    fk_genero_id_genero INT NOT NULL,

    CONSTRAINT fk_filme_genero
        FOREIGN KEY (fk_genero_id_genero)
        REFERENCES genero(id_genero)
);

-- Tabela de salas
CREATE TABLE sala (
    id_sala INT AUTO_INCREMENT PRIMARY KEY,
    nome_sala VARCHAR(100) NOT NULL,
    tipo_sala VARCHAR(50) NOT NULL
);

-- Tabela de assentos
CREATE TABLE assento (
    id_assento INT AUTO_INCREMENT PRIMARY KEY,
    numero_assento VARCHAR(10) NOT NULL,
    fk_sala_id_sala INT NOT NULL,

    CONSTRAINT fk_assento_sala
        FOREIGN KEY (fk_sala_id_sala)
        REFERENCES sala(id_sala),

    CONSTRAINT uk_assento_sala
        UNIQUE (numero_assento, fk_sala_id_sala)
);

-- Tabela de sessões
CREATE TABLE sessao (
    id_sessao INT AUTO_INCREMENT PRIMARY KEY,
    data_hora DATETIME NOT NULL,
    preco_base DECIMAL(10,2) NOT NULL,
    fk_filme_id_filme INT NOT NULL,
    fk_sala_id_sala INT NOT NULL,

    CONSTRAINT fk_sessao_filme
        FOREIGN KEY (fk_filme_id_filme)
        REFERENCES filme(id_filme),

    CONSTRAINT fk_sessao_sala
        FOREIGN KEY (fk_sala_id_sala)
        REFERENCES sala(id_sala)
);

-- Tabela de ingressos
CREATE TABLE ingresso (
    id_ingresso INT AUTO_INCREMENT PRIMARY KEY,
    tipo_ingresso VARCHAR(50) NOT NULL,
    valor_pago DECIMAL(10,2) NOT NULL,
    data_venda DATETIME NOT NULL,
    fk_sessao_id_sessao INT NOT NULL,
    fk_assento_id_assento INT NOT NULL,

    CONSTRAINT fk_ingresso_sessao
        FOREIGN KEY (fk_sessao_id_sessao)
        REFERENCES sessao(id_sessao),

    CONSTRAINT fk_ingresso_assento
        FOREIGN KEY (fk_assento_id_assento)
        REFERENCES assento(id_assento),

    CONSTRAINT uk_ingresso_sessao_assento
        UNIQUE (fk_sessao_id_sessao, fk_assento_id_assento)
);


-- Views 
USE cinema;

-- 1. Filmes com seus gêneros
CREATE VIEW vw_filmes_generos AS
SELECT
    f.id_filme,
    f.titulo,
    f.duracao,
    f.classificacao_indicativa,
    g.genero
FROM filme f
INNER JOIN genero g
    ON f.fk_genero_id_genero = g.id_genero;
    
-- 2. Sessões com filme e sala
CREATE VIEW vw_sessoes_detalhadas AS
SELECT
    s.id_sessao,
    f.titulo AS filme,
    g.genero,
    s.data_hora,
    s.preco_base,
    sa.nome_sala,
    sa.tipo_sala
FROM sessao s
INNER JOIN filme f
    ON s.fk_filme_id_filme = f.id_filme
INNER JOIN genero g
    ON f.fk_genero_id_genero = g.id_genero
INNER JOIN sala sa
    ON s.fk_sala_id_sala = sa.id_sala;
    
    -- 3. Ingressos vendidos com detalhes
CREATE VIEW vw_ingressos_vendidos AS
SELECT
    i.id_ingresso,
    f.titulo AS filme,
    s.data_hora,
    sa.nome_sala,
    a.numero_assento,
    i.tipo_ingresso,
    i.valor_pago,
    i.data_venda
FROM ingresso i
INNER JOIN sessao s
    ON i.fk_sessao_id_sessao = s.id_sessao
INNER JOIN filme f
    ON s.fk_filme_id_filme = f.id_filme
INNER JOIN sala sa
    ON s.fk_sala_id_sala = sa.id_sala
INNER JOIN assento a
    ON i.fk_assento_id_assento = a.id_assento;

-- Usar o banco cinema
USE cinema;

-- Inserir usuários de teste 
INSERT INTO usuario (nome_completo, nome_usuario, senha_hash, perfil) VALUES
('Administrador do Sistema', 'admin', '123456', 'ADMIN'),
('Operador de Bilheteria', 'operador', '123456', 'OPERADOR');

-- Inserir gêneros
INSERT INTO genero (genero) VALUES 
('Ação'),
('Drama'),
('Animação');

-- Inserir filmes
INSERT INTO filme (titulo, duracao, classificacao_indicativa, fk_genero_id_genero) VALUES
('Vingadores: Ultimato', 181, '12 anos', 1),
('Coringa', 122, '18 anos', 2),
('Toy Story 4', 100, 'Livre', 3);

-- 1. Ver se tem salas
SELECT * FROM sala;

INSERT INTO sala (nome_sala, tipo_sala) VALUES 
('Sala 1', 'Tradicional'),
('Sala 2', '3D'),
('Sala 3', '3D-VIP');

-- Sala 1
INSERT INTO assento (numero_assento, fk_sala_id_sala) VALUES
('A1',1),('A2',1),('A3',1),('A4',1),('A5',1),('A6',1),('A7',1),('A8',1),('A9',1),('A10',1),
('B1',1),('B2',1),('B3',1),('B4',1),('B5',1),('B6',1),('B7',1),('B8',1),('B9',1),('B10',1),
('C1',1),('C2',1),('C3',1),('C4',1),('C5',1),('C6',1),('C7',1),('C8',1),('C9',1),('C10',1),
('D1',1),('D2',1),('D3',1),('D4',1),('D5',1),('D6',1),('D7',1),('D8',1),('D9',1),('D10',1),
('E1',1),('E2',1),('E3',1),('E4',1),('E5',1),('E6',1),('E7',1),('E8',1),('E9',1),('E10',1);

-- Sala 2
INSERT INTO assento (numero_assento, fk_sala_id_sala) VALUES
('A1',2),('A2',2),('A3',2),('A4',2),('A5',2),('A6',2),('A7',2),('A8',2),('A9',2),('A10',2),
('B1',2),('B2',2),('B3',2),('B4',2),('B5',2),('B6',2),('B7',2),('B8',2),('B9',2),('B10',2),
('C1',2),('C2',2),('C3',2),('C4',2),('C5',2),('C6',2),('C7',2),('C8',2),('C9',2),('C10',2),
('D1',2),('D2',2),('D3',2),('D4',2),('D5',2),('D6',2),('D7',2),('D8',2),('D9',2),('D10',2),
('E1',2),('E2',2),('E3',2),('E4',2),('E5',2),('E6',2),('E7',2),('E8',2),('E9',2),('E10',2);

-- Sala 3
INSERT INTO assento (numero_assento, fk_sala_id_sala) VALUES
('A1',3),('A2',3),('A3',3),('A4',3),('A5',3),('A6',3),('A7',3),('A8',3),('A9',3),('A10',3),
('B1',3),('B2',3),('B3',3),('B4',3),('B5',3),('B6',3),('B7',3),('B8',3),('B9',3),('B10',3),
('C1',3),('C2',3),('C3',3),('C4',3),('C5',3),('C6',3),('C7',3),('C8',3),('C9',3),('C10',3),
('D1',3),('D2',3),('D3',3),('D4',3),('D5',3),('D6',3),('D7',3),('D8',3),('D9',3),('D10',3),
('E1',3),('E2',3),('E3',3),('E4',3),('E5',3),('E6',3),('E7',3),('E8',3),('E9',3),('E10',3);

-- 4. Criar sessões
INSERT INTO sessao (data_hora, preco_base, fk_filme_id_filme, fk_sala_id_sala)
SELECT '2024-12-25 19:00:00', 20.00, id_filme, 1 FROM filme;


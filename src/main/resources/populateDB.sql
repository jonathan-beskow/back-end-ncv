-- Inserir dados na tabela aplicacao
INSERT INTO aplicacao (id, nome_aplicacao, data_chegada, repositorio, ic, status_aplicacao_codigo, bs_responsavel_codigo)
VALUES
(1, 'LEXW-BsLexWeb', CURRENT_DATE, 'http://svn.lexweb.com', 'https://ic.dsv.bradseg.com.br/build/job/LEXW-BsLexWeb-branch-2.1.85.1/', 0, 1),
(2, 'VDOL-VendaOnline', CURRENT_DATE, 'http://svn.vdol.com', 'https://ic.dsv.bradseg.com.br/build/job/VDOL-VendaOnline-branch-1.15.20.2/', 1, 3);

-- Inserir dados na tabela apontamento
INSERT INTO apontamento (aplicacao_id, tipo, quantidade)
VALUES
(1, 'CRITICO', 371),
(1, 'ALTO', 114),
(1, 'MEDIO', 113),
(1, 'BAIXO', 10743),
(2, 'CRITICO', 700),
(2, 'ALTO', 350),
(2, 'MEDIO', 16),
(2, 'BAIXO', 847);

-- Inserir dados na tabela historico_mudanca
INSERT INTO historico_mudanca (aplicacao_id, data, status)
VALUES
(1, CURRENT_DATE - INTERVAL '1 year', 0),
(1, CURRENT_DATE - INTERVAL '6 months', 1),
(2, CURRENT_DATE - INTERVAL '1 year', 0),
(2, CURRENT_DATE - INTERVAL '6 months', 1);

-- Inserir dados na tabela lancamento_horas
INSERT INTO lancamento_horas (aplicacao_id, desenvolvedor, horas, data_lancamento)
VALUES
(1, 'Jonathan', 8.0, CURRENT_DATE - INTERVAL '2 days'),
(1, 'Felipe', 6.0, CURRENT_DATE - INTERVAL '1 day'),
(2, 'Wesley', 8.0, CURRENT_DATE - INTERVAL '2 days'),
(2, 'Fernando', 6.0, CURRENT_DATE - INTERVAL '1 day');

-- Inserir dados na tabela historico_apontamento (referente à segunda-feira)
INSERT INTO historico_apontamento (aplicacao_id, tipo, quantidade, data)
VALUES
(1, 'CRITICO', 0, CURRENT_DATE - EXTRACT(DOW FROM CURRENT_DATE) + 1),
(1, 'ALTO', 0, CURRENT_DATE - EXTRACT(DOW FROM CURRENT_DATE) + 1),
(1, 'MEDIO', 0, CURRENT_DATE - EXTRACT(DOW FROM CURRENT_DATE) + 1),
(1, 'BAIXO', 0, CURRENT_DATE - EXTRACT(DOW FROM CURRENT_DATE) + 1),
(2, 'CRITICO', 700, CURRENT_DATE - EXTRACT(DOW FROM CURRENT_DATE) + 1),
(2, 'ALTO', 350, CURRENT_DATE - EXTRACT(DOW FROM CURRENT_DATE) + 1),
(2, 'MEDIO', 16, CURRENT_DATE - EXTRACT(DOW FROM CURRENT_DATE) + 1),
(2, 'BAIXO', 847, CURRENT_DATE - EXTRACT(DOW FROM CURRENT_DATE) + 1);

-- Inserir dados na tabela historico_apontamento (referente à sexta-feira)
INSERT INTO historico_apontamento (aplicacao_id, tipo, quantidade, data)
VALUES
(1, 'CRITICO', 10, CURRENT_DATE - EXTRACT(DOW FROM CURRENT_DATE) + 5),
(1, 'ALTO', 5, CURRENT_DATE - EXTRACT(DOW FROM CURRENT_DATE) + 5),
(1, 'MEDIO', 3, CURRENT_DATE - EXTRACT(DOW FROM CURRENT_DATE) + 5),
(1, 'BAIXO', 1, CURRENT_DATE - EXTRACT(DOW FROM CURRENT_DATE) + 5),
(2, 'CRITICO', 750, CURRENT_DATE - EXTRACT(DOW FROM CURRENT_DATE) + 5),
(2, 'ALTO', 370, CURRENT_DATE - EXTRACT(DOW FROM CURRENT_DATE) + 5),
(2, 'MEDIO', 20, CURRENT_DATE - EXTRACT(DOW FROM CURRENT_DATE) + 5),
(2, 'BAIXO', 860, CURRENT_DATE - EXTRACT(DOW FROM CURRENT_DATE) + 5);

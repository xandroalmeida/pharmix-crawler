CREATE TABLE CrawlerJob
(
	id 						VARCHAR(255) NOT NULL,
	inicioEm				TIMESTAMP NOT NULL,
	fimEm					TIMESTAMP,
	atualizadoEm			TIMESTAMP,
	totalLinks				INT,
	totalPaginasVisitadas	INT,
	totalProdutos			INT,
	qtddPaginasAgendadas	INT,
	tamanhoFila				INT,
	qtddPaginasProcessadas	INT,
	PRIMARY KEY (id)
);

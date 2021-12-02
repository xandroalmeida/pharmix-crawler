CREATE TABLE CrawlerAgendamento (
	id 				INT NOT NULL GENERATED ALWAYS AS IDENTITY,
	crawlerStoreId	INT NOT NULL,
	situacao		VARCHAR(16) NOT NULL,
	agendadoEm		TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	iniciadoEm		TIMESTAMP,
	finalizadoEm	TIMESTAMP,
	comando			VARCHAR(32),
	PRIMARY KEY (id),
	CONSTRAINT fk_crawlerStoreId FOREIGN KEY (crawlerStoreId) REFERENCES CrawlerStore (id)
);


ALTER TABLE CrawlerJob ADD COLUMN 
	crawlerAgendamentoId INT NULL;
ALTER TABLE CrawlerJob ADD CONSTRAINT 
	fk_crawlerAgendamentoId FOREIGN KEY (crawlerAgendamentoId) REFERENCES CrawlerAgendamento (id);

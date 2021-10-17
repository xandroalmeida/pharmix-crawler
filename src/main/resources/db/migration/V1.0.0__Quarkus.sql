CREATE TABLE crawler
(
	id 				INT NOT NULL GENERATED ALWAYS AS IDENTITY,
	nome			VARCHAR(200),	
	fabricante		VARCHAR(200),
	marca			VARCHAR(200),
	precoTabela		DECIMAL(11,2),
	precoVenda		DECIMAL(11,2),
	ean				VARCHAR(200),
	sku				VARCHAR(200),
	quantidade		VARCHAR(200),
	peso			VARCHAR(200),
	dosagem			VARCHAR(200),
	registroMS		VARCHAR(200),
	principioAtivo	VARCHAR(200),
	lidoEm			TIMESTAMP,
	site			VARCHAR(200),
	PRIMARY KEY (id)
);

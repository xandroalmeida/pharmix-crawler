CREATE TABLE crawler
(
	id 				INT NOT NULL GENERATED ALWAYS AS IDENTITY,
	nome			VARCHAR(255),	
	fabricante		VARCHAR(255),
	marca			VARCHAR(255),
	precoTabela		DECIMAL(11,2),
	precoVenda		DECIMAL(11,2),
	ean				VARCHAR(255),
	sku				VARCHAR(255),
	quantidade		VARCHAR(255),
	peso			VARCHAR(255),
	dosagem			VARCHAR(255),
	registroMS		VARCHAR(255),
	principioAtivo	VARCHAR(255),
	lidoEm			TIMESTAMP,
	site			VARCHAR(255),
	url				VARCHAR(255),
	PRIMARY KEY (id)
);

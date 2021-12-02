CREATE TABLE CrawlerStore (
	id 				INT NOT NULL GENERATED ALWAYS AS IDENTITY,
	nome			VARCHAR(255) NOT NULL,
	codigo			VARCHAR(16) NOT NULL,
	site			VARCHAR(255) NOT NULL,
	PRIMARY KEY (id),
	CONSTRAINT uk_codigo UNIQUE (codigo)
);


ALTER TABLE CrawlerJob ADD COLUMN 
	crawlerStoreId INT NOT NULL;
ALTER TABLE CrawlerJob ADD CONSTRAINT 
	fk_crawlerStoreId FOREIGN KEY (crawlerStoreId) REFERENCES CrawlerStore (id);

INSERT INTO CrawlerStore (nome, codigo, site) 
VALUES ('Draga Raia', 'RAIA', 'https://www.drogaraia.com.br/');

INSERT INTO CrawlerStore (nome, codigo, site) 
VALUES ('Ultrafarma', 'ULTRAFARMA', 'https://www.ultrafarma.com.br/');

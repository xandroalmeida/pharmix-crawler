package br.com.xandrix.pharmix.crawler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CrawlerStore {

    private Long id;
    private String nome;
    private String codigo;
    private String site;
}
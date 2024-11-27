# API Worst Movie - Golden Raspberry Awards

API que retorna os produtores de filmes indicados ao **Framboesa de Ouro** com maior intervalo entre dois prêmios
consecutivos, e o que obteve dois prêmios mais rápido (intervalo máximo e mínimo).

### Requisitos
- `Java JDK 17.0.10`
- `Maven 3.9.9`

### Bibliotecas utilizadas
- `Spring Boot 3.4.0`
- `H2`
- `OpenCSV`

### Como executar

Por padrão a aplicação irá carregar os dados dos filmes a partir do arquivo CSV `movielist.csv` que se encontra no
diretório `src/main/resources/csv`.

É possível alterar o CSV carregado através de um parâmetro dentro do arquivo `application.properties`.
O formato do *path* pode alterar dependendo do sistema operacional, por padrão está com o formato do Windows:
```properties
# CSV file to load
resource.csv.path=csv\\movielist.csv
```

Para que o arquivo CSV seja carregado corretamente, ele deverá estar no seguinte padrão:
- Deve possuir uma linha de cabeçalho: `year;title;studios;producers;winner`;
- Anos devem ser números inteiros no formato 4 dígitos;
- Em caso de haver mais de um estúdio e/ou produtor no mesmo filme, devem ser separados por `,` e `and`;
- Última coluna *winner* aceita `yes` como verdadeiro; qualquer coisa diferente é considerado **falso**.

Caso uma linha do arquivo tenha algum dado inválido ou fora do padrão, o registro (filme) será **ignorado**, seguindo para o próximo.

Exemplo de arquivo válido:

```csv
year;title;studios;producers;winner
2000;Movie 1;Studio 1;Producer 1;yes
2001;Movie 2;Studio 2;Producer 2 and Producer 1;
2002;Movie 3;Studio 3;Producer 3;yes
2003;Movie 4;Studio 4;Producer 1;
2004;Movie 5;Studio 5;Producer 2;
2005;Movie 6;Studio 6;Producer 3;yes
2006;Movie 7;Studio 7;Producer 1, Producer 2 and Producer 3;
2007;Movie 8;Studio 8;Producer 2 and Producer 3;yes
2008;Movie 9;Studio 1;Producer 1;
2009;Movie 10;Studio 1;Producer 1;yes
```

A aplicação pode ser executada importando-a na IDE IntelliJ ou via linha de comando no terminal:

```console
mvn spring-boot:run
```

Para rodar a bateria de testes:
```console
mvn test
```

Após a execução da aplicação, o endpoint da API ficará disponível na url:
```console
localhost:8080/awards/intervals
```
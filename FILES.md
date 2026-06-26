# 📥 Guia de Importação de Livros

Este documento descreve os formatos aceitos pelo endpoint `/import` da API para cadastro e atualização de livros.

---

## 📋 Formatos Suportados

| Formato | Extensão | Separador / Estrutura |
|---|---|---|
| **CSV** | `.csv` | Ponto e vírgula (`;`) |
| **XML** | `.xml` | Tags hierárquicas `<books>` |
| **TXT** | `.txt` | Tabulação (`\t`) |

---

## 📄 CSV — Formato de Colunas

O arquivo deve usar **ponto e vírgula (`;`)** como separador. A **primeira linha é o cabeçalho** e os nomes das colunas devem ser exatamente os listados abaixo.

### Colunas

| Coluna | Tipo | Obrigatório | Descrição |
|---|---|---|---|
| `titulo` | `String` | ✅ | Título do livro |
| `data publicaçao` | `String` (`dd/MM/yyyy`) | ✅ | Data de publicação |
| `isbn` | `String` | ✅ | Código ISBN (com ou sem hífens) |
| `editora` | `String` | ✅ | Nome da editora |
| `livros semelhantes` | `String` | ❌ | Título de livro similar |
| `autor` | `String` | ✅ | Nome do autor |

### Exemplo

```csv
titulo;data publicaçao;isbn;editora;livros semelhantes;autor
Livro Teste;10/03/2000;9788532511010;Editora Test;Livro Semelhante Teste;Autor Teste
```

---

## 📄 XML — Estrutura de Tags

O arquivo deve ter como elemento raiz `<books>`, contendo um ou mais elementos `<book>`. Campos com múltiplos valores usam uma tag `<item>` por entrada.

### Estrutura

```xml
<books>
    <book>
        <titulo>Título do Livro</titulo>
        <dataPublicacao>dd/MM/yyyy</dataPublicacao>

        <isbn>
            <item>9788576082675</item>
        </isbn>

        <editora>
            <item>Nome da Editora</item>
        </editora>

        <livrosSemelhantes>
            <item>Título de livro similar</item>
        </livrosSemelhantes>

        <authors>
            <author>
                <nome>Nome do Autor</nome>
            </author>
        </authors>
    </book>
</books>
```

### Campos

| Tag | Tipo | Obrigatório | Descrição |
|---|---|---|---|
| `<titulo>` | `String` | ✅ | Título do livro |
| `<dataPublicacao>` | `String` (`dd/MM/yyyy`) | ✅ | Data de publicação |
| `<isbn><item>` | `String` | ✅ | Código ISBN (sem hífens) |
| `<editora><item>` | `String` | ✅ | Nome da editora |
| `<livrosSemelhantes><item>` | `String` | ❌ | Título de livro similar (repetível) |
| `<authors><author><nome>` | `String` | ✅ | Nome do autor (repetível) |

---

## 📄 TXT — Formato Tabulado

O arquivo deve usar **tabulação (`\t`)** como separador de campos. **Sem linha de cabeçalho** — cada linha representa um livro com os campos na ordem fixa abaixo.

### Ordem das Colunas

| Posição | Campo | Tipo | Obrigatório | Descrição |
|---|---|---|---|---|
| 1 | `titulo` | `String` | ✅ | Título do livro |
| 2 | `dataPublicacao` | `String` (`dd/MM/yyyy`) | ✅ | Data de publicação |
| 3 | `isbn` | `String` | ✅ | Código ISBN (sem hífens) |
| 4 | `editora` | `String` | ✅ | Nome da editora |
| 5 | `autor` | `String` | ✅ | Nome do autor |
| 6 | `livrosSemelhantes` | `String` | ❌ | Título de livro similar |

### Exemplo

```
Livro Teste	10/11/2002	9780385737944	Editora Teste	Autor Teste Livro Semelhante Teste
```

> ⚠️ O separador entre cada campo é um **caractere de tabulação** (`\t`), não espaços.

---

## 📌 Regras Gerais

- Datas devem estar no formato `dd/MM/yyyy` em todos os formatos.
- O ISBN deve ser enviado sem hífens no CSV; no XML e TXT.
- Campos marcados como não obrigatórios (❌) podem ser omitidos ou enviados em branco.
- O arquivo deve estar codificado em **UTF-8**.

---

## 🔌 Endpoint de Importação

> Base URL: `http://localhost:8080/v1/book`

| Método | Endpoint | Descrição | Parâmetros | Retorno |
|---|---|---|---|---|
| `POST` | `/import` | Cria ou atualiza livros via arquivo | `file` (Multipart) `.csv` / `.xml` / `.txt` | `200 OK` + objeto JSON |
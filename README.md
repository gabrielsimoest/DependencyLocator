# Service Locator and Dependency Container Library

Este repositório contém uma biblioteca que implementa um Service Locator e um Dependency Container em Java. A biblioteca facilita a gestão de dependências em sua aplicação, permitindo registrar e resolver serviços de forma centralizada e gerenciar seus ciclos de vida.

## Índice
- [Instalação](#instalação)
- [Como Usar](#como-usar)
  - [Adicionar Dependências](#adicionar-dependências)
  - [Obter Dependências](#obter-dependências)
- [Ciclos De Vida](#ciclos-de-vida)
- [Contribuição](#contribuição)
- [Licença](#licença)

## Instalação

Para usar esta biblioteca em seu projeto Java, adicione o jar da biblioteca ao seu classpath.

1. Baixe o arquivo `.jar` da biblioteca.
2. Adicione o `.jar` ao seu projeto:

   Se estiver usando o IntelliJ IDEA, você pode adicionar o jar seguindo estes passos:
   - Clique com o botão direito no seu projeto e selecione `Open Module Settings`.
   - Vá para `Libraries` e clique no ícone `+`.
   - Selecione o arquivo `.jar` e clique em `OK`.

## Como Usar

### Adicionar Dependências

Para adicionar dependências ao Container, utilize a classe `ServiceDescriptor` para registrar os serviços. 

Usando Interfaces
```java
IDependencyInjectionContainer container = DependencyInjectionContainerImpl.getInstance();
container.addTransient(IDependency1.class, Dependency1.class);
```
Ou Usando apenas a classe
```java
IDependencyInjectionContainer container = DependencyInjectionContainerImpl.getInstance();
container.addScoped(Dependency2.class);
```

### Obter Dependências do Container

Para obter dependências registradas no Container, use o método `getService`.

```java
IDependencyInjectionContainer container = DependencyInjectionContainerImpl.getInstance();
        
var dependency4 = (Dependency4)container.getManagedService(Dependency4.class);
dependency4.printRandomNumbers();
```

O método getManagedService também permite fornecer dependências adicionais que serão usadas caso não seja possível encontrar a dependência solicitada no Container.

## Ciclos De Vida

Ao configurar serviços em um contêiner de injeção de dependência (DI), é importante entender os diferentes ciclos de vida disponíveis para as dependencias. Os ciclos de vida determinam como as instâncias dos serviços são criadas e gerenciadas. Aqui estão os três mais comuns: Scoped, Transient e Singleton.

### Scoped (.addScoped)
- **Instância Única por Solicitação (por Escopo)**: Cada solicitação (por exemplo, uma solicitação HTTP em uma aplicação da web) recebe uma instância única do serviço.

### Transient (.addTransient)
- **Nova Instância a Cada Solicitação**: Uma nova instância do serviço é criada sempre que é solicitada.

### Singleton (.addSingleton)
- **Instância Única por Aplicação**: A mesma instância do serviço é usada em toda a aplicação.

Em resumo, o escopo Scoped é útil para manter o estado dentro de uma solicitação, o Transient é adequado para serviços leves e sem estado, e o Singleton é ideal para serviços que precisam ser compartilhados entre diferentes partes da aplicação e/ou que são caros de serem criados.

## Contribuição

Contribuições são bem-vindas! Sinta-se à vontade para abrir issues ou enviar pull requests para melhorias.

1. Fork o repositório
2. Crie uma nova branch: `git checkout -b minha-branch`
3. Faça suas alterações e commit: `git commit -m 'Minha nova feature'`
4. Faça o push para a branch: `git push origin minha-branch`
5. Abra um pull request

## Licença

Este projeto está licenciado sob os termos da licença GNU. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

# Manual da Aplicacao Cliente e Interface Grafica

Este documento descreve as funcionalidades e as instrucoes de execucao do modulo Cliente desenvolvido para o sistema de monitoramento de redes.

## 1. Instrucoes de Execucao

A execucao do modulo cliente depende de que o servidor ja esteja rodando. Siga os passos abaixo em terminais separados para garantir o funcionamento correto.

### Passo 1: Iniciar o Servidor
Certifique-se de que o seu terminal esta no diretorio raiz do codigo-fonte (`src`).
Compile e inicie o servidor:

```bash
javac server/ServerMain.java
java server.ServerMain
```

### Passo 2: Iniciar o Cliente
Em um novo terminal, tambem no diretorio `src`, inicie a interface grafica do cliente:

```bash
javac client/ClientMain.java
java client.ClientMain
```

Voce pode iniciar multiplos clientes executando o mesmo comando em diferentes janelas, atribuindo nomes de maquinas diferentes na tela de login para simular uma rede maior.

## 2. Visao Geral da Interface Grafica

O cliente foi desenvolvido utilizando Java Swing com o Look and Feel Nimbus, garantindo um visual moderno sem depender de bibliotecas externas.

### 2.1 Tela de Conexao
Atua como ponto de entrada para o registro da maquina.
- **IP do Servidor**: O endereco IPv4 onde o `ServerMain` esta rodando (padrao 127.0.0.1).
- **Nome da Maquina**: Identificador unico (ex: PC-01). Nomes iguais poderao sobrescrever dados no servidor dependendo da implementacao.

Apos clicar em "Conectar", o cliente realiza o handshake com o servidor via protocolo TCP (rota `REGISTER`).

### 2.2 Painel de Monitoramento Local
Localizado na esquerda da tela principal, este painel exibe o status de consumo de recursos simulado para a maquina atual.
- **Barras de Progresso Customizadas**: O componente `MetricBar` varia a cor dinamicamente (Verde, Amarelo, Vermelho) para indicar as zonas de alerta do uso de RAM e Disco.
- **Grafico Historico de CPU**: O componente `CpuLineChart` utiliza renderizacao Graphics2D pura para tracar as ultimas 30 variacoes da CPU em tempo real, permitindo rapida visualizacao de picos de processamento.

### 2.3 Visao Geral da Rede (Dashboard)
Painel direito contendo a listagem global de todas as maquinas conectadas.
- **Auto-Refresh TCP**: Uma thread assincrona envia pacotes `GET_MACHINE_LIST` a cada 3 segundos, mantendo a tabela atualizada sem travamentos na interface visual.
- **Pop-up de Detalhes**: Ao realizar duplo-clique sobre a linha correspondente a uma maquina, o cliente executa uma chamada TCP `GET_MACHINE_DETAILS` que retorna uma janela modal com o recorte em tempo real daquele terminal, incluindo o horario de recepcao do ultimo heartbeat.

### 2.4 Terminal de Eventos (Logs)
Painel inferior de estilo console que documenta e audita em tempo real as transacoes de rede e metadados vitais:
- Status de pacotes UDP disparados (`STATUS` e `HEARTBEAT`).
- Horarios de resposta do servidor.
- Status do painel de uptime continuo.

## 3. Comportamento Assincrono

As transacoes de rede foram isoladas da Thread principal da interface grafica para evitar engasgos (flickers ou UI freezes):
- Operacoes de envio de métricas (UDP) e contagem de tempo utilizam objetos `TimerTask`.
- Rotinas de recepcao e rendering de elementos da tela utilizam a combinacao nativa do `SwingWorker` e o metodo `SwingUtilities.invokeLater`, que alinham as respostas assincronas de volta no barramento visual sem gargalos de CPU.

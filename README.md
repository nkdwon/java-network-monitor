# Java Network Monitor

Sistema distribuído de monitoramento de máquinas em rede desenvolvido em Java utilizando TCP, UDP, multithreading e interface gráfica Java Swing.

---

## Informações Acadêmicas

**Instituição:** Pontifícia Universidade Católica de Minas Gerais (PUC Minas)

**Disciplina:** Redes de Computadores I

**Professor:** Max do Val Machado

**Semestre:** 5º

---

## Integrantes do Grupo

1. Felipe Barros Ratton de Almeida
2. Laura Menezes Heráclito Alves
3. Mateus Ribeiro Fernandes
4. Matheus Henrique Gonçalves
5. Vitor de Meira Gomes

---

## Sobre o Projeto

O projeto consiste no desenvolvimento de uma aplicação distribuída para monitoramento de máquinas em rede.

Cada cliente representa uma máquina monitorada e se comunica com um servidor central responsável por receber, processar e exibir informações de status dos dispositivos conectados.

A aplicação foi desenvolvida com o objetivo de demonstrar conceitos fundamentais de redes de computadores, incluindo comunicação cliente-servidor, utilização de protocolos TCP e UDP, processamento concorrente com multithreading e funcionamento de aplicações distribuídas em redes distintas.

---

## Objetivos

* Desenvolver uma aplicação cliente-servidor em Java.
* Implementar comunicação utilizando o protocolo TCP.
* Implementar comunicação utilizando o protocolo UDP.
* Utilizar multithreading para atender múltiplos clientes simultaneamente.
* Desenvolver uma interface gráfica utilizando Java Swing.
* Validar a comunicação entre redes distintas utilizando roteadores.
* Analisar o tráfego gerado através do Wireshark.

---

## Tecnologias Utilizadas

### Linguagem

* Java

### Interface Gráfica

* Java Swing

### Comunicação em Rede

* TCP (Transmission Control Protocol)
* UDP (User Datagram Protocol)

### Concorrência

* Threads
* Executor Services

### Ferramentas e Infraestrutura Utilizadas

- Roteadores físicos para validação da comunicação entre redes distintas
- Cisco Packet Tracer para simulação da topologia de rede
- Wireshark para análise do tráfego TCP e UDP

### Controle de Versão

* Git
* GitHub

---

## Estrutura do Projeto

```text
src/
│
├── client/
│   ├── ClientMain.java
│   ├── network/
│   │   ├── TcpClient.java
│   │   └── UdpClient.java
│   │
│   ├── gui/
│   │   ├── ConnectionScreen.java
│   │   ├── MonitorScreen.java
│   │   └── components/
│   │
│   └── model/
│       └── MachineStatus.java
│
├── common/
│   ├── dto/
│   │   ├── HeartbeatMessage.java
│   │   ├── StatusMessage.java
│   │   └── RegisterMessage.java
│   │
│   ├── protocol/
│   │   └── MessageType.java
│   │
│   └── utils/
│       └── Constants.java
│
└── server/
    ├── ServerMain.java
    │
    ├── network/
    │   ├── TcpServer.java
    │   ├── UdpServer.java
    │   └── ClientHandler.java
    │
    ├── service/
    │   └── MonitoringService.java
    │
    └── model/
        └── ConnectedMachine.java
```

### Cliente

Responsável pela interface gráfica e comunicação com o servidor.

### Servidor

Responsável pelo gerenciamento das conexões e processamento das informações recebidas.

### Common

Contém classes compartilhadas entre cliente e servidor, como mensagens, protocolos e utilitários.

---

## Requisitos do Trabalho

O projeto atende aos seguintes requisitos da disciplina:

- Aplicação desenvolvida em Java;
- Interface gráfica utilizando Java Swing;
- Utilização de TCP;
- Utilização de UDP;
- Utilização de multithreading;
- Comunicação entre redes distintas;
- Configuração e utilização de roteadores físicos;
- Desenvolvimento e validação de topologia no Cisco Packet Tracer;
- Análise de tráfego utilizando Wireshark;
- Testes de comunicação cliente-servidor em ambiente real de rede.

---

## Vídeo de Apresentação


https://github.com/user-attachments/assets/2a7a88f4-01b2-4e35-81fa-32a659e30a5d




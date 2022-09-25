# Exemplo de exclusão mútua utilizando socket TCP

## Introdução
Desenvolvido para *Sistemas Distribuídos (FURB BCC)* com Java. No exemplo o servidor aguarda a conexão de clientes para acessar o quadro de avisos. Caso um cliente B conecte enquanto outro cliente A estiver acessando, é solicitado para B que aguarde até o fim do acesso de A.

## Execução (utilizando CMD)
A porta do servidor está fixa como 90. Ao executar o cliente é necessário passar como parâmetro o endereço do servidor.

### Ações para executar no cliente
1- "LER": mostra a lista de mensagens.<br>
2 - "ESCREVER": adiciona "escreveu" à lista de mensagens.<br>
3 - "SAIR": desconecta o cliente do servidor.

### Passos
- Compilar arquivos

```
javac *.java
```

- Executar servidor
```
java ServidorTCP
```
- Abrir outro CMD e executar cliente (para mais clientes repetir esse passo)
```
java ClienteTCP 127.0.0.1
```

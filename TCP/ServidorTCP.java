package TCP;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ServidorTCP {
	public static void main(String[] args) throws Exception {
		String numeroPorta;
		ServerSocket serverSocket;
		Socket clientSocket;
		PrintWriter out;
		BufferedReader in;
		String comando;
		String msgBoard = "";

		/* Parametros */
		//numeroPorta = args[0];
		numeroPorta = "10";

		/* Inicializacao do server socket TCP */
		serverSocket = new ServerSocket(Integer.valueOf(numeroPorta).intValue());

		while (true) {
			/* Espera por um cliente */
			clientSocket = serverSocket.accept();

			System.out.println("Novo cliente: " + clientSocket.toString());

			/* Preparacao dos fluxos de entrada e saida */
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out.println("Conectado!");

			/* Recuperacao dos comandos */
			while ((comando = in.readLine()) != null) {
				System.out.println("Comando recebido: [" + comando + "]");
				/* Se comando for "HORA" */
				if (comando.equals("LER")) {
					out.println(msgBoard);
				}else if(comando.contains("ESCREVER:")) {
					msgBoard += comando.split(":")[1] + "; ";
					out.println();
				}
				else if (comando.equals("SAIR")) {
					break;
				} else {
					out.println("Comando Desconhecido");
				}
			}
			/* Finaliza tudo */
			System.out.print("Cliente desconectando... ");
			out.close();
			in.close();
			clientSocket.close();
			System.out.println("ok");
		}

	}
}
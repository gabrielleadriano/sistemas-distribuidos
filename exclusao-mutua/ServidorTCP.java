import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ServidorTCP {
	public static void main(String[] args) throws Exception {
		String numeroPorta;
		ServerSocket serverSocket;
		Socket newClientSocket;
		Socket clientSocket;
		PrintWriter out;
		BufferedReader in;
		String comando;
		int currentIndex = 0;
		List<Socket> clientList = new ArrayList<>();
		String msgBoard = "";

		/* Parametros */
		numeroPorta = args[0];

		/* Inicializacao do server socket TCP */
		serverSocket = new ServerSocket(new Integer(numeroPorta).intValue());

		while (true) {
			/* Espera por um cliente */
			newClientSocket = serverSocket.accept();
			clientList.add(newClientSocket);

			if (currentIndex < clientList.size() - 1) {
				System.out.println("dentro if");
				String msgAwait = "AGUARDE, VOCÊ ESTÁ NA POSICAO: " + clientList.size();

				out = new PrintWriter(newClientSocket.getOutputStream(), true);
				out.println(msgAwait);
			}
			clientSocket = clientList.get(currentIndex);

			System.out.println("Novo cliente: " + serverSocket.toString());

			/* Preparacao dos fluxos de entrada e saida */
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			/* Recuperacao dos comandos */
			while ((comando = in.readLine()) != null) {
				System.out.println("Comando recebido: [" + comando + "]");
				/* Se comando for "HORA" */
				if (comando.equals("LER")) {
					out.println(msgBoard);
				}else if(comando.contains("ESCREVER:")) {
					msgBoard += "\n" + comando.split(":")[1];
					break;
				}
				else if (comando.equals("SAIR")) {
					currentIndex++;
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

// Bianca Krug de Jesus, Gabrielle Alice Adriano, Vinícius Mueller Landi
import java.net.*;
import java.io.*;
import java.util.*;

public class ServidorTCP extends Thread {
	public static int currentIndex = 0;
	public static List<Socket> clientList = new ArrayList<>();
	public static ServerSocket serverSocket;
	public static Socket clientSocket;
	public static Socket newClientSocket;
	public static PrintWriter out;
	public static BufferedReader in;

	public void run() {
		while (clientList.size() > 0) {
			try {
				newClientSocket = serverSocket.accept();

				if (currentIndex == 0 && clientList.size() == 0) {
					clientList.add(newClientSocket);
					break;
				}
				clientList.add(newClientSocket);
				analizeClient();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void analizeClient() throws Exception {
		if (currentIndex < clientList.size() - 1) {
			String msgAwait = "AGUARDE, VOCÊ ESTÁ NA POSICAO: " + clientList.size();

			PrintWriter outNewClient = new PrintWriter(newClientSocket.getOutputStream(), true);
			BufferedReader inNewClient = new BufferedReader(new InputStreamReader(newClientSocket.getInputStream()));

			outNewClient.println(msgAwait);
			inNewClient.readLine();
		}
	}

	public static void main(String[] args) throws Exception {
		String numeroPorta = "9090";
		String comando;
		String msgBoard = "";

		/* Inicializacao do server socket TCP */
		serverSocket = new ServerSocket(new Integer(numeroPorta).intValue());

		while (true) {
			/* Se existe uma thread ainda ativa, o proximo cliente deve ser aceito por ela */
			if (clientList.size() == 0 && Thread.activeCount() == 1) {
				newClientSocket = serverSocket.accept();
				clientList.add(newClientSocket);
			}


			if (clientList.size() > 0) {
				ServidorTCP thread = new ServidorTCP();
				thread.start();
				clientSocket = clientList.get(currentIndex);

				System.out.println("Novo cliente: " + serverSocket.toString());

				/* Preparacao dos fluxos de entrada e saida */
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

				/* Recuperacao dos comandos */
				while ((comando = in.readLine()) != null) {
					System.out.println("Comando recebido: [" + comando + "]");

					if (comando.equals("LER")) {
						System.out.println("LER " + msgBoard);
						out.println(msgBoard);
					} else if (comando.equals("ESCREVER")) {
						msgBoard += "escreveu;";
						out.println("ESCRITO");
					} else if (comando.contains("SOLICITANDO ACESSO") || comando.contains("AGUARDANDO")) {
						out.println("ACESSO PERMITIDO");
					} else if (comando.equals("SAIR")) {
						currentIndex++;
						if (currentIndex == clientList.size()) {
							currentIndex = 0;
							clientList.clear();
							newClientSocket.close();
						}
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
			}
		}
	}
}

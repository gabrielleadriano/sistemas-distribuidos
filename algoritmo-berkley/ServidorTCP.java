
// Bianca Krug de Jesus, Gabrielle Alice Adriano, Vinícius Mueller Landi
import java.net.*;
import java.io.*;
import java.util.*;

public class ServidorTCP extends Thread {
	public static int currentIndex = 0;
	public static List<Socket> clientList = new ArrayList<>();
	public static int diferencas = 0;
	public static ServerSocket serverSocket;
	public static Socket clientSocket;
	public static Socket newClientSocket;
	public static PrintWriter out;
	public static BufferedReader in;

	public static void main(String[] args) throws Exception {
		String numeroPorta = "9090";
		String comando;
		String msgBoard = "";
		Random gerador = new Random();
		int horarioServidor = gerador.nextInt(86400);

		/* Inicializacao do server socket TCP */
		serverSocket = new ServerSocket(new Integer(numeroPorta).intValue());
		// TIMER
		//setTimer();
		
		Timer cronometro = new Timer();
		TimerTask tarefa = new TimerTask() {
			
			@Override
			public void run() {
				int media = Math.round(diferencas / (clientList.size()))
						
				for(Socket cliente : clientList) {
					clientSocket = cliente;
					out = new PrintWriter(clientSocket.getOutputStream(), true);
					out.println(media);
					
					/* Finaliza tudo */
					System.out.print("Cliente desconectando... ");
					out.close();
					in.close();
					clientSocket.close();
				}
				clientList.clear();
				diferencas = 0;
			}
		};
		
		int milissegundos = 30000;
		cronometro.schedule(tarefa, milissegundos);

		while (true) {
			clientSocket = serverSocket.accept();

			/* Preparacao dos fluxos de entrada e saida */
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			/* Recuperacao dos comandos */
			while (true) {
				comando = in.readLine();
				System.out.println("Comando recebido: [" + comando + "]");

				if (comando.equals("DIFERENCA:")) {
					clientList.add(clientSocket);
					int diferenca = Integer.parseInt(comando.split(" ")[1]);
					diferencas += diferenca;
					break;
				} else if (comando.contains("SOLICITANDO ACESSO")) {
					out.println(horarioServidor);
				}
			}
		}
	}

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

	/*
	 * private void setTimer() { Timer cronometro = new Timer(); TimerTask tarefa =
	 * new TimerTask() {
	 * 
	 * @Override public void run() { int media = Math.round(diferencas /
	 * (clientList.size()))
	 * 
	 * for(Socket cliente:clientList) { clientSocket = cliente; out = new
	 * PrintWriter(clientSocket.getOutputStream(), true); out.println(media);
	 * 
	 * Finaliza tudo System.out.print("Cliente desconectando... "); out.close();
	 * in.close(); clientSocket.close(); } clientList.clear(); diferencas = 0; } }
	 * int milissegundos = 30000; cronometro.schedule(tarefa, milissegundos); }
	 */

}

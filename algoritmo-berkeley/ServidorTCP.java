
// Bianca Krug de Jesus, Gabrielle Alice Adriano, Vin�cius Mueller Landi
import java.net.*;
import java.io.*;
import java.util.*;

public class ServidorTCP extends Thread {

	public static int currentIndex = 0;
	public static int diferencas = 0;
	public static int horarioServidor;
	public static ServerSocket serverSocket;
	public static Socket clientSocket;
	public static PrintWriter out;
	public static BufferedReader in;
	public static List<Socket> clientList = new ArrayList<>();

	public static void main(String[] args) throws Exception {
		String numeroPorta = "9090";
		String comando;
		String msgBoard = "";
		Random gerador = new Random();
		horarioServidor = gerador.nextInt(86400);
		printHorario(horarioServidor);

		/* Inicializacao do server socket TCP */
		serverSocket = new ServerSocket(new Integer(numeroPorta).intValue());

		setTimer();

		while (true) {
			clientSocket = serverSocket.accept();

			/* Preparacao dos fluxos de entrada e saida */
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			/* Recuperacao dos comandos */
			while (true) {
				comando = in.readLine();
				System.out.println("Comando recebido: [" + comando + "]");

				if (comando.contains("DIFERENCA:")) {
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

	private static void setTimer() {
		Timer cronometro = new Timer();
		TimerTask tarefa = new TimerTask() {

			@Override
			public void run() {
				if (clientList.size() <= 0) {
					return;
				}
				int media = Math.round(diferencas / (clientList.size() + 1));

				horarioServidor = horarioServidor + media;
				printHorario(horarioServidor);

				for (Socket cliente : clientList) {
					clientSocket = cliente;
					try {
						out = new PrintWriter(clientSocket.getOutputStream(), true);

						out.println(media);

						clientSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				clientList.clear();
				diferencas = 0;
				/* Finaliza tudo */
				
				System.out.println("Cliente desconectando... ");
				out.close();
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

		int milissegundos = 30000;
		cronometro.schedule(tarefa, milissegundos, milissegundos);
	}

	private static void printHorario(int s) {
		int hours = (int) Math.floor(s / 3600);
		int minutes = (int) Math.floor((s - (hours * 60 * 60)) / 60);
		int seconds = s - (hours * 60 * 60) - (minutes * 60);

		System.out.println("Horario: " + hours + ":" + minutes + ":" + seconds);
	}

}
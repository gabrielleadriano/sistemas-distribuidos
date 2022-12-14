
// Bianca Krug de Jesus, Gabrielle Alice Adriano, Vin�cius Mueller Landi
import java.io.*;
import java.net.*;
import java.util.Random;

public class ClienteTCP {

	public static void main(String[] args) throws Exception {
		Socket socket;
		BufferedReader in;
		PrintWriter out;
		BufferedReader inReader;
		String mensagemEnviar;
		String numeroPorta = "9090";

		/* Pegar parametros */
		String nomeServidor = "127.0.0.1";

		/* Inicializacao de socket TCP */
		socket = new Socket(nomeServidor, new Integer(numeroPorta).intValue());

		Random gerador = new Random();
		int s = gerador.nextInt(86400);
		printHorario(s);

		/* Inicializacao dos fluxos de entrada e saida */
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);

		/* Abertura da entrada padrao */
		inReader = new BufferedReader(new InputStreamReader(System.in));

		out.println("SOLICITANDO ACESSO");
		int respostaAcesso = Integer.valueOf(in.readLine());

		int diferenca = s - respostaAcesso;

		out.println("DIFERENCA: " + diferenca);

		int retorno = Integer.valueOf(in.readLine());

		s = (s + (retorno + (diferenca * -1)));

		printHorario(s);

		/* Finaliza tudo */
		out.close();
		in.close();
		socket.close();
	}

	private static void printHorario(int s) {
		int hours = (int) Math.floor(s / 3600);
		int minutes = (int) Math.floor((s - (hours * 60 * 60)) / 60);
		int seconds = s - (hours * 60 * 60) - (minutes * 60);

		System.out.println("Horario: " + hours + ":" + minutes + ":" + seconds);
	}
}
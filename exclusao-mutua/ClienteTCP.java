// Bianca Krug de Jesus, Gabrielle Alice Adriano, Vinícius Mueller Landi
import java.io.*;
import java.net.*;

public class ClienteTCP {

	public static void main(String[] args) throws Exception {
		Socket socket;
		BufferedReader in;
		PrintWriter out;
		BufferedReader inReader;
		String mensagemEnviar;
		String numeroPorta = "9090";

		/* Pegar parametros */
		String nomeServidor = args[0];

		/* Inicializacao de socket TCP */
		socket = new Socket(nomeServidor, new Integer(numeroPorta).intValue());

		/* Inicializacao dos fluxos de entrada e saida */
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);

		/* Abertura da entrada padrao */
		inReader = new BufferedReader(new InputStreamReader(System.in));

		out.println("SOLICITANDO ACESSO");
		String respostaAcesso = in.readLine();
		if (respostaAcesso.contains("AGUARDE")) {
			System.out.println("Retornou: [" + respostaAcesso + "]");
			out.println("AGUARDANDO");
			while (true) {
				if ((respostaAcesso = in.readLine()).contains("PERMITIDO")) {
					break;
				}
			}
		}

		System.out.println("Retornou: [" + respostaAcesso + "]");

		System.out.print("Msg: ");
		while ((mensagemEnviar = inReader.readLine()) != null) {

			/* Envio da mensagem */
			out.println(mensagemEnviar);

			/* Recebimento da resposta do servidor */
			String resposta = in.readLine();
			if (resposta == null)
				break;

			/* Imprime na tela o retorno */
			System.out.println("Retornou: [" + resposta + "]");
			System.out.print("Msg: ");

		}

		/* Finaliza tudo */
		out.close();
		in.close();
		socket.close();
	}
}

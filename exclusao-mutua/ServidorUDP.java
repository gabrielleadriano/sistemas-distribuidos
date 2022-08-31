import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.lang.Thread;

public class ServidorUDP {

	public static void main(String args[]) throws Exception {
		byte[] buf;
		DatagramSocket socket;
		DatagramPacket request, reply;
		int currentIndex = 0;
		String comando;
		List<DatagramPacket> requestList = new ArrayList<>();

		/* Parametros */
		String numeroPorta = args[0];

		/* Inicializacao do socket UDP */
		socket = new DatagramSocket(new Integer(numeroPorta).intValue());

		/* Laco de recebimento de datagramas */
		while (true) {
			request = null;
			reply = null;
			buf = new byte[1024];

			/* Preparacao do Datagrama de Recepcao */
			request = new DatagramPacket(buf, buf.length);

			/* Recepcao bloqueante dos dados */
			socket.receive(request);
			
			requestList.add(request);

			if(currentIndex < requestList.size() - 1) {
				String msgAwait = "AGUARDE, VOCÊ ESTÁ NA POSICAO: " + requestList.size();
				reply = new DatagramPacket(msgAwait.getBytes(), msgAwait.getBytes().length, requestList.get(requestList.size() - 1).getAddress(),
						requestList.get(requestList.size() - 1).getPort());
			}
			
			/* Recuperacao do comando */
			comando = new String(request.getData(), 0, request.getLength());

			if ("RECEBIDO".indexOf(comando) != -1) {
				System.out.println(comando + " por " + requestList.get(currentIndex).getAddress() + ":" + requestList.get(currentIndex).getPort());
				currentIndex++;
			} else {
				Thread.sleep(5000);

				/* Se comando for "HORA" */
				if ("HORA".indexOf(comando) != -1) {
					/* Prepara a hora para envio */
					String hora = new SimpleDateFormat("d MMM yyyy HH:mm:ss").format(new Date());

					/* Cria datagrama com a resposta */
					reply = new DatagramPacket(hora.getBytes(), hora.getBytes().length, requestList.get(currentIndex).getAddress(),
							requestList.get(currentIndex).getPort());

					/* Se comando NAO for "HORA" */
				} else {

					/* Cria datagrama com a resposta */
					reply = new DatagramPacket("Comando Desconhecido".getBytes(),
							"Comando Desconhecido".getBytes().length, requestList.get(currentIndex).getAddress(), requestList.get(currentIndex).getPort());
				}

				/* Envia resposta pelo socket UDP */
				socket.send(reply);
			}

		}
	}
}

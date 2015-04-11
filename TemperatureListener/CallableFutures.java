import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;




public class CallableFutures {

	private static final int THREADS=10;
	private static ExecutorService threadPool =Executors.newFixedThreadPool(THREADS);
	private static ExecutorService readerPool = Executors.newFixedThreadPool(THREADS);

	static AquariumList list = new AquariumList();
	public static void main(String[] args) throws IOException {


		ServerSocket serverSocket= new ServerSocket(1235);
		long sTime = System.currentTimeMillis();
		double elapsedTime=0L;
		while(true){
			Socket socket = serverSocket.accept();
			threadPool.execute(new ConnectionHandler(socket));
			elapsedTime=((new Date()).getTime() - sTime) /1000.0 ;
			if(elapsedTime>30){
				readerPool.execute(new WriteDatabasePeriodically(list));
				elapsedTime=0.0;
				sTime = System.currentTimeMillis();
			}
		}
	}

	public static class ConnectionHandler implements Runnable {

		private Socket socket;

		public ConnectionHandler(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			BufferedReader reader = null;

			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				// The read loop. Code only exits this loop if connection is lost / client disconnects
				while(true) {
					String line = reader.readLine();
					if(line == null) break;
					System.out.println(line);
					line=decrypth(line);
					System.out.println(line);
					String[] tokens=line.split(",");
					Aquarium aquarium= new Aquarium(Integer.parseInt(tokens[0]), tokens[1], Integer.parseInt(tokens[2]));
					list.add(aquarium);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidAlgorithmParameterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if(reader != null) reader.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
		
		public String decrypth(String receivedString) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
			//Desolve the strings mentioned in System and Architecture and Communication.txt
			StringBuilder encriptedString=new StringBuilder();
			encriptedString.append(receivedString.substring(0, 10));
			encriptedString.append(receivedString.substring(18));
			
			String hash_word=receivedString.substring(10,18);

			// Instantiate the cipher
			final SecretKeySpec key = new SecretKeySpec(hash_word.getBytes("ISO-8859-1"), "DES");
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(hash_word.getBytes());
			//specify that its decryption
			Cipher cipher = Cipher.getInstance("DES/CFB8/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
			//get the base64 decoding
			byte[] decordedValue = new Base64().decodeBase64(encriptedString.toString());
			byte[] decValue=cipher.doFinal(decordedValue);
			//convert and return
			String decyriptedString=new String(decValue,"ISO-8859-1");
			
			return decyriptedString;
		}
	}
}
import java.io.DataOutputStream;
import java.net.Socket;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;



public class SendCommand {




	public void encryptAndSend(User user,int command) throws Exception {
		//First 8 characters of salt
		String salt8=user.getSalt().substring(0, 8);	

		// Instantiate the cipher
		final SecretKeySpec key = new SecretKeySpec(salt8.getBytes("ISO-8859-1"), "DES");
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(salt8.getBytes());

		Cipher cipher = Cipher.getInstance("DES/CFB8/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
		
		//encryp the string format of userid,hash_name,command
		StringBuilder commandString= new StringBuilder();
		commandString.append(user.getId());
		commandString.append(',');
		//This will be the name of the fish
		commandString.append(user.getSalt());
		commandString.append(',');
		commandString.append(command);
		
		byte[] binaryData = cipher.doFinal(commandString.toString().getBytes("ISO-8859-1"));

		String encryptedString= new String(Base64.encodeBase64(binaryData), "ISO-8859-1");
		
		Socket socket = new Socket("localhost", 1234);
		DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
		
		dOut.writeUTF(encryptedString);
		
		socket.close();
		System.out.println("Command sent correctly");

	}



}

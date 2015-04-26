package mete.mertkan.objects;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;




public class SendCommand {

	public void encryptAndSend(User user,int command,Aquarium aquarium){

		try {
			//First 8 characters of salt
			String hash_word=user.getHash_name();	

			// Instantiate the cipher
			SecretKeySpec key;
			key = new SecretKeySpec(hash_word.getBytes("ISO-8859-1"), "DES");
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(hash_word.getBytes());

			Cipher cipher = Cipher.getInstance("DES/CFB8/NoPadding");
			cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);

			//encryp the string format of userid,hash_name,command
			StringBuilder commandString= new StringBuilder();
			commandString.append(user.getId());
			commandString.append(',');
			//This will be the name of the fish
			commandString.append(user.getHash_name());
			commandString.append(',');
			commandString.append(command);

			byte[] binaryData = cipher.doFinal(commandString.toString().getBytes("ISO-8859-1"));

			String encryptedString= new String(Base64.encodeBase64(binaryData), "ISO-8859-1");

			Socket socket = new Socket(aquarium.getIp_address(), 1234);
			DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());

			dOut.writeUTF(encryptedString);

			socket.close();
			System.out.println("Command sent correctly");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}


}

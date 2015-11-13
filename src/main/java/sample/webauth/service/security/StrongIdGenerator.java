package sample.webauth.service.security;

import java.security.SecureRandom;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Base64.Encoder;

/**
 * Cryptographically strong random id generator
 *
 */
public class StrongIdGenerator
{
	private SecureRandom randomGenerator;
	private MessageDigest msgDigest;
	private Base64.Encoder encoder;

	public StrongIdGenerator() throws Exception {

		try {
			this.randomGenerator = SecureRandom.getInstance("SHA1PRNG");
			this.msgDigest = MessageDigest.getInstance("SHA-256");
			this.encoder = Base64.getUrlEncoder();
		}
		catch (Exception ex) {
			throw new Exception("Error initializing id generator");
		}
	}

	/*
		Get random identifier
	*/
	public String getId() {
		String randomNumber = new Integer(this.randomGenerator.nextInt()).toString();
		byte[] bytes = this.msgDigest.digest(randomNumber.getBytes());
        return this.encoder.encodeToString(bytes);
	}
}	

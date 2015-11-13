package sample.webauth.service.security;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Base64.Decoder;

// adapted from https://www.owasp.org/index.php/Hashing_Java
public class PasswordHasher
{
	private final static int ITERATION_NUMBER = 1000;

	private SecureRandom randomGenerator;
	
	public PasswordHasher() throws Exception {
		try {
			this.randomGenerator = SecureRandom.getInstance("SHA1PRNG");
		}
		catch (Exception ex) {
			throw new Exception("Error initializing password hasher: " + ex.getMessage());
		}
	}
 
	/*
		Validate if a password matches with a hashed password
	*/
	public boolean check(String password, String hashedPassword) throws Exception {

		try {
			
			String[] parts = hashedPassword.split(":");

			if (parts.length != 2) {
				return false;
			}
			String digest = parts[0];
			String salt = parts[1];

			Base64.Decoder decoder = Base64.getDecoder();
			byte[] bDigest = decoder.decode(digest);
			byte[] bSalt = decoder.decode(salt);

			// Compute the new DIGEST
			byte[] proposedDigest = getHash(ITERATION_NUMBER, password, bSalt);

			return Arrays.equals(proposedDigest, bDigest);
		}
		catch (Exception ex) {
			throw new Exception("Error checking passwords: " + ex.getMessage());
		}
	}

	/*
		Calculate the hash of a password
	*/
	public String hash(String password) throws Exception {

		try {

			// Salt generation 64 bits long
			byte[] bSalt = new byte[8];
			this.randomGenerator.nextBytes(bSalt);
			// Digest computation
			byte[] bDigest = getHash(ITERATION_NUMBER, password, bSalt);
			Base64.Encoder encoder = Base64.getEncoder();
			String sDigest = encoder.encodeToString(bDigest);
			String sSalt = encoder.encodeToString(bSalt);

			return sDigest + ":" + sSalt;
		}
		catch (Exception ex) {
			throw new Exception("Error hashing password: " + ex.getMessage());
		}
   }
 
	/**
	* From a password, a number of iterations and a salt,
	* returns the corresponding digest
	* @param iterationNb int The number of iterations of the algorithm
	* @param password String The password to encrypt
	* @param salt byte[] The salt
	* @return byte[] The digested password
	* @throws NoSuchAlgorithmException If the algorithm doesn't exist
	*/
	public byte[] getHash(int iterationNb, String password, byte[] salt) throws Exception {

		MessageDigest msgDigest = MessageDigest.getInstance("SHA-1");
		msgDigest.reset();
		msgDigest.update(salt);
		byte[] input = msgDigest.digest(password.getBytes("UTF-8"));

		for (int i = 0; i < iterationNb; i++) {
			msgDigest.reset();
			input = msgDigest.digest(input);
		}

		return input;
	}
 }
package com.hello.forum.beans;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class SHA {

	/**
	 * SHA-256 암호화 함
	 * @param source 원본
	 * @param salt(String) SALT 값
	 * @return
	 */
	public String getEncrypt(String source, String salt) {
		return getEncrypt(source, salt.getBytes());
	}
	
	/**
	 * SHA-256 암호화 함
	 * @param source 원본
	 * @param salt(byte[]) SALT 값
	 * @return
	 */
	public String getEncrypt(String source, byte[] salt) {
		
		String result = "";
		
		byte[] a = source.getBytes();
		byte[] bytes = new byte[a.length + salt.length];
		
		System.arraycopy(a, 0, bytes, 0, a.length); //배열에password값과 salt의 값을 bytes에 복사한다
		System.arraycopy(salt, 0, bytes, a.length, salt.length);
		
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256"); // 엄청나게 큰 소수를 이용해서 암호화 함
			md.update(bytes); // bytes에 암호화함
			
			byte[] byteData = md.digest();  
			
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				//암호화된 bytes를 가져와서 한번 더 16진수 난수값을 집어넣는다(꼬아버림)
				sb.append(Integer.toString((byteData[i] & 0xFF) + 256, 16).substring(1)); 
			}
			
			result = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * SALT를 얻어온다.
	 * @return
	 */
	public String generateSalt() {
		Random random = new Random();
		
		byte[] salt = new byte[8];
		random.nextBytes(salt);
		
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < salt.length; i++) {
			// byte 값을 Hex 값으로 바꾸기.
			sb.append(String.format("%02x",salt[i]));
		}
		
		return sb.toString();
	}
}

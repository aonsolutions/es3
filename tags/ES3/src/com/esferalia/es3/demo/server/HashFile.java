package com.esferalia.es3.demo.server;

import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
 
/**
 * @author JF
 * Esta clase lee un archivo y convierte su contenido en MD5 o SHA
 * Imprime por pantalla el resultado.
 */
public class HashFile {
 
	public static void main(String[] args){
		md("D:/especializacion/PruebaHash.txt", "MD5");
	}
 
	public static void md(String f, String hash){
		try{
			//Obtengo el archivo
			FileInputStream fis = new FileInputStream(f);
			//Obtiene una instancia del algoritmo para aplicar el hash
			MessageDigest md = MessageDigest.getInstance(hash);
			DigestInputStream dis = new DigestInputStream(fis, md);
 
			//Creo un buffer para almacenar la info del archivo
			byte[] b = new byte[32*1024];
 
			int i = 0;
			do{
				i = dis.read(b, 0, 32*1024);
			}while(i == 32*1024);
 
			//Aplica el hash mediante la funciòn digest()
			//y convierte a hexadecimal
			String res = byteArrayToHexString(md.digest());
			dis.close();
 
			System.out.println("resultado:  " + res);
		}catch(Exception e){
			System.out.println("Error!");
		}
	}
 
	/**
	 * byteArrayToHexString
	 * Convierte un array de bytes a un String hexadecimal
	 */
	public static String byteArrayToHexString(byte[] b){
		StringBuilder sb = new StringBuilder(b.length * 2);
		for(int i = 0; i < b.length; i++){
			int v = b[i] & 0xff;
			if(v < 16){
				sb.append("0");
			}
			sb.append(Integer.toHexString(v));
		}
		return sb.toString();
	}
}

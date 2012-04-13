package com.esferalia.es3.demo.server.directory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

public class DirectoryManager {
	
	// FIXME Determinar un path para las pruebas
	public static final String BASE_PATH = "/srv/www/lighttpd/es3/mission/";
			// "C:\\workspace\\ES3\\src\\com\\esferalia\\es3\\demo\\public\\mission\\";
			// "/srv/www/lighttpd/es3/mission/";
	public static final String TEMP_DIR = "/srv/www/lighttpd/es3/temp/";
			// "C:\\temp\\";
			// "/srv/www/lighttpd/es3/temp/";
	
	// FIXME Server path Windows(true) o CentOS(false)
	private boolean localhost = false;

	public DirectoryManager() {
	}

/*	public void listAllFiles(String directory, CustomNode parent) throws IOException {
		// list all the files in the directory
		File[] children = new File(directory).listFiles();
		// loop through each
		for (int i = 0; i < children.length; i++) {
			FolderOrFile fof = new FolderOrFile(children[i].getName(),
					children[i].getPath());
			CustomNode node = new CustomNode(fof);
			if (children[i].isDirectory()) {
				// add as a child node
				parent.add(node);
				// call again for the subdirectory
				listAllFiles(children[i].getPath(), node);

			} else if (!(children[i].getName().endsWith(".js") || children[i].getName().endsWith(".swf"))) {
				// add it as a node and do nothing else
				node.setAllowsChildren(false);
				parent.add(node);
			}
		}
	}*/

/*	public void print(CustomNode tn) {
		if (!tn.isLeaf()) {
			System.out.println();
			System.out.println(tn + ":");
			Iterator<CustomNode> it = tn.children().iterator();
			while (it.hasNext()) {
				CustomNode node = it.next();
				print(node);
			}
		} else
			System.out.println(tn.getUserObject().getAbsolutePath());
	}*/
	
	public void createMission(int id) throws IOException{
		String stringPath = BASE_PATH + String.valueOf(id);
		// MÉTODO 1 - Path y Files
		try {
			File dir = new File(stringPath);
			// Path path = Paths.get(stringPath);
			dir.mkdirs();
			// Files.createDirectory(path);
		} catch (UnsupportedOperationException e){
			e.printStackTrace();			
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		
		
		// MÉTODO 2 - String y File
/*		File directorio = new File(stringPath);
		if (directorio.mkdir())
			System.out.println("Se ha creado directorio");
		else
			System.out.println("No se ha podido crear el directorio");*/
	}
	
	public void deleteMision(int id) throws IOException{
		String stringPath = BASE_PATH + String.valueOf(id);
		try{
			File directorio = new File(stringPath);
			FileUtils.cleanDirectory(directorio);
			FileUtils.deleteDirectory(directorio);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void changeFileDirectory(com.esferalia.es3.demo.client.dto.File file) throws IOException {
		// Extensión archivo original
		int punto = file.getName().lastIndexOf(".");
		String extension = file.getName().substring(punto);
		
		// Archivo a mover
		java.io.File archivo = new java.io.File(TEMP_DIR + file.getName());
		
		// Directorio destino
		java.io.File dir;
		// Path dirPath = Paths.get(BASE_PATH + file.getMission() + "\\" + file.getFileType().toString() + "\\");
		if (localhost)
			dir = new java.io.File(BASE_PATH + file.getMission() + "\\" + file.getFileType().toString() + "\\");
		else
			dir = new java.io.File(BASE_PATH + file.getMission() + "/" + file.getFileType().toString() + "/");
		if (!dir.exists())
			dir.mkdirs();
		
		// Mover el archivo a otro directorio
		File newFile = new java.io.File(dir, Integer.toString(file.getId()) + extension);
		copy(archivo, newFile);
		
	}
	
	public void deleteFile(com.esferalia.es3.demo.client.dto.File file) throws IOException{
		int punto = file.getName().lastIndexOf(".");
		String extension = file.getName().substring(punto);
		
		String filePath;
		if (localhost)
			filePath = BASE_PATH + file.getMission() + "\\" + file.getFileType().toString() + "\\" + file.getId() + extension;
		else
			filePath = BASE_PATH + file.getMission() + "/" + file.getFileType().toString() + "/" + file.getId() + extension;
		
		boolean success = (new java.io.File(filePath)).delete();
		if (!success) throw new IOException("No se ha podido borrar el archivo");
		else {
			java.io.File dir;
			if (localhost)
				dir = new java.io.File(BASE_PATH + file.getMission() + "\\" + file.getFileType().toString() + "\\");
			else
				dir = new java.io.File(BASE_PATH + file.getMission() + "/" + file.getFileType().toString() + "/");
			String[] files = dir.list();
			if (files.length < 1) {
				FileUtils.deleteDirectory(dir);
			}
		}
	}
	
	private static void copy(File src, File target) throws IOException{
		InputStream input = new FileInputStream(src);
		OutputStream out = new FileOutputStream(target);
		byte buffer [] = new byte [1024];
		int read = -1;
		while ( ( read = input.read(buffer) ) != -1 ) 
			out.write(buffer,0, read);
		input.close();
		out.close();
	}
	
}

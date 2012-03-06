package com.esferalia.es3.demo.server.directory;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

public class DirectoryManager {
	
	// FIXME Determinar un path para las pruebas
	private final String BASE_PATH = "C:\\workspace\\ES3\\src\\com\\esferalia\\es3\\demo\\public\\mission\\";
			// "C:\\workspace\\ES3\\src\\com\\esferalia\\es3\\demo\\public\\mission\\";
			// "/srv/www/lighttpd/es3/mission/";
	private final String TEMP_DIR = "C:\\temp\\";
			// "C:\\temp\\";
			// "/srv/www/lighttpd/es3/temp/";
	private final String LOG_PATH = "C:\\workspace\\ES3\\src\\com\\esferalia\\es3\\demo\\public\\Logging.txt";
			// "C:\\workspace\\ES3\\src\\com\\esferalia\\es3\\demo\\public\\Logging.txt";
			// "/srv/www/lighttpd/es3/Logging.txt";
	
	// Logging vars
	static private FileHandler fileTxt;
	private final static Logger LOGGER = Logger.getLogger(DirectoryManager.class.getName());

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
		
			Logger logger = Logger.getLogger("");
			logger.setLevel(Level.ALL);
			try {
				File logFile = new File(LOG_PATH);
				logFile.createNewFile();
				fileTxt = new FileHandler(LOG_PATH);
				logger.addHandler(fileTxt);
			} catch (IOException e) {
				LOGGER.severe("DIRECTORY MANAGER ERROR: " + e.getMessage());
				e.printStackTrace();
			}
			
			String param = "PARAMETER ID VALUE: " + id;
			LOGGER.severe(param);
			
		// MÉTODO 1 - Path y Files
		try {
			LOGGER.severe("JUST BEFORE CALLING Paths.get(stringPath)");
			Path path = Paths.get(stringPath);
			LOGGER.severe("JUST BEFORE CALLING createDirectory");
			Files.createDirectory(path);
		} catch (IOException e) {
			LOGGER.severe("IO_EXCEPTION: " + e.getMessage());
			e.printStackTrace();
		} catch (UnsupportedOperationException e){
			LOGGER.severe("UNSUPPORTED_OPERATION_EXCEPTION: " + e.getMessage());
			e.printStackTrace();			
		} catch (SecurityException e) {
			LOGGER.severe("SECURITY_EXCEPTION: " + e.getMessage());
			e.printStackTrace();
		} catch (InvalidPathException e) {
			LOGGER.severe("SECURITY_EXCEPTION: " + e.getMessage());
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
		File archivo = new File(TEMP_DIR + file.getName());
		// Directorio destino
		Path dirPath = Paths.get(BASE_PATH + file.getMission() + "\\" + file.getFileType().toString() + "\\");
		if (!new File(BASE_PATH + file.getMission() + "\\" + file.getFileType().toString() + "\\").exists())
			Files.createDirectory(dirPath);
		File dir = new File(BASE_PATH + file.getMission() + "\\" + file.getFileType().toString() + "\\");
		// Mover el archivo a otro directorio
		boolean moved = archivo.renameTo(new File(dir, Integer.toString(file.getId()) + extension));
		if (!moved)
			throw new IOException("No se ha podido mover el archivo");
	}
	
	public void deleteFile(com.esferalia.es3.demo.client.dto.File file) throws IOException{
		int punto = file.getName().lastIndexOf(".");
		String extension = file.getName().substring(punto);
		String filePath = BASE_PATH + file.getMission() + "\\" + file.getFileType().toString() + "\\" + file.getId() + extension;
		boolean success = (new File(filePath)).delete();
		if (!success) throw new IOException("No se ha podido borrar el archivo");
		else {
			File dir = new File(BASE_PATH + file.getMission() + "\\" + file.getFileType().toString() + "\\");
			String[] files = dir.list();
			if (files.length < 1) {
				FileUtils.deleteDirectory(dir);
			}
		}
	}
	
}

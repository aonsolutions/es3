package com.esferalia.es3.demo.server.directory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import com.esferalia.es3.demo.client.dto.CustomNode;
import com.esferalia.es3.demo.client.dto.FolderOrFile;

public class DirectoryManager {
	
	// FIXME Determinar un path para las pruebas
	private final String BASE_PATH = "C:\\workspace\\ES3\\src\\com\\esferalia\\es3\\demo\\public\\mission\\";

	public DirectoryManager() {
	}

	public void listAllFiles(String directory, CustomNode parent) {
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
	}

	public void print(CustomNode tn) {
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
	}
	
	public void createMission(int id){
		String stringPath = BASE_PATH + String.valueOf(id);
		
		// MÉTODO 1 - Path y Files
		Path path = Paths.get(stringPath);
		try {
			Files.createDirectory(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// MÉTODO 2 - String y File
/*		File directorio = new File(stringPath);
		if (directorio.mkdir())
			System.out.println("Se ha creado directorio");
		else
			System.out.println("No se ha podido crear el directorio");*/
	}
	
}

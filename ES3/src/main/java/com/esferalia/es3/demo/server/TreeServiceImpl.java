package com.esferalia.es3.demo.server;

import java.io.File;
import java.util.Iterator;
//import java.util.Iterator;

import com.esferalia.es3.demo.client.dto.FileType;
import com.esferalia.es3.demo.client.dto.FolderOrFile;
import com.esferalia.es3.demo.client.service.TreeService;
import com.esferalia.es3.demo.client.tree.CustomNode;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TreeServiceImpl extends RemoteServiceServlet implements
		TreeService {

	private CustomNode parent;
	
/*	private final String TEMP_DIR = "C:\\temp\\";
	private final String DEST_PATH = "C:\\workspace\\ES3\\src\\com\\esferalia\\es3\\demo\\public\\mission\\";*/
	
	public CustomNode getTree(String path) throws IllegalArgumentException {
		parent = new CustomNode();
		listAllFiles(path, parent);
		return parent;
	}


	public void listAllFiles(String directory, CustomNode parent) {
		// list all the files in the directory
		File[] children = new File(directory).listFiles();
		// loop through each
		for (int i = 0; i < children.length; i++) {
			FolderOrFile fof = new FolderOrFile(children[i].getName(), children[i].getPath());
			CustomNode node = new CustomNode(fof);
			if (children[i].isDirectory()) {
				if (children[i].getName().endsWith("video")){
					node.setFiletype(FileType.video);
					node.setLockedFolder(true);
				} else if (children[i].getName().endsWith("audio")){
					node.setFiletype(FileType.audio);
					node.setLockedFolder(true);
				} else if (children[i].getName().endsWith("imagen")){
					node.setFiletype(FileType.imagen);
					node.setLockedFolder(true);
				} else if (children[i].getName().endsWith("documento")){
					node.setFiletype(FileType.documento);
					node.setLockedFolder(true);
				} else {
					node.setFiletype(FileType.cartografia);
					node.setLockedFolder(true);
				}
				// add as a child node
				parent.add(node);
				// call again for the subdirectory
				listAllFiles(children[i].getPath(), node);

			} else {
				// add it as a node and do nothing else
				node.setAllowsChildren(false);
				parent.add(node);
			}
		}
	}	
	
/*	public void changeFileDirectory(com.esferalia.es3.demo.client.dto.File file, int id){
		// Archivo a mover 
		 File archivo= new File(TEMP_DIR + file.getName()); 
		// Directorio destino
		 File dir = new File(DEST_PATH + file.getMission() + "\\"); 
		// Mover el archivo a otro directorio
		// boolean moved = archivo.renameTo(new File(dir, file.getId()));
		boolean moved = archivo.renameTo(new File(dir, Integer.toString(id)));
		if (!moved) System.out.println("No se ha podido mover el archivo");
	}*/

/*	private void print(CustomNode tn) {
		if (!tn.isLeaf()) {
			System.out.println();
			System.out.println(tn + ":");
			Iterator<CustomNode> it = tn.children().iterator();
			while(it.hasNext()){
				CustomNode node = it.next();
				print(node);
			}
		} else
			System.out.println(tn.getUserObject().getAbsolutePath());
	}*/
}

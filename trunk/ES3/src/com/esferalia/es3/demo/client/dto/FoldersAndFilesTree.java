package com.esferalia.es3.demo.client.dto;

import com.esferalia.es3.demo.client.event.PlaySelectedEvent;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.Composite;
//import com.google.gwt.user.client.ui.ScrollPanel;
//import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;

import java.util.ArrayList;
import java.util.List;


public class FoldersAndFilesTree extends Composite{

//	private VerticalPanel mainPanel;
	
	/**
	 * The model that defines the nodes in the tree.
	 */
	private class CustomTreeModel implements TreeViewModel {

		// Lista con �nico nodo, que ser� la ra�z del �rbol que guardar� a los hijos
		private List<CustomNode> rangos;
		// Manager de eventos, enviar� el path del archivo seleccionado a la interfaz para que lo reproduzca
		private final HandlerManager eventBus;
		// Permite determinar c�mo se seleccionan los nodos. En este caso, de uno en uno.
		private SingleSelectionModel<CustomNode> selectionModel;
		// Proporciona una clave para los elementos de una lista
		private ProvidesKey<CustomNode> keyProvider;

		
		public CustomTreeModel(final HandlerManager Bus, CustomNode root) {
			
			this.eventBus = Bus;
			rangos = new ArrayList<CustomNode>();

			keyProvider = new ProvidesKey<CustomNode>() {
				public Object getKey(CustomNode item) {
					return (item == null) ? null : item.getUserObject().getAbsolutePath();
				}
		    };
			selectionModel = new SingleSelectionModel<CustomNode>(keyProvider);
			
			// Definir la acci�n que se ha de realizar cuando se seleccione un nodo del �rbol
			selectionModel.addSelectionChangeHandler(new Handler() {
				@Override
				public void onSelectionChange(SelectionChangeEvent event) {
					if (selectionModel.getSelectedObject().isLeaf()){
						PlaySelectedEvent selectedFile = new PlaySelectedEvent();
						String relativePath;
						String absolutePath;
						absolutePath = selectionModel.getSelectedObject().getUserObject().getAbsolutePath();
						relativePath = getRelativePath(absolutePath);
						selectedFile.setPath(relativePath);
						eventBus.fireEvent(selectedFile);
					}
				}

				// FIXME absolutePath and relativePath
				private String getRelativePath(String absolutePath) {
					int beginIndex;
					beginIndex = absolutePath.lastIndexOf("public\\");
					return "es3/" + absolutePath.substring(beginIndex+7);
//					beginIndex = absolutePath.lastIndexOf("public/");
//					return "http://lighttpd.esferalia.net/" + absolutePath.substring(beginIndex);
				}
			});
		}

		
		// Obtener el NodeInfo<?> que proporciona a los hijos el valor especificado.
		public <T> NodeInfo<?> getNodeInfo(T value) {
			// �En el caso en el que no se seleccione nada, mostrar el root vac�o?
			// Devuelve el nodo ra�z
			if (value == null) {
				// El proveedor que contiene la lista de condiciones.
				ListDataProvider<CustomNode> dataProvider = new ListDataProvider<CustomNode>(rangos);
				// Crear una celda para mostrar el padre
				Cell<CustomNode> cell = new AbstractCell<CustomNode>() {
					@Override
					public void render(
							com.google.gwt.cell.client.Cell.Context context,
							CustomNode value, SafeHtmlBuilder sb) {
						if (value != null) {
							sb.appendEscaped(value.getUserObject().getName());
						}
					}
				};
				return new DefaultNodeInfo<CustomNode>(dataProvider, cell, selectionModel, null);

			} else if (((CustomNode) value).getAllowsChildren()) {
				// Nodo Directorio
				List<CustomNode> hijos =((CustomNode) value).children();
				ListDataProvider<CustomNode> dataProvider2 = new ListDataProvider<CustomNode>(hijos);

				// Crear una celda para mostrar al hijo
				Cell<CustomNode> cell = new AbstractCell<CustomNode>() {
					public void render(com.google.gwt.cell.client.Cell.Context context,
							CustomNode value, SafeHtmlBuilder sb) {
						if (value != null)
							sb.appendEscaped(value.getUserObject().getName());
					}
				};
				return new DefaultNodeInfo<CustomNode>(dataProvider2, cell, selectionModel, null);
			}
			return null;
		}

		/**
		 * Check if the specified value represents a leaf node. Leaf nodes cannot be
		 * opened.
		 */
		public boolean isLeaf(Object value) {
			return ((CustomNode) value).isLeaf();
		}
	}

	public FoldersAndFilesTree(final HandlerManager Bus, CustomNode root) {
		
//		mainPanel = new VerticalPanel();
//		initWidget(mainPanel);

		TreeViewModel model = new CustomTreeModel(Bus, root);
		CellTree tree = new CellTree(model, root.getRoot());
		tree.ensureDebugId("FoldersAndFilesTree");
		initWidget(tree);
		
//		ScrollPanel conditionTreeWrapper = new ScrollPanel(tree);
//		conditionTreeWrapper.ensureDebugId("FoldersAndFilesTree-Wrapper");
//		conditionTreeWrapper.setSize("300px", "600px");
//
//		mainPanel.add(conditionTreeWrapper);
	}
}

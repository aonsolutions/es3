package com.esferalia.es3.demo.client.dto;

import java.io.Serializable;
import java.util.Vector;

public class CustomNode implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/** this node's parent, or null if this node has no parent */
    protected CustomNode parent;

    /** array of children, may be null if this node has no children */
    protected Vector<CustomNode> children;

    /** optional user object */
    protected FolderOrFile userObject;

    /** true if the node is able to have children */
    protected boolean allowsChildren;
    
    /**
     * Creates a tree node that has no parent and no children, but which
     * allows children.
     */
    public CustomNode() {
        this(null);
    }
    
    /**
     * Creates a tree node with no parent, no children, but which allows
     * children, and initializes it with the specified user object.
     *
     * @param userObject an Object provided by the user that constitutes
     *                   the node's data
     */
    public CustomNode(FolderOrFile userObject) {
        this(userObject, true);
    }

    /**
     * Creates a tree node with no parent, no children, initialized with
     * the specified user object, and that allows children only if
     * specified.
     *
     * @param userObject an Object provided by the user that constitutes
     *        the node's data
     * @param allowsChildren if true, the node is allowed to have child
     *        nodes -- otherwise, it is always a leaf node
     */
    public CustomNode(FolderOrFile userObject, boolean allowsChildren) {
        super();
        parent = null;
        this.allowsChildren = allowsChildren;
        this.setUserObject(userObject);
    }
    
    /**
     * Removes <code>newChild</code> from its present parent (if it has a
     * parent), sets the child's parent to this node, and then adds the child
     * to this node's child array at index <code>childIndex</code>.
     * <code>newChild</code> must not be null and must not be an ancestor of
     * this node.
     *
     * @param   newChild        the MutableTreeNode to insert under this node
     * @param   childIndex      the index in this node's child array
     *                          where this node is to be inserted
     * @exception       ArrayIndexOutOfBoundsException  if
     *                          <code>childIndex</code> is out of bounds
     * @exception       IllegalArgumentException        if
     *                          <code>newChild</code> is null or is an
     *                          ancestor of this node
     * @exception       IllegalStateException   if this node does not allow
     *                                          children
     * @see     #isNodeDescendant
     */
    public void insert(CustomNode newChild, int childIndex) {
        if (!allowsChildren) {
            throw new IllegalStateException("node does not allow children");
        } else if (newChild == null) {
            throw new IllegalArgumentException("new child is null");
        } else if (isNodeAncestor(newChild)) {
            throw new IllegalArgumentException("new child is an ancestor");
        }

        CustomNode oldParent = (CustomNode)newChild.getParent();

            if (oldParent != null) {
                oldParent.remove(newChild);
            }
            newChild.setParent(this);
            if (children == null) {
                children = new Vector<CustomNode>();
            }
            children.insertElementAt(newChild, childIndex);
    }
    
    /**
     * Removes <code>newChild</code> from its parent and makes it a child of
     * this node by adding it to the end of this node's child array.
     *
     * @see             #insert
     * @param   newChild        node to add as a child of this node
     * @exception       IllegalArgumentException    if <code>newChild</code>
     *                                          is null
     * @exception       IllegalStateException   if this node does not allow
     *                                          children
     */
    public void add(CustomNode newChild) {
        if(newChild != null && newChild.getParent() == this)
            insert(newChild, getChildCount() - 1);
        else
            insert(newChild, getChildCount());
    }
    
    /**
     * Sets this node's parent to <code>newParent</code> but does not
     * change the parent's child array.  This method is called from
     * <code>insert()</code> and <code>remove()</code> to
     * reassign a child's parent, it should not be messaged from anywhere
     * else.
     *
     * @param   newParent       this node's new parent
     */
    public void setParent(CustomNode newParent) {
        parent = newParent;
    }
    
    /**
     * Returns this node's parent or null if this node has no parent.
     *
     * @return  this node's parent TreeNode, or null if this node has no parent
     */
    public CustomNode getParent() {
        return parent;
    }
    
    /**
     * Creates and returns a forward-order enumeration of this node's
     * children.  Modifying this node's child array invalidates any child
     * enumerations created before the modification.
     *
     * @return  an Enumeration of this node's children
     */
    public Vector<CustomNode> children() {
    	return children;
    }
    
    /**
     * Determines whether or not this node is allowed to have children.
     * If <code>allows</code> is false, all of this node's children are
     * removed.
     * <p>
     * Note: By default, a node allows children.
     *
     * @param   allows  true if this node is allowed to have children
     */
    public void setAllowsChildren(boolean allows) {
        this.allowsChildren = allows;
    }

    /**
     * Returns true if this node is allowed to have children.
     *
     * @return  true if this node allows children, else false
     */
    public boolean getAllowsChildren() {
        return allowsChildren;
    }
    
    /**
     * Removes <code>aChild</code> from this node's child array, giving it a
     * null parent.
     *
     * @param   aChild  a child of this node to remove
     * @exception       IllegalArgumentException        if <code>aChild</code>
     *                                  is null or is not a child of this node
     */
    public void remove(CustomNode aChild) {
        if (aChild == null) {
            throw new IllegalArgumentException("argument is null");
        }

        if (!isNodeChild(aChild)) {
            throw new IllegalArgumentException("argument is not a child");
        }
        remove(getIndex(aChild));       // linear search
    }
    
    /**
     * Removes the child at the specified index from this node's children
     * and sets that node's parent to null. The child node to remove
     * must be a <code>MutableTreeNode</code>.
     *
     * @param   childIndex      the index in this node's child array
     *                          of the child to remove
     * @exception       ArrayIndexOutOfBoundsException  if
     *                          <code>childIndex</code> is out of bounds
     */
    public void remove(int childIndex) {
    	CustomNode child = (CustomNode)getChildAt(childIndex);
        children.removeElementAt(childIndex);
        child.setParent(null);
    }
    
    /**
     * Returns the child at the specified index in this node's child array.
     *
     * @param   index   an index into this node's child array
     * @exception       ArrayIndexOutOfBoundsException  if <code>index</code>
     *                                          is out of bounds
     * @return  the TreeNode in this node's child array at  the specified index
     */
    public CustomNode getChildAt(int index) {
        if (children == null) {
            throw new ArrayIndexOutOfBoundsException("node has no children");
        }
        return (CustomNode)children.elementAt(index);
    }
    
    /**
     * Returns the index of the specified child in this node's child array.
     * If the specified node is not a child of this node, returns
     * <code>-1</code>.  This method performs a linear search and is O(n)
     * where n is the number of children.
     *
     * @param   aChild  the TreeNode to search for among this node's children
     * @exception       IllegalArgumentException        if <code>aChild</code>
     *                                                  is null
     * @return  an int giving the index of the node in this node's child
     *          array, or <code>-1</code> if the specified node is a not
     *          a child of this node
     */
    public int getIndex(CustomNode aChild) {
        if (aChild == null) {
            throw new IllegalArgumentException("argument is null");
        }

        if (!isNodeChild(aChild)) {
            return -1;
        }
        return children.indexOf(aChild);        // linear search
    }
    
    /**
     * Returns the number of children of this node.
     *
     * @return  an int giving the number of children of this node
     */
    public int getChildCount() {
        if (children == null) {
            return 0;
        } else {
            return children.size();
        }
    }
    
    /**
     * Returns the root of the tree that contains this node.  The root is
     * the ancestor with a null parent.
     *
     * @see     #isNodeAncestor
     * @return  the root of the tree that contains this node
     */
    public CustomNode getRoot() {
    	CustomNode ancestor = this;
    	CustomNode previous;

        do {
            previous = ancestor;
            ancestor = ancestor.getParent();
        } while (ancestor != null);

        return previous;
    }
    
    /**
     * Returns true if <code>anotherNode</code> is an ancestor of this node
     * -- if it is this node, this node's parent, or an ancestor of this
     * node's parent.  (Note that a node is considered an ancestor of itself.)
     * If <code>anotherNode</code> is null, this method returns false.  This
     * operation is at worst O(h) where h is the distance from the root to
     * this node.
     *
     * @see             #isNodeDescendant
     * @see             #getSharedAncestor
     * @param   anotherNode     node to test as an ancestor of this node
     * @return  true if this node is a descendant of <code>anotherNode</code>
     */
    public boolean isNodeAncestor(CustomNode anotherNode) {
        if (anotherNode == null) {
            return false;
        }

        CustomNode ancestor = this;

        do {
            if (ancestor == anotherNode) {
                return true;
            }
        } while((ancestor = ancestor.getParent()) != null);

        return false;
    }
    
    /**
     * Returns true if <code>aNode</code> is a child of this node.  If
     * <code>aNode</code> is null, this method returns false.
     *
     * @return  true if <code>aNode</code> is a child of this node; false if
     *                  <code>aNode</code> is null
     */
    public boolean isNodeChild(CustomNode aNode) {
        boolean retval;

        if (aNode == null) {
            retval = false;
        } else {
            if (getChildCount() == 0) {
                retval = false;
            } else {
                retval = (aNode.getParent() == this);
            }
        }

        return retval;
    }
    
    /**
     * Returns true if this node has no children.  To distinguish between
     * nodes that have no children and nodes that <i>cannot</i> have
     * children (e.g. to distinguish files from empty directories), use this
     * method in conjunction with <code>getAllowsChildren</code>
     *
     * @see     #getAllowsChildren
     * @return  true if this node has no children
     */
    public boolean isLeaf() {
        return (getChildCount() == 0);
    }

	public FolderOrFile getUserObject() {
		return userObject;
	}

	public void setUserObject(FolderOrFile userObject) {
		this.userObject = userObject;
	}
}

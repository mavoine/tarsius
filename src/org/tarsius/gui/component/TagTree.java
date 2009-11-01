package org.tarsius.gui.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.bean.Tag;
import org.tarsius.gui.action.TagSelectionProvider;
import org.tarsius.i18n.I18n;
import org.tarsius.util.TagNameComparator;

public class TagTree extends JTree implements TagSelectionProvider {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(TagTree.class);
	private DefaultMutableTreeNode rootNode = null;
	
	public TagTree() {
		this.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);

		rootNode = new DefaultMutableTreeNode(I18n.translate("AllTags"));
		TreeModel tm = new DefaultTreeModel(this.rootNode);
		this.setToggleClickCount(3);
		this.setModel(tm);
	}
	
	public void setData(List<Tag> tags) {

		this.rootNode = new DefaultMutableTreeNode(I18n.translate("AllTags"));
		
		List<Tag> tagsTmp = new ArrayList<Tag>();
		if(tags != null){
			tagsTmp.addAll(tags);
		}
		
		Map<Integer,DefaultMutableTreeNode> alreadyPresent = 
			new HashMap<Integer,DefaultMutableTreeNode>();
		
		List<Tag> tagSet = new ArrayList<Tag>();
		tagSet.addAll(tagsTmp);
		Collections.sort(tagSet, new TagNameComparator());
		while(!tagsTmp.isEmpty()){
			Iterator<Tag> it = tagSet.iterator();
			while(it.hasNext()){
				Tag tag = (Tag)it.next();
				if(tag.getIdParent() == null){
					DefaultMutableTreeNode node = new DefaultMutableTreeNode(tag);
					this.rootNode.add(node);
					alreadyPresent.put(tag.getId(), node);
					tagsTmp.remove(tag);
				} else {
					DefaultMutableTreeNode node = new DefaultMutableTreeNode(tag);
					DefaultMutableTreeNode parent = alreadyPresent.get(tag.getIdParent());
					if(parent != null){
						parent.add(node);
						alreadyPresent.put(tag.getId(), node);
						tagsTmp.remove(tag);
					}
				}
			}
			tagSet.clear();
			Collections.sort(tagsTmp, new TagNameComparator());
			tagSet.addAll(tagsTmp);
		}
		TreeModel tm = new DefaultTreeModel(this.rootNode);
		this.setModel(tm);
	}

	/**
	 * Returns the tag selected in the tree, or null if no tag or the root 
	 * element of the tree is selected.
	 * @return Selected tag or null
	 */
	public Tag getSelectedTag() {
		Tag tag = null;
		TreePath path = this.getSelectionPath();
		if (path != null) {
			if (path.getLastPathComponent() != null) {
				DefaultMutableTreeNode selectedNode = 
					(DefaultMutableTreeNode)path.getLastPathComponent();
				Object obj = selectedNode.getUserObject();
				if(obj instanceof Tag){
					tag = (Tag)obj;
				}
			}
		}
		return tag;
	}
	
	/**
	 * Returns the tag located at coordinates x and y in the tree, or null if
	 * the coordinates are out of the tree drawing area.
	 * @param x
	 * @param y
	 * @return tag
	 */
	public Tag getTagAtLocation(int x, int y){
		Tag tag = null;
		TreePath path = this.getPathForLocation(x, y);
		if (path != null) {
			if (path.getLastPathComponent() != null) {
				DefaultMutableTreeNode selectedNode = 
					(DefaultMutableTreeNode)path.getLastPathComponent();
				Object obj = selectedNode.getUserObject();
				if(obj instanceof Tag){
					tag = (Tag)obj;
				}
			}
		}
		return tag;
	}
	
	/**
	 * Returns true if the root node of the Tag tree is selected, false otherwise.
	 * Returns false also if no element is selected in the tree.
	 * @return true if the root node is selected
	 */
	public boolean isRootNodeSelected(){
		boolean selected = false;
		TreePath path = this.getSelectionPath();
		if(path != null){
			Object obj = path.getLastPathComponent();
			if(obj instanceof DefaultMutableTreeNode){
				selected = (DefaultMutableTreeNode)obj == this.rootNode;
			}
		}
		return selected;
	}
	
//	public void selectTagAtLocation(int x, int y){
//		TreePath path = this.getPathForLocation(x, y);
//		if(path != null){
//			Object obj = path.getLastPathComponent();
//			if(obj instanceof DefaultMutableTreeNode){
//				DefaultMutableTreeNode node = (DefaultMutableTreeNode)obj;
////				if(node.getUserObject() instanceof Tag){
//					this.addSelectionPath(path);
////				} else {
////					// select the root node
////					this.rootNode.get
////				}
//			}
//		} else {
//			// select the root node
//			this.addSelectionRow(0);
//		}
//	}

}

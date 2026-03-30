package dsa.impl;

import dsa.iface.IPosition;
import dsa.util.TreePrinter;

public class AVLTree<T extends Comparable<T>> extends BinarySearchTree<T> {

   private void LeftRotate(BTPosition node){
      assert(node.right != null);
      var rSubtree = node.right;
      var rSubtreeL = node.right.left;
      var parent = node.parent;
      if(parent != null){
         if(parent.left == node){
            parent.left = rSubtree;
         }
         else if(parent.right == node){
            parent.right = rSubtree;
         }
      }
      else root = rSubtree;
      rSubtree.parent = parent;
      node.parent = rSubtree;
      rSubtree.left = node;
      node.right = rSubtreeL;
      if(rSubtreeL != null)rSubtreeL.parent = node;
   }

   private void RightRotate(BTPosition node){
      assert(node.left != null);
      var lSubtree = node.left;
      var lSubtreeR = node.left.right;
      var parent = node.parent;
      if(parent != null){
         if(parent.left == node){
            parent.left = lSubtree;
         }
         else {
            parent.right = lSubtree;
         }
      }
      else root = lSubtree;
       lSubtree.parent = parent;
       node.parent = lSubtree;
      lSubtree.right = node;
      node.left = lSubtreeR;
      if(lSubtreeR != null)lSubtreeR.parent = node;
   }

   @Override
   public void insert( T element ) {
      // TODO: Implement the insert(...) method.
      if(root.element == null) {expandExternal(root, element); return;}
      BTPosition node = (BTPosition) find(root, element);
      expandExternal(node, element);
      restructure(node);
   }

   private int getHeight( BTPosition node)
   {
      if(node == null)return 0;
      if(node.left == null && node.right == null) {return 1;}
      if(node.left != null && node.right == null) {return 1 + getHeight(node.left);}
      if(node.left == null) {return 1 + getHeight(node.right);}
      return 1 + Math.max(getHeight(node.left), getHeight(node.right));
   }

   @Override
   public boolean contains( T element ) {
      // TODO: Implement the contains(...) method.
      return find(root, element).element() == element;
   }

   @Override
   public void remove( T element ) {
      // TODO: Implement the remove(...) method.
      BTPosition node = (BTPosition) find(root, element);
      BTPosition p = (BTPosition) node.parent;
      if(p == null){
         p = root.right;
         if(p == null){
            root = root.left;
            return;
         }
         while(p.left != null || p.right != null){
            if(p.left != null)p = p.left;
            else p = p.right;
         }
         super.remove(element);
         restructure(p);
      }
      else{
         super.remove(element);
         restructure(p);

      }

   }

   private void restructure( BTPosition n ) {
      BTPosition node = n;
      while(true) {
         if(getHeight(node.left) - getHeight(node.right) >= 2) {
            //System.out.print("L");
            if(getHeight(node.left.left) < getHeight(node.left.right)) {
               //System.out.print("R");
               LeftRotate(node.left);
               //TreePrinter.printTree(this);
            }
            //else System.out.print("L");
            RightRotate(node);
            //TreePrinter.printTree(this);
         }
         else if(getHeight(node.right) - getHeight(node.left) >= 2) {
            //System.out.print("R");
            if(getHeight(node.right.left) > getHeight(node.right.right)) {
               RightRotate(node.right);
               //System.out.print("L");
               //TreePrinter.printTree(this);
            }
            //else System.out.print("R");
            LeftRotate(node);
            //TreePrinter.printTree(this);
         }
         //System.out.println(":" + node.element);
         if(node.parent == null)break;
         node = node.parent;
      }
   }

   @Override
   protected BTPosition newPosition( T element, BTPosition parent ) {
      return new AVLPosition( element, parent );
   }

   public class AVLPosition extends BTPosition {
      int height = 0;

      public AVLPosition( T element, BTPosition parent ) {
         super( element, parent );
      }
   }
}

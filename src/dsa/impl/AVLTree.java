package dsa.impl;

import dsa.iface.IPosition;
import dsa.util.TreePrinter;

public class AVLTree<T extends Comparable<T>> extends BinarySearchTree<T> {

   private int refreshDegree(AVLPosition node) {
      if(node == null) return 0;
      int leftHeight = refreshDegree((AVLPosition) node.left);
      int rightHeight = refreshDegree((AVLPosition) node.right);
      node.degree = leftHeight - rightHeight;
      return Math.max(leftHeight, rightHeight) + 1;
   }

   private void refreshAllDegrees() {
      if(root == null) return;
      refreshDegree((AVLPosition) root);
   }

   private void LeftRotate(AVLPosition node){
      assert(node.right != null);
      AVLPosition rSubtree = (AVLPosition)node.right;
      AVLPosition rSubtreeL = (AVLPosition)node.right.left;
      AVLPosition parent = (AVLPosition)node.parent;
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

   private void RightRotate(AVLPosition node){
      assert(node.left != null);
      AVLPosition lSubtree = (AVLPosition)node.left;
      AVLPosition lSubtreeR = (AVLPosition)node.left.right;
      AVLPosition parent = (AVLPosition)node.parent;
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

   private void LRRotate(AVLPosition node){
      AVLPosition lSubtree = (AVLPosition)node.left;
      AVLPosition lSubtreeR = (AVLPosition)node.left.right;
      int lSubtreeRDegree = lSubtreeR.degree;

      LeftRotate(lSubtree);
      RightRotate(node);
      if(lSubtreeRDegree == 0){
         node.degree = lSubtree.degree = lSubtreeR.degree = 0;
      }
      else if(lSubtreeRDegree == 1){
         lSubtree.degree = lSubtreeR.degree = 0;
         node.degree = -1;
      }
      else if(lSubtreeRDegree == -1){
         node.degree = lSubtreeR.degree = 0;
         lSubtree.degree = 1;
      }
      else throw new RuntimeException();
   }

   private void RLRRotate(AVLPosition node){
      AVLPosition rSubtree = (AVLPosition)node.right;
      AVLPosition rSubtreeL = (AVLPosition)node.right.left;
      int rSubtreeLDegree = rSubtreeL.degree;

      RightRotate(rSubtree);
      LeftRotate(node);
      if(rSubtreeLDegree == 0){
         node.degree = rSubtree.degree = rSubtreeL.degree = 0;
      }
      else if(rSubtreeLDegree == 1){
         node.degree = rSubtreeL.degree = 0;
         rSubtree.degree = -1;
      }
      else if(rSubtreeLDegree == -1){
         rSubtree.degree = rSubtreeL.degree = 0;
         node.degree = 1;
      }
      else throw new RuntimeException();

   }

   @Override
   public void insert( T element ) {
      // TODO: Implement the insert(...) method.
      if(root.element == null) {expandExternal(root, element); return;}
      AVLPosition node = (AVLPosition) find(root, element);
      expandExternal(node, element);

      refreshAllDegrees();
      restructure(node);
      refreshAllDegrees();
   }

   @Override
   public boolean contains( T element ) {
      // TODO: Implement the contains(...) method.
      return find(root, element).element() == element;
   }

   @Override
   public void remove( T element ) {
      // TODO: Implement the remove(...) method.
      AVLPosition node = (AVLPosition) find(root, element);
      AVLPosition p = (AVLPosition) node.parent;
      if(p == null){
         p = (AVLPosition)root.right;
         if(p == null){
            root = root.left;
            return;
         }
         while(p.left != null || p.right != null){
            if(p.left != null)p = (AVLPosition)p.left;
            else p = (AVLPosition)p.right;
         }
         super.remove(element);
         refreshAllDegrees();
         restructure(p);
      }
      else{
         super.remove(element);
         refreshAllDegrees();
         restructure(p);

      }

      refreshAllDegrees();

   }

   private void restructure( AVLPosition n ) {
      AVLPosition node = n;
      while(node != null) {
         if(node.degree >= 2) {
            AVLPosition left = (AVLPosition) node.left;
            if(left != null && left.degree < 0) {
               LeftRotate((AVLPosition)node.left);
               refreshAllDegrees();
            }
            RightRotate(node);
            refreshAllDegrees();
         }
         else if(node.degree <= -2) {
            AVLPosition right = (AVLPosition) node.right;
            if(right != null && right.degree > 0) {
               RightRotate((AVLPosition)node.right);
               refreshAllDegrees();
            }
            LeftRotate(node);
            refreshAllDegrees();
         }

         node = (AVLPosition)node.parent;
      }
   }

   @Override
   protected BTPosition newPosition( T element, BTPosition parent ) {
      return new AVLPosition( element, parent );
   }

   public class AVLPosition extends BTPosition {
      int degree = 0;// height_l - height_r

      public AVLPosition( T element, BTPosition parent ) {
         super( element, parent );
      }
   }
}

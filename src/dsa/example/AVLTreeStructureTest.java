package dsa.example;

import dsa.iface.IBinarySearchTree;
import dsa.iface.IBinaryTree;
import dsa.iface.IPosition;
import dsa.impl.AVLTree;
import dsa.impl.BinarySearchTree;
import dsa.util.TreePrinter;

/**
 * Simple class to compare the structure of an AVL tree with the
 *    expected outcome.
 *    
 * Inserts 6 values into an AVL Tree, which should cause 3 trinode restructurings:
 * - 1 double rotation (right-left) at the root when 54 is inserted.
 * - 1 single rotation (right-right) when 41 is inserted.
 * - 1 single rotation (left-left) at the root when 19 is inserted.
 * 
 * A Binary Search Tree is constructed that has the expected final shape for the AVL tree.
 * 
 * Result: AVL tree is the correct shape.
 * 
 * @author David Lillis
 */
public class AVLTreeStructureTest {

   public static void main( String[] args ) {

      // I want to insert the following values into an AVL tree, in order.
      int[] AVLOrder = new int[] { 30, 77, 54, 32, 41, 19 };
      
      // Inserting them in this order into a Binary Search Tree (BST) should give me
      //   the same result as the final AVL tree (remember a BST does not restructure)
      //   ... so in this case, 32 will always be the root (since I don't remove anything)
      //   , 30 will be its left child, etc...
      int[] BSTOrder  = new int[] { 32, 30, 54, 19, 41, 77 };
      
      // create my two trees
      IBinarySearchTree<Integer> t1 = new AVLTree<>();
      IBinarySearchTree<Integer> t2 = new BinarySearchTree<>();

      // insert the values into the two trees 
      for ( int v : AVLOrder ) {
         t1.insert(v);
         //TreePrinter.printTree(t1);
         //System.out.println("Rua!");
      }

      //t1.remove(32);
      //TreePrinter.printTree(t1);

      for ( int v : BSTOrder )
         t2.insert( v );

      
      System.out.println( "Is the AVL Tree in the expected shape? " + ( areEqual( t1, t1.root(), t2, t2.root() ) ? "YES! :-D" : "No! :-(" ) );
   }

   // check if two subtrees are equal (have the same shape and the same elements).
   // to check a whole tree, pass in the tree roots as the IPosition objects.
   private static <E extends Comparable<E>> boolean areEqual( IBinaryTree<E> t1, IPosition<E> p1, IBinaryTree<E> t2, IPosition<E> p2 ) {
      // they're both external nodes, so they are equal.
      if ( t1.isExternal( p1 ) && t2.isExternal( p2 ) )
         return true;
      // they are both internal, have the same element, and their left and right subtrees are also equal.
      else if ( t1.isInternal( p1 ) && t2.isInternal( p2 ) ) {
         return p1.element().equals( p2.element() ) && areEqual( t1, t1.left( p1 ), t2, t2.left( p2 ) ) && areEqual( t1, t1.right( p1 ), t2, t2.right( p2 ) );
      }
      // one is internal and the other is external: not the same tree.
      else {
         return false;
      }
   }
}

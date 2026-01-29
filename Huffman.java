package aed.huffman;

import es.upm.aedlib.Entry;
import es.upm.aedlib.Position;
import es.upm.aedlib.map.*;
import es.upm.aedlib.tree.*;
import es.upm.aedlib.priorityqueue.*;


public class Huffman {

  public static Map<Character,Integer> frequencies(String texto) {
    Map<Character,Integer> resul = new HashTableMap<>();
    if(texto.isEmpty()) {
    	return resul;
    }
    int i=0;
    while(i<texto.length()) {
    	Character caracter=texto.charAt(i);
    	if (resul.containsKey(caracter)) {
            // Si ya tenemos el elemento en Map aumentamos su valor 
            resul.put(caracter, resul.get(caracter) + 1);
        } else {
            // Si es la primera vez inicializamos su valor a uno
            resul.put(caracter, 1);
        }
    	i++;
    }
	return resul;
    
  }

//seguimos el algoritmo dado
public static BinaryTree<Character> constructHuffmanTree(Map<Character,Integer> charCounts) {
	 PriorityQueue<Integer,BinaryTree<Character>> Q = new SortedListPriorityQueue<>();
	 for  (Entry<Character, Integer> w : charCounts.entries())  {
		 char c= w.getKey();
		 BinaryTree<Character> T = new LinkedBinaryTree<>();
		 T.addRoot(c);
		 Q.enqueue(w.getValue(), T);
        
     }
	 
	 while(Q.size()>1){
		Entry<Integer, BinaryTree<Character>> left = Q.dequeue(); 
		Entry<Integer, BinaryTree<Character>> right = Q.dequeue();
		
		BinaryTree<Character> leftTree = left.getValue();
        BinaryTree<Character> rightTree = right.getValue();
        
		BinaryTree<Character> Tprima = joinTrees(' ',leftTree,rightTree);
		Q.enqueue(left.getKey()+right.getKey(), Tprima);
	 }
	 return Q.dequeue().getValue();
  }








  public static <E> BinaryTree<E> joinTrees(E e,
                                            BinaryTree<E> leftTree,
                                            BinaryTree<E> rightTree) {
	  
	  //creamos y añadimos la raiz al nuevo arbol
	  BinaryTree<E> arbol=new LinkedBinaryTree<>();
	  Position<E> root = arbol.addRoot(e); 
	
	  //si el subarbol izquierdo no esta vacio lo añadimos
	  if (!leftTree.isEmpty()) {
		  Position<E> newLeft = arbol.insertLeft(root, leftTree.root().element());
		  insertarArbolesRec(arbol, leftTree, leftTree.root(),newLeft);
	  }
	  
	  //si el subarbol derecho no esta vacio lo añadimos
	  if (!rightTree.isEmpty()) {
		  Position<E> newRight = arbol.insertRight(root, rightTree.root().element());
		  insertarArbolesRec(arbol, rightTree, rightTree.root(), newRight);
	  }
	  
    return arbol;
  }
  
  
  //arbol: arbol que estamos creando
  //tree: el arbol original, es decir, subarbol izquierdo o derecho
  // elemArbolOriginal: nodo correspondiente al arbol original que contiene el ultimo elemento añadido a arbol 
  //elemArbolNuevo: nodo correspondiente al arbol nuevo que contiene el ultimo elemento añadido a arbol
  private static <E> void insertarArbolesRec(BinaryTree<E> arbol, BinaryTree<E> tree, Position<E> elemArbolOriginal,
		  Position<E> elemArbolNuevo) {

	  //comprobamos si el ultimo nodo añadido al arbol tiene hijo izquierdo en el arbol original
	  if(tree.hasLeft(elemArbolOriginal)) {
		  //left corresponde al hijo izquierdo del arbol original 
		  Position <E> left=tree.left(elemArbolOriginal);
		  //newLeft corresponde al nodo que se crea cuando añadimos como hijo a left en el arbol que estamos creando
		  Position<E> newLeft=arbol.insertLeft(elemArbolNuevo, left.element());
		  insertarArbolesRec(arbol,tree,left, newLeft);
	  }
	//comprobamos si el ultimo nodo añadido al arbol tiene hijo derecho  en el arbol original
	  if(tree.hasRight(elemArbolOriginal)) {
		  //right corresponde al hijo derecho del arbol original 
		  Position <E> right = tree.right(elemArbolOriginal) ;
		  //newRight corresponde al nodo que se crea cuando añadimos como hijo a right en el arbol que estamos creando
		  Position<E> newRight= arbol.insertRight(elemArbolNuevo, right.element());
		  insertarArbolesRec(arbol,tree,right,newRight);
	  }
	  
	 
	 
	
}


public static Map<Character,String> characterCodes(BinaryTree<Character> tree) {
    Map<Character,String> mapa=new HashTableMap<>();
    characterCodesRes(tree, mapa, tree.root(),"");
    return mapa;
  }

  private static void characterCodesRes(BinaryTree<Character> tree, Map<Character, String> mapa,
		Position<Character> pos, String codigo) {
	//Si el elemento es una hoja lo añadimos al mapa, esto nos evita añadir nodos vacios intermedios
	  if (!tree.hasLeft(pos) && !tree.hasRight(pos)) {
	        mapa.put(pos.element(), codigo);
	        return;
	    }
	  //si nos deplazamos a la izquierda añadiremos un cero al código
	   if(tree.hasLeft(pos)) {
		   Position<Character> left=tree.left(pos);
		   characterCodesRes(tree, mapa, left, codigo+"0");
	   }
	  
	   //si nos deplazamos a la derecha añadiremos un uno al código
	   if(tree.hasRight(pos)) {
		   Position<Character> right=tree.right(pos);
		   characterCodesRes(tree, mapa, right, codigo+"1");
	   }
	  
	
}


public static String encode(String text, Map<Character,String> map) {
	String texto="";
    for(int i=0; i<text.length(); i++) {
    	Character c=text.charAt(i);
    	texto=texto+map.get(c);
    }
    return texto;
  }

  public static String decode(String encodedText, BinaryTree<Character> huffmanTree) {
    String texto="";
    Position<Character> pos = huffmanTree.root();
    //recorremos todo el texto codificado
    for (int i=0; i<encodedText.length(); i++) {
    	Character c=encodedText.charAt(i);
    	
    	if(c.equals('0')) {
    		//si el caracter es un cero recorremos el arbol a la izquierda
    		pos=huffmanTree.left(pos);
    		//cuando llegamos a una hoja hemos encontrado el caracter correspondiente 
    			if(huffmanTree.isExternal(pos)) {
    				texto=texto+pos;
    				//para e
        			pos=huffmanTree.root();
    			}
    		
    		
    		
    	}else {
    			pos=huffmanTree.right(pos);
    			if(huffmanTree.isExternal(pos)) {
    				texto=texto+pos;
        			pos=huffmanTree.root();
    			}
    	
    }
   
  }

    return texto;
  }
}

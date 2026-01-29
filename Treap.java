package aed.treap;

import es.upm.aedlib.Pair;
import es.upm.aedlib.Position;
import es.upm.aedlib.positionlist.*;
import es.upm.aedlib.tree.*;
import java.util.Iterator;
import java.util.Random;

public class Treap<E extends Comparable<E>> implements Iterable<E> {
  private LinkedBinaryTree<Pair<E,Integer>> treap;
  private Random rand;

  public Treap() {
    this.treap = new LinkedBinaryTree<Pair<E,Integer>>();
    this.rand = new Random();
  }
  
  public int size() {
    return treap.size();
  }

  public boolean isEmpty() {
    return treap.isEmpty();
  }

  public boolean add(E e) {
	//si el elemento es nulo lanza una excepcion
	  if(e==null) {
		  throw new IllegalArgumentException();
	  }
	  Pair<E, Integer> p= new Pair<>(e, rand.nextInt());
	  //si el treap esta vacio añadimos el elemento como raiz
	  if(treap.isEmpty()) {
		  treap.addRoot(p);
		  return true;
	  }
	  
	  Position<Pair<E,Integer>> w=search(e,treap.root());
	  
	//si treap contiene al elemento no lo añadiremos de nuevo
	 if(w.element().getLeft().equals(e)) {
		 return false;
	 }

	 
	  //si treap no contiene al elemento w sera el nodo donde p deberia ser insertado como hijo
	  
		  Position<Pair<E,Integer>> nodo=null;
		  //si e es mas pequeño lo insertamos en el lado izquierdo, en caso contrario lo insertaremos en el lado derecho
		  if(w.element().getLeft().compareTo(e)<0) {
			   nodo= treap.insertRight(w, p);
		  }else {
			  nodo=treap.insertLeft(w, p);
		  }
		  //una vez insertado lo rotamos para que cumpla el orden de prioridades
		  while (treap.parent(nodo) != null &&
		           nodo.element().getRight().compareTo(treap.parent(nodo).element().getRight())<0) {
			  
		        treap.rotate(nodo);
		    }
		  
		  return true;  
	  
	  

}
	   
  

  public boolean remove(E e) {
	//si el elemento es nulo lanza una excepcion
	  if(e==null) {
		  throw new IllegalArgumentException();
	  }
	  //si el treap esta vacio no podemos eliminar nada
	  if(treap.isEmpty()) {
		  return false;
	  }
	  Position<Pair<E,Integer>> w=search(e,treap.root()); 
	  //si treap no contiene al elemento no lo podemos borrar;
	  if(!w.element().getLeft().equals(e)) {
		  return false;
	  }
	  //si es una hoja la eliminamos
	  if(treap.isExternal(w)) {
		  treap.remove(w);
		  return true;
	  }
	  while(!treap.isExternal(w)) {
	  //si no es una hoja comprobaremos cual de sus hijos tiene mayor prioridad.
	  Position<Pair<E, Integer>> left  = treap.hasLeft(w) ? treap.left(w) : null;
	  Position<Pair<E, Integer>> right = treap.hasRight(w) ? treap.right(w) : null;
	
	  //si w solo tiene un hijo realizaremos la rotacion con ese hijo
	  if(left==null) {
		treap.rotate(right);
	  }else if(right==null) {
		treap.rotate(left);
		//si tiene dos hijos realizaremos la rotacion con el hijo de mayor prioridad
		}else {
		  if(left.element().getRight().compareTo(right.element().getRight())<0) {
			  treap.rotate(left);
		  }else {
			  treap.rotate(right);
		  }
		 
		  
	  }

	  }
	   //una vez hemos encontrado el nodo y es una hoja lo eliminamos
	  treap.remove(w);
	   return true;
  }

  public boolean contains(E e) {
	  //si el elemento es nulo lanza una excepcion
	  if(e==null) {
		  throw new IllegalArgumentException();
	  }
	  //si el arbol esta vacio no contendrá al elemento 
	  if (treap.isEmpty()) {
		  return false;
	  } 
	  // buscamos al nodo que lo contenga con search o el nodo que deberia ser su padre en caso de que treap no lo contenga
	    Position<Pair<E,Integer>> w = search(e, treap.root());
	    // si hemos encontrado el nodo devuelve true, si el nodo no esta en treap devuelve false
	    return w.element().getLeft().equals(e);
  }

  //necesito una forma de optener la lista que estoy creando en preorder 
  public Iterator<E> iterator() {
	  PositionList <E> list=new NodePositionList <>();
	  //si el arbol no esta vacio completaremos la lista en orden ascendente
	  if(!treap.isEmpty()) {
		  preorder(treap.root(),list)	;
				 }
	  
    return list.iterator();
  }
  
  
  //partiendo de un treap rellena una lista que contenga todos los nodos de treap en orden ascendente
  private void preorder(Position<Pair<E,Integer>> v, PositionList<E> list) {
	    //comprobamos si exiten hijo izquierdo y derecho
	    Position<Pair<E, Integer>> left  = treap.hasLeft(v) ? treap.left(v) : null;
		
	    if(left!=null) {
	    	preorder(left,list);//recorremos el subarbol izquierdo
	    }
	    
	    list.addLast(v.element().getLeft());//guardamos solo el elemento
	    
	    Position<Pair<E, Integer>> right = treap.hasRight(v) ? treap.right(v) : null;
	    if(right!=null) {
	    	preorder(right,list);//recorremos el subarbol derecho
	    }

	}

  
  @Override
  public String toString() {
    return treap.toString();
  }
  
  
  // busca un nodo con la clave e dentro el arbol. Si existe devuelve el nodo , si no existe devuelve el nodo donde e deberia ser insertado como hijo.
  private Position<Pair<E,Integer>> search(E e, Position<Pair<E, Integer>> v){
	  int cmp = e.compareTo(v.element().getLeft());
	  //si el elemento de v y e son iguales devolvemos v
	  if(cmp==0) {
		  return v;
	  }
 
	  
	  Position<Pair<E, Integer>> left  = treap.hasLeft(v) ? treap.left(v) : null;
	  Position<Pair<E, Integer>> right = treap.hasRight(v) ? treap.right(v) : null;
      
	    //si e es mas pequeño que el elemento de v seguimos recorriendo el subarbol izquierdo
      if(cmp<0) {
    	  if(left!=null) {
    		  return search(e,left);
    		  //si left es null es porque hemos llegado a una hoja sin encontrar e por tanto e debera ser agregada como hijo de esa hoja
    	  }else {
    		  return v;
    	  }  
      }
      
    //si e es mas grande que el elemento de v seguimos recorriendo el subarbol derecho
      if(cmp>0) {
    	  if(right!=null) {
    		  return search(e,right);
    		  //si right es null es porque hemos llegado a una hoja sin encontrar e por tanto e debera ser agregada como hijo de esa hoja
    	  }else {
    		  return v;
    	  }
      }
	 return null;
  }
}

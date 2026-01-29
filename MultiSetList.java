package aed.multisets;

import es.upm.aedlib.Pair;
import es.upm.aedlib.Position;
import es.upm.aedlib.positionlist.PositionList;
import es.upm.aedlib.positionlist.NodePositionList;


/**
 * Una implementacion de un multiset (multiconjunto) a traves de una lista
 * de posiciones.
 */
public class MultiSetList<E> implements MultiSet<E> {
  
  /**
   * La estructura de datos que guarda los elementos del multiset.
   */
  private PositionList<Pair<E,Integer>> elements;
  
  /**
   * El tamaño del multiset.
   */
  private int size;
  
  /**
   * Construye un multiset vacio.
   */
  public MultiSetList() {
    this.elements = new NodePositionList<Pair<E,Integer>>();
    this.size = 0;
  }

@Override
public void add(E elem, int n) {
   
	if (n<0) {
		throw new IllegalArgumentException("El número de elementos tiene que ser cero o mayor que cero");
	}
	
	if (n>0) {
		Position<Pair<E, Integer>> p = posicionPar (elem);
		// si elem ya estaba en elements actualizamos el par que lo contenía
		if (p!=null) {		
			Pair<E, Integer> elemNuevo =new Pair <>(elem,p.element().getRight() + n ) ;   				
			elements.set (p , elemNuevo);
		} else {
			elements.addLast( new Pair <>(elem,n));		
		}
		
		this.size=this.size+n;		
	}

}

	

/**
 * Borra n copias de elem en el multiset. Si no hay al menos n
 * copias no se borra ningun copia. Notad que elem podria ser null.
 * Devuelve true si logro borrar n copias, y false si no.
 *
 * @throws IllegalArgumentException si n<0.
 */
@Override
public boolean remove(E elem, int n) {
	if (n<0) {
		throw new IllegalArgumentException("El número de elementos tiene que ser cero o mayor que cero");
	}
	boolean elemBorrado = false;
	if (n==0) {
		return true;
	}
		
	//comprobamos si existe un par en elements que tenga por 'objeto' a elem
	Position<Pair<E, Integer>> p = posicionPar (elem);
	//si existe comprobamos que la cantidad a sustraer sea menor o igual que la multiplicidad de elem y actualizamos el size
	if (p!=null) {
		int maxElementosBorrar = p.element().getRight();
		//si es igual eliminamos el elemento
		if (n == maxElementosBorrar) {
			elements.remove(p);
			elemBorrado = true;
			this.size=this.size-n;
		}
		// si es menor modificamos la multiplicidad de elem
		if (n < maxElementosBorrar ) {
			Pair<E, Integer> elemNuevo =new Pair <>(elem, maxElementosBorrar - n) ;   				
			elements.set (p , elemNuevo);
			elemBorrado = true;
			this.size=this.size-n;
		}
		
	}

   return elemBorrado;	
	
}



@Override
public int multiplicity(E elem) {
	  /**
	   * Devuelve el numero de copias de elem en el multiset.
	   * Notad que elem podria ser null.
	   *
	   */
	
	int totalCopias = 0;
	//comprobamos si existe un par en elements que tenga por 'objeto' a elem
	Position<Pair<E, Integer>> elemActual = posicionPar (elem); 
	if (elemActual!=null) {
		totalCopias = elemActual.element().getRight();
	}
	
	return totalCopias;
}

@Override
public int size() {
	return this.size;
}

@Override
public boolean isEmpty() {
	return this.size==0;
}

@Override
public PositionList<E> elements() {
	PositionList<E> listaResul;
	listaResul = new NodePositionList<>();
			
	for (Pair<E, Integer> e :elements) {
		listaResul.addLast(e.getLeft());			
	}
	
	return listaResul;
}

@Override
public MultiSet<E> sum(MultiSet<E> s) {
	
	MultiSet<E> resul = new MultiSetList<>();
	// Metemos en resul todos los elementos de la lista "s"
	resul = s.intersection(s);
	
	// Y a esos les añadimos nuestros elementos
	Position<Pair<E, Integer>> listaActual= elements.first() ;	
	while (listaActual != null ) {
		resul.add(listaActual.element().getLeft() , listaActual.element().getRight());
		listaActual = elements.next(listaActual);
	}
	

	return resul;
}

@Override
public MultiSet<E> minus(MultiSet<E> s) {
	MultiSet<E> resul = new MultiSetList<>();
	// Los elementos tienen que estar en nuestra lista actual, y restarlos sin aparecen en "s"
	// Como multiplicidad se pone max(s1.multiplicity(e) − s2.multiplicity(e),0)
	for (Pair<E, Integer> e :elements) {
		int sMultiplicidad = s.multiplicity(e.getLeft());				
		int eMultiplicidad=multiplicity(e.getLeft());
		int multiplicidadAct=eMultiplicidad - sMultiplicidad;
	    resul.add(e.getLeft(), Math.max(multiplicidadAct ,0)  );

		
	}
		
	return resul;

}

@Override
public MultiSet<E> intersection(MultiSet<E> s) {
	MultiSet<E> resul = new MultiSetList<>();
	// Los elementos tienen que estar en nuestra lista actual, y en la que nos han pasado en "s"
	// Como multiplicidad se pone la menor
	for (Pair<E, Integer> e :elements) {
		int sMultiplicidad = s.multiplicity(e.getLeft());				
		if ( sMultiplicidad  > 0 ) {
			resul.add(e.getLeft(), Integer.min(sMultiplicidad, e.getRight())  );
		}
	}
		
	return resul;
}

@Override
public boolean subsetEqual(MultiSet<E> s) {
	boolean resul=true;
	Position<Pair<E, Integer>> elemLista= elements.first() ;	
	//recorremos elements hasta el final a no ser que encontremos un par que no cumpla la condicion 
	while (elemLista != null && resul ) {
		if ( s.multiplicity(elemLista.element().getLeft()) < this.multiplicity(elemLista.element().getLeft())) {
			resul=false;
		}
		elemLista = elements.next(elemLista);
	}
	
		
  
  return resul;
}

//devuelve el par que contiene al elemento e
private Position<Pair<E, Integer>> posicionPar (E elem){
	//elemActual contiene el par dentro de la lista elements sobre el que estamos haciendo la comparaciçon
	Position<Pair<E, Integer>> elemActual = elements.first() ;
	// elemResultado contiene el par, si existe en elements, que tenga por 'objeto' a elem.
	Position<Pair<E, Integer>> elemResultado = null;
	
	while (elemActual != null && elemResultado == null) {
		boolean sonIguales = false;
		//comprobamos si los elementos son null, si ambos son null entonces seran iguales
		if (elemActual.element().getLeft() == null) {
			if (elem == null) sonIguales = true; 
			// en otro caso, sabiendo que el elemActual no es null vemos si ambos son iguales
		} else {			
			if (elemActual.element().getLeft().equals(elem)) {
				sonIguales = true;
			}	
		}
		
		//si son iguales actualizamos elemResultado, si no, tomamos el siguiente par en elements
		if (sonIguales ) {
			elemResultado = elemActual;
		} else {			
			elemActual = elements.next(elemActual); 
		}		
	}
		
	return elemResultado;
}
}

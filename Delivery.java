package aed.delivery;

import es.upm.aedlib.graph.DirectedAdjacencyListGraph;
import es.upm.aedlib.graph.DirectedGraph;
import es.upm.aedlib.graph.Edge;
import es.upm.aedlib.graph.Vertex;
import es.upm.aedlib.map.HashTableMap;
import es.upm.aedlib.positionlist.NodePositionList;
import es.upm.aedlib.positionlist.PositionList;

public class Delivery<V> {

	private   DirectedGraph<V, Integer> grafo = null;
	

// Construct a graph out of a series of vertices and an adjacency matrix.
  // There are 'len' vertices. A null means no connection. A non-negative
  // number represents distance between nodes.
  public Delivery(V[] places, Integer[][] gmat) {
	  if (places == null || gmat == null) {
          throw new IllegalArgumentException();
      }
	  this.grafo = new DirectedAdjacencyListGraph<>();
	  HashTableMap<V, Vertex<V>> mapaVerts = new HashTableMap<>();

	  //añado todos los vertices al grafo
	  for (int i = 0; i < places.length; i++) {
          V place = places[i];

          if (place != null) {
        	  //guardamos los vertices en mapaVerts
              Vertex<V> VertNuevo = this.grafo.insertVertex(place);              
              mapaVerts.put(place, VertNuevo);
          } 
	  }
	  
      //bucle que añade las aristas al grafo
      for (int a = 0; a < places.length; a++) {
          for (int k = 0; k < places.length; k++) {
        	  Integer peso=gmat[a][k];
        	  //si existe una arista entre el vertice en la posicion a y el vertice en la posicion k la añadimos al grafo
        	  if(peso!=null) {
        		  Vertex<V> vertSalida=mapaVerts.get(places[a]);
        		  Vertex<V> vertLlegada = mapaVerts.get(places[k]);
        		  grafo.insertDirectedEdge(vertSalida, vertLlegada, peso);
        	  }
  
          }
        	  
      }
        	  

    System.out.println( "-------------");
    System.out.println( grafo.toString() );
            
	  
  }
    
  // Just return the graph that was constructed
  public DirectedGraph<V, Integer> getGraph() {
    return this.grafo;
  }

//Return a Hamiltonian path for the stored graph, or null if there is none.
 // The list containts a series of vertices, with no repetitions (even if the path
 // can be expanded to a cycle).
  
 public PositionList <Vertex<V>> tour() {
	 PositionList<Vertex<V>> res = new NodePositionList<>();
	 
	    // Intentar empezar desde cada vértice
	    for (Vertex<V> verticeActual : grafo.vertices() ) {
	      PositionList<Vertex<V>> path = new NodePositionList<>();
	      path.addLast(verticeActual);
	      
	      // Desde el vértice en el que estamos, intamos alcanzar el resto de vértices
	      if (tourRec(verticeActual, path, grafo.numVertices())) {	    	  
	        // Añadimos todo el camino encontrado
	        for (Vertex<V> v : path) {
	          res.addLast(v);	          	          
	        }
	        
	        System.out.println ( "Camino ->" + res.toString() );

	        return res;
	      }
	    }
	    // No existe camino hamiltoniano
	    return null; 
 }

 private boolean tourRec(Vertex<V> verticeActual, PositionList<Vertex<V>> path, int totalVertices) 
 {
	// Si ya hemos metido todos los vértices, es un camino hamiltoniano
	if (path.size() == totalVertices) {
	   return true;
	}
	
	// Vamos a ir probando los vértices a los que podemos llegar desde el actual
	for (Edge<Integer> e : grafo.outgoingEdges(verticeActual)) {
		Vertex<V> verticeSiguiente = grafo.endVertex(e);
		
		if (!existeVerticeEnCamino(path, verticeSiguiente)) {
		   path.addLast(verticeSiguiente);
		   if (tourRec(verticeSiguiente, path, totalVertices)) {
		        return true;
		   }
		
		// No hemos encontrado un camino válido, quitamos el vértice de nuestro path
		path.remove(path.last());
		}
	}
	return false;
}
//devuelve true si el vertice ya esta contenido el tour y false en caso contrario
private boolean existeVerticeEnCamino(PositionList<Vertex<V>> path, Vertex<V> vertice) {
  for (Vertex<V> p : path) {    
    if (p.element().equals(vertice.element())) return true;
  }
  return false;
}


public int length (PositionList<Vertex<V>> path) {
	int sumaLen = 0;
	if (path.isEmpty() || path.size() < 2) return 0;
	
	Vertex<V> vertSalida = null;
	Vertex<V> vertLlegada = null;
	
	for (Vertex<V> p : path) {
	   vertLlegada = p;
       if (vertSalida!=null) {
    	   // Tenemos que obtener la longitud entre el vertice de salida y su destino
   	       Edge<Integer> e = buscarArista (vertSalida, vertLlegada);
   	       if (e.element()!=null) sumaLen += e.element();  // el peso es un Integer
   	       
       }
		  
	   vertSalida = vertLlegada;	  
	}
	return sumaLen;

}


private Edge<Integer> buscarArista (Vertex<V> actual, Vertex<V> siguiente) {
	if (actual == null || siguiente == null || siguiente.element() == null ) return null;
	
	for (Edge<Integer> e : grafo.outgoingEdges(actual)) {		  
		  if (grafo.endVertex(e).element().equals(siguiente.element())) {
	      return e;
	    }
	  }
	  return null;  // no hay arista 
	}


public String toString() {
    return "Delivery";
  }
}

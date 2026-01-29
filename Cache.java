package aed.cache;

import java.util.Iterator;

import es.upm.aedlib.Entry;
import es.upm.aedlib.Position;
import es.upm.aedlib.map.*;
import es.upm.aedlib.positionlist.*;


public class Cache<Key,Value> {
  

  // Tamano de la cache
  private int maxCacheSize;

  // NO MODIFICA ESTOS ATTRIBUTOS, NI CAMBIA SUS NOMBRES: mainMemory, cacheContents, keyListLRU

  // Para acceder a la memoria M
  private Storage<Key,Value> mainMemory;
  // Un 'map' que asocia una clave con un ``CacheCell''
  private Map<Key,CacheCell<Key,Value>> cacheContents;
  // Una PositionList que guarda las claves en orden de
  // uso -- la clave mas recientemente usado sera el keyListLRU.first()
  private PositionList<Key> keyListLRU;
  


  // Constructor de la cache. Especifica el tamano maximo 
  // y la memoria que se va a utilizar
  public Cache(int maxCacheSize, Storage<Key,Value> mainMemory) {
    this.maxCacheSize = maxCacheSize;

    // NO CAMBIA
    this.mainMemory = mainMemory;
    this.cacheContents = new HashTableMap<Key,CacheCell<Key,Value>>();
    this.keyListLRU = new NodePositionList<Key>();
  }
  

   private CacheCell<Key, Value> actualizarElemCacheComoUltimoAccedido (CacheCell<Key, Value> cell,Key key ) {
       // cuando un elemento que ya estaba en el cache actualizamos su posición
	   keyListLRU.remove(cell.getPos());	   
	   keyListLRU.addFirst(key);
	   Position<Key> newPos=this.keyListLRU.first();
	   cell.setPos(newPos);
	   return cell;
   }
   
  // Devuelve el valor que corresponde a una clave "Key"
  public Value get(Key key) {
	  CacheCell<Key, Value> cell;
	  
	  // ¿Está la celda buscada en el caché?
	  cell = this.cacheContents.get(key);
	  //si esta en el cache devolvemos su valor 
	  if (cell != null) {
		  cell=actualizarElemCacheComoUltimoAccedido (cell,key);
		  return cell.getValue();
	  }
	  
	  // ¿Está la celda buscada en la memoria?
	  Value valor = mainMemory.read(key);
	  if (valor == null) {
		  return null; // No hemos encontrado la celda buscada, devuelvo nulo
	  } 
//si no está en el cache pero si en la memoria metemos la celda en el cache
	  cell=insertarElemCacheComoUltimoAccedido(key);
	 
	 //devolvemos el valor de la celda
    return cell.getValue();
  }



  
  // Establece un valor nuevo para la clave en la memoria cache
  public void put(Key key, Value value) {
 
	  CacheCell<Key, Value> cell = this.cacheContents.get(key);
	  //¿esta la celda en el cache?
	  if (cell == null) {
		  cell=insertarElemCacheComoUltimoAccedido(key);//insertamos una celda en el cache
	  } else {
		//si ya estaba en el cache actualizamos la posicion de su clave en la lista
		 cell= actualizarElemCacheComoUltimoAccedido(cell,key);  
	  }
	  
	  //actualizamos el valor del elemento y si ha sido modificado
	  cell.setValue(value);
	  cell.setDirty(true);

  }


public CacheCell<Key, Value>  insertarElemCacheComoUltimoAccedido(Key key) {
	CacheCell<Key, Value> cell ;
	  //si el cache está lleno eliminamos el elemento que mas tiempo lleve sin ser accedido  	  
	  if(this.cacheContents.size()==this.maxCacheSize) {
			CacheCell<Key, Value> aux=cacheContents.get(keyListLRU.last().element());
			//si su valor a sido modificado en el cache lo actualizamos en la memoria
			if(aux.getDirty()) {
				mainMemory.write(keyListLRU.last().element(), aux.getValue());
				aux.setDirty(false);
			}
			this.cacheContents.remove(keyListLRU.last().element());
			this.keyListLRU.remove(keyListLRU.last());
		}
	  //una vez hay hueco en el cache añadimos al cache la celda correspondiente de la memoria
	   this.keyListLRU.addFirst(key);
	   Position<Key> pos=keyListLRU.first();
	  cell = new CacheCell<> (mainMemory.read(key), false, pos);
	  this.cacheContents.put(key, cell);

	  return cell;
}

  // NO CAMBIA
  public String toString() {
    return "cache";
  }
}

 



package aed.almacen;

import es.upm.aedlib.indexedlist.IndexedList;
import es.upm.aedlib.indexedlist.ArrayIndexedList;


/**
 * Implementa la logica del almacen.
 */
public class Almacen implements ClienteAPI, AlmacenAPI, ProductorAPI {

  // Compras (sin ningun orden especial)
  private ArrayIndexedList<Compra> compras;
  // Productos ordenados ascendamente usando el productoId de un Product.
  private ArrayIndexedList<Producto> productos;

  // No es necesario cambiar el constructor
  /**
   * Crea un almacen.
   */
  public Almacen() {
    this.compras = new ArrayIndexedList<>();
    this.productos = new ArrayIndexedList<>();
  }

@Override
public void reabastecerProducto(String productoId, int cantidad) {
	// TODO Auto-generated method stub
	
}

@Override
public Producto getProducto(String productoId) {
	
	int res=busquedaBinaria(productoId);
	if (res!=-1) {
		return productos.get(res);
	}
	return null;
	
}

@Override
public Compra getCompra(Integer compraId) {
	int i;
	for(i=0; i<compras.size()   ; i++) {
		if(compras.get(i).getCompraId().equals(compraId))
			return compras.get(i);
	}
	 return null;
}


@Override
public IndexedList<Producto> getProductos() {
	IndexedList<Producto> lista=new ArrayIndexedList<Producto>();
	 for(int i=0; i<productos.size();i++)
		 lista.add(i, productos.get(i));
	return lista;
}

@Override
public IndexedList<Compra> getCompras() {
	IndexedList<Compra> lista=new ArrayIndexedList<Compra>();
	 for(int i=0; i<productos.size();i++)
		 lista.add(i, lista.get(i));
	return lista;
}

@Override
public IndexedList<Compra> comprasCliente(String clienteId) {
	// TODO Auto-generated method stub
	IndexedList<Compra> lista=new ArrayIndexedList<Compra>();
	
	for(int i=0; i<compras.size(); i++) {	
		if(compras.get(i).getClienteId().equals(clienteId)) {
			lista.add(lista.size(),compras.get(i) );
		}
	}
	
	return lista;
}

@Override
public IndexedList<Compra> comprasProducto(String productoId) {
	IndexedList<Compra> lista=new ArrayIndexedList<Compra>();
	
	for(int i=0; i<compras.size(); i++) {	
		if(compras.get(i).getProductoId().equals(productoId)) {
			lista.add(lista.size(),compras.get(i) );
		}
	}
	
	return lista;
}

@Override
public Integer pedir(String clienteId, String productoId, int cantidad) {
	int i=busquedaBinaria(productoId);
	if(i>-1) {
		if(productos.get(i).getCantidadDisponible()>cantidad) {
			Compra compra=new Compra(i, productoId, productoId, i);
		}
	}
		
		
		
		
		
		
		
		
		
	return null;
}


  // Implementa los métodos necesarios aqui ...

public int busquedaBinaria( String elem) {
	int last= productos.size();
	int first=0;
	while(first<=last) {
		int centro=(first+last)/2;
	     String valor= productos.get(centro).getProductoId();
		if(valor.compareTo(elem)==0) {
			return  centro;
		}else if(valor.compareTo(elem)<0) {
			first=centro+1;
		}else {
			last=centro-1;
		}
		 
		
	}
	return -1;
			
}
}































































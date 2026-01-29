package aed.delivery;

import es.upm.aedlib.Pair;
import es.upm.aedlib.positionlist.NodePositionList;
import es.upm.aedlib.positionlist.PositionList;


public class Buscar {
  
  public static Pair<String,PositionList<Direccion>> busca(Laberinto laberinto) {
	  PositionList<Direccion> camino = new NodePositionList<>();

	  if (buscaRec(laberinto, camino)) {
	    String regalo = laberinto.getRegalo();
	    return new Pair<>(regalo, camino);
	  }
	  return null;
  }
private static boolean puedoMoverHacia (Laberinto laberinto, Direccion d) {
	Laberinto l2 = new Laberinto(laberinto);
	l2.moverHacia(d);
	return ! l2.sueloMarcadoConTiza();	
}
private static Direccion dirContraria (Direccion d) {
	Direccion contraria = Direccion.NORTE;
	switch (d) {
	   case NORTE: contraria = Direccion.SUR; break;
	   case SUR: contraria = Direccion.NORTE; break;
	   case ESTE: contraria = Direccion.OESTE; break;
	   case OESTE: contraria = Direccion.ESTE; break;
	} 
	return contraria;
	
}
private static boolean buscaRec(Laberinto laberinto, PositionList<Direccion> camino) {
			
	// Si hay regalo en la posición actual, hemos terminado
	if (laberinto.tieneRegalo()) {
	return true;
	}
	
	// Probar todas las direcciones posibles
	Iterable<Direccion> dirs = laberinto.direccionesPosibles();
	for (Direccion dirDisponible : dirs) {		
		if (puedoMoverHacia (laberinto, dirDisponible)) {
			// Marcamos la posición actual como visitada
			laberinto.marcaSueloConTiza();	

			// Nos movemos a la direccion disponible 
			laberinto.moverHacia(dirDisponible);
			
			// Marcamos la posición a la que nos acabamos de mover, como visitada
			laberinto.marcaSueloConTiza();
						
			// Apilamos la dirección en el camino
			camino.addLast(dirDisponible);
						
			// Llamada recursiva
			if (buscaRec(laberinto, camino)) {
			   return true;  // se ha encontrado el regalo
			}
			
			// Desandamos el camino			
			laberinto.moverHacia(dirContraria (dirDisponible));
			camino.remove(camino.last());
			
		}

	}
		
	// No hay camino al regalo desde esta casilla
	return false;
	}
}

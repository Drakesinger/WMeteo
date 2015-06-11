
package ch.hearc.meteo.imp.afficheur.tools;

import javax.swing.ImageIcon;

/**
* Les images doivent se trouver dans un jar, et le jar dans le classpath!
* Le jar doit contenir le folder ressources. A l'interieur du folder ressource doit se trouver les images aux formats (jpg, voir mieux png pour la transparance)
*/
public class ImageStore
	{

	/*------------------------------------------------------------------*\
	|*		 Version Synchrone (bloquant)								*|
	\*------------------------------------------------------------------*/

	public static final ImageIcon altitude = ImageLoader.loadSynchrone("./res/images/altitude.png");
	public static final ImageIcon thermometer = ImageLoader.loadSynchrone("./res/images/thermometer.png");
	public static final ImageIcon pressure = ImageLoader.loadSynchrone("./res/images/pressure.png");

	/*------------------------------------------------------------------*\
	|*		Version Assynchrone	(non bloquant)							*|
	\*------------------------------------------------------------------*/

	//public static final ImageIcon warning = ImageLoader.loadAsynchroneJar("./ressources/warning.png");
	//public static final ImageIcon coffee = ImageLoader.loadAsynchroneJar("./ressources/coffee_logo.png");
	//public static final ImageIcon cervin = ImageLoader.loadAsynchroneJar("ressources/cervin.jpg");

	}

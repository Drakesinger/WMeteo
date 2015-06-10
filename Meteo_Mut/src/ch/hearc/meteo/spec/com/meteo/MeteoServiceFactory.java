
package ch.hearc.meteo.spec.com.meteo;

import ch.hearc.meteo.imp.com.real.MeteoService;
import ch.hearc.meteo.imp.com.real.com.ComConnexion;
import ch.hearc.meteo.imp.com.real.com.ComOption;

public class MeteoServiceFactory implements MeteoServiceFactory_I
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	@Override
	public MeteoService_I create(String portName)
		{

		ComConnexion comConnexion = new ComConnexion(portName, new ComOption());
		MeteoService meteoService = new MeteoService(comConnexion);
		comConnexion.setMeteoServiceCallback(meteoService);

		return meteoService;
		}

	/*------------------------------*\
	|*				Set				*|
	\*------------------------------*/

	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/
	}

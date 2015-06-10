
package ch.hearc.meteo.spec.afficheur;

import ch.hearc.meteo.imp.afficheur.real.AfficheurService;
import ch.hearc.meteo.spec.reseau.rmiwrapper.MeteoServiceWrapper_I;

public class AfficheurFactory implements AfficheurFactory_I
	{

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	/*------------------------------*\
	|*			  Static			*|
	\*------------------------------*/

	public static AfficheurService_I create(AffichageOptions affichageOptions, MeteoServiceWrapper_I meteoServiceRemote)
		{
		return new AfficheurService(affichageOptions, meteoServiceRemote);
		}

	@Override
	public AfficheurService_I createOnCentralPC(AffichageOptions affichageOptions, MeteoServiceWrapper_I meteoServiceRemote)
		{
		//return new AfficheurService(affichageOptions, meteoServiceRemote,);
		return AfficheurFactory.create(affichageOptions, meteoServiceRemote);
		}

	@Override
	public AfficheurService_I createOnLocalPC(AffichageOptions affichageOptions, MeteoServiceWrapper_I meteoServiceRemote)
		{
		return AfficheurFactory.create(affichageOptions, meteoServiceRemote);
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	}

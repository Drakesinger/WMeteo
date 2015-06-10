
package ch.hearc.meteo.imp.afficheur.real;

import ch.hearc.meteo.imp.afficheur.real.moo.AfficheurServiceMOO;
import ch.hearc.meteo.imp.afficheur.real.vue.JFrameAfficheurService;
import ch.hearc.meteo.spec.afficheur.AffichageOptions;
import ch.hearc.meteo.spec.afficheur.AfficheurService_I;
import ch.hearc.meteo.spec.com.meteo.MeteoServiceOptions;
import ch.hearc.meteo.spec.com.meteo.listener.event.MeteoEvent;
import ch.hearc.meteo.spec.reseau.rmiwrapper.MeteoServiceWrapper_I;

public class AfficheurService implements AfficheurService_I
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public AfficheurService(AffichageOptions affichageOptions, MeteoServiceWrapper_I meteoServiceRemote)
		{
		// The JFrame is a singleton
		//jFrameAfficheurService = JFrameAfficheurService.getInstance();

		if (meteoServiceRemote != null)
			{
			if (DEBUG_MODE)
				{
				System.out.println("meteoServiceRemote not null:");
				System.out.println(meteoServiceRemote.hashCode());
				}
			afficheurServiceMOO = new AfficheurServiceMOO(affichageOptions, meteoServiceRemote);
			}

		jFrameAfficheurService = new JFrameAfficheurService(true, afficheurServiceMOO);

		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	@Override
	public void printPression(MeteoEvent event)
		{
		afficheurServiceMOO.printAltitude(event);
		jFrameAfficheurService.refresh();
		}

	@Override
	public void printAltitude(MeteoEvent event)
		{
		afficheurServiceMOO.printTemperature(event);
		jFrameAfficheurService.refresh();
		}

	@Override
	public void printTemperature(MeteoEvent event)
		{
		afficheurServiceMOO.printPression(event);
		jFrameAfficheurService.refresh();
		}

	@Override
	public void updateMeteoServiceOptions(MeteoServiceOptions meteoServiceOptions)
		{
		jFrameAfficheurService.updateMeteoServiceOptions(meteoServiceOptions);
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Tools
	private AfficheurServiceMOO afficheurServiceMOO;
	// TODO private JPanelStation jPanelStation;
	/*------------------------------*\
	|*			  Static			*|
	\*------------------------------*/

	// Tools
	private JFrameAfficheurService jFrameAfficheurService;
	private static final boolean DEBUG_MODE = true;

	}

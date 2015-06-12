
package ch.hearc.meteo.imp.reseau;

import java.rmi.RemoteException;

import ch.hearc.meteo.imp.afficheur.simulateur.AfficheurSimulateurFactory;
import ch.hearc.meteo.spec.afficheur.AffichageOptions;
import ch.hearc.meteo.spec.afficheur.AfficheurService_I;
import ch.hearc.meteo.spec.reseau.RemoteAfficheurCreator_I;
import ch.hearc.meteo.spec.reseau.rmiwrapper.AfficheurServiceWrapper;
import ch.hearc.meteo.spec.reseau.rmiwrapper.MeteoServiceWrapper_I;

import com.bilat.tools.reseau.rmi.IdTools;
import com.bilat.tools.reseau.rmi.RmiTools;
import com.bilat.tools.reseau.rmi.RmiURL;

/**
 * <pre>
 * One instance only (Singleton) on PC-Central
 * RMI-Shared
 * </pre>
 */
public class RemoteAfficheurCreator implements RemoteAfficheurCreator_I
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	private RemoteAfficheurCreator() throws RemoteException
		{
		server();
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	/**
	 * meteoServiceRmiURL is the URL of the meteo service running on Local PC
	 * afficheurServicermiURL is the URL of the UI service running on Central PC
	 */
	@Override
	public RmiURL createRemoteAfficheurService(AffichageOptions affichageOptions, RmiURL meteoServiceRmiURL) throws RemoteException
		{
		// Create a RmiURL for the Central PC.
		RmiURL afficheurCentralServicermiURL = rmiUrl();
		try
			{
			MeteoServiceWrapper_I meteoServiceRemote = null;

				// client
				{
				// Connection to meteoService on PC-Local with meteoServiceRmiURL and retrive it's remote.
				meteoServiceRemote = (MeteoServiceWrapper_I)RmiTools.connectionRemoteObjectBloquant(meteoServiceRmiURL);
				}

				// server
				{
				// Change the UI title to CentralPC so we can diferentiate the GUIs.
				AffichageOptions affichageOptionsCentral = affichageOptions;
				affichageOptionsCentral.setTitre("CentralPC");

				// Startup the UI service and wrap it in a remote.
				AfficheurService_I afficheurService = createAfficheurService(affichageOptions, meteoServiceRemote);
				AfficheurServiceWrapper afficheurServiceWrapper = new AfficheurServiceWrapper(afficheurService);

				// Share afficheurService.
				RmiTools.shareObject(afficheurServiceWrapper, afficheurCentralServicermiURL);

				// Return the rmiURL of the UI service on Central PC to the Local PC that called this method.
				return afficheurCentralServicermiURL; // Retourner le RMI-ID pour une connection distante sur le serveur d'affichage
				}
			}
		catch (RemoteException e)
			{
			System.err.println("Could not share object. @:" + afficheurCentralServicermiURL);

			// Return the URL regardless.
			return afficheurCentralServicermiURL;
			}
		catch (Exception e)
			{
			e.printStackTrace();
			return null;
			}
		}

	/*------------------------------*\
	|*			  Static			*|
	\*------------------------------*/

	public static synchronized RemoteAfficheurCreator_I getInstance() throws RemoteException
		{
		if (INSTANCE == null)
			{
			INSTANCE = new RemoteAfficheurCreator();
			}

		return INSTANCE;
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	private AfficheurService_I createAfficheurService(AffichageOptions affichageOptions, MeteoServiceWrapper_I meteoServiceRemote)
		{
		return (new AfficheurSimulateurFactory()).createOnCentralPC(affichageOptions, meteoServiceRemote);
		//return (new AfficheurFactory()).createOnCentralPC(affichageOptions, meteoServiceRemote);
		}

	private void server() throws RemoteException
		{
		RmiTools.shareObject(this, new RmiURL(RMI_ID));
		}

	/*------------------------------*\
	|*			  Static			*|
	\*------------------------------*/

	/**
	 * Thread Safe
	 */
	private static RmiURL rmiUrl()
		{
		String id = IdTools.createID(PREFIXE);

		return new RmiURL(id);
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	/*------------------------------*\
	|*			  Static			*|
	\*------------------------------*/

	// Tools

	private static RemoteAfficheurCreator_I INSTANCE = null;

	// Tools final
	private static final String PREFIXE = "AFFICHEUR_SERVICE";

	public static final String RMI_ID = PREFIXE;
	}

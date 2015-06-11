
package ch.hearc.meteo.imp.use.remote.pclocal;

import java.rmi.RemoteException;

import ch.hearc.meteo.imp.afficheur.simulateur.AfficheurSimulateurFactory;
import ch.hearc.meteo.imp.com.simulateur.MeteoServiceSimulatorFactory;
import ch.hearc.meteo.imp.use.remote.PC_I;
import ch.hearc.meteo.spec.afficheur.AffichageOptions;
import ch.hearc.meteo.spec.afficheur.AfficheurFactory;
import ch.hearc.meteo.spec.afficheur.AfficheurService_I;
import ch.hearc.meteo.spec.com.meteo.MeteoServiceFactory;
import ch.hearc.meteo.spec.com.meteo.MeteoServiceOptions;
import ch.hearc.meteo.spec.com.meteo.MeteoService_I;
import ch.hearc.meteo.spec.com.meteo.exception.MeteoServiceException;
import ch.hearc.meteo.spec.com.meteo.listener.MeteoAdapter;
import ch.hearc.meteo.spec.com.meteo.listener.event.MeteoEvent;
import ch.hearc.meteo.spec.reseau.RemoteAfficheurCreator_I;
import ch.hearc.meteo.spec.reseau.rmiwrapper.AfficheurServiceWrapper;
import ch.hearc.meteo.spec.reseau.rmiwrapper.AfficheurServiceWrapper_I;
import ch.hearc.meteo.spec.reseau.rmiwrapper.MeteoServiceWrapper;

import com.bilat.tools.reseau.rmi.IdTools;
import com.bilat.tools.reseau.rmi.RmiTools;
import com.bilat.tools.reseau.rmi.RmiURL;

public class PCLocal implements PC_I
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public PCLocal(MeteoServiceOptions meteoServiceOptions, String portCom, AffichageOptions affichageOptions, RmiURL rmiURLafficheurManager)
		{
		this.meteoServiceOptions = meteoServiceOptions;
		this.affichageOptions = affichageOptions;
		this.rmiURLafficheurManager = rmiURLafficheurManager;
		this.portCom = portCom;
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	@Override
	public void run()
		{
		try
			{
			server(); // avant
			}
		catch (Exception e)
			{
			System.err.println("[PCLocal :  run : server : failed");
			e.printStackTrace();
			}

		try
			{
			client(); // aprés
			}
		catch (RemoteException e)
			{
			System.err.println("[PCLocal :  run : client : failed");
			e.printStackTrace();
			}
		catch (MeteoServiceException e)
			{
			System.err.println("Meteo service exception.");
			e.printStackTrace();
			}

		try
			{
			useLocalAndRemote(meteoService, afficheurService, remoteAfficheurCentral);
			//useRemote(meteoService, remoteAfficheurCentral);
			}
		catch (MeteoServiceException e)
			{
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	/*------------------------------*\
	|*				server			*|
	\*------------------------------*/

	private void server() throws MeteoServiceException, RemoteException
		{
		startMeteoService();
		startLocalUIService();
		}

	private void startMeteoService() throws MeteoServiceException, RemoteException
		{
		// Initialize the Meteo Service.
		meteoService = null;
		if (USE_SIMULATOR)
			{
			meteoService = (new MeteoServiceSimulatorFactory()).create(portCom);
			}
		else
			{
			meteoService = (new MeteoServiceFactory()).create(portCom);
			}

		// Wrap the MeteoService so that it can be used as a remote.
		meteoServiceWrapper = new MeteoServiceWrapper(meteoService);

		// Create it's RmiURL.
		//rmiURLmeteoService = RMI_METEO_SERVICE;
		//new RmiURL(IdTools.createID(PREFIXE_METEO));

		// Connect to the meteo service and start it.
		meteoService.connect();
		meteoService.start(meteoServiceOptions);

		// Share this meteoServiceWrapper
		RmiTools.shareObject(meteoServiceWrapper, RMI_METEO_SERVICE);
		}

	@SuppressWarnings("unused")
	private void startLocalUIService() throws MeteoServiceException, RemoteException
		{
		//Local affichage service initialization.
		afficheurService = null;
		if (USE_SIMULATOR)
			{
			afficheurService = (new AfficheurSimulateurFactory()).createOnLocalPC(affichageOptions, meteoServiceWrapper);
			}
		else
			{
			afficheurService = (new AfficheurFactory()).createOnLocalPC(affichageOptions, meteoServiceWrapper);
			}

		// Wrap it in a remote.
		afficheurServiceWrapper = new AfficheurServiceWrapper(afficheurService);
		RmiTools.shareObject(afficheurServiceWrapper, new RmiURL("AFFICHEUR_LOCAL"));
		}

	/*------------------------------*\
	|*				client			*|
	\*------------------------------*/

	private void client() throws RemoteException, MeteoServiceException
		{
		//Get the Remote (PCCentral) by connecting to the supplied RmiURL
		remoteAfficheurCreator = (RemoteAfficheurCreator_I)RmiTools.connectionRemoteObjectBloquant(rmiURLafficheurManager);
		System.out.println("Connection succesful to remote afficheur creator.");

		// Create central UI on the Central PC and share this afficheurServiceWrapper with it.
		RmiURL rmiURLRemoteAfficheur = remoteAfficheurCreator.createRemoteAfficheurService(affichageOptions, RMI_METEO_SERVICE);

		// rmiURLRemoteAfficheur is the URL of the Central PC's UI service.
		// Get a remote to the CentralPC UI service.
		remoteAfficheurCentral = (AfficheurServiceWrapper_I)RmiTools.connectionRemoteObjectBloquant(rmiURLRemoteAfficheur);

		//use(meteoService, afficheurService, remoteAfficheurCentral);
		}

	@SuppressWarnings("unused")
	private void useLocalAndRemote(final MeteoService_I meteoService, final AfficheurService_I afficheurService, final AfficheurServiceWrapper_I telecommandeAfficheurCentral) throws MeteoServiceException
		{
		meteoService.addMeteoListener(new MeteoAdapter()
			{

				@Override
				public void temperaturePerformed(MeteoEvent event)
					{
					afficheurService.printTemperature(event);
					try
						{
						telecommandeAfficheurCentral.printTemperature(event);
						}
					catch (RemoteException e)
						{
						System.err.println("RemoteException: cannot send temperature update to the PCCentral.");
						}
					}

				@Override
				public void altitudePerformed(MeteoEvent event)
					{
					afficheurService.printAltitude(event);
					try
						{
						telecommandeAfficheurCentral.printAltitude(event);
						}
					catch (RemoteException e)
						{
						System.err.println("RemoteException: cannot send Altitude update to the PCCentral.");
						}
					}

				@Override
				public void pressionPerformed(MeteoEvent event)
					{
					afficheurService.printPression(event);
					try
						{
						telecommandeAfficheurCentral.printPression(event);
						}
					catch (RemoteException e)
						{
						System.err.println("RemoteException: cannot send pression update to the PCCentral.");
						}
					}
			});

		// Modify MeteoServiceOptions
		Thread threadSimulationChangementDt = new Thread(new Runnable()
			{

				@Override
				public void run()
					{
					double x = 0;
					double dx = Math.PI / 10;

					while(true)
						{
						long dt = 1000 + (long)(5000 * Math.abs(Math.cos(x))); //ms

						System.out.println("modification dt temperature = " + dt);

						meteoService.getMeteoServiceOptions().setTemperatureDT(dt);

						//	System.out.println(meteoService.getMeteoServiceOptions());

						attendre(3000); // disons
						x += dx;
						}
					}
			});

		// Update GUI MeteoServiceOptions
		Thread threadPoolingOptions = new Thread(new Runnable()
			{

				@Override
				public void run()
					{

					while(true)
						{
						MeteoServiceOptions option = meteoService.getMeteoServiceOptions();
						afficheurService.updateMeteoServiceOptions(option);
						try
							{
							telecommandeAfficheurCentral.updateMeteoServiceOptions(option);
							}
						catch (Exception e)
							{
							System.err.println("RemoteException: cannot send option update to the PCCentral.");
							}

						//System.out.println(option);
						attendre(1000); //disons
						}
					}
			});

		threadSimulationChangementDt.start();
		threadPoolingOptions.start(); // update gui
		}

	@SuppressWarnings("unused")
	private void useRemote(final MeteoService_I meteoService, final AfficheurServiceWrapper_I telecommandeAfficheurCentral) throws MeteoServiceException
		{
		meteoService.addMeteoListener(new MeteoAdapter()
			{

				@Override
				public void temperaturePerformed(MeteoEvent event)
					{
					try
						{
						telecommandeAfficheurCentral.printTemperature(event);
						}
					catch (RemoteException e)
						{
						System.err.println("RemoteException: cannot send temperature update to the PCCentral.");
						}
					}

				@Override
				public void altitudePerformed(MeteoEvent event)
					{
					try
						{
						telecommandeAfficheurCentral.printAltitude(event);
						}
					catch (RemoteException e)
						{
						System.err.println("RemoteException: cannot send Altitude update to the PCCentral.");
						}
					}

				@Override
				public void pressionPerformed(MeteoEvent event)
					{
					try
						{
						telecommandeAfficheurCentral.printPression(event);
						}
					catch (RemoteException e)
						{
						System.err.println("RemoteException: cannot send pression update to the PCCentral.");
						}
					}
			});

		// Modify MeteoServiceOptions
		Thread threadSimulationChangementDt = new Thread(new Runnable()
			{

				@Override
				public void run()
					{
					double x = 0;
					double dx = Math.PI / 10;

					while(true)
						{
						long dt = 1000 + (long)(5000 * Math.abs(Math.cos(x))); //ms

						System.out.println("modification dt temperature = " + dt);

						meteoService.getMeteoServiceOptions().setTemperatureDT(dt);

						//	System.out.println(meteoService.getMeteoServiceOptions());

						attendre(3000); // disons
						x += dx;
						}
					}
			});

		// Update GUI MeteoServiceOptions
		Thread threadPoolingOptions = new Thread(new Runnable()
			{

				@Override
				public void run()
					{

					while(true)
						{
						MeteoServiceOptions option = meteoService.getMeteoServiceOptions();
						try
							{
							telecommandeAfficheurCentral.updateMeteoServiceOptions(option);
							}
						catch (Exception e)
							{
							System.err.println("RemoteException: cannot send option update to the PCCentral.");
							}

						//System.out.println(option);
						attendre(1000); //disons
						}
					}
			});

		threadSimulationChangementDt.start();
		threadPoolingOptions.start(); // update gui
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	private static void attendre(long delay)
		{
		try
			{
			Thread.sleep(delay);
			}
		catch (InterruptedException e)
			{
			e.printStackTrace();
			}
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Inputs
	private MeteoServiceOptions meteoServiceOptions;
	private String portCom;
	private AffichageOptions affichageOptions;
	private RmiURL rmiURLafficheurManager;

	// Tools
	private MeteoService_I meteoService;
	private AfficheurService_I afficheurService;
	private RemoteAfficheurCreator_I remoteAfficheurCreator;
	private MeteoServiceWrapper meteoServiceWrapper;
	private AfficheurServiceWrapper afficheurServiceWrapper;
	private AfficheurServiceWrapper_I remoteAfficheurCentral;

	/*------------------------------*\
	|*			  Static			*|
	\*------------------------------*/

	private static final boolean DEBUG_MODE = true;
	private final static boolean USE_SIMULATOR = true;

	/*------------------------------*\
	|*			  OTHERS			*|
	\*------------------------------*/

	// Tools final
	private static final String PREFIXE_METEO = "METEO_SERVICE";
	public static final String RMI_ID_METEO = PREFIXE_METEO;
	//public static final RmiURL RMI_METEO_SERVICE = new RmiURL(PREFIXE_METEO);
	public static final RmiURL RMI_METEO_SERVICE = new RmiURL(IdTools.createID(PREFIXE_METEO));

	}

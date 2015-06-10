
package ch.hearc.meteo.imp.use.remote.pclocal;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import ch.hearc.meteo.imp.reseau.RemoteAfficheurCreator;
import ch.hearc.meteo.spec.afficheur.AffichageOptions;
import ch.hearc.meteo.spec.com.meteo.MeteoServiceOptions;

import com.bilat.tools.reseau.rmi.RmiURL;

/**
 * Meteo station connected PC local
 * @author horia.mut
 *
 */
public class UsePCLocal
	{

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	public static void main(String[] args)
		{
		//saveProperties();
		readProperties();
		main();
		//saveProperties();
		}

	public static void main()
		{
		try
			{

			// URL of the UI manager the Local PC needs to connect to.
			RmiURL rmiURLAfficheurManager = new RmiURL(RemoteAfficheurCreator.RMI_ID, ip);
			//RmiURL rmiURLAfficheurManager = new RmiURL(RemoteAfficheurCreator.RMI_ID, ip, RmiTools.PORT_RMI_DEFAUT);

			int n = 2;
			int altitudeDT = 750;
			int pressionDT = 900;
			int temperatureDT = 1500;

			// Create a Meteo Service Options with custom delays.
			MeteoServiceOptions meteoServiceOptions = new MeteoServiceOptions(altitudeDT, pressionDT, temperatureDT);
			// Create new show options.
			AffichageOptions affichageOptions = new AffichageOptions(n, name + ";" + latitude + ";" + longitude);

			// Create the local PC instance and run it.
			PCLocal pc = new PCLocal(meteoServiceOptions, PORT_COM, affichageOptions, rmiURLAfficheurManager);
			pc.run();
			}
		catch (Exception e)
			{
			e.printStackTrace();
			}
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	/*------------------------------*\
	|*			  Static			*|
	\*------------------------------*/

	private static void readProperties()
		{
		try
			{
			FileInputStream fis = new FileInputStream(FILE_PROPERTIES);
			BufferedInputStream bis = new BufferedInputStream(fis);
			Properties propertie = new Properties();
			propertie.load(bis);

			stringIP = propertie.getProperty(ADRESSE_IP);
			ip = InetAddress.getByName(stringIP);

			name = propertie.getProperty(LOCATION_NAME);
			latitude = propertie.getProperty(LATITUDE);
			longitude = propertie.getProperty(LONGITUDE);

			bis.close();
			fis.close();
			}
		catch (FileNotFoundException e)
			{
			System.out.println("Could not find properites file " + e.getMessage());

			NO_CONFIGURATION_FILE = true;

			if (DEBUG_MODE)
				{
				System.err.println("Could not find properites file " + e.getMessage());
				e.printStackTrace();
				}
			}
		catch (UnknownHostException e)
			{
			System.out.println("Could not resolve host name./n Details:" + e.getMessage());
			if (DEBUG_MODE)
				{
				System.err.println("Could not resolve host name./n Details:" + e.getMessage());
				e.printStackTrace();
				}
			}
		catch (IOException e)
			{
			System.out.println("Could not open file " + e.getMessage());
			if (DEBUG_MODE)
				{
				System.err.println("Could not open file " + e.getMessage());
				e.printStackTrace();
				}
			}
		}

	private static void saveProperties()
		{
		try
			{
			FileOutputStream fos = new FileOutputStream(FILE_PROPERTIES);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			Properties propertie = new Properties();

			propertie.setProperty(ADRESSE_IP, "127.0.0.1");
			propertie.setProperty(LOCATION_NAME, "Neuch");
			propertie.setProperty(LATITUDE, "6.45");
			propertie.setProperty(LONGITUDE, "45.666");
			propertie.store(bos, STORE_NAME);

			bos.close();
			fos.close();
			}
		catch (FileNotFoundException e)
			{
			System.out.println("Could not find properites file " + e.getMessage());

			if (DEBUG_MODE)
				{
				System.err.println("Could not find properites file " + e.getMessage());
				e.printStackTrace();
				}
			}
		catch (IOException e)
			{
			System.out.println("Could not open file " + e.getMessage());
			if (DEBUG_MODE)
				{
				System.err.println("Could not open file " + e.getMessage());
				e.printStackTrace();
				}
			}
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	/*------------------------------*\
	|*			  Static			*|
	\*------------------------------*/

	// Inputs/Outputs & Tools
	private static String stringIP;
	private static InetAddress ip;
	private static String name;
	private static String latitude;
	private static String longitude;

	// Tools
	private static final String STORE_NAME = "Weather Station - Configuration File";
	private static final String ADRESSE_IP = "ADRESSE_IP";
	private static final String LOCATION_NAME = "LOCATION_NAME";
	private static final String LATITUDE = "LATITUDE";
	private static final String LONGITUDE = "LONGITUDE";
	private static final String FILE_PROPERTIES = "settings.ini";

	private static final String PORT_COM = "COM1";

	private static final boolean DEBUG_MODE = true;
	@SuppressWarnings("unused")
	private static boolean NO_CONFIGURATION_FILE = false;

	}

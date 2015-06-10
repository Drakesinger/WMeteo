
package ch.hearc.meteo.imp.use.remote.pccentral;

import java.rmi.RemoteException;

import ch.hearc.meteo.imp.reseau.RemoteAfficheurCreator;
import ch.hearc.meteo.imp.use.remote.PC_I;
import ch.hearc.meteo.spec.afficheur.AffichageOptions;

public class PCCentral implements PC_I
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public PCCentral(AffichageOptions affichageOptions)
		{
		this.affichageOptions = affichageOptions;
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	@Override
	public void run()
		{
		server();
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	private void server()
		{
		try
			{
			// Startup the GUI creator.
			RemoteAfficheurCreator.getInstance();
			}
		catch (RemoteException e)
			{
			if (DEBUG_MODE)
				{
				System.err.println("Remote exception " + e);
				}
			e.printStackTrace();
			}
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Inputs
	@SuppressWarnings("unused")
	private AffichageOptions affichageOptions;

	/*------------------------------*\
	|*			  Static			*|
	\*------------------------------*/

	private static final boolean DEBUG_MODE = true;

	}

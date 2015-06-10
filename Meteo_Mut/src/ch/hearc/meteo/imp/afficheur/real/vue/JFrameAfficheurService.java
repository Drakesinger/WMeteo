
package ch.hearc.meteo.imp.afficheur.real.vue;

import java.rmi.RemoteException;

import javax.swing.JFrame;

import ch.hearc.meteo.imp.afficheur.real.moo.AfficheurServiceMOO;
import ch.hearc.meteo.imp.afficheur.real.vue.panels.JPanelRoot;
import ch.hearc.meteo.spec.com.meteo.MeteoServiceOptions;

/**
 * TODO SINGLETON
 * @author horia.mut
 *
 */
public class JFrameAfficheurService extends JFrame
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	private JFrameAfficheurService(boolean isCentralPC)
		{
		this.isCentralPC = isCentralPC;

		geometry();
		control();
		apparence();

		Thread thread = new Thread(new Runnable()
			{

				@Override
				public void run()
					{
					while(true)
						{
						try
							{
							// TODO used for updating?
							verifyStation();
							Thread.sleep(POOLING_DELAY);
							}
						catch (Exception e)
							{
							e.printStackTrace();
							}
						}
					}
			});

		thread.start();

		}

	public JFrameAfficheurService(boolean isCentralPC, AfficheurServiceMOO afficheurServiceMOO)
		{
		this.isCentralPC = isCentralPC;
		this.afficheurServiceMOO = afficheurServiceMOO;

		geometry();
		control();
		apparence();

		Thread thread = new Thread(new Runnable()
			{

				@Override
				public void run()
					{
					while(true)
						{
						try
							{
							// TODO used for updating?
							verifyStation();
							Thread.sleep(POOLING_DELAY);
							}
						catch (Exception e)
							{
							e.printStackTrace();
							}
						}
					}
			});

		thread.start();

		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	/**
	 * Singleton version
	 * @param isCentral
	 * @return
	 */
	public synchronized static JFrameAfficheurService getInstance(boolean isCentral)
		{
		if (instance == null)
			{
			instance = new JFrameAfficheurService(isCentral);
			}
		return instance;
		}

	/**
	 * TODO use the result in a way
	 */
	public synchronized void verifyStation()
		{
		try
			{
			afficheurServiceMOO.getMeteoServiceRemote().isConnect();
			}
		catch (RemoteException e)
			{
			if (DEBUG_MODE)
				{
				e.printStackTrace();
				}
			}

		//		Iterator<JPanelStation> iterator = jPanelStations.iterator();
		//		while(iterator.hasNext())
		//			{
		//			JPanelStation jPanelStation = iterator.next();
		//			if (!jPanelStation.isConnected())
		//				{
		//				jPanelSummary.removeAfficheurServiceMOO(jPanelStation.getAfficheurServiceMOO());
		//				tabbedPane.remove(jPanelStation);
		//				iterator.remove();
		//				}
		//			}
		}

	public void refresh()
		{
		panelRoot.update();
		}

	public void updateMeteoServiceOptions(MeteoServiceOptions meteoServiceOptions)
		{
		panelRoot.updateMeteoServiceOptions(meteoServiceOptions);
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	private void geometry()
		{
		panelRoot = new JPanelRoot(afficheurServiceMOO);
		add(panelRoot);
		}

	private void control()
		{
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		}

	private void apparence()
		{
		setTitle(afficheurServiceMOO.getTitre());

		setSize(500, 550);
		setVisible(true);
		}

	/*------------------------------*\
	|*				Set				*|
	\*------------------------------*/

	public synchronized void setAfficheurServiceMOO(AfficheurServiceMOO afficheurServiceMOO)
		{
		this.afficheurServiceMOO = afficheurServiceMOO;
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Inputs
	private AfficheurServiceMOO afficheurServiceMOO;
	private boolean isCentralPC;

	// Tools
	private JPanelRoot panelRoot;

	/*------------------------------*\
	|*			  Static			*|
	\*------------------------------*/

	// Outputs
	private static JFrameAfficheurService instance;

	// Tools
	public static final int POOLING_DELAY = 1000;
	public static final boolean DEBUG_MODE = true;
	}

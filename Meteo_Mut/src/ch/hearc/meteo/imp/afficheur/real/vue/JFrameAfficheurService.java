
package ch.hearc.meteo.imp.afficheur.real.vue;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import ch.hearc.meteo.imp.afficheur.real.moo.AfficheurServiceMOO;
import ch.hearc.meteo.imp.afficheur.real.vue.panels.JPanelRoot;
import ch.hearc.meteo.spec.com.meteo.MeteoServiceOptions;
import ch.hearc.meteo.spec.com.meteo.exception.MeteoServiceException;

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
		this.addWindowListener(new WindowAdapter()
			{

				@Override
				public void windowClosing(WindowEvent e)
					{
					// Try to disconnect from the meteoService
					try
						{
						JFrameAfficheurService.this.afficheurServiceMOO.getMeteoServiceRemote().disconnect();
						JFrameAfficheurService.this.isDisconnected = true;
						}
					catch (RemoteException e1)
						{
						System.err.println("Could not call the meteoServiceRemote.");
						e1.printStackTrace();
						JFrameAfficheurService.this.isDisconnected = false;
						}
					catch (MeteoServiceException e2)
						{
						System.err.println("Could not disconnect.");
						e2.printStackTrace();
						JFrameAfficheurService.this.isDisconnected = false;
						}

					if (JFrameAfficheurService.this.isDisconnected)
						{
						System.exit(0); // 0 normal, -1 anormal
						}
					else
						{
						JOptionPane.showMessageDialog(JFrameAfficheurService.this, "Could not disconnect, please try again.");
						}
					}

			});
		}

	private void apparence()
		{
		setTitle(afficheurServiceMOO.getTitre());

		setSize(770, 645);
		setResizable(false);

		setLocationRelativeTo(null);
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
	@SuppressWarnings("unused")
	private boolean isCentralPC;

	// Tools
	private JPanelRoot panelRoot;
	private boolean isDisconnected = false;

	/*------------------------------*\
	|*			  Static			*|
	\*------------------------------*/

	// Outputs
	private static JFrameAfficheurService instance;

	// Tools
	public static final int POOLING_DELAY = 1000;
	public static final boolean DEBUG_MODE = true;
	}

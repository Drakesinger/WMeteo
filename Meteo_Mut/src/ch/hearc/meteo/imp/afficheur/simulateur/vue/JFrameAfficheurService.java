
package ch.hearc.meteo.imp.afficheur.simulateur.vue;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import ch.hearc.meteo.imp.afficheur.simulateur.moo.AfficheurServiceMOO;
import ch.hearc.meteo.spec.com.meteo.MeteoServiceOptions;
import ch.hearc.meteo.spec.com.meteo.exception.MeteoServiceException;

public class JFrameAfficheurService extends JFrame
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JFrameAfficheurService(AfficheurServiceMOO afficheurServiceMOO)
		{
		this.afficheurServiceMOO = afficheurServiceMOO;

		geometry();
		control();
		apparence();
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

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
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
		}

	private void apparence()
		{
		setTitle(afficheurServiceMOO.getTitre());

		setSize(1500, 900);
		//		setResizable(false);

		setLocationRelativeTo(null);
		setVisible(true);
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Inputs
	private AfficheurServiceMOO afficheurServiceMOO;

	// Tools
	private JPanelRoot panelRoot;
	private boolean isDisconnected = false;
	}


package ch.hearc.meteo.imp.afficheur.simulateur.vue;

import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import ch.hearc.meteo.imp.afficheur.simulateur.moo.AfficheurServiceMOO;

public class JPanelTabbedData extends JPanel
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JPanelTabbedData(AfficheurServiceMOO afficheurServiceMOO)
		{
		// Inputs
		this.afficheurServiceMOO = afficheurServiceMOO;

		// Tools
		this.pannelPression = new JPanelEvent(afficheurServiceMOO.getStatPression(), afficheurServiceMOO.getListPression(), "Pression");
		this.pannelAltitude = new JPanelEvent(afficheurServiceMOO.getStatAltitude(), afficheurServiceMOO.getListAltitude(), "Altitude");
		this.pannelTemperature = new JPanelEvent(afficheurServiceMOO.getStatTemperature(), afficheurServiceMOO.getListTemperature(), "Temperature");

		geometry();
		control();
		appearance();
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	public void update()
		{
		pannelPression.update();
		pannelAltitude.update();
		pannelTemperature.update();
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

	private void geometry()
		{
		// JComponent : Instanciation
		tabbedPane = new JTabbedPane(SwingConstants.TOP);

		buildTab("Pression", pannelPression,0);
		buildTab("Altitude", pannelAltitude,1);
		buildTab("Temperature", pannelTemperature,2);

			// Layout : Specification
			{
			FlowLayout flowlayout = new FlowLayout(FlowLayout.CENTER);
			setLayout(flowlayout);

			// flowlayout.setHgap(20);
			// flowlayout.setVgap(20);
			}

		// JComponent : add
		add(tabbedPane);
		}

	private void buildTab(String title, JPanelEvent pannelEvent, int index)
		{
		//Box box = Box.createVerticalBox();

		//box.add(Box.createVerticalStrut(15));
		//box.add(pannelEvent);
		//box.add(Box.createVerticalStrut(15));
		tabbedPane.insertTab(title, null, pannelEvent, null, index);
		//tabbedPane.addTab(title, null, pannelEvent, null);
		}

	private void control()
		{
		// rien
		}

	private void appearance()
		{
		// rien
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Input
	@SuppressWarnings("unused")
	private AfficheurServiceMOO afficheurServiceMOO;

	// Tools
	private JPanelEvent pannelPression;
	private JPanelEvent pannelAltitude;
	private JPanelEvent pannelTemperature;

	private JTabbedPane tabbedPane;
	}


package ch.hearc.meteo.imp.afficheur.simulateur.vue.panels;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import ch.hearc.meteo.imp.afficheur.simulateur.moo.AfficheurServiceMOO;
import ch.hearc.meteo.imp.afficheur.tools.ImageStore;

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

		buildTab("Pressure", pannelPression, 0, ImageStore.pressure);
		buildTab("Altitude", pannelAltitude, 1, ImageStore.altitude);
		buildTab("Temperature", pannelTemperature, 2, ImageStore.thermometer);

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

	private void buildTab(String title, JPanelEvent pannelEvent, int index, ImageIcon imageIcon)
		{
		int h = imageIcon.getIconHeight();

		Image iconScaled = imageIcon.getImage().getScaledInstance(h / 3, h / 3, Image.SCALE_FAST);
		JLabel label = new JLabel(new ImageIcon(iconScaled));

		tabbedPane.insertTab(title, null, pannelEvent, title, index);
		tabbedPane.setTabComponentAt(index, label);

		}

	private void control()
		{
		// rien
		}

	private void appearance()
		{
		tabbedPane.setPreferredSize(new Dimension(720, 500));
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

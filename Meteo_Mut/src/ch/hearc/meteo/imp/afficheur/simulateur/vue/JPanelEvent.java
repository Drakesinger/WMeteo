
package ch.hearc.meteo.imp.afficheur.simulateur.vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import ch.hearc.meteo.imp.afficheur.simulateur.moo.Stat;
import ch.hearc.meteo.imp.afficheur.simulateur.vue.atome.JPanelStat;
import ch.hearc.meteo.spec.com.meteo.listener.event.MeteoEvent;

public class JPanelEvent extends JPanel
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JPanelEvent(Stat stat, List<MeteoEvent> listMeteoEvent, String titre)
		{
		this.stat = stat;
		this.listMeteoEvent = listMeteoEvent;
		this.titre = titre;

		// Chart elements
		initializeChartElements();

		geometry();
		control();
		apparence();
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	/**
	 * Method to refresh components with new data
	 */
	public void update()
		{
		panelStat.update();
		updateChartData();
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	private void geometry()
		{
		panelStat = new JPanelStat(stat);
		chartPanel = buildChart();

		// Check dimensions to actually see the Chart!
		panelStat.setMaximumSize(new Dimension(180, 100));
		chartPanel.setPreferredSize(new Dimension(540, 400));

		Box boxH = Box.createHorizontalBox();
		boxH.add(Box.createHorizontalStrut(15));
		boxH.add(panelStat);
		boxH.add(Box.createHorizontalStrut(15));
		// Add the jFreeChart Panel.
		boxH.add(chartPanel);
		boxH.add(Box.createHorizontalStrut(15));

		Box boxV = Box.createVerticalBox();
		boxV.add(Box.createVerticalStrut(15));
		boxV.add(boxH);
		boxV.add(Box.createVerticalStrut(15));

		setLayout(new BorderLayout());
		add(boxV,BorderLayout.CENTER);
		//add(chartPanel, BorderLayout.CENTER);
		setBorder(BorderFactory.createTitledBorder(titre));
		}

	private void apparence()
		{
		// rien
		}

	private void control()
		{
		// rien
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Chart						    *|
	\*------------------------------------------------------------------*/

	private void initializeChartElements()
		{
		this.data = new TimeSeries(titre);
		this.dataSet = new TimeSeriesCollection();

		dataSet.removeAllSeries();
		dataSet.addSeries(data);
		}

	private void updateChartData()
		{
		// Only update while the list is NOT used.
		synchronized (listMeteoEvent)
			{
			Iterator<MeteoEvent> meteoEventsIterator = listMeteoEvent.iterator();

			int index = 0;
			TimeSeries timeSerie = dataSet.getSeries(index);

			while(meteoEventsIterator.hasNext())
				{
				MeteoEvent meteoEvent = meteoEventsIterator.next();
				timeSerie.addOrUpdate(new Millisecond(new Date(meteoEvent.getTime())), meteoEvent.getValue());
				}
			}
		}

	private ChartPanel buildChart()
		{
		final JFreeChart chart = createChart(dataSet);
		ChartPanel chartPanel = new ChartPanel(chart)
			{

				@Override
				public void paintComponent(Graphics g)
					{
					super.paintComponent(g);
					Dimension size = this.getSize();
					int w = (int)Math.rint(size.width);
					int h = (int)Math.rint(size.height);
					chart.draw((Graphics2D)g, new Rectangle2D.Double(0, 0, w, h));
					}
			};
		chartPanel.setMouseWheelEnabled(true);

		chartPanel.setDomainZoomable(true);
		chartPanel.setRangeZoomable(true);

		return chartPanel;
		}

	private JFreeChart createChart(TimeSeriesCollection dataset)
		{
		JFreeChart chart = ChartFactory.createTimeSeriesChart(titre, "temps", "Values", dataset, true, false, false);

		XYPlot plot = (XYPlot)chart.getPlot();

		Color backgroundColor = Color.WHITE;
		Color plotColor = Color.BLUE;
		Color plotBackground = Color.WHITE;

		chart.setBackgroundPaint(backgroundColor);

		chart.getTitle().setPaint(plotColor);
		plot.setBackgroundPaint(plotBackground);
		plot.setDomainGridlinePaint(Color.BLACK);
		plot.setRangeGridlinePaint(Color.BLACK);

		plot.setDomainCrosshairVisible(true);

		return chart;
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Inputs
	private Stat stat;

	private List<MeteoEvent> listMeteoEvent;
	private String titre;

	// Tools
	private JPanelStat panelStat;
	private ChartPanel chartPanel;

	// Chart elements
	private TimeSeriesCollection dataSet;
	private TimeSeries data;

	}

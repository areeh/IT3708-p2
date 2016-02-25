package display;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;


public class Plot extends JPanel {

/**
* A demonstration application showing an XY series containing a null value.
*
* @param title  the frame title.
*/
public Plot(List<Double> plotvals, String title, String yLabel) {
   this.setLayout(new GridLayout());
   plotDoubles(plotvals, title, yLabel);

}

public void plotDoubles(List<Double> plotvals, String title, String yLabel) {
	int iter = 0;
	final XYSeries series = new XYSeries("");
	
	
	for (double val : plotvals) {
		series.add(iter, val);
		iter ++;
	}
	
	final XYSeriesCollection data = new XYSeriesCollection(series);
	final JFreeChart chart = ChartFactory.createXYLineChart(
			title,
			"Generation",
			yLabel,
			data,
			PlotOrientation.VERTICAL,
			true,
			true,
			false
	);
	
	
	final ChartPanel chartPanel = new ChartPanel(chart);
	chartPanel.setPreferredSize(new java.awt.Dimension(800, 470));
	
	this.add(chartPanel);
}

//****************************************************************************
//* JFREECHART DEVELOPER GUIDE                                               *
//* The JFreeChart Developer Guide, written by David Gilbert, is available   *
//* to purchase from Object Refinery Limited:                                *
//*                                                                          *
//* http://www.object-refinery.com/jfreechart/guide.html                     *
//*                                                                          *
//* Sales are used to provide funding for the JFreeChart project - please    * 
//* support us so that we can continue developing free software.             *
//****************************************************************************

/**
* Starting point for the demonstration application.
*
* @param args  ignored.
*/
public static void main(final String[] args) {
}

}
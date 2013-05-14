/*******************************************************************************
 * Copyright (c) 2002, 2013 Innoopract Informationssysteme GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Innoopract Informationssysteme GmbH - initial API and implementation
 *    EclipseSource - ongoing development
 ******************************************************************************/
package phirea.rap.rap.widgets.demo;

import java.util.Random;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import phirea.rap.widgets.nvd3.charts.NVD3Chart;
import phirea.rap.widgets.nvd3.charts.NVD3Chart.ChartType;


public class ChartsDemo extends AbstractEntryPoint {

  @Override
  protected void createContents( final Composite parent ) {
    getShell().setText( "CkEditor Demo" );
    parent.setLayout( new GridLayout( 3, false ) );
    
    //Charts
    
    final NVD3Chart lineChart = new NVD3Chart( parent, SWT.NONE, "lineChart" );
    lineChart.setLayoutData( new GridData() );
    lineChart.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
    
    
    JsonArray jsonArray = new JsonArray();
    JsonObject series1 = new JsonObject();
    series1.add("key", "Series 1");
    
    JsonArray jsonArrayValues = new JsonArray();
    JsonObject value = new JsonObject();
    value.add("x", 0.5f);
    value.add("y", 0.5f);
    jsonArrayValues.add(value);
    
    value = new JsonObject();
    value.add("x", 1.0f);
    value.add("y", 0.8f);
    jsonArrayValues.add(value);
    
    value = new JsonObject();
    value.add("x", 1.5f);
    value.add("y", 2.0f);
    jsonArrayValues.add(value);
    
    value = new JsonObject();
    value.add("x", 2.0f);
    value.add("y", 1.8f);
    jsonArrayValues.add(value);
    
    series1.add("values", jsonArrayValues);
    
    jsonArray.add(series1);
    
    
    JsonObject series2 = new JsonObject();
    series2.add("key", "Series 2");
    
    jsonArrayValues = new JsonArray();
    value = new JsonObject();
    value.add("x", 0.5f);
    value.add("y", -0.8f);
    jsonArrayValues.add(value);
    
    value = new JsonObject();
    value.add("x", 1.0f);
    value.add("y", 0.3f);
    jsonArrayValues.add(value);
    
    value = new JsonObject();
    value.add("x", 1.5f);
    value.add("y", 1.1f);
    jsonArrayValues.add(value);
    
    value = new JsonObject();
    value.add("x", 2.0f);
    value.add("y", 3.0f);
    jsonArrayValues.add(value);
    
    series2.add("values", jsonArrayValues);
    series2.add("color", "#ff0023");
    
    jsonArray.add(series2);
    
	  
    lineChart
    	.setChartType(ChartType.LINE_WITH_FOCUS)
    	.setXAxisLabel("X Achse")
    		.setXAxisTickFormat(",r")
    	.setYAxisLabel("Y Achse")
    		.setYAxisTickFormat(".02f")
    	.setData(createLineTestData())
    	.updateChart();
    

    Label filler = new Label(parent, SWT.NONE);
    filler.setLayoutData( new GridData() );
    
    final NVD3Chart barChart = new NVD3Chart( parent, SWT.NONE, "barChart" );
    barChart.setLayoutData( new GridData() );
    barChart.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
    
	  
    barChart
    	.setChartType(ChartType.MULTIBAR)
    	.setXAxisLabel("X Achse")
    		.setXAxisTickFormat(",r")
    	.setYAxisLabel("Y Achse")
    		.setYAxisTickFormat(".02f")
    	.setData(jsonArray)
    	.updateChart();

    
    
    final NVD3Chart pieChart = new NVD3Chart( parent, SWT.NONE, "pieChart" );
    pieChart.setLayoutData( new GridData() );
    pieChart.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
    
    
    JsonArray pieChartDataArray = new JsonArray();
    
    JsonObject pieChartData = new JsonObject(); 
    pieChartData.add("key", "Cumulative Return");
    
    jsonArrayValues = new JsonArray();
    value = new JsonObject();
    value.add("label", "Label 1");
    value.add("value", 0.8f);
    jsonArrayValues.add(value);
    
    value = new JsonObject();
    value.add("label", "Label 2");
    value.add("value", 1.8f);
    jsonArrayValues.add(value);
    
    value = new JsonObject();
    value.add("label", "Label 3");
    value.add("value", 4.1f);
    jsonArrayValues.add(value);
    
    value = new JsonObject();
    value.add("label", "Label 4");
    value.add("value", 2.5f);
    jsonArrayValues.add(value);
    
    pieChartData.add("values", jsonArrayValues);
	
    pieChartDataArray.add(pieChartData);
    JsonArray nocheinArray = new JsonArray();
    nocheinArray.add(pieChartDataArray);
    pieChart
    	.setChartType(ChartType.PIE)
    	.setData(pieChartDataArray)
    	.updateChart();
    
    
    filler = new Label(parent, SWT.NONE);
    filler.setLayoutData( new GridData() );
    
    
    final NVD3Chart donutChart = new NVD3Chart( parent, SWT.NONE, "donutChart" );
    donutChart.setLayoutData( new GridData() );
    donutChart.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
    
    donutChart
    	.setChartType(ChartType.DONUT)
    	.setData(pieChartDataArray)
    	.updateChart();
    
  }
  
  protected JsonArray createLineTestData() {
	  
	  JsonArray jsonArray = new JsonArray();
	   
	  Random random = new Random();
	  
	  for(int i=0; i<3; i++) {
	  
		  JsonObject series = new JsonObject();
		  series.add("key", "Series " + i);
		  
		  JsonArray jsonArrayValues = new JsonArray();
		  
		  float[] fs = makeRandomArray(365, random);
		  
		  for(int j=0;j<fs.length; j++) {
			  JsonObject value = new JsonObject();
			  value.add("x", j);
			  
			  value.add("y", fs[j] );
			  jsonArrayValues.add(value);		  
		  }
		  series.add("values", jsonArrayValues);
		  jsonArray.add(series);
	  }
	  
	  return jsonArray;  
  }
  

	  protected float[] makeRandomArray(int n, Random rnd) {
	    float[] x = new float[n];

	    // add a handful of random bumps
	    for (int i=0; i<5; i++) {
	      addRandomBump(x, rnd);
	    }

	    return x;
	  }

	  protected void addRandomBump(float[] x, Random rnd) {
	    float height  = 1 / rnd.nextFloat();
	    float cx      = (float)(2 * rnd.nextFloat() - 0.5);
	    float r       = rnd.nextFloat() / 10;

	    for (int i = 0; i < x.length; i++) {
	      float a = (i / (float)x.length - cx) / r;
	      x[i] += height * Math.exp(-a * a);
	    }
	  }

}

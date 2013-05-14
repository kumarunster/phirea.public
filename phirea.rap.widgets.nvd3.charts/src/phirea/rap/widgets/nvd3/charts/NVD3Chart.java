package phirea.rap.widgets.nvd3.charts;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.JavaScriptLoader;
import org.eclipse.rap.rwt.lifecycle.WidgetUtil;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.OperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.service.ResourceManager;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;


public class NVD3Chart extends Composite {
	
	public enum ChartType {
		LINE,
		LINE_WITH_FOCUS,
		MULTIBAR,
		PIE,
		DONUT;
	}

	  private static final String RESOURCES_PATH = "nvd3js/resources/";
	  private static final String REGISTER_PATH = "NVD3Chart/";

	  private static final String[] RESOURCE_FILES = {
	    "d3.v2.js",
	    "nv.d3.css",
	    "nv.d3.js",
	    "chart_widget.js"
	  };
	  private static final String REMOTE_TYPE = "phirea.NVD3Chart";

	  private final RemoteObject remoteObject;
	  
//	  private ChartOptions options;
	  
	  private JsonObject options = new JsonObject();

	  private final OperationHandler operationHandler = new AbstractOperationHandler() {
	    @Override
	    public void handleSet( JsonObject properties ) {
	      JsonValue textValue = properties.get( "text" );
	      if( textValue != null ) {
//	        text = textValue.asString();
	      }
	    }
	  };

	  public NVD3Chart( Composite parent, int style, String chartId ) {
	    super( parent, style );
	    registerResources();
	    loadJavaScript();
	    Connection connection = RWT.getUISession().getConnection();
	    remoteObject = connection.createRemoteObject( REMOTE_TYPE );
	    remoteObject.setHandler( operationHandler );
	    remoteObject.set( "parent", WidgetUtil.getId( this ) );
	    remoteObject.set( "chartId", chartId);
	  }

	  private void registerResources() {
	    ResourceManager resourceManager = RWT.getResourceManager();
	    boolean isRegistered = resourceManager.isRegistered( REGISTER_PATH + RESOURCE_FILES[ 0 ] );
	    if( !isRegistered ) {
	      try {
	        for( String fileName : RESOURCE_FILES ) {
	          register( resourceManager, fileName );
	        }
	      } catch( IOException ioe ) {
	        throw new IllegalArgumentException( "Failed to load resources", ioe );
	      }
	    }
	  }

	  private void loadJavaScript() {
	    JavaScriptLoader jsLoader = RWT.getClient().getService( JavaScriptLoader.class );
	    ResourceManager resourceManager = RWT.getResourceManager();
	    jsLoader.require( resourceManager.getLocation( REGISTER_PATH + "d3.v2.js" ) );
	    jsLoader.require( resourceManager.getLocation( REGISTER_PATH + "nv.d3.js" ) );
	    jsLoader.require( resourceManager.getLocation( REGISTER_PATH + "chart_widget.js" ) );
	    
	  }
	 

	  private void register( ResourceManager resourceManager, String fileName ) throws IOException {
	    ClassLoader classLoader = NVD3Chart.class.getClassLoader();
	    InputStream inputStream = classLoader.getResourceAsStream( RESOURCES_PATH + fileName );
	    try {
	      resourceManager.register( REGISTER_PATH + fileName, inputStream );
	    } finally {
	      inputStream.close();
	    }
	  }

	  ////////////////////
	  // overwrite methods

	  @Override
	  public void setLayout( Layout layout ) {
	    throw new UnsupportedOperationException( "Cannot change internal layout of NVD3Chart" );
	  }

	  @Override
	  public void setFont( Font font ) {
	    super.setFont( font );
	    remoteObject.set( "font", getCssFont() );
	  }

	  @Override
	  public void dispose() {
	    remoteObject.destroy();
	    super.dispose();
	  }

	  //////
	  // API

//	  public void setText( String text ) {
//	    checkWidget();
//	    if( text == null ) {
//	      SWT.error( SWT.ERROR_NULL_ARGUMENT );
//	    }
//	    this.text = text;
//	    remoteObject.set( "text", text );
//	  }

//	  public String getText() {
//	    checkWidget();
//	    return text;
//	  }
	  
	  public NVD3Chart setChartType(ChartType chartType) {
		  this.options.add("chartType", chartType.name());  
		  return this;
		}
	  
	  public NVD3Chart setXAxisLabel(String label) {
		  this.options.add("xAxisLabel", label);  
		  return this;
	  }
	  
	  public NVD3Chart setYAxisLabel(String label) {
		  this.options.add("yAxisLabel", label);
		  //xAxisLabel = label;  
		  return this;
	  }
	  
	  
	  public NVD3Chart setXAxisTickFormat(String format) {
		  this.options.add("xAxisTickFormat", format);
		  //xAxisLabel = label;  
		  return this;
	  }
	  
	  
	  public NVD3Chart setYAxisTickFormat(String format) {
		  this.options.add("yAxisTickFormat", format);
		  //xAxisLabel = label;  
		  return this;
	  }
	  
	  public NVD3Chart setData(JsonArray data) {
		  this.options.add("data", data);
		  //xAxisLabel = label;  
		  return this;
	  }
	  
	  
	  public void updateChart() {
		if(this.options != null) {
			remoteObject.set("options", this.options);
//			remoteObject.call("onRender", null);
		}
	  }	  


	  private String getCssFont() {
	    StringBuilder result = new StringBuilder();
	    if( getFont() != null ) {
	      FontData data = getFont().getFontData()[ 0 ];
	      result.append( data.getHeight() );
	      result.append( "px " );
	      result.append( data.getName() );
	    }
	    return result.toString();
	  }

	
	}

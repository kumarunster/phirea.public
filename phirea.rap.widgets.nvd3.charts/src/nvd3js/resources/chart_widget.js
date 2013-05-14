

/*******************************************************************************
 * Simple test data generator
 */

function sinAndCos() {
	var sin = [], cos = [];
	for ( var i = 0; i < 100; i++) {
		sin.push({
			x : i,
			y : Math.sin(i / 10)
		});
		cos.push({
			x : i,
			y : .5 * Math.cos(i / 10)
		});
	}
	return [ {
		values : sin,
		key : 'Sine Wave',
		color : '#ff7f0e'
	}, {
		values : cos,
		key : 'Cosine Wave',
		color : '#2ca02c'
	} ];
}



function exampleData() {
    return [
    {
    key:"Cumulative Return",
    values:[
	{
		"label":"Label 1",
		"value":0.8
	},
	{
		"label":"Label 2",
		"value":1.8
	},
	{
		"label":"Label 3",
		"value":4.1
	},
	{
		"label":"Label 4",
		"value":2.5
	}
	]
  }];
}


function loadjscssfile(filename, filetype, id){
	 if (filetype=="js"){ //if filename is a external JavaScript file
	  
	  var fileref= document.getElementById(id);
	  if(!fileref) {
		  fileref=document.createElement('script');
		  fileref.setAttribute("id", id);
		  fileref.setAttribute("type","text/javascript");
		  fileref.setAttribute("src", filename);
		  document.getElementsByTagName( "head" )[ 0 ].appendChild(fileref);
	  }
	 }
	 else if (filetype=="css"){ //if filename is an external CSS file
	  var fileref= document.getElementById(id);
	  if(!fileref) {
		  fileref=document.createElement("link");
		  fileref.setAttribute("id", id);
		  fileref.setAttribute("rel", "stylesheet");
		  fileref.setAttribute("type", "text/css");
		  fileref.setAttribute("href", filename);
	  }
	 }
	 if (typeof fileref!="undefined")
	  document.getElementsByTagName("head")[0].appendChild(fileref)
	}


var NVD3Chart_BASEPATH = "rwt-resources/nvd3js/";

(function(){
  'use strict';

  rap.registerTypeHandler( "phirea.NVD3Chart", {

    factory : function( properties ) {
      return new phirea.NVD3Chart( properties );
    },

    destructor : "destroy",

    properties : [ "chartId", "options" ]

  } );

  if( !window.phirea ) {
    window.phirea = {};
  }

  phirea.NVD3Chart = function( properties ) {
    bindAll( this, [ "layout", "onReady", "onSend", "onRender" ] );
    this.parent = rap.getObject( properties.parent );
    this.chartId = properties.chartId;
    
    loadjscssfile("rwt-resources/NVD3Chart/nv.d3.css", "css", "nvd3cssLink");
//    loadjscssfile("rwt-resources/NVD3Chart/d3.v2.js", "js", "d3jsLink");
//    loadjscssfile("rwt-resources/NVD3Chart/nv.d3.js", "js", "nvd3jsLink");
    
    
    this.element = document.createElement( "div" );
    this.element.id = this.chartId;
    
    this.svg = document.createElementNS ("http://www.w3.org/2000/svg", "svg");
    this.element.appendChild(this.svg);
    
    this.parent.append( this.element );
    this.parent.addListener( "Resize", this.layout );
    rap.on( "render", this.onRender );
    console.log("finish construktor, created element :" + this.svg);
  };

  phirea.NVD3Chart.prototype = {

    ready : false,

    onReady : function() {
      this.ready = true;
      this.layout();
    },

    onRender : function() {
      if( this.element.parentNode ) {
        rap.off( "render", this.onRender );
        console.log("onRender: ");
        if(this.options) {
        	console.log("onRender: " + this.options.chartType);
            
        nv.addGraph(function(svg, options) {
        	var chart = undefined;
        	
        	console.log("options chartType: " + options.chartType); 
        	
        	switch(options.chartType) {
        	case 'LINE':
        		chart = nv.models.lineChart();
        		
            	chart.xAxis.axisLabel(options.xAxisLabel).tickFormat(d3.format(options.xAxisTickFormat));
            	chart.yAxis.axisLabel(options.yAxisLabel).tickFormat(d3.format(options.yAxisTickFormat));

        		break;
        	case 'LINE_WITH_FOCUS':
        		chart = nv.models.lineWithFocusChart();
        		
            	chart.xAxis.axisLabel(options.xAxisLabel).tickFormat(d3.format(options.xAxisTickFormat));
            	chart.x2Axis.axisLabel(options.xAxisLabel).tickFormat(d3.format(options.xAxisTickFormat));
            	chart.yAxis.axisLabel(options.yAxisLabel).tickFormat(d3.format(options.yAxisTickFormat));
            	chart.y2Axis.axisLabel(options.yAxisLabel).tickFormat(d3.format(options.yAxisTickFormat));
            	
        		break;
        	case 'MULTIBAR':
        		chart = nv.models.multiBarChart();

        		chart.xAxis.axisLabel(options.xAxisLabel).tickFormat(d3.format(options.xAxisTickFormat));
            	chart.yAxis.axisLabel(options.yAxisLabel).tickFormat(d3.format(options.yAxisTickFormat));
            	
        		break;
        	case 'PIE':
        		console.log("create PIE ");
        		chart = nv.models.pieChart()
        		      .x(function(d) { return d.label })
        		      .y(function(d) { return d.value })
        		      .showLabels(true);
        		
        		break;
        	case 'DONUT':
        		console.log("create PIE ");
        		chart = nv.models.pieChart()
        		      .x(function(d) { return d.label })
        		      .y(function(d) { return d.value })
        		      .showLabels(true)
        		      .donut(true);
        		
        		break;
        	}
        	
        	console.log("chart ist: " + options.chartType);
        	
        	d3.select(svg)
        	  	.datum(options.data)
        	  .transition().duration(500)
        	  	.call(chart);
        	
        	nv.utils.windowResize(function() {
        		d3.select(svg).call(chart)
        	});
        	return chart;
        }(this.svg, this.options));
        
        }
        
        rap.on( "send", this.onSend );
      }
    },

    onSend : function() {
//      if( this.editor.checkDirty() ) {
//        rap.getRemoteObject( this ).set( "text", this.editor.getData() );
//        this.editor.resetDirty();
//      }
    	
    	rap.getRemoteObject( this ).set( "options", this.options );
    },
    
    setOptions : function (options) {
    	this.options = options;
    	console.log("options set: " + options);
//    	this.onRender();
    },    
   

    setFont : function( font ) {
      if( this.ready ) {
        async( this, function() { // Needed by IE for some reason
//          this.editor.document.getBody().setStyle( "font", font );
        } );
      } else {
        this._font = font;
      }
    },
    
    setChartId : function(chartId) {
    	this.chartId = chartId;
    },

    destroy : function() {
      rap.off( "send", this.onSend );
//      this.editor.destroy();
      this.element.parentNode.removeChild( this.element );
    },

    layout : function() {
      if( this.ready ) {
        var area = this.parent.getClientArea();
        this.element.style.left = area[ 0 ] + "px";
        this.element.style.top = area[ 1 ] + "px";
//        this.editor.resize( area[ 2 ], area[ 3 ] );
      }
    }

  };

  var bind = function( context, method ) {
    return function() {
      return method.apply( context, arguments );
    };
  };

  var bindAll = function( context, methodNames ) {
    for( var i = 0; i < methodNames.length; i++ ) {
      var method = context[ methodNames[ i ] ];
      context[ methodNames[ i ] ] = bind( context, method );
    }
  };

  var async = function( context, func ) {
    window.setTimeout( function(){
      func.apply( context );
    }, 0 );
  };

}());
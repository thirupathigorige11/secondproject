
<html xmlns:jsp="http://java.sun.com/JSP/Page">
	<head>
		
		<script src="https://cdnjs.cloudflare.com/ajax/libs/d3/3.5.5/d3.min.js" charset="utf-8"></script>
		<script src="http://labratrevenge.com/d3-tip/javascripts/d3.tip.v0.6.3.js"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/d3-legend/1.7.0/d3-legend.min.js"></script>
		<title>Sumadhura_IMS</title>
		<link rel="shortcut icon" type="image/x-icon" href="images/Enlivenshortcut.png">
<style>
.nav-md{
overflow-x:hidden;
}
.finalAmountDiv{
    color: #f0ad4e;
    font-size: 24px;
}
.form-group label{
    text-align: left
}
.pro-table tbody tr td,.pro-table tbody tr th{
	margin:2px 3px;
	width:100%;
	min-width:213px; 
}

    .chart-container .active {
 	 background: lightcoral;
	}
	    .stacked-bar {
        font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
        width: 960px;
        height: 500px;
        position: relative;
    }

    svg {
        width: 100%;
        height: 100%;
        position: center;
        padding-left: 20px;
    }
    text{
        font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
    }
    .toolTip {
        font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
        position: absolute;
        display: none;
        width: auto;
        height: auto;
        background: none repeat scroll 0 0 white;
        border: 0 none;
        border-radius: 8px 8px 8px 8px;
        box-shadow: -3px 3px 15px #888888;
        color: black;
        font: 12px sans-serif;
        padding: 5px;
        text-align: center;
    }
    .tooltipstyle{    
   
    line-height: 1;   
    padding: 12px;
    /* background: #c2c2c2; */
    color: #000;
    font-weight:bold;
    border-radius: 8px;
    font-family: 'Open Sans', Arial, Helvetica, sans-serif;
   
    }
    .legend {
        font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
        font-size: 60%;
    }
    text {
        font: 14px sans-serif;
            font-weight: bold;
    }
    .axis text {
        font: 14px sans-serif;
            font-weight: bold;
    }
    .axis path{
        fill: none;
        stroke: #000;
    }
    .axis line {
        fill: none;
        stroke: #000;
        shape-rendering: crispEdges;
    }
    .x.axis path { 
        display: none;
    }
    .ToolTipClass, .ToolTipClass-Itm{
     margin-left: 20px;
    }
    .sideMenuClick{
    color:#251f1fe0;
    font-size: 14px;
    font-family: calibri;
    margin-left: 2px;
    font-weight: bold;
    margin-left: 3px;
    /* display: block;	 */
    }
    .sideMenuClick_index{
    color: #211d1de0;
    font-size: 12px;
    word-break: normal;
    }
   	.bar:hover {
  fill: #afaaa9;
}
/* .d3-tip {
 
  font-weight: bold;
  padding: 12px;
  background:#a99f9f;
  color: black;
  border-radius: 2px;  height: 200px;
 
} */

.d3-tip {
  line-height: 1;
 /*  font-weight: bold; */
  padding: 12px;
  background: #c2c2c2;
  color: #353430;
  border-radius: 8px;
  font-family: 'Open Sans', Arial, Helvetica, sans-serif;
  z-index:999;
}
/* Creates a small triangle extender for the tooltip */
.d3-tip:after {
  box-sizing: border-box;
  display: inline;
  font-size: 8px;
  width: 100%;
  line-height: 1;
  color: #a99f9f;
  content: "\25BC";
  position: absolute;
  text-align: center;
   z-index:999;
}

/* Style northward tooltips differently */
.d3-tip.n:after {
  margin: -1px 0 0 0;
  top: 100%;
  left: 0;
  z-index:999;
}
.xaxis .tick{
    transform: translate(20,0)!important;
    position: relative;
}

    .stacked-sideBar{
    width: 100px;
    height: 354px;
    border: 1px solid;
    position: absolute;
    margin-left: 936px;
    margin-top: -440px;
    }	   
.chart-container{
	float:left;
	width: 946px;
	height: 760px;
	z-index:-1;
    background-color: #eaeaea;
    border: 1px solid;
}
.breadcrumb{
    background: #eaeaea;
}
.form-horizontal{
/* background: rgb(234, 234, 234); */

}
.loader {
	display: none; 
	margin-top:20%;
   /*  margin: 0 auto; */
    border: 4px solid #c5b3b3;;
    border-radius: 50%;
    border-top: 4px solid orange;
    width: 43px;
    height: 43px;
    z-index: 10;
    -webkit-animation: spin 2s linear infinite;
    animation: spin 2s linear infinite;
}
.loader1 {
	display: none; 
	/* margin-top:20%; */
    margin: 20% auto;
    border: 4px solid #c5b3b3;;
    border-radius: 50%;
    border-top: 4px solid orange;
    width: 43px;
    height: 43px;
    z-index: 10;
    -webkit-animation: spin 2s linear infinite;
    animation: spin 2s linear infinite;
        position: absolute;
    right: 0;
    margin-right: 5%;
    /* width: 50px; 
    display: block;*/
    overflow: hidden;
}
.hoverClass:hover {
    background-color: #d8d3d3e0;

    
}
.hoverClass:nth-child(odd) {
background:#ccc;
}
.indexClass{
width:2px !important;
border:1px solid;
word-break: break-word;'
color:red;

}
.data{
float:left;
width:100px;
border:1px solid;
z-index:999;
overflow:hidden;
display:none;
background-color: #eaeaea;
border: 1px solid;
height:760px;

}
/* .data:hover{
width:400px;
height:400px;
} */

 #d3name{
    text-align: center;
    font-size: 24px;
    font-family: sans-serif;
	
}
</style>
	</head>
	 <body>
			<!-- --------Page Content start here-----------		 -->			
			<form  class="form-horizontal" action="dashboard.spring"  method="post"    autocomplete="off">		
				<div class="container">
			          <div id="d3name">Product Details In INR.</div>
						 <div class="chart-container">
						      <div class="loader"></div>	
							  <p class="text-center hideClass">Generating Report...</p>
							   <svg class="bar-chart"></svg>
						 </div>
						 <div class="loader1"style="position:absolute;"></div>
						 <div class="data" title="click here to view."></div>
		 		</div>
		        <div class="mouseHover"></div>
			</form>
            <!-- /page content -->     
		<!-- jQuery -->
	    <script src="js/jquery.min.js"></script> 
		<script>
			$(document).ready(function() {$(".up_down").click(function() {
								$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
								$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
								});

							});
			
/* ******* Method for bar Charts************		 */
		
	  var url = "productstock.spring";
	  $('.loader').show();
		 $.ajax({
			  url : url,
			  //url : "${pageContext.request.contextPath}/getVendorDetails.spring",
			  type : "GET",
			 dataType : "json",
			 // contentType:"xml",
			  success : function(response) {
			  $('.loader').hide();
			  $('.hideClass').hide();
			  //console.log(response);
			 $('.chart-container').css("border", "1px solid"); 
				  var dataset =  response;

					var dataArray = [];
					jQuery.each(dataset.xml.ProductList.Products, function(productIndex, productItem){
						var dataObject = {};
						dataObject['key'] = productIndex;
						dataObject['displayX'] = productIndex + "(" + productItem +")";
						dataObject['value'] = parseFloat(productItem);
						dataArray.push(dataObject);
						
					});
				 // console.log(JSON.parse(dataset.xml));
				  var div = d3.select(".bar-chart").append("div").attr("class", "toolTip");
				  var margin = {top: 20, right: 20, bottom: 30, left: 40},
				    width = 951 - margin.left - margin.right,
				    height = 500 - margin.top - margin.bottom;
				var formatPercent = d3.format(".2s");
				var x = d3.scale.ordinal().rangeRoundBands([0, width], .1);
				var y = d3.scale.linear().range([height, 0]);
			
				var chilProdData = dataset.xml.ChildProductList.Products;
				
				var subProdData = dataset.xml.SubProductList.Products;
				
				     var divTooltip = d3.select("body")
									  .append("div")
									  .attr("class", "tooltipstyle")
									  .style("position", "absolute")
									  .style("height", "auto")
									  .style("width", "auto")
									  .style("background-color", "#c2c2c2")
									  .style("z-index", "100")
									  .style("visibility", "hidden")
									  /* .text("a simple tooltip") */;
 				var tip = d3.tip()
				  .attr('class', 'd3-tip')
				  .offset([-10, 0])
				  .html(function(data, d) {
					  debugger;
					 
				  	var subProddata = subProdData[data.key], html ='';				  
				  	  	jQuery.each(subProddata, function(index, item){
					  		var ht = " <span class='floatLeft' style='float:left;z-index:999;'>" + index + "</span><span class='floatRight' style='float:right; margin-left:15px;'>" + item + "</span> <span style='color:red'></span><br>";
					  		html += ht;
					  	});				  
				     return html; 
				  });
				 
				var xAxis = d3.svg.axis()
						    .scale(x)
						    .orient("bottom")
						    .tickFormat((d,i)=>{
						    	return dataArray[i].displayX;
						    });
				
				
				var yAxis = d3.svg.axis()
						    .scale(y)
						    .orient("left")
						    .tickFormat(formatPercent);
				var svg = d3.select(".bar-chart")
						    .attr("width", width + margin.left + margin.right)
						    .attr("height", height + margin.top + margin.bottom)
						  	.append("g")
						    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
				 svg.call(tip); 
					 x.domain(dataArray.map(function(d) {return d.key; }));
					  y.domain([0, d3.max(dataArray, function(d) { return d.value; })]);
					  
					  svg.append("g")
				      .attr("class", "x axis")
				      .attr("transform", "translate(0," + height + ")")
				      .call(xAxis)				      
				      .selectAll("text")
				   	  .attr("y", 0)
				      .attr("x", 9)
				      .attr("dy", ".35em")
				      .attr("transform", "rotate(90)")
				      .style("text-anchor", "start")
					  .style("font-size", "16px")
					  .style("font-weight", "bold")
					  .style("cursor", "pointer")
					  .on('mouseover',  function(d){	
						  debugger;
			                 var lef;
			                 divTooltip.style("visibility", "visible");
			            	var subProddata = subProdData[d], html1 ='';
						  	  	jQuery.each(subProddata, function(index, item){
							  		var ht = " <span class='floatLeft' style='float:left;z-index:999;'>" + index + "</span><span class='floatRight' style='float:right; margin-left:15px;'>" + item + "</span> <span style='color:red'></span><br>";
							  		html1 += ht;
							  	});	
						  	  divTooltip.html(html1);
						  	  	if((d3.event.pageX+15)>1100){
						  	  	var divlength=$(".tooltipstyle").width();
						  	  	left=d3.event.pageX-(divlength+40);
						  	    divTooltip.style("left",  left+ "px");		
				                divTooltip.style("top", (d3.event.pageY - 50) + "px");	
						  	  	}
						  	  	else{
						  	  	left=d3.event.pageX+15;
						  	    divTooltip.style("left",  left+ "px");		
				                divTooltip.style("top", (d3.event.pageY - 50) + "px");	
						  	  	}
			            })
			            .on('click', function(data,d, e) {				   		
					  $('.data').show();					 
						  $('.data').html( '');
						  var indexIncre=0;
						  var headingg="";
						  var checkIsNum=/^[0-9]+$/;
						  var isObjectAvailble=true;
						  var ht ="";
						  debugger;
						  var subProddata, html ='';
						  var productname=data.key;
						  var subProddata = chilProdData[data], html ='';	
						  $('.loader1').show();
						  debugger;
						  $.ajax({
							  url : "getChildProdDetails.spring?prodName="+data,
							  type : "GET",
							 dataType : "json",
						 	// contentType:"xml",
							  success : function(response) {
								  console.log("response: "+JSON.stringify(response));
								  subProddata = response, html ='';
								  //var subProddata = chilProdData[data.key], html ='';
								   jQuery.each(jQuery.parseJSON(JSON.stringify(response)), function(index, item){		debugger;
										console.log("subProddata: "+JSON.stringify(item)); 
										console.log("subProddata: "+JSON.stringify(index)); 
										jQuery.each(JSON.parse(JSON.stringify(item)), function(index, item){	
									  		/* console.log("subProddata: "+index);
									  		console.log("subProddata: "+item); */
									  		debugger;
									  		if(!isNaN(index)){
									  			headingg="";
										  		ht="";
										  		jQuery.each(jQuery.parseJSON(JSON.stringify(item)), function(index, item){
										  			//console.log("subProddata: "+index);
										  				debugger;
										  			if(item==indexIncre){
										  				headingg += "<div class='subproduct' style='margin-top:10px;margin-bottom:12px'><span class=''><span class='sideMenuClick' style='font-size:18px;color:#ba5555;'> <u>"+ index +":</u></span></span></div>";
										  			/* 	headingg += ht; */
										  				//console.log("subProddata: "+index);
										  			//alert("subProddata: "+index);
										  			}
										  			else{
										  				  ht += "<div class='childproduct' style='background-color:#eaeaea;'><span class=''><strong><span class='sideMenuClick'> "+ index +":&nbsp</span><span class='sideMenuClick_index' style='width:10px;color: #0932f2;'>"+ item +"</span></div>";
												  		   //html += ht;
										  				//console.log(item+" : "+index);
										  			}
											  	});
										  		html+=headingg+ht;
									  			
										  		
									  		}else{
									  			isObjectAvailble=false;
										  		if(item==indexIncre){
										  			headingg += "<div class='subproduct' style='margin-top:10px;margin-bottom:12px'><span class=''><span class='sideMenuClick' style='font-size:18px;color:#ba5555;'> <u>"+ index +":</u></span></span></div>";
										  		}else{
										  			 ht += "<div class='childproduct' style='background-color:#eaeaea;'><span class=''><strong><span class='sideMenuClick'> "+ index +":&nbsp</span><span class='sideMenuClick_index' style='width:10px;color: #0932f2;'>"+ item +"</span></div>";
										  		}
									  		
									  		}//else condition
									  		
									  		//indexIncre++;
									  });
										
										
								   });
								 	 
									   if(isObjectAvailble==false){
										  html+=headingg+ht;
									  } 
									  $('.loader1').hide();
									   $('.data').html(html);
							  }
						  });  
						
			   		 })
				        .on("mouseout", function(d){			
				    	   divTooltip.style("visibility", "hidden") ;
					      }) 
					      
					  //.call(d3.behavior.zoom().on("zoom", zoom))
					  .style("font-family", "calibri");

					  svg.append("g")
				      .attr("class", "y axis")
				      .call(yAxis.ticks(15).tickFormat(function (d) {
				    	  if ((d / 10000000) >= 1) {
				    	        d = d / 10000000 + "Cr";
				    	    }
				    	  else if ((d / 100000) >= 1) {
				    	        d = d / 100000 + "L";
				    	    }
				    
				    	    return d;
				    	}))
				      .append("text")
				      .attr("transform", "rotate(-90)")
				      .attr("y", 6)
				      .attr("dy", ".71em")
				      .style("text-anchor", "end")
				      /* .text("Amount") */;
					  svg.append("text")
			            .attr("text-anchor", "middle")  // this makes it easy to centre the text as the transform is applied to the anchor
			            .attr("transform", "translate("+ (-45) +","+(height/2)+")rotate(-90)")  // text is drawn off the screen top left, move down and out and rotate
			            .text("Amount");

			      svg.append("text")
			            .attr("text-anchor", "middle")  // this makes it easy to centre the text as the transform is applied to the anchor
			            .attr("transform", "translate("+ (width/2) +","+(height+250)+")") // centre below axis
			            .text("Products");
				  svg.selectAll(".bar")
				      .data(dataArray)
				      .enter().append("rect")
				      .attr("class", "bar")				      
				      .attr("fill", "rgb(8, 208, 192)")
				      .attr("x", function(d) {
				    	  return x(d.key);
				    	  
				      })
				      .attr("width", x.rangeBand())
				      .attr("y", function(d) { return y(d.value); })
				      .attr("height", function(d) { return height - y(d.value); })
				      .on('mouseover', tip.show)
				      .on('mouseout', tip.hide)
					  .on('click', function(data,d, e) {				   		
					  $('.data').show();
					  //$(".chatAsu").css("background-color", "rgb(234, 234, 234)");
						  $('.data').html( '');
						  var indexIncre=0;
						  var headingg="";
						  var checkIsNum=/^[0-9]+$/;
						  var isObjectAvailble=true;
						  var ht ="";
						 
						  debugger;
						  var subProddata, html ='';
						  var productname=data.key;
						  var subProddata = chilProdData[data.key], html ='';	
						  $('.loader1').show();
						  debugger;
						  $.ajax({
							  url : "getChildProdDetails.spring?prodName="+data.key,
							  type : "GET",
							 dataType : "json",
						 	// contentType:"xml",
							  success : function(response) {
								
								  console.log("response: "+JSON.stringify(response));
								  subProddata = response, html ='';
								  //var subProddata = chilProdData[data.key], html ='';
								   jQuery.each(jQuery.parseJSON(JSON.stringify(response)), function(index, item){		debugger;
										console.log("subProddata: "+JSON.stringify(item)); 
										console.log("subProddata: "+JSON.stringify(index)); 
										jQuery.each(JSON.parse(JSON.stringify(item)), function(index, item){	
									  		/* console.log("subProddata: "+index);
									  		console.log("subProddata: "+item); */
									  		debugger;
									  		if(!isNaN(index)){
									  			headingg="";
										  		ht="";
										  		jQuery.each(jQuery.parseJSON(JSON.stringify(item)), function(index, item){
										  			//console.log("subProddata: "+index);
										  				debugger;
										  			if(item==indexIncre){
										  				headingg += "<div class='subproduct' style='margin-top:10px;margin-bottom:12px'><span class=''><span class='sideMenuClick' style='font-size:18px;color:#ba5555;'> <u>"+ index +":</u></span></span></div>";
										  			/* 	headingg += ht; */
										  				//console.log("subProddata: "+index);
										  			//alert("subProddata: "+index);
										  			}
										  			else{
										  				  ht += "<div class='childproduct' style='background-color:#eaeaea;'><span class=''><strong><span class='sideMenuClick'> "+ index +":&nbsp</span><span class='sideMenuClick_index' style='width:10px;color: #0932f2;'>"+ item +"</span></div>";
												  		   //html += ht;
										  				//console.log(item+" : "+index);
										  			}
											  	});
										  		html+=headingg+ht;
									  			
										  		
									  		}else{
									  			isObjectAvailble=false;
										  		if(item==indexIncre){
										  			headingg += "<div class='subproduct' style='margin-top:10px;margin-bottom:12px'><span class=''><span class='sideMenuClick' style='font-size:18px;color:#ba5555;'> <u>"+ index +":</u></span></span></div>";
										  		}else{
										  			 ht += "<div class='childproduct' style='background-color:#eaeaea;'><span class=''><strong><span class='sideMenuClick'> "+ index +":&nbsp</span><span class='sideMenuClick_index' style='width:10px;color: #0932f2;'>"+ item +"</span></div>";
										  		}
									  		
									  		}//else condition
									  		
									  		//indexIncre++;
									  });
										
										
								   });
								 	 
									   if(isObjectAvailble==false){
										  html+=headingg+ht;
									  } 
									  $('.loader1').hide();
									   $('.data').html(html);
							  }
						  });  
						
			   		 });
				 

			  }
		 });	
		 
		 $(".data").click(function(){
			 	$(".data").attr("title", "click out side to minimize.");
				$(".data").css({
					"margin-left":"-200px",
					"width":"300px",
					"height":"760px",
					"overflow":"scroll",
					/* "overflow-x":"scroll", */
					"background-color":"#eaeaea"
				});
				$(".loader1").css({
					"margin-right":"12%"
				});
				
				
			});
		 $("body").click(function(){
			 $(".data").attr("title", "click here to view.");
			 $(".data").css({
					"margin-left":"0px",
					"width":"100px"
					
				});
			 $(".loader1").css({
					"margin-right":"5%"
				});
		  });

		  $(".data").click(function(event) {
		    event.stopPropagation();
		  });
		</script>
	</body>
</html>

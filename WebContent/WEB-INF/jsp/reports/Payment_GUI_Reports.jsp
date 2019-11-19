<html xmlns:jsp="http://java.sun.com/JSP/Page">
	<head>
	    <script src="https://cdnjs.cloudflare.com/ajax/libs/d3/3.5.5/d3.min.js" charset="utf-8"></script>
		<script src="http://labratrevenge.com/d3-tip/javascripts/d3.tip.v0.6.3.js"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/d3-legend/1.7.0/d3-legend.min.js"></script>
		<style>
.nav-md{
 overflow-x:hidden;
}
			.axis path, .axis line {
				  fill: none;
				  stroke: #000;
				  shape-rendering: crispEdges;
	         }
			.bar {
			  fill: steelblue;
			}
			
			.x.axis path {
			  display: none;
			}
			div.tooltip {	
			    position: absolute;			
			    text-align: center;			
			    width: 100px;					
			    height: 45px;				
			    padding: 6px;				
			    font: 12px sans-serif;		
			    background: white;	
			    border: 1px solid;		
			    border-radius: 8px;			
			    pointer-events: none;			
			}
			.stacked-bar{
			  background-color:#eaeaea;
			  border:1px solid;
			  width: 1055px;
			  height: 600px;
			}
			text{
			 font: 14px sans-serif;
			 font-weight: bold;
			}
			.axis text{
			 font: 15px sans-serif;
			 font-weight: bold;
			}
			svg{
			 padding-left:60px;
			 margin-top:4%;
			}
			#d3name{
			    text-align: center;
			    font-size: 24px;
			    font-family: sans-serif;
			
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
			.hideClass{
			display:none;
			}
			@media only screen and (min-width:320px) and (max-width:768px){
			 .stacked-bar {
			   margin-left:0px;
			    width: 700px;
			   padding-left:0px;
			}
			.container-resp{
			overflow-x:scroll;
			}
			}
			.txtcls{
			/* font-size:30px; */
			    font-weight: unset;
			    color:#26254a;
			}
    </style>
	</head>
	<body>
			<!-- page content -->
			<form  class="form-horizontal"  style="width:1200px;" action="dashboard.spring"  method="post"    autocomplete="off">	
					<!-- ********** Chart for stacked ***********	 -->
					<div id="d3name">Payment Request for Next 7days.</div>
						<div class="container container-resp">
							<div class="stacked-bar" >		
								<div class="loader"></div>	
								<p class="text-center hideClass">Generating Report...</p>
							</div>
						 </div>	
			</form>
		<!-- jQuery -->
		<script src="js/jquery.min.js"></script>		
				<script type="text/javascript">
				
				var url = "purchaseReport.spring";
				 $('.loader').show();
				 $('.hideClass').show();
				 $.ajax({
					  url : url,
					  //url : "${pageContext.request.contextPath}/getVendorDetails.spring",
					  type : "GET",
					 dataType : "json",
					  success : function(response) {
						  $('.loader').hide();
						  $('.hideClass').hide();
						 // console.log(JSON.stringify(response));
					   var data=response.xml.label;
					   data.sort(function(a, b){
						  debugger;
					    var aa = a.name.split('-').reverse().join(),
						        bb = b.name.split('-').reverse().join();
						    return aa < bb ? -1 : (aa > bb ? 1 : 0); 
						});			   
						  var margin = {top: 20, right: 20, bottom: 30, left: 40},
						    width = 960 - margin.left - margin.right,
						    height = 500 - margin.top - margin.bottom;
						var divTooltip = d3.select("body").append("div")	
						    .attr("class", "tooltip")				
						    .style("opacity", 0);
						var x0 = d3.scale.ordinal()
						    .rangeRoundBands([0, width], .1);
		
						var x1 = d3.scale.ordinal();
		
						var y = d3.scale.linear()
						    .range([height, 0]);
		
						var color = d3.scale.ordinal()
						     .range(["#01b8aa", "#fea19c"]);
		
						var xAxis = d3.svg.axis()
						    .scale(x0)
						    .orient("bottom");
		
						var yAxis = d3.svg.axis()
						    .scale(y)
						    .orient("left")
						    .tickFormat(d3.format(".2s"));
		
						var svg = d3.select(".stacked-bar").append("svg")
						    .attr("width", width + margin.left + margin.right)
						    .attr("height", height + margin.top + margin.bottom)
						  .append("g")
						    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
		
						var ageNames = d3.keys(data[0]).filter(function(key) { return key !== "name"; });
		
						data.forEach(function(d) {
						    d.ages = ageNames.map(function(name) { return {name: name, value: +d[name]}; });
						  });
		
						x0.domain(data.map(function(d) { return d.name; }));
						x1.domain(ageNames).rangeRoundBands([0, x0.rangeBand()]);
						y.domain([0, d3.max(data, function(d) { return d3.max(d.ages, function(d) { return d.value+100000; }); })]);
		
						svg.append("g")
						      .attr("class", "x axis")
						      .attr("transform", "translate(0," + height + ")")
						      .call(xAxis)
						      .style("cursor", "pointer");
					
						svg.append("g")
						      .attr("class", "y axis")
						      .call(yAxis.ticks(10).tickFormat(function (d) {
								    	    if ((d / 100000) >= 1) {
								    	        d = d / 100000 + "L";
								    	    }
								    	    else{
								    	    	 d = d / 1000 + "k";							    	    
								    	    }
								    	    return d;
								})) 
							  .append("text")
						      .attr("transform", "rotate(-90)")
						      .attr("y", 6)
						      .attr("dy", ".71em")
						      .style("text-anchor", "end")
						      /* .text("Population") */;
						svg.append("text")
			            .attr("text-anchor", "middle") 
			            .attr("transform", "translate("+ (-60) +","+(height/2)+")rotate(-90)")
			            .text("Amount");
		
			           svg.append("text")
			            .attr("text-anchor", "middle")
			            .attr("transform", "translate("+ (350) +","+(height+65)+")")
			            .text("Date");
			           
						var state = svg.selectAll(".state")
						      .data(data)
						      .enter().append("g")
						      .attr("class", "state")
						      .attr("transform", function(d) { return "translate(" + x0(d.name) + ",0)"; });
		
						var bar=state.selectAll("rect")
						      .data(function(d) { return d.ages; })
						      .enter().append("rect")
						      .attr("width", x1.rangeBand())
						      .attr("x", function(d) { return x1(d.name); })
						      .attr("y", function(d) { return y(d.value); })
						      .attr("height", function(d) { return height - y(d.value); })
						      .style("fill", function(d) { return color(d.name); });
		
						var legend = svg.selectAll(".legend")
						      .data(ageNames.slice().reverse())
						      .enter().append("g")
						      .attr("class", "legend")
						      .attr("transform", function(d, i) { return "translate(0," + i * 20 + ")"; });
		
						var bar_enter=legend.append("rect")
						      .attr("x", width - 18)
						      .attr("width", 18)
						      .attr("height", 18)
						      .style("fill", color);
						state.selectAll("text")
						  .data(function(d) { 
							  //console.log("d: "+d);
							  return d.ages;
							  })
						  .enter().append("text")
						  .attr("class", "txtcls")
						  .attr("x", function(d) { return x1(d.name)+10; })
						  .attr("y", function(d) { return y(d.value); })
						  //.attr("transform", "rotate(90)")
						  .style("fill", "#26254a")
						  .text(function(d, i) { 
							  //console.log("d1: "+JSON.stringify(d));
							  //console.log("i1: "+i);
							  if(d.value=="0")
								  {
								  return "";
								  }
							  else{
								  return d.value;
							  }
							  
							  });
						legend.append("text")
						      .attr("x", width - 24)
						      .attr("y", 9)
						      .attr("dy", ".35em")
						      .style("text-anchor", "end")
						      .text(function(d) {
						    	  if(d=="HYD")
						    	    {
						    		  return "HYDERABAD";
						    		  }
						    	  else{
						    		  return d;
						    	  }
						    	  
						      });
						bar.on("mousemove", function(d){
							 var name;
							 if(d.name=="HYD")
					    	    {
								 name="HYDERABAD";
					    		  }
					    	  else{
					    		  name=d.name;
					    	  }
				            	 divTooltip.transition()		
				                 .duration(200)		
				                 .style("opacity", 1);			            	 
				            	  divTooltip.html(name+"<br>"+d.value)	 
				                 .style("left", (d3.event.pageX+15) + "px")		
				                 .style("top", (d3.event.pageY - 28) + "px");						            	
				            });
					    bar.on("mouseout", function(d){					              
					            	 divTooltip.transition()		
					                 .duration(500)		
					                 .style("opacity", 0);	
					         });	
		
					  }
					  });
		   </script>
	</body>
</html>
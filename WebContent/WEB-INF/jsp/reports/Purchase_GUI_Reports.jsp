<html xmlns:jsp="http://java.sun.com/JSP/Page">
	<head>
	
		<script src="https://cdnjs.cloudflare.com/ajax/libs/d3/3.5.5/d3.min.js" charset="utf-8"></script>
		<script src="http://labratrevenge.com/d3-tip/javascripts/d3.tip.v0.6.3.js"></script>
		<script src="https://d3js.org/d3.v4.js"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/d3-legend/1.7.0/d3-legend.min.js"></script>
		<style>
.nav-md{
	overflow-x:hidden;
}
.finalAmountDiv{
    color: #f0ad4e;
    font-size: 24px;
}
    svg {
        width: 100%;
        height: 100%;
        position: center;        
        margin-top: 4%;
        padding-left: 60px;
    }
    text{
        font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
    }
    .toolTip_bar{
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
    .legend {
        font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
        font-size: 60%;
    }
    text {
        font: 14px sans-serif;
            font-weight: bold;
    }
    .axis text {
        font: 15px sans-serif;
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
    .stacked-sideBar{
    width: 100px;
    height: 415px;
    position: absolute;
    border: 1px solid;
    margin-left: 984px;
    z-index:-2;
    position:absolute;
    }
   .nav_menu{
    height:auto !important;
    } 
    .stacked-bar {
    font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
    width: 1055px;
    height: 600px;
    background-color:#eaeaea;
    border:1px solid;
    /* padding-left:80px; */
    position: relative;
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
.paymenttitle{
	width:1000px;
	height:700px;
}
}
div.tooltip {	
    position: absolute;			
    text-align: center;			
    width: 80px;					
    height: 50px;					
    padding: 4px;				
    font: 12px sans-serif;		
    background: white;	
    border: 1px solid;		
    border-radius: 8px;			
    pointer-events: none;			
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
.linebar{
border:1px solid;
padding:10px;
 background-color:#eaeaea;
}
.indication{
background-color:#ffff;
width:145px;
height:75px;
/* z-index:9999; */
position:absolute;
right:50px;
top:30px;
}
.btn{
	/* background-color:red; */
	width:20px;
	padding:10px;
	margin-left:20px;
	/* margin-top:10px; */
	float:left;

}
#hyd{
background-color:#fea19e;
border:1px solid #fea19e;
}
#bnglr{
background-color:#01b8aa;
border:1px solid #01b8aa;
}
.span-name{
    margin-top: 20px;
    margin-left: 15px;
    font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
}
.para-cities{
margin-top:10px;
}
/* ************ Line chart CSS*********** */
 .axis path {
            fill: none;
            stroke: #777;
            shape-rendering: crispEdges;
        }
        .axis text {
              font-family: sans-serif;
            font-size: 12px;
        }
        
        /* line chart styles   */
        
.line {
  fill: none;
  stroke-width: 3px;
}

.overlay {
  fill: none;
  pointer-events: all;
}

.hover-line {
  stroke: #B74779;
  stroke-width: 2px;
  stroke-dasharray: 3,3;
}
.linechartdiv{
  border:1px solid;
  background-color:#eaeaea;
  position: relative;
  
}
#visualisation{
 padding-left:80px; 
 padding-bottom: 30px;
}
.indications{
    position: absolute;
    top: 20px;
    right: 43px;
    background-color:#ffff;
	width:161px;
	height:98px;
}
#localpur{
background-color:#036888;
border:1px solid #036888;
}
#marketpur{
background-color:#0D833C;
border:1px solid #0D833C;
}
#popur{
background-color:#D2392A;
border:1px solid #D2392A;
}

#d3name{
    text-align: center;
    font-size: 24px;
    font-family: sans-serif;

}
.paymenttitle{
margin-top:20px;
}
div.tltp{
	padding-top:16px;
	width:270px;
	height:85px;
}
.prev{
display:none;
float:left;
margin:5px 10px;
}
#divPages{
float:left;
}
#divPages div{
float:left;
margin:5px 10px;
}
.next{
float:left;
margin:5px 10px;
}
.divControl{
	display: none;
}
.faicon{
	font-size: 30px;
    margin-top: 20%;
}
.faicon:hover{
	cursor: pointer;
}
</style>
	</head>
				<!-- page content -->
			<form  class="form-horizontal"  style="width:1200px;" action="dashboard.spring"  method="post"    autocomplete="off">		
	<!-- --------Page Content start here-----------		 -->		
	
	<!-- ---------------- Line Chart-------------- -->
	    <div class="container container-resp">
	      <div class="col-md-12" style="margin-bottom: 40px;">
					<p id="reportptag">REPORTS</p>
						 <ul class="nav nav-pills nav-justified navStyle">
				    <li ><a href="#" onclick="redirectPage('marketingExpendature')" style="height: 60px;width: 150px;"> MARKETING EXPENDITURE</a></li>    
				    <li ><a href="#" onclick="redirectPage('boqreport')" style="height: 60px;width: 150px;">BOQ REPORT</a></li>
				    <li ><a href="#" onclick="redirectPage('stockDetails')" style="height: 60px;width: 150px;">STOCK DETAILS</a></li>
				    <li><a href="#" onclick="redirectPage('paybles')" style="height: 60px;width: 150px;">PAYABLES</a></li>
				    <li ><a href="#" onclick="redirectPage('purchaseExpen')" style="height: 60px;width: 150px;">PURCHASE EXPENDITURE</a></li>
				   
				    <li class="active"><a href="#" onclick="redirectPage('cashFlow')" style="background: #ef7e2d;height: 60px;width: 150px;">MARKETING CASH FLOW</a></li>
				     
				  </ul>
					</div>
				 <!-- <svg id="visualisation" width="1000" height="750"></svg> -->
				<!--  <div class="linebar"  width="1000" height="750"></div> -->
				
				<div class="col-md-12">	<div class="prev"><i class="fa fa-arrow-left faicon" aria-hidden="true"></i></div>
						<div id="divPages"></div>
						<div class="next"><i class="fa fa-arrow-right faicon" aria-hidden="true"></i></div></div>
				<div class="paymenttitle">
				<div id="d3name">Purchase Comparison.</div>
				
				<div class="linechartdiv">				
				<div class="indications">
				<p class="para-cities"><span class="btn" id="localpur"></span><span class="span-name">Local Purchase</span></p>
				<p class="para-cities"><span class="btn" id="marketpur"></span><span class="span-name">Market Purchase</span></p>
				<p class="para-cities"><span class="btn" id="popur"></span><span class="span-name">PO Purchase</span></p>
		       </div>
				<svg  id="visualisation" width="960" height="600"></svg>
				 </div>
				 </div>
		 	
		</div>
			</form>
				
   
		<!-- jQuery -->
		<script src="js/jquery.min.js"></script>
		<!-- Bootstrap -->
		<!-- <script src="js/bootstrap.min.js"></script> -->
		<!-- Custom Theme Scripts -->
		<!-- <script src="js/custom.js"></script> -->
		<script>
			$(document)
					.ready(
							function() {
								$(".up_down")
										.click(
												function() {
													$(this)
															.find('span')
															.toggleClass(
																	'fa-chevron-up fa-chevron-down');
													$(this)
															.find('span')
															.toggleClass(
																	'fa-chevron-right fa-chevron-left');
												});

							});
		
			/* $(function() {
				var div1 = $(".right_col").height();
				var div2 = $(".left_col").height();
				var div3 = Math.max(div1, div2);
				$(".right_col").css('max-height', div3);
				$(".left_col").css('min-height',
						$(document).height() - 65 + "px");
			});
	 */
			/* ***************************** Chart for Stacked chart************** */		 

	/* *************************** Start Line chart Code************** */
	
	var url = "	purhaseTypesAndotalCost.spring";
			
			 $.ajax({
				  url : url,
				  //url : "${pageContext.request.contextPath}/getVendorDetails.spring",
				  type : "GET",
				 dataType : "json",
				  success : function(response) {
				  
				  console.log("response: "+JSON.stringify(response.xml.data));
				// var data = response.xml.data;
				 var jsondata=$.parseJSON(JSON.stringify(response.xml));
				  var data = jsondata.data.reverse(); 
				  console.log("data: "+data);
			    /*  var objAssetSelection = $.parseJSON(JSON.stringify(response.xml));
				  objAssetSelection.data.reverse();
				  console.log("obj: "+JSON.stringify(objAssetSelection)); */ 
				  var trendsText = {'LocalPurchase': 'Local purches', 'MarketPurchase': 'market purches', 'PoPurchase': 'Po purches'};

				// set the dimensions and margins of the graph
				var margin = { top: 20, right: 80, bottom: 30, left: 50 },  
				    /* svg = d3.select('svg'), */
				    svg = d3.select('#visualisation'),
				    width =830,
				    height = +svg.attr('height') - margin.top - margin.bottom;
				var g = svg.append("g")
				  .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
             /*   alert("width: "+height);  */
				// set the ranges
				var x = d3.scaleBand().rangeRound([0, width]).padding(1),
				    y = d3.scaleLinear().rangeRound([height, 0]),
				    z = d3.scaleOrdinal(['#036888','#0D833C','#D2392A']);

				// define the line
				var line = d3.line()
				  .x(function(d) { return x(d.timescale); })
				  .y(function(d) { return y(d.total); });

				// scale the range of the data
				z.domain(d3.keys(data[0]).filter(function(key) {
				  return key !== "timescale";
				}));

				var trends = z.domain().map(function(name) {
				  return {
				    name: name,
				    values: data.map(function(d) {
				      return {
				        timescale: d.timescale,
				        total: +d[name]
				      };
				    })
				  };
				});

				x.domain(data.map(function(d) { return d.timescale; }));
				/* y.domain([100000, d3.max(trends, function(c) {
				  return d3.max(c.values, function(v) {
				    return v.total+200000;
				  });
				})]); */
				y.domain([30000,10000000]);
				// Draw the line
				var trend = g.selectAll(".trend")
				  .data(trends)
				  .enter()
				  .append("g")
				  
				  .attr("class", "trend");

				trend.append("path")
				  .attr("class", "line")
				  .attr("d", function(d) { return line(d.values); })
				  .style("stroke", function(d) { return z(d.name); });

				// Draw the empty value for every point
				var points = g.selectAll('.points')
				  .data(trends)
				  .enter()
				  .append('g')
				  .attr('class', 'points')				 
				  .append('text');
				   var divTooltip1 = d3.select("body").append("div")	
				    .attr("class", "tooltip tltp")				
				    .style("opacity", 0);
				// Draw the circle
				trend
				  .style("fill", "#FFF")
				  .style("stroke", function(d) { return z(d.name); })
				  .selectAll("circle.line")
				  .data(function(d){ /* alert(JSON.stringify(d.values)); */ return d.values }) 
				  .enter()
				  .append("circle")
				  .attr("r", 5)
				  .style("stroke-width", 3)
				  .attr("cx", function(d) { return x(d.timescale); })
				  .attr("cy", function(d) { return y(d.total); });

				// trend
				//   .selectAll("circle.text")
				//   .data(function(d){ return d.values })
				//   .enter()
				//   .append('text')
				//   .attr('x', function(d) { return x(d.timescale) + 15; })
				//   .attr('y', function(d) { return y(d.total); })
				//   .text(function(d) { return d.total; });

				// Draw the axis
				g.append("g")
				  .attr("class", "axis axis-x")
				  .attr("transform", "translate(0, " + height + ")")
				  .call(d3.axisBottom(x));

				g.append("g")
				  .attr("class", "axis axis-y")
				  .call(d3.axisLeft(y).ticks(20).tickFormat(function (d) {
			    	    if ((d / 100000) >= 1) {
			    	        d = d / 100000 + "L";
			    	    }
			    	    return d;
			    	}));
				
				 g.append("text")
		            .attr("text-anchor", "middle")  // this makes it easy to centre the text as the transform is applied to the anchor
		            .attr("transform", "translate("+ (-70) +","+(height/2)+")rotate(-90)")  // text is drawn off the screen top left, move down and out and rotate
		            .text("Amount");

		       g.append("text")
		            .attr("text-anchor", "middle")  // this makes it easy to centre the text as the transform is applied to the anchor
		            .attr("transform", "translate("+ (420) +","+(height+55)+")")  // centre below axis
		            .text("Month");

				var focus = g.append('g')
				  .attr('class', 'focus')
				  .style('display', 'none');

				focus.append('line')
				  .attr('class', 'x-hover-line hover-line')
				  .attr('y1' , 0)
				  .attr('y2', height);

				svg.append('rect')
				  .attr("transform", "translate(" + margin.left + "," + margin.top + ")")
				  .attr("class", "overlay")
				  .attr("width", width)
				  .attr("height", height)
				  .on("mouseover", mouseover)
				  .on("mouseout", mouseout)  
				  .on("mousemove", mousemove);

				var timeScales = data.map(function(name) { return x(name.timescale); });

				function mouseover() {
				  focus.style("display", null);
				  divTooltip1.transition()		
	                 .duration(200)		
	                 .style("opacity", 0);	
				  d3.selectAll('.points text').style("display", null);
				}
				function mouseout() {
				  focus.style("display", "none");
				  divTooltip1.transition()		
	                 .duration(200)		
	                 .style("opacity", 0);	
				  d3.selectAll('.points text').style("display", "none");
				}
				function mousemove() {
					debugger;
				  var i = d3.bisect(timeScales, d3.mouse(this)[0], 1);
				  var di = data[i-1];
				 console.log(di.LocalPurchase);
				 console.log(di.MarketPurchase);
				 console.log(di.POPurchase);
				  focus.attr("transform", "translate(" + x(di.timescale) + ",0)");				 
				  divTooltip1.transition()		
					                 .duration(200)		
					                 .style("opacity", .9);		
					            	  divTooltip1.html("<div><span class='col-md-6 text-left'>Month</span><span class='col-md-1'>:</span><span class='col-md-4 text-right'>"+di.timescale+"</span></div><div><span style='color:#036888' class='col-md-6 text-left'>Local Purchase</span><span class='col-md-1'>:</span><span class='col-md-4 text-right' style='color:#036888'> "+di.LocalPurchase+"</span></div><div><span style='color:#0D833C' class='text-left col-md-6'>Market Purchase</span><span class='col-md-1'>:</span><span class='col-md-4 text-right' style='color:#0D833C'>"+di.MarketPurchase+"</span></div><div class='clearfix'></div><div><span style='color:#D2392A' class='text-left col-md-6'>PO Purchase</span><span class='col-md-1'>:</span> <span class='col-md-4 text-right' style='color:#D2392A'>"+di.POPurchase+"</span></div>")	
					                 .style("left", (d3.event.pageX+15) + "px")		
					                 .style("top", (d3.event.pageY - 60) + "px");
				/*  d3.selectAll('.points text')
				    .attr('x', function(d) { return x(di.timescale) + 35; })
				    .attr('y', function(d) { return y(d.values[i-1].total); })
				    .text(function(d) { return d.values[i-1].total; })
				    .style('fill', function(d) { return z(d.name); });  */
				}

				  }
			 });
			 $(document).ready(function(){
				 var totalrows = 28;
				 var pageSize=4;
				 var noOfPage = totalrows/pageSize;
				 noOfPage = Math.ceil(noOfPage);
				 var data=[];
				 for(var i=1;i<=noOfPage;i++){
				   $("#divPages").append('<div class="page"><button class="btn btn-warning" type="button" style="width: auto;">Click '+i+'</div></div>'); 
				 }
				  var totalPagenum = $("div.page").length;
				  if(totalPagenum >2){
				   $("div.page").hide();
				   for(var n=1;n<=4;n++){
				    $("div.page:nth-child("+n+")").show();
				   }
				  }else{
					$("div.next").hide();
				   	$("div.prev").hide();
				  }
				 /*  $("div.divControl").hide();
				 for(var j=1;j<=pageSize;j++){
				    $("div.divControl:nth-child("+j+")").show();
				  } */
				  displayevent();
				   $("div.next").click(function(){debugger;
				   if($("div.selected:last").nextAll('div.page').length > 4){
				       $("div.selected").last().nextAll(':lt(4)').show();
				       $("div.selected").hide();
				       displayevent();
				       //lastposevent();
				       $("div.prev").show();
				       $("div.next").show();
				   }else{
					    $("div.selected").last().nextAll().show();
					    $("div.selected").hide();
					    displayevent();
					    $("div.next").hide();
					    $("div.prev").show();
				   }
				  });
				  $("div.prev").click(function(){debugger;
				  if($("div.selected:first").prevAll('div.page').length > 4){
				       $("div.selected").first().prevAll(':lt(4)').show();
				       $("div.selected").hide();
				       $("div.prev").show();
				       $("div.next").show();
				       displayevent();
				  }else{
				 	   $("div.selected").first().prevAll().show();
				       $("div.selected").hide();
				       $("div.prev").hide();
				       $("div.next").show();
				       displayevent();
				   }
				  });
				 $("div.page").click(function(){
					   var currentPage = $(this).text();
					   $("div.divControl").hide();
					   for (var k = (currentPage * pageSize) - (pageSize-1); k <= (currentPage * pageSize); k++){
					    $("div.divControl:nth-child("+k+")").show();
					  }  
				  });
				});
				function displayevent(){
				  $("div.page").each(function(){
					   if( $(this).css('display') === 'block') {
						$(this).addClass('selected');
					  }else{
					  	$(this).removeClass('selected');
					  }
				  });
				}
		</script>
	</body>
</html>
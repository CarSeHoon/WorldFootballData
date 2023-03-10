<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>WorldFootballData</title>
<!-- 개발하다가 웹페이지에 cache때문에 바뀐 css가 적용되지 않으묜 href에 ?ver=1.1이런식으로 쿼리스트링을 붙여주면 새로운 파일로인식하여 해결이가능하다 -->
<!-- 또는 웹페이지설정에서 캐시데이터를 비워주면 된다  -->
<link rel="stylesheet" href="/resources/css/reset.css" type="text/css">
<link rel="stylesheet" href="/resources/css/main.css?ver=1" type="text/css">
</head>
<body>
	<!--  페이지 Nav바 시작  -->
	<%@ include file="header.jsp"%>
	<!--  페이지 Nav바 끝  -->


	<!-- 페이지 바디  -->
	<div class="container">

	<div class="topNewsAndMap">
		<!-- 메인 페이지 상단 캐러셀 시작  -->
		<div class="main_news_box">
			<figure class="icon-cards mt-3">
				<div class="icon-cards__content">
				<c:forEach var="news" items="${newsList}" varStatus="status"
						begin="0" end="5">
						<div class="icon-cards__item">
							<a href="${news.newsUrl}"><img src="${news.newsImgUrl}"></img>
								<div class="text_box">${news.newsTitle }</div> </a>
						</div>
					</c:forEach>
				</div>
			</figure>
		</div>
		<div id="mapdiv"></div>
	</div>
		<!-- 메인 페이지 상단 캐러셀 시작  -->

		<!-- 최신뉴스기사 오늘경기일정 시작  -->
		<div class="content">

			<!-- 뉴스박스 시작  -->
			<div class="news_box">
				<div class="today_news_title">
					<h1>최신 뉴스</h1>
				</div>
				<div class="news_list_box">
					<ul class="news_list">

					</ul>
				</div>
			</div>
			<!-- 뉴스박스 끝   -->

			<!-- 오늘경기일정  시작  -->
			<div class="today_match_box">

				<div class="today_match_title">
					<h1 style="height:14px; margin-bottom: 40px;">오늘 경기 일정</h1>
					<div class="league_box">
						<select id="selectbox">
							<option value="all">전체일정</option>
							<option value="epl">프리미어리그</option>
							<option value="laliga">라리가</option>
							<option value="bundesliga">분데스리가</option>
							<option value="seriea">세리에A</option>
						</select>
					</div>
				</div>



				<div class="today_match_list"></div>

			</div>
			<!-- 오늘경기일정 끝  -->

		</div>
	</div>
	<script type="text/javascript">
    
     $(document).ready(function(){
    	 
    		/* 최신뉴스기사크롤링 */
         $.ajax({
             url :"/craw/crawSelect.ajax",
             dataType : "json",
             type : "post",
             success:function(data){
                for (var i = 0; i < data.length; i++){
                   $(".news_list").append(
                       "<li class='news'><a class='news_link' href="+ data[i].newsURL + ">"+
                     "<div class='img_area'>"+
                        "<img src="+ data[i].img + "></img>"+
                     "</div>"+
                     "<div class='text_area'>"+
                        "<strong>"+data[i].tit + "</strong>"+
                        "<p>"+data[i].content + "</p>"+
                        "<div class='information'>"+
                           "<span>"+data[i].officeName + "</span>&nbsp; &nbsp; <span>"+data[i].date + "</span>"+
                        "</div>"+
                     "</div>"+
               "</a></li>"
                     );    
                }
                
             }
         })
         /* 최신 뉴스 기사 크롤링 끝. */
         
         
         
    	 
	/* 오늘 경기 일정 크롤링  */
         $.ajax({
             url :"/craw/matchCrawling.ajax",
             dataType : "json",
             type : "post",
             success:function(data){
            	 
            	 if(data[0]){
            		 
            		 /* 경기일정박스 포문 시작. */
            	 for (var i = 0; i < data.length; i++){
            		 $(".today_match_list").append(
                             "<div class='today_match'>"+
     		                      "<div class='today_match_home'>"+
     		                         "<div class='team_name'>"+data[i].homeTN +"</div>"+
     		                         "<div class='team_emblem'><img src="+data[i].homeTeamImageUrl+" width='30' height='30'></div>"+
     		                         "<div class='score'>"+data[i].homeResult +"</div>"+
     		                      "</div>"+
                           
     		                      "<div class='match_time'>"+ data[i].startTime.replace(/\B(?=(\d{2})+(?!\d))/g, ':') +"</div>"+
     		                      
     		                      "<div class='today_match_away'>"+
     		                      	"<div class='score'>"+data[i].awayResult+"</div>"+
     		                         "<div class='team_emblem'><img class='home_img' src="+data[i].awayTeamImageUrl+" width='30' height='30'></div>"+ 
     		                      	"<div class='team_name'>"+data[i].awayTN+"</div>"+
     		                      "</div>"+
                       		"</div>"
                          );    
            	 } /* 경기일정박스 포문 끝. */
            	 
            	 /*  change function start. */
            	 $("#selectbox").on("change", function(){
            		 
            			 if($("option:selected", this).text()=="전체일정"){
            				 
            				 /* 기존에 선택되어 추가된 요소 비우기  */
                      		$(".today_match_list").empty();
            				 
                      		for (var i = 0; i < data.length; i++){
                      			$(".today_match_list").append(
                                        "<div class='today_match'>"+
                                        
                		                      "<div class='today_match_home'>"+
                		                         "<div class='team_name'>"+data[i].homeTN +"</div>"+
                		                         "<div class='team_emblem'><img src="+data[i].homeTeamImageUrl+" width='30' height='30'></div>"+
                		                         "<div class='score'>"+data[i].homeResult +"</div>"+
                		                      "</div>"+
                                      
                		                      "<div class='match_time'>"+ data[i].startTime.replace(/\B(?=(\d{2})+(?!\d))/g, ':') +"</div>"+
                		                      
                		                      "<div class='today_match_away'>"+
                		                      	"<div class='score'>"+data[i].awayResult+"</div>"+
                		                         "<div class='team_emblem'><img class='home_img' src="+data[i].awayTeamImageUrl+" width='30' height='30'></div>"+ 
                		                      	"<div class='team_name'>"+data[i].awayTN+"</div>"+
                		                      "</div>"+
                                      
                                  		"</div>"
                                     );   
                      		}
            				 
            			 } /* 전체일정 if finish */
            			 else if($("option:selected", this).text() == "프리미어리그"){
            				 /* 기존에 선택되어 추가된 요소 비우기  */
                       		$(".today_match_list").empty();
            				 var cnt = 0;
                       		for (var i = 0; i < data.length; i++){
                         		if(data[i].leagueName == "프리미어리그"){
                         			cnt++;
                         				$(".today_match_list").append(
                                                "<div class='today_match'>"+
                                                
                        		                      "<div class='today_match_home'>"+
                        		                         "<div class='team_name'>"+data[i].homeTN +"</div>"+
                        		                         "<div class='team_emblem'><img src="+data[i].homeTeamImageUrl+" width='30' height='30'></div>"+
                        		                         "<div class='score'>"+data[i].homeResult +"</div>"+
                        		                      "</div>"+
                                              
                        		                      "<div class='match_time'>"+ data[i].startTime.replace(/\B(?=(\d{2})+(?!\d))/g, ':') +"</div>"+
                        		                      
                        		                      "<div class='today_match_away'>"+
                        		                      	"<div class='score'>"+data[i].awayResult+"</div>"+
                        		                         "<div class='team_emblem'><img class='home_img' src="+data[i].awayTeamImageUrl+" width='30' height='30'></div>"+ 
                        		                      	"<div class='team_name'>"+data[i].awayTN+"</div>"+
                        		                      "</div>"+
                                              
                                          		"</div>"
                                             );    
                         		
                         		}
                         		}
                       		if(cnt == 0){
                          		 $(".today_match_list").append(
                           				"<div class='today_match' style='border: none; margin-top: 150px;'>"+
                           				
                           					"오늘은 경기가 없습니다."+
                           				"</div>"
                           		 );    
                        		}
            			 }
            			 else if($("option:selected", this).text() == "라리가"){
            				 /* 기존에 선택되어 추가된 요소 비우기  */
                        		$(".today_match_list").empty();
                        		var cnt = 0;
                        		for (var i = 0; i < data.length; i++){
                          		if(data[i].leagueName == "라리가"){
                          			cnt++;
                          			$(".today_match_list").append(
                                             "<div class='today_match'>"+
                                             
                     		                      "<div class='today_match_home'>"+
                     		                         "<div class='team_name'>"+data[i].homeTN +"</div>"+
                     		                         "<div class='team_emblem'><img src="+data[i].homeTeamImageUrl+" width='30' height='30'></div>"+
                     		                         "<div class='score'>"+data[i].homeResult +"</div>"+
                     		                      "</div>"+
                                           
                     		                      "<div class='match_time'>"+ data[i].startTime.replace(/\B(?=(\d{2})+(?!\d))/g, ':') +"</div>"+
                     		                      
                     		                      "<div class='today_match_away'>"+
                     		                      	"<div class='score'>"+data[i].awayResult+"</div>"+
                     		                         "<div class='team_emblem'><img class='home_img' src="+data[i].awayTeamImageUrl+" width='30' height='30'></div>"+ 
                     		                      	"<div class='team_name'>"+data[i].awayTN+"</div>"+
                     		                      "</div>"+
                                           
                                       		"</div>"
                                          );    
                          		}
                          		}
                        		if(cnt == 0){
                             		 $(".today_match_list").append(
                              				"<div class='today_match' style='border: none; margin-top: 150px;'>"+
                              				
                              					"오늘은 경기가 없습니다."+
                              				"</div>"
                              		 );    
                           		}
            			 }
            			 else if($("option:selected", this).text() == "분데스리가"){
            				 /* 기존에 선택되어 추가된 요소 비우기  */
                     		$(".today_match_list").empty();
                     		var cnt = 0;
                     		for (var i = 0; i < data.length; i++){
                       		if(data[i].leagueName == "분데스리가"){
                       			cnt++;
                       			$(".today_match_list").append(
                                          "<div class='today_match'>"+
                                          
                  		                      "<div class='today_match_home'>"+
                  		                         "<div class='team_name'>"+data[i].homeTN +"</div>"+
                  		                         "<div class='team_emblem'><img src="+data[i].homeTeamImageUrl+" width='30' height='30'></div>"+
                  		                         "<div class='score'>"+data[i].homeResult +"</div>"+
                  		                      "</div>"+
                                        
                  		                      "<div class='match_time'>"+ data[i].startTime.replace(/\B(?=(\d{2})+(?!\d))/g, ':') +"</div>"+
                  		                      
                  		                      "<div class='today_match_away'>"+
                  		                      	"<div class='score'>"+data[i].awayResult+"</div>"+
                  		                         "<div class='team_emblem'><img class='home_img' src="+data[i].awayTeamImageUrl+" width='30' height='30'></div>"+ 
                  		                      	"<div class='team_name'>"+data[i].awayTN+"</div>"+
                  		                      "</div>"+
                                        
                                    		"</div>"
                                       );    
                       		}
                       		}
                     		if(cnt == 0){
                         		 $(".today_match_list").append(
                          				"<div class='today_match' style='border: none; margin-top: 150px;'>"+
                          				
                          					"오늘은 경기가 없습니다."+
                          				"</div>"
                          		 );    
                       		}
            			 }
            			 else if($("option:selected", this).text() == "세리에A"){
            				 /* 기존에 선택되어 추가된 요소 비우기  */
                      		$(".today_match_list").empty();
                      		var cnt = 0;
                      		for (var i = 0; i < data.length; i++){
                      			cnt++;
                        		if(data[i].leagueName == "세리에A"){
                        			$(".today_match_list").append(
                                           "<div class='today_match'>"+
                                           
                   		                      "<div class='today_match_home'>"+
                   		                         "<div class='team_name'>"+data[i].homeTN +"</div>"+
                   		                         "<div class='team_emblem'><img src="+data[i].homeTeamImageUrl+" width='30' height='30'></div>"+
                   		                         "<div class='score'>"+data[i].homeResult +"</div>"+
                   		                      "</div>"+
                                         
                   		                      "<div class='match_time'>"+ data[i].startTime.replace(/\B(?=(\d{2})+(?!\d))/g, ':') +"</div>"+
                   		                      
                   		                      "<div class='today_match_away'>"+
                   		                      	"<div class='score'>"+data[i].awayResult+"</div>"+
                   		                         "<div class='team_emblem'><img class='home_img' src="+data[i].awayTeamImageUrl+" width='30' height='30'></div>"+ 
                   		                      	"<div class='team_name'>"+data[i].awayTN+"</div>"+
                   		                      "</div>"+
                                         
                                     		"</div>"
                                        );    
                        		}
                        		}
                      		if(cnt == 0){
                         		 $(".today_match_list").append(
                          				"<div class='today_match' style='border: none; margin-top: 150px;'>"+
                          					"오늘은 경기가 없습니다."+
                          				"</div>"
                          		 );    
                       		}
            			 }
            			 
            		}); /* select change function finish */
            	 
            	 
            	 
            	 } else {
            		 $(".today_match_list").append(
               				"<div class='today_match' style='border: none; margin-top: 150px;'>"+
               					"오늘은 경기가 없습니다."+
               				"</div>"
               		 );    
            	 }
                	
            	 
            	 
             }
         })
         /* 오늘경기 일정 끝. */
         
         
         
         
         
         
         
         
         
         
      // Data
         var groupData = [
           {
             "name": "Epl",
             "data": [
             { "id": "GB", "name": "Epl", "href":"epl"}
               ]
           }, 
           {
             "name": "Bundesliga",
             "data": [
             { "id": "DE", "name": "Bundesliga", "href":"bundesliga"}
             ]
           }, 
           {
             "name": "Laliga",
             "data": [
             { "id": "ES", "name": "Laliga", "href":"laliga"}
             ]
           }, {
             "name": "Serie A",
             "data": [
             { "id": "IT", "name": "Serie A", "href":"seriea"}
             ]
           }
         ];


         // Create root and chart
         var root = am5.Root.new("mapdiv");


         // Set themes
         root.setThemes([
           am5themes_Animated.new(root)
         ]);


         // Create chart
         var chart = root.container.children.push(am5map.MapChart.new(root, {
           minZoomLevel: 5,
           maxZoomLevel: 5,
           homeGeoPoint: { longitude: 0, latitude: 48 }
         }));


         // Create world polygon series
         var worldSeries = chart.series.push(am5map.MapPolygonSeries.new(root, {
           geoJSON: am5geodata_worldLow,
           exclude: ["AQ"]
         }));

         worldSeries.mapPolygons.template.setAll({
           fill: am5.color(0xaaaaaa)
         });

         worldSeries.events.on("datavalidated", () => {
           chart.goHome();
         });


         // Add legend
         var legend = chart.children.push(am5.Legend.new(root, {
           useDefaultMarker: true,
           centerX: am5.p50,
           x: am5.p50,
           centerY: am5.p100,
           y: am5.p100,
           dy: -20,
           background: am5.RoundedRectangle.new(root, {
             fill: am5.color(0xffffff),
             fillOpacity: 0
           })
         }));

         legend.valueLabels.template.set("forceHidden", true)


         // Create series for each group
         var colors = am5.ColorSet.new(root, {
           step: 2
         });
         colors.next();

         am5.array.each(groupData, function(group) {
           var countries = [];
           var color = colors.next();

           am5.array.each(group.data, function(country) {
             countries.push(country.id)
           });

           var polygonSeries = chart.series.push(am5map.MapPolygonSeries.new(root, {
             geoJSON: am5geodata_worldLow,
             include: countries,
             name: group.name,
             fill: color
           }));


           polygonSeries.mapPolygons.template.setAll({
             tooltipText: "[bold]{name}[/]",
             interactive: true,
             fill: color,
             strokeWidth: 2
           });

           polygonSeries.mapPolygons.template.states.create("hover", {
             fill: am5.Color.brighten(color, -0.3)
           });

           polygonSeries.mapPolygons.template.events.on("pointerover", function(ev) {
             ev.target.series.mapPolygons.each(function(polygon) {
               polygon.states.applyAnimate("hover");
             });
           });
           
           polygonSeries.mapPolygons.template.events.on("click", function(ev) {
              var href = ev.target.dataItem.dataContext.href;
                 window.location.href = '/'+href;
            });

           polygonSeries.mapPolygons.template.events.on("pointerout", function(ev) {
             ev.target.series.mapPolygons.each(function(polygon) {
               polygon.states.applyAnimate("default");
             });
           });
           polygonSeries.data.setAll(group.data);

           legend.data.push(polygonSeries);
         });
         
         
         
         
         
         
         
         
         
         
         
         
         
         
     });
     /* 도큐먼트 레디 펑션 끝. */

     
 </script>
</body>
</html>

























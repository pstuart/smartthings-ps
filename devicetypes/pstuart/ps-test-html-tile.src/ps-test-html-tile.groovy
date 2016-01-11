/**
 *  ps Test HTML Tile
 *
 *  Copyright 2015 Patrick Stuart
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
metadata {
	definition (name: "ps Test HTML Tile", namespace: "pstuart", author: "Patrick Stuart", oauth: true) {
		capability "Button"
        /*
        capability "Configuration"
        capability "Video Camera"
        capability "Video Capture"
        */
        capability "Refresh"
	}

	simulator {
		// TODO: define status and reply messages here
	}

	tiles (scale:2) {
    /*
    	multiAttributeTile(name: "videoPlayer", type: "videoPlayer", width: 6, height: 4){
            tileAttribute("device.switch", key: "CAMERA_STATUS") {
                attributeState("on", label: "Active", icon: "st.camera.dlink-indoor", action: "switch.off", backgroundColor: "#79b821", defaultState: true)
                attributeState("off", label: "Inactive", icon: "st.camera.dlink-indoor", action: "switch.on",  backgroundColor: "#ffffff")
                attributeState("restarting", label: "Connecting", icon: "st.camera.dlink-indoor", backgroundColor: "#53a7c0")
                attributeState("unavailable", label: "Unavailable", icon: "st.camera.dlink-indoor", action: "refresh.refresh", backgroundColor: "#F22000")
            }

            tileAttribute("device.camera", key: "PRIMARY_CONTROL") {
                attributeState("on", label: "Active", icon: "st.camera.dlink-indoor", backgroundColor: "#79b821", defaultState: true)
                attributeState("off", label: "Inactive", icon: "st.camera.dlink-indoor", backgroundColor: "#ffffff")
                attributeState("restarting", label: "Connecting", icon: "st.camera.dlink-indoor", backgroundColor: "#53a7c0")
                attributeState("unavailable", label: "Unavailable", icon: "st.camera.dlink-indoor", backgroundColor: "#F22000")
            }

            tileAttribute("device.startLive", key: "START_LIVE") {
                attributeState("live", action: "start", defaultState: true)
            }

            tileAttribute("device.stream", key: "STREAM_URL") {
                attributeState("activeURL", defaultState: true)
            }

            tileAttribute("device.profile", key: "STREAM_QUALITY") {
                attributeState("1", label: "720p",  action: "setProfileHD",  defaultState: true)
                attributeState("2", label: "h360p", action: "setProfileSDH", defaultState: true)
                attributeState("3", label: "l360p", action: "setProfileSDL", defaultState: true)
            }

            tileAttribute("device.betaLogo", key: "BETA_LOGO") {
                attributeState("betaLogo", label: "", value: "", defaultState: true)
            }
        }
        */
		htmlTile(name: "tileBalloon", action: "getBalloon", width: 6, height: 1)
        standardTile("tileRefresh", "device.refresh", width: 2, height: 2, decoration: "flat") {
            state "default", label: '', action: "refresh.refresh", icon: "st.secondary.refresh"
        }
        
        htmlTile(name:"htmlTile", action:"home", width:"6", height:"3", whitelist:["code.jquery.com", "googleapis.com", "fonts.googleapis.com", "cdnjs.cloudflare.com", "weloveiconfonts.com"])

        
        main "tileRefresh"
        details([ "tileBalloon", "tileRefresh", "htmlTile"])
	}
}

mappings {
    path("/getBalloon") {
        action: [ GET: "getBalloon" ]
    }
    path("/home") {
		action: [
			GET: "home"
		]
	}
	path("/getInitialData") {
		action:[
			GET: "getInitialData"
		]
	}
	path("/getMotionStates") {
		action:[
			GET: "getMotionStates"
		]
	}
	path("/getTempStates") {
		action:[
			GET: "getTempStates"
		]
	}
	path("/getBatteryStates") {
		action:[
			GET: "getBatteryStates"
		]
	}
	path("/consoleLog") {
		action:[
			POST: "consoleLog"
		]
	}
    
    
}

def getBalloon() {
    renderHTML {
        head {
            """ <style type="text/css">
                    body {
                        background-color: #A0A0A0;
                        font-family: arial;
                    }
                     #beta {
                        background-color: #FFFFFF;
                        -moz-border-radius: 25px;
                        -webkit-border-radius: 25px;
                        padding: 1px 10px;
                        color: #A0A0A0;
                        font-family: arial;
                    }
                    .beta-div {
                        width: 20%;
                        float: left;
                        padding-top: 3%;
                        padding-left: 2%
                    }
                    .word-div {
                        width: 75%;
                        float: right;
                        margin-left: 2px;
                        font-size: 85%;
                    }
                    .full-div {
                        width: 100%;
                        display: inline;
                    }
                    .white {
                        color: #FFFFFF;
                    }
                </style> """ 
        }
        body {
            """
            <div style="full-div">
                <div class="beta-div">
                    <span id="beta">BETA</span>
                </div>
                <div class="word-div white">
                    Having difficulties seeing Live Video? Turn off Wi-Fi to switch to cellular data and try again.
                </div>
            </div>
            """ 
        }
    }
}

// parse events into attributes
/*
def parse(String description) {
	log.debug "Parsing '${description}'"
	// TODO: handle 'button' attribute

}
*/

def home() {
	renderHTML {
		head {
		"""
		<script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
		<script src="http://cdnjs.cloudflare.com/ajax/libs/raphael/2.1.0/raphael-min.js"></script>
		<script src="http://cdnjs.cloudflare.com/ajax/libs/morris.js/0.5.1/morris.min.js"></script>
		<style type="text/css">
			@import "http://fonts.googleapis.com/css?family=Open+Sans:400,300,700";
			@import "http://weloveiconfonts.com/api/?family=entypo";
			@import "http://weloveiconfonts.com/api/?family=fontawesome";
			[class*="entypo-"]:before {
				font-family: 'entypo', sans-serif;
			}
			[class*="fontawesome-"]:before {
				font-family: 'FontAwesome', sans-serif;
			}
			* {
				-webkit-tap-highlight-color: rgba(255, 255, 255, 0) !important; 
				-webkit-focus-ring-color: rgba(255, 255, 255, 0) !important; 
				outline: none !important;
			}
			@-webkit-keyframes rotate {
			    from {
			        -webkit-transform: rotate(0deg);
			    }
			    to { 
			        -webkit-transform: rotate(360deg);
			    }
			}
			body {
				margin: 0 auto;
				width: 100%;
				height: 100%;
				color: #eee;
				font-family: 'Open Sans', sans-serif;
				background-color: #1F222A;
				overflow: hidden;
			}
			.wrapper {
				width: 100%;
				height: 100%;
				margin: 0 auto;
				background-color: #1F222A;
			}
			nav {
				margin: 5%;
				width: 100%;
				height: 5%;
				position: absolute;
				bottom: 2.5%;
				left: 0;
				right: 0;
			}
			#menu {
				padding: 5%;
				width: 100%;
				height: 5%;
				min-height: 5%;
				line-height: 5%;
				position: fixed;
				bottom: 0;
				left: 0;
				right: 0;
				background-color: #000000;
				opacity: .30;
				filter: Alpha(Opacity=30);
			}
			nav ul {
				width: 100%;
				height: 100%;
				display: block;
				z-index: 10;
				text-align: center;
				padding: 0px;
				margin: 0 auto;
			}
			nav ul li {
				position: relative;
				display: inline-block;
				list-style-type: none;
				vertical-align: middle;
			  	height: 100%;
			  	width: 17.5%;
			  	float: left;
			  	text-align: center;
			  	padding: 0px;
			  	margin: 0 auto;
			}
			nav ul li a {
			  	color: #FFFFFF;
			  	margin: 0 auto;
			  	font-size: 2.7em;
			  	text-align: center;
			  	width: 17.5%;
			  	padding: 0px;
			  	height: 100%;
			  	letter-spacing: -1px;
			  	text-decoration: none !important;
			  	font-weight: normal !important;
			  	opacity: .50;
				filter: Alpha(Opacity=50);
			}
			nav ul li a.active {
				opacity: 1.0;
				filter: Alpha(Opacity=100);
			}
			section {
				width: 100%;
				height: 100%;
				box-sizing: border-box;
				padding: 0px;
				text-align: center;
				position: absolute;
				left: 0;
				right: 0;
				top: 0;
				bottom: 0;
				background: #092756;
				background: -moz-radial-gradient(0% 100%, ellipse cover, rgba(104,128,138,.4) 10%,rgba(138,114,76,0) 40%),-moz-linear-gradient(top, rgba(57,173,219,.25) 0%, rgba(42,60,87,.4) 100%), -moz-linear-gradient(-45deg, #670d10 0%, #092756 100%);
				background: -webkit-radial-gradient(0% 100%, ellipse cover, rgba(104,128,138,.4) 10%,rgba(138,114,76,0) 40%), -webkit-linear-gradient(top, rgba(57,173,219,.25) 0%,rgba(42,60,87,.4) 100%), -webkit-linear-gradient(-45deg, #670d10 0%,#092756 100%);
				background: -o-radial-gradient(0% 100%, ellipse cover, rgba(104,128,138,.4) 10%,rgba(138,114,76,0) 40%), -o-linear-gradient(top, rgba(57,173,219,.25) 0%,rgba(42,60,87,.4) 100%), -o-linear-gradient(-45deg, #670d10 0%,#092756 100%);
				background: -ms-radial-gradient(0% 100%, ellipse cover, rgba(104,128,138,.4) 10%,rgba(138,114,76,0) 40%), -ms-linear-gradient(top, rgba(57,173,219,.25) 0%,rgba(42,60,87,.4) 100%), -ms-linear-gradient(-45deg, #670d10 0%,#092756 100%);
				background: -webkit-radial-gradient(0% 100%, ellipse cover, rgba(104,128,138,.4) 10%,rgba(138,114,76,0) 40%), linear-gradient(to bottom, rgba(57,173,219,.25) 0%,rgba(42,60,87,.4) 100%), linear-gradient(135deg, #670d10 0%,#092756 100%);
			}
			#header {
				width: 100%;
				height: 5%;
				position: fixed;
				left: 0;
				right: 0;
				top: 0;
				padding: 5%;
				background-color: #000000;
				opacity: .30;
				filter: Alpha(Opacity=30);
			}
			#motionTitle, #tempTitle, #batteryTitle, #eventTitle {
				text-align: left;
				float: left;
				display: inline-block;
				width: 100%;
				height: 100%;
				letter-spacing: 1px;
				font-size: 1.2em;
				font-weight: 100;
				padding: 0px;
				margin: 0 auto;
			}
			.tempIcon, .motionIcon, .batteryIcon, .eventIcon {
				width: 100%;
				height: 100%;
				margin-top: 15%;
				float: left;
				text-shadow: 0px 1px 0px rgb(204, 204, 204), 0px 2px 0px rgb(201, 201, 201), 0px 3px 0px rgb(187, 187, 187), 0px 4px 0px rgb(185, 185, 185), 0px 5px 0px rgb(170, 170, 170), 0px 6px 1px rgba(0, 0, 0, 0.1), 0px 0px 5px rgba(0, 0, 0, 0.1), 0px 1px 3px rgba(0, 0, 0, 0.3), 0px 3px 5px rgba(0, 0, 0, 0.2), 0px 5px 10px rgba(0, 0, 0, 0.25), 0px 20px 20px rgba(0, 0, 0, 0.15);
				color: #FFFFFF;
				letter-spacing: -0.02em;
				text-transform: uppercase;
				font-size: 9.0em;
				text-align: center;
			}
			#motionStatus, #tempStatus, #batteryStatus  {
				display: block;
				font-size: 0.9em;
				color: #FFFFFF;
				letter-spacing: 0.02em;
				text-transform: uppercase;
				text-align: center;
				text-shadow: 0px 6px 1px rgba(0, 0, 0, 0.1), 0px 0px 5px rgba(0, 0, 0, 0.1), 0px 1px 3px rgba(0, 0, 0, 0.3), 0px 3px 5px rgba(0, 0, 0, 0.2), 0px 5px 10px rgba(0, 0, 0, 0.25) !important;
			}
			.on {
				color: yellow !important;
				text-shadow: 0px 1px 0px rgb(255, 255, 102), 0px 2px 0px rgb(255, 255, 102), 0px 3px 0px rgb(255, 222, 0), 0px 4px 0px rgb(255, 222, 0), 0px 5px 0px rgb(255, 222, 0), 0px 6px 1px rgba(0, 0, 0, 0.1), 0px 0px 5px rgba(0, 0, 0, 0.1), 0px 1px 3px rgba(0, 0, 0, 0.3), 0px 3px 5px rgba(0, 0, 0, 0.2), 0px 5px 10px rgba(0, 0, 0, 0.25), 0px 20px 20px rgba(0, 0, 0, 0.15);
			}
			.off {
				color: #FFFFFF;
			}
			.rotate {
				font-weight: normal;
				-webkit-animation-name: rotate; 
				-webkit-animation-duration: 2.0s; 
				-webkit-animation-iteration-count: infinite;
				-webkit-animation-timing-function: linear;
			}
			section div ul {
				width: 100%;
				display: block;
				float: left;
				border-top: 1px solid #3D414C;
				margin-top: 15px;
				padding-top: 20px;
				padding-bottom: 70px;
				background-color: #1F222A;
			}
			section div ul li {
				display: block;
				float: left;
				padding-right: 10%;
				color: #3D414C;
				font-size: 2.2em;
			}
			section div ul li span {
				position: absolute;
				display: inline;
				color: #60646e;
				font-size: 0.4em;
				letter-spacing: 1px;
				font-weight: 300;
				line-height:70px;
				height:70px;
			}
			section div a {
				color: #60646e;
				font-size: 1.2em;
				text-decoration: none;
			}
			section div a:hover, section div ul li span:hover {
				color: #eee;
			}
			#pastMotionChart1, #pastMotionChart2, #pastMotionChart3, #pastTempChart1, #pastTempChart2, #pastTempChart3, #pastBatteryChart1, #pastBatteryChart2, #pastBatteryChart3, #eventStatus {
				color: #FFFFFF;
				position: absolute;
				width: auto;
				right: 0;
				left: 0;
				top: 12.5%;
				bottom: 15%;
				padding: 5%;
			}
			#motionViews, #tempViews, #batteryViews, #eventViews {
				margin: 5%;
				width: 100%;
				position: absolute;
				top: 0;
				right: 0;
				height: 5%;
				color: #ffffff;
				font-size: 1.0em;
				font-weight: 100;
				z-index: 20;
			}
			#motionHeader, #tempHeader, #batteryHeader, #eventHeader {
				margin: 5%;
				position: absolute;
				top: 0;
				left: 0;
				height: 5%;
				width: 100%;
				color: #ffffff;
				font-size: 1.0em;
				font-weight: 100;
			}
			.morris-hover {
				position: absolute;
			}
			.morris-hover.morris-default-style {
				border-radius: 1px;
				padding: 3px;
				color: transparent;
				background: #1F222A;
				border: none;
				text-align: center;
			}
			.morris-hover.morris-default-style .morris-hover-row-label {
				margin: 0.25em 0;
			}
			.morris-hover.morris-default-style .morris-hover-point {
				white-space: nowrap;
				margin: 0.1em 0;
			}
			#tempTrendWeekBtn, #tempTrendDayBtn, #tempTrendHourBtn, #tempNowBtn, #motionTrendWeekBtn, #motionTrendDayBtn, #motionTrendHourBtn, #motionNowBtn, #batteryTrendWeekBtn, #batteryTrendDayBtn, #batteryTrendHourBtn, #batteryNowBtn {
				opacity: .50;
				filter: Alpha(Opacity=50);
				color: #FFFFFF;
				height: 100%;
				padding-left: 5%;
				text-align: right;
				float: right;
				z-index: 20;
				display: inline-block;
				letter-spacing: 1px;
			}
			.eventfix {
				font-size: 0.5em;
				color:eee;
				text-align:left;
				padding-left:10%;
				overflow:hidden;
			}
			#tempBubble, #batteryBubble {
			    position: absolute;
			    bottom: -10px;
			    right: -1px;
			    background-color: #666666;
			    color: #FFFFFF;
			    border-radius: 5px;
			    padding: 4px;
			    padding-top: 1px;
			    padding-bottom: 1px;
			    font: 14px Verdana;
			    font-weight: bold;
			}
			.overlay-fetch {
				background-color: rgba(0, 0, 0, 0.3);
				color: #000;
				min-height: 100%;
				min-width: 100%;
				position: absolute;
				width: 100%;
				z-index: 2000;
				height: 100%;
				top: 0;
				bottom: 0;
				left: 0;
				right: 0;
				display: block;
			}
			.chart, #temp, #battery, #event {
				display: none;
			}
			.chartTitle {
				text-align: left;
				color: #eee;
				font-size: 0.9em;
			}
		</style>
		<script>
			function motion(evt) {
				APP.eventReceiver(evt);
			}
			function temperature(evt) {
				APP.eventReceiver(evt);
			}
			function battery(evt) {
				APP.eventReceiver(evt);
			}
		</script>
		"""
		}
		body {
		"""
		<div class="wrapper">
		  	<section>
		  		<div id="header"></div>
		  		<div id="motion">
		  			<div id="motionHeader">
		  				<h1 id="motionTitle">Motion Sensor</h1>
		  			</div>
					<div id="motionViews">
						<div id="motionTrendWeekBtn">week</div>
						<div id="motionTrendDayBtn">day</div>
						<div id="motionTrendHourBtn">hour</div>
					</div>
					<h2 id="motionPane">
						<div class="motionIcon fontawesome-refresh rotate" id="motionLight"></div>
						<span id="motionStatus">REFRESHING DATA</span>
					</h2>
					<div id="motionTrends">
						<div id="pastMotionChart3" class="chart"></div>
						<div id="pastMotionChart2" class="chart"></div>
						<div id="pastMotionChart1" class="chart"></div>
					</div>
				</div>
				<div id="temp">
					<div id="tempHeader">
						<h1 id="tempTitle">Temperature</h1>
					</div>
					<div id="tempViews">
						<div id="tempTrendWeekBtn">week</div>
						<div id="tempTrendDayBtn">day</div>
						<div id="tempTrendHourBtn">hour</div>
					</div>
					<h2 id="tempPane">
						<div class="tempIcon">0</div>
						<span id="tempStatus">CURRENT TEMP (°F)</span>
					</h2>
					<div id="tempTrends">
						<div id="pastTempChart3" class="chart"></div>
						<div id="pastTempChart2" class="chart"></div>
						<div id="pastTempChart1" class="chart"></div>
					</div>
				</div>
				<div id="battery">
					<div id="batteryHeader">
						<h1 id="batteryTitle">Battery Charge</h1>
					</div>
					<div id="batteryViews">
						<div id="batteryTrendWeekBtn">week</div>
						<div id="batteryTrendDayBtn">day</div>
						<div id="batteryTrendHourBtn">hour</div>
					</div>
					<h2 id="batteryPane">
						<div class="batteryIcon">0</div>
						<span id="batteryStatus">CURRENT CHARGE (%)</span>
					</h2>
					<div id="batteryTrends">
						<div id="pastBatteryChart3" class="chart"></div>
						<div id="pastBatteryChart2" class="chart"></div>
						<div id="pastBatteryChart1" class="chart"></div>
					</div>
				</div>
				<div id="event">
					<div id="eventHeader">
						<h1 id="eventTitle">Device Events</h1>
					</div>
					<div id="eventViews"></div>
					<h2 id="eventPane">
						<span id="eventStatus" class="eventfix"></span>
					</h2>
				</div>
				<div id="menu"></div>
		  	</section>
		  	<nav role="navigation">
		  		<ul>
		  			<li><a href="#" class="fontawesome-home" id="motionBtn"></a></li>
		  			<li><a href="#" class="fontawesome-cloud" id="tempBtn"></a><span id="tempBubble">0°F</span></li>
		  			<li><a href="#" class="fontawesome-bolt" id="batteryBtn"></a><span id="batteryBubble">0%</span></li>
		  			<li><a href="#" class="fontawesome-list-ul" id="eventBtn"></a></li>
		  			<li><a href="#" class="fontawesome-refresh" id="refreshBtn"></a></li>
		  		</ul>
		  	</nav>
		  	<div class="overlay-fetch"></div>
		</div>
		<script>
			var context = "motion";
			var savedTemp = "";
			var savedBattery= "";
			var pastMotionChartData1 = [];
			var pastMotionChartData2 = [];
			var pastMotionChartData3 = [];
			var tempMinutes = [];
			var tempHours = [];
			var tempDays = [];
			var pastTempChartData1 = [];
			var pastTempChartData2 = [];
			var pastTempChartData3 = [];
			var batteryMinutes = [];
			var batteryHours = [];
			var batteryDays = [];
			var pastBatteryChartData1 = [];
			var pastBatteryChartData2 = [];
			var pastBatteryChartData3 = [];
			var APP = {
				init: function() {
					document.addEventListener("touchstart", function() {}, true);
					ST.request("getInitialData").success(function(data) {
						\$("#motionLight").removeClass("fontawesome-refresh");
						\$("#motionLight").removeClass("rotate");
						\$("#motionLight").addClass("entypo-light-up");
						\$("#motionStatus").text("MOTION INACTIVE");
						\$("#refreshBtn").removeClass("active");
						\$("#motionBtn").addClass("active");
						\$(".overlay-fetch").fadeOut(100, "linear");
						savedTemp = data.temp;
						savedBattery = data.battery;
						\$(".tempIcon").text(savedTemp);
						\$(".batteryIcon").text(savedBattery);
						\$("#tempBubble").text(savedTemp+"°F");
						\$("#batteryBubble").text(savedBattery+"%");
						APP.render(data.currentState);
						APP.chartBuilder(data);
						APP.addBindings();
					}).GET();
				},
				render: function(state) {
					switch(state) {
						case "active":
							\$("#motionLight").removeClass("off");
							\$("#motionLight").addClass("on");
							\$("#motionStatus").removeClass("off");
							\$("#motionStatus").text("MOTION ACTIVE");
							\$("#motionStatus").addClass("on");
							break;
						case "inactive":
							\$("#motionLight").removeClass("on");
							\$("#motionLight").addClass("off");
							\$("#motionStatus").removeClass("on");
							\$("#motionStatus").text("MOTION INACTIVE");
							\$("#motionStatus").addClass("off");
							break;
					}
				},
				addBindings: function() {
					\$("#refreshBtn").on("touchstart", function(e) {
						e.preventDefault();
						\$('.chart').each(function() {
							\$(this).fadeOut(100);
						});
						\$("#motionPane").fadeIn(100);
						\$("#"+context).fadeOut(100, "linear", function() {
							\$("#motion").fadeIn(100, "linear", function() {
								\$("#"+context+"Btn").removeClass("active");
								\$("#refreshBtn").addClass("active");
								context = "motion";
							});
						});
						\$(".overlay-fetch").fadeIn(100, "linear", function() {
							\$("#motionLight").removeClass("on");
							\$("#motionLight").addClass("off");
							\$("#motionStatus").removeClass("on");
							\$("#motionStatus").addClass("off");
							\$("#motionLight").removeClass("entypo-light-up");
							\$("#motionLight").addClass("fontawesome-refresh");
							\$("#motionLight").addClass("rotate");
							\$("#motionStatus").text("REFRESHING DATA");
							ST.action("refresh");
							ST.request("getInitialData").success(function(data) {
								savedTemp = data.temp;
								savedBattery = data.battery;
								\$(".tempIcon").text(data.temp);
								\$(".batteryIcon").text(data.battery);
								\$("#tempBubble").text(data.temp+"°F");
								\$("#batteryBubble").text(data.battery+"%");
								\$("#motionLight").removeClass("rotate");
								\$("#motionLight").removeClass("fontawesome-refresh");
								\$("#motionLight").addClass("entypo-light-up");
								\$("#motionStatus").text("MOTION INACTIVE");
								APP.render(data.currentState);
								APP.chartBuilder(data);
								\$("#refreshBtn").removeClass("active");
								\$("#motionBtn").addClass("active");
								\$(".overlay-fetch").fadeOut(100, "linear");
							}).GET();
						});
					 });
					\$("#motionBtn").on("touchstart", function(e) {
						e.preventDefault();
						\$('.chart').each(function() {
							\$(this).fadeOut(100);
						});
						\$("#motionPane").fadeIn(100);
						\$("#"+context).fadeOut(100, "linear", function() {
							\$("#motion").fadeIn(100, "linear", function() {
								\$("#"+context+"Btn").removeClass("active");
								\$("#motionBtn").addClass("active");
								context = "motion";
							});
						});
					 });
					\$("#tempBtn, #tempBubble").on("touchstart", function(e) {
						e.preventDefault();
						\$('.chart').each(function() {
							\$(this).fadeOut(100);
						});
						\$("#tempPane").fadeIn(100);
						\$("#"+context).fadeOut(100, "linear", function() {
							\$("#temp").fadeIn(100, "linear", function() {
								\$("#"+context+"Btn").removeClass("active");
								\$("#tempBtn").addClass("active");
								context = "temp";
							});
						});
					 });
					\$("#batteryBtn, #batteryBubble").on("touchstart", function(e) {
						e.preventDefault();
						\$('.chart').each(function() {
							\$(this).fadeOut(100);
						});
						\$("#batteryPane").fadeIn(100);
						\$("#"+context).fadeOut(100, "linear", function() {
							\$("#battery").fadeIn(100, "linear", function() {
								\$("#"+context+"Btn").removeClass("active");
								\$("#batteryBtn").addClass("active");
								context = "battery";
							});
						});
					 });
					\$("#eventBtn").on("touchstart", function(e) {
						e.preventDefault();
						\$('.chart').each(function() {
							\$(this).fadeOut(100);
						});
						\$("#"+context).fadeOut(100, "linear", function() {
							\$("#event").fadeIn(100, "linear", function() {
								\$("#"+context+"Btn").removeClass("active");
								\$("#eventBtn").addClass("active");
								context = "event";
							});
						});
					 });
					\$("#motionTrendWeekBtn").on("touchstart", function(e) {
						e.preventDefault();
						\$("#pastMotionChart1").fadeOut(100);
						\$("#pastMotionChart2").fadeOut(100);
						\$("#pastMotionChart3").html("");
						\$("#"+context+"Pane").fadeOut(100, "linear", function() {
							\$("#pastMotionChart3").fadeIn(100, "linear", function() {
								if (pastMotionChartData3.length > 0) {
									Morris.Donut({
										element: 'pastMotionChart3',
										data: pastMotionChartData3,
										labelColor: '#FFF',
										padding: 5,
										resize: true
									});
								} else {
									\$("#pastMotionChart3").prepend("<div class='chartTitle'>No data available for this view</div>");
								}
							});
						});
					});
					\$("#motionTrendDayBtn").on("touchstart", function(e) {
						e.preventDefault();
						\$("#pastMotionChart1").fadeOut(100);
						\$("#pastMotionChart2").html("");
						\$("#pastMotionChart3").fadeOut(100);
						\$("#"+context+"Pane").fadeOut(100, "linear", function() {
							\$("#pastMotionChart2").fadeIn(100, "linear", function() {
								if (pastMotionChartData2.length > 0) {
									Morris.Donut({
										element: 'pastMotionChart2',
										data: pastMotionChartData2,
										labelColor: '#FFF',
										padding: 5,
										resize: true
									});
								} else {
									\$("#pastMotionChart2").prepend("<div class='chartTitle'>No data available for this view</div>");
								}
							});
						});
					});
					\$("#motionTrendHourBtn").on("touchstart", function(e) {
						e.preventDefault();
						\$("#pastMotionChart1").html("");
						\$("#pastMotionChart2").fadeOut(100);
						\$("#pastMotionChart3").fadeOut(100);
						\$("#"+context+"Pane").fadeOut(100, "linear", function() {
							\$("#pastMotionChart1").fadeIn(100, "linear", function() {
								if (pastMotionChartData1.length > 0) {
									Morris.Donut({
										element: 'pastMotionChart1',
										data: pastMotionChartData1,
										labelColor: '#FFF',
										padding: 5,
										resize: true
									});
								} else {
									\$("#pastMotionChart1").prepend("<div class='chartTitle'>No data available for this view</div>");
								}
							});
						});
					});
					\$("#tempTrendWeekBtn").on("touchstart", function(e) {
						e.preventDefault();
						\$("#pastTempChart1").fadeOut(100);
						\$("#pastTempChart2").fadeOut(100);
						\$("#pastTempChart3").html("");
						\$("#"+context+"Pane").fadeOut(100, "linear", function() {
							\$("#pastTempChart3").fadeIn(100, "linear", function() {
								if (pastTempChartData3.length > 0) {
									Morris.Line({
										element: 'pastTempChart3',
										data: pastTempChartData3,
										xkey: 'time',
										ykeys: ['value'],
										labels: ['Temperature'],
										xLabels: 'day',
										padding: 5,
										resize: true,
										postUnits: '°F',
										hideHover: 'always',
										gridTextColor: '#eee'
									});
								} else {
									\$("#pastTempChart3").prepend("<div class='chartTitle'>No data available for this view</div>");
								}
							});
						});
					});
					\$("#tempTrendDayBtn").on("touchstart", function(e) {
						e.preventDefault();
						\$("#pastTempChart1").fadeOut(100);
						\$("#pastTempChart2").html("");
						\$("#pastTempChart3").fadeOut(100);
						\$("#"+context+"Pane").fadeOut(100, "linear", function() {
							\$("#pastTempChart2").fadeIn(100, "linear", function() {
								if (pastTempChartData2.length > 0) {
									Morris.Line({
										element: 'pastTempChart2',
										data: pastTempChartData2,
										xkey: 'time',
										ykeys: ['value'],
										labels: ['Temperature'],
										xLabels: 'hour',
										padding: 5,
										resize: true,
										postUnits: '°F',
										hideHover: 'always',
										gridTextColor: '#eee'
									});
								} else {
									\$("#pastTempChart2").prepend("<div class='chartTitle'>No data available for this view</div>");
								}
							});
						});
					});
					\$("#tempTrendHourBtn").on("touchstart", function(e) {
						e.preventDefault();
						\$("#pastTempChart1").html("");
						\$("#pastTempChart2").fadeOut(100);
						\$("#pastTempChart3").fadeOut(100);
						\$("#"+context+"Pane").fadeOut(100, "linear", function() {
							\$("#pastTempChart1").fadeIn(100, "linear", function() {
								if (pastTempChartData1.length > 0) {
									Morris.Line({
										element: 'pastTempChart1',
										data: pastTempChartData1,
										xkey: 'time',
										ykeys: ['value'],
										labels: ['Temperature'],
										xLabels: 'minute',
										padding: 5,
										resize: true,
										postUnits: '°F',
										hideHover: 'always',
										gridTextColor: '#eee'
									});
								} else {
									\$("#pastTempChart1").prepend("<div class='chartTitle'>No data available for this view</div>");
								}
							});
						});
					});
					\$("#batteryTrendWeekBtn").on("touchstart", function(e) {
						e.preventDefault();
						\$("#pastBatteryChart1").fadeOut(100);
						\$("#pastBatteryChart2").fadeOut(100);
						\$("#pastBatteryChart3").html("");
						\$("#"+context+"Pane").fadeOut(100, "linear", function() {
							\$("#pastBatteryChart3").fadeIn(100, "linear", function() {
								if (pastBatteryChartData3.length > 0) {
									Morris.Line({
										element: 'pastBatteryChart3',
										data: pastBatteryChartData3,
										xkey: 'time',
										ykeys: ['value'],
										labels: ['Charge'],
										xLabels: 'day',
										padding: 5,
										resize: true,
										postUnits: '%',
										hideHover: 'always',
										gridTextColor: '#eee'
									});
								} else {
									\$("#pastBatteryChart3").prepend("<div class='chartTitle'>No data available for this view</div>");
								}
							});
						});
					});
					\$("#batteryTrendDayBtn").on("touchstart", function(e) {
						e.preventDefault();
						\$("#pastBatteryChart1").fadeOut(100);
						\$("#pastBatteryChart2").html("");
						\$("#pastBatteryChart3").fadeOut(100);
						\$("#"+context+"Pane").fadeOut(100, "linear", function() {
							\$("#pastBatteryChart2").fadeIn(100, "linear", function() {
								if (pastBatteryChartData2.length > 0) {
									Morris.Line({
										element: 'pastBatteryChart2',
										data: pastBatteryChartData2,
										xkey: 'time',
										ykeys: ['value'],
										labels: ['Charge'],
										xLabels: 'hour',
										padding: 5,
										resize: true,
										postUnits: '%',
										hideHover: 'always',
										gridTextColor: '#eee'
									});
								} else {
									\$("#pastBatteryChart2").prepend("<div class='chartTitle'>No data available for this view</div>");
								}
							});
						});
					});
					\$("#batteryTrendHourBtn").on("touchstart", function(e) {
						e.preventDefault();
						\$("#pastBatteryChart1").html("");
						\$("#pastBatteryChart2").fadeOut(100);
						\$("#pastBatteryChart3").fadeOut(100);
						\$("#"+context+"Pane").fadeOut(100, "linear", function() {
							\$("#pastBatteryChart1").fadeIn(100, "linear", function() {
								if (pastBatteryChartData1.length > 0) {
									Morris.Line({
										element: 'pastBatteryChart1',
										data: pastBatteryChartData1,
										xkey: 'time',
										ykeys: ['value'],
										labels: ['Charge'],
										xLabels: 'minute',
										padding: 5,
										resize: true,
										postUnits: '%',
										hideHover: 'always',
										gridTextColor: '#eee'
									});
								} else {
									\$("#pastBatteryChart1").prepend("<div class='chartTitle'>No data available for this view</div>");
								}
							});
						});
					});
				},
				chartBuilder: function(data) {
					switch(data.pastMotion1) {
						default:
							pastMotionChartData1 = data.pastMotion1;
					}
					switch(data.pastMotion2) {
						default:
							pastMotionChartData2 = data.pastMotion2;
					}
					switch(data.pastMotion3) {
						default:
							pastMotionChartData3 = data.pastMotion3;
					}
					switch(data.pastTemp1) {
						default:
							pastTempChartData1 = data.pastTemp1;
					}
					switch(data.pastTemp2) {
						default:
							pastTempChartData2 = data.pastTemp2;
					}
					switch(data.pastTemp3) {
						default:
							pastTempChartData3 = data.pastTemp3;
					}
					switch(data.pastBattery1) {
						default:
							pastBatteryChartData1 = data.pastBattery1;
					}
					switch(data.pastBattery2) {
						default:
							pastBatteryChartData2 = data.pastBattery2;
					}
					switch(data.pastBattery3) {
						default:
							pastBatteryChartData3 = data.pastBattery3;
					}
				},
				eventReceiver: function(evt) {
					switch(evt.name) {
						case "motion":
							date = new Date();
							if (evt.value == "active" || evt.value == "inactive") {
								APP.render(evt.value);
								\$("#eventStatus").prepend("Motion state is now "+evt.value+" - "+date.toLocaleTimeString()+"<br />");
							}
							break;
						case "temperature":
							date = new Date();
							savedTemp = evt.value;
							\$(".tempIcon").text(evt.value);
							\$("#tempBubble").text(evt.value+"°F");
							\$("#eventStatus").prepend("Temperature is now "+evt.value+"°F - "+date.toLocaleTimeString()+"<br />");
							break;
						case "battery":
							date = new Date();
							savedBattery = evt.value;
							\$(".batteryIcon").text(evt.value);
							\$("#batteryBubble").text(evt.value+"%");
							\$("#eventStatus").prepend("Battery power is now "+evt.value+"% - "+date.toLocaleTimeString()+"<br />");
							break;
					}
				},
				log: function(str) {
					if (typeof(str) === 'object') {
						str = JSON.stringify(str);
					}
					ST.request("consoleLog").data({str: str}).POST();
				}
			}
			\$(document).ready(function() {
				APP.init();
			});
		</script>
		"""
		}
	}
}

def getInitialData() {
	def date = new Date()
	def mActive = 0
	def mInactive = 0
	def dActive = 0
	def dInactive = 0
	def wActive = 0
	def wInactive = 0
	def pastTempMap = [:]
	def pastBatteryMap = [:]
	def pastMotionData1 = []
	def pastMotionData2 = []
	def pastMotionData3 = []
	def pastTempData1 = []
	def pastTempData2 = []
	def pastTempData3 = []
	def pastBatteryData1 = []
	def pastBatteryData2 = []
	def pastBatteryData3 = []
	def pastMotionStates = device.statesBetween("motion", date - 7, date, [max: 1000])
	def pastTempStates = device.statesBetween("temperature", date - 7, date, [max: 1000])
	def pastBatteryStates = device.statesBetween("battery", date - 7, date, [max: 1000])
	pastMotionStates.each { motionState ->
		if (motionState.value == "active" || motionState.value == "inactive") {
			if (motionState.date.getAt(Calendar.HOUR_OF_DAY) == date.getAt(Calendar.HOUR_OF_DAY) && motionState.date.getAt(Calendar.DAY_OF_MONTH) == date.getAt(Calendar.DAY_OF_MONTH)) {
				if (motionState.value == "active") {
					mActive = mActive + 1
				} else {
					mInactive = mInactive + 1
				}
			}
			else if (motionState.date.getAt(Calendar.HOUR_OF_DAY) != date.getAt(Calendar.HOUR_OF_DAY) && motionState.date.getAt(Calendar.DAY_OF_MONTH) == date.getAt(Calendar.DAY_OF_MONTH)) {
				if (motionState.value == "active") {
					dActive = dActive + 1
				} else {
					dInactive = dInactive + 1
				}
			}
			else if (motionState.date.getAt(Calendar.DAY_OF_MONTH) <= date.getAt(Calendar.DAY_OF_MONTH)) {
				if (motionState.value == "active") {
					wActive = wActive + 1
				} else {
					wInactive = wInactive + 1
				}
			}
		}
	}
	pastMotionData1.push([label: "Active", value: mActive])
	pastMotionData1.push([label: "Inactive", value: mInactive])
	pastMotionData2.push([label: "Active", value: dActive])
	pastMotionData2.push([label: "Inactive", value: dInactive])
	pastMotionData3.push([label: "Active", value: wActive])
	pastMotionData3.push([label: "Inactive", value: wInactive])
	log.debug "pastMotionData1: ${pastMotionData1}, pastMotionData2: ${pastMotionData2}, pastMotionData3: ${pastMotionData3}"
	pastTempStates.each { tempState ->
		if (tempState.date.getAt(Calendar.HOUR_OF_DAY) == date.getAt(Calendar.HOUR_OF_DAY) && tempState.date.getAt(Calendar.DAY_OF_MONTH) == date.getAt(Calendar.DAY_OF_MONTH)) {
			if (pastTempMap.containsKey("m${tempState.date.getAt(Calendar.MINUTE)}") == false) {
				pastTempData1.push([time: tempState.isoDate, value: tempState.value])
				pastTempMap.put("m${tempState.date.getAt(Calendar.MINUTE)}", tempState.value)
			}
		}
		else if (tempState.date.getAt(Calendar.HOUR_OF_DAY) != date.getAt(Calendar.HOUR_OF_DAY) && tempState.date.getAt(Calendar.DAY_OF_MONTH) == date.getAt(Calendar.DAY_OF_MONTH)) {
			if (pastTempMap.containsKey("h${tempState.date.getAt(Calendar.HOUR_OF_DAY)}") == false) {
				pastTempData2.push([time: tempState.isoDate, value: tempState.value])
				pastTempMap.put("h${tempState.date.getAt(Calendar.HOUR_OF_DAY)}", tempState.value)
			}
		}
		else if (tempState.date.getAt(Calendar.DAY_OF_MONTH) <= date.getAt(Calendar.DAY_OF_MONTH)) {
			if (pastTempMap.containsKey("d${tempState.date.getAt(Calendar.DAY_OF_MONTH)}") == false) {
				pastTempData3.push([time: tempState.isoDate, value: tempState.value])
				pastTempMap.put("d${tempState.date.getAt(Calendar.DAY_OF_MONTH)}", tempState.value)
			}
		}
	}
	log.debug "pastTempData1: ${pastTempData1}, pastTempData2: ${pastTempData2}, pastTempData3: ${pastTempData3}"
	pastBatteryStates.each { batteryState ->
		if (batteryState.date.getAt(Calendar.HOUR_OF_DAY) == date.getAt(Calendar.HOUR_OF_DAY) && batteryState.date.getAt(Calendar.DAY_OF_MONTH) == date.getAt(Calendar.DAY_OF_MONTH)) {
			if (pastBatteryMap.containsKey("m${batteryState.date.getAt(Calendar.MINUTE)}") == false) {
				pastBatteryData1.push([time: batteryState.isoDate, value: batteryState.value])
				pastBatteryMap.put("m${batteryState.date.getAt(Calendar.MINUTE)}", batteryState.value)
			}
		}
		else if (batteryState.date.getAt(Calendar.HOUR_OF_DAY) != date.getAt(Calendar.HOUR_OF_DAY) && batteryState.date.getAt(Calendar.DAY_OF_MONTH) == date.getAt(Calendar.DAY_OF_MONTH)) {
			if (pastBatteryMap.containsKey("h${batteryState.date.getAt(Calendar.HOUR_OF_DAY)}") == false) {
				pastBatteryData2.push([time: batteryState.isoDate, value: batteryState.value])
				pastBatteryMap.put("h${batteryState.date.getAt(Calendar.HOUR_OF_DAY)}", batteryState.value)
			}
		}
		else if (batteryState.date.getAt(Calendar.DAY_OF_MONTH) <= date.getAt(Calendar.DAY_OF_MONTH)) {
			if (pastBatteryMap.containsKey("d${batteryState.date.getAt(Calendar.DAY_OF_MONTH)}") == false) {
				pastBatteryData3.push([time: batteryState.isoDate, value: batteryState.value])
				pastBatteryMap.put("d${batteryState.date.getAt(Calendar.DAY_OF_MONTH)}", batteryState.value)
			}
		}
	}
	log.debug "pastBatteryData1: ${pastBatteryData1}, pastBatteryData2: ${pastBatteryData2}, pastBatteryData3: ${pastBatteryData3}"
	def result = [
		name: "initialData",
		currentState: device.currentState("motion"),
		temp: device.currentValue("temperature"),
		battery: device.currentValue("battery"),
		pastMotion1: pastMotionData1,
		pastMotion2: pastMotionData2,
		pastMotion3: pastMotionData3,
		pastTemp1: pastTempData1,
		pastTemp2: pastTempData2,
		pastTemp3: pastTempData3,
		pastBattery1: pastBatteryData1,
		pastBattery2: pastBatteryData2,
		pastBattery3: pastBatteryData3
	]
	return result
}

def getMotionStates() {
	def date = new Date()
	def mActive = 0
	def mInactive = 0
	def dActive = 0
	def dInactive = 0
	def wActive = 0
	def wInactive = 0
	def pastMotionData1 = []
	def pastMotionData2 = []
	def pastMotionData3 = []
	def pastMotionStates = device.statesBetween("motion", date - 7, date, [max: 1000])
	pastMotionStates.each { motionState ->
		if (motionState.value == "active" || motionState.value == "inactive") {
			if (motionState.date.getAt(Calendar.HOUR_OF_DAY) == date.getAt(Calendar.HOUR_OF_DAY) && motionState.date.getAt(Calendar.DAY_OF_MONTH) == date.getAt(Calendar.DAY_OF_MONTH)) {
				if (motionState.value == "active") {
					mActive = mActive + 1
				} else {
					mInactive = mInactive + 1
				}
			}
			else if (motionState.date.getAt(Calendar.HOUR_OF_DAY) != date.getAt(Calendar.HOUR_OF_DAY) && motionState.date.getAt(Calendar.DAY_OF_MONTH) == date.getAt(Calendar.DAY_OF_MONTH)) {
				if (motionState.value == "active") {
					dActive = dActive + 1
				} else {
					dInactive = dInactive + 1
				}
			}
			else if (motionState.date.getAt(Calendar.DAY_OF_MONTH) <= date.getAt(Calendar.DAY_OF_MONTH)) {
				if (motionState.value == "active") {
					wActive = wActive + 1
				} else {
					wInactive = wInactive + 1
				}
			}
		}
	}
	pastMotionData1.push([label: "Active", value: mActive])
	pastMotionData1.push([label: "Inactive", value: mInactive])
	pastMotionData2.push([label: "Active", value: dActive])
	pastMotionData2.push([label: "Inactive", value: dInactive])
	pastMotionData3.push([label: "Active", value: wActive])
	pastMotionData3.push([label: "Inactive", value: wInactive])
	log.debug "pastMotionData1: ${pastMotionData1}, pastMotionData2: ${pastMotionData2}, pastMotionData3: ${pastMotionData3}"
	def result = [
		pastMotion1: pastMotionData1,
		pastMotion2: pastMotionData2,
		pastMotion3: pastMotionData3
	]
	return result
}

def getTempStates() {
	def date = new Date()
	def pastTempMap = [:]
	def pastTempData1 = []
	def pastTempData2 = []
	def pastTempData3 = []
	def pastTempStates = device.statesBetween("temperature", date - 7, date, [max: 1000])
	pastTempStates.each { tempState ->
		if (tempState.date.getAt(Calendar.HOUR_OF_DAY) == date.getAt(Calendar.HOUR_OF_DAY) && tempState.date.getAt(Calendar.DAY_OF_MONTH) == date.getAt(Calendar.DAY_OF_MONTH)) {
			if (pastTempMap.containsKey("m${tempState.date.getAt(Calendar.MINUTE)}") == false) {
				pastTempData1.push([time: tempState.isoDate, value: tempState.value])
				pastTempMap.put("m${tempState.date.getAt(Calendar.MINUTE)}", tempState.value)
			}
		}
		else if (tempState.date.getAt(Calendar.HOUR_OF_DAY) != date.getAt(Calendar.HOUR_OF_DAY) && tempState.date.getAt(Calendar.DAY_OF_MONTH) == date.getAt(Calendar.DAY_OF_MONTH)) {
			if (pastTempMap.containsKey("h${tempState.date.getAt(Calendar.HOUR_OF_DAY)}") == false) {
				pastTempData2.push([time: tempState.isoDate, value: tempState.value])
				pastTempMap.put("h${tempState.date.getAt(Calendar.HOUR_OF_DAY)}", tempState.value)
			}
		}
		else if (tempState.date.getAt(Calendar.DAY_OF_MONTH) <= date.getAt(Calendar.DAY_OF_MONTH)) {
			if (pastTempMap.containsKey("d${tempState.date.getAt(Calendar.DAY_OF_MONTH)}") == false) {
				pastTempData3.push([time: tempState.isoDate, value: tempState.value])
				pastTempMap.put("d${tempState.date.getAt(Calendar.DAY_OF_MONTH)}", tempState.value)
			}
		}
	}
	log.debug "pastTempData1: ${pastTempData1}, pastTempData2: ${pastTempData2}, pastTempData3: ${pastTempData3}"
	def result = [
		pastTemp1: pastTempData1,
		pastTemp2: pastTempData2,
		pastTemp3: pastTempData3
	]
	return result
}

def getBatteryStates() {
	def date = new Date()
	def pastBatteryMap = [:]
	def pastBatteryData1 = []
	def pastBatteryData2 = []
	def pastBatteryData3 = []
	def pastBatteryStates = device.statesBetween("battery", date - 7, date, [max: 1000])
	pastBatteryStates.each { batteryState ->
		if (batteryState.date.getAt(Calendar.HOUR_OF_DAY) == date.getAt(Calendar.HOUR_OF_DAY) && batteryState.date.getAt(Calendar.DAY_OF_MONTH) == date.getAt(Calendar.DAY_OF_MONTH)) {
			if (pastBatteryMap.containsKey("m${batteryState.date.getAt(Calendar.MINUTE)}") == false) {
				pastBatteryData1.push([time: batteryState.isoDate, value: batteryState.value])
				pastBatteryMap.put("m${batteryState.date.getAt(Calendar.MINUTE)}", batteryState.value)
			}
		}
		else if (batteryState.date.getAt(Calendar.HOUR_OF_DAY) != date.getAt(Calendar.HOUR_OF_DAY) && batteryState.date.getAt(Calendar.DAY_OF_MONTH) == date.getAt(Calendar.DAY_OF_MONTH)) {
			if (pastBatteryMap.containsKey("h${batteryState.date.getAt(Calendar.HOUR_OF_DAY)}") == false) {
				pastBatteryData2.push([time: batteryState.isoDate, value: batteryState.value])
				pastBatteryMap.put("h${batteryState.date.getAt(Calendar.HOUR_OF_DAY)}", batteryState.value)
			}
		}
		else if (batteryState.date.getAt(Calendar.DAY_OF_MONTH) <= date.getAt(Calendar.DAY_OF_MONTH)) {
			if (pastBatteryMap.containsKey("d${batteryState.date.getAt(Calendar.DAY_OF_MONTH)}") == false) {
				pastBatteryData3.push([time: batteryState.isoDate, value: batteryState.value])
				pastBatteryMap.put("d${batteryState.date.getAt(Calendar.DAY_OF_MONTH)}", batteryState.value)
			}
		}
	}
	log.debug "pastBatteryData1: ${pastBatteryData1}, pastBatteryData2: ${pastBatteryData2}, pastBatteryData3: ${pastBatteryData3}"
	def result = [
		pastBattery1: pastBatteryData1,
		pastBattery2: pastBatteryData2,
		pastBattery3: pastBatteryData3
	]
	return result
}

def parse(String description) {
	//log.debug "description: $description"
	state.heartbeat = Calendar.getInstance().getTimeInMillis()
	Map map = [:]
	if (description?.startsWith('catchall:')) {
		map = parseCatchAllMessage(description)
	}
	else if (description?.startsWith('read attr -')) {
		map = parseReportAttributeMessage(description)
	}
	else if (description?.startsWith('temperature: ')) {
		map = parseCustomMessage(description)
	}
	else if (description?.startsWith('zone status')) {
		map = parseIasMessage(description)
	}
 
	//log.debug "Parse returned $map"
	def result = map ? createEvent(map) : null
	
	if (description?.startsWith('enroll request')) {
		List cmds = enrollResponse()
		//log.debug "enroll response: ${cmds}"
		result = cmds?.collect { new physicalgraph.device.HubAction(it) }
	}
	return result
}

private Map parseCatchAllMessage(String description) {
	Map resultMap = [:]
	def cluster = zigbee.parse(description)
	if (shouldProcessMessage(cluster)) {
		switch(cluster.clusterId) {
			case 0x0001:
				resultMap = getBatteryResult(cluster.data.last())
				break

			case 0x0402:
				// temp is last 2 data values. reverse to swap endian
				String temp = cluster.data[-2..-1].reverse().collect { cluster.hex1(it) }.join()
				def value = getTemperature(temp)
				resultMap = getTemperatureResult(value)
				break

			case 0x0406:
				//log.debug 'motion'
				resultMap.name = 'motion'
				break
		}
	}
	return resultMap
}

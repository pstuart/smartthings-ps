/**
 *  Live Code HVAC Fan Run Away
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
definition(
    name: "Live Code HVAC Fan Run Away",
    namespace: "pstuart",
    author: "Patrick Stuart",
    description: "My wife's first one today smartapp request.",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    oauth: true)


preferences {
	section("Thermostats") {
		input "tstats", "capability.thermostat", title: "HVAC System", required:true, multiple: true
        
	}
    
    section("Set how long (in minutes) you want the fan to run, minimum of 5 minutes.") {
    	input "fanmins", "number", title: "Minutes to Run Fan"
    }
    section ([mobileOnly:true]) {
        	label title: "Assign a name", required: false
            mode title: "Set for specific mode(s)", required: false
    }
}

mappings {
	path("/html") {action: [GET: "html"]}
    path("/updates") {action: [GET: "updates"]}
    path("/:id/:command") {action: [GET: "deviceAction"]}
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	// start the hvac fan
    // start a timer
    // at end of timer, stop hvac fan
    subscribe(app, startFan)
    startFan()
    log.debug "Fan Started"
}

def startFan(evt) {
	log.debug "startFan function hit"
	tstats*.fanOn()
    runIn(fanmins * 60, stopFan)
}

def stopFan(evt) {
	log.debug "stopFan function hit"
    unschedule()
    tstats*.fanAuto()
    log.debug "fan stopped"
    
}

def html() {
	state.updates = []
	render contentType: "text/html", data: """
    <html>
    <head>
    ${js()}
    </head>
    <body>
    <div id="wrapper">
    <div id="header">
    	Live Code Friday - HTML Tstat Fan Control
    </div>
    <div id="remote">
    <div id="tstats">
    ${sws()}
    </div>
    </div>
    </div>
    </body>
    </html>
    """
}

def sws() {
	def markup = ""
    tstats.each {
    	markup = markup + """
        	<div class="tstat" id="tstat_${it.id}">
            	<div class="name">${it.displayName}</div>
            	<div class="id">${it.id}</div>
                <div class="status">${it.currentValue("thermostatFanMode")}</div>
                <div class="actions"><a class="action" href="https://graph.api.smartthings.com/api/smartapps/installations/<fixthis>/${it.id}/toggle">Toggle</a></div>
            </div>
        """
    }
    
    markup
}


def js() { """
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
<script type="text/javascript">
	\$(document).ready(function() {
    	//def url = "https://graph.api.smartthings.com/api/smartapps/installations/" + ${app.id} +
    	alert("ready");
    });
</script>
"""
}

def deviceAction() {
	log.debug params.id
    log.debug params.command
    def sw = switches.find {it.id == params.id }
    if (sw.currentValue("thermostatFanMode") == "on") {
    sw.auto() 
     render contentType: "text/html", data: """auto"""
    }
    else 
    {
    sw.on()
    render contentType: "text/html", data: """on"""
    }
    
    
}
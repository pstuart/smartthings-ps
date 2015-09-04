/**
 *  Live Code HVAC Fac Run Away
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
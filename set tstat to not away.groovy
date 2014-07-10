/**
 *  Thermostat Away to Home
 *
 *  Copyright 2014 patrick@patrickstuart.com
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
    name: "Thermostat Away Mode Reset on Motion and Schedule",
    namespace: "ps",
    author: "patrick@patrickstuart.com",
    description: "Set the Nest thermostat (or any tstat) to home when motion is sensed or door is opened, etc.",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	
    	section("Thermostats") {
    		input "tstat1", "capability.thermostat", title: "Which Tstats?", multiple: true
		}
    
        section("When there's movement..."){
            input "motion1", "capability.motionSensor", title: "Where?", multiple: true, required: false
        }

		section("Schedule") {
			input(name: "days", type: "enum", title: "Allow Automatic Away/Not Away On These Days", description: "", 
            required: false, multiple: true, options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"])
		
			input( name: "timeAway", title: "Turn On Away Time?", type: "time", required: false)
		
			input( name: "timeHome", title: "Turn Off Away Time?", type: "time", required: false)
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
    subscribe(motion1, "motion.active", motionHandler)
	unschedule(changeModeHome)
    unschedule(changeModeAway)
	   
    schedule(timeHome, changeModeHome)
    schedule(timeAway, changeModeAway)
	
}



def changeModeHome(evt) {
	log.debug "change Mode Home Fired"
	def today = new Date().format("EEEE")
	//log.debug "today: ${today}, days: ${days}"
    
	if (!days || days.contains(today)) {
    	log.debug "Set to Not Away on $today"
    	setHome()
    }
}

def changeModeAway(evt) {
	log.debug "change Mode Away Fired"
	def today = new Date().format("EEEE")
	if (!days || days.contains(today)) {
    log.debug "Set to Away on $today"
    	setAway()
    }
}

def motionHandler(evt) {
	log.debug "Motion detected, $evt Set to Not Away"
    setHome()
}

def setHome() {
	for (t in settings.tstat1) {
    if (tstat1[0].latestValue("presence") == "away") {
    	log.debug "Setting tstat to here"
        t.present()
        }
    }
}

def setAway() {
	for (t in settings.tstat1) {
    if (tstat1[0].latestValue("presence") == "present") {
    	log.debug "Setting tstat to away"
        t.away()
        }
    }
}



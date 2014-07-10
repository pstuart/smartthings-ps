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
    name: "Thermostat Away Mode Reset on Motion",
    namespace: "ps",
    author: "patrick@patrickstuart.com",
    description: "Set the Nest thermostat (or any tstat) to home when motion is sensed or door is opened, etc.",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Settings") {
    input "tstat1", "capability.thermostat", multiple: true
	}
    
    section("When there's movement..."){
		input "motion1", "capability.motionSensor", title: "Where?"
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
}


def motionHandler(evt) {
	log.debug "Motion detected, $evt"
    
    for (t in settings.tstat1) {
    if (tstat1[0].latestValue("presence") == "away") {
    	log.debug "Setting tstat to here"
        t.present()
        }
    }
}

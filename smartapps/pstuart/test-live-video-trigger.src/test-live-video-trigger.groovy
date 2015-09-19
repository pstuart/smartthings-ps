/**
 *  Test Live Video Trigger
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
    name: "Test Live Video Trigger",
    namespace: "pstuart",
    author: "Patrick Stuart",
    description: "Test Live Video Trigger",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Pick Camera to Turn on/off") {
		input "camera", "capability.switch"
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
	subscribe(app, appTouch)
}

def appTouch(evt) {
	log.debug "app touch hit"
    def cameraState = camera.currentValue("switch")
    log.debug cameraState
    //def cameraRecord = camera.currentValue("record")
    //log.debug cameraRecord
    if (cameraState.startsWith("off")) {
   	 	camera.on()
    } else
    {
    	camera.off()  //turn off
    }
}
/**
 *  Generic Switch
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
	definition (name: "Generic Dimmer", namespace: "pstuart", author: "Patrick Stuart") {
		capability "Switch"
        capability "Switch Level"
	}

	simulator {
		// TODO: define status and reply messages here
	}

	tiles {
		standardTile("switch", "device.switch", width: 2, height: 2, canChangeIcon: true, canChangeBackground:true)
       	{
        	state "off", label: '${currentValue}', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#FF0000"
            state "on", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#00F208"
        }
        standardTile("toggle", "device.switch") 
        {
        	state "default", label:'Toggle', action: "toggle", icon: "st.secondary.refresh-icon"
        }
        main "switch"
        details(["switch","toggle"])
	}
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
	// TODO: handle 'switch' attribute

}

// handle commands
def on() {
	log.debug "Executing 'on'"
	// TODO: handle 'on' command
    sendEvent(name: "switch", value: "on")
}

def off() {
	log.debug "Executing 'off'"
	// TODO: handle 'off' command
    sendEvent(name: "switch", value: "off")
}

def toggle() {
	log.debug "Executing Toggle"
}


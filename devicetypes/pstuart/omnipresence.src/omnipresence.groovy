/**
 *  OmniPresence
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
	definition (name: "OmniPresence", namespace: "pstuart", author: "Patrick Stuart", oauth: true) {
		capability "Presence Sensor"
        capability "Sensor"
	}

	simulator {
		status "present": "presence: 1"
		status "not present": "presence: 0"
	}

	tiles {
		standardTile("presence", "device.presence", width: 2, height: 2, canChangeBackground: true) {
			state("present", labelIcon:"st.presence.tile.mobile-present", backgroundColor:"#53a7c0")
			state("not present", labelIcon:"st.presence.tile.mobile-not-present", backgroundColor:"#ebeef2")
		}
		main "presence"
		details "presence"
	}
}

// parse events into attributes
def parse(String description) {
    log.debug "Parsing '${description}'"
    def msg = parseLanMessage(description)

	log.debug "data ${msg.data}"
    log.debug "body ${msg.body}"
    log.debug "headers ${msg.headers}"
        if ( msg.headers.toString().contains("get /?present=true"))
        {
        	def results = [
                name: "presence",
                value: "present",
                unit: null,
                linkText: "",
                descriptionText: "is Present",
                handlerName: null,
                isStateChange: true
            	]
            log.debug "Parse returned $results.descriptionText"
            return results
    	} else if ( msg.headers.toString().contains("get /?present=false"))
        {
        	def results = [
                name: "presence",
                value: "not present",
                unit: null,
                linkText: "",
                descriptionText: "is Not Present",
                handlerName: null,
                isStateChange: true
            	]
            log.debug "Parse returned $results.descriptionText"
            return results
        }
}
/**
 *  Unknown Zigbee Test Device
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
	definition (name: "Unknown Zigbee Test Device", namespace: "pstuart", author: "Patrick Stuart") {
		capability "Refresh"
	}

	simulator {
		// TODO: define status and reply messages here
	}

	tiles {
		// TODO: define your main and details tiles here
	}
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"

}

// handle commands
def refresh() {
	log.debug "Executing 'refresh'"
	// TODO: handle 'refresh' command
    getClusters();
}

def getClusters() {
	log.debug "getClusters hit $device.deviceNetworkId please check full logs for response, may need a smartapp installed that subscribes to location parsing, like SHM, it will not be filtered to this device in the format like cp desc: ep_cnt:4, ep:01 C4 C5 C6"
	"zdo active 0x${device.deviceNetworkId}"
}

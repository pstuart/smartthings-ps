/**
 *  Insteon Switch (LOCAL)
 *
 *  Copyright 2014 patrick@patrickstuart.com
 *  Updated 1/4/15 by goldmichael@gmail.com
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
	definition (name: "Insteon Switch (LOCAL)", namespace: "pstuart", author: "patrick@patrickstuart.com") {
		capability "Switch"
		capability "Sensor"
		capability "Actuator"
        capability "Water Sensor"
		
        command "status"
	}

    preferences {
    input("InsteonIP", "string", title:"Insteon IP Address", description: "Please enter your Insteon Hub IP Address", defaultValue: "192.168.1.2", required: true, displayDuringSetup: true)
    input("InsteonPort", "string", title:"Insteon Port", description: "Please enter your Insteon Hub Port", defaultValue: 25105, required: true, displayDuringSetup: true)
    input("InsteonID", "string", title:"Device Insteon ID", description: "Please enter the devices Insteon ID - numbers only", defaultValue: "1E65F2", required: true, displayDuringSetup: true)
    input("InsteonHubUsername", "string", title:"Insteon Hub Username", description: "Please enter your Insteon Hub Username", defaultValue: "michael" , required: true, displayDuringSetup: true)
    input("InsteonHubPassword", "string", title:"Insteon Hub Password", description: "Please enter your Insteon Hub Password", defaultValue: "password" , required: true, displayDuringSetup: true)
   }

	simulator {
    
	}

	// UI tile definitions
	tiles {
		standardTile("water", "device.water", width: 2, height: 2, canChangeIcon: true) {
			state "dry", label: '${name}', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
			state "wet", label: '${name}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#79b821"
		}
        valueTile("battery", "device.battery", inactiveLabel: false, decoration: "flat") {
			state "battery", label:'${currentValue} battery', unit:""
		}
        standardTile("refresh", "device.button", inactiveLabel: false, decoration: "flat") {
            state "refresh", action:"status", icon:"st.secondary.refresh"
        }

		main "water"
		details(["water", "battery", "refresh"])
	}
}

// handle commands
def on() {
	//log.debug "Executing 'take'"
    sendEvent(name: "water", value: "wet")
    def host = InsteonIP

	def path = "/3?0262" + "${InsteonID}" + "0F12FF=I=3"
    log.debug "path is: $path"

	
    def userpassascii = "${InsteonHubUsername}:${InsteonHubPassword}"
	def userpass = "Basic " + userpassascii.encodeAsBase64().toString()
    def headers = [:] //"HOST:" 
    headers.put("HOST", "$host:$InsteonPort")
    headers.put("Authorization", userpass)
    

    try {
    def hubAction = new physicalgraph.device.HubAction(
    	method: method,
    	path: path,
    	headers: headers
        )  
    }
    catch (Exception e) {
    log.debug "Hit Exception on $hubAction"
    log.debug e
    }
}

def status() {
	log.debug "status hit"
	def host = InsteonIP
  
	def path = "/3?0262" + "${InsteonID}"  + "0F19FF=I=3"
    log.debug "path is: $path"


    def userpassascii = "${InsteonHubUsername}:${InsteonHubPassword}"
	def userpass = "Basic " + userpassascii.encodeAsBase64().toString()
    def headers = [:] //"HOST:" 
    headers.put("HOST", "$host:$InsteonPort")
    headers.put("Authorization", userpass)

    try {
    def hubAction = new physicalgraph.device.HubAction(
    	method: method,
    	path: path,
    	headers: headers
        )
    }
    catch (Exception e) {
    	log.debug "Hit Exception on $hubAction"
    	log.debug e
    }
    
}

def off() {
	//log.debug "Executing 'take'"
    sendEvent(name: "water", value: "dry")
    def host = InsteonIP
  
	def path = "/3?0262" + "${InsteonID}"  + "0F14FF=I=3"
    log.debug "path is: $path"


    def userpassascii = "${InsteonHubUsername}:${InsteonHubPassword}"
	def userpass = "Basic " + userpassascii.encodeAsBase64().toString()
    def headers = [:] //"HOST:" 
    headers.put("HOST", "$host:$InsteonPort")
    headers.put("Authorization", userpass)

    try {
    def hubAction = new physicalgraph.device.HubAction(
    	method: method,
    	path: path,
    	headers: headers
        )
    }
    catch (Exception e) {
    	log.debug "Hit Exception on $hubAction"
    	log.debug e
    }
}

def parse(String description) {
	log.debug "Parsing $description"
    def map = stringToMap(description)
    
    if (description == "0")
    {
    	sendEvent(name: "water", value: "wet")
    }
    if (description == "1")
    {
    	sendEvent(name: "water", value: "dry")
    }
    if (description == "2")
    {
    	sendEvent(name: "battery", value: "low")
    }
    if (description == "3")
    {
    	sendEvent(name: "battery", value: "ok")
    }
}
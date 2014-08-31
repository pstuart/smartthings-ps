/**
 *  ps_HueZigbeeHA
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
metadata {
	// Automatically generated. Make future change here.
	definition (name: "ps_HueZigbeeHA", namespace: "ps", author: "patrick@patrickstuart.com") {
		capability "Switch Level"
		capability "Actuator"
		capability "Color Control"
		capability "Switch"
		capability "Configuration"
		capability "Polling"
		capability "Refresh"
		capability "Sensor"

		command "setAdjustedColor"

		//fingerprint endpointId: "1", profileId: "C25D", inClusters: "0000,0003,0004,0005,0006,0008,000A"
        fingerprint endpointId: "01", profileId: "0104", deviceId: "0101", deviceVersion: "00", inClusters: "07 0000 0003 0004 0005 0006 0008 000A", outClusters: "00"
	}

	// simulator metadata
	simulator {
		// status messages
		status "on": "on/off: 1"
		status "off": "on/off: 0"

		// reply messages
		reply "zcl on-off on": "on/off: 1"
		reply "zcl on-off off": "on/off: 0"
        
        command "test"
	}

	// UI tile definitions
	tiles {
		standardTile("switch", "device.switch", width: 1, height: 1, canChangeIcon: true) {
			state "off", label: '${name}', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
			state "on", label: '${name}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#79b821"
		}
		standardTile("refresh", "device.switch", inactiveLabel: false, decoration: "flat") {
			state "default", label:"", action:"refresh.refresh", icon:"st.secondary.refresh"
		}
		controlTile("levelSliderControl", "device.level", "slider", height: 1, width: 2, inactiveLabel: false) {
			state "level", action:"switch level.setLevel"
		}
		valueTile("level", "device.level", inactiveLabel: false, decoration: "flat") {
			state "level", label: 'Level ${currentValue}%'
		}

		main(["switch"])
		details(["switch", "levelSliderControl", "level", "refresh"])
	}
}

// Parse incoming device messages to generate events
def parse(String description) {
	log.trace description
	if (description?.startsWith("catchall:")) {
		def msg = zigbee.parse(description)
		//log.trace msg
		//log.trace "data: $msg.data"
        //log.debug msg
        def payloadhex = description.tokenize(" ").last()
        def payload = payloadhex.decodeHex()
        def x = ""
        
        payload.each() { x += it as char }
		
        log.debug "Payload is $x"
        if(x.contains("sa c4.dm.cc 00 01"))
        {
        	def result = createEvent(name: "switch", value: "on")
            log.debug "Parse returned ${result?.descriptionText}"
            return result
        }
        if(x.contains("sa c4.dm.cc 00 00"))
        {
        	def result = createEvent(name: "switch", value: "off")
            log.debug "Parse returned ${result?.descriptionText}"
            return result
        }
        if(x.contains("sa c4.dm.cc 00 02"))
        {
        	log.debug "Double Tap Top"
        }
         
        
        if(description?.startsWith("catchall: 0104 0006")) 
        {
            log.debug "got a ha message"
            //def value = name == "switch" ? (description?.endsWith("01") ? "on" : "off") : null
			if(description?.endsWith("01"))
            {
                def result = createEvent(name: "switch", value: "on")
                log.debug "Parse returned ${result?.descriptionText}"
                return result
            }
            if(description?.endsWith("00"))
            {
                def result = createEvent(name: "switch", value: "off")
                log.debug "Parse returned ${result?.descriptionText}"
                return result
            }
        }
        
	}
}

def test() {
	"st cmd 0x${device.deviceNetworkId} c5 1 { 550 sa c4.dm.cc 00 001 }"
}
def on() {
	// just assume it works for now
	log.debug "on() $endpointId"
	sendEvent(name: "switch", value: "on")
	//"st cmd 0x${device.deviceNetworkId} ${endpointId} 6 1 {}"
	"st cmd 0x${device.deviceNetworkId} 1 6 1 {}"
}

def off() {
	// just assume it works for now
	log.debug "off() $endpointId"
	sendEvent(name: "switch", value: "off")
	//"st cmd 0x${device.deviceNetworkId} ${endpointId} 6 0 {}"
	"st cmd 0x${device.deviceNetworkId} 1 6 0 {}"
}

def refresh() {
	"st rattr 0x${device.deviceNetworkId} 1 6 0"
}

def poll(){
	log.debug "Poll is calling refresh"
	refresh()
}

def setLevel(value) {
	log.trace "setLevel($value)"
	def cmds = []

	if (value == 0) {
		sendEvent(name: "switch", value: "off")
		cmds << "st cmd 0x${device.deviceNetworkId} 1 6 0 {}"
		//cmds << "st cmd 0x${device.deviceNetworkId} ${endpointId} 6 0 {}"
	}
	else if (device.latestValue("switch") == "off") {
		sendEvent(name: "switch", value: "on")
	}

	sendEvent(name: "level", value: value)
	def level = new BigInteger(Math.round(value * 255 / 100).toString()).toString(16)
	//cmds << "st cmd 0x${device.deviceNetworkId} ${endpointId} 8 4 {${level} 0000}"
	cmds << "st cmd 0x${device.deviceNetworkId} 1 8 4 {${level} 0000}"

	//log.debug cmds
	cmds
}

private getEndpointId() {
	log.debug "Device.endpoint is $device.endpointId"
	//new BigInteger(device.endpointId, 16).toString()
}

//def endpointId() {
//	return device.endpointId
//}

private hex(value, width=2) {
	def s = new BigInteger(Math.round(value).toString()).toString(16)
	while (s.size() < width) {
		s = "0" + s
	}
	s
}

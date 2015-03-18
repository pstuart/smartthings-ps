/**
 *  ps_Control4_Dimmer_ZigbeeHA
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
	definition (name: "ps_Control4_Dimmer_ZigbeeHA", namespace: "ps", author: "patrick@patrickstuart.com") {
		capability "Switch Level"
		capability "Actuator"
		capability "Switch"
		capability "Configuration"
		capability "Refresh"
		capability "Polling"

        fingerprint endpointId: "01", profileId: "0104", deviceId: "0101", inClusters: "0000 0003 0004 0005 0006 0008 000A"
        fingerprint endpointId: "C4", profileId: "C25D", deviceId: "0101", inClusters: "0001"
        
	}
    
 	preferences {
		input("OnSpeed", "text", title:"Turn On Speed", description: "Please enter the speed at which the dimmer turns on", defaultValue:"1500", required: true, displayDuringSetup: true)
		input("OffSpeed", "text", title:"Turn Off Speed", description: "Please enter the speed at which the dimmer turns off", defaultValue:"1500", required: true, displayDuringSetup: true)
        input("DefaultOnValue", "text", title:"Default On Value", description: "Please enter the default value you want ST to turn on to, in case the last dimmed value is lost.", defaultValue:"75", required: true, displayDuringSetup: true)
        
    }
	simulator {
		// status messages
		status "on": "on/off: 1"
		status "off": "on/off: 0"

		// reply messages
		reply "zcl on-off on": "on/off: 1"
		reply "zcl on-off off": "on/off: 0"
        
        command "test"
        command "getClusters"
	}

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

def parse(String description) {
	//log.trace description
	if (description?.startsWith("catchall: C25")) {
		def msg = zigbee.parse(description)
        //log.trace msg
        def payloadhex = description.tokenize(" ").last()
        def payload = payloadhex.decodeHex()
        def x = ""
        payload.each() { x += it as char }
        
        //log.debug "Payload is $x"
        
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
            if(x.contains("sa c4.dm.cc 01 02"))
            {
                log.debug "Double Tap Bottom"
            } 
            if(x.contains("sa c4.dm.cc 00 03"))
            {
                log.debug "Triple Tap Top"
            }
            if(x.contains("sa c4.dm.cc 01 03"))
            {
                log.debug "Triple Tap Bottom"
            } 
            if(x.contains("sa c4.dm.t0c") || x.contains("sa c4.dm.b0c")) {
            	log.debug "switch is dimming $x"
                def l = convertHexToInt(x.tokenize(" ").last().split()) 
                log.debug l
            	def i = Math.round(l)
                sendEvent( name: "level", value: i )
            }
        
	}
        
    if (description?.startsWith("read attr")) {
   		//log.debug "Read Attr found"
        //def descMap = parseDescriptionAsMap(description)
        //log.debug descMap
        //log.debug description[-2..-1]
        def i = Math.round(convertHexToInt(description[-2..-1]) / 256 * 100 )
        
		sendEvent( name: "level", value: i )
    }
}

def test() {
/*
	def cmd = []
    cmd << "zcl global send-me-a-report 1 0x20 0x20 600 3600 {0100}"
    cmd << "delay 500"
    cmd << "send 0x${device.deviceNetworkId} 1 6"
    cmd << "delay 1000"
    cmd
    */
    //'zcl on-off on'
    log.debug "$OnSpeed onspeed $OffSpeed offspeed $DefaultOnValue defaultOnValue $state.lastOnValue is state.lastonvalue"
}

def getClusters() {
	log.debug "getClusters hit $device.deviceNetworkId"
    //"st rattr 0x${device.deviceNetworkId} 4 6 0x01"
	"zdo active 0x${device.deviceNetworkId}"
}

def on() {
	log.debug "on()"
	sendEvent(name: "switch", value: "on")
    
    //"st cmd 0x${device.deviceNetworkId} 1 6 1 {}"
    
    //get level in UI 
    def value = device.currentValue("level")
    if (value == 0) { value = state.lastOnValue
    log.debug "Value is $value"
    } 
    def level = new BigInteger(Math.round(value * 255 / 100).toString()).toString(16)
    //log.debug level
    
    def speed = OnSpeed //.toString().padLeft(4, '0')
    log.debug speed
    "st cmd 0x${device.deviceNetworkId} 1 8 4 {${level} ${speed} }"
}

def off() {
	log.debug "off()"
    //state.lastOnValue = device.currentValue("level")
	sendEvent(name: "switch", value: "off")
	//"st cmd 0x${device.deviceNetworkId} 1 6 0 {}"
    def speed = OffSpeed.toString().padLeft(4, '0')
    log.debug speed
 	"st cmd 0x${device.deviceNetworkId} 1 8 4 {00 ${speed} }"
}

def refresh() {
    [
	"st rattr 0x${device.deviceNetworkId} 1 6 0", "delay 100",
    "st rattr 0x${device.deviceNetworkId} 1 8 0"
    ]
}

def poll(){
	log.debug "Poll is calling refresh"
	refresh()
}

def setLevel(value) { setLevel(value,"0500") }

def setLevel(value, speed) {
	log.debug value
    log.debug speed //.toString().padLeft(4, '0')
    state.lastOnValue = value
    speed = speed.toString().padLeft(4, '0')
    log.trace "setLevel($value)"
   
	def cmds = []
	if (value < 8.0) {
    	log.debug "Value equals 0?"
		sendEvent(name: "switch", value: "off")
        
		cmds << "st cmd 0x${device.deviceNetworkId} 1 8 4 {00 0500}"
		//cmds << "st cmd 0x${device.deviceNetworkId} 1 6 0 {}"
	}
	else if (device.latestValue("switch") == "off") {
		sendEvent(name: "switch", value: "on")
	}

	sendEvent(name: "level", value: value)
	def level = new BigInteger(Math.round(value * 255 / 100).toString()).toString(16)
	cmds << "st cmd 0x${device.deviceNetworkId} 1 8 4 {${level} ${speed} }"
	cmds
}

//def setLevel(value) {
	
//}


def configure() {

	String zigbeeId = swapEndianHex(device.hub.zigbeeId)
	log.debug "Confuguring Reporting and Bindings."
	def configCmds = [	

        //Switch Reporting
        "zcl global send-me-a-report 6 0 0x10 0 3600 {01}", "delay 500",
        "send 0x${device.deviceNetworkId} 1 1", "delay 1000",

        //Level Control Reporting
        "zcl global send-me-a-report 8 0 0x20 5 3600 {0010}", "delay 200",
        "send 0x${device.deviceNetworkId} 1 1", "delay 1500",

        "zdo bind 0x${device.deviceNetworkId} 1 1 6 {${device.zigbeeId}} {}", "delay 1500",
		"zdo bind 0x${device.deviceNetworkId} 1 1 8 {${device.zigbeeId}} {}", "delay 500",
	]
    return configCmds + refresh() // send refresh cmds as part of config
}

def uninstalled() {

	log.debug "uninstalled()"
	response("zcl rftd")
 
}

private getEndpointId() {
	//log.debug "Device.endpoint is $device.endpointId"
	new BigInteger(device.endpointId, 16).toString()
}

private hex(value, width=2) {
	def s = new BigInteger(Math.round(value).toString()).toString(16)
	while (s.size() < width) {
		s = "0" + s
	}
	s
}

private Integer convertHexToInt(hex) {
	Integer.parseInt(hex,16)
}


private String swapEndianHex(String hex) {
    reverseArray(hex.decodeHex()).encodeHex()
}

private byte[] reverseArray(byte[] array) {
    int i = 0;
    int j = array.length - 1;
    byte tmp;
    while (j > i) {
        tmp = array[j];
        array[j] = array[i];
        array[i] = tmp;
        j--;
        i++;
    }
    return array
}

def parseDescriptionAsMap(description) {
    (description - "read attr - ").split(",").inject([:]) { map, param ->
        def nameAndValue = param.split(":")
        map += [(nameAndValue[0].trim()):nameAndValue[1].trim()]
    }
}

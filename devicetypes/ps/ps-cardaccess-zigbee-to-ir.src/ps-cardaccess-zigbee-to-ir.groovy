/**
 *  ps_CardAccess Zigbee to IR
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
	definition (name: "ps_CardAccess Zigbee to IR", namespace: "ps", author: "patrick@patrickstuart.com") {
		capability "Contact Sensor"
		capability "Polling"
		capability "Signal Strength"
		capability "Sensor"
		capability "Refresh"
		capability "Configuration"
		capability "Button"
		
        command "test"
        command "getClusters"
        
        fingerprint endpointId: "01", deviceId: "0101", profileId: "0104", inClusters: "0000, 000A, 0099"  //01 0104 0101 00 03 0000 000A 0099 00
		//fingerprint endpointId: "01", profileId: "0104", deviceId: "0101", inClusters: "0000 0003 0004 0005 0006 0008 000A"
        //fingerprint inClusters: "000A", endpointId: "01", deviceId: "0101", profileId: "0104", deviceVersion: "00"
		//fingerprint inClusters: "0408", outClusters: "AD0A", deviceVersion: "02", profileId: "C25D", endpointId: "91", deviceId: "0001"
        //fingerprint endpointId: "0C", profileId: "0104", deviceId: "0101", deviceVersion: "00", inClusters: "0000, 000A, 0099"
//01 0104 0101 00 03 0000 000A 0099 00
	}

	simulator {
		// TODO: define status and reply messages here
	}

	tiles {
		standardTile("SendIR", "device.button") {
            state "SendIR", action:"test2",  label:'${name}', icon:"st.unknown.zwave.remote-controller", backgroundColor: '#e14902'
        }
        standardTile("Test", "device.button") {
        	state "Test", label:'${name}', action:"test", icon: "st.thermostat.thermostat-down", backgroundColor: '#e14902'
        }
        standardTile("Test2", "device.button") {
        	state "Test2", label:'${name}', action:"test2", icon: "st.thermostat.thermostat-down", backgroundColor: '#e14902'
        }
		standardTile("refresh", "device.alarmStatus", inactiveLabel: false, decoration: "flat") {
            state "refresh", action:"refresh", icon:"st.secondary.refresh"
        }

		main(["SendIR"])
		details(["SendIR", "Test", "Test2","refresh"])
	}
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
	if (description?.startsWith("catchall:")) {
		def msg = zigbee.parse(description)
		log.trace msg
		//log.trace "data: $msg.data"
        //log.debug msg
        def payloadhex = description.tokenize(" ").last()
        def payload = payloadhex.decodeHex()
        def x = ""
        
        payload.each() { x += it as char }
		
        log.debug "Payload is $x"
        
        
        }
        

}


// handle commands
def poll() {
	log.debug "Executing 'poll'"
	// TODO: handle 'poll' command
}

def refresh() {
	log.debug "Refresh hit"
    /*
    //zigbee.refreshData("0", "4") + zigbee.refreshData("0", "5")
    
    //def payload = "0s1234 c4.wc0 q01\r\n".encodeAsHex()
    def payload = "0s1234 c4.wc0 q01\r\n".encodeAsHex()
    log.debug payload
    log.debug swapEndianHex(payload)
    //"send 0x${device.deviceNetworkId} 1 C25C {${swapEndianHex(payload)}}"
    //"st cmd 0x${device.deviceNetworkId} ${endpointId} C25C 1 {${swapEndianHex(payload)}}"
    //def cmd = "send 0x${device.deviceNetworkId} C5 C5 {${swapEndianHex(payload)}}"
    def cmd = [
    //"zdo bind 0x${device.deviceNetworkId} 0C 01 0099 {${device.zigbeeId}} {}",
    //"zdo bind 0x${device.deviceNetworkId}  0x01 0x0c 0x0101 {${device.zigbeeId}} {}",
    //"delay 500", 
    //"zcl global send-me-a-report 0104 0C 0099 0 67 {71}",
    //"delay 500",
    "st send 0x${device.deviceNetworkId} 0C 01 0C {6771}"
    ]
    //def cmd = "st cmd 0x${device.deviceNetworkId} 0x0C 0x0099 0x67 {0x71}"
    //def cmd = "zcl global send-me-a-report 0104 0C 0099 0 67 {71}"
    log.debug cmd
    cmd
    /*
    [
	"st rattr 0x${device.deviceNetworkId} 1 1 0x20", "delay 500",
    "st rattr 0x${device.deviceNetworkId} 1 0101 0", "delay 500",
    "st rattr 0x${device.deviceNetworkId} 1 C25C 0xC5", "delay 500",
    "st rattr 0x${device.deviceNetworkId} 1 C25C 0x0c40", "delay 500"
    ]
    */
	
}

def configure() {
	/*String zigbeeId = swapEndianHex(device.hub.zigbeeId)
	log.debug "Confuguring Reporting and Bindings."
	def configCmds = [	
        "zdo bind 0x${device.deviceNetworkId}  0x01 0x0c 0x0101 {${device.zigbeeId}} {}",
    "delay 500", 
    "zcl global send-me-a-report 0104 0C 0099 0 67 {71}",
    "delay 500"
        //Lock Reporting
        //"zcl global send-me-a-report 0104 0 0x30 0 3600 {01}", "delay 500",
        //"send 0x${device.deviceNetworkId} 1 1", "delay 1000",

        //Battery Reporting
        //"zcl global send-me-a-report 1 0x20 0x20 5 3600 {}", "delay 200",
        //"send 0x${device.deviceNetworkId} 1 1", "delay 1500",


        //"zdo bind 0x${device.deviceNetworkId} 1 1 0101 {${device.zigbeeId}} {}", "delay 500",
        //"zdo bind 0x${device.deviceNetworkId} 1 1 1 {${device.zigbeeId}} {}"

    ]
    return configCmds + refresh()
    */
}

def test() {
	log.debug "test hit"
	//"st rattr 0x${device.deviceNetworkId} 0xc5 0c001 0"
    //"st cmd 0x${device.deviceNetworkId} 1 0x99 67 {71}"
    //"st cmd 0x${device.deviceNetworkId} 1 0x99 67 {73 6c 00 00 ff 10 10 80}" //turn led on to green for 120 sec
    //"st cmd 0x${device.deviceNetworkId} 1 0x99 73 {74 04 00 00 01 01}" //send ir command on channel 1
    //"st cmd 0x${device.deviceNetworkId} 1 0x99 73 {74 04 00 00 02 01}" //unknown
    //"st cmd 0x${device.deviceNetworkId} 1 0x99 73 {74 04 00 00 01 02}"  //double ir duration
    //0x74 == 't' for transmit on port 1
    //0x04 == length of payload to follow
    //0x00 is 'code slot' of cached ir code
    //0x00 is the ir format (normal=0, no_carrier=1, inverted=2, direct=3)
    // last two bytes are repeat count with LSByte first "little-endian"
    
    "st cmd 0x${device.deviceNetworkId} 1 0x99 73 {74 04 00 00 06 00}"
    /*
response of type 0x74 contains current status.
Following byte (0xc4) is a status = bitmask of DELAYING=0x80, COMPLETED=0x08, TRANSMITTING=0x04, CONTINUOUS=0x02, IDLE=0x00
So, the first response of 0x74, 0xC4... means transmitting, delaying (timed transmit)
the second response of 0x74 0x08 means transmit COMPLETED
The extra byte that follow the TRANSMITTING status are the CRC of the cached IR payload in the Z2IR. 
The driver can use this to verify that what the Z2IR is transmitting matches what the driver *thinks* is being transmitted.

*/
}

def test2() {
	log.debug "Hit test 2"
    "st rattr 0x${device.deviceNetworkId} 0xc5 0 0"
}

def getClusters() {
	log.debug "getClusters hit $device.deviceNetworkId"
    //"st rattr 0x${device.deviceNetworkId} 4 6 0x01"
	"zdo active 0x${device.deviceNetworkId}"
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
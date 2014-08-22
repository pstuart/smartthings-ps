/**
 *  ps_GetPublicIP
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

preferences {
    }
    
metadata {
	definition (name: "ps_GetPublicIP", namespace: "ps", author: "patrick@patrickstuart.com") {
		capability "Polling"
        
        
		attribute "publicIp", "string"
    }

	simulator {
    
    }

	tiles {
        valueTile("publicIp", "device.publicIp", inactiveLabel: false, decoration: "flat", columns:2) {
        	state "default", label:'${currentValue}', unit:"Public IP"
        }
        standardTile("refresh", "device.poll", inactiveLabel: false, decoration: "flat") {
            state "default", action:"polling.poll", icon:"st.secondary.refresh"
        }
        
        
        main "publicIp"
        details(["publicIp", "refresh"])
	}
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
	def map = stringToMap(description)
    def bodyString = new String(map.body.decodeBase64())
    log.debug bodyString
	def body = new XmlSlurper().parseText(bodyString)
    log.debug body
    def publicip2 = body.toString().replace("Current IP CheckCurrent IP Address: ","")
        log.debug publicip2
        sendEvent(name: 'publicIp', value: publicip2)
}

// handle commands
def poll() {
	log.debug "Executing 'poll'"
    login()
}




def login() {   
               
        def method = "GET"
        def host = "216.146.38.70"
        def hosthex = convertIPtoHex(host)
        def porthex = convertPortToHex(80)
        device.deviceNetworkId = "$hosthex:$porthex" 
         def headers = [:]
        headers.put("HOST", "$host:80")
        def path = "/"

            def hubAction = new physicalgraph.device.HubAction(
        method: method,
        path: path,
        headers: headers
        )
        log.debug hubAction
        hubAction
        }



private String convertIPtoHex(ipAddress) {
String hex = ipAddress.tokenize( '.' ).collect { String.format( '%02x', it.toInteger() ) }.join()
log.debug "IP address entered is $ipAddress and the converted hex code is $hex"
return hex
}
private String convertPortToHex(port) {
String hexport = port.toString().format( '%04x', port.toInteger() )
log.debug hexport
return hexport
}
private Integer convertHexToInt(hex) {
Integer.parseInt(hex,16)
}
private String convertHexToIP(hex) {
log.debug("Convert hex to ip: $hex")
[convertHexToInt(hex[0..1]),convertHexToInt(hex[2..3]),convertHexToInt(hex[4..5]),convertHexToInt(hex[6..7])].join(".")
}
private getHostAddress() {
def parts = device.deviceNetworkId.split(":")
log.debug device.deviceNetworkId
def ip = convertHexToIP(parts[0])
def port = convertHexToInt(parts[1])
return ip + ":" + port
}

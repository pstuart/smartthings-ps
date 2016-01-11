/**
 *  Get AWS IP
 *
 *  Copyright 2015 patrick@patrickstuart.com
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
	definition (name: "Get AWS IP", namespace: "ps", author: "patrick@patrickstuart.com") {
		capability "Polling"
		attribute "publicIp", "string"
    }

	simulator {
    }

	tiles(scale: 2) {
        valueTile("publicIp", "device.publicIp", inactiveLabel: false, decoration: "flat", width: 3, height: 2) {
        	state "default", label:'${currentValue}', unit:"AWS IP"
        }
        standardTile("refresh", "device.poll", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
            state "default", action:"polling.poll", icon:"st.secondary.refresh"
        }
        valueTile("IPs", "device.ips", inactiveLabel: false, decoration: "flat", width: 6, height:7) {
            state "default", label:'${currentValue}', unit:"AWS IP List"
        }
        
        main "publicIp"
        details(["publicIp", "refresh", "IPs"])
	}
}

def parse(String description) {
	log.debug "Parsing '${description}'"        
}

// handle commands
def poll() {
	log.debug "Executing 'poll'"
    getIP()
}

def getIP() {   
        def path = "/st/ip.php"
        def params = [
            uri: "http://valinor.net",
            path : path
        ]
        httpGet(params) { resp -> 
        	log.debug resp.data
            def publicip2 = resp.data.toString()
        	sendEvent(name: 'publicIp', value: publicip2)
            def currentIps = device.currentValue("ips")
            if (!currentIps) { currentIps = "" }
            log.debug currentIps
            if (!currentIps.contains("$publicip2"))
            {
            	currentIps += "\r\n" + publicip2
            }
            log.debug currentIps
            sendEvent(name: 'ips', value: currentIps)
        }
}
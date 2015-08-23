/**
 *  SmartSlots v1.0.0
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
	definition (name: "SmartSlots v1.0", namespace: "pstuart", author: "Patrick Stuart") {
    	command "spin"
	}

	simulator {
	}

	tiles {
    	valueTile("slot1", "device.slot1", decoration:"flat") {
        	state "default", label:'${currentValue}', unit:"", backgroundColor:"#ffffff", icon:""
        }
        valueTile("slot2", "device.slot2", decoration:"flat") {
        	state "default", label:'${currentValue}', unit:"", backgroundColor:"#ffffff", icon:""
        }
        valueTile("slot3", "device.slot3", decoration:"flat") {
        	state "default", label:'${currentValue}', unit:"", backgroundColor:"#ffffff", icon:""
        }
        valueTile("slotResult", "device.slotResult", decoration:"flat") {
        	state "default", label:'${currentValue}', unit:"", backgroundColor:"#ffffff", icon:""
        }
        standardTile("Spin", "device.button") {
        	state "Spin", label:'${name}', action:"spin", icon: "st.thermostat.thermostat-down", backgroundColor: '#e14902'
        }
        
        main "Spin"
        details(["slot1", "slot2", "slot3", "slotResult", "Spin"])
	}
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"

}

def spin() {
	log.debug "Spin Hit"
    Random random = new Random()
    //log.debug random.nextInt(10)+1
    sendEvent(name: 'slot1', value: random.nextInt(10)+1)
    sendEvent(name: 'slot2', value: random.nextInt(10)+1)
    sendEvent(name: 'slot3', value: random.nextInt(10)+1)
    def slot1 = device.currentValue("slot1").toInteger()
    def slot2 = device.currentValue("slot2").toInteger()
    def slot3 = device.currentValue("slot3").toInteger()
    
    log.debug "$slot1 $slot2 $slot3 ${slot1 == slot2 && slot2 == slot3}"
   
    if (slot1 == slot2 && slot2 == slot3) {
    	log.debug "Winner"
        sendEvent(name: 'slotResult', value: "Winner")
    } else {
    	log.debug "Not a Winner"
        sendEvent(name: 'slotResult', value: "Not a Winner")
    }
}

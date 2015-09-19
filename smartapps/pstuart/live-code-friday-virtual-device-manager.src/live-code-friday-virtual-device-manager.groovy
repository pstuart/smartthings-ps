/**
 *  Live Code Friday Virtual Device Manager
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
 *  Tasks
 	Create a dynamic pages interface
    Pick a device type
    add/delete that child 
    
 */
definition(
    name: "Live Code Friday Virtual Device Manager",
    namespace: "pstuart",
    author: "Patrick Stuart",
    description: "Live Code Friday Virtual Device Manager",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	page(name: "firstPage")
    page(name: "inputPage")
    page(name: "switchPage")
    page(name: "addSwitchPage")
    page(name: "viewSwitchPage")
    page(name: "deletePage")
}

def firstPage() {
	dynamicPage(name: "firstPage", title: "Where to first?", install: true, uninstall: true) {
		section("Main Menu") {
        	paragraph "Version 1.1"
        	href(page: "inputPage", title: "Let's add Devices!")
        	
        }
        section("Later") {
        	paragraph "More to come..."
        }
        
   }
}

def inputPage() {
	dynamicPage(name: "inputPage", title: "Every 'input' type") {
		section("devices") {
        	href(page: "switchPage", title: "Switches")
            
        }
        section("Later") {
        	paragraph "more devices coming soon"
        }
	}
}

def switchPage() {
	dynamicPage(name: "switchPage", title: "Switches") {
		section("Switches") {
        	 def childDevices = getChildDevices()
             if (childDevices) {
             	log.debug "The Child Devices are $childDevices"
                def devices = "Switches Installed:\r\nThis is a second line\r\n"
             	childDevices.each {
                	log.debug it.deviceNetworkId
                    //def tempDevice = getChildDevice(it)
                    //log.debug tempDevice
                    //devices = devices + "test\r\n"
                	href(page: "viewSwitchPage", title: it.name, params: [dni: it.deviceNetworkId])
                }
                //paragraph devices
             } else {
             paragraph "No Switches Exist"
             }
        }
        
        section("Add Switch") {
        	// List Switches getChildDevices()
            
            // Add A Switch addChildDevice()
            // View A Switch / Delete that switch go to switch view
            href(page: "addSwitchPage", title: "Add A Switch")
        }
	}
}

def addSwitchPage() {
	dynamicPage(name: "addSwitchPage", title: "Switches") {
		section("devices") {
        	//add new virtual switch
            def result = addChildSwitch()
        	paragraph "Switch Added ${result}"
            
            href(page: "switchPage", title: "Switches")
        }
	}
}

def viewSwitchPage(params) {
	dynamicPage(name: "addSwitchPage", title: "Switches") {
    	section("switch") {
        	paragraph "Switch View here $params"
            log.debug params
            href(page: "deletePage", title: "Delete", params: [dni: params.dni])
            //deleteChildDevice(it.deviceNetworkId)
        }
    }
}

def deletePage(params) {
	dynamicPage(name: "deletePage", title: "Delete") {
    	section("switch") {
        	paragraph "Deleted Swithc with DNI of $params.dni"
            log.debug "Deleting $params.dni"
            //def delete = getChildDevices().findAll { it?.contains(params.dni) }
			//log.debug delete
            def delete = getChildDevice(params.dni)
            //removeChildDevices(delete)
            deleteChildDevice(delete.deviceNetworkId)
            
            href(page: "switchPage", title: "Switches")
        }
    }
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	// TODO: subscribe to attributes, devices, locations, etc.
}


def addChildSwitch() {
	//Get all devices installed as children
    def childDevices = getChildDevices()
    def counter = childDevices.size() + 1
    def dni = "pstuartSwitch_$counter" // TODO create random string /guid
    def deviceName = "Generic Switch $counter"
    
    log.debug dni
    def childDevice = addChildDevice("pstuart", "Generic Switch", dni, null, [name:deviceName])
    log.debug childDevice
    return childDevice
    //return dni
    
    
}
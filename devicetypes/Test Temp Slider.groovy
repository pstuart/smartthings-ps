/**
 *  ps_TestTempSlider
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
	definition (name: "ps_TestTempSlider", namespace: "ps", author: "patrick@patrickstuart.com") {
		capability "Polling"
		capability "Thermostat"
		capability "Temperature Measurement"
	}
    
    
    preferences {
    input("SliderMin", "Double", title: "Slider Min Adjust", description: "Set Slider Min")
	input("SliderMax", "Double", title: "Slider Max Adjust", description: "Set Slider Max")
	}
    

	simulator {
		// TODO: define status and reply messages here
       
	}

	tiles {
		// TODO: define your main and details tiles here
        valueTile("temperature", "device.temperature", width: 2, height: 2) {
			state("temperature", label:'${currentValue}°', unit:"F",
				backgroundColors:[
					[value: 31, color: "#153591"],
					[value: 44, color: "#1e9cbb"],
					[value: 59, color: "#90d2a7"],
					[value: 74, color: "#44b621"],
					[value: 84, color: "#f1d801"],
					[value: 95, color: "#d04e00"],
					[value: 96, color: "#bc2323"]
				]
			)
		}
        valueTile("thermostatSetpoint", "device.thermostatSetpoint", width: 1, height: 1, decoration: "flat") {
			state "thermostatSetpoint", label:'${currentValue}'
		}
		controlTile("coolSliderControl", "device.coolingSetpoint", "slider", height: 1, width: 2, inactiveLabel: false) {
			state "setCoolingSetpoint", action:"thermostat.setCoolingSetpoint", backgroundColor: "#1e9cbb"
		}
		standardTile("refresh", "device.thermostatMode", inactiveLabel: false, decoration: "flat") {
			state "default", action:"refresh.refresh", icon:"st.secondary.refresh"
		}
        main "temperature"
        details(["temperature", "thermostatSetpoint","coolSliderControl", "refresh"])
	
	}
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
	// TODO: handle 'temperature' attribute
	// TODO: handle 'heatingSetpoint' attribute
	// TODO: handle 'coolingSetpoint' attribute
	// TODO: handle 'thermostatSetpoint' attribute
	// TODO: handle 'thermostatMode' attribute
	// TODO: handle 'thermostatFanMode' attribute
	// TODO: handle 'thermostatOperatingState' attribute
	// TODO: handle 'temperature' attribute

}

// handle commands
def poll() {
	log.debug "Executing 'poll'"
	// TODO: handle 'poll' command
}

def setHeatingSetpoint() {
	log.debug "Executing 'setHeatingSetpoint'"
	// TODO: handle 'setHeatingSetpoint' command
}

def setCoolingSetpoint(tempF) {
	log.debug "Executing 'setCoolingSetpoint'"
	// TODO: handle 'setCoolingSetpoint' command
    log.debug tempF
    def privMin = 40
    def privMax = 90
    def PrefSet = false
    if (is(SliderMin))
    {
    PrefSet = true
    }
    if (is(SliderMax))
    {
    PrefSet = true
    }
    
    // So range is 1-100 need to map to a new min and max formula is y = ((x - a1)/(a2 - a1)) * (b2 - b1) + b1
    //OldRange = (OldMax - OldMin)  
//NewRange = (NewMax - NewMin)  
//NewValue = (((OldValue - OldMin) * NewRange) / OldRange) + NewMin
    //def newtempF = ((tempF.toInteger() - 1)/(100 - 1) * (privMax.toInteger() - privMin.toInteger()) + privMax.toInteger())
    def newtempF = (((tempF.toInteger() - 1) * (privMax.toInteger() - privMin.toInteger()) / (100/1) + privMin.toInteger()))
    log.debug newtempF
    //Set min and max for range
    
	sendEvent("name":"thermostatSetpoint", "value":newtempF)
}

def off() {
	log.debug "Executing 'off'"
	// TODO: handle 'off' command
}

def heat() {
	log.debug "Executing 'heat'"
	// TODO: handle 'heat' command
}

def emergencyHeat() {
	log.debug "Executing 'emergencyHeat'"
	// TODO: handle 'emergencyHeat' command
}

def cool() {
	log.debug "Executing 'cool'"
	// TODO: handle 'cool' command
}

def setThermostatMode() {
	log.debug "Executing 'setThermostatMode'"
	// TODO: handle 'setThermostatMode' command
}

def fanOn() {
	log.debug "Executing 'fanOn'"
	// TODO: handle 'fanOn' command
}

def fanAuto() {
	log.debug "Executing 'fanAuto'"
	// TODO: handle 'fanAuto' command
}

def fanCirculate() {
	log.debug "Executing 'fanCirculate'"
	// TODO: handle 'fanCirculate' command
}

def setThermostatFanMode() {
	log.debug "Executing 'setThermostatFanMode'"
	// TODO: handle 'setThermostatFanMode' command
}

def auto() {
	log.debug "Executing 'auto'"
	// TODO: handle 'auto' command
}

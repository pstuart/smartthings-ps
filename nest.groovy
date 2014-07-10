/**
 *  ps_Nest Thermostat
 *
 *  Copyright 2014 Patrick Stuart
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
 *
 *  Original Author: dianoga7@3dgo.net
 *	Author: patrick@patrickstuart.com
 *  Original Code: https://github.com/smartthings-users/device-type.nest
 *
 * INSTALLATION
 * =========================================
 * 1) Create a new device type (https://graph.api.smartthings.com/ide/devices)
 *     Name: Nest
 *     Author: dianoga7@3dgo.net
 *     Capabilities:
 *         Polling
 *         Relative Humidity Measurement
 *         Thermostat
 *     Custom Attributes:
 *         presence
 *     Custom Commands:
 *         away
 *         present
 *         setPresence
 *
 * 2) Create a new device (https://graph.api.smartthings.com/device/list)
 *     Name: Your Choice
 *     Device Network Id: Your Choice
 *     Type: Nest (should be the last option)
 *     Location: Choose the correct location
 *     Hub/Group: Leave blank
 *
 * 3) Update device preferences
 *     Click on the new device to see the details.
 *     Click the edit button next to Preferences
 *     Fill in your information.
 *     To find your serial number, login to http://home.nest.com. Click on the thermostat 
 *     you want to control. Under settings, go to Technical Info. Your serial number is 
 *     the second item.
 *
 * 4) That's it, you're done.
 *     
 * Copyright (C) 2013 Brian Steere <dianoga7@3dgo.net>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this 
 * software and associated documentation files (the "Software"), to deal in the Software 
 * without restriction, including without limitation the rights to use, copy, modify, 
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to 
 * permit persons to whom the Software is furnished to do so, subject to the following 
 * conditions: The above copyright notice and this permission notice shall be included 
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

preferences {
    input("username", "text", title: "Username", description: "Your Nest username (usually an email address)")
    input("password", "password", title: "Password", description: "Your Nest password")
    input("serial", "text", title: "Serial #", description: "The serial number of your thermostat")
}
 
 // for the UI
metadata {
    definition (name: "Nest Thermostat", author: "patrick@patrickstuart.com") {
        capability "Polling"
        capability "Relative Humidity Measurement"
        capability "Thermostat"
        capability "Actuator"
		capability "Sensor"
        capability "Refresh"
		capability "Configuration"
        capability "Location Mode"

        attribute "presence", "string"

        command "away"
        command "present"
        command "setPresence"
        command "setTempUp"
        command "setTempDown"
        command "setTempUpHeat"
        command "setTempDownHeat"
	}
    simulator {
    
    }

    tiles {
        valueTile("temperature", "device.temperature", width: 1, height: 1, canChangeIcon: false) {
            state("temperature", label: '${currentValue}°', unit:"F", backgroundColors: [
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
        
        standardTile("TempUp", "device.button") { state "Cool Up", label:'${name}', action:"setTempUp", icon: "st.thermostat.thermostat-up"}
        standardTile("TempDown", "device.button") { state "Cool Down", label:'${name}', action:"setTempDown", icon: "st.thermostat.thermostat-down", backgroundColor: '#ffffff'}
        
        standardTile("TempUpHeat", "device.button") { state "Heat Up", label:'${name}', action:"setTempUpHeat", icon: "st.thermostat.thermostat-up"}
        standardTile("TempDownHeat", "device.button") { state "Heat Down", label:'${name}', action:"setTempDownHeat", icon: "st.thermostat.thermostat-down", backgroundColor: '#ffffff'}
        
        
        standardTile("thermostatMode", "device.thermostatMode", inactiveLabel: false, decoration: "flat") {
            state "heat", label:'', action:"thermostat.heat", icon: "st.thermostat.heat", backgroundColor: '#E14902'
            state "cool", label:'', action:"thermostat.cool", icon: "st.thermostat.cool", backgroundColor: '#003CEC'
            state "range", label:'', action:"thermostat.auto", icon: "st.thermostat.auto", backgroundColor: '#000000'
            state "off", label:'', action:"thermostat.off", icon: "st.thermostat.heating-cooling-off"
        }
        standardTile("thermostatFanMode", "device.thermostatFanMode", inactiveLabel: false, decoration: "flat") {
            state "auto", label:'', action:"thermostat.fanOn", icon: "st.thermostat.fan-auto"
            state "on", label:'', action:"thermostat.fanCirculate", icon: "st.thermostat.fan-on"
            state "circulate", label:'', action:"thermostat.fanAuto", icon: "st.thermostat.fan-circulate"
        }
        valueTile("coolingSetpoint", "device.coolingSetpoint", inactiveLabel: false, decoration: "flat") {
            state "default", label:'${currentValue}°', unit:"F", backgroundColor:"#ffffff", icon:""
        }
        
        valueTile("heatingSetpoint", "device.heatingSetpoint", inactiveLabel: false, decoration: "flat") {
            state "default", label:'${currentValue}°', unit:"F", backgroundColor:"#ffffff", icon:""
        }
        
        valueTile("humidity", "device.humidity", inactiveLabel: false, decoration: "flat") {
            state "default", label:'${currentValue}%', unit:"Humidity"
        }
        standardTile("presence", "device.presence", inactiveLabel: false, decoration: "flat") {
            state "present", label:'${name}', action:"away", icon: "st.Home.home2"
            state "away", label:'${name}', action:"present", icon: "st.Transportation.transportation5"
        }
        standardTile("refresh", "device.thermostatMode", inactiveLabel: false, decoration: "flat") {
            state "default", action:"polling.poll", icon:"st.secondary.refresh"
        }
        main "temperature"
        details(["temperature", "thermostatMode",  "thermostatFanMode", "TempUp", "TempDown", //"coolSliderControl", 
        "coolingSetpoint", "TempUpHeat", "TempDownHeat", "heatingSetpoint", "humidity", "presence", "refresh"])
    }
}

// parse events into attributes
def parse(String description) {
	log.debug "Recieved a parse request for: $description"
    
}

def setTempUp() { 
def newtemp = device.currentValue("coolingSetpoint").toInteger() + 1
sendEvent(name: 'coolingSetpoint', value: newtemp)
setTargetTemp(newtemp)
}

def setTempDown() { 
def newtemp = device.currentValue("coolingSetpoint").toInteger() - 1
sendEvent(name: 'coolingSetpoint', value: newtemp)
setTargetTemp(newtemp)
}

def setTempUpHeat() { 
def newtemp = device.currentValue("heatingSetpoint").toInteger() + 1
sendEvent(name: 'heatingSetpoint', value: newtemp)
setCoolingSetpoint(newtemp)
}

def setTempDownHeat() { 
def newtemp = device.currentValue("heatingSetpoint").toInteger() - 1
sendEvent(name: 'heatingSetpoint', value: newtemp)
setHeatingSetpoint(newtemp)
}


// handle commands
def setHeatingSetpoint(temp) {
    setTargetTemp(temp)
}

def setCoolingSetpoint(temp) {
    setTargetTemp(temp)
}

def setTargetTemp(temp) {
def tmode = device.currentValue("thermostatMode")
if (tmode == "range")
{
log.debug "in auto mode about to send to api"
api('temperature', [
					'target_change_pending': true, 
                    'target_temperature_low': fToC(device.currentValue("heatingSetpoint")), 
                    'target_temperature_high': fToC(device.currentValue("coolingSetpoint")), 
                    'target_temperature': fToC(temp)
                    ]) {        }
}
else
{
	if (tmode == "heat")
    {
    api('temperature', ['target_change_pending': true, 'target_temperature': fToC(device.currentValue("heatingSetpoint"))]) {
                }
        }
        else if (tmode == "cool")
        {
        api('temperature', ['target_change_pending': true, 'target_temperature': fToC(device.currentValue("coolingSetpoint"))]) {
                }
        }
    }
}




def off() {
    setThermostatMode('off')
    log.debug "off"
}

def heat() {
    setThermostatMode('heat')
    log.debug "heat"
}

def emergencyHeat() {
    setThermostatMode('heat')
    log.debug "eheat"
}

def cool() {
    setThermostatMode('cool')
    log.debug "cool"
}

def auto() {
	def tmode = device.currentValue("thermostatMode")
	setThermostatMode(tmode)
    log.debug "auto Mode hit"
}

def setThermostatMode(mode) {
	log.debug "mode is $mode"
    // rotate through 4 options heat,cool, range, off
    switch (mode)
	{
	case 'heat' : 
    mode = "cool" 
    break
	case 'cool' : 
    mode = "range"
    break
	case 'range' : 
    mode = "off" 
    break
	case 'off' : 
    mode = "heat" 
    break
	default : 
    mode = "off" 
    break
	}
    
    api('thermostat_mode', ['target_change_pending': true, 'target_temperature_type': mode]) {
        sendEvent(name: 'thermostatMode', value: mode)
    }
}

def fanOn() {
    setThermostatFanMode('on')
}

def fanAuto() {
    setThermostatFanMode('auto')
}

def fanCirculate() {
    setThermostatFanMode('circulate')
}

def setThermostatFanMode(mode) {    
    def modes = [
        on: ['fan_mode': 'on'],
        auto: ['fan_mode': 'auto'],
        circulate: ['fan_mode': 'duty-cycle', 'fan_duty_cycle': 900]
    ]
    
    api('fan_mode', modes.getAt(mode)) {
        sendEvent(name: 'thermostatFanMode', value: mode)
    }
}

def away() {
	log.debug "Away Nest"
    setPresence('away')
}

def present() {
	log.debug "Home Nest"
    setPresence('present')
}

def setPresence(status) {
    log.debug "Status: $status"
    api('presence', ['away': status == 'away', 'away_timestamp': new Date().getTime(), 'away_setter': 0]) {
        sendEvent(name: 'presence', value: status)
    }
}

def refresh() {
	log.debug "nest refresh"
}

def poll() {
    log.debug "Executing 'poll'"
    api('status', []) {
    log.debug data.shared
        data.device = it.data.device.getAt(settings.serial)
        data.shared = it.data.shared.getAt(settings.serial)
        data.structureId = it.data.link.getAt(settings.serial).structure.tokenize('.')[1]
        data.structure = it.data.structure.getAt(data.structureId)
        
        data.device.fan_mode = data.device.fan_mode == 'duty-cycle'? 'circulate' : data.device.fan_mode
        data.structure.away = data.structure.away? 'away' : 'present'
                
        sendEvent(name: 'humidity', value: data.device.current_humidity)
        sendEvent(name: 'temperature', value: cToF((data.shared.current_temperature) as Double).round(), state: data.device.target_temperature_type)
        sendEvent(name: 'thermostatFanMode', value: data.device.fan_mode)
        sendEvent(name: 'thermostatMode', value: data.shared.target_temperature_type)
        //if temp type is ranged otherwise use target_temp
        if (data.shared.target_temperature_type == "range")
        {
        sendEvent(name: 'coolingSetpoint', value: cToF((data.shared.target_temperature_high) as Double).round()) 
        sendEvent(name: 'heatingSetpoint', value: cToF((data.shared.target_temperature_low) as Double).round())
        }
        else if (data.shared.target_temperature_type == "cool")
        {
        sendEvent(name: 'coolingSetpoint', value: cToF((data.shared.target_temperature) as Double).round())
        }
        else if (data.shared.target_temperature_type == "heat")
        {
        sendEvent(name: 'heatingSetpoint', value: cToF((data.shared.target_temperature) as Double).round())
        }
        else
        {
        sendEvent(name: 'coolingSetpoint', value: 0) //changed for auto mode for heat/cool?
        sendEvent(name: 'heatingSetpoint', value: 0)
        }
        
        sendEvent(name: 'presence', value: data.structure.away)
        
        //ambient_temperature_f?
        //has_leaf?
        //name = tstat name
    }
}

def api(method, args = [], success = {}) {
    if(!isLoggedIn()) {
        log.debug "Need to login"
        login(method, args, success)
        return
    }

    def methods = [
        'status': [uri: "/v2/mobile/${data.auth.user}", type: 'get'],
        'fan_mode': [uri: "/v2/put/device.${settings.serial}", type: 'post'],
        'thermostat_mode': [uri: "/v2/put/shared.${settings.serial}", type: 'post'],
        'temperature': [uri: "/v2/put/shared.${settings.serial}", type: 'post'],
        'presence': [uri: "/v2/put/structure.${data.structureId}", type: 'post']
    ]
    
    def request = methods.getAt(method)
    log.debug method
    //Potentially this is the only spot that triggers from Mode Changes
    log.debug "Current mode = ${location.mode}"
    
    log.debug "Logged in"
    log.debug "Arguments to send are : $args"
    doRequest(request.uri, args, request.type, success)
}

// Need to be logged in before this is called. So don't call this. Call api.
def doRequest(uri, args, type, success) {
    log.debug "Calling $type : $uri : $args"
    
    if(uri.charAt(0) == '/') {
        uri = "${data.auth.urls.transport_url}${uri}"
    }
    
    def params = [
        uri: uri,
        headers: [
            'X-nl-protocol-version': 1,
            'X-nl-user-id': data.auth.userid,
            'Authorization': "Basic ${data.auth.access_token}"
        ],
        body: args
    ]
    
    try {
        if(type == 'post') {
            httpPostJson(params, success)
        } else if (type == 'get') {    
            httpGet(params, success)
        }
    } catch (Throwable e) {
        login()
    }
}

def login(method = null, args = [], success = {}) {    
    def params = [
        uri: 'https://home.nest.com/user/login',
        body: [username: settings.username, password: settings.password]
    ]   
    
    httpPost(params) {response -> 
        data.auth = response.data
        data.auth.expires_in = Date.parse('EEE, dd-MMM-yyyy HH:mm:ss z', response.data.expires_in).getTime()
        log.debug data.auth
        
        api(method, args, success)
    }
}

def isLoggedIn() {
    if(!data.auth) {
        log.debug "No data.auth"
        return false
    }
    
    def now = new Date().getTime();
    return data.auth.expires_in > now
}

def cToF(temp) {
    return temp * 1.8 + 32
}

def fToC(temp) {
    return (temp - 32) / 1.8
}

def configure() {
	log.debug "Configure() nest hit"
}

def switchMode() {
	def currentMode = device.currentState("thermostatMode")?.value
    log.debug "nest current mode is $currentMode"
    }

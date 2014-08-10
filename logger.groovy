/**
 *  Xively Logger
 *
 *  Author: patrick@patrickstuart.com
 *  Date: 2014-04-14
 *
 *
 */

// Automatically generated. Make future change here.
definition(
    name: "Xively Logger",
    namespace: "ps",
    author: "patrick@patrickstuart.com",
    description: "Xively Logger",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")

preferences {
    section("Log devices...") {
        input "temperatures", "capability.temperatureMeasurement", title: "Temperatures", required:false, multiple: true
        input "humidities", "capability.relativeHumidityMeasurement", title: "Humidities", required:false, multiple: true
        input "contacts", "capability.contactSensor", title: "Contacts", required: false, multiple: true
        //input "accelerations", "capability.accelerationSensor", title: "Accelerations", required: false, multiple: true
        input "motions", "capability.motionSensor", title: "Motions", required: false, multiple: true
        input "switches", "capability.switch", title: "Switches", required: false, multiple: true
        input "batteries", "capability.battery", title: "Batteries", required:false, multiple: true
        input "thermostats", "capability.thermostat", title: "Select thermostat", required: false, multiple: true
    }
/*
    section ("ThinkSpeak channel id...14498") {
        input "channelId", "number", title: "Channel id"
    }

    section ("ThinkSpeak write key...PEYDNNJLT4WRG6I7") {
        input "channelKey", "text", title: "Channel key"
    }
  */  
    section ("Xively Info ") {
    	input "xi_apikey", "text", title: "Xively API Key"
        input "xi_feed", "number", title: "Xively Feed ID"
    }
}

def installed() {
    initialize()
}

def updated() {
    unsubscribe()
    
    initialize()
}

def initialize() {
	state.clear()
//	if(!state.subscribe) {
    	unschedule(checkSensors)
		//schedule("0 */15 * * * ?", "checkSensors") //Check every 5 mins
        schedule("0 */15 * * * ?", "checkSensors")
    	subscribe(app, appTouch)
//		state.subscribe = true
//	}
	/*
    subscribe(temperatures, "temperature", handleTemperatureEvent)
    subscribe(humidities, "humidities", handlehumidityEvent)
    subscribe(contacts, "contact", handleContactEvent)
    subscribe(accelerations, "acceleration", handleAccelerationEvent)
    subscribe(motions, "motion", handleMotionEvent)
    subscribe(switches, "switch", handleSwitchEvent)
    subscribe(thermostats, "temperature", handleThermostatTemperature)
    subscribe(thermostats, "humidity", handleThermostatHumidity)
    */

    //updateChannelInfo()
    //log.debug state.fieldMap
}

def appTouch(evt) {
	log.debug "appTouch: $evt"
	checkSensors()
}

/*
def handleTemperatureEvent(evt) {
	log.debug "EVENT is $evt.displayName"
    log.debug "EVENT VALUE is $evt.value"
    //log.debug "Device ID is $evt.deviceId"
    
    logField("temperature", evt) { it.toString() }
}

def handlehumidityEvent(evt) {
	log.debug "EVENT is $evt.displayName"
    log.debug "EVENT VALUE is $evt.value"
    
    logField("humidity", evt) { it.toString() }
}

def handleContactEvent(evt) {
	log.debug "EVENT is $evt.displayName"
    log.debug "EVENT VALUE is $evt.value"
    logField("contact", evt) { it == "open" ? "1" : "0" }
}

def handleAccelerationEvent(evt) {
	log.debug "EVENT is $evt.displayName"
    log.debug "EVENT VALUE is $evt.value"
    logField("acceleration", evt) { it == "active" ? "1" : "0" }
}

def handleMotionEvent(evt) {
	log.debug "EVENT is $evt.displayName"
    log.debug "EVENT VALUE is $evt.value"
    logField("motion", evt) { it == "active" ? "1" : "0" }
}

def handleSwitchEvent(evt) {
	log.debug "EVENT is $evt.displayName"
    log.debug "EVENT VALUE is $evt.value"
    logField("switch", evt) { it == "on" ? "1" : "0" }
}

def handleThermostatTemperature(evt) {
	log.debug "EVENT is $evt.displayName"
    log.debug "EVENT VALUE is $evt.value"
    log.debug "TStat Temperature event: $evt.value"
    logField("thermostat.temperature", evt) { it.toString() }
}

def handleThermostatHumidity(evt) {
	log.debug "EVENT is $evt.displayName"
    log.debug "EVENT VALUE is $evt.value"
	log.debug "TStat Humidity event: $evt.value"
    logField("thermostat.humidity", evt) { it.toString() }
}
*/

def checkSensors() {
	//updateChannelInfo()    
	//settings.sensors.each{ k -> log.debug k }
    //https://graph.api.smartthings.com/api/hubs/idHashString/devices 
    //https://graph.api.smartthings.com/device/listJson
    
    
    def logitems = []
    //def states = [:]
    for (t in settings.temperatures) {
	    //def deviceState = captureState(t)
		//log.debug "$t.name : $t.displayName : $deviceState.temperature"
        //def deviceID = t.id
		//states[deviceID] = deviceState
        //if (deviceID == "temperature") {
        //logitems.add([t.displayName, "temperature", deviceState.temperature] )
        
        logitems.add([t.displayName, "temperature", t.latestValue("temperature")] )
        state[t.displayName + ".temp"] = t.latestValue("temperature")
        
        //logField2("temperature", t.displayName, deviceState.temperature )
        //}
    }
    for (t in settings.humidities) {
	    //def deviceState = captureState(t)
		//def deviceID = t.id
		//states[deviceID] = deviceState
        //logitems.add([t.displayName, "humidity", deviceState.humidity])
        
        logitems.add([t.displayName, "humidity", t.latestValue("humidity")] )
        state[t.displayName + ".humidity"] = t.latestValue("humidity")
    }
    for (t in settings.batteries) {
	    //def deviceState = captureState(t)
        //log.debug deviceState
		//def deviceID = t.id
		//states[deviceID] = deviceState
        //logitems.add([t.displayName, "battery", deviceState.battery])
        
        logitems.add([t.displayName, "battery", t.latestValue("battery")] )
        state[t.displayName + ".battery"] = t.latestValue("battery")
    }
    //log.debug settings.batteries.size()
    for (t in settings.contacts) {
	    //def deviceState = captureState(t)
		//log.debug deviceState
        //def deviceID = t.id
		//states[deviceID] = deviceState
        //logitems.add([t.displayName, "contact", deviceState.contact])
        
        logitems.add([t.displayName, "contact", t.latestValue("contact")] )
        state[t.displayName + ".contact"] = t.latestValue("contact")
    }
    
    for (t in settings.motions) {
	    //def deviceState = captureState(t)
        //log.debug deviceState
		//def deviceID = t.id
		//states[deviceID] = deviceState
        //logitems.add([t.displayName, "motion", deviceState.motion])
        
        logitems.add([t.displayName, "motion", t.latestValue("motion")] )
        state[t.displayName + ".motion"] = t.latestValue("motion")
    }
    
    for (t in settings.switches) {
	    //def deviceState = captureState(t)
		//log.debug deviceState
        //def deviceID = t.id
		//states[deviceID] = deviceState
        //logitems.add([t.displayName, "contact", deviceState.switch])
        
        logitems.add([t.displayName, "switch", t.latestValue("switch")] )
        state[t.displayName + ".switch"] = t.latestValue("switch")
    }
    
    for (t in settings.thermostats) {
	    //def deviceState = captureState(t)
		//def deviceID = t.id + "tstat"
		//states[deviceID] = deviceState
        //logitems.add([t.displayName, "thermostat.temperature", deviceState.coolingSetpoint])
        //log.debug t
        logitems.add([t.displayName, "thermostat.coolingSetpoint", t.latestValue("coolingSetpoint")] )
        state[t.displayName + ".coolingSetpoint"] = t.latestValue("coolingSetpoint")
    }
    
    //log.debug states
    //log.debug logitems
    logField2(logitems)
    
}

private getFieldMap(channelInfo) {
    def fieldMap = [:]
    channelInfo?.findAll { it.key?.startsWith("field") }.each { fieldMap[it.value?.trim()] = it.key }
    return fieldMap
}

/*
private updateChannelInfo() {
    log.debug "Retrieving channel info for ${channelId}"

    def url = "http://api.thingspeak.com/channels/${channelId}/feed.json?key=${channelKey}&results=0"
    httpGet(url) {
        response ->
        if (response.status != 200 ) {
            log.debug "ThingSpeak data retrieval failed, status = ${response.status}"
        } else {
            state.channelInfo = response.data?.channel
        }
        log.debug response.data
    }

    state.fieldMap = getFieldMap(state.channelInfo)
}
*/

/*
private logField(capability, evt, Closure c) {
	log.debug "Got Log Request: $capability $evt.displayName $evt.value"
    def deviceName = evt.displayName.trim() + "." + capability
    def fieldNum = state.fieldMap[deviceName]
    if (!fieldNum) {
        log.debug "Device '${deviceName}' has no field"
        return
    }

    def value = c(evt.value)
    log.debug "Logging to channel ${channelId}, ${fieldNum}, value ${value}"

    def url = "http://api.thingspeak.com/update?key=${channelKey}&${fieldNum}=${value}"
    httpGet(url) { 
        response -> 
        if (response.status != 200 ) {
            log.debug "ThingSpeak logging failed, status = ${response.status}"
        }
        //log.debug response.data
    }
}
*/

private logField2(logItems) {
	def fieldvalues = ""
	log.debug logItems
    
    /*
    logItems.each() { item -> 
        //log.debug item[0]
        //log.debug item[1]
        //log.debug item[2]
        //log.debug "Got Log Item: $item.get(0) $item.get(1) $item.get(2)"
        
        def deviceName = item[0] + "." + item[1]
        //log.debug deviceName
        
        def fieldNum = state.fieldMap[deviceName]
        //log.debug fieldNum
        if (!fieldNum) {
            log.debug "Device '${deviceName}' has no field"
            return
        }
        //log.debug fieldNum
        
        
        
        //build string &fieldnum=value
        fieldvalues += "&$fieldNum=" +item[2]
        
        //def value = c(evt.value)
        log.debug "Logging to channel ${channelId}, ${fieldNum}, value ${item[2]}"
        
	}
    log.debug "String to send is: $fieldvalues"
    
    
    def url = "http://api.thingspeak.com/update?key=${channelKey}${fieldvalues}"
    log.debug url
    httpGet(url) { 
        response -> 
        if (response.status != 200 ) {
            log.debug "ThingSpeak logging failed, status = ${response.status}"
        }
        //log.debug response.data
    }
    
    */
    
    def xivelyinfo = ""
    logItems.eachWithIndex() { item, i ->
    //need to build string {"id":"channel","current_value": "value"} plus comma
    def channelname = item[0].replace(" ","_") + "_" + item[1]
    xivelyinfo += "{\"id\":\"${channelname}\",\"current_value\":\"${item[2]}\"}"
    if (i.toInteger() + 1 < logItems.size())
    {
    xivelyinfo += ","
    }
    
    }
    log.debug xivelyinfo
    def uri = "https://api.xively.com/v2/feeds/${xi_feed}.json"
    //def json = "{\"version\":\"1.0.0\",\"datastreams\":[{\"id\":\"${channel}\",\"current_value\":\"${value}\"}]}"
	def json = "{\"version\":\"1.0.0\",\"datastreams\":[${xivelyinfo} ]}"
    
    def headers = [
        "X-ApiKey" : "${xi_apikey}"
    ]

    def params = [
        uri: uri,
        headers: headers,
        body: json
    ]
	log.debug params.body
    httpPutJson(params) {response -> parseHttpResponse(response)}
}

def parseHttpResponse(response) {
    log.debug "HTTP Response: ${response}"
}

def captureState(theDevice) {
//	def deviceAttributes = theDevice.supportedAttributes
	def deviceAttrValue = [:]
	for ( attr in theDevice.supportedAttributes ) {
		def attrName = "${attr}"
		def attrValue = theDevice.currentValue(attrName)
		deviceAttrValue[attrName] = attrValue
	}
	return deviceAttrValue
}

/**
 *  ps_GE Link Bulb
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
 *	Credit goes to Chad Monroe @cmonroe for his original code
 *  
 */
metadata {
	definition (name: "ps_GE Link Bulb", namespace: "ps", author: "patrick@patrickstuart.com") {
	
		capability "Switch Level"
		capability "Actuator"
		capability "Switch"
		capability "Configuration"
		capability "Polling"
		capability "Refresh"
		capability "Sensor"
		
        command "getClusters"
		fingerprint endpointId: "1", profileId: "0104", inClusters: "0000,0003,0004,0005,0006,0008,1000", outClusters: "0019"
	}

	/* simulator metadata */
	simulator 
	{
		/* status messages */
		status "on": "on/off: 1"
		status "off": "on/off: 0"

		/* reply messages */
		reply "zcl on-off on": "on/off: 1"
		reply "zcl on-off off": "on/off: 0"
	}

	/* ui tile definitions */
	tiles 
	{
		standardTile( "switch", "device.switch", width: 2, height: 2, canChangeIcon: true ) 
		{
			state "off", label: '${name}', action: "switch.on", icon: "st.Lighting.light13", backgroundColor: "#ffffff"
			state "on", label: '${name}', action: "switch.off", icon: "st.Lighting.light11", backgroundColor: "#79b821"
		}

		standardTile( "refresh", "device.switch", inactiveLabel: false, decoration: "flat" ) 
		{
			state "default", label:"", action:"refresh.refresh", icon:"st.secondary.refresh"
		}

		controlTile( "levelSliderControl", "device.level", "slider", height: 1, width: 3, inactiveLabel: false ) 
		{
			state "level", action: "switch level.setLevel"
		}

		valueTile( "level", "device.level", inactiveLabel: false, decoration: "flat" ) 
		{
			state "level", label:'${currentValue} %', unit:"%", backgroundColor:"#ffffff"
		}

		main(["switch"])
		details( ["switch", "refresh", "level", "levelSliderControl"] )
	}
}

/* parse incoming device messages to generate events */
def parse(String description) 
{
	log.trace description
	if ( description?.startsWith("catchall:") ) 
	{
		def msg = zigbee.parse(description)
		//log.trace "catchall"
		//log.trace msg
		log.trace "data: $msg.data"
	}
	else 
	{
		def name = description?.startsWith( "on/off: " ) ? "switch" : null
		def value = name == "switch" ? ( description?.endsWith( " 1" ) ? "on" : "off" ) : null
		def result = createEvent( name: name, value: value )

		log.debug "parse returned ${result?.descriptionText}"
		return result
	}
}

def getClusters() {
	log.debug "getClusters hit $device.deviceNetworkId"
    //"st rattr 0x${device.deviceNetworkId} 4 6 0x01"
	"zdo active 0x${device.deviceNetworkId}"
    //x = new BigInteger(device.endpointId, 16).toString()
    log.debug "Device Endpoint is $x"
}

def on() 
{
	def cmds = []
    def level = device.latestValue( "level" ) as Integer ?: 0

    log.trace "on(): lastLevel=${level}"

    if ( ( level > 0 ) && ( level < 100 ) )
    {
    	def levelHex = new BigInteger( Math.round( level * 255 / 100 ).toString()).toString( 16 )

    	log.trace "on(): lastLevel=${level} valid, restoring to level=${levelHex}"

		sendEvent( name: "level", value: level )
		sendEvent( name: "switch.setLevel", value: level )

    	//cmds << "st cmd 0x${device.deviceNetworkId} ${endpointId} 6 1 {}"
		cmds << "st cmd 0x${device.deviceNetworkId} ${endpointId} 8 4 {${levelHex} 0000}"
	}
	else
	{
		sendEvent( name: "level", value: 99 )
		sendEvent( name: "switch.setLevel", value: 99 )

		cmds << "st cmd 0x${device.deviceNetworkId} ${endpointId} 6 1 {}"
	}

	log.debug "on(): zigbee cmds: ${cmds}"
	cmds
}

def off() 
{
	def cmds = []

	log.trace "off()"

	sendEvent( name: "switch", value: "off" )

	cmds << "st cmd 0x${device.deviceNetworkId} ${endpointId} 6 0 {}"

	log.debug "off(): zigbee cmds: ${cmds}"
	cmds
}

def refresh() 
{
	def cmds = []

	log.trace "refresh()"

	cmds << "st rattr 0x${device.deviceNetworkId} 1 6 0"

    log.debug "refresh(): zigbee cmds: ${cmds}"
	cmds
}

def poll()
{
	log.debug "poll(): calling refresh()"
	refresh()
}

def setLevel(value) 
{
	def cmds = []
	def level = value as Integer

	log.trace "setLevel($level)"

	if ( value == 0 ) 
	{
		sendEvent( name: "switch", value: "off" )

		cmds << "st cmd 0x${device.deviceNetworkId} ${endpointId} 6 0 {}"
	}
	else if ( device.latestValue( "switch" ) == "off" ) 
	{
		sendEvent( name: "switch", value: "on" )
		sendEvent( name: "level", value: level )
		sendEvent( name: "switch.setLevel", value: level )
	}

	def levelHex = new BigInteger( Math.round( level * 255 / 100 ).toString()).toString( 16 )
	cmds << "st cmd 0x${device.deviceNetworkId} ${endpointId} 8 4 {${levelHex} 0000}"

	log.debug "setLevel(): zigbee cmds: ${cmds}"
	cmds
}

private hex(value, width=2) 
{
	def s = new BigInteger(Math.round(value).toString()).toString(16)

	while ( s.size() < width ) 
	{
		s = "0" + s
	}

	s
}

private getEndpointId() 
{
	new BigInteger(device.endpointId, 16).toString()
}

/**
 *  Generic Camera Device v1.0.07102014
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
	definition (name: "Generic Camera Device", namespace: "ps", author: "patrick@patrickstuart.com") {
		capability "Image Capture"
		capability "Sensor"
		capability "Actuator"
        
	}

    preferences {
    input("CameraIP", "string", title:"Camera IP Address", description: "Please enter your camera's IP Address", required: true, displayDuringSetup: true)
    input("CameraPort", "string", title:"Camera Port", description: "Please enter your camera's Port", defaultValue: 80 , required: true, displayDuringSetup: true)
    input("CameraPath", "string", title:"Camera Path to Image", description: "Please enter the path to the image", defaultValue: "/SnapshotJPEG?Resolution=640x480&Quality=Clarity", required: true, displayDuringSetup: true)
    input("CameraAuth", "bool", title:"Does Camera require User Auth?", description: "Please choose if the camera requires authentication (only basic is supported)", defaultValue: true, displayDuringSetup: true)
    input("CameraPostGet", "string", title:"Does Camera use a Post or Get, normally Get?", description: "Please choose if the camera uses a POST or a GET command to retreive the image", defaultValue: "GET", displayDuringSetup: true)
    input("CameraUser", "string", title:"Camera User", description: "Please enter your camera's username", required: false, displayDuringSetup: true)
    input("CameraPassword", "string", title:"Camera Password", description: "Please enter your camera's password", required: false, displayDuringSetup: true)
	}
    
	simulator {
    
	}

	tiles {
		standardTile("camera", "device.image", width: 1, height: 1, canChangeIcon: false, inactiveLabel: true, canChangeBackground: true) {
			state "default", label: "", action: "", icon: "st.camera.dropcam-centered", backgroundColor: "#FFFFFF"
		}

		carouselTile("cameraDetails", "device.image", width: 3, height: 2) { }

		standardTile("take", "device.image", width: 1, height: 1, canChangeIcon: false, inactiveLabel: true, canChangeBackground: false) {
			state "take", label: "Take", action: "Image Capture.take", icon: "st.camera.camera", backgroundColor: "#FFFFFF", nextState:"taking"
			state "taking", label:'Taking', action: "", icon: "st.camera.take-photo", backgroundColor: "#53a7c0"
			state "image", label: "Take", action: "Image Capture.take", icon: "st.camera.camera", backgroundColor: "#FFFFFF", nextState:"taking"
		}

		main "camera"
		details(["cameraDetails", "take", "error"])
	}
}

def parse(String description) {
    log.debug "Parsing '${description}'"
    def map = stringToMap(description)

    def result = []

    if (map.bucket && map.key) { //got a s3 pointer
    	putImageInS3(map)
    }
    else if (map.headers && map.body) { //got device info response
    	def headerString = new String(map.headers.decodeBase64())
    	if (headerString.contains("404 Not Found")) {
    		state.snapshot = "/snapshot.cgi"
   		}

    	if (map.body) {
        def bodyString = new String(map.body.decodeBase64())
        def body = new XmlSlurper().parseText(bodyString)
        def productName = body?.productName?.text()
    	if (productName) {
            log.trace "Product Name: $productName"
            state.snapshot = ""
        }
    }
    }

    result
}

def putImageInS3(map) {

	def s3ObjectContent

	try {
		def imageBytes = getS3Object(map.bucket, map.key + ".jpg")

		if(imageBytes)
		{
			s3ObjectContent = imageBytes.getObjectContent()
			def bytes = new ByteArrayInputStream(s3ObjectContent.bytes)
			storeImage(getPictureName(), bytes)
		}
	}
	catch(Exception e) {
		log.error e
	}
	finally {
		if (s3ObjectContent) { s3ObjectContent.close() }
	}
}

// handle commands
def take() {
	def userpassascii = "${CameraUser}:${CameraPassword}"
	def userpass = "Basic " + userpassascii.encodeAsBase64().toString()
    def host = CameraIP 
    def hosthex = convertIPtoHex(host)
    def porthex = convertPortToHex(CameraPort)
    device.deviceNetworkId = "$hosthex:$porthex" 
    
    log.debug "The device id configured is: $device.deviceNetworkId"
    
    def path = CameraPath 
    log.debug "path is: $path"
    log.debug "Requires Auth: $CameraAuth"
    log.debug "Uses which method: $CameraPostGet"
    
    def headers = [:] 
    headers.put("HOST", "$host:$CameraPort")
   	if (CameraAuth == "true") {
        headers.put("Authorization", userpass)
    }
    
    log.debug "The Header is $headers"
    
    def method = "GET"
    try {
    	if (CameraPostGet.toUpperCase() == "POST") {
        	method = "POST"
        	}
        }
    catch (Exception e) { // HACK to get around default values not setting in devices
    	settings.CameraPostGet = "GET"
        log.debug e
        log.debug "You must not of set the perference for the CameraPOSTGET option"
    }
    
    log.debug "The method is $method"
    
    try {
    def hubAction = new physicalgraph.device.HubAction(
    	method: method,
    	path: path,
    	headers: headers
        )
        
	hubAction.options = [outputMsgToS3:true]
    log.debug hubAction
    hubAction
    }
    catch (Exception e) {
    	log.debug "Hit Exception $e on $hubAction"
    }
    
}



private getPictureName() {
	def pictureUuid = java.util.UUID.randomUUID().toString().replaceAll('-', '')
	return device.deviceNetworkId + "_$pictureUuid" + ".jpg"
}

private String convertIPtoHex(ipAddress) { 
    String hex = ipAddress.tokenize( '.' ).collect {  String.format( '%02x', it.toInteger() ) }.join()
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

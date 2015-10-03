/**
 *  Live Code Friday Dynamic Page Function Call Issue
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
definition(
    name: "Live Code Friday Dynamic Page Function Call Issue",
    namespace: "pstuart",
    author: "Patrick Stuart",
    description: "Live Code Friday Dynamic Page Function Call Issue",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	page(name: "firstPage")
	page(name: "secondPage")
}

def firstPage() {
	dynamicPage(name: "firstPage", title: "Where to first?", install: true, uninstall: true) {
		section("Main Menu") {
        	def test = testFunction("test1", "test2")
        	paragraph "Testing A Functional Logging Issue from function $test"
        	href(page: "secondPage", title: "Let's Add Devices!")
        }
        /*
        section("Later") {
        	paragraph "More to come..."
        }
        */
        
   }
}

def secondPage() {
	dynamicPage(name: "secondPage", title: "Where to now?", install: true, uninstall: true) {
		section("Main Menu 2") {
        	def test = testFunction("test1", "test2")
        	paragraph "Testing A Functional Logging Issue from function $test"
        	href(page: "secondPage", title: "Let's Add Devices!")
        }
        /*
        section("Later") {
        	paragraph "More to come..."
        }
        */
        
   }
}

def testFunction(pOne, pTwo) {
	log.debug "This will not show up until after 'Done' is clicked"
    log.debug pOne
    log.debug pTwo
    return "test"
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

// TODO: implement event handlers
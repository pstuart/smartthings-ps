/**
 *  HTTP Post Example
 *
 *  Copyright 2015 Charles Schwer
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 */
metadata {
    definition (name: "HTTP Post Example", namespace: "cschwer", author: "Charles Schwer") {
    }

    simulator {
    }

    tiles {
  }
}

// Parse events into attributes
def parse(String description) {
    log.debug "Parsing '${description}'"
    def msg = parseLanMessage(description)

	log.debug "data ${msg.data}"
    log.debug "body ${msg.body}"
    log.debug "headers ${msg.headers}"
}
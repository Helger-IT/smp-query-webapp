#
# Copyright (C) 2023-2026 Philip Helger (www.helger.com)
# philip[at]helger[dot]com
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#         http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# Part 1:

FROM ubuntu:latest AS build

# Install unzip
RUN apt-get update \
  && apt-get upgrade -y \
  && apt-get install -y unzip \
  && rm -rf /var/lib/apt/lists/*

COPY target/*.war ./phax.war

RUN unzip phax.war -d /phax

# Part 2:
    
FROM tomcat:10-jdk21

ENV CATALINA_OPTS="-Djava.security.egd=file:/dev/urandom"

WORKDIR $CATALINA_HOME/webapps
RUN rm -rf manager host-manager docs examples ROOT

COPY --from=build /phax $CATALINA_HOME/webapps/ROOT

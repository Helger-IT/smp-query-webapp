#!/bin/sh -e
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

version=1.0.0

#echo Docker login
#echo "$DOCKER_TOKEN" | docker login -u "$DOCKER_USER" --password-stdin

docker buildx build --platform=linux/amd64 --push --pull -t phelger/smpqwa:$version       -t phelger/smpqwa:latest     .
docker buildx build --platform=linux/arm64 --push --pull -t phelger/smpqwa-arm64:$version -t phelger/smpqwa-arm64:latest .

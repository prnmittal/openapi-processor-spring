/*
 * Copyright 2019 the original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.hauner.openapi.spring.converter

import com.github.hauner.openapi.spring.converter.mapping.AmbiguousTypeMappingException
import com.github.hauner.openapi.spring.converter.mapping.TypeMapping
import com.github.hauner.openapi.spring.model.Api
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.hauner.openapi.spring.support.OpenApiParser.parse

class DataTypeConverterArrayTypeMappingSpec extends Specification {

    @Unroll
    void "maps simple array schema to #responseTypeName via global array mapping" () {
        def openApi = parse ("""\
openapi: 3.0.2
info:
  title: API
  version: 1.0.0

paths:
  /array-string:
    get:
      responses:
        '200':
          content:
            application/vnd.any:
              schema:
                type: array
                items:
                  type: string
          description: none              
""")
        when:
        def options = new ApiOptions(packageName: 'pkg', typeMappings: [
            new TypeMapping (sourceTypeName: 'array', targetTypeName: targetTypeName)
        ])
        Api api = new ApiConverter (options).convert (openApi)

        then:
        def itf = api.interfaces.first ()
        def ep = itf.endpoints.first ()
        ep.response.responseType.name == responseTypeName

        where:
        targetTypeName         | responseTypeName
        'java.util.Collection' | 'Collection<String>'
        'java.util.List'       | 'List<String>'
        'java.util.Set'        | 'Set<String>'
    }

    void "throws when there are multiple global mappings for the array type" () {
        def openApi = parse ("""\
openapi: 3.0.2
info:
  title: API
  version: 1.0.0

paths:
  /page:
    get:
      parameters:
        - in: query
          name: date
          required: false
          schema:
            type: array
            items: 
              type: string
      responses:
        '204':
          description: none
""")

        when:
        def options = new ApiOptions(
            packageName: 'pkg',
            typeMappings: [
                new TypeMapping (
                    sourceTypeName: 'array',
                    targetTypeName: 'java.util.Collection'),
                new TypeMapping (
                    sourceTypeName: 'array',
                    targetTypeName: 'java.util.Collection')
            ])
        new ApiConverter (options).convert (openApi)

        then:
        def e = thrown (AmbiguousTypeMappingException)
        e.typeMappings == options.typeMappings
    }

}
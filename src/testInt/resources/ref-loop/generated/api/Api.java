/*
 * This class is auto generated by https://github.com/hauner/openapi-processor-spring.
 * DO NOT EDIT.
 */

package generated.api;

import generated.model.Foo;
import generated.model.Self;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public interface Api {

    @GetMapping(
            path = "/self-reference",
            produces = {"application/json"})
    ResponseEntity<Self> getSelfReference();

    @GetMapping(
            path = "/nested-self-reference",
            produces = {"application/json"})
    ResponseEntity<Foo> getNestedSelfReference();

}

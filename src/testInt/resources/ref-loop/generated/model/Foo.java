/*
 * This class is auto generated by https://github.com/hauner/openapi-processor-spring.
 * DO NOT EDIT.
 */

package generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Foo {

    @JsonProperty("child")
    private Bar child;

    public Bar getChild() {
        return child;
    }

    public void setChild(Bar child) {
        this.child = child;
    }

}

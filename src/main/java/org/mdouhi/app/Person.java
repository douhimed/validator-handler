package org.mdouhi.app;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode
class Person {

    private String name;
    private int age;

    @Override
    public String toString() {
        return String.format("Person : %s (%s)", name, age);
    }

}
package challenge;

import java.util.Optional;

public class Optionals {

    public void simple() {
        Optional<String> optional = Optional.of("Hello, Optional!");
        System.out.println(optional.isPresent());
        System.out.println(optional.get());

        Optional<String> optionalNullable = Optional.ofNullable(null);
        System.out.println(optionalNullable.isPresent());

        Optional<String> emptyOptional = Optional.empty();
        System.out.println(emptyOptional.isPresent());
    }


    public void check(Optional<String> optional) {
        // Throw an exception if the value is not present

    }
}

package com.typeb.hello_world_api.service;

import com.typeb.hello_world_api.exception.InvalidInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class HelloWorldServiceTest {

    private HelloWorldService service;

    @BeforeEach
    void setUp() {
        service = new HelloWorldService();
    }

    // =========================================================================
    // Happy path — A to M
    // =========================================================================

    @Nested
    @DisplayName("When name starts with A–M")
    class ValidNames {

        @ParameterizedTest(name = "name=\"{0}\" should return capitalised greeting")
        @ValueSource(strings = {"alice", "Alice", "ALICE", "bob", "Charlie",
                                "david", "eve", "frank", "grace", "henry",
                                "iris", "james", "kate", "liam", "mike",
                                "a", "m", "A", "M"})
        void shouldReturnCapitalisedName(String name) {
            String result = service.greet(name);
            assertThat(result).startsWith(String.valueOf(Character.toUpperCase(name.strip().charAt(0))));
        }

        @Test
        @DisplayName("'alice' should return 'Alice'")
        void lowercaseAlice() {
            assertThat(service.greet("alice")).isEqualTo("Alice");
        }

        @Test
        @DisplayName("'ALICE' preserves casing after first char")
        void uppercaseAlice() {
            assertThat(service.greet("ALICE")).isEqualTo("ALICE");
        }

        @Test
        @DisplayName("Leading whitespace is trimmed before processing")
        void leadingWhitespaceTrimmed() {
            assertThat(service.greet("  alice")).isEqualTo("Alice");
        }

        @Test
        @DisplayName("Boundary 'A' (first letter) is valid")
        void boundaryA() {
            assertThat(service.greet("A")).isEqualTo("A");
        }

        @Test
        @DisplayName("Boundary 'm' (last valid letter) is valid")
        void boundaryLowercaseM() {
            assertThat(service.greet("m")).isEqualTo("M");
        }
    }

    // =========================================================================
    // Invalid input — N to Z
    // =========================================================================

    @Nested
    @DisplayName("When name starts with N–Z")
    class InvalidNtoZ {

        @ParameterizedTest(name = "name=\"{0}\" should throw")
        @ValueSource(strings = {"nancy", "Nancy", "NANCY", "oscar", "peter",
                                "quinn", "rachel", "sam", "tom", "ursula",
                                "victor", "wendy", "xander", "yara", "zoe",
                                "n", "z", "N", "Z"})
        void shouldThrowInvalidInputException(String name) {
            assertThatThrownBy(() -> service.greet(name))
                    .isInstanceOf(InvalidInputException.class);
        }

        @Test
        @DisplayName("Boundary 'N' (first invalid letter) throws")
        void boundaryN() {
            assertThatThrownBy(() -> service.greet("N"))
                    .isInstanceOf(InvalidInputException.class);
        }

        @Test
        @DisplayName("Boundary 'Z' throws")
        void boundaryZ() {
            assertThatThrownBy(() -> service.greet("Z"))
                    .isInstanceOf(InvalidInputException.class);
        }
    }

    // =========================================================================
    // Edge cases — non-alphabetic first character
    // =========================================================================

    @Nested
    @DisplayName("When name starts with a non-letter")
    class NonAlphabeticFirst {

        @ParameterizedTest(name = "name=\"{0}\" should throw")
        @ValueSource(strings = {"123abc", "!hello", "9lives", "-dash"})
        void shouldThrowForNonAlphabeticStart(String name) {
            assertThatThrownBy(() -> service.greet(name))
                    .isInstanceOf(InvalidInputException.class);
        }
    }
}
package be.twofold.tinycast.generator;

import be.twofold.tinycast.CastNodeID;
import be.twofold.tinycast.CastPropertyID;
import be.twofold.tinycast.generator.model.*;

import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

final class TypeParser {
    private static final Pattern PART0_PATTERN = Pattern.compile("(.+?) (?:\\([^(]+)?\\(([\\w%]+)\\)");
    private static final Pattern PART1_PATTERN = Pattern.compile("[^(]+\\((\\w+)\\)(?:\\s+\\[([^]]+)])?");

    static TypeDef parse(RawType rawType) {
        List<CastNodeID> children = !rawType.children().isEmpty()
            ? Arrays.stream(rawType.children().split(","))
            .map(s -> parseType(s.strip()))
            .collect(Collectors.toList())
            : List.<CastNodeID>of();

        List<PropertyDef> properties = rawType.properties().lines()
            .map(TypeParser::parseProperty)
            .collect(Collectors.toList());

        return new TypeDef(
            parseType(rawType.name()),
            children,
            properties
        );
    }

    private static CastNodeID parseType(String s) {
        String name = Arrays.stream(s.split("\\s+"))
            .map(String::toUpperCase)
            .collect(Collectors.joining("_"));
        return CastNodeID.valueOf(name);
    }

    private static PropertyDef parseProperty(String s) {
        List<String> parts = Arrays.stream(s.split("\t"))
            .map(String::strip)
            .collect(Collectors.toList());


        NameAndKey part0 = parsePropertyName(parts.get(0));
        TypesAndValues part1 = parseTypesAndValues(parts.get(1));
        boolean part2 = parsePropertyBoolean(parts.get(2));
        boolean part3 = parsePropertyBoolean(parts.get(3));

        return new PropertyDef(
            part0.name(),
            part0.key(),
            part1.types(),
            part1.values(),
            part2,
            part3
        );
    }

    private static NameAndKey parsePropertyName(String s) {
        Matcher matcher = PART0_PATTERN.matcher(s);
        if (!matcher.matches()) {
            throw new IllegalArgumentException();
        }
        return new NameAndKey(matcher.group(1), matcher.group(2));
    }

    private static TypesAndValues parseTypesAndValues(String s) {
        Matcher matcher = PART1_PATTERN.matcher(s);

        EnumSet<CastPropertyID> allTypes = EnumSet.noneOf(CastPropertyID.class);
        ArrayList<String> allValues = new ArrayList<>();
        while (matcher.find()) {
            Collection<String> values = matcher.group(2) != null
                ? Arrays.stream(matcher.group(2).split(",")).map(String::strip).collect(Collectors.toSet())
                : List.of();
            if (!values.isEmpty() && !allValues.isEmpty()) {
                throw new IllegalArgumentException();
            }

            allTypes.add(stringToType(matcher.group(1)));
            allValues.addAll(values);
        }
        return new TypesAndValues(allTypes, allValues);
    }

    private static boolean parsePropertyBoolean(String s) {
        if (s.indexOf(' ') >= 0) {
            return false;
        }
        switch (s) {
            case "True":
                return true;
            case "False":
                return false;
            default:
                throw new UnsupportedOperationException();
        }
    }

    private static CastPropertyID stringToType(String s) {
        switch (s) {
            case "b":
                return CastPropertyID.BYTE;
            case "h":
                return CastPropertyID.SHORT;
            case "i":
                return CastPropertyID.INT;
            case "l":
                return CastPropertyID.LONG;
            case "f":
                return CastPropertyID.FLOAT;
            case "d":
                return CastPropertyID.DOUBLE;
            case "s":
                return CastPropertyID.STRING;
            case "v2":
                return CastPropertyID.VECTOR2;
            case "v3":
                return CastPropertyID.VECTOR3;
            case "v4":
                return CastPropertyID.VECTOR4;
            default:
                throw new UnsupportedOperationException();
        }
    }

    private static final class NameAndKey {
        private final String name;
        private final String key;

        private NameAndKey(String name, String key) {
            this.name = Objects.requireNonNull(name);
            this.key = Objects.requireNonNull(key);
        }

        public String name() {
            return name;
        }

        public String key() {
            return key;
        }
    }

    private static final class TypesAndValues {
        private final Set<CastPropertyID> types;
        private final List<String> values;

        private TypesAndValues(Set<CastPropertyID> types, List<String> values) {
            this.types = EnumSet.copyOf(types);
            this.values = List.copyOf(values);
        }

        public Set<CastPropertyID> types() {
            return types;
        }

        public List<String> values() {
            return values;
        }
    }
}

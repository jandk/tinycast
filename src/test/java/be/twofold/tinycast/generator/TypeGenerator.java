package be.twofold.tinycast.generator;

import be.twofold.tinycast.CastNodeID;
import be.twofold.tinycast.generator.model.PropertyDef;
import be.twofold.tinycast.generator.model.TypeDef;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

final class TypeGenerator {
    public static void main(String[] args) throws IOException {
        List<TypeDef> types = new TypeParser().parse(TypeGenerator.class.getResourceAsStream("/cast.json"));

        Map<CastNodeID, List<String>> arrayTypes = new LinkedHashMap<>();
        for (TypeDef type : types) {
            for (PropertyDef property : type.properties()) {
                if (property.isArray()) {
                    arrayTypes.computeIfAbsent(type.type(), __ -> new ArrayList<>()).add(property.getKey());
                }
            }
        }

        for (Map.Entry<CastNodeID, List<String>> type : arrayTypes.entrySet()) {
            String props = type.getValue().stream()
                .collect(Collectors.joining("\", \"", "\"", "\""));
            System.out.println("CastNodeID." + type.getKey() + ", Set.of(" + props + "),");
        }

        new TypeClassWriter().generate(types);
    }
}

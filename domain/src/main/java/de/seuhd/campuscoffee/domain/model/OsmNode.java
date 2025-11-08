package de.seuhd.campuscoffee.domain.model;

import lombok.Builder;
import org.jspecify.annotations.NonNull;

import java.util.Map;

/**
 * Represents an OpenStreetMap node with relevant Point of Sale information.
 * Extended to carry parsed tag and coordinate information needed to create a POS.
 */
@Builder
public record OsmNode(
        @NonNull Long nodeId,
        String name,
        String description,
        Double lat,
        Double lon,
        Map<String, String> tags,
        String street,
        String houseNumber,
        String city,
        Integer postalCode,
        String country,
        String phone,
        String website
) {
}

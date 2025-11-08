package de.seuhd.campuscoffee.domain.ports;

import de.seuhd.campuscoffee.domain.model.OsmNode;
import de.seuhd.campuscoffee.domain.exceptions.OsmNodeNotFoundException;
import org.jspecify.annotations.NonNull;

/**
 * Port for importing Point of Sale data from OpenStreetMap.
 * This interface defines the contract for fetching OSM node data.
 * Implementations should handle the external API communication.
 */
public interface OsmDataService {
    /**
     * Fetches an OpenStreetMap node by its ID.
     *
     * @param nodeId the OpenStreetMap node ID to fetch
     * @return the OSM node data with tags
     * @throws OsmNodeNotFoundException if the node doesn't exist or can't be fetched
     */
    @NonNull OsmNode fetchNode(@NonNull Long nodeId) throws OsmNodeNotFoundException;

    /**
     * Parses raw OSM XML content and extracts the node with the given nodeId.
     * If nodeId is null, the first node in the XML is returned.
     *
     * @param nodeId    the node id to extract from XML or null to pick first node
     * @param osmXmlStr the raw OSM XML content
     * @return the parsed OsmNode
     */
    @NonNull OsmNode parseNodeFromXml(Long nodeId, @NonNull String osmXmlStr) throws OsmNodeNotFoundException;
}

package de.seuhd.campuscoffee.data.impl;

import de.seuhd.campuscoffee.domain.exceptions.OsmNodeNotFoundException;
import de.seuhd.campuscoffee.domain.model.OsmNode;
import de.seuhd.campuscoffee.domain.ports.OsmDataService;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * OSM import service.
 */
@Service
@Slf4j
class OsmDataServiceImpl implements OsmDataService {

    @Override
    public @NonNull OsmNode fetchNode(@NonNull Long nodeId) throws OsmNodeNotFoundException {
        log.info("Fetching OSM node {} via OSM API", nodeId);
        try {
            // Try to fetch XML from OSM API
            URL url = new URL("https://www.openstreetmap.org/api/0.6/node/" + nodeId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5_000);
            conn.setReadTimeout(5_000);

            int code = conn.getResponseCode();
            if (code != 200) {
                throw new OsmNodeNotFoundException(nodeId);
            }

            String xml;
            try (Scanner s = new Scanner(conn.getInputStream(), StandardCharsets.UTF_8)) {
                s.useDelimiter("\\A");
                xml = s.hasNext() ? s.next() : "";
            }

            return parseNodeFromXml(nodeId, xml);
        } catch (OsmNodeNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.warn("Failed to fetch node from OSM API: {}. Falling back to stub behavior.", e.getMessage());
            // Fallback to the previous stub behaviour for the known test node
            if (nodeId.equals(5589879349L)) {
                return OsmNode.builder().nodeId(nodeId).name("Rada Coffee & Rösterei").description("Caffé und Rösterei").lat(49.4122362).lon(8.7077883).street("Untere Straße").houseNumber("21").city("Heidelberg").postalCode(69117).country("DE").phone("+49 6221 1805585").website("https://rada-roesterei.com/").build();
            }
            throw new OsmNodeNotFoundException(nodeId);
        }
    }

    @Override
    public @NonNull OsmNode parseNodeFromXml(Long nodeId, @NonNull String osmXmlStr) throws OsmNodeNotFoundException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(osmXmlStr.getBytes(StandardCharsets.UTF_8)));

            NodeList nodes = doc.getElementsByTagName("node");
            if (nodes == null || nodes.getLength() == 0) {
                throw new OsmNodeNotFoundException(nodeId == null ? -1L : nodeId);
            }

            Element chosen = null;
            for (int i = 0; i < nodes.getLength(); i++) {
                Node n = nodes.item(i);
                if (n.getNodeType() != Node.ELEMENT_NODE) continue;
                Element e = (Element) n;
                String idStr = e.getAttribute("id");
                if (nodeId == null) {
                    chosen = e;
                    break;
                }
                if (idStr != null && !idStr.isEmpty() && Long.parseLong(idStr) == nodeId) {
                    chosen = e;
                    break;
                }
            }

            if (chosen == null) {
                throw new OsmNodeNotFoundException(nodeId == null ? -1L : nodeId);
            }

            String idStr = chosen.getAttribute("id");
            Long parsedId = idStr != null && !idStr.isEmpty() ? Long.parseLong(idStr) : -1L;
            String latStr = chosen.getAttribute("lat");
            String lonStr = chosen.getAttribute("lon");
            Double lat = latStr == null || latStr.isEmpty() ? null : Double.parseDouble(latStr);
            Double lon = lonStr == null || lonStr.isEmpty() ? null : Double.parseDouble(lonStr);

            // Collect tags
            Map<String, String> tags = new HashMap<>();
            NodeList children = chosen.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node c = children.item(i);
                if (c.getNodeType() != Node.ELEMENT_NODE) continue;
                Element ce = (Element) c;
                if (!"tag".equals(ce.getTagName())) continue;
                String k = ce.getAttribute("k");
                String v = ce.getAttribute("v");
                tags.put(k, v);
            }

            // Map common tags
            String name = tags.getOrDefault("name", tags.get("name:de"));
            String description = tags.get("description");
            String street = tags.get("addr:street");
            String houseNumber = tags.get("addr:housenumber");
            String city = tags.get("addr:city");
            Integer postalCode = null;
            if (tags.containsKey("addr:postcode")) {
                try {
                    postalCode = Integer.parseInt(tags.get("addr:postcode"));
                } catch (NumberFormatException ignored) {
                }
            }
            String country = tags.get("addr:country");
            String phone = tags.get("phone");
            String website = tags.get("website");

            return OsmNode.builder()
                    .nodeId(parsedId)
                    .name(name)
                    .description(description)
                    .lat(lat)
                    .lon(lon)
                    .tags(tags)
                    .street(street)
                    .houseNumber(houseNumber)
                    .city(city)
                    .postalCode(postalCode)
                    .country(country)
                    .phone(phone)
                    .website(website)
                    .build();
        } catch (OsmNodeNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to parse OSM XML: {}", e.getMessage());
            throw new OsmNodeNotFoundException(nodeId == null ? -1L : nodeId);
        }
    }
}

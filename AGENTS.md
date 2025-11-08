# OSM Import Feature - Agents Log

## Overview
This feature adds the ability for administrators to import a Point-of-Sale (POS) from OpenStreetMap (OSM) XML data. It provides two ways to import: by providing an OSM node ID (the app fetches the XML from the OSM API) or by uploading raw OSM XML content. The import extracts commonly-used fields (name, description, address tags, coordinates) and creates or updates a POS in the CampusCoffee database. This integrates into the existing POS management module by implementing a new `OsmDataService` parser and wiring it through the domain `PosService` to the data adapter upsert logic.

## Agent Contributions

## Agent: ChatGPT
**Role:** Lead AI Developer

**Tasks:**
- Designed and implemented `OsmDataService` XML parsing and HTTP fetch fallback
- Implemented `importFromOsmXml` in `PosServiceImpl` and improved OSM-to-POS mapping
- Added REST endpoint `POST /api/pos/import/osm` (accepts nodeId path or raw XML body)
- Wrote this AGENTS.md and documented usage notes

**Files produced/modified:**
- domain/src/main/java/de/seuhd/campuscoffee/domain/model/OsmNode.java (extended)
- domain/src/main/java/de/seuhd/campuscoffee/domain/ports/OsmDataService.java (added parse method)
- domain/src/main/java/de/seuhd/campuscoffee/domain/ports/PosService.java (added importFromOsmXml)
- domain/src/main/java/de/seuhd/campuscoffee/domain/impl/PosServiceImpl.java (implemented XML import and mapping)
- data/src/main/java/de/seuhd/campuscoffee/data/impl/OsmDataServiceImpl.java (implemented XML parsing and HTTP fetch fallback)
- api/src/main/java/de/seuhd/campuscoffee/api/controller/PosController.java (added XML import endpoint)
- AGENTS.md (this file)

## Agent: GitHub Copilot
**Role:** Code Assistant

**Tasks:**
- Assisted with code completions and import suggestions
- Helped fix minor syntax issues and method signatures

## Usage Notes
- Import by node ID (server will fetch OSM API):
  POST /api/pos/import/osm/{nodeId}
  Example: POST /api/pos/import/osm/5589879349

- Import by raw XML upload:
  POST /api/pos/import/osm
  Content-Type: application/xml
  Body: the full OSM XML containing one or more <node> elements.

- Success log message example:
  "Successfully imported POS 'Rada Coffee & Rösterei' from OSM node 5589879349"

- Notes & assumptions:
  - The system matches by POS name to avoid duplicates (database enforces unique name); coordinates are parsed but not persisted in the current schema.
  - If required fields (e.g., `name`) are missing in the OSM node, an error is thrown and the import fails.



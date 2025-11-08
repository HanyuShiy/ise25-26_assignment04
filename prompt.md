# Project Requirement Proposal (PRP)

You are a senior software engineer.
Use the information below to implement a new feature in this software project.

## Goal

**Feature Goal**: Implement a feature that allows administrators to import a new Point of Sale (POS) directly from an existing OpenStreetMap (OSM) XML node, such as the one representing *Rada Coffee & Rösterei* (node id=5589879349).

**Deliverable**: A new Spring Boot service and REST API endpoint that:
- Parses OSM XML data,
- Extracts relevant POS information (e.g., name, address, coordinates),
- Creates or updates a POS entity in the CampusCoffee database.

**Success Definition**: 
- The admin can trigger the import by providing an OSM node ID or XML file.
- The OSM data is correctly parsed and stored as a new POS.
- If a POS with the same name and coordinates already exists, it is updated instead of duplicated.
- All operations are logged and testable via REST API call.

## User Persona

**Target User**: CampusCoffee system administrator

**Use Case**: The admin wants to quickly add new coffee shop locations to the application using real-world data from OpenStreetMap instead of manually entering details.

**User Journey**: 
1. The admin opens the admin dashboard.
2. They provide an OSM node ID or upload the XML.
3. The system fetches and parses the XML, extracts fields like `name`, `lat`, `lon`, and `addr:*`.
4. A new POS entityy is created in the database.
5. The admin receives a confirmation message ("Imported Rada Coffe & Rösterei successfully.")

**Pain Points Addressed**: 
    - Manual entry of café data is slow and error-prone.
    - Automates data integration from publicly available sources.

## Why

- **Business value**: Simplifies adding real-world cafés and enhances data consistency.
- **Integration**: Fits naturally into the POS management module of CampusCoffee.
- **Problem solved**: Manual POS data entry becomes unnecessary; allows scalable importing from OSM.

## What
- New REST endpoint:
`POST /api/pos/import/osm`
with body either:
```
{ "nodeId": 5589879349 }
```
or raw XML upload.

### Success Criteria
- A new `OsmImportService` feature exists and can parse valid OSM XML.
- The `POST /api/pos/import/osm` endpoint is functional and tested.
- Imported POS data (name, address, coordinates) appears in the database.

## Documentation & References

MUST READ - Include the following information in your context window.

The `README.md` file at the root of the project contains setup instructions and example API calls.

This Java Spring Boot application is structured as a multi-module Maven project following the ports-and-adapters architectural pattern.

There are the following submodules:
- `api` - Maven submodule for controller adapter.
- `application` - Maven submodule for Spring Boot application, test data import, and system tests.
- `data` - Maven submodule for data adapter.
- `domain` - Maven submodule for domain model, main business logic, and ports.

## Additional Context:
OpenStrrtMap(OSM) XML structure for a cafe entry:
```
<node id="5589879349" visible="true" version="11" changeset="165984125" timestamp="2025-05-08T13:35:07Z" user="Niiepce" uid="12992079" lat="49.4122362" lon="8.7077883">
    <tag k="addr:city" v="Heidelberg"/>
    <tag k="addr:country" v="DE"/>
    <tag k="addr:housenumber" v="21"/>
    <tag k="addr:postcode" v="69117"/>
    <tag k="addr:street" v="Untere Straße"/>
    <tag k="amenity" v="cafe"/>
    <tag k="cuisine" v="coffee_shop;breakfast"/>
    <tag k="description" v="Caffé und Rösterei"/>
    <tag k="diet:vegan" v="yes"/>
    <tag k="diet:vegetarian" v="yes"/>
    <tag k="indoor_seating" v="yes"/>
    <tag k="internet_access:fee" v="no"/>
    <tag k="name" v="Rada"/>
    <tag k="name:de" v="Rada Coffee & Rösterei"/>
    <tag k="name:en" v="Rada Coffee & Rösterei"/>
    <tag k="opening_hours" v="Mo-Fr 11:00-18:00; Sa-Su 10:00-18:00"/>
    <tag k="outdoor_seating" v="yes"/>
    <tag k="phone" v="+49 6221 1805585"/>
    <tag k="smoking" v="outside"/>
    <tag k="website" v="https://rada-roesterei.com/"/>
</node>
```

## Deliverable Extension – AGENTS.md Generation
When completing this task, you must generate a short `AGENTS.md` file in Markdown format.
This file should serve as lightweight developer documentation and collaboration log.

- **Requirements** for `AGENTS.md`
The generated file should contain the following sections:
1. Overview
    - One-paragraph summary of the implemented feature (Import Point-of-Sale from OSM XML)
    - What problem it solves and how it integrates into the CampusCoffee project.

2. Agent Contributions
List the GenAI agents that participated (e.g., ChatGPT, GitHub Copilot, Claude, etc.), and for each:
    - **Role** (e.g., “Lead AI Engineer”, “Code Completion Assistant”)
    - **Main tasks performed** (e.g., design service architecture, implement endpoint, write tests)
    - **Files or components produced**
Example:
``` markdown
## Agent: ChatGPT (GPT-5)
**Role:** Lead AI Developer  
**Tasks:**  
- Implemented `OsmImportService` and REST endpoint `/api/pos/import/osm`  
- Designed XML parsing logic and repository integration  
- Drafted test skeletons for OSM data import

## Agent: GitHub Copilot
**Role:** Code Assistant  
**Tasks:**  
- Provided autocompletions for imports and annotations  
- Suggested minor syntax fixes and formatting improvements
```
3. Usage Notes
- Briefly instructions on how to use the new feature.
- Mention expected log output and example success message.
Introduction
This project describes the implementation of an application processing events sent by robots moving on a grid. The application aims to generate real-time reports on the status of the robots and also send event histories to external systems.

Functionalities
The application has the following functionalities:

Event Processing: The application receives events sent by robots and updates their internal state.
Real-time Reports: On demand, the application can generate reports on the current state of all robots, including their position, battery level, etc.
Sending Event History: When a robot starts charging, the application sends all its events since the previous charging to an external system.
Events
The application handles the following events:

PositionChanged: This event is sent when a robot changes its position on the grid.
DestinationChanged: This event is sent when the robot's destination is changed.
ContainerPickedUp: This event is sent when a robot picks up a container.
ContainerDroppedOff: This event is sent when a robot drops off a container.
BatteryLevelChanged: This event is sent when the robot's battery level changes.
ChargingStarted: This event is sent when the robot starts charging.
ChargingEnded: This event is sent when the robot finishes charging.

Event Specification
Each bot sends different types of events (BotEvent). All events contain:

eventId - event identifier.
timestamp - event time in ISO 8601 format.
warehouseId - warehouse identifier.
eventName - event name.
eventSpecificData - a variable number of fields depending on the eventName.
Events are serialized into a CSV-like format:

{eventId},{timestamp},{warehouseId},{eventName},{eventField1},
{eventField2},...,{eventFieldN}
Field values cannot contain commas. Identifiers consist of letters, numbers, and start with a letter.

Example Event
1234567890,2023-11-14T12:34:56.789Z,WR4A,PositionChanged,10,20

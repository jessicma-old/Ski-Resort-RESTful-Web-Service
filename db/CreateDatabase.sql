CREATE SCHEMA IF NOT EXISTS ScalableAssignment2;
USE ScalableAssignment2;

DROP TABLE IF EXISTS SkiRecords;
DROP TABLE IF EXISTS LiftData;

CREATE TABLE LiftData (
LiftID INTEGER AUTO_INCREMENT,
Vertical INTEGER,
CONSTRAINT pk_LiftData_LiftID PRIMARY KEY (LiftID)
);

CREATE TABLE SkiRecords (
SkierID INTEGER,
DayNumber INTEGER,
TimeScanned INTEGER,
LiftID INTEGER,
ResortID INTEGER,
CONSTRAINT chk_SkiRecords_DayNum CHECK (DayNumber >= 1 AND DayNumber <=365),
CONSTRAINT chk_SkiRecords_RFIDTimeStamp CHECK (TimeScanned >= 0 AND TimeScanned <=360),
CONSTRAINT pk_SkiRecords PRIMARY KEY (SkierID,DayNumber,TimeScanned),
CONSTRAINT fk_SkiRecords_LiftID FOREIGN KEY (LiftID)
REFERENCES LiftData(LiftID)
ON UPDATE CASCADE ON DELETE CASCADE
);


LOAD DATA LOCAL INFILE '~/LiftVerticalData.csv' INTO TABLE LiftData
  FIELDS TERMINATED BY ','
  LINES TERMINATED BY '\n'
  IGNORE 1 LINES
  (LiftID,Vertical);
  
#/**
  LOAD DATA LOCAL INFILE '~/BSDSAssignment2Day1.csv' INTO TABLE SkiRecords
  FIELDS TERMINATED BY ','
  LINES TERMINATED BY '\n'
  IGNORE 1 LINES
  (ResortID,DayNumber,SkierID,LiftID,TimeScanned);
  #**/

Create Table Devices(
DeviceId Int Identity(1,1) Primary Key,
IpAddress Varchar(16) Not Null,
Port Varchar(6),
Category Varchar(20),
CreationTime DateTime Default(GetDate()) Not Null,
ModTime DateTime
);

Create Table DataRecords(
RecordId Int Identity(1,1) Primary Key,
Mac Varchar(18) Not Null,
Category Varchar(10) Not Null,
Data Int Not Null,
Position Varchar(20),
CreationTime DateTime Default(GetDate()) Not Null,
SendTime DateTime Default(GetDate()) Not Null,
DbTime DateTime Default(GetDate()) Not Null
);


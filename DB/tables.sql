-- Create Table Products(
-- ProductId Int Identity(1,1) Primary Key,
-- Name Varchar(100) Not Null,
-- Category Varchar(100),
-- Color Varchar(20),
-- UnitPrice Decimal Not Null,
-- AvailableQuantity Int Not Null)

-- Create Table UserInfo(
-- UserId Int Identity(1,1) Not null Primary Key,
-- FirstName Varchar(30) Not null,
-- LastName Varchar(30) Not null,
-- UserName Varchar(30) Not null,
-- Email Varchar(50) Not null,
-- Password Varchar(20) Not null,
-- CreatedDate DateTime Default(GetDate()) Not Null)

-- Insert Into UserInfo(FirstName, LastName, UserName, Email, Password) 
-- Values ('Inventory', 'Admin', 'InventoryAdmin', 'InventoryAdmin@abc.com', '$admin@2017')


Create Table Devices(
DeviceId Int Identity(1,1) Primary Key,
IpAddress Varchar(16) Not Null,
Category Varchar(20),
CreationTime DateTime Default(GetDate()) Not Null,
ModTime DateTime
);

Create Table DataRecords(
RecordId Int Identity(1,1) Primary Key,
Mac Varchar(18) Not Null,
Category Varchar(10) Not Null,
Data Int Not Null,
CreationTime DateTime Default(GetDate()) Not Null,
SendTime DateTime Default(GetDate()) Not Null,
DbTime DateTime Default(GetDate()) Not Null
);


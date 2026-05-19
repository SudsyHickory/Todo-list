TRUNCATE TABLE task RESTART IDENTITY;

INSERT INTO task (title, description, status, version) VALUES
('Buy milk', 'two litres', 'TODO', 0),
('Write report', NULL, 'TODO', 0),
('Submit timesheet', 'for last week', 'DONE', 0);
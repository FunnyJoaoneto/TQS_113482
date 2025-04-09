CREATE TABLE restaurant (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    location VARCHAR(255),
    location_code INTEGER,
    capacity INTEGER,
    opening_time VARCHAR(10),
    closing_time VARCHAR(10)
);

INSERT INTO restaurant (name, location, location_code, capacity, opening_time, closing_time) VALUES 
('Campus Cafeteria', 'Aveiro', 1010500, 200, '08:00', '20:00'),
('Library Cafe', 'Coimbra', 1060300, 50, '09:00', '18:00'),
('Sports Bar', 'Porto', 1131200, 150, '12:00', '23:00'),
('Beja Bistro', 'Beja', 1020500, 100, '10:00', '22:00'),
('Braga Grill', 'Braga', 1030300, 80, '11:00', '23:00'),
('Guimarães Tavern', 'Guimarães', 1030800, 120, '09:00', '21:00'),
('Bragança Diner', 'Bragança', 1040200, 60, '07:00', '19:00'),
('Lisbon Lounge', 'Lisboa', 1110600, 250, '08:00', '23:00'),
('Faro Seafood', 'Faro', 1080500, 150, '10:00', '23:00'),
('Viseu Vineyard', 'Viseu', 1182300, 90, '09:00', '21:00');


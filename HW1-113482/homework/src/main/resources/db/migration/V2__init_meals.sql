CREATE TABLE meal (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    description TEXT,
    price DOUBLE PRECISION,
    restaurant_id INTEGER REFERENCES restaurant(id)
);

INSERT INTO meal (name, description, price, restaurant_id) VALUES 
-- Campus Cafeteria (Restaurant ID 1)
('Pasta Carbonara', 'Creamy pasta with bacon', 12.2, 1),
('Grilled Chicken Salad', 'Healthy grilled chicken with fresh veggies', 13.0, 1),
('Beef Stew', 'Tender beef stew with mashed potatoes', 14.5, 1),
('Caesar Salad', 'Crisp lettuce, parmesan, and creamy Caesar dressing', 9.5, 1),

-- Library Cafe (Restaurant ID 2)
('Vegetarian Pizza', 'Pizza with fresh vegetables and cheese', 5.0, 2),
('Tomato Basil Soup', 'Rich and savory tomato soup with fresh basil', 6.0, 2),
('Quinoa Salad', 'A healthy salad with quinoa, avocado, and chickpeas', 7.5, 2),
('Avocado Toast', 'Whole grain toast topped with mashed avocado and a poached egg', 8.0, 2),

-- Sports Bar (Restaurant ID 3)
('Cheeseburger', 'Juicy burger with cheese and fries', 50.0, 3),
('Buffalo Wings', 'Spicy chicken wings with ranch dipping sauce', 12.5, 3),
('Fish Tacos', 'Grilled fish with salsa and avocado on soft tortillas', 14.0, 3),
('BBQ Ribs', 'Slow-cooked ribs with barbecue sauce and coleslaw', 18.0, 3),

-- Beja Bistro (Restaurant ID 4)
('Grilled Salmon', 'Fresh salmon fillet grilled to perfection', 16.0, 4),
('Chicken Alfredo', 'Creamy Alfredo pasta with grilled chicken', 15.5, 4),
('Caprese Salad', 'Tomatoes, mozzarella, and basil with balsamic drizzle', 8.0, 4),

-- Braga Grill (Restaurant ID 5)
('BBQ Chicken Skewers', 'Marinated chicken skewers grilled with a smoky barbecue glaze', 11.0, 5),
('Steak Frites', 'Grilled steak served with crispy French fries', 18.0, 5),
('Grilled Veggie Platter', 'Assorted grilled vegetables served with rice', 10.0, 5),
('Beef Tacos', 'Seasoned beef with toppings in soft taco shells', 9.5, 5),

-- Guimarães Tavern (Restaurant ID 6)
('Traditional Fish & Chips', 'Crispy fried fish served with golden fries', 13.5, 6),
('Roast Chicken', 'Oven-roasted chicken with vegetables', 14.0, 6),
('Bangers & Mash', 'Sausages served with mashed potatoes and gravy', 12.5, 6),

-- Bragança Diner (Restaurant ID 7)
('Pork Schnitzel', 'Breaded pork served with potato salad', 15.0, 7),
('Vegetarian Chili', 'Hearty chili made with beans and fresh vegetables', 11.5, 7),
('Fried Calamari', 'Crispy calamari served with marinara sauce', 9.0, 7),

-- Lisbon Lounge (Restaurant ID 8)
('Grilled Steak', 'Juicy steak served with grilled vegetables', 22.0, 8),
('Lobster Pasta', 'Pasta with fresh lobster and a creamy sauce', 24.5, 8),
('Paella', 'Spanish rice dish with seafood and chicken', 20.0, 8),

-- Faro Seafood (Restaurant ID 9)
('Grilled Shrimp', 'Juicy grilled shrimp with lemon butter sauce', 16.0, 9),
('Clams Casino', 'Baked clams with bacon, garlic, and breadcrumbs', 12.5, 9),
('Fish and Chips', 'Crispy battered fish served with fries', 14.0, 9),

-- Viseu Vineyard (Restaurant ID 10)
('Duck Breast', 'Pan-seared duck breast with a sweet berry sauce', 19.0, 10),
('Gnocchi', 'Homemade gnocchi in a creamy sage butter sauce', 16.0, 10),
('Cheese Platter', 'A selection of cheeses served with crackers', 10.0, 10);


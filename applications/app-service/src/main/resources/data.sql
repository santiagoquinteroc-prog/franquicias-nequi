INSERT INTO franchises (name) VALUES ('McDonald''s')
    ON CONFLICT (name) DO NOTHING;

INSERT INTO franchises (name) VALUES ('Burger King')
    ON CONFLICT (name) DO NOTHING;

INSERT INTO franchises (name) VALUES ('Subway')
    ON CONFLICT (name) DO NOTHING;


INSERT INTO branches (franchise_id, name)
SELECT id, 'McDonald''s Centro'
FROM franchises
WHERE name = 'McDonald''s'
    ON CONFLICT (franchise_id, name) DO NOTHING;

INSERT INTO branches (franchise_id, name)
SELECT id, 'McDonald''s Norte'
FROM franchises
WHERE name = 'McDonald''s'
    ON CONFLICT (franchise_id, name) DO NOTHING;

INSERT INTO branches (franchise_id, name)
SELECT id, 'Burger King Mall'
FROM franchises
WHERE name = 'Burger King'
    ON CONFLICT (franchise_id, name) DO NOTHING;

INSERT INTO branches (franchise_id, name)
SELECT id, 'Subway Estación'
FROM franchises
WHERE name = 'Subway'
    ON CONFLICT (franchise_id, name) DO NOTHING;


INSERT INTO products (branch_id, name, stock)
SELECT b.id, 'Big Mac', 50
FROM branches b
         JOIN franchises f ON f.id = b.franchise_id
WHERE f.name = 'McDonald''s' AND b.name = 'McDonald''s Centro'
    ON CONFLICT (branch_id, name) DO NOTHING;

INSERT INTO products (branch_id, name, stock)
SELECT b.id, 'Papas Fritas', 120
FROM branches b
         JOIN franchises f ON f.id = b.franchise_id
WHERE f.name = 'McDonald''s' AND b.name = 'McDonald''s Centro'
    ON CONFLICT (branch_id, name) DO NOTHING;

INSERT INTO products (branch_id, name, stock)
SELECT b.id, 'McPollo', 40
FROM branches b
         JOIN franchises f ON f.id = b.franchise_id
WHERE f.name = 'McDonald''s' AND b.name = 'McDonald''s Norte'
    ON CONFLICT (branch_id, name) DO NOTHING;

INSERT INTO products (branch_id, name, stock)
SELECT b.id, 'Whopper', 35
FROM branches b
         JOIN franchises f ON f.id = b.franchise_id
WHERE f.name = 'Burger King' AND b.name = 'Burger King Mall'
    ON CONFLICT (branch_id, name) DO NOTHING;

INSERT INTO products (branch_id, name, stock)
SELECT b.id, 'Onion Rings', 80
FROM branches b
         JOIN franchises f ON f.id = b.franchise_id
WHERE f.name = 'Burger King' AND b.name = 'Burger King Mall'
    ON CONFLICT (branch_id, name) DO NOTHING;

INSERT INTO products (branch_id, name, stock)
SELECT b.id, 'Sub de Pollo', 25
FROM branches b
         JOIN franchises f ON f.id = b.franchise_id
WHERE f.name = 'Subway' AND b.name = 'Subway Estación'
    ON CONFLICT (branch_id, name) DO NOTHING;

INSERT INTO products (branch_id, name, stock)
SELECT b.id, 'Sub de Atún', 20
FROM branches b
         JOIN franchises f ON f.id = b.franchise_id
WHERE f.name = 'Subway' AND b.name = 'Subway Estación'
    ON CONFLICT (branch_id, name) DO NOTHING;



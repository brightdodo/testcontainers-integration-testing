CREATE TABLE IF NOT EXISTS denomination (
                                            id INT PRIMARY KEY,
                                            name VARCHAR(50),
                                            currency VARCHAR(10),
                                            value DECIMAL(10, 2),
                                            coin BOOLEAN,
                                            bagged_quantity INT,
                                            bundled_quantity INT,
                                            plastic_quantity INT,
                                            version INT
);
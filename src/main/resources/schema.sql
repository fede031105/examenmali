-- Creamos las tablas SOLO si no existen.
-- Esto no borra tus 50 productos si ya están ahí.

CREATE TABLE IF NOT EXISTS clientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    telefono VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255),
    descripcion VARCHAR(255),
    precio DOUBLE,
    stock INT
);

CREATE TABLE IF NOT EXISTS servicios_reparacion (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255),
    descripcion VARCHAR(255),
    precio DOUBLE
);

CREATE TABLE IF NOT EXISTS facturas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    total DOUBLE,
    fecha DATETIME,
    pagada BOOLEAN,
    cantidad INT,
    item_nombre VARCHAR(255),
    cliente_nombre VARCHAR(255)
);
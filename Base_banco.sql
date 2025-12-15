CREATE DATABASE banco;
USE banco;
drop table cuentas;
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario VARCHAR(50) UNIQUE,
    password VARCHAR(50),
    nombre VARCHAR(100),
    saldo DECIMAL(10,2),
    activo BOOLEAN DEFAULT TRUE,
    intentos INT DEFAULT 0
);
 
-- truncate table usuarios;

INSERT INTO usuarios (usuario, password,nombre, saldo) VALUES
('cliente123', 'clave456', 'Usuario1', 1000),
('Camila Bueno', 'CB1234', 'Camila Bueno', 1000),
('Leonor Yumi', 'LY5678', 'Leonor Yumi', 1000),
('user', '123', 'UsuarioPrueba', 1000);



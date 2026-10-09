-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: orientapsi_db
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `id_rol` int NOT NULL,
  `nombres` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `apellidos` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `correo` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
  `clave_hash` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `telefono` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `estado` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'ACTIVO',
  `fecha_registro` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `correo` (`correo`),
  KEY `id_rol` (`id_rol`),
  CONSTRAINT `usuario_ibfk_1` FOREIGN KEY (`id_rol`) REFERENCES `rol` (`id_rol`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,1,'Admin','Mente Sana','admin@mentesana.com','$2a$12$sGiLpe4WVhQc3e5MAk/SA.sfzpuR0jLd7zhBW9cT8sQzXU0ERykci','999888777','ACTIVO','2026-09-05 16:18:10'),(2,2,'Laura','Gómez','laura.gomez@mentesana.com','psico123','987654321','ACTIVO','2026-09-05 15:23:02'),(3,3,'Carlos','Pérez','carlos.perez@gmail.com','$2a$12$2mp6NDnxeHHAC3aNAcp58OBNYY4Fvhpt08aOVg.swebRx96Fy51Hm','912345678','ACTIVO','2026-09-05 15:23:02'),(4,2,'jose','vena','hola123@gmail.com','$2a$12$LDPNiyTuofN9DLnxQIifnOxbfCc9vMFdgbPc39xuDWDKbGzaC.kgS',NULL,'ACTIVO','2026-09-05 16:19:06'),(5,2,'Psicologo','Prueba','psicologoprueba@mentesana.com','$2a$12$LkDAHGXwJdsM5Anrc4Z/z.Nc43v2aj4TX5ePYBkDAAIFI3jyGanMy',NULL,'ACTIVO','2026-10-05 17:14:09'),(6,3,'Paciente','Prueba','paciente@mentesana.com','$2a$12$9jVO06wgRfLLdaEWYGMvhunSvC276H3Va6RnTmgIG8XFU1PY0nt6u',NULL,'ACTIVO','2026-10-05 17:41:25'),(7,2,'María','María','maria.torres@test.com','$2a$12$XFRoOGfTjnEzmPrMkGQqF.4kUkiUROqLTmDbGPc/uL601mrp.etDm',NULL,'ACTIVO','2026-10-08 19:57:27'),(8,3,'Ana','Ramírez Soto','ana.ramirez@test.com','$2a$12$RYez1Ic7eOX3qLg9gqSeeu9GFycXOWw15yKJ1lvK76Tx.IoYiKs.e',NULL,'ACTIVO','2026-10-08 20:06:56'),(9,3,'Luis','Mendoza Ruiz','luis.mendoza@test.com','$2a$12$F3j2QCaHgLxhkGQ3g.dqH.JgMEw2UZPKyt74SI5r6I3p3XeneiYHi',NULL,'ACTIVO','2026-10-08 20:18:24');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-08 20:23:33

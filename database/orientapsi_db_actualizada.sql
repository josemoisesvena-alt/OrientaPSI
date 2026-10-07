CREATE DATABASE  IF NOT EXISTS `orientapsi_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `orientapsi_db`;
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
-- Table structure for table `cita`
--

DROP TABLE IF EXISTS `cita`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cita` (
  `id_cita` int NOT NULL AUTO_INCREMENT,
  `id_paciente` int NOT NULL,
  `id_horario` int NOT NULL,
  `motivo_consulta` varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `estado` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'PENDIENTE',
  `fecha_solicitud` datetime DEFAULT CURRENT_TIMESTAMP,
  `observacion_admin` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id_cita`),
  UNIQUE KEY `id_horario` (`id_horario`),
  KEY `id_paciente` (`id_paciente`),
  CONSTRAINT `cita_ibfk_1` FOREIGN KEY (`id_paciente`) REFERENCES `paciente` (`id_paciente`) ON DELETE CASCADE,
  CONSTRAINT `cita_ibfk_2` FOREIGN KEY (`id_horario`) REFERENCES `horario` (`id_horario`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cita`
--

LOCK TABLES `cita` WRITE;
/*!40000 ALTER TABLE `cita` DISABLE KEYS */;
INSERT INTO `cita` VALUES (3,1,1,'Consulta por estrés laboral','CONFIRMADA','2026-09-05 16:37:52',NULL),(6,1,2,'Depresion ','CONFIRMADA','2026-09-05 16:55:38',NULL),(19,1,9,'quiero','CONFIRMADA','2026-09-05 18:11:35',NULL),(20,1,10,'Prueba','CONFIRMADA','2026-09-05 18:17:49',NULL),(21,1,11,'hola','CONFIRMADA','2026-09-05 18:30:52',NULL),(22,1,12,'Traumas ','CONFIRMADA','2026-09-05 19:01:06',NULL),(23,1,13,'Consulta ','CONFIRMADA','2026-10-05 16:32:40',NULL),(24,1,14,'consultas','CANCELADA','2026-10-05 17:15:28',NULL),(25,3,15,'Prueba','PENDIENTE','2026-10-05 17:42:22',NULL);
/*!40000 ALTER TABLE `cita` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `especialidad`
--

DROP TABLE IF EXISTS `especialidad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `especialidad` (
  `id_especialidad` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `estado` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'ACTIVO',
  PRIMARY KEY (`id_especialidad`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `especialidad`
--

LOCK TABLES `especialidad` WRITE;
/*!40000 ALTER TABLE `especialidad` DISABLE KEYS */;
/*!40000 ALTER TABLE `especialidad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `horario`
--

DROP TABLE IF EXISTS `horario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `horario` (
  `id_horario` int NOT NULL AUTO_INCREMENT,
  `id_psicologo` int NOT NULL,
  `fecha` date NOT NULL,
  `hora_inicio` time NOT NULL,
  `hora_fin` time NOT NULL,
  `modalidad` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `estado` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'DISPONIBLE',
  `enlace_virtual` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id_horario`),
  UNIQUE KEY `uq_horario_psicologo` (`id_psicologo`,`fecha`,`hora_inicio`,`hora_fin`),
  CONSTRAINT `horario_ibfk_1` FOREIGN KEY (`id_psicologo`) REFERENCES `psicologo` (`id_psicologo`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `horario`
--

LOCK TABLES `horario` WRITE;
/*!40000 ALTER TABLE `horario` DISABLE KEYS */;
INSERT INTO `horario` VALUES (1,1,'2026-09-10','10:00:00','11:00:00','PRESENCIAL','OCUPADO',NULL),(2,1,'2026-09-12','11:00:00','12:00:00','VIRTUAL','OCUPADO',NULL),(9,4,'2026-09-07','20:10:00','20:15:00','AMBAS','OCUPADO',NULL),(10,4,'2026-09-25','10:17:00','11:17:00','AMBAS','OCUPADO',NULL),(11,4,'2026-09-30','21:30:00','22:30:00','VIRTUAL','OCUPADO',NULL),(12,4,'2026-09-06','01:00:00','02:00:00','AMBAS','OCUPADO',NULL),(13,4,'2026-10-06','10:31:00','11:31:00','PRESENCIAL','OCUPADO',NULL),(14,5,'2026-10-08','11:00:00','12:00:00','PRESENCIAL','DISPONIBLE',NULL),(15,5,'2026-10-20','09:41:00','10:41:00','VIRTUAL','OCUPADO',NULL);
/*!40000 ALTER TABLE `horario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paciente`
--

DROP TABLE IF EXISTS `paciente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `paciente` (
  `id_paciente` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  `estado` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'ACTIVO',
  PRIMARY KEY (`id_paciente`),
  UNIQUE KEY `id_usuario` (`id_usuario`),
  CONSTRAINT `paciente_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paciente`
--

LOCK TABLES `paciente` WRITE;
/*!40000 ALTER TABLE `paciente` DISABLE KEYS */;
INSERT INTO `paciente` VALUES (1,3,'1995-05-12','ACTIVO'),(3,6,'2015-03-29','ACTIVO');
/*!40000 ALTER TABLE `paciente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `psicologo`
--

DROP TABLE IF EXISTS `psicologo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `psicologo` (
  `id_psicologo` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `numero_colegiatura` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `presentacion` text COLLATE utf8mb4_unicode_ci,
  `modalidad` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'AMBAS',
  `anios_experiencia` int DEFAULT '0',
  `estado` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'ACTIVO',
  `foto_perfil` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id_psicologo`),
  UNIQUE KEY `id_usuario` (`id_usuario`),
  UNIQUE KEY `numero_colegiatura` (`numero_colegiatura`),
  CONSTRAINT `psicologo_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `psicologo`
--

LOCK TABLES `psicologo` WRITE;
/*!40000 ALTER TABLE `psicologo` DISABLE KEYS */;
INSERT INTO `psicologo` VALUES (1,2,'CPsP-45892','Especialista en psicología clínica.','AMBAS',8,'ACTIVO',NULL),(4,4,'CPSP-55555','Especialista en psicología clínica y educativa.','VIRTUAL',4,'ACTIVO',NULL),(5,5,'72538814','PROFESIONAL','AMBAS',8,'ACTIVO',NULL);
/*!40000 ALTER TABLE `psicologo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `psicologo_especialidad`
--

DROP TABLE IF EXISTS `psicologo_especialidad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `psicologo_especialidad` (
  `id_psicologo` int NOT NULL,
  `id_especialidad` int NOT NULL,
  `fecha_asignacion` date DEFAULT (curdate()),
  PRIMARY KEY (`id_psicologo`,`id_especialidad`),
  KEY `id_especialidad` (`id_especialidad`),
  CONSTRAINT `psicologo_especialidad_ibfk_1` FOREIGN KEY (`id_psicologo`) REFERENCES `psicologo` (`id_psicologo`) ON DELETE CASCADE,
  CONSTRAINT `psicologo_especialidad_ibfk_2` FOREIGN KEY (`id_especialidad`) REFERENCES `especialidad` (`id_especialidad`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `psicologo_especialidad`
--

LOCK TABLES `psicologo_especialidad` WRITE;
/*!40000 ALTER TABLE `psicologo_especialidad` DISABLE KEYS */;
/*!40000 ALTER TABLE `psicologo_especialidad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rol`
--

DROP TABLE IF EXISTS `rol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rol` (
  `id_rol` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `estado` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'ACTIVO',
  PRIMARY KEY (`id_rol`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rol`
--

LOCK TABLES `rol` WRITE;
/*!40000 ALTER TABLE `rol` DISABLE KEYS */;
INSERT INTO `rol` VALUES (1,'ADMINISTRADOR','Gestiona usuarios y agendas','ACTIVO'),(2,'PSICOLOGO','Atiende consultas y revisa su agenda','ACTIVO'),(3,'PACIENTE','Solicita y programa citas web','ACTIVO');
/*!40000 ALTER TABLE `rol` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,1,'Admin','Mente Sana','admin@mentesana.com','$2a$12$sGiLpe4WVhQc3e5MAk/SA.sfzpuR0jLd7zhBW9cT8sQzXU0ERykci','999888777','ACTIVO','2026-09-05 16:18:10'),(2,2,'Laura','Gómez','laura.gomez@mentesana.com','psico123','987654321','ACTIVO','2026-09-05 15:23:02'),(3,3,'Carlos','Pérez','carlos.perez@gmail.com','$2a$12$2mp6NDnxeHHAC3aNAcp58OBNYY4Fvhpt08aOVg.swebRx96Fy51Hm','912345678','ACTIVO','2026-09-05 15:23:02'),(4,2,'jose','vena','hola123@gmail.com','$2a$12$LDPNiyTuofN9DLnxQIifnOxbfCc9vMFdgbPc39xuDWDKbGzaC.kgS',NULL,'ACTIVO','2026-09-05 16:19:06'),(5,2,'Psicologo','Prueba','psicologoprueba@mentesana.com','$2a$12$LkDAHGXwJdsM5Anrc4Z/z.Nc43v2aj4TX5ePYBkDAAIFI3jyGanMy',NULL,'ACTIVO','2026-10-05 17:14:09'),(6,3,'Paciente','Prueba','paciente@mentesana.com','$2a$12$9jVO06wgRfLLdaEWYGMvhunSvC276H3Va6RnTmgIG8XFU1PY0nt6u',NULL,'ACTIVO','2026-10-05 17:41:25');
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

-- Dump completed on 2026-10-07  9:55:05

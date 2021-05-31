-- phpMyAdmin SQL Dump
-- version 5.0.3
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3300
-- Généré le : sam. 29 mai 2021 à 00:12
-- Version du serveur :  10.4.14-MariaDB
-- Version de PHP : 7.4.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `maskdetection`
--

-- --------------------------------------------------------

--
-- Structure de la table `alarms`
--

CREATE TABLE `alarms` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `path` longtext NOT NULL,
  `selected` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `alarms`
--

INSERT INTO `alarms` (`id`, `name`, `path`, `selected`) VALUES
(19, 'a1', 'C:\\Users\\Lahrach Omar\\Downloads\\Music\\Alarm-Fast-High-Pitch-A1-www.fesliyanstudios.com.mp3', 0),
(20, 'a2', 'C:\\Users\\Lahrach Omar\\Music\\Zoe_Wees_-_Control_(Official_Video)(256k).mp3', 0),
(21, 'a3', 'C:\\Users\\Lahrach Omar\\Downloads\\Music\\Alarm-Fast-High-Pitch-A2-www.fesliyanstudios.com.mp3', 0),
(22, 'a4', 'C:\\Users\\Lahrach Omar\\Downloads\\Music\\Alarm-Fast-High-Pitch-A3-Ring-Tone-www.fesliyanstudios.com.mp3', 1);

-- --------------------------------------------------------

--
-- Structure de la table `cameras`
--

CREATE TABLE `cameras` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `ipAddress` varchar(50) NOT NULL,
  `port` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `cameras`
--

INSERT INTO `cameras` (`id`, `name`, `ipAddress`, `port`) VALUES
(140, 'caméra 1', '192.168.1.1', 8080),
(141, 'caméra 2', '192.168.1.2', 8080),
(142, 'caméra 3', '192.168.1.3', 8080),
(144, 'caméra pc', '0', 0),
(145, 'cam', '192.168.8.100', 8080),
(146, 'cam1', '192.168.43.1', 8080);

-- --------------------------------------------------------

--
-- Structure de la table `persons`
--

CREATE TABLE `persons` (
  `id` int(11) NOT NULL,
  `photo` longtext NOT NULL,
  `number_of_detections` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `persons`
--

INSERT INTO `persons` (`id`, `photo`, `number_of_detections`) VALUES
(11, 'C:\\Users\\Lahrach Omar\\Downloads\\Work space\\Education\\S4\\PFA\\Je-te-surveille\\src\\app\\photos\\27-05-2021photo.jpg', 1),
(12, 'C:\\Users\\Lahrach Omar\\Downloads\\Work space\\Education\\S4\\PFA\\Je-te-surveille\\src\\app\\photos\\28-05-2021photo.jpg', 1);

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `fullName` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `users`
--

INSERT INTO `users` (`id`, `username`, `fullName`, `email`, `password`) VALUES
(4, 'admin', 'lahrach', '@', '202cb962ac59075b964b07152d234b70'),
(5, 'user', 'omar', '@gmail', '5d41402abc4b2a76b9719d911017c592'),
(6, 'hi', 'hi', 'hi', '49f68a5c8493ec2c0bf489821c21fc3b'),
(7, 'hhhh', 'hhhh', 'hhhh', 'e61e7de603852182385da5e907b4b232'),
(8, 'hihi', 'hihi', 'hihi', 'e9f5713dec55d727bb35392cec6190ce'),
(9, 'haha', 'haha', 'haha', '4e4d6c332b6fe62a63afe56171fd3725'),
(10, 's', 's', 's', '03c7c0ace395d80182db07ae2c30f034'),
(11, 'j', 'j', 'j', '363b122c528f54df4a0446b6bab05515'),
(12, 'l', 'l', 'l', '2db95e8e1a9267b7a1188556b2013b33'),
(13, 'm', 'm', 'm', '6f8f57715090da2632453988d9a1501b'),
(14, 'a', 'a', 'a', '0cc175b9c0f1b6a831c399e269772661'),
(15, 'k', 'k', 'k', '8ce4b16b22b58894aa86c421e8759df3'),
(16, 'o', 'o', 'o', 'd95679752134a2d9eb61dbd7b91c4bcc'),
(17, 'jdhff', 'el mehdi', 'elmaehdi@gmail.com', 'e10adc3949ba59abbe56e057f20f883e');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `alarms`
--
ALTER TABLE `alarms`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `cameras`
--
ALTER TABLE `cameras`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `persons`
--
ALTER TABLE `persons`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `alarms`
--
ALTER TABLE `alarms`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT pour la table `cameras`
--
ALTER TABLE `cameras`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=147;

--
-- AUTO_INCREMENT pour la table `persons`
--
ALTER TABLE `persons`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT pour la table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

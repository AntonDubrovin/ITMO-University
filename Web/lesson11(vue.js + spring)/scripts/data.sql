CREATE TABLE `post` (
  `id` bigint(20) NOT NULL,
  `creation_time` datetime DEFAULT NULL,
  `text` longtext NOT NULL,
  `title` varchar(60) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `post` (`id`, `creation_time`, `text`, `title`, `user_id`) VALUES
(1, '2019-12-16 17:59:44', '���� ������!\r\n\r\n����� ��������� Codeforces Global Round 6, ����� ������: �������, 17 ������� 2019 �. � 18:05.\r\n\r\n��� ������ ����� �� ����� Codeforces Global Rounds, ������� ���������� ��� ��������� XTX Markets. � ������� ����� ����������� ���, ������� ���� ����� ���������� ��� ����.\r\n\r\n������������ ��������� 2 ���� 30 �����, ��� ������� 8 �����, ��� ���� ���� �� ����� ����� ������������ � ���� �������. ����������� ����� ��������� ��������� �� ������ ������.', 'Codeforces Global Round 6', 1),
(2, '2019-12-16 18:00:50', '� ���� �������� ������� �� ����� ��� ������ ����� \"��������� � ��������� ������\", ������� � ����� � ����. ������ ���������� � ������ ����� �� ���� � ����� ������������� �� ����.\r\n\r\n���� ������ �������������, � �� �����������, �� ����� ������ ���������� (� �� ������) ������������� ���� ����� ���������. ��������, ��� ������.', '����������� ����� ����� � ����', 2),
(3, '2019-12-16 18:00:39', '������! � �������, 12 ������� 2019 �. � 16:35 ������� Codeforces Round #605 (Div. 3) � ��������� Codeforces ����� ��� �������� ���������. � ���� ������ ����� 6 ��� 7 ����� (��� 8), ������� ��������� �� ��������� ���, ����� ��������� ���������� ������������ ��� ���������� � ���������� �� 1600. ������ ��� ��������, ��� ������� 1600 � ���� ����� ������������������ �� ����� ��� ��������.\r\n\r\n����� ������� �� �������� ��������������� �������. ����� �������, �� ����� ������ ������ ����� ������������� �� ��������������� ������, � ����� ������ ����� 12-�� ������� ���� �������� �������. � ���������� ������� ��������� ����� � ��� �� ��� � �� ���� ���������, ���� � ������ �������� ������� ����� ��������� ��������.\r\n\r\n��� ����� ���������� 6 ��� 7 (��� 8) ����� � 2 ���� �� �� �������.\r\n\r\n����� �� �������� ������� � ���� ������ (� ����������� Div. 3 �������) ����� ��������� 10 �������.', 'Codeforces Round #605 (Div. 3)', 1);

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `admin` bit(1) NOT NULL,
  `creation_time` datetime DEFAULT NULL,
  `login` varchar(24) NOT NULL,
  `name` varchar(100) NOT NULL,
  `passwordSha` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `user` (`id`, `admin`, `creation_time`, `login`, `name`, `passwordSha`) VALUES
(1, b'0', '2019-12-16 17:57:45', 'mike', 'Mike Mirzayanov', '783a78e11175b9d9699599b39c475ab77950ee31'),
(2, b'0', '2019-12-16 17:58:40', 'tester', 'Tester Testerov', '4c6f0278e40dbd369cf6406b29123b18cc3df578');

ALTER TABLE `post`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK72mt33dhhs48hf9gcqrq4fxte` (`user_id`);

ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKew1hvam8uwaknuaellwhqchhb` (`login`);

ALTER TABLE `post`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

ALTER TABLE `user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

ALTER TABLE `post`
  ADD CONSTRAINT `FK72mt33dhhs48hf9gcqrq4fxte` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

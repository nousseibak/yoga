-- Création de la table TEACHERS
CREATE TABLE IF NOT EXISTS TEACHERS (
  id INT PRIMARY KEY AUTO_INCREMENT,
  last_name VARCHAR(40),
  first_name VARCHAR(40),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Création de la table SESSIONS
CREATE TABLE IF NOT EXISTS SESSIONS (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50),
  description VARCHAR(2000),
  date TIMESTAMP,
  teacher_id INT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (teacher_id) REFERENCES TEACHERS(id)
);

-- Création de la table USERS
CREATE TABLE IF NOT EXISTS USERS (
  id INT PRIMARY KEY AUTO_INCREMENT,
  last_name VARCHAR(40),
  first_name VARCHAR(40),
  admin BOOLEAN NOT NULL DEFAULT false,
  email VARCHAR(255),
  password VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Création de la table PARTICIPATE
CREATE TABLE IF NOT EXISTS PARTICIPATE (
  user_id INT,
  session_id INT,
  FOREIGN KEY (user_id) REFERENCES USERS(id),
  FOREIGN KEY (session_id) REFERENCES SESSIONS(id)
);


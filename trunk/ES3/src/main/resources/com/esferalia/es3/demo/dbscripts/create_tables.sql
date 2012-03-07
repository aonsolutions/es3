#
# Structure for the `mission` table : 
#

CREATE TABLE `mission` (
  `id` int(4) NOT NULL auto_increment COMMENT 'Identificador unico de la mision',
  `name` varchar(64) NOT NULL collate latin1_spanish_ci COMMENT 'Nombre de la mision',
  `alias` varchar(32) collate latin1_spanish_ci default NULL COMMENT 'Alias de la mision',
  `description` TEXT collate latin1_spanish_ci default NULL COMMENT 'Descripcion de la mision',
  `start_date` DATE NOT NULL COMMENT 'Fecha de inicio de la mision',
  `end_date` DATE NOT NULL COMMENT 'Fecha de fin de la mision',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci COMMENT='Mision';

#
# Structure for the `file` table : 
#

CREATE TABLE `file` (
  `id` int(4) NOT NULL auto_increment COMMENT 'Identificador unico del archivo',
  `mission` int(4) NOT NULL COMMENT 'Identificador unico de la mision',
  `name` varchar(64) collate latin1_spanish_ci default NULL COMMENT 'Nombre original del archivo',
  `type` varchar(15) collate latin1_spanish_ci default NULL COMMENT 'Tipo de archivo: imagen, video, audio, cartografia, documento',
  `description` text collate latin1_spanish_ci COMMENT 'Descripcion del archivo',
  `date_time` datetime NOT NULL COMMENT 'Fecha de inicio del archivo',
  `md5` varchar(128) collate latin1_spanish_ci NOT NULL COMMENT 'MD5 del archivo',
  PRIMARY KEY  (`id`),
  KEY `IDX_FILE_MISSION` (`mission`),
  CONSTRAINT `FK_FILE_MISSION` FOREIGN KEY (`mission`) REFERENCES `mission` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci COMMENT='Archivo';

#
# Structure for the `app_param` table : 
#

CREATE TABLE `app_param` (
  `id` int(4) NOT NULL auto_increment COMMENT 'Identificador unico',
  `name` varchar(32) collate latin1_spanish_ci NOT NULL COMMENT 'Nombre del Parametro',
  `value` varchar(64) collate latin1_spanish_ci default NULL COMMENT 'Valor del Parametro',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci COMMENT='Parametros de la Aplicacion';
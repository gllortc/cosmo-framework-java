-- Sequence: sys_users_usrid_seq

-- DROP SEQUENCE sys_users_usrid_seq;

CREATE SEQUENCE sys_users_usrid_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE sys_users_usrid_seq
  OWNER TO postgres;

-- Table: cosmo_users

-- DROP TABLE cosmo_users;

CREATE TABLE cosmo_users
(
  usrid integer NOT NULL DEFAULT nextval('sys_users_usrid_seq'::regclass),
  usrlogin character varying(64) NOT NULL,
  usrmail character varying(255) NOT NULL,
  usrpwd character varying(255) NOT NULL,
  usrname character varying(64) NOT NULL,
  usrcity character varying(64),
  usroptions integer DEFAULT 0,
  usrstatus integer NOT NULL DEFAULT 0,
  usrcreated timestamp with time zone NOT NULL,
  usrlastlogin time with time zone,
  usrlogoncount integer NOT NULL DEFAULT 0,
  CONSTRAINT pk_users PRIMARY KEY (usrid),
  CONSTRAINT uk_users_login UNIQUE (usrlogin),
  CONSTRAINT uk_users_mail UNIQUE (usrmail)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE cosmo_users
  OWNER TO postgres;
GRANT ALL ON TABLE cosmo_users TO postgres;
GRANT ALL ON TABLE cosmo_users TO public;
COMMENT ON TABLE cosmo_users
  IS 'Store user information (accounts)';

-- Table: cosmo_auth_roles

-- DROP TABLE cosmo_auth_roles;

CREATE TABLE cosmo_auth_roles
(
  roledescription text,
  roleappid character varying(255),
  roleid character varying(255) NOT NULL,
  CONSTRAINT "pk_RoleId" PRIMARY KEY (roleid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE cosmo_auth_roles
  OWNER TO postgres;
COMMENT ON TABLE cosmo_auth_roles
  IS 'Store role information';

-- Table: cosmo_auth_activity

-- DROP TABLE cosmo_auth_activity;

CREATE TABLE cosmo_auth_activity
(
  actid character varying(255) NOT NULL, -- Activity unique identifier
  actdefaultgrant boolean NOT NULL DEFAULT false, -- Default granting for all users:...
  actdescription text,
  actenabled boolean NOT NULL DEFAULT true,
  CONSTRAINT "pk_Activity" PRIMARY KEY (actid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE cosmo_auth_activity
  OWNER TO postgres;
COMMENT ON TABLE cosmo_auth_activity
  IS 'Store authorized activities';
COMMENT ON COLUMN cosmo_auth_activity.actid IS 'Activity unique identifier';
COMMENT ON COLUMN cosmo_auth_activity.actdefaultgrant IS 'Default granting for all users:

true -> by default, activity is GRANTED for all users
false -> by default, activity is DENIED for all users';

-- Table: cosmo_auth_user_role

-- DROP TABLE cosmo_auth_user_role;

CREATE TABLE cosmo_auth_user_role
(
  usrlogin character varying(64) NOT NULL,
  roleid character varying(255) NOT NULL,
  CONSTRAINT "pk_UserRole" PRIMARY KEY (roleid, usrlogin)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE cosmo_auth_user_role
  OWNER TO postgres;
COMMENT ON TABLE cosmo_auth_user_role
  IS 'Implements relation between USER and ROLE';

-- Table: cosmo_auth_role_activity

-- DROP TABLE cosmo_auth_role_activity;

CREATE TABLE cosmo_auth_role_activity
(
  actid character varying(255) NOT NULL,
  isgranted boolean NOT NULL DEFAULT true,
  roleid character varying(255) NOT NULL,
  CONSTRAINT "pk_RoleActivities" PRIMARY KEY (roleid, actid),
  CONSTRAINT "fk_ActivityId" FOREIGN KEY (actid)
      REFERENCES cosmo_auth_activity (actid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "fk_RoleId" FOREIGN KEY (roleid)
      REFERENCES cosmo_auth_roles (roleid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE cosmo_auth_role_activity
  OWNER TO postgres;
COMMENT ON TABLE cosmo_auth_role_activity
  IS 'Implements relation between ROLE and ACTIVITIES (privileges)';

-- Table: cosmo_auth_locks

-- DROP TABLE cosmo_auth_locks;

CREATE TABLE cosmo_auth_locks
(
  login character varying(64) NOT NULL,
  fails integer NOT NULL DEFAULT 0,
  lastattempt timestamp with time zone NOT NULL DEFAULT now(),
  ipaddress character varying(255) NOT NULL,
  CONSTRAINT "pk_Locks" PRIMARY KEY (login)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE cosmo_auth_locks
  OWNER TO postgres;
COMMENT ON TABLE cosmo_auth_locks
  IS 'Tabla usada para controlar los bloqueos en cuentas de usuario.';

-- Modify avatar_url to MEDIUMTEXT to allow larger base64 strings
ALTER TABLE users MODIFY COLUMN avatar_url MEDIUMTEXT;

-- Add cover_url column
ALTER TABLE users ADD COLUMN cover_url MEDIUMTEXT DEFAULT NULL;

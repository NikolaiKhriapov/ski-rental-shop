INSERT INTO application_user (id, application_user_role, client_id, email, enabled, locked, password, photo)
VALUES (1, 'ADMIN', null, 'admin', true, false, '$2a$10$2vinkTlnuLKMUh/Ph6fys.0J7b4dc1AnTDDFbR5IfZC3khvQqBZAy', 'no-user-photo')
ON CONFLICT (id) DO NOTHING;
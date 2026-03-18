-- Tạo user photographer
INSERT INTO users (email, password_hash, full_name, phone_number, gender, avatar_url, email_verified, status, created_at)
VALUES 
('photographer1@test.com', '$2a$10$zV/379cngDXttGME.AkBb.uljlBF9klEIagMmVA2W8fPJVkCa9CRm', 'Nguyễn Văn An', '0901234567', 'MALE', 'https://i.pravatar.cc/150?img=1', true, 'ACTIVE', NOW()),
('photographer2@test.com', '$2a$10$zV/379cngDXttGME.AkBb.uljlBF9klEIagMmVA2W8fPJVkCa9CRm', 'Trần Thị Bình', '0902345678', 'FEMALE', 'https://i.pravatar.cc/150?img=2', true, 'ACTIVE', NOW()),
('photographer3@test.com', '$2a$10$zV/379cngDXttGME.AkBb.uljlBF9klEIagMmVA2W8fPJVkCa9CRm', 'Lê Hoàng Nam', '0903456789', 'MALE', 'https://i.pravatar.cc/150?img=3', true, 'ACTIVE', NOW());

-- Gán role PHOTOGRAPHER
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.email IN ('photographer1@test.com', 'photographer2@test.com', 'photographer3@test.com')
AND r.name = 'ROLE_PHOTOGRAPHER';

-- Tạo photographer profiles
INSERT INTO photographers (user_id, bio, city, experience_years, average_rating, approval_status, approved_at, created_at)
VALUES
((SELECT id FROM users WHERE email = 'photographer1@test.com'), 
 'Chuyên chụp ảnh cưới và gia đình tại Hà Nội', 'Ha Noi', 5, 4.8, 'APPROVED', NOW(), NOW()),
((SELECT id FROM users WHERE email = 'photographer2@test.com'), 
 'Nhiếp ảnh gia chân dung và thời trang', 'Ha Noi', 3, 4.5, 'APPROVED', NOW(), NOW()),
((SELECT id FROM users WHERE email = 'photographer3@test.com'), 
 'Chuyên ảnh sự kiện và ngoại cảnh', 'Ho Chi Minh', 7, 4.2, 'APPROVED', NOW(), NOW());

-- Tạo photo concepts
INSERT INTO photo_concepts (name, description) VALUES
('Wedding', 'Chụp ảnh cưới hỏi'),
('Portrait', 'Chụp ảnh chân dung'),
('Family', 'Chụp ảnh gia đình'),
('Event', 'Chụp ảnh sự kiện'),
('Outdoor', 'Chụp ảnh ngoại cảnh');

-- Tạo packages cho photographer 1
INSERT INTO photo_packages (photographer_id, title, description, price, duration_minutes, active, created_at)
VALUES
((SELECT id FROM photographers WHERE user_id = (SELECT id FROM users WHERE email = 'photographer1@test.com')),
 'Gói Cưới Cơ Bản', 'Chụp ảnh cưới 4 tiếng', 1500000, 240, true, NOW()),
((SELECT id FROM photographers WHERE user_id = (SELECT id FROM users WHERE email = 'photographer1@test.com')),
 'Gói Gia Đình', 'Chụp ảnh gia đình 2 tiếng', 800000, 120, true, NOW());

-- Tạo packages cho photographer 2
INSERT INTO photo_packages (photographer_id, title, description, price, duration_minutes, active, created_at)
VALUES
((SELECT id FROM photographers WHERE user_id = (SELECT id FROM users WHERE email = 'photographer2@test.com')),
 'Gói Chân Dung', 'Chụp ảnh chân dung cá nhân', 600000, 90, true, NOW()),
((SELECT id FROM photographers WHERE user_id = (SELECT id FROM users WHERE email = 'photographer2@test.com')),
 'Gói Portrait Cao Cấp', 'Chụp ảnh chân dung studio', 1200000, 180, true, NOW());

-- Tạo packages cho photographer 3
INSERT INTO photo_packages (photographer_id, title, description, price, duration_minutes, active, created_at)
VALUES
((SELECT id FROM photographers WHERE user_id = (SELECT id FROM users WHERE email = 'photographer3@test.com')),
 'Gói Sự Kiện', 'Chụp ảnh sự kiện công ty', 2000000, 300, true, NOW());

-- Gán concepts cho packages
INSERT INTO photo_package_concepts (photo_package_id, photo_concept_id)
VALUES
-- Photographer 1: Wedding + Family concepts
((SELECT id FROM photo_packages WHERE title = 'Gói Cưới Cơ Bản'), (SELECT id FROM photo_concepts WHERE name = 'Wedding')),
((SELECT id FROM photo_packages WHERE title = 'Gói Gia Đình'), (SELECT id FROM photo_concepts WHERE name = 'Family')),
-- Photographer 2: Portrait concepts
((SELECT id FROM photo_packages WHERE title = 'Gói Chân Dung'), (SELECT id FROM photo_concepts WHERE name = 'Portrait')),
((SELECT id FROM photo_packages WHERE title = 'Gói Portrait Cao Cấp'), (SELECT id FROM photo_concepts WHERE name = 'Portrait')),
-- Photographer 3: Event + Outdoor
((SELECT id FROM photo_packages WHERE title = 'Gói Sự Kiện'), (SELECT id FROM photo_concepts WHERE name = 'Event')),
((SELECT id FROM photo_packages WHERE title = 'Gói Sự Kiện'), (SELECT id FROM photo_concepts WHERE name = 'Outdoor'));

-- Tạo availability cho photographer 1 và 2
INSERT INTO photographer_availability (photographer_id, start_time, end_time, created_at)
VALUES
((SELECT id FROM photographers WHERE user_id = (SELECT id FROM users WHERE email = 'photographer1@test.com')),
 '2026-03-12 08:00:00', '2026-03-12 18:00:00', NOW()),
((SELECT id FROM photographers WHERE user_id = (SELECT id FROM users WHERE email = 'photographer2@test.com')),
 '2026-03-12 08:00:00', '2026-03-12 18:00:00', NOW());
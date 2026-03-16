-- Increase character length for image URLs and avatar URLs using TEXT for maximum capacity
ALTER TABLE portfolio_images MODIFY COLUMN image_url TEXT NOT NULL;
ALTER TABLE users MODIFY COLUMN avatar_url TEXT;

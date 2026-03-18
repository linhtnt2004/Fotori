-- Add voucher_id to bookings table
ALTER TABLE bookings ADD COLUMN voucher_code VARCHAR(100);
ALTER TABLE bookings ADD CONSTRAINT fk_booking_voucher FOREIGN KEY (voucher_code) REFERENCES vouchers(code);

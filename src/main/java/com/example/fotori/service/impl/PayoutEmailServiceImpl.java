package com.example.fotori.service.impl;

import com.example.fotori.model.Booking;
import com.example.fotori.service.EmailService;
import com.example.fotori.service.PayoutEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayoutEmailServiceImpl implements PayoutEmailService {

    private final EmailService emailService;

    private static final DateTimeFormatter FMT =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private void send(String to, String subject, String body) {
        emailService.sendHtmlEmail(to, subject, body.replace("\n", "<br/>"));
    }

    private String photographerEmail(Booking b) {
        return b.getPhotographer().getUser().getEmail();
    }

    private String photographerName(Booking b) {
        return b.getPhotographer().getUser().getFullName();
    }

    private String customerName(Booking b) {
        return b.getUser().getFullName();
    }

    // ════════════════════════════════════════════════
    // MAIN FUNCTION
    // ════════════════════════════════════════════════

    @Override
    public void sendPayoutCompletedEmail(Booking booking) {
        send(
            photographerEmail(booking),
            "[Fotori] Bạn đã nhận được thanh toán 💰",
            buildPayoutCompleted(booking)
        );
    }

    // ════════════════════════════════════════════════
    // TEMPLATE
    // ════════════════════════════════════════════════

    private String buildPayoutCompleted(Booking b) {
        return String.format("""
            Xin chào %s,

            🎉 Tin vui! Bạn đã nhận được thanh toán cho đơn hàng trên Fotori.

            ──────────────────────────────
            THÔNG TIN THANH TOÁN
            ──────────────────────────────
            Mã đơn       : #%d
            Khách hàng   : %s
            Gói chụp     : %s
            Ngày chụp    : %s
            Doanh thu    : %,.0f VNĐ
            Trạng thái   : ✅ ĐÃ THANH TOÁN
            ──────────────────────────────

            💰 Số tiền sẽ được chuyển về tài khoản của bạn trong thời gian sớm nhất.

            📌 Lưu ý:
            - Nếu có vấn đề về thanh toán, vui lòng liên hệ hỗ trợ.
            - Kiểm tra lịch sử giao dịch trong hệ thống Fotori.

            Cảm ơn bạn đã đồng hành cùng Fotori!

            Trân trọng,
            Đội ngũ Fotori
            """,
                             photographerName(b),
                             b.getId(),
                             customerName(b),
                             b.getPhotoPackage().getTitle(),
                             b.getStartTime().format(FMT),
                             b.getTotalPrice()
        );
    }
}
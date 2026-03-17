package com.example.fotori.service.impl;

import com.example.fotori.model.Booking;
import com.example.fotori.service.BookingEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.example.fotori.service.EmailService;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingEmailServiceImpl implements BookingEmailService {

    private final EmailService emailService;

    private static final DateTimeFormatter FMT =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // ══════════════════════════════════════════════════════════════════════════
    // Helpers
    // ══════════════════════════════════════════════════════════════════════════

    private void send(String to, String subject, String body) {
        // Chuyển sang dùng EmailService (Brevo API)
        emailService.sendHtmlEmail(to, subject, body.replace("\n", "<br/>"));
    }

    private String customerEmail(Booking b)      { return b.getUser().getEmail(); }
    private String photographerEmail(Booking b)  { return b.getPhotographer().getUser().getEmail(); }
    private String customerName(Booking b)        { return b.getUser().getFullName(); }
    private String photographerName(Booking b)    { return b.getPhotographer().getUser().getFullName(); }

    // ══════════════════════════════════════════════════════════════════════════
    // 1. Booking vừa được tạo
    // ══════════════════════════════════════════════════════════════════════════

    @Override
    public void sendBookingCreatedEmails(Booking booking) {
        send(
            customerEmail(booking),
            "[Fotori] Đặt lịch thành công - Chờ thanh toán",
            buildCustomerBookingCreated(booking)
        );
        send(
            photographerEmail(booking),
            "[Fotori] Bạn có đơn đặt lịch mới!",
            buildPhotographerBookingCreated(booking)
        );
    }

    // ══════════════════════════════════════════════════════════════════════════
    // 2. Thanh toán thành công
    // ══════════════════════════════════════════════════════════════════════════

    @Override
    public void sendPaymentConfirmedEmails(Booking booking) {
        send(
            customerEmail(booking),
            "[Fotori] Xác nhận thanh toán thành công",
            buildCustomerPaymentConfirmed(booking)
        );
        send(
            photographerEmail(booking),
            "[Fotori] Đơn đặt lịch đã được thanh toán - Vui lòng xác nhận",
            buildPhotographerPaymentConfirmed(booking)
        );
    }

    // ══════════════════════════════════════════════════════════════════════════
    // 3. Photographer xác nhận nhận đơn
    // ══════════════════════════════════════════════════════════════════════════

    @Override
    public void sendPhotographerAcceptedEmail(Booking booking) {
        send(
            customerEmail(booking),
            "[Fotori] Nhiếp ảnh gia đã xác nhận lịch chụp của bạn!",
            buildCustomerAccepted(booking)
        );
    }

    // ══════════════════════════════════════════════════════════════════════════
    // 4. Photographer từ chối đơn
    // ══════════════════════════════════════════════════════════════════════════

    @Override
    public void sendPhotographerCancelledEmail(Booking booking) {
        send(
            customerEmail(booking),
            "[Fotori] Thông báo về đơn đặt lịch của bạn",
            buildCustomerCancelled(booking)
        );
    }

    // ══════════════════════════════════════════════════════════════════════════
    // 5. Photographer giao ảnh xong
    // ══════════════════════════════════════════════════════════════════════════

    @Override
    public void sendPhotosDeliveredEmail(Booking booking) {
        send(
            customerEmail(booking),
            "[Fotori] Ảnh của bạn đã sẵn sàng! 🎉",
            buildCustomerPhotosDelivered(booking)
        );
    }

    // ══════════════════════════════════════════════════════════════════════════
    // 6. Customer hủy đơn
    // ══════════════════════════════════════════════════════════════════════════

    @Override
    public void sendCustomerCancelledEmail(Booking booking) {
        send(
            photographerEmail(booking),
            "[Fotori] Khách hàng đã hủy đơn đặt lịch #" + booking.getId(),
            buildPhotographerCustomerCancelled(booking)
        );
        send(
            customerEmail(booking),
            "[Fotori] Xác nhận hủy đơn đặt lịch #" + booking.getId(),
            buildCustomerCancelConfirm(booking)
        );
    }

    // ══════════════════════════════════════════════════════════════════════════
    // 7. Đơn hàng hoàn tất (cả 2 bên DONE)
    // ══════════════════════════════════════════════════════════════════════════

    @Override
    public void sendOrderCompletedEmails(Booking booking) {
        send(
            customerEmail(booking),
            "[Fotori] Đơn hàng #" + booking.getId() + " đã hoàn tất! 🎉",
            buildCustomerOrderCompleted(booking)
        );
        send(
            photographerEmail(booking),
            "[Fotori] Đơn hàng #" + booking.getId() + " đã hoàn tất!",
            buildPhotographerOrderCompleted(booking)
        );
    }

    // ══════════════════════════════════════════════════════════════════════════
    // Email body templates
    // ══════════════════════════════════════════════════════════════════════════

    private String buildCustomerBookingCreated(Booking b) {
        return String.format("""
            Xin chào %s,

            Đặt lịch của bạn đã được tạo thành công trên Fotori!

            ──────────────────────────────
            CHI TIẾT ĐƠN ĐẶT LỊCH
            ──────────────────────────────
            Mã đơn       : #%d
            Nhiếp ảnh gia: %s
            Gói chụp     : %s
            Thời gian    : %s → %s
            Địa điểm     : %s
            Tổng tiền    : %,.0f VNĐ
            Phí dịch vụ  : 40.000 VNĐ
            Thành tiền   : %,.0f VNĐ
            ──────────────────────────────

            ⚠️  Vui lòng hoàn tất thanh toán để xác nhận lịch chụp.
            Đơn sẽ tự động hủy nếu không thanh toán trong 24 giờ.

            Trân trọng,
            Đội ngũ Fotori
            """,
            customerName(b),
            b.getId(),
            photographerName(b),
            b.getPhotoPackage().getTitle(),
            b.getStartTime().format(FMT),
            b.getEndTime().format(FMT),
            b.getLocation() != null ? b.getLocation() : "Chưa xác định",
            b.getTotalPrice(),
            b.getFinalPrice()
        );
    }

    private String buildPhotographerBookingCreated(Booking b) {
        return String.format("""
            Xin chào %s,

            Bạn vừa nhận được một đơn đặt lịch mới trên Fotori!

            ──────────────────────────────
            THÔNG TIN ĐƠN HÀNG
            ──────────────────────────────
            Mã đơn       : #%d
            Khách hàng   : %s
            Gói chụp     : %s
            Thời gian    : %s → %s
            Địa điểm     : %s
            Ghi chú      : %s
            Giá trị đơn  : %,.0f VNĐ
            ──────────────────────────────

            ℹ️  Đơn đang chờ khách hàng thanh toán.
            Bạn sẽ nhận được thông báo khi thanh toán hoàn tất.

            Trân trọng,
            Đội ngũ Fotori
            """,
            photographerName(b),
            b.getId(),
            customerName(b),
            b.getPhotoPackage().getTitle(),
            b.getStartTime().format(FMT),
            b.getEndTime().format(FMT),
            b.getLocation() != null ? b.getLocation() : "Chưa xác định",
            b.getNote() != null ? b.getNote() : "Không có",
            b.getTotalPrice()
        );
    }

    private String buildCustomerPaymentConfirmed(Booking b) {
        return String.format("""
            Xin chào %s,

            Thanh toán của bạn đã được xác nhận thành công! 🎉

            ──────────────────────────────
            XÁC NHẬN THANH TOÁN
            ──────────────────────────────
            Mã đơn       : #%d
            Nhiếp ảnh gia: %s
            Gói chụp     : %s
            Thời gian    : %s → %s
            Địa điểm     : %s
            Số tiền đã TT: %,.0f VNĐ
            Trạng thái   : ✅ ĐÃ THANH TOÁN
            ──────────────────────────────

            ✅ Đơn đang chờ nhiếp ảnh gia xác nhận.
            Chúng tôi sẽ thông báo ngay khi có phản hồi.

            Trân trọng,
            Đội ngũ Fotori
            """,
            customerName(b),
            b.getId(),
            photographerName(b),
            b.getPhotoPackage().getTitle(),
            b.getStartTime().format(FMT),
            b.getEndTime().format(FMT),
            b.getLocation() != null ? b.getLocation() : "Chưa xác định",
            b.getFinalPrice()
        );
    }

    private String buildPhotographerPaymentConfirmed(Booking b) {
        return String.format("""
            Xin chào %s,

            Khách hàng đã thanh toán thành công cho đơn #%d!

            ──────────────────────────────
            THÔNG TIN ĐƠN HÀNG
            ──────────────────────────────
            Khách hàng   : %s
            Gói chụp     : %s
            Thời gian    : %s → %s
            Địa điểm     : %s
            Ghi chú      : %s
            ──────────────────────────────

            ⚡ Vui lòng xác nhận hoặc từ chối đơn trên ứng dụng Fotori.

            Trân trọng,
            Đội ngũ Fotori
            """,
            photographerName(b),
            b.getId(),
            customerName(b),
            b.getPhotoPackage().getTitle(),
            b.getStartTime().format(FMT),
            b.getEndTime().format(FMT),
            b.getLocation() != null ? b.getLocation() : "Chưa xác định",
            b.getNote() != null ? b.getNote() : "Không có"
        );
    }

    private String buildCustomerAccepted(Booking b) {
        return String.format("""
            Xin chào %s,

            Tuyệt vời! Nhiếp ảnh gia đã xác nhận lịch chụp của bạn! 📸

            ──────────────────────────────
            LỊCH CHỤP ĐÃ XÁC NHẬN
            ──────────────────────────────
            Mã đơn       : #%d
            Nhiếp ảnh gia: %s
            Gói chụp     : %s
            Thời gian    : %s → %s
            Địa điểm     : %s
            ──────────────────────────────

            📌 Lưu ý chuẩn bị:
            - Đến đúng giờ tại địa điểm đã chọn
            - Chuẩn bị trang phục phù hợp với concept
            - Liên hệ nhiếp ảnh gia nếu có thay đổi

            Chúc bạn có buổi chụp ảnh tuyệt vời!

            Trân trọng,
            Đội ngũ Fotori
            """,
            customerName(b),
            b.getId(),
            photographerName(b),
            b.getPhotoPackage().getTitle(),
            b.getStartTime().format(FMT),
            b.getEndTime().format(FMT),
            b.getLocation() != null ? b.getLocation() : "Chưa xác định"
        );
    }

    private String buildCustomerCancelled(Booking b) {
        return String.format("""
            Xin chào %s,

            Rất tiếc! Nhiếp ảnh gia không thể nhận đơn #%d của bạn.

            ──────────────────────────────
            THÔNG TIN ĐƠN BỊ HỦY
            ──────────────────────────────
            Mã đơn       : #%d
            Nhiếp ảnh gia: %s
            Gói chụp     : %s
            Thời gian    : %s → %s
            ──────────────────────────────

            💰 Hoàn tiền:
            Số tiền %,.0f VNĐ sẽ được hoàn lại trong 3-5 ngày làm việc.

            🔍 Bạn có thể tìm nhiếp ảnh gia khác phù hợp tại Fotori.

            Trân trọng,
            Đội ngũ Fotori
            """,
            customerName(b),
            b.getId(),
            b.getId(),
            photographerName(b),
            b.getPhotoPackage().getTitle(),
            b.getStartTime().format(FMT),
            b.getEndTime().format(FMT),
            b.getFinalPrice()
        );
    }

    private String buildCustomerPhotosDelivered(Booking b) {
        return String.format("""
            Xin chào %s,

            Ảnh của bạn đã sẵn sàng! 🎉📸

            ──────────────────────────────
            ĐƠN HÀNG HOÀN THÀNH
            ──────────────────────────────
            Mã đơn       : #%d
            Nhiếp ảnh gia: %s
            Gói chụp     : %s
            Ngày chụp    : %s
            Trạng thái   : ✅ HOÀN THÀNH
            ──────────────────────────────

            📥 Vui lòng đăng nhập vào Fotori để xem và tải ảnh về.
            ⭐ Đừng quên đánh giá nhiếp ảnh gia!

            Trân trọng,
            Đội ngũ Fotori
            """,
            customerName(b),
            b.getId(),
            photographerName(b),
            b.getPhotoPackage().getTitle(),
            b.getStartTime().format(FMT)
        );
    }

    private String buildPhotographerCustomerCancelled(Booking b) {
        return String.format("""
            Xin chào %s,

            Khách hàng đã hủy đơn đặt lịch #%d.

            ──────────────────────────────
            THÔNG TIN ĐƠN BỊ HỦY
            ──────────────────────────────
            Khách hàng   : %s
            Gói chụp     : %s
            Thời gian    : %s → %s
            Địa điểm     : %s
            ──────────────────────────────

            Lịch của bạn đã được giải phóng cho thời gian này.

            Trân trọng,
            Đội ngũ Fotori
            """,
            photographerName(b),
            b.getId(),
            customerName(b),
            b.getPhotoPackage().getTitle(),
            b.getStartTime().format(FMT),
            b.getEndTime().format(FMT),
            b.getLocation() != null ? b.getLocation() : "Chưa xác định"
        );
    }

    private String buildCustomerCancelConfirm(Booking b) {
        return String.format("""
            Xin chào %s,

            Đơn đặt lịch #%d của bạn đã được hủy thành công.

            ──────────────────────────────
            THÔNG TIN ĐƠN ĐÃ HỦY
            ──────────────────────────────
            Nhiếp ảnh gia: %s
            Gói chụp     : %s
            Thời gian    : %s → %s
            ──────────────────────────────

            💰 Hoàn tiền:
            Số tiền %,.0f VNĐ sẽ được hoàn lại trong 3-5 ngày làm việc.

            Trân trọng,
            Đội ngũ Fotori
            """,
            customerName(b),
            b.getId(),
            photographerName(b),
            b.getPhotoPackage().getTitle(),
            b.getStartTime().format(FMT),
            b.getEndTime().format(FMT),
            b.getFinalPrice()
        );
    }

    private String buildCustomerOrderCompleted(Booking b) {
        return String.format("""
            Xin chào %s,

            Đơn hàng của bạn đã hoàn tất! 🎉

            ──────────────────────────────
            ĐƠN HÀNG HOÀN TẤT
            ──────────────────────────────
            Mã đơn       : #%d
            Nhiếp ảnh gia: %s
            Gói chụp     : %s
            Ngày chụp    : %s
            Tổng tiền    : %,.0f VNĐ
            Trạng thái   : ✅ HOÀN TẤT
            ──────────────────────────────

            ⭐ Hãy đánh giá nhiếp ảnh gia để giúp cộng đồng Fotori!
            🔗 Đăng nhập vào Fotori để để lại nhận xét.

            Cảm ơn bạn đã sử dụng dịch vụ Fotori!

            Trân trọng,
            Đội ngũ Fotori
            """,
            customerName(b),
            b.getId(),
            photographerName(b),
            b.getPhotoPackage().getTitle(),
            b.getStartTime().format(FMT),
            b.getFinalPrice()
        );
    }

    private String buildPhotographerOrderCompleted(Booking b) {
        return String.format("""
            Xin chào %s,

            Đơn hàng #%d đã hoàn tất thành công!

            ──────────────────────────────
            TỔNG KẾT ĐƠN HÀNG
            ──────────────────────────────
            Khách hàng   : %s
            Gói chụp     : %s
            Ngày chụp    : %s
            Doanh thu    : %,.0f VNĐ
            Trạng thái   : ✅ HOÀN TẤT
            ──────────────────────────────

            💰 Doanh thu sẽ được chuyển vào tài khoản trong 1-3 ngày làm việc.

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
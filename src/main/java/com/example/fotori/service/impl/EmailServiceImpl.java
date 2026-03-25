package com.example.fotori.service.impl;

import com.example.fotori.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${MAIL_USERNAME:fotori.official@gmail.com}")
    private String fromEmail;

    @Value("${app.frontend-url:https://fotori.vercel.app}")
    private String frontendUrl;

    @Value("${app.backend-url:https://fotori-production-87a3.up.railway.app/api}")
    private String backendUrl;

    @Override
    @Async
    public void sendVerificationEmail(String email, String token) {
        log.info("[START] sendVerificationEmail to: {}", email);
        String verifyUrl = backendUrl + "/auth/verify-email?token=" + token;
        
        String content = "<h3>Xác thực tài khoản Fotori</h3>" +
                "<p>Chào mừng bạn đến với Fotori! Vui lòng nhấn vào link bên dưới để xác thực tài khoản của bạn:</p>" +
                "<a href='" + verifyUrl + "'>" + verifyUrl + "</a>" +
                "<p>Link có hiệu lực trong 24 giờ.</p>";
        sendHtmlEmail(email, "Xác thực email của bạn - Fotori", content);
    }

    @Override
    @Async
    public void sendResetPasswordEmail(String email, String token) {
        log.info("[START] sendResetPasswordEmail to: {}", email);
        String resetUrl = frontendUrl + "/reset-password?token=" + token;
        
        String content = "<h3>Yêu cầu đặt lại mật khẩu</h3>" +
                "<p>Bạn nhận được email này vì chúng tôi đã nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn.</p>" +
                "<p>Nhấn vào nút bên dưới để thực hiện thay đổi:</p>" +
                "<a href='" + resetUrl + "' style='display:inline-block;padding:10px 20px;background-color:#628EBC;color:white;text-decoration:none;border-radius:5px;'>Đặt lại mật khẩu</a>" +
                "<p>Nếu bạn không yêu cầu, vui lòng bỏ qua email này.</p>";
        sendHtmlEmail(email, "Đặt lại mật khẩu - Fotori", content);
    }

    @Override
    @Async
    public void sendAdminNewPhotographerNotification(String photographerName, String photographerEmail) {
        log.info("[START] sendAdminNewPhotographerNotification for: {}", photographerEmail);
        String content = String.format(
            "<h3>Thông báo: Có thợ ảnh mới đăng ký</h3>" +
            "<p><b>Họ tên:</b> %s</p>" +
            "<p><b>Email:</b> %s</p>" +
            "<p>Vui lòng đăng nhập vào trang quản trị để xem xét và duyệt hồ sơ.</p>" +
            "<br/><a href='%s/admin-login' style='display:inline-block;padding:10px 20px;background-color:#628EBC;color:white;text-decoration:none;border-radius:5px;'>Đi tới Dashboard</a>",
            photographerName, photographerEmail, frontendUrl
        );
        sendHtmlEmail(fromEmail, "Thông báo: Có thợ ảnh mới đăng ký - Fotori", content);
    }

    @Override
    @Async
    public void sendPhotographerApprovalNotification(String toEmail, String photographerName) {
        log.info("[START] sendPhotographerApprovalNotification to: {}", toEmail);
        String content = String.format(
            "<h3>Chúc mừng %s! 🎉</h3>" +
            "<p>Hồ sơ Photographer của bạn đã được đội ngũ Fotori phê duyệt.</p>" +
            "<p>Giờ đây bạn đã có thể đăng tải portfolio, cài đặt các gói chụp và bắt đầu nhận booking từ khách hàng.</p>" +
            "<br/><a href='%s/login' style='display:inline-block;padding:10px 20px;background-color:#628EBC;color:white;text-decoration:none;border-radius:5px;'>Đăng nhập ngay</a>",
            photographerName, frontendUrl
        );
        sendHtmlEmail(toEmail, "Chúc mừng! Tài khoản Photographer của bạn đã được duyệt - Fotori", content);
    }

    @Override
    @Async
    public void sendPhotographerPendingNotification(String toEmail, String photographerName) {
        log.info("[START] sendPhotographerPendingNotification to: {}", toEmail);
        String content = String.format(
            "<h3>Xin chào %s!</h3>" +
            "<p>Email của bạn đã được xác thực thành công.</p>" +
            "<p>Hiện tại tài khoản Photographer của bạn đang được đội ngũ quản trị Fotori xem xét và phê duyệt. " +
            "Quá trình này thường mất từ 1 đến 2 ngày làm việc.</p>" +
            "<p>Chúng tôi sẽ gửi một email thông báo ngay khi tài khoản của bạn được duyệt.</p>" +
            "<p>Cảm ơn sự kiên nhẫn của bạn!</p>" +
            "<p>Trân trọng,<br/>Đội ngũ Fotori</p>",
            photographerName
        );
        sendHtmlEmail(toEmail, "Tài khoản của bạn đang chờ phê duyệt - Fotori", content);
    }

    @Override
    @Async
    public void sendHtmlEmail(String toEmail, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail, "Fotori");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText("<html><body>" + htmlContent + "</body></html>", true);
            
            mailSender.send(message);
            log.info("[SUCCESS] Email sent successfully to {}", toEmail);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("[ERROR] Failed to send email to {}: {}", toEmail, e.getMessage(), e);
        }
    }
}


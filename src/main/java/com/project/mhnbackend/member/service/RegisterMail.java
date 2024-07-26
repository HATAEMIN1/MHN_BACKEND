package com.project.mhnbackend.member.service;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class RegisterMail implements MailServiceInter {

    @Autowired
    private JavaMailSender emailSender;

    @Value("${mail.name}")
    private String mailUsername;

    private String ePw;

    // 랜덤 인증 코드 생성
    @Override
    public String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤, rnd 값에 따라서 아래 switch 문이 실행됨

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    break; // a~z (ex. 1+97=98 => (char)98 = 'b')
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    break; // A~Z
                case 2:
                    key.append((rnd.nextInt(10)));
                    break; // 0~9
            }
        }

        return key.toString();
    }

    // 이메일 내용 작성
    @Override
    public SimpleMailMessage createMessage(String to) throws UnsupportedEncodingException {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject("멍핸냥 MHN 회원가입 이메일 인증");

        String msgg = "";
        msgg += "안녕하세요\n";
        msgg += "애완동물 커뮤니티 어플 멍핸냥 입니다\n";
        msgg += "\n";
        msgg += "아래 코드를 회원가입 창으로 돌아가 입력해주세요\n";
        msgg += "\n";
        msgg += "항상 당신의 애완동물과의 행복을 바랍니다\n";
        msgg += "\n";
        msgg += "회원가입 인증 코드입니다.\n";
        msgg += "CODE : " + ePw + "\n";

        message.setText(msgg);
        message.setFrom(mailUsername);  // 발신자 이메일 주소를 설정 파일에서 불러온 값으로 설정

        return message;
    }

    // 메일 발송
    @Override
    public String sendSimpleMessage(String to) throws Exception {
        ePw = createKey(); // 랜덤 인증번호 생성
        SimpleMailMessage message = createMessage(to); // 메일 발송
        try {
            emailSender.send(message);
        } catch (MailException es) {
            es.printStackTrace();
            throw new IllegalArgumentException();
        }

        return ePw; // 메일로 보냈던 인증 코드를 서버로 반환
    }
}

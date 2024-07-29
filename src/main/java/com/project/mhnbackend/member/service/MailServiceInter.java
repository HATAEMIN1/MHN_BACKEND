package com.project.mhnbackend.member.service;

import java.io.UnsupportedEncodingException;
import org.springframework.mail.SimpleMailMessage;

public interface MailServiceInter {
    SimpleMailMessage createMessage(String to) throws UnsupportedEncodingException;
    String createKey();
    String sendSimpleMessage(String to) throws Exception;
}
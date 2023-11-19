package kz.almaty.moneytransferservice.service;

import kz.almaty.moneytransferservice.dto.LoginDto;
import kz.almaty.moneytransferservice.dto.RegisterDto;

public interface AuthService {

    String login(LoginDto loginDto);
    String register(RegisterDto registerDto);
}
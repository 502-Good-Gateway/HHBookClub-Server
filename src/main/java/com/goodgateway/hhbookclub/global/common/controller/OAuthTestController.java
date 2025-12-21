package com.goodgateway.hhbookclub.global.common.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 테스트용 OAuth 콜백 및 메인 페이지 컨트롤러
 * 프론트엔드 구현 전 OAuth 로그인 테스트용
 */
@RestController
public class OAuthTestController {

    @GetMapping(value = "/oauth/callback", produces = MediaType.TEXT_HTML_VALUE)
    public String oauthCallback(
            @RequestParam(required = false) String accessToken,
            @RequestParam(required = false) String refreshToken) {

        return """
                <!DOCTYPE html>
                <html lang="ko">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>HHBookClub - 로그인 성공</title>
                    <style>
                        * {
                            margin: 0;
                            padding: 0;
                            box-sizing: border-box;
                        }
                        body {
                            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
                            background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%);
                            min-height: 100vh;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            padding: 20px;
                        }
                        .container {
                            background: rgba(255, 255, 255, 0.95);
                            border-radius: 20px;
                            padding: 40px;
                            max-width: 600px;
                            width: 100%%;
                            box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
                        }
                        .header {
                            text-align: center;
                            margin-bottom: 30px;
                        }
                        .success-icon {
                            width: 80px;
                            height: 80px;
                            background: linear-gradient(135deg, #22c55e 0%%, #16a34a 100%%);
                            border-radius: 50%%;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            margin: 0 auto 20px;
                        }
                        .success-icon::after {
                            content: "✓";
                            color: white;
                            font-size: 40px;
                            font-weight: bold;
                        }
                        h1 {
                            color: #1f2937;
                            font-size: 28px;
                            margin-bottom: 10px;
                        }
                        .subtitle {
                            color: #6b7280;
                            font-size: 16px;
                        }
                        .token-section {
                            margin-top: 30px;
                        }
                        .token-label {
                            font-weight: 600;
                            color: #374151;
                            margin-bottom: 8px;
                            display: block;
                        }
                        .token-box {
                            background: #f3f4f6;
                            border: 1px solid #e5e7eb;
                            border-radius: 12px;
                            padding: 16px;
                            word-break: break-all;
                            font-family: 'Monaco', 'Menlo', monospace;
                            font-size: 12px;
                            color: #1f2937;
                            margin-bottom: 20px;
                            position: relative;
                        }
                        .copy-btn {
                            position: absolute;
                            top: 10px;
                            right: 10px;
                            background: #667eea;
                            color: white;
                            border: none;
                            padding: 6px 12px;
                            border-radius: 6px;
                            cursor: pointer;
                            font-size: 12px;
                            transition: background 0.2s;
                        }
                        .copy-btn:hover {
                            background: #5a67d8;
                        }
                        .main-btn {
                            display: block;
                            width: 100%%;
                            background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%);
                            color: white;
                            text-align: center;
                            padding: 16px;
                            border-radius: 12px;
                            text-decoration: none;
                            font-weight: 600;
                            font-size: 16px;
                            margin-top: 20px;
                            transition: transform 0.2s, box-shadow 0.2s;
                        }
                        .main-btn:hover {
                            transform: translateY(-2px);
                            box-shadow: 0 10px 20px rgba(102, 126, 234, 0.3);
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <div class="success-icon"></div>
                            <h1>🎉 로그인 성공!</h1>
                            <p class="subtitle">HHBookClub에 오신 것을 환영합니다</p>
                        </div>

                        <div class="token-section">
                            <label class="token-label">📌 Access Token</label>
                            <div class="token-box" id="accessToken">
                                %s
                                <button class="copy-btn" onclick="copyToken('accessToken')">복사</button>
                            </div>

                            <label class="token-label">🔄 Refresh Token</label>
                            <div class="token-box" id="refreshToken">
                                %s
                                <button class="copy-btn" onclick="copyToken('refreshToken')">복사</button>
                            </div>
                        </div>

                        <a href="/main" class="main-btn">메인 페이지로 이동 →</a>
                    </div>

                    <script>
                        function copyToken(elementId) {
                            const tokenBox = document.getElementById(elementId);
                            const text = tokenBox.childNodes[0].textContent.trim();
                            navigator.clipboard.writeText(text).then(() => {
                                const btn = tokenBox.querySelector('.copy-btn');
                                const originalText = btn.textContent;
                                btn.textContent = '복사됨!';
                                setTimeout(() => btn.textContent = originalText, 1500);
                            });
                        }
                    </script>
                </body>
                </html>
                """.formatted(
                accessToken != null ? accessToken : "토큰이 없습니다",
                refreshToken != null ? refreshToken : "토큰이 없습니다");
    }

    @GetMapping(value = "/main", produces = MediaType.TEXT_HTML_VALUE)
    public String mainPage() {
        return """
                <!DOCTYPE html>
                <html lang="ko">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>HHBookClub - 메인</title>
                    <style>
                        * {
                            margin: 0;
                            padding: 0;
                            box-sizing: border-box;
                        }
                        body {
                            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
                            background: linear-gradient(135deg, #1e3a5f 0%, #0f172a 100%);
                            min-height: 100vh;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                        }
                        .container {
                            text-align: center;
                            color: white;
                        }
                        h1 {
                            font-size: 48px;
                            margin-bottom: 20px;
                            background: linear-gradient(135deg, #60a5fa 0%, #a78bfa 100%);
                            -webkit-background-clip: text;
                            -webkit-text-fill-color: transparent;
                            background-clip: text;
                        }
                        p {
                            font-size: 20px;
                            color: #9ca3af;
                            margin-bottom: 40px;
                        }
                        .login-btn {
                            display: inline-block;
                            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                            color: white;
                            padding: 16px 40px;
                            border-radius: 12px;
                            text-decoration: none;
                            font-weight: 600;
                            font-size: 18px;
                            transition: transform 0.2s, box-shadow 0.2s;
                        }
                        .login-btn:hover {
                            transform: translateY(-3px);
                            box-shadow: 0 15px 30px rgba(102, 126, 234, 0.4);
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h1>📚 HHBookClub</h1>
                        <p>독서를 사랑하는 사람들의 커뮤니티</p>
                        <a href="/oauth2/authorization/google" class="login-btn">
                            🔐 Google로 로그인
                        </a>
                    </div>
                </body>
                </html>
                """;
    }
}

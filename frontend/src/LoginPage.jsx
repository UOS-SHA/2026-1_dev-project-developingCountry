
import { useState } from "react";
import "../styles/Login.css";

export default function LoginPage({ onLogin }) {

  const [username, setUsername] = useState("");  
  const [password, setPassword] = useState("");  
  const [error,    setError]    = useState("");  
  const [loading,  setLoading]  = useState(false); 


  const handleSubmit = async (e) => {
    e.preventDefault(); 
    setError("");       

   
    if (!username.trim() || !password.trim()) {
      setError("아이디와 비밀번호를 모두 입력해주세요.");
      return;
    }

    setLoading(true);

    try {
    
      const res = await fetch("/api/login", {
        method:  "POST",
        headers: { "Content-Type": "application/json" },
        body:    JSON.stringify({ username, password }),
      });

      const data = await res.json();

      if (!res.ok) {
    
        setError(data.message || "로그인에 실패했습니다.");
        return;
      }

   
      onLogin(data.user, data.token);

    } catch (err) {
     
      setError("서버와 연결할 수 없습니다. 잠시 후 다시 시도해주세요.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-page">
      <div className="login-card">

        {/* 헤더 */}
        <div className="login-card__header">
          <h1 className="login-card__title"> 로그인</h1>
          <p className="login-card__subtitle">계정 정보를 입력해주세요</p>
        </div>

        {/* 에러 메시지 */}
        {error && (
          <div className="login-card__error" role="alert">
            ⚠️ {error}
          </div>
        )}

        {/* 로그인 폼 */}
        <form className="login-form" onSubmit={handleSubmit}>

          {/* 아이디 */}
          <div className="login-form__group">
            <label className="login-form__label" htmlFor="username">
              아이디
            </label>
            <input
              id="username"
              type="text"
              className="login-form__input"
              placeholder="아이디를 입력하세요"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              autoComplete="username"
              disabled={loading}
            />
          </div>

          {/* 비밀번호 */}
          <div className="login-form__group">
            <label className="login-form__label" htmlFor="password">
              비밀번호
            </label>
            <input
              id="password"
              type="password"
              className="login-form__input"
              placeholder="비밀번호를 입력하세요"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              autoComplete="current-password"
              disabled={loading}
            />
          </div>

          {/* 로그인 버튼 */}
          <button
            type="submit"
            className={`login-form__btn ${loading ? "login-form__btn--loading" : ""}`}
            disabled={loading}
          >
            {loading ? "로그인 중..." : "로그인"}
          </button>
        </form>

        {/* 테스트 계정 안내 */}
        <div className="login-card__hint">
          <p>테스트 계정</p>
          <code>admin / 1234</code>
          <code>user1 / abcd</code>
        </div>

      </div>
    </div>
  );
}

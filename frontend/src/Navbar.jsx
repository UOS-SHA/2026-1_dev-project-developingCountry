
import "./Navbar.css";

export default function Navbar({ user, onLogout }) {
  return (
    <nav className="navbar">
      <div className="navbar__brand"> Login App</div>

      <div className="navbar__right">
        {user ? (
       
          <>
            <span className="navbar__username">
              👤 {user.name}님 환영합니다!
            </span>
            <button
              className="navbar__btn navbar__btn--logout"
              onClick={onLogout}
            >
              로그아웃
            </button>
          </>
        ) : (
    
          <span className="navbar__guest">로그인이 필요합니다</span>
        )}
      </div>
    </nav>
  );
}

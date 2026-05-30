
import "../styles/Home.css";

export default function HomePage({ user }) {

  const getGreeting = () => {
    const hour = new Date().getHours();
    if (hour < 12) return "좋은 아침이에요";
    if (hour < 18) return "좋은 오후예요";
    return "좋은 저녁이에요";
  };

  return (
    <div className="home-page">

      {/* 웰컴 카드 */}
      <div className="home-card">
        <div className="home-card__icon">🎉</div>
        <h1 className="home-card__title">
          {getGreeting()}, <span className="home-card__name">{user.name}</span>님!
        </h1>
        <p className="home-card__subtitle">로그인에 성공했습니다.</p>

        {/* 유저 정보 테이블 */}
        <div className="home-info">
          <h2 className="home-info__title">내 계정 정보</h2>
          <table className="home-info__table">
            <tbody>
              <tr>
                <th>이름</th>
                <td>{user.name}</td>
              </tr>
              <tr>
                <th>아이디</th>
                <td>{user.username}</td>
              </tr>
              <tr>
                <th>유저 ID</th>
                <td>#{user.id}</td>
              </tr>
              <tr>
                <th>상태</th>
                <td>
                  <span className="home-info__badge"> 로그인 중</span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        {/* 안내 메시지 */}
        <p className="home-card__note">
          우측 상단의 <strong>로그아웃</strong> 버튼을 눌러 로그아웃할 수 있습니다.
        </p>
      </div>

    </div>
  );
}

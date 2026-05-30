const express = require("express");
const cors = require("cors");
const jwt = require("jsonwebtoken");

const app = express();
const PORT = 4000;


const JWT_SECRET = "my_secret_key_change_this_in_production";


app.use(express.json());
app.use(cors({
    origin: "http://localhost:5173",
    credentials: true,
}));


const USERS = [
    { id: 1, username: "admin", password: "1234", name: "관리자" },
    { id: 2, username: "user1", password: "abcd", name: "일반유저" },
];

function verifyToken(req, res, next) {

    const authHeader = req.headers["authorization"];
    const token = authHeader && authHeader.split(" ")[1]; // "Bearer xxx" → "xxx"

    if (!token) {
        return res.status(401).json({ message: "토큰이 없습니다. 로그인이 필요합니다." });
    }


    jwt.verify(token, JWT_SECRET, (err, decoded) => {
        if (err) {
            return res.status(403).json({ message: "토큰이 만료되었거나 유효하지 않습니다." });
        }
        req.user = decoded;
        next();
    });
}

app.post("/api/login", (req, res) => {
    const { username, password } = req.body;


    if (!username || !password) {
        return res.status(400).json({ message: "아이디와 비밀번호를 입력해주세요." });
    }


    const user = USERS.find(
        (u) => u.username === username && u.password === password
    );

    if (!user) {
        return res.status(401).json({ message: "아이디 또는 비밀번호가 틀렸습니다." });
    }


    const token = jwt.sign(
        { id: user.id, username: user.username, name: user.name },
        JWT_SECRET,
        { expiresIn: "1h" }
    );

    console.log(`로그인 성공: ${user.username}`);

    return res.status(200).json({
        message: "로그인 성공!",
        token,
        user: {
            id: user.id,
            username: user.username,
            name: user.name,
        },
    });
});


app.post("/api/logout", verifyToken, (req, res) => {
    console.log(`👋 로그아웃: ${req.user.username}`);
    return res.status(200).json({ message: "로그아웃 되었습니다." });
});

app.get("/api/me", verifyToken, (req, res) => {
    return res.status(200).json({
        message: "유저 정보 조회 성공",
        user: req.user,
    });
});

app.listen(PORT, () => {
    console.log(`서버 실행 중: http://localhost:${PORT}`);
    console.log("테스트 계정: admin/1234, user1/abcd");
});

<server.js></server.js>
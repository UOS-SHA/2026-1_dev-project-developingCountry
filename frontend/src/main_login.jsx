
import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter } from "react-router-dom";
import App from "./App.jsx";

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    {/* BrowserRouter: URL 경로 기반 라우팅 활성화 */}
    <BrowserRouter>
      <App />
    </BrowserRouter>
  </React.StrictMode>
);

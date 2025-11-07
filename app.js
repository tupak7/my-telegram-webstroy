import React, { useEffect, useState } from "react";
import WebApp from "@twa-dev/sdk";

export default function App() {
  const [token, setToken] = useState("");
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [activeSection, setActiveSection] = useState("menu");

  // Пример данных пользователя (в реальном боте эти данные берутся с сервера по токену)
  const user = {
    username: "CryptoUser",
    balance: 125.75,
    depositHistory: ["+50 USDT", "+75.75 USDT"],
    withdrawHistory: ["-20 USDT", "-30 USDT"],
  };

  useEffect(() => {
    WebApp.ready();
  }, []);

  const handleLogin = () => {
    if (token.trim() === "") {
      alert("8595197693:AAFrVbRPpas6kA5p7dTQHAgn6gay-1qK7SM");
      return;
    }
    // В реальном проекте: отправляем токен на сервер и получаем данные
    setIsLoggedIn(true);
  };

  const renderLogin = () => (
    <div className="p-4 text-center">
      <h2>🔑 Вход в систему</h2>
      <p>8595197693:AAFrVbRPpas6kA5p7dTQHAgn6gay-1qK7SM</p>
      <input
        type="text"
        placeholder="8595197693:AAFrVbRPpas6kA5p7dTQHAgn6gay-1qK7SM"
        value={token}
        onChange={(e) => setToken(e.target.value)}
        className="input"
      />
      <button className="btn mt-4" onClick={handleLogin}>
        Войти
      </button>
    </div>
  );

  const renderMenu = () => (
    <div className="flex flex-col items-center gap-3 p-4">
      <button className="btn" onClick={() => setActiveSection("profile")}>
        👤 Профиль
      </button>
      <button className="btn" onClick={() => setActiveSection("deposit")}>
        💰 Пополнить
      </button>
      <button className="btn" onClick={() => setActiveSection("withdraw")}>
        💸 Вывести
      </button>
      <button className="btn" onClick={() => setActiveSection("depositHistory")}>
        📈 История пополнений
      </button>
      <button className="btn" onClick={() => setActiveSection("withdrawHistory")}>
        📉 История выводов
      </button>
    </div>
  );

  const renderProfile = () => (
    <div className="p-4 text-center">
      <h2>👤 Профиль</h2>
      <p>Ник: <b>{user.username}</b></p>
      <p>Баланс: <b>{user.balance} USDT</b></p>
      <p>Токен: <code>{token}</code></p>
      <button className="btn mt-4" onClick={() => setActiveSection("menu")}>⬅️ Назад</button>
    </div>
  );

  const renderDeposit = () => (
    <div className="p-4 text-center">
      <h2>💰 Пополнить</h2>
      <p>Выбери криптовалюту для пополнения:</p>
      <div className="flex flex-col gap-2 mt-3">
        <button className="btn">USDT (TRC20)</button>
        <button className="btn">BTC</button>
        <button className="btn">ETH</button>
      </div>
      <button className="btn mt-4" onClick={() => setActiveSection("menu")}>⬅️ Назад</button>
    </div>
  );

  const renderWithdraw = () => (
    <div className="p-4 text-center">
      <h2>💸 Вывести</h2>
      <p>Выбери криптовалюту для вывода:</p>
      <div className="flex flex-col gap-2 mt-3">
        <button className="btn">USDT (TRC20)</button>
        <button className="btn">BTC</button>
        <button className="btn">ETH</button>
      </div>
      <button className="btn mt-4" onClick={() => setActiveSection("menu")}>⬅️ Назад</button>
    </div>
  );

  const renderHistory = (list, title) => (
    <div className="p-4 text-center">
      <h2>{title}</h2>
      {list.length === 0 ? (
        <p>Нет записей</p>
      ) : (
        <ul className="mt-2">
          {list.map((item, i) => (
            <li key={i}>{item}</li>
          ))}
        </ul>
      )}
      <button className="btn mt-4" onClick={() => setActiveSection("menu")}>⬅️ Назад</button>
    </div>
  );
return (
    <div className="min-h-screen bg-gray-900 text-white flex flex-col items-center justify-center">
      {!isLoggedIn ? (
        renderLogin()
      ) : (
        <>
          {activeSection === "menu" && renderMenu()}
          {activeSection === "profile" && renderProfile()}
          {activeSection === "deposit" && renderDeposit()}
          {activeSection === "withdraw" && renderWithdraw()}
          {activeSection === "depositHistory" && renderHistory(user.depositHistory, "📈 История пополнений")}
          {activeSection === "withdrawHistory" && renderHistory(user.withdrawHistory, "📉 История выводов")}
        </>
      )}
    </div>
  );
}

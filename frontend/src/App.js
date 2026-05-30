import { useState } from 'react';
import './App.css';
import { BarChart, Bar, Cell, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';

// 1. 💡 구라 데이터를 캔들차트용(4가지 가격)으로 전격 업그레이드!
// 배열 안의 데이터가 [시가, 저가, 고가, 종가] 순서대로 들어가 있는 형태입니다.
// 구라 데이터 모음 (일주일치 캔들차트용 버전)
const STOCK_DATA = {
  samsung: [
    { date: '05-18', open: 71000, low: 70500, high: 73000, close: 72000 }, // 상승 (빨강)
    { date: '05-19', open: 72200, low: 71000, high: 72500, close: 71500 }, // 하락 (파랑)
    { date: '05-20', open: 71200, low: 70800, high: 73500, close: 73000 }, // 상승
    { date: '05-21', open: 73200, low: 72500, high: 74500, close: 74200 }, // 상승
    { date: '05-22', open: 74200, low: 73000, high: 75000, close: 73500 }, // 하락
    { date: '05-25', open: 73500, low: 73200, high: 76000, close: 75800 }, // 폭등
    { date: '05-26', open: 75800, low: 74000, high: 76200, close: 74500 }, // 폭락
  ],
  apple: [
    { date: '05-18', open: 241000, low: 238000, high: 243000, close: 240000 }, // 하락 (파랑)
    { date: '05-19', open: 239500, low: 239000, high: 245000, close: 242000 }, // 상승 (빨강)
    { date: '05-20', open: 243000, low: 237500, high: 244000, close: 239000 }, // 하락
    { date: '05-21', open: 238500, low: 235000, high: 241000, close: 236000 }, // 하락
    { date: '05-22', open: 236500, low: 236000, high: 246000, close: 245000 }, // 폭등
    { date: '05-25', open: 245000, low: 243000, high: 250000, close: 249000 }, // 상승
    { date: '05-26', open: 249000, low: 246500, high: 253000, close: 252000 }, // 상승
  ],
  tesla: [
    { date: '05-18', open: 540000, low: 535000, high: 560000, close: 550000 }, // 상승 (빨강)
    { date: '05-19', open: 555000, low: 550000, high: 660000, close: 652000 }, // 폭등 (장대양봉)
    { date: '05-20', open: 655000, low: 620000, high: 660000, close: 629000 }, // 폭락 (장대음봉)
    { date: '05-21', open: 630000, low: 615000, high: 635000, close: 618000 }, // 하락
    { date: '05-22', open: 618000, low: 605000, high: 625000, close: 610000 }, // 하락
    { date: '05-25', open: 612000, low: 610000, high: 640000, close: 638000 }, // 상승
    { date: '05-26', open: 638000, low: 635000, high: 650000, close: 645000 }, // 상승
  ]
};

function StockNav(props){
  return(
    <nav className="stock-nav">
      <button onClick={()=>props.onChangeStock('samsung')}>삼성전자</button>
      <button onClick={()=>props.onChangeStock('apple')}>애플</button>
      <button onClick={()=>props.onChangeStock('tesla')}>테슬라</button>
    </nav>
  )
}

// 2. 💡 진짜 캔들을 그리는 컴포넌트
function StockChart(props){
  return(
    <div className='chart-box'>
      <h3>{props.stockCode.toUpperCase()} (봉차트)</h3>
      <div style={{width:'100%', height:350}}>
        <ResponsiveContainer width="100%" height="100%">
          {/* BarChart를 기반으로 캔들을 구현합니다 */}
          <BarChart data={props.chartData} margin={{ top: 20, right: 30, left: 20, bottom: 5 }}>
            <CartesianGrid strokeDasharray="3 3"/>
            <XAxis dataKey="date"/>
            {/* 데이터 중 최소값과 최대값에 맞춰 Y축 범위를 자동으로 조절합니다 */}
            <YAxis domain={['dataMin - 5000', 'dataMax + 5000']}/>
            <Tooltip 
              // 마우스를 올렸을 때 4가지 가격을 다 보여주도록 툴팁을 커스텀 빌드합니다
              formatter={(value, name, props) => {
                const { open, high, low, close } = props.payload;
                return [
                  `시가: ${open}원 | 고가: ${high}원 | 저가: ${low}원 | 종가: ${close}원`
                ];
              }}
            />
            
            {/* 💡 핵심: 시가와 종가의 차이를 막대기로 그리고, 주가가 올랐는지(양봉) 내렸는지(음봉) 색칠합니다 */}
            <Bar dataKey={(item) => [item.open, item.close]}>
              {
                props.chartData.map((entry, index) => {
                  // 종가(close)가 시가(open)보다 크면 주가가 오른 것 -> 한국 국룰 '빨간색'
                  // 주가가 떨어졌으면 -> 한국 국룰 '파란색'
                  const isUp = entry.close >= entry.open;
                  return (
                    <Cell 
                      key={`cell-${index}`} 
                      fill={isUp ? '#ff4d4f' : '#1890ff'} 
                    />
                  );
                })
              }
            </Bar>
          </BarChart>
        </ResponsiveContainer>
      </div>
    </div>
  )
}

function App(){
  const [currentStock, setCurrentStock] = useState('samsung');
  const currentChartData = STOCK_DATA[currentStock];

  return(
    <div className="dashboard-container">
      <h1 style={{marginTop: 0, marginBottom:'10px'}}>모의 투자 플랫폼</h1>
      <p style={{color: '#6c757d', marginBottom:'30px'}}>버튼을 눌러 캔들차트 그래프를 확인하세요.</p>

      <StockNav currentStock={currentStock} onChangeStock={(_code)=>{
        setCurrentStock(_code);
      }}></StockNav>
      
      <StockChart stockCode={currentStock} chartData={currentChartData}></StockChart>
    </div>
  );
}

export default App;
// 透過 $(id) 來替代 document.getElementById(id)
const $ = (id) => document.getElementById(id);

const renderShipTable = (ships) => {
	
	const rows = ships.map(ship => `<tr>
										<td>${ship.name}</td>
										<td>${ship.type}</td>
										<td>${ship.length}</td>
										<td>${ship.width}</td>
									</tr>`).join('');
	  
	const table = `<table border=1>
					<thead>
						<tr>
							<th>名稱</th><th>種類</th><th>長度</th><th>寬度</th>
						</tr>
					</thead>
					<tbody>
						${rows}
					</tbody>
				</table>`;
				
	$('result').innerHTML = table;
};

// 待 DOM 加載完成之後再執行
document.addEventListener("DOMContentLoaded", async() => {
	
	// 監聽 xxxbutton 是否有被點擊
	$('todayBtn').addEventListener("click", async(event) => {
		console.log('todayBtn 被按下');
		const response = await fetch('http://localhost:8081/data/today');
		const {state, message, data} = await response.json();
		console.log(state, message, data);
		$('result').innerHTML = data;
	});
	
	$('lottoBtn').addEventListener("click", async(event) => {
		console.log('lottoBtn 被按下');
		const response = await fetch('http://localhost:8081/data/lotto');
		const {state, message, data} = await response.json();
		console.log(state, message, data);
		$('result').innerHTML = data;
	});
	
	$('shipBtn').addEventListener("click", async(event) => {
		console.log('shipBtn 被按下');
		const response = await fetch('http://localhost:8081/data/ship');
		const {state, message, data} = await response.json();
		console.log(state, message, data);
		$('result').innerHTML = `名稱: ${data.name}<br>
								 種類: ${data.type}<br>
								 長度: ${data.length}m<br>
								 寬度: ${data.width}m`;
	});
	
	$('shipByIdBtn').addEventListener("click", async(event) => {
		console.log('shipByIdBtn 被按下');
		const id = window.prompt('請輸入 id');
		const response = await fetch(`http://localhost:8081/data/ship/${id}`);
		const {state, message, data} = await response.json();
		console.log(state, message, data);
		if(state) {
			$('result').innerHTML = `名稱: ${data.name}<br>
									 種類: ${data.type}<br>
									 長度: ${data.length}m<br>
									 寬度: ${data.width}m`;
		} else {
			$('result').innerHTML = message;
		}				 
	});
	
	$('shipsBtn').addEventListener("click", async(event) => {
		console.log('shipsBtn 被按下');
		// 利用 HTML Table 將資料顯示出來
		//+---------+----------+--------+-------+
		//|  Name   |   Type   | Length | Width |
		//+---------+----------+--------+-------+
		//|         |          |        |       |
		//+---------+----------+--------+-------+
		const response = await fetch('http://localhost:8081/data/ships');
		const {state, message, data} = await response.json();
		console.log(state, message, data);
		// 資料渲染
		renderShipTable(data);
	});
	
	$('bmiBtn').addEventListener("click", async(event) => {
		console.log('bmiBtn 被按下');
		const h = window.prompt('請輸入身高');
		const w = window.prompt('請輸入體重');
		const response = await fetch(`http://localhost:8081/data/bmi?h=${h}&w=${w}`);
		const {state, message, data} = await response.json();
		console.log(state, message, data);
		// 資料渲染
		if(state) {
			$('result').innerHTML = `身高: ${data.height}<br>
									 體重: ${data.weight}<br>
									 BMI: ${data.bmi}<br>
									 診斷: ${data.result}`;
		} else {
			$('result').innerHTML = message;
		}	
	});
	
});
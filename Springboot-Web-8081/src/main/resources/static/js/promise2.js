/**
 * 使用 async 與 await 處理 Promise
 */

// 模擬煮飯的 function (需要 5 秒鐘)
function cookRice() {
	console.log("開始煮飯");
	let promise = new Promise((resolve) => {
		setTimeout(() => {resolve("煮飯完畢");}, 5000);	
	});
	return promise;
}

// 模擬擺桌子的 function (立即可以完成)
function setTable() {
	console.log("桌椅碗筷準備完成");
}

// 準備晚餐(使用 async 與 await 處理 Promise)
async function prepareDinner() {
	console.log("開始準備晚餐");
	
	// 開始煮飯
	const ricePromise = cookRice();
	
	// 擺桌子
	setTable();
	
	// 開始吃飯
	const result = await ricePromise;
	console.log(result);
	console.log("飯食準備完成, 開動... 飯吃完了~");
	
}

prepareDinner();
 
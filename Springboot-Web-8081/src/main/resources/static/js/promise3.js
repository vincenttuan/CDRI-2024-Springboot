const $ = (id) => document.getElementById(id);



document.addEventListener("DOMContentLoaded", () => {
	
	// 同步
	$('queryButton').addEventListener("click", async(event) => {
		console.log('begin');
		const response = await fetch('http://localhost:8081/data/lotto');
		const {state, message, data} = await response.json();
		console.log(state, message, data);
	
		// 做其他事情(同步)
		console.log('dosomething~~');
	
		console.log('end');
	});
	
	// 非同步
	$('queryButton2').addEventListener("click", (event) => {
		console.log('begin');
		fetch('http://localhost:8081/data/lotto')
			.then(response => response.json())
			.then(({state, message, data}) => {
				console.log(state, message, data);
				console.log('end');
			});
		// 做其他事情(非同步)
		console.log('dosomething~~');
	});
		
});










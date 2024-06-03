document.addEventListener("DOMContentLoaded", () => {
	// 按二下可以切換小月曆
	document.querySelectorAll('.date-text').forEach((span) => {
		//console.log(span);
		span.addEventListener("dblclick", (event) => {
			//console.log(span);
			const bookingId = event.target.dataset.bookingId;
			const input = document.querySelector(`.date-input[data-booking-id='${bookingId}']`);
			//console.log(bookingId);
			span.style.display = 'none';
			input.style.display = 'inline';
		});
	});
	
	// 監聽日期是否有被改變/按右鍵可以還原文字呈現(隱藏小月曆)
	document.querySelectorAll('.date-input').forEach((input) => {
		// 監聽日期是否有被改變
		input.addEventListener('change', (event) => {
			const bookingId = event.target.dataset.bookingId;
			const bookingDate = event.target.value;
			console.log(`bookingId=${bookingId}, bookingDate=${bookingDate}`);
			// 透過 fetch 來變更資料庫的資訊(Homework)
			
		});
		
		// 按下右鍵
		input.addEventListener('contextmenu', (event) => {
			event.preventDefault();
			const bookingId = event.target.dataset.bookingId;
			const span = document.querySelector(`.date-text[data-booking-id='${bookingId}']`);
			span.style.display = 'inline';
			input.style.display = 'none';
		});
	});
	
});
const REMOTE_URL = 'http://localhost:8080';

const $ = (id) => document.getElementById(id);

// 通用函數，用於檢查事件目標是否包含指定的類別，如果是則執行回調函數
const handleEvent = async (event, className, callback) => {
    if (event.target.classList.contains(className)) {
		var itemId, productId, amount, customerId;
		switch(className) {
			case 'products-add': // 新增產品
				await callback();
				break;
			case 'delete-product-button': // 刪除客戶
				productId = event.target.getAttribute('data-id');
				await callback(productId);
				break;
			case 'update-product-button': // 修改產品
				productId = event.target.getAttribute('data-id');
				await callback(productId);
				break;
			
			case 'delete-item-product-button': // 刪除訂單商品
				itemId = event.target.getAttribute('data-id');
		        productId = event.target.getAttribute('data-product-id');
		        await callback(itemId, productId);
				break;
			case 'customers-add': // 新增客戶
				await callback();
				break;
			case 'change-customer-password-button': // 更新客戶密碼
			case 'update-customer-button': // 修改客戶
			case 'delete-customer-button': // 刪除客戶
				customerId = event.target.getAttribute('data-id');
				await callback(customerId);
				break;	
			case 'checkout-order-button': // 結帳 checkout
				orderId = event.target.getAttribute('data-id');
				await callback(orderId);
				break;	
			default:
				productId = event.target.getAttribute('data-id');
		        amount = event.target.getAttribute('data-amount');
		        await callback(productId, amount);
		}
    }
};

// 使用解構賦值和模板字符串來簡化代碼
const fetchAndRenderData = async (url, containerId, renderFn) => {
	url = REMOTE_URL + url;
    try {
        const response = await fetch(url);
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);

        const { status, message, data } = await response.json();
        console.log(status, message, data, containerId);
        if (!status) throw new Error(`Failed to fetch data: ${message}`);
		
		// data.map(renderFn) 會得到一個數組，其中每個元素是 renderFn 函數的返回值
		// .join('') 用於將數組轉換為字符串給 innerHTML 屬性當作參數使用 
		// Array.isArray(data) 用於判斷 data 是否是數組
		// 如果 data 是數組，則使用 data.map(renderFn).join('') 作為 innerHTML 的參數
		// 如果 data 不是數組，則直接使用 renderFn(data) 作為 innerHTML 的參數
        $(containerId).innerHTML = Array.isArray(data) ? data.map(renderFn).join('') : renderFn(data);
    } catch (error) {
        console.error(`Error fetching data from ${url}:`, error);
        $(containerId).innerHTML = `<tr><td colspan="6">沒有權限讀取</td></tr>`;
    }
};

// 通用刪除
// 使用 Swal.fire 跳出 yes/no 彈窗來決定是否要刪除
const handleDelete = async (fullUrl, fetchAndRender) => {
	const result = await Swal.fire({
		title: '確定要刪除嗎?',
		text: "刪除後將無法恢復!",
		icon: 'warning',
		showCancelButton: true,
		confirmButtonText: '是的，刪除它!',
		cancelButtonText: '取消'
	});
	if (result.isConfirmed) {
		console.log('Confirmed');
	} else {
		console.log('Cancelled');
		return;
	}

	// 進行遠端刪除
	// delete fullUrl
	try {
		const response = await fetch(fullUrl, { method: 'DELETE' });
		if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
		const { status, message } = await response.json();
		if (!status) throw new Error(`Failed to delete: ${message}`);
		// 重新渲染列表
		//fetchAndRenderData('/customers', 'customers-body', renderCustomer);
		fetchAndRender();
	} catch (error) {
		console.error('Delete error', fullUrl, error);
		// Swal.fire dialog
		Swal.fire({
			title: '刪除失敗!',
			text: error,
			icon: 'error',
			confirmButtonText: '確定'
		});
	}
};



//-----------------------------------------------------------------------------------------------------------

// 使用解構賦值簡化 logout 函數
const logout = () => window.location.href = '/logout';

// 定義一個異步函數來加載 HTML 內容
const loadHTML = async (url, containerId) => {
	const fullUrl = REMOTE_URL + url;
    try {
        const response = await fetch(fullUrl); // 等待 fetch 请求完成
        const data = await response.text(); // 等待响应的文本内容
        $(containerId).innerHTML = data; // 将加载的 HTML 内容设置到指定的容器中
    } catch (error) {
        console.error('Error loading the content:', error); // 处理可能的错误
    }
};

//-----------------------------------------------------------------------------------------------------------

const renderUsername = ({username}) => `${username}`;

// 渲染產品列表的函數
const renderProduct = ({ id, name, cost, price, qty }) => `
    <tr>
        <td>${id}</td>
        <td>${name}</td>
        <td>${cost}</td>
        <td>${price}</td>
        <td>${qty}</td>
        <td title='買進'><span class="button-add pure-button buy-product-button" data-id="${id}" data-amount="1">買</span></td>
        <td title='修改'><span class="button-update pure-button update-product-button" data-id="${id}">修改</span></td>
        <td title='刪除'><span class="button-delete pure-button delete-product-button" data-id="${id}">刪除</span></td>
    </tr>`;

// 渲染客戶列表的函數
const renderCustomer = ({ id, role, username, password }) => `
    <tr>
        <td>${id}</td>
        <td>${role}</td>
        <td>${username}</td>
        <td title='${password}'>${password.substring(0, 10)}..</td>
        <td title='變更密碼'><span class="button-change pure-button change-customer-password-button" data-id="${id}">變更</span></td>
        <td title='修改'><span class="button-update pure-button update-customer-button" data-id="${id}">修改</span></td>
        <td title='刪除'><span class="button-delete pure-button delete-customer-button" data-id="${id}">刪除</span></td>
    </tr>`;

// 渲染訂單列表的函數
const renderOrder = ({ id, date, customerDto, total, updatable, itemDtos }) => {
    // 在遍歷 itemDtos 時，將 updatable 傳給 renderOrderItem
    const orderItemsHtml = itemDtos.map(itemDto => 
        renderOrderItem({ ...itemDto, updatable }) // 使用擴展運算符將 itemDto 的所有屬性與 updatable 合併後傳入
    ).join('');
    
    // 根據 updatable 的值動態決定是否添加 hide 類別
    const adjustColumnClass = updatable ? '' : 'hide';
    
    const deleteOrderButton = updatable ? `
    		<span class="button-update pure-button checkout-order-button" data-id="${id}">結帳</span>
    		&nbsp;
    		<span class="button-delete pure-button delete-order-button" data-id="${id}">刪除訂單</span>` : '已結帳';
    // 将 orderItemsHtml 嵌入到最终的 HTML 结构中
    return `
        <tr>
            <td>${id}</td>
            <td>${date}</td>
            <td>${customerDto.username}</td>
            <td>${total}</td>
            <td class="${updatable ? 'updatable' : 'toggle-items-visibility'}">${deleteOrderButton}</td>

        </tr>
        <tr>
            <td colspan="5">
                <table class="pure-table custom-table">
                	<thead>
                	<tr>
                        <th align="center">Id</th>
                        <th align="center">商品名稱</th>
                        <th align="center">價格</th>
                        <th align="center">數量</th>
                        <th align="center" class="${adjustColumnClass}">數量調整</th>
                        <th align="center">小計</th>
                    </tr>
                    </thead>
                    <tbody id="orders-items-table">
                    ${orderItemsHtml}
                    </tbody>
                </table>
            </td>
        </tr>
    `;
};

const renderOrderItem = ({ id, orderId, productDto, amount, updatable }) => {
    const adjustButtonsHtml = updatable ? `
        <span class="button-add pure-button add-item-product-button" data-id="${productDto.id}" data-amount="1">+</span>
        <span class="button-reduce pure-button reduce-item-product-button" data-id="${productDto.id}" data-amount="-1">-</span>
        <span class="button-delete pure-button delete-item-product-button" data-id="${id}" data-product-id="${productDto.id}">X</span>
    ` : '';
	
	// 根據 updatable 的值動態決定是否添加 hide 類別
    const adjustColumnClass = updatable ? '' : 'hide';
    
    return `
        <tr>
            <td>${id}</td>
            <td>${productDto.name}</td>
            <td>${productDto.price}</td>
            <td>${amount}</td>
            <td class="${adjustColumnClass}">${adjustButtonsHtml}</td>
            <td>${amount * productDto.price}</td>
        </tr>
    `;
};
    
// -- products ------------------------------------------------------ 
// 新增商品的函數
const handleAddProduct = async () => {
	// 使用 Swal.fire
	const result = await Swal.fire({
		title: '新增產品',
		html: `
            <input id="product-name-input" class="swal2-input" placeholder="請輸入商品名稱">
            <input id="product-cost-input" type="number" class="swal2-input" placeholder="請輸入成本">
            <input id="product-price-input" type="number" class="swal2-input" placeholder="請輸入價格">
            <input id="product-qty-input" type="number" class="swal2-input" placeholder="請輸入數量">`,
		focusConfirm: false,
		preConfirm: () => {
			const name = $('product-name-input').value;
			const cost = $('product-cost-input').value;
			const price = $('product-price-input').value;
			const qty = $('product-qty-input').value;

			// 檢查輸入值，如果需要的話
			if (!name || !cost || !price || !qty) {
				Swal.showValidationMessage("所有欄位都是必填的");
				return false; // 阻止彈窗關閉
			}

			return { name, cost, price, qty };
		}
	});
	
	if (result.value) {
		console.log(result.value);
		console.log('Name:', result.value.name);
		console.log('Cost:', result.value.cost);
		console.log('Price:', result.value.price);
		console.log('Qty:', result.value.qty);
	} else {
		console.log('No values');
		return;
	}
	
	// post http://localhost:8080/products
	const fullUrl = `${REMOTE_URL}/products`;
	try {
		const response = await fetch(fullUrl, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({
				name: result.value.name,
				cost: result.value.cost,
				price: result.value.price,
				qty: result.value.qty,
			})
		});
		if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
		const { status, message } = await response.json();
		if (!status) throw new Error(`Failed to add product: ${message}`);
		// 重新渲染產品列表
		fetchAndRenderData('/products', 'products-body', renderProduct);
	} catch (error) {
		console.error('Error adding product:', error);
	}
};

const handleUpdateProduct = async (productId) => {
	// http://localhost:8080/products/1 取得 product
	const getProductUrl = `${REMOTE_URL}/products/${productId}`;
	const productResponse = await fetch(getProductUrl);
	const { status, message, data } = await productResponse.json();
	console.log(status, message, data);
	
	// Swal.fire Form-UI
	const result = await Swal.fire({
		title: '修改產品',
		html: `
            <input id="product-name-input" class="swal2-input" placeholder="請輸入商品名稱" value="${data.name}">
            <input id="product-cost-input" type="number" class="swal2-input" placeholder="請輸入成本" value="${data.cost}">
            <input id="product-price-input" type="number" class="swal2-input" placeholder="請輸入價格" value="${data.price}">
            <input id="product-qty-input" type="number" class="swal2-input" placeholder="請輸入數量" value="${data.qty}">`,
		focusConfirm: false,
		preConfirm: () => {
			const name = $('product-name-input').value;
			const cost = $('product-cost-input').value;
			const price = $('product-price-input').value;
			const qty = $('product-qty-input').value;

			// 檢查輸入值，如果需要的話
			if (!name || !cost || !price || !qty) {
				Swal.showValidationMessage("所有欄位都是必填的");
				return false; // 阻止彈窗關閉
			}

			return { name, cost, price, qty };
		}
	});
	
	if (result.value) {
		console.log(result.value);
		console.log('Name:', result.value.name);
		console.log('Cost:', result.value.cost);
		console.log('Price:', result.value.price);
		console.log('Qty:', result.value.qty);
	} else {
		console.log('No values');
		return;
	}
	
	// put http://localhost:8080/products/1
	const fullUrl = `${REMOTE_URL}/products/${productId}`;
	try {
		const response = await fetch(fullUrl, {
			method: 'PUT',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({
				name: result.value.name,
				cost: result.value.cost,
				price: result.value.price,
				qty: result.value.qty
			})
		});
		if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
		const { status, message } = await response.json();
		if (!status) throw new Error(`Failed to update product: ${message}`);
		// 重新渲染產品列表
		fetchAndRenderData('/products', 'products-body', renderProduct);
	} catch (error) {
		console.error('Error updating product:', error);
	}
	  
};

// 處理刪除產品的異步邏輯
const handleDeleteProduct = async (productId) => {
	// 處理刪除客戶的異步邏輯
	handleDelete(`${REMOTE_URL}/products/${productId}`, () => fetchAndRenderData('/products', 'products-body', renderProduct));  
};

// 處理購買產品的異步邏輯
const handleBuyProduct = async (productId, amount) => {
    const fullUrl = `${REMOTE_URL}/orders`;
    // 在這裡添加異步處理購買邏輯的代碼
    // http-method:post http://localhost:8080/orders
    // 帶入 item json
    try {
		const response = await fetch(fullUrl, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({
				productId: productId,
				amount: amount
			})
		});
		if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
		const { status, message, data } = await response.json();
		//console.log(status, message);
		//const productQty = data.itemDtos.find(item => item.productDto.id === productId)?.qty;
		if (!status) throw new Error(`Failed to buy product: ${message}`);
		// 重新渲染訂單列表
		fetchAndRenderOrderData();
		// 更新單一產品列表
		updateProductQty(productId);
		
	} catch (error) {
		console.error('Error buying product:', error);
	}
    
};

// -- customers -------------------------------------------------------------------------------- 
// 新增客戶的函數
const handleAddCustomer = async () => {
	
	// 使用 Swal.fire
	const result = await Swal.fire({
	    title: '新增客戶',
	    html:`
	        <select id="customer-role-select" class="swal2-input">
	        	<option value="USER">ROLE_USER</option>
	        	<option value="ADMIN">ROLE_ADMIN</option>
	        </select>
	        <input id="customer-username-input" class="swal2-input" placeholder="請輸入用戶名稱">
	        <input id="customer-password-input" type="password" class="swal2-input" placeholder="請輸入密碼">`,
	    focusConfirm: false,
	    preConfirm: () => {
	        const role = document.getElementById('customer-role-select').value;
	        const username = document.getElementById('customer-username-input').value;
	        const password = document.getElementById('customer-password-input').value;
	        
	        // 檢查輸入值，如果需要的話
	        if (!role || !username || !password) {
	            Swal.showValidationMessage("所有欄位都是必填的");
	            return false; // 阻止彈窗關閉
	        }
	
	        return { role, username, password };
	    }
	});
	
	if (result.value) {
		console.log(result.value);
	    console.log('Role:', result.value.role);
	    console.log('Username:', result.value.username);
	    console.log('Password:', result.value.password);
	} else {
	    console.log('No values');
	    return;
	}
	
	// post http://localhost:8080/customers
	const fullUrl = `${REMOTE_URL}/customers`;
	try {
		const response = await fetch(fullUrl, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({
				role: result.value.role,
				username: result.value.username,
				password: result.value.password
			})
		});
		if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
		const { status, message } = await response.json();
		if (!status) throw new Error(`Failed to add customer: ${message}`);
		// 重新渲染客戶列表
		fetchAndRenderData('/customers', 'customers-body', renderCustomer);
	} catch (error) {
		console.error('Error adding customer:', error);
	}
};

// 處理更新客戶密碼的異步邏輯
const handleUpdateCustomerPassword = async (customerId) => {
	const fullUrl = `${REMOTE_URL}/customers/${customerId}/update-password`;
	// 使用 Swal.fire 白底彈窗請求用戶輸入新密碼
	var { value: newpassword } = await Swal.fire({
		title: '請輸入新密碼',
		input: 'text',
		inputPlaceholder: '新密碼',
		inputAttributes: {
			maxlength: 20,
			autocapitalize: 'off',
			autocorrect: 'off'
		},
		showCancelButton: true,
		confirmButtonText: '確定',
		cancelButtonText: '取消',
		inputValidator: (value) => {
			if (!value) {
				return '未輸入密碼!'; // 返回錯誤提示
			}
		}
	});
	
	
	// 印出新密碼
	console.log('newpassword:', newpassword);
	if (newpassword == null) { // 按下取消
		return;
	}
	newpassword = newpassword.trim(); // 去掉空格
	if (newpassword == "") { // 按下確定但未輸入密碼
		alert("未輸入密碼!?");
		return;
	}
	// 在這裡添加異步處理更新客戶密碼的代碼
	// 帶入 password json
	try {
		const response = await fetch(fullUrl, {
			method: 'PATCH',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({
				password: newpassword
			})
		});
		if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
		const { status, message } = await response.json();
		console.log(status, message);
		if (!status) throw new Error(`Failed to update customer password: ${message}`);
		// 重新渲染客戶列表
		fetchAndRenderData('/customers', 'customers-body', renderCustomer);
	} catch (error) {
		console.error('Error updating customer password:', error);
	}
};

// 處理修改客戶的異步邏輯
const handleUpdateCustomer = async (customerId) => {
	const getCustomerUrl = `${REMOTE_URL}/customers/${customerId}`;
	const customerResponse = await fetch(getCustomerUrl);
	const { status, message, data } = await customerResponse.json();
	console.log(status, message, data);
	
	// 使用 Swal.fire 白底彈窗請求修改用戶
	const result = await Swal.fire({
		title: '修改客戶',
		html: `
            <select id="customer-role-select" class="swal2-input">
            	<option value="USER" ${data.role=="USER"?"selected":""}>ROLE_USER</option>
            	<option value="ADMIN" ${data.role=="ADMIN"?"selected":""}>ROLE_ADMIN</option>
            </select>
            <input id="customer-username-input" class="swal2-input" placeholder="請輸入用戶名稱" value="${data.username}">`,
		focusConfirm: false,
		preConfirm: () => {
			const role = document.getElementById('customer-role-select').value;
			const username = document.getElementById('customer-username-input').value;

			// 檢查輸入值，如果需要的話
			if (!role || !username) {
				Swal.showValidationMessage("所有欄位都是必填的");
				return false; // 阻止彈窗關閉
			}

			return { role, username};
		}
	});
	
	if (result.value) {
		console.log(result.value);
		console.log('Role:', result.value.role);
		console.log('Username:', result.value.username);
	} else {
		console.log('No values');
		return;
	}
	
	// 進行遠端修改
	// put http://localhost:8080/customers/1
	const fullUrl = `${REMOTE_URL}/customers/${customerId}`;
	try {
		const response = await fetch(fullUrl, {
			method: 'PUT',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({
				role: result.value.role,
				username: result.value.username
			})
		});
		if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
		const { status, message } = await response.json();
		if (!status) throw new Error(`Failed to add customer: ${message}`);
		// 重新渲染客戶列表
		fetchAndRenderData('/customers', 'customers-body', renderCustomer);
	} catch (error) {
		console.error('Error adding customer:', error);
	}
	
};

// 處理刪除客戶的異步邏輯
const handleDeleteCustomer = async (customerId) => {
	handleDelete(`${REMOTE_URL}/customers/${customerId}`, () => fetchAndRenderData('/customers', 'customers-body', renderCustomer));
};

// -- orders ------------------------------------------------------ 

const fetchAndRenderOrderData = () => {
	fetchAndRenderData('/orders/customer/checkouted', 'orders-body-checkouted', renderOrder);
	fetchAndRenderData('/orders/customer/uncheckout', 'orders-body-uncheckout', renderOrder);
};

const updateProductQty = async (productId) => {
    // http://localhost:8080/products/1 取得 qty
	const fullUrl = `${REMOTE_URL}/products/${productId}`;
	const response = await fetch(fullUrl);
	const { status, message, data } = await response.json();
	if (!status) throw new Error(`Failed to fetch product: ${message}`);
	const productQty = data.qty;
	
    // 將 products-list-table 第一欄(index=0) id 為 productId 第五欄(index=4) qty 更新為 productQty
    const productTable = document.getElementById('products-list-table');
    const rows = productTable.querySelectorAll('tr');

    rows.forEach(row => {
        const cells = row.querySelectorAll('td');
        if (cells.length > 0 && cells[0].textContent.trim() == productId) {
            cells[4].textContent = productQty.toString();
            return;
        }
    });
};

// 結帳
const handleCheckoutOrder = async (orderId) => {
	fullUrl =`${REMOTE_URL}/orders/checkout/${orderId}`;
	const response = await fetch(fullUrl, { method: 'GET' });
	if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
	const { status, message } = await response.json();
	if (!status) throw new Error(`Failed to delete: ${message}`);
	// 重新渲染列表
	fetchAndRenderOrderData(); 
	fetchAndRenderData('/products', 'products-body', renderProduct);
		
};

// 處理刪除訂單的異步邏輯
const handleDeleteOrder = async (orderId) => {
	
	handleDelete(`${REMOTE_URL}/orders/${orderId}`, () => {
		fetchAndRenderOrderData(); 
		fetchAndRenderData('/products', 'products-body', renderProduct);
	});
	
};

// 處理刪除訂單商品的異步邏輯
const handleDeleteOrderItemProduct = async(itemId, productId) => {
	const fullUrl = `${REMOTE_URL}/orders/item/${itemId}`;
	try {
		const response = await fetch(fullUrl, {
			method: 'DELETE'
		});
		if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
		const { status, message, data } = await response.json();
		console.log(status, message);
		//const productQty = data.itemDtos.find(item => item.productDto.id === productId)?.qty;
		if (!status) throw new Error(`Failed to buy product: ${message}`);
		// 重新渲染訂單列表
		fetchAndRenderOrderData();
		// 更新單一產品列表
		updateProductQty(productId);
		
	} catch (error) {
		console.error('Error buying product:', error);
	}
};

//-----------------------------------------------------------------------------------------------------
// 等待 DOM 加載完成後再執行
document.addEventListener("DOMContentLoaded", async () => {
    
	// 在文檔加載完成後調用函數
	// 加上 await 關鍵字等待 loadHTML 函數完成
    // 載入 products-list.html 到 products-container
    await loadHTML('/products-list.html', 'products-container');
    // 載入 customers-list.html 到 customers-container
    await loadHTML('/customers-list.html', 'customers-container');
    // 載入 orders-list.html 到 orders-container
    await loadHTML('/orders-list.html', 'orders-container');
    
    // 渲染資料
    fetchAndRenderData('/user/name', 'username', renderUsername);
    fetchAndRenderData('/products', 'products-body', renderProduct);
    fetchAndRenderData('/customers', 'customers-body', renderCustomer);
    fetchAndRenderOrderData();
    
    // 下面的方法會等待所有的 HTML 加載完成後再執行
    $("logout").addEventListener("click", logout);
    
    // products ------------------------------------------------------
    $("products-add-link").addEventListener("click", async (event) => {
		event.preventDefault();  // 取消默認動作，這裡是阻止超鏈接跳轉
		console.log(event.target);
		await handleEvent(event, 'products-add', handleAddProduct);
	});
	
	$('products-list-table').addEventListener('click', async (event) => {
	    // 使用通用函數處理事件
	    await handleEvent(event, 'buy-product-button', handleBuyProduct);
	    await handleEvent(event, 'update-product-button', handleUpdateProduct);
	    await handleEvent(event, 'delete-product-button', handleDeleteProduct);
	});
	
	// customers ------------------------------------------------------
	$("customers-add-link").addEventListener("click", async (event) => {
		event.preventDefault();  // 取消默認動作，這裡是阻止超鏈接跳轉
		console.log(event.target);
		await handleEvent(event, 'customers-add', handleAddCustomer);
	});
	
	$("customers-body").addEventListener("click", async (event) => {
		event.preventDefault();  // 取消默認動作，這裡是阻止超鏈接跳轉
		console.log(event.target);
		await handleEvent(event, 'change-customer-password-button', handleUpdateCustomerPassword);
		await handleEvent(event, 'update-customer-button', handleUpdateCustomer);
		await handleEvent(event, 'delete-customer-button', handleDeleteCustomer);
	});
	
	// orders ------------------------------------------------------
	$('orders-body-checkouted').addEventListener('click', async (event) => {
		event.preventDefault();  // 取消默認動作，這裡是阻止超鏈接跳轉
		console.log(event.target);
		// 判斷 class 是否是 toggle-items-visibility
		if (event.target.classList.contains('toggle-items-visibility')) {
			console.log('點擊了 toggle-items-visibility');
			// 將該 table 的 tbody id="orders-items-table" 隱藏或顯示
			// 获取当前点击元素所在的行的下一行中的 tbody 元素
		    const tbody = event.target.closest('tr').nextElementSibling.querySelector('#orders-items-table');
		    
		    // 切换 tbody 的显示状态
		    if (tbody.style.display === 'none') {
		        tbody.style.display = '';
		    } else {
		        tbody.style.display = 'none';
		    }
		}
	});
	
	$('orders-body-uncheckout').addEventListener('click', async (event) => {
		event.preventDefault();  // 取消默認動作，這裡是阻止超鏈接跳轉
		console.log(event.target);
		// 使用通用函數處理事件
		await handleEvent(event, 'add-item-product-button', handleBuyProduct);
		await handleEvent(event, 'reduce-item-product-button', handleBuyProduct);
		await handleEvent(event, 'delete-item-product-button', handleDeleteOrderItemProduct);
		await handleEvent(event, 'delete-order-button', handleDeleteOrder);
		await handleEvent(event, 'checkout-order-button', handleCheckoutOrder);
	});
	
	$("orders-body-checkouted").style.display = "none";
	
	$("orders-uncheckout-link").addEventListener("click", (event) => {
		event.preventDefault();  // 取消默認動作，這裡是阻止超鏈接跳轉
		$("orders-body-uncheckout").style.display = "";
		$("orders-body-checkouted").style.display = 'none';
	});
	$("orders-checketouted-link").addEventListener("click", (event) => {
		event.preventDefault();  // 取消默認動作，這裡是阻止超鏈接跳轉
		$("orders-body-uncheckout").style.display = 'none';
		$("orders-body-checkouted").style.display = "";
	});
		
});

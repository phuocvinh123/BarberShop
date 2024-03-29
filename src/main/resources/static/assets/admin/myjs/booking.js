const bookingForm = document.getElementById('bookingForm');
const tBody = document.getElementById("tBody");
const ePagination = document.getElementById('pagination')
const eSearch = document.getElementById('search');
const formBody = document.getElementById('formBody');
const modalDetail = $('#staticBackdrop')
let stylistSelected = {};
let pageable = {
    page: 1,
    sort: 'id,desc',
    search: ''
}
let stylists = [];

async function getList() {
    const response = await fetch(`/api/bookings?page=${pageable.page - 1 || 0}&search=${pageable.search || ''}`);

    if (!response.ok) {
        // Xử lý trường hợp không thành công ở đây, ví dụ: throw một lỗi hoặc trả về một giá trị mặc định
        throw new Error("Failed to fetch data");
    }

    const result = await response.json();
    pageable = {
        ...pageable,
        ...result
    };
    genderPagination();
    renderTBody(result.content);
    return result; // Trả về kết quả mà bạn đã lấy từ response.json()
    // addEventEditAndDelete();
}

function renderTBody(items) {
    let str = '';
    items.forEach(e => {
        str += renderItemStr(e);
    })
    tBody.innerHTML = str;
}

const genderPagination = () => {
    ePagination.innerHTML = '';
    let str = '';
    //generate preview truoc
    str += `<li class="page-item ${pageable.first ? 'disabled' : ''}">
              <a class="page-link" href="#" tabindex="-1" aria-disabled="true">Previous</a>
            </li>`
    //generate 1234

    for (let i = 1; i <= pageable.totalPages; i++) {
        str += ` <li class="page-item ${(pageable.page) === i ? 'active' : ''}" aria-current="page">
      <a class="page-link" href="#">${i}</a>
    </li>`
    }
    //
    //generate next truoc
    str += `<li class="page-item ${pageable.last ? 'disabled' : ''}">
              <a class="page-link" href="#" tabindex="-1" aria-disabled="true">Next</a>
            </li>`
    //generate 1234
    ePagination.innerHTML = str;

    const ePages = ePagination.querySelectorAll('li'); // lấy hết li mà con của ePagination
    const ePrevious = ePages[0];
    const eNext = ePages[ePages.length - 1]

    ePrevious.onclick = () => {
        if (pageable.page === 1) {
            return;
        }
        pageable.page -= 1;
        getList();
    }
    eNext.onclick = () => {
        if (pageable.page === pageable.totalPages) {
            return;
        }
        pageable.page += 1;
        getList();
    }
    for (let i = 1; i < ePages.length - 1; i++) {
        if (i === pageable.page) {
            continue;
        }
        ePages[i].onclick = () => {
            pageable.page = i;
            getList();
        }
    }
}
const onSearch = (e) => {
    e.preventDefault()
    pageable.search = eSearch.value;
    pageable.page = 1;
    getList();
}
const searchInput = document.querySelector('#search');
searchInput.addEventListener('search', () => {
    onSearch(event)
});
// const onLoadSort = () => {
//     eHeaderPrice.onclick = () => {
//         let sort = 'price,desc'
//         const chevronDown = document.querySelector('.bx-chevron-down');
//         const chevronUp = document.querySelector('.bx-chevron-up');
//         chevronDown.style.display = 'block';
//         chevronUp.style.display = 'none';
//         if (pageable.sortCustom?.includes('price') && pageable.sortCustom?.includes('desc')) {
//             sort = 'price,asc';
//             chevronUp.style.display = 'block';
//             chevronDown.style.display = 'none';
//         }
//         pageable.sortCustom = sort;
//         getList();
//     }
// }

function renderItemStr(item) {
    const statusOptions = ['PAID', 'UNPAID', 'CANCELLED']; // Danh sách các status

    const selectedStatus = item.status;

    const statusSelectOptions = statusOptions.map(status => {
        if (status === selectedStatus) {
            return `<option class="${status}" value="${status}" selected>${status}</option>`; // Thêm thuộc tính selected nếu giá trị trùng khớp
        } else {
            return `<option class="${status}" value="${status}">${status}</option>`;
        }
    }).join(''); // Tạo các option cho ô select

    const dateTime = new Date(item.dayTimeBooking);
    const formattedDateTime = `${dateTime.getFullYear()}-${(dateTime.getMonth() + 1).toString().padStart(2, '0')}-${dateTime.getDate().toString().padStart(2, '0')} ${dateTime.getHours().toString().padStart(2, '0')}:${dateTime.getMinutes().toString().padStart(2, '0')}`;

    return `<tr>
        <td>${item.id}</td>
        <td>${item.name}</td>
        <td>${formattedDateTime}</td>
        <td>${item.phoneNumber}</td>
        <td>${item.bookingDetails}</td>
        <td>${item.totalPrice}</td>
        <td>${item.stylist}</td>
        <td>
            <select class="status-select ${selectedStatus}" data-item-id="${item.id}">${statusSelectOptions}</select> <!-- Ô select option -->
        </td>       
        <td>
        <button class="btn-primary" onclick="showdetail(${item.id})">Detail</button>
</td>            
    </tr>`;
}

function renderDetail(item) {
    let str = '';
    const dateTime = new Date(item.dayTimeBooking);
    const formattedDateTime = `${dateTime.getFullYear()}-${(dateTime.getMonth() + 1).toString().padStart(2, '0')}-${dateTime.getDate().toString().padStart(2, '0')} ${dateTime.getHours().toString().padStart(2, '0')}:${dateTime.getMinutes().toString().padStart(2, '0')}`;

    str += `
            <div>
                  <p>Status:</p>
                  <span style="color: blue">${item.status}</span>
                </div>
                <div>
                  <p>Name:</p>
                  <span>${item.name}</span>
                </div>
                <div>
                  <p>Phone:</p>
                  <span>${item.phoneNumber}</span>
                </div>
                <div>
                  <p>Date:</p>
                  <span>${formattedDateTime}</span>
                </div>
                <div>
                  <p>Stylist:</p>
                  <span>${item.stylist}</span>
                </div>
                <div>
                  <p>Service:</p>
                </div>
                <div  style="background-color: #8d8d93; color: white">
                   <label>Name</label>
                    <label>Price</th>
                </div>
                
                
               
    `

    const bookingdetail = item.bookingDetails;
    const elements = bookingdetail.split(",");

    elements.forEach(item => {
        const parts = item.split("-");
        const before = parts[0].trim();
        const after = parts[1].trim();
        str += `
            <div>
                   <label>${before}</label>
                    <label>${after}</label>
                </div>
        `
    })

    str += `
         <div style="background-color: #f1789d; color: white">
                  <p>Total Price:</p>
                  <span>${item.totalPrice}</span>
                </div>    
    `

    return str;
}

async function showdetail(id) {
    const res = await fetch('/api/bookings/detail/' + id);
    const result = await res.json();
    const str = renderDetail(result[0])
    formBody.innerHTML = str;
    modalDetail.modal('show')
}

document.addEventListener('change', async function (event) {
        const target = event.target;
        if (target.classList.contains('status-select')) {
            const itemId = target.getAttribute('data-item-id');
            const newStatus = target.value;

            target.classList.remove(...target.classList);
            target.classList.add('status-select')
            target.classList.add(newStatus);

            const res = await fetch('/api/bookings/' + itemId + '/' + newStatus, {
                method: 'PATCH',
            });

            if (res.ok) {
                webToast.Success({
                    status: 'Cập nhật trạng thái thành công',
                    message: '',
                    delay: 2000,
                    align: 'topright'
                });
            }
        }
});


window.onload = async () => {
    await renderTable()
    // onLoadSort();
    // renderForm(formBody, getDataInput());
}


async function renderTable() {
    const pageable = await getList();
    rooms = pageable.content;
    renderTBody(rooms);
}

const findById = async (id) => {
    const response = await fetch('/api/books/' + id);
    return await response.json();

}

function messagePrint() {
        webToast.Success({
            status: 'In hóa đơn thành công',
            message: '',
            delay: 2000,
            align: 'topright'
        });

}







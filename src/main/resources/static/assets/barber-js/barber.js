const divTimeButton = $('#div-time-button')
const divTimeButtonRow = $('#div-time-button .row')
let timeBooking;


const timeArr = [
    "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00"
]

const logoutButton = document.querySelector("#logout-button");
$(document).ready(function (){
    const avatar = document.querySelector("#user-avatar");
    const dropdownMenu = document.querySelector("#dropdown-menu");
    avatar.addEventListener("click", function () {
        if (dropdownMenu.style.display == "block") {
            dropdownMenu.style.display = "none";
        } else {
            dropdownMenu.style.display = "block";
        }
    });
})
// Xử lý khi người dùng bấm vào biểu tượng avatar



// Đóng dropdown menu khi người dùng bấm ra ngoài
window.addEventListener("click", function (event) {
    const dropdownMenu = document.querySelector("#dropdown-menu");
    if (!event.target.matches("#user-avatar")) {
        dropdownMenu.style.display = "none";
    }
});



// Lấy thẻ input theo id
const dateInput = document.getElementById('dayBooking');

// Lấy ngày hiện tại và chuyển định dạng thành yyyy-MM-dd (giống với định dạng của input date)
const currentDate = new Date();
const currentDateString = currentDate.toISOString().split('T')[0];

// Gán giá trị min cho input là ngày hiện tại
dateInput.min = currentDateString;

// Thêm sự kiện 'input' để kiểm tra ngày khi người dùng thay đổi giá trị
dateInput.addEventListener('input', function() {
    const selectedDate = new Date(dateInput.value);
    // Cắt bỏ phần giờ, phút, giây và mili giây để so sánh chỉ phần ngày
    const selectedDateString = selectedDate.toISOString().split('T')[0];
    const currentDateString = currentDate.toISOString().split('T')[0];
    console.log(selectedDateString);
    console.log(currentDateString)
    if (selectedDateString < currentDateString) {
        // Nếu ngày chọn nhỏ hơn ngày hiện tại, đặt giá trị của input thành ngày hiện tại
        dateInput.value = currentDateString;
        alert('Không thể chọn ngày trước ngày hiện tại.');
    }
});


function addService() {
    // Lấy giá trị đã chọn từ ô select
    const selectedService = document.getElementById("serviceBooker");
    const serviceText = selectedService.options[selectedService.selectedIndex].text;
    const serviceValue = selectedService.options[selectedService.selectedIndex].value;

    // Tạo một thẻ div mới để hiển thị dịch vụ đã chọn
    const serviceElement = document.createElement("div");
    serviceElement.classList.add("selected-service");

    // Tạo một thẻ span để chứa biểu tượng "X" và đặt thuộc tính onclick
    const deleteIcon = document.createElement("span");
    deleteIcon.classList.add("delete-icon");
    deleteIcon.innerHTML = '<i class="fa fa-times" onclick="deleteService(this); updateTotalPrice();"></i>';

    // Tạo một thẻ span để chứa nội dung dịch vụ
    const serviceTextSpan = document.createElement("span");
    serviceTextSpan.textContent = serviceText;

    serviceTextSpan.setAttribute("valueData", serviceValue);
    serviceTextSpan.setAttribute("name", "idHairDetails");


    // Thêm biểu tượng và nội dung dịch vụ vào thẻ div
    serviceElement.appendChild(serviceTextSpan);
    serviceElement.appendChild(deleteIcon);

    // Thêm thẻ div đã chọn vào một phần khác của trang (ví dụ: một div có id="selectedServices")
    const selectedServicesDiv = document.getElementById("selectedServices");
    selectedServicesDiv.appendChild(serviceElement);
}


function updateTotalPrice() {
    // Lấy tất cả các phần tử dịch vụ đã chọn
    const selectedServices = document.getElementById("selectedServices").children;

    // Tính tổng tiền từ các dịch vụ đã chọn
    let totalPrice = 0;
    for (let i = 0; i < selectedServices.length; i++) {
        let serviceText = selectedServices[i].textContent;
        let price = parseFloat(serviceText.split(":")[1]);
        totalPrice += price;
    }
    if (selectedServices.length < 1) {
        document.getElementById("totalPriceValue").textContent = "0 Đ";
    }
    // Cập nhật giá trị tổng tiền vào #totalPriceValue
    document.getElementById("totalPriceValue").textContent = totalPrice.toFixed(0) + " Đ";

}

function formatInputDate() {
    const dateInput = document.getElementById("dayBooking");
    const selectedDate = new Date(dateInput.value);

    const year = selectedDate.getFullYear();
    let month = (selectedDate.getMonth() + 1).toString().padStart(2, "0");
    let day = selectedDate.getDate().toString().padStart(2, "0");

    const formattedDate = `${year}-${month}-${day}`;
    dateInput.value = formattedDate;
}

async function showTimeFree(element) {
    const id = element.value;
    const dateInput = document.getElementById("dayBooking");
    const date = dateInput.value;

    const res = await fetch('/api/bookings/' + id + '/' + date);
    console.log(res);
    const responseJson = await res.json();
    console.log(responseJson)
    checkDisable(responseJson);

}


async function showTimeFreeDate(element) {
    const date = element.value;
    const idE = document.getElementById("stylistBooker");
    const id = idE.value;

    const res = await fetch('/api/bookings/' + id + '/' + date);
    console.log(res);
    const responseJson = await res.json();
    console.log(responseJson)
    checkDisable(responseJson);
}

function checkDisable(responseJson){

    // btnTimeBooking.css('background-color', '#3498DB');
    // btnTimeBooking.prop('disabled', false);

    divTimeButtonRow.empty();

    $.each(timeArr, (index, item) => {
        const str = renderAllTimeButtons(item)
        divTimeButtonRow.append(str)
    })

    const btnTime = $('#div-time-button .btn-time')

    $.each(responseJson, (index, item) => {
        console.log(item)
        const indexCurrent = findTimeInArray(item.toString())

        if (indexCurrent >= 0 ) {
            const str = renderRedButton(item)
            const elem = $(btnTime[indexCurrent])

            elem.replaceWith(str)
        }
    })

    changeColorButtonToYellow()
}


function deleteService(element) {
    // Lấy thẻ div chứa biểu tượng "X"
    const serviceDiv = element.parentElement;

    // Lấy phần tử cha của thẻ div để xóa thẻ div khỏi nó
    const selectedServicesDiv = serviceDiv.parentElement;

    // Xóa thẻ div chứa dịch vụ đã chọn
    selectedServicesDiv.remove(serviceDiv);
}

const bookingForm = document.getElementById('form-booking');

const eSelectedHairDetails = document.getElementsByName('idHairDetails');

const tBody = document.getElementById("tBody");
const eSelectedStylist = document.getElementsByName('selectedStylist');
const name = document.getElementById('nameBooker')
const phone = document.getElementById('phoneBooker')
// const dayBooking = document.getElementById()
// const timeBooking  = document.getElementById()

const ePagination = document.getElementById('pagination')
const eSearch = document.getElementById('search');
const eModalTitle = document.getElementById("staticBackdropLabel");
const submitBtn = document.getElementById("submit-btn");
// const eOptionsType = eSelectType.querySelectorAll("option");
const formBody = document.getElementById('formBody');
const eHeaderPrice = document.getElementById('header-price')


let bookingSelected = {};
let roomDetail;
let pageable = {
    page: 1,
    sort: 'id,desc',
    search: ''
}
let categories;
let types;
let rooms = [];

const idUser = document.getElementById("idUser").value;
bookingForm.onsubmit = async (e) => {
    const selectedOptions = [];
    const dayBooking = $('#dayBooking').val();

    for (let i = 0; i < eSelectedHairDetails.length; i++) {
        const value = eSelectedHairDetails[i].getAttribute("valueData");
        selectedOptions.push(value);
    }

    e.preventDefault();

    if(idUser == 0) {
        await webToast.Danger({
            status: 'Vui lòng đăng nhập để đặt lịch',
            message: '',
            delay: 2000,
            align: 'topright'
        });
        return;
    }

    if(selectedOptions.length === 0){
        await webToast.Danger({
            status: 'Vui lòng chọn dịch vụ',
            message: '',
            delay: 2000,
            align: 'topright'
        });
        return;
    }

    if(!dayBooking) {
        await webToast.Danger({
            status: 'Vui lòng chọn ngày',
            message: '',
            delay: 2000,
            align: 'topright'
        });
        return;
    }

    if(timeBooking == null) {
        await webToast.Danger({
            status: 'Vui lòng chọn giờ',
            message: '',
            delay: 2000,
            align: 'topright'
        });
        return;
    }
    let data = getDataFromForm(bookingForm);
    console.log(data)

    data = {
        ...data,
        stylist: {
            id: data.stylist
        },
        idHairDetails: selectedOptions,
        timeBooking: timeBooking + ":00",
        idUser: idUser
    }

    console.log(data)


            const res = await createBooking(data)
            if(res.ok){
                await webToast.Success({
                    status: 'Đặt lịch thành công',
                    message: '',
                    delay: 2000,
                    align: 'topright'
                });

                setTimeout(() => {
                    location.href = '/history/'+idUser
                }, 2000)
            }else{
                await webToast.Danger({
                    status: 'Lỗi, vui lòng kiểm tra lại thông tin',
                    message: '',
                    delay: 2000,
                    align: 'topright'
                });
            }


}

function getDataFromForm(form) {
    // event.preventDefault()
    const data = new FormData(form);
    return Object.fromEntries(data.entries())
}

async function createBooking(data) {
    console.log(data)
    const res = await fetch('/api/bookings', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    return res;
}

function getCurrentDateTime(timeStr) {
    const today = new Date().toLocaleDateString('EN', 'yyyy/MM/dd')
    return new Date(today + " " + timeStr);
}

function findTimeInArray(timeStr) {
    return timeArr.findIndex(item => item === timeStr)
}

function renderAllTimeButtons(timeStr) {
    // const timeValue = $(this).text().split(":")[0];
    // const hourValue = parseInt(timeStr, 10);

    const now = getCurrentDateTime(timeStr)
    const daySelected = document.getElementById('dayBooking')
    const dayNow = getDayNow();


    if (now < new Date().getTime() && daySelected.value === dayNow ) {
        return `
        <div class="col-2 btn-time" >
          <button class="red-button" type="button" disabled>${timeStr}</button>
        </div>
    `
    }

    return `
        <div class="col-2 btn-time">
          <button class="green-button" type="button">${timeStr}</button>
        </div>
    `
}

function getDayNow(){
    // Tạo một đối tượng Date mới
    const currentDate = new Date();

// Lấy thông tin về năm, tháng và ngày
    const year = currentDate.getFullYear();
    const month = (currentDate.getMonth() + 1).toString().padStart(2, '0'); // Thêm 0 ở trước nếu tháng < 10
    const day = currentDate.getDate().toString().padStart(2, '0'); // Thêm 0 ở trước nếu ngày < 10

// Tạo chuỗi theo định dạng "YYYY-MM-DD"
    const formattedDate = year + '-' + month + '-' + day;

    return formattedDate;
}

function renderRedButton(timeStr) {
    return `
        <div class="col-2 btn-time" >
          <button class="red-button" type="button" disabled>${timeStr}</button>
        </div>
    `
}


function changeColorButtonToYellow() {

    $('.btn-time button').on('click', function () {
        const className = $(this).attr('class')
        if (className.includes('yellow-button')) {
            $(this).removeClass('yellow-button').addClass('green-button')
        }
        else {
            if (className.includes('green-button')) {
                $('.yellow-button').removeClass('yellow-button').addClass('green-button')
                $(this).removeClass('green-button').addClass('yellow-button')
                timeBooking = $(this).text();
            }
        }
    })

}




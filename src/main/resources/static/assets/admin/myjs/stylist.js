const stylistForm = document.getElementById('stylistForm');
const tBody = document.getElementById("tBody");
const ePagination = document.getElementById('pagination')
const eSearch = document.getElementById('search');
const formBody = document.getElementById('formBody');

const input = document.getElementById("file");
let idImages = [];
let check = true;

let stylistSelected = {};
let pageable = {
    page: 1,
    sort: 'id,desc',
    search: ''
}
let stylists = [];

stylistForm.onsubmit = async (e) => {
    e.preventDefault();
    let data = getDataFromForm(stylistForm);
    console.log(data)
    data = {
        ...data,
        id: stylistSelected.id,
        files: idImages.map(e => {
            return {
                id: e
            }
        })
    }
    console.log(data)

    // let message = "Created"
    if (stylistSelected.id) {
        await editStylist(data);
        webToast.Success({
            status: 'Sửa thành công',
            message: '',
            delay: 2000,
            align: 'topright'
        });
        // message = "Edited"
    } else {
        await createStylist(data)
        webToast.Success({
            status: 'Thêm thành công',
            message: '',
            delay: 2000,
            align: 'topright'
        });
    }

    // alert(message);
    await renderTable();
    $('#staticBackdrop').modal('hide');

}

function getDataFromForm(form) {
    // event.preventDefault()
    const data = new FormData(form);
    return Object.fromEntries(data.entries())
}

async function getList() {
    const response = await fetch(`/api/stylists?page=${pageable.page - 1 || 0}&search=${pageable.search || ''}`);

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
    const eNext = ePages[ePages.length-1]

    ePrevious.onclick = () => {
        if(pageable.page === 1){
            return;
        }
        pageable.page -= 1;
        getList();
    }
    eNext.onclick = () => {
        if(pageable.page === pageable.totalPages){
            return;
        }
        pageable.page += 1;
        getList();
    }
    for (let i = 1; i < ePages.length - 1; i++) {
        if(i === pageable.page){
            continue;
        }
        ePages[i].onclick = () => {
            pageable.page = i;
            getList();
        }
    }
}


function renderItemStr(item) {
    const imagesHTML = item.images.map(imageUrl => `<img src="${imageUrl}" alt="" />`).join('');

    const statusOptions = ['FREE', 'BUSY']; // Danh sách các status

    const statusSelectOptions = statusOptions.map(status => {
        if (status === item.status) {
            return `<option class="${status}" value="${status}" selected>${status}</option>`; // Thêm thuộc tính selected nếu giá trị trùng khớp
        } else {
            return `<option class="${status}" value="${status}">${status}</option>`;
        }
    }).join(''); // Tạo các option cho ô select

    return `<tr>
                    <td>
                        ${item.id}
                    </td>
                    <td >
                        ${item.name}
                    </td>
                    <td class="image-container" style="width: 50px; height: 50px">
                       
                        ${imagesHTML}

                    </td>
                    <td>
                        ${item.gender}
                    </td>
                    <td>
                        <select class="status-select ${item.status}" data-item-id="${item.id}">${statusSelectOptions}</select>
                    </td>
                    
                    
                  
                    <td>
                        <div class="d-flex">
                        <button class="dropdown-item" onclick="showEdit(${item.id})"
                        data-bs-toggle="modal" data-bs-target="#staticBackdrop"
                        ><i class="bx bx-edit-alt me-1"></i> Edit</button
                            >
                        <button class="dropdown-item" onclick="deleteStylist(${item.id})"
                        ><i class="bx bx-trash me-1"></i> Delete</button
                        >
                        </div>
              </div>
            </div>
                    </td>
                </tr>`
}

document.addEventListener('change', async function(event) {
    const target = event.target;
    if (target.classList.contains('status-select')) {
        const itemId = target.getAttribute('data-item-id');
        const newStatus = target.value;

        target.classList.remove(...target.classList);
        target.classList.add('status-select')
        target.classList.add(newStatus);

        const res = await fetch('/api/stylists/' + itemId + '/' + newStatus, {
            method: 'PATCH',
        });
    }
});

window.onload = async () => {
    await renderTable()

    renderForm(formBody, getDataInput());
    // onLoadSort();
}
function getDataInput() {
    return [
        {
            label: 'Name',
            name: 'name',
            value: stylistSelected.name,
            required: true,
            pattern: "^[A-Za-zÀ-Ỷà-ỷẠ-Ỵạ-ỵĂăÂâĐđÊêÔôƠơƯưỨứỪừỰựỬửỮữỨứỬửỰựỦủỤụỠỡỞởỢợỞởỚớỔổỒồỐốỎỏỊịỈỉỌọỈỉỊịỆệỄễỀềẾếỂểỈỉỄễỆệỂểỀề0-9 ,.()]{2,20}",
            message: "Username must have minimum is 2 characters and maximum is 20 characters",
        },
        {
            label: 'Gender',
            name: 'gender',
            value: stylistSelected.gender,
            type: 'select',
            required: true,
            options: [{value: "FEMALE", name: "FEMALE"}, {value: "MALE", name:"MALE"}],
            message: 'Please choose Gender'
        },
        {
            label: 'Status',
            name: 'status',
            value: stylistSelected.status,
            type: 'select',
            required: true,
            options: [{value: "FREE", name: "FREE"}, {value: "BUSY", name:"BUSY"}],
            message: 'Please choose Status'
        },

    ];
}
async function renderTable() {
    const pageable = await getList();
    stylists = pageable.content;
    renderTBody(stylists);
}



// function clearForm() {
//     stylistForm.reset();
//     stylistSelected = {};
// }
function clearForm() {
    idImages = [];

    const imgEle = document.getElementById("images");
    const imageOld = imgEle.querySelectorAll('img, button');
    Array.from(imageOld).forEach((img) => {
        img.remove();
    });
    const avatarDefault = document.createElement('img');
    avatarDefault.src = '../assets/img/img.png';
    avatarDefault.classList.add('avatar-preview');
    avatarDefault.style = "height: 100px; width: 100px";
    imgEle.append(avatarDefault)
    stylistForm.reset();
    stylistSelected = {};
}

function showCreate() {
    $('#staticBackdropLabel').text('Create Stylist');
    clearForm();
    renderForm(formBody, getDataInput())
}

async function createStylist(data) {
    console.log(data)
    const res = await fetch('/api/stylists', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
}

async function showEdit(id) {
    $('#staticBackdropLabel').text('Edit Stylist');
    clearForm();
    stylistSelected = await findById(id);
    showImgInForm(stylistSelected.images);
    renderForm(formBody, getDataInput());
}

function showImgInForm(images) {
    const imgEle = document.getElementById("images");
    const imageOld = imgEle.querySelectorAll('img');
    for (let i = 0; i < imageOld.length; i++) {
        imgEle.removeChild(imageOld[i])
    }
    const avatarDefault = document.createElement('img');
    avatarDefault.src = '/assets/img/img.png';
    avatarDefault.classList.add('avatar-preview');
    imgEle.append(avatarDefault);

    images.forEach((img, index) => {
        let imageContainer = document.createElement('div');
        imageContainer.classList.add("div-avatar");

        let image = document.createElement('img');
        image.src = img;
        image.classList.add('avatar-preview');
        imageContainer.append(image);

        let deleteButton = document.createElement('button');
        // deleteButton.innerText = 'X';
        deleteButton.classList.add('delete-button');
        deleteButton.classList.add('btn-close');
        deleteButton.addEventListener('click', () => {

            input.disabled = true;
            removeImage(index); // Gọi hàm xóa ảnh khi click vào dấu X
            imageContainer.remove(); // Xóa container chứa ảnh và nút X
        });
        imageContainer.append(deleteButton);

        imgEle.append(imageContainer);
    })
}

async function removeImage(index) {
    try {
        const imgToDelete =stylistSelected.images[index];
        console.log(imgToDelete);
        const formData = new FormData;
        formData.append("url", imgToDelete);

        const response = await fetch("api/stylistImages", {
            method: 'DELETE',
            body: formData,
        });

        if (response.ok) {
            // Xóa ảnh khỏi mảng images
            stylistSelected.images.splice(index, 1);
            input.disabled = false;
            console.log('Image deleted successfully');
        } else {
            input.disabled = false;
            console.error('Error deleting image:', response.status);
        }
    } catch (error) {
        console.error('Error deleting image:', error);
    }
}

const findById = async (id) => {
    const response = await fetch('/api/stylists/' + id);
    return await response.json();
}

async function editStylist(data) {
    console.log(data);
    const res = await fetch('/api/stylists/' + data.id, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
}

async function deleteStylist(id) {
    var confirmBox = webToast.confirm("Bạn chắc chắn muốn xóa chứ??");
    confirmBox.click(async function () {

        const res = await fetch('/api/stylists/' + id, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(id)
        });

        if (res.ok) {
            // alert("Deleted");
            webToast.Success({
                status: 'Xóa thành công',
                message: '',
                delay: 2000,
                align: 'topright'
            });

            await getList();
        } else {
            webToast.Danger({
                status: 'Xóa lỗi rồi',
                message: '',
                delay: 2000,
                align: 'topright'
            });
        }
    });
}

const searchInput = document.querySelector('#search');
searchInput.addEventListener('search', () => {
    onSearch(event)
});

const onSearch = (e) => {
    e.preventDefault()
    pageable.search = eSearch.value;
    pageable.page = 1;
    getList();
}


async function previewImage(evt) {
    if(evt.target.files.length === 0){
        return;
    }

    if (check) {
        idImages = [];
    }

    // When the image is loaded, update the img element's src
    const files = evt.target.files
    for (let i = 0; i < files.length; i++) {
        webToast.Success(
            {
                status: `Uploading image ${i+1} / ${files.length} . . .`,
                message:'',
                delay: 1000,
                align: 'topright'
            }
        );

        const file = files[i];
        await previewImageFile(file, i);


        if (file) {
            disableSaveChangesButton()
            // Create a new FormData object and append the selected file
            const formData = new FormData();
            formData.append("avatar", file);
            formData.append("fileType", "image");

            try {
                // Make a POST request to upload the image
                const response = await fetch("/api/stylistImages", {
                    method: "POST",
                    body: formData,
                });

                if (response.ok) {
                    const result = await response.json();
                    check = false;

                    if (result && result.id) {
                        const id = result.id;
                        console.log(id)
                        const imgEle = document.getElementById("images");
                        const imageContainer = imgEle.lastChild;
                        const img = imageContainer.querySelector("img");
                        // img.dataset.id = id;
                        idImages.push(id);

                        const deleteButton = imageContainer.querySelector(".delete-button");
                        deleteButton.addEventListener("click", () => {
                            input.disabled = true;
                            deleteImage(id);
                            imageContainer.remove();
                            check = false;
                        });

                    } else {
                        console.error('Image ID not found in the response.');
                    }
                } else {
                    // Handle non-OK response (e.g., show an error message)
                    console.error('Failed to upload image:', response.statusText);
                }
            } catch (error) {
                // Handle network or other errors
                console.error('An error occurred:', error);
            }
        }
        enableSaveChangesButton()
    }
}

async function deleteImage(id) {
    try {
        const response = await fetch(`api/hairDetailImages/${id}`, {
            method: "DELETE"
        });
        if (response.ok) {
            input.disabled = false;
            console.log("Image deleted from the database.");
        } else {
            console.error("Failed to delete image from the database:", response.statusText);
        }
    } catch (error) {
        console.error("An error occurred:", error);
    }

}

async function previewImageFile(file) {
    const reader = new FileReader();
    reader.onload = function () {
        const imgEle = document.getElementById("images");
        const imageContainer = document.createElement('div');
        const img = document.createElement('img');

        imageContainer.classList.add("div-avatar");

        img.src =reader.result;
        img.classList.add('avatar-preview');
        imageContainer.append(img);

        let deleteButton = document.createElement('button');

        deleteButton.classList.add('delete-button');
        deleteButton.classList.add('btn-close');

        imageContainer.append(deleteButton);

        imgEle.append(imageContainer);
    };
    reader.readAsDataURL(file);
}

// 2 hàm để tự động Disable nút SaveChange khi tải ảnh lên
function disableSaveChangesButton() {
    const saveChangesButton = document.getElementById('saveChangesButton');
    saveChangesButton.disabled = true;
}
function enableSaveChangesButton() {
    const saveChangesButton = document.getElementById('saveChangesButton');
    saveChangesButton.disabled = false;
}






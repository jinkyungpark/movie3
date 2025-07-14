const fileInput = document.querySelector("#fileInput");

function checkExtension(fileName, fileSize) {
  //파일 업로드가 가능한 파일인지 확인하는 코드 작성

  //파일 업로드 가능 확장자
  var regex = new RegExp("(.*?).(png|gif|jpg)$");

  //var regex = new RegExp("(.*?)\.(sh|zip|alz)$");
  var maxSize = 10485760; //10MB

  if (fileSize >= maxSize) {
    alert("파일 사이즈 초과");
    return false;
  }
  if (!regex.test(fileName)) {
    alert("해당 종류의 파일은 업로드 할 수 없습니다.");
    return false; // 지정해 둔 확장자가 아니면 false
  }
  return true; // 두 경우에 해당 안됨
}

function showUploadImages(arr) {
  console.log("showUploadImages ", arr);

  const output = document.querySelector(".uploadResult ul");

  let tags = "";
  arr.forEach((obj, idx) => {
    tags += `<li data-name="${obj.fileName}" data-path="${obj.folderPath}" data-uuid="${obj.uuid}">`;
    tags += `<div><a href="">`;
    tags += `<img src="/upload/display?fileName=${obj.thumbnailURL}" class="block"></a>`;
    tags += `<span class="text-sm d-inline-block mx-1">${obj.fileName}</span>`;
    tags += `<a href="#" data-file="${obj.imageURL}"><i class="fa-solid fa-xmark"></i></a>`;
    tags += `</div>`;
    tags += `</li>`;
  });
  output.insertAdjacentHTML("beforeend", tags);
}

fileInput.addEventListener("change", (e) => {
  // formData 객체 구성해서 폼에 첨부된 파일 업로드
  let formData = new FormData();

  const inputFile = e.target;
  const files = inputFile.files;
  //   console.log("files", files);

  for (let index = 0; index < files.length; index++) {
    if (!checkExtension(files[index].name, files[index].size)) return false;

    // console.log(files[index]);
    formData.append("uploadFiles", files[index]);
  }

  //   console.log(formData); //찍어봐야 안나옴
  for (var value of formData.values()) {
    console.log(value);
  }

  fetch("/upload/uploadAjax", {
    method: "post",
    //headers를 설정하지 않거나, headers:{} 비어 있는 상태로 보내기
    headers: {
      "X-CSRF-TOKEN": csrfValue,
    },
    body: formData,
  })
    .then((response) => {
      if (!response.ok) throw new Error("에러");
      return response.json();
    })
    .then((result) => {
      console.log(result);

      // 도착한 데이터를 화면에 보여주기
      showUploadImages(result);
    })
    .catch((err) => console.log(err));
});

// 등록 버튼을 누르면
// 사용자가 작성한 폼 + 첨부된 파일 정보 전송
document.querySelector("#register-form").addEventListener("submit", (e) => {
  // submit 중지
  e.preventDefault();

  const form = e.target;

  // 첨부 파일 정보 수집
  const attachInfos = document.querySelectorAll(".uploadResult ul li");
  console.log("attachInfos", attachInfos);

  let result = "";
  attachInfos.forEach((obj, idx) => {
    console.log("첨부 파일 정보" + obj);

    result += `<input type="hidden" name="movieImages[${idx}].uuid" value="${obj.dataset.uuid}">`;
    result += `<input type="hidden" name="movieImages[${idx}].path" value="${obj.dataset.path}">`;
    result += `<input type="hidden" name="movieImages[${idx}].imgName" value="${obj.dataset.name}">`;
  });

  console.log(result);

  // 수집된 정보를 폼에 추가
  form.insertAdjacentHTML("beforeend", result);
  // console.log(form.innerHTML);
  form.submit();
});

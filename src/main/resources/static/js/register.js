// X 를 누르면 파일 삭제- register 시(나중에 modify 랑 겹치는 부분이어서)
// register.js 에 작성(동작이 다름)
document.querySelector(".uploadResult").addEventListener("click", (e) => {
  e.preventDefault();

  // console.log(e.target); // i 태그 가져옴
  const removeDiv = e.target.closest("li");
  console.log(removeDiv);

  //a 속성 data-file 가져오기
  const fileName = e.target.closest("a").dataset.file;
  console.log(fileName);

  const formData = new FormData();
  formData.append("fileName", fileName);

  fetch("/upload/removeFile", {
    method: "post",
    //headers를 설정하지 않거나, headers:{} 비어 있는 상태로 보내기
    headers: {
      "X-CSRF-TOKEN": csrfValue,
    },
    body: formData,
  })
    .then((response) => {
      if (!response.ok) throw new Error("에러");
      // 서버 쪽에서 true 넘어옴
      return response.text();
    })
    .then((result) => {
      console.log(result);

      if (result) {
        //해당 태그 제거
        removeDiv.remove();
      }
    })
    .catch((err) => console.log(err));
});

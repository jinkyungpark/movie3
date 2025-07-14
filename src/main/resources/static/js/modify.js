let actionForm = document.querySelector("form");

document.querySelector(".btn-danger").addEventListener("click", () => {
  if (!confirm("정말로 삭제하시겠습니까?")) return;

  actionForm.action = "/movie/remove";
  actionForm.method = "post";
  actionForm.submit();
});

document.querySelector(".btn-success").addEventListener("click", () => {
  if (!confirm("수정하시겠습니까?")) {
    return;
  }

  actionForm.action = "/movie/modify";
  actionForm.method = "post";
  actionForm.submit();
});

// x 버튼 누르면 이미지 제거 - register 작업과 다름
document.querySelector(".uploadResult").addEventListener("click", (e) => {
  e.preventDefault();

  console.log(e.target); // i 태그 가져옴
  const removeDiv = e.target.closest("li");
  console.log(removeDiv);

  if (confirm("정말로 삭제하시겠습니까?")) {
    //해당 태그 제거
    removeDiv.remove();
  }
});

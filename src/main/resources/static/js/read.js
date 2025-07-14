// 페이지가 열리면 해당 영화에 대한 리뷰 가져오기
const formatTime = (str) => {
  const date = new Date(str);

  return date.getFullYear() + "/" + (date.getMonth() + 1) + "/" + date.getDate() + " " + date.getHours() + ":" + date.getMinutes();
};

const reviewLoaded = () => {
  fetch(`/reviews/${mno}/all`)
    .then((response) => {
      if (!response.ok) throw new Error("에러 발생");
      return response.json();
    })
    .then((data) => {
      console.log(data);

      // data 길이가 댓글의 개수이니까 ~~ 개의 댓글 부분 수정
      document.querySelector(".review-cnt").innerHTML = data.length;
      if (data.length > 0) reviewList.classList.remove("hidden");

      // 가져온 데이터 만들어서 댓글 영역에 보여주기

      let result = "";
      data.forEach((review) => {
        result += `<div class="d-flex justify-content-between border-bottom py-2 review-row" data-rno="${review.reviewNo}">`;
        result += `<div class="flex-grow-1 align-self-center">`;
        result += `<div><span class="font-semibold">${review.text}</span></div>`;
        result += `<div class="small text-muted"><span class="d-inline-block mr-3">${review.nickname}</span>`;
        result += `평점 : <span class="grade">${review.grade}</span><div class="starrr"></div>`;
        result += `</div>`;
        result += `<div class="text-muted"><span class="small">${formatTime(review.createdDate)}</span></div></div>`;
        result += `<div class="d-flex flex-column align-self-center">`;
        if (user == `${review.email}`) {
          result += `<div class="mb-2"><button class="btn btn-outline-danger btn-sm">삭제</button></div>`;
          result += `<div><button class="btn btn-outline-success btn-sm">수정</button></div>`;
        }
        result += `</div></div>`;
      });
      reviewList.innerHTML = result;
    })
    .catch((err) => console.log(err));
};

// function rating(result, grade) {
//   result.querySelector("a:nth-child(" + grade + ")").click();
// }

// 리뷰 가져오기 호출
reviewLoaded();

const reviewForm = document.querySelector(".review-form");
// review-edit 클릭시 감춰뒀던 리뷰 작성 폼 보이기(수정 버튼에서 걸림)
// document.querySelector(".review-edit").addEventListener("click", (e) => {
//   reviewForm.classList.toggle("show");
// });

// 리뷰 등록
// 시큐리티 후 변경
if (reviewForm) {
  reviewForm.querySelector("button").addEventListener("click", () => {
    // 폼에 있는 내용 끌어올리기
    // 폼 안 내용 가져와서 보내기
    const mid = reviewForm.querySelector("#mid");
    const text = reviewForm.querySelector("#text");
    const reviewNo = reviewForm.querySelector("#reviewNo").value;
    // 컨트롤러에서 작성자 비교하기 위해서
    const email = reviewForm.querySelector("#email");

    const review = {
      mno: mno, // 앞에서 잡아놓은 무비 번호
      mid: mid.value, // 작성자
      email: email.value,
      text: text.value,
      grade: grade || 0, // 앞에서 잡아놓은 변수
      reviewNo: reviewNo,
    };

    if (!reviewNo) {
      // 새 댓글 등록
      fetch(`/reviews/${mno}`, {
        headers: {
          "content-type": "application/json",
          "X-CSRF-TOKEN": csrfValue,
        },
        body: JSON.stringify(review),
        method: "post",
      })
        .then((response) => {
          if (!response.ok) throw new Error("에러 발생");
          return response.json();
        })
        .then((data) => {
          console.log(data);

          // 리뷰 폼 clear

          // grade 가 3개라면 3개를 다시 클릭해서 초기화 시켜야 함
          reviewForm.querySelector(".starrr a:nth-child(" + grade + ")").click();

          //self.location.reload();
        })
        .catch((err) => console.log(err));
    } else {
      // 수정
      fetch(`/reviews/${mno}/${reviewNo}`, {
        headers: {
          "content-type": "application/json",
          "X-CSRF-TOKEN": csrfValue,
        },
        body: JSON.stringify(review),
        method: "put",
      })
        .then((response) => {
          if (!response.ok) throw new Error("에러 발생");
          return response.text();
        })
        .then((data) => {
          console.log(data);

          // 작성 내용 제거
          mid.value = "";
          text.value = "";
          replyNo = "";

          let updateRno = data;
          alert(updateRno + " 번 댓글이 수정되었습니다.");
          // replyLoaded();

          self.location.reload();
        })
        .catch((err) => console.log(err));
    }
  });
}

// 리뷰 삭제
reviewList.addEventListener("click", (e) => {
  //어느 버튼의 이벤트인가?
  //console.log(e.target);

  const btn = e.target;
  //reviewNo 가져오기
  const reviewNo = btn.closest(".review-row").dataset.rno;
  console.log("replyNo ", reviewNo);
  // 컨트롤러에서 작성자 비교하기 위해서
  const email = reviewForm.querySelector("#email");
  //console.log("email", email.value);

  let method = "delete";
  if (btn.classList.contains("btn-outline-danger")) {
    if (!confirm("정말로 삭제하시겠습니까?")) return;

    const form = new FormData();
    form.append("email", email.value);

    fetch(`/reviews/${mno}/${reviewNo}`, {
      method: method,
      headers: {
        "X-CSRF-TOKEN": csrfValue,
      },
      body: form,
    })
      .then((response) => {
        if (!response.ok) throw new Error("에러 발생");
        return response.text();
      })
      .then((data) => {
        console.log(data);

        alert("댓글이 삭제되었습니다.");
        self.location.reload();
      })
      .catch((err) => console.log(err));
  } else if (btn.classList.contains("btn-outline-success")) {
    // 수정할 댓글 가져와서 화면에 보여주기

    method = "get";

    fetch(`/reviews/${mno}/${reviewNo}`, {
      method: method,
    })
      .then((response) => {
        if (!response.ok) throw new Error("에러 발생");
        return response.json();
      })
      .then((data) => {
        console.log(data);

        reviewForm.querySelector("#reviewNo").value = data.reviewNo;
        reviewForm.querySelector("#mid").value = data.mid;
        reviewForm.querySelector("#text").value = data.text;

        reviewForm.querySelector(".starrr a:nth-child(" + data.grade + ")").click();

        reviewForm.querySelector("button").innerHTML = "리뷰 수정";
      })
      .catch((err) => console.log(err));
  }
});

// 첨부 파일 목록에서 이미지 클릭하면 큰 포스터 사진 보여주기
const imgModal = document.getElementById("imgModal");

if (imgModal) {
  imgModal.addEventListener("show.bs.modal", (e) => {
    // 모달을 뜨게 만든 li 가져오기
    const posterLi = e.relatedTarget;
    // Li data- 에 담긴 값 가져오기
    const file = posterLi.getAttribute("data-file");
    console.log("file ", file);

    imgModal.querySelector(".modal-title").textContent = `${title}`;
    const modalBody = imgModal.querySelector(".modal-body");
    modalBody.innerHTML = `<img style="width:100%" src="/upload/display?fileName=${file}&size=1">`;
  });
}

// 검색 창에서 엔터를 치면 이벤트 발생
document.querySelector("[name='keyword']").addEventListener("keyup", (e) => {
  if (e.keyCode == 13) {
    //alert("엔터에요");

    //검색어 가져오기
    const keyword = e.target.value;
    console.log("keyword", keyword);
    //검색 폼 가져오기
    const searchForm = document.querySelector("#searchForm");
    //검색 폼 안 keyword에 검색어 추가하기
    searchForm.querySelector("[name='keyword']").value = keyword;

    //console.log(searchForm);

    searchForm.submit();
  }
});

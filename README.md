# modak_11.2
# 이름 생성 오류 수정 완료
기존의 binding.createnameEdittext.text.toString().equals("") 구문에서 equals 함수 대신 isEmpty() 함수를 사용하여 아예 비어있는 것을 지목하였다. 저렇게 쌍따움표사이에 아무것도 없더라도 무슨 이유에서인지 오류가 나타난다.

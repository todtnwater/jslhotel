
// 텍스트 입력 여부 확인
function checkValue(obj, msg){
	
	let result = false;
	let val = obj.value;
	
	if(val == ""){
		alert(msg);
		obj.focus();
		result = true;
	}
	
	return result;
}

// 텍스트 길이 확인
function checkLength(obj, min, max, msg){
	
	let result = false;
	let len = obj.value.length;
	let a_msg = "[" + msg + "] " + min + "자 이상 " + max + "자 이내로 작성해 주세요.";
	
	if(len < min || len > max){
		result = true;
		alert(a_msg);
	} 
	
	return result;
}
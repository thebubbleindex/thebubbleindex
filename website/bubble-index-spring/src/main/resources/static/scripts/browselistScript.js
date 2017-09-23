function OnSubmitForm() {
		var w = document.forms['myform'].elements['symbol'].selectedIndex;
		var selected_text = document.forms['myform'].elements['symbol'].options[w].text;
		var more_selected_text = document.forms['myform'].elements['symbol'].options[w].value;
		var oldAction = "@{Application.plot(Type)}";
		var newAction = oldAction.concat("&name=" + selected_text + "&symbol=" + more_selected_text);
		document.forms['myform'].action = newAction;
		return true;
	}